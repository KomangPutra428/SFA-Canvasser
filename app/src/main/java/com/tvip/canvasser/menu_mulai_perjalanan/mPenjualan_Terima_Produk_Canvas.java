package com.tvip.canvasser.menu_mulai_perjalanan;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.google.android.material.textfield.TextInputLayout;
import com.tvip.canvasser.Perangkat.HttpsTrustManager;
import com.tvip.canvasser.R;
import com.tvip.canvasser.pojo.data_terima_produk_canvas;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

public class mPenjualan_Terima_Produk_Canvas extends AppCompatActivity {

    String id_std, id_driver, currentDate;
    EditText tanggalkirim;


    ListView listproductpenjualan, listdocdoitem, listdocdoitemprice;

    static List<data_terima_produk_canvas> data_terima_produk_canvasList = new ArrayList<>();
    static mPenjualan_Terima_Produk_Canvas.ListViewAdapteProductPenjualan adapter;


    EditText edt_jumlah_tunai, edt_jumlah_transfer;

    TextInputLayout txt_input_jumlah_transfer, txt_input_jumlah_tunai;
    LinearLayout linear_keterangan_photo;

    private SimpleDateFormat dateFormatter;
    private Calendar date;
    String idstd, idcustomer, nomor_do;
    SearchView cariproduct;

    SharedPreferences sharedPreferences;

    AutoCompleteTextView pilihpembayaran;
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

    TextView total_jenis_pembayaran, tv_submit_loading,
            tv_terima_produk, tv_totalharga_format_rupiah, tv_totaldiskon_format_rupiah,
            tv_total_jenis_pembayaran_rupiah,tv_total_jenis_pembayaran_text;

    Button bt_submit_berhasil;
    RelativeLayout relative_foto_bukti_transfer;

    public static final int PERMISSION_BLUETOOTH = 1;

    private final Locale locale = new Locale("id", "ID");
    private final DateFormat df = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss", locale);
    private final NumberFormat nf = NumberFormat.getCurrencyInstance(locale);

    ArrayList<String> array_pilihan_customer;

