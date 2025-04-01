package com.example.nike.Activity.User;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nike.R;

public class SearchActivity extends AppCompatActivity {
    ImageView btn_back_home, btn_clear;
    AutoCompleteTextView searchEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Anhxa();
        searchEditText.requestFocus();

        // Hiển thị bàn phím ngay khi vào màn hình
        searchEditText.postDelayed(() -> {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(searchEditText, InputMethodManager.SHOW_IMPLICIT);
            }
        }, 200);

        searchEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String searchQuery = searchEditText.getText().toString().trim();
                if (!searchQuery.isEmpty()) {
                    startSearch(searchQuery);
                }
                return true; // Ngăn xuống dòng
            }
            return false;
        });


        btn_back_home.setOnClickListener(v -> {
            Intent intent = new Intent(SearchActivity.this, TrangChuActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finishAffinity();
        });

        btn_clear.setOnClickListener(v -> searchEditText.setText(""));

        // Danh sách các đề xuất
        String[] suggestions = {"Nike Air Force 1", "Nike Air Zoom", "Nike Dunk", "Nike Air", "Nike Jordan"};

        // Gán adapter cho AutoCompleteTextView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, suggestions);
        searchEditText.setAdapter(adapter);
    }

    private void Anhxa() {
        btn_back_home = findViewById(R.id.btn_back_shop_to_home);
        searchEditText = findViewById(R.id.textField_search);
        btn_clear = findViewById(R.id.clear_icon);
    }

    private void startSearch(String query) {
        Intent intent = new Intent(SearchActivity.this, SearchProductActivity.class);
        intent.putExtra("search_query", query);
        startActivity(intent);
    }
}
