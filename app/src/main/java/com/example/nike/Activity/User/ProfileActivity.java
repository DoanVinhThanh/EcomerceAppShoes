package com.example.nike.Activity.User;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.nike.Fragment.OrderFragment;
import com.example.nike.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileActivity extends AppCompatActivity {

    private TextView txtUsername;
    private ImageView profileImage,settingsProfile;
    private AppCompatButton editprofile;
    private FirebaseAuth mAuth;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        txtUsername = findViewById(R.id.txtUsername);
        profileImage = findViewById(R.id.profile_image);
        editprofile = findViewById(R.id.edit_profile);
        Toolbar toolbar = findViewById(R.id.back_home);
        settingsProfile = findViewById(R.id.settings_profile);



        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        loadUserProfile();

        editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this,EditProfile.class);
                startActivity(intent );
                finish();
            }
        });
        toolbar.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileActivity.this, TrangChuActivity.class);
            startActivity(intent);
            finish();
        });




        // Chuyển sang trang SettingsActivity khi nhấn "Settings"
        settingsProfile.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileActivity.this, SettingActivity.class);
            startActivity(intent);
            finish();
        });

    }
    @Override
    protected void onResume() {
        super.onResume();
        loadUserProfile();  // Gọi lại khi quay lại màn hình
    }

    private void loadUserProfile() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String uid = user.getUid();
            db.collection("users").document(uid)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String username = documentSnapshot.getString("name");
                            String imageUrl = documentSnapshot.getString("avatar");

                            txtUsername.setText(username != null ? username : "Tên chưa cập nhật");

                            if (imageUrl != null && !imageUrl.isEmpty()) {
                                Glide.with(ProfileActivity.this)
                                        .load(imageUrl)
                                        .placeholder(R.drawable.avarta) // Ảnh mặc định nếu null
                                        .into(profileImage);
                            } else {
                                profileImage.setImageResource(R.drawable.avarta);
                            }
                        }
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(ProfileActivity.this, "Lỗi tải dữ liệu!", Toast.LENGTH_SHORT).show());
        }
    }
}
