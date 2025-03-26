package com.example.nike.Activity.User;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nike.DatabaseHelper;
import com.example.nike.databinding.ActivityForgetPasswordBinding;

public class ForgetPasswordActivity extends AppCompatActivity {
    ActivityForgetPasswordBinding binding;
    DatabaseHelper databaseHelper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityForgetPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        databaseHelper=new DatabaseHelper(this);
        binding.btnForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String email = binding.forgotEmail.getText().toString();
                    String pass = binding.newPassword.getText().toString();
                    String repass = binding.confirmPassword.getText().toString();
                    if (email.equals("") || pass.equals("") || repass.equals("")) {
                        Toast.makeText(ForgetPasswordActivity.this, "vui lòng điền thông tin", Toast.LENGTH_SHORT).show();
                    } else {
                        if (pass.equals(repass)) {
                            int updatepass = databaseHelper.updatepass(email, pass);
                            if (updatepass == 1) {
                                binding.forgotEmail.setText("");
                                binding.confirmPassword.setText("");
                                Toast.makeText(ForgetPasswordActivity.this, "đồi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ForgetPasswordActivity.this, SignInActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(ForgetPasswordActivity.this, "tài khoản không tồn tại", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(ForgetPasswordActivity.this, "Mật khẩu không trùng khớp", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                catch (Exception e){
                    Toast.makeText(ForgetPasswordActivity.this, "Out of bound"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}