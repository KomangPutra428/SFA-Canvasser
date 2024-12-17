package com.tvip.canvasser.menu_mulai_perjalanan;

import static com.tvip.canvasser.menu_mulai_perjalanan.mulai_perjalanan.STD;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.tabs.TabLayout;
import com.tvip.canvasser.R;
import com.tvip.canvasser.menu_selesai_perjalanan.akhirikegiatan;
import com.tvip.canvasser.pojo.data_pelanggan_non_rute_pojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class scangagal_nonrute extends AppCompatActivity {
    TabLayout tablayout;
    ListView list_nonrute;
    List<data_pelanggan_non_rute_pojo> dataPelangganNonRutePojos = new ArrayList<>();
    ListViewAdapterDaftarNonRutePelanggan adapter;
    SweetAlertDialog pDialog;
    SharedPreferences sharedPreferences;
    SearchView caripelanggan;

    private RequestQueue requestQueue;
    ArrayList<String> ItemNumber = new ArrayList<>();
    ArrayList<String> pilih_ritase = new ArrayList<>();

    String intnumber, pilihritase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scangagal_nonrute);

        tablayout = findViewById(R.id.tablayout);
        list_nonrute = findViewById(R.id.list_nonrute);
        caripelanggan = findViewById(R.id.caripelanggan);

        tablayout.getTabAt(0).getOrCreateBadge().setNumber(0);
        tablayout.getTabAt(1).getOrCreateBadge().setNumber(0);
        tablayout.getTabAt(2).getOrCreateBadge().setNumber(0);

        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String nik_baru = sharedPreferences.getString("szDocCall", null);
        String[] parts = nik_baru.split("-");
        String restnomor = parts[0];
        ListDatapelangganNonRute("0", "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_List_NonRute?surat_tugas="+STD+"&szSoldToBranchId=" + restnomor);

        tablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab){
                int position = tab.getPosition();
                sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                String nik_baru = sharedPreferences.getString("szDocCall", null);
                String[] parts = nik_baru.split("-");
                String restnomor = parts[0];
                if(position == 0){
                    ListDatapelangganNonRute("0", "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_List_NonRute?surat_tugas="+STD+"&szSoldToBranchId=" + restnomor);
                } else if (position == 1){
                    ListDatapelangganNonRute("1", "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_List_NonRute?surat_tugas="+STD+"&szSoldToBranchId=" + restnomor);
                } else {
                    ListDatapelangganNonRute("2", "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_List_NonRute?surat_tugas="+STD+"&szSoldToBranchId=" + restnomor);
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        list_nonrute.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String nama = ((data_pelanggan_non_rute_pojo) parent.getItemAtPosition(position)).getSzName();
                String kode = ((data_pelanggan_non_rute_pojo) parent.getItemAtPosition(position)).getSzId();
                String alamat = ((data_pelanggan_non_rute_pojo) parent.getItemAtPosition(position)).getSzAddress();

                String langitude = ((data_pelanggan_non_rute_pojo) parent.getItemAtPosition(position)).getSzLatitude();
                String longitude = ((data_pelanggan_non_rute_pojo) parent.getItemAtPosition(position)).getSzLongitude();
                String bSuccess = ((data_pelanggan_non_rute_pojo) parent.getItemAtPosition(position)).getBsuccess();
                String bStarted = ((data_pelanggan_non_rute_pojo) parent.getItemAtPosition(position)).getbStarted();
                String bPostPone = ((data_pelanggan_non_rute_pojo) parent.getItemAtPosition(position)).getbPostPone();
                String szDocDo = ((data_pelanggan_non_rute_pojo) parent.getItemAtPosition(position)).getSzDocDO();
                String intItemNumber = ((data_pelanggan_non_rute_pojo) parent.getItemAtPosition(position)).getIntItemNumber();

                if(szDocDo.equals("null")){

                    pilih_ritase.clear();
                    ItemNumber.clear();

                    Dialog dialog = new Dialog(scangagal_nonrute.this);
                    dialog.setCancelable(false);
                    dialog.setContentView(R.layout.pilih_dononrute);

                    TextView pilih = dialog.findViewById(R.id.pilih);
                    AutoCompleteTextView act_pilih_ritase = dialog.findViewById(R.id.act_pilih_ritase);

                    Button batal = dialog.findViewById(R.id.tidak);
                    Button ya = dialog.findViewById(R.id.ya);

                    act_pilih_ritase.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            intnumber = ItemNumber.get(position).toString();
                            pilihritase = pilih_ritase.get(position).toString();

                            System.out.println("Print out DO = " + intnumber + ". " + pilihritase);
                        }
                    });

                    ya.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if(act_pilih_ritase.getText().toString().length() == 0){
                                act_pilih_ritase.setError("Pilih DO");
                            } else {
                                Intent i = new Intent(getBaseContext(), detailscangagal_dalamrute.class);

                                i.putExtra("idStd", STD);
                                i.putExtra("nama", nama);
                                i.putExtra("kode", kode);
                                i.putExtra("alamat", alamat);
                                i.putExtra("langitude", langitude);
                                i.putExtra("longitude", longitude);
                                i.putExtra("szDocDo", pilihritase);
                                i.putExtra("intItemNumbers", intnumber);

                                i.putExtra("intItemNumbersNonRute", intnumber);
                                i.putExtra("DoNonRute", pilihritase);



                                startActivity(i);
                               // getNumber(kode, nama, alamat, langitude, longitude);

                            }
                        }
                    });

                    StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_current_std_NonRute_do?szDocId=" + STD, //szDocId,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    try {
                                        int number = 0;
                                        JSONObject obj = new JSONObject(response);
                                        if (obj.getString("status").equals("true")) {
                                            final JSONArray movieArray = obj.getJSONArray("data");
                                            for (int i = 0; i < movieArray.length(); i++) {
                                                final JSONObject movieObject = movieArray.getJSONObject(i);
                                                pilih_ritase.add(movieObject.getString("szDocDo"));
                                                ItemNumber.add(movieObject.getString("intItemNumber"));
                                            }



                                        }
                                        act_pilih_ritase.setAdapter(new ArrayAdapter<String>(scangagal_nonrute.this, android.R.layout.simple_expandable_list_item_1, pilih_ritase));




