package com.tvip.canvasser.driver_canvaser;

import static android.view.View.GONE;
import static com.tvip.canvasser.menu_mulai_perjalanan.menu_pelanggan.idcustomer;
import static com.tvip.canvasser.menu_mulai_perjalanan.menu_pelanggan.pengaturanBar;
import static com.tvip.canvasser.menu_mulai_perjalanan.menu_pelanggan.textpenjualan;
import static com.tvip.canvasser.menu_mulai_perjalanan.mulai_perjalanan.condition;


import static com.tvip.canvasser.menu_mulai_perjalanan.mulai_perjalanan.STD;
import static com.tvip.canvasser.menu_mulai_perjalanan.mulai_perjalanan.pelanggan;

import static com.tvip.canvasser.menu_utama.MainActivity.cityName;
import static com.tvip.canvasser.menu_utama.MainActivity.latitude;
import static com.tvip.canvasser.menu_utama.MainActivity.longitude;

import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.tvip.canvasser.Perangkat.ExceptionHandler;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.tvip.canvasser.Perangkat.HttpsTrustManager;
import com.tvip.canvasser.R;
import com.tvip.canvasser.menu_mulai_perjalanan.GPSTracker;
import com.tvip.canvasser.menu_mulai_perjalanan.Utility;
import com.tvip.canvasser.menu_mulai_perjalanan.menu_pelanggan;
import com.tvip.canvasser.menu_mulai_perjalanan.mulai_perjalanan;
import com.tvip.canvasser.menu_utama.MainActivity;
import com.tvip.canvasser.pojo.data_product_bkb_pojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class mCanvaser_Terima_Produk extends AppCompatActivity {

    String statusLoop;

    ArrayList<String> Reason_Selesai_Perjalanan = new ArrayList<>();
    private RequestQueue requestQueue;
    private RequestQueue requestQueue2;
    private RequestQueue requestQueue3;
    private RequestQueue requestQueue4;

    private RequestQueue requestQueue5;
    private RequestQueue requestQueue6;
    private RequestQueue requestQueue7;
    private RequestQueue requestQueue8;

    private RequestQueue requestQueue9;
    private RequestQueue requestQueue10;
    private RequestQueue requestQueue11;
    private RequestQueue requestQueue12;

    private RequestQueue requestQueue13;
    private RequestQueue requestQueue14;
    private RequestQueue requestQueue15;
    private RequestQueue requestQueue16;

    private RequestQueue requestQueue17;
    private RequestQueue requestQueue18;
    private RequestQueue requestQueue19;
    private RequestQueue requestQueue20;

    private RequestQueue requestQueue21;
    private RequestQueue requestQueue22;
    private RequestQueue requestQueue23;
    private RequestQueue requestQueue24;

    private RequestQueue requestQueue25;
    private RequestQueue requestQueue26;
    private RequestQueue requestQueue27;
    private RequestQueue requestQueue28;

    private RequestQueue requestQueue29;
    private RequestQueue requestQueue30;
    private RequestQueue requestQueue31;
    private RequestQueue requestQueue32;

    private RequestQueue requestQueue33;
    private RequestQueue requestQueue34;
    private RequestQueue requestQueue35;
    private RequestQueue requestQueue36;

    private RequestQueue requestQueue37;
    private RequestQueue requestQueue38;
    private RequestQueue requestQueue39;
    private RequestQueue requestQueue40;

    private RequestQueue requestQueue41;
    private RequestQueue requestQueue42;
    private RequestQueue requestQueue43;
    private RequestQueue requestQueue44;
    private RequestQueue requestQueue45;
    private RequestQueue requestQueue47;
    private RequestQueue requestQueue48;



    private RequestQueue requestQueue50;
    private RequestQueue requestQueue55;
    private RequestQueue requestQueue56;






    Button bt_canvaser_submit, bt_canvaser_batal;
    SweetAlertDialog pDialog, Success;
    RelativeLayout buttonExpand, buttonExpand1;
    LinearLayout linear_detail_terima_produk, linear_detail_terima_produk1;

    static String PaymentTerm;
    String bAllowToCredit;

    String currentDateandTime;

    AutoCompleteTextView act_pilihCustomer;

    String tax, string_header_ritase, string_nodo;
    static String DocDo, SOInduk2;

    ArrayList<String> array_pilihan_customer;

    EditText edt_jumlah_tunai, edt_jumlah_transfer;
    Button batal, lanjutkan, bt_camera, bt_konfirmasi, bt_camera_bukti_transfer;
    TextView delivery_order, tv_totalharga, tv_totaldiskon, tv_status;

    ContentValues cv;
    Uri imageUri;
    ImageView img, img_bukti_transfer;
    String encodeImageString, string_harga_satuan, string_total_harga_row, string_diskon_tiv, string_diskon_distributor, string_diskon_internal;

    String string_ppn, string_dpp;
    String currentDate;
    String string_jenis_pembyaran;

    TextInputLayout txt_input_jumlah_transfer, txt_input_jumlah_tunai;
    LinearLayout linear_keterangan_photo;

    private SimpleDateFormat dateFormatter;
    private Calendar date;
    SearchView cariproduct;

    SharedPreferences sharedPreferences;

    AutoCompleteTextView pilihpembayaran;
    EditText tv_canvaser_catatan;
    ImageButton refresh;

    String driver, szHelper1, szVehicleId, id_pb, mixornot;
    ArrayList<String> pilih_ritase = new ArrayList<>();

    ArrayList<String> driver_list = new ArrayList<>();
    ArrayList<String> szHelper1_list = new ArrayList<>();
    ArrayList<String> szVehicleId_list = new ArrayList<>();
    ArrayList<String> id_pb_list = new ArrayList<>();
    ArrayList<String> ritase_list = new ArrayList<>();
    ArrayList<String> nodot = new ArrayList<>();
    ArrayList<String> refstd = new ArrayList<>();

    ImageView uploadgambar;
    Bitmap bitmap, bitmap2, bitmap_bukti_transfer;
    TextView tv_tanggal, tv_alamat, tv_longlat, tv_no_std, tv_no_customer, tv_ppn, tv_dpp;
    TextView qty_order_asli, qty_order_asli1, qty_order_sudah_edit, qty_order_sudah_edit1;

    GPSTracker gps;

    String Status;

    static CheckBox chk_tunai;
    static CheckBox chk_transfer;
    static CheckBox chk_credit;
    int jenis_pembayaran = 0;

    ImageView img1,img2,img3,img4,img5,img6,img7,img8,img9,img10;

    TextView total_jenis_pembayaran, tv_submit_loading,
            tv_terima_produk, tv_totalharga_format_rupiah, tv_totaldiskon_format_rupiah,
            tv_total_jenis_pembayaran_rupiah,tv_total_jenis_pembayaran_text, tv_harga_satuan,
            tv_total_harga_row_format_rupiah, qty_order_asli4;

    Button bt_submit_berhasil;
    RelativeLayout relative_foto_bukti_transfer;
    ImageButton bt_edit_qty_produk, bt_edit_qty_produk1, bt_edit_qty_produk2;

    static TextView tv_canvas_no_std;
    static TextView tv_canvas_no_bkb;
    static TextView tv_canvas_no_so;

    static List<data_product_bkb_pojo> data_terima_produk_pojos = new ArrayList<>();
    static List<data_product_bkb_pojo> draft = new ArrayList<>();

    static ListViewAdapteProductPenjualan adapter;

    static ListViewAdapteProductDraft adapter2;

    static ListView listproduk, list_produk_ingin_dicanvas;
    static TextView tv_diskon_format_rupiah, tv_total_harga_format_rupiah, total_pembayaran;
    TextView tv_tanggal_diterima;

    static int total_harga, total_diskon;

    String soInduk;
    AutoCompleteTextView act_pilih_ritase, act_pilih_customer, act_alih_customer, act_pilih_alasan;
    CheckBox checkalihcustomer;

    EditText tv_canvaser_catatan_alih;

    LinearLayout linear_alihcustomer;

    Dialog dialog;

    public static int next;
    private static byte[] reserve = new byte[1024 * 1024]; // Reserves 1MB.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mcanvaser_terima_produk);
        HttpsTrustManager.allowAllSSL();

        statusLoop = "gagal";

        getSTD();

        getTax();

        tv_total_jenis_pembayaran_rupiah = findViewById(R.id.tv_total_jenis_pembayaran_rupiah);
        tv_tanggal_diterima = findViewById(R.id.tv_tanggal_diterima);
        listproduk = findViewById(R.id.listproduk);

        SimpleDateFormat sdf2 = new SimpleDateFormat("EEEE, dd MMMM yyyy");
        String currentDateandTime2 = sdf2.format(new Date());
        tv_tanggal_diterima.setText(currentDateandTime2);


        pDialog = new SweetAlertDialog(mCanvaser_Terima_Produk.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Harap Menunggu");
        pDialog.setCancelable(false);
        pDialog.show();

        tv_total_harga_format_rupiah = findViewById(R.id.tv_total_harga_format_rupiah);
        tv_diskon_format_rupiah = findViewById(R.id.tv_diskon_format_rupiah);

        bt_canvaser_batal = findViewById(R.id.bt_canvaser_batal);
        bt_canvaser_submit = findViewById(R.id.bt_canvaser_submit);
        buttonExpand = findViewById(R.id.button_expand_listview);
        buttonExpand1 = findViewById(R.id.button_expand_listview1);

        total_pembayaran = findViewById(R.id.total_pembayaran);
        tv_canvas_no_std = findViewById(R.id.tv_canvas_no_std);
        tv_canvas_no_bkb = findViewById(R.id.tv_canvas_no_bkb);
        tv_canvas_no_so = findViewById(R.id.tv_canvas_no_so);

        bt_edit_qty_produk = findViewById(R.id.bt_edit_qty_produk);
        bt_edit_qty_produk1 = findViewById(R.id.bt_edit_qty_produk1);
        bt_edit_qty_produk2 = findViewById(R.id.bt_edit_qty_produk3);

        linear_detail_terima_produk = findViewById(R.id.linear_detail_terima_produk);
        linear_detail_terima_produk1 = findViewById(R.id.linear_detail_terima_produk1);

        edt_jumlah_transfer = findViewById(R.id.edt_jumlah_transfer);
        edt_jumlah_tunai = findViewById(R.id.edt_jumlah_tunai);
        tv_terima_produk = findViewById(R.id.tv_terima_produk);

        qty_order_asli = findViewById(R.id.qty_order_asli);
        qty_order_asli1 = findViewById(R.id.qty_order_asli1);
        qty_order_sudah_edit = findViewById(R.id.qty_order);
        qty_order_sudah_edit1 = findViewById(R.id.qty_order1);
        qty_order_asli4 = findViewById(R.id.qty_order_asli4);



        getComparator();



        tv_total_harga_row_format_rupiah = findViewById(R.id.tv_total_harga_row_format_rupiah);
        tv_total_harga_format_rupiah = findViewById(R.id.tv_total_harga_format_rupiah);

        tv_harga_satuan = findViewById(R.id.tv_list_terima_produk_harga_satuan_format_rupiah);
        tv_canvaser_catatan = findViewById(R.id.tv_canvaser_catatan);

        dateFormatter = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
        cariproduct = findViewById(R.id.cariproduct);
        img = (ImageView) findViewById(R.id.img_background);
        img_bukti_transfer = (ImageView) findViewById(R.id.img_background_bukti_transfer);

        chk_tunai = findViewById(R.id.chk_tunai);
        chk_transfer = findViewById(R.id.chk_transfer);
        chk_credit = findViewById(R.id.chk_credit);

        txt_input_jumlah_transfer = findViewById(R.id.txt_input_jumlah_transfer);
        txt_input_jumlah_tunai = findViewById(R.id.txt_input_jumlah_tunai);

        pilihpembayaran = findViewById(R.id.pilihpembayaran);

        bt_konfirmasi = findViewById(R.id.bt_oke);
        bt_camera_bukti_transfer = findViewById(R.id.foto_bukti_transfer);
        relative_foto_bukti_transfer = findViewById(R.id.relative_bukti_transfer);

        total_jenis_pembayaran = findViewById(R.id.tv_total_jenis_pembayaran);
        tv_total_jenis_pembayaran_text = findViewById(R.id.tv_total_jenis_pembayaran_text);

        delivery_order = findViewById(R.id.tv_delivery_order);
        tv_canvas_no_std.setText(STD);

        act_pilihCustomer = findViewById(R.id.autoCompleteTextView_pilihcustomer);

        dialog = new Dialog(mCanvaser_Terima_Produk.this);
        dialog.setContentView(R.layout.isi_product);
        dialog.setCancelable(false);

        dialog.show();

        act_pilih_ritase = dialog.findViewById(R.id.act_pilih_ritase);
        act_pilih_customer = dialog.findViewById(R.id.act_pilih_customer);
        list_produk_ingin_dicanvas = dialog.findViewById(R.id.list_produk_ingin_dicanvas);
        Button batal = dialog.findViewById(R.id.tidak);
        Button ok = dialog.findViewById(R.id.ya);
        TextView pilih = dialog.findViewById(R.id.pilih);

        checkalihcustomer = dialog.findViewById(R.id.checkalihcustomer);
        checkalihcustomer.setEnabled(false);


        linear_alihcustomer = dialog.findViewById(R.id.linear_alihcustomer);
        act_alih_customer = dialog.findViewById(R.id.act_alih_customer);
        act_pilih_alasan = dialog.findViewById(R.id.act_pilih_alasan);
        tv_canvaser_catatan_alih = dialog.findViewById(R.id.tv_canvaser_catatan_alih);



        act_alih_customer.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // When textview lost focus check the textview data valid or not
                if (!hasFocus) {
                    if (!array_pilihan_customer.contains(act_alih_customer.getText().toString())) {
                        act_alih_customer.setText(""); // clear your TextView
                    }
                }
            }
        });

        checkalihcustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((CompoundButton) view).isChecked()){
                    act_alih_customer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String[] parts2 = act_alih_customer.getText().toString().split(",");
                            String customerafter = parts2[1];
                            getProduct2(customerafter);
                            list_produk_ingin_dicanvas.setVisibility(GONE);
                        }
                    });
                } else {
                    String[] parts3 = act_pilih_customer.getText().toString().split(",");
                    String customerafter = parts3[1];
                    getProduct2(customerafter);
                    list_produk_ingin_dicanvas.setVisibility(GONE);
                }
            }
        });



        if(!pengaturanBar.getTitle().toString().equals("Pelanggan Dalam Rute")){
            checkalihcustomer.setVisibility(GONE);
        }

        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String nik_baru_2 = sharedPreferences.getString("szDocCall", null);

        if(nik_baru_2.contains("RP")){
            checkalihcustomer.setVisibility(GONE);
        } else if(nik_baru_2.contains("SPV")){
            checkalihcustomer.setVisibility(GONE);
        } else {

        }

        pilihAlihCustomer();

        pilihReason();

        checkalihcustomer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    linear_alihcustomer.setVisibility(View.VISIBLE);
                } else {
                    linear_alihcustomer.setVisibility(GONE);
                }
            }
        });

        list_produk_ingin_dicanvas.setFocusable(true);
        list_produk_ingin_dicanvas.setLongClickable(true);

        pilih.setText("Pilih BKB");


        batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

        if(getIntent().getStringExtra("idCustomer") == null){

        } else {
            StringRequest rest = new StringRequest(Request.Method.GET,  "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_Detail_Pelanggan?szCustomerId=" + getIntent().getStringExtra("idCustomer"),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getString("status").equals("true")) {
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    for (int i = 0; i < jsonArray.length(); i++) {

                                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                        act_pilih_customer.setText(jsonObject1.getString("szName") + "," + jsonObject1.getString("szCustomerId"));
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
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            if (requestQueue55 == null) {
                requestQueue55 = Volley.newRequestQueue(mCanvaser_Terima_Produk.this);
                requestQueue55.add(rest);
            } else {
                requestQueue55.add(rest);
            }

        }

        act_pilih_ritase.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                checkalihcustomer.setEnabled(true);
                if(pengaturanBar.getTitle().toString().equals("Pelanggan Dalam Rute")){
                    id_pb = id_pb_list.get(position).toString();
                    string_header_ritase = ritase_list.get(position).toString();
                    string_nodo = nodot.get(position).toString();
                    mixornot = refstd.get(position).toString();
                } else {
                    id_pb = id_pb_list.get(position).toString();
                    string_header_ritase = ritase_list.get(position).toString();
                    mixornot = refstd.get(position).toString();
                }

                next = position;
                String outofroute;
                if(menu_pelanggan.pengaturanBar.getTitle().toString().equals("Pelanggan Dalam Rute") || menu_pelanggan.pengaturanBar.getTitle().toString().equals("Pelanggan Non Rute")){
                    outofroute = "0";
                } else {
                    outofroute = "1";
                }

                    StringRequest rest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_cek_bkb?nomor_bkb=" + act_pilih_ritase.getText().toString() + "&customerId=" + idcustomer.getText().toString() + "&bOutOfRoute=" + outofroute,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if(pelanggan.equals("canvaser")){

                                        try {
                                            JSONObject obj = new JSONObject(response);
                                            final JSONArray movieArray = obj.getJSONArray("data");
                                            for (int i = 0; i < movieArray.length(); i++) {
                                                final JSONObject movieObject = movieArray.getJSONObject(i);
                                                if(movieObject.getString("szStatusPending").equals("1")){
                                                    if(act_pilih_customer.getText().toString().length() == 0){
                                                        act_pilih_customer.setError("Pilih Customer");
                                                    } else {
                                                        String[] parts = act_pilih_customer.getText().toString().split(",");
                                                        String id_cs = parts[1];
                                                        getProduct2(id_cs);
                                                    }
                                                    break;
                                                } else if (i == movieArray.length() - 1){
                                                    new SweetAlertDialog(mCanvaser_Terima_Produk.this, SweetAlertDialog.WARNING_TYPE)
                                                            .setTitleText("Maaf, BKB ini sudah dilakukan transaksi")
                                                            .setConfirmText("OK")
                                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                @Override
                                                                public void onClick(SweetAlertDialog sDialog) {
                                                                    sDialog.dismissWithAnimation();
                                                                    finish();
                                                                }
                                                            })
                                                            .show();
                                                }

                                            }


                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }


                                    } else {
                                        try {
                                            JSONObject obj = new JSONObject(response);
                                            final JSONArray movieArray = obj.getJSONArray("data");
                                            for (int i = 0; i < movieArray.length(); i++) {
                                                final JSONObject movieObject = movieArray.getJSONObject(i);
                                                if(movieObject.getString("szStatusPending").equals("1")){
                                                    if(act_pilih_customer.getText().toString().length() == 0){
                                                        act_pilih_customer.setError("Pilih Customer");
                                                    } else {
                                                        String[] parts = act_pilih_customer.getText().toString().split(",");
                                                        String id_cs = parts[1];
                                                        getProduct2(id_cs);
                                                    }
                                                    break;
                                                } else if (i == movieArray.length() - 1){
                                                    new SweetAlertDialog(mCanvaser_Terima_Produk.this, SweetAlertDialog.WARNING_TYPE)
                                                            .setTitleText("Maaf, BKB ini sudah dilakukan transaksi")
                                                            .setConfirmText("OK")
                                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                @Override
                                                                public void onClick(SweetAlertDialog sDialog) {
                                                                    sDialog.dismissWithAnimation();
                                                                    finish();
                                                                }
                                                            })
                                                            .show();
                                                }

                                            }


                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    if(act_pilih_customer.getText().toString().length() == 0){
                                        act_pilih_customer.setError("Pilih Customer");
                                    } else {
                                        String[] parts = act_pilih_customer.getText().toString().split(",");
                                        String id_cs = parts[1];
                                        getProduct2(id_cs);
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
                    };
                    rest.setRetryPolicy(new DefaultRetryPolicy(
                            10000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    RequestQueue requestkota = Volley.newRequestQueue(mCanvaser_Terima_Produk.this);
                    requestkota.add(rest);

            }
        });




        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(checkalihcustomer.isChecked()){
                    act_alih_customer.setError(null);
                    act_pilih_alasan.setError(null);
                    tv_canvaser_catatan_alih.setError(null);
                    if(act_pilih_ritase.getText().toString().contains("DONE")){
                        new SweetAlertDialog(mCanvaser_Terima_Produk.this, SweetAlertDialog.WARNING_TYPE)
                                .setContentText("BKB Sudah Selesai Dijalankan")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                    }
                                })
                                .show();
                    } else if (act_pilih_ritase.getText().toString().length() ==0){
                        new SweetAlertDialog(mCanvaser_Terima_Produk.this, SweetAlertDialog.WARNING_TYPE)
                                .setContentText("Pilih salah satu BKB yang ingin dijalankan")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                    }
                                })
                                .show();
                    } else if (act_alih_customer.getText().toString().length() ==0){
                        act_alih_customer.setError("Pilih Customer");
                    } else if (!array_pilihan_customer.contains(act_alih_customer.getText().toString())) {
                        act_alih_customer.setText(""); // clear your TextView
                    } else if (act_pilih_alasan.getText().toString().length() ==0){
                        act_pilih_alasan.setError("Pilih Alasan");
                    } else if (tv_canvaser_catatan_alih.getText().toString().length() ==0){
                        tv_canvaser_catatan_alih.setError("Isi Keterangan");
                    } else if (act_pilih_customer.getText().toString().length() ==0){
                        new SweetAlertDialog(mCanvaser_Terima_Produk.this, SweetAlertDialog.WARNING_TYPE)
                                .setContentText("Pilih Customer")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                    }
                                })
                                .show();
                    } else if (tv_canvas_no_bkb.getText().toString().length() ==0){
                        act_pilihCustomer.setFocusable(false);
                        act_pilihCustomer.setLongClickable(false);
                        tv_canvas_no_bkb.setText(act_pilih_ritase.getText().toString());
                        act_pilihCustomer.setText(act_pilih_customer.getText().toString());
                        dialog.dismiss();
                        ok.setEnabled(false);
                        for(int e = 0; e <= draft.size()-1 ;e++){

                            if (!(adapter2.getItem(e).getQtyEdit().equals("0")) || adapter2.getItem(e).getQtyEdit() == null || adapter2.getItem(e).getQtyEdit().equals("")){
                                String harga;

                                if(adapter2.getItem(e).getDecPrice().equals("null")){
                                    harga = "0.0000";
                                } else {
                                    harga = adapter2.getItem(e).getDecPrice();
                                }
                                final data_product_bkb_pojo movieItem = new data_product_bkb_pojo(
                                        adapter2.getItem(e).getDecQty(),
                                        adapter2.getItem(e).getSzProductId(),
                                        adapter2.getItem(e).getSzName(),
                                        harga,
                                        adapter2.getItem(e).getSzUomId(),
                                        adapter2.getItem(e).getDecQtyDeliveredDO(),
                                        adapter2.getItem(e).getQtyEdit()); //digunakan untuk edit qty


                                data_terima_produk_pojos.add(movieItem);

                            }

                        }

                        adapter = new ListViewAdapteProductPenjualan(data_terima_produk_pojos, getApplicationContext());

                        listproduk.setAdapter(adapter);
                        Utility.setListViewHeightBasedOnChildren(listproduk);
                        int jumlah_harga = 0;

                        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
                        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();
                        formatRp.setCurrencySymbol("Rp. ");
                        formatRp.setMonetaryDecimalSeparator(',');
                        formatRp.setGroupingSeparator('.');
                        kursIndonesia.setDecimalFormatSymbols(formatRp);


                        for(int i = 0; i < mCanvaser_Terima_Produk.data_terima_produk_pojos.size();i++){
                            jumlah_harga += Integer.parseInt(mCanvaser_Terima_Produk.adapter.getItem(i).getTotalHarga());
                        }

                        tv_total_harga_format_rupiah.setText(kursIndonesia.format(jumlah_harga));
                        total_pembayaran.setText(kursIndonesia.format(jumlah_harga));

                        total_harga = jumlah_harga;
                    } else if (act_pilih_ritase.getText().toString().equals(tv_canvas_no_bkb.getText().toString())){
                        new SweetAlertDialog(mCanvaser_Terima_Produk.this, SweetAlertDialog.WARNING_TYPE)
                                .setContentText("BKB Sudah Diinput")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                    }
                                })
                                .show();
                    } else {
                        act_pilihCustomer.setFocusable(false);
                        act_pilihCustomer.setLongClickable(false);
                        tv_canvas_no_bkb.setText(act_pilih_ritase.getText().toString());
                        act_pilihCustomer.setText(act_pilih_customer.getText().toString());
                        ok.setEnabled(false);
                        dialog.dismiss();


                        for(int e = 0; e <= draft.size()-1 ;e++){

                            if (!(adapter2.getItem(e).getQtyEdit().equals("0")) || adapter2.getItem(e).getDecQty() == null || adapter2.getItem(e).getDecQty().equals("")){
                                String harga;

                                if(adapter2.getItem(e).getDecPrice().equals("null")){
                                    harga = "0.0000";
                                } else {
                                    harga = adapter2.getItem(e).getDecPrice();
                                }
                                final data_product_bkb_pojo movieItem = new data_product_bkb_pojo(
                                        adapter2.getItem(e).getDecQty(),
                                        adapter2.getItem(e).getSzProductId(),
                                        adapter2.getItem(e).getSzName(),
                                        harga,
                                        adapter2.getItem(e).getSzUomId(),
                                        adapter2.getItem(e).getDecQtyDeliveredDO(),
                                        adapter2.getItem(e).getQtyEdit()); //digunakan untuk edit qty


                                data_terima_produk_pojos.add(movieItem);





                            }




                        }

                        adapter = new ListViewAdapteProductPenjualan(data_terima_produk_pojos, getApplicationContext());

                        listproduk.setAdapter(adapter);
                        Utility.setListViewHeightBasedOnChildren(listproduk);
                        int jumlah_harga = 0;

                        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
                        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();
                        formatRp.setCurrencySymbol("Rp. ");
                        formatRp.setMonetaryDecimalSeparator(',');
                        formatRp.setGroupingSeparator('.');
                        kursIndonesia.setDecimalFormatSymbols(formatRp);


                        for(int i = 0; i < mCanvaser_Terima_Produk.data_terima_produk_pojos.size();i++){
                            jumlah_harga += Integer.parseInt(mCanvaser_Terima_Produk.adapter.getItem(i).getTotalHarga());
                        }

                        tv_total_harga_format_rupiah.setText(kursIndonesia.format(jumlah_harga));
                        total_pembayaran.setText(kursIndonesia.format(jumlah_harga));

                        total_harga = jumlah_harga;
                    }
                } else {
                    if(act_pilih_ritase.getText().toString().contains("DONE")){
                        new SweetAlertDialog(mCanvaser_Terima_Produk.this, SweetAlertDialog.WARNING_TYPE)
                                .setContentText("BKB Sudah Selesai Dijalankan")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                    }
                                })
                                .show();
                    } else if (act_pilih_ritase.getText().toString().length() ==0){
                        new SweetAlertDialog(mCanvaser_Terima_Produk.this, SweetAlertDialog.WARNING_TYPE)
                                .setContentText("Pilih salah satu BKB yang ingin dijalankan")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                    }
                                })
                                .show();
                    } else if (act_pilih_customer.getText().toString().length() ==0){
                        new SweetAlertDialog(mCanvaser_Terima_Produk.this, SweetAlertDialog.WARNING_TYPE)
                                .setContentText("Pilih Customer")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                    }
                                })
                                .show();
                    } else if (tv_canvas_no_bkb.getText().toString().length() ==0){
                        act_pilihCustomer.setFocusable(false);
                        act_pilihCustomer.setLongClickable(false);
                        tv_canvas_no_bkb.setText(act_pilih_ritase.getText().toString());
                        act_pilihCustomer.setText(act_pilih_customer.getText().toString());
                        ok.setEnabled(false);
                        dialog.dismiss();

                        for(int e = 0; e <= draft.size()-1 ;e++){

                            if (!(adapter2.getItem(e).getQtyEdit().equals("0")) || adapter2.getItem(e).getQtyEdit() == null || adapter2.getItem(e).getQtyEdit().equals("")){
                                String harga;

                                if(adapter2.getItem(e).getDecPrice().equals("null")){
                                    harga = "0.0000";
                                } else {
                                    harga = adapter2.getItem(e).getDecPrice();
                                }
                                final data_product_bkb_pojo movieItem = new data_product_bkb_pojo(
                                        adapter2.getItem(e).getDecQty(),
                                        adapter2.getItem(e).getSzProductId(),
                                        adapter2.getItem(e).getSzName(),
                                        harga,
                                        adapter2.getItem(e).getSzUomId(),
                                        adapter2.getItem(e).getDecQtyDeliveredDO(),
                                        adapter2.getItem(e).getQtyEdit()); //digunakan untuk edit qty


                                data_terima_produk_pojos.add(movieItem);

                            }

                        }

                        adapter = new ListViewAdapteProductPenjualan(data_terima_produk_pojos, getApplicationContext());

                        listproduk.setAdapter(adapter);
                        Utility.setListViewHeightBasedOnChildren(listproduk);
                        int jumlah_harga = 0;

                        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
                        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();
                        formatRp.setCurrencySymbol("Rp. ");
                        formatRp.setMonetaryDecimalSeparator(',');
                        formatRp.setGroupingSeparator('.');
                        kursIndonesia.setDecimalFormatSymbols(formatRp);


                        for(int i = 0; i < mCanvaser_Terima_Produk.data_terima_produk_pojos.size();i++){
                            jumlah_harga += Integer.parseInt(mCanvaser_Terima_Produk.adapter.getItem(i).getTotalHarga());
                        }

                        tv_total_harga_format_rupiah.setText(kursIndonesia.format(jumlah_harga));
                        total_pembayaran.setText(kursIndonesia.format(jumlah_harga));

                        total_harga = jumlah_harga;
                    } else if (act_pilih_ritase.getText().toString().equals(tv_canvas_no_bkb.getText().toString())){
                        new SweetAlertDialog(mCanvaser_Terima_Produk.this, SweetAlertDialog.WARNING_TYPE)
                                .setContentText("BKB Sudah Diinput")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                    }
                                })
                                .show();
                    } else {
                        act_pilihCustomer.setFocusable(false);
                        act_pilihCustomer.setLongClickable(false);
                        tv_canvas_no_bkb.setText(act_pilih_ritase.getText().toString());
                        act_pilihCustomer.setText(act_pilih_customer.getText().toString());
                        ok.setEnabled(false);

                        dialog.dismiss();


                        for(int e = 0; e <= draft.size()-1 ;e++){

                            if (!(adapter2.getItem(e).getQtyEdit().equals("0")) || adapter2.getItem(e).getDecQty() == null || adapter2.getItem(e).getDecQty().equals("")){
                                String harga;

                                if(adapter2.getItem(e).getDecPrice().equals("null")){
                                    harga = "0.0000";
                                } else {
                                    harga = adapter2.getItem(e).getDecPrice();
                                }
                                final data_product_bkb_pojo movieItem = new data_product_bkb_pojo(
                                        adapter2.getItem(e).getDecQty(),
                                        adapter2.getItem(e).getSzProductId(),
                                        adapter2.getItem(e).getSzName(),
                                        harga,
                                        adapter2.getItem(e).getSzUomId(),
                                        adapter2.getItem(e).getDecQtyDeliveredDO(),
                                        adapter2.getItem(e).getQtyEdit()); //digunakan untuk edit qty


                                data_terima_produk_pojos.add(movieItem);





                            }




                        }

                        adapter = new ListViewAdapteProductPenjualan(data_terima_produk_pojos, getApplicationContext());

                        listproduk.setAdapter(adapter);
                        Utility.setListViewHeightBasedOnChildren(listproduk);
                        int jumlah_harga = 0;

                        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
                        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();
                        formatRp.setCurrencySymbol("Rp. ");
                        formatRp.setMonetaryDecimalSeparator(',');
                        formatRp.setGroupingSeparator('.');
                        kursIndonesia.setDecimalFormatSymbols(formatRp);


                        for(int i = 0; i < mCanvaser_Terima_Produk.data_terima_produk_pojos.size();i++){
                            jumlah_harga += Integer.parseInt(mCanvaser_Terima_Produk.adapter.getItem(i).getTotalHarga());
                        }

                        tv_total_harga_format_rupiah.setText(kursIndonesia.format(jumlah_harga));
                        total_pembayaran.setText(kursIndonesia.format(jumlah_harga));

                        total_harga = jumlah_harga;

                    }
                }
            }
        });

        act_pilih_customer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
                String nama_customer = (String) parent.getItemAtPosition(position);

                String[] parts = nama_customer.split(",");
                String nama_cs = parts[0];
                String id_cs = parts[1];

                if(act_pilih_ritase.getText().toString().length() == 0){
                    act_pilih_ritase.setError("Pilih BKB");
                } else {
                    getProduct2(id_cs);
                    getTermPayment(id_cs);
                }

                //TODO Do something with the selected text
            }
        });




        bt_canvaser_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOnline()){
                    if(data_terima_produk_pojos.size() == 0){
                        new SweetAlertDialog(mCanvaser_Terima_Produk.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Produk Masih Kosong, Silahkan isi produk yang ingin di Canvas")
                                .setConfirmText("OK")
                                .show();
                    } else if(!chk_credit.isChecked() && !chk_tunai.isChecked() && !chk_transfer.isChecked()){
                        new SweetAlertDialog(mCanvaser_Terima_Produk.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Pilih Metode Pembayaran yang ingin dipilih")
                                .setConfirmText("OK")
                                .show();
                    } else if (chk_tunai.isChecked() || chk_transfer.isChecked()){
                        new SweetAlertDialog(mCanvaser_Terima_Produk.this, SweetAlertDialog.WARNING_TYPE)
                                .setContentText("Apakah yakin pelanggan ini membayar dengan tunai ?")
                                .setCancelText("Tidak")
                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                    }
                                })
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                        if(tv_total_jenis_pembayaran_rupiah.getText().toString().equals(total_pembayaran.getText().toString())){
                                            final Dialog dialogstatus = new Dialog(mCanvaser_Terima_Produk.this);
                                            dialogstatus.setContentView(R.layout.dialog_submit_terima_produk);
                                            dialogstatus.setCancelable(false);

                                            Button bt_submit = dialogstatus.findViewById(R.id.bt_submit_terima_produk);
                                            Button bt_cek_kembali = dialogstatus.findViewById(R.id.bt_cek_kembali_produk);

                                            bt_cek_kembali.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    dialogstatus.dismiss();
                                                }
                                            });

                                            bt_submit.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {

                                                    dialogstatus.dismiss();


                                                    if(data_terima_produk_pojos.size() == 0){
                                                        pDialog.dismissWithAnimation();
                                                        new SweetAlertDialog(mCanvaser_Terima_Produk.this, SweetAlertDialog.WARNING_TYPE)
                                                                .setContentText("Data Belum Lengkap")
                                                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                    @Override
                                                                    public void onClick(SweetAlertDialog sDialog) {
                                                                        sDialog.dismissWithAnimation();
                                                                    }
                                                                })
                                                                .show();
                                                    } else {
                                                        for (int i=data_terima_produk_pojos.size()-1; i>=0; i--) {
                                                            if(adapter.getItem(i).getQtyEdit().equals("0")){
                                                                adapter.remove(adapter.getItem(i));
                                                                Utility.setListViewHeightBasedOnChildren(listproduk);
                                                                adapter.notifyDataSetChanged();
                                                            } else {
                                                                Utility.setListViewHeightBasedOnChildren(listproduk);
                                                                adapter.notifyDataSetChanged();
                                                            }
                                                        }
                                                        pDialog = new SweetAlertDialog(mCanvaser_Terima_Produk.this, SweetAlertDialog.PROGRESS_TYPE);
                                                        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                                                        pDialog.setTitleText("Harap Menunggu");
                                                        pDialog.setCancelable(false);
                                                        pDialog.show();

                                                        new Handler().postDelayed(new Runnable() {
                                                            @Override
                                                            public void run() {

                                                                if(checkalihcustomer.isChecked()){
                                                                    history_alihCustomer();
                                                                } else {
                                                                    if (mulai_perjalanan.pelanggan.equals("canvaser")) {
                                                                        getDo();
                                                                    } else if (mulai_perjalanan.pelanggan.equals("non_rute")) {
                                                                        updateCustomer();
                                                                    } else if (mulai_perjalanan.pelanggan.equals("canvaser_luar")) {
                                                                        Toast.makeText(mCanvaser_Terima_Produk.this, condition, Toast.LENGTH_SHORT).show();
                                                                        if (menu_pelanggan.tv_terima_produk.getText().toString().equals("1")) {
                                                                            getLastitem();
                                                                        } else if (condition.equals("NoaddNewDo")) {
                                                                            getDo();
                                                                        }  else{
                                                                            getLastitem();
                                                                        }
                                                                    }
                                                                }

                                                            }
                                                        }, 2000);







                                                    }



                                                }
                                            });

                                            dialogstatus.show();
                                        } else {
                                            new SweetAlertDialog(mCanvaser_Terima_Produk.this, SweetAlertDialog.WARNING_TYPE)
                                                    .setTitleText("Konfirmasi harga Harus Sama dari Total Harga")
                                                    .setConfirmText("OK")
                                                    .show();
                                        }
                                    }
                                })
                                .show();

                    } else if (chk_credit.isChecked()){
                        new SweetAlertDialog(mCanvaser_Terima_Produk.this, SweetAlertDialog.WARNING_TYPE)
                                .setContentText("Apakah yakin pelanggan ini membayar dengan kredit ?")
                                .setCancelText("Tidak")
                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                    }
                                })
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                        final Dialog dialogstatus = new Dialog(mCanvaser_Terima_Produk.this);
                                        dialogstatus.setContentView(R.layout.dialog_submit_terima_produk);
                                        dialogstatus.setCancelable(false);

                                        Button bt_submit = dialogstatus.findViewById(R.id.bt_submit_terima_produk);
                                        Button bt_cek_kembali = dialogstatus.findViewById(R.id.bt_cek_kembali_produk);

                                        bt_cek_kembali.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                dialogstatus.dismiss();
                                            }
                                        });

                                        bt_submit.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {

                                                dialogstatus.dismiss();



                                                if(data_terima_produk_pojos.size() == 0){
                                                    pDialog.dismissWithAnimation();
                                                    new SweetAlertDialog(mCanvaser_Terima_Produk.this, SweetAlertDialog.WARNING_TYPE)
                                                            .setContentText("Data Belum Lengkap")
                                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                @Override
                                                                public void onClick(SweetAlertDialog sDialog) {
                                                                    sDialog.dismissWithAnimation();
                                                                }
                                                            })
                                                            .show();
                                                } else {

                                                    for (int i=data_terima_produk_pojos.size()-1; i>=0; i--) {
                                                        if(adapter.getItem(i).getQtyEdit().equals("0")){
                                                            adapter.remove(adapter.getItem(i));
                                                            Utility.setListViewHeightBasedOnChildren(listproduk);
                                                            adapter.notifyDataSetChanged();
                                                        } else {
                                                            Utility.setListViewHeightBasedOnChildren(listproduk);
                                                            adapter.notifyDataSetChanged();
                                                        }
                                                    }

                                                    pDialog = new SweetAlertDialog(mCanvaser_Terima_Produk.this, SweetAlertDialog.PROGRESS_TYPE);
                                                    pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                                                    pDialog.setTitleText("Harap Menunggu");
                                                    pDialog.setCancelable(false);
                                                    pDialog.show();

                                                    new Handler().postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            if(checkalihcustomer.isChecked()){
                                                                history_alihCustomer();
                                                            } else {
                                                                if (mulai_perjalanan.pelanggan.equals("canvaser")) {
                                                                    getDo();
                                                                } else if (mulai_perjalanan.pelanggan.equals("non_rute")) {
                                                                    updateCustomer();
                                                                } else if (mulai_perjalanan.pelanggan.equals("canvaser_luar")) {
                                                                    Toast.makeText(mCanvaser_Terima_Produk.this, condition, Toast.LENGTH_SHORT).show();
                                                                    if (menu_pelanggan.tv_terima_produk.getText().toString().equals("1")) {
                                                                        getLastitem();
                                                                    } else if (condition.equals("NoaddNewDo")) {
                                                                        getDo();
                                                                    }  else{
                                                                        getLastitem();
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }, 2000);


                                                }



                                            }
                                        });

                                        dialogstatus.show();
                                    }
                                })
                                .show();

                    }
                } else {
                    new SweetAlertDialog(mCanvaser_Terima_Produk.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Jaringan Tidak Stabil, Silahkan Dicoba lagi")
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

        bt_canvaser_batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.clear();
                finish();

            }
        });

        buttonExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                    Intent intent = new Intent(getApplicationContext(), sisa_stock.class);