    Button batal, lanjutkan, bt_camera, bt_konfirmasi, bt_camera_bukti_transfer;
    TextView delivery_order, tv_totalharga, tv_totaldiskon, tv_status;
    String encodeImageString, string_harga_satuan, string_total_harga_row, string_diskon_tiv, string_diskon_distributor, string_diskon_internal;
    AutoCompleteTextView act_pilihCustomer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mpenjualan_terima_produk_canvas);
        HttpsTrustManager.allowAllSSL();

        id_std = getIntent().getStringExtra("idStd");
        id_driver = getIntent().getStringExtra("szEmployeeId");

        listproductpenjualan = findViewById(R.id.listproductpenjualan);

        Toast.makeText(mPenjualan_Terima_Produk_Canvas.this, "idStd = " + id_std, Toast.LENGTH_SHORT).show();
        Toast.makeText(mPenjualan_Terima_Produk_Canvas.this, "idDriver = " + id_driver, Toast.LENGTH_SHORT).show();

        tanggalkirim = findViewById(R.id.tanggalkirim);

        currentDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault()).format(new Date());
        String currentDate2 = new SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault()).format(new Date());
        tanggalkirim.setText(currentDate2);

        edt_jumlah_transfer = findViewById(R.id.edt_jumlah_transfer);
        edt_jumlah_tunai = findViewById(R.id.edt_jumlah_tunai);
        tv_terima_produk = findViewById(R.id.tv_terima_produk);

        dateFormatter = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
        cariproduct = findViewById(R.id.cariproduct);
        img = (ImageView) findViewById(R.id.img_background);
        img_bukti_transfer = (ImageView) findViewById(R.id.img_background_bukti_transfer);

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

        tv_no_std.setText(id_std);

        act_pilihCustomer = findViewById(R.id.autoCompleteTextView_pilihcustomer);

        String[] parts = id_std.split("-");
        String idbranch = parts[0];

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


        StringRequest pilih_customer = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_pilihancustomer?id_branch=" + idbranch,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("status").equals("true")) {

                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    //parameter jika mengambul data dari url url_get_data_checklist_stagging
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    //String string_nomor_polisi = jsonObject1.getString("license_number");
                                    //parameter jika mengambul data dari url url_get_data_variant_transportir_checklist_stagging
                                    String string_nama_customer = jsonObject1.getString("nama_customer");
                                    String string_id_customer = jsonObject1.getString("id_customer");

                                    array_pilihan_customer.add(string_nama_customer+" ,"+string_id_customer);
                                }
                            }
                            act_pilihCustomer.setAdapter(new ArrayAdapter<String>(mPenjualan_Terima_Produk_Canvas.this, android.R.layout.simple_expandable_list_item_1, array_pilihan_customer));
                            act_pilihCustomer.setThreshold(1);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(mPenjualan_Terima_Produk_Canvas.this, "Ada kesalahan, silahkan coba kembali", Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(mPenjualan_Terima_Produk_Canvas.this);
        requestQueue.add(pilih_customer);


        act_pilihCustomer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
                String nama_customer = (String) parent.getItemAtPosition(position);

                String[] parts = nama_customer.split(",");
                String nama_cs = parts[0];
                String id_cs = parts[1];

                tv_no_customer.setText(id_cs);



                //TODO Do something with the selected text
            }
        });

        get_produk_canvas();


    }

    public void get_produk_canvas(){

        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_Detail_terima_produk_canvas",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {
                            int number = 0;
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data_produk");
                            for (int i = 0; i < movieArray.length(); i++) {
                                final JSONObject movieObject = movieArray.getJSONObject(i);
                                final data_terima_produk_canvas movieItem = new data_terima_produk_canvas(
                                        movieObject.getString("szProductId"),
                                        movieObject.getString("parameter_ppn"),
                                        movieObject.getString("szName"),
                                        movieObject.getString("decPrice"),
                                        movieObject.getString("szOrderItemtypeId"),
                                        movieObject.getString("szUomId"), //digunakan untuk qty asli, tanpa adanya perubahan
                                        movieObject.getString("id_customer"),
                                        movieObject.getString("qty_awal"),
                                        movieObject.getString("qty_terjual")); //digunakan untuk edit qty

//                                nomor_do = movieObject.getString("szDocId");
//                                delivery_order.setText(nomor_do);

                                data_terima_produk_canvasList.add(movieItem);

                                adapter = new mPenjualan_Terima_Produk_Canvas.ListViewAdapteProductPenjualan(data_terima_produk_canvasList, getApplicationContext());
                                listproductpenjualan.setAdapter(adapter);

                            }


                        } catch(JSONException e){
                            e.printStackTrace();

                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(mPenjualan_Terima_Produk_Canvas.this, "FAIL", Toast.LENGTH_SHORT).show();

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

                params.put("id_customer", "336-0017031");
                params.put("id_std", "336-0079665");

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
        RequestQueue requestQueue2 = Volley.newRequestQueue(mPenjualan_Terima_Produk_Canvas.this);
        requestQueue2.add(stringRequest2);

    }

    public class ListViewAdapteProductPenjualan extends ArrayAdapter<data_terima_produk_canvas> {

        private class ViewHolder {
            TextView namaproduk, qty_order, total_harga, harga_satuan, diskon_tiv, diskon_distributor, diskon_internal;
//            TextView intItemNumber, szOrderItemTypeId, bTaxable, szTaxId, decTaxRate, decTax, decDpp, tv_qty_produk;
//            TextView total_harga_row_format_rupiah, harga_satuan_format_rupiah, diskon_tiv_format_rupiah, diskon_distributor_format_rupiah,
//                    diskon_internal_format_rupiah;
//
//            LinearLayout linear_detail_terima_produk;
//            RelativeLayout buttonExpand;
//            ImageButton bt_plus, bt_minus, bt_edit_qty_produk;
//            String string_qty_produk, string_qty_order_produk_asli, string_harga_satuan,
//                    string_total_harga_row, string_diskon_internal, string_diskon_tiv,
//                    string_diskon_distributor;
//
//            TextView qty_order_asli;
//            TextView decTax_setelah_edit, decDpp_setelah_edit;
//            TextView tax;
        }

        List<data_terima_produk_canvas> data_terima_produk_canvasList;
        private Context context;

        public ListViewAdapteProductPenjualan(List<data_terima_produk_canvas> data_terima_produk_canvasList, Context context) {
            super(context, R.layout.list_terima_produk, data_terima_produk_canvasList);
            this.data_terima_produk_canvasList = data_terima_produk_canvasList;
            this.context = context;
        }

        public int getCount() {
            return data_terima_produk_canvasList.size();
        }

        public data_terima_produk_canvas getItem(int position) {
            return data_terima_produk_canvasList.get(position);
        }

        @Override
        public int getViewTypeCount() {
            int count;
            if (data_terima_produk_canvasList.size() > 0) {
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
            final mPenjualan_Terima_Produk_Canvas.ListViewAdapteProductPenjualan.ViewHolder viewHolder;
            data_terima_produk_canvas data = getItem(position);

            if (convertView == null) {
                viewHolder = new mPenjualan_Terima_Produk_Canvas.ListViewAdapteProductPenjualan.ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.list_terima_produk, parent, false);

                viewHolder.namaproduk = (TextView) convertView.findViewById(R.id.namaproduk);

                convertView.setTag(viewHolder);

            } else {
                viewHolder = (mPenjualan_Terima_Produk_Canvas.ListViewAdapteProductPenjualan.ViewHolder) convertView.getTag();
            }

            viewHolder.namaproduk.setText(data.getSzName());

            listproductpenjualan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                }
            });


            return convertView;
        }

    }


    private String formatRupiah(Double number){
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        return formatRupiah.format(number);
    }

}