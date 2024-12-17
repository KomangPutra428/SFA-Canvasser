package com.tvip.canvasser.menu_mulai_perjalanan;

import static com.tvip.canvasser.menu_mulai_perjalanan.mulai_perjalanan.STD;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
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
import android.widget.LinearLayout;
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
import com.tvip.canvasser.driver_canvaser.mCanvaser_history_detail;
import com.tvip.canvasser.pojo.data_pelanggan_dalam_rute_pojo;
import com.tvip.canvasser.pojo.data_pelanggan_luar_rute_pojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class summary_penjualan extends AppCompatActivity {
    TabLayout tablayout;
    ListView list_summary;
    List<data_pelanggan_dalam_rute_pojo> dataPelangganDalamRutePojos = new ArrayList<>();
    ListViewAdapterDaftarDalamPelanggan adapter;

    List<data_pelanggan_luar_rute_pojo> dataPelangganLuarRutePojos = new ArrayList<>();
    ListViewAdapterDaftarLuarPelanggan adapter2;

    String nomor;

    SweetAlertDialog pDialog;

    int angka;

    SearchView caripelanggan;


    SharedPreferences sharedPreferences;

    ArrayList<String> doInduk = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary_penjualan);
        tablayout = findViewById(R.id.tablayout);
        list_summary = findViewById(R.id.list_summary);
        caripelanggan = findViewById(R.id.caripelanggan);

        angka = 0;

        tablayout.getTabAt(0).getOrCreateBadge().setNumber(0);
        tablayout.getTabAt(1).getOrCreateBadge().setNumber(0);
        tablayout.getTabAt(2).getOrCreateBadge().setNumber(0);
        tablayout.getTabAt(3).getOrCreateBadge().setNumber(0);
        tablayout.getTabAt(4).getOrCreateBadge().setNumber(0);

        countLuarRute();
        countNonRute();

        count();
        tablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab){
                int position = tab.getPosition();
                angka = tab.getPosition();
                countLuarRute();
                if(position == 0){
                    ListDatapelangganDalamRute("0");
                } else if (position == 1){
                    ListDatapelangganDalamRute("1");
                } else if (position == 2){
                    ListDatapelangganDalamRute("2");
                } else if (position == 3){
                    ListLuarPelanggan();
                } else {
                    StdNonRute();
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        list_summary.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                doInduk.clear();
                if(tablayout.getSelectedTabPosition() == 4){
                    pDialog = new SweetAlertDialog(summary_penjualan.this, SweetAlertDialog.PROGRESS_TYPE);
                    pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                    pDialog.setTitleText("Harap Menunggu");
                    pDialog.setCancelable(false);
                    pDialog.show();
                    sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                    String szEmployeeId = sharedPreferences.getString("szDocCall", null);
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_SuratTugasNonRute?szEmployeeId=" + szEmployeeId, //szDocId,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    pDialog.dismissWithAnimation();

                                    try {
                                        int number = 0;
                                        JSONObject obj = new JSONObject(response);
                                        if (obj.getString("status").equals("true")) {
                                            final JSONArray movieArray = obj.getJSONArray("data");
                                            final JSONObject movieObject = movieArray.getJSONObject(0);

                                            String STDNonRute = movieObject.getString("szDocId");
                                            final Dialog dialog_submit = new Dialog(summary_penjualan.this);
                                            dialog_submit.setContentView(R.layout.pilih_do);
                                            dialog_submit.setCancelable(false);
                                            dialog_submit.show();

                                            AutoCompleteTextView act_pilih_do = dialog_submit.findViewById(R.id.act_pilih_do);
                                            Button tidak = dialog_submit.findViewById(R.id.tidak);
                                            Button ya = dialog_submit.findViewById(R.id.ya);

                                            tidak.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    dialog_submit.dismiss();
                                                }
                                            });

                                            String szName = ((data_pelanggan_dalam_rute_pojo) parent.getItemAtPosition(position)).getSzName();
                                            String szCustomerId = ((data_pelanggan_dalam_rute_pojo) parent.getItemAtPosition(position)).getSzCustomerId();
                                            String tunda = ((data_pelanggan_dalam_rute_pojo) parent.getItemAtPosition(position)).getbPostPone();
                                            String success = ((data_pelanggan_dalam_rute_pojo) parent.getItemAtPosition(position)).getBsuccess();


                                            StringRequest channel_status = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_SO?szDocId=" + STDNonRute + "&szCustomerId=" + szCustomerId,
                                                    new Response.Listener<String>() {
                                                        @RequiresApi(api = Build.VERSION_CODES.O)
                                                        @Override
                                                        public void onResponse(String response) {

                                                            try {
                                                                JSONObject obj = new JSONObject(response);
                                                                final JSONArray movieArray = obj.getJSONArray("data");

                                                                JSONObject biodatas = null;

                                                                for (int i = 0; i < movieArray.length(); i++) {
                                                                    JSONObject jsonObject1 = movieArray.getJSONObject(i);
                                                                    String szDocDO = jsonObject1.getString("szDocDO");


                                                                        if(jsonObject1.getString("szStatusPending").equals("1")){
                                                                            doInduk.add(szDocDO + " (DO PENDING)");
                                                                        } else {
                                                                            doInduk.add(szDocDO);
                                                                        }



                                                                }

                                                                act_pilih_do.setAdapter(new ArrayAdapter<String>(summary_penjualan.this, android.R.layout.simple_expandable_list_item_1, doInduk));

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
                                            RequestQueue channel_statusQueue = Volley.newRequestQueue(summary_penjualan.this);
                                            channel_statusQueue.add(channel_status);

                                            act_pilih_do.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                    nomor = String.valueOf(position);
                                                }
                                            });

                                            ya.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    if(act_pilih_do.getText().toString().length() == 0){
                                                        act_pilih_do.setError("Pilih DO");
                                                    } else {
                                                        act_pilih_do.setError(null);

                                                        Intent detail = new Intent(getApplicationContext(), mCanvaser_history_detail.class);
                                                        detail.putExtra("szName", szName);
                                                        detail.putExtra("szCustomerId", szCustomerId);
                                                        detail.putExtra("STD", STDNonRute);
                                                        detail.putExtra("bOutOfRoute", "0");
                                                        detail.putExtra("Tunda", tunda);
                                                        detail.putExtra("success", success);
                                                        detail.putExtra("nomor", nomor);
                                                        startActivity(detail);
                                                    }

                                                }
                                            });
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
                                    new SweetAlertDialog(summary_penjualan.this, SweetAlertDialog.WARNING_TYPE)
                                            .setTitleText("Maaf, surat tugas DO belum tersedia")
                                            .setConfirmText("OK")
                                            .show();
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
                    RequestQueue requestQueue = Volley.newRequestQueue(summary_penjualan.this);
                    requestQueue.add(stringRequest);
                } else if(tablayout.getSelectedTabPosition() == 3){
                    final Dialog dialog_submit = new Dialog(summary_penjualan.this);
                    dialog_submit.setContentView(R.layout.pilih_do);
                    dialog_submit.setCancelable(false);
                    dialog_submit.show();

                    AutoCompleteTextView act_pilih_do = dialog_submit.findViewById(R.id.act_pilih_do);
                    Button tidak = dialog_submit.findViewById(R.id.tidak);
                    Button ya = dialog_submit.findViewById(R.id.ya);

                    tidak.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog_submit.dismiss();
                        }
                    });

                    String szName = ((data_pelanggan_luar_rute_pojo) parent.getItemAtPosition(position)).getSzName();
                    String szCustomerId = ((data_pelanggan_luar_rute_pojo) parent.getItemAtPosition(position)).getSzCustomerId();
                    String tunda = ((data_pelanggan_luar_rute_pojo) parent.getItemAtPosition(position)).getbPostPone();
                    String success = ((data_pelanggan_luar_rute_pojo) parent.getItemAtPosition(position)).getBsuccess();


                    StringRequest channel_status = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_SO?szDocId=" + STD + "&szCustomerId=" + szCustomerId + "&bOutOfRoute=1",
                            new Response.Listener<String>() {
                                @RequiresApi(api = Build.VERSION_CODES.O)
                                @Override
                                public void onResponse(String response) {

                                    try {
                                        JSONObject obj = new JSONObject(response);
                                        final JSONArray movieArray = obj.getJSONArray("data");

                                        JSONObject biodatas = null;

                                        for (int i = 0; i < movieArray.length(); i++) {
                                            JSONObject jsonObject1 = movieArray.getJSONObject(i);
                                            String szDocDO = jsonObject1.getString("szDocDO");

                                            if(jsonObject1.getString("szStatusPending").equals("1")){
                                                doInduk.add(szDocDO + " (DO PENDING)");
                                            } else {
                                                doInduk.add(szDocDO);
                                            }


                                        }

                                        act_pilih_do.setAdapter(new ArrayAdapter<String>(summary_penjualan.this, android.R.layout.simple_expandable_list_item_1, doInduk));

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
                    RequestQueue channel_statusQueue = Volley.newRequestQueue(summary_penjualan.this);
                    channel_statusQueue.add(channel_status);

                    act_pilih_do.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            nomor = String.valueOf(position);
                        }
                    });

                    ya.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(act_pilih_do.getText().toString().length() == 0){
                                act_pilih_do.setError("Pilih DO");
                            } else {
                                act_pilih_do.setError(null);
                                Intent detail = new Intent(getApplicationContext(), mCanvaser_history_detail.class);
                                detail.putExtra("szName", szName);
                                detail.putExtra("szCustomerId", szCustomerId);
                                detail.putExtra("STD", STD);
                                detail.putExtra("bOutOfRoute", "1");
                                detail.putExtra("Tunda", tunda);
                                detail.putExtra("success", success);
                                detail.putExtra("nomor", nomor);
                                startActivity(detail);
                            }

                        }
                    });


                } else if (tablayout.getSelectedTabPosition() == 2 || tablayout.getSelectedTabPosition() == 1){

                    final Dialog dialog_submit = new Dialog(summary_penjualan.this);
                    dialog_submit.setContentView(R.layout.pilih_do);
                    dialog_submit.setCancelable(false);



                    AutoCompleteTextView act_pilih_do = dialog_submit.findViewById(R.id.act_pilih_do);
                    Button tidak = dialog_submit.findViewById(R.id.tidak);
                    Button ya = dialog_submit.findViewById(R.id.ya);

                    tidak.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog_submit.dismiss();
                        }
                    });

                    String szName = ((data_pelanggan_dalam_rute_pojo) parent.getItemAtPosition(position)).getSzName();
                    String szCustomerId = ((data_pelanggan_dalam_rute_pojo) parent.getItemAtPosition(position)).getSzCustomerId();
                    String tunda = ((data_pelanggan_dalam_rute_pojo) parent.getItemAtPosition(position)).getbPostPone();
                    String success = ((data_pelanggan_dalam_rute_pojo) parent.getItemAtPosition(position)).getBsuccess();


                    StringRequest channel_status = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_SO?szDocId=" + STD + "&szCustomerId=" + szCustomerId,
                            new Response.Listener<String>() {
                                @RequiresApi(api = Build.VERSION_CODES.O)
                                @Override
                                public void onResponse(String response) {

                                    try {
                                        JSONObject obj = new JSONObject(response);
                                        final JSONArray movieArray = obj.getJSONArray("data");

                                        JSONObject biodatas = null;

                                        for (int i = 0; i < movieArray.length(); i++) {
                                            JSONObject jsonObject1 = movieArray.getJSONObject(i);
                                            String szDocDO = jsonObject1.getString("szDocDO");

                                            if(jsonObject1.getString("szStatusPending").equals("1")){
                                                doInduk.add(szDocDO + " (DO PENDING)");
                                            } else {
                                                doInduk.add(szDocDO);
                                            }


                                        }

                                        act_pilih_do.setAdapter(new ArrayAdapter<String>(summary_penjualan.this, android.R.layout.simple_expandable_list_item_1, doInduk));

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
                    RequestQueue channel_statusQueue = Volley.newRequestQueue(summary_penjualan.this);
                    channel_statusQueue.add(channel_status);

                    act_pilih_do.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            nomor = String.valueOf(position);
                        }
                    });

                    ya.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(act_pilih_do.getText().toString().length() == 0){
                                act_pilih_do.setError("Pilih DO");
                            } else {
                                act_pilih_do.setError(null);
                                Intent detail = new Intent(getApplicationContext(), mCanvaser_history_detail.class);
                                detail.putExtra("szName", szName);
                                detail.putExtra("szCustomerId", szCustomerId);
                                detail.putExtra("STD", STD);
                                detail.putExtra("bOutOfRoute", "0");
                                detail.putExtra("Tunda", tunda);
                                detail.putExtra("success", success);
                                detail.putExtra("nomor", nomor);
                                startActivity(detail);
                            }

                        }
                    });

                    pDialog = new SweetAlertDialog(summary_penjualan.this, SweetAlertDialog.PROGRESS_TYPE);
                    pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                    pDialog.setTitleText("Harap Menunggu");
                    pDialog.setCancelable(false);
                    pDialog.show();

                    StringRequest cekDO = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_SO?szDocId=" + STD + "&szCustomerId=" + szCustomerId,
                            new Response.Listener<String>() {
                                @RequiresApi(api = Build.VERSION_CODES.O)
                                @Override
                                public void onResponse(String response) {
                                    pDialog.dismissWithAnimation();

                                    try {
                                        JSONObject obj = new JSONObject(response);
                                        final JSONArray movieArray = obj.getJSONArray("data");

                                        JSONObject biodatas = null;

                                        for (int i = 0; i < movieArray.length(); i++) {
                                            JSONObject jsonObject1 = movieArray.getJSONObject(i);
                                            String szDocDO = jsonObject1.getString("szDocDO");

                                            if(!jsonObject1.getString("szDocSO").equals("")){
                                                dialog_submit.show();
                                                break;
                                            } else if (i == movieArray.length() - 1){
                                                dialog_submit.show();
                                                break;
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

                    cekDO.setRetryPolicy(
                            new DefaultRetryPolicy(
                                    500000,
                                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                            ));
                    RequestQueue cekDOQueue = Volley.newRequestQueue(summary_penjualan.this);
                    cekDOQueue.add(cekDO);


                } else {
                    new SweetAlertDialog(summary_penjualan.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Maaf, Pelanggan ini belum ada transaksi")
                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                }
            }


        });
    }

    private void countNonRute() {
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String szEmployeeId = sharedPreferences.getString("szDocCall", null);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_SuratTugasNonRute?szEmployeeId=" + szEmployeeId, //szDocId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        pDialog.dismissWithAnimation();

                        try {
                            int number = 0;
                            JSONObject obj = new JSONObject(response);
                            if (obj.getString("status").equals("true")) {
                                final JSONArray movieArray = obj.getJSONArray("data");
                                final JSONObject movieObject = movieArray.getJSONObject(0);

                                String STDNonRute = movieObject.getString("szDocId");
                                countall(STDNonRute);

                            }



                        } catch(JSONException e){
                            e.printStackTrace();

                        }
                    }

                    private void countall(String stdNonRute) {
                        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_List_Dalam_Pelanggan?surat_tugas=" + stdNonRute,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        pDialog.dismissWithAnimation();

                                        try {
                                            int number2 = 0;


                                            JSONObject obj = new JSONObject(response);
                                            final JSONArray movieArray = obj.getJSONArray("data");
                                            for (int i = 0; i < movieArray.length(); i++) {

                                                number2++;
                                                tablayout.getTabAt(4).getOrCreateBadge().setNumber(number2);


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
                        RequestQueue requestQueue = Volley.newRequestQueue(summary_penjualan.this);
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

        stringRequest.setRetryPolicy(
                new DefaultRetryPolicy(
                        500000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                ));
        RequestQueue requestQueue = Volley.newRequestQueue(summary_penjualan.this);
        requestQueue.add(stringRequest);
    }

    private void count() {
        String link;
        link = "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_List_Dalam_Pelanggan?surat_tugas=" + STD;

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

    private void ListLuarPelanggan() {
        pDialog = new SweetAlertDialog(summary_penjualan.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Harap Menunggu");
        pDialog.setCancelable(false);
        pDialog.show();
        adapter.clear();
        adapter2 = new ListViewAdapterDaftarLuarPelanggan(dataPelangganLuarRutePojos, getApplicationContext());
        list_summary.setAdapter(adapter2);
        adapter2.clear();
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String nik_baru = sharedPreferences.getString("szDocCall", null);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_Canvas_Pelanggan_Luar?surat_tugas=" + STD + "&szEmployeeId=" + nik_baru,
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
                                        movieObject.getString("bsuccess"),
                                        movieObject.getString("bScanBarcode"),
                                        movieObject.getString("bPostPone"),
                                        movieObject.getString("szDocDO"));
                                if(movieObject.getString("bPostPone").equals("1") || movieObject.getString("bsuccess").equals("1")){
                                    dataPelangganLuarRutePojos.add(movieItem);
                                    adapter2.notifyDataSetChanged();
                                }






                                caripelanggan.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                                    @Override
                                    public boolean onQueryTextSubmit(String text) {
                                        return false;
                                    }

                                    @Override
                                    public boolean onQueryTextChange(String newText) {
                                        adapter2.getFilter().filter(newText);
                                        return true;
                                    }
                                });

                                adapter2.notifyDataSetChanged();

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

    private void ListDatapelangganDalamRute(String s) {
        pDialog = new SweetAlertDialog(summary_penjualan.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Harap Menunggu");
        pDialog.setCancelable(false);
        pDialog.show();
        adapter = new ListViewAdapterDaftarDalamPelanggan(dataPelangganDalamRutePojos, getApplicationContext());
        list_summary.setAdapter(adapter);
        adapter.clear();

        String link;

        if(s.equals("0")){
            link = "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_List_Dalam_Pelanggan_belum?surat_tugas=" + STD;
        } else {
            link = "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_List_Dalam_Pelanggan_Baru?surat_tugas=" + STD;
        }
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

                                final data_pelanggan_dalam_rute_pojo movieItem = new data_pelanggan_dalam_rute_pojo(
                                        movieObject.getString("szCustomerId"),
                                        movieObject.getString("szName").toUpperCase(),
                                        movieObject.getString("intItemNumber"),
                                        movieObject.getString("szAddress"),
                                        movieObject.getString("szLatitude"),
                                        movieObject.getString("szLongitude"),
                                        movieObject.getString("bStarted"),
                                        movieObject.getString("bFinisihed"),
                                        movieObject.getString("bScanBarcode"),
                                        movieObject.getString("bPostPone"),
                                        movieObject.getString("szRefDocId"),
                                        movieObject.getString("szFailReason"),
                                        movieObject.getString("szDocDO"),
                                        customer_awal,
                                        nama_tokoalih,
                                        alamatalih,
                                        movieObject.getString("szStatusPending"));

                                dataPelangganDalamRutePojos.add(movieItem);

                                if(movieObject.getString("bPostPone").equals("0") && movieObject.getString("bsuccess").equals("0") && movieObject.getString("bFinisihed").equals("0")) {
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



                                if(s.equals("0")){
                                    if(!movieObject.getString("bPostPone").equals("0") || !movieObject.getString("bFinisihed").equals("0")) {
                                        dataPelangganDalamRutePojos.remove(movieItem);
                                        adapter.notifyDataSetChanged();
                                    }
                                } else if(s.equals("1")){
                                    if(!movieObject.getString("bPostPone").equals("1")) {
                                        dataPelangganDalamRutePojos.remove(movieItem);
                                        adapter.notifyDataSetChanged();
                                    }
                                } else if(s.equals("2")){
                                    if(!(movieObject.getString("bFinisihed").equals("1") && movieObject.getString("bPostPone").equals("0"))) {
                                        dataPelangganDalamRutePojos.remove(movieItem);
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
            MaterialCardView alihcustomer = listViewItem.findViewById(R.id.alihcustomer);
            LinearLayout linearalihcustomer = listViewItem.findViewById(R.id.linearalihcustomer);
            TextView std = listViewItem.findViewById(R.id.std);

            TextView namatokoalih = listViewItem.findViewById(R.id.namatokoalih);
            TextView alamatalih = listViewItem.findViewById(R.id.alamatalih);
            TextView stdalih = listViewItem.findViewById(R.id.stdalih);



            namatoko.setText(dataPelangganDalamRutePojosFiltered.get(position).getSzName());
            alamat.setText(dataPelangganDalamRutePojosFiltered.get(position).getSzAddress());
            std.setText(dataPelangganDalamRutePojosFiltered.get(position).getSzCustomerId());

            if(!dataPelangganDalamRutePojosFiltered.get(position).getAlamatalih().equals("null")){
                linearalihcustomer.setVisibility(View.VISIBLE);
                namatokoalih.setText(dataPelangganDalamRutePojosFiltered.get(position).getNama_tokoalih());
                alamatalih.setText(dataPelangganDalamRutePojosFiltered.get(position).getAlamatalih());
                stdalih.setText(dataPelangganDalamRutePojosFiltered.get(position).getSzCustomerId_After());
            }

            if(dataPelangganDalamRutePojosFiltered.get(position).getbPostPone().equals("1")){
                status.setText("Ditunda");
                warna.setCardBackgroundColor(Color.parseColor("#A2C21D"));
            } else if(dataPelangganDalamRutePojosFiltered.get(position).getBsuccess().equals("1")){
                status.setText("Selesai");
                warna.setCardBackgroundColor(Color.parseColor("#1EB547"));
            } else if(tablayout.getSelectedTabPosition() == 1){
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
                            if(itemsModel.getSzName().contains(searchStr) || itemsModel.getSzCustomerId().contains(searchStr) || itemsModel.getSzCustomerId_After().contains(searchStr) || itemsModel.getNama_tokoalih().contains(searchStr)){
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

            if(dataPelangganLuarRutePojosFiltered.get(position).getbPostPone().equals("1")){
                status.setText("Ditunda");
                warna.setCardBackgroundColor(Color.parseColor("#A2C21D"));
            } else if(dataPelangganLuarRutePojosFiltered.get(position).getBsuccess().equals("1")){
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
        if(angka == 0){
            ListDatapelangganDalamRute("0");
        } else if (angka == 1){
            ListDatapelangganDalamRute("1");
        } else if (angka == 2){
            ListDatapelangganDalamRute("2");
        } else if (angka == 3){
            ListLuarPelanggan();
        } else {
            StdNonRute();
        }


    }

    private void StdNonRute() {
        pDialog = new SweetAlertDialog(summary_penjualan.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Harap Menunggu");
        pDialog.setCancelable(false);
        pDialog.show();
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String szEmployeeId = sharedPreferences.getString("szDocCall", null);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_SuratTugasNonRute?szEmployeeId=" + szEmployeeId, //szDocId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        pDialog.dismissWithAnimation();

                        try {
                            int number = 0;
                            JSONObject obj = new JSONObject(response);
                            if (obj.getString("status").equals("true")) {
                                final JSONArray movieArray = obj.getJSONArray("data");
                                final JSONObject movieObject = movieArray.getJSONObject(0);

                                String STDNonRute = movieObject.getString("szDocId");
                                ListNonRute(STDNonRute);

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
                        new SweetAlertDialog(summary_penjualan.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Maaf, surat tugas DO belum tersedia")
                                .setConfirmText("OK")
                                .show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(summary_penjualan.this);
        requestQueue.add(stringRequest);

    }

    private void ListNonRute(String STDNonRute) {
        adapter = new ListViewAdapterDaftarDalamPelanggan(dataPelangganDalamRutePojos, getApplicationContext());
        list_summary.setAdapter(adapter);
        adapter.clear();


        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_List_Dalam_Pelanggan?surat_tugas=" + STDNonRute,
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
                                final data_pelanggan_dalam_rute_pojo movieItem = new data_pelanggan_dalam_rute_pojo(
                                        movieObject.getString("szCustomerId"),
                                        movieObject.getString("szName").toUpperCase(),
                                        movieObject.getString("intItemNumber"),
                                        movieObject.getString("szAddress"),
                                        movieObject.getString("szLatitude"),
                                        movieObject.getString("szLongitude"),
                                        movieObject.getString("bStarted"),
                                        movieObject.getString("bFinisihed"),
                                        movieObject.getString("bScanBarcode"),
                                        movieObject.getString("bPostPone"),
                                        movieObject.getString("szRefDocId"),
                                        movieObject.getString("szFailReason"),
                                        movieObject.getString("szDocDO"),
                                       "null",
                                        "null",
                                        "null",
                                        "");

                                dataPelangganDalamRutePojos.add(movieItem);


                                    number2++;
                                    tablayout.getTabAt(4).getOrCreateBadge().setNumber(number2);



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

    private void countLuarRute() {
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String nik_baru = sharedPreferences.getString("szDocCall", null);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_Canvas_Pelanggan_Luar?surat_tugas=" + STD + "&szEmployeeId=" + nik_baru,
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

                                if(movieObject.getString("bPostPone").equals("1") || movieObject.getString("bsuccess").equals("1")){
                                    number++;
                                    tablayout.getTabAt(3).getOrCreateBadge().setNumber(number);
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


}