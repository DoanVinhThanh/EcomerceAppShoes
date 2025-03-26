package com.example.nike.Activity.User;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nike.R;

public class SettingActivity extends AppCompatActivity {

    LinearLayout btn_setting_shop_to_home;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Anhxa();
        btn_setting_shop_to_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this, TrangChuActivity.class);
                startActivity(intent);
                finishAffinity();

            }
        });

    }
    private void Anhxa()
    {
        btn_setting_shop_to_home = findViewById(R.id.btn_setting_shop_to_home);
    }
}


