package com.tvip.canvasser.menu_mulai_perjalanan;

import static com.tvip.canvasser.menu_mulai_perjalanan.mulai_perjalanan.STD;
import static com.tvip.canvasser.menu_mulai_perjalanan.mulai_perjalanan.pelanggan;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.ContentUris;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.google.android.material.appbar.MaterialToolbar;
import com.tvip.canvasser.Perangkat.HttpsTrustManager;
import com.tvip.canvasser.R;
import com.tvip.canvasser.driver_canvaser.mCanvaser_Terima_Produk;
import com.tvip.canvasser.menu_utama.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class menu_pelanggan extends AppCompatActivity {
    LinearLayout info, penjualan, feedback, kompetitor, promo, survey, pengajuankredit, infopengiriman;
    public static String no_surat;
    public static MaterialToolbar pengaturanBar;
    Button kunjungan;
    String noSO;
    TextView idstd;
    public static TextView idcustomer;
    SharedPreferences sharedPreferences;
    static String tax;
    ArrayList<String> failed = new ArrayList<>();

    SweetAlertDialog pDialog;
    RelativeLayout warning_selesai_kunjungan;

    public static TextView textpenjualan, tv_terima_produk;

    public static String DocDo;

    private RequestQueue requestQueue, requestQueue2, requestQueue3;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        textpenjualan = findViewById(R.id.textpenjualan);
        idcustomer = findViewById(R.id.tv_szCustomerId);





        warning_selesai_kunjungan = findViewById(R.id.relative_warning_kunjungan_selesai);

        pengaturanBar = findViewById(R.id.pengaturanBar);
        kunjungan = findViewById(R.id.kunjungan);

        tv_terima_produk = findViewById(R.id.tv_terima_produk);

        SharedPreferences.Editor editor = getSharedPreferences("MY_PREFS_NAME",MODE_PRIVATE).edit();
        editor.putString("nosurat", "no_surat");
        editor.putString("DocDo", "DO");

        editor.apply();

        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String name= sharedPreferences.getString("nosurat", getIntent().getStringExtra("kode"));
        String DO = sharedPreferences.getString("DocDo", getIntent().getStringExtra("szDocDo"));

        //Toast.makeText(menu_pelanggan.this, "kode toko " + name, Toast.LENGTH_SHORT).show();
        no_surat = name;
        DocDo = DO;

        Toast.makeText(this, DocDo, Toast.LENGTH_SHORT).show();

        idstd = findViewById(R.id.tv_idStd);
        idstd.setText(STD);


        idcustomer.setText(name);

        String outofroute;


        pDialog = new SweetAlertDialog(menu_pelanggan.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Harap Menunggu");
        pDialog.setCancelable(false);
        pDialog.show();

        pengajuankredit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), pengajuan_kredit.class);
                startActivity(intent);
            }
        });

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), menu_info.class);
                startActivity(intent);

            }
        });



        promo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), posm.class);
                startActivity(intent);
            }
        });

        survey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), survey_input.class);
                startActivity(intent);
            }
        });

        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), feedback.class);
                startActivity(intent);
            }
        });

        kompetitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), kompetitor.class);
                startActivity(intent);
            }
        });


        if(pelanggan.equals("canvaser_luar")){
            pengaturanBar.setTitle("Pelanggan Luar Rute");
        } else if (pelanggan.equals("canvaser")){
            pengaturanBar.setTitle("Pelanggan Dalam Rute");
        } else {
            pengaturanBar.setTitle("Pelanggan Non Rute");
        }

        penjualan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (tv_terima_produk.getText().toString().equals("1")) {
                    if(pengaturanBar.getTitle().toString().equals("Pelanggan Luar Rute")){
                        Intent intent = new Intent(getBaseContext(), mCanvaser_Terima_Produk.class);
                        intent.putExtra("idStd", idstd.getText().toString());
                        intent.putExtra("idCustomer", idcustomer.getText().toString());
                        intent.putExtra("Status", "Success");
                        startActivity(intent);
                    } else if(pengaturanBar.getTitle().toString().equals("Pelanggan Dalam Rute")){
                        Toast.makeText(menu_pelanggan.this, "Produk telah diterima pelanggan", Toast.LENGTH_SHORT).show();
                    } else  if(pengaturanBar.getTitle().toString().equals("Pelanggan Non Rute")){
                        Toast.makeText(menu_pelanggan.this, "Produk telah diterima pelanggan", Toast.LENGTH_SHORT).show();
                    }
                } else if (tv_terima_produk.getText().toString().equals("0")) {

                    Intent intent = new Intent(getBaseContext(), mCanvaser_Terima_Produk.class);

                    intent.putExtra("idStd", idstd.getText().toString());
                    intent.putExtra("idCustomer", idcustomer.getText().toString());
                    intent.putExtra("Status", "Success");

                    startActivity(intent);
                }

            }
        });



        kunjungan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 // deletePictures();
                warning_selesai_kunjungan.setVisibility(View.GONE);

                failed.clear();
                final Dialog dialogstatus = new Dialog(menu_pelanggan.this);
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
                        getLonglat();
                        if(statuskunjungan.getText().toString().length() == 0){
                            statuskunjungan.setError("Isi Status");

                        } else if(statuskunjungan.getText().toString().equals("Tunda Kunjungan")){
                            new SweetAlertDialog(menu_pelanggan.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setContentText("Kunjungan Ditunda")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.dismissWithAnimation();
                                            StringRequest stringRequest2 = new StringRequest(Request.Method.PUT, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_Status_Kunjungan",
                                                    new Response.Listener<String>() {

                                                        @Override
                                                        public void onResponse(String response) {
                                                            pDialog = new SweetAlertDialog(menu_pelanggan.this, SweetAlertDialog.PROGRESS_TYPE);
                                                            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                                                            pDialog.setTitleText("Harap Menunggu");
                                                            pDialog.setCancelable(false);
                                                            pDialog.show();

//                                                            Intent i = new Intent(getBaseContext(), mulai_perjalanan.class);
//                                                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Call Only, if you wants to clears the activity stack else ignore it.
//                                                            startActivity(i);
//                                                            finish();
//                                                            System.gc();
                                                            getCompare("1", "0", "1", "");

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
                                                    params.put("bFinisihed", "1");
                                                    params.put("bSuccess", "0");
                                                    params.put("dtmFinish", currentDateandTime2);
                                                    params.put("szFailReason", "");
                                                    params.put("bVisited", "1");
                                                    params.put("szDocDO", DocDo);


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
                                            RequestQueue requestQueue2 = Volley.newRequestQueue(menu_pelanggan.this);
                                            requestQueue2.add(stringRequest2);

                                        }
                                    })
                                    .show();

                        } else if(statuskunjungan.getText().toString().equals("Selesai Kunjungan")) {

                            dialogstatus.dismiss();


                            if(tv_terima_produk.getText().toString().equals("1")){
                            StringRequest stringRequest2 = new StringRequest(Request.Method.PUT, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_Status_Kunjungan",
                                    new Response.Listener<String>() {

                                        @Override
                                        public void onResponse(String response) {

                                            final SweetAlertDialog pDialog2 = new SweetAlertDialog(menu_pelanggan.this, SweetAlertDialog.SUCCESS_TYPE);
                                            pDialog2.setTitleText("Anda berhasil menyelesaikan kunjungan");
                                            pDialog2.setConfirmText("OK");
                                            pDialog2.setCancelable(false);
                                            pDialog2.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                    pDialog2.dismissWithAnimation();

                                                    pDialog = new SweetAlertDialog(menu_pelanggan.this, SweetAlertDialog.PROGRESS_TYPE);
                                                    pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                                                    pDialog.setTitleText("Harap Menunggu");
                                                    pDialog.setCancelable(false);
                                                    pDialog.show();

//                                                    Intent i = new Intent(getBaseContext(), mulai_perjalanan.class);
//                                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Call Only, if you wants to clears the activity stack else ignore it.
//                                                    startActivity(i);
//                                                    finish();
//                                                    System.gc();
                                                    getCompare("1", "1", "0", "");

                                                }
                                            });
                                            pDialog2.show();


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
                                    params.put("bVisited", "1");
                                    params.put("szFailReason", "");

                                    params.put("dtmFinish", currentDateandTime2);
                                    params.put("szDocDO", DocDo);

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
                            RequestQueue requestQueue2 = Volley.newRequestQueue(menu_pelanggan.this);
                            requestQueue2.add(stringRequest2);
                            } else {
                                final Dialog dialog = new Dialog(menu_pelanggan.this);
                                dialog.setContentView(R.layout.topup_reason);
                                dialog.setCancelable(false);
                                dialog.show();



                                final AutoCompleteTextView editpilihalasan = dialog.findViewById(R.id.editpilihalasan);
                                Button tidak = dialog.findViewById(R.id.tidak);
                                Button ya = dialog.findViewById(R.id.ya);

                                StringRequest rest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_FailedVisit",
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                try {
                                                    JSONObject jsonObject = new JSONObject(response);
                                                    if (jsonObject.getString("status").equals("true")) {
                                                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                                                        for (int i = 0; i < jsonArray.length(); i++) {
                                                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                                            String id = jsonObject1.getString("szId");
                                                            String jenis_istirahat = jsonObject1.getString("szName");
                                                            failed.add(id + "-" + jenis_istirahat);

                                                        }
                                                    }
                                                    editpilihalasan.setAdapter(new ArrayAdapter<String>(menu_pelanggan.this, android.R.layout.simple_expandable_list_item_1, failed));
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
                                        5000,
                                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                RequestQueue requestkota = Volley.newRequestQueue(menu_pelanggan.this);
                                requestkota.getCache().clear();
                                requestkota.add(rest);
                                
                                tidak.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                    }
                                });

                                ya.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        editpilihalasan.setError(null);
                                        if(editpilihalasan.getText().toString().length() == 0){
                                            editpilihalasan.setError("Pilih Alasan");
                                        } else {
                                            new SweetAlertDialog(menu_pelanggan.this, SweetAlertDialog.SUCCESS_TYPE)
                                                    .setContentText("Kunjungan Selesai")
                                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override
                                                        public void onClick(SweetAlertDialog sDialog) {
                                                            sDialog.dismissWithAnimation();
                                                            StringRequest stringRequest2 = new StringRequest(Request.Method.PUT, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_Status_Kunjungan",
                                                                    new Response.Listener<String>() {

                                                                        @Override
                                                                        public void onResponse(String response) {
                                                                            pDialog = new SweetAlertDialog(menu_pelanggan.this, SweetAlertDialog.PROGRESS_TYPE);
                                                                            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                                                                            pDialog.setTitleText("Harap Menunggu");
                                                                            pDialog.setCancelable(false);
                                                                            pDialog.show();

                                                                            getCompare("1", "0", "0", editpilihalasan.getText().toString());
                                                                            sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                                                                            String nik_baru = sharedPreferences.getString("szDocCall", null);

                                                                            if(nik_baru.contains("RP")){

                                                                            } else if(nik_baru.contains("SPV")){

                                                                            } else {
                                                                                postDOPending(DocDo);
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

                                                                @Override
                                                                protected Map<String, String> getParams() throws AuthFailureError {
                                                                    Map<String, String> params = new HashMap<String, String>();
                                                                    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                                                    String currentDateandTime2 = sdf2.format(new Date());

                                                                    String[] parts = editpilihalasan.getText().toString().split("-");
                                                                    String restnomor = parts[0];
                                                                    String restnomorbaru = restnomor.replace(" ", "");

                                                                    params.put("szDocId", STD);
                                                                    params.put("szCustomerId", idcustomer.getText().toString());
                                                                    params.put("bPostPone", "0");
                                                                    params.put("bFinisihed", "1");
                                                                    params.put("bVisited", "1");
                                                                    params.put("bSuccess", "0");
                                                                    params.put("szFailReason", restnomorbaru);


                                                                    params.put("dtmFinish", currentDateandTime2);
                                                                    params.put("szDocDO", DocDo);



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
                                                            RequestQueue requestQueue2 = Volley.newRequestQueue(menu_pelanggan.this);
                                                            requestQueue2.getCache().clear();
                                                            requestQueue2.add(stringRequest2);

//                                                            Intent i = new Intent(getBaseContext(), mulai_perjalanan.class);
//                                                            i.setFlags(FLAG_ACTIVITY_CLEAR_TOP); // Call Only, if you wants to clears the activity stack else ignore it.
//                                                            startActivity(i);
//                                                            finish();
//                                                            System.gc();

                                                        }
                                                    })
                                                    .show();

                                        }
                                    }
                                });
                            }







                        }
                    }
                });
                dialogstatus.show();
            }
        });

        infopengiriman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(menu_pelanggan.this, mInfo_Pengiriman_Customer.class);
                intent.putExtra("idCustomer", idcustomer.getText().toString());
                startActivity(intent);

            }
        });

        if(menu_pelanggan.pengaturanBar.getTitle().toString().equals("Pelanggan Dalam Rute")){
            outofroute = "0";
        } else {
            outofroute = "1";
        }
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_BKBKeterangan?id_std=" + STD, //szDocId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            int number = 0;
                            JSONObject obj = new JSONObject(response);
                            if (obj.getString("status").equals("true")) {
                                final JSONArray movieArray = obj.getJSONArray("data");
                                    final JSONObject movieObject = movieArray.getJSONObject(movieArray.length()-1);

                                    if (movieObject.getString("mix_ref_std").equals("") || movieObject.getString("mix_ref_std").equals("null")){
                                        textpenjualan.setText("Penjualan");
                                    } else {
                                        textpenjualan.setText("Penjualan (MIX)");
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
        RequestQueue requestQueue = Volley.newRequestQueue(menu_pelanggan.this);
        requestQueue.add(stringRequest);


    }

    private void postDOPending(String docDo) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_DO_Pending?szDocId=" + docDo, //szDocId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

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
        RequestQueue requestQueue = Volley.newRequestQueue(menu_pelanggan.this);
        requestQueue.add(stringRequest);
    }

    private void getLonglat() {
            StringRequest channel_status = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_Longlat?szDocId="+STD+"&szCustomerId=" + idcustomer.getText().toString(),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject obj = new JSONObject(response);
                                final JSONArray movieArray = obj.getJSONArray("data");

                                JSONObject biodatas = null;
                                for (int i = 0; i < movieArray.length(); i++) {

                                    biodatas = movieArray.getJSONObject(i);

                                    if(biodatas.getString("szLatitude").equals("")){
                                        putRadiusLongLat(0);
                                    } else {
                                        Location startPoint=new Location("locationA");
                                        startPoint.setLatitude(Double.valueOf(MainActivity.latitude));
                                        startPoint.setLongitude(Double.valueOf(MainActivity.longitude));

                                        Location endPoint=new Location("locationA");
                                        endPoint.setLatitude(Double.valueOf(biodatas.getString("szLatitude")));
                                        endPoint.setLongitude(Double.valueOf(biodatas.getString("szLongitude")));
                                        double distance2 = startPoint.distanceTo(endPoint);
                                        int value = (int) distance2;

                                        System.out.println("Hasil Radius = " + String.valueOf(value));

                                        putRadiusLongLat(value);
                                    }





                                }

                            } catch (JSONException e) {
                                e.printStackTrace();

                            }
                        }

                        private void putRadiusLongLat(int value) {
                            StringRequest stringRequest2 = new StringRequest(Request.Method.PUT, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_DoccallItemSFA3",
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

                                    params.put("szDocId", STD);
                                    params.put("szCustomerId", idcustomer.getText().toString());
                                    params.put("szRefDocId", getIntent().getStringExtra("szDocDo"));

                                    if(pelanggan.equals("canvaser_luar")){
                                        params.put("bOutOfRoute", "1");
                                    } else {
                                        params.put("bOutOfRoute", "0");
                                    }

                                    params.put("decRadiusDiff", String.valueOf(value));

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
                            RequestQueue requestQueue2 = Volley.newRequestQueue(menu_pelanggan.this);
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

            channel_status.setRetryPolicy(
                    new DefaultRetryPolicy(
                            5000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                    ));
            RequestQueue channel_statusQueue = Volley.newRequestQueue(this);
            channel_statusQueue.add(channel_status);
    }

    private void getNoSO() {
        StringRequest channel_status = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_NoInduk?noso_turunan=" + idcustomer.getText().toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");

                            JSONObject biodatas = null;
                            for (int i = 0; i < movieArray.length(); i++) {

                                biodatas = movieArray.getJSONObject(i);
                                noSO = biodatas.getString("noso_turunan");

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        noSO = "Tidak Ada SO";
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
        RequestQueue channel_statusQueue = Volley.newRequestQueue(menu_pelanggan.this);
        channel_statusQueue.add(channel_status);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        getNoSO();
        history_terima_produk();

        StringRequest channel_status = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_Taxtype",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");

                            JSONObject biodatas = null;
                            for (int i = 0; i < movieArray.length(); i++) {

                                biodatas = movieArray.getJSONObject(i);
                                tax = biodatas.getString("decTax");

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        noSO = "Tidak Ada SO";
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
        if (requestQueue2 == null) {
            requestQueue2 = Volley.newRequestQueue(menu_pelanggan.this);
            requestQueue2.add(channel_status);
        } else {
            requestQueue2.add(channel_status);
        }

    }

    public void history_terima_produk(){
        String outofroute;
        if(menu_pelanggan.pengaturanBar.getTitle().toString().equals("Pelanggan Dalam Rute") || menu_pelanggan.pengaturanBar.getTitle().toString().equals("Pelanggan Non Rute")){
            outofroute = "0";
        } else {
            outofroute = "1";
        }
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_SO?szDocId="+STD+"&szCustomerId=" + idcustomer.getText().toString() + "&bOutOfRoute=" + outofroute, //szDocId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.dismissWithAnimation();

                        try {
                            int number = 0;
                            JSONObject obj = new JSONObject(response);
                            if (obj.getString("status").equals("true")) {
                                final JSONArray movieArray = obj.getJSONArray("data");
                                for (int i = 0; i < movieArray.length(); i++) {
                                    final JSONObject movieObject = movieArray.getJSONObject(i);
                                    getDocDo(DocDo);

                                }






                            }


                        } catch(JSONException e){
                            e.printStackTrace();

                        }
                    }

                    private void getDocDo(String szDocDO) {

                        StringRequest rest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_DocDo?szDocId="+ szDocDO,

                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        tv_terima_produk.setText("1");
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        tv_terima_produk.setText("0");
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
                        if (requestQueue3 == null) {
                            requestQueue3 = Volley.newRequestQueue(menu_pelanggan.this);
                            requestQueue3.add(rest);
                        } else {
                            requestQueue3.add(rest);
                        }
                    }


                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        tv_terima_produk.setText("0");
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

    @Override
    public void onBackPressed() {
        warning_selesai_kunjungan.setVisibility(View.VISIBLE);
        return;
    }

    private void UpdatesfaDoccallItem(String bVisited, String bSuccess, String bPostPone, String szFailReason, String s) {
        StringRequest stringRequest2 = new StringRequest(Request.Method.PUT, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_DoccallItemSFA2",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), "Sedang Memuat...", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(menu_pelanggan.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        System.exit(0);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Sedang Memuat...", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(menu_pelanggan.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        System.exit(0);
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

                String[] parts = szFailReason.split("-");
                String restnomor = parts[0];
                String restnomorbaru = restnomor.replace(" ", "");


                params.put("szDocId", STD);
                params.put("szCustomerId", idcustomer.getText().toString());

                if(pelanggan.equals("canvaser_luar")){
                    params.put("bOutOfRoute", "1");
                } else {
                    params.put("bOutOfRoute", "0");
                }

                params.put("dtmFinish", currentDateandTime2);

                params.put("bVisited", bVisited);
                params.put("bSuccess", bSuccess);
                params.put("bPostPone", bPostPone);
                params.put("szFailReason", restnomorbaru);
                params.put("decDuration", s);

                params.put("szRefDocId", getIntent().getStringExtra("szDocDo"));

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
        RequestQueue requestQueue2 = Volley.newRequestQueue(menu_pelanggan.this);
        requestQueue2.add(stringRequest2);
    }

    private void getCompare(String bVisited, String bSuccess, String bPostPone, String szFailReason) {
        String outofroute;
        if(menu_pelanggan.pengaturanBar.getTitle().toString().equals("Pelanggan Dalam Rute") || menu_pelanggan.pengaturanBar.getTitle().toString().equals("Pelanggan Non Rute")){
            outofroute = "0";
        } else {
            outofroute = "1";
        }
        StringRequest channel_status = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_SO?szDocId=" + STD + "&szCustomerId=" + idcustomer.getText().toString() + "&bOutOfRoute=" + outofroute,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");

                            JSONObject biodatas = null;
                            for (int i = 0; i < movieArray.length(); i++) {

                                biodatas = movieArray.getJSONObject(i);

                                String startDate = biodatas.getString("dtmStart");
                                String stopDate = biodatas.getString("dtmFinish");

                                if(biodatas.getString("bFinisihed").equals("1")){
                                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                                    LocalDateTime dateTime1= LocalDateTime.parse(startDate, formatter);
                                    LocalDateTime dateTime2= LocalDateTime.parse(stopDate, formatter);

                                    long diffInSeconds = java.time.Duration.between(dateTime1, dateTime2).getSeconds();

                                    UpdateDecDuration(String.valueOf(diffInSeconds));

                                    UpdatesfaDoccallItem(bVisited, bSuccess, bPostPone, szFailReason, String.valueOf(diffInSeconds));

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

        channel_status.setRetryPolicy(
                new DefaultRetryPolicy(
                        5000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                ));
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(menu_pelanggan.this);
            requestQueue.add(channel_status);
        } else {
            requestQueue.add(channel_status);
        }

    }

    private void UpdateDecDuration(String valueOf) {
        StringRequest stringRequest2 = new StringRequest(Request.Method.PUT, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_DecDuration",
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

                params.put("szDocId", STD);
                params.put("szCustomerId", idcustomer.getText().toString());

                if(pelanggan.equals("canvaser_luar")){
                    params.put("bOutOfRoute", "1");
                } else {
                    params.put("bOutOfRoute", "0");
                }

                params.put("decDuration", valueOf);

                System.out.println("Params duration = " + params);



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
        RequestQueue requestQueue2 = Volley.newRequestQueue(menu_pelanggan.this);
        requestQueue2.add(stringRequest2);
    }

    private void deletePictures(){

        String[] projection = {MediaStore.Images.Media._ID};
        Cursor cursor = getApplicationContext().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null,null, null);
        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID));
            Uri deleteUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
            getApplicationContext().getContentResolver().delete(deleteUri, null, null);
        }
        cursor.close();

    }

}