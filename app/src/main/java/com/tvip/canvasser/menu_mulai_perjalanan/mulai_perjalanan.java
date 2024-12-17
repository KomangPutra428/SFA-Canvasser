package com.tvip.canvasser.menu_mulai_perjalanan;

import static com.tvip.canvasser.menu_utama.MainActivity.latitude;
import static com.tvip.canvasser.menu_utama.MainActivity.longitude;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dantsu.escposprinter.EscPosPrinter;
import com.dantsu.escposprinter.connection.bluetooth.BluetoothConnection;
import com.dantsu.escposprinter.connection.bluetooth.BluetoothPrintersConnections;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.tvip.canvasser.Perangkat.HttpsTrustManager;
import com.tvip.canvasser.R;
import com.tvip.canvasser.driver_canvaser.mCanvaser_Terima_Produk;
import com.tvip.canvasser.menu_persiapan.outlet_kritis;
import com.tvip.canvasser.menu_utama.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class mulai_perjalanan extends AppCompatActivity {
    MaterialCardView dalamrute, pelanggancanvas, summarykunjungan, laporanpenjualan, pelanggan_canvaser,
    pelanggan_canvaser_luar, outletkritis, pelanggannonrute;
    public static String pelanggan;
    SharedPreferences sharedPreferences;
    static String id_pelanggan;
    static String szVehicleId, szHelper1;
    public static String STD, StatusBarcode;
    SweetAlertDialog pDialog;
    private RequestQueue requestQueue32;

    String PaymentTerm, bAllowToCredit;

    String intitemnumber, szCustomerId2;


    ArrayList<String> DOs = new ArrayList<>();

    AutoCompleteTextView act_pilih_do;

    private RequestQueue requestQueue2, requestQueue7;


    String customer;

    ArrayList<String> pilih_ritase = new ArrayList<>();

    public static final int PERMISSION_BLUETOOTH = 1;

    private final Locale locale = new Locale("id", "ID");
    private final DateFormat df = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss", locale);
    private final NumberFormat nf = NumberFormat.getCurrencyInstance(locale);

    public static String condition;
    String idcustomer, idstd, string_ritase, string_employeeid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mulai_perjalanan);
        HttpsTrustManager.allowAllSSL();

        dalamrute = findViewById(R.id.dalamrute);
        pelanggancanvas = findViewById(R.id.bt_pelanggan_canvas);
        summarykunjungan = findViewById(R.id.summarykunjungan);
        laporanpenjualan = findViewById(R.id.laporanpenjualan);
        pelanggan_canvaser = findViewById(R.id.pelangganbaru);
        pelanggan_canvaser_luar = findViewById(R.id.pelangganbaruluar);
        outletkritis = findViewById(R.id.outletkritis);
        pelanggannonrute = findViewById(R.id.pelanggannonrute);

        condition = "NoaddNewDo";
        getStd();


        System.out.println("longitude = " + MainActivity.longitude);
        System.out.println("latitude = " + MainActivity.latitude);

        outletkritis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), outlet_kritis.class);
                startActivity(intent);
            }
        });

        laporanpenjualan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), laporan_penjualan.class);
                startActivity(intent);
            }
        });

        summarykunjungan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog = new SweetAlertDialog(mulai_perjalanan.this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("Harap Menunggu");
                pDialog.setCancelable(false);
                pDialog.show();
                sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                String szEmployeeId = sharedPreferences.getString("szDocCall", null);
                StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Persiapan/index_List_Surat_Tugas_canvaser?szEmployeeId=" + szEmployeeId, //szDocId,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                pDialog.dismissWithAnimation();

                                try {
                                    int number = 0;
                                    JSONObject obj = new JSONObject(response);
                                    if (obj.getString("status").equals("true")) {
                                        final JSONArray movieArray = obj.getJSONArray("data");
                                        final JSONObject movieObject = movieArray.getJSONObject(movieArray.length()-1);
                                        STD = movieObject.getString("szDocId");
                                        Intent intent = new Intent(getApplicationContext(), summary_penjualan.class);
                                        startActivity(intent);
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
                                STD = "null";
                                Intent intent = new Intent(getApplicationContext(), summary_penjualan.class);
                                startActivity(intent);
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
                RequestQueue requestQueue = Volley.newRequestQueue(mulai_perjalanan.this);
                requestQueue.add(stringRequest);

            }
        });

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


                                id_pelanggan = movieObject.getString("szDocId");

                                szHelper1 = movieObject.getString("szHelper1");
                                szVehicleId = movieObject.getString("szVehicleId");



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
        RequestQueue requestQueue = Volley.newRequestQueue(mulai_perjalanan.this);
        requestQueue.add(stringRequest);

        //ini untuk delivery bisa nganvas
        pelanggancanvas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent dalam_rute = new Intent(getBaseContext(), mSurat_Tugas_Pelanggan_Canvaser.class);
                startActivity(dalam_rute);

            }
        });

        //ini untuk delievery
        dalamrute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog = new SweetAlertDialog(mulai_perjalanan.this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("Harap Menunggu");
                pDialog.setCancelable(false);
                pDialog.show();
                sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                String szEmployeeId = sharedPreferences.getString("szDocCall", null);
                StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Persiapan/index_List_Surat_Tugas_canvaser?szEmployeeId=" + szEmployeeId, //szDocId,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                pDialog.dismissWithAnimation();

                                try {
                                    int number = 0;
                                    JSONObject obj = new JSONObject(response);
                                    if (obj.getString("status").equals("true")) {
                                        final JSONArray movieArray = obj.getJSONArray("data");
                                        final JSONObject movieObject = movieArray.getJSONObject(movieArray.length()-1);
                                        STD = movieObject.getString("szDocId");
                                        pelanggan = "dalam";
                                        IntentIntegrator intentIntegrator = new IntentIntegrator(mulai_perjalanan.this);
                                        intentIntegrator.initiateScan();
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
                                if (error instanceof TimeoutError || error instanceof NoConnectionError)     {
                                    Toast.makeText(mulai_perjalanan.this, "Terjadi kesalahan saat memuat list Surat Tugas", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(mulai_perjalanan.this, "Surat Tugas Tidak Ada", Toast.LENGTH_SHORT).show();
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
                };

                stringRequest.setRetryPolicy(
                        new DefaultRetryPolicy(
                                500000,
                                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                        ));
                RequestQueue requestQueue = Volley.newRequestQueue(mulai_perjalanan.this);
                requestQueue.add(stringRequest);


            }
        });

        //ini unutk canvaser
        pelanggan_canvaser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.latitude == null){
                    new SweetAlertDialog(mulai_perjalanan.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Lokasi belum ditemukan")
                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                } else {
                    pDialog = new SweetAlertDialog(mulai_perjalanan.this, SweetAlertDialog.PROGRESS_TYPE);
                    pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                    pDialog.setTitleText("Harap Menunggu");
                    pDialog.setCancelable(false);
                    pDialog.show();
                    sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                    String szEmployeeId = sharedPreferences.getString("szDocCall", null);
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Persiapan/index_List_Surat_Tugas_canvaser?szEmployeeId=" + szEmployeeId, //szDocId,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    pDialog.dismissWithAnimation();

                                    try {
                                        int number = 0;
                                        JSONObject obj = new JSONObject(response);
                                        if (obj.getString("status").equals("true")) {
                                            final JSONArray movieArray = obj.getJSONArray("data");
                                            final JSONObject movieObject = movieArray.getJSONObject(movieArray.length()-1);
                                            STD = movieObject.getString("szDocId");
                                            pelanggan = "canvaser";
                                            IntentIntegrator intentIntegrator = new IntentIntegrator(mulai_perjalanan.this);
                                            intentIntegrator.initiateScan();
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
                                    if (error instanceof TimeoutError || error instanceof NoConnectionError)     {
                                        Toast.makeText(mulai_perjalanan.this, "Terjadi kesalahan saat memuat list Surat Tugas", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(mulai_perjalanan.this, "Surat Tugas Tidak Ada", Toast.LENGTH_SHORT).show();
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
                    };

                    stringRequest.setRetryPolicy(
                            new DefaultRetryPolicy(
                                    500000,
                                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                            ));
                    RequestQueue requestQueue = Volley.newRequestQueue(mulai_perjalanan.this);
                    requestQueue.add(stringRequest);

                }

            }
        });

        pelanggan_canvaser_luar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.latitude == null){
                    new SweetAlertDialog(mulai_perjalanan.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Lokasi belum ditemukan")
                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                } else {
                    pDialog = new SweetAlertDialog(mulai_perjalanan.this, SweetAlertDialog.PROGRESS_TYPE);
                    pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                    pDialog.setTitleText("Harap Menunggu");
                    pDialog.setCancelable(false);
                    pDialog.show();
                    sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                    String szEmployeeId = sharedPreferences.getString("szDocCall", null);
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Persiapan/index_List_Surat_Tugas_canvaser?szEmployeeId=" + szEmployeeId, //szDocId,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    try {
                                        int number = 0;
                                        JSONObject obj = new JSONObject(response);
                                        if (obj.getString("status").equals("true")) {
                                            final JSONArray movieArray = obj.getJSONArray("data");
                                            final JSONObject movieObject = movieArray.getJSONObject(movieArray.length()-1);
                                            STD = movieObject.getString("szDocId");
                                            cekDalamRute();
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
                                    if (error instanceof TimeoutError || error instanceof NoConnectionError)     {
                                        Toast.makeText(mulai_perjalanan.this, "Terjadi kesalahan saat memuat list Surat Tugas", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(mulai_perjalanan.this, "Surat Tugas Tidak Ada", Toast.LENGTH_SHORT).show();
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
                    };

                    stringRequest.setRetryPolicy(
                            new DefaultRetryPolicy(
                                    500000,
                                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                            ));
                    if (requestQueue2 == null) {
                        requestQueue2 = Volley.newRequestQueue(mulai_perjalanan.this);
                        requestQueue2.add(stringRequest);
                    } else {
                        requestQueue2.add(stringRequest);
                    }


                }

            }
        });

        pelanggannonrute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                String nik_baru = sharedPreferences.getString("szDocCall", null);

                if(MainActivity.latitude == null){
                    new SweetAlertDialog(mulai_perjalanan.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Lokasi belum ditemukan")
                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                } else {
                    pDialog = new SweetAlertDialog(mulai_perjalanan.this, SweetAlertDialog.PROGRESS_TYPE);
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

                                            if(movieObject.getString("bStarted").equals("0")){
                                                new SweetAlertDialog(mulai_perjalanan.this, SweetAlertDialog.WARNING_TYPE)
                                                        .setContentText("Silahkan isi KM awal terlebih dahulu")
                                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                            @Override
                                                            public void onClick(SweetAlertDialog sDialog) {
                                                                sDialog.dismissWithAnimation();
                                                            }
                                                        })
                                                        .show();
                                            } else if(movieObject.getString("bFinished").equals("1")){
                                                new SweetAlertDialog(mulai_perjalanan.this, SweetAlertDialog.WARNING_TYPE)
                                                        .setContentText("Surat Tugas Sudah Selesai Dijalankan")
                                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                            @Override
                                                            public void onClick(SweetAlertDialog sDialog) {
                                                                sDialog.dismissWithAnimation();
                                                            }
                                                        })
                                                        .show();
                                            } else {
                                                STD = movieObject.getString("szDocId");
                                                pelanggan = "non_rute";
                                                IntentIntegrator intentIntegrator = new IntentIntegrator(mulai_perjalanan.this);
                                                intentIntegrator.initiateScan();
                                            }


                                        }



                                    } catch(JSONException e){
                                        e.printStackTrace();

                                    }
                                }

                                private void getDO() {
                                    StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Persiapan/index_List_Surat_Tugas_canvaser?szEmployeeId=" + szEmployeeId, //szDocId,
                                            new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {

                                                    try {
                                                        int number = 0;
                                                        JSONObject obj = new JSONObject(response);
                                                        if (obj.getString("status").equals("true")) {
                                                            final JSONArray movieArray = obj.getJSONArray("data");
                                                            final JSONObject movieObject = movieArray.getJSONObject(movieArray.length()-1);
                                                            pDialog.dismissWithAnimation();
                                                            pelanggan = "non_rute";
                                                            Intent intent = new Intent(getApplicationContext(), mCanvaser_Terima_Produk.class);
                                                            intent.putExtra("DOCDO", movieObject.getString("szDocDO"));
                                                            intent.putExtra("Status", "Failed");

                                                            startActivity(intent);

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
                                                    new SweetAlertDialog(mulai_perjalanan.this, SweetAlertDialog.WARNING_TYPE)
                                                            .setContentText("DO Sudah Habis")
                                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                @Override
                                                                public void onClick(SweetAlertDialog sDialog) {
                                                                    sDialog.dismissWithAnimation();
                                                                }
                                                            })
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
                                    RequestQueue requestQueue = Volley.newRequestQueue(mulai_perjalanan.this);
                                    requestQueue.add(stringRequest);
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    pDialog.dismissWithAnimation();
                                    new SweetAlertDialog(mulai_perjalanan.this, SweetAlertDialog.WARNING_TYPE)
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
                    RequestQueue requestQueue = Volley.newRequestQueue(mulai_perjalanan.this);
                    requestQueue.add(stringRequest);

                }

            }
        });

    }

    private void cekDalamRute() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_CekDalam?szDocId=" + STD, //szDocId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                        String nik_baru = sharedPreferences.getString("szDocCall", null);
                        if(nik_baru.contains("RP")){
                            pDialog.dismissWithAnimation();
                            pelanggan = "canvaser_luar";
                            IntentIntegrator intentIntegrator = new IntentIntegrator(mulai_perjalanan.this);
                            intentIntegrator.initiateScan();
                        } else if(nik_baru.contains("SPV")){
                            pDialog.dismissWithAnimation();
                            pelanggan = "canvaser_luar";
                            IntentIntegrator intentIntegrator = new IntentIntegrator(mulai_perjalanan.this);
                            intentIntegrator.initiateScan();
                        } else {
                            pDialog.dismissWithAnimation();
                            new SweetAlertDialog(mulai_perjalanan.this, SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("Fitur ini tidak bisa digunakan, silahkan gunakan fitur Alih Customer")
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
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                        String nik_baru = sharedPreferences.getString("szDocCall", null);
                        if(nik_baru.contains("RP")){
                            pDialog.dismissWithAnimation();
                            pelanggan = "canvaser_luar";
                            IntentIntegrator intentIntegrator = new IntentIntegrator(mulai_perjalanan.this);
                            intentIntegrator.initiateScan();
                        } else if(nik_baru.contains("SPV")){
                            pDialog.dismissWithAnimation();
                            pelanggan = "canvaser_luar";
                            IntentIntegrator intentIntegrator = new IntentIntegrator(mulai_perjalanan.this);
                            intentIntegrator.initiateScan();
                        } else {
                            pDialog.dismissWithAnimation();
                            new SweetAlertDialog(mulai_perjalanan.this, SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("Fitur ini tidak bisa digunakan, silahkan gunakan fitur Alih Customer")
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
        RequestQueue requestQueue = Volley.newRequestQueue(mulai_perjalanan.this);
        requestQueue.add(stringRequest);
    }


    private void getStd() {

    }

    @Override
    protected void onResume() {
        super.onResume();

        System.out.println("longitude = " + MainActivity.longitude);
        System.out.println("latitude = " + MainActivity.latitude);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Hasil tidak ditemukan", Toast.LENGTH_SHORT).show();

            } else {

                try {
                    JSONObject object = new JSONObject(result.getContents());
                    System.out.println(object.getString("nik"));


                } catch (JSONException e) {

                    e.printStackTrace();
                    pDialog = new SweetAlertDialog(mulai_perjalanan.this, SweetAlertDialog.PROGRESS_TYPE);
                    pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                    pDialog.setTitleText("Harap Menunggu");
                    pDialog.setCancelable(false);
                    pDialog.show();

                    //JIKA PELANGGAN DALAM RUTE,
                    //pelanggan.equals("dalam") berasal ketika menekan button dalam rute
                    if (pelanggan.equals("dalam")) {

                        StringRequest rest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_Scan_Dalam_Pelanggan_Driver?szCustomerId="+result.getContents(),
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            if (jsonObject.getString("status").equals("true")) {

                                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                                for (int i = 0; i < 1; i++) {
                                                    pDialog.dismissWithAnimation();
                                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                                    if(jsonObject1.getString("bStarted").equals("1") && jsonObject1.getString("bFinisihed").equals("1")){

                                                        final Dialog dialog = new Dialog(mulai_perjalanan.this);
                                                        dialog.setContentView(R.layout.pilih_ritase_barcode);
                                                        dialog.show();

                                                        final AutoCompleteTextView editpilihalasan = dialog.findViewById(R.id.editpilihalasan);
                                                        Button tidak = dialog.findViewById(R.id.tidak);
                                                        Button ya = dialog.findViewById(R.id.ya);
                                                        TextView nama_toko = dialog.findViewById(R.id.dialog_nama_toko);
                                                        TextView alamat_toko = dialog.findViewById(R.id.dialog_alamat_toko);

                                                        StringRequest rest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_Scan_Dalam_Pelanggan_Driver?szCustomerId="+result.getContents(),
                                                                new Response.Listener<String>() {
                                                                    @Override
                                                                    public void onResponse(String response) {
                                                                        try {
                                                                            JSONObject jsonObject = new JSONObject(response);
                                                                            if (jsonObject.getString("status").equals("true")) {
                                                                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                                                                for (int i = 0; i < jsonArray.length(); i++) {
                                                                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                                                                    String ritase = jsonObject1.getString("ritase");

                                                                                    String string_nama_toko = jsonObject1.getString("szName");
                                                                                    String string_alamat_toko = jsonObject1.getString("szAddress");

                                                                                    nama_toko.setText(string_nama_toko);
                                                                                    alamat_toko.setText(string_alamat_toko);

                                                                                    pilih_ritase.add(ritase);

                                                                                    Toast.makeText(mulai_perjalanan.this, "Ritase berhasil dimuat", Toast.LENGTH_SHORT).show();

                                                                                }
                                                                            }
                                                                            editpilihalasan.setAdapter(new ArrayAdapter<String>(mulai_perjalanan.this, android.R.layout.simple_expandable_list_item_1, pilih_ritase));
                                                                        } catch (JSONException e) {
                                                                            e.printStackTrace();
                                                                        }
                                                                    }
                                                                },
                                                                new Response.ErrorListener() {
                                                                    @Override
                                                                    public void onErrorResponse(VolleyError error) {
                                                                        Toast.makeText(mulai_perjalanan.this, "Terjadi kesalahan pada saat memuat jenis alasan", Toast.LENGTH_SHORT).show();
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
                                                        RequestQueue requestkota = Volley.newRequestQueue(mulai_perjalanan.this);
                                                        requestkota.add(rest);

                                                        //JIKA MENEKAN BUTTON BATAL
                                                        tidak.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                dialog.dismiss();
                                                                finish();
                                                            }
                                                        });

                                                        //JIKA MENEKAN BUTTON YA
                                                        ya.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                ya.setText("Loading...");

                                                                Toast.makeText(mulai_perjalanan.this, editpilihalasan.getText().toString(), Toast.LENGTH_SHORT).show();

                                                                StringRequest rest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_Scan_Ritase_Dalam_Pelanggan_Driver?szCustomerId="+result.getContents()+"&ritase="+editpilihalasan.getText().toString(),
                                                                        new Response.Listener<String>() {
                                                                            @Override
                                                                            public void onResponse(String response) {
                                                                                ya.setText("Ya");

                                                                                try {
                                                                                    JSONObject jsonObject = new JSONObject(response);
                                                                                    if (jsonObject.getString("status").equals("true")) {
                                                                                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                                                                                        for (int i = 0; i < jsonArray.length(); i++) {

                                                                                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);


                                                                                            //JIKA bStaretd & bfinshed yang berada di doccallitem bernilai 1 maka toko tersebut sudah selesai dikunjungi
                                                                                            if (jsonObject1.getString("bFinisihed").equals("1")){


                                                                                                TextInputLayout textinput_pilihritase = (TextInputLayout) dialog.findViewById(R.id.textinput_pilih_ritase);
                                                                                                textinput_pilihritase.setError("Pelanggan sudah selesai dikunjungi");

                                                                                                //selain ituu maka user akan dialihkan menuju menu pelnaggaan
                                                                                            }

                                                                                            if (jsonObject1.getString("bFinisihed").equals("0")){

                                                                                                idcustomer = jsonObject1.getString("szCustomerId");
                                                                                                idstd = jsonObject1.getString("szDocId");

                                                                                                Intent ok = new Intent(getBaseContext(), menu_pelanggan.class);

                                                                                                ok.putExtra("kode", idcustomer);
                                                                                                ok.putExtra("idStd", idstd);
                                                                                                startActivity(ok);

                                                                                                dialog.dismiss();

                                                                                                updatebScanBarcode(idcustomer);
                                                                                            }

                                                                                            Toast.makeText(mulai_perjalanan.this, "Ritase berhasil dimuat", Toast.LENGTH_SHORT).show();

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
                                                                                Toast.makeText(mulai_perjalanan.this, "Terjadi kesalahan pada saat memuat jenis alasan", Toast.LENGTH_SHORT).show();
                                                                                ya.setText("Ya");
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
                                                                RequestQueue requestkota = Volley.newRequestQueue(mulai_perjalanan.this);
                                                                requestkota.add(rest);
                                                            }
                                                        });

                                                    } else {

                                                        idcustomer = jsonObject1.getString("szCustomerId");
                                                        idstd = jsonObject1.getString("szDocId");

                                                        Intent ok = new Intent(getBaseContext(), menu_pelanggan.class);

                                                        ok.putExtra("kode", idcustomer);
                                                        ok.putExtra("idStd", idstd);
                                                        startActivity(ok);
                                                        updatebScanBarcode(idcustomer);
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

                                        //JIKA SCAN TIDAK SESUAI MAKA AKAN MASUK KE MENU YANG BERISI KUMPULAN LIST TOKO2

                                        pDialog.dismissWithAnimation();
                                        Intent dalam_rute = new Intent(getBaseContext(), scangagal_dalamrute.class);
                                        startActivity(dalam_rute);

                                        //---------------------------------------------------------------------------------------

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
                        RequestQueue requestkota = Volley.newRequestQueue(mulai_perjalanan.this);
                        requestkota.add(rest);

                        //JIKA PELANGGAN DILUAR RUTE

                    } else if (pelanggan.equals("luar")){
                        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                        String nik_baru = sharedPreferences.getString("szDocCall", null);
                        StringRequest rest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_List_Luar_Pelanggan_detail?surat_tugas="+STD+"&szId="+nik_baru+"&szCustomerId="+result.getContents(),
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            if (jsonObject.getString("status").equals("true")) {

                                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                                for (int i = 0; i < 1; i++) {
                                                    pDialog.dismissWithAnimation();
                                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                                    idcustomer = jsonObject1.getString("szCustomerId");
                                                    idstd = jsonObject1.getString("szDocId");

                                                    Intent ok = new Intent(getBaseContext(), menu_pelanggan.class);

                                                    ok.putExtra("kode", idcustomer);
                                                    ok.putExtra("idStd", STD);
                                                    startActivity(ok);

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


                                        pDialog.dismissWithAnimation();
                                        Intent surat_tugas_canvaser = new Intent(getBaseContext(), mSurat_Tugas_Pelanggan_Canvaser.class);
                                        startActivity(surat_tugas_canvaser);

                                        //---------------------------------------------------------------------------------------

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
                        RequestQueue requestkota = Volley.newRequestQueue(mulai_perjalanan.this);
                        requestkota.add(rest);

                        //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

                    } else if(pelanggan.equals("canvaser")) {
                        StringRequest rest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_Comparing_Pelanggan?surat_tugas="+STD+"&szCustomerId=" + result.getContents(),
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            if (jsonObject.getString("status").equals("true")) {
                                                JSONArray jsonArray = jsonObject.getJSONArray("data");

                                                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                                                    pDialog.dismissWithAnimation();

                                                    if(jsonObject1.getString("bFinisihed").equals("1") && jsonObject1.getString("bsuccess").equals("1")){
                                                        pDialog.dismissWithAnimation();

                                                        final Dialog dialogDO = new Dialog(mulai_perjalanan.this);
                                                        dialogDO.setContentView(R.layout.pilih_do);
                                                        dialogDO.setCancelable(false);
                                                        dialogDO.show();

                                                        act_pilih_do = dialogDO.findViewById(R.id.act_pilih_do);
                                                        Button tidak = dialogDO.findViewById(R.id.tidak);
                                                        Button ya = dialogDO.findViewById(R.id.ya);
//
                                                        DOs.clear();
                                                        StringRequest rest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_Comparing_Pelanggan?surat_tugas="+STD+"&szCustomerId=" + result.getContents(),
                                                                new Response.Listener<String>() {
                                                                    @Override
                                                                    public void onResponse(String response) {

                                                                        try {
                                                                            JSONObject jsonObject = new JSONObject(response);
                                                                            if (jsonObject.getString("status").equals("true")) {
                                                                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                                                                for (int i = 0; i < jsonArray.length(); i++) {

                                                                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                                                                    if(jsonObject1.getString("bOutOfRoute").equals("0") && jsonObject1.getString("bsuccess").equals("0")){
                                                                                        DOs.add(jsonObject1.getString("szDocDO"));
                                                                                    }
                                                                                }
                                                                                act_pilih_do.setAdapter(new ArrayAdapter<String>(mulai_perjalanan.this, android.R.layout.simple_expandable_list_item_1, DOs));

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
                                                        RequestQueue requestkota = Volley.newRequestQueue(mulai_perjalanan.this);
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
                                                                    StringRequest pilih_customer = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_cekdo?szDocDO=" + act_pilih_do.getText().toString(),

                                                                            new Response.Listener<String>() {
                                                                                @Override
                                                                                public void onResponse(String response) {
                                                                                    try {
                                                                                        JSONObject jsonObject = new JSONObject(response);
                                                                                        if (jsonObject.getString("status").equals("true")) {

                                                                                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                                                                                            for (int i = 0; i < jsonArray.length(); i++) {

                                                                                                //parameter jika mengambul data dari url url_get_data_checklist_stagging
                                                                                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                                                                                if(jsonObject1.getString("bSuccess").equals("1")){
                                                                                                    new SweetAlertDialog(mulai_perjalanan.this, SweetAlertDialog.WARNING_TYPE)
                                                                                                            .setTitleText("Maaf, DO ini sudah dilakukan transaksi")
                                                                                                            .setConfirmText("OK")
                                                                                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                                                                @Override
                                                                                                                public void onClick(SweetAlertDialog sDialog) {
                                                                                                                    sDialog.dismissWithAnimation();
                                                                                                                }
                                                                                                            })
                                                                                                            .show();
                                                                                                } else {
                                                                                                    intitemnumber = jsonObject1.getString("intItemNumber");
                                                                                                    szCustomerId2 = jsonObject1.getString("szCustomerId");
                                                                                                    act_pilih_do.setError(null);
                                                                                                    StatusBarcode = "1";
                                                                                                    Intent intent = new Intent(mulai_perjalanan.this, menu_pelanggan.class);
                                                                                                    intent.putExtra("idStd", STD);
                                                                                                    intent.putExtra("kode", result.getContents());
                                                                                                    intent.putExtra("szDocDo", act_pilih_do.getText().toString());

                                                                                                    startActivity(intent);

                                                                                                    putBarcode(result.getContents(), act_pilih_do.getText().toString());

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
                                                                    RequestQueue requestQueue = Volley.newRequestQueue(mulai_perjalanan.this);
                                                                    requestQueue.add(pilih_customer);


                                                                }

                                                            }
                                                        });
                                                    } else if(jsonObject1.getString("bFinisihed").equals("1") && !jsonObject1.getString("szFailReason").equals("")){
                                                        pDialog.dismissWithAnimation();


                                                        final Dialog dialogDO = new Dialog(mulai_perjalanan.this);
                                                        dialogDO.setContentView(R.layout.pilih_do);
                                                        dialogDO.setCancelable(false);
                                                        dialogDO.show();

                                                        act_pilih_do = dialogDO.findViewById(R.id.act_pilih_do);
                                                        Button tidak = dialogDO.findViewById(R.id.tidak);
                                                        Button ya = dialogDO.findViewById(R.id.ya);
//
                                                        DOs.clear();
                                                        StringRequest rest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_Comparing_Pelanggan?surat_tugas="+STD+"&szCustomerId=" + result.getContents(),
                                                                new Response.Listener<String>() {
                                                                    @Override
                                                                    public void onResponse(String response) {

                                                                        try {
                                                                            JSONObject jsonObject = new JSONObject(response);
                                                                            if (jsonObject.getString("status").equals("true")) {
                                                                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                                                                for (int i = 0; i < jsonArray.length(); i++) {

                                                                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                                                                    if(jsonObject1.getString("bOutOfRoute").equals("0") && jsonObject1.getString("bsuccess").equals("0")){
                                                                                        DOs.add(jsonObject1.getString("szDocDO"));
                                                                                    }
                                                                                }
                                                                                act_pilih_do.setAdapter(new ArrayAdapter<String>(mulai_perjalanan.this, android.R.layout.simple_expandable_list_item_1, DOs));

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
                                                        RequestQueue requestkota = Volley.newRequestQueue(mulai_perjalanan.this);
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
                                                                    StringRequest pilih_customer = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_cekdo?szDocDO=" + act_pilih_do.getText().toString(),

                                                                            new Response.Listener<String>() {
                                                                                @Override
                                                                                public void onResponse(String response) {
                                                                                    try {
                                                                                        JSONObject jsonObject = new JSONObject(response);
                                                                                        if (jsonObject.getString("status").equals("true")) {

                                                                                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                                                                                            for (int i = 0; i < jsonArray.length(); i++) {

                                                                                                //parameter jika mengambul data dari url url_get_data_checklist_stagging
                                                                                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                                                                                if(jsonObject1.getString("bSuccess").equals("1")){
                                                                                                    new SweetAlertDialog(mulai_perjalanan.this, SweetAlertDialog.WARNING_TYPE)
                                                                                                            .setTitleText("Maaf, DO ini sudah dilakukan transaksi")
                                                                                                            .setConfirmText("OK")
                                                                                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                                                                @Override
                                                                                                                public void onClick(SweetAlertDialog sDialog) {
                                                                                                                    sDialog.dismissWithAnimation();
                                                                                                                }
                                                                                                            })
                                                                                                            .show();
                                                                                                } else {
                                                                                                    intitemnumber = jsonObject1.getString("intItemNumber");
                                                                                                    szCustomerId2 = jsonObject1.getString("szCustomerId");
                                                                                                    act_pilih_do.setError(null);
                                                                                                    StatusBarcode = "1";
                                                                                                    Intent intent = new Intent(mulai_perjalanan.this, menu_pelanggan.class);
                                                                                                    intent.putExtra("idStd", STD);
                                                                                                    intent.putExtra("kode", result.getContents());
                                                                                                    intent.putExtra("szDocDo", act_pilih_do.getText().toString());

                                                                                                    startActivity(intent);

                                                                                                    putBarcode(result.getContents(), act_pilih_do.getText().toString());

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
                                                                    RequestQueue requestQueue = Volley.newRequestQueue(mulai_perjalanan.this);
                                                                    requestQueue.add(pilih_customer);


                                                                }

                                                            }
                                                        });

                                                    } else {
                                                        pDialog.dismissWithAnimation();


                                                        final Dialog dialogDO = new Dialog(mulai_perjalanan.this);
                                                        dialogDO.setContentView(R.layout.pilih_do);
                                                        dialogDO.setCancelable(false);
                                                        dialogDO.show();


                                                        act_pilih_do = dialogDO.findViewById(R.id.act_pilih_do);
                                                        Button tidak = dialogDO.findViewById(R.id.tidak);
                                                        Button ya = dialogDO.findViewById(R.id.ya);
//
                                                        DOs.clear();
                                                        StringRequest rest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_Comparing_Pelanggan?surat_tugas="+STD+"&szCustomerId=" + result.getContents(),
                                                                new Response.Listener<String>() {
                                                                    @Override
                                                                    public void onResponse(String response) {

                                                                        try {
                                                                            JSONObject jsonObject = new JSONObject(response);
                                                                            if (jsonObject.getString("status").equals("true")) {
                                                                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                                                                for (int i = 0; i < jsonArray.length(); i++) {

                                                                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                                                                    DOs.add(jsonObject1.getString("szDocDO"));
                                                                                }
                                                                                act_pilih_do.setAdapter(new ArrayAdapter<String>(mulai_perjalanan.this, android.R.layout.simple_expandable_list_item_1, DOs));

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
                                                        RequestQueue requestkota = Volley.newRequestQueue(mulai_perjalanan.this);
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
                                                                    StringRequest pilih_customer = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_cekdo?szDocDO=" + act_pilih_do.getText().toString(),

                                                                            new Response.Listener<String>() {
                                                                                @Override
                                                                                public void onResponse(String response) {
                                                                                    try {
                                                                                        JSONObject jsonObject = new JSONObject(response);
                                                                                        if (jsonObject.getString("status").equals("true")) {

                                                                                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                                                                                            for (int i = 0; i < jsonArray.length(); i++) {

                                                                                                //parameter jika mengambul data dari url url_get_data_checklist_stagging
                                                                                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                                                                                if(jsonObject1.getString("bSuccess").equals("1")){
                                                                                                    new SweetAlertDialog(mulai_perjalanan.this, SweetAlertDialog.WARNING_TYPE)
                                                                                                            .setTitleText("Maaf, DO ini sudah dilakukan transaksi")
                                                                                                            .setConfirmText("OK")
                                                                                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                                                                @Override
                                                                                                                public void onClick(SweetAlertDialog sDialog) {
                                                                                                                    sDialog.dismissWithAnimation();
                                                                                                                }
                                                                                                            })
                                                                                                            .show();
                                                                                                } else {
                                                                                                    intitemnumber = jsonObject1.getString("intItemNumber");
                                                                                                    szCustomerId2 = jsonObject1.getString("szCustomerId");
                                                                                                    act_pilih_do.setError(null);
                                                                                                    StatusBarcode = "1";
                                                                                                    Intent intent = new Intent(mulai_perjalanan.this, menu_pelanggan.class);
                                                                                                    intent.putExtra("idStd", STD);
                                                                                                    intent.putExtra("kode", result.getContents());
                                                                                                    intent.putExtra("szDocDo", act_pilih_do.getText().toString());

                                                                                                    startActivity(intent);

                                                                                                    putBarcode(result.getContents(), act_pilih_do.getText().toString());

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
                                                                    RequestQueue requestQueue = Volley.newRequestQueue(mulai_perjalanan.this);
                                                                    requestQueue.add(pilih_customer);


                                                                }

                                                            }
                                                        });


                                                    }



                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    private void putBarcode(String szCustomerId, String szDocDO) {
                                        StringRequest stringRequest2 = new StringRequest(Request.Method.PUT, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_ScanBarcode",
                                                new Response.Listener<String>() {

                                                    @Override
                                                    public void onResponse(String response) {
                                                        SF(intitemnumber, szCustomerId2);


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

                                                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                                String currentDateandTime2 = sdf2.format(new Date());

                                                params.put("szDocId", STD);

                                                params.put("szCustomerId", szCustomerId);
                                                params.put("szDocDO", szDocDO);

                                                params.put("dtmStart", currentDateandTime2);

                                                params.put("szLongitude", MainActivity.longitude);
                                                params.put("szLatitude", MainActivity.latitude);
                                                params.put("szReasonFailedScan", "");

                                                System.out.println(params);



                                                return params;
                                            }

                                        };
                                        stringRequest2.setRetryPolicy(
                                                new DefaultRetryPolicy(
                                                        5000,
                                                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                                                )
                                        );
                                        RequestQueue requestQueue2 = Volley.newRequestQueue(mulai_perjalanan.this);
                                        requestQueue2.add(stringRequest2);
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        pDialog.dismissWithAnimation();
                                        StatusBarcode = "0";

                                        Intent surat_tugas_canvaser = new Intent(getBaseContext(), scangagal_dalamrute.class);
                                        startActivity(surat_tugas_canvaser);

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
                        RequestQueue requestkota = Volley.newRequestQueue(mulai_perjalanan.this);
                        requestkota.add(rest);

                    } else if(pelanggan.equals("canvaser_luar")) {
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
                                                    luarrute(movieObject.getString("szEmployeeId"), result.getContents());
                                                } else {
                                                    luarrute(movieObject.getString("szBackupEmployee"), result.getContents());
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
                        RequestQueue requestQueue = Volley.newRequestQueue(mulai_perjalanan.this);
                        requestQueue.add(stringRequest);


                    } else {
                        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                        String nik_baru = sharedPreferences.getString("szDocCall", null);
                        String[] parts = nik_baru.split("-");
                        String restnomor = parts[0];
                        StringRequest channel_status = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_List_NonRute_Scan?surat_tugas="+STD+"&szSoldToBranchId=" + restnomor+"&szId=" + result.getContents(),
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        try {
                                            JSONObject obj = new JSONObject(response);
                                            final JSONArray movieArray = obj.getJSONArray("data");

                                            JSONObject biodatas = null;
                                            for (int i = 0; i < movieArray.length(); i++) {
                                                final JSONObject movieObject = movieArray.getJSONObject(i);

                                                if(movieObject.getString("bStarted").equals("null")){
                                                    getNumber(result.getContents());
                                                } else {
                                                    Intent ok = new Intent(getBaseContext(), menu_pelanggan.class);
                                                    ok.putExtra("kode", result.getContents());
                                                    ok.putExtra("szDocDo", movieObject.getString("szDocDO"));
                                                    startActivity(ok);
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
                                        pDialog.dismissWithAnimation();
                                        Intent dalam_rute = new Intent(getBaseContext(), scangagal_nonrute.class);
                                        startActivity(dalam_rute);
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
                    }
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void luarrute(String szEmployeeId, String contents) {
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String nik_baru = sharedPreferences.getString("szDocCall", null);
        StringRequest rest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_detail_Canvas_Pelanggan_Luar?surat_tugas=" + STD + "&szEmployeeId=" + szEmployeeId + "&szCustomerId=" + contents,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        customer = contents;
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("status").equals("true")) {

                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < 1; i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    String id = jsonObject1.getString("szDocDO");
                                    if(id.equals("null")){

                                        StringRequest stringRequest2 = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_Payment?szId=" + customer,
                                                new Response.Listener<String>() {

                                                    @Override
                                                    public void onResponse(String response) {

                                                        try {
                                                            JSONObject obj = new JSONObject(response);
                                                            final JSONArray movieArray = obj.getJSONArray("data");
                                                            for (int i = 0; i < movieArray.length(); i++) {
                                                                final JSONObject movieObject = movieArray.getJSONObject(i);

                                                                PaymentTerm = movieObject.getString("szPaymetTermId");
                                                                bAllowToCredit = movieObject.getString("bAllowToCredit");
                                                                getLastItem();



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
                                            requestQueue7 = Volley.newRequestQueue(mulai_perjalanan.this);
                                            requestQueue7.add(stringRequest2);
                                        } else {
                                            requestQueue7.add(stringRequest2);
                                        }
                                    } else {
                                        Intent ok = new Intent(getBaseContext(), menu_pelanggan.class);
                                        ok.putExtra("kode", contents);
                                        ok.putExtra("szDocDo", id);
                                        startActivity(ok);
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

                        //JIKA SCAN TIDAK SESUAI MAKA AKAN MASUK KE MENU YANG BERISI KUMPULAN LIST TOKO2

                        pDialog.dismissWithAnimation();
                        StatusBarcode = "0";
                        Intent surat_tugas_canvaser = new Intent(getBaseContext(), scangagal_luarrute.class);
                        startActivity(surat_tugas_canvaser);

//                                        Intent intent = new Intent(mulai_perjalanan.this, mCanvaser_Terima_Produk.class);

//                                        startActivity(intent);

                        //---------------------------------------------------------------------------------------

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
        RequestQueue requestkota = Volley.newRequestQueue(mulai_perjalanan.this);
        requestkota.add(rest);
    }


    private void getNumber(String contents) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_firstNumber?szDocId=" + STD,
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
                                updateCustomers(movieObject.getString("intItemNumber"), movieObject.getString("szDocDO"), contents);


//
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
                        new SweetAlertDialog(mulai_perjalanan.this, SweetAlertDialog.WARNING_TYPE)
                                .setContentText("DO Non-rute sudah habis")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                    }
                                })
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
        RequestQueue requestQueue = Volley.newRequestQueue(mulai_perjalanan.this);
        requestQueue.add(stringRequest);
    }

    private void updateCustomers(String intItemNumber, String szDocDO, String contents) {
        StringRequest stringRequest2 = new StringRequest(Request.Method.PUT, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_update_nonrute",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Intent ok = new Intent(getBaseContext(), menu_pelanggan.class);
                        ok.putExtra("kode", contents);
                        ok.putExtra("szDocDo", szDocDO);
                        startActivity(ok);
                        update();
                    }

                    private void update() {
                        StringRequest stringRequest2 = new StringRequest(Request.Method.PUT, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_ScanBarcode",
                                new Response.Listener<String>() {

                                    @Override
                                    public void onResponse(String response) {
                                        sfa_doccallitem(intItemNumber, szDocDO, contents);
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

                                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String currentDateandTime2 = sdf2.format(new Date());

                                params.put("szDocId", STD);

                                params.put("szCustomerId", contents);
                                params.put("szDocDO", szDocDO);

                                params.put("dtmStart", currentDateandTime2);

                                params.put("szLongitude", MainActivity.longitude);
                                params.put("szLatitude", MainActivity.latitude);
                                params.put("szReasonFailedScan", "");

                                System.out.println(params);



                                return params;
                            }

                        };
                        stringRequest2.setRetryPolicy(
                                new DefaultRetryPolicy(
                                        5000,
                                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                                )
                        );
                        RequestQueue requestQueue2 = Volley.newRequestQueue(mulai_perjalanan.this);
                        requestQueue2.add(stringRequest2);
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
                params.put("szCustomerId", contents);


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
        RequestQueue requestQueue2 = Volley.newRequestQueue(mulai_perjalanan.this);
        requestQueue2.add(stringRequest2);
    }

    private void sfa_doccallitem(String intItemNumber, String szDocDO, String contents) {
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_DocCallItem",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse response = error.networkResponse;
                if(response != null && response.data != null){

                }else{
                    String errorMessage=error.getClass().getSimpleName();
                    if(!TextUtils.isEmpty(errorMessage)){

                    }
                }
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

                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String currentDateandTime2 = sdf2.format(new Date());

                params.put("szDocId", STD);
                params.put("intItemNumber", intItemNumber);

                params.put("szCustomerId", contents);

                params.put("dtmStart", currentDateandTime2);
                params.put("dtmFinish", currentDateandTime2);

                params.put("bVisited", "1");
                params.put("bSuccess", "0");


                params.put("szFailReason", "");
                params.put("szLangitude", MainActivity.latitude);
                params.put("szLongitude", MainActivity.longitude);

                if(pelanggan.equals("canvaser_luar")){
                    params.put("bOutOfRoute", "1");
                } else {
                    params.put("bOutOfRoute", "0");
                }

                params.put("szRefDocId", szDocDO);

                params.put("bScanBarcode", "1");

                params.put("szReasonIdCheckin", "");
                params.put("szReasonFailedScan", "");
                params.put("decRadiusDiff", "0");

                System.out.println("params = " + params);
                return params;
            }

        };
        stringRequest2.setRetryPolicy(
                new DefaultRetryPolicy(
                        5000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
        );
        RequestQueue requestQueue2 = Volley.newRequestQueue(mulai_perjalanan.this);
        requestQueue2.getCache().clear();
        requestQueue2.add(stringRequest2);
    }

    private void SF(String intItemNumber, String szCustomerId) {
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_DocCallItem",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
//                        DocSOMDBA(s);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse response = error.networkResponse;
                if(response != null && response.data != null){

                }else{
                    String errorMessage=error.getClass().getSimpleName();
                    if(!TextUtils.isEmpty(errorMessage)){

                    }
                }
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




                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String currentDateandTime2 = sdf2.format(new Date());

                params.put("szDocId", STD);
                params.put("intItemNumber", intItemNumber);

                params.put("szCustomerId", szCustomerId);

                params.put("dtmStart", currentDateandTime2);
                params.put("dtmFinish", currentDateandTime2);

                params.put("bVisited", "1");
                params.put("bSuccess", "0");


                params.put("szFailReason", "");
                params.put("szLangitude", MainActivity.latitude);
                params.put("szLongitude", MainActivity.longitude);

                if(pelanggan.equals("canvaser_luar")){
                    params.put("bOutOfRoute", "1");
                } else {
                    params.put("bOutOfRoute", "0");
                }

                params.put("szRefDocId", act_pilih_do.getText().toString());

                params.put("bScanBarcode", "0");

                params.put("szReasonIdCheckin", "");
                params.put("szReasonFailedScan", "");
                params.put("decRadiusDiff", "0");








                return params;
            }

        };
        stringRequest2.setRetryPolicy(
                new DefaultRetryPolicy(
                        5000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
        );
        RequestQueue requestQueue2 = Volley.newRequestQueue(mulai_perjalanan.this);
        requestQueue2.getCache().clear();
        requestQueue2.add(stringRequest2);
    }

    private void getLastItem() {

            StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_LastItem?szDocId=" + STD, //szDocId,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {


                            try {
                                int number = 0;
                                JSONObject obj = new JSONObject(response);
                                if (obj.getString("status").equals("true")) {
                                    final JSONArray movieArray = obj.getJSONArray("data");
                                    final JSONObject movieObject = movieArray.getJSONObject(movieArray.length()-1);

                                    int lastitem = movieObject.getInt("intItemNumber") + 1;
                                    getDOBaru(String.valueOf(lastitem));



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
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void getDOBaru(String lastitem) {
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_DocDoData",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            String message = jsonObj.getString("message");
                            postDOccallItem(lastitem, message);


                        } catch (JSONException e) {
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
                sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                String nik_baru = sharedPreferences.getString("szDocCall", null);

                String[] parts = nik_baru.split("-");
                String branch = parts[0];

                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String currentDateandTime2 = sdf2.format(new Date());

                SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd");
                String currentDateandTime3 = sdf3.format(new Date());


                params.put("dtmDoc", currentDateandTime3);
                params.put("szCustomerId", customer);
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
                params.put("szMobileId", currentDateandTime3);

                params.put("szUserCreatedId", nik_baru);
                params.put("dtmCreated", currentDateandTime2);

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
        if (requestQueue32 == null) {
            requestQueue32 = Volley.newRequestQueue(mulai_perjalanan.this);
            requestQueue32.add(stringRequest2);
        } else {
            requestQueue32.add(stringRequest2);
        }
    }

    private void postDOccallItem(String lastitem, String s) {
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, " https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_insertdocallitem",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        pDialog.dismissWithAnimation();

                        StatusBarcode = "1";
                        Intent ok = new Intent(getBaseContext(), menu_pelanggan.class);

                        ok.putExtra("kode", customer);
                        ok.putExtra("szDocDo", s);


                        SF2(lastitem, customer, s);



                        startActivity(ok);
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


                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String currentDateandTime2 = sdf2.format(new Date());

                params.put("szDocId", STD);
                params.put("intItemNumber", lastitem);
                params.put("szCustomerId", customer);

                params.put("dtmStart", currentDateandTime2);
                params.put("dtmFinish", currentDateandTime2);

                params.put("dtmLastCheckin", currentDateandTime2);

                params.put("szLangitude", latitude);
                params.put("szLongitude", longitude);

                params.put("szDocSO", "");
                params.put("szDocDO", s);

                params.put("szRefDocId", "");

                params.put("bOutOfRoute", "1");
                params.put("bScanBarcode", "1");

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
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest2);
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

                                int nomor = Integer.parseInt(biodatas.getString("intItemNumber")) + 1;
                                postPelangganLuar(szCustomerId, currentDateandTime2, currentDateandTime21, c, currentDateandTime22, szDocSO, szRefDocId, String.valueOf(nomor));



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
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void SF2(String s, String szCustomerId, String s1) {
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_DocCallItem",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(mulai_perjalanan.this, response, Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mulai_perjalanan.this, String.valueOf(error), Toast.LENGTH_SHORT).show();
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




                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String currentDateandTime2 = sdf2.format(new Date());

                params.put("szDocId", mulai_perjalanan.id_pelanggan);
                params.put("intItemNumber", s);

                params.put("szCustomerId", szCustomerId);

                params.put("dtmStart", currentDateandTime2);
                params.put("dtmFinish", currentDateandTime2);

                params.put("bVisited", "1");
                params.put("bSuccess", "0");


                params.put("szFailReason", "");
                params.put("szLangitude", MainActivity.latitude);
                params.put("szLongitude", MainActivity.longitude);

                if(pelanggan.equals("canvaser_luar")){
                    params.put("bOutOfRoute", "1");
                } else {
                    params.put("bOutOfRoute", "0");
                }

                params.put("szRefDocId", s1);

                params.put("bScanBarcode", "0");

                params.put("szReasonIdCheckin", "");
                params.put("szReasonFailedScan", "");
                params.put("decRadiusDiff", "0");

                System.out.println("Params = " + params);


                return params;
            }

        };
        stringRequest2.setRetryPolicy(
                new DefaultRetryPolicy(
                        5000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
        );
        RequestQueue requestQueue2 = Volley.newRequestQueue(mulai_perjalanan.this);
        requestQueue2.getCache().clear();
        requestQueue2.add(stringRequest2);
    }

    private void postPelangganLuar(String szCustomerId, String currentDateandTime2, String currentDateandTime21, char c, String currentDateandTime22, String szDocSO, String szRefDocId, String s) {

        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, " https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_PelangganLuar",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        pDialog.dismissWithAnimation();
                        Intent ok = new Intent(getBaseContext(), menu_pelanggan.class);
                        ok.putExtra("kode", szCustomerId);
                        startActivity(ok);

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
                params.put("intItemNumber", s);
                params.put("szCustomerId", szCustomerId);

                params.put("dtmStart", currentDateandTime2);
                params.put("dtmFinish", currentDateandTime2);

                params.put("bScanBarcode", "1");
                params.put("dtmLastCheckin", currentDateandTime2);
                params.put("szDocSO", szDocSO);
                params.put("szRefDocId", szRefDocId);


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
        RequestQueue requestQueue2 = Volley.newRequestQueue(mulai_perjalanan.this);
        requestQueue2.add(stringRequest2);
    }

    private void updatebScanBarcode(String idcustomer) {
        StringRequest stringRequest2 = new StringRequest(Request.Method.PUT, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_ScanBarcode_Driver",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(mulai_perjalanan.this, "Berhasil update scan barcode ke docalitem", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(mulai_perjalanan.this, "Terjadi Kesalahan saat update scan barcode ke docalitem", Toast.LENGTH_SHORT).show();
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

                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String currentDateandTime2 = sdf2.format(new Date());

                params.put("szDocId", idstd);
                params.put("szCustomerId", idcustomer);
                params.put("dtmStart", currentDateandTime2);

                params.put("szLongitude", MainActivity.longitude);
                params.put("szLatitude", MainActivity.latitude);
                params.put("szReasonFailedScan", "");

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
        RequestQueue requestQueue2 = Volley.newRequestQueue(mulai_perjalanan.this);
        requestQueue2.add(stringRequest2);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void doPrint(View view) {
        try {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH}, mulai_perjalanan.PERMISSION_BLUETOOTH);
            } else {
                BluetoothConnection connection = BluetoothPrintersConnections.selectFirstPaired();
                if (connection != null) {
                    EscPosPrinter printer = new EscPosPrinter(connection, 203, 48f, 32);
                    final String text =
                            "[C]<b>TESTING SAJA YAA</b>\n" +
                            "[L]\n" +
                            "[L]" + df.format(new Date()) + "\n" +
                            "[C]================================\n" +
                            "[L]<b>MIZONE CP BRAZIL FULL SLEEVE 500ML 1X12</b>\n" +
                            "[L]    1 pcs[R]" + nf.format(25000) + "\n" +
                            "[L]<b>MIZONE BREAKFREE CHERRYBLOSSOM 1X12 500ML</b>\n" +
                            "[L]    1 pcs[R]" + nf.format(45000) + "\n" +
                            "[L]<b>LEVITE WILDBERRIES LIME MINT 1X12 350ML</b>\n" +
                            "[L]    1 pcs[R]" + nf.format(20000) + "\n" +
                            "[L]<b>AQUA REFLECTIONS SPARK VVIP  750ML 1X6</b>\n" +
                            "[L]    1 pcs[R]" + nf.format(300000) + "\n" +
                            "[C]--------------------------------\n" +
                            "[L]TOTAL[R]" + nf.format(90000) + "\n" +
                            "[L]DISCOUNT [R]" + nf.format(13500) + "\n" +
                            "[L]TAX [R]" + nf.format(7650) + "\n" +
                            "[L]<b>GRAND TOTAL[R]" + nf.format(84150) + "</b>\n" +
                            "[C]--------------------------------\n" +
                            "[C]\n" +
                            //"[C]<barcode type='ean13' height='10'>202105160005</barcode>\n" +
                            "[C]--------------------------------\n" +
                            "[C]MAKASIH KAWAN SUDAH BERBELANJA\n" +
                            "[C]TESTING FOOTER\n" +
                            "[L]\n" +
                            //"[L]<qrcode>Testing</qrcode>\n";
                            "[L]\n";


                    printer.printFormattedText(text);
                } else {
                    Toast.makeText(this, "No printer was connected!", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            Log.e("APP", "Can't print", e);
        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(mulai_perjalanan.this, MainActivity.class);
        startActivity(i);
        finish();

    }

}