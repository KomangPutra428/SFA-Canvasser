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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
import com.google.android.material.card.MaterialCardView;
import com.tvip.canvasser.Perangkat.HttpsTrustManager;
import com.tvip.canvasser.R;
import com.tvip.canvasser.pojo.data_ritaselhp_pojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Summary_Transaksi_Lhp extends AppCompatActivity {

    LinearLayout wrap_lhp;
    MaterialCardView cardlist_lhp;
    TextView txt_jml_rit, txt_noDo_lhp, txt_nobkb;

    ListView list_ritase;
    List<data_ritaselhp_pojo> data_ritaselhp_pojos = new ArrayList<>();
    ListViewAdapterRitaseLhp adapter;

    SharedPreferences sharedPreferences;
    RelativeLayout relative_info_std_tidak_ditemukan;
    SweetAlertDialog pDialog;

    String employee_driver, idbranch_driver;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary_transaksi_lhp);
        HttpsTrustManager.allowAllSSL();

        list_ritase = findViewById(R.id.list_akhiri_kegiatan);
        relative_info_std_tidak_ditemukan = findViewById(R.id.relative_info_std_tidak_ditemukan);

        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        employee_driver = sharedPreferences.getString("szDocCall", null);
        String[] nikdriver = employee_driver.split("-");
        idbranch_driver = nikdriver[0];


        getRitase();

    }

    private void getRitase() {

        pDialog = new SweetAlertDialog(Summary_Transaksi_Lhp.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Harap Menunggu");
        pDialog.show();
        pDialog.setCancelable(false);

        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String nik_baru = sharedPreferences.getString("szDocCall", null);
        String[] parts = nik_baru.split("-");
        String restnomor = parts[0];

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_SummaryLHP?driver=" + nik_baru + "&branchId=" + restnomor, //mulai_perjalanan.id_pelanggan,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        relative_info_std_tidak_ditemukan.setVisibility(View.GONE);
                        pDialog.dismissWithAnimation();

                        try {
                            int number = 0;
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");

                            for (int i = 0; i < movieArray.length(); i++) {
                                final JSONObject movieObject = movieArray.getJSONObject(i);
                                final data_ritaselhp_pojo movieItem = new data_ritaselhp_pojo(
                                        movieObject.getString("ritase"),
                                        movieObject.getString("nomor_do"),
                                        movieObject.getString("nomor_std"),
                                        movieObject.getString("nomor_bkb"),
                                        movieObject.getString("mix_ref_std"));

                                data_ritaselhp_pojos.add(movieItem);
                                adapter = new Summary_Transaksi_Lhp.ListViewAdapterRitaseLhp(data_ritaselhp_pojos, getApplicationContext());
                                list_ritase.setAdapter(adapter);

                                if (!movieObject.getString("ritase").equals(akhirikegiatan.tv_ritase.getText().toString())) {
                                    data_ritaselhp_pojos.remove(movieItem);
                                    adapter.notifyDataSetChanged();
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
//                        Toast.makeText(Summary_Transaksi_Lhp.this, "Terjadi Kesalahan", Toast.LENGTH_SHORT).show();
                        pDialog.dismissWithAnimation();

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(Summary_Transaksi_Lhp.this, "Connection Error", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(Summary_Transaksi_Lhp.this, "Auth Failure Error", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ServerError) {
                            Toast.makeText(Summary_Transaksi_Lhp.this, "Server Error", Toast.LENGTH_SHORT).show();
                            relative_info_std_tidak_ditemukan.setVisibility(View.VISIBLE);
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(Summary_Transaksi_Lhp.this, "Network Error", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ParseError) {
                            Toast.makeText(Summary_Transaksi_Lhp.this, "Parse Error", Toast.LENGTH_SHORT).show();
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
                        50000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                ));

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    public class ListViewAdapterRitaseLhp extends ArrayAdapter<data_ritaselhp_pojo> {

        private List<data_ritaselhp_pojo> data_ritaselhp_pojos;

        private final Context context;

        public ListViewAdapterRitaseLhp(List<data_ritaselhp_pojo> data_ritaselhp_pojos, Context context) {
            super(context, R.layout.layout_ritase_lhp, data_ritaselhp_pojos);
            this.data_ritaselhp_pojos = data_ritaselhp_pojos;
            this.context = context;
        }

        @SuppressLint("NewApi")
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = LayoutInflater.from(context);

            @SuppressLint({"ViewHolder", "InflateParams"})
            View listViewItem = inflater.inflate(R.layout.layout_ritase_lhp, null, true);

            TextView txt_noDo_lhp = listViewItem.findViewById(R.id.txt_noDo_lhp);
            TextView txt_jml_rit = listViewItem.findViewById(R.id.txt_jml_rit);
            TextView txt_nobkb = listViewItem.findViewById(R.id.txt_nobkb);
            TextView status_mix = listViewItem.findViewById(R.id.status_mix);
            TextView txt_nobkbmix = listViewItem.findViewById(R.id.txt_nobkbmix);
            LinearLayout wrap_txt_lhp_bkb_mix = listViewItem.findViewById(R.id.wrap_txt_lhp_bkb_mix);

            data_ritaselhp_pojo data = getItem(position);

            txt_noDo_lhp.setText(data.getNomor_std());
            txt_jml_rit.setText(data.getRitase());
            txt_nobkb.setText(data.getBkb());

            if (data.getMix_ref_std().equals("") || data.getMix_ref_std().equals("null")) {
                status_mix.setText("BKB Non Mix");
                wrap_txt_lhp_bkb_mix.setVisibility(View.GONE);
            } else {
                status_mix.setText("BKB Mix");
                sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                String nik_baru = sharedPreferences.getString("szDocCall", null);
                String[] parts = nik_baru.split("-");
                String restnomor = parts[0];

                StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_BKBMix?driver=" + nik_baru + "&branchId=" + restnomor + "&ritase=" + data.getRitase(), //mulai_perjalanan.id_pelanggan,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                relative_info_std_tidak_ditemukan.setVisibility(View.GONE);
                                pDialog.dismissWithAnimation();

                                try {
                                    int number = 0;
                                    JSONObject obj = new JSONObject(response);
                                    final JSONArray movieArray = obj.getJSONArray("data");

                                    for (int i = 0; i < movieArray.length(); i++) {
                                        final JSONObject movieObject = movieArray.getJSONObject(i);
                                        if (!movieObject.getString("nomor_bkb").equals(txt_nobkb.getText().toString())) {
                                            txt_nobkbmix.setText(movieObject.getString("nomor_bkb"));
                                        }

                                        movieObject.getString("ritase");

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

                RequestQueue requestQueue = Volley.newRequestQueue(Summary_Transaksi_Lhp.this);
                requestQueue.add(stringRequest);
            }

            list_ritase.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    String nomordo = ((data_ritaselhp_pojo) parent.getItemAtPosition(position)).getNomor_do();
                    String nomorstd = ((data_ritaselhp_pojo) parent.getItemAtPosition(position)).getNomor_std();
                    String ritase = ((data_ritaselhp_pojo) parent.getItemAtPosition(position)).getRitase();
                    String nomorbkb = ((data_ritaselhp_pojo) parent.getItemAtPosition(position)).getBkb();



                    if(akhirikegiatan.jenis_ritase.equals("Non Rute")){
                        pDialog = new SweetAlertDialog(Summary_Transaksi_Lhp.this, SweetAlertDialog.PROGRESS_TYPE);
                        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                        pDialog.setTitleText("Harap Menunggu");
                        pDialog.setCancelable(false);
                        pDialog.show();
                        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_last_km?nomor_std=" + nomorstd, //mulai_perjalanan.id_pelanggan,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            int number = 0;
                                            JSONObject obj = new JSONObject(response);
                                            final JSONArray movieArray = obj.getJSONArray("data");

                                            for (int i = 0; i < movieArray.length(); i++) {
                                                pDialog.dismissWithAnimation();

                                                final JSONObject movieObject = movieArray.getJSONObject(i);
                                                int km_before = movieObject.getInt("km_lhp");
                                                String ritase_before = movieObject.getString("ritase");


                                                Dialog dialog = new Dialog(Summary_Transaksi_Lhp.this);
                                                dialog.setContentView(R.layout.topup_km_lhp);
                                                dialog.show();

                                                TextView keterangan = dialog.findViewById(R.id.keterangan);
                                                EditText editkm = dialog.findViewById(R.id.editkm);
                                                EditText editkmbefore = dialog.findViewById(R.id.editkmbefore);

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

                                                Button tidak = dialog.findViewById(R.id.tidak);
                                                Button ya = dialog.findViewById(R.id.ya);

                                                keterangan.setText("Isi Kilometer Rit " + ritase);

                                                editkmbefore.setText(movieObject.getString("km_lhp"));

                                                tidak.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        dialog.dismiss();
                                                    }
                                                });

                                                ya.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        if (editkm.getText().toString().length() == 0) {
                                                            editkm.setError("Isi KM Rit " + ritase);
                                                        } else if (editkm.getText().toString().length() < 6) {
                                                            editkm.setError("KM harus minimal 6 Digit");
                                                        } else if (km_before >= Integer.parseInt(editkm.getText().toString())) {
                                                            new SweetAlertDialog(Summary_Transaksi_Lhp.this, SweetAlertDialog.WARNING_TYPE)
                                                                    .setTitleText("KM Rit " + ritase + " Harus Lebih Besar Daripada Rit " + ritase_before)
                                                                    .setConfirmText("OK")
                                                                    .show();
                                                        } else {
                                                            Intent i = new Intent(getBaseContext(), detail_summary_transaksi_lhp_new.class);

                                                            i.putExtra("nomordo", nomordo);
                                                            i.putExtra("nomorstd", nomorstd);
                                                            i.putExtra("ritase", ritase);
                                                            i.putExtra("nobkb", nomorbkb);
                                                            i.putExtra("kilometer", editkm.getText().toString());

                                                            startActivity(i);
                                                        }
                                                    }
                                                });

                                            }

                                        } catch (JSONException e) {
                                            e.printStackTrace();

                                        }
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        bySTDNonRute(nomorstd, ritase, nomordo, nomorbkb);

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

                        RequestQueue requestQueue = Volley.newRequestQueue(Summary_Transaksi_Lhp.this);
                        requestQueue.add(stringRequest);
                    } else {
                        pDialog = new SweetAlertDialog(Summary_Transaksi_Lhp.this, SweetAlertDialog.PROGRESS_TYPE);
                        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                        pDialog.setTitleText("Harap Menunggu");
                        pDialog.setCancelable(false);
                        pDialog.show();
                        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_last_km?nomor_std=" + nomorstd, //mulai_perjalanan.id_pelanggan,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            int number = 0;
                                            JSONObject obj = new JSONObject(response);
                                            final JSONArray movieArray = obj.getJSONArray("data");

                                            for (int i = 0; i < movieArray.length(); i++) {
                                                pDialog.dismissWithAnimation();

                                                final JSONObject movieObject = movieArray.getJSONObject(i);
                                                int km_before = movieObject.getInt("km_lhp");
                                                String ritase_before = movieObject.getString("ritase");


                                                Dialog dialog = new Dialog(Summary_Transaksi_Lhp.this);
                                                dialog.setContentView(R.layout.topup_km_lhp);
                                                dialog.show();

                                                TextView keterangan = dialog.findViewById(R.id.keterangan);
                                                EditText editkm = dialog.findViewById(R.id.editkm);
                                                EditText editkmbefore = dialog.findViewById(R.id.editkmbefore);

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

                                                Button tidak = dialog.findViewById(R.id.tidak);
                                                Button ya = dialog.findViewById(R.id.ya);

                                                keterangan.setText("Isi Kilometer Rit " + ritase);

                                                editkmbefore.setText(movieObject.getString("km_lhp"));

                                                tidak.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        dialog.dismiss();
                                                    }
                                                });

                                                ya.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        if (editkm.getText().toString().length() == 0) {
                                                            editkm.setError("Isi KM Rit " + ritase);
                                                        } else if (editkm.getText().toString().length() < 6) {
                                                            editkm.setError("KM harus minimal 6 Digit");
                                                        } else if (km_before >= Integer.parseInt(editkm.getText().toString())) {
                                                            new SweetAlertDialog(Summary_Transaksi_Lhp.this, SweetAlertDialog.WARNING_TYPE)
                                                                    .setTitleText("KM Rit " + ritase + " Harus Lebih Besar Daripada Rit " + ritase_before)
                                                                    .setConfirmText("OK")
                                                                    .show();
                                                        } else {
                                                            Intent i = new Intent(getBaseContext(), detail_summary_transaksi_lhp_new.class);

                                                            i.putExtra("nomordo", nomordo);
                                                            i.putExtra("nomorstd", nomorstd);
                                                            i.putExtra("ritase", ritase);
                                                            i.putExtra("nobkb", nomorbkb);
                                                            i.putExtra("kilometer", editkm.getText().toString());

                                                            startActivity(i);
                                                        }
                                                    }
                                                });

                                            }

                                        } catch (JSONException e) {
                                            e.printStackTrace();

                                        }
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        bySTD(nomorstd, ritase, nomordo, nomorbkb);

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

                        RequestQueue requestQueue = Volley.newRequestQueue(Summary_Transaksi_Lhp.this);
                        requestQueue.add(stringRequest);
                    }


                }
            });

            return listViewItem;
        }
    }

    private void bySTDNonRute(String nomorstd, String ritase, String nomordo, String nomorbkb) {
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String nik_baru = sharedPreferences.getString("szDocCall", null);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_SuratTugasNonRute?szEmployeeId=" + nik_baru,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");
                            for (int i = 0; i < 1; i++) {
                                final JSONObject movieObject = movieArray.getJSONObject(i);

                                pDialog.dismissWithAnimation();
                                Dialog dialog = new Dialog(Summary_Transaksi_Lhp.this);
                                dialog.setContentView(R.layout.topup_km_lhp);
                                dialog.show();

                                TextView keterangan = dialog.findViewById(R.id.keterangan);
                                EditText editkm = dialog.findViewById(R.id.editkm);
                                Button tidak = dialog.findViewById(R.id.tidak);
                                Button ya = dialog.findViewById(R.id.ya);
                                EditText editkmbefore = dialog.findViewById(R.id.editkmbefore);

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
                                editkmbefore.setText(movieObject.getString("decKMStart"));
                                editkmbefore.setHint("KM Awal");

                                keterangan.setText("Isi Kilometer Rit " + ritase);

                                tidak.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                    }
                                });

                                ya.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (editkm.getText().toString().length() == 0) {
                                            editkm.setError("Isi KM Rit " + ritase);
                                        } else if (editkm.getText().toString().length() < 6) {
                                            editkm.setError("KM harus minimal 6 Digit");
                                        } else if (Integer.parseInt(editkmbefore.getText().toString()) >= Integer.parseInt(editkm.getText().toString())) {
                                            new SweetAlertDialog(Summary_Transaksi_Lhp.this, SweetAlertDialog.WARNING_TYPE)
                                                    .setTitleText("KM Rit " + ritase + " Harus Lebih Besar Daripada KM Awal")
                                                    .setConfirmText("OK")
                                                    .show();
                                        } else {
                                            Intent i = new Intent(getBaseContext(), detail_summary_transaksi_lhp_new.class);

                                            i.putExtra("nomordo", nomordo);
                                            i.putExtra("nomorstd", nomorstd);
                                            i.putExtra("ritase", ritase);
                                            i.putExtra("nobkb", nomorbkb);
                                            i.putExtra("kilometer", editkm.getText().toString());

                                            startActivity(i);
                                        }
                                    }
                                });


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
        RequestQueue requestQueue = Volley.newRequestQueue(Summary_Transaksi_Lhp.this);
        requestQueue.add(stringRequest);
    }

    private void bySTD(String nomorstd, String ritase, String nomordo, String nomorbkb) {
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

                                pDialog.dismissWithAnimation();
                                Dialog dialog = new Dialog(Summary_Transaksi_Lhp.this);
                                dialog.setContentView(R.layout.topup_km_lhp);
                                dialog.show();

                                TextView keterangan = dialog.findViewById(R.id.keterangan);
                                EditText editkm = dialog.findViewById(R.id.editkm);
                                Button tidak = dialog.findViewById(R.id.tidak);
                                Button ya = dialog.findViewById(R.id.ya);
                                EditText editkmbefore = dialog.findViewById(R.id.editkmbefore);

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
                                editkmbefore.setText(movieObject.getString("decKMStart"));
                                editkmbefore.setHint("KM Awal");

                                keterangan.setText("Isi Kilometer Rit " + ritase);

                                tidak.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                    }
                                });

                                ya.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (editkm.getText().toString().length() == 0) {
                                            editkm.setError("Isi KM Rit " + ritase);
                                        } else if (editkm.getText().toString().length() < 6) {
                                            editkm.setError("KM harus minimal 6 Digit");
                                        }else if (Integer.parseInt(editkmbefore.getText().toString()) >= Integer.parseInt(editkm.getText().toString())) {
                                            new SweetAlertDialog(Summary_Transaksi_Lhp.this, SweetAlertDialog.WARNING_TYPE)
                                                    .setTitleText("KM Rit " + ritase + " Harus Lebih Besar Daripada KM Awal")
                                                    .setConfirmText("OK")
                                                    .show();
                                        } else {
                                            Intent i = new Intent(getBaseContext(), detail_summary_transaksi_lhp_new.class);

                                            i.putExtra("nomordo", nomordo);
                                            i.putExtra("nomorstd", nomorstd);
                                            i.putExtra("ritase", ritase);
                                            i.putExtra("nobkb", nomorbkb);
                                            i.putExtra("kilometer", editkm.getText().toString());

                                            startActivity(i);
                                        }
                                    }
                                });


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
        RequestQueue requestQueue = Volley.newRequestQueue(Summary_Transaksi_Lhp.this);
        requestQueue.add(stringRequest);
    }
}