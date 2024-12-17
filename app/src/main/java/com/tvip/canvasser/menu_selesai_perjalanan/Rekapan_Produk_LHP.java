package com.tvip.canvasser.menu_selesai_perjalanan;

import static com.tvip.canvasser.menu_selesai_perjalanan.detail_summary_transaksi_lhp_new.ritase;

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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tvip.canvasser.R;
import com.tvip.canvasser.pojo.data_rekap_produk_lhp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Rekapan_Produk_LHP extends AppCompatActivity {

    String string_nomor_std;

    ListView list_rekap_lhp;
    List<data_rekap_produk_lhp> data_rekap_produk_lhps = new ArrayList<>();
    ListViewgetRekapLHP adapter_rekapproduk;
    SharedPreferences sharedPreferences;
    String employee_id, idbranch_driver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rekapan_produk_lhp);
        list_rekap_lhp = findViewById(R.id.list_rekap_produk_lhp);
        string_nomor_std = getIntent().getStringExtra("nomorstd");

        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        employee_id = sharedPreferences.getString("szDocCall", null);
        String[] nikdriver = employee_id.split("-");
        idbranch_driver = nikdriver[0];

        Toast.makeText(Rekapan_Produk_LHP.this, "TEST " + string_nomor_std, Toast.LENGTH_SHORT).show();

        getRekapLHP();
    }

    private void getRekapLHP() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_rekapproduklhp?id_std=" + string_nomor_std + "&depo=" + idbranch_driver, //mulai_perjalanan.id_pelanggan,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            int number = 0;
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");

                            for (int i = 0; i < movieArray.length(); i++) {
                                final JSONObject movieObject = movieArray.getJSONObject(i);
                                final data_rekap_produk_lhp movieItem = new data_rekap_produk_lhp(
                                        movieObject.getString("id_produk"),
                                        movieObject.getString("szName"),
                                        movieObject.getString("qtySO"),
                                        movieObject.getString("amountSO"),
                                        movieObject.getString("qtyDO"),
                                        movieObject.getString("amountDO"));

                                data_rekap_produk_lhps.add(movieItem);
                                adapter_rekapproduk = new ListViewgetRekapLHP(data_rekap_produk_lhps, getApplicationContext());
                                list_rekap_lhp.setAdapter(adapter_rekapproduk);

                                if(movieObject.getString("qtySO").equals("null")){
                                    data_rekap_produk_lhps.remove(movieItem);
                                    adapter_rekapproduk.notifyDataSetChanged();
                                }
                            }


                        } catch(JSONException e){
                            e.printStackTrace();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (error instanceof TimeoutError || error instanceof NoConnectionError)     {
                            Toast.makeText(Rekapan_Produk_LHP.this, "Connection Error", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(Rekapan_Produk_LHP.this, "Auth Failure Error", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ServerError) {
                            Toast.makeText(Rekapan_Produk_LHP.this, "Server Error", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(Rekapan_Produk_LHP.this, "Network Error", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ParseError) {
                            Toast.makeText(Rekapan_Produk_LHP.this, "Parse Error", Toast.LENGTH_SHORT).show();
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
                        50000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                ));

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }


    public class ListViewgetRekapLHP extends ArrayAdapter<data_rekap_produk_lhp> {

        private class ViewHolder {
            TextView tv_idproduk,  tv_namaproduk, tv_qtyawal, tv_totalhargaqtyawal,
                    tv_qtyterjual, tv_totalhargaqtyterjual;
        }

        List<data_rekap_produk_lhp> data_rekap_produk_lhps;
        private Context context;

        public ListViewgetRekapLHP(List<data_rekap_produk_lhp> data_rekap_produk_lhps, Context context) {
            super (context, R.layout.layout_rekap_produk_lhp, data_rekap_produk_lhps);
            this.data_rekap_produk_lhps = data_rekap_produk_lhps;
            this.context = context;
        }

        public int getCount() {
            return data_rekap_produk_lhps.size();
        }

        public data_rekap_produk_lhp getItem(int position) {
            return data_rekap_produk_lhps.get(position);
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
            final ListViewgetRekapLHP.ViewHolder viewHolder;
            data_rekap_produk_lhp movieItem = data_rekap_produk_lhps.get(position);

            if (convertView == null) {

                viewHolder = new ListViewgetRekapLHP.ViewHolder();

                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.layout_rekap_produk_lhp, parent, false);

                viewHolder.tv_idproduk = (TextView) convertView.findViewById(R.id.tv_id_produk_rekap_lhp);
                viewHolder.tv_namaproduk = (TextView) convertView.findViewById(R.id.tv_nama_produk_rekap_lhp);
                viewHolder.tv_qtyawal = (TextView) convertView.findViewById(R.id.tv_qty_awal_rekap_lhp);
                viewHolder.tv_totalhargaqtyawal = (TextView) convertView.findViewById(R.id.tv_total_harga_qty_awal_rekap_lhp);
                viewHolder.tv_qtyterjual = (TextView) convertView.findViewById(R.id.tv_qty_terjual_rekap_lhp);
                viewHolder.tv_totalhargaqtyterjual = (TextView) convertView.findViewById(R.id.tv_total_harga_qty_terjual_rekap_lhp);

            } else {
                viewHolder = (ListViewgetRekapLHP.ViewHolder) convertView.getTag();
            }

            StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_ProductAwal?ritase=" + ritase + "&driverId=" + employee_id +"&szProductId=" + movieItem.getIdproduk(), //mulai_perjalanan.id_pelanggan,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                int number = 0;
                                JSONObject obj = new JSONObject(response);
                                final JSONArray movieArray = obj.getJSONArray("data");

                                for (int i = 0; i < movieArray.length(); i++) {
                                    final JSONObject movieObject = movieArray.getJSONObject(i);

                                    String qtyTerjual = movieObject.getString("decQtyDeliveredDO");
                                    String[] partsTerjual = qtyTerjual.split("\\.");
                                    String szIdSliceTerjual = partsTerjual[0];

                                    String qty = movieObject.getString("decQty");
                                    String[] partsqty = qty.split("\\.");
                                    String szIdSliceQty = partsqty[0];

                                    int totalawal = Integer.parseInt(szIdSliceTerjual) + Integer.parseInt(szIdSliceQty);

                                    viewHolder.tv_qtyawal.setText(String.valueOf(totalawal));
                                    viewHolder.tv_qtyterjual.setText(szIdSliceTerjual);

                                }

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
                            50000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                    ));

            RequestQueue requestQueue = Volley.newRequestQueue(Rekapan_Produk_LHP.this);
            requestQueue.add(stringRequest);

            StringRequest stringRequest2 = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_TotalPerSTD?driver="+employee_id+"&ritase="+ ritase+"&szProductId="+movieItem.getIdproduk(), //mulai_perjalanan.id_pelanggan,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                int number = 0;
                                JSONObject obj = new JSONObject(response);
                                final JSONArray movieArray = obj.getJSONArray("data");

                                for (int i = 0; i < movieArray.length(); i++) {
                                    final JSONObject movieObject = movieArray.getJSONObject(i);

                                    String qtyTerjual = movieObject.getString("Total_Product");
                                    String[] partsTerjual = qtyTerjual.split("\\.");
                                    String szIdSliceTerjual = partsTerjual[0];

                                    if(movieObject.getString("Total_Product").equals("null")){
                                        viewHolder.tv_totalhargaqtyterjual.setText(formatRupiah(Double.valueOf(0)));
                                        data_rekap_produk_lhps.remove(position);
                                        adapter_rekapproduk.notifyDataSetChanged();
                                    } else {
                                        viewHolder.tv_totalhargaqtyterjual.setText(formatRupiah(Double.valueOf(szIdSliceTerjual)));

                                    }

                                }

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

            stringRequest2.setRetryPolicy(
                    new DefaultRetryPolicy(
                            50000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                    ));

            RequestQueue requestQueue2 = Volley.newRequestQueue(Rekapan_Produk_LHP.this);
            requestQueue2.add(stringRequest2);

            String Qtyawal = movieItem.getQtyawal();
            String[] str_Qtyawal = Qtyawal.split("\\.");
            String qty_awal = str_Qtyawal[0];

            String totalqtyawal = movieItem.getTotalhargaqtyawal();
            String[] str_totalqtyawal = totalqtyawal.split("\\.");
            String total_qty_awal = str_totalqtyawal[0];

            String qtyterjual = movieItem.getQtyterjual();
            String[] str_qtyterjual = qtyterjual.split("\\.");
            String qty_terjual = str_qtyterjual[0];

            String totalqtyterjual = movieItem.getTotalhargaqtyterjual();
            String[] str_totalqtyterjual = totalqtyterjual.split("\\.");
            String total_qty_terjual = str_totalqtyterjual[0];

            String string_qty_awal = ((qty_awal.equals("null")) ? "0" : qty_awal);
            String string_total_harga_qty_awal = ((total_qty_awal.equals("null")) ? formatRupiah(Double.valueOf("0")) : formatRupiah(Double.valueOf(total_qty_awal)));
            String string_total_harga_qty_terjual = formatRupiah(Double.valueOf(total_qty_terjual));

            viewHolder.tv_idproduk.setText(movieItem.getIdproduk());
            viewHolder.tv_namaproduk.setText(movieItem.getNamaproduk());
           // viewHolder.tv_qtyawal.setText(string_qty_awal);
            viewHolder.tv_totalhargaqtyawal.setText(string_total_harga_qty_awal);
            viewHolder.tv_qtyterjual.setText(qty_terjual);


            convertView.setTag(viewHolder);
            return convertView;

        }

    }


    private String formatRupiah(Double number){
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        return formatRupiah.format(number);
    }
}