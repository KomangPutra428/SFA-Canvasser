package com.tvip.canvasser.menu_persiapan;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.card.MaterialCardView;
import com.tvip.canvasser.Perangkat.HttpsTrustManager;
import com.tvip.canvasser.R;
import com.tvip.canvasser.menu_mulai_perjalanan.Utility;
import com.tvip.canvasser.menu_mulai_perjalanan.mPenjualan_Terima_Produk_Canvas;
import com.tvip.canvasser.pojo.Ritase_pojo;
import com.tvip.canvasser.pojo.data_daftar_kunjungan;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class callplan extends AppCompatActivity {
    TextView tanggal_callplan, surattugas;
    MaterialCardView listpelanggan;
    SharedPreferences sharedPreferences;
    static String szDocId;
    static String idStd;
    EditText editkm;

    ListView listdaftarkunjungan, listRitase;
    List<data_daftar_kunjungan> datadaftarkunjungan = new ArrayList<>();
    List<Ritase_pojo> ritasePojos = new ArrayList<>();

    ListViewAdapterCallPlan adapter;

    ListViewAdapterRitase adapter2;

    MaterialToolbar pengaturanBar;

    ArrayList<String> list_products = new ArrayList<>();

    SweetAlertDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HttpsTrustManager.allowAllSSL();
        setContentView(R.layout.activity_callplan);
        tanggal_callplan = findViewById(R.id.tanggal_callplan);
        surattugas = findViewById(R.id.surattugas);
        listpelanggan = findViewById(R.id.listpelanggan);
        pengaturanBar = findViewById(R.id.persiapanbar);

        listRitase = findViewById(R.id.listRitase);



        //Toast.makeText(callplan.this, "TEST" + szDocId, Toast.LENGTH_SHORT).show();

        listdaftarkunjungan = findViewById(R.id.listdaftarkunjungan);
        //bottom_sheet = findViewById(R.id.bottom_sheet);
        //sheetBehavior = BottomSheetBehavior.from(bottom_sheet);
        //caripelanggan = findViewById(R.id.caripelanggan);
        //tablayout = findViewById(R.id.tablayout);
        //linearmap = findViewById(R.id.linearmap);
        //outlet = findViewById(R.id.outlet);
        //nogeotag = findViewById(R.id.nogeotag);

        listdaftarkunjungan.setAdapter(adapter);

        listRitase.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Dialog dialogstatus = new Dialog(callplan.this);
                dialogstatus.setContentView(R.layout.popup_list_product);

                ListView listprodukperBKB = dialogstatus.findViewById(R.id.listprodukperBKB);

                dialogstatus.show();
                list_products.clear();
                String bkb = ((Ritase_pojo) parent.getItemAtPosition(position)).getId_bkb();
                StringRequest rest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_RitaseListBody?szDocId=" + bkb,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if (jsonObject.getString("status").equals("true")) {
                                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                            String[] asli = jsonObject1.getString("decQty").split("\\.");
                                            String splitasli = asli[0];

                                            String name = jsonObject1.getString("szName");
                                            list_products.add(name + " = " + splitasli + " Qty");

                                        }
                                    }
                                    listprodukperBKB.setAdapter(new ArrayAdapter<String>(callplan.this, android.R.layout.simple_expandable_list_item_1, list_products));

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
                RequestQueue requestkota = Volley.newRequestQueue(callplan.this);
                requestkota.add(rest);
            }
        });

        listpelanggan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SweetAlertDialog pDialog = new SweetAlertDialog(callplan.this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("Harap Menunggu");
                pDialog.show();
                pDialog.setCancelable(false);
                sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                String nik_baru = sharedPreferences.getString("szDocCall", null);
                StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Persiapan/index_SuratTugas?nik_baru=" + nik_baru,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONObject obj = new JSONObject(response);
                                    final JSONArray movieArray = obj.getJSONArray("data");
                                    for (int i = 0; i < 1; i++) {
                                        final JSONObject movieObject = movieArray.getJSONObject(i);
                                        Toast.makeText(callplan.this, movieObject.getString("bStarted"), Toast.LENGTH_SHORT).show();
                                        if(movieObject.getString("bStarted").equals("0")){
                                            pDialog.dismissWithAnimation();
                                            final Dialog dialog = new Dialog(callplan.this);
                                            dialog.setContentView(R.layout.topup_km);

                                            editkm = dialog.findViewById(R.id.editkm);
                                            Button tidak = dialog.findViewById(R.id.tidak);
                                            Button ya = dialog.findViewById(R.id.ya);

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



                                            ya.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    if(editkm.getText().toString().length()==0){
                                                        new SweetAlertDialog(callplan.this, SweetAlertDialog.WARNING_TYPE)
                                                                .setTitleText("Harap isi KM")
                                                                .setConfirmText("OK")
                                                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                    @Override
                                                                    public void onClick(SweetAlertDialog sDialog) {
                                                                        sDialog.dismissWithAnimation();
                                                                    }
                                                                })
                                                                .show();
                                                    } else if(editkm.getText().toString().length()< 6){
                                                        new SweetAlertDialog(callplan.this, SweetAlertDialog.WARNING_TYPE)
                                                                .setTitleText("KM harus 6 Digit")
                                                                .setConfirmText("OK")
                                                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                    @Override
                                                                    public void onClick(SweetAlertDialog sDialog) {
                                                                        sDialog.dismissWithAnimation();
                                                                    }
                                                                })
                                                                .show();
                                                    } else {
                                                        final SweetAlertDialog pDialog2 = new SweetAlertDialog(callplan.this, SweetAlertDialog.PROGRESS_TYPE);
                                                        pDialog2.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                                                        pDialog2.setTitleText("Harap Menunggu");
                                                        pDialog2.setCancelable(false);
                                                        pDialog2.show();

                                                        StringRequest stringRequest2 = new StringRequest(Request.Method.PUT, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Persiapan/index_KMStart",
                                                                new Response.Listener<String>() {

                                                                    @Override
                                                                    public void onResponse(String response) {
                                                                        pDialog2.dismissWithAnimation();
                                                                        new SweetAlertDialog(callplan.this, SweetAlertDialog.SUCCESS_TYPE)
                                                                                .setContentText("Data Sudah Disimpan")
                                                                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                                    @Override
                                                                                    public void onClick(SweetAlertDialog sDialog) {
                                                                                        sDialog.dismissWithAnimation();
                                                                                        Intent daftarkunjungan = new Intent(getBaseContext(), daftar_kunjungan.class);
                                                                                        daftarkunjungan.putExtra("szDocId", szDocId);
                                                                                        startActivity(daftarkunjungan);
                                                                                        postSFADoccall();
                                                                                        dialog.cancel();
                                                                                    }
                                                                                })
                                                                                .show();
                                                                        pDialog2.cancel();

                                                                    }
                                                                }, new Response.ErrorListener() {
                                                            @Override
                                                            public void onErrorResponse(VolleyError error) {
                                                                pDialog2.dismissWithAnimation();
                                                                new SweetAlertDialog(callplan.this, SweetAlertDialog.SUCCESS_TYPE)
                                                                        .setContentText("Data Sudah Disimpan")
                                                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                            @Override
                                                                            public void onClick(SweetAlertDialog sDialog) {
                                                                                sDialog.dismissWithAnimation();
                                                                                Intent daftarkunjungan = new Intent(getBaseContext(), daftar_kunjungan.class);
                                                                                daftarkunjungan.putExtra("szDocId", szDocId);
                                                                                startActivity(daftarkunjungan);
                                                                                postSFADoccall();
                                                                                dialog.cancel();
                                                                            }
                                                                        })
                                                                        .show();
                                                                pDialog2.cancel();

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

                                                                params.put("szDocId", szDocId);
                                                                params.put("decKMStart", editkm.getText().toString());

                                                                params.put("dtmStart", currentDateandTime2);
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
                                                        RequestQueue requestQueue2 = Volley.newRequestQueue(callplan.this);
                                                        requestQueue2.add(stringRequest2);

                                                    }

                                                }
                                            });

                                            tidak.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    if(editkm.getText().toString().length()==0){
                                                        dialog.dismiss();
                                                    }

                                                }
                                            });

                                            dialog.show();

                                        } else {
                                            pDialog.dismissWithAnimation();
                                            Intent daftarkunjungan = new Intent(getBaseContext(), daftar_kunjungan.class);
                                            daftarkunjungan.putExtra("szDocId", szDocId);
                                            startActivity(daftarkunjungan);
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
                RequestQueue requestQueue = Volley.newRequestQueue(callplan.this);
                requestQueue.add(stringRequest);


            }
        });


        //-------------------------------------------------------------------------------------------------

         persiapan_daftarkunjungan();

        listdaftarkunjungan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                idStd = ((data_daftar_kunjungan) parent.getItemAtPosition(position)).getIdStd();

                final SweetAlertDialog pDialog = new SweetAlertDialog(callplan.this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("Harap Menunggu");
                pDialog.show();
                pDialog.setCancelable(false);

                sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                String nik_baru = sharedPreferences.getString("szDocCall", null);

                //UNTUK szDocStatus WAJIB APPLIED KALO NGGA, NGGA BAKAL NGE GET
                StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Persiapan/index_SuratTugasInsertKM?IdStd=" + idStd,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONObject obj = new JSONObject(response);
                                    final JSONArray movieArray = obj.getJSONArray("data");
                                    for (int i = 0; i < 1; i++) {
                                        final JSONObject movieObject = movieArray.getJSONObject(i);

                                        String route_type = movieObject.getString("szRouteType");

                                        //PADA SAAT MOBIL BERANGKAT DAN KM MASIH 0 MAKA DIWAJIBKAN MENGISI KM
                                        if(movieObject.getString("bStarted").equals("0")){
                                            pDialog.dismissWithAnimation();
                                            final Dialog dialog = new Dialog(callplan.this);
                                            dialog.setContentView(R.layout.topup_km);

                                            editkm = dialog.findViewById(R.id.editkm);
                                            Button tidak = dialog.findViewById(R.id.tidak);
                                            Button ya = dialog.findViewById(R.id.ya);
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


                                            ya.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {

                                                    Toast.makeText(callplan.this, "OKE " + idStd, Toast.LENGTH_SHORT).show();
                                                    //PADA SAAT MENEKAN BUTTON
                                                    //AKAN KELUAR ALERT JIKA KM BELUM DIISI
                                                    if(editkm.getText().toString().length()==0){
                                                        new SweetAlertDialog(callplan.this, SweetAlertDialog.WARNING_TYPE)
                                                                .setTitleText("Harap isi KM terlebih dahulu")
                                                                .setConfirmText("OK")
                                                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                    @Override
                                                                    public void onClick(SweetAlertDialog sDialog) {
                                                                        sDialog.dismissWithAnimation();
                                                                    }
                                                                })
                                                                .show();
                                                    } else if(editkm.getText().toString().length()< 6){
                                                        new SweetAlertDialog(callplan.this, SweetAlertDialog.WARNING_TYPE)
                                                                .setTitleText("KM harus 6 Digit")
                                                                .setConfirmText("OK")
                                                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                    @Override
                                                                    public void onClick(SweetAlertDialog sDialog) {
                                                                        sDialog.dismissWithAnimation();
                                                                    }
                                                                })
                                                                .show();
                                                    } else {
                                                        //JIKA SUDAH MENGISI KEMUDIAN KLIK YA, MAKA AKAN UPDATE KE KM START
                                                        final SweetAlertDialog pDialog2 = new SweetAlertDialog(callplan.this, SweetAlertDialog.PROGRESS_TYPE);
                                                        pDialog2.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                                                        pDialog2.setTitleText("Harap Menunggu");
                                                        pDialog2.setCancelable(false);
                                                        pDialog2.show();

                                                        StringRequest stringRequest2 = new StringRequest(Request.Method.PUT, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Persiapan/index_KMStart",
                                                                new Response.Listener<String>() {

                                                                    @Override
                                                                    public void onResponse(String response) {
                                                                        pDialog2.dismissWithAnimation();
                                                                        new SweetAlertDialog(callplan.this, SweetAlertDialog.SUCCESS_TYPE)
                                                                                .setContentText("Data Sudah Disimpan")
                                                                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                                    @Override
                                                                                    public void onClick(SweetAlertDialog sDialog) {

                                                                                        sDialog.dismissWithAnimation();
                                                                                        Intent daftarkunjungan = new Intent(getBaseContext(), daftar_kunjungan.class);
                                                                                        daftarkunjungan.putExtra("szDocId", szDocId);
                                                                                        daftarkunjungan.putExtra("routetype", route_type);
                                                                                        postSFADoccall();

                                                                                        startActivity(daftarkunjungan);
                                                                                        dialog.cancel();
                                                                                    }
                                                                                })
                                                                                .show();
                                                                        pDialog2.cancel();

                                                                    }
                                                                }, new Response.ErrorListener() {
                                                            @Override
                                                            public void onErrorResponse(VolleyError error) {
                                                                pDialog2.dismissWithAnimation();
                                                                new SweetAlertDialog(callplan.this, SweetAlertDialog.SUCCESS_TYPE)
                                                                        .setContentText("Data Sudah Disimpan")
                                                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                            @Override
                                                                            public void onClick(SweetAlertDialog sDialog) {
                                                                                sDialog.dismissWithAnimation();
                                                                                Intent daftarkunjungan = new Intent(getBaseContext(), daftar_kunjungan.class);
                                                                                daftarkunjungan.putExtra("szDocId", szDocId);
                                                                                daftarkunjungan.putExtra("routetype", route_type);
                                                                                postSFADoccall();
                                                                                startActivity(daftarkunjungan);
                                                                                dialog.cancel();
                                                                            }
                                                                        })
                                                                        .show();
                                                                pDialog2.cancel();

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

                                                                params.put("szDocId", idStd); //NOMOR STD. ex. 336-0067615
                                                                params.put("decKMStart", editkm.getText().toString()); //ISI DARI KM YANG DIINPUT
                                                                params.put("dtmStart", currentDateandTime2); //TANGGAL HARI INI
                                                                params.put("szUserUpdatedId", nik_baru); //NOMOR sZemployeeId. ex. 336-RP049
                                                                params.put("dtmLastUpdated", currentDateandTime2); //TANGGAL HARI INI


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
                                                        RequestQueue requestQueue2 = Volley.newRequestQueue(callplan.this);
                                                        requestQueue2.add(stringRequest2);

                                                    }

                                                }
                                            });

                                            tidak.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    if(editkm.getText().toString().length()==0){
                                                        dialog.dismiss();
                                                    } else {
                                                        dialog.dismiss();
                                                    }

                                                }
                                            });

                                            dialog.show();

                                            //JIKA KM NYA SUDAH DIISI MAKA AKAN LNGUSUNG DIALIHKAN KE MENU DAFTAR KUNJUNGAN
                                        } else {
                                            pDialog.dismissWithAnimation();
                                            Intent daftarkunjungan = new Intent(getBaseContext(), daftar_kunjungan.class);
                                            daftarkunjungan.putExtra("szDocId", szDocId);
                                            daftarkunjungan.putExtra("routetype", route_type);

                                            startActivity(daftarkunjungan);
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
                                pDialog.dismissWithAnimation();

                                if (error instanceof TimeoutError || error instanceof NoConnectionError)     {
                                    Toast.makeText(callplan.this, "Connection Error", Toast.LENGTH_SHORT).show();
                                } else if (error instanceof AuthFailureError) {
                                    Toast.makeText(callplan.this, "Auth Failure Error", Toast.LENGTH_SHORT).show();
                                } else if (error instanceof ServerError) {
                                    Toast.makeText(callplan.this, "Server Error", Toast.LENGTH_SHORT).show();
                                } else if (error instanceof NetworkError) {
                                    Toast.makeText(callplan.this, "Network Error", Toast.LENGTH_SHORT).show();
                                } else if (error instanceof ParseError) {
                                    Toast.makeText(callplan.this, "Parse Error", Toast.LENGTH_SHORT).show();
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
                RequestQueue requestQueue = Volley.newRequestQueue(callplan.this);
                requestQueue.add(stringRequest);



            }
        });

    }

    private void postSFADoccall() {
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_SFADoccall",
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


                SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String currentDateandTime3 = sdf3.format(new Date());

                SimpleDateFormat dtmDoc = new SimpleDateFormat("yyyy-MM-dd");
                String currentDateandTime4 = dtmDoc.format(new Date());

                String[] parts2 = nik_baru.split("-");
                String restnomor2 = parts2[0];


                params.put("szDocId", idStd);
                params.put("dtmDoc", currentDateandTime4);
                params.put("dtmStart", currentDateandTime3);
                params.put("dtmFinish", currentDateandTime3);
                params.put("bStarted", "1");
                params.put("bFinisihed", "0");

                params.put("decKMStart", editkm.getText().toString());
                params.put("decKMFinish", "0");

                params.put("szBranchId", restnomor2);


                if(restnomor2.equals("321") || restnomor2.equals("336") || restnomor2.equals("324") || restnomor2.equals("317") || restnomor2.equals("036")){
                    params.put("szCompanyId", "ASA");
                } else {
                    params.put("szCompanyId", "TVIP");
                }

                params.put("szDocStatus", "Draft");

                params.put("szUserCreatedId", nik_baru);
                params.put("dtmCreated", currentDateandTime3);







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
        RequestQueue requestQueue2 = Volley.newRequestQueue(callplan.this);
        requestQueue2.getCache().clear();
        requestQueue2.add(stringRequest2);
    }

    //DISINI UNTUK GET EMPLOYEE ID DAN MEMBEDAKAN MANA CAN & DELIVERY
    private void persiapan_daftarkunjungan() {

        SharedPreferences sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String employeeId = sharedPreferences.getString("szDocCall", null);

        pDialog = new SweetAlertDialog(callplan.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Harap Menunggu");
        pDialog.show();
        pDialog.setCancelable(false);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Persiapan/index_SuratTugasDriver?nik_baru=" + employeeId, //szDocId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        pDialog.dismissWithAnimation();

                        try {

                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");
                            for (int i = 0; i < movieArray.length(); i++) {
                                final JSONObject movieObject = movieArray.getJSONObject(i);

                                String route_type = movieObject.getString("szRouteType");

                                Toast.makeText(callplan.this, "routenya " + route_type, Toast.LENGTH_SHORT).show();

                                if (route_type.equals("DEL")){
                                    pengaturanBar.setTitle("Daftar Kunjungan");
                                    listpelanggan_Delivery();
                                } else if (route_type.equals("CAN")){
                                    pengaturanBar.setTitle("Daftar Kunjungan");
                                    Toast.makeText(callplan.this, "routenya CAN", Toast.LENGTH_SHORT).show();
                                    listpelanggan_Canvaser();
                                } else {
                                    pengaturanBar.setTitle("Daftar Kunjungan");
                                    Toast.makeText(callplan.this, "routenya CAN - NON RUTE", Toast.LENGTH_SHORT).show();
                                    listpelanggan_Canvaser();
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

    //YANG DIPAKE DELIVERY
    //MENAMPILKAN LIST PELANGGAN BERDASARKAN SURAT TUGAS sZDocId
    private void listpelanggan_Delivery() {

        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        //szEmployeeId = 336-RP049
        String szEmployeeId = sharedPreferences.getString("szDocCall", null);
        Toast.makeText(callplan.this, "OKE = " + szEmployeeId, Toast.LENGTH_SHORT).show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Persiapan/index_List_Surat_Tugas2?szEmployeeId=" + szEmployeeId, //szDocId,
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
                                            movieObject.getString("tgl_dokumen"),
                                            movieObject.getString("ritase"));

                                    datadaftarkunjungan.add(movieItem);



                                }

                                adapter = new ListViewAdapterCallPlan(datadaftarkunjungan, getApplicationContext());
                                listdaftarkunjungan.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                            } else {
                                Toast.makeText(callplan.this, "Data belum ada", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(callplan.this, "Terjadi kesalahan saat memuat list Surat Tugas", Toast.LENGTH_SHORT).show();
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

    private void getRitase(String szDocId) {
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        //szEmployeeId = 336-RP049
        String szEmployeeId = sharedPreferences.getString("szDocCall", null);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_RitaseList?id_std=" + szDocId, //szDocId,
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
                                    final Ritase_pojo movieItem = new Ritase_pojo(
                                            movieObject.getString("id_bkb"),
                                            movieObject.getString("ritase"),
                                            movieObject.getString("jenis_type"));
                                    ritasePojos.add(movieItem);

                                    adapter2 = new ListViewAdapterRitase(ritasePojos, getApplicationContext());
                                    listRitase.setAdapter(adapter2);
                                    adapter2.notifyDataSetChanged();

                                    if(!movieObject.getString("id_driver").equals(szEmployeeId)){
                                        ritasePojos.remove(movieItem);
                                        adapter2.notifyDataSetChanged();
                                    }
                                    
                                }

                                Collections.sort(ritasePojos, new Comparator<Ritase_pojo>() {
                                    @Override
                                    public int compare(Ritase_pojo o1, Ritase_pojo o2) {
                                        return o1.getRitase().compareTo(o2.getRitase());
                                    }
                                });

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

    //YANG DIPAKE DELIVERY
    //MENAMPILKAN LIST PELANGGAN BERDASARKAN SURAT TUGAS sZDocId
    private void listpelanggan_Canvaser() {
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        //szEmployeeId = 336-RP049
        String szEmployeeId = sharedPreferences.getString("szDocCall", null);
        Toast.makeText(callplan.this, "OKE = " + szEmployeeId, Toast.LENGTH_SHORT).show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Persiapan/index_List_Surat_Tugas_canvaser_all?szEmployeeId=" + szEmployeeId, //szDocId,
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
                                            movieObject.getString("szDocId"),
                                            movieObject.getString("dtmDoc"),
                                            movieObject.getString("szRouteType"));
                                    getRitase(movieObject.getString("szDocId"));
                                    System.out.println("Link = " + "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_RitaseList?id_std=" + movieObject.getString("szDocId"));

                                    datadaftarkunjungan.add(movieItem);

                                }

                                adapter = new ListViewAdapterCallPlan(datadaftarkunjungan, getApplicationContext());
                                listdaftarkunjungan.setAdapter(adapter);
                                Utility.setListViewHeightBasedOnChildren(listdaftarkunjungan);
                                adapter.notifyDataSetChanged();



                            } else {
                                Toast.makeText(callplan.this, "Data belum ada", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(callplan.this, "Terjadi kesalahan saat memuat list Surat Tugas", Toast.LENGTH_SHORT).show();
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

            daftar_kunjungan_ritase.setText("Route Type = " + data.getRitase());

            return listViewItem;
        }
    }

    public class ListViewAdapterRitase extends ArrayAdapter<Ritase_pojo> {

        private class ViewHolder {
            TextView daftar_kunjungan_nomor_surat_tugas, daftar_kunjungan_ritase, jenisroute, statusBTU, jenistransaksi;
        }

        List<Ritase_pojo> Ritase_pojoList;
        private Context context;

        public ListViewAdapterRitase(List<Ritase_pojo> Ritase_pojoList, Context context) {
            super(context, R.layout.ritase, Ritase_pojoList);
            this.Ritase_pojoList = Ritase_pojoList;
            this.context = context;
        }

        public int getCount() {
            return Ritase_pojoList.size();
        }

        public Ritase_pojo getItem(int position) {
            return Ritase_pojoList.get(position);
        }

        @Override
        public int getViewTypeCount() {
            int count;
            if (Ritase_pojoList.size() > 0) {
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
            Ritase_pojo data = getItem(position);

            if (convertView == null) {
                viewHolder = new ListViewAdapterRitase.ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.ritase, parent, false);


                viewHolder.daftar_kunjungan_nomor_surat_tugas = (TextView) convertView.findViewById(R.id.daftar_kunjungan_nomor_surat_tugas);
                viewHolder.daftar_kunjungan_ritase = (TextView) convertView.findViewById(R.id.daftar_kunjungan_ritase);
                viewHolder.jenisroute = (TextView) convertView.findViewById(R.id.jenisroute);
                viewHolder.statusBTU = (TextView) convertView.findViewById(R.id.statusBTU);
                viewHolder.jenistransaksi = (TextView) convertView.findViewById(R.id.jenistransaksi);

                convertView.setTag(viewHolder);

            } else {
                viewHolder = (ListViewAdapterRitase.ViewHolder) convertView.getTag();
            }

            viewHolder.daftar_kunjungan_nomor_surat_tugas.setText("No BKB = " + data.getId_bkb());
            viewHolder.daftar_kunjungan_ritase.setText("Ritase = " + data.getRitase());
            viewHolder.jenisroute.setText("Jenis Route = " + data.getJenis_type());

            StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_BTU?szRefNo=" + data.getId_bkb(), //szDocId,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response){
                            try {
                                int number = 0;
                                JSONObject obj = new JSONObject(response);
                                if (obj.getString("status").equals("true")) {
                                    final JSONArray movieArray = obj.getJSONArray("data");
                                    for (int i = 0; i < movieArray.length(); i++) {
                                        final JSONObject movieObject = movieArray.getJSONObject(i);

                                        if(movieObject.getString("szRefType").equals(movieObject.getString("szRefPaymentType"))){
                                            viewHolder.statusBTU.setVisibility(View.VISIBLE);
                                            viewHolder.statusBTU.setText(" ("+movieObject.getString("szRefType")+")");
                                            viewHolder.statusBTU.setTextColor(Color.parseColor("#0F4C81"));

                                            viewHolder.jenistransaksi.setText("Jenis Transaksi = BTU");
                                            viewHolder.jenistransaksi.setVisibility(View.VISIBLE);
                                        } else {
                                            viewHolder.statusBTU.setVisibility(View.VISIBLE);
                                            viewHolder.statusBTU.setText(" ("+movieObject.getString("szRefType")+" dan "+movieObject.getString("szRefPaymentType")+")");
                                            viewHolder.statusBTU.setTextColor(Color.parseColor("#0F4C81"));

                                            viewHolder.jenistransaksi.setText("Jenis Transaksi = BTU");
                                            viewHolder.jenistransaksi.setVisibility(View.VISIBLE);
                                        }
                                    }




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
                            cekKredit();
                        }

                        private void cekKredit() {
                            StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_LHP?nomor_bkb=" + data.getId_bkb(), //szDocId,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response){
                                            try {
                                                JSONObject jsonObject = new JSONObject(response);
                                                if (jsonObject.getString("status").equals("true")) {
                                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                                    for (int i = 0; i < jsonArray.length(); i++) {
                                                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);


                                                        String mesin = jsonObject1.getString("total_pembayaran_mesin");
                                                        String tunai = jsonObject1.getString("total_pembayaran_tunai");
                                                        String transfer = jsonObject1.getString("total_pembayaran_transfer");

                                                        if(mesin.equals("") && tunai.equals("") && transfer.equals("")){
                                                            viewHolder.statusBTU.setVisibility(View.VISIBLE);
                                                            viewHolder.statusBTU.setText("(Full Kredit)");
                                                            viewHolder.statusBTU.setTextColor(Color.parseColor("#0F4C81"));
                                                        }
                                                    }
                                                }
                                                viewHolder.jenistransaksi.setText("Jenis Transaksi = Non - BTU");
                                                viewHolder.jenistransaksi.setVisibility(View.VISIBLE);

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            viewHolder.jenistransaksi.setText("Jenis Transaksi = Non - BTU");
                                            viewHolder.jenistransaksi.setVisibility(View.VISIBLE);
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
                            RequestQueue requestQueue = Volley.newRequestQueue(callplan.this);
                            requestQueue.add(stringRequest);
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
            RequestQueue requestQueue = Volley.newRequestQueue(callplan.this);
            requestQueue.add(stringRequest);





            return convertView;
        }

    }




    private static String tanggal(String inputDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date date = null;
        try {
            date = simpleDateFormat.parse(inputDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date == null) {
            return "";
        }
        SimpleDateFormat convetDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        return convetDateFormat.format(date);
    }

}