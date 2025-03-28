package com.example.nike.Activity.User;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.bumptech.glide.Glide;
import com.example.nike.FirebaseHelper;
import com.example.nike.Model.ProductAdmin;
import com.example.nike.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ProductDetailActivity extends AppCompatActivity {
    private ImageView imgProduct, btnBack;
    private TextView tvProductPrice, tvProductCategory, tvProductDescription,tvProductTitle;
    private AppCompatButton btnSelectSize;

    private List<String> currentSizes = new ArrayList<>();
    private String selectedSize = "Select Size ▼";

    private FirebaseHelper firebaseHelper;
    private String productId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        imgProduct = findViewById(R.id.imgProduct);
        tvProductPrice = findViewById(R.id.tvProductPrice);
        tvProductCategory = findViewById(R.id.tvProductCategory);
        tvProductDescription = findViewById(R.id.tvProductDescription);
        btnSelectSize = findViewById(R.id.btnSelectSize);
        btnBack = findViewById(R.id.btn_Back);
        tvProductTitle = findViewById(R.id.tvProductTitle);

        firebaseHelper = new FirebaseHelper();
        productId = getIntent().getStringExtra("productId");

        if (productId != null) {
            loadProductDetails(productId);
        }

        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(ProductDetailActivity.this, TrangChuActivity.class);
            startActivity(intent);
            finish();
        });

        btnSelectSize.setOnClickListener(v -> {
            if (currentSizes.isEmpty()) return;
            String[] sizesArray = currentSizes.toArray(new String[0]);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Select Size")
                    .setItems(sizesArray, (dialog, which) -> {
                        selectedSize = sizesArray[which];
                        btnSelectSize.setText(selectedSize);
                    })
                    .show();
        });
    }

    private void loadProductDetails(String productId) {
        firebaseHelper.getProductById(productId, product -> {
            if (product != null) {
                Glide.with(this).load(product.getImageUrl()).into(imgProduct);
                tvProductTitle.setText(product.getCategory());
                DecimalFormat decimalFormat = new DecimalFormat("#,### VNĐ");
                tvProductPrice.setText(decimalFormat.format(product.getPrice()));
                tvProductCategory.setText(product.getName());
                tvProductDescription.setText(product.getDescription());

                currentSizes = product.getSizes(); // Lưu sizes cho sản phẩm
            }
        });
    }
}
