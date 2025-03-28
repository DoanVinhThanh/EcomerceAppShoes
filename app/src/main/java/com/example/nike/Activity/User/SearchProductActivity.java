package com.example.nike.Activity.User;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nike.Adapter.ProductAdapter;
import com.example.nike.Model.Product;
import com.example.nike.Model.ProductAdmin;
import com.example.nike.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class SearchProductActivity extends AppCompatActivity {
    private RecyclerView recyclerViewProducts;
    private ProductAdapter productAdapter;
    private List<ProductAdmin> productList;
    private String categoryName;
    private TextView txtCategoryTitle;
    private FirebaseFirestore db;
    LinearLayout btnSearchToHome;
    private TextView txtNoProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_product);

        recyclerViewProducts = findViewById(R.id.recyclerView_search_products);
        txtCategoryTitle = findViewById(R.id.txtCategoryTitle);
        txtNoProducts = findViewById(R.id.txtNoProducts);
        btnSearchToHome = findViewById(R.id.btn_search_to_home);


        // Khởi tạo Firestore và danh sách sản phẩm
        db = FirebaseFirestore.getInstance();
        productList = new ArrayList<>();

        // Lấy dữ liệu từ Intent
        categoryName = getIntent().getStringExtra("category_name");
        if (categoryName != null) {
            txtCategoryTitle.setText("Tìm kiếm: " + categoryName);
        } else {
            txtCategoryTitle.setText("Tìm kiếm");
        }

        // Cấu hình RecyclerView với GridLayout (2 cột)
        recyclerViewProducts.setLayoutManager(new GridLayoutManager(this, 2));
        productAdapter = new ProductAdapter(this,productList);
        recyclerViewProducts.setAdapter(productAdapter);

        // Tải sản phẩm từ Firestore theo tên danh mục
        loadProductsByCategory(categoryName);
        btnSearchToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchProductActivity.this, TrangChuActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void loadProductsByCategory(String categoryName) {
        db.collection("products")
                .whereEqualTo("category", categoryName)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    productList.clear();
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        ProductAdmin product = document.toObject(ProductAdmin.class);
                        productList.add(product);
                    }

                    if (productList.isEmpty()) {
                        txtNoProducts.setVisibility(View.VISIBLE); // Hiển thị thông báo
                        recyclerViewProducts.setVisibility(View.GONE); // Ẩn RecyclerView
                    } else {
                        txtNoProducts.setVisibility(View.GONE); // Ẩn thông báo
                        recyclerViewProducts.setVisibility(View.VISIBLE); // Hiển thị danh sách
                        productAdapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Lỗi khi tải sản phẩm: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
