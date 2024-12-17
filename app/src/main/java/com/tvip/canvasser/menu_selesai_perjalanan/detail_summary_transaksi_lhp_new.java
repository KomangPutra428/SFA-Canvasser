package com.tvip.canvasser.menu_selesai_perjalanan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputLayout;
import com.tvip.canvasser.R;
import com.tvip.canvasser.menu_mulai_perjalanan.Utility;
import com.tvip.canvasser.menu_utama.MainActivity;
import com.tvip.canvasser.pojo.data_lhp_do_pojo;
import com.tvip.canvasser.pojo.data_setor_lhp_new_pojo;
import com.tvip.canvasser.pojo.data_sum_lhp_pojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class detail_summary_transaksi_lhp_new extends AppCompatActivity {

    ListView list_parent_lhp_by_std, list_total_pembayaran_lhp;

    List<data_lhp_do_pojo> data_lhp_do_pojos = new ArrayList<>();
    detail_summary_transaksi_lhp_new.ListViewAdapterSummaryTransaksiParent adapter_parent;

    List<data_setor_lhp_new_pojo> data_setor_lhp_new_pojos = new ArrayList<>();

    List<data_sum_lhp_pojo> data_sum_lhp_pojos = new ArrayList<>();
    ListViewAdapterSumTransaksi adapterSumTransaksi;

    String nomor_do;
    String nomor_std;
    static String ritase;
    String szBkbId ;
    SharedPreferences sharedPreferences;
    TextView tv_id_driver, tv_name_driver, tv_nopol;

    String sumtransfer, sumtunai;

    //TEXTVIEW FOOTER
    TextView  tv_sumtotal, tv_sumamount,tv_sumfixtotal, tv_sumfixtotal_non_format_rupiah;
    EditText edt_setor_mesin, edt_setor_tunai, edt_setor_transfer;
    TextInputLayout txt_input_setor_tunai, txt_input_jumlah_mesin, txt_input_setor_transfer;
    String sumtotalstr;

    SweetAlertDialog pDialog;
    //checkbox

    CheckBox chk_kredit, chk_mesin_setor, chk_setor_tunai, chk_setor_transfer;
    TextView tv_setor_tunai, tv_setor_mesin, tv_setor_transfer, tv_total_setor,
            tv_total_setor_text, tv_total_stor_non_format_rupiah, tv_dialog_total_setoran_non_rupiah,
            tv_dialog_total_setoran, tv_dialog_total_tunai, tv_dialog_total_transfer;
    Button btn_setor, btn_confirm_setor, bt_detail_bottom_sheet, bt_rekapan_produk;
    LinearLayout wrap_chck_tf, wrap_chck_tunai_mesin, wrape_layout_setor;

    int jenis_pembayaran_transfer = 0;

    String dtmdoc, id_driver,string_status_pembayaran, total_pembayaran_tunai, string_ritase,  string_statuslhp,
            total_pembayaran_mesin, total_pembayaran_customer, customerId, string_nomor_std, string_nomor_bkb, string_nomor_do, employee_id,
            string_jenis_pembayaran_transfer, total_pembayaran_transfer, idbranch_driver;

    BottomSheetBehavior sheetBehavior;
    BottomSheetDialog sheetDialog;
    View bottom_sheet, view;
    LottieAnimationView lottie_lhp_success;

    RelativeLayout relative_loading, relative_background_succes, relative_selesai_ritase;
    Button bt_lhp_success;
    String no_std;

    Dialog dialogstatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_summary_transaksi_lhp_new);

        list_parent_lhp_by_std = findViewById(R.id.list_parent_lhp_by_std);
        list_total_pembayaran_lhp = findViewById(R.id.list_total_pembayaran_lhp);

        tv_id_driver = findViewById(R.id.tv_id_driver);
        tv_name_driver = findViewById(R.id.tv_name_driver);
        tv_nopol = findViewById(R.id.tv_nopol);

        tv_sumfixtotal = findViewById(R.id.tv_sumfixtotal);
        tv_sumfixtotal_non_format_rupiah = findViewById(R.id.tv_sumfixtotal_non_format_rupiah);

        tv_sumamount = findViewById(R.id.tv_sumamount);
        tv_sumtotal =  findViewById(R.id.tv_sumtotal);

        relative_loading = findViewById(R.id.relative_background_loading);
        lottie_lhp_success = findViewById(R.id.lottie_lhp_success);
        relative_background_succes = findViewById(R.id.relative_background_succes);
        relative_selesai_ritase = findViewById(R.id.relative_selesai_ritase);

        bt_lhp_success = findViewById(R.id.bt_lhp_success);
        bt_lhp_success.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                relative_background_succes.setVisibility(View.GONE);
                get_cek_lhp();

                Intent i = new Intent(getBaseContext(), MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Call Only, if you wants to clears the activity stack else ignore it.
                startActivity(i);
                finish();
                System.gc();

            }
        });

        wrap_chck_tf = findViewById(R.id.wrap_chck_tf);
        wrap_chck_tunai_mesin = findViewById(R.id.wrap_chck_tunai_mesin);
        wrape_layout_setor = findViewById(R.id.wrape_layout_setor);
        bottom_sheet = findViewById(R.id.bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(bottom_sheet);
        bt_detail_bottom_sheet = findViewById(R.id.bt_setor_sekarang_bottom_dialog);

        nomor_do = getIntent().getStringExtra("nomordo");
        nomor_std = getIntent().getStringExtra("nomorstd");
        ritase = getIntent().getStringExtra("ritase");
        szBkbId = getIntent().getStringExtra("nobkb");

        string_nomor_bkb = getIntent().getStringExtra("nomor_bkb");
        string_nomor_do = getIntent().getStringExtra("nomor_do");
        string_nomor_std = getIntent().getStringExtra("nomorstd");
        customerId = getIntent().getStringExtra("customerId");
        total_pembayaran_customer = getIntent().getStringExtra("total_pembayaran_customer");
        string_status_pembayaran = getIntent().getStringExtra("status_pembayaran");
        total_pembayaran_tunai = getIntent().getStringExtra("total_pembayaran_tunai");
        total_pembayaran_mesin = getIntent().getStringExtra("total_pembayaran_mesin");
        total_pembayaran_transfer = getIntent().getStringExtra("total_pembayaran_transfer");
        string_ritase = getIntent().getStringExtra("ritase");
        string_statuslhp = getIntent().getStringExtra("statuslhp");

        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        employee_id = sharedPreferences.getString("szDocCall", null);
        String[] nikdriver = employee_id.split("-");
        idbranch_driver = nikdriver[0];

        bt_rekapan_produk = findViewById(R.id.bt_rekapan_produk);
        bt_rekapan_produk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getBaseContext(), Rekapan_Produk_LHP.class);
                i.putExtra("nomorstd", no_std);
                i.putExtra("bkb", szBkbId);

                startActivity(i);

            }
        });

        pDialog = new SweetAlertDialog(detail_summary_transaksi_lhp_new.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Harap Menunggu");
        pDialog.setCancelable(false);
        pDialog.show();

        get_lhp_by_std_parent();
        get_cek_lhp();

        //getSetorLHP();

    }
    private void get_cek_lhp() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_ceksetoranlhp?nomor_std="+ nomor_std + "&branch=" + idbranch_driver + "&id_driver=" + employee_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        bt_detail_bottom_sheet.setVisibility(View.GONE);
