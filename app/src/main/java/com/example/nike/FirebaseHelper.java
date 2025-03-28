package com.example.nike;

import com.example.nike.Model.AdminCategory;
import com.example.nike.Model.ProductAdmin;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class FirebaseHelper {
    private final FirebaseFirestore db;

    public FirebaseHelper() {
        db = FirebaseFirestore.getInstance();
    }

    public interface CategoryCallback {
        void onCallback(List<AdminCategory> categories);
    }

    public interface ProductCallback {
        void onCallback(List<ProductAdmin> products);
    }
    public interface SingleProductCallback {
        void onCallback(ProductAdmin product);
    }

    // Lấy sản phẩm theo ID
    public void getProductById(String productId, SingleProductCallback callback) {
        db.collection("products").document(productId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        ProductAdmin product = documentSnapshot.toObject(ProductAdmin.class);
                        product.setId(documentSnapshot.getId()); // Gán ID Firestore cho sản phẩm
                        callback.onCallback(product);
                    } else {
                        callback.onCallback(null);
                    }
                })
                .addOnFailureListener(e -> callback.onCallback(null)); // Trả về null nếu lỗi
    }
    public void getCategories(CategoryCallback callback) {
        db.collection("categories").get()
                .addOnSuccessListener(querySnapshot -> {
                    List<AdminCategory> categoryList = new ArrayList<>();
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        AdminCategory category = document.toObject(AdminCategory.class);
                        categoryList.add(category);
                    }
                    callback.onCallback(categoryList);
                })
                .addOnFailureListener(e -> {
                    callback.onCallback(new ArrayList<>()); // Trả về danh sách rỗng nếu lỗi
                });
    }

    public void getProducts(ProductCallback callback) {
        db.collection("products").get()
                .addOnSuccessListener(querySnapshot -> {
                    List<ProductAdmin> productList = new ArrayList<>();
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        ProductAdmin product = document.toObject(ProductAdmin.class);
                        product.setId(document.getId()); // Lưu ID Firestore
                        productList.add(product);
                    }
                    callback.onCallback(productList);
                })
                .addOnFailureListener(e -> {
                    callback.onCallback(new ArrayList<>()); // Trả về danh sách rỗng nếu lỗi
                });
    }
}

