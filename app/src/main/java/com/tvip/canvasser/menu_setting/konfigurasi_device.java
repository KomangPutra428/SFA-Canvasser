package com.tvip.canvasser.menu_setting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class konfigurasi_device extends AppCompatActivity {
    TextView edituserid, editpassword, editdepoid, editnamadepo, editipwifi, editportwifi, editipgprs;
    SharedPreferences sharedPreferences;
    EditText edittype;

    ArrayList<String> employees = new ArrayList<>();

    AutoCompleteTextView editemployeeid;

    Button batal, simpan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konfigurasi_device);
        edituserid = findViewById(R.id.edituserid);
        editpassword = findViewById(R.id.editpassword);
        editemployeeid = findViewById(R.id.editemployeeid);
        editdepoid = findViewById(R.id.editdepoid);
        editnamadepo = findViewById(R.id.editnamadepo);
        editipwifi = findViewById(R.id.editipwifi);
        editportwifi = findViewById(R.id.editportwifi);
        editipgprs = findViewById(R.id.editipgprs);
        edittype = findViewById(R.id.edittype);

        batal = findViewById(R.id.batal);
        simpan = findViewById(R.id.simpan);

        WifiManager wifiMgr = (WifiManager) getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
        int ip = wifiInfo.getIpAddress();
        String ipAddress = Formatter.formatIpAddress(ip);
        editipwifi.setText(ipAddress);
        editportwifi.setText("8080");
        getLocalIpAddress();

        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);

        String lokasi = sharedPreferences.getString("lokasi", null);
        String nik_baru = sharedPreferences.getString("szDocCall", null);
        String employee = sharedPreferences.getString("nik_baru", null);
        String password = sharedPreferences.getString("password", null);

        editemployeeid.setText(nik_baru);
        edituserid.setText(employee);
        editnamadepo.setText(lokasi);
        editpassword.setText(password);

        StringRequest rest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_Employee_Id?szIdCard=" + employee,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("status").equals("true")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    employees.add(jsonObject1.getString("szId"));


                                }
                                editemployeeid.setAdapter(new ArrayAdapter<String>(konfigurasi_device.this, android.R.layout.simple_expandable_list_item_1, employees));

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
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestkota = Volley.newRequestQueue(konfigurasi_device.this);
        requestkota.add(rest);

        batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getSharedPreferences("user_details",MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("szDocCall", editemployeeid.getText().toString());

                String[] parts = editemployeeid.getText().toString().split("-");
                String result = parts[0];

                if(editemployeeid.getText().toString().contains("RP")){
                    if(result.equals("336") || result.equals("321") || result.equals("324")){
                        editor.putString("link","rest_server_canvaser_asa_baru_LHP");
                    } else {
                        editor.putString("link","rest_server_canvaser_tvip_baru_LHP");
                    }

                } else if(editemployeeid.getText().toString().contains("SPV")){
                    if(result.equals("336") || result.equals("321") || result.equals("324")){
                        editor.putString("link","rest_server_canvaser_asa_baru_LHP");
                    } else {
                        editor.putString("link","rest_server_canvaser_tvip_baru_LHP");
                    }
                } else {
                    if(result.equals("336") || result.equals("321") || result.equals("324")){
                        editor.putString("link","rest_server_canvaser_asa_baru_DO_Pending");
                    } else {
                        editor.putString("link","rest_server_canvaser_tvip_baru_DO_Pending");
                    }
                }
                editor.commit();
                finish();

            }
        });


        if(nik_baru.contains("D")){
            edittype.setText("CAN");
        } else if(nik_baru.contains("CRL")){
            edittype.setText("CRL");
        } else if(nik_baru.contains("C")){
            edittype.setText("COL");
        } else if(nik_baru.contains("PR")){
            edittype.setText("TKO");
        } else if(nik_baru.contains("RP")){
            edittype.setText("DEL");
        } else if(nik_baru.contains("STO")) {
            edittype.setText("MER");
        }

        StringRequest depo = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/master/lokasi/index_kode?namadepo=" + lokasi,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.getString("status").equals("true")) {
                                JSONArray movieArray = obj.getJSONArray("data");
                                for (int i = 0; i < movieArray.length(); i++) {
                                    final JSONObject movieObject = movieArray.getJSONObject(i);

                                    editdepoid.setText(movieObject.getString("kode_dms"));

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
        depo.setRetryPolicy(
                new DefaultRetryPolicy(
                        500000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                ));
        RequestQueue depoQueue = Volley.newRequestQueue(konfigurasi_device.this);
        depoQueue.add(depo);

    }

    public String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        String ip = Formatter.formatIpAddress(inetAddress.hashCode());
                        editipgprs.setText(ip);
                        return ip;
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e(TAG, ex.toString());
        }
        return null;
    }


}