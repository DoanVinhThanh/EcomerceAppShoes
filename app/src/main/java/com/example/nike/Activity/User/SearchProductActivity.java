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
import com.example.nike.Model.ProductAdmin;
import com.example.nike.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class SearchProductActivity extends AppCompatActivity {
    private RecyclerView recyclerViewProducts;
    private ProductAdapter productAdapter;
    private List<ProductAdmin> productList;
    private String searchQuery;
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

        // Nhận từ khóa tìm kiếm từ Intent
        searchQuery = getIntent().getStringExtra("search_query");

        if (searchQuery != null && !searchQuery.isEmpty()) {
            txtCategoryTitle.setText("Kết quả tìm kiếm: " + searchQuery);
        } else {
            txtCategoryTitle.setText("Kết quả tìm kiếm");
        }

        // Cấu hình RecyclerView với GridLayout (2 cột)
        recyclerViewProducts.setLayoutManager(new GridLayoutManager(this, 2));
        productAdapter = new ProductAdapter(this, productList);
        recyclerViewProducts.setAdapter(productAdapter);

        // Tìm kiếm sản phẩm từ Firestore theo danh mục hoặc tên
        searchProducts(searchQuery);

        btnSearchToHome.setOnClickListener(v -> {
            Intent intent = new Intent(SearchProductActivity.this, TrangChuActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void searchProducts(String query) {
        Query firestoreQuery = db.collection("products");

        if (query != null && !query.isEmpty()) {
            firestoreQuery = firestoreQuery.whereGreaterThanOrEqualTo("name", query)
                    .whereLessThanOrEqualTo("name", query + "\uf8ff");
        }

        firestoreQuery.get().addOnSuccessListener(querySnapshot -> {
            productList.clear();
            for (QueryDocumentSnapshot document : querySnapshot) {
                ProductAdmin product = document.toObject(ProductAdmin.class);
                productList.add(product);
            }

            if (productList.isEmpty()) {
                txtNoProducts.setVisibility(View.VISIBLE);
                recyclerViewProducts.setVisibility(View.GONE);
            } else {
                txtNoProducts.setVisibility(View.GONE);
                recyclerViewProducts.setVisibility(View.VISIBLE);
                productAdapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Lỗi khi tải sản phẩm: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }
}
