package com.tvip.canvasser.menu_mulai_perjalanan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.tvip.canvasser.R;

public class menu_info extends AppCompatActivity {
    LinearLayout info, updateoutlet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_info);
        info = findViewById(R.id.info);
        updateoutlet = findViewById(R.id.updateoutlet);

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), info_pelanggan.class);
                startActivity(intent);
            }
        });

        updateoutlet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), update_outlet.class);
                startActivity(intent);
            }
        });
    }
}