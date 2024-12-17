package com.tvip.canvasser.menu_persiapan;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.google.android.material.appbar.MaterialToolbar;
import com.tvip.canvasser.Perangkat.HttpsTrustManager;
import com.tvip.canvasser.R;
import com.tvip.canvasser.pojo.data_pelanggan_pojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class outlet_kritis extends AppCompatActivity {
    ListView listoutletkritis;
    ListViewAdapterOutletKritis adapter;
    MaterialToolbar topAppBar;
    List<data_pelanggan_pojo> dataPelangganPojos = new ArrayList<>();
    SharedPreferences sharedPreferences;
    SearchView caripelanggan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HttpsTrustManager.allowAllSSL();
        setContentView(R.layout.activity_outlet_kritis);
        listoutletkritis = findViewById(R.id.listoutletkritis);
        caripelanggan = findViewById(R.id.caripelanggan);


        topAppBar = (MaterialToolbar) findViewById(R.id.persiapanbar);
        setSupportActionBar(topAppBar);

        outletkritis();

        listoutletkritis.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                Intent i = new Intent(getBaseContext(), detail_outlet_kritis.class);
                String SzId = ((data_pelanggan_pojo) parent.getItemAtPosition(position)).getSzId();
                String pelanggan = ((data_pelanggan_pojo) parent.getItemAtPosition(position)).getSzName();
                String alamat = ((data_pelanggan_pojo) parent.getItemAtPosition(position)).getSzAddress();


                i.putExtra("SzId", SzId);
                i.putExtra("pelanggan", pelanggan);
                i.putExtra("alamat", alamat);

                startActivity(i);
            }
        });


    }

    private void outletkritis() {
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String nik_baru = sharedPreferences.getString("szDocCall", null);
        String[] parts = nik_baru.split("-");
        String restnomor = parts[0];

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        Date date = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateOutput = format.format(date);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Persiapan/index_Outlet_kritis?szBranchId="+restnomor+"&tanggal="+dateOutput,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            int number = 0;
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");
                            for (int i = 0; i < movieArray.length(); i++) {
                                final JSONObject movieObject = movieArray.getJSONObject(i);
                                final data_pelanggan_pojo movieItem = new data_pelanggan_pojo(
                                        movieObject.getString("szCustomerId"),
                                        movieObject.getString("szName"),
                                        movieObject.getString("szAddress"),
                                        movieObject.getString("szLatitude"),
                                        movieObject.getString("szLongitude"));

                                dataPelangganPojos.add(movieItem);

                            }
                            adapter = new ListViewAdapterOutletKritis(dataPelangganPojos, getApplicationContext());
                            listoutletkritis.setAdapter(adapter);
                            adapter.notifyDataSetChanged();

                            caripelanggan.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                                @Override
                                public boolean onQueryTextSubmit(String text) {
                                    return false;
                                }

                                @Override
                                public boolean onQueryTextChange(String newText) {
                                    adapter.getFilter().filter(newText);
                                    return true;
                                }
                            });


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

    public static class ListViewAdapterOutletKritis extends ArrayAdapter<data_pelanggan_pojo> {
        private List<data_pelanggan_pojo> data_pelanggans;

        private Context context;

        public ListViewAdapterOutletKritis(List<data_pelanggan_pojo> data_pelanggans, Context context) {
            super(context, R.layout.list_outletkritis, data_pelanggans);
            this.data_pelanggans = data_pelanggans;
            this.context = context;
        }

        @SuppressLint("NewApi")
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = LayoutInflater.from(context);

            View listViewItem = inflater.inflate(R.layout.list_outletkritis, null, true);

            TextView namatoko = listViewItem.findViewById(R.id.namatoko);
            TextView alamat = listViewItem.findViewById(R.id.alamat);

            data_pelanggan_pojo data = getItem(position);

            namatoko.setText(data.getSzName());
            alamat.setText(data.getSzAddress());

            return listViewItem;
        }
    }
}