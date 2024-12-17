package com.tvip.canvasser.menu_mulai_perjalanan;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
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
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dantsu.escposprinter.EscPosPrinter;
import com.dantsu.escposprinter.connection.bluetooth.BluetoothConnection;
import com.dantsu.escposprinter.connection.bluetooth.BluetoothPrintersConnections;
import com.dantsu.escposprinter.textparser.PrinterTextParserImg;
import com.google.android.material.textfield.TextInputLayout;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.tvip.canvasser.Perangkat.HttpsTrustManager;
import com.tvip.canvasser.R;
import com.tvip.canvasser.pojo.data_docdoitem_pojo;
import com.tvip.canvasser.pojo.data_docdoitemprice_pojo;
import com.tvip.canvasser.pojo.data_produk_penjualan_pojo;
import com.tvip.canvasser.pojo.data_terima_produk_pojo;
import com.tvip.canvasser.pojo.total_penjualan_pojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
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

public class mPenjualan_Terima_Produk extends AppCompatActivity {

    static ListView listproductpenjualan, listdocdoitem, listdocdoitemprice;

    static List<data_produk_penjualan_pojo> data_produk_penjualan_pojos = new ArrayList<>();
    static List<total_penjualan_pojo>totalPenjualanPojos = new ArrayList<>();

    static List<data_terima_produk_pojo> data_terima_produk_pojos = new ArrayList<>();
    static mPenjualan_Terima_Produk.ListViewAdapteProductPenjualan adapter;

    static mPenjualan_Terima_Produk.ListViewAdapteDocDoItem adapter_docdoitem;
    static List<data_docdoitem_pojo> data_docdoitem_pojos = new ArrayList<>();

    static mPenjualan_Terima_Produk.ListViewAdapteDocDoItemPrice adapter_docdoitemprice;
    static List<data_docdoitemprice_pojo> data_docdoitemprice_pojos = new ArrayList<>();

    Button batal, lanjutkan, bt_camera, bt_konfirmasi, bt_camera_bukti_transfer;
    TextView delivery_order, tv_totalharga, tv_totaldiskon, tv_status;
    String encodeImageString, string_harga_satuan, string_total_harga_row, string_diskon_tiv, string_diskon_distributor, string_diskon_internal;

    //STRING HEADER POST
    String string_header_szDocId, string_header_szCustomerId, string_header_szEmployeeId, string_header_szDocSoId,
            string_header_szVehicleId, string_header_szHelper1, string_header_szHelper2, string_header_szSalesId, string_header_szBranchId,
            string_header_szCompanyId, string_header_szDocStatus, string_header_szBkbId, string_header_dtmDoc, string_header_szOrderTypeId,
            string_header_bCash, string_header_bInvoiced, string_header_szPaymentTermId, string_header_bDirectWarehouse, string_header_szWarehouseId,
            string_header_szStockTypeId, string_header_szCustomerPO, string_header_dtmCustomerPO, string_header_szDocStockOutCustomerId, string_header_szReturnFromId,
            string_header_szDescription, string_header_szPromoDesc, string_header_intPrintedCount, string_header_szUserCreatedId, string_header_szUserUpdatedId, string_header_dtmCreated,
            string_header_dtmLastUpdated, string_header_dtmMobileTransaction, string_header_szManualNo, string_header_szMobileId, string_header_idstd, string_header_ritase;

    String string_footer_nodok_DO,string_footer_nodok_SO,string_footer_ppn,string_footer_dpp,
            string_footer_totaldiskon,string_footer_totalharga,string_footer_depo;

    String string_ppn, string_dpp;
    String currentDate;
    String string_jenis_pembyaran;

    EditText edt_jumlah_tunai, edt_jumlah_transfer;

    TextInputLayout txt_input_jumlah_transfer, txt_input_jumlah_tunai;
    LinearLayout linear_keterangan_photo;

    private SimpleDateFormat dateFormatter;
    private Calendar date;
    String idstd, idcustomer, nomor_do;
    SearchView cariproduct;

    SharedPreferences sharedPreferences;

    AutoCompleteTextView pilihpembayaran;
    EditText tanggalkirim;
    EditText catatan;
    ImageButton refresh;
    String[] jenis = {"Tunai"};

    LinearLayout fotooutlet, linear_gambar;
    ImageView uploadgambar;
    Bitmap bitmap, bitmap2, bitmap_bukti_transfer;
    TextView tv_tanggal, tv_alamat, tv_longlat, tv_no_std, tv_no_customer, tv_ppn, tv_dpp;

    ContentValues cv;
    Uri imageUri;
    ImageView img, img_bukti_transfer;
    GPSTracker gps;

    CheckBox chk_tunai, chk_transfer;
    SweetAlertDialog pDialog;
    int jenis_pembayaran = 0;

    ImageView img1,img2,img3,img4,img5,img6,img7,img8,img9,img10;

    TextView total_jenis_pembayaran, tv_submit_loading,
            tv_terima_produk, tv_totalharga_format_rupiah, tv_totaldiskon_format_rupiah,
            tv_total_jenis_pembayaran_rupiah,tv_total_jenis_pembayaran_text;

    Button bt_submit_berhasil;
    RelativeLayout relative_foto_bukti_transfer;

    public static final int PERMISSION_BLUETOOTH = 1;

    private final Locale locale = new Locale("id", "ID");
    private final DateFormat df = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss", locale);
    private final NumberFormat nf = NumberFormat.getCurrencyInstance(locale);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mpenjualan_terima_produk);
        HttpsTrustManager.allowAllSSL();

        //LISTVIEW-----------------------------------------------------------------------------
        listproductpenjualan = findViewById(R.id.listproductpenjualan);
        listdocdoitem = findViewById(R.id.list_docdoitem);
        listdocdoitemprice = findViewById(R.id.list_docdoitemprice);

        final Dialog dialog_submit = new Dialog(mPenjualan_Terima_Produk.this);
        dialog_submit.setContentView(R.layout.dialog_loading_submit_terima_produk);
        dialog_submit.setCancelable(false);

        img1 = dialog_submit.findViewById(R.id.img_1);
        img2 = dialog_submit.findViewById(R.id.img_2);
        img3 = dialog_submit.findViewById(R.id.img_3);
        img4 = dialog_submit.findViewById(R.id.img_4);
        img5 = dialog_submit.findViewById(R.id.img_5);
        img6 = dialog_submit.findViewById(R.id.img_6);
        img7 = dialog_submit.findViewById(R.id.img_7);
        img8 = dialog_submit.findViewById(R.id.img_8);
        img9 = dialog_submit.findViewById(R.id.img_9);
        img10 = dialog_submit.findViewById(R.id.img_10);

        bt_submit_berhasil = dialog_submit.findViewById(R.id.bt_submit_berhasil);
        tv_submit_loading = dialog_submit.findViewById(R.id.tv_submit_loading);

        edt_jumlah_transfer = findViewById(R.id.edt_jumlah_transfer);
        edt_jumlah_tunai = findViewById(R.id.edt_jumlah_tunai);

        tv_terima_produk = findViewById(R.id.tv_terima_produk);

        dateFormatter = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
        cariproduct = findViewById(R.id.cariproduct);
        img = (ImageView) findViewById(R.id.img_background);
        img_bukti_transfer = (ImageView) findViewById(R.id.img_background_bukti_transfer);

        System.out.println("HASIL ID STD = " + getIntent().getStringExtra("idStd"));
        System.out.println("HASIL ID CUSTOMER = " + getIntent().getStringExtra("idCustomer"));
        idstd = getIntent().getStringExtra("idStd");
        idcustomer = getIntent().getStringExtra("idCustomer");

        chk_tunai = findViewById(R.id.chk_tunai);
        chk_transfer = findViewById(R.id.chk_transfer);

        txt_input_jumlah_transfer = findViewById(R.id.txt_input_jumlah_transfer);
        txt_input_jumlah_tunai = findViewById(R.id.txt_input_jumlah_tunai);

        batal = findViewById(R.id.batal);
        lanjutkan = findViewById(R.id.lanjutkan);
        bt_camera = findViewById(R.id.foto);

        pilihpembayaran = findViewById(R.id.pilihpembayaran);
        tanggalkirim = findViewById(R.id.tanggalkirim);
        catatan = findViewById(R.id.catatan);
        refresh = findViewById(R.id.refresh);

        total_jenis_pembayaran = findViewById(R.id.tv_total_jenis_pembayaran);
        tv_total_jenis_pembayaran_text = findViewById(R.id.tv_total_jenis_pembayaran_text);

        delivery_order = findViewById(R.id.tv_delivery_order);

        tv_status = findViewById(R.id.tv_status);
        tv_no_std = findViewById(R.id.tv_no_std);
        tv_no_customer = findViewById(R.id.tv_no_customer);
        tv_ppn = findViewById(R.id.ppn);
        tv_dpp = findViewById(R.id.dpp);
        tv_totalharga = findViewById(R.id.total);
        tv_totalharga_format_rupiah = findViewById(R.id.tv_total_harga_format_rupiah);
        tv_totaldiskon_format_rupiah = findViewById(R.id.tv_diskon_format_rupiah);
        tv_total_jenis_pembayaran_rupiah = findViewById(R.id.tv_total_jenis_pembayaran_rupiah);

        bt_konfirmasi = findViewById(R.id.bt_oke);
        bt_camera_bukti_transfer = findViewById(R.id.foto_bukti_transfer);
        relative_foto_bukti_transfer = findViewById(R.id.relative_bukti_transfer);


        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>
                (this, android.R.layout.select_dialog_item, jenis);
        pilihpembayaran.setAdapter(adapter2);//setting the adapter data into the AutoCompleteTextView

        currentDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault()).format(new Date());
        String currentDate2 = new SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault()).format(new Date());
        tanggalkirim.setText(currentDate2);

        batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.clear();
                finish();
            }
        });

        lanjutkan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (img.equals(null)) {

                } else if (total_jenis_pembayaran.getText().toString().equals("total")){

                    Toast.makeText(mPenjualan_Terima_Produk.this, "Mohon konfirmasi pembayaran terlebih dahulu", Toast.LENGTH_SHORT).show();

                } else if (Integer.parseInt(total_jenis_pembayaran.getText().toString()) >= Integer.parseInt(tv_totalharga.getText().toString())){

                    final Dialog dialogstatus = new Dialog(mPenjualan_Terima_Produk.this);
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
                        public void onClick(View v) {

                            if (img.getDrawable() == null){

                                Toast.makeText(mPenjualan_Terima_Produk.this, "Upload foto terlebih dahulu", Toast.LENGTH_SHORT).show();

                            } else {

                                dialog_submit.show();

                                bt_submit_berhasil.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        dialog_submit.dismiss();
                                        adapter.clear();
                                        finish();

                                    }
                                });

                                //POST KE TABLE HISTORY DRIVER DOCDO
                                post_historydriverdocdo();

                                //POST KE TABLE HISTORY DRIVER DOCDO ITEM
                                posthistorydriverdocdoitem();

                                //POST KE TABLE HISTORY DRIVER DOCDO ITEM PRICE
                                posthistorydriverdocdoitemprice();

                                //POST KE TABLE HISTORY DRIVER TOTAL HARGA DO
                                post_historydrivertotalhargado();

                                //POST KE HISTORY DRIVER TERIMA PRODUK
                                post_historydriverterimaproduk();

                                //POST KE HISTORY DRIVER TERIMA PRODUK ITEM
                                post_historydriverterimaprodukitem();

                                //UPDATE KE TABLE DOCDO DI MDBA DAN DMS
                                update_docdo_driver_dms_mdba();

                                //UPDATE KE TABLE DOCDOITEM DI MDBA DAN DMS
                                update_docdoitem_driver_dms_mdba();

                                //UPDATE KE TABLE DOCDOITEM DI MDBA DAN DMS
                                update_docdoitemprice_driver_dms_mdba();

                                //UPDATE KE TABLE MDBATOTALHARGADO
                                update_totalhargado_mdba();

                                //UPLOAD FOTO
                                upload_foto();

                                //UPLOAD FOTO BUKTI TRANSFER
                                upload_foto_canvaser();

                            }

                            dialogstatus.dismiss();

                        }
                    });

                    dialogstatus.show();

                } else {

                    Toast.makeText(mPenjualan_Terima_Produk.this, "Total pembayaran tidak sesuai", Toast.LENGTH_SHORT).show();

                }