//                            pDialog.dismissWithAnimation();

                                    } catch(JSONException e){
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

                    stringRequest.setRetryPolicy(
                            new DefaultRetryPolicy(
                                    500000,
                                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                            ));
                    RequestQueue requestQueue = Volley.newRequestQueue(scangagal_nonrute.this);
                    requestQueue.add(stringRequest);

                    batal.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    pilih.setText("Pilih DO");
                    act_pilih_ritase.setHint("Pilih DO");

                    dialog.show();
//

//
//                    startActivity(i);
                } else if(bStarted.equals("0")){
                    Intent i = new Intent(getBaseContext(), detailscangagal_dalamrute.class);
                    i.putExtra("idStd", STD);
                    i.putExtra("nama", nama);
                    i.putExtra("kode", kode);
                    i.putExtra("alamat", alamat);
                    i.putExtra("langitude", langitude);
                    i.putExtra("longitude", longitude);
                    i.putExtra("szDocDo", szDocDo);
                    i.putExtra("intItemNumbers", intItemNumber);
                    startActivity(i);
                } else if (bSuccess.equals("1") && bPostPone.equals("0")){
                    new SweetAlertDialog(scangagal_nonrute.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Toko sudah selesai dikunjungi")
                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                } else {
                    Intent i = new Intent(getBaseContext(), menu_pelanggan.class);
                    i.putExtra("idStd", STD);
                    i.putExtra("kode", kode);
                    i.putExtra("szDocDo", szDocDo);
                    startActivity(i);
                }

            }
        });

    }

    private void getNumber(String kode, String nama, String alamat, String langitude, String longitude) {
        pDialog = new SweetAlertDialog(scangagal_nonrute.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Harap Menunggu");
        pDialog.setCancelable(false);
        pDialog.show();

        updateCustomer(intnumber, kode, pilihritase, nama, alamat, langitude, longitude);

    }

    private void updateCustomer(String intItemNumber, String kode, String szDocDO, String nama, String alamat, String langitude, String longitude) {
        StringRequest stringRequest2 = new StringRequest(Request.Method.PUT, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_update_nonrute",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

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

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                String nik_baru = sharedPreferences.getString("szDocCall", null);


                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String currentDateandTime2 = sdf2.format(new Date());

                params.put("szDocId", STD);
                params.put("intItemNumber", intItemNumber);
                params.put("szCustomerId", kode);


                return params;
            }

        };
        stringRequest2.setRetryPolicy(
                new DefaultRetryPolicy(
                        500000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
        );
        RequestQueue requestQueue2 = Volley.newRequestQueue(scangagal_nonrute.this);
        requestQueue2.add(stringRequest2);
    }

    private void ListDatapelangganNonRute(String condition, String link) {
        pDialog = new SweetAlertDialog(scangagal_nonrute.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Harap Menunggu");
        pDialog.setCancelable(false);
        pDialog.show();

        System.out.println("Link = " + link);
        adapter = new ListViewAdapterDaftarNonRutePelanggan(dataPelangganNonRutePojos, getApplicationContext());
        list_nonrute.setAdapter(adapter);
        adapter.clear();


        StringRequest stringRequest = new StringRequest(Request.Method.GET, link,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.dismissWithAnimation();
                        try {
                            int number = 0;
                            int number1 = 0;
                            int number2 = 0;
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");
                            for (int i = 0; i < movieArray.length(); i++) {
                                final JSONObject movieObject = movieArray.getJSONObject(i);
                                final data_pelanggan_non_rute_pojo movieItem = new data_pelanggan_non_rute_pojo(
                                        movieObject.getString("intItemNumber"),
                                        movieObject.getString("szId"),
                                        movieObject.getString("szName").toUpperCase(),
                                        movieObject.getString("szAddress"),
                                        movieObject.getString("szLatitude"),
                                        movieObject.getString("szLongitude"),
                                        movieObject.getString("bStarted"),
                                        movieObject.getString("bFinisihed"),
                                        movieObject.getString("bScanBarcode"),
                                        movieObject.getString("bPostPone"),
                                        movieObject.getString("szDocDO"));

                                dataPelangganNonRutePojos.add(movieItem);

                                if(!movieObject.getString("bPostPone").equals("1") || !movieObject.getString("bFinisihed").equals("1")) {
                                    number++;
                                    tablayout.getTabAt(0).getOrCreateBadge().setNumber(number);
                                }

                                if(movieObject.getString("bPostPone").equals("1")) {
                                    number1++;
                                    tablayout.getTabAt(1).getOrCreateBadge().setNumber(number1);
                                }

                                if(movieObject.getString("bFinisihed").equals("1") && movieObject.getString("bPostPone").equals("0")) {
                                    number2++;
                                    tablayout.getTabAt(2).getOrCreateBadge().setNumber(number2);
                                }

                                if(condition.equals("0")){
                                    if(movieObject.getString("bPostPone").equals("1") || movieObject.getString("bFinisihed").equals("1")) {
                                        dataPelangganNonRutePojos.remove(movieItem);
                                        adapter.notifyDataSetChanged();
                                    }
                                } else if(condition.equals("1")){
                                    if(!movieObject.getString("bPostPone").equals("1")) {
                                        dataPelangganNonRutePojos.remove(movieItem);
                                        adapter.notifyDataSetChanged();
                                    }
                                } else if(condition.equals("2")){
                                   if(!(movieObject.getString("bFinisihed").equals("1") && movieObject.getString("bPostPone").equals("0"))) {
                                       dataPelangganNonRutePojos.remove(movieItem);
                                        adapter.notifyDataSetChanged();
                                    }
                                }

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



                                adapter.notifyDataSetChanged();

                            }
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

    public class ListViewAdapterDaftarNonRutePelanggan extends BaseAdapter implements Filterable {
        private List<data_pelanggan_non_rute_pojo> dataPelangganNonRutePojos;
        private List<data_pelanggan_non_rute_pojo> dataPelangganNonRutePojosFiltered;


        private Context context;

        public ListViewAdapterDaftarNonRutePelanggan(List<data_pelanggan_non_rute_pojo> dataPelangganNonRutePojos, Context context) {
            this.dataPelangganNonRutePojos = dataPelangganNonRutePojos;
            this.dataPelangganNonRutePojosFiltered = dataPelangganNonRutePojos;
            this.context = context;
        }

        @Override
        public int getCount() {
            return dataPelangganNonRutePojosFiltered.size();
        }

        @Override
        public Object getItem(int position) {
            return dataPelangganNonRutePojosFiltered.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public void clear() {
            dataPelangganNonRutePojosFiltered.clear();

        }

        @SuppressLint("NewApi")
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = LayoutInflater.from(context);

            View listViewItem = inflater.inflate(R.layout.list_rute, null, true);

            TextView namatoko = listViewItem.findViewById(R.id.namatoko);
            TextView alamat = listViewItem.findViewById(R.id.alamat);
            TextView status = listViewItem.findViewById(R.id.status);
            TextView std = listViewItem.findViewById(R.id.std);
            MaterialCardView warna = listViewItem.findViewById(R.id.warna);


            std.setText(dataPelangganNonRutePojosFiltered.get(position).getSzId());

            namatoko.setText(dataPelangganNonRutePojosFiltered.get(position).getSzName());
            alamat.setText(dataPelangganNonRutePojosFiltered.get(position).getSzAddress());

            if(tablayout.getSelectedTabPosition() == 1){
                status.setText("Ditunda");
                warna.setCardBackgroundColor(Color.parseColor("#A2C21D"));
            } else if(tablayout.getSelectedTabPosition() == 2){
                status.setText("Selesai");
                warna.setCardBackgroundColor(Color.parseColor("#1EB547"));
            }
            return listViewItem;
        }
        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {

                    FilterResults filterResults = new FilterResults();
                    if(constraint == null || constraint.length() == 0){
                        filterResults.count = dataPelangganNonRutePojos.size();
                        filterResults.values = dataPelangganNonRutePojos;

                    }else{
                        List<data_pelanggan_non_rute_pojo> resultsModel = new ArrayList<>();
                        String searchStr = constraint.toString().toUpperCase();

                        for(data_pelanggan_non_rute_pojo itemsModel:dataPelangganNonRutePojos){
                            if(itemsModel.getSzName().contains(searchStr) || itemsModel.getSzId().contains(searchStr)){
                                resultsModel.add(itemsModel);

                            }
                            filterResults.count = resultsModel.size();
                            filterResults.values = resultsModel;
                        }


                    }

                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {

                    dataPelangganNonRutePojosFiltered = (List<data_pelanggan_non_rute_pojo>) results.values;
                    notifyDataSetChanged();

                }
            };
            return filter;
        }
    }
}