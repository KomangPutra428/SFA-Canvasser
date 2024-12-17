package com.tvip.canvasser.menu_mulai_perjalanan;

import static com.tvip.canvasser.menu_mulai_perjalanan.menu_pelanggan.no_surat;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.tvip.canvasser.R;
import com.tvip.canvasser.pojo.data_produk_penjualan_pojo;
import com.tvip.canvasser.pojo.total_penjualan_pojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class product_penjualan extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    static ListView listproductpenjualan;
    static List<data_produk_penjualan_pojo>data_produk_penjualan_pojos = new ArrayList<>();
    static List<total_penjualan_pojo>totalPenjualanPojos = new ArrayList<>();
    static ListViewAdapteProductPenjualan adapter;
    SweetAlertDialog pDialog;

    Button batal, lanjutkan;

    private SimpleDateFormat dateFormatter;
    private Calendar date;
    String idstd, idcustomer;

    SearchView cariproduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_penjualan);
        listproductpenjualan = findViewById(R.id.listproductpenjualan);
        dateFormatter = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
        cariproduct = findViewById(R.id.cariproduct);

        System.out.println("HASIL ID STD = " + getIntent().getStringExtra("idStd"));
        System.out.println("HASIL ID CUSTOMER = " + getIntent().getStringExtra("idCustomer"));
        idstd = getIntent().getStringExtra("idStd");
        idcustomer = getIntent().getStringExtra("idCustomer");

        batal = findViewById(R.id.batal);
        lanjutkan = findViewById(R.id.lanjutkan);

        batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        lanjutkan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for(int i = 0; i < data_produk_penjualan_pojos.size();i++){

                    if (!(adapter.getItem(i).getExpired_date() == null)){
                        Double harga = Double.valueOf(adapter.getItem(i).getDecPrice());
                        int total_harga = harga.intValue() * Integer.parseInt(adapter.getItem(i).getStock_qty());
                        totalPenjualanPojos.add(new total_penjualan_pojo(
                                adapter.getItem(i).getSzId(),
                                adapter.getItem(i).getSzName(),
                                adapter.getItem(i).getDecPrice(),
                                adapter.getItem(i).getStock_qty(),
                                adapter.getItem(i).getDisplay(),
                                String.valueOf(total_harga),
                                adapter.getItem(i).getDisc_total(),
                                adapter.getItem(i).getStock(),
                                adapter.getItem(i).getExpired(),
                                adapter.getItem(i).getSzUomId(),
                                adapter.getItem(i).getDisc_distributor(),
                                adapter.getItem(i).getDisc_internal(),
                                adapter.getItem(i).getDisc_principle()));
                    }

                    if(i == data_produk_penjualan_pojos.size() -1){
                        Intent intent = new Intent(getBaseContext(), summary_order.class);
                        startActivity(intent);
                    }
                }
            }
        });



        pDialog = new SweetAlertDialog(product_penjualan.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Harap Menunggu");
        pDialog.setCancelable(false);
        pDialog.show();

        String[] parts = no_surat.split("-");
        String restnomor = parts[0];
        String restnomorbaru = restnomor.replace(" ", "");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_Driver_Detail_Penjualan?id_customer="+idcustomer+"&id_std="+idstd,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            int number = 0;
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");
                            for (int i = 0; i < movieArray.length(); i++) {
                                final JSONObject movieObject = movieArray.getJSONObject(i);
                                final data_produk_penjualan_pojo movieItem = new data_produk_penjualan_pojo(
                                        movieObject.getString("id_produk"),
                                        movieObject.getString("nama_produk"),
                                        movieObject.getString("qty_produk"),
                                        movieObject.getString("totalharga"));

                                data_produk_penjualan_pojos.add(movieItem);

                                adapter = new ListViewAdapteProductPenjualan(data_produk_penjualan_pojos, getApplicationContext());
                                listproductpenjualan.setAdapter(adapter);

                                cariproduct.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                                    @Override
                                    public boolean onQueryTextSubmit(String text) {
                                        return false;
                                    }

                                    @Override
                                    public boolean onQueryTextChange(String newText) {
                                        adapter.filter(newText);
                                        return true;
                                    }
                                });
                            }


                            pDialog.dismissWithAnimation();

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

    public class ListViewAdapteProductPenjualan extends ArrayAdapter<data_produk_penjualan_pojo> {

        private class ViewHolder {
            TextView namaproduk, qty_order;
            ImageButton refresh;
            MaterialButton tambahproduk;
        }

        List<data_produk_penjualan_pojo> data_produk_penjualan_pojos;
        ArrayList<data_produk_penjualan_pojo> data_produk_penjualanList;
        private Context context;

        public ListViewAdapteProductPenjualan(List<data_produk_penjualan_pojo> data_produk_penjualan_pojos, Context context) {
            super(context, R.layout.list_penjualanproduct, data_produk_penjualan_pojos);
            this.data_produk_penjualan_pojos = data_produk_penjualan_pojos;
            this.data_produk_penjualanList = new ArrayList<data_produk_penjualan_pojo>();
            this.data_produk_penjualanList.addAll(data_produk_penjualan_pojos);
            this.context = context;

        }



        public int getCount() {
            return data_produk_penjualan_pojos.size();
        }

        public data_produk_penjualan_pojo getItem(int position) {
            return data_produk_penjualan_pojos.get(position);
        }


        @Override
        public int getViewTypeCount() {
            int count;
            if (data_produk_penjualan_pojos.size() > 0) {
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
            data_produk_penjualan_pojo data = getItem(position);
            if (convertView == null) {
                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.list_penjualanproduct, parent, false);

                viewHolder.namaproduk = (TextView) convertView.findViewById(R.id.namaproduk);
                viewHolder.qty_order = (TextView) convertView.findViewById(R.id.qty_order);

                viewHolder.refresh = (ImageButton) convertView.findViewById(R.id.refresh);
                viewHolder.tambahproduk = (MaterialButton) convertView.findViewById(R.id.tambahproduk);


                convertView.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.namaproduk.setText(data.getSzName());
            if(data.getStock_qty() == null){
                viewHolder.qty_order.setText("Order : 0");
            } else {
                viewHolder.qty_order.setText("Order : " + data.getStock_qty());
            }

            viewHolder.tambahproduk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(getApplicationContext(), sisa_stock.class);
                    intent.putExtra("list", String.valueOf(position));
                    intent.putExtra("nama_barang", data.getSzName());
                    intent.putExtra("uang", data.getDecPrice());
                    startActivity(intent);
                }
            });

            viewHolder.refresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    product_penjualan.adapter.getItem(position).setStock(null);
                    product_penjualan.adapter.getItem(position).setDisplay(null);
                    product_penjualan.adapter.getItem(position).setExpired(null);
                    product_penjualan.adapter.getItem(position).setExpired_date(null);

                    product_penjualan.adapter.getItem(position).setStock_qty(null);
                    product_penjualan.adapter.getItem(position).setDisc_principle(null);
                    product_penjualan.adapter.getItem(position).setDisc_distributor(null);
                    product_penjualan.adapter.getItem(position).setDisc_internal(null);
                    product_penjualan.adapter.getItem(position).setDisc_total(null);

                    viewHolder.qty_order.setText("Order : 0");

                    product_penjualan.listproductpenjualan.setAdapter(product_penjualan.adapter);
                    product_penjualan.adapter.notifyDataSetChanged();

                }
            });

            return convertView;
        }

        public void filter(String charText) {
            charText = charText.toLowerCase(Locale.getDefault());
            data_produk_penjualan_pojos.clear();
            if (charText.length() == 0) {
                data_produk_penjualan_pojos.addAll(data_produk_penjualanList);
            } else {
                for (data_produk_penjualan_pojo wp : data_produk_penjualanList) {
                    if (wp.getSzName().toLowerCase(Locale.getDefault()).contains(charText)) {
                        data_produk_penjualan_pojos.add(wp);
                    }
                }
            }
            notifyDataSetChanged();
        }



    }

    @Override
    public void onBackPressed() {
        adapter.clear();
        finish();
        super.onBackPressed();
    }




}