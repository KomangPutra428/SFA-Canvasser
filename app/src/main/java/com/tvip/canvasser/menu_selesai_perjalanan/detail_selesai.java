package com.tvip.canvasser.menu_selesai_perjalanan;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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
import com.tvip.canvasser.menu_mulai_perjalanan.Utility;
import com.tvip.canvasser.pojo.sales_performance_pojo;

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

public class detail_selesai extends AppCompatActivity {
    ListView performance;
    List<sales_performance_pojo>sales_performance_pojos = new ArrayList<>();
    SharedPreferences sharedPreferences;
    ListViewAdapterSalesPerformance adapter;
    TextView qty;
    Button akhir;
    SweetAlertDialog pDialog;

    TextView target, achivment, tanggal, totalperformance;
    TextView extracall, noo;
    TextView cpr, cprtotal;
    TextView ecr, ecrtotal;

    TextView cprpercent, ecrpercent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HttpsTrustManager.allowAllSSL();
        setContentView(R.layout.activity_detail_selesai);
        performance = findViewById(R.id.performance);
        qty = findViewById(R.id.qty);
        akhir = findViewById(R.id.akhir);

        cprpercent = findViewById(R.id.cprpercent);
        ecrpercent = findViewById(R.id.ecrpercent);

        target = findViewById(R.id.target);
        achivment = findViewById(R.id.achivment);
        tanggal = findViewById(R.id.tanggal);
        totalperformance = findViewById(R.id.totalperformance);

        extracall = findViewById(R.id.extracall);
        noo = findViewById(R.id.noo);

        cpr = findViewById(R.id.cpr);
        cprtotal = findViewById(R.id.cprtotal);

        ecr = findViewById(R.id.ecr);
        ecrtotal = findViewById(R.id.ecrtotal);


        getDataDoccall();
        getSalesPerformance();


