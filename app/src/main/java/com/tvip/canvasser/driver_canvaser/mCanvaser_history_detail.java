package com.tvip.canvasser.driver_canvaser;

import static android.view.View.GONE;

import static com.tvip.canvasser.driver_canvaser.mCanvaser_Terima_Produk.DocDo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
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
import com.tvip.canvasser.R;
import com.tvip.canvasser.menu_mulai_perjalanan.Utility;
import com.tvip.canvasser.menu_utama.MainActivity;
import com.tvip.canvasser.pojo.data_history_bkb_pojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

public class mCanvaser_history_detail extends AppCompatActivity {

    TextView tv_canvas_no_do, tv_canvas_no_std, tv_canvas_no_bkb, tv_canvas_no_so, drivertext, helpertext, tv_canvas_nama_customer;
    List<data_history_bkb_pojo> dataHistoryBkbPojoList = new ArrayList<>();

    TextView tv_diskon_format_rupiah, tv_total_harga_format_rupiah;
    ListViewAdapteProductPenjualan adapter;
    ListView listproduk;

    String alihcustomer;

    String DocDo, depo, jenispembayaran, totaldikurangdiskon;

    Button batal, print;
    private RequestQueue requestQueue36;

    SharedPreferences sharedPreferences;
    String PaymentTerm;
    SweetAlertDialog pDialog;

    private final Locale locale = new Locale("id", "ID");
    private final NumberFormat nf = NumberFormat.getCurrencyInstance(locale);

    LinearLayout linearbutton;

