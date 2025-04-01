package com.example.nike.Activity.User;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.bumptech.glide.Glide;
import com.example.nike.FirebaseHelper;
import com.example.nike.Model.ProductAdmin;
import com.example.nike.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductDetailActivity extends AppCompatActivity {
    private ImageView imgProduct, btnBack;
    private TextView tvProductPrice, tvProductCategory, tvProductDescription,tvProductTitle;
    private AppCompatButton btnSelectSize,btnAddToCart;

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
        btnAddToCart = findViewById(R.id.btn_AddToCart);
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
        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Kiểm tra xem đã chọn size chưa
                if (selectedSize.equals("Select Size ▼")) {
                    Toast.makeText(ProductDetailActivity.this,
                            "Vui lòng chọn size trước khi thêm vào giỏ hàng",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                // Lấy user ID từ Firebase Authentication
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = mAuth.getCurrentUser();

                if (currentUser == null) {
                    Toast.makeText(ProductDetailActivity.this,
                            "Vui lòng đăng nhập để thêm sản phẩm vào giỏ hàng",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                String userId = currentUser.getUid();
                FirebaseFirestore db = FirebaseFirestore.getInstance();

                // Kiểm tra xem sản phẩm với size này đã có trong giỏ hàng chưa
                db.collection("cart")
                        .whereEqualTo("userId", userId)
                        .whereEqualTo("productId", productId)
                        .whereEqualTo("size", selectedSize)
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                QuerySnapshot querySnapshot = task.getResult();
                                if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                    // Sản phẩm đã tồn tại -> tăng quantity
                                    DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                                    long currentQuantity = document.getLong("quantity");
                                    String documentId = document.getId();

                                    db.collection("cart")
                                            .document(documentId)
                                            .update("quantity", currentQuantity + 1)
                                            .addOnSuccessListener(aVoid -> {
                                                Toast.makeText(ProductDetailActivity.this,
                                                        "Đã tăng số lượng sản phẩm trong giỏ hàng",
                                                        Toast.LENGTH_SHORT).show();
                                            })
                                            .addOnFailureListener(e -> {
                                                Toast.makeText(ProductDetailActivity.this,
                                                        "Lỗi khi cập nhật giỏ hàng: " + e.getMessage(),
                                                        Toast.LENGTH_SHORT).show();
                                            });
                                } else {
                                    // Sản phẩm chưa tồn tại -> thêm mới
                                    firebaseHelper.getProductById(productId, product -> {
                                        if (product != null) {
                                            Map<String, Object> cartItem = new HashMap<>();
                                            cartItem.put("userId", userId);
                                            cartItem.put("productId", productId);
                                            cartItem.put("size", selectedSize);
                                            cartItem.put("quantity", 1);
                                            cartItem.put("imageUrl", product.getImageUrl());
                                            cartItem.put("productName", product.getName());
                                            cartItem.put("price", product.getPrice());

                                            db.collection("cart")
                                                    .add(cartItem)
                                                    .addOnSuccessListener(documentReference -> {
                                                        Toast.makeText(ProductDetailActivity.this,
                                                                "Đã thêm sản phẩm vào giỏ hàng",
                                                                Toast.LENGTH_SHORT).show();
                                                    })
                                                    .addOnFailureListener(e -> {
                                                        Toast.makeText(ProductDetailActivity.this,
                                                                "Lỗi khi thêm vào giỏ hàng: " + e.getMessage(),
                                                                Toast.LENGTH_SHORT).show();
                                                    });
                                        }
                                    });
                                }
                            } else {
                                Toast.makeText(ProductDetailActivity.this,
                                        "Lỗi khi kiểm tra giỏ hàng: " + task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
            }
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
