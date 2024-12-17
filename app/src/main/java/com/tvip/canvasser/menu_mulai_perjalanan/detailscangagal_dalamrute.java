package com.tvip.canvasser.menu_mulai_perjalanan;

import static com.tvip.canvasser.menu_mulai_perjalanan.mulai_perjalanan.STD;
import static com.tvip.canvasser.menu_mulai_perjalanan.mulai_perjalanan.pelanggan;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.google.android.material.appbar.MaterialToolbar;
import com.tvip.canvasser.Perangkat.HttpsTrustManager;
import com.tvip.canvasser.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class detailscangagal_dalamrute extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    static TextView idstd;
    static TextView namatoko;
    static EditText code;
    static EditText address;
    static AutoCompleteTextView alasangagalscan;
    Button batal, lanjutkan;
    ArrayList<String> GagalScan = new ArrayList<>();
    static String intItemNumbers, intItemNumbers_nonrute;

    LottieAnimationView lottie_alasan_gagal_scan, lottie_button_lanjutkan;
    RelativeLayout rl_reload_detailscangagal;
    LinearLayout linear_alasan_gagal_scan;

    MaterialToolbar persiapanbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailscangagal_dalamrute);
        HttpsTrustManager.allowAllSSL();
        idstd = findViewById(R.id.idstd);
        namatoko = findViewById(R.id.namatoko);
        code = findViewById(R.id.code);
        address = findViewById(R.id.address);
        alasangagalscan = findViewById(R.id.alasangagalscan);

        Toast.makeText(this, getIntent().getStringExtra("szDocDo"), Toast.LENGTH_SHORT).show();


        batal = findViewById(R.id.batal);
        lanjutkan = findViewById(R.id.lanjutkan);

        lottie_alasan_gagal_scan = findViewById(R.id.lottie_alasan_gagal_scan);
        rl_reload_detailscangagal = findViewById(R.id.rl_reload_detailscangagal);
        linear_alasan_gagal_scan = findViewById(R.id.linear_alasan_gagal_scan);
        lottie_button_lanjutkan = findViewById(R.id.lottie_button_lanjutkan);

        persiapanbar = findViewById(R.id.persiapanbar);

        idstd.setText(getIntent().getStringExtra("idStd"));
        namatoko.setText(getIntent().getStringExtra("nama"));
        code.setText(getIntent().getStringExtra("kode"));
        address.setText(getIntent().getStringExtra("alamat"));
        intItemNumbers = getIntent().getStringExtra("intItemNumbers");
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        Toast.makeText(this, getIntent().getStringExtra("intItemNumbers"), Toast.LENGTH_SHORT).show();


        Toast.makeText(detailscangagal_dalamrute.this, "sZDocId = " + mulai_perjalanan.id_pelanggan, Toast.LENGTH_SHORT).show();
        Toast.makeText(detailscangagal_dalamrute.this, "kode = " + code.getText().toString(), Toast.LENGTH_SHORT).show();

        if(pelanggan.equals("canvaser_luar")){
            persiapanbar.setTitle("Pelanggan Luar Rute");
        } else if(pelanggan.equals("canvaser")){
            persiapanbar.setTitle("Pelanggan Dalam Rute");
        } else {
            persiapanbar.setTitle("Pelanggan Non Rute");
        }


        alasangagalscan.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                    alasangagalscan.showDropDown();
            }
        });

        rl_reload_detailscangagal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alasan_gagal_scan();
            }
        });

        alasangagalscan.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                alasangagalscan.showDropDown();
                return false;
            }
        });

        batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        lanjutkan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alasangagalscan.setError(null);
                lottie_button_lanjutkan.setVisibility(View.VISIBLE);
                lanjutkan.setText("Loading...");

                //JIKA BELUM MEMILIH PILIHAN MAKA AKAN TAMPIL ALERT SILAHKAN PILIH ALASAN
                if(alasangagalscan.getText().toString().length() ==0){
                    alasangagalscan.setError("Silahkan Pilih Alasan");
                    lottie_button_lanjutkan.setVisibility(View.GONE);
                    lanjutkan.setText("Lanjutkan");
                } else {
                    if(getIntent().getStringExtra("langitude").equals("")){
                        Toast.makeText(getBaseContext(), "Toko Tidak Ada Longlat", Toast.LENGTH_LONG).show();
                        lottie_button_lanjutkan.setVisibility(View.GONE);
                        lanjutkan.setText("Lanjutkan");
                    } else {

                        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                        String nik_baru = sharedPreferences.getString("szDocCall", null);
                        String[] parts = nik_baru.split("-");
                        String restnomor = parts[0];

                        if(pelanggan.equals("non_rute")){
                            StringRequest channel_status = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_List_NonRute_Scan?surat_tugas="+STD+"&szSoldToBranchId=" + restnomor+"&szId=" + code.getText().toString(),
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
                                                        getNumber(code.getText().toString(), namatoko.getText().toString(), address.getText().toString(), getIntent().getStringExtra("langitude"), getIntent().getStringExtra("longitude"));
                                                    } else {
                                                        lanjutkan.setText("Lanjutkan");
                                                        Intent direction = new Intent(getBaseContext(), scangagal_map.class);
                                                        direction.putExtra("langitude", getIntent().getStringExtra("langitude"));
                                                        direction.putExtra("longitude", getIntent().getStringExtra("longitude"));
                                                        direction.putExtra("toko", namatoko.getText().toString());
                                                        direction.putExtra("alamat", address.getText().toString());
                                                        direction.putExtra("kode", code.getText().toString());
                                                        direction.putExtra("idStd", getIntent().getStringExtra("idStd"));
                                                        direction.putExtra("szDocDo", getIntent().getStringExtra("szDocDo"));


                                                        startActivity(direction);
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
                                            getNumber(code.getText().toString(), namatoko.getText().toString(), address.getText().toString(), getIntent().getStringExtra("langitude"), getIntent().getStringExtra("longitude"));
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
                            RequestQueue channel_statusQueue = Volley.newRequestQueue(detailscangagal_dalamrute.this);
                            channel_statusQueue.add(channel_status);
                        } else {
                            StringRequest stringRequest2 = new StringRequest(Request.Method.PUT, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_gagal_scan",
                                    new Response.Listener<String>() {

                                        @Override
                                        public void onResponse(String response) {
                                            lottie_button_lanjutkan.setVisibility(View.GONE);
                                            lanjutkan.setText("Lanjutkan");

                                            Toast.makeText(detailscangagal_dalamrute.this, "Berhasil Update bStarted ke DocCalItem", Toast.LENGTH_SHORT).show();
                                            Intent direction = new Intent(getBaseContext(), scangagal_map.class);

                                            direction.putExtra("langitude", getIntent().getStringExtra("langitude"));
                                            direction.putExtra("longitude", getIntent().getStringExtra("longitude"));
                                            direction.putExtra("toko", namatoko.getText().toString());
                                            direction.putExtra("alamat", address.getText().toString());
                                            direction.putExtra("kode", code.getText().toString());
                                            direction.putExtra("idStd", getIntent().getStringExtra("idStd"));
                                            direction.putExtra("szDocDo", getIntent().getStringExtra("szDocDo"));


                                            startActivity(direction);
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            lottie_button_lanjutkan.setVisibility(View.GONE);
                                            lanjutkan.setText("Lanjutkan");

                                            Intent direction = new Intent(getBaseContext(), scangagal_map.class);

                                            direction.putExtra("langitude", getIntent().getStringExtra("langitude"));
                                            direction.putExtra("longitude", getIntent().getStringExtra("longitude"));
                                            direction.putExtra("toko", namatoko.getText().toString());
                                            direction.putExtra("alamat", address.getText().toString());
                                            direction.putExtra("kode", code.getText().toString());
                                            direction.putExtra("idStd", getIntent().getStringExtra("idStd"));
                                            direction.putExtra("szDocDo", getIntent().getStringExtra("szDocDo"));

                                            startActivity(direction);

                                            String rest = alasangagalscan.getText().toString();
                                            String[] parts = rest.split("-");
                                            String restnomor = parts[0];
                                            String restnomorbaru = restnomor.replace(" ", "");

                                            System.out.println("szDocId = " + getIntent().getStringExtra("idStd"));
                                            System.out.println("szCustomerId = " + getIntent().getStringExtra("kode"));
                                            System.out.println("szReasonFailedScan = " + restnomorbaru);

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

                                    String rest = alasangagalscan.getText().toString();
                                    String[] parts = rest.split("-");
                                    String restnomor = parts[0];
                                    String restnomorbaru = restnomor.replace(" ", "");

                                    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    String currentDateandTime2 = sdf2.format(new Date());

                                    params.put("szDocId", STD);
                                    params.put("szCustomerId", getIntent().getStringExtra("kode"));
                                    params.put("szReasonFailedScan", restnomorbaru);
                                    params.put("szDocDO", getIntent().getStringExtra("szDocDo"));
                                    params.put("dtmStart", currentDateandTime2);

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
                            RequestQueue requestQueue2 = Volley.newRequestQueue(detailscangagal_dalamrute.this);
                            requestQueue2.add(stringRequest2);
                        }
                    }
                }


            }
        });

        alasan_gagal_scan();

    }

    private void getNumber(String kode, String nama, String alamat, String langitude, String longitude) {
        intItemNumbers_nonrute = getIntent().getStringExtra("intItemNumbersNonRute");
        updateBstarted(getIntent().getStringExtra("DoNonRute"));
        updateCustomer(getIntent().getStringExtra("intItemNumbersNonRute"), kode, getIntent().getStringExtra("DoNonRute"), nama, alamat, langitude, longitude);


        //        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_firstNumber?szDocId=" + STD,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            int number = 0;
//                            JSONObject obj = new JSONObject(response);
//                            final JSONArray movieArray = obj.getJSONArray("data");
//                            for (int i = 0; i < movieArray.length(); i++) {
//                                final JSONObject movieObject = movieArray.getJSONObject(i);
//
////
//                            }
//                        } catch(JSONException e){
//                            e.printStackTrace();
//
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        new SweetAlertDialog(detailscangagal_dalamrute.this, SweetAlertDialog.WARNING_TYPE)
//                                .setContentText("DO Non-rute sudah habis")
//                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                                    @Override
//                                    public void onClick(SweetAlertDialog sDialog) {
//                                        sDialog.dismissWithAnimation();
//                                    }
//                                })
//                                .show();
//                    }
//                })
//
//        {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                HashMap<String, String> params = new HashMap<String, String>();
//                String creds = String.format("%s:%s", "admin", "Databa53");
//                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
//                params.put("Authorization", auth);
//                return params;
//            }
//        };
//
//        stringRequest.setRetryPolicy(
//                new DefaultRetryPolicy(
//                        500000,
//                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
//                ));
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        requestQueue.add(stringRequest);
    }

    private void updateCustomer(String intItemNumber, String kode, String szDocDO, String nama, String alamat, String langitude, String longitude) {
        updateBstarted(szDocDO);
    }

    private void updateBstarted(String szDocDO) {
        StringRequest stringRequest2 = new StringRequest(Request.Method.PUT, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_gagal_scan",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        lottie_button_lanjutkan.setVisibility(View.GONE);
                        lanjutkan.setText("Lanjutkan");

                        Toast.makeText(detailscangagal_dalamrute.this, "Berhasil Update bStarted ke DocCalItem", Toast.LENGTH_SHORT).show();
                        Intent direction = new Intent(getBaseContext(), scangagal_map.class);

                        direction.putExtra("langitude", getIntent().getStringExtra("langitude"));
                        direction.putExtra("longitude", getIntent().getStringExtra("longitude"));
                        direction.putExtra("toko", namatoko.getText().toString());
                        direction.putExtra("alamat", address.getText().toString());
                        direction.putExtra("kode", code.getText().toString());
                        direction.putExtra("idStd", getIntent().getStringExtra("idStd"));
                        direction.putExtra("szDocDo", szDocDO);


                        startActivity(direction);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        lottie_button_lanjutkan.setVisibility(View.GONE);
                        lanjutkan.setText("Lanjutkan");

                        Intent direction = new Intent(getBaseContext(), scangagal_map.class);

                        direction.putExtra("langitude", getIntent().getStringExtra("langitude"));
                        direction.putExtra("longitude", getIntent().getStringExtra("longitude"));
                        direction.putExtra("toko", namatoko.getText().toString());
                        direction.putExtra("alamat", address.getText().toString());
                        direction.putExtra("kode", code.getText().toString());
                        direction.putExtra("idStd", getIntent().getStringExtra("idStd"));
                        direction.putExtra("szDocDo", szDocDO);

                        startActivity(direction);

                        String rest = alasangagalscan.getText().toString();
                        String[] parts = rest.split("-");
                        String restnomor = parts[0];
                        String restnomorbaru = restnomor.replace(" ", "");

                        System.out.println("szDocId = " + getIntent().getStringExtra("idStd"));
                        System.out.println("szCustomerId = " + getIntent().getStringExtra("kode"));
                        System.out.println("szReasonFailedScan = " + restnomorbaru);

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

                String rest = alasangagalscan.getText().toString();
                String[] parts = rest.split("-");
                String restnomor = parts[0];
                String restnomorbaru = restnomor.replace(" ", "");

                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String currentDateandTime2 = sdf2.format(new Date());

                params.put("szDocId", STD);
                params.put("szCustomerId", "");
                params.put("szReasonFailedScan", restnomorbaru);
                params.put("szDocDO", szDocDO);
                params.put("dtmStart", currentDateandTime2);

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
        RequestQueue requestQueue2 = Volley.newRequestQueue(detailscangagal_dalamrute.this);
        requestQueue2.add(stringRequest2);
    }

    private void alasan_gagal_scan() {
        lottie_alasan_gagal_scan.setVisibility(View.VISIBLE);
        //MENAMPILKAN LIST JIKA GAGAL DI SCAN
        StringRequest rest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_Scan_Barcode_Failed",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        lottie_alasan_gagal_scan.setVisibility(View.GONE);
                        linear_alasan_gagal_scan.setVisibility(View.GONE);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("status").equals("true")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    String id = jsonObject1.getString("szId");
                                    String jenis_istirahat = jsonObject1.getString("szName");
                                    GagalScan.add(id + "-" + jenis_istirahat);

                                }
                            }
                            alasangagalscan.setAdapter(new ArrayAdapter<String>(detailscangagal_dalamrute.this, android.R.layout.simple_expandable_list_item_1, GagalScan));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        lottie_alasan_gagal_scan.setVisibility(View.GONE);
                        linear_alasan_gagal_scan.setVisibility(View.VISIBLE);
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
        RequestQueue requestkota = Volley.newRequestQueue(detailscangagal_dalamrute.this);
        requestkota.add(rest);
    }
}