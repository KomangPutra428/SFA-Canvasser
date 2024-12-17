package com.tvip.canvasser.menu_selesai_perjalanan;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
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
import android.widget.EditText;
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
import com.google.android.material.card.MaterialCardView;
import com.tvip.canvasser.Perangkat.HttpsTrustManager;
import com.tvip.canvasser.R;
import com.tvip.canvasser.menu_utama.MainActivity;
import com.tvip.canvasser.pojo.data_ritase_pojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class akhirikegiatan extends AppCompatActivity {

    SweetAlertDialog pDialog;
    ListView list_akhiri_kegiatan;
    List<data_ritase_pojo> data_akhirikegiatan_pojos = new ArrayList<>();
    akhirikegiatan.ListViewAdapterAkhiriKegiatan adapter;
    SharedPreferences sharedPreferences;

    Dialog dialog, dialogstatus, dialog_km;
    TextView tv_total_semua_harga;
    TextView tv_nomor_bkb;
    TextView tv_nama_driver;
    static TextView tv_ritase;
    TextView tv_total_pembayaran_format_rupiah;
    String ritase, ritase2;

    ArrayList<String> pilih_ritase = new ArrayList<>();
    ArrayList<String> ritase_list = new ArrayList<>();

    Button bt_akhir_kegiatan, ya, tidak;

    static String string_ritase;
    static String FinishKM;
    String string_std;
    EditText editkm;

    EditText editkmlast;

    TextView tv_driver_akhir_kegiatan;
    TextView tv_nomor_bkb_akhir_kegiatan;
    static TextView tv_ritase_akhir_kegiatan;
    TextView textKeterangan;
    String selection;
    static String STD;
    int numbering;
    AutoCompleteTextView act_pilih_ritase;

    static String jenis_ritase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_akhirikegiatan);
        HttpsTrustManager.allowAllSSL();

        list_akhiri_kegiatan = findViewById(R.id.list_akhiri_kegiatan);
        tv_total_semua_harga = findViewById(R.id.tv_total_semua_harga);
        tv_total_pembayaran_format_rupiah = findViewById(R.id.tv_total_semua_harga_format_rupiah);

        tv_driver_akhir_kegiatan = findViewById(R.id.tv_driver_akhir_kegiatan);
        tv_nomor_bkb_akhir_kegiatan = findViewById(R.id.tv_nomor_bkb_akhir_kegiatan);
        tv_ritase_akhir_kegiatan = findViewById(R.id.tv_ritase_akhir_kegiatan);

        tv_nomor_bkb = findViewById(R.id.tv_nomor_bkb_akhir_kegiatan);
        tv_nama_driver = findViewById(R.id.tv_driver_akhir_kegiatan);
        tv_ritase = findViewById(R.id.tv_ritase_akhir_kegiatan);

        bt_akhir_kegiatan = findViewById(R.id.bt_akhiri_kegiatan);
        textKeterangan = findViewById(R.id.textKeterangan);

        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String employee_id = sharedPreferences.getString("szDocCall", null);
        Toast.makeText(akhirikegiatan.this, "EmployeeId = " + employee_id, Toast.LENGTH_SHORT).show();

        dialog = new Dialog(akhirikegiatan.this);
        dialog.setContentView(R.layout.pilih_ritase);
        dialog.setCancelable(false);

        dialog_km = new Dialog(akhirikegiatan.this);
        dialog_km.setContentView(R.layout.topup_kmends);
        editkm = dialog_km.findViewById(R.id.editkm);
        editkmlast = dialog_km.findViewById(R.id.editkmlast);
        tidak = dialog_km.findViewById(R.id.tidak);
        ya = dialog_km.findViewById(R.id.ya);

        editkm.addTextChangedListener(new TextWatcher() {
            private boolean isUpdating = false;  // Prevent recursive updates

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No need to handle this directly
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // No need to handle this directly
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isUpdating) {
                    return;
                }

                isUpdating = true;

                String currentText = s.toString();

                // Remove leading zeros for processing
                String newText = currentText.replaceFirst("^0+(?!$)", "");

                // If length is greater than 6, truncate to 6 characters
                if (newText.length() > 6) {
                    newText = newText.substring(0, 6);
                }

                // Pad with leading zeros until the length is 6
                StringBuilder formattedString = new StringBuilder(newText);
                while (formattedString.length() < 6) {
                    formattedString.insert(0, "0");
                }

                // Update the text only if it's different
                if (!formattedString.toString().equals(currentText)) {
                    // Temporarily remove listener to prevent recursion
                    editkm.removeTextChangedListener(this);

                    // Set the new formatted text
                    editkm.setText(formattedString.toString());

                    // Set the cursor to the last position (end of the text)
                    editkm.setSelection(formattedString.length());

                    // Re-add the TextWatcher
                    editkm.addTextChangedListener(this);
                }

                isUpdating = false;
            }
        });

        if(getIntent().getStringExtra("linkstd").contains("https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_SuratTugasNonRuteV2?szDocId=")){
            jenis_ritase = "Non Rute";
        } else {
            jenis_ritase = "Rute";
        }

        if(getIntent().getStringExtra("Status").equals("Ritase")){
            dialog.show();
        } else {
            bt_akhir_kegiatan.setText("AKHIRI KEGIATAN");
            textKeterangan.setText("STD");
            sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
            String szEmployeeId = sharedPreferences.getString("szDocCall", null);
            System.out.println("Link = " + getIntent().getStringExtra("linkstd"));
            StringRequest rest = new StringRequest(Request.Method.GET, getIntent().getStringExtra("linkstd"),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getString("status").equals("true")) {
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                        string_ritase = jsonObject1.getString("szDocId");
                                        STD = string_ritase;
                                        getBKB("akhirikegiatan");

                                        getLastKM(STD);


                                        getJumlah("akhirikegiatan");
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
            RequestQueue requestkota = Volley.newRequestQueue(akhirikegiatan.this);
            requestkota.add(rest);


        }

        //DIALOG UNUTK MENAMPILKAN KILOMETER



        act_pilih_ritase = dialog.findViewById(R.id.act_pilih_ritase);
        Button batal = dialog.findViewById(R.id.tidak);
        Button ok = dialog.findViewById(R.id.ya);



        //UNTUK MENDAPATKAN RITASE, DIAMBIL BERDASARKAN employee_id
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        //szEmployeeId = 336-RP049
        String szEmployeeId = sharedPreferences.getString("szDocCall", null);
        StringRequest rest = new StringRequest(Request.Method.GET, getIntent().getStringExtra("linkstd"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("status").equals("true")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    string_ritase = jsonObject1.getString("szDocId");


                                    getSPinner();
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    private void getSPinner() {
                        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                        String szEmployeeId = sharedPreferences.getString("szDocCall", null);
                        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_BKB?id_std=" + string_ritase, //szDocId,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        try {
                                            int number = 0;
                                            JSONObject obj = new JSONObject(response);
                                            if (obj.getString("status").equals("true")) {
                                                final JSONArray movieArray = obj.getJSONArray("data");
                                                for (int i = 0; i < movieArray.length(); i++) {
                                                    final JSONObject movieObject = movieArray.getJSONObject(i);
                                                    String doneornot;
                                                    if(movieObject.getString("id_driver").equals(szEmployeeId)){
                                                        if (movieObject.getString("statusActivity").equals("1")){
                                                            doneornot = "(DONE)";
                                                        } else {
                                                            doneornot = "";
                                                        }

                                                        if (!pilih_ritase.contains(movieObject.getString("ritase") + doneornot)){
                                                            pilih_ritase.add(movieObject.getString("ritase") + doneornot);
                                                        }
                                                    }



                                                }



                                            }
                                            act_pilih_ritase.setAdapter(new ArrayAdapter<String>(akhirikegiatan.this, android.R.layout.simple_expandable_list_item_1, pilih_ritase));




//                            pDialog.dismissWithAnimation();

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
                        RequestQueue requestQueue = Volley.newRequestQueue(akhirikegiatan.this);
                        requestQueue.add(stringRequest);

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
        RequestQueue requestkota = Volley.newRequestQueue(akhirikegiatan.this);
        requestkota.add(rest);



        batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                finish();
            }
        });

        act_pilih_ritase.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
                selection = act_pilih_ritase.getText().toString();
                ritase2 = act_pilih_ritase.getText().toString();




                //TODO Do something with the selected text
            }
        });

        //SETELAH MENDAPATKAN RITASE,KEMUDIAN KIRIM PARAMETER RITASE, BERSAMA DENGAN EMPLOYEE_ID
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(act_pilih_ritase.getText().toString().contains("DONE")){
                    new SweetAlertDialog(akhirikegiatan.this, SweetAlertDialog.WARNING_TYPE)
                            .setContentText("Ritase Sudah Selesai Dijalankan")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                } else {
                    ok.setText("Loading...");

                    if (ok.getText().toString().equals("Loading...")){
                        ok.setEnabled(false);
                    }

                    System.out.println("Ritase " + selection);
                    ritase = act_pilih_ritase.getText().toString();
                    sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                    String szEmployeeId = sharedPreferences.getString("szDocCall", null);
                    StringRequest rest = new StringRequest(Request.Method.GET, getIntent().getStringExtra("linkstd"),
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        if (jsonObject.getString("status").equals("true")) {
                                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                                string_ritase = jsonObject1.getString("szDocId");
                                                STD = string_ritase;
                                                getBKB("akhiriritase");

                                                getJumlah("akhiriritase");
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
                    RequestQueue requestkota = Volley.newRequestQueue(akhirikegiatan.this);
                    requestkota.add(rest);

                }



            }
        });

        bt_akhir_kegiatan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialogstatus = new Dialog(akhirikegiatan.this);
                dialogstatus.setContentView(R.layout.dialog_submit_akhir_kegiatan);
                dialogstatus.setCancelable(false);

                Button bt_submit = dialogstatus.findViewById(R.id.bt_submit_akhir_kegiatan);
                Button bt_batal = dialogstatus.findViewById(R.id.bt_batal_akhir_kegiatan);

                bt_batal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialogstatus.dismiss();
                    }
                });

                bt_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(getIntent().getStringExtra("Status").equals("Ritase")){
                            pDialog = new SweetAlertDialog(akhirikegiatan.this, SweetAlertDialog.PROGRESS_TYPE);
                            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                            pDialog.setTitleText("Harap Menunggu");
                            pDialog.setCancelable(false);
                            pDialog.show();
                            getTotalHarga();
                        } else {
                            ya.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if(editkmlast.getText().toString().length()==0){

                                        new SweetAlertDialog(akhirikegiatan.this, SweetAlertDialog.WARNING_TYPE)
                                                .setTitleText("Harap isi KM")
                                                .setConfirmText("OK")
                                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(SweetAlertDialog sDialog) {
                                                        sDialog.dismissWithAnimation();
                                                    }
                                                })
                                                .show();
                                    } else if(Integer.parseInt(editkm.getText().toString()) > Integer.parseInt(editkmlast.getText().toString())){

                                        new SweetAlertDialog(akhirikegiatan.this, SweetAlertDialog.WARNING_TYPE)
                                                .setTitleText("KM Akhir Tidak Boleh lebih kecil dari KM Awal")
                                                .setConfirmText("OK")
                                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(SweetAlertDialog sDialog) {
                                                        sDialog.dismissWithAnimation();
                                                    }
                                                })
                                                .show();
                                    } else {

                                        ya.setText("Loading...");

                                        if (ya.getText().toString().equals("Loading...")){
                                            ya.setEnabled(false);
                                        }

                                        putSFADoccalls();

                                    }

                                }
                            });

                            tidak.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if(editkmlast.getText().toString().length()==0){
                                        dialog_km.dismiss();
                                    }

                                }

                            });

                            dialog_km.show();

                        }
                        dialogstatus.dismiss();