        akhir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog = new SweetAlertDialog(detail_selesai.this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("Harap Menunggu");
                pDialog.setCancelable(false);
                pDialog.show();

                sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                String nik_baru = sharedPreferences.getString("szDocCall", null);
                StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Persiapan/index_SuratTugas?nik_baru=" + nik_baru,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONObject obj = new JSONObject(response);
                                    final JSONArray movieArray = obj.getJSONArray("data");
                                    for (int i = 0; i < movieArray.length(); i++) {
                                        final JSONObject movieObject = movieArray.getJSONObject(i);
                                        String szDocId = movieObject.getString("szDocId");
                                        pDialog.dismissWithAnimation();

                                        //JIKA KM KENDARAAN AKHIR BERISI 0
                                        if(movieObject.getString("decKMFinish").equals("0")){
                                            final Dialog dialog = new Dialog(detail_selesai.this);
                                            dialog.setContentView(R.layout.topup_km_akhir);

                                            final EditText editkm = dialog.findViewById(R.id.editkmakhir);
                                            final TextView keterangan = dialog.findViewById(R.id.keterangan);
                                            Button tidak = dialog.findViewById(R.id.tidak);
                                            Button ya = dialog.findViewById(R.id.ya);
                                            final EditText editkmawal = dialog.findViewById(R.id.editkmawal);


                                            editkmawal.setText(movieObject.getString("decKMStart"));
                                            String awal = movieObject.getString("decKMStart");
                                            keterangan.setText("Isi KM Akhir");

                                            ya.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    if(editkm.getText().toString().length()==0){
                                                        new SweetAlertDialog(detail_selesai.this, SweetAlertDialog.WARNING_TYPE)
                                                                .setTitleText("Harap isi KM")
                                                                .setConfirmText("OK")
                                                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                    @Override
                                                                    public void onClick(SweetAlertDialog sDialog) {
                                                                        sDialog.dismissWithAnimation();
                                                                    }
                                                                })
                                                                .show();
                                                    } else if(totalperformance.getText().toString().equals("0.0 %")){
                                                        new SweetAlertDialog(detail_selesai.this, SweetAlertDialog.WARNING_TYPE)
                                                                .setTitleText("Harap Selesaikan Kunjungan Terlebih Dahulu")
                                                                .setConfirmText("OK")
                                                                .show();
                                                    } else if(Integer.parseInt(editkm.getText().toString()) <= Integer.parseInt(awal)){
                                                        new SweetAlertDialog(detail_selesai.this, SweetAlertDialog.WARNING_TYPE)
                                                                .setTitleText("KM Akhir Harus Lebih Besar Daripada KM Awal")
                                                                .setConfirmText("OK")
                                                                .show();
                                                    } else {
                                                        final SweetAlertDialog pDialog2 = new SweetAlertDialog(detail_selesai.this, SweetAlertDialog.PROGRESS_TYPE);
                                                        pDialog2.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                                                        pDialog2.setTitleText("Harap Menunggu");
                                                        pDialog2.setCancelable(false);
                                                        pDialog2.show();

                                                        StringRequest stringRequest2 = new StringRequest(Request.Method.PUT, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Persiapan/index_KMFinish",
                                                                new Response.Listener<String>() {

                                                                    @Override
                                                                    public void onResponse(String response) {
                                                                        if(totalperformance.getText().toString().contains("100.0 %")){
                                                                            StringRequest stringRequest2 = new StringRequest(Request.Method.PUT, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Selesai_Perjalanan/index_FinishDoccall",
                                                                                    new Response.Listener<String>() {

                                                                                        @Override
                                                                                        public void onResponse(String response) {
                                                                                            pDialog.dismissWithAnimation();
                                                                                            new SweetAlertDialog(detail_selesai.this, SweetAlertDialog.SUCCESS_TYPE)
                                                                                                    .setContentText("Data Sudah Disimpan")
                                                                                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                                                        @Override
                                                                                                        public void onClick(SweetAlertDialog sDialog) {
                                                                                                            sDialog.dismissWithAnimation();
                                                                                                            finish();
                                                                                                        }
                                                                                                    })
                                                                                                    .show();

                                                                                        }
                                                                                    },
                                                                                    new Response.ErrorListener() {
                                                                                        @Override
                                                                                        public void onErrorResponse(VolleyError error) {
                                                                                            pDialog.dismissWithAnimation();
                                                                                            new SweetAlertDialog(detail_selesai.this, SweetAlertDialog.SUCCESS_TYPE)
                                                                                                    .setContentText("Data Sudah Disimpan")
                                                                                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                                                        @Override
                                                                                                        public void onClick(SweetAlertDialog sDialog) {
                                                                                                            sDialog.dismissWithAnimation();
                                                                                                            finish();
                                                                                                        }
                                                                                                    })
                                                                                                    .show();
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

                                                                                    params.put("szDocId", szDocId);
                                                                                    params.put("dtmFinish", currentDateandTime2);
                                                                                    params.put("szUserUpdatedId", nik_baru);
                                                                                    params.put("dtmLastUpdated", currentDateandTime2);


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
                                                                            RequestQueue requestQueue2 = Volley.newRequestQueue(detail_selesai.this);
                                                                            requestQueue2.add(stringRequest2);

                                                                        } else {
                                                                            pDialog2.dismissWithAnimation();
                                                                            dialog.cancel();
                                                                            new SweetAlertDialog(detail_selesai.this, SweetAlertDialog.SUCCESS_TYPE)
                                                                                    .setContentText("Data Sudah Disimpan")
                                                                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                                        @Override
                                                                                        public void onClick(SweetAlertDialog sDialog) {
                                                                                            sDialog.dismissWithAnimation();
                                                                                            Intent intent = new Intent(getApplicationContext(), com.tvip.canvasser.menu_selesai_perjalanan.selesai_perjalanan.class);
                                                                                            startActivity(intent);
                                                                                        }
                                                                                    })
                                                                                    .show();
                                                                            pDialog2.cancel();
                                                                        }


                                                                    }
                                                                }, new Response.ErrorListener() {
                                                            @Override
                                                            public void onErrorResponse(VolleyError error) {
                                                                if(totalperformance.getText().toString().contains("100")){
                                                                    StringRequest stringRequest2 = new StringRequest(Request.Method.PUT, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Selesai_Perjalanan/index_FinishDoccall",
                                                                            new Response.Listener<String>() {

                                                                                @Override
                                                                                public void onResponse(String response) {
                                                                                    pDialog.dismissWithAnimation();
                                                                                    new SweetAlertDialog(detail_selesai.this, SweetAlertDialog.SUCCESS_TYPE)
                                                                                            .setContentText("Data Sudah Disimpan")
                                                                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                                                @Override
                                                                                                public void onClick(SweetAlertDialog sDialog) {
                                                                                                    sDialog.dismissWithAnimation();
                                                                                                    finish();
                                                                                                }
                                                                                            })
                                                                                            .show();

                                                                                }
                                                                            },
                                                                            new Response.ErrorListener() {
                                                                                @Override
                                                                                public void onErrorResponse(VolleyError error) {
                                                                                    pDialog.dismissWithAnimation();
                                                                                    new SweetAlertDialog(detail_selesai.this, SweetAlertDialog.SUCCESS_TYPE)
                                                                                            .setContentText("Data Sudah Disimpan")
                                                                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                                                @Override
                                                                                                public void onClick(SweetAlertDialog sDialog) {
                                                                                                    sDialog.dismissWithAnimation();
                                                                                                    finish();
                                                                                                }
                                                                                            })
                                                                                            .show();
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

                                                                            params.put("szDocId", szDocId);
                                                                            params.put("dtmFinish", currentDateandTime2);
                                                                            params.put("szUserUpdatedId", nik_baru);
                                                                            params.put("dtmLastUpdated", currentDateandTime2);


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
                                                                    RequestQueue requestQueue2 = Volley.newRequestQueue(detail_selesai.this);
                                                                    requestQueue2.add(stringRequest2);
                                                                } else {
                                                                    pDialog2.dismissWithAnimation();
                                                                    dialog.cancel();
                                                                    new SweetAlertDialog(detail_selesai.this, SweetAlertDialog.SUCCESS_TYPE)
                                                                            .setContentText("Data Sudah Disimpan")
                                                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                                @Override
                                                                                public void onClick(SweetAlertDialog sDialog) {
                                                                                    sDialog.dismissWithAnimation();
                                                                                    Intent intent = new Intent(getApplicationContext(), com.tvip.canvasser.menu_selesai_perjalanan.selesai_perjalanan.class);
                                                                                    startActivity(intent);
                                                                                }
                                                                            })
                                                                            .show();
                                                                    pDialog2.cancel();
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
                                                                params.put("szDocId", szDocId);
                                                                params.put("decKMFinish", editkm.getText().toString());


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
                                                        RequestQueue requestQueue2 = Volley.newRequestQueue(detail_selesai.this);
                                                        requestQueue2.add(stringRequest2);

                                                    }

                                                }
                                            });

                                            tidak.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    dialog.dismiss();
                                                }
                                            });
                                            dialog.show();

                                        } else {
                                            pDialog.dismissWithAnimation();
                                            Intent intent = new Intent(getApplicationContext(), com.tvip.canvasser.menu_selesai_perjalanan.selesai_perjalanan.class);
                                            startActivity(intent);
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
                                new SweetAlertDialog(detail_selesai.this, SweetAlertDialog.ERROR_TYPE)
                                        .setContentText("Surat Tugas Belum Ada")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.dismissWithAnimation();
                                            }
                                        })
                                        .show();
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
                RequestQueue requestQueue = Volley.newRequestQueue(detail_selesai.this);
                requestQueue.add(stringRequest);
            }
        });

    }

    private void getCallPerformance(String szDocId) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_AOPerformance?surat_tugas=" + szDocId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            int extra = 0;
                            int noos = 0;
                            int bFinisihed = 0;
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");
                            for (int i = 0; i < movieArray.length(); i++) {
                                final JSONObject movieObject = movieArray.getJSONObject(i);

                                if(movieObject.getString("bOutOfRoute").equals("1")){
                                    extra++;
                                    extracall.setText(String.valueOf(extra));
                                }

                                if(movieObject.getString("bNewCustomer").equals("1")){
                                    noos++;
                                    noo.setText(String.valueOf(noos));
                                }

                                if(movieObject.getString("bFinisihed").equals("1")){
                                    bFinisihed++;
                                    ecr.setText(String.valueOf(bFinisihed));
                                }

                                cpr.setText(String.valueOf(i + 1));
                                cprtotal.setText(String.valueOf(i + 1));
                                ecrtotal.setText(String.valueOf(i + 1));
                                cprpercent.setText("100.0%");


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

    private void getDataDoccall() {
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

                                getAOPerformance(movieObject.getString("szDocId"));
                                getCallPerformance(movieObject.getString("szDocId"));

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
        RequestQueue requestQueue = Volley.newRequestQueue(detail_selesai.this);
        requestQueue.add(stringRequest);
    }

    private void getAOPerformance(String szDocId) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_AOPerformance?surat_tugas=" + szDocId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            int number = 0;
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");
                            for (int i = 0; i < movieArray.length(); i++) {
                                final JSONObject movieObject = movieArray.getJSONObject(i);
                                SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
                                String currentDateandTime2 = sdf2.format(new Date());

                                target.setText(String.valueOf(i + 1));

                                if(movieObject.getString("bSuccess").equals("1")){
                                    number ++;
                                    achivment.setText(String.valueOf(number));
                                }

                                tanggal.setText(currentDateandTime2);

                                int a = Integer.parseInt(achivment.getText().toString());
                                int b = Integer.parseInt(target.getText().toString());

                                float percent = (a / (b * 1.0f)) *100;
                                totalperformance.setText(String.valueOf(percent) + " %");
                                ecrpercent.setText(String.valueOf(percent) + " %");



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

    private void getSalesPerformance() {
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String nik_baru = sharedPreferences.getString("szDocCall", null);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_SalesPerformance?szEmployeeId=" + nik_baru,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            int number = 0;
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");
                            for (int i = 0; i < movieArray.length(); i++) {
                                final JSONObject movieObject = movieArray.getJSONObject(i);
                                final sales_performance_pojo movieItem = new sales_performance_pojo(
                                        movieObject.getString("decQty"),
                                        movieObject.getString("szName"),
                                        movieObject.getString("decAmount"));

                                sales_performance_pojos.add(movieItem);

                                adapter = new ListViewAdapterSalesPerformance(sales_performance_pojos, getApplicationContext());
                                performance.setAdapter(adapter);
                                Utility.setListViewHeightBasedOnChildren(performance);



                            }
                            int total = 0;
                            int totalvalue;
                            for(int i = 0; i < sales_performance_pojos.size();i++){
                                String szId = adapter.getItem(i).getDecAmount();
                                String[] parts = szId.split("\\.");
                                String szIdSlice = parts[0];

                                totalvalue = Integer.parseInt(szIdSlice);
                                total+=totalvalue;
                                qty.setText(String.valueOf(total));

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

    public static class ListViewAdapterSalesPerformance extends ArrayAdapter<sales_performance_pojo> {
        private List<sales_performance_pojo> sales_performance_pojos;

        private Context context;

        public ListViewAdapterSalesPerformance(List<sales_performance_pojo> sales_performance_pojos, Context context) {
            super(context, R.layout.list_penjualan_sub, sales_performance_pojos);
            this.sales_performance_pojos = sales_performance_pojos;
            this.context = context;
        }

        @SuppressLint("NewApi")
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = LayoutInflater.from(context);

            View listViewItem = inflater.inflate(R.layout.list_penjualan_sub, null, true);

            TextView produk = listViewItem.findViewById(R.id.produk);
            TextView qty = listViewItem.findViewById(R.id.qty);

            sales_performance_pojo data = sales_performance_pojos.get(position);

            produk.setText(data.getSzName());

            String szId = data.getDecAmount();
            String[] parts = szId.split("\\.");
            String szIdSlice = parts[0];

            produk.setText(data.getDecQty() + " x " + data.getSzName());
            qty.setText(szIdSlice);

            return listViewItem;
        }
    }
}