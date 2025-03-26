package com.example.nike.Activity.User;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.nike.R;

public class WaitActivity extends AppCompatActivity {
    AppCompatButton btnWaitSignIn , btnWaitSignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait);

        btnWaitSignIn = findViewById(R.id.btn_wait_sign_in);
        btnWaitSignUp = findViewById(R.id.btn_wait_sign_up);

        btnWaitSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(WaitActivity.this,SignInActivity.class);
                startActivity(intent1);
                finish();
            }
        });
        btnWaitSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(WaitActivity.this,SignUpActivity.class);
                startActivity(intent2);
                finish();
            }
        });
    }
}