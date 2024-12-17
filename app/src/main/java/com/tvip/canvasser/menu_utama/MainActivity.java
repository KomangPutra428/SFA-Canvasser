package com.tvip.canvasser.menu_utama;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.tvip.canvasser.BuildConfig;
import com.tvip.canvasser.Perangkat.GPSTracker;
import com.tvip.canvasser.Perangkat.HttpsTrustManager;
import com.tvip.canvasser.Perangkat.MyFirebaseServices;
import com.tvip.canvasser.R;
import com.tvip.canvasser.menu_login.login;
import com.tvip.canvasser.menu_mulai_perjalanan.mulai_perjalanan;
import com.tvip.canvasser.menu_selesai_perjalanan.akhirikegiatan;
import com.tvip.canvasser.menu_selesai_perjalanan.detail_selesai;
import com.tvip.canvasser.menu_setting.setting;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    private static final String CHANNEL_ID = "101";

    String nama_file;

    TextView txt_depo, istirahat_text;
    public static TextView txt_nama;
    LinearLayout persiapan, istirahat, mulaiperjalanan, selesai_perjalanan;
    LocationManager locationManager;
    ArrayList<String> Istirahat = new ArrayList<>();
    ImageButton biodata, setting;

    public static String longitude, latitude, kodelokasi, cityName, kode_dms;
    String id_istirahat;
    Context context;

    SweetAlertDialog pDialog;

    TextView txt_employeeid;

    String kode_employee;

    ImageView button_persiapan, image_mulaiperjalanan, image_istirahat, button_selesai_perjalanan;

    public static ArrayList<String> jenisrute = new ArrayList<String>();



    // download apk //

    private static final int MY_PERMISSIONS_REQUEST_WRITE_STORAGE = 123;
    private String basicAuthCredentials = "admin:Databa53"; // Your credentials here

    private long downloadID;
    private BroadcastReceiver onComplete = new BroadcastReceiver() {
        public void onReceive(Context ctxt, Intent intent) {
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            if (id == downloadID) {
                pDialog.dismissWithAnimation();
                installApk();
            }
        }
    };

    // ============ //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HttpsTrustManager.allowAllSSL();
        txt_employeeid = findViewById(R.id.txt_employeeid);
        registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));






        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_DENIED)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
            {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 2);
                return;
            }
        }

        FirebaseApp.initializeApp(this);

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                //If task is failed then
                String token = task.getResult();
                updateDeviceId(token);
            }
        });


        button_persiapan = findViewById(R.id.button_persiapan);
        image_mulaiperjalanan = findViewById(R.id.image_mulaiperjalanan);
        image_istirahat = findViewById(R.id.image_istirahat);
        button_selesai_perjalanan = findViewById(R.id.button_selesai_perjalanan);
        mulaiperjalanan = findViewById(R.id.mulaiperjalanan);
        selesai_perjalanan = findViewById(R.id.selesai_perjalanan);


        txt_nama = findViewById(R.id.txt_nama);
        txt_depo = findViewById(R.id.txt_depo);
        biodata = findViewById(R.id.biodata);
        persiapan = findViewById(R.id.persiapan);
        istirahat = findViewById(R.id.istirahat);
        istirahat_text = findViewById(R.id.istirahat_text);
        setting = findViewById(R.id.setting);

        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        getLocation();

        final String lokasi = sharedPreferences.getString("lokasi", null);
        final String nama = sharedPreferences.getString("nama", null);
        final String nik_baru = sharedPreferences.getString("nik_baru", null);
        kode_employee = sharedPreferences.getString("szDocCall", null);

        SharedPreferences preferences = getSharedPreferences("user_details",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();



        String[] parts = kode_employee.split("-");
        String result = parts[0];

        if(kode_employee.contains("RP")){
            if(result.equals("336") || result.equals("321") || result.equals("324")){
                editor.putString("link","rest_server_canvaser_asa_baru_LHP");
                editor.commit();
            } else {
                editor.putString("link","rest_server_canvaser_tvip_baru_LHP");
                editor.commit();
            }
        } else if(kode_employee.contains("SPV")){
            if(result.equals("336") || result.equals("321") || result.equals("324")){
                editor.putString("link","rest_server_canvaser_asa_baru_LHP");
                editor.commit();
            } else {
                editor.putString("link","rest_server_canvaser_tvip_baru_LHP");
                editor.commit();
            }
        } else {
            if(result.equals("336") || result.equals("321") || result.equals("324")){
                editor.putString("link","rest_server_canvaser_asa_baru_DO_Pending");
                editor.commit();
            } else {
                editor.putString("link","rest_server_canvaser_tvip_baru_DO_Pending");
                editor.commit();
            }
        }




        // Check for write external storage permission
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_WRITE_STORAGE);
        } else {
            downloadApk();
        }

        StringRequest location = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/master/lokasi/index_depo?depo_nama=" + lokasi,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");

                            JSONObject biodatas = null;
                            for (int i = 0; i < movieArray.length(); i++) {

                                biodatas = movieArray.getJSONObject(i);
                                Toast.makeText(getApplicationContext(), biodatas.getString("kode_dms"), Toast.LENGTH_SHORT).show();
                                kode_dms = (biodatas.getString("kode_dms"));

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

        location.setRetryPolicy(
                new DefaultRetryPolicy(
                        500000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                ));
        RequestQueue biodataQueue = Volley.newRequestQueue(this);
        biodataQueue.add(location);

        Toast.makeText(MainActivity.this, "NIK NYA" + nik_baru, Toast.LENGTH_SHORT).show();
        txt_nama.setText(nama);
        txt_employeeid.setText(kode_employee);
        txt_depo.setText("Depo "+lokasi);

        biodata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentbiodata = new Intent(getBaseContext(), com.tvip.canvasser.menu_biodata.biodata.class);
                startActivity(intentbiodata);
            }
        });

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentbiodata = new Intent(getBaseContext(), com.tvip.canvasser.menu_setting.setting.class);
                startActivity(intentbiodata);
            }
        });

        createNotificationChannel();





        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/master/lokasi/index_kode?namadepo=" + lokasi,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.getString("status").equals("true")) {
                                JSONArray movieArray = obj.getJSONArray("data");
                                for (int i = 0; i < movieArray.length(); i++) {
                                    final JSONObject movieObject = movieArray.getJSONObject(i);

                                    kodelokasi = movieObject.getString("kode_dms");


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
        stringRequest.setRetryPolicy(
                new DefaultRetryPolicy(
                        500000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                ));
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);
    }

    private void updateDeviceId(String token) {
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Notification/index_cekDeviceId",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
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
                String employeeid = sharedPreferences.getString("szDocCall", null);
                String nik_baru = sharedPreferences.getString("nik_baru", null);

                String[] parts2 = employeeid.split("-");
                String restnomor2 = parts2[0];

                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String currentDateandTime2 = sdf2.format(new Date());

                params.put("preseller_nik", nik_baru);
                params.put("preseller_employeeId", employeeid);
                params.put("preseller_name", txt_nama.getText().toString());
                params.put("preseller_branch", restnomor2);

                params.put("preseller_location", sharedPreferences.getString("lokasi", null));
                params.put("device_brand", Build.BRAND);

                params.put("device_model", Build.MODEL);
                params.put("device_sdk", String.valueOf(Build.VERSION.SDK_INT));
                params.put("device_version", Build.VERSION.RELEASE);
                params.put("apps_version", BuildConfig.VERSION_NAME);

                params.put("apps_last_open", currentDateandTime2);
                params.put("device_token", token);
                params.put("status_user", "1");



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
        RequestQueue requestQueue2 = Volley.newRequestQueue(MainActivity.this);
        requestQueue2.getCache().clear();
        requestQueue2.add(stringRequest2);
    }

    private void postIstirahat(String szDocId, String istirahat) {
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Istirahat/index_istirahat",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        pDialog.dismissWithAnimation();
                        new SweetAlertDialog(MainActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                .setContentText("Data Sudah Disimpan")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                        updateUI();
                                    }
                                })
                                .show();

                    }
                }, new Response.ErrorListener() {
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

                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                String currentDateandTime = sdf.format(new Date());

                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
                String currentDateandTime2 = sdf2.format(new Date());

                String[] parts = istirahat.split("-");
                String restnomor = parts[0];
                String restnomorbaru = restnomor.replace(" ", "");

                params.put("szDocId", nik_baru + "_" + currentDateandTime);
                params.put("szDocCallId", szDocId);
                params.put("dtmStarted", currentDateandTime2);

                params.put("szAttendanceTypeId", restnomorbaru);
                params.put("szLongitude", latitude);

                params.put("szLangitude", longitude);
                params.put("szUserCreatedId", nik_baru);
                params.put("dtmCreated", currentDateandTime2);



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
        RequestQueue requestQueue2 = Volley.newRequestQueue(MainActivity.this);
        requestQueue2.add(stringRequest2);


    }


    @SuppressLint("MissingPermission")
    private void getLocation() {

        GPSTracker gpsTracker = new GPSTracker(MainActivity.this);
        if(gpsTracker.canGetLocation()){
            latitude = String.valueOf(gpsTracker.getLatitude());
            longitude = String.valueOf(gpsTracker.getLongitude());

            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            List<Address> addresses = null;
            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1);
                cityName = addresses.get(0).getAddressLine(0);
                System.out.println("Alamat = " + cityName);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (addresses != null && addresses.size() > 0) {

            } else {

            }


        } else {
            gpsTracker.showSettingsAlert();

        }

    }



    @Override
    public void onBackPressed() {
        new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Apakah anda yakin?")
                .setContentText("Anda akan keluar dari aplikasi ini")
                .setConfirmText("Yes")
                .setCancelText("Cancel")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        finishAffinity();
                    }
                })
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                    }
                })
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUIFinished();
        getChoose();
        jenisrute.clear();

    }

    private void getChoose() {
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String nik_baru = sharedPreferences.getString("szDocCall", null);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Persiapan/index_SuratTugasAll?nik_baru="+ nik_baru,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");
                            for (int i = 0; i < movieArray.length(); i++) {
                                final JSONObject movieObject = movieArray.getJSONObject(i);

                                if(movieObject.getString("bStarted").equals("1")){
                                    if(movieObject.getString("szRouteType").equals("CAN")){
                                        jenisrute.add("Canvas Rute" + " - " + movieObject.getString("szDocId"));
                                    } else {
                                        jenisrute.add("Canvas Non Rute" + " - " + movieObject.getString("szDocId"));
                                    }
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

        stringRequest.setRetryPolicy(
                new DefaultRetryPolicy(
                        500000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                ));
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);
    }

    private void updateStart() {
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String nik_baru = sharedPreferences.getString("szDocCall", null);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Persiapan/index_SuratTugasAll?nik_baru="+ nik_baru,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");
                            for (int i = 0; i < movieArray.length(); i++) {
                                final JSONObject movieObject = movieArray.getJSONObject(i);

                                if(movieObject.getString("bStarted").contains("0")){
                                    image_mulaiperjalanan.setImageDrawable(getResources().getDrawable(R.drawable.mulai_perjalanan_hitam));
                                    image_istirahat.setImageDrawable(getResources().getDrawable(R.drawable.istirahat_hitam));
                                    button_selesai_perjalanan.setImageDrawable(getResources().getDrawable(R.drawable.selesai_perjalanan_hitam));

                                    mulaiperjalanan.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                                                    .setTitleText("Surat Tugas Belum Dijalankan")
                                                    .setConfirmText("OK")
                                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override
                                                        public void onClick(SweetAlertDialog sDialog) {
                                                            sDialog.dismissWithAnimation();
                                                        }
                                                    })
                                                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override
                                                        public void onClick(SweetAlertDialog sDialog) {
                                                            sDialog.cancel();
                                                        }
                                                    })
                                                    .show();
                                        }
                                    });

                                    istirahat.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                                                    .setTitleText("Surat Tugas Belum Dijalankan")
                                                    .setConfirmText("OK")
                                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override
                                                        public void onClick(SweetAlertDialog sDialog) {
                                                            sDialog.dismissWithAnimation();
                                                        }
                                                    })
                                                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override
                                                        public void onClick(SweetAlertDialog sDialog) {
                                                            sDialog.cancel();
                                                        }
                                                    })
                                                    .show();
                                        }
                                    });

                                    selesai_perjalanan.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                                                    .setTitleText("Surat Tugas Belum Dijalankan")
                                                    .setConfirmText("OK")
                                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override
                                                        public void onClick(SweetAlertDialog sDialog) {
                                                            sDialog.dismissWithAnimation();
                                                        }
                                                    })
                                                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override
                                                        public void onClick(SweetAlertDialog sDialog) {
                                                            sDialog.cancel();
                                                        }
                                                    })
                                                    .show();
                                        }
                                    });
                                } else {
                                    image_mulaiperjalanan.setImageDrawable(getResources().getDrawable(R.drawable.mulai_perjalanan));
                                    image_istirahat.setImageDrawable(getResources().getDrawable(R.drawable.button_istirahat));
                                    button_selesai_perjalanan.setImageDrawable(getResources().getDrawable(R.drawable.button_selesai_perjalanan));

                                    mulaiperjalanan.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if ((image_mulaiperjalanan.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.mulai_perjalanan_hitam).getConstantState())) {
                                                new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                                                        .setTitleText("Anda Masih Menggunakan Mode Istirahat")
                                                        .setConfirmText("OK")
                                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                            @Override
                                                            public void onClick(SweetAlertDialog sDialog) {
                                                                sDialog.dismissWithAnimation();
                                                            }
                                                        })
                                                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                            @Override
                                                            public void onClick(SweetAlertDialog sDialog) {
                                                                sDialog.cancel();
                                                            }
                                                        })
                                                        .show();
                                            } else {
                                                Intent mulaiperjalanan = new Intent(getBaseContext(), mulai_perjalanan.class);
                                                startActivity(mulaiperjalanan);
                                            }

                                        }
                                    });

                                    istirahat.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Istirahat.clear();
                                            if (image_mulaiperjalanan.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.mulai_perjalanan_hitam).getConstantState()) {
                                                final Dialog dialog = new Dialog(MainActivity.this);
                                                dialog.setContentView(R.layout.popup_selesai_istirahat);
                                                Button tidak = dialog.findViewById(R.id.tidak);
                                                Button ya = dialog.findViewById(R.id.ya);

                                                dialog.show();

                                                tidak.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        dialog.cancel();
                                                    }
                                                });

                                                ya.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        dialog.cancel();
                                                        pDialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                                                        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                                                        pDialog.setTitleText("Harap Menunggu");
                                                        pDialog.setCancelable(false);
                                                        pDialog.show();

                                                        StringRequest stringRequest2 = new StringRequest(Request.Method.PUT, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Istirahat/index_istirahat",
                                                                new Response.Listener<String>() {

                                                                    @Override
                                                                    public void onResponse(String response) {
                                                                        pDialog.dismissWithAnimation();
                                                                        new SweetAlertDialog(MainActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                                                                .setContentText("Data Sudah Disimpan")
                                                                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                                    @Override
                                                                                    public void onClick(SweetAlertDialog sDialog) {
                                                                                        sDialog.dismissWithAnimation();
                                                                                        button_persiapan.setImageDrawable(getResources().getDrawable(R.drawable.button_persiapan));
                                                                                        image_mulaiperjalanan.setImageDrawable(getResources().getDrawable(R.drawable.mulai_perjalanan));
                                                                                        image_istirahat.setImageDrawable(getResources().getDrawable(R.drawable.button_istirahat));
                                                                                        button_selesai_perjalanan.setImageDrawable(getResources().getDrawable(R.drawable.button_selesai_perjalanan));

                                                                                    }
                                                                                })
                                                                                .show();
                                                                        pDialog.cancel();

                                                                    }
                                                                }, new Response.ErrorListener() {
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


                                                                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
                                                                String currentDateandTime2 = sdf2.format(new Date());

                                                                params.put("iInternalId", id_istirahat);

                                                                params.put("dtmFinished", currentDateandTime2);
                                                                params.put("szUserUpdatedId", nik_baru);
                                                                params.put("dtmLastUpdated", currentDateandTime2);


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
                                                        RequestQueue requestQueue2 = Volley.newRequestQueue(MainActivity.this);
                                                        requestQueue2.add(stringRequest2);

                                                    }
                                                });

                                            } else {
                                                final Dialog dialog = new Dialog(MainActivity.this);
                                                dialog.setContentView(R.layout.topup_istirahat);

                                                final AutoCompleteTextView istirahat = dialog.findViewById(R.id.editpilihistirahat);
                                                Button tidak = dialog.findViewById(R.id.tidak);
                                                Button ya = dialog.findViewById(R.id.ya);

                                                StringRequest rest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Istirahat/index_JenisIstirahat",
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
                                                                            Istirahat.add(id + "-" + jenis_istirahat);

                                                                        }
                                                                    }
                                                                    istirahat.setAdapter(new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_expandable_list_item_1, Istirahat));
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
                                                RequestQueue requestkota = Volley.newRequestQueue(MainActivity.this);
                                                requestkota.add(rest);

                                                dialog.show();

                                                istirahat.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                                                    @Override
                                                    public void onFocusChange(View v, boolean hasFocus) {
                                                        istirahat.showDropDown();
                                                    }
                                                });
                                                istirahat.setOnTouchListener(new View.OnTouchListener() {
                                                    @Override
                                                    public boolean onTouch(View v, MotionEvent event) {
                                                        istirahat.showDropDown();
                                                        return false;
                                                    }
                                                });

                                                tidak.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        dialog.dismiss();
                                                    }
                                                });

                                                ya.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {

                                                        if (istirahat.getText().toString().length() == 0) {
                                                            istirahat.setError("Pilih Jenis Istirahat");
                                                        } else if(longitude == null){
                                                            new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                                                                    .setTitleText("Lokasi belum ditemukan")
                                                                    .setConfirmText("OK")
                                                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                        @Override
                                                                        public void onClick(SweetAlertDialog sDialog) {
                                                                            sDialog.dismissWithAnimation();
                                                                        }
                                                                    })
                                                                    .show();
                                                        } else {
                                                            dialog.cancel();
                                                            pDialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                                                            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                                                            pDialog.setTitleText("Harap Menunggu");
                                                            pDialog.setCancelable(false);
                                                            pDialog.show();

                                                            sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                                                            String nik_baru = sharedPreferences.getString("szDocCall", null);
                                                            StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Persiapan/index_SuratTugas?nik_baru=" + nik_baru,
                                                                    new Response.Listener<String>() {
                                                                        @Override
                                                                        public void onResponse(String response) {

                                                                            try {
                                                                                JSONObject obj = new JSONObject(response);
                                                                                final JSONArray movieArray = obj.getJSONArray("data");
                                                                                for (int i = 0; i < movieArray.length(); i++) {
                                                                                    final JSONObject movieObject = movieArray.getJSONObject(i);

                                                                                    postIstirahat(movieObject.getString("szDocId"), istirahat.getText().toString());

                                                                                }


                                                                            } catch (JSONException e) {
                                                                                e.printStackTrace();

                                                                            }
                                                                        }
                                                                    },
                                                                    new Response.ErrorListener() {
                                                                        @Override
                                                                        public void onErrorResponse(VolleyError error) {
                                                                            pDialog.dismissWithAnimation();
                                                                            new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE)
                                                                                    .setContentText("Surat Tugas Belum Ada")
                                                                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                                        @Override
                                                                                        public void onClick(SweetAlertDialog sDialog) {
                                                                                            sDialog.dismissWithAnimation();
                                                                                        }
                                                                                    })
                                                                                    .show();
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

                                                            stringRequest.setRetryPolicy(
                                                                    new DefaultRetryPolicy(
                                                                            500000,
                                                                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                                                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                                                                    ));
                                                            RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
                                                            requestQueue.add(stringRequest);
                                                        }

                                                    }
                                                });


                                            }
                                        }
                                    });

                                    mulaiperjalanan.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if ((image_mulaiperjalanan.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.mulai_perjalanan_hitam).getConstantState())) {
                                                new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                                                        .setTitleText("Anda Masih Menggunakan Mode Istirahat")
                                                        .setConfirmText("OK")
                                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                            @Override
                                                            public void onClick(SweetAlertDialog sDialog) {
                                                                sDialog.dismissWithAnimation();
                                                            }
                                                        })
                                                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                            @Override
                                                            public void onClick(SweetAlertDialog sDialog) {
                                                                sDialog.cancel();
                                                            }
                                                        })
                                                        .show();
                                            } else {
                                                Intent mulaiperjalanan = new Intent(getBaseContext(), mulai_perjalanan.class);
                                                startActivity(mulaiperjalanan);
                                            }

                                        }
                                    });

                                    selesai_perjalanan.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if ((button_selesai_perjalanan.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.selesai_perjalanan_hitam).getConstantState())) {
                                                new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                                                        .setTitleText("Anda Masih Menggunakan Mode Istirahat")
                                                        .setConfirmText("OK")
                                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                            @Override
                                                            public void onClick(SweetAlertDialog sDialog) {
                                                                sDialog.dismissWithAnimation();
                                                            }
                                                        })
                                                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                            @Override
                                                            public void onClick(SweetAlertDialog sDialog) {
                                                                sDialog.cancel();
                                                            }
                                                        })
                                                        .show();
                                            } else {
                                                Intent intent = new Intent(getApplicationContext(), detail_selesai.class);
                                                startActivity(intent);

                                            }

                                        }
                                    });

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
                        image_mulaiperjalanan.setImageDrawable(getResources().getDrawable(R.drawable.mulai_perjalanan_hitam));
                        image_istirahat.setImageDrawable(getResources().getDrawable(R.drawable.istirahat_hitam));
                        button_selesai_perjalanan.setImageDrawable(getResources().getDrawable(R.drawable.selesai_perjalanan_hitam));

                        mulaiperjalanan.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText("Surat Tugas Hari Ini Belum Ada")
                                        .setConfirmText("OK")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.dismissWithAnimation();
                                            }
                                        })
                                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.cancel();
                                            }
                                        })
                                        .show();
                            }
                        });

                        istirahat.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText("Surat Tugas Hari Ini Belum Ada")
                                        .setConfirmText("OK")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.dismissWithAnimation();
                                            }
                                        })
                                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.cancel();
                                            }
                                        })
                                        .show();
                            }
                        });

                        selesai_perjalanan.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText("Surat Tugas Hari Ini Belum Ada")
                                        .setConfirmText("OK")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.dismissWithAnimation();
                                            }
                                        })
                                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.cancel();
                                            }
                                        })
                                        .show();
                            }
                        });

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
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);

    }

    private void updateUIFinished() {
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String nik_baru = sharedPreferences.getString("szDocCall", null);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Persiapan/index_SuratTugasAll?nik_baru="+ nik_baru,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");
                            for (int i = 0; i < movieArray.length(); i++) {
                                final JSONObject movieObject = movieArray.getJSONObject(i);

                                if(movieObject.getString("bStarted").equals("0")){
                                    image_mulaiperjalanan.setImageDrawable(getResources().getDrawable(R.drawable.mulai_perjalanan_hitam));
                                    image_istirahat.setImageDrawable(getResources().getDrawable(R.drawable.istirahat_hitam));
                                    button_selesai_perjalanan.setImageDrawable(getResources().getDrawable(R.drawable.selesai_perjalanan_hitam));

                                    persiapan.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent biodata = new Intent(getBaseContext(), com.tvip.canvasser.menu_persiapan.persiapan.class);
                                            startActivity(biodata);
                                        }
                                    });

                                    mulaiperjalanan.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                                                    .setTitleText("Surat Tugas Belum Dijalankan")
                                                    .setConfirmText("OK")
                                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override
                                                        public void onClick(SweetAlertDialog sDialog) {
                                                            sDialog.dismissWithAnimation();
                                                        }
                                                    })
                                                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override
                                                        public void onClick(SweetAlertDialog sDialog) {
                                                            sDialog.cancel();
                                                        }
                                                    })
                                                    .show();
                                        }
                                    });

                                    istirahat.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                                                    .setTitleText("Surat Tugas Belum Dijalankan")
                                                    .setConfirmText("OK")
                                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override
                                                        public void onClick(SweetAlertDialog sDialog) {
                                                            sDialog.dismissWithAnimation();
                                                        }
                                                    })
                                                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override
                                                        public void onClick(SweetAlertDialog sDialog) {
                                                            sDialog.cancel();
                                                        }
                                                    })
                                                    .show();
                                        }
                                    });

                                    selesai_perjalanan.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                                                    .setTitleText("Surat Tugas Belum Dijalankan")
                                                    .setConfirmText("OK")
                                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override
                                                        public void onClick(SweetAlertDialog sDialog) {
                                                            sDialog.dismissWithAnimation();
                                                        }
                                                    })
                                                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override
                                                        public void onClick(SweetAlertDialog sDialog) {
                                                            sDialog.cancel();
                                                        }
                                                    })
                                                    .show();
                                        }
                                    });
                                    break;

                            } else if(movieObject.getString("bFinished").equals("1")){
                                    button_persiapan.setImageDrawable(getResources().getDrawable(R.drawable.persiapan_hitam));
                                    image_mulaiperjalanan.setImageDrawable(getResources().getDrawable(R.drawable.mulai_perjalanan_hitam));
                                    image_istirahat.setImageDrawable(getResources().getDrawable(R.drawable.istirahat_hitam));
                                    button_selesai_perjalanan.setImageDrawable(getResources().getDrawable(R.drawable.selesai_perjalanan_hitam));

                                    persiapan.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                                                    .setTitleText("Surat Tugas Sudah Diselesaikan")
                                                    .setConfirmText("OK")
                                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override
                                                        public void onClick(SweetAlertDialog sDialog) {
                                                            sDialog.dismissWithAnimation();
                                                        }
                                                    })
                                                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override
                                                        public void onClick(SweetAlertDialog sDialog) {
                                                            sDialog.cancel();
                                                        }
                                                    })
                                                    .show();
                                        }
                                    });

                                    mulaiperjalanan.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                                                    .setTitleText("Surat Tugas Sudah Diselesaikan")
                                                    .setConfirmText("OK")
                                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override
                                                        public void onClick(SweetAlertDialog sDialog) {
                                                            sDialog.dismissWithAnimation();
                                                        }
                                                    })
                                                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override
                                                        public void onClick(SweetAlertDialog sDialog) {
                                                            sDialog.cancel();
                                                        }
                                                    })
                                                    .show();
                                        }
                                    });

                                    istirahat.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                                                    .setTitleText("Surat Tugas Sudah Diselesaikan")
                                                    .setConfirmText("OK")
                                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override
                                                        public void onClick(SweetAlertDialog sDialog) {
                                                            sDialog.dismissWithAnimation();
                                                        }
                                                    })
                                                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override
                                                        public void onClick(SweetAlertDialog sDialog) {
                                                            sDialog.cancel();
                                                        }
                                                    })
                                                    .show();
                                        }
                                    });

                                    selesai_perjalanan.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                                                    .setTitleText("Surat Tugas Sudah Diselesaikan")
                                                    .setConfirmText("OK")
                                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override
                                                        public void onClick(SweetAlertDialog sDialog) {
                                                            sDialog.dismissWithAnimation();
                                                        }
                                                    })
                                                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override
                                                        public void onClick(SweetAlertDialog sDialog) {
                                                            sDialog.cancel();
                                                        }
                                                    })
                                                    .show();
                                        }
                                    });
                                } else {
                                    button_persiapan.setImageDrawable(getResources().getDrawable(R.drawable.button_persiapan));
                                    image_mulaiperjalanan.setImageDrawable(getResources().getDrawable(R.drawable.mulai_perjalanan));
                                    image_istirahat.setImageDrawable(getResources().getDrawable(R.drawable.icon_ritase));
                                    button_selesai_perjalanan.setImageDrawable(getResources().getDrawable(R.drawable.button_selesai_perjalanan));

                                    persiapan.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent biodata = new Intent(getBaseContext(), com.tvip.canvasser.menu_persiapan.persiapan.class);
                                            startActivity(biodata);
                                        }
                                    });

                                    mulaiperjalanan.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent mulaiperjalanan = new Intent(getBaseContext(), mulai_perjalanan.class);
                                            startActivity(mulaiperjalanan);
                                        }
                                    });

                                    istirahat.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            final Dialog dialog = new Dialog(MainActivity.this);
                                            dialog.setContentView(R.layout.pilih_rute);
                                            dialog.show();

                                            AutoCompleteTextView act_pilih_jenis = dialog.findViewById(R.id.act_pilih_jenis);
                                            Button tidak = dialog.findViewById(R.id.tidak);
                                            Button ya = dialog.findViewById(R.id.ya);

                                            ArrayAdapter<String> adapter=new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_dropdown_item_1line,jenisrute);
                                            act_pilih_jenis.setAdapter(adapter);
                                            act_pilih_jenis.setAdapter(adapter);

                                            tidak.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    dialog.dismiss();
                                                }
                                            });

                                            ya.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    if(act_pilih_jenis.getText().toString().length() == 0){
                                                        act_pilih_jenis.setError("Pilih Jenis Rute");
                                                    } else {
                                                        dialog.dismiss();
                                                        if(act_pilih_jenis.getText().toString().contains("Canvas Rute")){
                                                            Intent intent = new Intent(getApplicationContext(), akhirikegiatan.class);
                                                            intent.putExtra("Status", "Ritase");
                                                            intent.putExtra("linkstd", "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Persiapan/index_List_Surat_Tugas_canvaser?szEmployeeId=" + kode_employee);
                                                            startActivity(intent);
                                                        } else {
                                                            String szId = act_pilih_jenis.getText().toString();
                                                            String[] parts = szId.split(" - ");
                                                            String szIdSlice = parts[1];

                                                            Intent intent = new Intent(getApplicationContext(), akhirikegiatan.class);
                                                            intent.putExtra("Status", "Ritase");
                                                            intent.putExtra("linkstd", "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_SuratTugasNonRuteV2?szDocId=" + szIdSlice);
                                                            startActivity(intent);
                                                        }
                                                    }
                                                }
                                            });





                                        }
                                    });

                                    selesai_perjalanan.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                                final Dialog dialog = new Dialog(MainActivity.this);
                                                dialog.setContentView(R.layout.pilih_rute);
                                                dialog.show();

                                                AutoCompleteTextView act_pilih_jenis = dialog.findViewById(R.id.act_pilih_jenis);
                                                Button tidak = dialog.findViewById(R.id.tidak);
                                                Button ya = dialog.findViewById(R.id.ya);

                                                ArrayAdapter<String> adapter=new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_dropdown_item_1line,MainActivity.jenisrute);
                                                act_pilih_jenis.setAdapter(adapter);
                                                act_pilih_jenis.setAdapter(adapter);

                                                tidak.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        dialog.dismiss();
                                                    }
                                                });

                                                ya.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        if(act_pilih_jenis.getText().toString().length() == 0){
                                                            act_pilih_jenis.setError("Pilih Jenis Rute");
                                                        } else {
                                                            dialog.dismiss();
                                                            sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                                                            String szEmployeeId = sharedPreferences.getString("szDocCall", null);
                                                            if(act_pilih_jenis.getText().toString().contains("Canvas Rute")){
                                                                pDialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                                                                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                                                                pDialog.setTitleText("Harap Menunggu");
                                                                pDialog.setCancelable(false);
                                                                pDialog.show();
                                                                StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Persiapan/index_List_Surat_Tugas_canvaser?szEmployeeId=" + szEmployeeId, //szDocId,
                                                                        new Response.Listener<String>() {
                                                                            @Override
                                                                            public void onResponse(String response) {

                                                                                pDialog.dismissWithAnimation();

                                                                                try {
                                                                                    int number = 0;
                                                                                    JSONObject obj = new JSONObject(response);
                                                                                    if (obj.getString("status").equals("true")) {
                                                                                        final JSONArray movieArray = obj.getJSONArray("data");
                                                                                        final JSONObject movieObject = movieArray.getJSONObject(movieArray.length()-1);

                                                                                        if(movieObject.getString("bFinished").equals("1") && movieObject.getString("bFullFilled").equals("1")){
                                                                                            new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                                                                                                    .setTitleText("Surat Tugas Sudah Diselesaikan")
                                                                                                    .setConfirmText("OK")
                                                                                                    .show();
                                                                                        } else {
                                                                                            Intent intent = new Intent(getApplicationContext(), akhirikegiatan.class);
                                                                                            intent.putExtra("Status", "Akhiri");
                                                                                            intent.putExtra("linkstd", "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Persiapan/index_List_Surat_Tugas_canvaser?szEmployeeId=" + kode_employee);
                                                                                            startActivity(intent);

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
                                                                                pDialog.dismissWithAnimation();
                                                                                if (error instanceof TimeoutError || error instanceof NoConnectionError)     {
                                                                                    new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                                                                                            .setTitleText("Terjadi kesalahan saat memuat list Surat Tugas")
                                                                                            .setConfirmText("OK")
                                                                                            .show();
                                                                                } else {
                                                                                    new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                                                                                            .setTitleText("Surat Tugas Tidak Ada")
                                                                                            .setConfirmText("OK")
                                                                                            .show();
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
                                                                                500000,
                                                                                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                                                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                                                                        ));
                                                                RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
                                                                requestQueue.add(stringRequest);
                                                            } else {
                                                                pDialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                                                                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                                                                pDialog.setTitleText("Harap Menunggu");
                                                                pDialog.setCancelable(false);
                                                                pDialog.show();

                                                                StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_SuratTugasNonRute?szEmployeeId=" + szEmployeeId, //szDocId,
                                                                        new Response.Listener<String>() {
                                                                            @Override
                                                                            public void onResponse(String response) {

                                                                                pDialog.dismissWithAnimation();

                                                                                try {
                                                                                    int number = 0;
                                                                                    JSONObject obj = new JSONObject(response);
                                                                                    if (obj.getString("status").equals("true")) {
                                                                                        final JSONArray movieArray = obj.getJSONArray("data");
                                                                                        final JSONObject movieObject = movieArray.getJSONObject(0);

                                                                                        if(movieObject.getString("bFinished").equals("1") && movieObject.getString("bFullFilled").equals("1")){
                                                                                            new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                                                                                                    .setTitleText("Surat Tugas Sudah Diselesaikan")
                                                                                                    .setConfirmText("OK")
                                                                                                    .show();
                                                                                        } else {
                                                                                            String szId = act_pilih_jenis.getText().toString();
                                                                                            String[] parts = szId.split(" - ");
                                                                                            String szIdSlice = parts[1];
                                                                                            Intent intent = new Intent(getApplicationContext(), akhirikegiatan.class);
                                                                                            intent.putExtra("Status", "Akhiri");
                                                                                            intent.putExtra("linkstd", "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_SuratTugasNonRuteV2?szDocId=" + szIdSlice);
                                                                                            startActivity(intent);
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
                                                                                pDialog.dismissWithAnimation();
                                                                                if (error instanceof TimeoutError || error instanceof NoConnectionError)     {
                                                                                    new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                                                                                            .setTitleText("Terjadi kesalahan saat memuat list Surat Tugas")
                                                                                            .setConfirmText("OK")
                                                                                            .show();
                                                                                } else {
                                                                                    new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                                                                                            .setTitleText("Surat Tugas Tidak Ada")
                                                                                            .setConfirmText("OK")
                                                                                            .show();
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
                                                                                500000,
                                                                                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                                                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                                                                        ));
                                                                RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
                                                                requestQueue.add(stringRequest);

                                                            }
                                                        }
                                                    }
                                                });

                                        }
                                    });
                                    break;
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
                        image_mulaiperjalanan.setImageDrawable(getResources().getDrawable(R.drawable.mulai_perjalanan_hitam));
                        image_istirahat.setImageDrawable(getResources().getDrawable(R.drawable.istirahat_hitam));
                        button_selesai_perjalanan.setImageDrawable(getResources().getDrawable(R.drawable.selesai_perjalanan_hitam));

                        persiapan.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent biodata = new Intent(getBaseContext(), com.tvip.canvasser.menu_persiapan.persiapan.class);
                                startActivity(biodata);
                            }
                        });

                        mulaiperjalanan.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText("Surat Tugas Belum Tersedia")
                                        .setConfirmText("OK")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.dismissWithAnimation();
                                            }
                                        })
                                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.cancel();
                                            }
                                        })
                                        .show();
                            }
                        });

                        istirahat.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText("Surat Tugas Belum Tersedia")
                                        .setConfirmText("OK")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.dismissWithAnimation();
                                            }
                                        })
                                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.cancel();
                                            }
                                        })
                                        .show();
                            }
                        });

                        selesai_perjalanan.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText("Surat Tugas Belum Tersedia")
                                        .setConfirmText("OK")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.dismissWithAnimation();
                                            }
                                        })
                                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.cancel();
                                            }
                                        })
                                        .show();
                            }
                        });
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
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);
    }

    private void updateUI() {
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String nik_baru = sharedPreferences.getString("szDocCall", null);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Istirahat/index_Istirahat_szId?szId="+ nik_baru,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");
                            for (int i = 0; i < movieArray.length(); i++) {
                                final JSONObject movieObject = movieArray.getJSONObject(i);
                                id_istirahat = movieObject.getString("iInternalId");
                                if(movieObject.getString("szUserUpdatedId").equals("")){
                                    button_persiapan.setImageDrawable(getResources().getDrawable(R.drawable.persiapan_hitam));
                                    image_mulaiperjalanan.setImageDrawable(getResources().getDrawable(R.drawable.mulai_perjalanan_hitam));
                                    button_selesai_perjalanan.setImageDrawable(getResources().getDrawable(R.drawable.selesai_perjalanan_hitam));

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

        stringRequest.setRetryPolicy(
                new DefaultRetryPolicy(
                        500000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                ));
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Push_NotifiCation";
            String description = "Receve Firebase notification";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_WRITE_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                downloadApk();
            } else {
                Toast.makeText(this, "Permission denied!", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void downloadApk() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Persiapan/index_Download?nama_aplikasi="+ "SFA Canvasser",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");
                            for (int i = 0; i < movieArray.length(); i++) {
                                final JSONObject movieObject = movieArray.getJSONObject(i);

                                if(!movieObject.getString("versi").equals(BuildConfig.VERSION_NAME)){
                                    nama_file =movieObject.getString("nama_file");
                                    SweetAlertDialog updateaplikasi = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE);
                                    updateaplikasi.setTitleText("Update Version " + movieObject.getString("versi"));
                                    updateaplikasi.setContentText("Versi baru sekarang telah tersedia. Silahkan update versi anda terlebih dahulu.");
                                    updateaplikasi.setCancelable(false);
                                    updateaplikasi.setConfirmText("Download");
                                    updateaplikasi.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.dismissWithAnimation();
                                            pDialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                                            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                                            pDialog.setTitleText("Harap Menunggu");
                                            pDialog.setCancelable(false);
                                            pDialog.show();

                                            DownloadManager.Request request = null;
                                            try {
                                                request = new DownloadManager.Request(Uri.parse(movieObject.getString("link_download")));
                                            } catch (JSONException e) {
                                                throw new RuntimeException(e);
                                            }
                                            request.setTitle("Your App Title");
                                            request.setDescription("Downloading APK");
                                            try {
                                                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, movieObject.getString("nama_file"));
                                            } catch (JSONException e) {
                                                throw new RuntimeException(e);
                                            }
                                            request.setAllowedOverMetered(true);
                                            request.setAllowedOverRoaming(true);

                                            // Set Basic Authentication header
                                            try {
                                                String auth = "Basic " + Base64.encodeToString(basicAuthCredentials.getBytes("UTF-8"), Base64.NO_WRAP);
                                                request.addRequestHeader("Authorization", auth);
                                            } catch (UnsupportedEncodingException e) {
                                                e.printStackTrace();
                                            }

                                            DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                                            downloadID = downloadManager.enqueue(request);
                                        }
                                    });
                                    updateaplikasi.show();

                                } else {

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
                        DownloadImmediately();
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
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);
    }

    private void DownloadImmediately() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Persiapan/index_Download_force?nama_aplikasi="+ "SFA Canvasser",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");
                            for (int i = 0; i < movieArray.length(); i++) {
                                final JSONObject movieObject = movieArray.getJSONObject(i);

                                if(!movieObject.getString("versi").equals(BuildConfig.VERSION_NAME)){
                                    nama_file =movieObject.getString("nama_file");
                                    SweetAlertDialog updateaplikasi = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE);
                                    updateaplikasi.setTitleText("Update Version " + movieObject.getString("versi"));
                                    updateaplikasi.setContentText("Batas update version telah habis. Harap hubungi ICT.");
                                    updateaplikasi.setCancelable(false);
                                    updateaplikasi.setConfirmText("Keluar");
                                    updateaplikasi.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.dismissWithAnimation();
                                            finish();
                                            finishAffinity();
                                        }
                                    });
                                    updateaplikasi.show();

                                } else {

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

        stringRequest.setRetryPolicy(
                new DefaultRetryPolicy(
                        500000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                ));
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);
    }

    private void installApk() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        File apkFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), nama_file);
        Uri apkUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", apkFile);
        intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(onComplete);
    }

}