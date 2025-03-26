package com.example.nike.Activity.User;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nike.R;

public class SuccessfulPaymentActivity extends AppCompatActivity {
    private Button btnThanhCong;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_payment);
        btnThanhCong = findViewById(R.id.btn_Thanh_Cong);
        btnThanhCong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SuccessfulPaymentActivity.this , TrangChuActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        });

    }
}