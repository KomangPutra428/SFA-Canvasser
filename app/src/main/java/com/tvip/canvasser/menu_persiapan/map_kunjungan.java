package com.tvip.canvasser.menu_persiapan;

import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
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
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.tvip.canvasser.Perangkat.HttpsTrustManager;
import com.tvip.canvasser.R;
import com.tvip.canvasser.pojo.data_detail_produk;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class map_kunjungan extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    SharedPreferences sharedPreferences;
    BottomSheetBehavior sheetBehavior;
    View bottom_sheet;
    BottomSheetDialog sheetDialog;
    TextView nama_toko, alamat_toko;
    LottieAnimationView lottie_loading_detail_produk;
    LinearLayout linear_geotag;

    ListView listdetailproduk;
    List<data_detail_produk> dataDetailProduk_pojos = new ArrayList<>();
    ListViewAdapterDetailProduk adapterDetailProduk;
    RelativeLayout relative_rl_detail_produk;

    String nosot, idstd, idcustomer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HttpsTrustManager.allowAllSSL();
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        setContentView(R.layout.activity_map_kunjungan);
        bottom_sheet = findViewById(R.id.bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(bottom_sheet);
        lottie_loading_detail_produk = findViewById(R.id.lottie_loading_detail_produk);
        linear_geotag = findViewById(R.id.linear_geotag);

        nama_toko = findViewById(R.id.nama_toko);
        alamat_toko = findViewById(R.id.alamat_toko);

        listdetailproduk = findViewById(R.id.list_detail_produk);
        listdetailproduk.setAdapter(adapterDetailProduk);
        relative_rl_detail_produk = findViewById(R.id.rl_detail_produk);

        nosot = getIntent().getStringExtra("nosot");
        idstd = getIntent().getStringExtra("id_std");
        idcustomer = getIntent().getStringExtra("id_customer");

        nama_toko.setText(getIntent().getStringExtra("namatoko"));
        alamat_toko.setText(getIntent().getStringExtra("address"));

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (getIntent().getStringExtra("route_type").equals("CAN")){

            relative_rl_detail_produk.setVisibility(View.GONE);

        } else if (getIntent().getStringExtra("route_type").equals("DEL")) {

            relative_rl_detail_produk.setVisibility(View.GONE);

            list_detail_produk();

        } else {

            Toast.makeText(map_kunjungan.this, "Terjadi Kesalahan, Route Type gagal dimuat", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (getIntent().getStringExtra("langitude").equals("")){
            linear_geotag.setVisibility(View.VISIBLE);
            Toast.makeText(getApplicationContext(), "Tidak ada GeoTag", Toast.LENGTH_SHORT).show();

        } else {

            Double langitude = Double.valueOf(getIntent().getStringExtra("langitude"));
            Double longitude = Double.valueOf(getIntent().getStringExtra("longitude"));

            // Add a marker in Sydney and move the camera
            LatLng zoom = new LatLng(langitude, longitude);

            mMap.addMarker(new MarkerOptions().position(zoom).title(getIntent().getStringExtra("namatoko")));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(zoom));

            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    showBottomSheetDialog();
                }
            });

            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(zoom, 20));

        }

    }

    private void showBottomSheetDialog() {
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

        namatoko.setText(getIntent().getStringExtra("namatoko"));
        kode.setText(getIntent().getStringExtra("szId"));
        alamat.setText(getIntent().getStringExtra("address"));

        StringRequest channel_status = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Persiapan/index_Detail_Pelanggan?szId=" + getIntent().getStringExtra("szId"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");

                            JSONObject biodatas = null;
                            for (int i = 0; i < movieArray.length(); i++) {

                                biodatas = movieArray.getJSONObject(i);

                                term_payment.setText(biodatas.getString("szPaymetTermId"));
                                limit_kredit.setText(biodatas.getString("decCreditLimit"));

                                channel.setText(biodatas.getString("Nama_Channel"));
                                status.setText(biodatas.getString("szStatus"));


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
                }) {
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


        direction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Double langitude = Double.valueOf(getIntent().getStringExtra("langitude"));
                Double longitude = Double.valueOf(getIntent().getStringExtra("longitude"));

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://maps.google.com?q=" + langitude + "," + longitude));
                startActivity(i);
            }
        });

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

    private String formatRupiah(Double number) {
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        formatRupiah.setMaximumFractionDigits(0);
        return formatRupiah.format(number);
    }

    //MENAMPILKAN LIST DETAIL PRODUK
    private void list_detail_produk() {
        lottie_loading_detail_produk.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_Driver_Detail_Produk?id_customer="+idcustomer+"&id_std="+idstd, //szDocId,

                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        lottie_loading_detail_produk.setVisibility(View.GONE);
                        try {
                            int number = 0;
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");
                            for (int i = 0; i < movieArray.length(); i++) {
                                final JSONObject movieObject = movieArray.getJSONObject(i);
                                final data_detail_produk movieItem = new data_detail_produk(
                                        movieObject.getString("nosot"),
                                        movieObject.getString("id_std"),
                                        movieObject.getString("id_produk"),
                                        movieObject.getString("qty"),
                                        movieObject.getString("nama"),
                                        movieObject.getString("satuan"));

                                dataDetailProduk_pojos.add(movieItem);

                            }

                            adapterDetailProduk = new ListViewAdapterDetailProduk(dataDetailProduk_pojos, getApplicationContext());
                            listdetailproduk.setAdapter(adapterDetailProduk);
                            adapterDetailProduk.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        lottie_loading_detail_produk.setVisibility(View.GONE);
                        Toast.makeText(map_kunjungan.this, "Terjadi kesalahan saat memuat list produk", Toast.LENGTH_SHORT).show();

                    }
                }) {
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

    public static class ListViewAdapterDetailProduk extends ArrayAdapter<data_detail_produk> {
        private List<data_detail_produk> dataDetailProduk_pojos;

        private Context context;

        public ListViewAdapterDetailProduk(List<data_detail_produk> dataDetailProduk_pojos, Context context) {
            super(context, R.layout.list_detail_produk, dataDetailProduk_pojos);
            this.dataDetailProduk_pojos = dataDetailProduk_pojos;
            this.context = context;
        }

        @SuppressLint("NewApi")
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = LayoutInflater.from(context);

            View listViewItem = inflater.inflate(R.layout.list_detail_produk, null, true);

            TextView namaproduk = listViewItem.findViewById(R.id.nama_produk);
            TextView qtyproduk = listViewItem.findViewById(R.id.qty_produk);
            TextView satuanproduk = listViewItem.findViewById(R.id.satuan_produk);

            data_detail_produk data = getItem(position);

            namaproduk.setText(data.getNama());
            qtyproduk.setText(data.getQty());
            satuanproduk.setText(data.getSatuan());


            return listViewItem;
        }
    }

}