//                        relative_selesai_ritase.setVisibility(View.VISIBLE);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        relative_selesai_ritase.setVisibility(View.GONE);

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


    //DISINI AKAN MENAMPILKAN NOIMOR DO, ID TOKO, NAMA CUSTOMER
    private void get_lhp_by_std_parent() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Selesai_Perjalanan/index_detail_getSTD?nomor_std="+ nomor_std + "&ritase=" + ritase + "&branchId=" + idbranch_driver,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        getSUmTotal();

                        try {
                            int number = 0;
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");
                            for (int i = 0; i < movieArray.length(); i++) {
                                final JSONObject movieObject = movieArray.getJSONObject(i);
                                final data_lhp_do_pojo movieItem = new data_lhp_do_pojo(

                                        movieObject.getString("nomor_do"),
                                        movieObject.getString("id_toko"),
                                        movieObject.getString("nama_toko"),
                                        movieObject.getString("nomor_std"),
                                        movieObject.getString("nomor_bkb"),
                                        movieObject.getString("ritase"),
                                        movieObject.getString("branchId"));

                                no_std = movieObject.getString("nomor_std");

                                data_lhp_do_pojos.add(movieItem);
                            }


                            adapter_parent = new ListViewAdapterSummaryTransaksiParent(data_lhp_do_pojos, getApplicationContext());
                            list_parent_lhp_by_std.setAdapter(adapter_parent);
                            adapter_parent.notifyDataSetChanged();




//                            pDialog.dismissWithAnimation();

                        } catch(JSONException e){
                            e.printStackTrace();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismissWithAnimation();
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

    public class ListViewAdapterSummaryTransaksiParent extends ArrayAdapter<data_lhp_do_pojo> {

        private class ViewHolder {
            TextView nomor_do, id_toko, nama_toko, tv_sub_total_row, tv_sumamount,
                    string_statuslhp,
                    tv_sumtotal, tv_sumdiskon, tv_sumfixtotal, tv_nomor_std, tv_nomor_bkb,
                    tv_branch, tv_nomor_ritase;

            TextView tv_sumtotal_non_rupiah, tv_sumdiskon_non_rupiah, tv_sumamount_non_rupiah;
            ImageView down, up;
            LinearLayout listproduk_layout;
            ListView listproduk;

            ListViewAdapterSetorLHP adapter;
            List<data_setor_lhp_new_pojo> data_setor_lhp_new_pojos = new ArrayList<>();

        }

        List<data_lhp_do_pojo> data_lhp_do_pojos;
        private Context context;

        public ListViewAdapterSummaryTransaksiParent(List<data_lhp_do_pojo> data_lhp_do_pojos, Context context) {
            super(context, R.layout.list_detail_do_lhp, data_lhp_do_pojos);
            this.data_lhp_do_pojos = data_lhp_do_pojos;
            this.context = context;

        }

        public int getCount() {
            return data_lhp_do_pojos.size();
        }

        public data_lhp_do_pojo getItem(int position) {
            return data_lhp_do_pojos.get(position);
        }

        @Override
        public int getViewTypeCount() {
            return getCount();
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
            data_lhp_do_pojo movieItem = data_lhp_do_pojos.get(position);

//            if (movieItem.getStatuslhp().equals("1")) {
//                wrap_chck_tf.setVisibility(View.GONE);
//                wrap_chck_tunai_mesin.setVisibility(View.GONE);
//                wrape_layout_setor.setVisibility(View.GONE);
//
//            } else {
//                wrap_chck_tf.setVisibility(View.VISIBLE);
//                wrap_chck_tunai_mesin.setVisibility(View.VISIBLE);
//                wrape_layout_setor.setVisibility(View.VISIBLE);
//                Toast.makeText(detail_summary_transaksi_lhp_new.this, "GAGAL DISABLE", Toast.LENGTH_SHORT).show();
//            }

            if (convertView == null) {

                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.list_detail_do_lhp, parent, false);

                viewHolder.nomor_do = (TextView) convertView.findViewById(R.id.tv_nomor_do);
                viewHolder.id_toko = (TextView) convertView.findViewById(R.id.tv_id_toko);
                viewHolder.nama_toko = (TextView) convertView.findViewById(R.id.tv_nama_toko);
                viewHolder.tv_nomor_std = (TextView) convertView.findViewById(R.id.tv_nomor_std);

//                viewHolder.tv_nomor_bkb = (TextView) convertView.findViewById(R.id.tv_nomor_bkb);
//                viewHolder.tv_nomor_ritase = (TextView) convertView.findViewById(R.id.tv_nomor_ritase);
//                viewHolder.tv_branch = (TextView) convertView.findViewById(R.id.tv_branch);
                viewHolder.down = (ImageView) convertView.findViewById(R.id.down);
                viewHolder.up = (ImageView) convertView.findViewById(R.id.up);

                viewHolder.tv_sub_total_row = (TextView) convertView.findViewById(R.id.tv_sub_total_row);

                viewHolder.tv_sumamount = (TextView) convertView.findViewById(R.id.tv_sumamount);
                viewHolder.tv_sumtotal = (TextView)  convertView.findViewById(R.id.tv_sumtotal);
                viewHolder.tv_sumdiskon = (TextView)  convertView.findViewById(R.id.tv_sumdiskon);

                viewHolder.tv_sumamount_non_rupiah = (TextView) convertView.findViewById(R.id.tv_sumamount_non_rupiah);
                viewHolder.tv_sumtotal_non_rupiah = (TextView) convertView.findViewById(R.id.tv_sumtotal_non_rupiah);
                viewHolder.tv_sumdiskon_non_rupiah = (TextView) convertView.findViewById(R.id.tv_sumdiskon_non_rupiah);

                viewHolder.listproduk_layout = (LinearLayout) convertView.findViewById(R.id.listproduk_layout);
                viewHolder.listproduk = (ListView) convertView.findViewById(R.id.listproduk_lhp);

//                viewHolder.data_setor_lhp_new_pojos = new ArrayList<>();

                sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                String employee_id = sharedPreferences.getString("szDocCall", null);
                Toast.makeText(detail_summary_transaksi_lhp_new.this, "EmployeeIdOK = " + employee_id, Toast.LENGTH_SHORT).show();
                viewHolder.adapter = new ListViewAdapterSetorLHP(viewHolder.data_setor_lhp_new_pojos, getApplicationContext());
                viewHolder.adapter.clear();
                viewHolder.adapter.notifyDataSetChanged();
                StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Selesai_Perjalanan/index_detail_lhp_by_nomor_do_Canvass?szDocId=" + movieItem.getNomor_do() + "&szBranchId=" + idbranch_driver, //mulai_perjalanan.id_pelanggan,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {
                                    int number = 0;
                                    JSONObject obj = new JSONObject(response);
                                    final JSONArray movieArray = obj.getJSONArray("data");

                                    for (int i = 0; i < movieArray.length(); i++) {
                                        final JSONObject movieObject = movieArray.getJSONObject(i);
                                        String diskon, harga;
                                        if(movieObject.getString("harga").equals("null")){
                                            harga = "0";
                                        } else {
                                            harga = movieObject.getString("harga");
                                        }

                                        if(movieObject.getString("diskon").equals("null")){
                                            diskon = "0";
                                        } else {
                                            diskon = movieObject.getString("diskon");
                                        }
                                        final data_setor_lhp_new_pojo movieItem = new data_setor_lhp_new_pojo(
                                                movieObject.getString("id_driver"),
                                                movieObject.getString("nomor_do"),
                                                movieObject.getString("nomor_std"),
                                                movieObject.getString("bkb"),
                                                movieObject.getString("tanggal_do"),
                                                movieObject.getString("driver"),
                                                movieObject.getString("id_toko"),
                                                movieObject.getString("toko"),
                                                movieObject.getString("rit"),
                                                movieObject.getString("idproduk"),
                                                movieObject.getString("produk"),
                                                movieObject.getString("nopol"),
                                                movieObject.getString("stock_awal_docsoitem"),
                                                movieObject.getString("terjual"),
                                                movieObject.getString("stock_akhir"),
                                                harga,
                                                movieObject.getString("sub_total"),
                                                diskon,
                                                movieObject.getString("total"),
                                                movieObject.getString("tipe_pay"),
                                                movieObject.getString("totalhargado"),
                                                movieObject.getString("sumDiskon"),
                                                movieObject.getString("sumTotal"),
                                                movieObject.getString("sumAmount"));


                                        String id_driver = movieObject.getString("id_driver");
                                        tv_id_driver.setText(id_driver);

                                        String driver = movieObject.getString("driver");
                                        tv_name_driver.setText(driver);

                                        String nopol = movieObject.getString("nopol");
                                        tv_nopol.setText(nopol);

                                        String sumamount = formatRupiah(Double.valueOf(movieObject.getString("sumAmount")));
                                        viewHolder.tv_sumamount.setText(sumamount);

                                        String sumtotal = formatRupiah(Double.valueOf(movieObject.getString("sumTotal")));
                                        viewHolder.tv_sumtotal.setText(sumtotal);

                                        //viewHolder.tv_sumdiskon_non_rupiah.setText(movieItem.getDiskon());

                                        String sumdiskon = formatRupiah(Double.valueOf(movieObject.getString("sumDiskon")));
                                        viewHolder.tv_sumdiskon.setText(sumdiskon);

                                        System.out.println("docsoitem"+ movieObject.getString("stock_awal_docsoitem"));
                                        System.out.println("id_driver"+  movieObject.getString("id_driver"));
                                        System.out.println("bkb test"+  movieObject.getString("bkb"));
                                        viewHolder.data_setor_lhp_new_pojos.add(movieItem);

                                    }

                                    viewHolder.listproduk.setAdapter(viewHolder.adapter);
                                    viewHolder.adapter.notifyDataSetChanged();
                                    Utility.setListViewHeightBasedOnChildren(viewHolder.listproduk);

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
                                500000,
                                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                        ));
                RequestQueue requestQueue = Volley.newRequestQueue(detail_summary_transaksi_lhp_new.this);
                requestQueue.add(stringRequest);

                viewHolder.listproduk_layout.setVisibility(View.VISIBLE);

                convertView.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            //viewHolder.data_setor_lhp_new_pojos.clear();

            //-------------------------------------------------------------------------------------------------------------------------------

            viewHolder.nomor_do.setText(movieItem.getNomor_do());
            viewHolder.nama_toko.setText(movieItem.getToko());
            viewHolder.id_toko.setText(movieItem.getId_toko());
            viewHolder.tv_nomor_std.setText(movieItem.getNomorstd());

            //BOTTOM SHEET DIALOG
            bt_detail_bottom_sheet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bt_detail_bottom_sheet.setEnabled(false);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            bt_detail_bottom_sheet.setEnabled(true);
                        }
                    }, 1500);

                    view = getLayoutInflater().inflate(R.layout.layout_bottom_sheet_setor_sekarang, null);

                    chk_setor_tunai = view.findViewById(R.id.chk_setor_tunai);
                    chk_setor_transfer = view.findViewById(R.id.chk_setor_transfer);
                    chk_mesin_setor = view.findViewById(R.id.chk_mesin_setor);

                    chk_kredit = view.findViewById(R.id.chk_kredit);

                    txt_input_setor_tunai = view.findViewById(R.id.txt_input_setor_tunai);
                    txt_input_jumlah_mesin = view.findViewById(R.id.txt_input_jumlah_mesin);
                    txt_input_setor_transfer = view.findViewById(R.id.txt_input_setor_transfer);

                    edt_setor_tunai = view.findViewById(R.id.edt_setor_tunai);
                    edt_setor_mesin = view.findViewById(R.id.edt_setor_mesin);
                    edt_setor_transfer = view.findViewById(R.id.edt_setor_transfer);

                    tv_setor_tunai = view.findViewById(R.id.tv_setor_tunai);
                    tv_setor_mesin = view.findViewById(R.id.tv_setor_mesin);
                    tv_setor_transfer = view.findViewById(R.id.tv_setor_transfer);

                    tv_total_setor = view.findViewById(R.id.tv_total_setor);
                    tv_total_setor_text = view.findViewById(R.id.tv_total_setor_text);

                    btn_setor = view.findViewById(R.id.btn_setor);
                    btn_confirm_setor = view.findViewById(R.id.btn_confirm_setor);

                    tv_dialog_total_setoran = view.findViewById(R.id.tv_dialog_total_setoran);
                    tv_dialog_total_setoran.setText(tv_sumfixtotal.getText().toString());

                    tv_dialog_total_tunai = view.findViewById(R.id.tv_dialog_total_tunai);
                    tv_dialog_total_transfer = view.findViewById(R.id.tv_dialog_total_transfer);

                    tv_dialog_total_transfer.setText(sumtransfer);
                    tv_dialog_total_tunai.setText(sumtunai);

                    tv_total_stor_non_format_rupiah = view.findViewById(R.id.tv_total_stor_non_format_rupiah);
                    tv_dialog_total_setoran_non_rupiah = view.findViewById(R.id.tv_dialog_total_setoran_non_rupiah);
                    tv_dialog_total_setoran_non_rupiah.setText(tv_sumfixtotal_non_format_rupiah.getText().toString());


                    if (sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    }


                    if(tv_sumfixtotal_non_format_rupiah.getText().toString().equals("0")){
                        chk_setor_tunai.setEnabled(false);
                        chk_mesin_setor.setEnabled(false);
                        chk_setor_transfer.setEnabled(false);
                    } else {
                        chk_kredit.setEnabled(false);
                        chk_setor_tunai.setEnabled(true);
                        chk_mesin_setor.setEnabled(true);
                        chk_setor_transfer.setEnabled(true);
                    }

                    chk_kredit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean b) {

                            if (b) {
                                btn_setor.setBackgroundTintList(ContextCompat.getColorStateList(detail_summary_transaksi_lhp_new.this,R.color.color_text_blue));

                                chk_kredit.setChecked(true);

                                chk_setor_tunai.setChecked(false);
                                chk_setor_transfer.setChecked(false);
                                chk_mesin_setor.setChecked(false);

                                txt_input_jumlah_mesin.setVisibility(View.GONE);
                                edt_setor_mesin.setText("");

                                txt_input_setor_tunai.setVisibility(View.GONE);
                                edt_setor_tunai.setText("");

                                txt_input_setor_transfer.setVisibility(View.GONE);
                                edt_setor_transfer.setText("");

                                // Everytime you check the checkBox, the following code will execute:
                                // Your code here //
                            }

                        }
                    });

                    chk_setor_tunai.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                            if (b) {
                                chk_kredit.setChecked(false);
                                chk_setor_tunai.setChecked(true);

                                // Everytime you check the checkBox, the following code will execute:
                                // Your code here //
                            }
                        }
                    });

                    chk_setor_transfer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                            if (b) {
                                chk_kredit.setChecked(false);
                                chk_setor_transfer.setChecked(true);

                                // Everytime you check the checkBox, the following code will execute:
                                // Your code here //
                            }
                        }
                    });

                    chk_mesin_setor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                            if (b) {
                                chk_kredit.setChecked(false);
                                chk_mesin_setor.setChecked(true);

                                // Everytime you check the checkBox, the following code will execute:
                                // Your code here //
                            }
                        }
                    });

                    chk_mesin_setor.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if(chk_mesin_setor.isChecked()){
                                txt_input_jumlah_mesin.setVisibility(View.VISIBLE);
                                edt_setor_mesin.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void afterTextChanged(Editable s) {
                                        // TODO Auto-generated method stub
                                        edt_setor_mesin.removeTextChangedListener(this);

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
                                            edt_setor_mesin.setText(formattedString);
                                            edt_setor_mesin.setSelection(edt_setor_mesin.getText().length());
                                        } catch (NumberFormatException nfe) {
                                            nfe.printStackTrace();
                                        }

                                        edt_setor_mesin.addTextChangedListener(this);

                                        String str = edt_setor_mesin.getText().toString().replaceAll( ",", "" );
                                        tv_setor_mesin.setText(str);
                                        System.out.println("hasil mesin" + str);
                                    }

                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                        // TODO Auto-generated method stub
                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                                        if (!edt_setor_mesin.getText().toString().equals("")){
                                            txt_input_jumlah_mesin.setError(null);
                                        } else {
                                            txt_input_jumlah_mesin.setError("Jumlah setor mesin tidak boleh kosong");
                                        }
                                    }
                                });

                            } else if (!chk_mesin_setor.isChecked()){

                                txt_input_jumlah_mesin.setVisibility(View.GONE);
                                edt_setor_mesin.setText("");
                                txt_input_jumlah_mesin.setError(null);

                            }

                        }
                    });

                    chk_setor_tunai.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if(chk_setor_tunai.isChecked()){
                                txt_input_setor_tunai.setVisibility(View.VISIBLE);

                                edt_setor_tunai.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void afterTextChanged(Editable s) {
                                        // TODO Auto-generated method stub
                                        edt_setor_tunai.removeTextChangedListener(this);

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
                                            edt_setor_tunai.setText(formattedString);
                                            edt_setor_tunai.setSelection(edt_setor_tunai.getText().length());
                                        } catch (NumberFormatException nfe) {
                                            nfe.printStackTrace();
                                        }

                                        edt_setor_tunai.addTextChangedListener(this);

                                        String str = edt_setor_tunai.getText().toString().replaceAll( ",", "" );
                                        tv_setor_tunai.setText(str);
                                        System.out.println("hasil tunai" + str);
                                        System.out.println("hasil tunai text" + tv_setor_tunai.getText().toString());
                                    }

                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                        // TODO Auto-generated method stub
                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                                        if (!edt_setor_tunai.getText().toString().equals("")){
                                            txt_input_setor_tunai.setError(null);
                                        } else {
                                            txt_input_setor_tunai.setError("Jumlah setor tunai tidak boleh kosong");
                                        }
                                    }
                                });


                            } else if (!chk_setor_tunai.isChecked()){
                                txt_input_setor_tunai.setVisibility(View.GONE);
                                edt_setor_tunai.setText("");
                                txt_input_setor_tunai.setError(null);

                            }

                        }
                    });

                    chk_setor_transfer.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if(chk_setor_transfer.isChecked()){
                                txt_input_setor_transfer.setVisibility(View.VISIBLE);

                                edt_setor_transfer.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void afterTextChanged(Editable s) {
                                        // TODO Auto-generated method stub
                                        edt_setor_transfer.removeTextChangedListener(this);

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
                                            edt_setor_transfer.setText(formattedString);
                                            edt_setor_transfer.setSelection(edt_setor_transfer.getText().length());
                                        } catch (NumberFormatException nfe) {
                                            nfe.printStackTrace();
                                        }

                                        edt_setor_transfer.addTextChangedListener(this);

                                        String str = edt_setor_transfer.getText().toString().replaceAll( ",", "" );
                                        tv_setor_transfer.setText(str);
                                        System.out.println("hasil transfer" + str);
                                    }

                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                        // TODO Auto-generated method stub
                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                                        if (!edt_setor_transfer.getText().toString().equals("")){
                                            txt_input_setor_transfer.setError(null);
                                        } else {
                                            txt_input_setor_transfer.setError("Jumlah setor transfer tidak boleh kosong");
                                        }
                                    }
                                });


                            } else if (!chk_setor_transfer.isChecked()){
                                txt_input_setor_transfer.setVisibility(View.GONE);
                                edt_setor_transfer.setText("");
                                txt_input_setor_transfer.setError(null);

                            }

                        }
                    });

                    btn_confirm_setor.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            //7 = transfer, tunai dan mesin
                            if(chk_mesin_setor.isChecked() && chk_setor_tunai.isChecked() && chk_setor_transfer.isChecked()){

                                if (edt_setor_mesin.getText().toString().equals("") && edt_setor_tunai.getText().toString().equals("") && edt_setor_transfer.getText().toString().equals("")) {
                                    txt_input_jumlah_mesin.setError("jumlah mesin , tunai, dan transfer tdk boleh kosong");
                                    txt_input_setor_tunai.setError("jumlah tunai, mesin, transfer tidak boleh kosong");
                                    txt_input_setor_transfer.setError("jumlah transfer, mesin, tunai tidak boleh kosong");
                                } else if (edt_setor_tunai.getText().toString().equals("") && edt_setor_transfer.getText().toString().equals("")) {
                                    txt_input_setor_tunai.setError("tunai tidak boleh kosong");
                                    txt_input_setor_transfer.setError("jumlah transfer tidak boleh kosong");
                                } else if (edt_setor_tunai.getText().toString().equals("") && edt_setor_mesin.getText().toString().equals("")) {
                                    txt_input_setor_tunai.setError("tunai tidak boleh kosong");
                                    txt_input_jumlah_mesin.setError("jumlah mesin tidak boleh kosong");
                                } else if (edt_setor_tunai.getText().toString().equals("")) {
                                    txt_input_setor_tunai.setError("tunai tidak boleh kosong");
                                } else if (edt_setor_mesin.getText().toString().equals("")) {
                                    txt_input_jumlah_mesin.setError("jumlah mesin , tunai, dan transfer tdk boleh kosong");
                                } else if (edt_setor_transfer.getText().toString().equals("")) {
                                    txt_input_setor_transfer.setError("jumlah transfer tidak boleh kosong");
                                } else {
                                    int jumlah_setor_tunai = Integer.parseInt(tv_setor_tunai.getText().toString());
                                    int jumlah_setor_mesin = Integer.parseInt(tv_setor_mesin.getText().toString());
                                    int jumlah_setor_transfer = Integer.parseInt(tv_setor_transfer.getText().toString());

                                    String total = String.valueOf(jumlah_setor_tunai + jumlah_setor_mesin + jumlah_setor_transfer);

                                    tv_total_stor_non_format_rupiah.setText(total);
                                    jenis_pembayaran_transfer = 7;

                                    String totalhargarupiah = formatRupiah(Double.valueOf(total));
                                    tv_total_setor.setText(totalhargarupiah);
                                    tv_total_setor_text.setText("transfer, tunai dan mesin");
                                }

                                //4=mesin&tunai
                            } else if (chk_mesin_setor.isChecked() && chk_setor_tunai.isChecked()) {
                                if (edt_setor_mesin.getText().toString().equals("") && edt_setor_tunai.getText().toString().equals("")) {
                                    txt_input_jumlah_mesin.setError("jumlah setor mesin dan tunai tidak boleh kosong");
                                    txt_input_setor_tunai.setError("jumlah setor tunai dan mesin tidak boleh kosong");
                                } else if (!edt_setor_mesin.getText().toString().equals("") && edt_setor_tunai.getText().toString().equals("")) {
                                    txt_input_setor_tunai.setError("jumlah setor tunai tidak boleh kosong");
                                } else if (edt_setor_mesin.getText().toString().equals("") && !edt_setor_tunai.getText().toString().equals("")) {
                                    txt_input_jumlah_mesin.setError("jumlah mesin tidak boleh kosong");
                                } else {
                                    int jumlah_setor_tunai = Integer.parseInt(tv_setor_tunai.getText().toString());
                                    int jumlah_setor_mesin = Integer.parseInt(tv_setor_mesin.getText().toString());

                                    String total = String.valueOf(jumlah_setor_tunai + jumlah_setor_mesin);
                                    jenis_pembayaran_transfer = 4;
                                    tv_total_stor_non_format_rupiah.setText(total);

                                    String totalhargarupiah = formatRupiah(Double.valueOf(total));
                                    tv_total_setor.setText(totalhargarupiah);
                                    tv_total_setor_text.setText("mesin dan tunai");
                                }

                                //6 = tunai&transfer
                            } else if (chk_setor_transfer.isChecked() && chk_setor_tunai.isChecked()) {

                                if (edt_setor_transfer.getText().toString().equals("") && edt_setor_tunai.getText().toString().equals("")) {
                                    txt_input_setor_transfer.setError("jumlah transfer dan tunai tidak boleh kosong");
                                    txt_input_setor_tunai.setError("jumlah tunai dan transfer tidak boleh kosong");
                                } else if (!edt_setor_transfer.getText().toString().equals("") && edt_setor_tunai.getText().toString().equals("")) {
                                    txt_input_setor_tunai.setError("jumlah tunai tidak boleh kosong");
                                } else if (edt_setor_transfer.getText().toString().equals("") && !edt_setor_tunai.getText().toString().equals("")) {
                                    txt_input_setor_transfer.setError("jumlah transfer tidak boleh kosong");
                                } else {
                                    Toast.makeText(detail_summary_transaksi_lhp_new.this, "DAPET jumlah", Toast.LENGTH_SHORT).show();
                                    int jumlah_transfer = Integer.parseInt(tv_setor_transfer.getText().toString());
                                    int jumlah_tunai_setor = Integer.parseInt(tv_setor_tunai.getText().toString());


                                    String total = String.valueOf(jumlah_transfer + jumlah_tunai_setor);
                                    jenis_pembayaran_transfer = 6;
                                    tv_total_stor_non_format_rupiah.setText(total);

                                    String totalhargarupiah = formatRupiah(Double.valueOf(total));
                                    tv_total_setor.setText(totalhargarupiah);
                                    tv_total_setor_text.setText("transfer & tunai");

                                }

                                //5 = mesin&transfer
                            } else if (chk_setor_transfer.isChecked() && chk_mesin_setor.isChecked()){

                                if (edt_setor_transfer.getText().toString().equals("") && edt_setor_mesin.getText().toString().equals("")) {
                                    txt_input_setor_transfer.setError("jumlah setor transfer dan mesin tidak boleh kosong");
                                    txt_input_jumlah_mesin.setError("jumlah setor mesin dan transfer tidak boleh kosong");
                                } else if(!edt_setor_transfer.getText().toString().equals("") && edt_setor_mesin.getText().toString().equals("")){
                                    txt_input_jumlah_mesin.setError("jumlah mesin tidak boleh kosong");
                                } else if (edt_setor_transfer.getText().toString().equals("") && !edt_setor_mesin.getText().toString().equals("")) {
                                    txt_input_setor_transfer.setError("jumlah transfer tidak boleh kosong");
                                } else {
                                    int jumlah_transfer = Integer.parseInt(tv_setor_transfer.getText().toString());
                                    int jumlah_mesin_setor = Integer.parseInt(tv_setor_mesin.getText().toString());


                                    String total = String.valueOf(jumlah_transfer + jumlah_mesin_setor);
                                    jenis_pembayaran_transfer = 5;
                                    tv_total_stor_non_format_rupiah.setText(total);

                                    String totalhargarupiah = formatRupiah(Double.valueOf(total));
                                    tv_total_setor.setText(totalhargarupiah);
                                    tv_total_setor_text.setText("transfer & mesin");
                                    Toast.makeText(detail_summary_transaksi_lhp_new.this, "DAPET jumlah", Toast.LENGTH_SHORT).show();
                                }

                                //1 = SETOR TUNAI
                            } else if (chk_setor_tunai.isChecked()) {
                                if (edt_setor_tunai.getText().toString().equals("")) {
                                    txt_input_setor_tunai.setError("Jumlah setor tunai tidak boleh kosong");
                                } else {
                                    int jumlah_setor_tunai = Integer.parseInt(tv_setor_tunai.getText().toString());
                                    String string_jumlah_tunai = String.valueOf(jumlah_setor_tunai);

                                    jenis_pembayaran_transfer = 2;
                                    tv_total_stor_non_format_rupiah.setText(string_jumlah_tunai);

                                    String totalhargarupiah = formatRupiah(Double.valueOf(string_jumlah_tunai));
                                    tv_total_setor.setText(totalhargarupiah);
                                    tv_total_setor_text.setText("tunai: ");
                                }

                                //1 = SETOR MESIN
                            } else if (chk_mesin_setor.isChecked()) {
                                if (edt_setor_mesin.getText().toString().equals("")){
                                    txt_input_jumlah_mesin.setError("jumlah setor mesin tidak boleh kosong");
                                } else {
                                    int jumlah_setor_mesin = Integer.parseInt(tv_setor_mesin.getText().toString());
                                    String string_jumlah_mesin = String.valueOf(jumlah_setor_mesin);

                                    jenis_pembayaran_transfer = 1;
                                    tv_total_stor_non_format_rupiah.setText(string_jumlah_mesin);

                                    String totalhargarupiah = formatRupiah(Double.valueOf(string_jumlah_mesin));
                                    tv_total_setor.setText(totalhargarupiah);
                                    tv_total_setor_text.setText("mesin: ");

                                }

                                //3 = SETOR TRANSFER
                            }  else if (chk_setor_transfer.isChecked()) {
                                if (edt_setor_transfer.getText().toString().equals("")) {
                                    txt_input_setor_transfer.setError("jumlah setor transfer tidak boleh kosong");
                                } else {
                                    int jumlah_setor_transfer = Integer.parseInt(tv_setor_transfer.getText().toString());
                                    String string_jumlah_transfer = String.valueOf(jumlah_setor_transfer);

                                    jenis_pembayaran_transfer = 3;
                                    tv_total_stor_non_format_rupiah.setText(string_jumlah_transfer);

                                    String totalhargarupiah = formatRupiah(Double.valueOf(string_jumlah_transfer));
                                    tv_total_setor.setText(totalhargarupiah);
                                    tv_total_setor_text.setText("transfer: ");
                                }
                            }

                            int total_setor_non_rupiah = Integer.parseInt(tv_total_stor_non_format_rupiah.getText().toString());

                            //JIKA TOTAL SETORNYA NYA LEBIH DARI 0 MAKA BUTTONS SETOR SEKARANG AKAN BERUBAH WARNA
                            //JADI BIRU, DEFAULTNYA WARNA ABU-ABU
                            if (total_setor_non_rupiah > 0){
                                //Toast.makeText(detail_summary_transaksi_lhp_new.this, "OK NIH LEBIH DARI 0", Toast.LENGTH_SHORT).show();
                                btn_setor.setBackgroundTintList(ContextCompat.getColorStateList(detail_summary_transaksi_lhp_new.this,R.color.color_text_blue));
                            }
                        }
                    });

                    btn_setor.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            btn_setor.setEnabled(false);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    btn_setor.setEnabled(true);
                                }
                            }, 1500);

                            int total_setor_non_rupiah = Integer.parseInt(tv_total_stor_non_format_rupiah.getText().toString());
                            int total_pembayaran_non_rupiah = Integer.parseInt(tv_dialog_total_setoran_non_rupiah.getText().toString());

                            if(chk_kredit.isChecked()){
                                dialogstatus = new Dialog(detail_summary_transaksi_lhp_new.this);
                                dialogstatus.setContentView(R.layout.dialog_submit_lhp);
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

                                        pDialog = new SweetAlertDialog(detail_summary_transaksi_lhp_new.this, SweetAlertDialog.PROGRESS_TYPE);
                                        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                                        pDialog.setTitleText("Harap Menunggu");
                                        pDialog.setCancelable(false);
                                        pDialog.show();

                                        for (int i = 0; i < data_lhp_do_pojos.size(); i++) {

                                            int finalI = i;

                                            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_historysetorlhp_km",
                                                    new Response.Listener<String>() {

                                                        @Override
                                                        public void onResponse(String response) {
                                                            updateKredit(adapterSumTransaksi.getItem(finalI).getNomor_do());
                                                            if(finalI == data_lhp_do_pojos.size() -1){
                                                                pDialog.dismiss();
                                                                SweetAlertDialog success = new SweetAlertDialog(detail_summary_transaksi_lhp_new.this, SweetAlertDialog.SUCCESS_TYPE);
                                                                success.setContentText("Data Sudah Disimpan");
                                                                success.setCancelable(false);
                                                                success.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                            @Override
                                                                            public void onClick(SweetAlertDialog sDialog) {
                                                                                sDialog.dismissWithAnimation();
                                                                                Intent i = new Intent(getBaseContext(), MainActivity.class);
                                                                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                                                startActivity(i);

                                                                            }
                                                                        });
                                                                success.show();
                                                            }

                                                        }
                                                    }, new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    //img2.setBackgroundColor(Color.parseColor("#E91E63"));
                                                    dialogstatus.dismiss();
                                                    Toast.makeText(detail_summary_transaksi_lhp_new.this, "Terjadi Kesalahan Saat Submit Setoran", Toast.LENGTH_SHORT).show();
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
                                                    SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd");
                                                    String currentDateandTime3 = sdf3.format(new Date());

                                                    params.put("dtmDoc", currentDateandTime3);
                                                    params.put("id_driver", tv_id_driver.getText().toString());
                                                    params.put("nomor_do", adapter_parent.getItem(finalI).getNomor_do());
                                                    params.put("nomor_bkb", adapter_parent.getItem(finalI).getNomorbkb());
                                                    params.put("nomor_std", nomor_std);
                                                    params.put("customerId", adapter_parent.getItem(finalI).getId_toko());
                                                    params.put("total_pembayaran_customer", adapterSumTransaksi.getItem(finalI).getSumtotal());

                                                    if(chk_kredit.isChecked()){
                                                        params.put("total_pembayaran_mesin","");
                                                        params.put("total_pembayaran_tunai", "");
                                                        params.put("total_pembayaran_transfer",  "");
                                                        params.put("status_pembayaran", "0");
                                                    } else {
                                                        params.put("total_pembayaran_mesin",tv_setor_mesin.getText().toString());
                                                        params.put("total_pembayaran_tunai", tv_setor_tunai.getText().toString());
                                                        params.put("total_pembayaran_transfer",  tv_setor_transfer.getText().toString());
                                                        params.put("status_pembayaran", String.valueOf(jenis_pembayaran_transfer));
                                                    }

                                                    params.put("branch", adapter_parent.getItem(finalI).getBranchId());
                                                    params.put("ritase", adapter_parent.getItem(finalI).getRitase());
                                                    params.put("status_lhp", "1");

                                                    if(chk_kredit.isChecked()){
                                                        params.put("before_total_pembayaran_customer", "");
                                                        params.put("before_total_pembayaran_mesin", "");
                                                        params.put("before_total_pembayaran_tunai", "");
                                                        params.put("before_total_pembayaran_transfer", "");
                                                        params.put("before_status_pembayaran", "0");
                                                    } else {
                                                        params.put("before_total_pembayaran_customer", adapterSumTransaksi.getItem(finalI).getSumtotal());
                                                        params.put("before_total_pembayaran_mesin", tv_setor_mesin.getText().toString());
                                                        params.put("before_total_pembayaran_tunai", tv_setor_tunai.getText().toString());
                                                        params.put("before_total_pembayaran_transfer", tv_setor_transfer.getText().toString());
                                                        params.put("before_status_pembayaran", String.valueOf(jenis_pembayaran_transfer));
                                                    }

                                                    params.put("km_lhp", getIntent().getStringExtra("kilometer"));



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
                                            RequestQueue requestQueue2 = Volley.newRequestQueue(detail_summary_transaksi_lhp_new.this);
                                            requestQueue2.add(stringRequest2);

                                        }

                                    }
                                });

                                dialogstatus.show();
                            } else if (total_setor_non_rupiah == 0) {

                                Toast.makeText(detail_summary_transaksi_lhp_new.this, "Mohon pilih metode setor terlebih dahulu", Toast.LENGTH_SHORT).show();

                            } else if (total_setor_non_rupiah < total_pembayaran_non_rupiah) {

                                Toast.makeText(detail_summary_transaksi_lhp_new.this, "Total Pembayaran tidak boleh kurang dari Total Setoran", Toast.LENGTH_SHORT).show();

                            } else {

                                final Dialog dialogstatus = new Dialog(detail_summary_transaksi_lhp_new.this);
                                dialogstatus.setContentView(R.layout.dialog_submit_lhp);
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

                                        pDialog = new SweetAlertDialog(detail_summary_transaksi_lhp_new.this, SweetAlertDialog.PROGRESS_TYPE);
                                        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                                        pDialog.setTitleText("Harap Menunggu");
                                        pDialog.setCancelable(false);
                                        pDialog.show();

                                        for (int i = 0; i < data_lhp_do_pojos.size(); i++) {

                                            int finalI = i;

                                            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_historysetorlhp_km",
                                                    new Response.Listener<String>() {

                                                        @Override
                                                        public void onResponse(String response) {
                                                            relative_background_succes.setVisibility(View.VISIBLE);
                                                            //lottie_lhp_success.setVisibility(View.VISIBLE);

                                                            //Toast.makeText(detail_summary_transaksi_lhp_new.this, "POST BERHASIL", Toast.LENGTH_SHORT).show();
                                                            if(finalI == data_lhp_do_pojos.size() -1){
                                                                pDialog.dismiss();
                                                                SweetAlertDialog success = new SweetAlertDialog(detail_summary_transaksi_lhp_new.this, SweetAlertDialog.SUCCESS_TYPE);
                                                                success.setContentText("Data Sudah Disimpan");
                                                                success.setCancelable(false);
                                                                success.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                    @Override
                                                                    public void onClick(SweetAlertDialog sDialog) {
                                                                        sDialog.dismissWithAnimation();
                                                                        Intent i = new Intent(getBaseContext(), MainActivity.class);
                                                                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                                        startActivity(i);

                                                                    }
                                                                });
                                                                success.show();
                                                            }
                                                        }
                                                    }, new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    //img2.setBackgroundColor(Color.parseColor("#E91E63"));
                                                    dialogstatus.dismiss();
                                                    Toast.makeText(detail_summary_transaksi_lhp_new.this, "Terjadi Kesalahan Saat Submit Setoran", Toast.LENGTH_SHORT).show();
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
                                                    SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd");
                                                    String currentDateandTime3 = sdf3.format(new Date());

                                                    params.put("dtmDoc", currentDateandTime3);
                                                    params.put("id_driver", tv_id_driver.getText().toString());
                                                    params.put("nomor_do", adapter_parent.getItem(finalI).getNomor_do());
                                                    params.put("nomor_bkb", adapter_parent.getItem(finalI).getNomorbkb());
                                                    params.put("nomor_std", nomor_std);
                                                    params.put("customerId", adapter_parent.getItem(finalI).getId_toko());
                                                    params.put("total_pembayaran_customer", adapterSumTransaksi.getItem(finalI).getSumtotal());

                                                    if(chk_kredit.isChecked()){
                                                        params.put("total_pembayaran_mesin","");
                                                        params.put("total_pembayaran_tunai", "");
                                                        params.put("total_pembayaran_transfer",  "");
                                                        params.put("status_pembayaran", "0");
                                                    } else {
                                                        params.put("total_pembayaran_mesin",tv_setor_mesin.getText().toString());
                                                        params.put("total_pembayaran_tunai", tv_setor_tunai.getText().toString());
                                                        params.put("total_pembayaran_transfer",  tv_setor_transfer.getText().toString());
                                                        params.put("status_pembayaran", String.valueOf(jenis_pembayaran_transfer));
                                                    }

                                                    params.put("branch", adapter_parent.getItem(finalI).getBranchId());
                                                    params.put("ritase", adapter_parent.getItem(finalI).getRitase());
                                                    params.put("status_lhp", "1");

                                                    if(chk_kredit.isChecked()){
                                                        params.put("before_total_pembayaran_customer", adapterSumTransaksi.getItem(finalI).getSumtotal());
                                                        params.put("before_total_pembayaran_mesin", tv_setor_mesin.getText().toString());
                                                        params.put("before_total_pembayaran_tunai", tv_setor_tunai.getText().toString());
                                                        params.put("before_total_pembayaran_transfer", tv_setor_transfer.getText().toString());
                                                        params.put("before_status_pembayaran", String.valueOf(jenis_pembayaran_transfer));
                                                    } else {
                                                        params.put("before_total_pembayaran_customer", "");
                                                        params.put("before_total_pembayaran_mesin", "");
                                                        params.put("before_total_pembayaran_tunai", "");
                                                        params.put("before_total_pembayaran_transfer", "");
                                                        params.put("before_status_pembayaran", "");
                                                    }
                                                    params.put("km_lhp", getIntent().getStringExtra("kilometer"));


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
                                            RequestQueue requestQueue2 = Volley.newRequestQueue(detail_summary_transaksi_lhp_new.this);
                                            requestQueue2.add(stringRequest2);

                                        }

                                    }
                                });

                                dialogstatus.show();

                            }

                        }
                    });

                    sheetDialog = new BottomSheetDialog(detail_summary_transaksi_lhp_new.this);
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
            });


