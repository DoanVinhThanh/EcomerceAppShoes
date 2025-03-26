package com.example.nike.Activity.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nike.Adapter.ProductAdminAdapter;
import com.example.nike.DatabaseHelper;
import com.example.nike.Model.ProductAdmin;
import com.example.nike.R;

import java.util.List;

public class QuanLySanPhamActivity extends AppCompatActivity {
    private ImageView btnBackProductAdmin;
    private RecyclerView recyclerView;
    private ProductAdminAdapter adapter;
    private DatabaseHelper databaseHelper;
    private AppCompatButton btnAddProduct;
    private List<ProductAdmin> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_san_pham);

        // Ánh xạ View
        btnBackProductAdmin = findViewById(R.id.btn_back_product_admin);
        recyclerView = findViewById(R.id.recyclerView_product);
        btnAddProduct = findViewById(R.id.btnAddProduct);
        databaseHelper = new DatabaseHelper(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnBackProductAdmin.setOnClickListener(v -> {
            Intent intent = new Intent(QuanLySanPhamActivity.this, AdminTrangChuActivity.class);
            startActivity(intent);
            finish();
        });

        btnAddProduct.setOnClickListener(v ->
                startActivity(new Intent(QuanLySanPhamActivity.this, AddEditProductActivity.class))
        );
    }

    // Load sản phẩm từ SQLite
    private void loadProducts() {
        productList = databaseHelper.getAllProducts();
        adapter = new ProductAdminAdapter(productList, this);
        recyclerView.setAdapter(adapter);
    }

    public void deleteProduct(final int productId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xóa sản phẩm");
        builder.setMessage("Bạn có chắc chắn muốn xóa sản phẩm này?");
        builder.setPositiveButton("Có", (dialog, which) -> {
            databaseHelper.deleteProduct(productId);
            Toast.makeText(QuanLySanPhamActivity.this, "Đã xóa sản phẩm", Toast.LENGTH_SHORT).show();
            loadProducts();
        });
        builder.setNegativeButton("Không", null);
        builder.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProducts();
    }
}