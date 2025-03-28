package com.example.nike.Activity.Admin;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nike.Adapter.ProductAdminAdapter;
import com.example.nike.Model.ProductAdmin;
import com.example.nike.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class QuanLySanPhamActivity extends AppCompatActivity implements ProductAdminAdapter.OnItemClickListener {
    private ImageView btnBackProductAdmin;
    private RecyclerView recyclerView;
    private ProductAdminAdapter adapter;
    private AppCompatButton btnAddProduct;
    private FirebaseFirestore db;
    private List<ProductAdmin> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_san_pham);

        // Ánh xạ View
        btnBackProductAdmin = findViewById(R.id.btn_back_product_admin);
        recyclerView = findViewById(R.id.recyclerView_product);
        btnAddProduct = findViewById(R.id.btnAddProduct);
        db = FirebaseFirestore.getInstance();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productList = new ArrayList<>();
        adapter = new ProductAdminAdapter(productList, this, this);
        recyclerView.setAdapter(adapter);

        btnBackProductAdmin.setOnClickListener(v -> {
            startActivity(new Intent(QuanLySanPhamActivity.this, AdminTrangChuActivity.class));
            finish();
        });

        btnAddProduct.setOnClickListener(v -> {
            Intent intent = new Intent(QuanLySanPhamActivity.this, AddEditProductActivity.class);
            startActivity(intent);
        });

        loadProducts();
    }

    private void loadProducts() {
        CollectionReference productsRef = db.collection("products");
        productsRef.get().addOnSuccessListener(queryDocumentSnapshots -> {
            productList.clear();
            for (DocumentSnapshot document : queryDocumentSnapshots) {
                ProductAdmin product = document.toObject(ProductAdmin.class);
                product.setId(document.getId()); // Lưu ID Firestore
                productList.add(product);
            }
            adapter.notifyDataSetChanged();
        }).addOnFailureListener(e ->
                Toast.makeText(QuanLySanPhamActivity.this, "Lỗi tải sản phẩm!", Toast.LENGTH_SHORT).show()
        );
    }

    @Override
    public void onItemClick(ProductAdmin product) {
        Toast.makeText(this, "Nhấn vào sản phẩm: " + product.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEditClick(ProductAdmin product) {
        Intent intent = new Intent(this, AddEditProductActivity.class);
        intent.putExtra("productId", product.getId());
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(ProductAdmin product) {
        new AlertDialog.Builder(this)
                .setTitle("Xóa sản phẩm")
                .setMessage("Bạn có chắc muốn xóa sản phẩm này?")
                .setPositiveButton("Xóa", (dialog, which) -> deleteProduct(product))
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void deleteProduct(ProductAdmin product) {
        db.collection("products").document(product.getId())
                .delete()
                .addOnSuccessListener(aVoid -> {
                    productList.remove(product);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(this, "Xóa sản phẩm thành công!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Lỗi khi xóa sản phẩm!", Toast.LENGTH_SHORT).show());
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProducts();
    }
}

