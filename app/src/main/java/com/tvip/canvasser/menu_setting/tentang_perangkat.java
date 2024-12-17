package com.tvip.canvasser.menu_setting;

import static com.tvip.canvasser.menu_splash.splash.getMacAddr;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import com.tvip.canvasser.BuildConfig;
import com.tvip.canvasser.R;

import java.util.Locale;

public class tentang_perangkat extends AppCompatActivity {
    TextView deviceid, version;
    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tentang_perangkat);
        deviceid = findViewById(R.id.deviceid);
        version = findViewById(R.id.version);

        String rest = getMacAddr();
        rest = rest.replace(":", "");
        rest = rest.replace(" ", "");

        deviceid.setText(rest.toUpperCase(Locale.ROOT));
        version.setText("Version " + BuildConfig.VERSION_NAME);


    }
}