//                    intent.putExtra("list", String.valueOf(position));
//                    intent.putExtra("nama_barang", data.getSzName());
//                    intent.putExtra("uang", data.getDecPrice());
//                    startActivity(intent)

                if (linear_detail_terima_produk.getVisibility() == GONE) {
                    //expandedChildList.set(arg2, true);
                    linear_detail_terima_produk.setVisibility(View.VISIBLE);
                }
                else {
                    //expandedChildList.set(arg2, false);
                    linear_detail_terima_produk.setVisibility(GONE);
                }
            }
        });

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        currentDateandTime = sdf.format(new Date());

        buttonExpand1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                    Intent intent = new Intent(getApplicationContext(), sisa_stock.class);
//                    intent.putExtra("list", String.valueOf(position));
//                    intent.putExtra("nama_barang", data.getSzName());
//                    intent.putExtra("uang", data.getDecPrice());
//                    startActivity(intent)

                if (linear_detail_terima_produk1.getVisibility() == GONE) {
                    //expandedChildList.set(arg2, true);
                    linear_detail_terima_produk1.setVisibility(View.VISIBLE);
                }
                else {
                    //expandedChildList.set(arg2, false);
                    linear_detail_terima_produk1.setVisibility(GONE);
                }
            }
        });

        array_pilihan_customer = new ArrayList<>();


        act_pilihCustomer.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                    act_pilihCustomer.showDropDown();
            }
        });

        act_pilihCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                act_pilihCustomer.showDropDown();
            }
        });

        if(getIntent().getStringExtra("Status").equals("Success")){
            act_pilihCustomer.setFocusable(false);
            act_pilihCustomer.setLongClickable(false);

            StringRequest pilih_customer = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_Detail_Pelanggan?szCustomerId=" + getIntent().getStringExtra("idCustomer"),

                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getString("status").equals("true")) {

                                    pDialog.dismissWithAnimation();

                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    for (int i = 0; i < jsonArray.length(); i++) {

                                        //parameter jika mengambul data dari url url_get_data_checklist_stagging
                                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                        //String string_nomor_polisi = jsonObject1.getString("license_number");
                                        //parameter jika mengambul data dari url url_get_data_variant_transportir_checklist_stagging

                                        String string_nama_customer = jsonObject1.getString("szName");
                                        String string_id_customer = jsonObject1.getString("szCustomerId");

                                        act_pilihCustomer.setText(string_nama_customer+","+string_id_customer);
//                                        getProduct(string_id_customer);

                                        getTermPayment(string_id_customer);


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
                            Toast.makeText(mCanvaser_Terima_Produk.this, "Ada kesalahan, silahkan coba kembali", Toast.LENGTH_SHORT).show();

                            pDialog.dismissWithAnimation();
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
            if (requestQueue == null) {
                requestQueue = Volley.newRequestQueue(mCanvaser_Terima_Produk.this);
                requestQueue.add(pilih_customer);
            } else {
                requestQueue.add(pilih_customer);
            }

        } else {
            if(mulai_perjalanan.pelanggan.equals("canvaser")){
                StringRequest pilih_customer = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Persiapan/index_List_Toko_Canvaser?IdStd=" + STD,

                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if (jsonObject.getString("status").equals("true")) {

                                        pDialog.dismissWithAnimation();

                                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                                        for (int i = 0; i < jsonArray.length(); i++) {

                                            //parameter jika mengambul data dari url url_get_data_checklist_stagging
                                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                            //String string_nomor_polisi = jsonObject1.getString("license_number");
                                            //parameter jika mengambul data dari url url_get_data_variant_transportir_checklist_stagging
                                            String string_nama_customer = jsonObject1.getString("szName");
                                            String string_id_customer = jsonObject1.getString("szId");
                                            array_pilihan_customer.add(string_nama_customer+","+string_id_customer);

                                            if(!jsonObject1.getString("szDocSO").equals("")){
                                                array_pilihan_customer.remove(string_nama_customer+","+string_id_customer);
                                            }

                                        }
                                    }
                                    act_pilih_customer.setAdapter(new ArrayAdapter<String>(mCanvaser_Terima_Produk.this, android.R.layout.simple_expandable_list_item_1, array_pilihan_customer));
                                    act_pilih_customer.setThreshold(1);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(mCanvaser_Terima_Produk.this, "Ada kesalahan, silahkan coba kembali", Toast.LENGTH_SHORT).show();

                                pDialog.dismissWithAnimation();
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
                if (requestQueue2 == null) {
                    requestQueue2 = Volley.newRequestQueue(mCanvaser_Terima_Produk.this);
                    requestQueue2.add(pilih_customer);
                } else {
                    requestQueue2.add(pilih_customer);
                }

                act_pilihCustomer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
                        String nama_customer = (String) parent.getItemAtPosition(position);

                        String[] parts = nama_customer.split(",");
                        String nama_cs = parts[0];
                        String id_cs = parts[1];
//                        getProduct(id_cs);
                        getTermPayment(id_cs);
                        //tv_no_customer.setText(id_cs);

                        //TODO Do something with the selected text
                    }
                });
            } else if (pelanggan.equals("non_rute")){

                sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                String nik_baru = sharedPreferences.getString("szDocCall", null);
                String[] parts = nik_baru.split("-");
                String restnomor = parts[0];

                StringRequest pilih_customer = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_pilihancustomerNonRute?szSoldToBranchId=" + restnomor ,

                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if (jsonObject.getString("status").equals("true")) {

                                        pDialog.dismissWithAnimation();

                                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                                        for (int i = 0; i < jsonArray.length(); i++) {

                                            //parameter jika mengambul data dari url url_get_data_checklist_stagging
                                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                            //String string_nomor_polisi = jsonObject1.getString("license_number");
                                            //parameter jika mengambul data dari url url_get_data_variant_transportir_checklist_stagging
                                            String string_nama_customer = jsonObject1.getString("nama_customer");
                                            String string_id_customer = jsonObject1.getString("id_customer");
                                            array_pilihan_customer.add(string_nama_customer+","+string_id_customer);

                                        }
                                    }
                                    act_pilih_customer.setAdapter(new ArrayAdapter<String>(mCanvaser_Terima_Produk.this, android.R.layout.simple_expandable_list_item_1, array_pilihan_customer));
                                    act_pilih_customer.setThreshold(1);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(mCanvaser_Terima_Produk.this, String.valueOf(error), Toast.LENGTH_SHORT).show();

                                pDialog.dismissWithAnimation();
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
                if (requestQueue3 == null) {
                    requestQueue3 = Volley.newRequestQueue(mCanvaser_Terima_Produk.this);
                    requestQueue3.add(pilih_customer);
                } else {
                    requestQueue3.add(pilih_customer);
                }

                act_pilihCustomer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
                        String nama_customer = (String) parent.getItemAtPosition(position);

                        String[] parts = nama_customer.split(",");
                        String nama_cs = parts[0];
                        String id_cs = parts[1];
//                        getProduct(id_cs);
                        getTermPayment(id_cs);
                        //tv_no_customer.setText(id_cs);

                        //TODO Do something with the selected text
                    }
                });
            } else {
                sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                String nik_baru = sharedPreferences.getString("szDocCall", null);

                StringRequest pilih_customer = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_Canvas_Pelanggan_Luar?surat_tugas=" + STD + "&szEmployeeId=" + nik_baru ,

                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if (jsonObject.getString("status").equals("true")) {

                                        pDialog.dismissWithAnimation();

                                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                                        for (int i = 0; i < jsonArray.length(); i++) {

                                            //parameter jika mengambul data dari url url_get_data_checklist_stagging
                                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                            //String string_nomor_polisi = jsonObject1.getString("license_number");
                                            //parameter jika mengambul data dari url url_get_data_variant_transportir_checklist_stagging
                                            String string_nama_customer = jsonObject1.getString("szName");
                                            String string_id_customer = jsonObject1.getString("szCustomerId");
                                            array_pilihan_customer.add(string_nama_customer+","+string_id_customer);


                                            if(!jsonObject1.getString("szDocSO").equals("null")){
                                                array_pilihan_customer.remove(string_nama_customer+","+string_id_customer);
                                            }

                                        }
                                    }
                                    act_pilih_customer.setAdapter(new ArrayAdapter<String>(mCanvaser_Terima_Produk.this, android.R.layout.simple_expandable_list_item_1, array_pilihan_customer));
                                    act_pilih_customer.setThreshold(1);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(mCanvaser_Terima_Produk.this, String.valueOf(error), Toast.LENGTH_SHORT).show();

                                pDialog.dismissWithAnimation();
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
                if (requestQueue4 == null) {
                    requestQueue4 = Volley.newRequestQueue(mCanvaser_Terima_Produk.this);
                    requestQueue4.add(pilih_customer);
                } else {
                    requestQueue4.add(pilih_customer);
                }

                act_pilihCustomer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
                        String nama_customer = (String) parent.getItemAtPosition(position);

                        String[] parts = nama_customer.split(",");
                        String nama_cs = parts[0];
                        String id_cs = parts[1];
//                        getProduct(id_cs);
                        getTermPayment(id_cs);
                        //tv_no_customer.setText(id_cs);

                        //TODO Do something with the selected text
                    }
                });
            }

        }



        bt_camera_bukti_transfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withActivity(mCanvaser_Terima_Produk.this)
                        .withPermission(Manifest.permission.CAMERA)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
