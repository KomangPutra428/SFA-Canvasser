package com.tvip.canvasser.menu_selesai_perjalanan;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
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
import com.tvip.canvasser.Perangkat.HttpsTrustManager;
import com.tvip.canvasser.R;
import com.tvip.canvasser.menu_utama.MainActivity;
import com.tvip.canvasser.pojo.data_pelanggan_belum_finish_pojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class selesai_perjalanan extends AppCompatActivity {
    ListView list_ditunda;
    Button batal, lanjutkan;
    List<data_pelanggan_belum_finish_pojo> dataPelangganBelumFinishPojos = new ArrayList<>();
    ListViewAdapterDaftarBelumFinishPelanggan adapter;
    SharedPreferences sharedPreferences;
    ArrayList<String> Reason_Selesai_Perjalanan = new ArrayList<>();
    ArrayList<String> szDocDos = new ArrayList<>();

    String szDocId;
    SweetAlertDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HttpsTrustManager.allowAllSSL();
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);

        setContentView(R.layout.activity_selesai_perjalanan);
        list_ditunda = findViewById(R.id.list_ditunda);
        batal = findViewById(R.id.batal);
        lanjutkan = findViewById(R.id.lanjutkan);

        StringRequest rest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Selesai_Perjalanan/index_Reason_Tunda_Pelanggan",
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
                                    Reason_Selesai_Perjalanan.add(id + "-" + jenis_istirahat);

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
        rest.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestkota = Volley.newRequestQueue(selesai_perjalanan.this);
        requestkota.add(rest);

        batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        lanjutkan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int y = 0; y < dataPelangganBelumFinishPojos.size(); y++) {
                    if(adapter.getItem(y).getReason() == null){
                        new SweetAlertDialog(selesai_perjalanan.this, SweetAlertDialog.WARNING_TYPE)
                                .setContentText("Data Belum Lengkap")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                    }
                                })
                                .show();
                        break;
                    } else if (y == dataPelangganBelumFinishPojos.size() -1 ){
                        pDialog = new SweetAlertDialog(selesai_perjalanan.this, SweetAlertDialog.PROGRESS_TYPE);
                        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                        pDialog.setTitleText("Harap Menunggu");
                        pDialog.setCancelable(false);
                        pDialog.show();
                        StringRequest stringRequest2 = new StringRequest(Request.Method.PUT, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Selesai_Perjalanan/index_FinishDoccall",
                                new Response.Listener<String>() {

                                    @Override
                                    public void onResponse(String response) {
                                        putSFADoccall();
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        putSFADoccall();
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

                                params.put("szDocId", akhirikegiatan.STD);
                                params.put("dtmFinish", currentDateandTime2);
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
                        RequestQueue requestQueue2 = Volley.newRequestQueue(selesai_perjalanan.this);
                        requestQueue2.add(stringRequest2);
                        break;
                    }
                }
            }
        });

        getSuratTugas();


    }

    private void putSFADoccall() {
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String nik_baru = sharedPreferences.getString("szDocCall", null);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Persiapan/index_SuratTugas?nik_baru="+ nik_baru,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");
                            for (int i = 0; i < movieArray.length(); i++) {
                                final JSONObject movieObject = movieArray.getJSONObject(i);

                                putSFADoccalls(akhirikegiatan.STD);


                            }


                        } catch(JSONException e){
                            e.printStackTrace();

                        }
                    }


                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        putSFADoccalls(akhirikegiatan.string_ritase);

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
                        60000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                ));
        RequestQueue requestQueue = Volley.newRequestQueue(selesai_perjalanan.this);
        requestQueue.getCache().clear();
        stringRequest.setShouldCache(false);
        requestQueue.add(stringRequest);
    }

    private void putSFADoccalls(String szDocId) {
            StringRequest stringRequest2 = new StringRequest(Request.Method.PUT, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Selesai_Perjalanan/index_SFADoccall",
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            pDialog.dismissWithAnimation();
                            new SweetAlertDialog(selesai_perjalanan.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setContentText("Data Sudah Disimpan")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.dismissWithAnimation();
                                            Intent i = new Intent(getBaseContext(), MainActivity.class);
                                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(i);

                                        }
                                    })
                                    .show();


                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            pDialog.dismissWithAnimation();
                            new SweetAlertDialog(selesai_perjalanan.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setContentText("Data Sudah Disimpan")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.dismissWithAnimation();
                                            Intent i = new Intent(getBaseContext(), MainActivity.class);
                                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(i);

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

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();

                    sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                    String nik_baru = sharedPreferences.getString("szDocCall", null);
                    String[] parts = nik_baru.split("-");
                    String restnomor = parts[0];

                    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String currentDateandTime2 = sdf2.format(new Date());

                    params.put("szDocId", akhirikegiatan.STD);
                    params.put("szBranchId", MainActivity.kode_dms);


                    params.put("dtmFinish", currentDateandTime2);
                    params.put("bFinished", "1");
                    params.put("decKMFinish", akhirikegiatan.FinishKM);
                    params.put("dtmLastUpdated", currentDateandTime2);


                    return params;
                }

            };
            stringRequest2.setRetryPolicy(
                    new DefaultRetryPolicy(
                            60000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                    )
            );
            RequestQueue requestQueue2 = Volley.newRequestQueue(selesai_perjalanan.this);
            requestQueue2.getCache().clear();
            stringRequest2.setShouldCache(false);
            requestQueue2.add(stringRequest2);
    }


    private void getSuratTugas() {
        pDialog = new SweetAlertDialog(selesai_perjalanan.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Harap Menunggu");
        pDialog.setCancelable(false);
        pDialog.show();
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String nik_baru = sharedPreferences.getString("szDocCall", null);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Persiapan/index_SuratTugas?nik_baru="+ nik_baru,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");
                            for (int i = 0; i < movieArray.length(); i++) {
                                final JSONObject movieObject = movieArray.getJSONObject(i);

                                getDataBelumFinish(akhirikegiatan.STD);
                                szDocId = akhirikegiatan.STD;

                            }


                        } catch(JSONException e){
                            e.printStackTrace();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        getDataBelumFinish(akhirikegiatan.STD);
                        szDocId = akhirikegiatan.STD;
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
        RequestQueue requestQueue = Volley.newRequestQueue(selesai_perjalanan.this);
        requestQueue.add(stringRequest);
    }

    private void getDataBelumFinish(String szDocId) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Selesai_Perjalanan/index_List_Tunda_Pelanggan?surat_tugas=" + szDocId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            int number = 0;
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");
                            pDialog.dismissWithAnimation();
                            for (int i = 0; i < movieArray.length(); i++) {
                                final JSONObject movieObject = movieArray.getJSONObject(i);
                                final data_pelanggan_belum_finish_pojo movieItem = new data_pelanggan_belum_finish_pojo(
                                        movieObject.getString("szCustomerId"),
                                        movieObject.getString("szName"),
                                        movieObject.getString("szAddress"),
                                        movieObject.getString("szLatitude"),
                                        movieObject.getString("szLongitude"),
                                        movieObject.getString("bStarted"),
                                        movieObject.getString("bsuccess"),
                                        movieObject.getString("bScanBarcode"),
                                        movieObject.getString("bPostPone"),
                                        movieObject.getString("szFailReason"),
                                        movieObject.getString("szDocDO"));

                                dataPelangganBelumFinishPojos.add(movieItem);
                                szDocDos.add(movieObject.getString("szDocDO"));



                                adapter = new ListViewAdapterDaftarBelumFinishPelanggan(dataPelangganBelumFinishPojos, getApplicationContext());
                                list_ditunda.setAdapter(adapter);

                                if(!movieObject.getString("bsuccess").equals("1") && movieObject.getString("szDOcSo").equals("")){
                                    DraftToVoid(movieObject.getString("szDocDO"));
                                }

                                if(movieObject.getString("bsuccess").equals("1")){
                                    dataPelangganBelumFinishPojos.remove(movieItem);
                                    adapter.notifyDataSetChanged();
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

    public class ListViewAdapterDaftarBelumFinishPelanggan extends ArrayAdapter<data_pelanggan_belum_finish_pojo> {

        private class ViewHolder {
            TextView namatoko, teksdo;
            AutoCompleteTextView editpilihalasan;
        }

        List<data_pelanggan_belum_finish_pojo> data_pelanggan_belum_finish_pojos;
        private Context context;

        public ListViewAdapterDaftarBelumFinishPelanggan(List<data_pelanggan_belum_finish_pojo> data_pelanggan_belum_finish_pojos, Context context) {
            super(context, R.layout.list_daftarbelumselesai, data_pelanggan_belum_finish_pojos);
            this.data_pelanggan_belum_finish_pojos = data_pelanggan_belum_finish_pojos;
            this.context = context;

        }

        public int getCount() {
            return data_pelanggan_belum_finish_pojos.size();
        }

        public data_pelanggan_belum_finish_pojo getItem(int position) {
            return data_pelanggan_belum_finish_pojos.get(position);
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
            data_pelanggan_belum_finish_pojo movieItem = data_pelanggan_belum_finish_pojos.get(position);
            if (convertView == null) {
                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.list_daftarbelumselesai, parent, false);

                viewHolder.namatoko = (TextView) convertView.findViewById(R.id.namatoko);
                viewHolder.teksdo = (TextView) convertView.findViewById(R.id.teksdo);
                viewHolder.editpilihalasan = (AutoCompleteTextView) convertView.findViewById(R.id.editpilihalasan);

                convertView.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            int nomor_row = position;

            viewHolder.namatoko.setText(movieItem.getSzName());

            viewHolder.teksdo.setText("NO DO = " + movieItem.getSzDocDO());

            viewHolder.editpilihalasan.setAdapter(new ArrayAdapter<String>(selesai_perjalanan.this, android.R.layout.simple_expandable_list_item_1, Reason_Selesai_Perjalanan));

            viewHolder.editpilihalasan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String rest = viewHolder.editpilihalasan.getText().toString();
                    String[] parts = rest.split("-");

                    String deskripsi = parts[1];
                    String docdo = movieItem.getSzDocDO();

                    putSzDescription(deskripsi, docdo);



                    StringRequest stringRequest2 = new StringRequest(Request.Method.PUT, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Selesai_Perjalanan/index_FailReason",
                            new Response.Listener<String>() {

                                @Override
                                public void onResponse(String response) {
                                    viewHolder.editpilihalasan.setError(null);

                                    sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                                    String nik_baru = sharedPreferences.getString("szDocCall", null);

                                    if(nik_baru.contains("RP")){

                                    } else if(nik_baru.contains("SPV")){

                                    } else {
                                        postDoPending(docdo);
                                    }

                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    viewHolder.editpilihalasan.setError("Data Belum Disimpan");
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

                            String rest = viewHolder.editpilihalasan.getText().toString();
                            String[] parts = rest.split("-");
                            String restnomor = parts[0];
                            String restnomorbaru = restnomor.replace(" ", "");

                            String kode = movieItem.getSzCustomerId();

                            params.put("szDocId", szDocId);
                            params.put("szCustomerId", kode);
                            params.put("szDocDO", movieItem.getSzDocDO());

                            params.put("szFailReason", restnomorbaru);



                            return params;
                        }

                    };
                    stringRequest2.setRetryPolicy(
                            new DefaultRetryPolicy(
                                    60000,
                                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                            )
                    );
                    RequestQueue requestQueue2 = Volley.newRequestQueue(selesai_perjalanan.this);
                    requestQueue2.getCache().clear();
                    stringRequest2.setShouldCache(false);
                    requestQueue2.add(stringRequest2);
                }

                private void putSzDescription(String deskripsi, String docdo) {
                    StringRequest stringRequest2 = new StringRequest(Request.Method.PUT, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_DocDoDescription",
                            new Response.Listener<String>() {

                                @Override
                                public void onResponse(String response) {
                                    viewHolder.editpilihalasan.setError(null);
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    viewHolder.editpilihalasan.setError("Data Belum Disimpan");
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



                            params.put("szDocId", docdo);
                            params.put("szDescription", deskripsi);



                            return params;
                        }

                    };
                    stringRequest2.setRetryPolicy(
                            new DefaultRetryPolicy(
                                    60000,
                                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                            )
                    );
                    RequestQueue requestQueue2 = Volley.newRequestQueue(selesai_perjalanan.this);
                    requestQueue2.getCache().clear();
                    stringRequest2.setShouldCache(false);
                    requestQueue2.add(stringRequest2);
                }
            });


            viewHolder.editpilihalasan.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    getItem(position).setReason(s.toString());
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    getItem(position).setReason(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {
                    getItem(position).setReason(s.toString());
                }
            });




            return convertView;
        }
    }

    private void postDoPending(String docdo) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_DO_Pending?szDocId=" + docdo, //szDocId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

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
        RequestQueue requestQueue = Volley.newRequestQueue(selesai_perjalanan.this);
        requestQueue.add(stringRequest);
    }


    private void DraftToVoid(String szDocDo) {
        StringRequest stringRequest2 = new StringRequest(Request.Method.PUT, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_DraftToVoid",
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
                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String currentDateandTime2 = sdf2.format(new Date());
                sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                String nik_baru = sharedPreferences.getString("szDocCall", null);

                params.put("szDocId", szDocDo);
                params.put("szDocStatus", "Draft");
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
        RequestQueue requestQueue2 = Volley.newRequestQueue(selesai_perjalanan.this);
        requestQueue2.add(stringRequest2);
    }

}