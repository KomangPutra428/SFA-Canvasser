package com.tvip.canvasser.menu_mulai_perjalanan;

import static com.tvip.canvasser.menu_mulai_perjalanan.detailscangagal_dalamrute.intItemNumbers;
import static com.tvip.canvasser.menu_mulai_perjalanan.detailscangagal_dalamrute.intItemNumbers_nonrute;
import static com.tvip.canvasser.menu_mulai_perjalanan.mulai_perjalanan.STD;
import static com.tvip.canvasser.menu_mulai_perjalanan.mulai_perjalanan.pelanggan;


import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tvip.canvasser.Perangkat.GPSTracker;
import com.tvip.canvasser.Perangkat.HttpsTrustManager;
import com.tvip.canvasser.R;
import com.tvip.canvasser.menu_utama.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class scangagal_map extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    TextView namatoko, alamat, idstd;
    Button arahkan, lanjutkan;
    String langitude, longitude;
    LocationManager locationManager;
    TextView tv_longlat;
    private RequestQueue requestQueue, requestQueue2;

    ArrayList<String> GagalCheckin = new ArrayList<>();

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HttpsTrustManager.allowAllSSL();
        setContentView(R.layout.activity_scangagal_map);
        idstd = findViewById(R.id.idstd);
        namatoko = findViewById(R.id.namatoko);
        alamat = findViewById(R.id.alamat);

        arahkan = findViewById(R.id.arahkan);
        lanjutkan = findViewById(R.id.lanjutkan);

        idstd.setText("std " + STD);
        getIntent().getStringExtra("langitude");
        namatoko.setText(getIntent().getStringExtra("toko"));
        alamat.setText(getIntent().getStringExtra("alamat"));
        tv_longlat = findViewById(R.id.tv_longlat);
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        //MENGARAHKAN KE LOKASI TOKO
        arahkan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Double langitude = Double.valueOf(getIntent().getStringExtra("langitude"));
                Double longitude = Double.valueOf(getIntent().getStringExtra("longitude"));

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://maps.google.com?q="+langitude+","+longitude));
                startActivity(i);
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        getLocation();

        lanjutkan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //JIKA LATIDUTE DAN LONGITUT ADA
                if(longitude != null && langitude != null){

                    Location startPoint=new Location("locationA");
                    startPoint.setLatitude(Double.valueOf(langitude));
                    startPoint.setLongitude(Double.valueOf(longitude));

                    //LOCATION DARI TOKO
                    Location endPoint=new Location("locationA");
                    endPoint.setLatitude(Double.valueOf(getIntent().getStringExtra("langitude")));
                    endPoint.setLongitude(Double.valueOf(getIntent().getStringExtra("longitude")));
                    double distance2 = startPoint.distanceTo(endPoint);
                    int value = (int) distance2;

                    //JIKA ANDA BERADA DI LUAR RADIUS
                    if(value > 30){

                        final Dialog dialog = new Dialog(scangagal_map.this);
                        dialog.setContentView(R.layout.diluarradius);

                        Button tidak = dialog.findViewById(R.id.tidak);
                        Button ya = dialog.findViewById(R.id.ya);

                        tidak.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });

                        //AKAN UPDATE langitude dan szLongitude ke tabel doccallitem
                        ya.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (mulai_perjalanan.pelanggan.equals("non_rute")) {
                                    showDialogCheckin();
                                } else {
                                    showDialogCheckin();

                                    StringRequest stringRequest2 = new StringRequest(Request.Method.PUT, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_latlong",
                                            new Response.Listener<String>() {

                                                @Override
                                                public void onResponse(String response) {
                                                    Toast.makeText(scangagal_map.this, "Berhasi Update", Toast.LENGTH_SHORT).show();


                                                }
                                            },
                                            new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    Toast.makeText(scangagal_map.this, "Terjadi Kesalahan", Toast.LENGTH_SHORT).show();
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

                                            params.put("szDocId", STD);
                                            params.put("szCustomerId", getIntent().getStringExtra("kode"));
                                            params.put("szDocDO", getIntent().getStringExtra("szDocDo"));
                                            params.put("bVisited", "0");
                                            params.put("szLangitude", langitude);
                                            params.put("szLongitude", longitude);
                                            System.out.println(params);

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
                                    RequestQueue requestQueue2 = Volley.newRequestQueue(scangagal_map.this);
                                    requestQueue2.add(stringRequest2);
                                }


                            }
                        });
                        dialog.show();

                    //JIKA ANDA BERADA DI DALAM RADIUS
                    } else {
                        if (mulai_perjalanan.pelanggan.equals("non_rute")) {
                            StringRequest stringRequest2 = new StringRequest(Request.Method.PUT, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_update_nonrute",
                                    new Response.Listener<String>() {

                                        @Override
                                        public void onResponse(String response) {
                                            updateNonRute();
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

                                    sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                                    String nik_baru = sharedPreferences.getString("szDocCall", null);


                                    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    String currentDateandTime2 = sdf2.format(new Date());

                                    params.put("szDocId", STD);
                                    params.put("intItemNumber", intItemNumbers_nonrute);
                                    params.put("szCustomerId", getIntent().getStringExtra("kode"));


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
                            RequestQueue requestQueue2 = Volley.newRequestQueue(scangagal_map.this);
                            requestQueue2.add(stringRequest2);

                        } else {
                            StringRequest stringRequest2 = new StringRequest(Request.Method.PUT, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_latlong",
                                    new Response.Listener<String>() {

                                        @Override
                                        public void onResponse(String response) {
                                            Toast.makeText(scangagal_map.this, "Berhasi Update", Toast.LENGTH_SHORT).show();
                                            SF(getIntent().getStringExtra("szDocDo"));
                                            Intent reason = new Intent(getBaseContext(), menu_pelanggan.class);
                                            reason.putExtra("kode", getIntent().getStringExtra("kode"));
                                            reason.putExtra("idStd", STD);
                                            reason.putExtra("szDocDo", getIntent().getStringExtra("szDocDo"));
                                            startActivity(reason);
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Toast.makeText(scangagal_map.this, "Terjadi Kesalahan", Toast.LENGTH_SHORT).show();
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

                                    params.put("szDocId", STD);
                                    params.put("szCustomerId", getIntent().getStringExtra("kode"));
                                    params.put("szDocDO", getIntent().getStringExtra("szDocDo"));
                                    params.put("bVisited", "1");
                                    params.put("szLangitude", langitude);
                                    params.put("szLongitude", longitude);

                                    System.out.println(params);


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
                            RequestQueue requestQueue2 = Volley.newRequestQueue(scangagal_map.this);
                            requestQueue2.add(stringRequest2);
                        }




                    }
                } else {

                    //TIDAK MENAGKTIFKAN GPS SEHINGGA LOKASI TIDAK DITEMUKAN
                    Toast.makeText(getBaseContext(),
                            "Lokasi Tidak Terdeteksi, tunggu beberapa saat kemudian klik tombol Lanjutkan" ,
                            Toast.LENGTH_LONG).show();
                }
            }

            private void updateNonRute() {
                StringRequest stringRequest2 = new StringRequest(Request.Method.PUT, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_latlong",
                        new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(scangagal_map.this, "Berhasi Update", Toast.LENGTH_SHORT).show();
                                SF(getIntent().getStringExtra("szDocDo"));
                                Intent reason = new Intent(getBaseContext(), menu_pelanggan.class);
                                reason.putExtra("kode", getIntent().getStringExtra("kode"));
                                reason.putExtra("idStd", STD);
                                reason.putExtra("szDocDo", getIntent().getStringExtra("szDocDo"));
                                startActivity(reason);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(scangagal_map.this, "Terjadi Kesalahan", Toast.LENGTH_SHORT).show();
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

                        params.put("szDocId", STD);
                        params.put("szCustomerId", getIntent().getStringExtra("kode"));
                        params.put("szDocDO", getIntent().getStringExtra("szDocDo"));
                        params.put("bVisited", "1");
                        params.put("szLangitude", langitude);
                        params.put("szLongitude", longitude);

                        System.out.println(params);


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
                RequestQueue requestQueue2 = Volley.newRequestQueue(scangagal_map.this);
                requestQueue2.add(stringRequest2);
            }
        });
    }

    private void showDialogCheckin() {
        final Dialog dialog = new Dialog(scangagal_map.this);
        dialog.setContentView(R.layout.gagal_checkin);
        dialog.setCancelable(false);
        dialog.show();

        TextView toko = dialog.findViewById(R.id.toko);
        TextView kodetoko = dialog.findViewById(R.id.kodetoko);
        TextView alamattoko = dialog.findViewById(R.id.alamattoko);

        AutoCompleteTextView alasangagalcheckin = dialog.findViewById(R.id.alasangagalcheckin);
        Button batal = dialog.findViewById(R.id.batal);
        Button lanjutkan = dialog.findViewById(R.id.lanjutkan);

        toko.setText(detailscangagal_dalamrute.namatoko.getText().toString());
        kodetoko.setText(detailscangagal_dalamrute.code.getText().toString());
        alamattoko.setText(detailscangagal_dalamrute.address.getText().toString());

        StringRequest rest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_Check_In_Failed",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("status").equals("true")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    String id = jsonObject1.getString("szId");
                                    String jenis_istirahat = jsonObject1.getString("szName");
                                    GagalCheckin.add(id + "-" + jenis_istirahat);

                                }
                            }
                            alasangagalcheckin.setAdapter(new ArrayAdapter<String>(scangagal_map.this, android.R.layout.simple_expandable_list_item_1, GagalCheckin));
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
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(scangagal_map.this);
            requestQueue.getCache().clear();
            requestQueue.add(rest);
        } else {
            requestQueue.add(rest);
        }

        batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        lanjutkan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alasangagalcheckin.setError(null);
                if(alasangagalcheckin.getText().toString().length() == 0){
                    alasangagalcheckin.setError("Silahkan Pilih Alasan");
                } else {
                    if(pelanggan.equals("non_rute")){
                        StringRequest stringRequest2 = new StringRequest(Request.Method.PUT, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_update_nonrute",
                                new Response.Listener<String>() {

                                    @Override
                                    public void onResponse(String response) {

                                        StringRequest stringRequest2 = new StringRequest(Request.Method.PUT, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_latlong",
                                                new Response.Listener<String>() {

                                                    @Override
                                                    public void onResponse(String response) {
                                                        methodpost();
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

                                                params.put("szDocId", STD);
                                                params.put("szCustomerId", getIntent().getStringExtra("kode"));
                                                params.put("szDocDO", getIntent().getStringExtra("szDocDo"));
                                                params.put("bVisited", "0");
                                                params.put("szLangitude", langitude);
                                                params.put("szLongitude", longitude);
                                                System.out.println(params);

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
                                        RequestQueue requestQueue2 = Volley.newRequestQueue(scangagal_map.this);
                                        requestQueue2.add(stringRequest2);
                                    }

                                    private void methodpost() {
                                        Intent dalam_rute = new Intent(getBaseContext(), menu_pelanggan.class);

                                        dalam_rute.putExtra("kode", kodetoko.getText().toString());
                                        dalam_rute.putExtra("idStd", STD);
                                        dalam_rute.putExtra("szDocDo", getIntent().getStringExtra("szDocDo"));

                                        dalam_rute.putExtra("Status", "Failed");
                                        SF(getIntent().getStringExtra("szDocDo"));
                                        startActivity(dalam_rute);
                                        StringRequest stringRequest2 = new StringRequest(Request.Method.PUT, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_gagal_CheckIn",
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

                                                String rest = alasangagalcheckin.getText().toString();
                                                String[] parts = rest.split("-");
                                                String restnomor = parts[0];
                                                String restnomorbaru = restnomor.replace(" ", "");
                                                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                                String currentDateandTime2 = sdf2.format(new Date());

                                                params.put("szDocId", STD);
                                                params.put("szCustomerId", detailscangagal_dalamrute.code.getText().toString());
                                                params.put("szReasonIdCheckin", restnomorbaru);
                                                params.put("szDocDO", getIntent().getStringExtra("szDocDo"));


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

                                        RequestQueue requestQueue2 = Volley.newRequestQueue(scangagal_map.this);
                                        requestQueue2.add(stringRequest2);
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

                                sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                                String nik_baru = sharedPreferences.getString("szDocCall", null);


                                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String currentDateandTime2 = sdf2.format(new Date());

                                params.put("szDocId", STD);
                                params.put("intItemNumber", intItemNumbers_nonrute);
                                params.put("szCustomerId", getIntent().getStringExtra("kode"));


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
                        RequestQueue requestQueue2 = Volley.newRequestQueue(scangagal_map.this);
                        requestQueue2.add(stringRequest2);
                    } else {
                        Intent dalam_rute = new Intent(getBaseContext(), menu_pelanggan.class);

                        dalam_rute.putExtra("kode", kodetoko.getText().toString());
                        dalam_rute.putExtra("idStd", STD);
                        dalam_rute.putExtra("szDocDo", getIntent().getStringExtra("szDocDo"));

                        dalam_rute.putExtra("Status", "Failed");
                        SF(getIntent().getStringExtra("szDocDo"));
                        startActivity(dalam_rute);
                        StringRequest stringRequest2 = new StringRequest(Request.Method.PUT, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_gagal_CheckIn",
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

                                String rest = alasangagalcheckin.getText().toString();
                                String[] parts = rest.split("-");
                                String restnomor = parts[0];
                                String restnomorbaru = restnomor.replace(" ", "");
                                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String currentDateandTime2 = sdf2.format(new Date());

                                params.put("szDocId", STD);
                                params.put("szCustomerId", detailscangagal_dalamrute.code.getText().toString());
                                params.put("szReasonIdCheckin", restnomorbaru);
                                params.put("szDocDO", getIntent().getStringExtra("szDocDo"));


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

                        RequestQueue requestQueue2 = Volley.newRequestQueue(scangagal_map.this);
                        requestQueue2.add(stringRequest2);
                    }
                }
            }
        });
    }

    private void SF(String szDocDo) {
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_DocCallItem",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
//                        DocSOMDBA(s);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse response = error.networkResponse;
                if(response != null && response.data != null){

                }else{
                    String errorMessage=error.getClass().getSimpleName();
                    if(!TextUtils.isEmpty(errorMessage)){

                    }
                }
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


                String rest2 = detailscangagal_dalamrute.alasangagalscan.getText().toString();
                String[] parts2 = rest2.split("-");
                String restnomor2 = parts2[0];

                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String currentDateandTime2 = sdf2.format(new Date());

                params.put("szDocId", STD);

                if(pelanggan.equals("non_rute")){
                    params.put("intItemNumber", intItemNumbers_nonrute);
                } else {
                    params.put("intItemNumber", intItemNumbers);
                }


                params.put("szCustomerId", detailscangagal_dalamrute.code.getText().toString());

                params.put("dtmStart", currentDateandTime2);
                params.put("dtmFinish", currentDateandTime2);

                params.put("bVisited", "1");
                params.put("bSuccess", "0");


                params.put("szFailReason", "");
                params.put("szLangitude", MainActivity.latitude);
                params.put("szLongitude", MainActivity.longitude);

                if(pelanggan.equals("canvaser_luar")){
                    params.put("bOutOfRoute", "1");
                } else {
                    params.put("bOutOfRoute", "0");
                }

                params.put("szRefDocId", getIntent().getStringExtra("szDocDo"));

                params.put("bScanBarcode", "0");

                params.put("szReasonIdCheckin", "");
                params.put("szReasonFailedScan", restnomor2);
                params.put("decRadiusDiff", "0");

                System.out.println("Scangagal = " + params);








                return params;
            }

        };
        stringRequest2.setRetryPolicy(
                new DefaultRetryPolicy(
                        5000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
        );
        if (requestQueue2 == null) {
            requestQueue2 = Volley.newRequestQueue(scangagal_map.this);
            requestQueue2.getCache().clear();
            requestQueue2.add(stringRequest2);
        } else {
            requestQueue2.add(stringRequest2);
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Double langitude = Double.valueOf(getIntent().getStringExtra("langitude"));
        Double longitude = Double.valueOf(getIntent().getStringExtra("longitude"));

        // Add a marker in Sydney and move the camera
        LatLng zoom = new LatLng(langitude, longitude);

        mMap.addMarker(new MarkerOptions().position(zoom).title(getIntent().getStringExtra("toko")));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(zoom));

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(zoom, 20));
    }

    @SuppressLint("MissingPermission")
    private void getLocation() {

        GPSTracker gpsTracker = new GPSTracker(scangagal_map.this);
        if(gpsTracker.canGetLocation()){
            langitude = String.valueOf(gpsTracker.getLatitude());
            longitude = String.valueOf(gpsTracker.getLongitude());
        }else{
            gpsTracker.showSettingsAlert();

        }

    }

}