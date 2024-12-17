package com.tvip.canvasser.menu_persiapan;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.tvip.canvasser.Perangkat.HttpsTrustManager;
import com.tvip.canvasser.R;
import com.tvip.canvasser.pojo.data_list_pelanggan;
import com.tvip.canvasser.pojo.data_pelanggan_pojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.view.View.GONE;
import static com.tvip.canvasser.menu_persiapan.callplan.szDocId;
import static com.tvip.canvasser.menu_persiapan.callplan.idStd;

public class daftar_kunjungan extends AppCompatActivity implements OnMapReadyCallback {
    SharedPreferences sharedPreferences;
    ListView listdaftarkunjungan;
    List<data_list_pelanggan> data_list_pelanggans = new ArrayList<>();
    List<data_pelanggan_pojo> dataPelangganPojos_2 = new ArrayList<>();

    ListViewAdapterDaftarKunjungan adapter;

    TabLayout tablayout;
    private GoogleMap mMap;
    RelativeLayout linearmap;
    TextView outlet, nogeotag;
    BottomSheetBehavior sheetBehavior;
    BottomSheetDialog sheetDialog;
    View bottom_sheet;
    SearchView caripelanggan;

    SweetAlertDialog pDialog;
    MaterialToolbar pengaturanBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HttpsTrustManager.allowAllSSL();
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        setContentView(R.layout.activity_daftar_kunjungan);
        listdaftarkunjungan = findViewById(R.id.listdaftarkunjungan);
        bottom_sheet = findViewById(R.id.bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(bottom_sheet);
        pengaturanBar = findViewById(R.id.persiapanbar);


        caripelanggan = findViewById(R.id.caripelanggan);

        tablayout = findViewById(R.id.tablayout);
        listdaftarkunjungan = findViewById(R.id.listdaftarkunjungan);
        linearmap = findViewById(R.id.linearmap);
        outlet = findViewById(R.id.outlet);
        nogeotag = findViewById(R.id.nogeotag);


        //Toast.makeText(daftar_kunjungan.this, "ID STD = " + idStd, Toast.LENGTH_SHORT).show();

        listdaftarkunjungan.setAdapter(adapter);

        tablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab){
                int position = tab.getPosition();
                if(position == 0){
                    listdaftarkunjungan.setVisibility(View.VISIBLE);
                    caripelanggan.setVisibility(View.VISIBLE);
                    linearmap.setVisibility(GONE);
                } else {
                    listdaftarkunjungan.setVisibility(GONE);
                    caripelanggan.setVisibility(View.GONE);
                    linearmap.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                if(position == 0){
                    listdaftarkunjungan.setVisibility(View.VISIBLE);
                    caripelanggan.setVisibility(View.VISIBLE);
                    linearmap.setVisibility(GONE);
                } else {
                    listdaftarkunjungan.setVisibility(GONE);
                    caripelanggan.setVisibility(GONE);
                    linearmap.setVisibility(View.VISIBLE);
                }
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.listmap);
        mapFragment.getMapAsync(this);

        String routetype =  getIntent().getStringExtra("routetype");

        if (routetype.equals("CAN")) {
            pengaturanBar.setTitle("Daftar Kunjungan");
            listpelanggan_canvaser();
        } else if (routetype.equals("DEL")) {
            pengaturanBar.setTitle("Daftar Kunjungan");
            listpelanggan_delivery();
        } else {
            Toast.makeText(daftar_kunjungan.this, "Non Rute", Toast.LENGTH_SHORT).show();
        }



        listdaftarkunjungan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                String langitude = ((data_list_pelanggan) parent.getItemAtPosition(position)).getSzLangitude();
                String longitude = ((data_list_pelanggan) parent.getItemAtPosition(position)).getSzLongitude();
                String namatoko = ((data_list_pelanggan) parent.getItemAtPosition(position)).getSzName();
                String alamat = ((data_list_pelanggan) parent.getItemAtPosition(position)).getSzAddress();
                String szId = ((data_list_pelanggan) parent.getItemAtPosition(position)).getSzId();
                String nosot = ((data_list_pelanggan) parent.getItemAtPosition(position)).getNosot();
                String idStd = ((data_list_pelanggan) parent.getItemAtPosition(position)).getIdstd();
                String idcustomer = ((data_list_pelanggan) parent.getItemAtPosition(position)).getIdcustomer();

                if(langitude.length() == 0){
//                    new SweetAlertDialog(daftar_kunjungan.this, SweetAlertDialog.WARNING_TYPE)
//                            .setTitleText("Toko Ini Tidak Ada GeoTag")
//                            .setConfirmText("OK")
//                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                                @Override
//                                public void onClick(SweetAlertDialog sDialog) {
//                                    sDialog.dismissWithAnimation();
//                                }
//                            })
//                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                                @Override
//                                public void onClick(SweetAlertDialog sDialog) {
//                                    sDialog.cancel();
//                                }
//                            })
//                            .show();

                    Intent i = new Intent(getBaseContext(), map_kunjungan.class);
                    i.putExtra("langitude", langitude);
                    i.putExtra("longitude", longitude);
                    i.putExtra("namatoko", namatoko);
                    i.putExtra("szId", szId);
                    i.putExtra("address", alamat);
                    i.putExtra("nosot", nosot);
                    i.putExtra("id_std", idStd);
                    i.putExtra("id_customer", idcustomer);
                    i.putExtra("route_type", routetype);


                    startActivity(i);

                } else {
                    Intent i = new Intent(getBaseContext(), map_kunjungan.class);
                    i.putExtra("langitude", langitude);
                    i.putExtra("longitude", longitude);
                    i.putExtra("namatoko", namatoko);
                    i.putExtra("szId", szId);
                    i.putExtra("address", alamat);
                    i.putExtra("nosot", nosot);
                    i.putExtra("id_std", idStd);
                    i.putExtra("id_customer", idcustomer);
                    i.putExtra("route_type", routetype);


                    startActivity(i);
                }

            }
        });

    }

    //MENAMPILKAN LIST PELANGGAN BERDASARKAN SURAT TUGAS sZDocId, DELIVERY
    private void listpelanggan_delivery() {

        pDialog = new SweetAlertDialog(daftar_kunjungan.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Harap Menunggu");
        pDialog.show();
        pDialog.setCancelable(false);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Persiapan/index_List_Toko?IdStd=" + idStd, //szDocId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        pDialog.dismissWithAnimation();

                        try {
                            int number = 0;
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");
                            for (int i = 0; i < movieArray.length(); i++) {
                                final JSONObject movieObject = movieArray.getJSONObject(i);
                                final data_list_pelanggan movieItem = new data_list_pelanggan(
                                        movieObject.getString("szId"),
                                        movieObject.getString("nm_toko_customer"),
                                        movieObject.getString("szAddress"),
                                        movieObject.getString("szLatitude"),
                                        movieObject.getString("szLongitude"),
                                        movieObject.getString("nosot"),
                                        movieObject.getString("id_std"),
                                        movieObject.getString("id_customer"));

                                tablayout.getTabAt(0).setText(movieArray.length() + " Pelanggan");
                                outlet.setText(String.valueOf(movieArray.length()));

                                if (movieObject.getString("szLatitude").equals(""))
                                    number++;  {
                                    nogeotag.setText(String.valueOf(number));
                                }
                                data_list_pelanggans.add(movieItem);

                            }
                            adapter = new ListViewAdapterDaftarKunjungan(data_list_pelanggans, getApplicationContext());
                            listdaftarkunjungan.setAdapter(adapter);
                            adapter.notifyDataSetChanged();

                            caripelanggan.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                                @Override
                                public boolean onQueryTextSubmit(String text) {
                                    return false;
                                }

                                @Override
                                public boolean onQueryTextChange(String newText) {
                                    adapter.getFilter().filter(newText);
                                    return true;
                                }
                            });

//                            pDialog.dismissWithAnimation();

                        } catch(JSONException e){
                            e.printStackTrace();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismissWithAnimation();
                    }
                })

        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                String creds = String.format("%s:%s", "admin", "Databa53");
                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                params.put("Authorization", auth);
                return params;
            }
        };

        stringRequest.setRetryPolicy(
                new DefaultRetryPolicy(
                        500000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                ));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    //MENAMPILKAN LIST PELANGGAN BERDASARKAN SURAT TUGAS sZDocId, DELIVERY
    private void listpelanggan_canvaser() {

        pDialog = new SweetAlertDialog(daftar_kunjungan.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Harap Menunggu");
        pDialog.show();
        pDialog.setCancelable(false);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Persiapan/index_List_Toko_Canvaser?IdStd=" + idStd, //szDocId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        pDialog.dismissWithAnimation();

                        try {
                            int number = 0;
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");
                            for (int i = 0; i < movieArray.length(); i++) {
                                final JSONObject movieObject = movieArray.getJSONObject(i);
                                final data_list_pelanggan movieItem = new data_list_pelanggan(
                                        movieObject.getString("szId"),
                                        movieObject.getString("szName"),
                                        movieObject.getString("szAddress"),
                                        movieObject.getString("szLatitude"),
                                        movieObject.getString("szLongitude"),
                                        movieObject.getString("szDocSO"),
                                        movieObject.getString("szDocId"),
                                        movieObject.getString("szCustomerId"));

                                tablayout.getTabAt(0).setText(movieArray.length() + " Pelanggan");
                                outlet.setText(String.valueOf(movieArray.length()));

                                if (movieObject.getString("szLatitude").equals(""))
                                    number++;  {
                                    nogeotag.setText(String.valueOf(number));
                                }
                                data_list_pelanggans.add(movieItem);

                            }
                            adapter = new ListViewAdapterDaftarKunjungan(data_list_pelanggans, getApplicationContext());
                            listdaftarkunjungan.setAdapter(adapter);
                            adapter.notifyDataSetChanged();

                            caripelanggan.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                                @Override
                                public boolean onQueryTextSubmit(String text) {
                                    return false;
                                }

                                @Override
                                public boolean onQueryTextChange(String newText) {
                                    adapter.getFilter().filter(newText);
                                    return true;
                                }
                            });

//                            pDialog.dismissWithAnimation();

                        } catch(JSONException e){
                            e.printStackTrace();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismissWithAnimation();
                    }
                })

        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                String creds = String.format("%s:%s", "admin", "Databa53");
                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                params.put("Authorization", auth);
                return params;
            }
        };

        stringRequest.setRetryPolicy(
                new DefaultRetryPolicy(
                        500000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                ));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public static class ListViewAdapterDaftarKunjungan extends ArrayAdapter<data_list_pelanggan> {
        private List<data_list_pelanggan> data_list_pelanggans;

        private Context context;

        public ListViewAdapterDaftarKunjungan(List<data_list_pelanggan> data_list_pelanggans, Context context) {
            super(context, R.layout.list_daftarkunjungan, data_list_pelanggans);
            this.data_list_pelanggans = data_list_pelanggans;
            this.context = context;
        }

        @SuppressLint("NewApi")
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = LayoutInflater.from(context);

            View listViewItem = inflater.inflate(R.layout.list_daftarkunjungan, null, true);

            TextView namatoko = listViewItem.findViewById(R.id.namatoko);
            TextView alamat = listViewItem.findViewById(R.id.alamat);

            data_list_pelanggan data = getItem(position);

            namatoko.setText(data.getSzName());
            alamat.setText(data.getSzAddress());

            return listViewItem;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Persiapan/index_List_Pelanggan?surat_tugas=" + szDocId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            int number = 0;
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");
                            for (int i = 0; i < movieArray.length(); i++) {
                                final JSONObject movieObject = movieArray.getJSONObject(i);
                                final data_pelanggan_pojo movieItem = new data_pelanggan_pojo(
                                        movieObject.getString("szId"),
                                        movieObject.getString("szName"),
                                        movieObject.getString("szAddress"),
                                        movieObject.getString("szLatitude"),
                                        movieObject.getString("szLongitude"));

                                dataPelangganPojos_2.add(movieItem);

                                if(!dataPelangganPojos_2.get(i).getSzLangitude().isEmpty()){
                                    Double langitude = Double.valueOf(dataPelangganPojos_2.get(i).getSzLangitude());
                                    Double longitude = Double.valueOf(dataPelangganPojos_2.get(i).getSzLongitude());

                                    LatLng zoom = new LatLng(langitude, longitude);

                                    mMap.addMarker(new MarkerOptions().position(new LatLng(langitude,longitude)).title(dataPelangganPojos_2.get(i).getSzId() + " (" + dataPelangganPojos_2.get(i).getSzName() + ")"));
                                    mMap.moveCamera(CameraUpdateFactory.newLatLng(zoom));
                                    mMap.animateCamera(CameraUpdateFactory.zoomTo(13));

                                    int finalI = i;
                                    mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                                        @Override
                                        public void onInfoWindowClick(Marker marker) {
                                            String szId = marker.getTitle();
                                            String[] parts = szId.split(" ");
                                            String szIdSlice = parts[0];
                                            showBottomSheetDialog(szIdSlice);
                                        }
                                    });
                                }


                            }

                        } catch(JSONException e){
                            e.printStackTrace();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        pDialog.dismissWithAnimation();
                    }
                })

        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                String creds = String.format("%s:%s", "admin", "Databa53");
                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                params.put("Authorization", auth);
                return params;
            }
        };

        stringRequest.setRetryPolicy(
                new DefaultRetryPolicy(
                        500000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                ));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }

    private void showBottomSheetDialog(String szIdSlice) {
        View view = getLayoutInflater().inflate(R.layout.keterangan_pelanggan, null);

        TextView namatoko = view.findViewById(R.id.namatoko);
        TextView kode = view.findViewById(R.id.kode);
        TextView alamat = view.findViewById(R.id.alamat);
        TextView term_payment = view.findViewById(R.id.term_payment);
        TextView limit_kredit = view.findViewById(R.id.limit_kredit);
        TextView piutang = view.findViewById(R.id.piutang);
        TextView sisa_limit_kredit = view.findViewById(R.id.sisa_limit_kredit);
        TextView channel = view.findViewById(R.id.channel);
        TextView status = view.findViewById(R.id.status);
        Button direction = view.findViewById(R.id.direction);

        kode.setText(szIdSlice);

        StringRequest channel_status = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Persiapan/index_Detail_Pelanggan?szId=" + szIdSlice,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");

                            JSONObject biodatas = null;
                            for (int i = 0; i < movieArray.length(); i++) {

                                biodatas = movieArray.getJSONObject(i);
                                namatoko.setText(biodatas.getString("szName"));
                                alamat.setText(biodatas.getString("szAddress"));

                                term_payment.setText(biodatas.getString("szPaymetTermId"));
                                limit_kredit.setText(biodatas.getString("decCreditLimit"));

                                channel.setText(biodatas.getString("Nama_Channel"));
                                status.setText(biodatas.getString("szStatus"));

                                Double langitude = Double.valueOf(biodatas.getString("szLatitude"));
                                Double longitude = Double.valueOf(biodatas.getString("szLongitude"));

                                direction.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent i = new Intent(Intent.ACTION_VIEW);
                                        i.setData(Uri.parse("https://maps.google.com?q="+langitude+","+longitude));
                                        startActivity(i);
                                    }
                                });



                            }

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                String creds = String.format("%s:%s", "admin", "Databa53");
                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                params.put("Authorization", auth);
                return params;
            }
        };

        channel_status.setRetryPolicy(
                new DefaultRetryPolicy(
                        500000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                ));
        RequestQueue channel_statusQueue = Volley.newRequestQueue(this);
        channel_statusQueue.add(channel_status);


        if (sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }

        sheetDialog = new BottomSheetDialog(this);
        sheetDialog.setContentView(view);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            sheetDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        sheetDialog.show();
        sheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                sheetDialog = null;
            }
        });
    }
}