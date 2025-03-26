package com.example.nike.Activity.User;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nike.R;

public class SuccessPaymentActivity extends AppCompatActivity {
    private Button btnThanhToan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_order_payment);
        btnThanhToan = findViewById(R.id.btn_Thanh_Toan);
        btnThanhToan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SuccessPaymentActivity.this , SuccessfulPaymentActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        });

    }
}