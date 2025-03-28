package com.example.nike.Activity.User;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import com.example.nike.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswordActivity extends AppCompatActivity {
    private TextInputEditText forgotEmail;
    private AppCompatButton btnForget;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        forgotEmail = findViewById(R.id.forgot_email);
        btnForget = findViewById(R.id.btn_forget);
        mAuth = FirebaseAuth.getInstance();

        btnForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });
    }

    private void resetPassword() {
        String email = forgotEmail.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Vui lòng nhập email", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(ForgetPasswordActivity.this,
                                "Email đặt lại mật khẩu đã được gửi!",
                                Toast.LENGTH_LONG).show();
                        startActivity(new Intent(ForgetPasswordActivity.this, SignInActivity.class));
                        finish();
                    } else {
                        Toast.makeText(ForgetPasswordActivity.this,
                                "Lỗi! Vui lòng kiểm tra email",
                                Toast.LENGTH_LONG).show();
                    }
                });
    }
}
