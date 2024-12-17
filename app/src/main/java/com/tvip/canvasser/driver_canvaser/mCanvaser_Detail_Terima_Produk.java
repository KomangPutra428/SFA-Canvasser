package com.tvip.canvasser.driver_canvaser;

import static android.view.View.GONE;
import static com.tvip.canvasser.driver_canvaser.mCanvaser_Terima_Produk.DocDo;
import static com.tvip.canvasser.driver_canvaser.mCanvaser_Terima_Produk.chk_credit;
import static com.tvip.canvasser.driver_canvaser.mCanvaser_Terima_Produk.draft;
import static com.tvip.canvasser.driver_canvaser.mCanvaser_Terima_Produk.next;
import static com.tvip.canvasser.driver_canvaser.mCanvaser_Terima_Produk.tv_diskon_format_rupiah;
import static com.tvip.canvasser.menu_mulai_perjalanan.menu_pelanggan.idcustomer;
import static com.tvip.canvasser.menu_mulai_perjalanan.mulai_perjalanan.STD;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dantsu.escposprinter.EscPosPrinter;
import com.dantsu.escposprinter.connection.bluetooth.BluetoothConnection;
import com.dantsu.escposprinter.connection.bluetooth.BluetoothPrintersConnections;
import com.tvip.canvasser.Perangkat.HttpsTrustManager;
import com.tvip.canvasser.R;
import com.tvip.canvasser.menu_login.login;
import com.tvip.canvasser.menu_mulai_perjalanan.Utility;
import com.tvip.canvasser.menu_mulai_perjalanan.menu_pelanggan;
import com.tvip.canvasser.menu_selesai_perjalanan.detail_selesai;
import com.tvip.canvasser.pojo.data_history_bkb_pojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class mCanvaser_Detail_Terima_Produk extends AppCompatActivity {
    private RequestQueue requestQueue;
    private RequestQueue requestQueue36;
    SweetAlertDialog pDialog, alert;
    Button bt_canvaser_kembali, bt_canvaser_print;
    TextView tv_canvas_nama_customer, tv_canvas_catatan, tv_canvas_total_harga, tv_canvas_total_harga_row,
            tv_canvas_qty_row, tv_canvas_qty_row1, tv_tanggal_diterima, tv_canvas_no_std, tv_canvas_no_bkb,
            drivertext, helpertext, tv_canvas_no_so, diskon, total_pembayaran, tv_canvas_no_do;
    String PaymentTerm;

    List<data_history_bkb_pojo> dataHistoryBkbPojoList = new ArrayList<>();

    public static final int PERMISSION_BLUETOOTH = 1;

    private final Locale locale = new Locale("id", "ID");
    private final DateFormat df = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss", locale);
    private final NumberFormat nf = NumberFormat.getCurrencyInstance(locale);

    SharedPreferences sharedPreferences;
    ListView listproduk;

    String depo;

    ListViewAdapteProductPenjualanAfter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mcanvaser_detail_terima_produk);
        HttpsTrustManager.allowAllSSL();
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);

        diskon = findViewById(R.id.tv_diskon_format_rupiah);
        total_pembayaran = findViewById(R.id.total_pembayaran);

        tv_canvas_no_do = findViewById(R.id.tv_canvas_no_do);
        
        bt_canvaser_kembali = findViewById(R.id.bt_canvaser_detail_produk_kembali);
        bt_canvaser_print = findViewById(R.id.bt_canvaser_detail_produk_print);
        tv_canvas_nama_customer = findViewById(R.id.tv_canvas_nama_customer);
        tv_canvas_catatan = findViewById(R.id.tv_catatan);
        tv_canvas_total_harga = findViewById(R.id.tv_total_harga_format_rupiah);
        tv_canvas_total_harga_row = findViewById(R.id.tv_total_harga_row_format_rupiah);
        tv_canvas_qty_row = findViewById(R.id.qty_order);
        tv_canvas_qty_row1 = findViewById(R.id.qty_order1);
        tv_tanggal_diterima = findViewById(R.id.tv_tanggal_diterima);
        tv_canvas_no_std = findViewById(R.id.tv_canvas_no_std);
        tv_canvas_no_bkb = findViewById(R.id.tv_canvas_no_bkb);

        drivertext = findViewById(R.id.drivertext);
        helpertext = findViewById(R.id.helpertext);
        tv_canvas_no_so = findViewById(R.id.tv_canvas_no_so);
        listproduk = findViewById(R.id.listproduk);

        tv_canvas_nama_customer.setText(getIntent().getStringExtra("idCustomer"));
        tv_canvas_catatan.setText(getIntent().getStringExtra("catatan"));
        tv_canvas_total_harga_row.setText(getIntent().getStringExtra("totalhargarow"));
        tv_canvas_qty_row.setText(getIntent().getStringExtra("totalorderqty"));
        tv_canvas_qty_row1.setText(getIntent().getStringExtra("totalorderqty"));

        getTermPayment();

        tv_canvas_no_so.setText(mCanvaser_Terima_Produk.tv_canvas_no_so.getText().toString());

        getHelper();

        getDepo();
        
        updateBcash();
        updateBcashSO();

        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf2 = new SimpleDateFormat("EEEE, dd MMMM yyyy");
        String currentDateandTime2 = sdf2.format(new Date());

        tv_tanggal_diterima.setText(currentDateandTime2);
        tv_canvas_no_std.setText(STD);
        tv_canvas_no_bkb.setText(mCanvaser_Terima_Produk.tv_canvas_no_bkb.getText().toString());

        bt_canvaser_kembali.setOnClickListener(view -> {
            String[] parts = tv_canvas_nama_customer.getText().toString().split(",");
            String id_cs = parts[1];
            mCanvaser_Terima_Produk.adapter.clear();
            Intent i = new Intent(mCanvaser_Detail_Terima_Produk.this, menu_pelanggan.class);
            i.putExtra("kode", idcustomer.getText().toString());
            i.putExtra("szDocDo", tv_canvas_no_do.getText().toString());

            startActivity(i);
            finish();
        });

        bt_canvaser_print.setOnClickListener(view -> {
            pDialog = new SweetAlertDialog(mCanvaser_Detail_Terima_Produk.this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Harap Menunggu");
            pDialog.setCancelable(false);
            pDialog.show();
            StringRequest stringRequest2 = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_DocDo?szDocId=" + DocDo,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            pDialog.dismissWithAnimation();
                            print_do();
                            updateDO();
                            Toast.makeText(mCanvaser_Detail_Terima_Produk.this, "OK PRINT", Toast.LENGTH_SHORT).show();



                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    pDialog.dismissWithAnimation();
                    alert = new SweetAlertDialog(mCanvaser_Detail_Terima_Produk.this, SweetAlertDialog.ERROR_TYPE);
                    alert.setCancelable(false);
                    alert.setContentText("Print gagal, inputan data tidak lengkap. Silahkan coba input ulang.")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                    DocDo = menu_pelanggan.DocDo;
                                    Intent intent = new Intent(getApplicationContext(), mCanvaser_Terima_Produk.class);
                                    startActivity(intent);
                                }
                            });
                    alert.show();
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
            RequestQueue requestQueue2 = Volley.newRequestQueue(mCanvaser_Detail_Terima_Produk.this);
            requestQueue2.add(stringRequest2);
        });

        tv_canvas_no_do.setText(DocDo);
        getDetailPenjualan(DocDo);

    }

    private void updateDO() {
        StringRequest stringRequest2 = new StringRequest(Request.Method.PUT, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_DocSo",
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

                params.put("szDocId", DocDo);
                params.put("dtmCustomerPO", currentDateandTime2);



                return params;
            }

        };
        stringRequest2.setRetryPolicy(
                new DefaultRetryPolicy(
                        10000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
        );
        if (requestQueue36 == null) {
            requestQueue36 = Volley.newRequestQueue(mCanvaser_Detail_Terima_Produk.this);
            requestQueue36.add(stringRequest2);
        } else {
            requestQueue36.add(stringRequest2);
        }
    }


    private void getDetailPenjualan(String docDo) {
        pDialog = new SweetAlertDialog(mCanvaser_Detail_Terima_Produk.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Harap Menunggu");
        pDialog.setCancelable(false);
        pDialog.show();
        StringRequest stringRequest2 = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_DocDo?szDocId=" + docDo,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        pDialog.dismissWithAnimation();

                        try {
                            int jumlah_diskon = 0;
                            int totalharga = 0;
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");
                            for (int i = 0; i < movieArray.length(); i++) {
                                final JSONObject movieObject = movieArray.getJSONObject(i);

                                final data_history_bkb_pojo movieItem = new data_history_bkb_pojo(
                                        movieObject.getString("szDocId"),
                                        movieObject.getString("szName"),
                                        movieObject.getString("szProductId"),
                                        movieObject.getString("decPrice"),
                                        movieObject.getString("decQty"),
                                        movieObject.getString("decAmount"),
                                        movieObject.getString("decDiscPrinciple"),
                                        movieObject.getString("decDiscDistributor"),
                                        movieObject.getString("decDiscInternal")); //digunakan untuk edit qty


                                dataHistoryBkbPojoList.add(movieItem);

                                adapter = new ListViewAdapteProductPenjualanAfter(dataHistoryBkbPojoList, getApplicationContext());
                                listproduk.setAdapter(adapter);
                                Utility.setListViewHeightBasedOnChildren(listproduk);

                                jumlah_diskon += Integer.parseInt(movieObject.getString("decDiscDistributor")) + Integer.parseInt(movieObject.getString("decDiscInternal")) + Integer.parseInt(movieObject.getString("decDiscPrinciple"));
                                totalharga += Integer.parseInt(movieObject.getString("decPrice")) * Integer.parseInt(movieObject.getString("decQty"));



                                DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
                                DecimalFormatSymbols formatRp = new DecimalFormatSymbols();
                                formatRp.setCurrencySymbol("Rp. ");
                                formatRp.setMonetaryDecimalSeparator(',');
                                formatRp.setGroupingSeparator('.');
                                kursIndonesia.setDecimalFormatSymbols(formatRp);

                                diskon.setText(kursIndonesia.format(jumlah_diskon));
                                tv_canvas_total_harga.setText(kursIndonesia.format(totalharga));

                                String totaldikurangdiskon = kursIndonesia.format(totalharga - jumlah_diskon);

                                total_pembayaran.setText(totaldikurangdiskon);
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
        RequestQueue requestQueue2 = Volley.newRequestQueue(mCanvaser_Detail_Terima_Produk.this);
        requestQueue2.add(stringRequest2);
    }

    private void updateBcashSO() {
        StringRequest stringRequest2 = new StringRequest(Request.Method.PUT, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_DocSobCash",
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

                params.put("szDocId", tv_canvas_no_so.getText().toString());

                if(chk_credit.isChecked()){
                    params.put("bCash", "0");
                } else {
                    params.put("bCash", "1");
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
        RequestQueue requestQueue2 = Volley.newRequestQueue(mCanvaser_Detail_Terima_Produk.this);
        requestQueue2.add(stringRequest2);
    }

    private void updateBcash() {
        StringRequest stringRequest2 = new StringRequest(Request.Method.PUT, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_DocDObCash",
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

                params.put("szDocId", tv_canvas_no_do.getText().toString());

                if(chk_credit.isChecked()){
                    params.put("bCash", "0");
                } else {
                    params.put("bCash", "1");
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
        RequestQueue requestQueue2 = Volley.newRequestQueue(mCanvaser_Detail_Terima_Produk.this);
        requestQueue2.add(stringRequest2);
    }

    private void getDO() {
        String[] parts = getIntent().getStringExtra("idCustomer").split(",");
        String szIdSlice = parts[1];
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_SO?szDocId="+STD+"&szCustomerId=" + szIdSlice, //szDocId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            int number = 0;
                            JSONObject obj = new JSONObject(response);
                            if (obj.getString("status").equals("true")) {
                                final JSONArray movieArray = obj.getJSONArray("data");
                                final JSONObject movieObject = movieArray.getJSONObject(next);





                            }


                        } catch(JSONException e){
                            e.printStackTrace();

                        }
                    }


                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        getDO();
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

    private void getDepo() {
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String nik_baru = sharedPreferences.getString("szDocCall", null);
        String[] parts = nik_baru.split("-");
        String restnomor = parts[0];
        @SuppressLint("SetTextI18n") StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/master/lokasi/index_kodenikdepo?kode_dms=" + restnomor, //szDocId,
                response -> {


                    try {
                        JSONObject obj = new JSONObject(response);
                        if (obj.getString("status").equals("true")) {
                            final JSONArray movieArray = obj.getJSONArray("data");
                            final JSONObject movieObject = movieArray.getJSONObject(movieArray.length()-1);

                            depo = movieObject.getString("depo_nama");


                        }


                    } catch(JSONException e){
                        e.printStackTrace();

                    }
                },
                error -> {

                })
        {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> params = new HashMap<>();
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

    private void getHelper() {
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        @SuppressLint("SetTextI18n") StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_BKB?id_std=" + mCanvaser_Terima_Produk.tv_canvas_no_std.getText().toString(), //szDocId,
                response -> {


                    try {
                        JSONObject obj = new JSONObject(response);
                        if (obj.getString("status").equals("true")) {
                            final JSONArray movieArray = obj.getJSONArray("data");
                            final JSONObject movieObject = movieArray.getJSONObject(movieArray.length()-1);

                            drivertext.setText(movieObject.getString("id_driver") + "-" + movieObject.getString("nama_driver"));
                            helpertext.setText(movieObject.getString("szHelper1") + "-" + movieObject.getString("nama_helper"));


                        }


                    } catch(JSONException e){
                        e.printStackTrace();

                    }
                },
                error -> {

                })
        {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> params = new HashMap<>();
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
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(mCanvaser_Detail_Terima_Produk.this);
            requestQueue.add(stringRequest);
        } else {
            requestQueue.add(stringRequest);
        }
    }


    public void print_do(){

        try {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH}, mCanvaser_Detail_Terima_Produk.PERMISSION_BLUETOOTH);
            } else {
                BluetoothConnection connection = BluetoothPrintersConnections.selectFirstPaired();
                if (connection != null) {
                    EscPosPrinter printer = new EscPosPrinter(connection, 203, 48f, 32);
                    StringBuilder sb = new StringBuilder();
                    sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                    String nik_baru = sharedPreferences.getString("szDocCall", null);
                    String[] parts = nik_baru.split("-");
                    String branch = parts[0];

                    if (branch.equals("321") || branch.equals("336") || branch.equals("317") || branch.equals("'036'")) {
                        sb.append("[C]<b>PT. ADI SUKSES ABADI</b>\n");
                    } else {
                        sb.append("[C]<b>PT. TIRTA VARIA INTIPRATAMA</b>\n");
                    }

                    SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yy");
                    String currentDateandTime2 = sdf2.format(new Date());

                    sb.append("[L]\n");
                    sb.append("[L]<b>No.DO          : "+ DocDo+"</b>\n");
                    sb.append("[L]<b>Tanggal.DO     : "+currentDateandTime2+"</b>\n");

                    String[] parts3 = tv_canvas_nama_customer.getText().toString().split(",");
                    String namacustomer = parts3[0];
                    String idsales = parts3[1];

                    sb.append("[L]<b>ID Customer     : </b>" + idsales +"\n");
                    sb.append("[L]<b>Nama Customer   : </b>" + namacustomer +"\n");

                    sb.append("[L]<b>ID Sales-Nama   : "+drivertext.getText().toString()+"</b>\n");

                    if(chk_credit.isChecked()){
                        sb.append("[L]<b>Jenis Transaksi : </b>" + "KREDIT" +"\n");
                    } else {
                        sb.append("[L]<b>Jenis Transaksi : </b>" + "TUNAI" +"\n");
                    }

                    sb.append("[C]================================\n");


                    for(int i = 0; i <= dataHistoryBkbPojoList.size() -1; i++){
                        String[] decprice = dataHistoryBkbPojoList.get(i).getDecPrice().split("\\.");
                        String restnomordecprice = decprice[0];
                        String editqty = dataHistoryBkbPojoList.get(i).getDecQty();

                        int totalharga = Integer.parseInt(restnomordecprice) * Integer.parseInt(editqty);

                        if(!dataHistoryBkbPojoList.get(i).getDecQty().equals("0")){
                            sb.append("[L]<b>"+dataHistoryBkbPojoList.get(i).getSzName()+"</b>\n");
                            sb.append("[L]    " + dataHistoryBkbPojoList.get(i).getDecQty() + " Qty "+  nf.format(totalharga) + "\n");
                        }
                    }

                    DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
                    DecimalFormatSymbols formatRp = new DecimalFormatSymbols();
                    formatRp.setCurrencySymbol("Rp. ");
                    formatRp.setMonetaryDecimalSeparator(',');
                    formatRp.setGroupingSeparator('.');
                    kursIndonesia.setDecimalFormatSymbols(formatRp);

                    sb.append("[C]--------------------------------\n");
                    sb.append("[L]TOTAL[R]" + tv_canvas_total_harga.getText().toString() + "\n");
                    sb.append("[L]DISCOUNT [R]" + diskon.getText().toString() + "\n");
                    sb.append("[L]TOTAL PEMBAYARAN [R]" + total_pembayaran.getText().toString() + "\n");
                    sb.append("[L]HARGA SUDAH TERMASUK PPN [R]" + "\n");

                    sb.append("[C]--------------------------------\n");
                    sb.append("[C]\n");
                    sb.append("[C]--------------------------------\n");
                    sb.append("[L]TANDA TANGAN\n");
                    sb.append("\n");
                    sb.append("\n");
                    sb.append("\n");
                    sb.append("[L]Customer                  Sales[R]\n");
                    sb.append("\n");
                    sb.append("[C]TERIMA KASIH\n");
                    sb.append("[C]--------------------------------\n");
                    sb.append("[L]\n");
                    sb.append("[L]\n");
//                                    "[L]TAX [R]" + nf.format(7650) + "\n" +
//                                    "[L]<b>GRAND TOTAL[R]" + tv_canvas_total_harga.getText().toString() + "</b>\n" +



                    printer.printFormattedText(String.valueOf(sb));
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
        mCanvaser_Terima_Produk.adapter.clear();
        String[] parts = tv_canvas_nama_customer.getText().toString().split(",");
        String id_cs = parts[1];

        Intent i = new Intent(mCanvaser_Detail_Terima_Produk.this, menu_pelanggan.class);
        i.putExtra("kode", idcustomer.getText().toString());
        i.putExtra("szDocDo", tv_canvas_no_do.getText().toString());
        startActivity(i);
        finish();

    }

    private void getTermPayment() {
        String[] parts = getIntent().getStringExtra("idCustomer").split(",");
        String szIdSlice = parts[1];
        StringRequest stringRequest2 = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_Payment?szId=" + szIdSlice,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");
                            for (int i = 0; i < movieArray.length(); i++) {
                                final JSONObject movieObject = movieArray.getJSONObject(i);

                                PaymentTerm = movieObject.getString("szPaymetTermId");




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
        RequestQueue requestQueue2 = Volley.newRequestQueue(mCanvaser_Detail_Terima_Produk.this);
        requestQueue2.add(stringRequest2);
    }

    public class ListViewAdapteProductPenjualanAfter extends ArrayAdapter<data_history_bkb_pojo> {

        private class ViewHolder {
            TextView namaproduk, qty_order, total_harga, harga_satuan, diskon_tiv, diskon_distributor, diskon_internal;
            TextView intItemNumber, szOrderItemTypeId, bTaxable, szTaxId, decTaxRate, decTax, decDpp, tv_qty_produk;
            TextView total_harga_row_format_rupiah, harga_satuan_format_rupiah, diskon_tiv_format_rupiah, diskon_distributor_format_rupiah,
                    diskon_internal_format_rupiah;

            LinearLayout linear_detail_terima_produk, qtyaslilayout;
            RelativeLayout buttonExpand;
            ImageButton bt_plus, bt_minus, bt_edit_qty_produk;
            ImageButton bt_hapus;

            TextView qty_order_asli;
            TextView decTax_setelah_edit, decDpp_setelah_edit;
            TextView tax;
        }

        List<data_history_bkb_pojo> data_terima_produk_pojos;
        private Context context;

        public ListViewAdapteProductPenjualanAfter(List<data_history_bkb_pojo> data_terima_produk_pojos, Context context) {
            super(context, R.layout.list_terima_produk, data_terima_produk_pojos);
            this.data_terima_produk_pojos = data_terima_produk_pojos;
            this.context = context;
        }

        public int getCount() {
            return data_terima_produk_pojos.size();
        }

        public data_history_bkb_pojo getItem(int position) {
            return data_terima_produk_pojos.get(position);
        }

        @Override
        public int getViewTypeCount() {
            int count;
            if (data_terima_produk_pojos.size() > 0) {
                count = getCount();
            } else {
                count = 1;
            }
            return count;
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        public long getItemId(int position) {
            return 0;
        }

        @SuppressLint("NewApi")
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder viewHolder;
            data_history_bkb_pojo data = getItem(position);

            if (convertView == null) {
                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.list_terima_produk, parent, false);

                viewHolder.namaproduk = (TextView) convertView.findViewById(R.id.namaproduk);
                viewHolder.qty_order = (TextView) convertView.findViewById(R.id.qty_order); //qty yang berubah ketika diedit
                viewHolder.total_harga = (TextView) convertView.findViewById(R.id.tv_total_harga); //total harga per 1 produk/ 1 row/ 1lsitview

                viewHolder.harga_satuan = (TextView) convertView.findViewById(R.id.tv_list_terima_produk_harga_satuan);
                viewHolder.diskon_tiv = (TextView) convertView.findViewById(R.id.tv_list_terima_produk_diskon_tiv);
                viewHolder.diskon_distributor = (TextView) convertView.findViewById(R.id.tv_list_terima_produk_diskon_distributor);
                viewHolder.diskon_internal = (TextView) convertView.findViewById(R.id.tv_list_terima_produk_diskon_internal);

                viewHolder.intItemNumber = (TextView) convertView.findViewById(R.id.tv_intItemNumber_body_data);
                viewHolder.szOrderItemTypeId = (TextView) convertView.findViewById(R.id.tv_szOrderItemTypeId_body_data);
                viewHolder.bTaxable = (TextView) convertView.findViewById(R.id.tv_bTaxable_body_data);
                viewHolder.szTaxId = (TextView) convertView.findViewById(R.id.tv_szTaxId_body_data);
                viewHolder.decTaxRate = (TextView) convertView.findViewById(R.id.tv_decTaxRate_body_data);
                viewHolder.decTax = (TextView) convertView.findViewById(R.id.tv_decTax_body_data); //decTax Asli, tanpa perubahan
                viewHolder.decDpp = (TextView) convertView.findViewById(R.id.tv_decDpp_body_data); //decDpp Asli, tanpa perubahan

                viewHolder.total_harga_row_format_rupiah = (TextView) convertView.findViewById(R.id.tv_total_harga_row_format_rupiah);
                viewHolder.harga_satuan_format_rupiah = (TextView) convertView.findViewById(R.id.tv_list_terima_produk_harga_satuan_format_rupiah);
                viewHolder.diskon_tiv_format_rupiah = (TextView) convertView.findViewById(R.id.tv_list_terima_produk_diskon_tiv_format_rupiah);
                viewHolder.diskon_distributor_format_rupiah = (TextView) convertView.findViewById(R.id.tv_list_terima_produk_diskon_distributor_format_rupiah);
                viewHolder.diskon_internal_format_rupiah = (TextView) convertView.findViewById(R.id.tv_list_terima_produk_diskon_internal_format_rupiah);


                viewHolder.bt_plus = (ImageButton) convertView.findViewById(R.id.bt_plus);
                viewHolder.bt_minus = (ImageButton) convertView.findViewById(R.id.bt_minus);
                viewHolder.tv_qty_produk = (TextView) convertView.findViewById(R.id.tv_qty_produk);
                viewHolder.bt_hapus = (ImageButton) convertView.findViewById(R.id.bt_hapus);

                viewHolder.qty_order_asli = (TextView) convertView.findViewById(R.id.qty_order_asli); //qty tetap/tidak berubah, asli dari mdba
                viewHolder.linear_detail_terima_produk = (LinearLayout) convertView.findViewById(R.id.linear_detail_terima_produk);
                viewHolder.bt_edit_qty_produk = (ImageButton) convertView.findViewById(R.id.bt_edit_qty_produk);

                viewHolder.decTax_setelah_edit = (TextView) convertView.findViewById(R.id.tv_decTax_body_data_after_edit_qty); //decTax, jika dilakukan perubahan qty per row, maka akan berubah jugaa nilainya
                viewHolder.decDpp_setelah_edit = (TextView) convertView.findViewById(R.id.tv_decDpp_body_data_after_edit_qty); //decDpp, jika dilakukan perubahan qty per row, maka akan berubah jugaa nilainya

                //viewHolder.refresh = (ImageButton) convertView.findViewById(R.id.refresh);
                viewHolder.buttonExpand = (RelativeLayout) convertView.findViewById(R.id.button_expand_listview);

                viewHolder.tax = (TextView) convertView.findViewById(R.id.tv_tax_body_data);

                convertView.setTag(viewHolder);

                viewHolder.qtyaslilayout = (LinearLayout) convertView.findViewById(R.id.qtyaslilayout);

            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.qtyaslilayout.setVisibility(GONE);
            DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
            DecimalFormatSymbols formatRp = new DecimalFormatSymbols();
            formatRp.setCurrencySymbol("Rp. ");
            formatRp.setMonetaryDecimalSeparator(',');
            formatRp.setGroupingSeparator('.');
            kursIndonesia.setDecimalFormatSymbols(formatRp);
            viewHolder.bt_hapus.setVisibility(GONE);
            int totalqty = Integer.parseInt(data.getDecPrice()) * Integer.parseInt(data.getDecQty());

            viewHolder.total_harga_row_format_rupiah.setText(kursIndonesia.format(totalqty));

            viewHolder.namaproduk.setText(data.getSzName());
            viewHolder.qty_order_asli.setText(data.getDecQty());


            viewHolder.diskon_tiv_format_rupiah.setText(kursIndonesia.format(Integer.parseInt(data.getDecDiscPrinciple())));

            viewHolder.diskon_distributor_format_rupiah.setText(kursIndonesia.format(Integer.parseInt(data.getDecDiscDistributor())));

            viewHolder.diskon_internal_format_rupiah.setText(kursIndonesia.format(Integer.parseInt(data.getDecDiscInternal())));


            viewHolder.qty_order.setText(data.getDecQty());



            viewHolder.harga_satuan_format_rupiah.setText(kursIndonesia.format(Integer.parseInt(data.getDecPrice())));

            viewHolder.linear_detail_terima_produk.setVisibility(View.VISIBLE);
            viewHolder.buttonExpand.setVisibility(GONE);

            viewHolder.bt_edit_qty_produk.setVisibility(GONE);




            return convertView;
        }
    }
}