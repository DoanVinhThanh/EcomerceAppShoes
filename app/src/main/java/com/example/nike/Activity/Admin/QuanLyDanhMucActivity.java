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
import com.example.nike.Adapter.CategoryAdminAdapter;
import com.example.nike.DatabaseHelper;
import com.example.nike.Model.AdminCategory;
import com.example.nike.R;

import java.util.List;

public class QuanLyDanhMucActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    ImageView btnBackCategoryAdmin;
    private RecyclerView recyclerView;
    private CategoryAdminAdapter adapter;
    private List<AdminCategory> categoryList;
    private DatabaseHelper databaseHelper;
    private Button btnAddCategory;
    private Uri imageUri; // Lưu URI hình ảnh khi chọn từ thư viện
    private ImageView imgPreview; // Cập nhật ImageView sau khi chọn ảnh

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_danh_muc);
        btnBackCategoryAdmin = findViewById(R.id.btn_back_category_admin);
        recyclerView = findViewById(R.id.recyclerView);
        btnAddCategory = findViewById(R.id.btnAddCategory);
        databaseHelper = new DatabaseHelper(this);
        btnBackCategoryAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuanLyDanhMucActivity.this,AdminTrangChuActivity.class);
                startActivity(intent);
                finish();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadCategories();

        btnAddCategory.setOnClickListener(v -> openCategoryDialog(null));
    }

    private void loadCategories() {
        categoryList = databaseHelper.getAllCategories();
        adapter = new CategoryAdminAdapter(categoryList, new CategoryAdminAdapter.OnCategoryClickListener() {
            @Override
            public void onEditClick(AdminCategory category) {
                openCategoryDialog(category);
            }

            @Override
            public void onDeleteClick(AdminCategory category) {
                confirmDeleteCategory(category);
            }
        });
        recyclerView.setAdapter(adapter);
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
            if (name.isEmpty() || imageUri == null) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (category == null) {
                databaseHelper.addCategory(name, imageUri.toString());
                Toast.makeText(this, "Thêm danh mục thành công!", Toast.LENGTH_SHORT).show();
            } else {
                databaseHelper.updateCategory(category.getId(), name, imageUri.toString());
                Toast.makeText(this, "Cập nhật danh mục thành công!", Toast.LENGTH_SHORT).show();
            }

            loadCategories(); // Cập nhật lại danh sách sau khi thêm/sửa
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.getData();
            if (imgPreview != null) {
                Glide.with(this).load(imageUri).into(imgPreview); // Hiển thị ảnh đã chọn
            }
        }
    }

    private void confirmDeleteCategory(AdminCategory category) {
        new AlertDialog.Builder(this)
                .setTitle("Xóa danh mục")
                .setMessage("Bạn có chắc muốn xóa danh mục này không?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    databaseHelper.deleteCategory(category.getId());
                    Toast.makeText(this, "Danh mục đã bị xóa!", Toast.LENGTH_SHORT).show();
                    loadCategories(); // Cập nhật lại danh sách sau khi xóa
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}
