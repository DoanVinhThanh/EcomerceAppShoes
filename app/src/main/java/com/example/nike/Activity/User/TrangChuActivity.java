package com.example.nike.Activity.User;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.nike.Fragment.FavoriteFragment;
import com.example.nike.Fragment.HomeFragment;
import com.example.nike.Fragment.OrderFragment;
import com.example.nike.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class TrangChuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    Toolbar toolbar1;
    NavigationView navigationView;
    LinearLayout navHeader;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private ImageView profileImage;
    private TextView txtName, txtEmail, txtPhone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trang_chu);
        Anhxa();
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        loadUserInfo();
        //side menu
        setSupportActionBar(toolbar1);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this , drawerLayout, toolbar1, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }



    }
    private void loadUserInfo() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();

            db.collection("users").document(userId)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String name = document.getString("name");
                                String email = document.getString("email");
                                String phone = document.getString("phone");
                                String avatar = document.getString("avatar");

                                // Gán dữ liệu vào TextView
                                txtName.setText(name);
                                txtEmail.setText(email);
                                txtPhone.setText(phone);

                                // Load ảnh đại diện
                                if (avatar != null && !avatar.isEmpty()) {
                                    Glide.with(this)
                                            .load(avatar)
                                            .placeholder(R.drawable.avarta) // Ảnh mặc định nếu URL null
                                            .into(profileImage);
                                }
                            }
                        } else {
                            Log.e("Firestore", "Lỗi khi lấy dữ liệu: ", task.getException());
                        }
                    });
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        String title = "Trang chủ";


        if (id == R.id.nav_home) {
            replaceFragment(new HomeFragment());
            title = "Trang Chủ";
        } else if (id == R.id.nav_shop) {
            Intent intent = new Intent(TrangChuActivity.this, SearchActivity.class);
            startActivity(intent);
            title = "Mua Hàng";
        } else if (id == R.id.nav_order) {
            replaceFragment(new OrderFragment());
            title = "Đơn Hàng";
        } else if (id == R.id.nav_favorite) {
            replaceFragment(new FavoriteFragment());
            title = "Yêu Thích";

        } else if (id == R.id.nav_setting) {
            Intent intent = new Intent(TrangChuActivity.this, SettingActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_logout) {
            FirebaseAuth.getInstance().signOut();
            signOutUser();
            finishAffinity();
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void signOutUser() {
        Intent intent = new Intent(TrangChuActivity.this,SignInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }


    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container,fragment);
        fragmentTransaction.commit();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.search) {
            Intent intent = new Intent(TrangChuActivity.this , SearchActivity.class);
            startActivity(intent);
        }
        if (id == R.id.cart) {
            Intent intent = new Intent(TrangChuActivity.this , CartActivity.class);
            startActivity(intent);
        }

        return true;
    }

    public void Anhxa(){
        toolbar1 = findViewById(R.id.toolbar_layout);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        navHeader = navigationView.getHeaderView(0).findViewById(R.id.nav_header);
        profileImage = navHeader.findViewById(R.id.profile_image);
        txtName = (TextView) navHeader.getChildAt(1);
        txtEmail = (TextView) navHeader.getChildAt(2);
        txtPhone = (TextView) navHeader.getChildAt(3);

        navHeader.setOnClickListener(v -> {
            Intent intent = new Intent(TrangChuActivity.this, ProfileActivity.class);
            startActivity(intent);
            drawerLayout.closeDrawer(GravityCompat.START);
        });



    }
}