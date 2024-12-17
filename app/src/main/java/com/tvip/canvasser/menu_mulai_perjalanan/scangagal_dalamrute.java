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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.tabs.TabLayout;
import com.tvip.canvasser.Perangkat.HttpsTrustManager;
import com.tvip.canvasser.R;
import com.tvip.canvasser.pojo.data_pelanggan_dalam_rute_pojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class scangagal_dalamrute extends AppCompatActivity {
    TabLayout tablayout;
    ListView list_dalamrute;
    List<data_pelanggan_dalam_rute_pojo> dataPelangganDalamRutePojos = new ArrayList<>();
    ListViewAdapterDaftarDalamPelanggan adapter;
    SearchView caripelanggan;
    SharedPreferences sharedPreferences;

    AutoCompleteTextView act_pilih_do;

    Dialog dialog;
    TextView tv_ritase;
    String ritase;

    ArrayList<String> DOs = new ArrayList<>();

    SweetAlertDialog pDialog;

    int angka;

    ArrayList<String> pilih_ritase = new ArrayList<>();

    String string_ritase, string_employeeid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HttpsTrustManager.allowAllSSL();

        setContentView(R.layout.activity_scangagal_dalamrute);
        tablayout = findViewById(R.id.tablayout);
        list_dalamrute = findViewById(R.id.list_dalamrute);
        caripelanggan = findViewById(R.id.caripelanggan);

        //---------------------------------------------------------------------------------------------------
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String employee_id = sharedPreferences.getString("szDocCall", null);

        tablayout.getTabAt(0).getOrCreateBadge().setNumber(0);
        tablayout.getTabAt(1).getOrCreateBadge().setNumber(0);
        tablayout.getTabAt(2).getOrCreateBadge().setNumber(0);
        getCountFinish();

        ListDatapelangganDalamRute("0");
        tablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab){
                int position = tab.getPosition();
                angka = position;
                if(position == 0){
                    ListDatapelangganDalamRute("0");
                } else if (position == 1){
                    ListDatapelangganDalamRute("1");
                } else {
                    ListDatapelangganDalamRute("2");
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                if(position == 0){
                    ListDatapelangganDalamRute("0");
                } else if (position == 1){
                    ListDatapelangganDalamRute("1");
                } else {
                    ListDatapelangganDalamRute("2");
                }

            }
        });

        list_dalamrute.setOnItemClickListener((parent, v, position, id) -> {
            String idstd = STD;

            String nama = ((data_pelanggan_dalam_rute_pojo) parent.getItemAtPosition(position)).getSzName();
            String kode = ((data_pelanggan_dalam_rute_pojo) parent.getItemAtPosition(position)).getSzCustomerId();
            String alamat = ((data_pelanggan_dalam_rute_pojo) parent.getItemAtPosition(position)).getSzAddress();

            String langitude = ((data_pelanggan_dalam_rute_pojo) parent.getItemAtPosition(position)).getSzLatitude();
            String longitude = ((data_pelanggan_dalam_rute_pojo) parent.getItemAtPosition(position)).getSzLongitude();
            String intnumber = ((data_pelanggan_dalam_rute_pojo) parent.getItemAtPosition(position)).getIntItemNumber();

            if(((data_pelanggan_dalam_rute_pojo) parent.getItemAtPosition(position)).getBsuccess().equals("0") && !((data_pelanggan_dalam_rute_pojo) parent.getItemAtPosition(position)).getSzFailReason().equals("")){
                new SweetAlertDialog(scangagal_dalamrute.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Pelanggan Sudah Selesai Dikunjungi")
                        .setConfirmText("OK")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .show();
            } else if(((data_pelanggan_dalam_rute_pojo) parent.getItemAtPosition(position)).getBsuccess().equals("1")){
                new SweetAlertDialog(scangagal_dalamrute.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Pelanggan Sudah Selesai Dikunjungi")
                        .setConfirmText("OK")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .show();

            } else if (((data_pelanggan_dalam_rute_pojo) parent.getItemAtPosition(position)).getbStarted().equals("1")){
                final Dialog dialogDO = new Dialog(scangagal_dalamrute.this);
                dialogDO.setContentView(R.layout.pilih_do);
                dialogDO.show();

                act_pilih_do = dialogDO.findViewById(R.id.act_pilih_do);
                Button tidak = dialogDO.findViewById(R.id.tidak);
                Button ya = dialogDO.findViewById(R.id.ya);
//
                DOs.clear();
                StringRequest rest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_Comparing_Pelanggan?surat_tugas="+STD+"&szCustomerId=" + kode,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if (jsonObject.getString("status").equals("true")) {
                                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                                        for (int i = 0; i < jsonArray.length(); i++) {

                                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                            if(angka == 0){
                                                if(jsonObject1.getString("bOutOfRoute").equals("0") && jsonObject1.getString("bFinisihed").equals("0")){
                                                    if(jsonObject1.getString("szStatusPending").equals("1")){
                                                        DOs.add(jsonObject1.getString("szDocDO") + " (DO PENDING / "+jsonObject1.getString("satuan") +")");
                                                    } else {
                                                        DOs.add(jsonObject1.getString("szDocDO") + " (" + jsonObject1.getString("satuan") +")");
                                                    }
                                                }
                                            } else if (angka == 1){
                                                if(jsonObject1.getString("bOutOfRoute").equals("0") && jsonObject1.getString("bPostPone").equals("1")){
                                                    if(jsonObject1.getString("szStatusPending").equals("1")){
                                                        DOs.add(jsonObject1.getString("szDocDO") + " (DO PENDING / "+jsonObject1.getString("satuan") +")");
                                                    } else {
                                                        DOs.add(jsonObject1.getString("szDocDO") + " (" + jsonObject1.getString("satuan") +")");
                                                    }
                                                }
                                            }

                                        }
                                        act_pilih_do.setAdapter(new ArrayAdapter<String>(scangagal_dalamrute.this, android.R.layout.simple_expandable_list_item_1, DOs));

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
                rest.setRetryPolicy(new DefaultRetryPolicy(
                        500000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                RequestQueue requestkota = Volley.newRequestQueue(scangagal_dalamrute.this);
                requestkota.add(rest);

                tidak.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogDO.dismiss();
                    }
                });

                ya.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(act_pilih_do.getText().toString().length() == 0){
                            act_pilih_do.setError("Pilih DO");
                        } else {
                            pDialog = new SweetAlertDialog(scangagal_dalamrute.this, SweetAlertDialog.PROGRESS_TYPE);
                            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                            pDialog.setTitleText("Harap Menunggu");
                            pDialog.setCancelable(false);
                            pDialog.show();

                            String[] parts = act_pilih_do.getText().toString().split("\\(");
                            String restnomor = parts[0];
                            String restnomorbaru = restnomor.replace(" ", "");
                            StringRequest pilih_customer = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_cekdo?szDocDO=" + restnomorbaru,

                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            pDialog.dismissWithAnimation();
                                            try {
                                                JSONObject jsonObject = new JSONObject(response);
                                                if (jsonObject.getString("status").equals("true")) {

                                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                                    for (int i = 0; i < jsonArray.length(); i++) {

                                                        //parameter jika mengambul data dari url url_get_data_checklist_stagging
                                                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                                        String[] parts = act_pilih_do.getText().toString().split("\\(");
                                                        String restnomor = parts[0];
                                                        String restnomorbaru = restnomor.replace(" ", "");

                                                        if(jsonObject1.getString("bVisited").equals("1")){
                                                            act_pilih_do.setError(null);
                                                            Intent intent = new Intent(getBaseContext(), menu_pelanggan.class);

                                                            intent.putExtra("idStd", idstd);
                                                            intent.putExtra("kode", kode);
                                                            intent.putExtra("szDocDo", restnomorbaru);

                                                            startActivity(intent);
                                                        } else {
                                                            act_pilih_do.setError(null);
                                                            Intent intent = new Intent(getBaseContext(), detailscangagal_dalamrute.class);

                                                            intent.putExtra("idStd", idstd);
                                                            intent.putExtra("nama", nama);
                                                            intent.putExtra("kode", kode);
                                                            intent.putExtra("alamat", alamat);
                                                            intent.putExtra("langitude", langitude);
                                                            intent.putExtra("longitude", longitude);
                                                            intent.putExtra("szDocDo", restnomorbaru);
                                                            intent.putExtra("intItemNumbers", intnumber);


                                                            startActivity(intent);
                                                        }



                                                    }
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

                            pilih_customer.setRetryPolicy(new DefaultRetryPolicy(
                                    10000,
                                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                            RequestQueue requestQueue = Volley.newRequestQueue(scangagal_dalamrute.this);
                            requestQueue.add(pilih_customer);


                        }

                    }
                });

            } else {
                final Dialog dialogDO = new Dialog(scangagal_dalamrute.this);
                dialogDO.setContentView(R.layout.pilih_do);
                dialogDO.show();

                dialogDO.show();

                act_pilih_do = dialogDO.findViewById(R.id.act_pilih_do);
                Button tidak = dialogDO.findViewById(R.id.tidak);
                Button ya = dialogDO.findViewById(R.id.ya);
//
                DOs.clear();
                StringRequest rest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_Comparing_Pelanggan?surat_tugas="+STD+"&szCustomerId=" + kode,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if (jsonObject.getString("status").equals("true")) {
                                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                                        for (int i = 0; i < jsonArray.length(); i++) {

                                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                            if(angka == 0){
                                                if(jsonObject1.getString("bOutOfRoute").equals("0") && jsonObject1.getString("bFinisihed").equals("0")){
                                                    if(jsonObject1.getString("szStatusPending").equals("1")){
                                                        DOs.add(jsonObject1.getString("szDocDO") + " (DO PENDING / "+jsonObject1.getString("satuan") +")");
                                                    } else {
                                                        DOs.add(jsonObject1.getString("szDocDO") + " (" + jsonObject1.getString("satuan") +")");
                                                    }
                                                }
                                            } else if (angka == 1){
                                                if(jsonObject1.getString("bOutOfRoute").equals("0") && jsonObject1.getString("bPostPone").equals("1")){
                                                    if(jsonObject1.getString("szStatusPending").equals("1")){
                                                        DOs.add(jsonObject1.getString("szDocDO") + " (DO PENDING / "+jsonObject1.getString("satuan") +")");
                                                    } else {
                                                        DOs.add(jsonObject1.getString("szDocDO") + " (" + jsonObject1.getString("satuan") +")");
                                                    }
                                                }
                                            }
                                        }
                                        act_pilih_do.setAdapter(new ArrayAdapter<String>(scangagal_dalamrute.this, android.R.layout.simple_expandable_list_item_1, DOs));

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
                rest.setRetryPolicy(new DefaultRetryPolicy(
                        500000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                RequestQueue requestkota = Volley.newRequestQueue(scangagal_dalamrute.this);
                requestkota.add(rest);

                tidak.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogDO.dismiss();
                    }
                });

                ya.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(act_pilih_do.getText().toString().length() == 0){
                            act_pilih_do.setError("Pilih DO");
                        } else {
                            act_pilih_do.setError(null);
                            Intent i = new Intent(getBaseContext(), detailscangagal_dalamrute.class);
                            Toast.makeText(scangagal_dalamrute.this, intnumber, Toast.LENGTH_SHORT).show();

                            String[] parts = act_pilih_do.getText().toString().split("\\(");
                            String restnomor = parts[0];
                            String restnomorbaru = restnomor.replace(" ", "");

                            i.putExtra("idStd", idstd);
                            i.putExtra("nama", nama);
                            i.putExtra("kode", kode);
                            i.putExtra("alamat", alamat);
                            i.putExtra("langitude", langitude);
                            i.putExtra("longitude", longitude);
                            i.putExtra("szDocDo", restnomorbaru);
                            i.putExtra("intItemNumbers", intnumber);


                            startActivity(i);
                        }

                    }
                });

            }

        });

    }

    private void getCountFinish() {
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String employee_id = sharedPreferences.getString("szDocCall", null);
        Toast.makeText(scangagal_dalamrute.this, "EmployeeId = " + employee_id, Toast.LENGTH_SHORT).show();

        String link;

        link = "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_List_Dalam_Pelanggan?surat_tugas=" + STD;


        StringRequest stringRequest = new StringRequest(Request.Method.GET, link,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.dismissWithAnimation();
                        tablayout.setEnabled(true);
                        try {
                            int number1 = 0;
                            int number2 = 0;
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");
                            for (int i = 0; i < movieArray.length(); i++) {

                                final JSONObject movieObject = movieArray.getJSONObject(i);

                                if(movieObject.getString("bPostPone").equals("1")) {
                                    number1++;
                                    tablayout.getTabAt(1).getOrCreateBadge().setNumber(number1);
                                }

                                if(movieObject.getString("bFinisihed").equals("1") && movieObject.getString("bPostPone").equals("0")) {
                                    number2++;
                                    tablayout.getTabAt(2).getOrCreateBadge().setNumber(number2);
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
                        pDialog.dismissWithAnimation();
                        tablayout.setEnabled(false);
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
                        5000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                ));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);
    }

    private void ListDatapelangganDalamRute(String s) {

        pDialog = new SweetAlertDialog(scangagal_dalamrute.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Harap Menunggu");
        pDialog.setCancelable(false);
        pDialog.show();

        adapter = new ListViewAdapterDaftarDalamPelanggan(dataPelangganDalamRutePojos, getApplicationContext());
        list_dalamrute.setAdapter(adapter);
        adapter.clear();
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String employee_id = sharedPreferences.getString("szDocCall", null);
        Toast.makeText(scangagal_dalamrute.this, "EmployeeId = " + employee_id, Toast.LENGTH_SHORT).show();

        String link;

        if(s.equals("0")){
            link = "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_List_Dalam_Pelanggan_belum?surat_tugas=" + STD;
        } else {
            link = "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_List_Dalam_Pelanggan_Baru?surat_tugas=" + STD;
        }

        System.out.println("Link = " + link);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, link,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.dismissWithAnimation();
                        tablayout.setEnabled(true);
                        try {
                            int number = 0;
                            int number2 = 0;
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");
                            for (int i = 0; i < movieArray.length(); i++) {


                                final JSONObject movieObject = movieArray.getJSONObject(i);

                                String customer_awal, nama_tokoalih, alamatalih;

                                if(s.equals("0")){
                                    customer_awal = "null";
                                    nama_tokoalih = "null";
                                    alamatalih = "null";
                                } else {
                                    customer_awal = movieObject.getString("szCustomerId_After");
                                    nama_tokoalih = movieObject.getString("nama_tokoalih");
                                    alamatalih = movieObject.getString("alamatalih");
                                }

                                String DOcMix = null;

                                if(movieObject.getString("mix_do").equals("null")){
                                    DOcMix = movieObject.getString("szDocDO");
                                } else if(movieObject.getString("mix_do").equals(movieObject.getString("szDocDO"))){
                                    DOcMix = movieObject.getString("szDocDO");
                                } else if(!movieObject.getString("mix_do").equals(movieObject.getString("szDocDO"))){
                                    DOcMix = movieObject.getString("szDocDO") + ", " + movieObject.getString("mix_do") + " (MIX)";
                                }
                                final data_pelanggan_dalam_rute_pojo movieItem = new data_pelanggan_dalam_rute_pojo(
                                        movieObject.getString("szCustomerId"),
                                        movieObject.getString("szName").toUpperCase(),
                                        movieObject.getString("intItemNumber"),
                                        movieObject.getString("szAddress"),
                                        movieObject.getString("szLatitude"),
                                        movieObject.getString("szLongitude"),
                                        movieObject.getString("bStarted"),
                                        movieObject.getString("bsuccess"),
                                        movieObject.getString("bScanBarcode"),
                                        movieObject.getString("bPostPone"),
                                        movieObject.getString("szRefDocId"),
                                        movieObject.getString("szFailReason"),
                                        DOcMix,
                                        customer_awal,
                                        nama_tokoalih,
                                        alamatalih,
                                        movieObject.getString("szStatusPending"));

                                dataPelangganDalamRutePojos.add(movieItem);

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


                                if(movieObject.getString("szDocSO").contains("AQ") || movieObject.getString("szDocSO").contains("VT")){
                                    dataPelangganDalamRutePojos.remove(movieItem);
                                    adapter.notifyDataSetChanged();
                                }

                                if(movieObject.getString("bPostPone").equals("0") && movieObject.getString("bsuccess").equals("0") && movieObject.getString("bFinisihed").equals("0")) {
                                    number++;
                                    tablayout.getTabAt(0).getOrCreateBadge().setNumber(number);
                                }

                                if(movieObject.getString("bFinisihed").equals("1") && movieObject.getString("bPostPone").equals("0")) {
                                    number2++;
                                    tablayout.getTabAt(2).getOrCreateBadge().setNumber(number2);
                                }


                                if(s.equals("0")){
                                    if(!movieObject.getString("bPostPone").equals("0") || !movieObject.getString("bsuccess").equals("0") || !movieObject.getString("bFinisihed").equals("0")) {
                                        dataPelangganDalamRutePojos.remove(movieItem);
                                        adapter.notifyDataSetChanged();
                                    }
                                } else if(s.equals("1")){
                                    if(!movieObject.getString("bPostPone").equals("1")) {
                                        dataPelangganDalamRutePojos.remove(movieItem);
                                        adapter.notifyDataSetChanged();
                                    }
                                } else if(s.equals("2")){
                                    pDialog.dismissWithAnimation();
                                    if(!(movieObject.getString("bFinisihed").equals("1") && movieObject.getString("bPostPone").equals("0"))) {
                                        dataPelangganDalamRutePojos.remove(movieItem);
                                        adapter.notifyDataSetChanged();
                                    }

                                }
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
                        tablayout.setEnabled(false);
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
                        5000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                ));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);
    }

    public class ListViewAdapterDaftarDalamPelanggan extends BaseAdapter implements Filterable {
        private List<data_pelanggan_dalam_rute_pojo> dataPelangganDalamRutePojos;
        private List<data_pelanggan_dalam_rute_pojo> dataPelangganDalamRutePojosFiltered;

        private Context context;

        public ListViewAdapterDaftarDalamPelanggan(List<data_pelanggan_dalam_rute_pojo> dataPelangganDalamRutePojos, Context context) {
            this.dataPelangganDalamRutePojos = dataPelangganDalamRutePojos;
            this.dataPelangganDalamRutePojosFiltered = dataPelangganDalamRutePojos;
            this.context = context;
        }

        @Override
        public int getCount() {
            return dataPelangganDalamRutePojosFiltered.size();
        }

        @Override
        public Object getItem(int position) {
            return dataPelangganDalamRutePojosFiltered.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public void clear() {
            dataPelangganDalamRutePojosFiltered.clear();

        }

        @SuppressLint("NewApi")
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = LayoutInflater.from(context);

            View listViewItem = inflater.inflate(R.layout.list_rute, null, true);

            TextView namatoko = listViewItem.findViewById(R.id.namatoko);
            TextView alamat = listViewItem.findViewById(R.id.alamat);
            TextView status = listViewItem.findViewById(R.id.status);
            MaterialCardView warna = listViewItem.findViewById(R.id.warna);
            TextView std = listViewItem.findViewById(R.id.std);

            TextView namatokoalih = listViewItem.findViewById(R.id.namatokoalih);
            TextView alamatalih = listViewItem.findViewById(R.id.alamatalih);
            TextView stdalih = listViewItem.findViewById(R.id.stdalih);
            TextView nodo = listViewItem.findViewById(R.id.nodo);


            LinearLayout linearalihcustomer = listViewItem.findViewById(R.id.linearalihcustomer);
            nodo.setVisibility(View.VISIBLE);


            nodo.setText("NO DO : " + dataPelangganDalamRutePojosFiltered.get(position).getSzDocDO());

            namatoko.setText(dataPelangganDalamRutePojosFiltered.get(position).getSzName());
            alamat.setText(dataPelangganDalamRutePojosFiltered.get(position).getSzAddress());
            std.setText(dataPelangganDalamRutePojosFiltered.get(position).getSzCustomerId());

            if(!dataPelangganDalamRutePojosFiltered.get(position).getAlamatalih().equals("null")){
                linearalihcustomer.setVisibility(View.VISIBLE);
                namatokoalih.setText(dataPelangganDalamRutePojosFiltered.get(position).getNama_tokoalih());
                alamatalih.setText(dataPelangganDalamRutePojosFiltered.get(position).getAlamatalih());
                stdalih.setText(dataPelangganDalamRutePojosFiltered.get(position).getSzCustomerId_After());
            }

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
                        filterResults.count = dataPelangganDalamRutePojos.size();
                        filterResults.values = dataPelangganDalamRutePojos;

                    }else{
                        List<data_pelanggan_dalam_rute_pojo> resultsModel = new ArrayList<>();
                        String searchStr = constraint.toString().toUpperCase();

                        for(data_pelanggan_dalam_rute_pojo itemsModel:dataPelangganDalamRutePojos){
                            if(itemsModel.getSzName().contains(searchStr) || itemsModel.getSzCustomerId().contains(searchStr) || itemsModel.getSzCustomerId_After().contains(searchStr) || itemsModel.getNama_tokoalih().contains(searchStr) || itemsModel.getSzDocDO().contains(searchStr)){
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

                    dataPelangganDalamRutePojosFiltered = (List<data_pelanggan_dalam_rute_pojo>) results.values;
                    notifyDataSetChanged();

                }
            };
            return filter;
        }
    }
}