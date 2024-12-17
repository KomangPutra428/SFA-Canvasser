package com.tvip.canvasser.menu_mulai_perjalanan;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.tvip.canvasser.R;
import com.tvip.canvasser.pojo.total_penjualan_pojo;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.tvip.canvasser.menu_mulai_perjalanan.menu_pelanggan.no_surat;
import static com.tvip.canvasser.menu_mulai_perjalanan.product_penjualan.totalPenjualanPojos;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class summary_order extends AppCompatActivity {
    ListView listtotalpenjualan;
    ListViewAdapterTotalPenjualan adapter;
    TextView jumlah, diskon, total;
    int jumlah_harga = 0;
    int jumlah_diskon = 0;
    int jumlah_stock = 0;
    int total_harga = 0;
    
    String SOInduk;

    Button batal, lanjutkan;
    AutoCompleteTextView pilihpembayaran;
    EditText tanggalkirim;
    EditText catatan;
    SignaturePad signature_pad;
    ImageButton refresh;
    String[] jenis = {"Tunai", "Kredit"};
    BottomSheetBehavior sheetBehavior;
    BottomSheetDialog sheetDialog;
    View bottom_sheet;

    SweetAlertDialog pDialog;

    private SimpleDateFormat dateFormatter;
    private Calendar date;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary_order);
        bottom_sheet = findViewById(R.id.bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(bottom_sheet);

        listtotalpenjualan = findViewById(R.id.listtotalpenjualan);
        dateFormatter = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());

        pilihpembayaran = findViewById(R.id.pilihpembayaran);
        tanggalkirim = findViewById(R.id.tanggalkirim);
        catatan = findViewById(R.id.catatan);
        signature_pad = findViewById(R.id.signature_pad);
        refresh = findViewById(R.id.refresh);

        jumlah = findViewById(R.id.jumlah);
        diskon = findViewById(R.id.diskon);
        total = findViewById(R.id.total);

        batal = findViewById(R.id.batal);
        lanjutkan = findViewById(R.id.lanjutkan);
        refresh.bringToFront();
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signature_pad.clear();
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
                if(jumlah_stock > 350){
                    new SweetAlertDialog(summary_order.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Jumlah Produk Maksimal 350")
                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                }else if(pilihpembayaran.getText().toString().length() == 0){
                    pilihpembayaran.setText("Pilih Pembayaran");
                } else if (tanggalkirim.getText().toString().length() == 0){
                    tanggalkirim.setText("Isi Tanggal Kirim");
                } else if (catatan.getText().toString().length() == 0){
                    catatan.setText("Isi Catatan");
                } else if (signature_pad.getSignatureBitmap() == null){
                    new SweetAlertDialog(summary_order.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Isi Tanda Tangan")
                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .show();

                } else {
                    pDialog = new SweetAlertDialog(summary_order.this, SweetAlertDialog.PROGRESS_TYPE);
                    pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                    pDialog.setTitleText("Harap Menunggu");
                    pDialog.setCancelable(false);
                    pDialog.show();

                    StringRequest channel_status = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_Detail_Pelanggan?szCustomerId=" + no_surat,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    try {
                                        JSONObject obj = new JSONObject(response);
                                        final JSONArray movieArray = obj.getJSONArray("data");

                                        JSONObject biodatas = null;
                                        for (int i = 0; i < movieArray.length(); i++) {

                                            biodatas = movieArray.getJSONObject(i);

                                            if (menu_pelanggan.pengaturanBar.getTitle().equals("Pelanggan Luar Rute")) {
                                                sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                                                String nik_baru = sharedPreferences.getString("szDocCall", null);
                                                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddHHmmss");
                                                String currentDateandTime2 = sdf2.format(new Date());
                                                postSoDoc(nik_baru + "_" + currentDateandTime2);
                                                totalHarga(nik_baru + "_" + currentDateandTime2);
                                                DocSO(nik_baru + "_" + currentDateandTime2);
                                                getSzId(nik_baru + "_" + currentDateandTime2);
                                            } else {
                                                if (biodatas.getString("szDocSO").equals("")) {
                                                    sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                                                    String nik_baru = sharedPreferences.getString("szDocCall", null);
                                                    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddHHmmss");
                                                    String currentDateandTime2 = sdf2.format(new Date());
                                                    postSoDoc(nik_baru + "_" + currentDateandTime2);
                                                    totalHarga(nik_baru + "_" + currentDateandTime2);
                                                    DocSO(nik_baru + "_" + currentDateandTime2);
                                                    getSzId(nik_baru + "_" + currentDateandTime2);

                                                } else {
                                                    postSoDoc(biodatas.getString("szDocSO"));
                                                    totalHarga(biodatas.getString("szDocSO"));
                                                    DocSO(biodatas.getString("szDocSO"));
                                                    getSzId(biodatas.getString("szDocSO"));
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

                                    sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                                    String nik_baru = sharedPreferences.getString("szDocCall", null);

                                    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddHHmmss");
                                    String currentDateandTime2 = sdf2.format(new Date());
                                    postSoDoc(nik_baru + "_" + currentDateandTime2);
                                    getSzId(nik_baru + "_" + currentDateandTime2);


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
                    RequestQueue channel_statusQueue = Volley.newRequestQueue(summary_order.this);
                    channel_statusQueue.add(channel_status);

                }
            }
        });

        String[] parts = no_surat.split("-");
        String branch = parts[0];

        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateandTime2 = sdf2.format(new Date());
        StringRequest biodata = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_NoSoInduk?depo="+branch+"&tgl=" + currentDateandTime2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");

                            JSONObject biodatas = null;
                            for (int i = 0; i < movieArray.length(); i++) {

                                biodatas = movieArray.getJSONObject(i);
                                SOInduk = (biodatas.getString("iniso_induk"));





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

        biodata.setRetryPolicy(
                new DefaultRetryPolicy(
                        500000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                ));
        RequestQueue biodataQueue = Volley.newRequestQueue(this);
        biodataQueue.add(biodata);

        tanggalkirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar currentDate = Calendar.getInstance();
                Calendar twoDaysAgo = (Calendar) currentDate.clone();
                twoDaysAgo.add(Calendar.DATE, 1);

                date = currentDate.getInstance();

                DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        date.set(year, monthOfYear, dayOfMonth);

                        tanggalkirim.setText(dateFormatter.format(date.getTime()));
                    }
                };
                DatePickerDialog datePickerDialog = new DatePickerDialog(summary_order.this, dateSetListener, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(twoDaysAgo.getTimeInMillis());
                datePickerDialog.show();

            }
        });



        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>
                (this, android.R.layout.select_dialog_item, jenis);
        pilihpembayaran.setAdapter(adapter2);//setting the adapter data into the AutoCompleteTextView

        batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                totalPenjualanPojos.clear();
                adapter.notifyDataSetChanged();
                finish();
            }
        });

        adapter = new ListViewAdapterTotalPenjualan(totalPenjualanPojos, getApplicationContext());
        listtotalpenjualan.setAdapter(adapter);
        Utility.setListViewHeightBasedOnChildren(listtotalpenjualan);

        adapter.notifyDataSetChanged();

        for(int i = 0; i < totalPenjualanPojos.size();i++){
            jumlah_stock += Integer.parseInt(adapter.getItem(i).getStock());
        }

        for(int i = 0; i < totalPenjualanPojos.size();i++){
            jumlah_harga += Integer.parseInt(adapter.getItem(i).getJumlah_harga());
            jumlah_diskon += Integer.parseInt(adapter.getItem(i).getDisc_distributor()) + Integer.parseInt(adapter.getItem(i).getDisc_internal()) + Integer.parseInt(adapter.getItem(i).getDisc_principle());
        }
        total_harga = jumlah_harga - jumlah_diskon;

        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();
        formatRp.setCurrencySymbol("Rp. ");
        formatRp.setMonetaryDecimalSeparator(',');
        formatRp.setGroupingSeparator('.');
        kursIndonesia.setDecimalFormatSymbols(formatRp);

        jumlah.setText(kursIndonesia.format(jumlah_harga));
        diskon.setText(kursIndonesia.format(jumlah_diskon));
        total.setText(kursIndonesia.format(total_harga));
    }

    private void getSzId(String no_surats) {
        for(int i = 0; i < totalPenjualanPojos.size(); i++){
            int finalI = i;
            StringRequest rest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_SzPride?szProductId="+adapter.getItem(i).getSzId()+"&employeeId=" + no_surat,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getString("status").equals("true")) {
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                        int discount = Integer.parseInt(adapter.getItem(finalI).getDisc_principle()) + Integer.parseInt(adapter.getItem(finalI).getDisc_distributor()) + Integer.parseInt(adapter.getItem(finalI).getDisc_internal());

                                        String[] parts = adapter.getItem(finalI).getDecPrice().split("\\.");
                                        String restnomor = parts[0];

                                        int counts = Integer.parseInt(adapter.getItem(finalI).getStock()) * Integer.parseInt(restnomor);
                                        postItem(jsonObject1.getString("pricesegment"), no_surats, String.valueOf(finalI), adapter.getItem(finalI).getDecPrice(), discount, counts, adapter.getItem(finalI).getDisc_principle(), adapter.getItem(finalI).getDisc_distributor(), adapter.getItem(finalI).getDisc_internal());

                                        String segment = jsonObject1.getString("pricesegment");
                                        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_DocSoPriceMDBA",
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
                                                sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                                                String nik_baru = sharedPreferences.getString("szDocCall", null);

                                                double dpp = (counts / 1.11);
                                                int ppn = Math.toIntExact(counts - Math.round(dpp));

                                                params.put("szDocId", no_surats);
                                                params.put("intItemNumber", String.valueOf(finalI));

                                                params.put("szPriceId", segment);
                                                params.put("decPrice", adapter.getItem(finalI).getDecPrice());
                                                params.put("decDiscount", String.valueOf(discount) + ".0000");
                                                params.put("decAmount", String.valueOf(counts) + ".0000");

                                                params.put("decTax", String.valueOf(Math.round(dpp)));
                                                params.put("decDpp", String.valueOf(ppn));
                                                params.put("szTaxId", "PPN");

                                                params.put("decTaxRate", menu_pelanggan.tax);

                                                params.put("decDiscPrinciple", adapter.getItem(finalI).getDisc_principle() + ".0000");
                                                params.put("decDiscDistributor", adapter.getItem(finalI).getDisc_distributor() + ".0000");
                                                params.put("decDiscInternal", adapter.getItem(finalI).getDisc_internal() + ".0000");





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
                                        RequestQueue requestQueue2 = Volley.newRequestQueue(summary_order.this);
                                        requestQueue2.add(stringRequest2);


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
                            int discount = Integer.parseInt(adapter.getItem(finalI).getDisc_principle()) + Integer.parseInt(adapter.getItem(finalI).getDisc_distributor()) + Integer.parseInt(adapter.getItem(finalI).getDisc_internal());

                            String[] parts = adapter.getItem(finalI).getDecPrice().split("\\.");
                            String restnomor = parts[0];

                            String[] parts3 = no_surat.split("-");
                            String restnomor3 = parts3[0];

                            int counts = Integer.parseInt(adapter.getItem(finalI).getStock()) * Integer.parseInt(restnomor);
                            postItem(restnomor3+"-C_00", no_surats, String.valueOf(finalI), adapter.getItem(finalI).getDecPrice(), discount, counts, adapter.getItem(finalI).getDisc_principle(), adapter.getItem(finalI).getDisc_distributor(), adapter.getItem(finalI).getDisc_internal());

                            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_DocSoPriceMDBA",
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
                                    sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                                    String nik_baru = sharedPreferences.getString("szDocCall", null);

                                    double dpp = (counts / 1.11);
                                    int ppn = Math.toIntExact(counts - Math.round(dpp));

                                    params.put("szDocId", no_surats);
                                    params.put("intItemNumber", String.valueOf(finalI));

                                    params.put("szPriceId", restnomor3+"-C_00");
                                    params.put("decPrice", adapter.getItem(finalI).getDecPrice());
                                    params.put("decDiscount", String.valueOf(discount) + ".0000");
                                    params.put("decAmount", String.valueOf(counts) + ".0000");

                                    params.put("decTax", String.valueOf(Math.round(dpp)));
                                    params.put("decDpp", String.valueOf(ppn));
                                    params.put("szTaxId", "PPN");

                                    params.put("decTaxRate", menu_pelanggan.tax);

                                    params.put("decDiscPrinciple", adapter.getItem(finalI).getDisc_principle() + ".0000");
                                    params.put("decDiscDistributor", adapter.getItem(finalI).getDisc_distributor() + ".0000");
                                    params.put("decDiscInternal", adapter.getItem(finalI).getDisc_internal() + ".0000");





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
                            RequestQueue requestQueue2 = Volley.newRequestQueue(summary_order.this);
                            requestQueue2.add(stringRequest2);
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
            RequestQueue requestkota = Volley.newRequestQueue(summary_order.this);
            requestkota.add(rest);
        }
    }


    private void postItem(String pricesegment, String no_surats, String s, String decPrice, int discount, int counts, String disc_principle, String disc_distributor, String disc_internal) {
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_DocSoPrice",
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
                sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                String nik_baru = sharedPreferences.getString("szDocCall", null);

                double dpp = (counts / 1.11);
                int ppn = Math.toIntExact(counts - Math.round(dpp));

                params.put("szDocId", no_surats);
                params.put("intItemNumber", s);

                params.put("szPriceId", pricesegment);
                params.put("decPrice", decPrice);
                params.put("decDiscount", String.valueOf(discount) + ".0000");
                params.put("decAmount", String.valueOf(counts) + ".0000");

                params.put("decTax", String.valueOf(Math.round(dpp)));
                params.put("decDpp", String.valueOf(ppn));
                params.put("szTaxId", "PPN");

                params.put("decTaxRate", menu_pelanggan.tax);

                params.put("decDiscPrinciple", disc_principle + ".0000");
                params.put("decDiscDistributor", disc_distributor + ".0000");
                params.put("decDiscInternal", disc_internal + ".0000");





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
        RequestQueue requestQueue2 = Volley.newRequestQueue(summary_order.this);
        requestQueue2.add(stringRequest2);

    }

    private void DocSO(String s) {
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_DocSoData",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        DocSOMDBA(s);
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
                sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                String nik_baru = sharedPreferences.getString("szDocCall", null);

                String[] parts = no_surat.split("-");
                String branch = parts[0];


                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String currentDateandTime2 = sdf2.format(new Date());

                params.put("szDocId", s);
                params.put("dtmDoc", currentDateandTime2);
                params.put("szCustomerId", no_surat);
                params.put("szEmployeeId", nik_baru);

                if(pilihpembayaran.getText().toString().equals("Tunai")){
                    params.put("bCash", "1");
                } else {
                    params.put("bCash", "0");
                }
                params.put("szPaymentTermId", "0HARI");

                params.put("dtmPO", currentDateandTime2);
                params.put("dtmPOExpired", currentDateandTime2);
                params.put("szBranchId", branch);
                params.put("szDocStatus", "Applied");

                params.put("szUserCreatedId", nik_baru);
                params.put("dtmCreated", currentDateandTime2);
                params.put("dtmReqDlvDate", convertFormat(tanggalkirim.getText().toString()));

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
        RequestQueue requestQueue2 = Volley.newRequestQueue(summary_order.this);
        requestQueue2.add(stringRequest2);
    }

    private void DocSOMDBA(String s) {
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_DocSoDataMDBA",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

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

                String[] parts = no_surat.split("-");
                String branch = parts[0];


                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String currentDateandTime2 = sdf2.format(new Date());

                params.put("szDocId", s);
                params.put("dtmDoc", currentDateandTime2);
                params.put("szCustomerId", no_surat);
                params.put("szEmployeeId", nik_baru);

                if(pilihpembayaran.getText().toString().equals("Tunai")){
                    params.put("bCash", "1");
                } else {
                    params.put("bCash", "0");
                }
                params.put("szPaymentTermId", "0HARI");

                params.put("dtmPO", currentDateandTime2);
                params.put("dtmPOExpired", currentDateandTime2);
                params.put("szBranchId", branch);
                params.put("szDocStatus", "Applied");

                params.put("szUserCreatedId", nik_baru);
                params.put("dtmCreated", currentDateandTime2);
                params.put("dtmReqDlvDate", convertFormat(tanggalkirim.getText().toString()));

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
        RequestQueue requestQueue2 = Volley.newRequestQueue(summary_order.this);
        requestQueue2.add(stringRequest2);
    }

    private void totalHarga(String szRefDocId) {
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_TotalHarga",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        postSOInduk(szRefDocId);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismissWithAnimation();
                new SweetAlertDialog(summary_order.this, SweetAlertDialog.ERROR_TYPE)
                        .setContentText("No SO sudah terinput")
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

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                String nik_baru = sharedPreferences.getString("szDocCall", null);

                String[] parts = no_surat.split("-");
                String branch = parts[0];

                double dpp = (total_harga / 1.11);
                int ppn = Math.toIntExact(total_harga - Math.round(dpp));

                params.put("szDocSo", szRefDocId);
                params.put("dpp", String.valueOf(Math.round(dpp)));
                params.put("ppn", String.valueOf(ppn));
                params.put("totalDiscount", String.valueOf(jumlah_diskon));
                params.put("totalHarga", String.valueOf(total_harga));
                params.put("depo", branch);


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
        RequestQueue requestQueue2 = Volley.newRequestQueue(summary_order.this);
        requestQueue2.add(stringRequest2);
    }

    private void postSOInduk(String szRefDocId) {
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_SoInduk",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        postPenjualanMDBA(szRefDocId);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismissWithAnimation();
                new SweetAlertDialog(summary_order.this, SweetAlertDialog.ERROR_TYPE)
                        .setContentText("No SO sudah terinput")
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

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                String nik_baru = sharedPreferences.getString("szDocCall", null);

                SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String currentDateandTime3 = sdf3.format(new Date());

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String currentDateandTime = sdf.format(new Date());

                String[] parts = no_surat.split("-");
                String branch = parts[0];

                params.put("noso_induk", SOInduk);
                params.put("noso_turunan", szRefDocId);
                params.put("id_customer", no_surat);
                params.put("id_employ", nik_baru);
                params.put("Id_User_Created", nik_baru);
                params.put("kode_company", "ASA");
                params.put("kode_depo", branch);

                params.put("tgl_cari", currentDateandTime);
                params.put("tgl_insert", currentDateandTime3);


                params.put("status", "OPEN");

                if(pilihpembayaran.getText().toString().equals("Tunai")){
                    params.put("type_bayar", "1");
                } else {
                    params.put("type_bayar", "2");
                }


                params.put("req_date", convertFormat(tanggalkirim.getText().toString()));



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
        RequestQueue requestQueue2 = Volley.newRequestQueue(summary_order.this);
        requestQueue2.add(stringRequest2);
    }

    private void postPenjualanMDBA(String szRefDocId) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    docsoMDBA(szRefDocId);
                }
            }, 500000);
        for(int i = 0;i <=  totalPenjualanPojos.size() - 1 ;i++) {
            int finalI = i;
            int finalI1 = i;
            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_PenjualanItemMDBA",
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println("Nama Error : " + String.valueOf(error));

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


                    params.put("szDocId", szRefDocId);
                    params.put("intItemNumber", String.valueOf(finalI));
                    params.put("szProductId", adapter.getItem(finalI1).getSzId());

                    params.put("decQty", adapter.getItem(finalI1).getStock());
                    params.put("szUomId", adapter.getItem(finalI1).getSzUomId());


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
            RequestQueue requestQueue2 = Volley.newRequestQueue(summary_order.this);
            requestQueue2.add(stringRequest2);
        }
    }

    private void docsoMDBA(String szRefDocId) {
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_DocSoMDBA",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        postSoDocItemMDBA(szRefDocId);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismissWithAnimation();
                new SweetAlertDialog(summary_order.this, SweetAlertDialog.ERROR_TYPE)
                        .setContentText("No SO sudah terinput")
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

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                String nik_baru = sharedPreferences.getString("szDocCall", null);

                SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String currentDateandTime3 = sdf3.format(new Date());


                params.put("szDocId", szRefDocId);
                params.put("dtmDoc", currentDateandTime3);
                params.put("szEmployeeId", nik_baru);
                params.put("szCustomerId", no_surat);
                params.put("decAmount", String.valueOf(total_harga) + ".0000");
                params.put("intPrintedCount", "0");
                params.put("szDocCallId", mulai_perjalanan.id_pelanggan);



                String[] parts = no_surat.split("-");
                String branch = parts[0];

                params.put("szBranchId", branch);

                if(branch.equals("321") || branch.equals("336") || branch.equals("317") || branch.equals("'036'")){
                    params.put("szCompanyId", "ASA");
                } else {
                    params.put("szCompanyId", "TVIP");
                }
                params.put("szDocStatus", "Draft");


                params.put("szUserCreatedId", nik_baru);
                params.put("szUserUpdatedId", nik_baru);
                params.put("dtmCreated", currentDateandTime3);

                params.put("dtmLastUpdated", currentDateandTime3);

                params.put("dtmDelivery", convertFormat(tanggalkirim.getText().toString()));
                params.put("szNote", catatan.getText().toString());

                if(pilihpembayaran.getText().toString().equals("Tunai")){
                    params.put("bCash", "1");
                } else {
                    params.put("bCash", "0");
                }







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
        RequestQueue requestQueue2 = Volley.newRequestQueue(summary_order.this);
        requestQueue2.add(stringRequest2);
    }

    private void postSoDocItemMDBA(String szRefDocId) {

        for(int i = 0;i <=  totalPenjualanPojos.size() - 1 ;i++) {
            int finalI = i;
            int finalI1 = i;
            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_DocSoItemMDBA",
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println("Nama Error : " + String.valueOf(error));

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

                    int principla = Integer.parseInt(adapter.getItem(finalI1).getDisc_principle());
                    int distributor = Integer.parseInt(adapter.getItem(finalI1).getDisc_distributor());
                    int Internal = Integer.parseInt(adapter.getItem(finalI1).getDisc_internal());

                    params.put("szDocId", szRefDocId);
                    params.put("intItemNumber", String.valueOf(finalI));
                    params.put("szProductId", adapter.getItem(finalI1).getSzId());

                    params.put("decQty", adapter.getItem(finalI1).getStock());
                    params.put("decPrice", adapter.getItem(finalI1).getDecPrice() + ".0000");
                    params.put("decAmount", adapter.getItem(finalI1).getJumlah_harga() + ".0000");
                    params.put("decDiscount", String.valueOf(principla + distributor + Internal) + ".0000");

                    params.put("szUomId", adapter.getItem(finalI1).getSzUomId());


                    params.put("decDiscPrinciple", adapter.getItem(finalI1).getDisc_principle() + ".0000");
                    params.put("decDiscountDistributor", adapter.getItem(finalI1).getDisc_distributor() + ".0000");
                    params.put("decDiscountInternal", adapter.getItem(finalI1).getDisc_internal() + ".0000");


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
            RequestQueue requestQueue2 = Volley.newRequestQueue(summary_order.this);
            requestQueue2.add(stringRequest2);
        }
    }

    private void postSoDoc(String szRefDocId) {
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_DocSo",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        postSoDocItem(szRefDocId);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismissWithAnimation();
                new SweetAlertDialog(summary_order.this, SweetAlertDialog.ERROR_TYPE)
                        .setContentText("No SO sudah terinput")
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

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                String nik_baru = sharedPreferences.getString("szDocCall", null);

                SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String currentDateandTime3 = sdf3.format(new Date());


                params.put("szDocId", szRefDocId);
                params.put("dtmDoc", currentDateandTime3);
                params.put("szEmployeeId", nik_baru);
                params.put("szCustomerId", no_surat);
                params.put("decAmount", String.valueOf(total_harga) + ".0000");
                params.put("intPrintedCount", "0");
                params.put("szDocCallId", mulai_perjalanan.id_pelanggan);


                String[] parts = no_surat.split("-");
                String branch = parts[0];

                params.put("szBranchId", branch);

                if(branch.equals("321") || branch.equals("336") || branch.equals("317") || branch.equals("'036'")){
                    params.put("szCompanyId", "ASA");
                } else {
                    params.put("szCompanyId", "TVIP");
                }
                params.put("szDocStatus", "Draft");


                params.put("szUserCreatedId", nik_baru);
                params.put("szUserUpdatedId", nik_baru);
                params.put("dtmCreated", currentDateandTime3);

                params.put("dtmLastUpdated", currentDateandTime3);

                params.put("dtmDelivery", convertFormat(tanggalkirim.getText().toString()));
                params.put("szNote", catatan.getText().toString());

                if(pilihpembayaran.getText().toString().equals("Tunai")){
                    params.put("bCash", "1");
                } else {
                    params.put("bCash", "0");
                }


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
        RequestQueue requestQueue2 = Volley.newRequestQueue(summary_order.this);
        requestQueue2.add(stringRequest2);
    }

    private void postSoDocItem(String szRefDocId) {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                pDialog.dismissWithAnimation();
                new SweetAlertDialog(summary_order.this, SweetAlertDialog.SUCCESS_TYPE)
                        .setContentText("Data Sudah Disimpan")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                                totalPenjualanPojos.clear();
                                adapter.notifyDataSetChanged();
                                Intent intent = new Intent(getApplicationContext(), menu_pelanggan.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.putExtra("kode", no_surat);
                                startActivity(intent);
                            }
                        })
                        .show();

            }
        }, 500000);

        for(int i = 0;i <=  totalPenjualanPojos.size() - 1 ;i++){
            int finalI = i;
            int finalI1 = i;
            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_PenjualanItem",
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println("Nama Error : " + String.valueOf(error));

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


                    params.put("szDocId", szRefDocId);
                    params.put("intItemNumber", String.valueOf(finalI));
                    params.put("szProductId", adapter.getItem(finalI1).getSzId());

                    params.put("decQty", adapter.getItem(finalI1).getStock());
                    params.put("szUomId", adapter.getItem(finalI1).getSzUomId());



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
            RequestQueue requestQueue2 = Volley.newRequestQueue(summary_order.this);
            requestQueue2.add(stringRequest2);
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_upload_gambar",
                        new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response) {
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

                        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
                        String currentDateandTime2 = sdf2.format(new Date());

                        String[] parts = no_surat.split("-");
                        String restnomor = parts[0];
                        String restnomorbaru = restnomor.replace(" ", "");

                        params.put("iId", currentDateandTime2);
                        params.put("szId", mulai_perjalanan.id_pelanggan);

                        params.put("szImageType", "SALES");
                        params.put("szImage", ImageToString(signature_pad.getSignatureBitmap()));
                        params.put("szCustomerId", no_surat);

                        params.put("szBranchId", restnomorbaru);
                        params.put("szUserCreatedId", nik_baru);
                        params.put("szUserUpdatedId", nik_baru);

                        params.put("dtmCreated", currentDateandTime2);
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
                RequestQueue requestQueue2 = Volley.newRequestQueue(summary_order.this);
                requestQueue2.add(stringRequest2);

            }
        }, 500000);

        for(int i = 0;i <=  totalPenjualanPojos.size() - 1 ;i++){
            int finalI = i;
            int finalI1 = i;
            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_DocSoItem",
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println("Nama Error : " + String.valueOf(error));

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

                    int principla = Integer.parseInt(adapter.getItem(finalI1).getDisc_principle());
                    int distributor = Integer.parseInt(adapter.getItem(finalI1).getDisc_distributor());
                    int Internal = Integer.parseInt(adapter.getItem(finalI1).getDisc_internal());

                    params.put("szDocId", szRefDocId);
                    params.put("intItemNumber", String.valueOf(finalI));
                    params.put("szProductId", adapter.getItem(finalI1).getSzId());

                    params.put("decQty", adapter.getItem(finalI1).getStock());
                    params.put("decPrice", adapter.getItem(finalI1).getDecPrice() + ".0000");
                    params.put("decAmount", adapter.getItem(finalI1).getJumlah_harga() + ".0000");
                    params.put("decDiscount", String.valueOf(principla + distributor + Internal) + ".0000");

                    params.put("szUomId", adapter.getItem(finalI1).getSzUomId());


                    params.put("decDiscPrinciple", adapter.getItem(finalI1).getDisc_principle() + ".0000");
                    params.put("decDiscountDistributor", adapter.getItem(finalI1).getDisc_distributor() + ".0000");
                    params.put("decDiscountInternal", adapter.getItem(finalI1).getDisc_internal() + ".0000");





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
            RequestQueue requestQueue2 = Volley.newRequestQueue(summary_order.this);
            requestQueue2.add(stringRequest2);
        }
    }


    private String ImageToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    public class ListViewAdapterTotalPenjualan extends ArrayAdapter<total_penjualan_pojo> {
        private List<total_penjualan_pojo> totalPenjualanPojos;

        private Context context;

        public ListViewAdapterTotalPenjualan(List<total_penjualan_pojo> totalPenjualanPojos, Context context) {
            super(context, R.layout.list_total_penjualan, totalPenjualanPojos);
            this.totalPenjualanPojos = totalPenjualanPojos;
            this.context = context;
        }

        @SuppressLint("NewApi")
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = LayoutInflater.from(context);

            View listViewItem = inflater.inflate(R.layout.list_total_penjualan, null, true);

            TextView namaproduk = listViewItem.findViewById(R.id.namaproduk);
            TextView qty_order = listViewItem.findViewById(R.id.qty_order);
            TextView total = listViewItem.findViewById(R.id.total);
            ImageButton more = listViewItem.findViewById(R.id.more);

            total_penjualan_pojo data = totalPenjualanPojos.get(position);

            String[] parts = data.getDecPrice().split("\\.");
            String restnomor = parts[0];
            String restnomorbaru = restnomor.replace(" ", "");

            namaproduk.setText(data.getSzName());

            int harga = Integer.parseInt(data.getJumlah_harga());
            int totals = Integer.parseInt(restnomorbaru);

            DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
            DecimalFormatSymbols formatRp = new DecimalFormatSymbols();
            formatRp.setCurrencySymbol("Rp. ");
            formatRp.setMonetaryDecimalSeparator(',');
            formatRp.setGroupingSeparator('.');
            kursIndonesia.setDecimalFormatSymbols(formatRp);

            qty_order.setText("Order : " + data.getStock() + " x " + kursIndonesia.format(totals));


            total.setText(kursIndonesia.format(harga));

            more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showBottomSheetDialog(data.getSzName(), data.getStockawal(), data.getDisplay(), data.getExpired());
                }
            });




            return listViewItem;
        }
    }

    @Override
    public void onBackPressed() {
        totalPenjualanPojos.clear();
        adapter.notifyDataSetChanged();
        finish();
        super.onBackPressed();
    }

    private void showBottomSheetDialog(String szName, String stockawal, String display, String expired) {
        View view = getLayoutInflater().inflate(R.layout.detail_qty, null);

        TextView qty_stok = view.findViewById(R.id.stok);
        TextView qty_display = view.findViewById(R.id.display);
        TextView qty_expired = view.findViewById(R.id.expired);
        TextView barang = view.findViewById(R.id.barang);

        barang.setText(szName);
        qty_stok.setText(stockawal);
        qty_display.setText(display);
        qty_expired.setText(expired);

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

    public static String convertFormat(String inputDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy");
        Date date = null;
        try {
            date = simpleDateFormat.parse(inputDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date == null) {
            return "";
        }
        SimpleDateFormat convetDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return convetDateFormat.format(date);
    }
}