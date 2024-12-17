package com.tvip.canvasser.menu_mulai_perjalanan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.tvip.canvasser.menu_mulai_perjalanan.menu_pelanggan.no_surat;

public class update_outlet extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    AutoCompleteTextView jenisperubahan;
    EditText editnilai;
    LinearLayout fotooutlet, linear_gambar;
    ImageView uploadgambar;
    Bitmap bitmap;
    Button simpan, batal;
    TextView textupload;
    ArrayList<String> ReasonUpdate = new ArrayList<>();
    SweetAlertDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_outlet);
        jenisperubahan = findViewById(R.id.jenisperubahan);
        fotooutlet = findViewById(R.id.fotooutlet);
        linear_gambar = findViewById(R.id.linear_gambar);
        editnilai = findViewById(R.id.editnilai);
        simpan = findViewById(R.id.simpan);
        batal = findViewById(R.id.batal);

        uploadgambar = findViewById(R.id.uploadgambar);
        textupload = findViewById(R.id.textupload);

        fotooutlet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(update_outlet.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

            }
        });

        jenisperubahan.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                    jenisperubahan.showDropDown();
            }
        });

        jenisperubahan.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                jenisperubahan.showDropDown();
                return false;
            }
        });

        StringRequest rest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_Reason_Update",
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
                                    ReasonUpdate.add(id + "-" + jenis_istirahat);

                                }
                            }
                            jenisperubahan.setAdapter(new ArrayAdapter<String>(update_outlet.this, android.R.layout.simple_expandable_list_item_1, ReasonUpdate));
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
        RequestQueue requestkota = Volley.newRequestQueue(update_outlet.this);
        requestkota.add(rest);

        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jenisperubahan.setError(null);
                editnilai.setError(null);
                if(jenisperubahan.getText().toString().length() == 0){
                    jenisperubahan.setError("Silahkan Pilih Alasan");
                } else if (editnilai.getText().toString().length() == 0) {
                    editnilai.setError("Silahkan Isi Nilai");
                } else if (textupload.getVisibility() == View.VISIBLE) {
                    new SweetAlertDialog(update_outlet.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Upload Gambar")
                            .setConfirmText("OK")
                            .show();
                } else {
                    pDialog = new SweetAlertDialog(update_outlet.this, SweetAlertDialog.PROGRESS_TYPE);
                    pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                    pDialog.setTitleText("Harap Menunggu");
                    pDialog.setCancelable(false);
                    pDialog.show();
                    StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_update_outlet",
                            new Response.Listener<String>() {

                                @Override
                                public void onResponse(String response) {
                                    uploadgambaroutlet();

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

                            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddHHmmss");
                            String currentDateandTime2 = sdf2.format(new Date());

                            SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String currentDateandTime3 = sdf3.format(new Date());

                            String[] parts = jenisperubahan.getText().toString().split("-");
                            String restnomor = parts[0];
                            String restnomorbaru = restnomor.replace(" ", "");

                            params.put("szDocId", currentDateandTime2);
                            params.put("dtmDoc", currentDateandTime3);

                            params.put("szEmployeeId", nik_baru);
                            params.put("szCustomerId", no_surat);
                            params.put("CustomerFieldUpdate", restnomorbaru);

                            params.put("szValue", editnilai.getText().toString());
                            params.put("szDocCallId", mulai_perjalanan.id_pelanggan);

                            params.put("szUserCreatedId", nik_baru);
                            params.put("szUserUpdatedId", nik_baru);

                            params.put("dtmCreated", currentDateandTime2);
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
                    RequestQueue requestQueue2 = Volley.newRequestQueue(update_outlet.this);
                    requestQueue2.add(stringRequest2);
                }
            }
        });


    }

    private void uploadgambaroutlet() {
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_upload_gambar",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        pDialog.dismissWithAnimation();
                        SweetAlertDialog success = new SweetAlertDialog(update_outlet.this, SweetAlertDialog.SUCCESS_TYPE);
                        success.setContentText("Data Telah Disimpan");
                        success.setCancelable(false);
                        success.setConfirmText("OK");
                        success.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                success.dismissWithAnimation();
                                finish();
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

                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String currentDateandTime2 = sdf2.format(new Date());

                String[] parts = nik_baru.split("-");
                String restnomor = parts[0];
                String restnomorbaru = restnomor.replace(" ", "");

                params.put("iId", currentDateandTime2);
                params.put("szId", mulai_perjalanan.id_pelanggan);

                params.put("szImageType", "SURVEY");
                params.put("szImage", ImageToString(bitmap));
                params.put("szCustomerId", no_surat);

                params.put("szBranchId", restnomorbaru);
                params.put("szUserCreatedId", nik_baru);
                params.put("szUserUpdatedId", nik_baru);

                params.put("dtmCreated", currentDateandTime2);
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
        RequestQueue requestQueue2 = Volley.newRequestQueue(update_outlet.this);
        requestQueue2.add(stringRequest2);
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

    private String ImageToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }
}