//                        bt_submit.setText("Loading...");
//
//                        if (bt_submit.getText().toString().equals("Loading...")){
//                            bt_submit.setEnabled(false);
//                        }

                    }
                });

                dialogstatus.show();

            }
        });
    }

    private void getLastKM(String std) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_last_km?nomor_std=" + std, //mulai_perjalanan.id_pelanggan,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            int number = 0;
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");

                            for (int i = 0; i < movieArray.length(); i++) {

                                final JSONObject movieObject = movieArray.getJSONObject(i);



                                if(movieObject.getString("szRouteType").equals("CAN-NON-RUTE")){
                                    editkm.setText(movieObject.getString("decKMStart"));
                                } else {
                                    editkm.setText(movieObject.getString("km_lhp"));
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
                        50000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                ));

        RequestQueue requestQueue = Volley.newRequestQueue(akhirikegiatan.this);
        requestQueue.add(stringRequest);
    }

    private void getJumlah(String condition) {
        if(condition.equals("akhiriritase")){
            tv_ritase.setText(selection);
        } else {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_BKB?id_std=" + STD, //szDocId,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                int number = 0;
                                JSONObject obj = new JSONObject(response);
                                if (obj.getString("status").equals("true")) {
                                    final JSONArray movieArray = obj.getJSONArray("data");
                                    final JSONObject movieObject = movieArray.getJSONObject(movieArray.length() -1);

                                    selection = String.valueOf(movieArray.length());
                                    tv_ritase.setText(selection);

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
    }

    private void getBKB(String condition) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_BKB?id_std=" + STD, //szDocId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ListDatapelangganDalamRute();
                        try {
                            int number = 0;
                            JSONObject obj = new JSONObject(response);
                            if (obj.getString("status").equals("true")) {
                                final JSONArray movieArray = obj.getJSONArray("data");
                                if(condition.equals("akhiriritase")){
                                    number = numbering;
                                } else {
                                    number = movieArray.length() -1;
                                }
                                final JSONObject movieObject = movieArray.getJSONObject(number);


                                tv_nomor_bkb_akhir_kegiatan.setText(movieObject.getString("id_bkb"));

                                tv_driver_akhir_kegiatan.setText(movieObject.getString("id_driver") + "-" + movieObject.getString("nama_driver"));

                                tv_ritase.setText(ritase2);

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

    private void ListDatapelangganDalamRute() {

        adapter = new akhirikegiatan.ListViewAdapterAkhiriKegiatan(data_akhirikegiatan_pojos, getApplicationContext());
        list_akhiri_kegiatan.setAdapter(adapter);

        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String employee_id = sharedPreferences.getString("szDocCall", null);
        Toast.makeText(akhirikegiatan.this, "EmployeeId = " + employee_id, Toast.LENGTH_SHORT).show();
        System.out.println("https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_Ritase?szDocId="+STD);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_Ritase?szDocId="+STD, //mulai_perjalanan.id_pelanggan,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        dialog.dismiss();

                        try {
                            int number = 0;
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");

                            for (int i = 0; i < movieArray.length(); i++) {
                                final JSONObject movieObject = movieArray.getJSONObject(i);
                                String[] harga = movieObject.getString("total_harga").split("\\.");
                                String splitharga = harga[0];

                                final data_ritase_pojo movieItem = new data_ritase_pojo(
                                        movieObject.getString("szCustomerId"),
                                        movieObject.getString("szName"),
                                        movieObject.getString("szAddress"),
                                        splitharga,
                                        movieObject.getString("bOutOfRoute"),
                                        movieObject.getString("ritase"));


                                data_akhirikegiatan_pojos.add(movieItem);

                                if(getIntent().getStringExtra("Status").equals("Ritase")){
                                    if(!movieObject.getString("ritase").equals(tv_ritase.getText().toString())){
                                        data_akhirikegiatan_pojos.remove(movieItem);
                                        adapter.notifyDataSetChanged();
                                    }
                                } else {
                                    tv_ritase_akhir_kegiatan.setText(STD);
                                }




                                adapter.notifyDataSetChanged();

                            }

                            int total = 0;
                            for(int i = 0; i < data_akhirikegiatan_pojos.size(); i++){

                                String totalharga = adapter.getItem(i).getTotal_harga();
                                total+=Integer.parseInt(totalharga);
                                tv_total_semua_harga.setText(String.valueOf(total));

                                String formatrupiah = formatRupiah(Double.parseDouble(String.valueOf(total)));
                                tv_total_pembayaran_format_rupiah.setText(formatrupiah);

                            }

                        } catch(JSONException e){
                            e.printStackTrace();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
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

    public class ListViewAdapterAkhiriKegiatan extends ArrayAdapter<data_ritase_pojo> {
        private List<data_ritase_pojo> data_akhirikegiatan_pojos;

        private final Context context;

        public ListViewAdapterAkhiriKegiatan(List<data_ritase_pojo> data_akhirikegiatan_pojos, Context context) {
            super(context, R.layout.list_akhiri_kegiatan, data_akhirikegiatan_pojos);
            this.data_akhirikegiatan_pojos = data_akhirikegiatan_pojos;
            this.context = context;
        }

        @SuppressLint("NewApi")
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = LayoutInflater.from(context);

            @SuppressLint({"ViewHolder", "InflateParams"})
            View listViewItem = inflater.inflate(R.layout.list_akhiri_kegiatan, null, true);

            TextView id_customer = listViewItem.findViewById(R.id.tv_akhiri_kegiatan_idCustomer);
            TextView total_harga = listViewItem.findViewById(R.id.tv_akhiri_kegiatan_total_harga);
            TextView nama_toko = listViewItem.findViewById(R.id.tv_namatoko);
            TextView alamat_toko = listViewItem.findViewById(R.id.tv_alamattoko);
            TextView total_harga_format_rupiah = listViewItem.findViewById(R.id.tv_akhiri_kegiatan_total_harga_format_rupiah);

            MaterialCardView warna = listViewItem.findViewById(R.id.warna);
            TextView status = listViewItem.findViewById(R.id.status);

            data_ritase_pojo data = getItem(position);

            DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
            DecimalFormatSymbols formatRp = new DecimalFormatSymbols();
            formatRp.setCurrencySymbol("Rp. ");
            formatRp.setMonetaryDecimalSeparator(',');
            formatRp.setGroupingSeparator('.');
            kursIndonesia.setDecimalFormatSymbols(formatRp);

            id_customer.setText(data.getSzCustomerId());
            total_harga_format_rupiah.setText(kursIndonesia.format(Integer.parseInt(data.getTotal_harga())));
            nama_toko.setText(data.getSzName());
            alamat_toko.setText(data.getSzAddress());

            if(data.getbOutOfRoute().equals("0")){
                status.setText("Dalam Rute");
                warna.setCardBackgroundColor(Color.parseColor("#1EB547"));
            } else {
                status.setText("Luar Rute");
                warna.setCardBackgroundColor(Color.parseColor("#A2C21D"));
            }

            return listViewItem;
        }
    }

    //BERGUNA UNUTK UPDATE DATA KE DOCCALL, DISINI AKAN UPDATE bFinihsed MEnjadi 1, dan isi KM terakhir
    //karena disini unutk mengakhiri ritase

    private void getTotalHarga() {
        if(getIntent().getStringExtra("Status").equals("Ritase")){
            StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_TotalHargaPerBKB?nomor_std="+STD+"&ritase=" + tv_ritase.getText().toString(),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                int number = 0;
                                JSONObject obj = new JSONObject(response);
                                final JSONArray movieArray = obj.getJSONArray("data");
                                for (int i = 0; i < movieArray.length(); i++) {
                                    final JSONObject movieObject = movieArray.getJSONObject(i);

                                    String bkb = movieObject.getString("nomor_bkb");
                                    String hitungan = movieObject.getString("hitungan");

                                    post_historydriverbkb(bkb, hitungan, i, movieArray.length());




                                }
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
        } else {
            final SweetAlertDialog Success = new SweetAlertDialog(akhirikegiatan.this, SweetAlertDialog.SUCCESS_TYPE);
            Success.setTitleText("Berhasil!\nAnda berhasil menyelesaikan ritase");
            Success.setConfirmText("OK");
            Success.setCancelable(false);
            Success.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    if(getIntent().getStringExtra("Status").equals("Ritase")){
                        Intent i = new Intent(akhirikegiatan.this, Summary_Transaksi_Lhp.class);
                        startActivity(i);
//                        finish();
                        Success.dismissWithAnimation();
                    } else {
                        cekBelumSelesai();
                        Success.dismissWithAnimation();
                    }

                }
            });

            Success.show();
        }

    }

    private void post_historydriverbkb(String bkb, String hitungan, int i, int length) {

        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_mdba_history_driver_bkb",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        if(i == length-1){
                            pDialog.dismissWithAnimation();
                            dialogstatus.dismiss();

                            final SweetAlertDialog Success = new SweetAlertDialog(akhirikegiatan.this, SweetAlertDialog.SUCCESS_TYPE);
                            Success.setTitleText("Berhasil!\nAnda berhasil menyelesaikan ritase");
                            Success.setConfirmText("OK");
                            Success.setCancelable(false);
                            Success.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    if(getIntent().getStringExtra("Status").equals("Ritase")){
                                        Intent i = new Intent(akhirikegiatan.this, Summary_Transaksi_Lhp.class);
                                        startActivity(i);
//                                        finish();
                                        Success.dismissWithAnimation();
                                    } else {
                                        cekBelumSelesai();
                                        Success.dismissWithAnimation();
                                    }

                                }
                            });

                            Success.show();
                        }


                    }

                    private void cekBelumSelesai() {
                            pDialog = new SweetAlertDialog(akhirikegiatan.this, SweetAlertDialog.PROGRESS_TYPE);
                            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                            pDialog.setTitleText("Harap Menunggu");
                            pDialog.setCancelable(false);
                            pDialog.show();
                            StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Selesai_Perjalanan/index_List_Tunda_Pelanggan?surat_tugas=" + STD,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            pDialog.dismissWithAnimation();
                                            Intent intent = new Intent (getApplicationContext(), selesai_perjalanan.class);
                                            startActivity(intent);

                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            pDialog.dismissWithAnimation();
                                            finish();
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
                            RequestQueue requestQueue = Volley.newRequestQueue(akhirikegiatan.this);
                            requestQueue.add(stringRequest);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(i == length-1) {
                    dialogstatus.dismiss();
                    pDialog.dismissWithAnimation();
                    final SweetAlertDialog Success = new SweetAlertDialog(akhirikegiatan.this, SweetAlertDialog.SUCCESS_TYPE);
                    Success.setTitleText("Berhasil!\nAnda berhasil menyelesaikan perjalanan");
                    Success.setConfirmText("OK");
                    Success.setCancelable(false);
                    Success.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            if(getIntent().getStringExtra("Status").equals("Ritase")){
                                finish();
                                Success.dismissWithAnimation();
                            } else {
                                cekBelumSelesai();
                                Success.dismissWithAnimation();
                            }

                        }
                    });
                    Success.show();
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

                sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                String nik_baru = sharedPreferences.getString("szDocCall", null);
                String[] nikbaru = nik_baru.split("-");
                String nikbaru_split = nikbaru[0];

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                String currentDateandTime = sdf.format(new Date());

                params.put("dtmDoc", currentDateandTime);
                params.put("noBkb", bkb);
                params.put("driverId", nik_baru);

                params.put("routeType", "CAN");

                params.put("ritase", tv_ritase.getText().toString());
                params.put("branchId", nikbaru_split);
                params.put("amount", hitungan);
                params.put("userCreated", nik_baru);
                params.put("userUpdated", nik_baru);
                params.put("statusActivity", "1");

                System.out.println("Params post = " + params);

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
        RequestQueue requestQueue2 = Volley.newRequestQueue(akhirikegiatan.this);
        requestQueue2.add(stringRequest2);
    }

    private void cekBelumSelesai() {
        pDialog = new SweetAlertDialog(akhirikegiatan.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Harap Menunggu");
        pDialog.setCancelable(false);
        pDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Selesai_Perjalanan/index_List_Tunda_Pelanggan?surat_tugas=" + STD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.dismissWithAnimation();
                        Intent intent = new Intent (getApplicationContext(), selesai_perjalanan.class);
                        startActivity(intent);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismissWithAnimation();
                        finish();
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
        RequestQueue requestQueue = Volley.newRequestQueue(akhirikegiatan.this);
        requestQueue.add(stringRequest);
    }

    private String formatRupiah(Double number){
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        return formatRupiah.format(number);
    }



    private void putSFADoccalls() {
        pDialog = new SweetAlertDialog(akhirikegiatan.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Harap Menunggu");
        pDialog.setCancelable(false);
        pDialog.show();
        StringRequest stringRequest2 = new StringRequest(Request.Method.PUT, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Selesai_Perjalanan/index_dmsDocCall_driver",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        dialog_km.dismiss();

                        getTotalHarga();

                        FinishKM = editkmlast.getText().toString();

                        putSfa_Doccall();


                    }

                    private void putSfa_Doccall() {
                        StringRequest stringRequest2 = new StringRequest(Request.Method.PUT, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Selesai_Perjalanan/index_SFADoccall",
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

                                sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                                String nik_baru = sharedPreferences.getString("szDocCall", null);
                                String[] parts = nik_baru.split("-");
                                String restnomor = parts[0];

                                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String currentDateandTime2 = sdf2.format(new Date());


                                params.put("szDocId", STD);
                                params.put("szBranchId", MainActivity.kode_dms);

                                params.put("dtmFinish", currentDateandTime2);
                                params.put("bFinished", "1");
                                params.put("decKMFinish", editkmlast.getText().toString());
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
                        RequestQueue requestQueue2 = Volley.newRequestQueue(akhirikegiatan.this);
                        requestQueue2.getCache().clear();
                        stringRequest2.setShouldCache(false);
                        requestQueue2.add(stringRequest2);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        dialog_km.dismiss();

                        getTotalHarga();

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
                params.put("dtmFinish", currentDateandTime2);
                params.put("bFinished", "1");
                params.put("decKMFinish", editkmlast.getText().toString());
                params.put("dtmLastUpdated", currentDateandTime2);

                System.out.println("HASIL decKMFinish " + editkmlast.getText().toString());
                System.out.println("HASIL szDocId " + string_std);

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

        RequestQueue requestQueue2 = Volley.newRequestQueue(akhirikegiatan.this);
        requestQueue2.add(stringRequest2);
    }

}