//                for(int i = 0; i < data_produk_penjualan_pojos.size();i++){
//
////                    if (!(adapter.getItem(i).getExpired_date() == null)){
////                        Double harga = Double.valueOf(adapter.getItem(i).getDecPrice());
////                        int total_harga = harga.intValue() * Integer.parseInt(adapter.getItem(i).getStock_qty());
////                        totalPenjualanPojos.add(new total_penjualan_pojo(
////                                adapter.getItem(i).getSzId(),
////                                adapter.getItem(i).getSzName(),
////                                adapter.getItem(i).getDecPrice(),
////                                adapter.getItem(i).getStock_qty(),
////                                adapter.getItem(i).getDisplay(),
////                                String.valueOf(total_harga),
////                                adapter.getItem(i).getDisc_total(),
////                                adapter.getItem(i).getStock(),
////                                adapter.getItem(i).getExpired(),
////                                adapter.getItem(i).getSzUomId(),
////                                adapter.getItem(i).getDisc_distributor(),
////                                adapter.getItem(i).getDisc_internal(),
////                                adapter.getItem(i).getDisc_principle()));
////                    }
//                    if(i == data_produk_penjualan_pojos.size() -1){
//                        Intent intent = new Intent(getBaseContext(), summary_order.class);
//                        startActivity(intent);
//                    }
//                }
            }
        });

        bt_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                linear_keterangan_photo = findViewById(R.id.linear_keterangan_photo);
                tv_tanggal = findViewById(R.id.tv_tanggal_photo);
                tv_longlat = findViewById(R.id.tv_longlat_photo);
                tv_alamat = findViewById(R.id.tv_alamat_photo);

                gps = new GPSTracker(mPenjualan_Terima_Produk.this);

                if (gps.canGetLocation()) {

                    linear_keterangan_photo.setVisibility(View.VISIBLE);

                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();

                    // \n is for new line
                    Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();

                    Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                    List<Address> addresses = null;
                    try {
                        addresses = geocoder.getFromLocation(latitude, longitude, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                    String currentDateandTime = sdf.format(new Date());

                    String cityName = addresses.get(0).getAddressLine(0);
                    String stateName = addresses.get(0).getLocality();
                    String countryName = addresses.get(0).getCountryName();

                    System.out.println("cityName" + cityName);
                    System.out.println("stateName" + stateName);
                    System.out.println("countryName" + countryName);

                    tv_tanggal.setText(currentDateandTime);
                    tv_longlat.setText(latitude + ", " + longitude);
                    tv_alamat.setText(cityName);

                    /**
                     * If you only want the city name then you should use addresses.get(0).getLocality() instead, addresses.get(0).getAddressLine(0)
                     * might give you more information additionally to your city name based on the address (Read the Geocoder Documentation).
                     * but the Locality gives you exactly the cityName.
                     */

                } else {

                    Toast.makeText(getApplicationContext(), "Terjadi Kesalahan", Toast.LENGTH_SHORT).show();

                }

                Dexter.withActivity(mPenjualan_Terima_Produk.this)
                        .withPermission(Manifest.permission.CAMERA)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
//                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                                startActivityForResult(intent, 2);

                                cv = new ContentValues();
                                cv.put(MediaStore.Images.Media.TITLE, "My Picture");
                                cv.put(MediaStore.Images.Media.DESCRIPTION, "From Camera");

                                imageUri = getContentResolver().insert(
                                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv);
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                                startActivityForResult(intent, 1);
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

        bt_camera_bukti_transfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withActivity(mPenjualan_Terima_Produk.this)
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

                            if (!edt_jumlah_transfer.getText().toString().equals("")){
                                txt_input_jumlah_transfer.setError(null);
                            } else {
                                txt_input_jumlah_transfer.setError("Jumlah transfer tidak boleh kosong");
                            }
                        }
                    });

                } else if (!chk_transfer.isChecked()){
                    img_bukti_transfer.setImageBitmap(null);
                    txt_input_jumlah_transfer.setVisibility(View.GONE);
                    edt_jumlah_transfer.setText("");
                    txt_input_jumlah_transfer.setError(null);
                    relative_foto_bukti_transfer.setVisibility(View.GONE);
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

                            if (!edt_jumlah_tunai.getText().toString().equals("")){
                                txt_input_jumlah_tunai.setError(null);
                            } else {
                                txt_input_jumlah_tunai.setError("Jumlah tunai tidak boleh kosong");
                            }
                        }
                    });


                } else if (!chk_tunai.isChecked()){
                    txt_input_jumlah_tunai.setVisibility(View.GONE);
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

        bt_konfirmasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (chk_tunai.isChecked() && !chk_transfer.isChecked()) {

                    if (edt_jumlah_tunai.getText().toString().equals("")) {
                        txt_input_jumlah_tunai.setError("Jumlah tunai tidak boleh kosong");

                    } else {

                        int jumlah_tunai = Integer.parseInt(edt_jumlah_tunai.getText().toString());
                        String string_jumlah_tunai = String.valueOf(jumlah_tunai);
                        //Toast.makeText(mPenjualan_Terima_Produk.this, "jumlah tunai = " + string_jumlah_tunai, Toast.LENGTH_SHORT).show();

                        jenis_pembayaran = 1;

                        total_jenis_pembayaran.setText(string_jumlah_tunai);

                        String totalhargarupiah = formatRupiah(Double.valueOf(string_jumlah_tunai));
                        tv_total_jenis_pembayaran_rupiah.setText(totalhargarupiah);
                        tv_total_jenis_pembayaran_text.setText("tunai : ");

                    }

                } else if (!chk_tunai.isChecked() && chk_transfer.isChecked()) {


                    if (edt_jumlah_transfer.getText().toString().equals("")) {
                        txt_input_jumlah_transfer.setError("Jumlah transfer tidak boleh kosong");

                    } else {
                        int jumlah_transfer = Integer.parseInt(edt_jumlah_transfer.getText().toString());
                        String string_jumlah_transfer = String.valueOf(jumlah_transfer);
                        //Toast.makeText(mPenjualan_Terima_Produk.this, "jumlah transfeer = " + string_jumlah_transfer, Toast.LENGTH_SHORT).show();

                        jenis_pembayaran = 2;
                        total_jenis_pembayaran.setText(string_jumlah_transfer);

                        String totalhargarupiah = formatRupiah(Double.valueOf(string_jumlah_transfer));
                        tv_total_jenis_pembayaran_rupiah.setText(totalhargarupiah);
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

                        int jumlah_tunai = Integer.parseInt(edt_jumlah_tunai.getText().toString());
                        int jumlah_transfer = Integer.parseInt(edt_jumlah_transfer.getText().toString());

                        String total = String.valueOf(jumlah_tunai + jumlah_transfer);

                        //Toast.makeText(mPenjualan_Terima_Produk.this, "jumlah keduanya = " + total, Toast.LENGTH_SHORT).show();

                        jenis_pembayaran = 3;
                        total_jenis_pembayaran.setText(total);

                        String totalhargarupiah = formatRupiah(Double.valueOf(total));
                        tv_total_jenis_pembayaran_rupiah.setText(totalhargarupiah);
                        tv_total_jenis_pembayaran_text.setText("tunai & transfer : ");

                    }
                }

            }
        });

        header_data();

        body_data();

        footer_data();

        docDoItem_data();

        docDoItemPrice_data();

        mdba_driver_totalhargaDO();

        //Toast.makeText(mPenjualan_Terima_Produk.this, "ON CREATE", Toast.LENGTH_SHORT).show();


