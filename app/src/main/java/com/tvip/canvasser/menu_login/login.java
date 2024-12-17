package com.tvip.canvasser.menu_login;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tvip.canvasser.menu_utama.MainActivity;
import com.tvip.canvasser.Perangkat.HttpsTrustManager;
import com.tvip.canvasser.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class login extends AppCompatActivity {
    EditText nikbaru;
    public static EditText password;
    ArrayList<String> employees = new ArrayList<>();
    Button login;
    SweetAlertDialog success;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        HttpsTrustManager.allowAllSSL();

        nikbaru = findViewById(R.id.nikbaru);
        password = findViewById(R.id.editpassword);
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        login = findViewById(R.id.login);

        if (ActivityCompat.checkSelfPermission(
                login.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                login.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nikbaru.getText().toString().length() == 0 && password.getText().toString().length() == 0) {
                    nikbaru.setError("NIK diperlukan!");
                    password.setError("Password diperlukan!");
                } else if (nikbaru.getText().toString().length() == 0) {
                    nikbaru.setError("NIK diperlukan!");
                } else if (password.getText().toString().length() == 0) {
                    password.setError("Password diperlukan!");
                } else {
                    sendLogin();
                }
            }
        });


    }

    private void sendLogin() {

        final SweetAlertDialog pDialog = new SweetAlertDialog(login.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Harap Menunggu");

        pDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/api/login/index?nik_baru=" + nikbaru.getText().toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.getString("status").equals("true")) {
                                JSONArray movieArray = obj.getJSONArray("data");
                                for (int i = 0; i < movieArray.length(); i++) {

                                    final JSONObject movieObject = movieArray.getJSONObject(i);
                                    pDialog.cancel();

                                    if (movieObject.getString("password").equals(md5(password.getText().toString()))) {
                                        final String lokasi = movieObject.getString("lokasi_struktur");
                                        final String jabatan = movieObject.getString("jabatan_struktur");
                                        final String level = movieObject.getString("level_jabatan_karyawan");
                                        final String nama = movieObject.getString("nama_karyawan_struktur");

                                        if(movieObject.getString("szBranch").equals("336") || movieObject.getString("szBranch").equals("321") || movieObject.getString("szBranch").equals("324")){
                                            getDoccall(lokasi, jabatan, level, nama, "rest_server_canvaser_asa_baru_LHP");
                                        } else {
                                            getDoccall(lokasi, jabatan, level, nama, "rest_server_canvaser_tvip_baru_LHP");
                                        }
                                    } else if (!movieObject.getString("password").equals(md5(password.getText().toString()))) {
                                        new SweetAlertDialog(login.this, SweetAlertDialog.ERROR_TYPE)
                                                .setContentText("Oops... NIK / Password Salah")
                                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(SweetAlertDialog sDialog) {
                                                        sDialog.dismissWithAnimation();
                                                    }
                                                })
                                        .show();
                                    }

                                }

                            } else {
                                new SweetAlertDialog(login.this, SweetAlertDialog.ERROR_TYPE)
                                        .setContentText("Oops... NIK / Password Salah")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.dismissWithAnimation();
                                            }
                                        })
                                        .show();
                                pDialog.cancel();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.cancel();
                        if (error instanceof ServerError) {
                            new SweetAlertDialog(login.this, SweetAlertDialog.ERROR_TYPE)
                                    .setContentText("Nik anda salah!")
                                    .show();
                        } else {
                            new SweetAlertDialog(login.this, SweetAlertDialog.ERROR_TYPE)
                                    .setContentText("Jaringan sedang bermasalah!")
                                    .show();
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
        };
        stringRequest.setRetryPolicy(
                new DefaultRetryPolicy(
                        500000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                ));
        RequestQueue requestQueue = Volley.newRequestQueue(login.this);
        requestQueue.add(stringRequest);
    }

    private void getDoccall(String lokasi, String jabatan, String level, String nama, String link) {
        StringRequest biodata = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+link+"/utilitas/Mulai_Perjalanan/index_nik_Doccall?nik_baru=" + nikbaru.getText().toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");
                            for (int i = 0; i < movieArray.length(); i++) {
                                final JSONObject movieObject = movieArray.getJSONObject(i);

                                //JIKA PRESELLER MAKA TKO
                                if (movieObject.getString("szRouteType").equals("TKO")){
                                    Toast.makeText(login.this, "OK TKO", Toast.LENGTH_SHORT).show();

                                    success = new SweetAlertDialog(login.this, SweetAlertDialog.SUCCESS_TYPE);
                                    success.setContentText("Selamat Datang TKO");
                                    success.setCancelable(false);
                                    success.setConfirmText("OK");
                                    success.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {

                                            Intent intent = new Intent(login.this, MainActivity.class);
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putString("nik_baru", nikbaru.getText().toString());

                                            try {
                                                editor.putString("szDocCall", movieObject.getString("szId"));
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                            editor.putString("lokasi", lokasi);
                                            editor.putString("jabatan", jabatan);
                                            editor.putString("level", level);
                                            editor.putString("nama", nama);
                                            editor.putString("password", password.getText().toString());

                                            editor.apply();
                                            sDialog.dismissWithAnimation();
                                            startActivity(intent);
                                        }
                                    })
                                    .show();

                                    //JIKA DRIVER DELIVRY MAKA DEL
                                } else if (movieObject.getString("szRouteType").equals("DEL")){
                                    Toast.makeText(login.this, "OK DEL", Toast.LENGTH_SHORT).show();

                                    success = new SweetAlertDialog(login.this, SweetAlertDialog.SUCCESS_TYPE);
                                    success.setContentText("Selamat Datang Delivery");
                                    success.setCancelable(false);
                                    success.setConfirmText("OK");
                                    success.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {

                                            Intent intent = new Intent(login.this, MainActivity.class);
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putString("nik_baru", nikbaru.getText().toString());

                                            try {
                                                editor.putString("szDocCall", movieObject.getString("szId"));
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                            editor.putString("lokasi", lokasi);
                                            editor.putString("jabatan", jabatan);
                                            editor.putString("level", level);
                                            editor.putString("nama", nama);
                                            editor.putString("password", password.getText().toString());


                                            editor.apply();
                                            sDialog.dismissWithAnimation();
                                            startActivity(intent);
                                        }
                                    })
                                    .show();

                                } else if (movieObject.getString("szRouteType").equals("CAN") || (movieObject.getString("szRouteType").equals("CAN-NON-RUTE"))) {
                                    chooseIDEmployee(nikbaru.getText().toString(), movieObject.getString("szId"), lokasi, jabatan, level, nama, password.getText().toString(), link);
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
                        Toast.makeText(login.this, "ERROR RESPONSE", Toast.LENGTH_SHORT).show();
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

        biodata.setRetryPolicy(
                new DefaultRetryPolicy(
                        500000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                ));
        RequestQueue biodataQueue = Volley.newRequestQueue(this);
        biodataQueue.add(biodata);
    }

    private void chooseIDEmployee(String nikbaru, String szId, String lokasi, String jabatan, String level, String nama, String toString1, String link) {

        final Dialog dialog = new Dialog(login.this);
        dialog.setContentView(R.layout.pilih_id_employee);
        dialog.show();

        AutoCompleteTextView act_pilih_karyawan = dialog.findViewById(R.id.act_pilih_karyawan);
        Button tidak = dialog.findViewById(R.id.tidak);
        Button ya = dialog.findViewById(R.id.ya);

        tidak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        ya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(act_pilih_karyawan.getText().toString().length() == 0){
                    act_pilih_karyawan.setError("Pilih ID");
                } else {
                    success = new SweetAlertDialog(login.this, SweetAlertDialog.SUCCESS_TYPE);
                    success.setContentText("Selamat Datang Canvaser");
                    success.setCancelable(false);
                    success.setConfirmText("OK");
                    success.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {

                            Intent intent = new Intent(login.this, MainActivity.class);
                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            editor.putString("nik_baru", nikbaru);
                            editor.putString("szDocCall", act_pilih_karyawan.getText().toString());
                            editor.putString("lokasi", lokasi);
                            editor.putString("jabatan", jabatan);
                            editor.putString("level", level);
                            editor.putString("nama", nama);
                            editor.putString("password", password.getText().toString());


                            String[] parts = act_pilih_karyawan.getText().toString().split("-");
                            String result = parts[0];

                            if(act_pilih_karyawan.getText().toString().contains("RP")){
                                if(result.equals("336") || result.equals("321") || result.equals("324")){
                                    editor.putString("link","rest_server_canvaser_asa_baru_LHP");
                                } else {
                                    editor.putString("link","rest_server_canvaser_tvip_baru_LHP");
                                }

                            } else if(act_pilih_karyawan.getText().toString().contains("SPV")){
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

                            editor.apply();
                            sDialog.dismissWithAnimation();
                            startActivity(intent);
                        }
                    });
                    success.show();
                }
            }
        });

        StringRequest rest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+link+"/utilitas/Mulai_Perjalanan/index_Employee_Id?szIdCard=" + nikbaru,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        employees.clear();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("status").equals("true")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    employees.add(jsonObject1.getString("szId"));


                                }
                                act_pilih_karyawan.setAdapter(new ArrayAdapter<String>(login.this, android.R.layout.simple_expandable_list_item_1, employees));

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
        RequestQueue requestkota = Volley.newRequestQueue(login.this);
        requestkota.add(rest);




    }

    public String md5(String s) {
        String digest = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(s.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder(2*hash.length);
            for(byte b : hash)
            {
                sb.append(String.format("%02x", b&0xff));
            }
            digest = sb.toString();
        } catch (NoSuchAlgorithmException ex)
        {
            Logger.getLogger(login.class.getName()).log(Level.SEVERE, null, ex);
        }
        return digest;
    }

    @Override
    public void onBackPressed() {
        new SweetAlertDialog(login.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Apakah anda yakin?")
                .setContentText("Anda akan keluar dari aplikasi ini")
                .setConfirmText("Yes")
                .setCancelText("Cancel")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        finishAffinity();
                        finish();
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
}