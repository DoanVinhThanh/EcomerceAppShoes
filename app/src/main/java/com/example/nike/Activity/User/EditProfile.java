package com.example.nike.Activity.User;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.nike.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfile extends AppCompatActivity {

    private TextInputEditText edtName, edtPhone, edtDob;
    private AppCompatButton btnSave;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private Calendar calendar;
    private static final int PICK_IMAGE_REQUEST = 1;
    private CircleImageView profileImage;
    private TextView tvChangeImage;
    private StorageReference storageReference;
    private Uri imageUri;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Khởi tạo Firestore và FirebaseAuth
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference("profile_images");
        calendar = Calendar.getInstance();

        // Ánh xạ View
        profileImage = findViewById(R.id.profile_image);
        tvChangeImage = findViewById(R.id.tv_change_image);
        edtName = findViewById(R.id.edt_name);
        edtPhone = findViewById(R.id.edt_phone);
        edtDob = findViewById(R.id.edt_dob);
        btnSave = findViewById(R.id.btn_save);
        Toolbar toolbar = findViewById(R.id.toolbar);

        toolbar.setOnClickListener(v -> {
            Intent intent = new Intent(EditProfile.this, ProfileActivity.class);
            startActivity(intent);
            finish();
        });


        // Cấu hình Toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());

        // Hiển thị ảnh và dữ liệu hiện tại
        loadUserProfile();

        // Xử lý sự kiện
        tvChangeImage.setOnClickListener(v -> openGallery());
        edtDob.setOnClickListener(v -> showDatePicker());
        btnSave.setOnClickListener(v -> saveUserData());

        // Kiểm tra và bật/tắt nút Lưu
        setupInputValidation();

    }

    private void setupInputValidation() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputFields();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        edtName.addTextChangedListener(textWatcher);
        edtPhone.addTextChangedListener(textWatcher);
        edtDob.addTextChangedListener(textWatcher);
    }

    private void checkInputFields() {
        String name = edtName.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String dob = edtDob.getText().toString().trim();

        btnSave.setEnabled(!name.isEmpty() && !phone.isEmpty() && !dob.isEmpty());
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Chọn ảnh đại diện"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            profileImage.setImageURI(imageUri);
        }
    }

    private void showDatePicker() {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePicker = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    calendar.set(selectedYear, selectedMonth, selectedDay);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    edtDob.setText(sdf.format(calendar.getTime()));
                },
                year, month, day
        );

        datePicker.show();
    }

    private void saveUserData() {
        if (user == null) {
            Toast.makeText(this, "Người dùng chưa đăng nhập", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = user.getUid();
        String name = edtName.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String dob = edtDob.getText().toString().trim();

        if (name.isEmpty() || phone.isEmpty() || dob.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Đang cập nhật...");
        progressDialog.show();

        if (imageUri != null) {
            StorageReference fileReference = storageReference.child(userId + ".jpg");
            fileReference.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> fileReference.getDownloadUrl()
                            .addOnSuccessListener(uri -> {
                                updateFirestoreData(userId, name, phone, dob, uri.toString());
                            }))
                    .addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        Toast.makeText(EditProfile.this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            updateFirestoreData(userId, name, phone, dob, null);
        }
    }

    private void updateFirestoreData(String userId, String name, String phone, String dob, String imageUrl) {
        DocumentReference userRef = db.collection("users").document(userId);
        Map<String, Object> userData = new HashMap<>();
        userData.put("name", name);
        userData.put("phone", phone);
        userData.put("dob", dob);
        if (imageUrl != null) userData.put("avatar", imageUrl);

        userRef.update(userData)
                .addOnSuccessListener(aVoid -> {
                    progressDialog.dismiss();
                    Toast.makeText(EditProfile.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();

                    // Chuyển về ProfileActivity
                    Intent intent = new Intent(EditProfile.this, ProfileActivity.class);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(EditProfile.this, "Lỗi: vui nhập đầy đủ thông tin  " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void loadUserProfile() {
        if (user == null) return;

        db.collection("users").document(user.getUid())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        if (documentSnapshot.contains("avatar")) {
                            String imageUrl = documentSnapshot.getString("avatar");
                            if (imageUrl != null) {
                                Glide.with(this).load(imageUrl).into(profileImage);
                            }
                        }
                        edtName.setText(documentSnapshot.getString("name"));
                        edtPhone.setText(documentSnapshot.getString("phone"));
                        edtDob.setText(documentSnapshot.getString("dob"));
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Lỗi tải thông tin người dùng", Toast.LENGTH_SHORT).show());
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(EditProfile.this, ProfileActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
