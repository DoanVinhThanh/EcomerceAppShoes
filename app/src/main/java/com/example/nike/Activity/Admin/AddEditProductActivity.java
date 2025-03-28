package com.example.nike.Activity.Admin;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.nike.Model.ProductAdmin;
import com.example.nike.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddEditProductActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText etProductName, etProductPrice, etProductSize,etProductDescription;
    private Spinner categorySpinner;
    private ImageView imgProduct , btnAddToQLSM;
    private Button btnSave;
    private Uri imageUri;

    private FirebaseFirestore db;
    private StorageReference storageRef;

    private String selectedCategory;
    private String productId;
    private String existingImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_product);

        etProductName = findViewById(R.id.etProductName);
        etProductPrice = findViewById(R.id.etProductPrice);
        etProductSize = findViewById(R.id.etProductSize);
        etProductDescription = findViewById(R.id.etProductDescription);
        categorySpinner = findViewById(R.id.CategoryProduct);
        imgProduct = findViewById(R.id.imgProduct);
        btnSave = findViewById(R.id.btnSave);
        btnAddToQLSM = findViewById(R.id.btn_add_edit_product_to_QLSM);

        db = FirebaseFirestore.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference("product_images");

        setupCategorySpinner();

        imgProduct.setOnClickListener(v -> openFileChooser());
        btnSave.setOnClickListener(v -> saveProduct());

        productId = getIntent().getStringExtra("productId");
        if (productId != null) {
            loadProductDetails(productId);
        }
        btnAddToQLSM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddEditProductActivity.this,QuanLySanPhamActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void setupCategorySpinner() {
        db.collection("categories").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<String> categories = new ArrayList<>();
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        categories.add(document.getString("name"));
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    categorySpinner.setAdapter(adapter);

                    categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            selectedCategory = categories.get(position);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {}
                    });
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Lỗi tải danh mục!", Toast.LENGTH_SHORT).show());
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            Glide.with(this).load(imageUri).into(imgProduct);
        }
    }

    private void saveProduct() {
        if (imageUri != null) {
            uploadImageToFirebase();
        } else {
            saveProductToFirestore(existingImageUrl);
        }
    }

    private void uploadImageToFirebase() {
        String fileName = System.currentTimeMillis() + ".jpg";
        StorageReference fileRef = storageRef.child(fileName);

        fileRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl()
                        .addOnSuccessListener(uri -> saveProductToFirestore(uri.toString())))
                .addOnFailureListener(e -> Toast.makeText(this, "Lỗi tải ảnh!", Toast.LENGTH_SHORT).show());
    }

    private void saveProductToFirestore(String imageUrl) {
        String name = etProductName.getText().toString().trim();
        String price = etProductPrice.getText().toString().trim();
        String sizeInput = etProductSize.getText().toString().trim();
        String description = etProductDescription.getText().toString().trim(); // Lấy dữ liệu mô tả sản phẩm

        if (name.isEmpty() || price.isEmpty() || sizeInput.isEmpty() || description.isEmpty() || selectedCategory == null) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        List<String> sizes = Arrays.asList(sizeInput.split(","));
        ProductAdmin product = new ProductAdmin(productId, name, selectedCategory, Double.parseDouble(price), sizes, imageUrl, description);

        if (productId == null) {
            db.collection("products").add(product)
                    .addOnSuccessListener(documentReference -> {
                        String generatedId = documentReference.getId();
                        product.setId(generatedId);
                        db.collection("products").document(generatedId).set(product);
                        Toast.makeText(this, "Thêm sản phẩm thành công!", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Lỗi khi thêm sản phẩm!", Toast.LENGTH_SHORT).show());
        } else {
            product.setId(productId);
            db.collection("products").document(productId).set(product)
                    .addOnSuccessListener(aVoid -> Toast.makeText(this, "Cập nhật sản phẩm thành công!", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(this, "Lỗi khi cập nhật sản phẩm!", Toast.LENGTH_SHORT).show());
        }
    }

    // Cập nhật phương thức tải chi tiết sản phẩm
    private void loadProductDetails(String productId) {
        db.collection("products").document(productId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        ProductAdmin product = documentSnapshot.toObject(ProductAdmin.class);
                        if (product != null) {
                            etProductName.setText(product.getName());
                            etProductPrice.setText(String.valueOf(product.getPrice()));
                            etProductSize.setText(String.join(", ", product.getSizes()));
                            etProductDescription.setText(product.getDescription()); // Hiển thị mô tả sản phẩm
                            existingImageUrl = product.getImageUrl();
                            Glide.with(this).load(existingImageUrl).into(imgProduct);
                        }
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Lỗi tải sản phẩm!", Toast.LENGTH_SHORT).show());
    }
}
