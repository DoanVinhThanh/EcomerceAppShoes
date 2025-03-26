package com.example.nike.Activity.User;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nike.DatabaseHelper;
import com.example.nike.databinding.ActivitySignUpBinding;

public class SignUpActivity extends AppCompatActivity {
    ActivitySignUpBinding binding;
    DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        databaseHelper=new DatabaseHelper(this);
        binding.etBirthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });
        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email= binding.signupEmail.getText().toString();
                String name = binding.signupName.getText().toString();
                String dob = binding.etBirthDate.getText().toString();
                String password = binding.signupPassword.getText().toString();
                String confirmPassword= binding.signupConfirm.getText().toString();

                if(name.equals("")||dob.equals("")||email.equals("")|| password.equals("")||confirmPassword.equals(""))
                    Toast.makeText(SignUpActivity.this, "All fields are mandatory", Toast.LENGTH_SHORT).show();
                else{
                    if (password.equals(confirmPassword)){
                        Boolean checkUserEmail=databaseHelper.checkEmail(email);
                        if(checkUserEmail==false){
                            Boolean insert = databaseHelper.insertData(email, name, dob, password); if(insert==true){
                                Toast.makeText(SignUpActivity.this, "Signup Successfully", Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(getApplicationContext(),SignInActivity.class);
                                startActivity(intent);
                            }else {
                                Toast.makeText(SignUpActivity.this, "Signup Failed", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(SignUpActivity.this, "User already exists, Please login", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(SignUpActivity.this, "Invalid password", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        binding.signupToSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),SignInActivity.class);
                startActivity(intent);
            }
        });
    }
    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                        String dob = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                        binding.etBirthDate.setText(dob);
                    }
                }, year, month, day);

        datePickerDialog.show();
    }
}