    // alihcustomer //
    TextView tv_alihcustomer, tv_alasan, keterangan;
    LinearLayout linearalihcustomer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mcanvaser_history_detail);

        tv_alihcustomer = findViewById(R.id.tv_alihcustomer);
        tv_alasan = findViewById(R.id.tv_alasan);
        keterangan = findViewById(R.id.keterangan);
        linearalihcustomer = findViewById(R.id.linearalihcustomer);

        tv_diskon_format_rupiah = findViewById(R.id.tv_diskon_format_rupiah);
        tv_total_harga_format_rupiah = findViewById(R.id.tv_total_harga_format_rupiah);
        tv_canvas_no_do = findViewById(R.id.tv_canvas_no_do);
        getJenis();

        batal = findViewById(R.id.bt_canvaser_detail_produk_kembali);
        print = findViewById(R.id.bt_canvaser_detail_produk_print);

        tv_canvas_no_std = findViewById(R.id.tv_canvas_no_std);
        tv_canvas_no_bkb = findViewById(R.id.tv_canvas_no_bkb);
        tv_canvas_no_so = findViewById(R.id.tv_canvas_no_so);
        drivertext = findViewById(R.id.drivertext);
        helpertext = findViewById(R.id.helpertext);
        tv_canvas_nama_customer = findViewById(R.id.tv_canvas_nama_customer);
        listproduk = findViewById(R.id.listproduk);
        linearbutton = findViewById(R.id.linearbutton2);

        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);

        if(getIntent().getStringExtra("Tunda").equals("1")){
            print.setText("Selesai Kunjungan");
            print.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    pDialog = new SweetAlertDialog(mCanvaser_history_detail.this, SweetAlertDialog.PROGRESS_TYPE);
                    pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                    pDialog.setTitleText("Harap Menunggu");
                    pDialog.setCancelable(false);
                    pDialog.show();
                    StringRequest stringRequest2 = new StringRequest(Request.Method.PUT, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_Status_Kunjungan",
                            new Response.Listener<String>() {

                                @Override
                                public void onResponse(String response) {
                                    pDialog.dismissWithAnimation();
                                    new SweetAlertDialog(mCanvaser_history_detail.this, SweetAlertDialog.SUCCESS_TYPE)
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

                            params.put("szDocId", getIntent().getStringExtra("STD"));
                            params.put("szCustomerId", getIntent().getStringExtra("szCustomerId"));
                            params.put("bPostPone", "0");
                            params.put("bFinisihed", "1");
                            params.put("bSuccess", "1");
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
                    RequestQueue requestQueue2 = Volley.newRequestQueue(mCanvaser_history_detail.this);
                    requestQueue2.add(stringRequest2);
                }
            });
        } else if (getIntent().getStringExtra("success").equals("0")){
            print.setVisibility(GONE);
        } else {
            print.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    print_do();
                    updateDO();
                }
            });
        }



        tv_canvas_nama_customer.setText(getIntent().getStringExtra("szName") + "," + getIntent().getStringExtra("szCustomerId"));
        tv_canvas_no_std.setText(getIntent().getStringExtra("STD"));

        getTermPayment(getIntent().getStringExtra("szCustomerId"));

        getSO();
        getDepo();



        batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });




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
            requestQueue36 = Volley.newRequestQueue(mCanvaser_history_detail.this);
            requestQueue36.add(stringRequest2);
        } else {
            requestQueue36.add(stringRequest2);
        }
    }

    private void getAlihCustomer() {
        StringRequest rest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_AlihCustomer?szDocId=" +DocDo,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        alihcustomer = "TRUE";

                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getString("status").equals("true")) {
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                        String customer = jsonObject1.getString("szName") + "," + jsonObject1.getString("szCustomerId_After");
                                        String reason = jsonObject1.getString("Alasan");
                                        String keteranganreason = jsonObject1.getString("szDescription");

                                        tv_alihcustomer.setText(customer);
                                        tv_alasan.setText(reason);
                                        keterangan.setText(keteranganreason);

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
                        linearalihcustomer.setVisibility(View.GONE);
                        alihcustomer = "FALSE";
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
        RequestQueue requestkota = Volley.newRequestQueue(mCanvaser_history_detail.this);
        requestkota.add(rest);
    }

    private void getJenis() {

    }

    private void getDepo() {
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

    private void print_do() {
        try {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH}, mCanvaser_Detail_Terima_Produk.PERMISSION_BLUETOOTH);
            } else {
                BluetoothConnection connection = BluetoothPrintersConnections.selectFirstPaired();
                if (connection != null) {
                    EscPosPrinter printer = new EscPosPrinter(connection, 203, 48f, 32);
                    StringBuilder sb = new StringBuilder();

                    SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yy");
                    String currentDateandTime2 = sdf2.format(new Date());

                    sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                    String nik_baru = sharedPreferences.getString("szDocCall", null);
                    String[] parts = nik_baru.split("-");
                    String branch = parts[0];

                    if (branch.equals("321") || branch.equals("336") || branch.equals("317") || branch.equals("'036'")) {
                        sb.append("[C]<b>PT. ADI SUKSES ABADI</b>\n");
                    } else {
                        sb.append("[C]<b>PT. TIRTA VARIA INTIPRATAMA</b>\n");
                    }

                    sb.append("[L]\n");
                    sb.append("[L]<b>No.DO           : "+DocDo+"</b>\n");
                    sb.append("[L]<b>Tanggal.DO      : "+currentDateandTime2+"</b>\n");

                    String namacustomer, idsales;
                    if(!alihcustomer.equals("TRUE")){
                        String[] parts3 = tv_canvas_nama_customer.getText().toString().split(",");
                        namacustomer = parts3[0];
                        idsales = parts3[1];
                    } else {
                        String[] parts3 = tv_alihcustomer.getText().toString().split(",");
                        namacustomer = parts3[0];
                        idsales = parts3[1];
                    }


                    sb.append("[L]<b>ID Customer     : </b>" + idsales +"\n");
                    sb.append("[L]<b>Nama Customer   : </b>" + namacustomer +"\n");

                    sb.append("[L]<b>ID Sales-Nama   : "+drivertext.getText().toString()+"</b>\n");

                    if(PaymentTerm.equals("0HARI")){
                        sb.append("[L]<b>Jenis Transaksi : </b>" + "TUNAI" +"\n");
                    } else {
                        sb.append("[L]<b>Jenis Transaksi : </b>" + "KREDIT" +"\n");
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

                    sb.append("[C]--------------------------------\n");
                    sb.append("[L]TOTAL[R]" + tv_total_harga_format_rupiah.getText().toString() + "\n");
                    sb.append("[L]DISCOUNT [R]" + tv_diskon_format_rupiah.getText().toString() + "\n");
                    sb.append("[L]TOTAL PEMBAYARAN [R]" + totaldikurangdiskon + "\n");
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

    private void getSO() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_SODetail?szDocId="+getIntent().getStringExtra("STD")+"&szCustomerId=" + getIntent().getStringExtra("szCustomerId") +"&bOutOfRoute=" + getIntent().getStringExtra("bOutOfRoute"), //szDocId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            int number = 0;
                            JSONObject obj = new JSONObject(response);
                            if (obj.getString("status").equals("true")) {
                                final JSONArray movieArray = obj.getJSONArray("data");
                                    final JSONObject movieObject = movieArray.getJSONObject(Integer.parseInt(getIntent().getStringExtra("nomor")));
                                    tv_canvas_no_so.setText(movieObject.getString("szDocSO"));
                                    historyBkb(movieObject.getString("szDocDO"));

                                    listPenjualan(movieObject.getString("szDocDO"));

                                    DocDo = movieObject.getString("szDocDO");
                                    tv_canvas_no_do.setText(DocDo);

                                getAlihCustomer();


                            }

//                            pDialog.dismissWithAnimation();

                        } catch(JSONException e){
                            e.printStackTrace();

                        }
                    }

                    private void historyBkb(String szDocSO) {
                        if (szDocSO.equals("")) {
                            sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                            String nik_baru = sharedPreferences.getString("szDocCall", null);

                            drivertext.setText(nik_baru + "-" + MainActivity.txt_nama.getText().toString());

                        } else {
                            StringRequest stringRequest2 = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_current_bkb_by_do?szDocDO=" + szDocSO,
                                    new Response.Listener<String>() {

                                        @Override
                                        public void onResponse(String response) {

                                            try {
                                                int jumlah_diskon = 0;
                                                int totalharga = 0;
                                                JSONObject obj = new JSONObject(response);
                                                final JSONArray movieArray = obj.getJSONArray("data");
                                                for (int i = 0; i < movieArray.length(); i++) {
                                                    final JSONObject movieObject = movieArray.getJSONObject(i);

                                                    tv_canvas_no_bkb.setText(movieObject.getString("nomor_bkb"));
                                                    jenispembayaran = movieObject.getString("jenis_pembayaran");


                                                    getBKB(movieObject.getString("nomor_bkb"));





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
                            RequestQueue requestQueue2 = Volley.newRequestQueue(mCanvaser_history_detail.this);
                            requestQueue2.add(stringRequest2);
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

    private void listPenjualan(String szDocDO) {
        StringRequest stringRequest2 = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_DocDo?szDocId=" + szDocDO,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

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

                                adapter = new ListViewAdapteProductPenjualan(dataHistoryBkbPojoList, getApplicationContext());
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

                                tv_diskon_format_rupiah.setText(kursIndonesia.format(jumlah_diskon));
                                tv_total_harga_format_rupiah.setText(kursIndonesia.format(totalharga));

                                totaldikurangdiskon = kursIndonesia.format(totalharga - jumlah_diskon);
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
        RequestQueue requestQueue2 = Volley.newRequestQueue(mCanvaser_history_detail.this);
        requestQueue2.add(stringRequest2);
    }

    private void getBKB(String nomor_bkb) {
        System.out.println("Link = " + "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_driver?id_bkb=" + nomor_bkb);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_driver?id_bkb=" + nomor_bkb, //szDocId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            int number = 0;
                            JSONObject obj = new JSONObject(response);
                            if (obj.getString("status").equals("true")) {
                                final JSONArray movieArray = obj.getJSONArray("data");
                                final JSONObject movieObject = movieArray.getJSONObject(movieArray.length() -1);


                                drivertext.setText(movieObject.getString("id_driver") + "-" + movieObject.getString("nama_driver"));
                                helpertext.setText(movieObject.getString("szHelper1") + "-" + movieObject.getString("nama_helper"));

                            }

//                            pDialog.dismissWithAnimation();

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

    public class ListViewAdapteProductPenjualan extends ArrayAdapter<data_history_bkb_pojo> {

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

        public ListViewAdapteProductPenjualan(List<data_history_bkb_pojo> data_terima_produk_pojos, Context context) {
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
    private void getTermPayment(String string_id_customer) {
        StringRequest stringRequest2 = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_Payment?szId=" + string_id_customer,
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
        RequestQueue requestQueue2 = Volley.newRequestQueue(mCanvaser_history_detail.this);
        requestQueue2.add(stringRequest2);
    }
}