//                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                                startActivityForResult(intent, 3);

                                cv = new ContentValues();
                                cv.put(MediaStore.Images.Media.TITLE, "My Picture");
                                cv.put(MediaStore.Images.Media.DESCRIPTION, "From Camera");

                                imageUri = getContentResolver().insert(
                                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv);
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                                startActivityForResult(intent, 2);


                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();
                            }
                        }).check();
            }

        });

        chk_transfer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {

                    chk_transfer.setChecked(true);
                    act_pilihCustomer.setFocusable(false);
                    chk_credit.setChecked(false);
                    // Everytime you check the checkBox, the following code will execute:
                    // Your code here //
                }
            }
        });

        chk_credit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {

                    chk_tunai.setChecked(false);
                    chk_transfer.setChecked(false);

                    relative_foto_bukti_transfer.setVisibility(GONE);
                    txt_input_jumlah_transfer.setVisibility(GONE);
                    txt_input_jumlah_tunai.setVisibility(GONE);


                    // Everytime you check the checkBox, the following code will execute:
                    // Your code here //
                }
            }
        });

        chk_tunai.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {

                    chk_tunai.setChecked(true);
                    act_pilihCustomer.setFocusable(false);
                    chk_credit.setChecked(false);

                    // Everytime you check the checkBox, the following code will execute:
                    // Your code here //
                }
            }
        });

        chk_transfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(chk_transfer.isChecked()){
                    relative_foto_bukti_transfer.setVisibility(View.VISIBLE);
                    txt_input_jumlah_transfer.setVisibility(View.VISIBLE);
                    edt_jumlah_transfer.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void afterTextChanged(Editable s) {
                            // TODO Auto-generated method stub
                        }

                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                            // TODO Auto-generated method stub
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                            edt_jumlah_transfer.removeTextChangedListener(this);

                            try {
                                String originalString = s.toString();

                                Long longval;
                                if (originalString.contains(",")) {
                                    originalString = originalString.replaceAll(",", "");
                                }
                                longval = Long.parseLong(originalString);

                                DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                                formatter.applyPattern("#,###,###,###");
                                String formattedString = formatter.format(longval);

                                //setting text after format to EditText
                                edt_jumlah_transfer.setText(formattedString);
                                edt_jumlah_transfer.setSelection(edt_jumlah_transfer.getText().length());
                            } catch (NumberFormatException nfe) {
                                nfe.printStackTrace();
                            }

                            edt_jumlah_transfer.addTextChangedListener(this);

                            String str = edt_jumlah_transfer.getText().toString().replaceAll( ",", "" );

                            if (!edt_jumlah_transfer.getText().toString().equals("")){
                                txt_input_jumlah_transfer.setError(null);
                            } else {
                                txt_input_jumlah_transfer.setError("Jumlah transfer tidak boleh kosong");
                            }

                        }
                    });

                } else if (!chk_transfer.isChecked()){
                    img_bukti_transfer.setImageBitmap(null);
                    txt_input_jumlah_transfer.setVisibility(GONE);
                    edt_jumlah_transfer.setText("");
                    txt_input_jumlah_transfer.setError(null);
                    relative_foto_bukti_transfer.setVisibility(GONE);
                }
//                else if (chk_tunai.isChecked() && chk_transfer.isChecked()){
//
//                    bt_oke.setVisibility(View.VISIBLE);
//                }

            }
        });

        chk_tunai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(chk_tunai.isChecked()){
                    txt_input_jumlah_tunai.setVisibility(View.VISIBLE);
                    edt_jumlah_tunai.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void afterTextChanged(Editable s) {
                            // TODO Auto-generated method stub
                        }

                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                            // TODO Auto-generated method stub
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            edt_jumlah_tunai.removeTextChangedListener(this);

                            try {
                                String originalString = s.toString();

                                Long longval;
                                if (originalString.contains(",")) {
                                    originalString = originalString.replaceAll(",", "");
                                }
                                longval = Long.parseLong(originalString);

                                DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                                formatter.applyPattern("#,###,###,###");
                                String formattedString = formatter.format(longval);

                                //setting text after format to EditText
                                edt_jumlah_tunai.setText(formattedString);
                                edt_jumlah_tunai.setSelection(edt_jumlah_tunai.getText().length());
                            } catch (NumberFormatException nfe) {
                                nfe.printStackTrace();
                            }

                            edt_jumlah_tunai.addTextChangedListener(this);

                            String str = edt_jumlah_tunai.getText().toString().replaceAll( ",", "" );

                            if (!edt_jumlah_tunai.getText().toString().equals("")){
                                txt_input_jumlah_tunai.setError(null);
                            } else {
                                txt_input_jumlah_tunai.setError("Jumlah tunai tidak boleh kosong");
                            }
                        }
                    });


                } else if (!chk_tunai.isChecked()){
                    txt_input_jumlah_tunai.setVisibility(GONE);
                    edt_jumlah_tunai.setText("");
                    txt_input_jumlah_tunai.setError(null);
                }

            }
        });


        //check current state of a check box (true or false)
