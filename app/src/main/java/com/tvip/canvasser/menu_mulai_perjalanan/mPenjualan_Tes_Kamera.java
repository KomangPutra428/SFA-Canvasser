package com.tvip.canvasser.menu_mulai_perjalanan;

import static com.tvip.canvasser.menu_mulai_perjalanan.menu_pelanggan.no_surat;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.android.material.button.MaterialButton;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.tvip.canvasser.R;
import com.tvip.canvasser.pojo.data_produk_penjualan_pojo;
import com.tvip.canvasser.pojo.data_terima_produk_pojo;
import com.tvip.canvasser.pojo.total_penjualan_pojo;

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
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class mPenjualan_Tes_Kamera extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    ImageView img, bt_increase, bt_decrease, bt_increase_detail_job, bt_decrease_detail_job;
    Bitmap bitmap;
    String encodeImageString;
    String idstd, idcustomer;
    TextView delivery_order;
    GPSTracker gps;


    ListView listproductpenjualan;
    List<data_produk_penjualan_pojo> data_produk_penjualan_pojos = new ArrayList<>();
    List<total_penjualan_pojo>totalPenjualanPojos = new ArrayList<>();
    List<data_terima_produk_pojo> data_terima_produk_pojos = new ArrayList<>();
    mPenjualan_Tes_Kamera.ListViewAdapteProductPenjualan adapter;

    ImageButton refresh;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mpenjualan_tes_kamera);

        System.out.println("HASIL ID STD = " + getIntent().getStringExtra("idStd"));
        System.out.println("HASIL ID CUSTOMER = " + getIntent().getStringExtra("idCustomer"));
        idstd = getIntent().getStringExtra("idStd");
        idcustomer = getIntent().getStringExtra("idCustomer");

        delivery_order = findViewById(R.id.tv_delivery_order);
        listproductpenjualan = findViewById(R.id.listproductpenjualan);

        img = (ImageView) findViewById(R.id.img_background);
        refresh = findViewById(R.id.refresh);


        Button bt_camera = findViewById(R.id.foto);
        bt_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withActivity(mPenjualan_Tes_Kamera.this)
                        .withPermission(Manifest.permission.CAMERA)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(intent, 2);
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();
                            }
                        }).check();
            }
        });

        body_data();
    }

    public void body_data(){

        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://restserver.tvip.co.id:8765/android/"+sharedPreferences.getString("link", null)+"/utilitas/Mulai_Perjalanan/index_Detail_terima_produk",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {
                            int number = 0;
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data_body");
                            for (int i = 0; i < movieArray.length(); i++) {


                                adapter = new mPenjualan_Tes_Kamera.ListViewAdapteProductPenjualan(data_terima_produk_pojos, getApplicationContext());
                                listproductpenjualan.setAdapter(adapter);

                            }


                        } catch(JSONException e){
                            e.printStackTrace();

                        }

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
                SharedPreferences sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                String nik_baru = sharedPreferences.getString("szDocCall", null);

                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
                String currentDateandTime2 = sdf2.format(new Date());

                String[] parts = no_surat.split("-");
                String restnomor = parts[0];
                String restnomorbaru = restnomor.replace(" ", "");

                params.put("id_customer", idcustomer);
                params.put("id_std", idstd);

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
        RequestQueue requestQueue2 = Volley.newRequestQueue(mPenjualan_Tes_Kamera.this);
        requestQueue2.add(stringRequest2);

    }

    public class ListViewAdapteProductPenjualan extends ArrayAdapter<data_terima_produk_pojo> {

        private class ViewHolder {
            TextView namaproduk, qty_order, total_harga, harga_satuan, diskon_tiv, diskon_distributor, diskon_internal;
            LinearLayout linear_detail_terima_produk;
            MaterialButton buttonExpand;
        }

        List<data_terima_produk_pojo> data_terima_produk_pojos;
        private Context context;

        public ListViewAdapteProductPenjualan(List<data_terima_produk_pojo> data_terima_produk_pojos, Context context) {
            super(context, R.layout.list_terima_produk, data_terima_produk_pojos);
            this.data_terima_produk_pojos = data_terima_produk_pojos;
            this.context = context;
        }

        public int getCount() {
            return data_terima_produk_pojos.size();
        }

        public data_terima_produk_pojo getItem(int position) {
            return data_terima_produk_pojos.get(position);
        }

        @Override
        public int getViewTypeCount() {
            int count;
            if (data_terima_produk_pojos.size() > 0) {
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
            final mPenjualan_Tes_Kamera.ListViewAdapteProductPenjualan.ViewHolder viewHolder;
            data_terima_produk_pojo data = getItem(position);

            if (convertView == null) {
                viewHolder = new mPenjualan_Tes_Kamera.ListViewAdapteProductPenjualan.ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.list_terima_produk, parent, false);

                viewHolder.namaproduk = (TextView) convertView.findViewById(R.id.namaproduk);
                viewHolder.qty_order = (TextView) convertView.findViewById(R.id.qty_order);
                viewHolder.total_harga = (TextView) convertView.findViewById(R.id.tv_total_harga); //total harga per 1 produk/ 1 row/ 1lsitview

                viewHolder.harga_satuan = (TextView) convertView.findViewById(R.id.tv_list_terima_produk_harga_satuan);
                viewHolder.diskon_tiv = (TextView) convertView.findViewById(R.id.tv_list_terima_produk_diskon_tiv);
                viewHolder.diskon_distributor = (TextView) convertView.findViewById(R.id.tv_list_terima_produk_diskon_distributor);
                viewHolder.diskon_internal = (TextView) convertView.findViewById(R.id.tv_list_terima_produk_diskon_internal);

                viewHolder.linear_detail_terima_produk = (LinearLayout) convertView.findViewById(R.id.linear_detail_terima_produk);

                //viewHolder.refresh = (ImageButton) convertView.findViewById(R.id.refresh);

                convertView.setTag(viewHolder);

            } else {
                viewHolder = (mPenjualan_Tes_Kamera.ListViewAdapteProductPenjualan.ViewHolder) convertView.getTag();
            }

//            double number = Double.parseDouble(branch);
//            String COUNTRY = "ID";
//            String LANGUAGE = "id";
//            String str = NumberFormat.getCurrencyInstance(new Locale(LANGUAGE, COUNTRY)).format(number);
//
//            System.out.println("HASILNYA = " + str);

            //viewHolder.harga_satuan.setText(kursIndonesia.format(data.getHarga_satuan()));

            viewHolder.namaproduk.setText(data.getNama_produk());

            String[] total_harga_row = data.getTotalhargarow().split("\\.");
            String string_total_harga_row = total_harga_row[0];

            String[] harga_satuan = data.getHarga_satuan().split("\\.");
            String string_harga_satuan = harga_satuan[0];

            String[] diskon_tiv = data.getDiskon_tiv().split("\\.");
            String string_diskon_tiv = diskon_tiv[0];

            String[] diskon_distributor = data.getDiskon_distributor().split("\\.");
            String string_diskon_distributor = diskon_distributor[0];

            String[] diskon_internal = data.getDiskon_internal().split("\\.");
            String string_diskon_internal= diskon_internal[0];


//            DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
//            DecimalFormatSymbols formatRp = new DecimalFormatSymbols();
//            formatRp.setCurrencySymbol("Rp. ");
//            formatRp.setMonetaryDecimalSeparator(',');
//            formatRp.setGroupingSeparator('.');
//            kursIndonesia.setDecimalFormatSymbols(formatRp);

            viewHolder.total_harga.setText(string_total_harga_row);

            viewHolder.harga_satuan.setText(string_harga_satuan);
            viewHolder.diskon_tiv.setText(string_diskon_tiv);
            viewHolder.diskon_distributor.setText(string_diskon_distributor);
            viewHolder.diskon_internal.setText(string_diskon_internal);

            if(data.getQty_produk() == null){
                viewHolder.qty_order.setText("0");
            } else {
                viewHolder.qty_order.setText(data.getQty_produk());
            }

            viewHolder.buttonExpand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent intent = new Intent(getApplicationContext(), sisa_stock.class);
//                    intent.putExtra("list", String.valueOf(position));
//                    intent.putExtra("nama_barang", data.getSzName());
//                    intent.putExtra("uang", data.getDecPrice());
//                    startActivity(intent);

                    if (viewHolder.linear_detail_terima_produk.getVisibility() == View.GONE) {
                        //expandedChildList.set(arg2, true);
                        viewHolder.linear_detail_terima_produk.setVisibility(View.VISIBLE);
                    }
                    else {
                        //expandedChildList.set(arg2, false);
                        viewHolder.linear_detail_terima_produk.setVisibility(View.GONE);
                    }
                }
            });

            return convertView;
        }

