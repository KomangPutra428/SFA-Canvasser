package com.tvip.canvasser.menu_mulai_perjalanan;

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
import android.widget.Button;
import android.widget.CheckBox;
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
import com.tvip.canvasser.R;
import com.tvip.canvasser.pojo.data_posm_foto_pojo;
import com.tvip.canvasser.pojo.data_posm_pojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class cek_posm extends AppCompatActivity {
    ListView list_cekposm;
    SharedPreferences sharedPreferences;
    List<data_posm_pojo> dataPosmPojoList = new ArrayList<>();
    Button batal, lanjutkan;
    ListViewAdapterCekPosm adapter;
    static List<data_posm_foto_pojo> dataPosmFotoPojoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cek_posm);
        list_cekposm = findViewById(R.id.list_cekposm);
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
                for(int i = 0; i <= dataPosmPojoList.size() - 1;i++){
                    if(adapter.getItem(i).getSelected() == true){
                        dataPosmFotoPojoList.add(new data_posm_foto_pojo(adapter.getItem(i).getSzId(), adapter.getItem(i).getSzName(), adapter.getItem(i).getQty(), "0", adapter.getItem(i).getSzUomId()));
                    }

                    if(i == dataPosmPojoList.size() -1){
                        Intent fotomateri = new Intent(getBaseContext(), foto_materi.class);
                        fotomateri.putExtra("POSM", "cek");
                        startActivity(fotomateri);
                    }
                }
            }
        });

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_Product?jenis=POSM",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            int number = 0;
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");
                            for (int i = 0; i < movieArray.length(); i++) {
                                final JSONObject movieObject = movieArray.getJSONObject(i);
                                final data_posm_pojo movieItem = new data_posm_pojo(
                                        movieObject.getString("szId"),
                                        movieObject.getString("szName"),
                                        movieObject.getString("szUomId"));

                                dataPosmPojoList.add(movieItem);

                                adapter = new ListViewAdapterCekPosm(dataPosmPojoList, getApplicationContext());
                                list_cekposm.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

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

    public class ListViewAdapterCekPosm extends ArrayAdapter<data_posm_pojo> {

        private class ViewHolder {
            TextView produk;
            CheckBox checkbox;

        }

        List<data_posm_pojo> data_posm_pojos;
        private Context context;

        public ListViewAdapterCekPosm(List<data_posm_pojo> data_posm_pojos, Context context) {
            super(context, R.layout.list_cekposm, data_posm_pojos);
            this.data_posm_pojos = data_posm_pojos;
            this.context = context;

        }

        public int getCount() {
            return data_posm_pojos.size();
        }

        public data_posm_pojo getItem(int position) {
            return data_posm_pojos.get(position);
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
            data_posm_pojo movieItem = data_posm_pojos.get(position);
            if (convertView == null) {
                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.list_cekposm, parent, false);
                viewHolder.produk = (TextView) convertView.findViewById(R.id.produk);
                viewHolder.checkbox = (CheckBox) convertView.findViewById(R.id.checkbox);


                convertView.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.produk.setText(movieItem.getSzName());
            viewHolder.checkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(getItem(position).getSelected() == false){
                        data_posm_pojos.get(position).setSelected(true);
                    } else {
                        data_posm_pojos.get(position).setSelected(false);
                    }
//                    if (data_posm_pojos.get(position).getSelected()){
//                    }
//                    else {

//                    }

                }
            });
            return convertView;
        }
    }
}