//        boolean bol_chk_transfer = chk_transfer.isChecked();
//        boolean bol_chk_tunai = chk_tunai.isChecked();
//
//        if(bol_chk_transfer){
//            Toast.makeText(mPenjualan_Terima_Produk.this, "OK", Toast.LENGTH_SHORT).show();
//        }else{
//            Toast.makeText(mPenjualan_Terima_Produk.this, "GAK OK", Toast.LENGTH_SHORT).show();
//        }

        //BUTTON SAAT KONFIRMASI METODE PEMBAYARAN YANG DIPAKAI

        edt_jumlah_tunai.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                tv_total_jenis_pembayaran_rupiah.setText("0");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tv_total_jenis_pembayaran_rupiah.setText("0");
            }

            @Override
            public void afterTextChanged(Editable s) {
                tv_total_jenis_pembayaran_rupiah.setText("0");
            }
        });

        edt_jumlah_transfer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                tv_total_jenis_pembayaran_rupiah.setText("0");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tv_total_jenis_pembayaran_rupiah.setText("0");
            }

            @Override
            public void afterTextChanged(Editable s) {
                tv_total_jenis_pembayaran_rupiah.setText("0");
            }
        });

        bt_konfirmasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (chk_tunai.isChecked() && !chk_transfer.isChecked()) {

                    if (edt_jumlah_tunai.getText().toString().equals("")) {
                        txt_input_jumlah_tunai.setError("Jumlah tunai tidak boleh kosong");

                    } else {

                        int jumlah_tunai = Integer.parseInt(edt_jumlah_tunai.getText().toString().replaceAll( ",", "" ));
                        String string_jumlah_tunai = String.valueOf(jumlah_tunai);
                        //Toast.makeText(mPenjualan_Terima_Produk.this, "jumlah tunai = " + string_jumlah_tunai, Toast.LENGTH_SHORT).show();

                        jenis_pembayaran = 1;

                        total_jenis_pembayaran.setText(string_jumlah_tunai);

                        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
                        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();
                        formatRp.setCurrencySymbol("Rp. ");
                        formatRp.setMonetaryDecimalSeparator(',');
                        formatRp.setGroupingSeparator('.');
                        kursIndonesia.setDecimalFormatSymbols(formatRp);

                        tv_total_jenis_pembayaran_rupiah.setText(kursIndonesia.format(jumlah_tunai));


                        tv_total_jenis_pembayaran_text.setText("tunai : ");

                    }

                } else if (!chk_tunai.isChecked() && chk_transfer.isChecked()) {


                    if (edt_jumlah_transfer.getText().toString().equals("")) {
                        txt_input_jumlah_transfer.setError("Jumlah transfer tidak boleh kosong");

                    } else {
                        int jumlah_transfer = Integer.parseInt(edt_jumlah_transfer.getText().toString().replaceAll( ",", "" ));
                        String string_jumlah_transfer = String.valueOf(jumlah_transfer);
                        //Toast.makeText(mPenjualan_Terima_Produk.this, "jumlah transfeer = " + string_jumlah_transfer, Toast.LENGTH_SHORT).show();

                        jenis_pembayaran = 2;
                        total_jenis_pembayaran.setText(string_jumlah_transfer);


                        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
                        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();
                        formatRp.setCurrencySymbol("Rp. ");
                        formatRp.setMonetaryDecimalSeparator(',');
                        formatRp.setGroupingSeparator('.');
                        kursIndonesia.setDecimalFormatSymbols(formatRp);

                        tv_total_jenis_pembayaran_rupiah.setText(kursIndonesia.format(jumlah_transfer));

                        tv_total_jenis_pembayaran_text.setText("transfer : ");

                    }

                } else if (chk_tunai.isChecked() && chk_transfer.isChecked()) {


                    if (edt_jumlah_tunai.getText().toString().equals("") && edt_jumlah_transfer.getText().toString().equals("")) {
                        txt_input_jumlah_tunai.setError("Jumlah tunai & transfer tidak boleh kosong");
                        txt_input_jumlah_transfer.setError("Jumlah tunai & transfer tidak boleh kosong");

                    } else if (!edt_jumlah_tunai.getText().toString().equals("") && edt_jumlah_transfer.getText().toString().equals("")) {
                        txt_input_jumlah_transfer.setError("Jumlah transfer tidak boleh kosong");

                    } else if (edt_jumlah_tunai.getText().toString().equals("") && !edt_jumlah_transfer.getText().toString().equals("")) {
                        txt_input_jumlah_tunai.setError("Jumlah tunai tidak boleh kosong");

                    } else {

                        int jumlah_tunai = Integer.parseInt(edt_jumlah_tunai.getText().toString().replaceAll( ",", "" ));
                        int jumlah_transfer = Integer.parseInt(edt_jumlah_transfer.getText().toString().replaceAll( ",", "" ));

                        String total = String.valueOf(jumlah_tunai + jumlah_transfer);

                        //Toast.makeText(mPenjualan_Terima_Produk.this, "jumlah keduanya = " + total, Toast.LENGTH_SHORT).show();

                        jenis_pembayaran = 3;
                        total_jenis_pembayaran.setText(total);

                        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
                        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();
                        formatRp.setCurrencySymbol("Rp. ");
                        formatRp.setMonetaryDecimalSeparator(',');
                        formatRp.setGroupingSeparator('.');
                        kursIndonesia.setDecimalFormatSymbols(formatRp);

                        String totalhargarupiah = formatRupiah(Double.valueOf(total));
                        tv_total_jenis_pembayaran_rupiah.setText(kursIndonesia.format(Integer.parseInt(total)));
                        tv_total_jenis_pembayaran_text.setText("tunai & transfer : ");

                    }
                }

            }
        });

        bt_edit_qty_produk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialogstatus = new Dialog(mCanvaser_Terima_Produk.this);
                dialogstatus.setContentView(R.layout.dialog_edit_qty_produk);

                Button batal = dialogstatus.findViewById(R.id.batal);
                Button ok = dialogstatus.findViewById(R.id.ok);
                TextView dialog_nama_produk = dialogstatus.findViewById(R.id.dialog_nama_produk);
                TextView dialog_qty_produk = dialogstatus.findViewById(R.id.dialog_qty_produk);

                TextView dialog_qty_produk_editan = dialogstatus.findViewById(R.id.dialog_qty_produk_editan);

                ImageButton bt_image_minus = dialogstatus.findViewById(R.id.bt_minus);
                ImageButton bt_image_plus = dialogstatus.findViewById(R.id.bt_plus);
                TextView qty_produk_edit = dialogstatus.findViewById(R.id.tv_qty_produk);

                dialog_nama_produk.setText("Aqua Galon Isi");
                dialog_qty_produk.setText(qty_order_asli.getText().toString());


                bt_image_plus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String szId = qty_produk_edit.getText().toString();
                        int total_qty = Integer.parseInt(String.valueOf(szId));


                        if(total_qty >= Integer.parseInt(qty_order_asli.getText().toString())){
                            new SweetAlertDialog(mCanvaser_Terima_Produk.this, SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("Qty Tidak Boleh Lebih Besar")
                                    .setConfirmText("OK")
                                    .show();
                            dialog_qty_produk_editan.setText(String.valueOf(total_qty));
                        } else {
                            total_qty++;
                            qty_produk_edit.setText(String.valueOf(total_qty));
                            dialog_qty_produk_editan.setText(String.valueOf(total_qty));
                        }


                    }
                });

                bt_image_minus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String szId = qty_produk_edit.getText().toString();
                        int total_qty = Integer.parseInt(String.valueOf(szId));
                        total_qty--;
                        qty_produk_edit.setText(String.valueOf(total_qty));
                        dialog_qty_produk_editan.setText(String.valueOf(total_qty));
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

                        qty_order_sudah_edit.setText(dialog_qty_produk_editan.getText().toString());
                        int totalharga = Integer.parseInt(qty_order_sudah_edit.getText().toString()) * Integer.parseInt(tv_harga_satuan.getText().toString());
                        tv_total_harga_row_format_rupiah.setText(String.valueOf(totalharga));


                        String total_harga_row_rupiah = formatRupiah((double) totalharga);
                        tv_total_harga_format_rupiah.setText(total_harga_row_rupiah);

                        dialogstatus.dismiss();

                    }
                });

                dialogstatus.show();
            }
        });

        bt_edit_qty_produk1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialogstatus = new Dialog(mCanvaser_Terima_Produk.this);
                dialogstatus.setContentView(R.layout.dialog_edit_qty_produk);

                Button batal = dialogstatus.findViewById(R.id.batal);
                Button ok = dialogstatus.findViewById(R.id.ok);
                TextView dialog_nama_produk = dialogstatus.findViewById(R.id.dialog_nama_produk);
                TextView dialog_qty_produk = dialogstatus.findViewById(R.id.dialog_qty_produk);

                TextView dialog_qty_produk_editan = dialogstatus.findViewById(R.id.dialog_qty_produk_editan);

                ImageButton bt_image_minus = dialogstatus.findViewById(R.id.bt_minus);
                ImageButton bt_image_plus = dialogstatus.findViewById(R.id.bt_plus);
                TextView qty_produk_edit = dialogstatus.findViewById(R.id.tv_qty_produk);

                dialog_nama_produk.setText("Aqua Galon Botol");
                dialog_qty_produk.setText(qty_order_asli.getText().toString());


                bt_image_plus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String szId = qty_produk_edit.getText().toString();
                        int total_qty = Integer.parseInt(String.valueOf(szId));


                        if(total_qty >= Integer.parseInt(qty_order_asli.getText().toString())){
                            new SweetAlertDialog(mCanvaser_Terima_Produk.this, SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("Qty Tidak Boleh Lebih Besar")
                                    .setConfirmText("OK")
                                    .show();
                            dialog_qty_produk_editan.setText(String.valueOf(total_qty));
                        } else {
                            total_qty++;
                            qty_produk_edit.setText(String.valueOf(total_qty));
                            dialog_qty_produk_editan.setText(String.valueOf(total_qty));
                        }


                    }
                });

                bt_image_minus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String szId = qty_produk_edit.getText().toString();
                        int total_qty = Integer.parseInt(String.valueOf(szId));
                        total_qty--;
                        qty_produk_edit.setText(String.valueOf(total_qty));
                        dialog_qty_produk_editan.setText(String.valueOf(total_qty));
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

                        qty_order_sudah_edit1.setText(dialog_qty_produk_editan.getText().toString());
                        int totalharga = Integer.parseInt(qty_order_sudah_edit.getText().toString()) * Integer.parseInt(tv_harga_satuan.getText().toString());
                        tv_total_harga_row_format_rupiah.setText(String.valueOf(totalharga));

                        //tv_total_harga_format_rupiah.setText(String.valueOf(totalharga));

                        String total_harga_row_rupiah = formatRupiah((double) totalharga);
                        tv_total_harga_format_rupiah.setText(total_harga_row_rupiah);

                        dialogstatus.dismiss();

                    }
                });

                dialogstatus.show();
            }
        });

        bt_edit_qty_produk2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialogstatus = new Dialog(mCanvaser_Terima_Produk.this);
                dialogstatus.setContentView(R.layout.dialog_edit_qty_produk);

                Button batal = dialogstatus.findViewById(R.id.batal);
                Button ok = dialogstatus.findViewById(R.id.ok);
                TextView dialog_nama_produk = dialogstatus.findViewById(R.id.dialog_nama_produk);
                TextView dialog_qty_produk = dialogstatus.findViewById(R.id.dialog_qty_produk);

                TextView dialog_qty_produk_editan = dialogstatus.findViewById(R.id.dialog_qty_produk_editan);

                ImageButton bt_image_minus = dialogstatus.findViewById(R.id.bt_minus);
                ImageButton bt_image_plus = dialogstatus.findViewById(R.id.bt_plus);
                TextView qty_produk_edit = dialogstatus.findViewById(R.id.tv_qty_produk);

                dialog_nama_produk.setText("Aqua Galon Botol");
                dialog_qty_produk.setText(qty_order_asli.getText().toString());


                bt_image_plus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String szId = qty_produk_edit.getText().toString();
                        int total_qty = Integer.parseInt(String.valueOf(szId));


                        if(total_qty >= Integer.parseInt(qty_order_asli.getText().toString())){
                            new SweetAlertDialog(mCanvaser_Terima_Produk.this, SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("Qty Tidak Boleh Lebih Besar")
                                    .setConfirmText("OK")
                                    .show();
                            dialog_qty_produk_editan.setText(String.valueOf(total_qty));
                        } else {
                            total_qty++;
                            qty_produk_edit.setText(String.valueOf(total_qty));
                            dialog_qty_produk_editan.setText(String.valueOf(total_qty));
                        }


                    }
                });

                bt_image_minus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String szId = qty_produk_edit.getText().toString();
                        int total_qty = Integer.parseInt(String.valueOf(szId));
                        total_qty--;
                        qty_produk_edit.setText(String.valueOf(total_qty));
                        dialog_qty_produk_editan.setText(String.valueOf(total_qty));
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

                        qty_order_asli4.setText(dialog_qty_produk_editan.getText().toString());
                        int totalharga = Integer.parseInt(qty_order_sudah_edit.getText().toString()) * Integer.parseInt(tv_harga_satuan.getText().toString());
                        tv_total_harga_row_format_rupiah.setText(String.valueOf(totalharga));

                        //tv_total_harga_format_rupiah.setText(String.valueOf(totalharga));

                        String total_harga_row_rupiah = formatRupiah((double) totalharga);
                        tv_total_harga_format_rupiah.setText(total_harga_row_rupiah);

                        dialogstatus.dismiss();

                    }
                });

                dialogstatus.show();
            }
        });

    }

    private void history_alihCustomer() {
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String nik_baru = sharedPreferences.getString("szDocCall", null);
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_historyAlihCustomer",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        updateCustomerChange();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(statusLoop.equals("gagal")){
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        statusLoop = "Sukses";
                        new SweetAlertDialog(mCanvaser_Terima_Produk.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Jaringan Bermasalah")
                                .setConfirmText("OK")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        pDialog.dismissWithAnimation();
                                        sweetAlertDialog.dismissWithAnimation();
                                        statusLoop = "gagal";
                                    }
                                })
                                .show();
                    } else if (error instanceof AuthFailureError) {
                        Toast.makeText(mCanvaser_Terima_Produk.this, "Auth Failure Error", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof ServerError) {
                        updateCustomerChange();
                    } else if (error instanceof NetworkError) {
                        Toast.makeText(mCanvaser_Terima_Produk.this, "Network Error", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof ParseError) {
                        Toast.makeText(mCanvaser_Terima_Produk.this, "Parse Error", Toast.LENGTH_SHORT).show();
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

                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String currentDateandTime2 = sdf2.format(new Date());

                SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd");
                String currentDateandTime3 = sdf3.format(new Date());

                String[] parts = act_pilihCustomer.getText().toString().split(",");
                String customerbefore = parts[1];

                String[] parts2 = act_alih_customer.getText().toString().split(",");
                String customerafter = parts2[1];

                String rest = act_pilih_alasan.getText().toString();
                String[] alasan = rest.split("-");
                String restnomor = alasan[0];
                String restnomorbaru = restnomor.replace(" ", "");

                params.put("dtmDoc", currentDateandTime3);
                params.put("szDocId", string_nodo);

                params.put("szCustomerId_Before", customerbefore);
                params.put("szCustomerId_After", customerafter);
                params.put("szFailReason", restnomorbaru);
                params.put("szDescription", tv_canvaser_catatan_alih.getText().toString());

                params.put("UserCreatedId", nik_baru);
                params.put("dtmCreated", currentDateandTime2);
                
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
        if (requestQueue48 == null) {
            requestQueue48 = Volley.newRequestQueue(mCanvaser_Terima_Produk.this);
            requestQueue48.add(stringRequest2);
        } else {
            requestQueue48.add(stringRequest2);
        }
    }

    private void updateCustomerChange() {
        StringRequest stringRequest2 = new StringRequest(Request.Method.PUT, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_ChangeCustomer",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        updateDetProduk();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(statusLoop.equals("gagal")){
                            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                statusLoop = "Sukses";
                                new SweetAlertDialog(mCanvaser_Terima_Produk.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Jaringan Bermasalah")
                                        .setConfirmText("OK")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                pDialog.dismissWithAnimation();
                                                sweetAlertDialog.dismissWithAnimation();
                                                statusLoop = "gagal";
                                            }
                                        })
                                        .show();
                            } else if (error instanceof AuthFailureError) {
                                Toast.makeText(mCanvaser_Terima_Produk.this, "Auth Failure Error", Toast.LENGTH_SHORT).show();
                            } else if (error instanceof ServerError) {
                                updateDetProduk();
                            } else if (error instanceof NetworkError) {
                                Toast.makeText(mCanvaser_Terima_Produk.this, "Network Error", Toast.LENGTH_SHORT).show();
                            } else if (error instanceof ParseError) {
                                Toast.makeText(mCanvaser_Terima_Produk.this, "Parse Error", Toast.LENGTH_SHORT).show();
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


                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String currentDateandTime2 = sdf2.format(new Date());

                String[] parts2 = act_alih_customer.getText().toString().split(",");
                String customerafter = parts2[1];

                params.put("szDocId", string_nodo);
                params.put("szCustomerId", customerafter);



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
        RequestQueue requestQueue2 = Volley.newRequestQueue(mCanvaser_Terima_Produk.this);
        requestQueue2.add(stringRequest2);
    }

    private void updateDetProduk() {
        StringRequest stringRequest2 = new StringRequest(Request.Method.PUT, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_ChangeDetProduk",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        getDo();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(statusLoop.equals("gagal")){
                            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                statusLoop = "Sukses";
                                new SweetAlertDialog(mCanvaser_Terima_Produk.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Jaringan Bermasalah")
                                        .setConfirmText("OK")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                pDialog.dismissWithAnimation();
                                                sweetAlertDialog.dismissWithAnimation();
                                                statusLoop = "gagal";
                                            }
                                        })
                                        .show();
                            } else if (error instanceof AuthFailureError) {
                                Toast.makeText(mCanvaser_Terima_Produk.this, "Auth Failure Error", Toast.LENGTH_SHORT).show();
                            } else if (error instanceof ServerError) {
                                getDo();
                            } else if (error instanceof NetworkError) {
                                Toast.makeText(mCanvaser_Terima_Produk.this, "Network Error", Toast.LENGTH_SHORT).show();
                            } else if (error instanceof ParseError) {
                                Toast.makeText(mCanvaser_Terima_Produk.this, "Parse Error", Toast.LENGTH_SHORT).show();
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


                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String currentDateandTime2 = sdf2.format(new Date());

                String[] parts2 = act_alih_customer.getText().toString().split(",");
                String customerafter = parts2[1];

                params.put("nodot", string_nodo);
                params.put("id_customer", customerafter);



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
        RequestQueue requestQueue2 = Volley.newRequestQueue(mCanvaser_Terima_Produk.this);
        requestQueue2.add(stringRequest2);
    }

    private void pilihReason() {
        StringRequest rest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Selesai_Perjalanan/index_Reason_Tunda_Pelanggan",
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
                                    Reason_Selesai_Perjalanan.add(id + "-" + jenis_istirahat);

                                }
                            }
                            act_pilih_alasan.setAdapter(new ArrayAdapter<String>(mCanvaser_Terima_Produk.this, android.R.layout.simple_expandable_list_item_1, Reason_Selesai_Perjalanan));

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
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestkota = Volley.newRequestQueue(mCanvaser_Terima_Produk.this);
        requestkota.add(rest);
    }

    private void pilihAlihCustomer() {
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String nik_baru = sharedPreferences.getString("szDocCall", null);

        StringRequest pilih_customer = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_Canvas_Pelanggan_Luar?surat_tugas=" + STD + "&szEmployeeId=" + nik_baru ,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("status").equals("true")) {

                                pDialog.dismissWithAnimation();

                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    //parameter jika mengambul data dari url url_get_data_checklist_stagging
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    //String string_nomor_polisi = jsonObject1.getString("license_number");
                                    //parameter jika mengambul data dari url url_get_data_variant_transportir_checklist_stagging
                                    String string_nama_customer = jsonObject1.getString("szName");
                                    String string_id_customer = jsonObject1.getString("szCustomerId");
                                    array_pilihan_customer.add(string_nama_customer+","+string_id_customer);


                                    if(!jsonObject1.getString("szDocSO").equals("null")){
                                        array_pilihan_customer.remove(string_nama_customer+","+string_id_customer);
                                    }

                                }
                            }
                            act_alih_customer.setAdapter(new ArrayAdapter<String>(mCanvaser_Terima_Produk.this, android.R.layout.simple_expandable_list_item_1, array_pilihan_customer));
                            act_alih_customer.setThreshold(1);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(mCanvaser_Terima_Produk.this, String.valueOf(error), Toast.LENGTH_SHORT).show();

                        pDialog.dismissWithAnimation();
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
        if (requestQueue4 == null) {
            requestQueue4 = Volley.newRequestQueue(mCanvaser_Terima_Produk.this);
            requestQueue4.add(pilih_customer);
        } else {
            requestQueue4.add(pilih_customer);
        }
    }

    private void getSTD() {
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String szEmployeeId = sharedPreferences.getString("szDocCall", null);
        String link;
        if(pengaturanBar.getTitle().toString().equals("Pelanggan Non Rute")){
            link = "index_List_Surat_Tugas_canvaser_non_rute";
        } else {
            link = "index_List_Surat_Tugas_canvaser";
        }
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Persiapan/"+link+"?szEmployeeId=" + szEmployeeId, //szDocId,
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

                                    driver = movieObject.getString("szEmployeeId");
                                    szHelper1 = movieObject.getString("szHelper1");
                                    szVehicleId = movieObject.getString("szVehicleId");

                                }
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
//                        pDialog.dismissWithAnimation();
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
                        10000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                ));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void getProduct2(String id_cs) {
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String nik_baru = sharedPreferences.getString("szDocCall", null);
        String[] parts = nik_baru.split("-");
        String restnomor = parts[0];

        pDialog = new SweetAlertDialog(mCanvaser_Terima_Produk.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Harap Menunggu");
        pDialog.setCancelable(false);
        pDialog.show();

        draft.clear();

        StringRequest stringRequest2 = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_ProductBKB?szId="+id_cs+"&szDocId="+act_pilih_ritase.getText().toString()+"&szBranchId=" + restnomor +"&id_customer=" + id_cs,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        list_produk_ingin_dicanvas.setVisibility(View.VISIBLE);

                        pDialog.dismissWithAnimation();
                        try {
                            int number = 0;
                            String harga;
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");
                            for (int i = 0; i < movieArray.length(); i++) {
                                final JSONObject movieObject = movieArray.getJSONObject(i);
                                if(movieObject.getString("decPrice").equals("null")){
                                    harga = "0.0000";
                                } else {
                                    harga = movieObject.getString("decPrice");
                                }
                                final data_product_bkb_pojo movieItem = new data_product_bkb_pojo(
                                        movieObject.getString("decQty"),
                                        movieObject.getString("szProductId"),
                                        movieObject.getString("szName"),
                                        harga,
                                        movieObject.getString("szUomId"),
                                        movieObject.getString("decQtyDeliveredDO"),
                                        "0"); //digunakan untuk edit qty


                                draft.add(movieItem);


                                adapter2 = new ListViewAdapteProductDraft(draft, getApplicationContext());

                                list_produk_ingin_dicanvas.setAdapter(adapter2);
                                Utility.setListViewHeightBasedOnChildren(list_produk_ingin_dicanvas);

                                adapter2.notifyDataSetChanged();

                                if(movieObject.getString("decQty").equals("0.00")){
                                    draft.remove(movieItem);
                                    adapter2.notifyDataSetChanged();
                                }

                            }

                            if(draft.size() == 0){
                                new SweetAlertDialog(mCanvaser_Terima_Produk.this, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText("Stock Canvas sudah habis, silahkan akhiri ritase")
                                        .setConfirmText("OK")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.dismissWithAnimation();
                                                finish();
                                            }
                                        })
                                        .show();
                            }




                        } catch(JSONException e){
                            e.printStackTrace();

                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismissWithAnimation();

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
                        10000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
        );
        if (requestQueue38 == null) {
            requestQueue38 = Volley.newRequestQueue(mCanvaser_Terima_Produk.this);
            requestQueue38.add(stringRequest2);
        } else {
            requestQueue38.add(stringRequest2);
        }
    }

    private void updateCustomer() {
        StringRequest stringRequest2 = new StringRequest(Request.Method.PUT, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_DetProdukStd",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        updateDocDos();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        updateDocDos();
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

                String[] parts = act_pilihCustomer.getText().toString().split(",");
                String restnomor = parts[1];

                params.put("nodot", menu_pelanggan.DocDo);
                params.put("id_customer", idcustomer.getText().toString());



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
        if (requestQueue5 == null) {
            requestQueue5 = Volley.newRequestQueue(mCanvaser_Terima_Produk.this);
            requestQueue5.add(stringRequest2);
        } else {
            requestQueue5.add(stringRequest2);
        }
    }

    private void updateDocDos() {
        StringRequest stringRequest2 = new StringRequest(Request.Method.PUT, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_DocDoNonRute",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        getDo();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        getDo();
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

                String[] parts = act_pilihCustomer.getText().toString().split(",");
                String restnomor = parts[1];

                params.put("szDocId", menu_pelanggan.DocDo);
                params.put("szCustomerId", idcustomer.getText().toString());
                params.put("szVehicleId", szVehicleId);
                params.put("szHelper1", szHelper1);


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
        if (requestQueue6 == null) {
            requestQueue6 = Volley.newRequestQueue(mCanvaser_Terima_Produk.this);
            requestQueue6.add(stringRequest2);
        } else {
            requestQueue6.add(stringRequest2);
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
                                bAllowToCredit = movieObject.getString("bAllowToCredit");




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
                        10000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
        );
        if (requestQueue7 == null) {
            requestQueue7 = Volley.newRequestQueue(mCanvaser_Terima_Produk.this);
            requestQueue7.add(stringRequest2);
        } else {
            requestQueue7.add(stringRequest2);
        }
    }

    private void getLastitem() {

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
                        if(statusLoop.equals("gagal")){
                            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                statusLoop = "Sukses";
                                new SweetAlertDialog(mCanvaser_Terima_Produk.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Jaringan Bermasalah")
                                        .setConfirmText("OK")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                pDialog.dismissWithAnimation();
                                                sweetAlertDialog.dismissWithAnimation();
                                                statusLoop = "gagal";
                                            }
                                        })
                                        .show();
                            } else if (error instanceof AuthFailureError) {
                                Toast.makeText(mCanvaser_Terima_Produk.this, "Auth Failure Error", Toast.LENGTH_SHORT).show();
                            } else if (error instanceof ServerError) {
                                statusLoop = "Sukses";
                                new SweetAlertDialog(mCanvaser_Terima_Produk.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Jaringan Bermasalah")
                                        .setConfirmText("OK")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                pDialog.dismissWithAnimation();
                                                sweetAlertDialog.dismissWithAnimation();
                                                statusLoop = "gagal";
                                            }
                                        })
                                        .show();
                            } else if (error instanceof NetworkError) {
                                Toast.makeText(mCanvaser_Terima_Produk.this, "Network Error", Toast.LENGTH_SHORT).show();
                            } else if (error instanceof ParseError) {
                                Toast.makeText(mCanvaser_Terima_Produk.this, "Parse Error", Toast.LENGTH_SHORT).show();
                            }
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
                        10000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                ));
        if (requestQueue8 == null) {
            requestQueue8 = Volley.newRequestQueue(mCanvaser_Terima_Produk.this);
            requestQueue8.add(stringRequest);
        } else {
            requestQueue8.add(stringRequest);
        }
    }

    private void getDOBaru(String lastitem) {
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
                            postDOccallItem(lastitem, message);


                        } catch (JSONException e) {
                            e.printStackTrace();

                        }



                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(statusLoop.equals("gagal")){
                            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                statusLoop = "Sukses";
                                new SweetAlertDialog(mCanvaser_Terima_Produk.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Jaringan Bermasalah")
                                        .setConfirmText("OK")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                pDialog.dismissWithAnimation();
                                                sweetAlertDialog.dismissWithAnimation();
                                                statusLoop = "gagal";
                                            }
                                        })
                                        .show();
                            } else if (error instanceof AuthFailureError) {
                                Toast.makeText(mCanvaser_Terima_Produk.this, "Auth Failure Error", Toast.LENGTH_SHORT).show();
                            } else if (error instanceof ServerError) {
                                statusLoop = "Sukses";
                                new SweetAlertDialog(mCanvaser_Terima_Produk.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Jaringan Bermasalah")
                                        .setConfirmText("OK")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                pDialog.dismissWithAnimation();
                                                sweetAlertDialog.dismissWithAnimation();
                                                statusLoop = "gagal";
                                            }
                                        })
                                        .show();
                            } else if (error instanceof NetworkError) {
                                Toast.makeText(mCanvaser_Terima_Produk.this, "Network Error", Toast.LENGTH_SHORT).show();
                            } else if (error instanceof ParseError) {
                                Toast.makeText(mCanvaser_Terima_Produk.this, "Parse Error", Toast.LENGTH_SHORT).show();
                            }
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
                params.put("szCustomerId", idcustomer.getText().toString());
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

                return params;
            }

        };





        channel_status.setRetryPolicy(
                new DefaultRetryPolicy(
                        10000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                ));
        RequestQueue requestQueue = Volley.newRequestQueue(mCanvaser_Terima_Produk.this);
        requestQueue.add(channel_status);

    }

    private void postDOccallItem(String intnumber, String s) {
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_insertdocallitem",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        getDo();
                    }

                    private void SF() {
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

                                String[] customer = act_pilihCustomer.getText().toString().split(",");
                                String restnomor = customer[1];

                                params.put("szDocId", STD);
                                params.put("intItemNumber", intnumber);

                                params.put("szCustomerId", idcustomer.getText().toString());

                                params.put("dtmStart", currentDateandTime2);
                                params.put("dtmFinish", currentDateandTime2);

                                params.put("bVisited", "1");
                                params.put("bSuccess", "1");


                                params.put("szFailReason", "");
                                params.put("szLangitude", MainActivity.latitude);
                                params.put("szLongitude", MainActivity.longitude);

                                if(pelanggan.equals("canvaser_luar")){
                                    params.put("bOutOfRoute", "1");
                                } else {
                                    params.put("bOutOfRoute", "0");
                                }

                                params.put("szRefDocId", s);

                                params.put("bScanBarcode", "0");

                                params.put("szReasonIdCheckin", "");
                                params.put("szReasonFailedScan", "");
                                params.put("decRadiusDiff", "0");

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
                        RequestQueue requestQueue2 = Volley.newRequestQueue(mCanvaser_Terima_Produk.this);
                        requestQueue2.getCache().clear();
                        requestQueue2.add(stringRequest2);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(statusLoop.equals("gagal")){
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        statusLoop = "Sukses";
                        new SweetAlertDialog(mCanvaser_Terima_Produk.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Jaringan Bermasalah")
                                .setConfirmText("OK")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        pDialog.dismissWithAnimation();
                                        sweetAlertDialog.dismissWithAnimation();
                                        statusLoop = "gagal";
                                    }
                                })
                                .show();
                    } else if (error instanceof AuthFailureError) {
                        Toast.makeText(mCanvaser_Terima_Produk.this, "Auth Failure Error", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof ServerError) {
                        getDo();
                    } else if (error instanceof NetworkError) {
                        Toast.makeText(mCanvaser_Terima_Produk.this, "Network Error", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof ParseError) {
                        Toast.makeText(mCanvaser_Terima_Produk.this, "Parse Error", Toast.LENGTH_SHORT).show();
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

                String[] customer = act_pilihCustomer.getText().toString().split(",");
                String restnomor = customer[1];

                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String currentDateandTime2 = sdf2.format(new Date());

                params.put("szDocId", STD);
                params.put("intItemNumber", intnumber);
                params.put("szCustomerId", idcustomer.getText().toString());

                params.put("dtmStart", currentDateandTime2);
                params.put("dtmFinish", currentDateandTime2);

                params.put("dtmLastCheckin", currentDateandTime2);

                params.put("szLangitude", latitude);
                params.put("szLongitude", longitude);

                params.put("szDocSO", "");
                params.put("szDocDO", s);

                params.put("szRefDocId", "");

                params.put("bOutOfRoute", "1");
                params.put("bScanBarcode", mulai_perjalanan.StatusBarcode);

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
        if (requestQueue11 == null) {
            requestQueue11 = Volley.newRequestQueue(mCanvaser_Terima_Produk.this);
            requestQueue11.add(stringRequest2);
        } else {
            requestQueue11.add(stringRequest2);
        }
    }

    private void getComparator() {
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String szEmployeeId = sharedPreferences.getString("szDocCall", null);
        String link;
        if(pengaturanBar.getTitle().toString().equals("Pelanggan Non Rute")){
            link = "index_List_Surat_Tugas_canvaser_non_rute";
        } else {
            link = "index_List_Surat_Tugas_canvaser";
        }
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Persiapan/"+link+"?szEmployeeId=" + szEmployeeId, //szDocId,
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

                                pilihBKB(movieObject.getString("szDocId"));

                            }


                        } catch(JSONException e){
                            e.printStackTrace();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pilihBKB(STD);

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
                        10000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                ));
        if (requestQueue12 == null) {
            requestQueue12 = Volley.newRequestQueue(mCanvaser_Terima_Produk.this);
            requestQueue12.add(stringRequest);
        } else {
            requestQueue12.add(stringRequest);
        }
    }

    private void pilihBKB(String szDocId) {
        //
        if(pengaturanBar.getTitle().toString().equals("Pelanggan Luar Rute")){
            StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_BKBALL?id_std=" + szDocId, //szDocId,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                int number = 0;
                                JSONObject obj = new JSONObject(response);
                                if (obj.getString("status").equals("true")) {
                                    final JSONArray movieArray = obj.getJSONArray("data");
                                    for (int i = 0; i < movieArray.length(); i++) {
                                        final JSONObject movieObject = movieArray.getJSONObject(i);

//
                                        driver_list.add(movieObject.getString("id_driver"));
                                        szHelper1_list.add(movieObject.getString("szHelper1"));
                                        szVehicleId_list.add(movieObject.getString("szVehicleId"));
                                        id_pb_list.add(movieObject.getString("id_pb"));
                                        ritase_list.add(movieObject.getString("ritase"));
                                        refstd.add(movieObject.getString("mix_ref_std"));

                                        if(movieObject.getString("statusActivity").equals("1")){
                                            pilih_ritase.remove(movieObject.getString("id_bkb"));
                                            driver_list.remove(movieObject.getString("id_driver"));
                                            szHelper1_list.remove(movieObject.getString("szHelper1"));
                                            szVehicleId_list.remove(movieObject.getString("szVehicleId"));
                                            id_pb_list.remove(movieObject.getString("id_pb"));
                                            ritase_list.remove(movieObject.getString("ritase"));
                                            refstd.remove(movieObject.getString("mix_ref_std"));
                                        } else if(movieObject.getString("id_bkb").equals("null")){
                                            pilih_ritase.remove(movieObject.getString("id_bkb"));
                                            driver_list.remove(movieObject.getString("id_driver"));
                                            szHelper1_list.remove(movieObject.getString("szHelper1"));
                                            szVehicleId_list.remove(movieObject.getString("szVehicleId"));
                                            id_pb_list.remove(movieObject.getString("id_pb"));
                                            ritase_list.remove(movieObject.getString("ritase"));
                                            refstd.remove(movieObject.getString("mix_ref_std"));

                                        } else {
                                            pilih_ritase.add(movieObject.getString("id_bkb"));
                                        }



                                    }



                                }
                                act_pilih_ritase.setAdapter(new ArrayAdapter<String>(mCanvaser_Terima_Produk.this, android.R.layout.simple_expandable_list_item_1, pilih_ritase));




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
                            10000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                    ));
            if (requestQueue50 == null) {
                requestQueue50 = Volley.newRequestQueue(mCanvaser_Terima_Produk.this);
                requestQueue50.add(stringRequest);
            } else {
                requestQueue50.add(stringRequest);
            }
        } else if(pengaturanBar.getTitle().toString().equals("Pelanggan Dalam Rute")){
            sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
            String nik_baru = sharedPreferences.getString("szDocCall", null);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_BKBDalamV2?id_std="+STD+"&id_customer="+idcustomer.getText().toString()+"&id_driver="+nik_baru,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                int number = 0;
                                JSONObject obj = new JSONObject(response);
                                if (obj.getString("status").equals("true")) {
                                    final JSONArray movieArray = obj.getJSONArray("data");
                                    for (int i = 0; i < movieArray.length(); i++) {
                                        final JSONObject movieObject = movieArray.getJSONObject(i);

//
                                        pilih_ritase.add(movieObject.getString("id_bkb"));
                                        driver_list.add(movieObject.getString("id_driver"));
                                        szHelper1_list.add(movieObject.getString("szHelper1"));
                                        szVehicleId_list.add(movieObject.getString("szVehicleId"));
                                        id_pb_list.add(movieObject.getString("id_pb"));
                                        ritase_list.add(movieObject.getString("ritase"));
                                        nodot.add(movieObject.getString("nodot"));
                                        refstd.add(movieObject.getString("mix_ref_std"));

                                        if(!movieObject.getString("nodot").equals(menu_pelanggan.DocDo)){
                                            pilih_ritase.remove(movieObject.getString("id_bkb"));
                                            driver_list.remove(movieObject.getString("id_driver"));
                                            szHelper1_list.remove(movieObject.getString("szHelper1"));
                                            szVehicleId_list.remove(movieObject.getString("szVehicleId"));
                                            id_pb_list.remove(movieObject.getString("id_pb"));
                                            ritase_list.remove(movieObject.getString("ritase"));
                                            nodot.remove(movieObject.getString("nodot"));
                                            refstd.remove(movieObject.getString("mix_ref_std"));
                                        }
//                                        else if(movieObject.getString("statusActivity").equals("1")){
//                                            pilih_ritase.remove(movieObject.getString("id_bkb"));
//                                            driver_list.remove(movieObject.getString("id_driver"));
//                                            szHelper1_list.remove(movieObject.getString("szHelper1"));
//                                            szVehicleId_list.remove(movieObject.getString("szVehicleId"));
//                                            id_pb_list.remove(movieObject.getString("id_pb"));
//                                            ritase_list.remove(movieObject.getString("ritase"));
//                                            nodot.remove(movieObject.getString("nodot"));
//                                            refstd.remove(movieObject.getString("mix_ref_std"));
//                                        } else if(movieObject.getString("id_bkb").equals("null")){
//                                            pilih_ritase.remove(movieObject.getString("id_bkb"));
//                                            driver_list.remove(movieObject.getString("id_driver"));
//                                            szHelper1_list.remove(movieObject.getString("szHelper1"));
//                                            szVehicleId_list.remove(movieObject.getString("szVehicleId"));
//                                            id_pb_list.remove(movieObject.getString("id_pb"));
//                                            ritase_list.remove(movieObject.getString("ritase"));
//                                            nodot.remove(movieObject.getString("nodot"));
//                                            refstd.remove(movieObject.getString("mix_ref_std"));
//                                        }



                                    }



                                }
                                act_pilih_ritase.setAdapter(new ArrayAdapter<String>(mCanvaser_Terima_Produk.this, android.R.layout.simple_expandable_list_item_1, pilih_ritase));





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
                            10000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                    ));
            RequestQueue requestQueue = Volley.newRequestQueue(mCanvaser_Terima_Produk.this);
            requestQueue.add(stringRequest);
        } else {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_bkb_do?id_std="+STD+"&nodot="+menu_pelanggan.DocDo,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                int number = 0;
                                JSONObject obj = new JSONObject(response);
                                if (obj.getString("status").equals("true")) {
                                    final JSONArray movieArray = obj.getJSONArray("data");
                                    for (int i = 0; i < movieArray.length(); i++) {
                                        final JSONObject movieObject = movieArray.getJSONObject(i);

//
                                        driver_list.add(movieObject.getString("id_driver"));
                                        szHelper1_list.add(movieObject.getString("szHelper1"));
                                        szVehicleId_list.add(movieObject.getString("szVehicleId"));
                                        id_pb_list.add(movieObject.getString("id_pb"));
                                        ritase_list.add(movieObject.getString("ritase"));
                                        nodot.add(movieObject.getString("nodot"));
                                        refstd.add(movieObject.getString("mix_ref_std"));

                                        if(movieObject.getString("statusActivity").equals("1")){
                                            pilih_ritase.remove(movieObject.getString("id_bkb"));
                                            driver_list.remove(movieObject.getString("id_driver"));
                                            szHelper1_list.remove(movieObject.getString("szHelper1"));
                                            szVehicleId_list.remove(movieObject.getString("szVehicleId"));
                                            id_pb_list.remove(movieObject.getString("id_pb"));
                                            ritase_list.remove(movieObject.getString("ritase"));
                                            nodot.remove(movieObject.getString("nodot"));
                                            refstd.remove(movieObject.getString("mix_ref_std"));
                                        } else if(movieObject.getString("id_bkb").equals("null")){
                                            pilih_ritase.remove(movieObject.getString("id_bkb"));
                                            driver_list.remove(movieObject.getString("id_driver"));
                                            szHelper1_list.remove(movieObject.getString("szHelper1"));
                                            szVehicleId_list.remove(movieObject.getString("szVehicleId"));
                                            id_pb_list.remove(movieObject.getString("id_pb"));
                                            ritase_list.remove(movieObject.getString("ritase"));
                                            nodot.remove(movieObject.getString("nodot"));
                                            refstd.remove(movieObject.getString("mix_ref_std"));
                                        } else if(!movieObject.getString("nodot").equals(menu_pelanggan.DocDo)){
                                            pilih_ritase.remove(movieObject.getString("id_bkb"));
                                            driver_list.remove(movieObject.getString("id_driver"));
                                            szHelper1_list.remove(movieObject.getString("szHelper1"));
                                            szVehicleId_list.remove(movieObject.getString("szVehicleId"));
                                            id_pb_list.remove(movieObject.getString("id_pb"));
                                            ritase_list.remove(movieObject.getString("ritase"));
                                            nodot.remove(movieObject.getString("nodot"));
                                            refstd.remove(movieObject.getString("mix_ref_std"));
                                        }else {
                                            System.out.println("Nodot 4 = " + movieObject.getString("nodot") + " nodot yang dipilih = " + menu_pelanggan.DocDo);

                                            pilih_ritase.add(movieObject.getString("id_bkb"));
                                        }



                                    }



                                }
                                act_pilih_ritase.setAdapter(new ArrayAdapter<String>(mCanvaser_Terima_Produk.this, android.R.layout.simple_expandable_list_item_1, pilih_ritase));





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
                            10000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                    ));
            RequestQueue requestQueue = Volley.newRequestQueue(mCanvaser_Terima_Produk.this);
            requestQueue.add(stringRequest);
        }


    }

    private void putQty() {
        for(int i = 0;i <=  data_terima_produk_pojos.size() - 1 ;i++) {
            int finalI = i;
            StringRequest stringRequest2 = new StringRequest(Request.Method.PUT, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_Delivery",
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

                    String[] asli = adapter.getItem(finalI).getDecQty().split("\\.");
                    String splitnomor = asli[0];

                    String edit = adapter.getItem(finalI).getQtyEdit();

                    String[] deliveredqty = adapter.getItem(finalI).getDecQtyDeliveredDO().split("\\.");
                    String splitdelivered = deliveredqty[0];

                    int decQty = Integer.parseInt(splitnomor) - Integer.parseInt(edit);

                    int decQtyDeliveredDO = Integer.parseInt(splitdelivered) + Integer.parseInt(edit);

                    params.put("szDocId", tv_canvas_no_bkb.getText().toString());

                    params.put("szProductId", adapter.getItem(finalI).getSzProductId());
                    params.put("decQty", String.valueOf(decQty) + ".00");
                    params.put("decQtyDeliveredDO", String.valueOf(decQtyDeliveredDO) + ".00");


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
            if (requestQueue13 == null) {
                requestQueue13 = Volley.newRequestQueue(mCanvaser_Terima_Produk.this);
                requestQueue13.add(stringRequest2);
            } else {
                requestQueue13.add(stringRequest2);
            }
        }

    }

    private void getTax() {
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
                        10000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                ));
        if (requestQueue14 == null) {
            requestQueue14 = Volley.newRequestQueue(mCanvaser_Terima_Produk.this);
            requestQueue14.add(channel_status);
        } else {
            requestQueue14.add(channel_status);
        }
    }

    private void getNoInduk() {

        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateandTime2 = sdf2.format(new Date());

        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String nik_baru = sharedPreferences.getString("szDocCall", null);
        String[] parts = nik_baru.split("-");
        String restnomor = parts[0];
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_NoSoInduk?tgl="+currentDateandTime2+"&depo=" + restnomor,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");
                            for (int i = 0; i < movieArray.length(); i++) {
                                final JSONObject movieObject = movieArray.getJSONObject(i);
                                getSO(movieObject.getString("iniso_induk"));

                                soInduk = movieObject.getString("iniso_induk");
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(statusLoop.equals("gagal")){
                            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                statusLoop = "Sukses";
                                new SweetAlertDialog(mCanvaser_Terima_Produk.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Jaringan Bermasalah")
                                        .setConfirmText("OK")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                pDialog.dismissWithAnimation();
                                                sweetAlertDialog.dismissWithAnimation();
                                                statusLoop = "gagal";
                                            }
                                        })
                                        .show();
                            } else if (error instanceof AuthFailureError) {
                                Toast.makeText(mCanvaser_Terima_Produk.this, "Auth Failure Error", Toast.LENGTH_SHORT).show();
                            } else if (error instanceof ServerError) {
                                statusLoop = "Sukses";
                                new SweetAlertDialog(mCanvaser_Terima_Produk.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Jaringan Bermasalah")
                                        .setConfirmText("OK")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                pDialog.dismissWithAnimation();
                                                sweetAlertDialog.dismissWithAnimation();
                                                statusLoop = "gagal";
                                            }
                                        })
                                        .show();
                            } else if (error instanceof NetworkError) {
                                Toast.makeText(mCanvaser_Terima_Produk.this, "Network Error", Toast.LENGTH_SHORT).show();
                            } else if (error instanceof ParseError) {
                                Toast.makeText(mCanvaser_Terima_Produk.this, "Parse Error", Toast.LENGTH_SHORT).show();
                            }
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
                        10000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                ));
        if (requestQueue15 == null) {
            requestQueue15 = Volley.newRequestQueue(mCanvaser_Terima_Produk.this);
            requestQueue15.add(stringRequest);
        } else {
            requestQueue15.add(stringRequest);
        }

    }

    private void postSoInduk(String iniso_induk) {

            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_SoInduk",
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject api_response = new JSONObject(response);
                                String message = api_response.getString("noSo");
                                SOInduk2 = message;
                                mdbatotalharga();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if(statusLoop.equals("gagal")){
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            statusLoop = "Sukses";
                            new SweetAlertDialog(mCanvaser_Terima_Produk.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Jaringan Bermasalah")
                                    .setConfirmText("OK")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            pDialog.dismissWithAnimation();
                                            sweetAlertDialog.dismissWithAnimation();
                                            statusLoop = "gagal";
                                        }
                                    })
                                    .show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(mCanvaser_Terima_Produk.this, "Auth Failure Error", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ServerError) {
                            postDocSo();
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(mCanvaser_Terima_Produk.this, "Network Error", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ParseError) {
                            Toast.makeText(mCanvaser_Terima_Produk.this, "Parse Error", Toast.LENGTH_SHORT).show();
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

                    SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String currentDateandTime3 = sdf3.format(new Date());

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String currentDateandTime = sdf.format(new Date());

                    String[] parts = nik_baru.split("-");
                    String branch = parts[0];

                    String[] customer = act_pilihCustomer.getText().toString().split(",");
                    String restnomor = customer[1];



                    params.put("noso_induk", "");

                    params.put("noso_turunan", "");

                    if(checkalihcustomer.isChecked()){
                        String[] parts2 = act_alih_customer.getText().toString().split(",");
                        String customerafter = parts2[1];
                        params.put("id_customer", customerafter);
                    } else {
                        params.put("id_customer", idcustomer.getText().toString());
                    }

                    params.put("id_employ", nik_baru);
                    params.put("Id_User_Created", nik_baru);
                    if (branch.equals("321") || branch.equals("336") || branch.equals("317") || branch.equals("'036'")) {
                        params.put("kode_company", "ASA");
                    } else {
                        params.put("kode_company", "TVIP");
                    }
                    params.put("kode_depo", branch);

                    params.put("tgl_cari", currentDateandTime);
                    params.put("tgl_insert", currentDateandTime3);


                    params.put("status", "OPEN");

                    if (chk_credit.isChecked()) {
                        params.put("type_bayar", "1");
                    } else {
                        params.put("type_bayar", "2");
                    }


                    params.put("req_date", currentDateandTime3);


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
        if (requestQueue16 == null) {
            requestQueue16 = Volley.newRequestQueue(mCanvaser_Terima_Produk.this);
            requestQueue16.add(stringRequest2);
        } else {
            requestQueue16.add(stringRequest2);
        }

    }

    private void putProductQty() {
        for(int i = 0; i < data_terima_produk_pojos.size(); i++){
            int finalI = i;
            StringRequest stringRequest2 = new StringRequest(Request.Method.PUT, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_produkstd",
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            if (finalI == data_terima_produk_pojos.size() - 1) {
                                getDOInduk();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if(statusLoop.equals("gagal")){
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            statusLoop = "Sukses";
                            System.out.println("Error 1");
                            new SweetAlertDialog(mCanvaser_Terima_Produk.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Jaringan Bermasalah")
                                    .setConfirmText("OK")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            pDialog.dismissWithAnimation();
                                            sweetAlertDialog.dismissWithAnimation();
                                            statusLoop = "gagal";
                                        }
                                    })
                                    .show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(mCanvaser_Terima_Produk.this, "Auth Failure Error", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ServerError) {
                            if (finalI == data_terima_produk_pojos.size() - 1) {
                                getDOInduk();
                            }
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(mCanvaser_Terima_Produk.this, "Network Error", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ParseError) {
                            Toast.makeText(mCanvaser_Terima_Produk.this, "Parse Error", Toast.LENGTH_SHORT).show();
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


                    params.put("nodot", DocDo);
                    params.put("id_produk", adapter.getItem(finalI).getSzProductId());
                    params.put("id_bkb", tv_canvas_no_bkb.getText().toString());


                    params.put("nosot", "");


                    params.put("userid_insert", nik_baru);

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
            if (requestQueue45 == null) {
                requestQueue45 = Volley.newRequestQueue(mCanvaser_Terima_Produk.this);
                requestQueue45.add(stringRequest2);
            } else {
                requestQueue45.add(stringRequest2);
            }
        }

    }

//    private void postDOInduk(String inido_induk, String noso_induk) {
//            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_DoInduk",
//                    new Response.Listener<String>() {
//
//                        @Override
//                        public void onResponse(String response) {
////                            postDocDoItem(DocDo);
//
//
//                        }
//                    }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    {
//                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
//                            statusLoop = "Sukses";
//                            new SweetAlertDialog(mCanvaser_Terima_Produk.this, SweetAlertDialog.ERROR_TYPE)
//                                    .setTitleText("Jaringan Bermasalah")
//                                    .setConfirmText("OK")
//                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                                        @Override
//                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
//                                            pDialog.dismissWithAnimation();
//                                            sweetAlertDialog.dismissWithAnimation();
//                                            statusLoop = "gagal";
//                                        }
//                                    })
//                                    .show();
//                        } else if (error instanceof AuthFailureError) {
//                            Toast.makeText(mCanvaser_Terima_Produk.this, "Auth Failure Error", Toast.LENGTH_SHORT).show();
//                        } else if (error instanceof ServerError) {
////                            postDocDoItem(DocDo);
//
//                            getSegment2();
//                            putQty();
//                        } else if (error instanceof NetworkError) {
//                            Toast.makeText(mCanvaser_Terima_Produk.this, "Network Error", Toast.LENGTH_SHORT).show();
//                        } else if (error instanceof ParseError) {
//                            Toast.makeText(mCanvaser_Terima_Produk.this, "Parse Error", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//
//                }
//
//            }) {
//                @Override
//                public Map<String, String> getHeaders() throws AuthFailureError {
//                    HashMap<String, String> params = new HashMap<String, String>();
//                    String creds = String.format("%s:%s", "admin", "Databa53");
//                    String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
//                    params.put("Authorization", auth);
//                    return params;
//                }
//
//                @Override
//                protected Map<String, String> getParams() throws AuthFailureError {
//                    Map<String, String> params = new HashMap<String, String>();
//
//
//
//                    return params;
//                }
//
//            };
//            stringRequest2.setRetryPolicy(
//                    new DefaultRetryPolicy(
//                            10000,
//                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
//                    )
//            );
//        if (requestQueue17 == null) {
//            requestQueue17 = Volley.newRequestQueue(mCanvaser_Terima_Produk.this);
//            requestQueue17.add(stringRequest2);
//        } else {
//            requestQueue17.add(stringRequest2);
//        }
//    }

    private void getLastNumber() {
        Toast.makeText(getApplicationContext(), "Buat Baru", Toast.LENGTH_SHORT).show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_std_lastItem?id_bkb=" + tv_canvas_no_bkb.getText().toString(), //szDocId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            int number = 0;
                            JSONObject obj = new JSONObject(response);
                            if (obj.getString("status").equals("true")) {
                                final JSONArray movieArray = obj.getJSONArray("data");
                                for (int i = 0; i < movieArray.length(); i++) {
                                    final JSONObject movieObject = movieArray.getJSONObject(i);
                                        postProductQty(movieObject.getString("nourut_item"));

                                }



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
                        if(statusLoop.equals("gagal")){
                            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                statusLoop = "Sukses";
                                new SweetAlertDialog(mCanvaser_Terima_Produk.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Jaringan Bermasalah")
                                        .setConfirmText("OK")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                pDialog.dismissWithAnimation();
                                                sweetAlertDialog.dismissWithAnimation();
                                                statusLoop = "gagal";
                                            }
                                        })
                                        .show();
                            } else if (error instanceof AuthFailureError) {
                                Toast.makeText(mCanvaser_Terima_Produk.this, "Auth Failure Error", Toast.LENGTH_SHORT).show();
                            } else if (error instanceof ServerError) {
                                statusLoop = "Sukses";
                                new SweetAlertDialog(mCanvaser_Terima_Produk.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Jaringan Bermasalah")
                                        .setConfirmText("OK")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                pDialog.dismissWithAnimation();
                                                sweetAlertDialog.dismissWithAnimation();
                                                statusLoop = "gagal";
                                            }
                                        })
                                        .show();
                            } else if (error instanceof NetworkError) {
                                Toast.makeText(mCanvaser_Terima_Produk.this, "Network Error", Toast.LENGTH_SHORT).show();
                            } else if (error instanceof ParseError) {
                                Toast.makeText(mCanvaser_Terima_Produk.this, "Parse Error", Toast.LENGTH_SHORT).show();
                            }
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
                        10000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                ));
        RequestQueue requestQueue = Volley.newRequestQueue(mCanvaser_Terima_Produk.this);
        requestQueue.add(stringRequest);

    }

    private void postProductQty(String nourut_item) {
        for(int i = 0; i <= data_terima_produk_pojos.size()-1; i++){
            int finalI = i;
            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_produkstd",
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            if (finalI == data_terima_produk_pojos.size() - 1) {
                                getDOInduk();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if(statusLoop.equals("gagal")){
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            statusLoop = "Sukses";
                            new SweetAlertDialog(mCanvaser_Terima_Produk.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Jaringan Bermasalah")
                                    .setConfirmText("OK")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            pDialog.dismissWithAnimation();
                                            sweetAlertDialog.dismissWithAnimation();
                                            statusLoop = "gagal";
                                        }
                                    })
                                    .show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(mCanvaser_Terima_Produk.this, "Auth Failure Error", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ServerError) {
                            if (finalI == data_terima_produk_pojos.size() - 1) {
                                getDOInduk();
                            }
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(mCanvaser_Terima_Produk.this, "Network Error", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ParseError) {
                            Toast.makeText(mCanvaser_Terima_Produk.this, "Parse Error", Toast.LENGTH_SHORT).show();
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

                    SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String currentDateandTime3 = sdf3.format(new Date());

                    SimpleDateFormat sdf4 = new SimpleDateFormat("yyyy-MM-dd");
                    String currentDateandTime4 = sdf4.format(new Date());

                    String[] customer = act_pilihCustomer.getText().toString().split(",");
                    String restnomor = customer[1];

                    params.put("id_std", tv_canvas_no_std.getText().toString());
                    params.put("nosot", "");

                    params.put("id_driver", nik_baru);
                    params.put("id_customer", idcustomer.getText().toString());
                    params.put("id_produk", adapter.getItem(finalI).getSzProductId());
                    params.put("id_pb", id_pb);
                    params.put("id_bkb", tv_canvas_no_bkb.getText().toString());

                    String[] parts = nik_baru.split("-");
                    String branch = parts[0];

                    String[] asli = adapter.getItem(finalI).getQtyEdit().split("\\.");
                    String splitasli = asli[0];

                    params.put("nodot", DocDo);
                    params.put("qty_produk", splitasli);
                    params.put("nourut_item", String.valueOf(finalI + 1 + Integer.parseInt(nourut_item)));
                    params.put("tgl_insert", currentDateandTime4);
                    params.put("id_typejual", adapter.getItem(finalI).getSzUomId());
                    params.put("depo", branch);

                    if(textpenjualan.getText().toString().equals("Penjualan (MIX)")){
                        params.put("status_produk_mix", "1");
                    } else {
                        params.put("status_produk_mix", "0");
                    }




                    System.out.println("Hasil params " + params);


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
            if (requestQueue47 == null) {
                requestQueue47 = Volley.newRequestQueue(mCanvaser_Terima_Produk.this);
                requestQueue47.add(stringRequest2);
            } else {
                requestQueue47.add(stringRequest2);
            }

        }

    }

    private void postDocSo() {

            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_DocSo",
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            postSoDocItem(tv_canvas_no_so.getText().toString());
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if(statusLoop.equals("gagal")){
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            statusLoop = "Sukses";
                            new SweetAlertDialog(mCanvaser_Terima_Produk.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Jaringan Bermasalah")
                                    .setConfirmText("OK")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            pDialog.dismissWithAnimation();
                                            sweetAlertDialog.dismissWithAnimation();
                                            statusLoop = "gagal";
                                        }
                                    })
                                    .show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(mCanvaser_Terima_Produk.this, "Auth Failure Error", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ServerError) {
                            postSoDocItem(tv_canvas_no_so.getText().toString());
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(mCanvaser_Terima_Produk.this, "Network Error", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ParseError) {
                            Toast.makeText(mCanvaser_Terima_Produk.this, "Parse Error", Toast.LENGTH_SHORT).show();
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

                    SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String currentDateandTime3 = sdf3.format(new Date());

                    SimpleDateFormat sdf4 = new SimpleDateFormat("yyyy-MM-dd");
                    String currentDateandTime4 = sdf4.format(new Date());

                    String[] customer = act_pilihCustomer.getText().toString().split(",");
                    String restnomor = customer[1];

                    params.put("szDocId", tv_canvas_no_so.getText().toString());



                    params.put("dtmDoc", currentDateandTime4);
                    params.put("szEmployeeId", nik_baru);

                    if(checkalihcustomer.isChecked()){
                        String[] parts2 = act_alih_customer.getText().toString().split(",");
                        String customerafter = parts2[1];
                        params.put("szCustomerId", customerafter);
                    } else {
                        params.put("szCustomerId", idcustomer.getText().toString());
                    }

                    params.put("decAmount", String.valueOf(total_harga) + ".0000");
                    params.put("intPrintedCount", "0");
                    params.put("szDocCallId", STD);

                    String[] parts = nik_baru.split("-");
                    String branch = parts[0];

                    params.put("szBranchId", branch);

                    if (branch.equals("321") || branch.equals("336") || branch.equals("317") || branch.equals("'036'")) {
                        params.put("szCompanyId", "ASA");
                    } else {
                        params.put("szCompanyId", "TVIP");
                    }
                    params.put("szDocStatus", "Draft");


                    params.put("szUserCreatedId", nik_baru);
                    params.put("szUserUpdatedId", nik_baru);
                    params.put("dtmCreated", currentDateandTime3);

                    params.put("dtmLastUpdated", currentDateandTime3);

                    params.put("dtmDelivery", currentDateandTime4);
                    params.put("szNote", tv_canvaser_catatan.getText().toString());

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
                            10000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                    )
            );
        if (requestQueue18 == null) {
            requestQueue18 = Volley.newRequestQueue(mCanvaser_Terima_Produk.this);
            requestQueue18.add(stringRequest2);
        } else {
            requestQueue18.add(stringRequest2);
        }

    }

    private void postSoDocItem(String noSo) {
        for(int i = 0;i <=  data_terima_produk_pojos.size() - 1 ;i++) {
            int finalI = i;
            int finalI1 = i;
            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_PenjualanItem",
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            if (finalI == data_terima_produk_pojos.size() - 1) {
                                getSegment();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if(statusLoop.equals("gagal")){
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            statusLoop = "Sukses";
                            new SweetAlertDialog(mCanvaser_Terima_Produk.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Jaringan Bermasalah")
                                    .setConfirmText("OK")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            pDialog.dismissWithAnimation();
                                            sweetAlertDialog.dismissWithAnimation();
                                            statusLoop = "gagal";
                                        }
                                    })
                                    .show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(mCanvaser_Terima_Produk.this, "Auth Failure Error", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ServerError) {
                            if (finalI == data_terima_produk_pojos.size() - 1) {
                                getSegment();
                            }
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(mCanvaser_Terima_Produk.this, "Network Error", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ParseError) {
                            Toast.makeText(mCanvaser_Terima_Produk.this, "Parse Error", Toast.LENGTH_SHORT).show();
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



                    params.put("szDocId", noSo);

                    params.put("intItemNumber", String.valueOf(finalI));
                    params.put("szProductId", adapter.getItem(finalI1).getSzProductId());

                    params.put("decQty", adapter.getItem(finalI1).getQtyEdit());
                    params.put("szUomId", adapter.getItem(finalI1).getSzUomId());


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
            if (requestQueue20 == null) {
                requestQueue20 = Volley.newRequestQueue(mCanvaser_Terima_Produk.this);
                requestQueue20.add(stringRequest2);
            } else {
                requestQueue20.add(stringRequest2);
            }
        }
    }

    private void getSegment() {
        for(int i = 0; i < data_terima_produk_pojos.size(); i++){
            int finalI = i;
            String restnomor;
            if(checkalihcustomer.isChecked()){
                String[] parts2 = act_alih_customer.getText().toString().split(",");
                restnomor = parts2[1];
            } else {
                String[] customer = act_pilihCustomer.getText().toString().split(",");
                restnomor = customer[1];
            }


            sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
            String nik_baru = sharedPreferences.getString("szDocCall", null);
            String[] parts = nik_baru.split("-");
            String restnomor1 = parts[0];

            StringRequest rest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_SzPride?szProductId="+adapter.getItem(i).getSzProductId()+"&employeeId=" + restnomor + "&kode=" + restnomor1,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getString("status").equals("true")) {
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                        int discount = Integer.parseInt(adapter.getItem(finalI).getDiskon_tiv()) + Integer.parseInt(adapter.getItem(finalI).getDiskon_distributor()) + Integer.parseInt(adapter.getItem(finalI).getDiskon_internal());

                                        String[] parts = adapter.getItem(finalI).getDecPrice().split("\\.");
                                        String restnomor = parts[0];

                                        int counts = Integer.parseInt(adapter.getItem(finalI).getQtyEdit()) * Integer.parseInt(restnomor);
                                        postSegment(jsonObject1.getString("pricesegment"), tv_canvas_no_so.getText().toString(), String.valueOf(finalI), adapter.getItem(finalI).getDecPrice(), discount, counts, adapter.getItem(finalI).getDiskon_tiv(), adapter.getItem(finalI).getDiskon_distributor(), adapter.getItem(finalI).getDiskon_internal(), String.valueOf(finalI));



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
                            int discount = Integer.parseInt(adapter.getItem(finalI).getDiskon_tiv()) + Integer.parseInt(adapter.getItem(finalI).getDiskon_distributor()) + Integer.parseInt(adapter.getItem(finalI).getDiskon_internal());

                            String[] parts = adapter.getItem(finalI).getDecPrice().split("\\.");
                            String restnomor = parts[0];
                            String[] customer = act_pilihCustomer.getText().toString().split(",");
                            String restnomor2 = customer[1];
                            String[] parts3 = restnomor2.split("-");
                            String restnomor3 = parts3[0];

                            int counts = Integer.parseInt(adapter.getItem(finalI).getQtyEdit()) * Integer.parseInt(restnomor);
                            postSegment(restnomor3+"-C_00", tv_canvas_no_so.getText().toString(), String.valueOf(finalI), adapter.getItem(finalI).getDecPrice(), discount, counts, adapter.getItem(finalI).getDiskon_tiv(), adapter.getItem(finalI).getDiskon_distributor(), adapter.getItem(finalI).getDiskon_internal(), String.valueOf(finalI));

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
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            if (requestQueue21 == null) {
                requestQueue21 = Volley.newRequestQueue(mCanvaser_Terima_Produk.this);
                requestQueue21.add(rest);
            } else {
                requestQueue21.add(rest);
            }
        }
    }

    private void postSegment(String pricesegment, String no_surats, String s, String decPrice, int discount, int counts, String disc_principle, String disc_distributor, String disc_internal, String finalI) {
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_DocSoPrice",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        if(Integer.parseInt(finalI) == data_terima_produk_pojos.size() - 1){
                            postSoInduk(tv_canvas_no_so.getText().toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(statusLoop.equals("gagal")){
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        statusLoop = "Sukses";
                        new SweetAlertDialog(mCanvaser_Terima_Produk.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Jaringan Bermasalah")
                                .setConfirmText("OK")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        pDialog.dismissWithAnimation();
                                        sweetAlertDialog.dismissWithAnimation();
                                        statusLoop = "gagal";
                                    }
                                })
                                .show();
                    } else if (error instanceof AuthFailureError) {
                        Toast.makeText(mCanvaser_Terima_Produk.this, "Auth Failure Error", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof ServerError) {
                        if(Integer.parseInt(finalI) == data_terima_produk_pojos.size() - 1){
                            mdbatotalharga();
                        }
                    } else if (error instanceof NetworkError) {
                        Toast.makeText(mCanvaser_Terima_Produk.this, "Network Error", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof ParseError) {
                        Toast.makeText(mCanvaser_Terima_Produk.this, "Parse Error", Toast.LENGTH_SHORT).show();
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

                params.put("szProductId", adapter.getItem(Integer.parseInt(finalI)).getSzProductId());

                params.put("szDocId", tv_canvas_no_so.getText().toString());

                params.put("intItemNumber", String.valueOf(Integer.parseInt(finalI)));

                params.put("szPriceId", pricesegment);
                params.put("decPrice", decPrice);
                params.put("decDiscount", "0.0000");
                params.put("decAmount", String.valueOf(counts - discount));

                params.put("szTaxId", "PPN");

                params.put("decTaxRate", tax);

                params.put("decDiscPrinciple", disc_principle + ".0000");
                params.put("decDiscDistributor", disc_distributor + ".0000");
                params.put("decDiscInternal", disc_internal + ".0000");





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
        if (requestQueue22 == null) {
            requestQueue22 = Volley.newRequestQueue(mCanvaser_Terima_Produk.this);
            requestQueue22.add(stringRequest2);
        } else {
            requestQueue22.add(stringRequest2);
        }
    }

    private void mdbatotalharga() {
            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_TotalHarga",
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            getDo();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if(statusLoop.equals("gagal")){
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            statusLoop = "Sukses";
                            new SweetAlertDialog(mCanvaser_Terima_Produk.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Jaringan Bermasalah")
                                    .setConfirmText("OK")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            pDialog.dismissWithAnimation();
                                            sweetAlertDialog.dismissWithAnimation();
                                            statusLoop = "gagal";
                                        }
                                    })
                                    .show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(mCanvaser_Terima_Produk.this, "Auth Failure Error", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ServerError) {
                            getDo();
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(mCanvaser_Terima_Produk.this, "Network Error", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ParseError) {
                            Toast.makeText(mCanvaser_Terima_Produk.this, "Parse Error", Toast.LENGTH_SHORT).show();
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

                    String[] parts = nik_baru.split("-");
                    String branch = parts[0];

                    double base = 100d / 111;
                    double dpp = (total_harga * base);
                    double DoubleValue = dpp;
                    int IntValue = (int) Math.round(DoubleValue);

                    int ppn = Math.toIntExact(total_harga - IntValue);


                    params.put("szDocSo", tv_canvas_no_so.getText().toString());

                    params.put("totalDiscount", String.valueOf(total_diskon));
                    params.put("totalHarga", String.valueOf(total_harga));
                    params.put("depo", branch);

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
            if (requestQueue23 == null) {
                requestQueue23 = Volley.newRequestQueue(mCanvaser_Terima_Produk.this);
                requestQueue23.add(stringRequest2);
            } else {
                requestQueue23.add(stringRequest2);
            }

    }

    private void updateSO() {
        StringRequest stringRequest2 = new StringRequest(Request.Method.PUT, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_updateSO",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        getDo();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        getDo();
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
                String[] customer = act_pilihCustomer.getText().toString().split(",");
                String restnomor = customer[1];

                params.put("szDocId", STD);
                params.put("szCustomerId", idcustomer.getText().toString());
                params.put("szDocSO", tv_canvas_no_so.getText().toString());
                if(mulai_perjalanan.pelanggan.equals("canvaser_luar")) {
                    params.put("bOutOfRoute", "1");
                } else {
                    params.put("bOutOfRoute", "0");
                }

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
        if (requestQueue24 == null) {
            requestQueue24 = Volley.newRequestQueue(mCanvaser_Terima_Produk.this);
            requestQueue24.add(stringRequest2);
        } else {
            requestQueue24.add(stringRequest2);
        }
    }

    private void getDo() {
        String restnomor;
        if(checkalihcustomer.isChecked()){
            String[] parts2 = act_alih_customer.getText().toString().split(",");
            restnomor = parts2[1];
        } else {
            String[] customer = act_pilihCustomer.getText().toString().split(",");
            restnomor = customer[1];
        }

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


                            final JSONObject movieObject = movieArray.getJSONObject(movieArray.length()-1);

                            if(pengaturanBar.getTitle().toString().equals("Pelanggan Dalam Rute")){
                                Toast.makeText(getApplicationContext(), "1", Toast.LENGTH_SHORT).show();
                                DocDo = string_nodo;
                                post_historydriverterimaproduk();
                            } else if (menu_pelanggan.tv_terima_produk.getText().toString().equals("1")) {
                                Toast.makeText(getApplicationContext(), "2", Toast.LENGTH_SHORT).show();
                                DocDo = movieObject.getString("szDocDO");
                                System.out.println("Docdo = " + DocDo);
                                post_historydriverterimaproduk();
                            } else if (condition.equals("NoaddNewDo")) {
                                Toast.makeText(getApplicationContext(), "3", Toast.LENGTH_SHORT).show();
                                DocDo = menu_pelanggan.DocDo;
                                post_historydriverterimaproduk();
                            } else {
                                Toast.makeText(getApplicationContext(), "4", Toast.LENGTH_SHORT).show();
                                DocDo = movieObject.getString("szDocDO");
                                post_historydriverterimaproduk();
                            }



                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if(statusLoop.equals("gagal")){
                            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                statusLoop = "Sukses";
                                new SweetAlertDialog(mCanvaser_Terima_Produk.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Jaringan Bermasalah")
                                        .setConfirmText("OK")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                pDialog.dismissWithAnimation();
                                                sweetAlertDialog.dismissWithAnimation();
                                                statusLoop = "gagal";
                                            }
                                        })
                                        .show();
                            } else if (error instanceof AuthFailureError) {
                                Toast.makeText(mCanvaser_Terima_Produk.this, "Auth Failure Error", Toast.LENGTH_SHORT).show();
                            } else if (error instanceof ServerError) {
                                statusLoop = "Sukses";
                                new SweetAlertDialog(mCanvaser_Terima_Produk.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Jaringan Bermasalah")
                                        .setConfirmText("OK")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                pDialog.dismissWithAnimation();
                                                sweetAlertDialog.dismissWithAnimation();
                                                statusLoop = "gagal";
                                            }
                                        })
                                        .show();
                            } else if (error instanceof NetworkError) {
                                Toast.makeText(mCanvaser_Terima_Produk.this, "Network Error", Toast.LENGTH_SHORT).show();
                            } else if (error instanceof ParseError) {
                                Toast.makeText(mCanvaser_Terima_Produk.this, "Parse Error", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "1", Toast.LENGTH_SHORT).show();
                                DocDo = string_nodo;
                                post_historydriverterimaproduk();
                            }
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

        channel_status.setRetryPolicy(
                new DefaultRetryPolicy(
                        10000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                ));
        if (requestQueue25 == null) {
            requestQueue25 = Volley.newRequestQueue(mCanvaser_Terima_Produk.this);
            requestQueue25.add(channel_status);
        } else {
            requestQueue25.add(channel_status);
        }
    }

//    private void putSO() {
//        StringRequest stringRequest2 = new StringRequest(Request.Method.PUT, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_DoccallItemDoubleRute",
//                new Response.Listener<String>() {
//
//                    @Override
//                    public void onResponse(String response) {
//                        getDOInduk();
//
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        if(statusLoop.equals("gagal")){
//                            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
//                                statusLoop = "Sukses";
//                                new SweetAlertDialog(mCanvaser_Terima_Produk.this, SweetAlertDialog.ERROR_TYPE)
//                                        .setTitleText("Jaringan Bermasalah")
//                                        .setConfirmText("OK")
//                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                                            @Override
//                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
//                                                pDialog.dismissWithAnimation();
//                                                sweetAlertDialog.dismissWithAnimation();
//                                                statusLoop = "gagal";
//                                            }
//                                        })
//                                        .show();
//                            } else if (error instanceof AuthFailureError) {
//                                Toast.makeText(mCanvaser_Terima_Produk.this, "Auth Failure Error", Toast.LENGTH_SHORT).show();
//                            } else if (error instanceof ServerError) {
//                                getDOInduk();
//                            } else if (error instanceof NetworkError) {
//                                Toast.makeText(mCanvaser_Terima_Produk.this, "Network Error", Toast.LENGTH_SHORT).show();
//                            } else if (error instanceof ParseError) {
//                                Toast.makeText(mCanvaser_Terima_Produk.this, "Parse Error", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//
//
//
//                    }
//
//                }) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                HashMap<String, String> params = new HashMap<String, String>();
//                String creds = String.format("%s:%s", "admin", "Databa53");
//                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
//                params.put("Authorization", auth);
//                return params;
//            }
//
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                String currentDateandTime2 = sdf2.format(new Date());
//
//                params.put("szDocDo", DocDo);
//                params.put("szDocSO", tv_canvas_no_so.getText().toString());
//
//
//                return params;
//            }
//
//        };
//        stringRequest2.setRetryPolicy(
//                new DefaultRetryPolicy(
//                        10000,
//                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
//                )
//        );
//        RequestQueue requestQueue2 = Volley.newRequestQueue(mCanvaser_Terima_Produk.this);
//        requestQueue2.add(stringRequest2);
//    }

    private void getDOInduk() {
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String nik_baru = sharedPreferences.getString("szDocCall", null);
        String[] parts = nik_baru.split("-");
        String restnomor = parts[0];

        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateandTime2 = sdf2.format(new Date());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_NoDoInduk?tgl="+currentDateandTime2+"&depo=" + restnomor,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");
                            for (int i = 0; i < movieArray.length(); i++) {
                                final JSONObject movieObject = movieArray.getJSONObject(i);

                                getlastSO(movieObject.getString("inido_induk"));

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(statusLoop.equals("gagal")){
                            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                statusLoop = "Sukses";
                                System.out.println("Error 2");
                                new SweetAlertDialog(mCanvaser_Terima_Produk.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Jaringan Bermasalah")
                                        .setConfirmText("OK")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                pDialog.dismissWithAnimation();
                                                sweetAlertDialog.dismissWithAnimation();
                                                statusLoop = "gagal";
                                            }
                                        })
                                        .show();
                            } else if (error instanceof AuthFailureError) {
                                Toast.makeText(mCanvaser_Terima_Produk.this, "Auth Failure Error", Toast.LENGTH_SHORT).show();
                            } else if (error instanceof ServerError) {
                                statusLoop = "Sukses";
                                new SweetAlertDialog(mCanvaser_Terima_Produk.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Jaringan Bermasalah")
                                        .setConfirmText("OK")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                pDialog.dismissWithAnimation();
                                                sweetAlertDialog.dismissWithAnimation();
                                                statusLoop = "gagal";
                                            }
                                        })
                                        .show();
                            } else if (error instanceof NetworkError) {
                                Toast.makeText(mCanvaser_Terima_Produk.this, "Network Error", Toast.LENGTH_SHORT).show();
                            } else if (error instanceof ParseError) {
                                Toast.makeText(mCanvaser_Terima_Produk.this, "Parse Error", Toast.LENGTH_SHORT).show();
                            }
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
                        10000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                ));
        if (requestQueue26 == null) {
            requestQueue26 = Volley.newRequestQueue(mCanvaser_Terima_Produk.this);
            requestQueue26.add(stringRequest);
        } else {
            requestQueue26.add(stringRequest);
        }
    }

    private void getlastSO(String inido_induk) {
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String nik_baru = sharedPreferences.getString("szDocCall", null);
        getSegment2(inido_induk);
        putQty();
    }