//            viewHolder.tv_nomor_bkb.setText(movieItem.getNomorbkb());
//            viewHolder.tv_branch.setText(movieItem.getBranchId());
//            viewHolder.tv_nomor_ritase.setText(movieItem.getRitase());

            return convertView;

        }
    }

    private void updateKredit(String nomor_do) {
        StringRequest stringRequest2 = new StringRequest(Request.Method.PUT, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_updatestatuskredit",
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

                params.put("nomor_do", nomor_do);
                params.put("status_kredit", "1");


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
        RequestQueue requestQueue2 = Volley.newRequestQueue(detail_summary_transaksi_lhp_new.this);
        requestQueue2.add(stringRequest2);
    }


    public class ListViewAdapterSetorLHP extends ArrayAdapter<data_setor_lhp_new_pojo> {

        private class ViewHolder {
            TextView txt_id_driver, txt_nomor_do, txt_nomor_std, txt_tanggal_do, txt_driver,
                    txt_toko, txt_id_toko, txt_rit, txt_id_produk, txt_name_produk, txt_nopol,
                    txt_terjual, txt_stock_akhir, txt_harga_produk, txt_sub_total,
                    txt_diskon, txt_total, txt_tipe_pembayaran, txt_nomor_bkb, txt_stock_docso;
            ImageView down, up;
            LinearLayout listproduk_layout;
            ListView listproduk;

        }

        private List<data_setor_lhp_new_pojo> data_setor_lhp_new_pojos;

        private final Context context;

        public ListViewAdapterSetorLHP(List<data_setor_lhp_new_pojo> data_setor_lhp_new_pojo, Context context) {
            super (context, R.layout.layout_detail_lhp, data_setor_lhp_new_pojo);
            this.data_setor_lhp_new_pojos = data_setor_lhp_new_pojo;
            this.context = context;
        }

        public int getCount() {
            return data_setor_lhp_new_pojos.size();
        }

        public data_setor_lhp_new_pojo getItem(int position) {
            return data_setor_lhp_new_pojos.get(position);
        }

        @Override
        public int getViewTypeCount() {
            return getCount();
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

            final ListViewAdapterSetorLHP.ViewHolder viewHolder;
            data_setor_lhp_new_pojo data = data_setor_lhp_new_pojos.get(position);

            if (convertView == null) {

                viewHolder = new ListViewAdapterSetorLHP.ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.layout_detail_lhp, parent, false);

                viewHolder.txt_id_driver = convertView.findViewById(R.id.txt_id_driver);
                viewHolder.txt_nomor_do = convertView.findViewById(R.id.txt_nomor_do);
                viewHolder.txt_nomor_std = convertView.findViewById(R.id.txt_nomor_std);
                viewHolder.txt_nomor_bkb = convertView.findViewById(R.id.txt_nomor_bkb);
                viewHolder.txt_tanggal_do = convertView.findViewById(R.id.txt_tanggal_do);
                viewHolder.txt_driver = convertView.findViewById(R.id.txt_driver);
                viewHolder.txt_toko = convertView.findViewById(R.id.txt_toko);
                viewHolder.txt_id_toko = convertView.findViewById(R.id.txt_id_toko);
                viewHolder.txt_rit = convertView.findViewById(R.id.txt_rit);
                viewHolder.txt_id_produk = convertView.findViewById(R.id.txt_id_produk);
                viewHolder.txt_name_produk = convertView.findViewById(R.id.txt_name_produk);
                viewHolder.txt_nopol = convertView.findViewById(R.id.txt_nopol);

                viewHolder.txt_stock_docso = convertView.findViewById(R.id.txt_stock_docso);

                viewHolder.txt_terjual = convertView.findViewById(R.id.txt_terjual);
                viewHolder.txt_stock_akhir = convertView.findViewById(R.id.txt_stock_akhir);
                viewHolder.txt_harga_produk = convertView.findViewById(R.id.txt_harga_produk);


                viewHolder.txt_sub_total = convertView.findViewById(R.id.txt_sub_total);
                viewHolder.txt_diskon = convertView.findViewById(R.id.txt_diskon);
                viewHolder.txt_total = convertView.findViewById(R.id.txt_total);
                viewHolder.txt_tipe_pembayaran = convertView.findViewById(R.id.txt_tipe_pembayaran);


                for(int i = 0; i < data_setor_lhp_new_pojos.size(); i++) {

                    String totaldiskon = data.getDiskon();
                    String[] stringtotaldiskon = totaldiskon.split("\\.");
                    String str_totaldiskon = stringtotaldiskon[0];

                    String hargaproduk = formatRupiah(Double.valueOf(String.valueOf(data.getHarga())));
                    System.out.println("TOTAL SEMUA" + hargaproduk);
                    viewHolder.txt_harga_produk.setText(hargaproduk);

                    String totalparent = formatRupiah(Double.valueOf(String.valueOf(data.getTotal())));
                    viewHolder.txt_total.setText(totalparent);

                    String subtotalparent = formatRupiah(Double.valueOf(String.valueOf(data.getSubtotal())));
                    viewHolder.txt_sub_total.setText(subtotalparent);

                    System.out.println("HASIL DARI TOTAL DISETOR " + Integer.parseInt(data.getTotal()));

                }

                convertView.setTag(viewHolder);

            } else {
                viewHolder = (ListViewAdapterSetorLHP.ViewHolder) convertView.getTag();
            }


            viewHolder.txt_id_driver.setText(data.getId_driver());
            viewHolder.txt_nomor_do.setText(data.getNomor_do());
            viewHolder.txt_nomor_std.setText(data.getNomor_std());
            viewHolder.txt_nomor_bkb.setText(data.getBkb());
            viewHolder.txt_tanggal_do.setText(data.getTanggaldo());
            viewHolder.txt_driver.setText(data.getDriver());
            viewHolder.txt_toko.setText(data.getToko());
            viewHolder.txt_id_toko.setText(data.getIdtoko());
            viewHolder.txt_rit.setText(data.getRit());
            viewHolder.txt_id_produk.setText(data.getIdproduk());
            viewHolder.txt_name_produk.setText (data.getProduk());
            viewHolder.txt_nopol.setText(data.getNopol());


            viewHolder.txt_stock_docso.setText(data.getStockawaldocso());

            viewHolder.txt_terjual.setText(data.getTerjual());
            viewHolder.txt_stock_akhir.setText(data.getStockakhir());

            viewHolder.txt_tipe_pembayaran.setText(data.getPay());

            if(data.getPay().equals("KREDIT")){
                viewHolder.txt_tipe_pembayaran.setBackgroundResource(R.drawable.outline_shape_red);
                viewHolder.txt_tipe_pembayaran.setTextColor(Color.parseColor("#FB4141"));
            } else {
                viewHolder.txt_tipe_pembayaran.setBackgroundResource(R.drawable.outline_shape_green);
                viewHolder.txt_tipe_pembayaran.setTextColor(Color.parseColor("#2ECC71"));

            }


//
//            String totalsumamount = formatRupiah(Double.valueOf(String.valueOf(data.getSumAmount())));
//            viewHolder.txt_sumamount.setText(totalsumamount);
            //viewHolder.txt_harga_produk.setText(harga_satuan_row_rupiah);

            String szId = data.getDiskon();
            String[] parts = szId.split("\\.");
            String szIdSlice = parts[0];

            String diskonparent = formatRupiah(Double.valueOf(String.valueOf(szIdSlice)));
            viewHolder.txt_diskon.setText(diskonparent);

            return convertView;

        }

    }

    private String formatRupiah(Double number){
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        return formatRupiah.format(number);
    }

    private void getSUmTotal() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Selesai_Perjalanan/index_sumlhp?nomor_std=" + nomor_std + "&szBkbId=" + szBkbId + "&szBranchId=" + idbranch_driver,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        pDialog.dismissWithAnimation();
                        relative_loading.setVisibility(View.GONE);

                        try {
                            int number = 0;
                            int number1 = 0;
                            int number2 = 0;
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");
                            for (int i = 0; i < movieArray.length(); i++) {
                                final JSONObject movieObject = movieArray.getJSONObject(i);
                                final data_sum_lhp_pojo movieItem = new data_sum_lhp_pojo(
                                        movieObject.getString("sumAmount"),
                                        movieObject.getString("sumTotal"),
                                        movieObject.getString("Sumtest"),
                                        movieObject.getString("sumDiskon"),
                                        movieObject.getString("nomor_do"),
                                        movieObject.getString("nomor_std"),
                                        movieObject.getString("bkb"),
                                        movieObject.getString("id_toko"));

                                data_sum_lhp_pojos.add(movieItem);

                                int totalharga = Integer.parseInt(movieObject.getString("sumTotal"));


                                // number += totalharga;

                                adapterSumTransaksi = new ListViewAdapterSumTransaksi(data_sum_lhp_pojos, getApplicationContext());
                                list_total_pembayaran_lhp.setAdapter(adapterSumTransaksi);
                                adapterSumTransaksi.notifyDataSetChanged();

                                if(!movieObject.getString("rit").equals(akhirikegiatan.tv_ritase.getText().toString())){
                                    data_sum_lhp_pojos.remove(movieItem);
                                    adapterSumTransaksi.notifyDataSetChanged();
                                }

                                if(movieObject.getString("tipe_pay").equals("Tidak Diketahui") && !movieObject.getString("rit").equals(akhirikegiatan.tv_ritase.getText().toString())){
                                    data_sum_lhp_pojos.remove(movieItem);
                                    adapterSumTransaksi.notifyDataSetChanged();
                                }

                                if(!movieObject.getString("tipe_pay").equals("Tidak Diketahui") && movieObject.getString("rit").equals(akhirikegiatan.tv_ritase.getText().toString())){
                                    number += totalharga;
                                    int totaltunai = Integer.parseInt(movieObject.getString("sumTunai"));
                                    int totaltransfer = Integer.parseInt(movieObject.getString("sumTransfer"));

                                    String transfer = formatRupiah(Double.valueOf(totaltransfer));
                                    String tunai = formatRupiah(Double.valueOf(totaltunai));

                                    sumtransfer = transfer;
                                    sumtunai = tunai;
                                }

                                String sumsetor = formatRupiah(Double.valueOf(number));
                                tv_sumfixtotal.setText(sumsetor);
                                tv_sumfixtotal_non_format_rupiah.setText(String.valueOf(number));

//                                System.out.println("HOHO"+ movieObject.getString("sumTotal"));
                                System.out.println("NO STD" + nomor_std);
                                System.out.println("BKB" + szBkbId);

                                System.out.println("HASILNYA NIH BOIS " + movieItem.getSumamount());

                                System.out.println("HASILNYA NIH getNomor_bkb " + movieItem.getNomor_bkb());
                                System.out.println("HASILNYA NIH getNomor_do " + movieItem.getNomor_do());
                                System.out.println("HASILNYA NIH getNomor_customer " + movieItem.getNomor_customer());
                                System.out.println("HASILNYA NIH getNomor_std " + movieItem.getNomor_std());
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

    public class ListViewAdapterSumTransaksi extends ArrayAdapter<data_sum_lhp_pojo> {

        private class ViewHolder {
            TextView  tv_sumamount, tv_sumtotal, tv_sumdiskon, tv_sumfixtotal,
                    tv_no_customer, tv_no_do, tv_no_bkb, tv_no_std;
            LinearLayout listproduk_layout;
        }

        List<data_sum_lhp_pojo> data_sum_lhp_pojos;
        private Context context;

        public ListViewAdapterSumTransaksi(List<data_sum_lhp_pojo> data_sum_lhp_pojos, Context context) {
            super (context, R.layout.list_total_pembayaran_lhp, data_sum_lhp_pojos);
            this.data_sum_lhp_pojos = data_sum_lhp_pojos;
            this.context = context;
        }

        public int getCount() {
            return data_sum_lhp_pojos.size();
        }

        public data_sum_lhp_pojo getItem(int position) {
            return data_sum_lhp_pojos.get(position);
        }

        @Override
        public int getViewTypeCount() {
            return getCount();
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
            final ListViewAdapterSumTransaksi.ViewHolder viewHolder;
            data_sum_lhp_pojo movieItem = data_sum_lhp_pojos.get(position);

            if (convertView == null) {

                viewHolder = new ListViewAdapterSumTransaksi.ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.list_total_pembayaran_lhp, parent, false);

                viewHolder.tv_sumamount = (TextView) convertView.findViewById(R.id.tv_sumamount);
                viewHolder.tv_sumtotal = (TextView) convertView.findViewById(R.id.tv_sumtotal);
                viewHolder.tv_sumdiskon = (TextView) convertView.findViewById(R.id.tv_sumdiskon);
                viewHolder.tv_sumfixtotal = (TextView) convertView.findViewById(R.id.tv_sumfixtotal);
                viewHolder.listproduk_layout = (LinearLayout) convertView.findViewById(R.id.listproduk_layout);

                viewHolder.tv_no_customer = (TextView) convertView.findViewById(R.id.tv_no_customer);
                viewHolder.tv_no_std = (TextView) convertView.findViewById(R.id.tv_no_std);
                viewHolder.tv_no_bkb = (TextView) convertView.findViewById(R.id.tv_no_bkb);
                viewHolder.tv_no_do = (TextView) convertView.findViewById(R.id.tv_no_do);

            } else {
                viewHolder = (ListViewAdapterSumTransaksi.ViewHolder) convertView.getTag();
            }

            viewHolder.tv_sumamount.setText(movieItem.getSumamount());

            System.out.println("HASILNY AADALAHH" + viewHolder.tv_sumamount.getText().toString());

            convertView.setTag(viewHolder);
            return convertView;

        }
    }
}