//        String[] parts = no_surat.split("-");
//        String restnomor = parts[0];
//        String restnomorbaru = restnomor.replace(" ", "");
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_Driver_Detail_Penjualan?id_customer="+idcustomer+"&id_std="+idstd,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//
//                        try {
//                            int number = 0;
//                            JSONObject obj = new JSONObject(response);
//                            final JSONArray movieArray = obj.getJSONArray("data");
//                            for (int i = 0; i < movieArray.length(); i++) {
//                                final JSONObject movieObject = movieArray.getJSONObject(i);
//                                final data_terima_produk_pojo movieItem = new data_terima_produk_pojo(
//                                        movieObject.getString("no_so_turunan"),
//                                        movieObject.getString("no_std"),
//                                        movieObject.getString("no_do"),
//                                        movieObject.getString("id_produk"),
//                                        movieObject.getString("nama_produk"),
//                                        movieObject.getString("qty_produk"),
//                                        movieObject.getString("harga_satuan"),
//                                        movieObject.getString("ppn"),
//                                        movieObject.getString("dpp"),
//                                        movieObject.getString("totaldiskon"),
//                                        movieObject.getString("totalharga"),
//                                        movieObject.getString("totalhargarow"),
//                                        movieObject.getString("id_customer"));
//
//                                String nomor_do = movieObject.getString("no_do");
//                                        delivery_order.setText(nomor_do);
//                                data_terima_produk_pojos.add(movieItem);
//
//                                adapter = new mPenjualan_Terima_Produk.ListViewAdapteProductPenjualan(data_terima_produk_pojos, getApplicationContext());
//                                listproductpenjualan.setAdapter(adapter);
//
////                                cariproduct.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
////                                    @Override
////                                    public boolean onQueryTextSubmit(String text) {
////                                        return false;
////                                    }
////
////                                    @Override
////                                    public boolean onQueryTextChange(String newText) {
////                                        adapter.filter(newText);
////                                        return true;
////                                    }
////                                });
//                            }
//
//                            pDialog.dismissWithAnimation();
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
//                        pDialog.dismissWithAnimation();
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

    /** CATATAN:
     *
     * PADA SAAT KLIK BUTTON SUBMIT DI MENU TERIMA PRODUK, AKAN:
     *
     * A. POST, ke dalam 4 tabel. Post ini digunakan untuk menyimpan ke history,
     *    inputan asli dari mdba tanpa adanya perubahan. Masuk ke tabel:
     *    1. mdba_driver_history_docdo          [OK]
     *    2. mdba_driver_history_docdoitem      [OK]
     *    3. mdba_driver_history_docdoitemprice [OK]
     *    4. mdba_driver_history_totalhargado   [OK]
     *
     * B. UPDATE, Ke dalam 4 tabel MDBA. Update jika terdapat perubahan qty produk & harga
     *    1. dms_sd_docdo               [PROGRESS]
     *    2. dms_sd_docdoitem           [PROGRESS]
     *    3. dms_sd_docdoitemprice      [PROGRESS]
     *    4. mdbatotalhargado           [PROGRESS]
     *
     * C. UPDATE, ke dalam 3 tabel DMS. Sama seperti update ke tabel mdba
     *    1. dms_sd_docdo               [PROGRESS]
     *    2. dms_sd_docdoitem           [PROGRESS]
     *    3. dms_sd_docdoitemprice      [PROGRESS]
     *
     * D. POST, ke dalam 2 tabel. untuk history terima produk & akan dipake unutuk mencatat LPH
     *    1. mdba_driver_history_terimaproduk     [OK]
     *    2. mdba_driver_history_terimaprodukitem [OK]
     *
     * 10 DESEMBER 2022
     */


    //----------------------------------GET DATAAA, DIPAKE UNTUK HISTORY MDBA DRIVER------------------------------------------------

    //UNTUK GET/MENDAPATKAN nomor dokumen, seperti nomor DO, BKB. dari tabel dummymdbaasa`.`dms_sd_docdo
    public void header_data(){

        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_Detail_terima_produk",
                new Response.Listener<String>() {

                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onResponse(String response) {


                        try {
                            int number = 0;
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data_heder");
                            for (int i = 0; i < movieArray.length(); i++) {

                                final JSONObject movieObject = movieArray.getJSONObject(i);

                                string_header_szDocId = movieObject.getString("szDocId");
                                string_header_dtmDoc = movieObject.getString("dtmDoc");
                                string_header_szCustomerId = movieObject.getString("szCustomerId");
                                string_header_szEmployeeId = movieObject.getString("szEmployeeId");
                                string_header_szOrderTypeId = movieObject.getString("szOrderTypeId");

                                string_header_bCash = movieObject.getString("bCash");
                                string_header_bInvoiced = movieObject.getString("bInvoiced");
                                string_header_szPaymentTermId = movieObject.getString("szPaymentTermId");
                                string_header_szDocSoId = movieObject.getString("szDocSoId");
                                string_header_szVehicleId = movieObject.getString("szVehicleId");

                                string_header_szHelper1 = movieObject.getString("szHelper1");
                                string_header_bDirectWarehouse = movieObject.getString("bDirectWarehouse");
                                string_header_szWarehouseId = movieObject.getString("szWarehouseId");
                                string_header_szStockTypeId = movieObject.getString("szStockTypeId");
                                string_header_szCustomerPO = movieObject.getString("szCustomerPO");

                                string_header_dtmCustomerPO = movieObject.getString("dtmCustomerPO");
                                string_header_szSalesId = movieObject.getString("szSalesId");
                                string_header_szDocStockOutCustomerId = movieObject.getString("szDocStockOutCustomerId");
                                string_header_szReturnFromId = movieObject.getString("szReturnFromId");
                                string_header_szDescription = movieObject.getString("szDescription");

                                string_header_szPromoDesc = movieObject.getString("szPromoDesc");
                                string_header_intPrintedCount = movieObject.getString("intPrintedCount");
                                string_header_szBranchId = movieObject.getString("szBranchId");
                                string_header_szCompanyId = movieObject.getString("szCompanyId");
                                string_header_szDocStatus = movieObject.getString("szDocStatus");

                                string_header_szUserCreatedId = movieObject.getString("szUserCreatedId");
                                string_header_szUserUpdatedId = movieObject.getString("szUserUpdatedId");
                                string_header_dtmCreated = movieObject.getString("dtmCreated");
                                string_header_dtmLastUpdated = movieObject.getString("dtmLastUpdated");
                                string_header_dtmMobileTransaction = movieObject.getString("dtmMobileTransaction");

                                string_header_szMobileId = movieObject.getString("szMobileId");
                                string_header_szManualNo = movieObject.getString("szManualNo");
                                string_header_szBkbId = movieObject.getString("szBkbId");

                                string_header_idstd = movieObject.getString("id_std");
                                string_header_ritase = movieObject.getString("ritase");

                                tv_no_customer.setText(string_header_szCustomerId);
                                tv_no_std.setText(string_header_idstd);

                                history_terima_produk();

                                tv_status = findViewById(R.id.tv_status);
                                tv_status.setText(string_header_szDocStatus);

                                if (string_header_szDocStatus.equals("Applied")) {
                                    tv_status.setVisibility(View.VISIBLE);
                                    tv_status.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.color_background_blue));
                                    tv_status.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.color_text_blue));
                                } else if (string_header_szDocStatus.equals("Draft")) {
                                    tv_status.setVisibility(View.VISIBLE);
                                    tv_status.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.color_background_green));
                                    tv_status.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.color_text_green));
                                } else {
                                    tv_status.setVisibility(View.VISIBLE);
                                    tv_status.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.color_background_grey));
                                    tv_status.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.color_text_grey));
                                }

                            }

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

                params.put("id_customer", idcustomer);
                params.put("id_std", idstd);

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
        RequestQueue requestQueue2 = Volley.newRequestQueue(mPenjualan_Terima_Produk.this);
        requestQueue2.add(stringRequest2);


    }

    //UNUTK GET/MENDAPATKAN DOCDOITEM `dummymdbaasa`.`dms_sd_docdoitem`
    public void docDoItem_data(){

        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_Detail_terima_produk",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {
                            int number = 0;
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data_docdoitem");
                            for (int i = 0; i < movieArray.length(); i++) {
                                final JSONObject movieObject = movieArray.getJSONObject(i);

                                final data_docdoitem_pojo movieItem = new data_docdoitem_pojo(
                                        movieObject.getString("szDocId"),
                                        movieObject.getString("intItemNumber"),
                                        movieObject.getString("szProductId"),
                                        movieObject.getString("szOrderItemTypeId"),
                                        movieObject.getString("szTrnType"),
                                        movieObject.getString("decQty"),
                                        movieObject.getString("szUomId"));

                                System.out.println("BERHASIL LOAD DOCDOITEM");

                                data_docdoitem_pojos.add(movieItem);
                                adapter_docdoitem = new mPenjualan_Terima_Produk.ListViewAdapteDocDoItem(data_docdoitem_pojos, getApplicationContext());
                                listdocdoitem.setAdapter(adapter_docdoitem);

                            }

                        } catch(JSONException e){
                            e.printStackTrace();

                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                System.out.println("GAGAL LOAD DOCDOITEM");

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

                params.put("id_customer", idcustomer);
                params.put("id_std", idstd);

                return params;
            }

        };
        stringRequest2.setRetryPolicy(
                new DefaultRetryPolicy(
                        50000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
        );
        RequestQueue requestQueue2 = Volley.newRequestQueue(mPenjualan_Terima_Produk.this);
        requestQueue2.add(stringRequest2);

    }

    //UNUTK GET/MENDAPATKAN DOCDOITEM PRICE, GET DARI TABEL `dummymdbaasa`.`dms_sd_docdoitemprice`
    public void docDoItemPrice_data(){

        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_Detail_terima_produk",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {
                            int number = 0;
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data_docdoitemprice");
                            for (int i = 0; i < movieArray.length(); i++) {
                                final JSONObject movieObject = movieArray.getJSONObject(i);

                                final data_docdoitemprice_pojo movieItem = new data_docdoitemprice_pojo(
                                        movieObject.getString("szDocId"),
                                        movieObject.getString("intItemNumber"),
                                        movieObject.getString("intItemDetailNumber"),
                                        movieObject.getString("szPriceId"),
                                        movieObject.getString("decPrice"),
                                        movieObject.getString("decDiscount"),
                                        movieObject.getString("bTaxable"),
                                        movieObject.getString("decAmount"),
                                        movieObject.getString("decTax"),
                                        movieObject.getString("decDpp"),
                                        movieObject.getString("szTaxId"),
                                        movieObject.getString("decTaxRate"),
                                        movieObject.getString("decDiscPrinciple"),
                                        movieObject.getString("decDiscDistributor"),
                                        movieObject.getString("decDiscInternal"));

                                System.out.println("BERHASIL LOAD DOCDOITEM PRICE");

                                data_docdoitemprice_pojos.add(movieItem);
                                adapter_docdoitemprice = new mPenjualan_Terima_Produk.ListViewAdapteDocDoItemPrice(data_docdoitemprice_pojos, getApplicationContext());
                                listdocdoitemprice.setAdapter(adapter_docdoitemprice);

                            }

                        } catch(JSONException e){
                            e.printStackTrace();

                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                System.out.println("GAGAL LOAD DOCDOITEMPRICE");

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

                params.put("id_customer", idcustomer);
                params.put("id_std", idstd);

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
        RequestQueue requestQueue2 = Volley.newRequestQueue(mPenjualan_Terima_Produk.this);
        requestQueue2.add(stringRequest2);

    }

    //UNTUK GET/MENDAPATKAN TOTAL HARGA DO, GET DARI TABEL dummymdbaasa`.`mdbatotalhargado
    public void mdba_driver_totalhargaDO(){

        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_Detail_terima_produk",
                new Response.Listener<String>() {

                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onResponse(String response) {

                        try {
                            int number = 0;
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data_foter");
                            for (int i = 0; i < movieArray.length(); i++) {

                                final JSONObject movieObject = movieArray.getJSONObject(i);

                                string_footer_nodok_DO = movieObject.getString("nodok_DO");
                                string_footer_nodok_SO = movieObject.getString("nodok_SO");
                                string_footer_ppn = movieObject.getString("ppn");
                                string_footer_dpp = movieObject.getString("dpp");
                                string_footer_totaldiskon = movieObject.getString("totaldiskon");
                                string_footer_totalharga = movieObject.getString("totalharga");
                                string_footer_depo = movieObject.getString("depo");

                            }

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

                params.put("id_customer", idcustomer);
                params.put("id_std", idstd);

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
        RequestQueue requestQueue2 = Volley.newRequestQueue(mPenjualan_Terima_Produk.this);
        requestQueue2.add(stringRequest2);


    }


    //----------------------------GET DATA YANG DITAMPILAN DI MENU TAMPIL PRODUK--------------------

    //UNTUK GET/MENDAPATKAN HARGA PER ROW (DI DALAM LISTVIEW), SEPERTI DISKON TIV, DISKON INTERNAL, DISKON DISTRIBUSI
    //DITAMPILKAN BERUPA LISTVIEW
    public void body_data(){

        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_Detail_terima_produk",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {
                            int number = 0;
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data_body");
                            for (int i = 0; i < movieArray.length(); i++) {
                                final JSONObject movieObject = movieArray.getJSONObject(i);
                                final data_terima_produk_pojo movieItem = new data_terima_produk_pojo(
                                        movieObject.getString("intItemDetailNumber"),
                                        movieObject.getString("decTax"),
                                        movieObject.getString("szUomId"),
                                        movieObject.getString("szProductId"),
                                        movieObject.getString("szName"),
                                        movieObject.getString("decQty"), //digunakan untuk qty asli, tanpa adanya perubahan
                                        movieObject.getString("harga_satuan"),
                                        movieObject.getString("diskon_tiv"),
                                        movieObject.getString("diskon_distributor"),
                                        movieObject.getString("diskon_internal"),
                                        movieObject.getString("decAmount"),
                                        movieObject.getString("jumlah"),
                                        movieObject.getString("intItemNumber"),
                                        movieObject.getString("szOrderItemTypeId"),
                                        movieObject.getString("bTaxable"),
                                        movieObject.getString("szTaxId"),
                                        movieObject.getString("decTaxRate"),
                                        movieObject.getString("decTax"),
                                        movieObject.getString("decDpp"),
                                        movieObject.getString("decQty"),
                                        movieObject.getString("tax")); //digunakan untuk edit qty

                                nomor_do = movieObject.getString("szDocId");
                                delivery_order.setText(nomor_do);
                                data_terima_produk_pojos.add(movieItem);

                                adapter = new mPenjualan_Terima_Produk.ListViewAdapteProductPenjualan(data_terima_produk_pojos, getApplicationContext());
                                listproductpenjualan.setAdapter(adapter);

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

//                String[] parts = no_surat.split("-");
//                String restnomor = parts[0];
//                String restnomorbaru = restnomor.replace(" ", "");

                params.put("id_customer", idcustomer);
                params.put("id_std", idstd);

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
        RequestQueue requestQueue2 = Volley.newRequestQueue(mPenjualan_Terima_Produk.this);
        requestQueue2.add(stringRequest2);

    }

    //UNTUK GET/MENDAPATKAN TOTAL HARGA DAN TOTAL DISKON, GET DARI TABEL dummymdbaasa`.`mdbatotalhargado
    //GET INI DITAMPILKAN DI MENU TERIMA PRODUK, TEPATNYA DIATAS BUTTON SUBMIT
    public void footer_data(){

        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_Detail_terima_produk",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {
                            int number = 0;
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data_foter");
                            for (int i = 0; i < movieArray.length(); i++) {

                                final JSONObject movieObject = movieArray.getJSONObject(i);

                                String string_totalharga = movieObject.getString("totalharga");
                                String string_diskon = movieObject.getString("totaldiskon");
                                string_ppn = movieObject.getString("ppn");
                                string_dpp = movieObject.getString("dpp");

                                tv_ppn.setText(string_ppn);
                                tv_dpp.setText(string_dpp);

                                tv_totalharga.setText(string_totalharga);

                                String totalhargarupiah = formatRupiah(Double.valueOf(string_totalharga));
                                tv_totalharga_format_rupiah.setText(totalhargarupiah);

                                tv_totaldiskon = findViewById(R.id.diskon);
                                tv_totaldiskon.setText(string_diskon);

                                String totaldiskonrupiah = formatRupiah(Double.valueOf(string_diskon));
                                tv_totaldiskon_format_rupiah.setText(totaldiskonrupiah);

                            }

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
                SharedPreferences sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                String nik_baru = sharedPreferences.getString("szDocCall", null);

                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
                String currentDateandTime2 = sdf2.format(new Date());

//                String[] parts = no_surat.split("-");
//                String restnomor = parts[0];
//                String restnomorbaru = restnomor.replace(" ", "");

                params.put("id_customer", idcustomer);
                params.put("id_std", idstd);

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
        RequestQueue requestQueue2 = Volley.newRequestQueue(mPenjualan_Terima_Produk.this);
        requestQueue2.add(stringRequest2);


    }


    //-------------------------LISTVIEW ADAPTER-----------------------------------------------------

    /**
     * ADAPTER INI DITAMPILKAN DI MENU TERIMA PRODUK (VISIBILTY.VISIBLE)
     */

    //ADAPTER YANG DITAMPILKAN BERUPA LISTVIEW DI MENU TERIMA PRODDUK
    public class ListViewAdapteProductPenjualan extends ArrayAdapter<data_terima_produk_pojo> {

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
        }

        List<data_terima_produk_pojo> data_terima_produk_pojos;
        private Context context;

        public ListViewAdapteProductPenjualan(List<data_terima_produk_pojo> data_terima_produk_pojos, Context context) {
            super(context, R.layout.list_terima_produk, data_terima_produk_pojos);
            this.data_terima_produk_pojos = data_terima_produk_pojos;
            this.context = context;
        }

        public int getCount() {
            return data_terima_produk_pojos.size();
        }

        public data_terima_produk_pojo getItem(int position) {
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
            final mPenjualan_Terima_Produk.ListViewAdapteProductPenjualan.ViewHolder viewHolder;
            data_terima_produk_pojo data = getItem(position);

            if (convertView == null) {
                viewHolder = new mPenjualan_Terima_Produk.ListViewAdapteProductPenjualan.ViewHolder();
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

                convertView.setTag(viewHolder);

            } else {
                viewHolder = (mPenjualan_Terima_Produk.ListViewAdapteProductPenjualan.ViewHolder) convertView.getTag();
            }

//            double number = Double.parseDouble(branch);
//            String COUNTRY = "ID";
//            String LANGUAGE = "id";
//            String str = NumberFormat.getCurrencyInstance(new Locale(LANGUAGE, COUNTRY)).format(number);
//
//            System.out.println("HASILNYA = " + str);

            //viewHolder.harga_satuan.setText(kursIndonesia.format(data.getHarga_satuan()));

            viewHolder.namaproduk.setText(data.getNama_produk());

            String[] total_harga_row = data.getTotalhargarow().split("\\.");
            viewHolder.string_total_harga_row = total_harga_row[0];

            String[] harga_satuan = data.getHarga_satuan().split("\\.");
            viewHolder.string_harga_satuan = harga_satuan[0];

            String[] diskon_tiv = data.getDiskon_tiv().split("\\.");
            viewHolder.string_diskon_tiv = diskon_tiv[0];

            String[] diskon_distributor = data.getDiskon_distributor().split("\\.");
            viewHolder.string_diskon_distributor = diskon_distributor[0];

            String[] diskon_internal = data.getDiskon_internal().split("\\.");
            viewHolder.string_diskon_internal = diskon_internal[0];

            String[] qty_produk = data.getQty_produk().split("\\.");
            viewHolder.string_qty_produk= qty_produk[0];

            //MENDAPATKAN QTY PRODUK ASLI TANPA PERUBAHAN
            String[] qty_order_asli = data.getQty_produk_asli().split("\\.");
            viewHolder.string_qty_order_produk_asli = qty_order_asli[0];


//            DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
//            DecimalFormatSymbols formatRp = new DecimalFormatSymbols();
//            formatRp.setCurrencySymbol("Rp. ");
//            formatRp.setMonetaryDecimalSeparator(',');
//            formatRp.setGroupingSeparator('.');
//            kursIndonesia.setDecimalFormatSymbols(formatRp);

            viewHolder.total_harga.setText(viewHolder.string_total_harga_row);
            viewHolder.harga_satuan.setText(viewHolder.string_harga_satuan);

            viewHolder.diskon_tiv.setText(viewHolder.string_diskon_tiv);
            viewHolder.diskon_distributor.setText(viewHolder.string_diskon_distributor);
            viewHolder.diskon_internal.setText(viewHolder.string_diskon_internal);

            String harga_row_rupiah = formatRupiah(Double.valueOf(viewHolder.string_total_harga_row));
            viewHolder.total_harga_row_format_rupiah.setText(harga_row_rupiah);

            String harga_satuan_row_rupiah = formatRupiah(Double.valueOf(viewHolder.string_harga_satuan));
            viewHolder.harga_satuan_format_rupiah.setText(harga_satuan_row_rupiah);

            String diskon_tiv_rupiah = formatRupiah(Double.valueOf(viewHolder.string_diskon_tiv));
            viewHolder.diskon_tiv_format_rupiah.setText(diskon_tiv_rupiah);

            String diskon_distributor_rupiah = formatRupiah(Double.valueOf(viewHolder.string_diskon_distributor));
            viewHolder.diskon_distributor_format_rupiah.setText(diskon_distributor_rupiah);

            String diskon_internal_rupiah = formatRupiah(Double.valueOf(viewHolder.string_diskon_internal));
            viewHolder.diskon_internal_format_rupiah.setText(diskon_internal_rupiah);

            viewHolder.intItemNumber.setText(data.getIntItemNumber());
            viewHolder.szOrderItemTypeId.setText(data.getSzOrderItemTypeId());
            viewHolder.bTaxable.setText(data.getbTaxable());
            viewHolder.szTaxId.setText(data.getSzTaxId());
            viewHolder.decTaxRate.setText(data.getDecTaxRate());
            viewHolder.decTax.setText(data.getDecTax());
            viewHolder.decDpp.setText(data.getDecDpp());

            viewHolder.tv_qty_produk.setText(viewHolder.string_qty_produk);
            viewHolder.qty_order_asli.setText(viewHolder.string_qty_order_produk_asli);

            viewHolder.tax.setText(data.getTax()); //untuk mendapatkan tax terupdate

            viewHolder.bt_plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int quantity_asli = Integer.parseInt(viewHolder.string_qty_order_produk_asli); // get the quantity for this row item
                    int quantity = Integer.parseInt(viewHolder.string_qty_produk); // get the quantity for this row item
                    int harga_satuan = Integer.parseInt(viewHolder.string_harga_satuan);
                    int total_harga_row = Integer.parseInt(viewHolder.string_total_harga_row);
                    int diskon_internal = Integer.parseInt(viewHolder.string_diskon_internal);
                    int diskon_distributor = Integer.parseInt(viewHolder.string_diskon_distributor);
                    int diskon_tiv = Integer.parseInt(viewHolder.string_diskon_tiv);

                    data.setQty_produk(String.valueOf(quantity + 1)); // update it by adding 1
                    viewHolder.tv_qty_produk.setText(Integer.toString(quantity));

                    int total = quantity * harga_satuan - diskon_internal - diskon_distributor - diskon_tiv;

                    System.out.println("HASIL PERKALIAN = " + total);

                    adapter.notifyDataSetChanged();// set the new description (that uses the updated qunatity)

                    if (quantity == quantity_asli - 1){
                        viewHolder.bt_plus.setVisibility(View.GONE);
                    }
                }
            });

            viewHolder.bt_minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int quantity_asli = Integer.parseInt(viewHolder.string_qty_order_produk_asli); // get the quantity for this row item
                    int quantity = Integer.parseInt(viewHolder.string_qty_produk); // get the quantity for this row item
                    int harga_satuan = Integer.parseInt(viewHolder.string_harga_satuan);
                    int total_harga_row = Integer.parseInt(viewHolder.string_total_harga_row);
                    int diskon_internal = Integer.parseInt(viewHolder.string_diskon_internal);
                    int diskon_distributor = Integer.parseInt(viewHolder.string_diskon_distributor);
                    int diskon_tiv = Integer.parseInt(viewHolder.string_diskon_tiv);


                    data.setQty_produk(String.valueOf(quantity - 1)); // update it by adding 1
                    viewHolder.tv_qty_produk.setText(Integer.toString(quantity));

                    int total = quantity * harga_satuan - diskon_internal - diskon_distributor - diskon_tiv;

                    adapter.notifyDataSetChanged();// set the new description (that uses the updated qunatity)

                    System.out.println("HASIL PERKALIAN = " + total);

                    if (quantity <= quantity_asli){
                        viewHolder.bt_plus.setVisibility(View.VISIBLE);
                    }

                }
            });


            if(data.getQty_produk() == null){
                viewHolder.qty_order.setText("0");
            } else {
                String szId = data.getQty_produk();
                String[] parts = szId.split("\\.");
                String szIdSlice = parts[0];
                viewHolder.qty_order.setText(szIdSlice);
            }

            viewHolder.buttonExpand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent intent = new Intent(getApplicationContext(), sisa_stock.class);
