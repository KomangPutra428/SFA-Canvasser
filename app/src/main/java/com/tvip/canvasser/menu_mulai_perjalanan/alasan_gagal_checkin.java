package com.tvip.canvasser.menu_mulai_perjalanan;

import static com.tvip.canvasser.menu_mulai_perjalanan.detailscangagal_dalamrute.intItemNumbers;
import static com.tvip.canvasser.menu_mulai_perjalanan.mulai_perjalanan.STD;
import static com.tvip.canvasser.menu_mulai_perjalanan.mulai_perjalanan.pelanggan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.tvip.canvasser.R;
import com.tvip.canvasser.menu_utama.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class alasan_gagal_checkin extends AppCompatActivity {
    TextView toko, kodetoko, alamattoko, idstd;
    AutoCompleteTextView alasangagalcheckin;
    Button batal, lanjutkan;
    LinearLayout fotooutlet, linear_gambar;
    ImageView uploadgambar;
    Bitmap bitmap;
    TextView textupload;
    ArrayList<String> GagalCheckin = new ArrayList<>();
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alasan_gagal_checkin);

        toko = findViewById(R.id.toko);
        kodetoko = findViewById(R.id.kodetoko);
        alamattoko = findViewById(R.id.alamattoko);
        idstd = findViewById(R.id.tv_idStd);

        alasangagalcheckin = findViewById(R.id.alasangagalcheckin);
        fotooutlet = findViewById(R.id.fotooutlet);
        uploadgambar = findViewById(R.id.uploadgambar);
        batal = findViewById(R.id.batal);
        lanjutkan = findViewById(R.id.lanjutkan);
        linear_gambar = findViewById(R.id.linear_gambar);
        textupload = findViewById(R.id.textupload);

        toko.setText(detailscangagal_dalamrute.namatoko.getText().toString());
        kodetoko.setText(detailscangagal_dalamrute.code.getText().toString());
        alamattoko.setText(detailscangagal_dalamrute.address.getText().toString());
        idstd.setText(detailscangagal_dalamrute.idstd.getText().toString());


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

                            alasangagalcheckin.setAdapter(new ArrayAdapter<String>(alasan_gagal_checkin.this, android.R.layout.simple_expandable_list_item_1, GagalCheckin));
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
        RequestQueue requestkota = Volley.newRequestQueue(alasan_gagal_checkin.this);
        requestkota.add(rest);

        alasangagalcheckin.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                    alasangagalcheckin.showDropDown();
            }
        });

        alasangagalcheckin.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                alasangagalcheckin.showDropDown();
                return false;
            }
        });

        fotooutlet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(alasan_gagal_checkin.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

            }
        });

        batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        lanjutkan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alasangagalcheckin.setError(null);
                //JIKA ALASAN GAGAL CHECKIN BELUM DIPILIH MAKA AKAN KELUAR PERINGATAN
                if(alasangagalcheckin.getText().toString().length() == 0){
                    alasangagalcheckin.setError("Silahkan Pilih Alasan");

                } else {
                    Intent dalam_rute = new Intent(getBaseContext(), menu_pelanggan.class);

                    dalam_rute.putExtra("kode", kodetoko.getText().toString());
                    dalam_rute.putExtra("idStd", STD);
                    dalam_rute.putExtra("szDocDo", getIntent().getStringExtra("szDocDo"));

                    dalam_rute.putExtra("Status", "Failed");
                    SF();
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
                            params.put("dtmStart", currentDateandTime2);
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

                    RequestQueue requestQueue2 = Volley.newRequestQueue(alasan_gagal_checkin.this);
                    requestQueue2.add(stringRequest2);
                }

            }
        });
    }

    private void SF() {
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

                String rest = alasangagalcheckin.getText().toString();
                String[] parts = rest.split("-");
                String restnomor = parts[0];

                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String currentDateandTime2 = sdf2.format(new Date());

                params.put("szDocId", STD);
                params.put("intItemNumber", intItemNumbers);

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

                params.put("szReasonIdCheckin", restnomor);
                params.put("szReasonFailedScan", restnomor2);
                params.put("decRadiusDiff", "0");

                System.out.println("params = " + params);
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
        RequestQueue requestQueue2 = Volley.newRequestQueue(alasan_gagal_checkin.this);
        requestQueue2.getCache().clear();
        requestQueue2.add(stringRequest2);
    }


    private String ImageToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 1){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Image"), 1);
            }else{
                Toast.makeText(getApplicationContext(), "You don't have permission to access gallery!", Toast.LENGTH_LONG).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Uri path = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(path);
                bitmap = BitmapFactory.decodeStream(inputStream);
                uploadgambar.setImageBitmap(bitmap);
                uploadgambar.setVisibility(View.VISIBLE);
                textupload.setVisibility(View.GONE);
                ViewGroup.LayoutParams params = linear_gambar.getLayoutParams();
                ViewGroup.LayoutParams params2 = uploadgambar.getLayoutParams();
                ViewGroup.LayoutParams params3 = fotooutlet.getLayoutParams();
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                params2.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                params2.width = ViewGroup.LayoutParams.WRAP_CONTENT;

                params3.height = ViewGroup.LayoutParams.WRAP_CONTENT;


                linear_gambar.setLayoutParams(params);
                uploadgambar.setLayoutParams(params2);
                uploadgambar.setPadding(20,20,20,20);
                fotooutlet.setLayoutParams(params3);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private String imagetoString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }
}