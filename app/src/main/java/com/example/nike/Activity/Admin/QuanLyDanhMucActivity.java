package com.example.nike.Activity.Admin;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.nike.Activity.User.TrangChuActivity;
import com.example.nike.Adapter.CategoryAdminAdapter;
import com.example.nike.Model.AdminCategory;
import com.example.nike.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.ArrayList;
import java.util.List;

public class QuanLyDanhMucActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private RecyclerView recyclerView;
    private CategoryAdminAdapter adapter;
    private List<AdminCategory> categoryList = new ArrayList<>();
    private Button btnAddCategory;
    private Uri imageUri;
    private ImageView imgPreview,btnBackCategoryAdmin;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference storageRef = FirebaseStorage.getInstance().getReference("category_images");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_danh_muc);

        btnAddCategory = findViewById(R.id.btnAddCategory);
        recyclerView = findViewById(R.id.recyclerView);
        btnBackCategoryAdmin = findViewById(R.id.btn_back_category_admin);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnAddCategory.setOnClickListener(v -> openCategoryDialog(null));
        btnBackCategoryAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuanLyDanhMucActivity.this, AdminTrangChuActivity.class);
                startActivity(intent);
                finish();
            }
        });
        loadCategories();
    }

    private void loadCategories() {
        db.collection("categories").get()
                .addOnSuccessListener(querySnapshot -> {
                    categoryList.clear();
                    for (var document : querySnapshot) {
                        categoryList.add(document.toObject(AdminCategory.class));
                    }
                    adapter = new CategoryAdminAdapter(categoryList, new CategoryAdminAdapter.OnCategoryClickListener() {
                        @Override
                        public void onEditClick(AdminCategory category) {
                            openCategoryDialog(category);
                        }

                        @Override
                        public void onDeleteClick(AdminCategory category) {
                            deleteCategory(category);
                        }
                    });
                    recyclerView.setAdapter(adapter);
                });
    }

    private void openCategoryDialog(AdminCategory category) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(category == null ? "Thêm Danh Mục" : "Sửa Danh Mục");

        View view = getLayoutInflater().inflate(R.layout.dialog_category, null);
        EditText edtName = view.findViewById(R.id.edtCategoryName);
        imgPreview = view.findViewById(R.id.imgPreview);
        Button btnChooseImage = view.findViewById(R.id.btnChooseImage);

        if (category != null) {
            edtName.setText(category.getName());
            imageUri = Uri.parse(category.getImageUrl());
            Glide.with(this).load(imageUri).into(imgPreview);
        }

        btnChooseImage.setOnClickListener(v -> openImagePicker());

        builder.setView(view);
        builder.setPositiveButton("Lưu", (dialog, which) -> {
            String name = edtName.getText().toString().trim();
            uploadImageToFirebase(imageUri, name);

        });

        builder.setNegativeButton("Hủy", null);
        builder.show();
    }

    private void uploadImageToFirebase(Uri imageUri, String categoryName) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference("category_images/" + System.currentTimeMillis() + ".jpg");
        if (imageUri == null) {
            Toast.makeText(this, "Vui lòng chọn ảnh!", Toast.LENGTH_SHORT).show();
            return;
        }

        storageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    String imageUrl = uri.toString();
                    saveCategoryToFirestore(categoryName, imageUrl);
                }))
                .addOnFailureListener(e -> Toast.makeText(this, "Lỗi tải ảnh lên!", Toast.LENGTH_SHORT).show());
    }

    private void saveCategoryToFirestore(String name, String imageUrl) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference categoriesRef = db.collection("categories");

        String categoryId = categoriesRef.document().getId();
        AdminCategory category = new AdminCategory(categoryId, name, imageUrl);

        categoriesRef.document(categoryId).set(category)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Thêm danh mục thành công!", Toast.LENGTH_SHORT).show();
                    loadCategories();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Lỗi khi thêm danh mục!", Toast.LENGTH_SHORT).show());
    }


    private void deleteCategory(AdminCategory category) {
        db.collection("categories").document(category.getId()).delete()
                .addOnSuccessListener(aVoid -> loadCategories());
    }
    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            imgPreview.setImageURI(imageUri);
        }
    }

}
