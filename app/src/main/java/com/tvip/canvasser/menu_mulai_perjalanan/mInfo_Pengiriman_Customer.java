package com.tvip.canvasser.menu_mulai_perjalanan;

import static com.tvip.canvasser.menu_mulai_perjalanan.pengiriman_info.convertFormat;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.tvip.canvasser.pojo.data_penjualan_terakhir_pojo;
import com.tvip.canvasser.pojo.data_product_pojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class mInfo_Pengiriman_Customer extends AppCompatActivity {
    ListView list_pengiriman_terakhir;
    List<data_penjualan_terakhir_pojo> dataPenjualanTerakhirPojos = new ArrayList<>();
    mInfo_Pengiriman_Customer.ListViewAdapterDaftarKunjunganMain adapter;

    String idcustomer, nik_driver, string_idcustomer;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minfo_pengiriman_customer);
        HttpsTrustManager.allowAllSSL();


        list_pengiriman_terakhir = findViewById(R.id.list_pengiriman_terakhir);

        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        nik_driver = sharedPreferences.getString("szDocCall", null);

        Intent i = getIntent();
        string_idcustomer = i.getStringExtra("idCustomer");

        getPenjualanTerakhir();

    }

    private void getPenjualanTerakhir() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_infopengiriman_driver?customerId="+ string_idcustomer +"&driver=" + nik_driver,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            int number = 0;
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");
                            for (int i = 0; i < movieArray.length(); i++) {
                                final JSONObject movieObject = movieArray.getJSONObject(i);
                                final data_penjualan_terakhir_pojo movieItem = new data_penjualan_terakhir_pojo(
                                        movieObject.getString("nomor_bkb"),
                                        movieObject.getString("dtmDoc"),
                                        movieObject.getString("customerId"),
                                        movieObject.getString("driver"),
                                        movieObject.getString("total_harga"),
                                        movieObject.getString("nomor_do"));

                                dataPenjualanTerakhirPojos.add(movieItem);
                            }

                            adapter = new mInfo_Pengiriman_Customer.ListViewAdapterDaftarKunjunganMain(dataPenjualanTerakhirPojos, getApplicationContext());
                            list_pengiriman_terakhir.setAdapter(adapter);
                            adapter.notifyDataSetChanged();


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
                        500000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                ));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public class ListViewAdapterDaftarKunjunganMain extends ArrayAdapter<data_penjualan_terakhir_pojo> {

        private class ViewHolder {
            TextView no_so, tanggal, no_do, no_bkb;
            ImageView down, up;
            LinearLayout listproduk_layout;
            ListView listproduk;
            pengiriman_info.ListViewAdapterDaftarKunjunganMainSub adapter;

            List<data_product_pojo> dataProductPojos = new ArrayList<>();


        }

        List<data_penjualan_terakhir_pojo> data_penjualan_terakhir_pojos;
        private Context context;

        public ListViewAdapterDaftarKunjunganMain(List<data_penjualan_terakhir_pojo> data_penjualan_terakhir_pojos, Context context) {
            super(context, R.layout.list_pengiriman_terakhir, data_penjualan_terakhir_pojos);
            this.data_penjualan_terakhir_pojos = data_penjualan_terakhir_pojos;
            this.context = context;

        }

        public int getCount() {
            return data_penjualan_terakhir_pojos.size();
        }

        public data_penjualan_terakhir_pojo getItem(int position) {
            return data_penjualan_terakhir_pojos.get(position);
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
            final mInfo_Pengiriman_Customer.ListViewAdapterDaftarKunjunganMain.ViewHolder viewHolder;
            data_penjualan_terakhir_pojo movieItem = data_penjualan_terakhir_pojos.get(position);
            if (convertView == null) {
                viewHolder = new mInfo_Pengiriman_Customer.ListViewAdapterDaftarKunjunganMain.ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());

                convertView = inflater.inflate(R.layout.list_pengiriman_terakhir, parent, false);

                viewHolder.tanggal = (TextView) convertView.findViewById(R.id.tanggal);
                viewHolder.no_so = (TextView) convertView.findViewById(R.id.no_so);
                viewHolder.no_do = (TextView) convertView.findViewById(R.id.no_do);
                viewHolder.down = (ImageView) convertView.findViewById(R.id.down);
                viewHolder.up = (ImageView) convertView.findViewById(R.id.up);
                viewHolder.listproduk_layout = (LinearLayout) convertView.findViewById(R.id.listproduk_layout);
                viewHolder.listproduk = (ListView) convertView.findViewById(R.id.listproduk);
                viewHolder.no_bkb = (TextView) convertView.findViewById(R.id.no_bkb);

                viewHolder.dataProductPojos = new ArrayList<>();

                convertView.setTag(viewHolder);

            } else {
                viewHolder = (mInfo_Pengiriman_Customer.ListViewAdapterDaftarKunjunganMain.ViewHolder) convertView.getTag();
            }
            viewHolder.dataProductPojos.clear();

            StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_infopengirimanproduk_driver?nomor_do=" + movieItem.getSzDocCallId(),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                int number = 0;
                                JSONObject obj = new JSONObject(response);
                                final JSONArray movieArray = obj.getJSONArray("data");
                                for (int i = 0; i < movieArray.length(); i++) {
                                    final JSONObject movieObject = movieArray.getJSONObject(i);
                                    final data_product_pojo movieItem = new data_product_pojo(
                                            movieObject.getString("szProductId"),
                                            movieObject.getString("decQty"));

                                    viewHolder.dataProductPojos.add(movieItem);
                                }

                                viewHolder.adapter = new pengiriman_info.ListViewAdapterDaftarKunjunganMainSub(viewHolder.dataProductPojos, getApplicationContext());
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
            RequestQueue requestQueue = Volley.newRequestQueue(mInfo_Pengiriman_Customer.this);
            requestQueue.add(stringRequest);

            viewHolder.tanggal.setText(convertFormat(movieItem.getDtmDoc()));
            viewHolder.no_bkb.setText(movieItem.getSzDocId());
            viewHolder.no_do.setText(movieItem.getSzDocCallId());

            viewHolder.down.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewHolder.down.setVisibility(View.GONE);
                    viewHolder.up.setVisibility(View.VISIBLE);
                    viewHolder.listproduk_layout.setVisibility(View.VISIBLE);
                }
            });

            viewHolder.up.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewHolder.down.setVisibility(View.VISIBLE);
                    viewHolder.up.setVisibility(View.GONE);
                    viewHolder.listproduk_layout.setVisibility(View.GONE);
                }
            });
            return convertView;
        }
    }


}