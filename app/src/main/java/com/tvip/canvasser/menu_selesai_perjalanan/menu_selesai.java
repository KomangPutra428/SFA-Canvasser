package com.tvip.canvasser.menu_selesai_perjalanan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.tvip.canvasser.R;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class menu_selesai extends AppCompatActivity {
    LinearLayout akhiriperjalanan, lhp;
    SharedPreferences sharedPreferences;
    SweetAlertDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_selesai);
        akhiriperjalanan = findViewById(R.id.akhiriperjalanan);
        lhp = findViewById(R.id.lhp);

        lhp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(menu_selesai.this, Summary_Transaksi_Lhp.class);
                startActivity(i);
            }
        });

//        akhiriperjalanan.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v)
//        });
    }
}