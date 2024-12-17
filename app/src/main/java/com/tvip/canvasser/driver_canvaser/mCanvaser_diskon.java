package com.tvip.canvasser.driver_canvaser;


import static com.tvip.canvasser.driver_canvaser.mCanvaser_Terima_Produk.tv_diskon_format_rupiah;
import static com.tvip.canvasser.driver_canvaser.mCanvaser_Terima_Produk.tv_total_harga_format_rupiah;
import static com.tvip.canvasser.driver_canvaser.mCanvaser_Terima_Produk.total_pembayaran;



import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tvip.canvasser.R;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class mCanvaser_diskon extends AppCompatActivity {
    ImageButton qtyorderminus, qtyorderadd;
    EditText qtyorder;

    EditText disc_principal, disc_distributor, disc_internal;
    Button reset, order;
    TextView namaproduk, harga;
    int count;
    int price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mcanvaser_diskon);

        disc_principal = findViewById(R.id.disc_principal);
        disc_distributor = findViewById(R.id.disc_distributor);
        disc_internal = findViewById(R.id.disc_internal);

        disc_internal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                disc_internal.removeTextChangedListener(this);

                try {
                    String originalString = s.toString();

                    Long longval;
                    if (originalString.contains(",")) {
                        originalString = originalString.replaceAll(",", "");
                    }
                    longval = Long.parseLong(originalString);

                    DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                    formatter.applyPattern("#,###,###,###");
                    String formattedString = formatter.format(longval);

                    //setting text after format to EditText
                    disc_internal.setText(formattedString);
                    disc_internal.setSelection(disc_internal.getText().length());
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }

                disc_internal.addTextChangedListener(this);


            }
        });


        disc_distributor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                disc_distributor.removeTextChangedListener(this);

                try {
                    String originalString = s.toString();

                    Long longval;
                    if (originalString.contains(",")) {
                        originalString = originalString.replaceAll(",", "");
                    }
                    longval = Long.parseLong(originalString);

                    DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                    formatter.applyPattern("#,###,###,###");
                    String formattedString = formatter.format(longval);

                    //setting text after format to EditText
                    disc_distributor.setText(formattedString);
                    disc_distributor.setSelection(disc_distributor.getText().length());
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }

                disc_distributor.addTextChangedListener(this);


            }
        });


        disc_principal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                disc_principal.removeTextChangedListener(this);

                try {
                    String originalString = s.toString();

                    Long longval;
                    if (originalString.contains(",")) {
                        originalString = originalString.replaceAll(",", "");
                    }
                    longval = Long.parseLong(originalString);

                    DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                    formatter.applyPattern("#,###,###,###");
                    String formattedString = formatter.format(longval);

                    //setting text after format to EditText
                    disc_principal.setText(formattedString);
                    disc_principal.setSelection(disc_principal.getText().length());
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }

                disc_principal.addTextChangedListener(this);


            }
        });

        namaproduk = findViewById(R.id.namaproduk);

        qtyorderminus = findViewById(R.id.qtyorderminus);
        qtyorder = findViewById(R.id.qtyorder);
        qtyorderadd = findViewById(R.id.qtyorderadd);

        reset = findViewById(R.id.reset);
        order = findViewById(R.id.order);

        harga = findViewById(R.id.harga);

        qtyorder.setText("0");
        namaproduk.setText(getIntent().getStringExtra("nama_produk"));

        count = 0;

        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();
        formatRp.setCurrencySymbol("Rp. ");
        formatRp.setMonetaryDecimalSeparator(',');
        formatRp.setGroupingSeparator('.');
        kursIndonesia.setDecimalFormatSymbols(formatRp);

        qtyorder.setText(getIntent().getStringExtra("qtyedit"));

        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int harga = Integer.parseInt(getIntent().getStringExtra("hargasatuan")) * Integer.parseInt(qtyorder.getText().toString());
                int discount = Integer.parseInt(disc_principal.getText().toString().replaceAll( ",", "" )) + Integer.parseInt(disc_distributor.getText().toString().replaceAll( ",", "" )) + Integer.parseInt(disc_internal.getText().toString().replaceAll( ",", "" ));



                if(qtyorder.getText().toString().length() == 0){
                    qtyorder.setError("Isi Qty");
                } else if(harga <= discount && !getIntent().getStringExtra("nama_produk").contains("TISSUE")){
                    new SweetAlertDialog(mCanvaser_diskon.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Harga Tidak Boleh Lebih Kecil Dari Diskon")
                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .show();

                } else if(qtyorder.getText().toString().length() != 0){
                    int Qty = Integer.parseInt(qtyorder.getText().toString());

                    String qtyasli = getIntent().getStringExtra("qty_asli");
                    String[] Qtyparts = qtyasli.split("\\.");
                    String QtypartsSlice = Qtyparts[0];

                    if(Qty > Integer.parseInt(QtypartsSlice) && getIntent().getStringExtra("nama_produk").contains("tissue")){
                        new SweetAlertDialog(mCanvaser_diskon.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Qty Tidak Boleh Melebihi Batas Qty asli")
                                .setConfirmText("OK")
                                .show();
                        qtyorder.setText("0");
                    } else {
                        if(getIntent().getStringExtra("hargasatuan").equals("null")){
                            price = (0);
                        } else {
                            price = Integer.parseInt(getIntent().getStringExtra("hargasatuan"));
                        }

                        int qty = Integer.parseInt(qtyorder.getText().toString());

                        int totalharga = price * qty;

                        int jumlah_harga = 0;
                        int jumlah_diskon = 0;


                        mCanvaser_Terima_Produk.adapter.getItem(Integer.parseInt(getIntent().getStringExtra("position"))).setQtyEdit(qtyorder.getText().toString());
                        mCanvaser_Terima_Produk.adapter.getItem(Integer.parseInt(getIntent().getStringExtra("position"))).setTotalHarga(String.valueOf(totalharga));

                        mCanvaser_Terima_Produk.adapter.getItem(Integer.parseInt(getIntent().getStringExtra("position"))).setDiskon_tiv(disc_principal.getText().toString().replaceAll( ",", "" ));
                        mCanvaser_Terima_Produk.adapter.getItem(Integer.parseInt(getIntent().getStringExtra("position"))).setDiskon_distributor(disc_distributor.getText().toString().toString().replaceAll( ",", "" ));
                        mCanvaser_Terima_Produk.adapter.getItem(Integer.parseInt(getIntent().getStringExtra("position"))).setDiskon_internal(disc_internal.getText().toString().replaceAll( ",", "" ));

                        mCanvaser_Terima_Produk.listproduk.setAdapter(mCanvaser_Terima_Produk.adapter);
                        mCanvaser_Terima_Produk.adapter.notifyDataSetChanged();



                        for(int i = 0; i < mCanvaser_Terima_Produk.data_terima_produk_pojos.size();i++){
                            jumlah_harga += Integer.parseInt(mCanvaser_Terima_Produk.adapter.getItem(i).getTotalHarga());
                            jumlah_diskon += Integer.parseInt(mCanvaser_Terima_Produk.adapter.getItem(i).getDiskon_distributor()) + Integer.parseInt(mCanvaser_Terima_Produk.adapter.getItem(i).getDiskon_internal()) + Integer.parseInt(mCanvaser_Terima_Produk.adapter.getItem(i).getDiskon_tiv());
                        }

                        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
                        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();
                        formatRp.setCurrencySymbol("Rp. ");
                        formatRp.setMonetaryDecimalSeparator(',');
                        formatRp.setGroupingSeparator('.');
                        kursIndonesia.setDecimalFormatSymbols(formatRp);
                        int totalasli = jumlah_harga - jumlah_diskon;

                        mCanvaser_Terima_Produk.total_harga = jumlah_harga;
                        mCanvaser_Terima_Produk.total_diskon = jumlah_diskon;

                        tv_diskon_format_rupiah.setText(kursIndonesia.format(jumlah_diskon));
                        tv_total_harga_format_rupiah.setText(kursIndonesia.format(jumlah_harga));
                        total_pembayaran.setText(kursIndonesia.format(jumlah_harga - jumlah_diskon));


                        finish();
                    }
                }

            }
        });

        if(getIntent().getStringExtra("hargasatuan").equals("null")){
            harga.setText("Harga Per Produk = " + kursIndonesia.format(0));
        } else {
            harga.setText("Harga Per Produk = " + kursIndonesia.format(Integer.parseInt(getIntent().getStringExtra("hargasatuan"))));
        }



        reset.setOnClickListener(v -> {
            count = 0;
            qtyorder.setText(String.valueOf(count));

            disc_principal.setText("0");
            disc_distributor.setText("0");
            disc_internal.setText("0");

        });

        if(getIntent().getStringExtra("nama_produk").contains("TISSUE")){
            disc_principal.setFocusable(false);
            disc_principal.setLongClickable(false);

            disc_distributor.setFocusable(false);
            disc_distributor.setLongClickable(false);

            disc_internal.setFocusable(false);
            disc_internal.setLongClickable(false);
        }







        qtyorderadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                String qty = getIntent().getStringExtra("qty_asli");
                String[] Qtyparts = qty.split("\\.");
                String qtySlice = Qtyparts[0];
                int max = Integer.parseInt(qtySlice);

                if(max < count){
                    count --;
                    new SweetAlertDialog(mCanvaser_diskon.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Qty Tidak Boleh Melebihi Batas Qty asli")
                            .setConfirmText("OK")
                            .show();
                } else {
                    qtyorder.setText(String.valueOf(count));
                }

                qtyorder.setError(null);
            }

        });
        qtyorderminus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qtyorder.setText(String.valueOf(count));
                qtyorder.setError(null);
                if (count == 0) {
                    return;
                }
                count--;
            }
        });

    }
}