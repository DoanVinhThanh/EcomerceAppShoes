package com.example.nike.Activity.User;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nike.Adapter.ProductAdapter;
import com.example.nike.DatabaseHelper;
import com.example.nike.R;

public class SearchProductActivity extends AppCompatActivity {

    private RecyclerView recyclerViewProducts;
    private ProductAdapter productAdapter;
    private DatabaseHelper databaseHelper;
    private int categoryId;
    private String categoryName;
    private TextView txtCategoryTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_product);
        recyclerViewProducts = findViewById(R.id.recyclerView_search_products);
        txtCategoryTitle = findViewById(R.id.txtCategoryTitle);

        databaseHelper = new DatabaseHelper(this);

        // Lấy dữ liệu từ Intent
        Intent intent = getIntent();
        if (intent != null) {
            categoryId = intent.getIntExtra("category_id", -1);
            categoryName = intent.getStringExtra("category_name");
            txtCategoryTitle.setText("Danh mục: " + categoryName);
        }

        // Hiển thị danh sách sản phẩm theo danh mục
        //loadProductsByCategory(categoryId);
    }

//    private void loadProductsByCategory(int categoryId) {
//        List<ProductAdmin> productList = databaseHelper.getProductsByCategory(categoryId);
//        recyclerViewProducts.setLayoutManager(new GridLayoutManager(this, 2));
//        productAdapter = new ProductAdapter(productList);
//        recyclerViewProducts.setAdapter(productAdapter);
//    }
}
