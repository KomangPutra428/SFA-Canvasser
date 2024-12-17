package com.tvip.canvasser.driver_canvaser;

import static com.tvip.canvasser.menu_mulai_perjalanan.mulai_perjalanan.STD;

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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tvip.canvasser.Perangkat.HttpsTrustManager;
import com.tvip.canvasser.R;
import com.tvip.canvasser.menu_mulai_perjalanan.mulai_perjalanan;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class mCanvaser_Menu_Pelanggan extends AppCompatActivity {

    LinearLayout info, penjualan, feedback, kompetitor, promo, survey, pengajuankredit, infopengiriman;
    public static String no_surat;
    Button kunjungan;
    String noSO;
    TextView idstd, idcustomer, tv_terima_produk;
    SharedPreferences sharedPreferences;
    static String tax;
    ArrayList<String> failed = new ArrayList<>();

    SweetAlertDialog pDialog;
    RelativeLayout warning_selesai_kunjungan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mcanvaser_menu_pelanggan);
        HttpsTrustManager.allowAllSSL();

        setContentView(R.layout.activity_menu_pelanggan);
        infopengiriman = findViewById(R.id.infopengiriman);
        info = findViewById(R.id.info);
        penjualan = findViewById(R.id.penjualan);
        feedback = findViewById(R.id.feedback);
        kompetitor = findViewById(R.id.kompetitor);
        promo = findViewById(R.id.promo);
        survey = findViewById(R.id.survey);
        pengajuankredit = findViewById(R.id.pengajuankredit);

        warning_selesai_kunjungan = findViewById(R.id.relative_warning_kunjungan_selesai);

        kunjungan = findViewById(R.id.kunjungan);

        tv_terima_produk = findViewById(R.id.tv_terima_produk);

        SharedPreferences.Editor editor = getSharedPreferences("MY_PREFS_NAME",MODE_PRIVATE).edit();
        editor.putString("nosurat", "no_surat");
        editor.apply();

        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String name= sharedPreferences.getString("nosurat", getIntent().getStringExtra("kode"));

        //Toast.makeText(menu_pelanggan.this, "kode toko " + name, Toast.LENGTH_SHORT).show();
        no_surat = name;

        idstd = findViewById(R.id.tv_idStd);
        idstd.setText(STD);
        System.out.println("id_std " + getIntent().getStringExtra("idStd"));

        idcustomer = findViewById(R.id.tv_szCustomerId);
        idcustomer.setText(name);

        kunjungan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                warning_selesai_kunjungan.setVisibility(View.GONE);

                failed.clear();
                final Dialog dialogstatus = new Dialog(mCanvaser_Menu_Pelanggan.this);
                dialogstatus.setContentView(R.layout.pilih_status_kunjungan);

                Button batal = dialogstatus.findViewById(R.id.batal);
                Button ok = dialogstatus.findViewById(R.id.ok);
                final AutoCompleteTextView statuskunjungan = dialogstatus.findViewById(R.id.statuskunjungan);

                String[] data = {"Selesai Kunjungan", "Tunda Kunjungan"};

                ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, data);
                statuskunjungan.setAdapter(adapter);
                statuskunjungan.setThreshold(1);
                statuskunjungan.dismissDropDown();

                statuskunjungan.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if(hasFocus)
                            statuskunjungan.showDropDown();
                    }
                });

                statuskunjungan.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        statuskunjungan.showDropDown();
                        return false;
                    }
                });

                batal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogstatus.dismiss();
                    }
                });

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(statuskunjungan.getText().toString().length() == 0){
                            statuskunjungan.setError("Isi Status");

                        } else if(statuskunjungan.getText().toString().equals("Tunda Kunjungan")){

                            StringRequest stringRequest2 = new StringRequest(Request.Method.PUT, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_Status_Kunjungan",
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
                                    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    String currentDateandTime2 = sdf2.format(new Date());

                                    params.put("szDocId", STD);
                                    params.put("szCustomerId", idcustomer.getText().toString());
                                    params.put("bPostPone", "1");
                                    params.put("bFinisihed", "0");
                                    params.put("bSuccess", "0");
                                    params.put("dtmFinish", currentDateandTime2);
                                    params.put("szFailReason", "");


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
                            RequestQueue requestQueue2 = Volley.newRequestQueue(mCanvaser_Menu_Pelanggan.this);
                            requestQueue2.add(stringRequest2);

                            final SweetAlertDialog pDialog = new SweetAlertDialog(mCanvaser_Menu_Pelanggan.this, SweetAlertDialog.SUCCESS_TYPE);
                            pDialog.setTitleText("Kunjungan berhasil ditunda");
                            pDialog.setConfirmText("OK");
                            pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    pDialog.dismissWithAnimation();
                                    Intent i = new Intent(mCanvaser_Menu_Pelanggan.this, mulai_perjalanan.class);
                                    startActivity(i);
                                }
                            });
                            pDialog.setCancelable(false);
                            pDialog.show();


                        } else if(statuskunjungan.getText().toString().equals("Selesai Kunjungan")) {

                            dialogstatus.dismiss();
                            StringRequest stringRequest2 = new StringRequest(Request.Method.PUT, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_Status_Kunjungan",
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
                                    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    String currentDateandTime2 = sdf2.format(new Date());

                                    params.put("szDocId", STD);
                                    params.put("szCustomerId", idcustomer.getText().toString());
                                    params.put("bPostPone", "0");
                                    params.put("bFinisihed", "1");
                                    params.put("bSuccess", "1");
                                    params.put("szFailReason", "");

                                    params.put("dtmFinish", currentDateandTime2);

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
                            RequestQueue requestQueue2 = Volley.newRequestQueue(mCanvaser_Menu_Pelanggan.this);
                            requestQueue2.add(stringRequest2);

                            final SweetAlertDialog pDialog = new SweetAlertDialog(mCanvaser_Menu_Pelanggan.this, SweetAlertDialog.SUCCESS_TYPE);
                            pDialog.setTitleText("Anda berhasil menyelesaikan kunjungan");
                            pDialog.setConfirmText("OK");
                            pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    pDialog.dismissWithAnimation();
                                    Intent i = new Intent(mCanvaser_Menu_Pelanggan.this, mulai_perjalanan.class);
                                    startActivity(i);
                                }
                            });
                            pDialog.setCancelable(false);
                            pDialog.show();

                        }
                    }
                });
                dialogstatus.show();
            }
        });

        infopengiriman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mCanvaser_Menu_Pelanggan.this, mCanvaser_Info_Pengiriman.class);
                startActivity(intent);

            }
        });
    }

    @Override
    public void onBackPressed() {

    }
}