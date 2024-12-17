package com.tvip.canvasser.menu_mulai_perjalanan;

import static com.tvip.canvasser.menu_mulai_perjalanan.sisa_stock.uang;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tvip.canvasser.R;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class diskon extends AppCompatActivity {
    ImageButton qtyorderminus, qtyorderadd;
    EditText qtyorder;
    int count;

    EditText disc_principal, disc_distributor, disc_internal;
    Button reset, order;
    TextView namaproduk, harga;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diskon);

        disc_principal = findViewById(R.id.disc_principal);
        disc_distributor = findViewById(R.id.disc_distributor);
        disc_internal = findViewById(R.id.disc_internal);

        String[] parts = uang.split("\\.");
        String szIdSlice = parts[0];
        harga = findViewById(R.id.harga);

        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();
        formatRp.setCurrencySymbol("Rp. ");
        formatRp.setMonetaryDecimalSeparator(',');
        formatRp.setGroupingSeparator('.');
        kursIndonesia.setDecimalFormatSymbols(formatRp);


        harga.setText("Harga Per Produk = " + kursIndonesia.format(Integer.parseInt(szIdSlice)));

        namaproduk = findViewById(R.id.namaproduk);

        qtyorderminus = findViewById(R.id.qtyorderminus);
        qtyorder = findViewById(R.id.qtyorder);
        qtyorderadd = findViewById(R.id.qtyorderadd);

        reset = findViewById(R.id.reset);
        order = findViewById(R.id.order);

        count = 0;
        qtyorder.setText(String.valueOf(count));

        if(uang.equals("0.0000")){
            disc_principal.setFocusable(false);
            disc_principal.setLongClickable(false);

            disc_distributor.setFocusable(false);
            disc_distributor.setLongClickable(false);

            disc_internal.setFocusable(false);
            disc_internal.setLongClickable(false);
        }

        disc_principal.setText("0");
        disc_distributor.setText("0");
        disc_internal.setText("0");

        namaproduk.setText(sisa_stock.namaproduk.getText().toString());

        reset.setOnClickListener(v -> {
            count = 0;
            qtyorder.setText(String.valueOf(count));

            disc_principal.setText("0");
            disc_distributor.setText("0");
            disc_internal.setText("0");

        });

        order.setOnClickListener(v -> {
            int harga = Integer.parseInt(szIdSlice) * Integer.parseInt(qtyorder.getText().toString());

            int discount = Integer.parseInt(disc_principal.getText().toString()) + Integer.parseInt(disc_distributor.getText().toString()) + Integer.parseInt(disc_internal.getText().toString());


            if(qtyorder.getText().toString().length() == 0 || qtyorder.getText().toString().equals("0")){
                qtyorder.setError("Qty tidak boleh kosong");
            } else if(harga < discount){
                new SweetAlertDialog(diskon.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Harga Tidak Boleh Lebih Kecil Dari Diskon")
                        .setConfirmText("OK")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .show();

            } else if(uang.equals("0.0000")){
                int total = Integer.parseInt(qtyorder.getText().toString()) + Integer.parseInt(disc_principal.getText().toString()) + Integer.parseInt(disc_distributor.getText().toString()) + Integer.parseInt(disc_internal.getText().toString());

                product_penjualan.adapter.getItem(Integer.parseInt(sisa_stock.listid.getText().toString())).setStock(sisa_stock.qtystock.getText().toString());
                product_penjualan.adapter.getItem(Integer.parseInt(sisa_stock.listid.getText().toString())).setDisplay(sisa_stock.qtydisplay.getText().toString());
                product_penjualan.adapter.getItem(Integer.parseInt(sisa_stock.listid.getText().toString())).setExpired(sisa_stock.qtyexpired.getText().toString());
                product_penjualan.adapter.getItem(Integer.parseInt(sisa_stock.listid.getText().toString())).setExpired_date(sisa_stock.tglorder.getText().toString());

                product_penjualan.adapter.getItem(Integer.parseInt(sisa_stock.listid.getText().toString())).setStock_qty(qtyorder.getText().toString());
                product_penjualan.adapter.getItem(Integer.parseInt(sisa_stock.listid.getText().toString())).setDisc_principle(disc_principal.getText().toString());
                product_penjualan.adapter.getItem(Integer.parseInt(sisa_stock.listid.getText().toString())).setDisc_distributor(disc_distributor.getText().toString());
                product_penjualan.adapter.getItem(Integer.parseInt(sisa_stock.listid.getText().toString())).setDisc_internal(disc_internal.getText().toString());
                product_penjualan.adapter.getItem(Integer.parseInt(sisa_stock.listid.getText().toString())).setDisc_total(String.valueOf(total));

                product_penjualan.listproductpenjualan.setAdapter(product_penjualan.adapter);
                product_penjualan.adapter.notifyDataSetChanged();

                Intent intent = new Intent(getApplicationContext(), product_penjualan.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            } else if (harga <= discount){
                disc_principal.setText("0");
                disc_distributor.setText("0");
                disc_internal.setText("0");
                new SweetAlertDialog(diskon.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Diskon Tidak Boleh Lebih Besar dari Total Harga")
                        .setConfirmText("OK")
                        .show();

            } else {
                int total = Integer.parseInt(qtyorder.getText().toString()) + Integer.parseInt(disc_principal.getText().toString()) + Integer.parseInt(disc_distributor.getText().toString()) + Integer.parseInt(disc_internal.getText().toString());

                product_penjualan.adapter.getItem(Integer.parseInt(sisa_stock.listid.getText().toString())).setStock(sisa_stock.qtystock.getText().toString());
                product_penjualan.adapter.getItem(Integer.parseInt(sisa_stock.listid.getText().toString())).setDisplay(sisa_stock.qtydisplay.getText().toString());
                product_penjualan.adapter.getItem(Integer.parseInt(sisa_stock.listid.getText().toString())).setExpired(sisa_stock.qtyexpired.getText().toString());
                product_penjualan.adapter.getItem(Integer.parseInt(sisa_stock.listid.getText().toString())).setExpired_date(sisa_stock.tglorder.getText().toString());

                product_penjualan.adapter.getItem(Integer.parseInt(sisa_stock.listid.getText().toString())).setStock_qty(qtyorder.getText().toString());
                product_penjualan.adapter.getItem(Integer.parseInt(sisa_stock.listid.getText().toString())).setDisc_principle(disc_principal.getText().toString());
                product_penjualan.adapter.getItem(Integer.parseInt(sisa_stock.listid.getText().toString())).setDisc_distributor(disc_distributor.getText().toString());
                product_penjualan.adapter.getItem(Integer.parseInt(sisa_stock.listid.getText().toString())).setDisc_internal(disc_internal.getText().toString());
                product_penjualan.adapter.getItem(Integer.parseInt(sisa_stock.listid.getText().toString())).setDisc_total(String.valueOf(total));

                product_penjualan.listproductpenjualan.setAdapter(product_penjualan.adapter);
                product_penjualan.adapter.notifyDataSetChanged();

                Intent intent = new Intent(getApplicationContext(), product_penjualan.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }

        });

        qtyorderadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                qtyorder.setText(String.valueOf(count));
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