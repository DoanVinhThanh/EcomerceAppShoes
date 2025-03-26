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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.nike.DatabaseHelper;
import com.example.nike.Model.AdminCategory;
import com.example.nike.Model.ProductAdmin;
import com.example.nike.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddEditProductActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private EditText etProductName, etProductPrice, etProductSize;
    private Spinner categorySpinner;
    private ImageView imgProduct;
    private Button btnSave;
    private List<AdminCategory> categoryList;
    private List<String> categoryNames;
    private int selectedCategoryId = -1;
    private DatabaseHelper dbHelper;
    private Uri imageUri;
    private int productId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_product);

        etProductName = findViewById(R.id.etProductName);
        etProductPrice = findViewById(R.id.etProductPrice);
        etProductSize = findViewById(R.id.etProductSize);
        categorySpinner = findViewById(R.id.CategoryProduct);
        imgProduct = findViewById(R.id.imgProduct);
        btnSave = findViewById(R.id.btnSave);
        dbHelper = new DatabaseHelper(this);

        loadCategoriesIntoSpinner();
        imgProduct.setOnClickListener(v -> openImagePicker());
        btnSave.setOnClickListener(v -> saveProduct());

        if (savedInstanceState != null) {
            imageUri = savedInstanceState.getParcelable("imageUri");
            if (imageUri != null) {
                Glide.with(this).load(imageUri).into(imgProduct);
            }
        }

        if (getIntent().hasExtra("productId")) {
            productId = getIntent().getIntExtra("productId", -1);
            loadProductData(productId);
        }
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.getData();
            if (imageUri != null) {
                Glide.with(this).load(imageUri).into(imgProduct);
            }
        }
    }

    private void loadCategoriesIntoSpinner() {
        categoryList = dbHelper.getAllCategories();
        categoryNames = new ArrayList<>();
        for (AdminCategory category : categoryList) {
            categoryNames.add(category.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoryNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategoryId = categoryList.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedCategoryId = -1;
            }
        });
    }

    private void saveProduct() {
        String name = etProductName.getText().toString().trim();
        String price = etProductPrice.getText().toString().trim();
        String sizeText = etProductSize.getText().toString().trim();

        if (name.isEmpty() || price.isEmpty() || sizeText.isEmpty() || selectedCategoryId == -1) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        List<String> sizes = Arrays.asList(sizeText.split(","));
        String imagePath = (imageUri != null) ? imageUri.toString() : (productId != -1 ? dbHelper.getProductById(productId).getImageUrlProduct() : "");

        ProductAdmin product = new ProductAdmin(productId, name, price, sizes, selectedCategoryId, imagePath);

        if (productId == -1) {
            dbHelper.addProduct(product);
            Toast.makeText(this, "Đã thêm sản phẩm", Toast.LENGTH_SHORT).show();
        } else {
            dbHelper.updateProduct(product);
            Toast.makeText(this, "Đã cập nhật sản phẩm", Toast.LENGTH_SHORT).show();
        }

        finish();
    }

    private void loadProductData(int productId) {
        ProductAdmin product = dbHelper.getProductById(productId);
        if (product != null) {
            etProductName.setText(product.getNameProduct());
            etProductPrice.setText(product.getPriceProduct());
            etProductSize.setText(String.join(",", product.getSizeProduct()));
            setCategorySpinnerSelection(product.getIdCategory());

            if (product.getImageUrlProduct() != null && !product.getImageUrlProduct().isEmpty()) {
                imageUri = Uri.parse(product.getImageUrlProduct());
                Glide.with(this).load(imageUri).into(imgProduct);
            }
        }
    }

    private void setCategorySpinnerSelection(int productCategoryId) {
        for (int i = 0; i < categoryList.size(); i++) {
            if (categoryList.get(i).getId() == productCategoryId) {
                categorySpinner.setSelection(i);
                break;
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("imageUri", imageUri);
    }
}