//        public void filter(String charText) {
//            charText = charText.toLowerCase(Locale.getDefault());
//            data_terima_produk_pojos.clear();
//            if (charText.length() == 0) {
//                data_terima_produk_pojos.addAll(data_produk_penjualanList);
//            } else {
//                for (data_terima_produk_pojo wp : data_produk_penjualanList) {
//                    if (wp.getSzName().toLowerCase(Locale.getDefault()).contains(charText)) {
//                        data_produk_penjualan_pojos.add(wp);
//                    }
//                }
//            }
//            notifyDataSetChanged();
//        }

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        LinearLayout linear_keterangan_photo = findViewById(R.id.linear_keterangan_photo);
        TextView tv_tanggal = findViewById(R.id.tv_tanggal_photo);
        TextView tv_longlat = findViewById(R.id.tv_longlat_photo);
        TextView tv_alamat = findViewById(R.id.tv_alamat_photo);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            Uri filepath = data.getData();

            gps = new GPSTracker(mPenjualan_Tes_Kamera.this);

            try {
                InputStream inputStream = getContentResolver().openInputStream(filepath);
                bitmap = BitmapFactory.decodeStream(inputStream);
                img.setImageBitmap(bitmap);
                encodeBitmapImage(bitmap);

                if(gps.canGetLocation()) {

                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();

                    linear_keterangan_photo.setVisibility(View.VISIBLE);

                    // \n is for new line
                    Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();

                } else {
                    // Can't get location.
                    // GPS or network is not enabled.
                    // Ask user to enable GPS/network in settings.

                    linear_keterangan_photo.setVisibility(View.GONE);

                    Toast.makeText(getApplicationContext(), "GAK DAPET", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception ex) {

            }
        } else if (requestCode == 2 && resultCode == RESULT_OK) {

            gps = new GPSTracker(mPenjualan_Tes_Kamera.this);


            if(gps.canGetLocation()) {
                double latitude = gps.getLatitude();
                double longitude = gps.getLongitude();

                linear_keterangan_photo.setVisibility(View.VISIBLE);

                // \n is for new line
                Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();

                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> addresses = null;
                try {
                    addresses = geocoder.getFromLocation(latitude, longitude, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
                String currentDateandTime = sdf.format(new Date());

                String cityName = addresses.get(0).getAddressLine(0);
                String stateName = addresses.get(0).getLocality();
                String countryName = addresses.get(0).getCountryName();

                System.out.println("cityName" + cityName);
                System.out.println("stateName" + stateName);
                System.out.println("countryName" + countryName);

                tv_tanggal.setText(currentDateandTime);
                tv_longlat.setText(latitude+", "+longitude);
                tv_alamat.setText(cityName);

                /**
                 * If you only want the city name then you should use addresses.get(0).getLocality() instead, addresses.get(0).getAddressLine(0)
                 * might give you more information additionally to your city name based on the address (Read the Geocoder Documentation).
                 * but the Locality gives you exactly the cityName.
                 */

            } else {
                // Can't get location.
                // GPS or network is not enabled.
                // Ask user to enable GPS/network in settings.
                linear_keterangan_photo.setVisibility(View.GONE);

                gps.showSettingsAlert();
            }

            bitmap = (Bitmap) data.getExtras().get("data");
            img.setImageBitmap(bitmap);
            encodeBitmapImage(bitmap);

        } else if (resultCode == RESULT_CANCELED) {
            // user cancelled Image capture
            Toast.makeText(getApplicationContext(),
                    "Cancelled", Toast.LENGTH_SHORT)
                    .show();
        } else {
            // failed to capture image
            Toast.makeText(getApplicationContext(),
                    "Error!", Toast.LENGTH_SHORT)
                    .show();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void encodeBitmapImage(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        byte[] bytesofimage = byteArrayOutputStream.toByteArray();
        encodeImageString = android.util.Base64.encodeToString(bytesofimage, Base64.DEFAULT);
    }
}