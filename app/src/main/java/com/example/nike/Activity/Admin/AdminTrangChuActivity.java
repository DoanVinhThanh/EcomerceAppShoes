package com.example.nike.Activity.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nike.R;

public class AdminTrangChuActivity extends AppCompatActivity {
    LinearLayout btnQuanLySanPham,btnQuanLyDanhMuc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_trang_chu);
        Anhxa();
        btnQuanLySanPham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminTrangChuActivity.this,QuanLySanPhamActivity.class);
                startActivity(intent);
            }
        });
        btnQuanLyDanhMuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminTrangChuActivity.this,QuanLyDanhMucActivity.class);
                startActivity(intent);
            }
        });
    }

    private void Anhxa() {
        btnQuanLySanPham = findViewById(R.id.btn_quan_ly_san_pham);
        btnQuanLyDanhMuc = findViewById(R.id.btn_quan_ly_danh_muc);

    }
}