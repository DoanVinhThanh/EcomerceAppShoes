package com.example.nike.Activity.User;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nike.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mAuth = FirebaseAuth.getInstance();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if (currentUser != null) {
                    // Nếu đã đăng nhập -> Chuyển sang MainActivity
                    startActivity(new Intent(SplashActivity.this, TrangChuActivity.class));
                } else {
                    // Nếu chưa đăng nhập -> Chuyển sang WaitActivity
                    startActivity(new Intent(SplashActivity.this, WaitActivity.class));
                }
                finish();
            }
        }, 3000);
    }
}