//    private void postDocDoItem(String szDocDo) {
//        for(int i = 0;i <=  data_terima_produk_pojos.size() - 1 ;i++) {
//            int finalI = i;
//            int finalI1 = i;
//            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_DocDoItem",
//                    new Response.Listener<String>() {
//
//                        @Override
//                        public void onResponse(String response) {
//                            if (finalI == data_terima_produk_pojos.size() - 1) {
//
//                            }
//                        }
//                    }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    {
//                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
//                            statusLoop = "Sukses";
//                            new SweetAlertDialog(mCanvaser_Terima_Produk.this, SweetAlertDialog.ERROR_TYPE)
//                                    .setTitleText("Jaringan Bermasalah")
//                                    .setConfirmText("OK")
//                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                                        @Override
//                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
//                                            pDialog.dismissWithAnimation();
//                                            sweetAlertDialog.dismissWithAnimation();
//                                            statusLoop = "gagal";
//                                        }
//                                    })
//                                    .show();
//                        } else if (error instanceof AuthFailureError) {
//                            Toast.makeText(mCanvaser_Terima_Produk.this, "Auth Failure Error", Toast.LENGTH_SHORT).show();
//                        } else if (error instanceof ServerError) {
//                            if (finalI == data_terima_produk_pojos.size() - 1) {
//                                getSegment2();
//                            }
//                        } else if (error instanceof NetworkError) {
//                            Toast.makeText(mCanvaser_Terima_Produk.this, "Network Error", Toast.LENGTH_SHORT).show();
//                        } else if (error instanceof ParseError) {
//                            Toast.makeText(mCanvaser_Terima_Produk.this, "Parse Error", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//
//                }
//
//            }) {
//                @Override
//                public Map<String, String> getHeaders() throws AuthFailureError {
//                    HashMap<String, String> params = new HashMap<String, String>();
//                    String creds = String.format("%s:%s", "admin", "Databa53");
//                    String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
//                    params.put("Authorization", auth);
//                    return params;
//                }
//
//                @Override
//                protected Map<String, String> getParams() throws AuthFailureError {
//                    Map<String, String> params = new HashMap<String, String>();
//                    sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
//
//
//                    params.put("szDocId", DocDo);
//                    params.put("intItemNumber", String.valueOf(finalI));
//                    params.put("szProductId", adapter.getItem(finalI1).getSzProductId());
//
//                    params.put("decQty", adapter.getItem(finalI1).getQtyEdit());
//                    params.put("szUomId", adapter.getItem(finalI1).getSzUomId());
//
//
//                    return params;
//                }
//
//            };
//            stringRequest2.setRetryPolicy(
//                    new DefaultRetryPolicy(
//                            10000,
//                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
//                    )
//            );
//            if (requestQueue28 == null) {
//                requestQueue28 = Volley.newRequestQueue(mCanvaser_Terima_Produk.this);
//                requestQueue28.add(stringRequest2);
//            } else {
//                requestQueue28.add(stringRequest2);
//            }
//        }
//    }

    private void getSegment2(String inido_induk) {
        for(int i = 0; i < data_terima_produk_pojos.size(); i++){
            int finalI = i;
            String restnomor;
            if(checkalihcustomer.isChecked()){
                String[] customer = act_alih_customer.getText().toString().split(",");
                restnomor = customer[1];
            } else {
                String[] customer = act_pilihCustomer.getText().toString().split(",");
                restnomor = customer[1];
            }


            sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
            String nik_baru = sharedPreferences.getString("szDocCall", null);
            String[] parts = nik_baru.split("-");
            String restnomor1 = parts[0];

            StringRequest rest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_SzPride?szProductId="+adapter.getItem(i).getSzProductId()+"&employeeId=" + restnomor + "&kode=" + restnomor1,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getString("status").equals("true")) {
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                        int discount = Integer.parseInt(adapter.getItem(finalI).getDiskon_tiv()) + Integer.parseInt(adapter.getItem(finalI).getDiskon_distributor()) + Integer.parseInt(adapter.getItem(finalI).getDiskon_internal());

                                        String[] parts = adapter.getItem(finalI).getDecPrice().split("\\.");
                                        String restnomor = parts[0];

                                        int counts = Integer.parseInt(adapter.getItem(finalI).getQtyEdit()) * Integer.parseInt(restnomor);
                                        postDocDoItemPrice(jsonObject1.getString("pricesegment"), tv_canvas_no_so.getText().toString(), String.valueOf(finalI), adapter.getItem(finalI).getDecPrice(), discount, counts, adapter.getItem(finalI).getDiskon_tiv(), adapter.getItem(finalI).getDiskon_distributor(), adapter.getItem(finalI).getDiskon_internal(), String.valueOf(finalI), inido_induk);

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
                            int discount = Integer.parseInt(adapter.getItem(finalI).getDiskon_tiv()) + Integer.parseInt(adapter.getItem(finalI).getDiskon_distributor()) + Integer.parseInt(adapter.getItem(finalI).getDiskon_internal());

                            String[] parts = adapter.getItem(finalI).getDecPrice().split("\\.");
                            String restnomor = parts[0];
                            String[] customer = act_pilihCustomer.getText().toString().split(",");
                            String restnomor2 = customer[1];
                            String[] parts3 = restnomor2.split("-");
                            String restnomor3 = parts3[0];

                            int counts = Integer.parseInt(adapter.getItem(finalI).getQtyEdit()) * Integer.parseInt(restnomor);
                            postDocDoItemPrice(restnomor3+"-C_00", tv_canvas_no_so.getText().toString(), String.valueOf(finalI), adapter.getItem(finalI).getDecPrice(), discount, counts, adapter.getItem(finalI).getDiskon_tiv(), adapter.getItem(finalI).getDiskon_distributor(), adapter.getItem(finalI).getDiskon_internal(), String.valueOf(finalI), inido_induk);

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
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            if (requestQueue29 == null) {
                requestQueue29 = Volley.newRequestQueue(mCanvaser_Terima_Produk.this);
                requestQueue29.add(rest);
            } else {
                requestQueue29.add(rest);
            }
        }
    }

    private void postDocDoItemPrice(String pricesegment, String no_surats, String s, String decPrice, int discount, int counts, String disc_principle, String disc_distributor, String disc_internal, String finalI, String inido_induk) {
            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_DocDo_all",
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            if(Integer.parseInt(finalI) == data_terima_produk_pojos.size() - 1){
                                updateDO();
                                updateSOItem();
                                updateDocDo();


                                updateDocDoDms();

                                if(!mulai_perjalanan.pelanggan.equals("canvaser")){
                                    updateDocDo();
                                }

                                putFinish();

                                if(chk_transfer.isChecked()){
                                    postImage();
                                }

                                Status = "DONE";

                                condition = "addNewDo";


                                pDialog.dismissWithAnimation();

                                updateSFA();


                                Intent intent = new Intent(mCanvaser_Terima_Produk.this, mCanvaser_Detail_Terima_Produk.class);

                                if(checkalihcustomer.isChecked()){
                                    intent.putExtra("idCustomer", act_alih_customer.getText().toString());
                                } else {
                                    intent.putExtra("idCustomer", act_pilihCustomer.getText().toString());
                                }

                                intent.putExtra("totalharga", tv_total_harga_format_rupiah.getText().toString());
                                intent.putExtra("catatan", tv_canvaser_catatan.getText().toString());
                                intent.putExtra("totalhargarow", tv_total_harga_row_format_rupiah.getText().toString());
                                intent.putExtra("totalorderqty", qty_order_sudah_edit.getText().toString());
                                startActivity(intent);
                                finish();
                                System.gc();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    {
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            statusLoop = "Sukses";
                            System.out.println("Error 11");
                            new SweetAlertDialog(mCanvaser_Terima_Produk.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Jaringan Bermasalah")
                                    .setConfirmText("OK")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            pDialog.dismissWithAnimation();
                                            sweetAlertDialog.dismissWithAnimation();
                                            statusLoop = "gagal";
                                        }
                                    })
                                    .show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(mCanvaser_Terima_Produk.this, "Auth Failure Error", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ServerError) {
                            if(Integer.parseInt(finalI) == data_terima_produk_pojos.size() - 1){
                                updateDO();
                                updateSOItem();
                                updateDocDo();


                                updateDocDoDms();

                                if(!mulai_perjalanan.pelanggan.equals("canvaser")){
                                    updateDocDo();
                                }

                                putFinish();

                                if(chk_transfer.isChecked()){
                                    postImage();
                                }

                                Status = "DONE";

                                condition = "addNewDo";


                                pDialog.dismissWithAnimation();

                                updateSFA();


                                Intent intent = new Intent(mCanvaser_Terima_Produk.this, mCanvaser_Detail_Terima_Produk.class);

                                if(checkalihcustomer.isChecked()){
                                    intent.putExtra("idCustomer", act_alih_customer.getText().toString());
                                } else {
                                    intent.putExtra("idCustomer", act_pilihCustomer.getText().toString());
                                }

                                intent.putExtra("totalharga", tv_total_harga_format_rupiah.getText().toString());
                                intent.putExtra("catatan", tv_canvaser_catatan.getText().toString());
                                intent.putExtra("totalhargarow", tv_total_harga_row_format_rupiah.getText().toString());
                                intent.putExtra("totalorderqty", qty_order_sudah_edit.getText().toString());
                                startActivity(intent);
                                finish();
                                System.gc();
                            }
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(mCanvaser_Terima_Produk.this, "Network Error", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ParseError) {
                            Toast.makeText(mCanvaser_Terima_Produk.this, "Parse Error", Toast.LENGTH_SHORT).show();
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

                    SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String currentDateandTime3 = sdf3.format(new Date());

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String currentDateandTime = sdf.format(new Date());

                    String[] parts = nik_baru.split("-");
                    String branch = parts[0];

                    String restnomor;
                    if(checkalihcustomer.isChecked()){
                        String[] customer = act_alih_customer.getText().toString().split(",");
                        restnomor = customer[1];
                    } else {
                        restnomor = idcustomer.getText().toString();
                    }

                    // postTotalHarga
                    params.put("nodok_DO", DocDo);
                    params.put("nodok_SO", "");
                    params.put("totaldiskon", String.valueOf(total_diskon));
                    params.put("totalHarga", String.valueOf(total_harga));
                    params.put("depo", branch);

                    // postDOInduk //

                    params.put("nodo_induk", inido_induk);
                    params.put("nodo_turunan", DocDo);

                    params.put("noso_induk", "");
                    params.put("noso_turunan", "");

                    params.put("tgl_insert", currentDateandTime3);

                    params.put("id_customer", restnomor);
                    params.put("id_pengemudi", driver);

                    params.put("id_kendaraan", szVehicleId);
                    params.put("id_helper1", szHelper1);

                    params.put("id_user_insert", nik_baru);
                    params.put("kode_depo", branch);

                    params.put("tgl_cari", currentDateandTime);
                    params.put("status", "OPEN");
                    if (chk_credit.isChecked()) {
                        params.put("type_bayar", "1");
                    } else {
                        params.put("type_bayar", "2");
                    }
                    params.put("req_date", currentDateandTime3);

                    // postDocDOItem //
                    params.put("szProductId", adapter.getItem(Integer.parseInt(finalI)).getSzProductId());
                    params.put("decQty", adapter.getItem(Integer.parseInt(finalI)).getQtyEdit());
                    params.put("szUomId", adapter.getItem(Integer.parseInt(finalI)).getSzUomId());


                    // postDocDOItemPrice //
                    params.put("szProductId", adapter.getItem(Integer.parseInt(finalI)).getSzProductId());
                    params.put("szDocId", DocDo);
                    params.put("intItemNumber", String.valueOf(finalI));

                    params.put("szPriceId", pricesegment);
                    params.put("decPrice", decPrice);
                    params.put("decDiscount", "0.0000");
                    params.put("decAmount", String.valueOf(counts - discount));

                    params.put("szTaxId", "PPN");

                    params.put("decTaxRate", tax);

                    params.put("decDiscPrinciple", disc_principle + ".0000");
                    params.put("decDiscDistributor", disc_distributor + ".0000");
                    params.put("decDiscInternal", disc_internal + ".0000");





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
        if (requestQueue30 == null) {
            requestQueue30 = Volley.newRequestQueue(mCanvaser_Terima_Produk.this);
            requestQueue30.add(stringRequest2);
        } else {
            requestQueue30.add(stringRequest2);
        }
    }

//    private void mdbatotalhargaxdo() {
//
//            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_TotalHargaDo_v2",
//                    new Response.Listener<String>() {
//
//                        @Override
//                        public void onResponse(String response) {
//
//                        }
//                    }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//
//                    {
//                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
//                            statusLoop = "Sukses";
//                            new SweetAlertDialog(mCanvaser_Terima_Produk.this, SweetAlertDialog.ERROR_TYPE)
//                                    .setTitleText("Jaringan Bermasalah")
//                                    .setConfirmText("OK")
//                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                                        @Override
//                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
//                                            pDialog.dismissWithAnimation();
//                                            sweetAlertDialog.dismissWithAnimation();
//                                            statusLoop = "gagal";
//                                        }
//                                    })
//                                    .show();
//                        } else if (error instanceof AuthFailureError) {
//                            Toast.makeText(mCanvaser_Terima_Produk.this, "Auth Failure Error", Toast.LENGTH_SHORT).show();
//                        } else if (error instanceof ServerError) {
//                            updateDO();
//                            updateDocDo();
//
//                            putFinish();
//                            updateSFA();
//                            condition = "addNewDo";
//                            pDialog.dismissWithAnimation();
//                            Intent intent = new Intent(mCanvaser_Terima_Produk.this, mCanvaser_Detail_Terima_Produk.class);
//
//
//                            if(checkalihcustomer.isChecked()){
//                                intent.putExtra("idCustomer", act_alih_customer.getText().toString());
//                            } else {
//                                intent.putExtra("idCustomer", act_pilihCustomer.getText().toString());
//                            }
//
//                            intent.putExtra("totalharga", tv_total_harga_format_rupiah.getText().toString());
//                            intent.putExtra("catatan", tv_canvaser_catatan.getText().toString());
//                            intent.putExtra("totalhargarow", tv_total_harga_row_format_rupiah.getText().toString());
//                            intent.putExtra("totalorderqty", qty_order_sudah_edit.getText().toString());
//                            startActivity(intent);
//                            finish();
//                            System.gc();
//
//                        } else if (error instanceof NetworkError) {
//                            Toast.makeText(mCanvaser_Terima_Produk.this, "Network Error", Toast.LENGTH_SHORT).show();
//                        } else if (error instanceof ParseError) {
//                            Toast.makeText(mCanvaser_Terima_Produk.this, "Parse Error", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                }
//
//            }) {
//                @Override
//                public Map<String, String> getHeaders() throws AuthFailureError {
//                    HashMap<String, String> params = new HashMap<String, String>();
//                    String creds = String.format("%s:%s", "admin", "Databa53");
//                    String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
//                    params.put("Authorization", auth);
//                    return params;
//                }
//
//                @Override
//                protected Map<String, String> getParams() throws AuthFailureError {
//                    Map<String, String> params = new HashMap<String, String>();
//                    sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
//                    String nik_baru = sharedPreferences.getString("szDocCall", null);
//
//                    String[] parts = nik_baru.split("-");
//                    String branch = parts[0];
//
//                    params.put("nodok_DO", DocDo);
//                    params.put("nodok_SO", "");
//                    params.put("totaldiskon", String.valueOf(total_diskon));
//                    params.put("totalHarga", String.valueOf(total_harga));
//                    params.put("depo", branch);
//
//                    return params;
//                }
//
//            };
//            stringRequest2.setRetryPolicy(
//                    new DefaultRetryPolicy(
//                            10000,
//                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
//                    )
//            );
//        if (requestQueue31 == null) {
//            requestQueue31 = Volley.newRequestQueue(mCanvaser_Terima_Produk.this);
//            requestQueue31.add(stringRequest2);
//        } else {
//            requestQueue31.add(stringRequest2);
//        }
//    }

    private void updateSFA() {
        StringRequest stringRequest2 = new StringRequest(Request.Method.PUT, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_DoccallItemSFA2",
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

                params.put("dtmFinish", currentDateandTime2);

                if(pelanggan.equals("canvaser_luar")){
                    params.put("bOutOfRoute", "1");
                } else {
                    params.put("bOutOfRoute", "0");
                }

                params.put("bVisited", "1");
                params.put("bSuccess", "1");
                params.put("bPostPone", "0");
                params.put("szFailReason", "");
                params.put("decDuration", "0");
                params.put("szRefDocId", DocDo);


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
        RequestQueue requestQueue2 = Volley.newRequestQueue(mCanvaser_Terima_Produk.this);
        requestQueue2.add(stringRequest2);
    }

    private void putFinish() {
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
                params.put("bVisited", "1");
                params.put("szFailReason", "");

                params.put("dtmFinish", currentDateandTime2);
                params.put("szDocDO", DocDo);

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
        RequestQueue requestQueue2 = Volley.newRequestQueue(mCanvaser_Terima_Produk.this);
        requestQueue2.add(stringRequest2);
    }



    private void updateDocDoDms() {
        StringRequest stringRequest2 = new StringRequest(Request.Method.PUT, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_MobileDms",
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
                String[] customer = act_pilihCustomer.getText().toString().split(",");
                String restnomor = customer[1];

                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String currentDateandTime2 = sdf2.format(new Date());

                sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                String nik_baru = sharedPreferences.getString("szDocCall", null);

                params.put("szDocId", DocDo);
                params.put("szDocSoId", tv_canvas_no_so.getText().toString());
                params.put("dtmLastUpdated", currentDateandTime2);

                params.put("szUserUpdatedId", nik_baru);



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
        if (requestQueue33 == null) {
            requestQueue33 = Volley.newRequestQueue(mCanvaser_Terima_Produk.this);
            requestQueue33.add(stringRequest2);
        } else {
            requestQueue33.add(stringRequest2);
        }
    }

    private void updateDocDo() {
        StringRequest stringRequest2 = new StringRequest(Request.Method.PUT, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_DocBKBMobile",
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
                String[] customer = act_pilihCustomer.getText().toString().split(",");
                String restnomor = customer[1];

                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String currentDateandTime2 = sdf2.format(new Date());

                sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                String nik_baru = sharedPreferences.getString("szDocCall", null);

                params.put("szDocId", DocDo);
                params.put("szBkbId", tv_canvas_no_bkb.getText().toString());
                params.put("szDocSoId", tv_canvas_no_so.getText().toString());
                params.put("dtmLastUpdated", currentDateandTime2);
                params.put("szUserUpdatedId", nik_baru);

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
        if (requestQueue34 == null) {
            requestQueue34 = Volley.newRequestQueue(mCanvaser_Terima_Produk.this);
            requestQueue34.add(stringRequest2);
        } else {
            requestQueue34.add(stringRequest2);
        }
    }

    private void updateSOItem() {
        StringRequest stringRequest2 = new StringRequest(Request.Method.PUT, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_SoItem",
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
                String[] customer = act_pilihCustomer.getText().toString().split(",");
                String restnomor = customer[1];

                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String currentDateandTime2 = sdf2.format(new Date());

                params.put("szDocId", tv_canvas_no_so.getText().toString());



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
        if (requestQueue35 == null) {
            requestQueue35 = Volley.newRequestQueue(mCanvaser_Terima_Produk.this);
            requestQueue35.add(stringRequest2);
        } else {
            requestQueue35.add(stringRequest2);
        }
    }

    private void updateDO() {

    }

    private void getBKB(String szDocId) {
            sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
            String szEmployeeId = sharedPreferences.getString("szDocCall", null);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_BKBALL?id_std=" + szDocId, //szDocId,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            pDialog.dismissWithAnimation();

                            try {
                                int number = 0;
                                JSONObject obj = new JSONObject(response);
                                if (obj.getString("status").equals("true")) {
                                    final JSONArray movieArray = obj.getJSONArray("data");
                                    final JSONObject movieObject = movieArray.getJSONObject(movieArray.length() -1);


                                    tv_canvas_no_bkb.setText(movieObject.getString("id_bkb"));

                                    driver = movieObject.getString("id_driver");
                                    szHelper1 = movieObject.getString("szHelper1");
                                    szVehicleId = movieObject.getString("szVehicleId");

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
                            10000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                    ));
        if (requestQueue37 == null) {
            requestQueue37 = Volley.newRequestQueue(mCanvaser_Terima_Produk.this);
            requestQueue37.add(stringRequest);
        } else {
            requestQueue37.add(stringRequest);
        }

    }

    private void getSO(String iniso_induk) {
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String nik_baru = sharedPreferences.getString("szDocCall", null);
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_DocSoData",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            String message = jsonObj.getString("message");
                            tv_canvas_no_so.setText(message);
                            postSoDocItem(tv_canvas_no_so.getText().toString());


                        } catch (JSONException e) {
                            e.printStackTrace();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(statusLoop.equals("gagal")){
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        statusLoop = "Sukses";
                        new SweetAlertDialog(mCanvaser_Terima_Produk.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Jaringan Bermasalah")
                                .setConfirmText("OK")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        pDialog.dismissWithAnimation();
                                        sweetAlertDialog.dismissWithAnimation();
                                        statusLoop = "gagal";
                                    }
                                })
                                .show();
                    } else if (error instanceof AuthFailureError) {
                        Toast.makeText(mCanvaser_Terima_Produk.this, "Auth Failure Error", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof ServerError) {
                        postSoInduk(iniso_induk);
                    } else if (error instanceof NetworkError) {
                        Toast.makeText(mCanvaser_Terima_Produk.this, "Network Error", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof ParseError) {
                        Toast.makeText(mCanvaser_Terima_Produk.this, "Parse Error", Toast.LENGTH_SHORT).show();
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

                String[] parts = nik_baru.split("-");
                String branch = parts[0];


                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String currentDateandTime2 = sdf2.format(new Date());

                SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd");
                String currentDateandTime3 = sdf3.format(new Date());



                params.put("dtmDoc", currentDateandTime3);

                if(checkalihcustomer.isChecked()){
                    String[] parts2 = act_alih_customer.getText().toString().split(",");
                    String customerafter = parts2[1];
                    params.put("szCustomerId", customerafter);
                } else {
                    params.put("szCustomerId", idcustomer.getText().toString());
                }

                params.put("szEmployeeId", nik_baru);

                if(PaymentTerm.equals("0HARI")){
                    params.put("bCash", "1");
                } else {
                    params.put("bCash", "0");
                }
                params.put("szPaymentTermId", PaymentTerm);

                params.put("dtmPO", currentDateandTime2);
                params.put("dtmPOExpired", currentDateandTime2);
                params.put("szBranchId", branch);
                params.put("szDocStatus", "Applied");

                params.put("szUserCreatedId", nik_baru);
                params.put("dtmCreated", currentDateandTime2);
                params.put("dtmReqDlvDate", currentDateandTime3);
                System.out.println("Params DocSo = " + params);

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
        if (requestQueue41 == null) {
            requestQueue41 = Volley.newRequestQueue(mCanvaser_Terima_Produk.this);
            requestQueue41.add(stringRequest2);
        } else {
            requestQueue41.add(stringRequest2);
        }

//        String[] parts = nik_baru.split("-");
//        String restnomor = parts[0];
//        String restnomorbaru = restnomor.replace(" ", "");
//        StringRequest channel_status = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_Counter?szId=" +  "SO" + restnomorbaru + "COU",
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            JSONObject obj = new JSONObject(response);
//                            final JSONArray movieArray = obj.getJSONArray("data");
//
//                            JSONObject biodatas = null;
//                            for (int i = 0; i < movieArray.length(); i++) {
//
//                                biodatas = movieArray.getJSONObject(i);
//
//                                int lastcounter = biodatas.getInt("intLastCounter") + 1;
//                                if (lastcounter <= 999999) {
//                                    tv_canvas_no_so.setText(restnomorbaru + "-0" +String.valueOf(lastcounter));
//                                    postDocSo2(restnomorbaru + "-0" +String.valueOf(lastcounter));
//                                } else if (lastcounter >= 1000000){
//                                    postDocSo2(restnomorbaru + "-" +String.valueOf(lastcounter));
//                                    tv_canvas_no_so.setText(restnomorbaru + "-" +String.valueOf(lastcounter));
//                                }
//
//                                postSoInduk(iniso_induk);
//                                putCounter(String.valueOf(lastcounter));
//
//
//                            }
//
//
//                        } catch(JSONException e){
//                            e.printStackTrace();
//
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//
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
//        channel_status.setRetryPolicy(
//                new DefaultRetryPolicy(
//                        10000,
//                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
//                ));
//        if (requestQueue40 == null) {
//            requestQueue40 = Volley.newRequestQueue(mCanvaser_Terima_Produk.this);
//            requestQueue40.add(channel_status);
//        } else {
//            requestQueue40.add(channel_status);
//        }

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 2){

            if (resultCode == Activity.RESULT_OK) {

                try {
                    bitmap2 = MediaStore.Images.Media.getBitmap(
                            getContentResolver(), imageUri);

                    int width=720;
                    int height=720;

                    int screenWidth = getResources().getDisplayMetrics().widthPixels;
                    int screenHeight = getResources().getDisplayMetrics().heightPixels;

                    bitmap2 = Bitmap.createScaledBitmap(bitmap2, width, height, true);
                    img_bukti_transfer.setImageBitmap(bitmap2);

                    ViewGroup.LayoutParams paramktp = img_bukti_transfer.getLayoutParams();

                    double sizeInDP = 226;
                    int marginInDp = (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP, (float) sizeInDP, getResources()
                                    .getDisplayMetrics());

                    double sizeInDP2 = 226;
                    int marginInDp2 = (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP, (float) sizeInDP2, getResources()
                                    .getDisplayMetrics());

                    paramktp.width = marginInDp;
                    paramktp.height = marginInDp2;
                    img_bukti_transfer.setLayoutParams(paramktp);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }



    public class ListViewAdapteProductPenjualan extends ArrayAdapter<data_product_bkb_pojo> {

        private class ViewHolder {
            TextView namaproduk, qty_order, total_harga, harga_satuan, diskon_tiv, diskon_distributor, diskon_internal;
            TextView intItemNumber, szOrderItemTypeId, bTaxable, szTaxId, decTaxRate, decTax, decDpp, tv_qty_produk;
            TextView total_harga_row_format_rupiah, harga_satuan_format_rupiah, diskon_tiv_format_rupiah, diskon_distributor_format_rupiah,
                    diskon_internal_format_rupiah;

            LinearLayout linear_detail_terima_produk;
            RelativeLayout buttonExpand;
            ImageButton bt_plus, bt_minus, bt_edit_qty_produk;
            String string_qty_produk, string_qty_order_produk_asli, string_harga_satuan,
                    string_total_harga_row, string_diskon_internal, string_diskon_tiv,
                    string_diskon_distributor;

            TextView qty_order_asli;
            TextView decTax_setelah_edit, decDpp_setelah_edit;
            TextView tax;
            ImageButton bt_hapus;
        }

        List<data_product_bkb_pojo> data_terima_produk_pojos;
        private Context context;

        public ListViewAdapteProductPenjualan(List<data_product_bkb_pojo> data_terima_produk_pojos, Context context) {
            super(context, R.layout.list_terima_produk, data_terima_produk_pojos);
            this.data_terima_produk_pojos = data_terima_produk_pojos;
            this.context = context;
        }

        public int getCount() {
            return data_terima_produk_pojos.size();
        }

        public data_product_bkb_pojo getItem(int position) {
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
            return position;
        }

        public void remove(int position){
            if(data_terima_produk_pojos.get(position).getQtyEdit().equals("0")){
                data_terima_produk_pojos.remove(position);
                Utility.setListViewHeightBasedOnChildren(listproduk);
                notifyDataSetChanged();
            }
        }

        @SuppressLint("NewApi")
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder viewHolder;
            data_product_bkb_pojo data = getItem(position);

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

                viewHolder.qty_order_asli = (TextView) convertView.findViewById(R.id.qty_order_asli); //qty tetap/tidak berubah, asli dari mdba
                viewHolder.linear_detail_terima_produk = (LinearLayout) convertView.findViewById(R.id.linear_detail_terima_produk);
                viewHolder.bt_edit_qty_produk = (ImageButton) convertView.findViewById(R.id.bt_edit_qty_produk);

                viewHolder.decTax_setelah_edit = (TextView) convertView.findViewById(R.id.tv_decTax_body_data_after_edit_qty); //decTax, jika dilakukan perubahan qty per row, maka akan berubah jugaa nilainya
                viewHolder.decDpp_setelah_edit = (TextView) convertView.findViewById(R.id.tv_decDpp_body_data_after_edit_qty); //decDpp, jika dilakukan perubahan qty per row, maka akan berubah jugaa nilainya

                //viewHolder.refresh = (ImageButton) convertView.findViewById(R.id.refresh);
                viewHolder.buttonExpand = (RelativeLayout) convertView.findViewById(R.id.button_expand_listview);

                viewHolder.tax = (TextView) convertView.findViewById(R.id.tv_tax_body_data);
                viewHolder.bt_hapus = (ImageButton) convertView.findViewById(R.id.bt_hapus);

                convertView.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.bt_hapus.setVisibility(GONE);

            viewHolder.bt_hapus.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Integer index = (Integer) v.getTag();
                    data_terima_produk_pojos.remove(position);
                    Utility.setListViewHeightBasedOnChildren(listproduk);
                    notifyDataSetChanged();


                }
            });

            DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
            DecimalFormatSymbols formatRp = new DecimalFormatSymbols();
            formatRp.setCurrencySymbol("Rp. ");
            formatRp.setMonetaryDecimalSeparator(',');
            formatRp.setGroupingSeparator('.');
            kursIndonesia.setDecimalFormatSymbols(formatRp);

            viewHolder.namaproduk.setText(data.getSzName());
            viewHolder.qty_order_asli.setText(data.getDecQty());

            if(data.getDiskon_tiv() == null){
                data.setDiskon_tiv("0");
                viewHolder.diskon_tiv_format_rupiah.setText(kursIndonesia.format(Integer.parseInt(data.getDiskon_tiv())));
            } else {
                viewHolder.diskon_tiv_format_rupiah.setText(kursIndonesia.format(Integer.parseInt(data.getDiskon_tiv())));
            }

            if(data.getDiskon_distributor() == null){
                data.setDiskon_distributor("0");
                viewHolder.diskon_distributor_format_rupiah.setText(kursIndonesia.format(Integer.parseInt(data.getDiskon_distributor())));
            } else {
                viewHolder.diskon_distributor_format_rupiah.setText(kursIndonesia.format(Integer.parseInt(data.getDiskon_distributor())));
            }

            if(data.getDiskon_internal() == null){
                data.setDiskon_internal("0");
                viewHolder.diskon_internal_format_rupiah.setText(kursIndonesia.format(Integer.parseInt(data.getDiskon_internal())));
            } else {
                viewHolder.diskon_internal_format_rupiah.setText(kursIndonesia.format(Integer.parseInt(data.getDiskon_internal())));
            }

            if(data.getQtyEdit() == null){
                data.setQtyEdit("0");
                viewHolder.qty_order.setText(data.getQtyEdit());
            } else {
                viewHolder.qty_order.setText(data.getQtyEdit());
            }


            if(data.getDecPrice().equals("null")){
                viewHolder.harga_satuan_format_rupiah.setText(kursIndonesia.format(0));
            } else {
                String szId = data.getDecPrice();
                String[] parts = szId.split("\\.");
                String szIdSlice = parts[0];
                viewHolder.harga_satuan_format_rupiah.setText(kursIndonesia.format(Integer.parseInt(szIdSlice)));
            }

            if(viewHolder.qty_order.getText().toString().length() == 0){
                data.setTotalHarga("0");
            } else {
                String szId = data.getDecPrice();
                String[] parts = szId.split("\\.");
                String szIdSlice = parts[0];

                if(data.getDecPrice().equals("null")){
                    data.setTotalHarga("0");
                } else {
                    int qtyorder = Integer.parseInt(viewHolder.qty_order.getText().toString());
                    int total = Integer.parseInt(szIdSlice);
                    data.setTotalHarga(String.valueOf(qtyorder * total));
                }

            }



            viewHolder.total_harga_row_format_rupiah.setText(kursIndonesia.format(Integer.parseInt(data.getTotalHarga())));



            viewHolder.linear_detail_terima_produk.setVisibility(View.VISIBLE);
            viewHolder.buttonExpand.setVisibility(GONE);

            if(Status == null){

            } else {
                viewHolder.bt_edit_qty_produk.setVisibility(GONE);
                viewHolder.bt_hapus.setVisibility(GONE);
            }

            viewHolder.bt_edit_qty_produk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String szId = data.getDecPrice();
                    String[] parts = szId.split("\\.");
                    String szIdSlice = parts[0];

                    Intent intent = new Intent(getApplicationContext(), mCanvaser_diskon.class);
                    intent.putExtra("position", String.valueOf(position));
                    intent.putExtra("qty_asli", data.getDecQty());
                    intent.putExtra("qty", viewHolder.qty_order.getText().toString());
                    intent.putExtra("qtyedit", data.getQtyEdit());
                    intent.putExtra("hargasatuan", szIdSlice);
                    intent.putExtra("nama_produk", data.getSzName());
                    intent.putExtra("Status", getIntent().getStringExtra("Status"));




                    startActivity(intent);

                }
            });



            return convertView;
        }



    }

    private String ImageToString_buktiTransfer(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    private String formatRupiah(Double number) {
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        return formatRupiah.format(number);
    }

    private void post_historydriverterimaproduk() {

        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_mdba_history_driver_terimaproduk",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        if (mulai_perjalanan.pelanggan.equals("canvaser")) {
                            putProductQty();
                        } else if (mulai_perjalanan.pelanggan.equals("non_rute")) {
                            putProductQty();
                        } else {
                            getLastNumber();
                        }
                        post_historydriverterimaprodukitem_canvas();




                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(statusLoop.equals("gagal")){
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        statusLoop = "Sukses";
                        new SweetAlertDialog(mCanvaser_Terima_Produk.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Jaringan Bermasalah")
                                .setConfirmText("OK")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        pDialog.dismissWithAnimation();
                                        sweetAlertDialog.dismissWithAnimation();
                                        statusLoop = "gagal";
                                    }
                                })
                                .show();
                    } else if (error instanceof AuthFailureError) {
                        Toast.makeText(mCanvaser_Terima_Produk.this, "Auth Failure Error", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof ServerError) {
                        if (mulai_perjalanan.pelanggan.equals("canvaser")) {
                            putProductQty();
                        } else if (mulai_perjalanan.pelanggan.equals("non_rute")) {
                            putProductQty();
                        } else {
                            getLastNumber();
                        }
                        post_historydriverterimaprodukitem_canvas();
                    } else if (error instanceof NetworkError) {
                        Toast.makeText(mCanvaser_Terima_Produk.this, "Network Error", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof ParseError) {
                        Toast.makeText(mCanvaser_Terima_Produk.this, "Parse Error", Toast.LENGTH_SHORT).show();
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
                String[] parts = nik_baru.split("-");
                String restnomor = parts[0];

                params.put("dtmDoc", currentDateandTime); //tanggal kapan input, tanggal sekarang                               [ok]
                params.put("nomor_do", DocDo); //nomor DO (DELIVERY ORDER)                                                   [ok]
                params.put("nomor_bkb", tv_canvas_no_bkb.getText().toString()); //nomor BKB                                                     [ok]
                params.put("nomor_std", STD); //nomr STD (SURAT TUGAS DISTIRBUSI)                               [ok]

                params.put("ritase", string_header_ritase); //ritase                                                            [ok]
                params.put("driver", nik_baru); //driver by employee                                          [ok]
                params.put("branchId", restnomor); //branch

                if(restnomor.equals("321") || restnomor.equals("336") || restnomor.equals("324") || restnomor.equals("317") || restnomor.equals("036")){
                    params.put("companyId", "ASA");
                } else {
                    params.put("companyId", "TVIP");
                }
                int total = 0;
                for(int i = 0; i < data_terima_produk_pojos.size(); i++){
                    String szId = adapter.getItem(i).getTotalHarga();
                    String[] parts2 = szId.split("\\.");
                    String szIdSlice = parts2[0];

                    total+=Integer.parseInt(szIdSlice);

                    String[] parts3 = tax.split("\\.");
                    String taxSplit = parts3[0];

                    float rumus_dpp = 100 + Integer.parseInt(taxSplit); //data.getTax berisi nilai 11 jadi 100+11= 111
                    float hasil_semua_dpp = (float)(100 / rumus_dpp) * (float)total; //dpp
                    float hasil_semua_ppn = total - hasil_semua_dpp;


                    params.put("ppn", String.valueOf(hasil_semua_dpp));
                    params.put("dpp", String.valueOf(hasil_semua_ppn));
                }
                params.put("total_diskon", String.valueOf(total_diskon));
                params.put("total_harga", String.valueOf(total_harga));

                String id_cs;
                if(checkalihcustomer.isChecked()){
                    String[] parts2 = act_alih_customer.getText().toString().split(",");
                    id_cs = parts2[1];
                } else {
                    String[] customer = act_pilihCustomer.getText().toString().split(",");
                    id_cs = customer[1];
                }

                params.put("customerId", id_cs); //id customer toko pelanggan                              [ok]
                params.put("foto_customer", currentDateandTime); //tanggal foto pelanggan

                params.put("alamat_foto_customer", cityName); //alamat pada saat foto toko/pelanggan      [ok]

                params.put("longlat_foto_customer", latitude + ", " + longitude); //longlat pada saat foto toko/pelanggan   [ok]
                params.put("pembayaran", pilihpembayaran.getText().toString()); //pembayaran yang digunakan, saat ini tunai     [ok]

                params.put("tanggal_terima_produk", currentDateandTime); //tanggal menerima produk, tanggal sekaranag                  [ok]
                params.put("catatan", tv_canvaser_catatan.getText().toString()); //catatan yang dicatat si driver                           [ok]
                params.put("selesai_kunjungan", "1"); //selesai terima produk
                params.put("selesai_terima_produk", "1"); //selesai terima produk

                params.put("jenis_pembayaran", String.valueOf(jenis_pembayaran));

                params.put("pembayaran_tunai", edt_jumlah_tunai.getText().toString().replaceAll( ",", "" ));
                params.put("pembayaran_transfer", edt_jumlah_transfer.getText().toString().replaceAll( ",", "" ));
                params.put("pembayaran_total", String.valueOf(total_harga));

                if(chk_transfer.isChecked()){
                    params.put("foto_bukti_transfer", "https://apisec.tvip.co.id/image_sfa_apps/foto_bukti_kanvas/IMG-BuktiTransfer" + "_" + nik_baru + "_" + "no_cust-"+id_cs + "_" + "no_do-"+ DocDo + "_" + currentDateandTime + ".jpeg");

                } else {
                    params.put("foto_bukti_transfer", currentDateandTime);
                }

                if(mixornot.equals("null") || mixornot.equals("")){
                    params.put("produk_mix", "0");
                } else {
                    params.put("produk_mix", "1");
                }






                 //foto bukti transfer
                System.out.println("Hasil params = " + params);
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
        if (requestQueue42 == null) {
            requestQueue42 = Volley.newRequestQueue(mCanvaser_Terima_Produk.this);
            requestQueue42.add(stringRequest2);
        } else {
            requestQueue42.add(stringRequest2);
        }
    }

    private void postImage() {
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://apisec.tvip.co.id/mobile_eis_2/upload_sfa.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getApplicationContext(), "Upload Foto gagal", Toast.LENGTH_SHORT).show();

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


                String gambar = ImageToString_buktiTransfer(bitmap2);

                String id_cs;
                if(checkalihcustomer.isChecked()){
                    String[] customer = act_alih_customer.getText().toString().split(",");
                    id_cs = customer[1];
                } else {
                    id_cs = idcustomer.getText().toString();
                }

                params.put("nik", "IMG-BuktiTransfer" + "_" + nik_baru + "_" + "no_cust-"+id_cs + "_" + "no_do-"+ DocDo + "_" + currentDateandTime);
                params.put("nama_folder", "foto_bukti_kanvas");
                params.put("foto", gambar);

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

        if (requestQueue43 == null) {
            requestQueue43 = Volley.newRequestQueue(mCanvaser_Terima_Produk.this);
            requestQueue43.add(stringRequest2);
        } else {
            requestQueue43.add(stringRequest2);
        }
    }

    private void post_historydriverterimaprodukitem_canvas() {

        for(int i = 0;i <=  data_terima_produk_pojos.size() - 1 ;i++) {

            int finalI1 = i;

            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_mdba_history_driver_terimaprodukitem",
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

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    String currentDateandTime = sdf.format(new Date());

                    params.put("dtmDoc", currentDateandTime); //tanggal kapan input, tanggal sekarang                                      [ok]
                    params.put("nomor_do", DocDo); //nomor DO (DELIVERY ORDER)                                                   [ok]
                    params.put("nomor_bkb", tv_canvas_no_bkb.getText().toString()); //nomor BKB                                                     [ok]
                    params.put("nomor_std", tv_canvas_no_std.getText().toString()); //nomr STD (SURAT TUGAS DISTIRBUSI)                               [ok]

                    params.put("intItemNumber", String.valueOf(finalI1));
                    params.put("szProductId", adapter.getItem(finalI1).getSzName());
                    params.put("szOrderItemTypeId", String.valueOf(finalI1));
                    params.put("decQty", adapter.getItem(finalI1).getQtyEdit());
                    params.put("szUomId", adapter.getItem(finalI1).getSzUomId());
                    params.put("decPrice", adapter.getItem(finalI1).getDecPrice());

                    params.put("decDiscount", "0");

                    if(!adapter.getItem(finalI1).getSzName().equals("VT.5GALON BT") || adapter.getItem(finalI1).getSzName().equals("AQ.5GALLON BTL")){
                        params.put("bTaxable", "1");
                        String[] parts3 = tax.split("\\.");
                        String taxSplit = parts3[0];
                        float rumus_dpp = 100 + Integer.parseInt(taxSplit); //data.getTax berisi nilai 11 jadi 100+11= 111
                        float hasil_semua_dpp = (float)(100 / rumus_dpp) * (float)total_harga; //dpp
                        float hasil_semua_ppn = total_harga - hasil_semua_dpp;

                        params.put("decTax", String.valueOf(hasil_semua_ppn));
                        params.put("decDpp", String.valueOf(hasil_semua_dpp));

                        params.put("szTaxId", "PPN");
                        params.put("decTaxRate", tax);
                    } else {
                        params.put("bTaxable", "0");

                        params.put("decTax", "0");
                        params.put("decDpp", "0");

                        params.put("szTaxId", "");
                        params.put("decTaxRate", "0");
                    }

                    params.put("decAmount", adapter.getItem(finalI1).getTotalHarga()); //total harga produk per row



                    params.put("decDiscPrinciple",adapter.getItem(finalI1).getDiskon_tiv());
                    params.put("decDiscDistributor", adapter.getItem(finalI1).getDiskon_distributor());
                    params.put("decDiscInternal", adapter.getItem(finalI1).getDiskon_internal());

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
            if (requestQueue44 == null) {
                requestQueue44 = Volley.newRequestQueue(mCanvaser_Terima_Produk.this);
                requestQueue44.add(stringRequest2);
            } else {
                requestQueue44.add(stringRequest2);
            }
        }
    }

    public class ListViewAdapteProductDraft extends ArrayAdapter<data_product_bkb_pojo> {
        private class ViewHolder {
            TextView namaproduk, tv_id_produk_listview;
            EditText edt_listview_isi_qty_canvas;
            ImageView bt_remove_list;

        }

        List<data_product_bkb_pojo> data_terima_produk_pojos;
        private Context context;

        public ListViewAdapteProductDraft(List<data_product_bkb_pojo> data_terima_produk_pojos, Context context) {
            super(context, R.layout.list_product, data_terima_produk_pojos);
            this.data_terima_produk_pojos = data_terima_produk_pojos;
            this.context = context;
            notifyDataSetChanged();

        }

        public int getCount() {
            return data_terima_produk_pojos.size();
        }

        public data_product_bkb_pojo getItem(int position) {
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
            data_product_bkb_pojo data = getItem(position);

            if (convertView == null) {
                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.list_product, parent, false);

                viewHolder.namaproduk = (TextView) convertView.findViewById(R.id.tv_nama_produk_listview);
                viewHolder.tv_id_produk_listview = (TextView) convertView.findViewById(R.id.tv_id_produk_listview); //qty yang berubah ketika diedit
                viewHolder.edt_listview_isi_qty_canvas = (EditText) convertView.findViewById(R.id.edt_listview_isi_qty_canvas); //total harga per 1 produk/ 1 row/ 1lsitview
                viewHolder.bt_remove_list = (ImageView) convertView.findViewById(R.id.bt_remove_list); //total harga per 1 produk/ 1 row/ 1lsitview



                convertView.setTag(viewHolder);



            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.namaproduk.setText(data.getSzName());
            viewHolder.tv_id_produk_listview.setText(data.getSzProductId());

            viewHolder.edt_listview_isi_qty_canvas.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewHolder.edt_listview_isi_qty_canvas.getText().clear();
                }
            });

            viewHolder.bt_remove_list.setVisibility(GONE);

            viewHolder.bt_remove_list.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Integer index = (Integer) v.getTag();
                    data_terima_produk_pojos.remove(position);
                    Utility.setListViewHeightBasedOnChildren(listproduk);
                    notifyDataSetChanged();
                }
            });


            viewHolder.edt_listview_isi_qty_canvas.addTextChangedListener(new TextWatcher() {

                @Override
                public void afterTextChanged(Editable s) {

                }

                @Override
                public void beforeTextChanged(CharSequence s, int start,
                                              int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(viewHolder.edt_listview_isi_qty_canvas.getText().toString().length() ==0){

                    } else {
                        String qtyasli = data.getDecQty();
                        String[] Qtyparts = qtyasli.split("\\.");
                        String QtypartsSlice = Qtyparts[0];

                        if(Integer.parseInt(viewHolder.edt_listview_isi_qty_canvas.getText().toString()) > Integer.parseInt(QtypartsSlice)){
                            new SweetAlertDialog(mCanvaser_Terima_Produk.this, SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("Qty Tidak Boleh Melebihi Batas Qty asli")
                                    .setConfirmText("OK")
                                    .show();
                            viewHolder.edt_listview_isi_qty_canvas.setText("0");
                            data.setQtyEdit("0");
                        } else {
                            data.setQtyEdit(viewHolder.edt_listview_isi_qty_canvas.getText().toString());
                        }
                    }



                }
            });




            return convertView;
        }

    }

    @Override
    public void onBackPressed() {
        adapter.clear();
        finish();
    }

    public boolean isOnline() {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            int timeoutMs = 150;
            Socket sock = new Socket();
            SocketAddress sockaddr = new InetSocketAddress("8.8.8.8", 53);

            sock.connect(sockaddr, timeoutMs);
            System.out.println("Ping Address = " + sockaddr);
            sock.close();

            return true;
        } catch (IOException e) { return false; }
    }

    @Override
    public void onStop() {
        super.onStop();
        StringRequest rest = new StringRequest(Request.Method.GET,  "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Rollback/rollback?szDocDO=" + DocDo,
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
        };
        rest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        if (requestQueue56 == null) {
            requestQueue56 = Volley.newRequestQueue(mCanvaser_Terima_Produk.this);
            requestQueue56.add(rest);
        } else {
            requestQueue56.add(rest);
        }
    }





}