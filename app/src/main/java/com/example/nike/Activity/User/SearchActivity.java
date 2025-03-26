package com.example.nike.Activity.User;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nike.R;

public class SearchActivity extends AppCompatActivity {
    ImageView btn_back_home,btn_clear;
    AutoCompleteTextView searchEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Anhxa();
        searchEditText.requestFocus();

        searchEditText.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(searchEditText, InputMethodManager.SHOW_IMPLICIT);
            }
        }, 200);

        btn_back_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchActivity.this, TrangChuActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                finishAffinity();

            }
        });


        // Danh sách các đề xuất
        String[] suggestions = {"Nike Air Force 1",
                                "Nike Air Zoom Pegasus 39",
                                "Nike Dunk Low",
                                "Nike Zoom Fly 5",
                                "Nike Dunk" ,
                                "Nike Air",
                                "Nike Zoom Mercurial Vapor 15"};

        // Sử dụng ArrayAdapter để hiển thị các đề xuất
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, suggestions);
        searchEditText.setAdapter(adapter);

        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchEditText.setText("");
            }
        });
    }

    private void Anhxa()
    {

        btn_back_home = findViewById(R.id.btn_back_shop_to_home);
        searchEditText = findViewById(R.id.textField_search);
        btn_clear = findViewById(R.id.clear_icon);

    }
}