//                    intent.putExtra("list", String.valueOf(position));
//                    intent.putExtra("nama_barang", data.getSzName());
//                    intent.putExtra("uang", data.getDecPrice());
//                    startActivity(intent)

                    if (viewHolder.linear_detail_terima_produk.getVisibility() == View.GONE) {
                        //expandedChildList.set(arg2, true);
                        viewHolder.linear_detail_terima_produk.setVisibility(View.VISIBLE);
                    }
                    else {
                        //expandedChildList.set(arg2, false);
                        viewHolder.linear_detail_terima_produk.setVisibility(View.GONE);
                    }
                }
            });

            viewHolder.bt_edit_qty_produk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Dialog dialogstatus = new Dialog(mPenjualan_Terima_Produk.this);
                    dialogstatus.setContentView(R.layout.dialog_edit_qty_produk);

                    String string_qty_produk_edit;

                    String[] qty_produk = data.getQty_produk().split("\\.");
                    string_qty_produk_edit = qty_produk[0];

                    Button batal = dialogstatus.findViewById(R.id.batal);
                    Button ok = dialogstatus.findViewById(R.id.ok);
                    TextView dialog_nama_produk = dialogstatus.findViewById(R.id.dialog_nama_produk);
                    TextView dialog_qty_produk = dialogstatus.findViewById(R.id.dialog_qty_produk);

                    TextView dialog_qty_produk_editan = dialogstatus.findViewById(R.id.dialog_qty_produk_editan);

                    ImageButton bt_image_minus = dialogstatus.findViewById(R.id.bt_minus);
                    ImageButton bt_image_plus = dialogstatus.findViewById(R.id.bt_plus);
                    TextView qty_produk_edit = dialogstatus.findViewById(R.id.tv_qty_produk);

                    dialog_nama_produk.setText(data.getNama_produk());
                    dialog_qty_produk.setText(data.getQty_produk_asli());

                    String szId = data.getQty_produk();
                    String[] parts = szId.split("\\.");
                    String szIdSlice = parts[0];

                    qty_produk_edit.setText(szIdSlice);
                    dialog_qty_produk_editan.setText(szIdSlice + " Qty");


                    bt_image_plus.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String szId = qty_produk_edit.getText().toString();
                            int total_qty = Integer.parseInt(String.valueOf(szId));

                            String szIds = data.getQty_produk_asli();
                            String[] parts = szIds.split("\\.");
                            String szIdSlice = parts[0];

                            if(total_qty >= Integer.parseInt(szIdSlice)){
                                new SweetAlertDialog(mPenjualan_Terima_Produk.this, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText("Qty Tidak Boleh Lebih Besar")
                                        .setConfirmText("OK")
                                        .show();
                                dialog_qty_produk_editan.setText(String.valueOf(total_qty));
                            } else {
                                total_qty++;
                                qty_produk_edit.setText(String.valueOf(total_qty));
                                dialog_qty_produk_editan.setText(String.valueOf(total_qty) + " Qty");
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
                            dialog_qty_produk_editan.setText(String.valueOf(total_qty) + " Qty");
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
                            viewHolder.qty_order.setText(qty_produk_edit.getText().toString());
                            data.setQty_produk(qty_produk_edit.getText().toString());

                            int qty = Integer.parseInt(qty_produk_edit.getText().toString());

                            int harga_satuan = Integer.parseInt(viewHolder.string_harga_satuan);

                            int total_harga = qty * harga_satuan;

                            int diskonDistributor = Integer.parseInt(viewHolder.string_diskon_distributor);
                            int diskonTIV = Integer.parseInt(viewHolder.string_diskon_tiv);
                            int diskonInternal = Integer.parseInt(viewHolder.string_diskon_internal);

                            int total_diskon = diskonDistributor + diskonTIV + diskonInternal;

                            int total_harga_diskon = total_harga - total_diskon;

                            viewHolder.total_harga.setText(String.valueOf(total_harga_diskon));
                            data.setTotalhargarow(String.valueOf(total_harga_diskon));

                            String total_harga_row_rupiah = formatRupiah((double) total_harga_diskon);
                            viewHolder.total_harga_row_format_rupiah.setText(total_harga_row_rupiah);

                            //RUMUS MENCARI DPP
                            float dpp = 100 + Integer.parseInt(data.getTax()); //data.getTax berisi nilai 11 jadi 100+11= 111
//                          float rumus_tax = 100/tax;
//                          float total_dpp = (100 * total_harga_diskon) / dpp; //dpp

                            float total_dpp = (float)(100 / dpp) * (float)total_harga_diskon; //dpp

                            //RUMUS MENCARI PPN
                            float total_ppn = total_harga_diskon - total_dpp;

                            System.out.println("HASIL TOTAL DPP = " + total_dpp);
                            System.out.println("HASIL TOTAL PPN = " + total_ppn);

                            viewHolder.decDpp_setelah_edit.setText(String.valueOf(total_dpp));
                            data.setDecDpp(String.valueOf(total_dpp));
                            viewHolder.decTax_setelah_edit.setText(String.valueOf(total_ppn));
                            data.setDecTax(String.valueOf(total_ppn));


                            System.out.println("tax " + dpp);
                            System.out.println("hasil total harga diskon " + total_harga_diskon);
                            System.out.println("HASILNUA " + total_dpp);

                            //viewHolder.decTax_setelah_edit.setText(String.valueOf(total_tax));

                            int total = 0;

                            for(int i = 0; i < data_terima_produk_pojos.size(); i++){
                                String szId = adapter.getItem(i).getTotalhargarow();
                                String[] parts = szId.split("\\.");
                                String szIdSlice = parts[0];

                                total+=Integer.parseInt(szIdSlice);

                                System.out.println("Hasil Looping = " + szIdSlice + " Total = " + String.valueOf(total));
                                tv_totalharga.setText(String.valueOf(total));

                                String totalformatrupiah = formatRupiah((double) total);
                                tv_totalharga_format_rupiah.setText(totalformatrupiah);

                                float rumus_dpp = 100 + Integer.parseInt(data.getTax()); //data.getTax berisi nilai 11 jadi 100+11= 111
                                float hasil_semua_dpp = (float)(100 / rumus_dpp) * (float)total; //dpp
                                float hasil_semua_ppn = total - hasil_semua_dpp;

                                tv_dpp.setText(String.valueOf(hasil_semua_dpp));
                                tv_ppn.setText(String.valueOf(hasil_semua_ppn));

                                System.out.println("TOTAL DARI SEMUA DPP " + hasil_semua_dpp);
                                System.out.println("TOTAL DARI SEMUA PPN " + hasil_semua_ppn);


                            }

                            dialogstatus.dismiss();

                        }
                    });

                    dialogstatus.show();
                }
            });

            listproductpenjualan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                }
            });


            return convertView;
        }

