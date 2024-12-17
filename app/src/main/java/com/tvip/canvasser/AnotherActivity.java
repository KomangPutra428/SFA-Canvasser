package com.tvip.canvasser;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class AnotherActivity extends AppCompatActivity {
    TextView error;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_another);

        error = (TextView) findViewById(R.id.error);

        error.setText(getIntent().getStringExtra("error"));
    }
}