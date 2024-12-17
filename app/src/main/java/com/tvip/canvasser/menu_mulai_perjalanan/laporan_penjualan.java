package com.tvip.canvasser.menu_mulai_perjalanan;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
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
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;
import com.tvip.canvasser.R;
import com.tvip.canvasser.pojo.laporan_penjualan_pojo;
import com.tvip.canvasser.pojo.laporan_penjualan_product_pojo;
import com.tvip.canvasser.pojo.summary_SKU_pojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class laporan_penjualan extends AppCompatActivity {
    ListView listlaporanpenjualan;
    List<laporan_penjualan_pojo> laporanPenjualanPojoList = new ArrayList<>();
    List<summary_SKU_pojo> summarySkuPojos = new ArrayList<>();

    ListViewAdapterLaporanPenjualan adapter;
    ListViewAdapterSummarySKU adapter2;
    SharedPreferences sharedPreferences;
    ScrollView totalpenjualan;
    TabLayout tablayout;

    ListView summarySKU;
    TextView qty;

    private int jumlahSKU = 0;
    private int SKUvalue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_penjualan);
        listlaporanpenjualan = findViewById(R.id.listlaporanpenjualan);
        totalpenjualan = findViewById(R.id.totalpenjualan);
        tablayout = findViewById(R.id.tablayout);

        summarySKU = findViewById(R.id.summarySKU);
        qty = findViewById(R.id.qty);

        tablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab){
                int position = tab.getPosition();
                if(position == 0){
                    listlaporanpenjualan.setVisibility(View.VISIBLE);
                    totalpenjualan.setVisibility(GONE);
                } else if(position == 1){
                    listlaporanpenjualan.setVisibility(GONE);
                    totalpenjualan.setVisibility(VISIBLE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                if(position == 0){
                    listlaporanpenjualan.setVisibility(View.VISIBLE);
                    totalpenjualan.setVisibility(GONE);
                } else if(position == 1){
                    listlaporanpenjualan.setVisibility(GONE);
                    totalpenjualan.setVisibility(VISIBLE);
                }
            }
        });

        getTotalPenjualan();
        getSummaryPenjualan();


    }

    private void getSummaryPenjualan() {
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String nik_baru = sharedPreferences.getString("szDocCall", null);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_SummarySKU?szEmployeeId=" + nik_baru,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            int number = 0;
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");
                            for (int i = 0; i < movieArray.length(); i++) {
                                final JSONObject movieObject = movieArray.getJSONObject(i);
                                final summary_SKU_pojo movieItem = new summary_SKU_pojo(
                                        movieObject.getString("szName"),
                                        movieObject.getString("decQty"));

                                summarySkuPojos.add(movieItem);

                                SKUvalue = Integer.parseInt(movieObject.getString("decQty"));
                                jumlahSKU+=SKUvalue;
                                qty.setText(String.valueOf(jumlahSKU));

                                adapter2 = new ListViewAdapterSummarySKU(summarySkuPojos, getApplicationContext());
                                summarySKU.setAdapter(adapter2);
                                Utility.setListViewHeightBasedOnChildren(summarySKU);


                            }
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

    private void getTotalPenjualan() {
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String nik_baru = sharedPreferences.getString("szDocCall", null);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_LaporanPenjualan?szEmployeeId=" + nik_baru,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            int number = 0;
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");
                            for (int i = 0; i < movieArray.length(); i++) {
                                final JSONObject movieObject = movieArray.getJSONObject(i);
                                final laporan_penjualan_pojo movieItem = new laporan_penjualan_pojo(
                                        movieObject.getString("szDocId"),
                                        movieObject.getString("szCustomerId"),
                                        movieObject.getString("szName"));

                                laporanPenjualanPojoList.add(movieItem);

                                adapter = new ListViewAdapterLaporanPenjualan(laporanPenjualanPojoList, getApplicationContext());
                                listlaporanpenjualan.setAdapter(adapter);


                            }
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

    public class ListViewAdapterLaporanPenjualan extends ArrayAdapter<laporan_penjualan_pojo> {

        private class ViewHolder {
            TextView namatoko;
            ImageView down, up;
            LinearLayout listproduk_layout;
            ListView listproduk;
            List<laporan_penjualan_product_pojo> laporanPenjualanPojoList;
            ListViewAdapterLaporanPenjualanSub adapter;
            TextView total;

        }

        List<laporan_penjualan_pojo> laporan_penjualan_pojos;
        private Context context;

        public ListViewAdapterLaporanPenjualan(List<laporan_penjualan_pojo> laporan_penjualan_pojos, Context context) {
            super(context, R.layout.list_laporan_penjualan, laporan_penjualan_pojos);
            this.laporan_penjualan_pojos = laporan_penjualan_pojos;
            this.context = context;

        }

        public int getCount() {
            return laporan_penjualan_pojos.size();
        }

        public laporan_penjualan_pojo getItem(int position) {
            return laporan_penjualan_pojos.get(position);
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
            laporan_penjualan_pojo movieItem = laporan_penjualan_pojos.get(position);
            if (convertView == null) {
                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.list_laporan_penjualan, parent, false);
                viewHolder.namatoko = (TextView) convertView.findViewById(R.id.namatoko);
                viewHolder.down = (ImageView) convertView.findViewById(R.id.down);
                viewHolder.up = (ImageView) convertView.findViewById(R.id.up);
                viewHolder.listproduk = (ListView) convertView.findViewById(R.id.listproduk);
                viewHolder.listproduk_layout = (LinearLayout) convertView.findViewById(R.id.listproduk_layout);
                viewHolder.total = (TextView) convertView.findViewById(R.id.total);

                viewHolder.laporanPenjualanPojoList = new ArrayList<>();
                convertView.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.laporanPenjualanPojoList.clear();
            StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_LaporanPenjualan_Product?szDocId=" + movieItem.getSzDocId(),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject obj = new JSONObject(response);
                                final JSONArray movieArray = obj.getJSONArray("data");
                                for (int i = 0; i < movieArray.length(); i++) {
                                    final JSONObject movieObject = movieArray.getJSONObject(i);
                                    final laporan_penjualan_product_pojo movieItem = new laporan_penjualan_product_pojo(
                                            movieObject.getString("szName"),
                                            movieObject.getString("decQty"),
                                            movieObject.getString("decPrice"),
                                            movieObject.getString("decAmount"));

                                    viewHolder.laporanPenjualanPojoList.add(movieItem);

                                    int total = 0;
                                    int totalvalue;

                                    String szId = movieObject.getString("decAmount");
                                    String[] parts = szId.split("\\.");
                                    String szIdSlice = parts[0];

                                    totalvalue = Integer.parseInt(szIdSlice);
                                    total+=totalvalue;
                                    viewHolder.total.setText(String.valueOf(total));
                                }

                                viewHolder.adapter = new ListViewAdapterLaporanPenjualanSub(viewHolder.laporanPenjualanPojoList, getApplicationContext());
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
            RequestQueue requestQueue = Volley.newRequestQueue(laporan_penjualan.this);
            requestQueue.add(stringRequest);


            viewHolder.namatoko.setText(movieItem.getSzName());
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

    public static class ListViewAdapterLaporanPenjualanSub extends ArrayAdapter<laporan_penjualan_product_pojo> {
        private List<laporan_penjualan_product_pojo> laporan_penjualan_product_pojos;

        private Context context;

        public ListViewAdapterLaporanPenjualanSub(List<laporan_penjualan_product_pojo> laporan_penjualan_product_pojos, Context context) {
            super(context, R.layout.list_penjualan_sub, laporan_penjualan_product_pojos);
            this.laporan_penjualan_product_pojos = laporan_penjualan_product_pojos;
            this.context = context;
        }

        @SuppressLint("NewApi")
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = LayoutInflater.from(context);

            View listViewItem = inflater.inflate(R.layout.list_penjualan_sub, null, true);

            TextView produk = listViewItem.findViewById(R.id.produk);
            TextView qty = listViewItem.findViewById(R.id.qty);

            laporan_penjualan_product_pojo data = laporan_penjualan_product_pojos.get(position);

            produk.setText(data.getSzName());

            String szId = data.getDecPrice();
            String[] parts = szId.split("\\.");
            String szIdSlice = parts[0];

            qty.setText(data.getDecQty() + " x " + szIdSlice);

            return listViewItem;
        }
    }

    public static class ListViewAdapterSummarySKU extends ArrayAdapter<summary_SKU_pojo> {
        private List<summary_SKU_pojo> summarySkuPojos;

        private Context context;

        public ListViewAdapterSummarySKU(List<summary_SKU_pojo> summarySkuPojos, Context context) {
            super(context, R.layout.list_penjualan_sub, summarySkuPojos);
            this.summarySkuPojos = summarySkuPojos;
            this.context = context;
        }

        @SuppressLint("NewApi")
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = LayoutInflater.from(context);

            View listViewItem = inflater.inflate(R.layout.list_penjualan_sub, null, true);

            TextView produk = listViewItem.findViewById(R.id.produk);
            TextView qty = listViewItem.findViewById(R.id.qty);

            summary_SKU_pojo data = summarySkuPojos.get(position);

            produk.setText(data.getSzName());
            qty.setText(data.getDecQty());


            return listViewItem;
        }
    }
}