//        public void filter(String charText) {
//            charText = charText.toLowerCase(Locale.getDefault());
//            data_terima_produk_pojos.clear();
//            if (charText.length() == 0) {
//                data_terima_produk_pojos.addAll(data_produk_penjualanList);
//            } else {
//                for (data_terima_produk_pojo wp : data_produk_penjualanList) {
//                    if (wp.getSzName().toLowerCase(Locale.getDefault()).contains(charText)) {
//                        data_produk_penjualan_pojos.add(wp);
//                    }
//                }
//            }
//            notifyDataSetChanged();
//        }

    }

//    public void showDeviceInfo(String name){
//
//        final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
//        View mView = getLayoutInflater().inflate(R.layout.popup_device, null);
//
//
//        TextView DeviceName = (TextView)mView.findViewById(R.id.deviceName);
//        DeviceName.setText(name); //setting device name
//        alert.setView(mView);
//
//        final AlertDialog alertDialog = alert.create();
//        alertDialog.setCanceledOnTouchOutside(true);
//
//
//        alertDialog.show();
//
//    }


    //----------------------------LISTVIEW ADAPTER MDBA HISOTRY DRIVER------------------------------

    /**
     * ADAPTER INI TIDAK DITAMPILKAN DI MENU TERIMA PRODUK (VISIBILTY.GONE)
     */

    //ADAPTER YANG DITAMPILKAN BERUPA LISTVIEW DI MENU TERIMA PRODDUK
    public class ListViewAdapteDocDoItem extends ArrayAdapter<data_docdoitem_pojo> {

        private class ViewHolder {
            TextView tv_docdoitem_szDocId, tv_docdoitem_intItemNumber, tv_docdoitem_szProductId, tv_docdoitem_szOrderItemTypeId, tv_docdoitem_szTrnType,
                    tv_docdoitem_decQty, tv_docdoitem_szUomId;
        }

        List<data_docdoitem_pojo> data_docdoitem_pojos;
        private Context context;

        public ListViewAdapteDocDoItem(List<data_docdoitem_pojo> data_docdoitem_pojos, Context context) {
            super(context, R.layout.list_docdoitem, data_docdoitem_pojos);
            this.data_docdoitem_pojos = data_docdoitem_pojos;
            this.context = context;
        }

        public int getCount() {
            return data_docdoitem_pojos.size();
        }

        public data_docdoitem_pojo getItem(int position) {
            return data_docdoitem_pojos.get(position);
        }

        @Override
        public int getViewTypeCount() {
            int count;
            if (data_docdoitem_pojos.size() > 0) {
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
            final mPenjualan_Terima_Produk.ListViewAdapteDocDoItem.ViewHolder viewHolder;
            data_docdoitem_pojo data = getItem(position);

            if (convertView == null) {
                viewHolder = new mPenjualan_Terima_Produk.ListViewAdapteDocDoItem.ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.list_docdoitem, parent, false);

                viewHolder.tv_docdoitem_szDocId = (TextView) convertView.findViewById(R.id.tv_szDocId);
                viewHolder.tv_docdoitem_intItemNumber = (TextView) convertView.findViewById(R.id.tv_intItemNumber);
                viewHolder.tv_docdoitem_szProductId = (TextView) convertView.findViewById(R.id.tv_szProductId);
                viewHolder.tv_docdoitem_szOrderItemTypeId = (TextView) convertView.findViewById(R.id.tv_szOrderItemTypeId);
                viewHolder.tv_docdoitem_szTrnType = (TextView) convertView.findViewById(R.id.tv_szTrnType);
                viewHolder.tv_docdoitem_decQty = (TextView) convertView.findViewById(R.id.tv_decQty);
                viewHolder.tv_docdoitem_szUomId = (TextView) convertView.findViewById(R.id.tv_szUomId);

                convertView.setTag(viewHolder);

            } else {
                viewHolder = (mPenjualan_Terima_Produk.ListViewAdapteDocDoItem.ViewHolder) convertView.getTag();
            }

            viewHolder.tv_docdoitem_szDocId.setText(data.getSzDocId());
            viewHolder.tv_docdoitem_intItemNumber.setText(data.getSzDointItemNumbercId());
            viewHolder.tv_docdoitem_szProductId.setText(data.getSzProductId());
            viewHolder.tv_docdoitem_szOrderItemTypeId.setText(data.getSzOrderItemTypeId());
            viewHolder.tv_docdoitem_szTrnType.setText(data.getSzTrnType());
            viewHolder.tv_docdoitem_decQty.setText(data.getDecQty());
            viewHolder.tv_docdoitem_szUomId.setText(data.getSzUomId());


            return convertView;
        }

    }

    //ADAPTER YANG DITAMPILKAN BERUPA LISTVIEW DI MENU TERIMA PRODDUK
    public class ListViewAdapteDocDoItemPrice extends ArrayAdapter<data_docdoitemprice_pojo> {

        private class ViewHolder {
            TextView tv_docdoitemprice_szDocId, tv_docdoitemprice_intItemNumber, tv_docdoitemprice_intItemDetailNumber, tv_docdoitemprice_szPriceId, tv_docdoitemprice_decPrice,
                    tv_docdoitemprice_decDiscount, tv_docdoitemprice_bTaxable, tv_docdoitemprice_decAmount, tv_docdoitemprice_decTax, tv_docdoitemprice_decDpp, tv_docdoitemprice_szTaxId,
                    tv_docdoitemprice_decTaxRate, tv_docdoitemprice_decDiscPrinciple, dtv_docdoitemprice_decDiscDistributor, tv_docdoitemprice_decDiscInternal;
        }

        List<data_docdoitemprice_pojo> data_docdoitemprice_pojos;
        private Context context;

        public ListViewAdapteDocDoItemPrice(List<data_docdoitemprice_pojo> data_docdoitemprice_pojos, Context context) {
            super(context, R.layout.list_docdoitemprice, data_docdoitemprice_pojos);
            this.data_docdoitemprice_pojos = data_docdoitemprice_pojos;
            this.context = context;
        }

        public int getCount() {
            return data_docdoitemprice_pojos.size();
        }

        public data_docdoitemprice_pojo getItem(int position) {
            return data_docdoitemprice_pojos.get(position);
        }

        @Override
        public int getViewTypeCount() {
            int count;
            if (data_docdoitemprice_pojos.size() > 0) {
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
            final mPenjualan_Terima_Produk.ListViewAdapteDocDoItemPrice.ViewHolder viewHolder;
            data_docdoitemprice_pojo data = getItem(position);

            if (convertView == null) {
                viewHolder = new mPenjualan_Terima_Produk.ListViewAdapteDocDoItemPrice.ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.list_docdoitemprice, parent, false);

                viewHolder.tv_docdoitemprice_szDocId = (TextView) convertView.findViewById(R.id.tv_szDocId);
                viewHolder.tv_docdoitemprice_intItemNumber = (TextView) convertView.findViewById(R.id.tv_intItemNumber);
                viewHolder.tv_docdoitemprice_intItemDetailNumber = (TextView) convertView.findViewById(R.id.tv_intItemDetailNumber);
                viewHolder.tv_docdoitemprice_szPriceId = (TextView) convertView.findViewById(R.id.tv_szPriceId);
                viewHolder.tv_docdoitemprice_decPrice = (TextView) convertView.findViewById(R.id.tv_decPrice);
                viewHolder.tv_docdoitemprice_decDiscount = (TextView) convertView.findViewById(R.id.tv_decDiscount);
                viewHolder.tv_docdoitemprice_bTaxable = (TextView) convertView.findViewById(R.id.tv_bTaxable);
                viewHolder.tv_docdoitemprice_decAmount = (TextView) convertView.findViewById(R.id.tv_decAmount);
                viewHolder.tv_docdoitemprice_decTax = (TextView) convertView.findViewById(R.id.tv_decTax);
                viewHolder.tv_docdoitemprice_decDpp = (TextView) convertView.findViewById(R.id.tv_decDpp);
                viewHolder.tv_docdoitemprice_szTaxId = (TextView) convertView.findViewById(R.id.tv_szTaxId);
                viewHolder.tv_docdoitemprice_decTaxRate = (TextView) convertView.findViewById(R.id.tv_decTaxRate);
                viewHolder.tv_docdoitemprice_decDiscPrinciple = (TextView) convertView.findViewById(R.id.tv_decDiscPrinciple);
                viewHolder.dtv_docdoitemprice_decDiscDistributor = (TextView) convertView.findViewById(R.id.tv_decDiscDistributor);
                viewHolder.tv_docdoitemprice_decDiscInternal = (TextView) convertView.findViewById(R.id.tv_decDiscInternal);

                convertView.setTag(viewHolder);

            } else {
                viewHolder = (mPenjualan_Terima_Produk.ListViewAdapteDocDoItemPrice.ViewHolder) convertView.getTag();
            }

            viewHolder.tv_docdoitemprice_szDocId.setText(data.getSzDocId());
            viewHolder.tv_docdoitemprice_intItemNumber.setText(data.getIntItemNumber());
            viewHolder.tv_docdoitemprice_intItemDetailNumber.setText(data.getIntItemDetailNumber());
            viewHolder.tv_docdoitemprice_szPriceId.setText(data.getSzPriceId());
            viewHolder.tv_docdoitemprice_decPrice.setText(data.getDecPrice());
            viewHolder.tv_docdoitemprice_decDiscount.setText(data.getDecDiscount());
            viewHolder.tv_docdoitemprice_bTaxable.setText(data.getbTaxable());
            viewHolder.tv_docdoitemprice_decAmount.setText(data.getDecAmount());
            viewHolder.tv_docdoitemprice_decTax.setText(data.getDecTax());
            viewHolder.tv_docdoitemprice_decDpp.setText(data.getDecDpp());
            viewHolder.tv_docdoitemprice_szTaxId.setText(data.getSzTaxId());
            viewHolder.tv_docdoitemprice_decTaxRate.setText(data.getDecTaxRate());
            viewHolder.tv_docdoitemprice_decDiscPrinciple.setText(data.getDecDiscPrinciple());
            viewHolder.dtv_docdoitemprice_decDiscDistributor.setText(data.getDecDiscDistributor());
            viewHolder.tv_docdoitemprice_decDiscInternal.setText(data.getDecDiscInternal());


            return convertView;
        }

    }

    //-----------------------------------------FOTO------------------------------------------------

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1){

            if (resultCode == Activity.RESULT_OK) {

                try {
                    bitmap = MediaStore.Images.Media.getBitmap(
                            getContentResolver(), imageUri);

                    int width=720;
                    int height=720;
                    bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
                    img.setImageBitmap(bitmap);

                    ViewGroup.LayoutParams paramktp = img.getLayoutParams();

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
                    img.setLayoutParams(paramktp);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } else if (requestCode == 2){

            if (resultCode == Activity.RESULT_OK) {

                try {
                    bitmap2 = MediaStore.Images.Media.getBitmap(
                            getContentResolver(), imageUri);

                    int width=720;
                    int height=720;
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

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//
//        LinearLayout linear_keterangan_photo = findViewById(R.id.linear_keterangan_photo);
//        tv_tanggal = findViewById(R.id.tv_tanggal_photo);
//        tv_longlat = findViewById(R.id.tv_longlat_photo);
//        tv_alamat = findViewById(R.id.tv_alamat_photo);
//
//        if (resultCode != RESULT_CANCELED) {
//
//            if (requestCode == 1 && resultCode == RESULT_OK) {
//
//
//
//                Uri filepath = data.getData();
//
//                gps = new GPSTracker(mPenjualan_Terima_Produk.this);
//
//                try {
//
//                    InputStream inputStream = getContentResolver().openInputStream(filepath);
//                    bitmap = BitmapFactory.decodeStream(inputStream);
//                    img.setImageBitmap(bitmap);
//
//                    if (gps.canGetLocation()) {
//                        double latitude = gps.getLatitude();
//                        double longitude = gps.getLongitude();
//
//                        // \n is for new line
//                        //Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
//
//                    } else {
//                        // Can't get location.
//                        // GPS or network is not enabled.
//                        // Ask user to enable GPS/network in settings.
//
//                        Toast.makeText(getApplicationContext(), "Terjadi Kesalahan", Toast.LENGTH_SHORT).show();
//                    }
//
//                } catch (Exception ex) {
//
//                }
//
//            } else if (requestCode == 2 && resultCode == RESULT_OK) {
//
//                gps = new GPSTracker(mPenjualan_Terima_Produk.this);
//
//                if (gps.canGetLocation()) {
//
//                    linear_keterangan_photo.setVisibility(View.VISIBLE);
//
//                    double latitude = gps.getLatitude();
//                    double longitude = gps.getLongitude();
//
//                    // \n is for new line
//                    Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
//
//                    Geocoder geocoder = new Geocoder(this, Locale.getDefault());
//                    List<Address> addresses = null;
//                    try {
//                        addresses = geocoder.getFromLocation(latitude, longitude, 1);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
//                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
//                    String currentDateandTime = sdf.format(new Date());
//
//                    String cityName = addresses.get(0).getAddressLine(0);
//                    String stateName = addresses.get(0).getLocality();
//                    String countryName = addresses.get(0).getCountryName();
//
//                    System.out.println("cityName" + cityName);
//                    System.out.println("stateName" + stateName);
//                    System.out.println("countryName" + countryName);
//
//                    tv_tanggal.setText(currentDateandTime);
//                    tv_longlat.setText(latitude + ", " + longitude);
//                    tv_alamat.setText(cityName);
//
//                    /**
//                     * If you only want the city name then you should use addresses.get(0).getLocality() instead, addresses.get(0).getAddressLine(0)
//                     * might give you more information additionally to your city name based on the address (Read the Geocoder Documentation).
//                     * but the Locality gives you exactly the cityName.
//                     */
//
//                } else {
//
//                    Toast.makeText(getApplicationContext(), "Terjadi Kesalahan", Toast.LENGTH_SHORT).show();
//
//                }
//
//                bitmap = (Bitmap) data.getExtras().get("data");
//                img.setImageBitmap(bitmap);
//            } else if (requestCode == 3 && resultCode == RESULT_OK) {
//
//                bitmap = (Bitmap) data.getExtras().get("data");
//                img_bukti_transfer.setImageBitmap(bitmap);
//            }
//
//        } else if (resultCode == RESULT_CANCELED) {
//            // user cancelled Image capture
//            Toast.makeText(getApplicationContext(),
//                    "Cancelled", Toast.LENGTH_SHORT)
//                    .show();
//        } else {
//            // failed to capture image
//            Toast.makeText(getApplicationContext(),
//                    "Error!", Toast.LENGTH_SHORT)
//                    .show();
//        }
//
//        super.onActivityResult(requestCode, resultCode, data);
//    }

//    private void encodeBitmapImage(Bitmap bitmap) {
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
//        byte[] bytesofimage = byteArrayOutputStream.toByteArray();
//        encodeImageString = android.util.Base64.encodeToString(bytesofimage, Base64.DEFAULT);
//    }

    private String ImageToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    private String ImageToString_buktiTransfer(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    public void CallToFunction() {
        if (img == null){

            Toast.makeText(mPenjualan_Terima_Produk.this, "FOTO PELANGGAN BELUM DI UPLOAD", Toast.LENGTH_SHORT).show();
        } else {

            Toast.makeText(mPenjualan_Terima_Produk.this, "FOTO PELANGGAN SUDAH DI UPLOAD", Toast.LENGTH_SHORT).show();

        }
    }


    //-------------------------------------------POST DATAA-----------------------------------------

    //UNTUK POST KE TABLE HISTORY DRIVER DOCDO, (HEADER)
    private void post_historydriverdocdo() {

        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_mdba_history_driver_docdo",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        img1.setBackgroundColor(Color.parseColor("#FF74EF79"));
                        //Toast.makeText(mPenjualan_Terima_Produk.this, "OKE posthistorydriverdocdo BERHASIL", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                img1.setBackgroundColor(Color.parseColor("#E91E63"));
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

                params.put("szDocId", string_header_szDocId);
                params.put("dtmDoc", string_header_dtmDoc);
                params.put("szCustomerId", string_header_szCustomerId);
                params.put("szEmployeeId", string_header_szEmployeeId);
                params.put("szOrderTypeId", string_header_szOrderTypeId);

                params.put("bCash", string_header_bCash);
                params.put("bInvoiced", string_header_bInvoiced);
                params.put("szPaymentTermId", string_header_szPaymentTermId);
                params.put("szDocSoId", string_header_szDocSoId);
                params.put("szVehicleId", string_header_szVehicleId);

                params.put("szHelper1", string_header_szHelper1);
                params.put("bDirectWarehouse", string_header_bDirectWarehouse);
                params.put("szWarehouseId", string_header_szWarehouseId);
                params.put("szStockTypeId", string_header_szStockTypeId);
                params.put("szCustomerPO", string_header_szCustomerPO);

                params.put("dtmCustomerPO", string_header_dtmCustomerPO);
                params.put("szSalesId", string_header_szSalesId);
                params.put("szDocStockOutCustomerId", string_header_szDocStockOutCustomerId);
                params.put("szReturnFromId", string_header_szReturnFromId);
                params.put("szDescription", string_header_szDescription);

                params.put("szPromoDesc", string_header_szPromoDesc);
                params.put("intPrintedCount", string_header_intPrintedCount);
                params.put("szBranchId", string_header_szBranchId);
                params.put("szCompanyId", string_header_szCompanyId);
                params.put("szDocStatus", string_header_szDocStatus);

                params.put("szUserCreatedId", string_header_szUserCreatedId);
                params.put("szUserUpdatedId", string_header_szUserUpdatedId);
                params.put("dtmCreated", string_header_dtmCreated);
                params.put("dtmLastUpdated", string_header_dtmLastUpdated);
                params.put("dtmMobileTransaction", string_header_dtmMobileTransaction);

                params.put("szMobileId", string_header_szMobileId);
                params.put("szManualNo", string_header_szManualNo);
                params.put("szBkbId", string_header_szBkbId);

//                System.out.println("HASIL szDocId" + string_header_szDocId);
//                System.out.println("HASIL szCustomerId" + string_header_szCustomerId);
//                System.out.println("HASIL szEmployeeId" + string_header_szEmployeeId);
//                System.out.println("HASIL szDocSoId" + string_header_szDocSoId);
//                System.out.println("HASIL szVehicleId" + string_header_szVehicleId);
//                System.out.println("HASIL szHelper1" + string_header_szHelper1);
//                System.out.println("HASIL szHelper2" + string_header_szHelper2);
//                System.out.println("HASIL szSalesId" + string_header_szSalesId);
//                System.out.println("HASIL szBranchId" + string_header_szBranchId);
//                System.out.println("HASIL szCompanyId" + string_header_szCompanyId);
//                System.out.println("HASIL szDocStatus" + string_header_szDocStatus);
//                System.out.println("HASIL szBkbId" + string_header_szBkbId);

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
        RequestQueue requestQueue2 = Volley.newRequestQueue(mPenjualan_Terima_Produk.this);
        requestQueue2.add(stringRequest2);
    }

    //UNUTK POST KE TABLE HISTORY TOTAL HARGA DO, (FOOTER)
    private void post_historydrivertotalhargado() {

        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_mdba_history_driver_totalhargado",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        img2.setBackgroundColor(Color.parseColor("#FF74EF79"));

                        //Toast.makeText(mPenjualan_Terima_Produk.this, "OKE posthistorydriver total harga DO BERHASIL", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                img2.setBackgroundColor(Color.parseColor("#E91E63"));

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

                params.put("nodok_DO", string_footer_nodok_DO);
                params.put("nodok_SO", string_footer_nodok_SO);
                params.put("ppn", string_footer_ppn);
                params.put("dpp", string_footer_dpp);
                params.put("totaldiskon", string_footer_totaldiskon);
                params.put("totalharga", string_footer_totalharga);
                params.put("depo", string_footer_depo);

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
        RequestQueue requestQueue2 = Volley.newRequestQueue(mPenjualan_Terima_Produk.this);
        requestQueue2.add(stringRequest2);
    }

    //UNTUK POST KE TABLE HISTORY DRIVER DOCDO ITEM
//    private void posthistorydriverdocdoitem() {
//
//        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_mdba_history_driver_docdoitem",
//                new Response.Listener<String>() {
//
//                    @Override
//                    public void onResponse(String response) {
//                        Toast.makeText(mPenjualan_Terima_Produk.this, "OKE posthistorydriverdocdoitem BERHASIL", Toast.LENGTH_SHORT).show();
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//
//        }) {
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
//                sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
//
//                params.put("szDocId", string_docdoitem_szDocId);
//                params.put("intItemNumber", string_docdoitem_intItemNumber);
//                params.put("szProductId", string_docdoitem_szProductId);
//                params.put("szOrderItemTypeId", string_docdoitem_szOrderItemTypeId);
//                params.put("szTrnType", string_docdoitem_szTrnType);
//                params.put("decQty", string_docdoitem_decQty);
//                params.put("szUomId", string_docdoitem_szUomId);
//
//                return params;
//            }
//
//        };
//        stringRequest2.setRetryPolicy(
//                new DefaultRetryPolicy(
//                        500000,
//                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
//                )
//        );
//        RequestQueue requestQueue2 = Volley.newRequestQueue(mPenjualan_Terima_Produk.this);
//        requestQueue2.add(stringRequest2);
//    }

    private void posthistorydriverdocdoitem() {

        for(int i = 0;i <=  data_docdoitem_pojos.size() - 1 ;i++) {

            int finalI = i;
            int finalI1 = i;

            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_mdba_history_driver_docdoitem",
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            img3.setBackgroundColor(Color.parseColor("#FF74EF79"));

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    img3.setBackgroundColor(Color.parseColor("#E91E63"));
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

                    params.put("szDocId", adapter_docdoitem.getItem(finalI1).getSzDocId());
                    params.put("intItemNumber", adapter_docdoitem.getItem(finalI1).getSzDointItemNumbercId());
                    params.put("szProductId", adapter_docdoitem.getItem(finalI1).getSzProductId());
                    params.put("szOrderItemTypeId", adapter_docdoitem.getItem(finalI1).getSzOrderItemTypeId());
                    params.put("szTrnType", adapter_docdoitem.getItem(finalI1).getSzTrnType());
                    params.put("decQty", adapter_docdoitem.getItem(finalI1).getDecQty());
                    params.put("szUomId", adapter_docdoitem.getItem(finalI1).getSzUomId());


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
            RequestQueue requestQueue2 = Volley.newRequestQueue(mPenjualan_Terima_Produk.this);
            requestQueue2.add(stringRequest2);
        }
    }

    private void posthistorydriverdocdoitemprice() {

        for(int i = 0;i <=  data_docdoitemprice_pojos.size() - 1 ;i++) {
            int finalI = i;
            int finalI1 = i;
            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_mdba_history_driver_docdoitemprice",
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            img4.setBackgroundColor(Color.parseColor("#FF74EF79"));

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    img4.setBackgroundColor(Color.parseColor("#E91E63"));
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

                    params.put("szDocId", adapter_docdoitemprice.getItem(finalI1).getSzDocId());
                    params.put("intItemNumber", adapter_docdoitemprice.getItem(finalI1).getIntItemNumber());
                    params.put("intItemDetailNumber", adapter_docdoitemprice.getItem(finalI1).getIntItemDetailNumber());
                    params.put("szPriceId", adapter_docdoitemprice.getItem(finalI1).getSzPriceId());
                    params.put("decPrice", adapter_docdoitemprice.getItem(finalI1).getDecPrice());
                    params.put("decDiscount", adapter_docdoitemprice.getItem(finalI1).getDecDiscount());
                    params.put("bTaxable", adapter_docdoitemprice.getItem(finalI1).getbTaxable());
                    params.put("decAmount", adapter_docdoitemprice.getItem(finalI1).getDecAmount());
                    params.put("decTax", adapter_docdoitemprice.getItem(finalI1).getDecTax());
                    params.put("decDpp", adapter_docdoitemprice.getItem(finalI1).getDecDpp());
                    params.put("szTaxId", adapter_docdoitemprice.getItem(finalI1).getSzTaxId());
                    params.put("decTaxRate", adapter_docdoitemprice.getItem(finalI1).getDecTaxRate());
                    params.put("decDiscPrinciple", adapter_docdoitemprice.getItem(finalI1).getDecDiscPrinciple());
                    params.put("decDiscDistributor", adapter_docdoitemprice.getItem(finalI1).getDecDiscDistributor());
                    params.put("decDiscInternal", adapter_docdoitemprice.getItem(finalI1).getDecDiscInternal());


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
            RequestQueue requestQueue2 = Volley.newRequestQueue(mPenjualan_Terima_Produk.this);
            requestQueue2.add(stringRequest2);
        }
    }

    //UNTUK POST KE TABLE MDBA HISTORY TERIMA PRODUK
    private void post_historydriverterimaproduk() {

        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_mdba_history_driver_terimaproduk",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        img5.setBackgroundColor(Color.parseColor("#FF74EF79"));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                img5.setBackgroundColor(Color.parseColor("#E91E63"));
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

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                String currentDateandTime = sdf.format(new Date());

                params.put("dtmDoc", currentDateandTime); //tanggal kapan input, tanggal sekarang                               [ok]
                params.put("nomor_do", nomor_do); //nomor DO (DELIVERY ORDER)                                                   [ok]
                params.put("nomor_bkb", string_header_szBkbId); //nomor BKB                                                     [ok]
                params.put("nomor_std", string_header_idstd); //nomr STD (SURAT TUGAS DISTIRBUSI)                               [ok]

                params.put("ritase", string_header_ritase); //ritase                                                            [ok]
                params.put("driver", string_header_szEmployeeId); //driver by employee                                          [ok]
                params.put("branchId", string_header_szBranchId); //branch                                                      [ok]
                params.put("companyId", string_header_szCompanyId); //company                                                   [ok]

                params.put("ppn", tv_ppn.getText().toString());
                params.put("dpp", tv_dpp.getText().toString());
                params.put("total_diskon", tv_totaldiskon.getText().toString());
                params.put("total_harga", tv_totalharga.getText().toString());

                params.put("customerId", string_header_szCustomerId); //id customer toko pelanggan                              [ok]
                params.put("foto_customer", tv_tanggal.getText().toString()); //tanggal foto pelanggan                          [ok]
                params.put("alamat_foto_customer", tv_alamat.getText().toString()); //alamat pada saat foto toko/pelanggan      [ok]
                params.put("longlat_foto_customer", tv_longlat.getText().toString()); //longlat pada saat foto toko/pelanggan   [ok]
                params.put("pembayaran", pilihpembayaran.getText().toString()); //pembayaran yang digunakan, saat ini tunai     [ok]

                params.put("tanggal_terima_produk", currentDateandTime); //tanggal menerima produk, tanggal sekaranag                  [ok]
                params.put("catatan", catatan.getText().toString()); //catatan yang dicatat si driver                           [ok]
                params.put("selesai_kunjungan", "0"); //selesai terima produk
                params.put("selesai_terima_produk", "1"); //selesai terima produk

                params.put("jenis_pembayaran", String.valueOf(jenis_pembayaran));

                params.put("pembayaran_tunai", edt_jumlah_tunai.getText().toString());
                params.put("pembayaran_transfer", edt_jumlah_transfer.getText().toString());
                params.put("pembayaran_total", total_jenis_pembayaran.getText().toString());

                params.put("foto_bukti_transfer", tv_tanggal.getText().toString()); //foto bukti transfer

//                if (chk_tunai.isChecked() && !chk_transfer.isChecked()) {
//
//                    if (edt_jumlah_tunai.getText().toString().equals("")) {
//                        txt_input_jumlah_tunai.setError("Jumlah tunai tidak boleh kosong");
//
//                    } else {
//                        int jumlah_tunai = Integer.parseInt(edt_jumlah_tunai.getText().toString());
//                        String string_jumlah_tunai = String.valueOf(jumlah_tunai);
//                        Toast.makeText(mPenjualan_Terima_Produk.this, "jumlah tunai = " + string_jumlah_tunai, Toast.LENGTH_SHORT).show();
//
//                        jenis_pembayaran = 1;
//
//                        params.put("jenis_pembayaran", "1");
//                        params.put("pembayaran_tunai", string_jumlah_tunai);
//                        params.put("pembayaran_transfer", "");
//                        params.put("pembayaran_total", string_jumlah_tunai);
//
//                        System.out.println("TUNAI NIH = " + "pembayaran = " + string_jumlah_tunai + "total = " + string_jumlah_tunai);
//
//                    }
//
//                } else if (!chk_tunai.isChecked() && chk_transfer.isChecked()) {
//
//                    if (edt_jumlah_transfer.getText().toString().equals("")) {
//                        txt_input_jumlah_transfer.setError("Jumlah transfer tidak boleh kosong");
//
//                    } else {
//                        int jumlah_transfer = Integer.parseInt(edt_jumlah_transfer.getText().toString());
//                        String string_jumlah_transfer = String.valueOf(jumlah_transfer);
//                        Toast.makeText(mPenjualan_Terima_Produk.this, "jumlah transfeer = " + string_jumlah_transfer, Toast.LENGTH_SHORT).show();
//
//                        jenis_pembayaran = 2;
//
//                        params.put("jenis_pembayaran", "2");
//                        params.put("pembayaran_tunai", "");
//                        params.put("pembayaran_transfer", string_jumlah_transfer);
//                        params.put("pembayaran_total", string_jumlah_transfer);
//
//                        System.out.println("TRANSFER NIH = " + "pembayaran = " + string_jumlah_transfer + "total = " + string_jumlah_transfer);
//
//
//                    }
//
//                } else if (chk_tunai.isChecked() && chk_transfer.isChecked()) {
//
//                    if (edt_jumlah_tunai.getText().toString().equals("") && edt_jumlah_transfer.getText().toString().equals("")) {
//                        txt_input_jumlah_tunai.setError("Jumlah tunai & transfer tidak boleh kosong");
//                        txt_input_jumlah_transfer.setError("Jumlah tunai & transfer tidak boleh kosong");
//
//                    } else if (!edt_jumlah_tunai.getText().toString().equals("") && edt_jumlah_transfer.getText().toString().equals("")) {
//                        txt_input_jumlah_transfer.setError("Jumlah transfer tidak boleh kosong");
//
//                    } else if (edt_jumlah_tunai.getText().toString().equals("") && !edt_jumlah_transfer.getText().toString().equals("")) {
//                        txt_input_jumlah_tunai.setError("Jumlah tunai tidak boleh kosong");
//
//                    } else {
//
//                        int jumlah_tunai = Integer.parseInt(edt_jumlah_tunai.getText().toString());
//                        int jumlah_transfer = Integer.parseInt(edt_jumlah_transfer.getText().toString());
//
//                        String total = String.valueOf(jumlah_tunai + jumlah_transfer);
//
//                        jenis_pembayaran = 3;
//
//                        params.put("jenis_pembayaran", "3");
//                        params.put("pembayaran_tunai", String.valueOf(jumlah_tunai));
//                        params.put("pembayaran_transfer", String.valueOf(jumlah_transfer));
//                        params.put("pembayaran_total", total);
//
//                        System.out.println("TRANSFER dan TUNAI NIH = " + "pembayaran tunai = " + String.valueOf(jumlah_tunai) + "pembayaran transfer = " + String.valueOf(jumlah_transfer) + "total = " + total);
//
//                    }
//                }
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
        RequestQueue requestQueue2 = Volley.newRequestQueue(mPenjualan_Terima_Produk.this);
        requestQueue2.add(stringRequest2);
    }

    //UNTUK POST KE TABLE MDBA HISTORY TERIMA PRODUK ITEM
    private void post_historydriverterimaprodukitem() {

        for(int i = 0;i <=  data_terima_produk_pojos.size() - 1 ;i++) {

            int finalI = i;
            int finalI1 = i;

            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_mdba_history_driver_terimaprodukitem",
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            img6.setBackgroundColor(Color.parseColor("#FF74EF79"));
                            //Toast.makeText(mPenjualan_Terima_Produk.this, "OKE BERHASIL", Toast.LENGTH_SHORT).show();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    img6.setBackgroundColor(Color.parseColor("#E91E63"));

                    System.out.println("DISKON TIV " + adapter.getItem(finalI1).getDiskon_tiv());
                    System.out.println("DISKON DISTRIBUTOR " + adapter.getItem(finalI1).getDiskon_distributor());
                    System.out.println("DISKON INTERNAL " + adapter.getItem(finalI1).getDiskon_internal());

                    //Toast.makeText(getApplicationContext(), "Terjadi kesalahan saat submit", Toast.LENGTH_SHORT).show();

                    System.out.println("Nama Error : ");

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

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                    String currentDateandTime = sdf.format(new Date());

                    params.put("dtmDoc", currentDateandTime); //tanggal kapan input, tanggal sekarang                                      [ok]
                    params.put("nomor_do", nomor_do); //nomor DO (DELIVERY ORDER)                                                   [ok]
                    params.put("nomor_bkb", string_header_szBkbId); //nomor BKB                                                     [ok]
                    params.put("nomor_std", string_header_idstd); //nomr STD (SURAT TUGAS DISTIRBUSI)                               [ok]

                    params.put("intItemNumber", adapter.getItem(finalI1).getIntItemNumber());
                    params.put("szProductId", adapter.getItem(finalI1).getNama_produk());
                    params.put("szOrderItemTypeId", adapter.getItem(finalI1).getSzOrderItemTypeId());
                    params.put("decQty", adapter.getItem(finalI1).getQty_produk());
                    params.put("szUomId", adapter.getItem(finalI1).getSzUomId());
                    params.put("decPrice", adapter.getItem(finalI1).getHarga_satuan());

                    params.put("decDiscount", "0");
                    params.put("bTaxable", adapter.getItem(finalI1).getbTaxable());
                    params.put("decAmount", adapter.getItem(finalI1).getTotalhargarow()); //total harga produk per row

                    params.put("decTax", adapter.getItem(finalI1).getDecTax());
                    params.put("decDpp", adapter.getItem(finalI1).getDecDpp());

                    params.put("szTaxId", adapter.getItem(finalI1).getSzTaxId());
                    params.put("decTaxRate", adapter.getItem(finalI1).getDecTaxRate());

                    params.put("decDiscPrinciple",adapter.getItem(finalI1).getDiskon_tiv());
                    params.put("decDiscDistributor", adapter.getItem(finalI1).getDiskon_distributor());
                    params.put("decDiscInternal", adapter.getItem(finalI1).getDiskon_internal());

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
            RequestQueue requestQueue2 = Volley.newRequestQueue(mPenjualan_Terima_Produk.this);
            requestQueue2.add(stringRequest2);
        }
    }

    //-----------------------UPDATE KE TABLE DMS DAN MDBA KETIKA AADA PERUBAHAN QTY-------------------

    //UNTUK UPDATE KE TABLE DMS DAN MDBA DOCDO
    private void update_docdo_driver_dms_mdba(){

        StringRequest stringRequest2 = new StringRequest(Request.Method.PUT, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_docdo_driver",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        img7.setBackgroundColor(Color.parseColor("#FF74EF79"));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        img7.setBackgroundColor(Color.parseColor("#E91E63"));
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

                //NANTI DITAMBAH
                params.put("szDocId", delivery_order.getText().toString());
                params.put("szCustomerId", tv_no_customer.getText().toString());
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
        RequestQueue requestQueue2 = Volley.newRequestQueue(mPenjualan_Terima_Produk.this);
        requestQueue2.add(stringRequest2);
    }

    //UNTUK UPDATE KE TABLE DMS DAN MDBA DOCDO
    private void update_docdoitem_driver_dms_mdba(){

        for(int i = 0;i <=  data_terima_produk_pojos.size() - 1 ;i++) {

            int finalI = i;
            int finalI1 = i;

            StringRequest stringRequest2 = new StringRequest(Request.Method.PUT, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_docdoitem_driver",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            img8.setBackgroundColor(Color.parseColor("#FF74EF79"));
                            //Toast.makeText(mPenjualan_Terima_Produk.this, "OKE BERHASIL", Toast.LENGTH_SHORT).show();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    img8.setBackgroundColor(Color.parseColor("#E91E63"));
                    //Toast.makeText(getApplicationContext(), "Terjadi kesalahan saat submit", Toast.LENGTH_SHORT).show();

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

                    params.put("szDocId", delivery_order.getText().toString());
                    params.put("intItemNumber", adapter.getItem(finalI1).getIntItemNumber());
                    params.put("decQty", adapter.getItem(finalI1).getQty_produk()); //BUG PADA SAAT DI SUBMIT, qty yanng masuk sama semua

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
            RequestQueue requestQueue2 = Volley.newRequestQueue(mPenjualan_Terima_Produk.this);
            requestQueue2.add(stringRequest2);
        }

    }

    //UNTUK UPDATE KE TABLE DMS DAN MDBA DOCDO
    private void update_docdoitemprice_driver_dms_mdba(){

        for(int i = 0;i <=  data_terima_produk_pojos.size() - 1 ;i++) {

            int finalI = i;
            int finalI1 = i;

            StringRequest stringRequest2 = new StringRequest(Request.Method.PUT, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_docdoitemprice_driver",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            img9.setBackgroundColor(Color.parseColor("#FF74EF79"));
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    img9.setBackgroundColor(Color.parseColor("#E91E63"));
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

                    params.put("szDocId", delivery_order.getText().toString());
                    params.put("intItemNumber", adapter.getItem(finalI1).getIntItemNumber());

                    params.put("decPrice", adapter.getItem(finalI1).getHarga_satuan());
                    params.put("decAmount", adapter.getItem(finalI1).getTotalhargarow());
                    params.put("decTax", adapter.getItem(finalI1).getDecTax());
                    params.put("decDpp", adapter.getItem(finalI1).getDecDpp());

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
            RequestQueue requestQueue2 = Volley.newRequestQueue(mPenjualan_Terima_Produk.this);
            requestQueue2.add(stringRequest2);
        }

    }

    //UNTUK UPDATE KE TABLE DMS DAN MDBA DOCDO
    private void update_totalhargado_mdba(){

        StringRequest stringRequest2 = new StringRequest(Request.Method.PUT, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_mdbatotalhargado_driver",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        img10.setBackgroundColor(Color.parseColor("#FF74EF79"));
                        bt_submit_berhasil.setVisibility(View.VISIBLE);
                        tv_submit_loading.setText("Berhasil submit");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        img10.setBackgroundColor(Color.parseColor("#E91E63"));
                        bt_submit_berhasil.setVisibility(View.VISIBLE);

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

                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String currentDateandTime2 = sdf2.format(new Date());

                //NANTI DITAMBAH
                params.put("nodok_DO", string_footer_nodok_DO);
                params.put("ppn", tv_ppn.getText().toString());
                params.put("dpp", tv_dpp.getText().toString());
                params.put("totalharga", tv_totalharga.getText().toString());

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
        RequestQueue requestQueue2 = Volley.newRequestQueue(mPenjualan_Terima_Produk.this);
        requestQueue2.add(stringRequest2);
    }

    //-----------------------------------------------PRINT DO----------------------------------------
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void doPrint() {
        try {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH}, mPenjualan_Terima_Produk.PERMISSION_BLUETOOTH);
            } else {
                BluetoothConnection connection = BluetoothPrintersConnections.selectFirstPaired();
                if (connection != null) {
                    EscPosPrinter printer = new EscPosPrinter(connection, 203, 48f, 32);
                    final String text = "[C]<img>" + PrinterTextParserImg.bitmapToHexadecimalString(printer,
                            this.getApplicationContext().getResources().getDrawableForDensity(R.drawable.cart,
                                    DisplayMetrics.DENSITY_LOW, getTheme())) + "</img>\n" +
                            "[L]\n" +
                            "[L]" + df.format(new Date()) + "\n" +
                            "[C]================================\n" +
                            "[L]<b>Aqua Galon</b>\n" +
                            "[L]    10 pcs[R]" + nf.format(25000) + "\n" +
                            "[L]<b>Aqua Tissue</b>\n" +
                            "[L]    10 pcs[R]" + nf.format(45000) + "\n" +
                            "[L]<b>Vit Galon</b>\n" +
                            "[L]    10 pcs[R]" + nf.format(20000) + "\n" +
                            "[C]--------------------------------\n" +
                            "[L]TOTAL[R]" + nf.format(90000) + "\n" +
                            "[L]DISCOUNT 15%[R]" + nf.format(13500) + "\n" +
                            "[L]TAX 10%[R]" + nf.format(7650) + "\n" +
                            "[L]<b>GRAND TOTAL[R]" + nf.format(84150) + "</b>\n" +
                            "[C]--------------------------------\n" +
                            "[C]<barcode type='ean13' height='10'>202105160005</barcode>\n" +
                            "[C]--------------------------------\n" +
                            "[C]Thanks For Shopping\n" +
                            "[C]https://kodejava.org\n" +
                            "[L]\n" +
                            "[L]<qrcode>https://kodejava.org</qrcode>\n";

                    printer.printFormattedText(text);
                } else {
                    Toast.makeText(this, "No printer was connected!", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            Log.e("APP", "Can't print", e);
        }
    }

    public void history_terima_produk(){

        StringRequest rest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_historyterimaproduk?customerId="+tv_no_customer.getText().toString()+"&nomor_std="+tv_no_std.getText().toString(),

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status").equals("true")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                    String string_terima_produk = jsonObject1.getString("selesai_terima_produk");

                                    tv_terima_produk.setText(string_terima_produk);

                                    //Toast.makeText(mPenjualan_Terima_Produk.this, "DAPET", Toast.LENGTH_SHORT).show();

                                    if (tv_terima_produk.getText().toString().equals("1")){

                                        pDialog = new SweetAlertDialog(mPenjualan_Terima_Produk.this, SweetAlertDialog.WARNING_TYPE);
                                        pDialog.setTitleText("Produk sudah diterima pelanggan");
                                        pDialog.setConfirmText("OK");
                                        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {

                                                pDialog.dismissWithAnimation();
                                                adapter.clear();
                                                finish();
                                            }
                                        });
                                        pDialog.setCancelable(false);
                                        pDialog.show();

                                    } else if (tv_terima_produk.getText().toString().equals("0")) {

                                        //Toast.makeText(mPenjualan_Terima_Produk.this, "OK", Toast.LENGTH_SHORT).show();
                                    }


                                }

                            } else if (jsonObject.getString("status").equals("false")){

                                tv_terima_produk.setText("0");

                                //Toast.makeText(mPenjualan_Terima_Produk.this, "ENGGA", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("customerId", tv_no_customer.getText().toString());
                params.put("nomor_std", tv_no_std.getText().toString());

                return params;
            }
        };
        rest.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestkota = Volley.newRequestQueue(mPenjualan_Terima_Produk.this);
        requestkota.add(rest);

    }

    public void upload_foto() {

        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://apisec.tvip.co.id/mobile_eis_2/upload_sfa.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Toast.makeText(getApplicationContext(), "Upload Foto Berhasil", Toast.LENGTH_SHORT).show();
                        update_foto_jika_berhasil_response();
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

                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
                String currentDateandTime2 = sdf2.format(new Date());

                String gambar = ImageToString(bitmap);

                params.put("nik", "IMG-TerimaProduk" + "_" + nik_baru + "_" + "no_cust-"+tv_no_customer.getText().toString() + "_" + "no_do-"+delivery_order.getText().toString() + "_" + currentDateandTime2);
                params.put("nama_folder", "foto_driver_terima_produk");
                params.put("foto", gambar);


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

        RequestQueue requestQueue2 = Volley.newRequestQueue(this);
        requestQueue2.getCache().clear();
        requestQueue2.add(stringRequest2);
    }

    public void upload_foto_canvaser() {

        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://apisec.tvip.co.id/mobile_eis_2/upload_sfa.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Toast.makeText(getApplicationContext(), "Upload Foto Berhasil", Toast.LENGTH_SHORT).show();
                        update_foto_bukti_transfer_jika_berhasil_response();
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

                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
                String currentDateandTime2 = sdf2.format(new Date());

                String gambar = ImageToString_buktiTransfer(bitmap2);

                params.put("nik", "IMG-BuktiTransfer" + "_" + nik_baru + "_" + "no_cust-"+tv_no_customer.getText().toString() + "_" + "no_do-"+delivery_order.getText().toString() + "_" + currentDateandTime2);
                params.put("nama_folder", "foto_driver_bukti_transfer");
                params.put("foto", gambar);

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

        RequestQueue requestQueue2 = Volley.newRequestQueue(this);
        requestQueue2.getCache().clear();
        requestQueue2.add(stringRequest2);
    }

    //akan update url ke database history terima prooduk
    public void update_foto_jika_berhasil_response() {

        StringRequest stringRequest2 = new StringRequest(Request.Method.PUT, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_foto_customerterimaproduk_driver",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Toast.makeText(getApplicationContext(), "Upload Foto Berhasil", Toast.LENGTH_SHORT).show();

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

                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
                String currentDateandTime2 = sdf2.format(new Date());

                params.put("nomor_do", delivery_order.getText().toString());
                params.put("foto_customer", "https://apisec.tvip.co.id/image_sfa_apps/foto_driver_terima_produk/IMG-TerimaProduk" + "_" + nik_baru + "_" + "no_cust-"+tv_no_customer.getText().toString() + "_" + "no_do-"+delivery_order.getText().toString() + "_" + currentDateandTime2+".jpeg");

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

        RequestQueue requestQueue2 = Volley.newRequestQueue(this);
        requestQueue2.getCache().clear();
        requestQueue2.add(stringRequest2);
    }

    //akan update url ke database history terima prooduk
    public void update_foto_bukti_transfer_jika_berhasil_response() {

        StringRequest stringRequest2 = new StringRequest(Request.Method.PUT, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_foto_buktitransfer_driver",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Toast.makeText(getApplicationContext(), "Upload Foto Berhasil", Toast.LENGTH_SHORT).show();

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

                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
                String currentDateandTime2 = sdf2.format(new Date());

                params.put("nomor_do", delivery_order.getText().toString());
                params.put("foto_bukti_transfer", "https://apisec.tvip.co.id/image_sfa_apps/foto_driver_bukti_transfer/IMG-BuktiTransfer" + "_" + nik_baru + "_" + "no_cust-"+tv_no_customer.getText().toString() + "_" + "no_do-"+delivery_order.getText().toString() + "_" + currentDateandTime2+".jpeg");

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

        RequestQueue requestQueue2 = Volley.newRequestQueue(this);
        requestQueue2.getCache().clear();
        requestQueue2.add(stringRequest2);
    }


    private String formatRupiah(Double number){
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        return formatRupiah.format(number);
    }

    @Override
    public void onBackPressed() {
        adapter.clear();
        finish();
        super.onBackPressed();
    }
}