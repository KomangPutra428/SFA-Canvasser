package com.tvip.canvasser.menu_mulai_perjalanan;

import static com.tvip.canvasser.menu_mulai_perjalanan.menu_pelanggan.no_surat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.tvip.canvasser.pojo.data_posm_foto_pojo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class foto_materi extends AppCompatActivity {
    ListView list_fotomateriposm;
    ListViewAdapterFoto adapterFoto;
    ListViewAdapterFotoCek adapterFoto2;
    Button batal, lanjutkan;
    int request;
    SharedPreferences sharedPreferences;
    Bitmap bitmap;
    String currentDateandTime2;
    SweetAlertDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foto_materi);

        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddHHmmss");
        currentDateandTime2 = sdf2.format(new Date());

        batal = findViewById(R.id.batal);
        lanjutkan = findViewById(R.id.lanjutkan);

        batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                if(getIntent().getStringExtra("POSM").equals("materi")){
                    adapterFoto.clear();
                } else {
                    adapterFoto2.clear();
                }
            }
        });

        lanjutkan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getIntent().getStringExtra("POSM").equals("materi")){
                    materiPosm();
                } else {
                    cekPosm();
                }

            }
        });

        list_fotomateriposm = findViewById(R.id.list_fotomateriposm);

        list_fotomateriposm.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                ActivityCompat.requestPermissions(foto_materi.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, position);
                request = position;
            }
        });

        if(getIntent().getStringExtra("POSM").equals("materi")){
            adapterFoto = new ListViewAdapterFoto(materi_posm.dataPosmFotoPojoList, foto_materi.this);
            list_fotomateriposm.setAdapter(adapterFoto);
        } else {
            adapterFoto2 = new ListViewAdapterFotoCek(cek_posm.dataPosmFotoPojoList, foto_materi.this);
            list_fotomateriposm.setAdapter(adapterFoto2);
        }
    }

    private void cekPosm() {
        pDialog = new SweetAlertDialog(foto_materi.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Harap Menunggu");
        pDialog.setCancelable(false);
        pDialog.show();

        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_CekPosm",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        CheckedPosm();

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

                params.put("szDocId", currentDateandTime2 + "_" + no_surat);
                params.put("szCustomerId", no_surat);
                params.put("szEmployeeId", nik_baru);

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
        RequestQueue requestQueue2 = Volley.newRequestQueue(foto_materi.this);
        requestQueue2.add(stringRequest2);
    }

    private void CheckedPosm() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                uploadFotoCek();
            }
        }, 500000);

        for(int i = 0; i < cek_posm.dataPosmFotoPojoList.size(); i++) {
            int finalI = i;
            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_SalesCekItem",
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
                    String nik_baru = sharedPreferences.getString("szDocCall", null);


                    params.put("szDocId", currentDateandTime2 + "_" + no_surat);
                    params.put("intItemNumber", String.valueOf(finalI));

                    params.put("szProductId", adapterFoto2.getItem(finalI).getSzId());

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
            RequestQueue requestQueue2 = Volley.newRequestQueue(foto_materi.this);
            requestQueue2.add(stringRequest2);
        }
    }

    private void uploadFotoCek() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                pDialog.dismissWithAnimation();
                new SweetAlertDialog(foto_materi.this, SweetAlertDialog.SUCCESS_TYPE)
                        .setContentText("Data Sudah Disimpan")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                                finish();
                                if(getIntent().getStringExtra("POSM").equals("materi")){
                                    adapterFoto.clear();
                                } else {
                                    adapterFoto2.clear();
                                }
                            }
                        })
                        .show();
            }
        }, 500000);

        for(int i = 0; i < cek_posm.dataPosmFotoPojoList.size(); i++) {
            int finalI = i;
            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_upload_gambar_posm",
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
                    String nik_baru = sharedPreferences.getString("szDocCall", null);

                    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
                    String currentDateandTime2 = sdf2.format(new Date());

                    String[] parts = no_surat.split("-");
                    String restnomor = parts[0];
                    String restnomorbaru = restnomor.replace(" ", "");

                    params.put("szId", mulai_perjalanan.id_pelanggan);

                    params.put("szImageType", "POSM");
                    params.put("szImage", adapterFoto2.getItem(finalI).getFoto());
                    params.put("szCustomerId", no_surat);
                    params.put("intItemNumber", String.valueOf(finalI));


                    params.put("szBranchId", restnomorbaru);
                    params.put("szUserCreatedId", nik_baru);
                    params.put("szUserUpdatedId", nik_baru);

                    params.put("dtmCreated", currentDateandTime2);
                    params.put("dtmLastUpdated", currentDateandTime2);
                    params.put("szSurveyId", adapterFoto2.getItem(finalI).getSzName());


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
            RequestQueue requestQueue2 = Volley.newRequestQueue(foto_materi.this);
            requestQueue2.add(stringRequest2);
        }
    }

    private void materiPosm() {
        pDialog = new SweetAlertDialog(foto_materi.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Harap Menunggu");
        pDialog.setCancelable(false);
        pDialog.show();

        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_SalesPosm",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        qtyPosm();

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

                params.put("szDocId", currentDateandTime2 + "_" + no_surat);
                params.put("szCustomerId", no_surat);
                params.put("szEmployeeId", nik_baru);

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
        RequestQueue requestQueue2 = Volley.newRequestQueue(foto_materi.this);
        requestQueue2.add(stringRequest2);
    }

    private void qtyPosm() {


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                uploadFoto();
            }
        }, 500000);

        for(int i = 0; i < materi_posm.dataPosmFotoPojoList.size(); i++){
            int finalI = i;
            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_SalesPosmItem",
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
                    String nik_baru = sharedPreferences.getString("szDocCall", null);



                    params.put("szDocId", currentDateandTime2 + "_" + no_surat);
                    params.put("intItemNumber", String.valueOf(finalI));
                    params.put("szProductId", adapterFoto.getItem(finalI).getSzId());

                    params.put("decQty", adapterFoto.getItem(finalI).getQty());
                    params.put("szUomId", nik_baru);

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
            RequestQueue requestQueue2 = Volley.newRequestQueue(foto_materi.this);
            requestQueue2.add(stringRequest2);
        }
    }

    private void uploadFoto() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                pDialog.dismissWithAnimation();
                new SweetAlertDialog(foto_materi.this, SweetAlertDialog.SUCCESS_TYPE)
                        .setContentText("Data Sudah Disimpan")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();

                                Intent intent = new Intent(getApplicationContext(), menu_pelanggan.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);

                                if(getIntent().getStringExtra("POSM").equals("materi")){
                                    adapterFoto.clear();
                                } else {
                                    adapterFoto2.clear();
                                }
                            }
                        })
                        .show();
            }
        }, 500000);

        for(int i = 0; i < materi_posm.dataPosmFotoPojoList.size(); i++){
            int finalI = i;
            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_upload_gambar_posm",
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
                    String nik_baru = sharedPreferences.getString("szDocCall", null);

                    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
                    String currentDateandTime2 = sdf2.format(new Date());

                    String[] parts = no_surat.split("-");
                    String restnomor = parts[0];
                    String restnomorbaru = restnomor.replace(" ", "");

                    params.put("szId", mulai_perjalanan.id_pelanggan);

                    params.put("szImageType", "POSM");
                    params.put("szImage", adapterFoto.getItem(finalI).getFoto());
                    params.put("szCustomerId", no_surat);
                    params.put("intItemNumber", String.valueOf(finalI));


                    params.put("szBranchId", restnomorbaru);
                    params.put("szUserCreatedId", nik_baru);
                    params.put("szUserUpdatedId", nik_baru);

                    params.put("dtmCreated", currentDateandTime2);
                    params.put("dtmLastUpdated", currentDateandTime2);
                    params.put("szSurveyId", adapterFoto.getItem(finalI).getSzName());


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
            RequestQueue requestQueue2 = Volley.newRequestQueue(foto_materi.this);
            requestQueue2.add(stringRequest2);
        }

    }

    @Override
    public void onBackPressed() {
        finish();
        if(getIntent().getStringExtra("POSM").equals("materi")){
            adapterFoto.clear();
        } else {
            adapterFoto2.clear();
        }
    }

    public class ListViewAdapterFotoCek extends ArrayAdapter<data_posm_foto_pojo> {

        private class ViewHolder {
            TextView produk;
            ImageView uploadgambar;
        }

        List<data_posm_foto_pojo> dataPosmPojoList;
        private Context context;

        public ListViewAdapterFotoCek(List<data_posm_foto_pojo> dataPosmPojoList, Context context) {
            super(context, R.layout.list_uploadfoto, dataPosmPojoList);
            this.dataPosmPojoList = dataPosmPojoList;
            this.context = context;

        }

        public int getCount() {
            return cek_posm.dataPosmFotoPojoList.size();
        }

        public data_posm_foto_pojo getItem(int position) {
            return cek_posm.dataPosmFotoPojoList.get(position);
        }

        @Override
        public int getViewTypeCount() {
            int count;
            if (cek_posm.dataPosmFotoPojoList.size() > 0) {
                count = getCount();
            } else {
                count = 1;
            }
            return count;
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
            data_posm_foto_pojo movieItem = cek_posm.dataPosmFotoPojoList.get(position);
            if (convertView == null) {
                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.list_uploadfoto, parent, false);
                viewHolder.produk = (TextView) convertView.findViewById(R.id.produk);
                viewHolder.uploadgambar = (ImageView) convertView.findViewById(R.id.uploadgambar);

                convertView.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.produk.setText(movieItem.getSzName());
            if(!movieItem.getFoto().equals("0")){
                viewHolder.uploadgambar.setImageBitmap(StringToBitMap(movieItem.getFoto()));
            }


            return convertView;
        }
    }

    public class ListViewAdapterFoto extends ArrayAdapter<data_posm_foto_pojo> {

        private class ViewHolder {
            TextView produk;
            ImageView uploadgambar;
        }

        List<data_posm_foto_pojo> dataPosmPojoList;
        private Context context;

        public ListViewAdapterFoto(List<data_posm_foto_pojo> dataPosmPojoList, Context context) {
            super(context, R.layout.list_uploadfoto, dataPosmPojoList);
            this.dataPosmPojoList = dataPosmPojoList;
            this.context = context;

        }

        public int getCount() {
            return materi_posm.dataPosmFotoPojoList.size();
        }

        public data_posm_foto_pojo getItem(int position) {
            return materi_posm.dataPosmFotoPojoList.get(position);
        }

        @Override
        public int getViewTypeCount() {
            int count;
            if (materi_posm.dataPosmFotoPojoList.size() > 0) {
                count = getCount();
            } else {
                count = 1;
            }
            return count;
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
            data_posm_foto_pojo movieItem = materi_posm.dataPosmFotoPojoList.get(position);
            if (convertView == null) {
                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.list_uploadfoto, parent, false);
                viewHolder.produk = (TextView) convertView.findViewById(R.id.produk);
                viewHolder.uploadgambar = (ImageView) convertView.findViewById(R.id.uploadgambar);

                convertView.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.produk.setText(movieItem.getSzName());
            if(!movieItem.getFoto().equals("0")){
                viewHolder.uploadgambar.setImageBitmap(StringToBitMap(movieItem.getFoto()));
            }


            return convertView;
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == request){
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
                if(getIntent().getStringExtra("POSM").equals("materi")){
                    InputStream inputStream = getContentResolver().openInputStream(path);
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    adapterFoto.getItem(request).setFoto(imagetoString(bitmap));
                    adapterFoto.notifyDataSetChanged();
                } else {
                    InputStream inputStream = getContentResolver().openInputStream(path);
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    adapterFoto2.getItem(request).setFoto(imagetoString(bitmap));
                    adapterFoto2.notifyDataSetChanged();
                }



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

    public Bitmap StringToBitMap(String encodedString){
        try{
            byte [] encodeByte = Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }
        catch(Exception e){
            e.getMessage();
            return null;
        }
    }

    private String ImageToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

}