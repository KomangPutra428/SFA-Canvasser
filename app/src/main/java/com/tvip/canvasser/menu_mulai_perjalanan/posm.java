package com.tvip.canvasser.menu_mulai_perjalanan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.tvip.canvasser.R;

public class posm extends AppCompatActivity {
    LinearLayout materi, cek;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posm);

        materi = findViewById(R.id.materi);
        cek = findViewById(R.id.cek);

        materi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent material = new Intent(getBaseContext(), materi_posm.class);
                startActivity(material);
            }
        });

        cek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cek = new Intent(getBaseContext(), cek_posm.class);
                startActivity(cek);
            }
        });

    }
}