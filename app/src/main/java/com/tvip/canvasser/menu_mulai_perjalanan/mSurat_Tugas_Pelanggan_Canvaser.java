package com.tvip.canvasser.menu_mulai_perjalanan;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.card.MaterialCardView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.tvip.canvasser.Perangkat.HttpsTrustManager;
import com.tvip.canvasser.R;
import com.tvip.canvasser.pojo.data_daftar_kunjungan;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class mSurat_Tugas_Pelanggan_Canvaser extends AppCompatActivity {

    TextView tanggal_callplan, surattugas;
    MaterialCardView listpelanggan;
    SharedPreferences sharedPreferences;
    static String szDocId;
    static String idStd;
    String pelanggan;


    ListView listdaftarkunjungan;
    List<data_daftar_kunjungan> datadaftarkunjungan = new ArrayList<>();

    mSurat_Tugas_Pelanggan_Canvaser.ListViewAdapterCallPlan adapter;

    MaterialToolbar pengaturanBar;


    SweetAlertDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HttpsTrustManager.allowAllSSL();
        setContentView(R.layout.activity_msurat_tugas_pelanggan_canvaser);

        HttpsTrustManager.allowAllSSL();
        setContentView(R.layout.activity_callplan);
        tanggal_callplan = findViewById(R.id.tanggal_callplan);
        surattugas = findViewById(R.id.surattugas);
        listpelanggan = findViewById(R.id.listpelanggan);
        pengaturanBar = findViewById(R.id.persiapanbar);

        //Toast.makeText(callplan.this, "TEST" + szDocId, Toast.LENGTH_SHORT).show();

        listdaftarkunjungan = findViewById(R.id.listdaftarkunjungan);

        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String nik_baru = sharedPreferences.getString("szDocCall", null);

//        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Persiapan/index_SuratTugas?nik_baru="+ nik_baru,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//
//                        try {
//                            JSONObject obj = new JSONObject(response);
//                            final JSONArray movieArray = obj.getJSONArray("data");
//                            for (int i = 0; i < movieArray.length(); i++) {
//                                final JSONObject movieObject = movieArray.getJSONObject(i);
//
//                                tanggal_callplan.setText("Call Plan : " + movieObject.getString("dtmDoc"));
//                                surattugas.setText(movieObject.getString("szDocId"));
//                                szDocId = movieObject.getString("szDocId");
//
//                            }
//
//
//                        } catch(JSONException e){
//                            e.printStackTrace();
//
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//
//                    }
//                })
//
//        {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                HashMap<String, String> params = new HashMap<String, String>();
//                String creds = String.format("%s:%s", "admin", "Databa53");
//                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
//                params.put("Authorization", auth);
//                return params;
//            }
//        };
//
//        stringRequest.setRetryPolicy(
//                new DefaultRetryPolicy(
//                        500000,
//                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
//                ));
//        RequestQueue requestQueue = Volley.newRequestQueue(callplan.this);
//        requestQueue.add(stringRequest);

        //-------------------------------------------------------------------------------------------------


        listdaftarkunjungan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                idStd = ((data_daftar_kunjungan) parent.getItemAtPosition(position)).getIdStd();
                String tanggaldokumen = ((data_daftar_kunjungan) parent.getItemAtPosition(position)).getTanggaldokumen();
                String ritase = ((data_daftar_kunjungan) parent.getItemAtPosition(position)).getRitase();

                sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                String nik_baru = sharedPreferences.getString("szDocCall", null);

                IntentIntegrator intentIntegrator = new IntentIntegrator(mSurat_Tugas_Pelanggan_Canvaser.this);
                intentIntegrator.initiateScan();


                pelanggan = "luar";

            }
        });

        list_surat_tugas_pelanggan_canvas();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Hasil tidak ditemukan", Toast.LENGTH_SHORT).show();

            } else {

                try {
                    JSONObject object = new JSONObject(result.getContents());
                    System.out.println(object.getString("nik"));

                } catch (JSONException e) {

                    e.printStackTrace();

                    pDialog = new SweetAlertDialog(mSurat_Tugas_Pelanggan_Canvaser.this, SweetAlertDialog.PROGRESS_TYPE);
                    pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                    pDialog.setTitleText("Harap Menunggu");
                    pDialog.setCancelable(false);
                    pDialog.show();

                    //JIKA PELANGGAN DALAM RUTE,
                    //pelanggan.equals("dalam") berasal ketika menekan button dalam rute
                    if (pelanggan.equals("dalam")) {

                        Toast.makeText(mSurat_Tugas_Pelanggan_Canvaser.this, "Pelanggan Dalam Rute", Toast.LENGTH_SHORT).show();
                        //JIKA PELANGGAN DILUAR RUTE

                    } else if (pelanggan.equals("luar")){

                        StringRequest rest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_Scan_Dalam_Pelanggan_Driver?szCustomerId="+result.getContents(),
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            if (jsonObject.getString("status").equals("true")) {

                                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                                for (int i = 0; i < 1; i++) {
                                                    pDialog.dismissWithAnimation();
                                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);


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

                                        //JIKA SCAN TIDAK SESUAI MAKA AKAN MASUK KE MENU YANG BERISI KUMPULAN LIST TOKO2

                                        pDialog.dismissWithAnimation();

                                        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                                        //szEmployeeId = 336-RP049
                                        String szEmployeeId = sharedPreferences.getString("szDocCall", null);
                                        Toast.makeText(mSurat_Tugas_Pelanggan_Canvaser.this, "OKE = " + szEmployeeId, Toast.LENGTH_SHORT).show();

                                        Intent ok = new Intent(getBaseContext(), mPenjualan_Terima_Produk_Canvas.class);
                                        ok.putExtra("szEmployeeId", szEmployeeId);
                                        ok.putExtra("idStd", idStd);
                                        startActivity(ok);

                                        //---------------------------------------------------------------------------------------

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
                        RequestQueue requestkota = Volley.newRequestQueue(mSurat_Tugas_Pelanggan_Canvaser.this);
                        requestkota.add(rest);

                        //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

                    } else {

                        Toast.makeText(mSurat_Tugas_Pelanggan_Canvaser.this, "Terjadi Kesalahan", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    //YANG DIPAKE DELIVERY
    //MENAMPILKAN LIST PELANGGAN BERDASARKAN SURAT TUGAS sZDocId
    private void list_surat_tugas_pelanggan_canvas() {

        pDialog = new SweetAlertDialog(mSurat_Tugas_Pelanggan_Canvaser.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Harap Menunggu");
        pDialog.show();
        pDialog.setCancelable(false);

        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        //szEmployeeId = 336-RP049
        String szEmployeeId = sharedPreferences.getString("szDocCall", null);
        Toast.makeText(mSurat_Tugas_Pelanggan_Canvaser.this, "OKE = " + szEmployeeId, Toast.LENGTH_SHORT).show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_suratTugasUntukCanvas?id_driver=" + szEmployeeId, //szDocId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        pDialog.dismissWithAnimation();

                        try {
                            int number = 0;
                            JSONObject obj = new JSONObject(response);
                            if (obj.getString("status").equals("true")) {
                                final JSONArray movieArray = obj.getJSONArray("data");
                                for (int i = 0; i < movieArray.length(); i++) {
                                    final JSONObject movieObject = movieArray.getJSONObject(i);
                                    final data_daftar_kunjungan movieItem = new data_daftar_kunjungan(
                                            movieObject.getString("id_std"),
                                            movieObject.getString("dtmCreated"),
                                            movieObject.getString("ritase"));

                                    datadaftarkunjungan.add(movieItem);

                                }

                                adapter = new mSurat_Tugas_Pelanggan_Canvaser.ListViewAdapterCallPlan(datadaftarkunjungan, getApplicationContext());
                                listdaftarkunjungan.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                            } else {
                                Toast.makeText(mSurat_Tugas_Pelanggan_Canvaser.this, "Data belum ada", Toast.LENGTH_SHORT).show();
                            }


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
                        pDialog.dismissWithAnimation();
                        Toast.makeText(mSurat_Tugas_Pelanggan_Canvaser.this, "Terjadi kesalahan saat memuat list Surat Tugas", Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onResume() {
        super.onResume();
        //updateUIFinished();
        //updateStart();
    }

    public static class ListViewAdapterCallPlan extends ArrayAdapter<data_daftar_kunjungan> {
        private List<data_daftar_kunjungan> adptr_data_pelanggan_kunjungan;

        private Context context;

        public ListViewAdapterCallPlan(List<data_daftar_kunjungan> adptr_data_pelanggan_kunjungan, Context context) {
            super(context, R.layout.list_surat_tugas_daftar_kunjungan, adptr_data_pelanggan_kunjungan);
            this.adptr_data_pelanggan_kunjungan = adptr_data_pelanggan_kunjungan;
            this.context = context;
        }

        @SuppressLint("NewApi")
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = LayoutInflater.from(context);

            View listViewItem = inflater.inflate(R.layout.list_surat_tugas_daftar_kunjungan, null, true);

            TextView daftar_kunjungan_nomor_surat_tugas = listViewItem.findViewById(R.id.daftar_kunjungan_nomor_surat_tugas);
            TextView daftar_kunjungan_tanggal_dokumen = listViewItem.findViewById(R.id.daftar_kunjungan_tanggal_dokumen);
            TextView daftar_kunjungan_ritase = listViewItem.findViewById(R.id.daftar_kunjungan_ritase);

            data_daftar_kunjungan data = getItem(position);

            daftar_kunjungan_nomor_surat_tugas.setText(data.getIdStd());
            //daftar_kunjungan_tanggal_dokumen.setText(data.getTanggaldokumen());

            //MENGUBAH FORMAT TANGGAL
            String strCurrentDate= data.getTanggaldokumen();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date newDate = null;
            try {
                newDate = format.parse(strCurrentDate);
                format = new SimpleDateFormat("dd MMMM yyyy");
                String date = format.format(newDate);
                daftar_kunjungan_tanggal_dokumen.setText(date);
                System.out.println("HASIL TANGGAL = " + date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            daftar_kunjungan_ritase.setText("Ritase " + data.getRitase());

            return listViewItem;
        }
    }
}