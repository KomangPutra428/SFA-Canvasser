package com.tvip.canvasser.menu_mulai_perjalanan;

import static com.tvip.canvasser.menu_mulai_perjalanan.mulai_perjalanan.STD;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.tabs.TabLayout;
import com.tvip.canvasser.R;
import com.tvip.canvasser.menu_utama.MainActivity;
import com.tvip.canvasser.pojo.data_pelanggan_luar_rute_pojo;

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

public class scangagal_luarrute extends AppCompatActivity {
    TabLayout tablayout;
    ListView list_luarrute;
    List<data_pelanggan_luar_rute_pojo> dataPelangganLuarRutePojos = new ArrayList<>();
    ListViewAdapterDaftarLuarPelanggan adapter;
    SweetAlertDialog pDialog;
    SharedPreferences sharedPreferences;
    SearchView caripelanggan;
    int nomor;

    String id_pelanggan, szVehicleId, szHelper1;

    private RequestQueue requestQueue7;


    int nomorposition;

    private RequestQueue requestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scangagal_luarrute);
        tablayout = findViewById(R.id.tablayout);
        caripelanggan = findViewById(R.id.caripelanggan);
        nomorposition = 0;

        tablayout.getTabAt(0).getOrCreateBadge().setNumber(0);
        tablayout.getTabAt(1).getOrCreateBadge().setNumber(0);
        tablayout.getTabAt(2).getOrCreateBadge().setNumber(0);

        getSuratTugas();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_LastData?surat_tugas=" + STD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");

                            JSONObject biodatas = null;
                            for (int i = 0; i < movieArray.length(); i++) {

                                biodatas = movieArray.getJSONObject(i);

                                nomor = Integer.parseInt(biodatas.getString("intItemNumber")) + 1;

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

        stringRequest.setRetryPolicy(
                new DefaultRetryPolicy(
                        500000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                ));
        RequestQueue biodataQueue = Volley.newRequestQueue(this);
        biodataQueue.add(stringRequest);

        list_luarrute = findViewById(R.id.list_luarrute);
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String nik_baru = sharedPreferences.getString("szDocCall", null);
        tablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab){
                int position = tab.getPosition();
                getCount2();
                getCount3();
                nomorposition = position;
                if(position == 0){
                    cekBackup();
                } else if (position == 1){
                    ListDatapelangganLuarRute("1", "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_ListLuarPelangganSuratTugas?surat_tugas=" + STD);
                } else {
                    ListDatapelangganLuarRute("2", "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_ListLuarPelangganSuratTugasSelesai?surat_tugas=" + STD);
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                getCount2();
                getCount3();
                nomorposition = position;
                if(position == 0){
                    cekBackup();
                } else if (position == 1){
                    ListDatapelangganLuarRute("1", "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_ListLuarPelangganSuratTugas?surat_tugas=" + STD);
                } else {
                    ListDatapelangganLuarRute("2", "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_ListLuarPelangganSuratTugasSelesai?surat_tugas=" + STD);
                }

            }
        });



        list_luarrute.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                String nama = ((data_pelanggan_luar_rute_pojo) parent.getItemAtPosition(position)).getSzName();
                String kode = ((data_pelanggan_luar_rute_pojo) parent.getItemAtPosition(position)).getSzCustomerId();
                String alamat = ((data_pelanggan_luar_rute_pojo) parent.getItemAtPosition(position)).getSzAddress();

                String langitude = ((data_pelanggan_luar_rute_pojo) parent.getItemAtPosition(position)).getSzLatitude();
                String longitude = ((data_pelanggan_luar_rute_pojo) parent.getItemAtPosition(position)).getSzLongitude();
                String bSuccess = ((data_pelanggan_luar_rute_pojo) parent.getItemAtPosition(position)).getBsuccess();
                String bStarted = ((data_pelanggan_luar_rute_pojo) parent.getItemAtPosition(position)).getbStarted();
                String bPostPone = ((data_pelanggan_luar_rute_pojo) parent.getItemAtPosition(position)).getbPostPone();
                String szDocDo = ((data_pelanggan_luar_rute_pojo) parent.getItemAtPosition(position)).getSzDocDO();

                if(bSuccess.equals("1")){
                    Intent i = new Intent(getBaseContext(), menu_pelanggan.class);
                    i.putExtra("kode", kode);
                    i.putExtra("szDocDo", szDocDo);
                    startActivity(i);
                } else if (bPostPone.equals("1")){
                    Intent i = new Intent(getBaseContext(), menu_pelanggan.class);
                    i.putExtra("kode", kode);
                    i.putExtra("szDocDo", szDocDo);
                    startActivity(i);
                } else if (bStarted.equals("1")){
                    Intent i = new Intent(getBaseContext(), menu_pelanggan.class);
                    i.putExtra("kode", kode);
                    i.putExtra("szDocDo", szDocDo);
                    startActivity(i);
                }  else if(bStarted.equals("null")){
                    pDialog = new SweetAlertDialog(scangagal_luarrute.this, SweetAlertDialog.PROGRESS_TYPE);
                    pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                    pDialog.setTitleText("Harap Menunggu");
                    pDialog.setCancelable(false);
                    pDialog.show();
                    StringRequest stringRequest2 = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_Payment?szId=" + kode,
                            new Response.Listener<String>() {

                                @Override
                                public void onResponse(String response) {

                                    try {
                                        JSONObject obj = new JSONObject(response);
                                        final JSONArray movieArray = obj.getJSONArray("data");
                                        for (int i = 0; i < movieArray.length(); i++) {
                                            final JSONObject movieObject = movieArray.getJSONObject(i);

                                            String PaymentTerm = movieObject.getString("szPaymetTermId");
                                            String bAllowToCredit = movieObject.getString("bAllowToCredit");
                                            StringRequest channel_status = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_Detail_Pelanggan?szCustomerId=" + kode,
                                                    new Response.Listener<String>() {
                                                        @Override
                                                        public void onResponse(String response) {

                                                            try {
                                                                JSONObject obj = new JSONObject(response);
                                                                final JSONArray movieArray = obj.getJSONArray("data");

                                                                JSONObject biodatas = null;
                                                                for (int i = 0; i < movieArray.length(); i++) {

                                                                    biodatas = movieArray.getJSONObject(i);
                                                                    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
                                                                    String currentDateandTime2 = sdf2.format(new Date());

                                                                    getJumlah(kode, currentDateandTime2, currentDateandTime2, '1', currentDateandTime2, biodatas.getString("szDocSO"), biodatas.getString("szRefDocId"));






                                                                }

                                                            } catch (JSONException e) {
                                                                e.printStackTrace();

                                                            }
                                                        }

                                                        private void getJumlah(String szCustomerId, String currentDateandTime2, String currentDateandTime21, char c, String currentDateandTime22, String szDocSO, String szRefDocId) {
                                                            StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_LastData?surat_tugas=" + mulai_perjalanan.id_pelanggan,
                                                                    new Response.Listener<String>() {
                                                                        @Override
                                                                        public void onResponse(String response) {
                                                                            try {
                                                                                JSONObject obj = new JSONObject(response);
                                                                                final JSONArray movieArray = obj.getJSONArray("data");

                                                                                JSONObject biodatas = null;
                                                                                for (int i = 0; i < movieArray.length(); i++) {

                                                                                    biodatas = movieArray.getJSONObject(i);

                                                                                    nomor = Integer.parseInt(biodatas.getString("intItemNumber")) + 1;

                                                                                    getDOBaru();




                                                                                }

                                                                            } catch (JSONException e) {
                                                                                e.printStackTrace();

                                                                            }

                                                                        }

                                                                        private void getDOBaru() {
                                                                            sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                                                                            String nik_baru = sharedPreferences.getString("szDocCall", null);

                                                                            String[] parts = nik_baru.split("-");
                                                                            String restnomor = parts[0];
                                                                            String restnomorbaru = restnomor.replace(" ", "");
                                                                            StringRequest channel_status = new StringRequest(Request.Method.POST, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_DocDoData",
                                                                                    new Response.Listener<String>() {
                                                                                        @Override
                                                                                        public void onResponse(String response) {
                                                                                            try {
                                                                                                JSONObject jsonObj = new JSONObject(response);
                                                                                                String message = jsonObj.getString("message");
                                                                                                postPelangganLuar(szCustomerId, currentDateandTime2, currentDateandTime21, c, currentDateandTime22, szDocSO, szRefDocId, message);



                                                                                            } catch (JSONException e) {
                                                                                                e.printStackTrace();

                                                                                            }



                                                                                        }
                                                                                    },
                                                                                    new Response.ErrorListener() {
                                                                                        @Override
                                                                                        public void onErrorResponse(VolleyError error) {
                                                                                            pDialog.dismissWithAnimation();
                                                                                            if (error instanceof TimeoutError || error instanceof NoConnectionError)     {
                                                                                                new SweetAlertDialog(scangagal_luarrute.this, SweetAlertDialog.WARNING_TYPE)
                                                                                                        .setTitleText("Kesalahan dalam sistem, silahkan dicoba lagi")
                                                                                                        .setConfirmText("OK")
                                                                                                        .show();
                                                                                            } else {
                                                                                                new SweetAlertDialog(scangagal_luarrute.this, SweetAlertDialog.WARNING_TYPE)
                                                                                                        .setTitleText("Counter DO Duplicate Harap Info Ke Team ICT")
                                                                                                        .setConfirmText("OK").show();
                                                                                            }
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

                                                                                @Override
                                                                                protected Map<String, String> getParams() throws AuthFailureError {
                                                                                    Map<String, String> params = new HashMap<String, String>();
                                                                                    sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                                                                                    String nik_baru = sharedPreferences.getString("szDocCall", null);

                                                                                    String[] parts = nik_baru.split("-");
                                                                                    String branch = parts[0];

                                                                                    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                                                                    String currentDateandTime2 = sdf2.format(new Date());

                                                                                    SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd");
                                                                                    String currentDateandTime3 = sdf3.format(new Date());


                                                                                    params.put("dtmDoc", currentDateandTime3);
                                                                                    params.put("szCustomerId", szCustomerId);
                                                                                    params.put("szEmployeeId", nik_baru);

                                                                                    if(PaymentTerm.equals("0HARI")){
                                                                                        params.put("bCash", "1");
                                                                                    } else {
                                                                                        params.put("bCash", "0");
                                                                                    }
                                                                                    params.put("szPaymentTermId", PaymentTerm);

                                                                                    params.put("szVehicleId", szVehicleId);
                                                                                    params.put("szHelper1", szHelper1);

                                                                                    params.put("dtmCustomerPO", currentDateandTime2);
                                                                                    params.put("szSalesId", nik_baru);
                                                                                    params.put("szBranchId", branch);

                                                                                    if(branch.equals("321") || branch.equals("336") || branch.equals("324") || branch.equals("317") || branch.equals("036")){
                                                                                        params.put("szCompanyId", "ASA");
                                                                                    } else {
                                                                                        params.put("szCompanyId", "TVIP");
                                                                                    }

                                                                                    params.put("szUserCreatedId", nik_baru);
                                                                                    params.put("dtmCreated", currentDateandTime3);

                                                                                    params.put("szUserCreatedId", nik_baru);
                                                                                    params.put("dtmCreated", currentDateandTime2);

                                                                                    System.out.println("Params = " + params);

                                                                                    return params;
                                                                                }

                                                                            };





                                                                            channel_status.setRetryPolicy(
                                                                                    new DefaultRetryPolicy(
                                                                                            500000,
                                                                                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                                                                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                                                                                    ));
                                                                            RequestQueue requestQueue = Volley.newRequestQueue(scangagal_luarrute.this);
                                                                            requestQueue.add(channel_status);
                                                                        }

                                                                        private void postPelangganLuar(String szCustomerId, String currentDateandTime2, String currentDateandTime21, char c, String currentDateandTime22, String szDocSO, String szRefDocId, String s) {

                                                                            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, " https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_PelangganLuar",
                                                                                    new Response.Listener<String>() {

                                                                                        @Override
                                                                                        public void onResponse(String response) {
                                                                                            pDialog.dismissWithAnimation();
                                                                                            Intent i = new Intent(getBaseContext(), detailscangagal_dalamrute.class);

                                                                                            i.putExtra("nama", nama);
                                                                                            i.putExtra("kode", kode);
                                                                                            i.putExtra("alamat", alamat);
                                                                                            i.putExtra("langitude", langitude);
                                                                                            i.putExtra("longitude", longitude);
                                                                                            i.putExtra("szDocDo", s);
                                                                                            i.putExtra("intItemNumbers", String.valueOf(nomor));



                                                                                            startActivity(i);

                                                                                        }
                                                                                    }, new Response.ErrorListener() {
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

                                                                                    params.put("szDocId", mulai_perjalanan.id_pelanggan);
                                                                                    params.put("intItemNumber", String.valueOf(nomor));
                                                                                    params.put("szCustomerId", szCustomerId);

                                                                                    params.put("dtmStart", currentDateandTime2);
                                                                                    params.put("dtmFinish", currentDateandTime2);

                                                                                    params.put("bScanBarcode", "0");
                                                                                    params.put("dtmLastCheckin", currentDateandTime2);
                                                                                    params.put("szDocDO", s);
                                                                                    if(szRefDocId.equals("")){
                                                                                        params.put("szRefDocId", "");
                                                                                    } else {
                                                                                        params.put("szRefDocId", szRefDocId);
                                                                                    }

                                                                                    System.out.println("Params = " + params);
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
                                                                            RequestQueue requestQueue2 = Volley.newRequestQueue(scangagal_luarrute.this);
                                                                            requestQueue2.add(stringRequest2);
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
                                                            RequestQueue requestQueue = Volley.newRequestQueue(scangagal_luarrute.this);
                                                            requestQueue.add(stringRequest);
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
                                            if (requestQueue == null) {
                                                requestQueue = Volley.newRequestQueue(scangagal_luarrute.this);
                                                requestQueue.add(channel_status);
                                            } else {
                                                requestQueue.add(channel_status);
                                            }



                                        }


                                    } catch(JSONException e){
                                        e.printStackTrace();

                                    }

                                }
                            }, new Response.ErrorListener() {

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
                            SharedPreferences sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                            String nik_baru = sharedPreferences.getString("szDocCall", null);

                            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
                            String currentDateandTime2 = sdf2.format(new Date());


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
                    if (requestQueue7 == null) {
                        requestQueue7 = Volley.newRequestQueue(scangagal_luarrute.this);
                        requestQueue7.add(stringRequest2);
                    } else {
                        requestQueue7.add(stringRequest2);
                    }


                } else if(bStarted.equals("0")){
                    Intent i = new Intent(getBaseContext(), detailscangagal_dalamrute.class);
                    Toast.makeText(scangagal_luarrute.this, String.valueOf(nomor), Toast.LENGTH_SHORT).show();

                    i.putExtra("nama", nama);
                    i.putExtra("kode", kode);
                    i.putExtra("alamat", alamat);
                    i.putExtra("langitude", langitude);
                    i.putExtra("longitude", longitude);
                    i.putExtra("szDocDo", szDocDo);
                    i.putExtra("intItemNumbers", String.valueOf(nomor));


                    startActivity(i);
                }






            }




        });
    }

    private void cekBackup() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_Backup?szDocId=" + STD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");
                            for (int i = 0; i < movieArray.length(); i++) {
                                final JSONObject movieObject = movieArray.getJSONObject(i);

                                if(movieObject.getString("szBackupEmployee").equals("null")){
                                    getCount(movieObject.getString("szEmployeeId"));
                                    ListDatapelangganLuarRute("0", "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_Canvas_Pelanggan_Luar?surat_tugas=" + STD + "&szEmployeeId=" + movieObject.getString("szEmployeeId"));
                                } else {
                                    getCount(movieObject.getString("szBackupEmployee"));
                                    ListDatapelangganLuarRute("0", "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_Canvas_Pelanggan_Luar?surat_tugas=" + STD + "&szEmployeeId=" + movieObject.getString("szBackupEmployee"));
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
        RequestQueue requestQueue = Volley.newRequestQueue(scangagal_luarrute.this);
        requestQueue.add(stringRequest);
    }

    private void getSuratTugas() {
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String nik_baru = sharedPreferences.getString("szDocCall", null);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Persiapan/index_SuratTugas?nik_baru="+ nik_baru,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");
                            for (int i = 0; i < movieArray.length(); i++) {
                                final JSONObject movieObject = movieArray.getJSONObject(i);


                                szVehicleId = movieObject.getString("szVehicleId");
                                szHelper1 = movieObject.getString("szHelper1");



                            }


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
        RequestQueue requestQueue = Volley.newRequestQueue(scangagal_luarrute.this);
        requestQueue.add(stringRequest);
    }

    private void getCount2() {
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String nik_baru = sharedPreferences.getString("szDocCall", null);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_ListLuarPelangganSuratTugas?surat_tugas=" + STD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        tablayout.setEnabled(false);
                        try {
                            int number = 0;
                            int number1 = 0;
                            int number2 = 0;
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");
                            for (int i = 0; i < movieArray.length(); i++) {
                                final JSONObject movieObject = movieArray.getJSONObject(i);

                                if(movieObject.getString("bPostPone").equals("1")){
                                    number1++;
                                    tablayout.getTabAt(1).getOrCreateBadge().setNumber(number1);
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

    private void getCount(String szEmployeeId) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_Canvas_Pelanggan_Luar?surat_tugas=" + STD + "&szEmployeeId=" + szEmployeeId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        tablayout.setEnabled(false);
                        try {
                            int number = 0;
                            int number1 = 0;
                            int number2 = 0;
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");
                            for (int i = 0; i < movieArray.length(); i++) {
                                final JSONObject movieObject = movieArray.getJSONObject(i);

                                if((movieObject.getString("bPostPone").equals("null") || movieObject.getString("bPostPone").equals("0")) && (movieObject.getString("bFinisihed").equals("null") || movieObject.getString("bFinisihed").equals("0"))) {
                                    number++;
                                    tablayout.getTabAt(0).getOrCreateBadge().setNumber(number);
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

    private void ListDatapelangganLuarRute(String s, String link) {
        pDialog = new SweetAlertDialog(scangagal_luarrute.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Harap Menunggu");
        pDialog.setCancelable(false);
        pDialog.show();

        System.out.println("Link = " + link);
        adapter = new ListViewAdapterDaftarLuarPelanggan(dataPelangganLuarRutePojos, getApplicationContext());
        list_luarrute.setAdapter(adapter);
        adapter.clear();


        StringRequest stringRequest = new StringRequest(Request.Method.GET, link,
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
                                final data_pelanggan_luar_rute_pojo movieItem = new data_pelanggan_luar_rute_pojo(
                                        movieObject.getString("szCustomerId"),
                                        movieObject.getString("szName").toUpperCase(),
                                        movieObject.getString("szAddress"),
                                        movieObject.getString("szLatitude"),
                                        movieObject.getString("szLongitude"),
                                        movieObject.getString("bStarted"),
                                        movieObject.getString("bFinisihed"),
                                        movieObject.getString("bScanBarcode"),
                                        movieObject.getString("bPostPone"),
                                        movieObject.getString("szDocDO"));

                                dataPelangganLuarRutePojos.add(movieItem);

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

                                if(s.equals("0")){
                                    if(movieObject.getString("bPostPone").equals("1") || movieObject.getString("bFinisihed").equals("1")) {
                                        dataPelangganLuarRutePojos.remove(movieItem);
                                        adapter.notifyDataSetChanged();
                                    }
                                } else if(s.equals("1")){
                                    if(!movieObject.getString("bPostPone").equals("1")) {
                                        dataPelangganLuarRutePojos.remove(movieItem);
                                        adapter.notifyDataSetChanged();
                                    }
                                } else if(s.equals("2")){
                                    if (dataPelangganLuarRutePojos.contains(movieObject.getString("szCustomerId"))){
                                        dataPelangganLuarRutePojos.remove(movieItem);
                                        adapter.notifyDataSetChanged();
                                    } else if(!(movieObject.getString("bFinisihed").equals("1") && movieObject.getString("bPostPone").equals("0"))) {
                                        dataPelangganLuarRutePojos.remove(movieItem);
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

    public class ListViewAdapterDaftarLuarPelanggan extends BaseAdapter implements Filterable {
        private List<data_pelanggan_luar_rute_pojo> dataPelangganLuarRutePojos;
        private List<data_pelanggan_luar_rute_pojo> dataPelangganLuarRutePojosFiltered;


        private Context context;

        public ListViewAdapterDaftarLuarPelanggan(List<data_pelanggan_luar_rute_pojo> dataPelangganLuarRutePojos, Context context) {
            this.dataPelangganLuarRutePojos = dataPelangganLuarRutePojos;
            this.dataPelangganLuarRutePojosFiltered = dataPelangganLuarRutePojos;
            this.context = context;
        }

        @Override
        public int getCount() {
            return dataPelangganLuarRutePojosFiltered.size();
        }

        @Override
        public Object getItem(int position) {
            return dataPelangganLuarRutePojosFiltered.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public void clear() {
            dataPelangganLuarRutePojosFiltered.clear();

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


            std.setText(dataPelangganLuarRutePojosFiltered.get(position).getSzCustomerId());

            namatoko.setText(dataPelangganLuarRutePojosFiltered.get(position).getSzName());
            alamat.setText(dataPelangganLuarRutePojosFiltered.get(position).getSzAddress());

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
                        filterResults.count = dataPelangganLuarRutePojos.size();
                        filterResults.values = dataPelangganLuarRutePojos;

                    }else{
                        List<data_pelanggan_luar_rute_pojo> resultsModel = new ArrayList<>();
                        String searchStr = constraint.toString().toUpperCase();

                        for(data_pelanggan_luar_rute_pojo itemsModel:dataPelangganLuarRutePojos){
                            if(itemsModel.getSzName().contains(searchStr) || itemsModel.getSzCustomerId().contains(searchStr)){
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

                    dataPelangganLuarRutePojosFiltered = (List<data_pelanggan_luar_rute_pojo>) results.values;
                    notifyDataSetChanged();

                }
            };
            return filter;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        getCount2();
        getCount3();
        if(nomorposition == 0){
            cekBackup();
        } else if (nomorposition == 1){
            ListDatapelangganLuarRute("1", "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_ListLuarPelangganSuratTugas?surat_tugas=" + STD);
        } else {
            ListDatapelangganLuarRute("2", "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_ListLuarPelangganSuratTugasSelesai?surat_tugas=" + STD);
        }
    }

    private void getCount3() {
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String nik_baru = sharedPreferences.getString("szDocCall", null);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_ListLuarPelangganSuratTugasSelesai?surat_tugas=" + STD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        tablayout.setEnabled(false);
                        try {
                            int number = 0;
                            int number1 = 0;
                            int number2 = 0;
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");
                            for (int i = 0; i < movieArray.length(); i++) {
                                final JSONObject movieObject = movieArray.getJSONObject(i);

                                number2++;
                                tablayout.getTabAt(2).getOrCreateBadge().setNumber(number2);



                            }
                        } catch(JSONException e){
                            e.printStackTrace();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
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


}