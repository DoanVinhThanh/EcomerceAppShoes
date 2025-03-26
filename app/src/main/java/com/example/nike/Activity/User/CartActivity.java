package com.example.nike.Activity.User;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nike.Adapter.CartAdapter;
import com.example.nike.Model.Product;
import com.example.nike.R;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView_cart;
    private TextView tvEmptyMessage_cart;
    private ImageView imgEmpty_cart;
    private AppCompatButton btnShopNowCart, btn_Check_out;

    LinearLayout Linear_total_cart, Linear_btn_bag_to_home;
    private CartAdapter adapter;
    private List<Product> cartShoesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        Anhxa();
        // Giả sử dữ liệu được lấy từ cơ sở dữ liệu hoặc một nguồn khác
        setDataRCVcart();

        showlistcart();

        btnShopNowCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartActivity.this , TrangChuActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        });
        Linear_btn_bag_to_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartActivity.this , TrangChuActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        });
        btn_Check_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartActivity.this , SuccessPaymentActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        });




    }
    private void Anhxa(){
        recyclerView_cart = findViewById(R.id.recycler_view_cart);
        tvEmptyMessage_cart = findViewById(R.id.empty_msg_cart);
        imgEmpty_cart = findViewById(R.id.empty_img_cart);
        btnShopNowCart = findViewById(R.id.btnShopnow_cart);
        Linear_total_cart = findViewById(R.id.total_cart);
        Linear_btn_bag_to_home = findViewById(R.id.btn_bag_to_home);
        btn_Check_out = findViewById(R.id.btn_check_out);

    }


    private void setDataRCVcart(){
        recyclerView_cart.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false ));

        cartShoesList = new ArrayList<>();
        cartShoesList.add(new Product(R.drawable.nike_vaporfly_3_electric, "Nike Vaporfly 3 Electric",4500000,"Men"));
        cartShoesList.add(new Product(R.drawable.nike_infinityrn_4_electric, "Nike Infinityrn 4 Electric",4500000,"Men"));
        cartShoesList.add(new Product(R.drawable.nike_invicible_3_electric, "Nike Invicible 3 Electric",4500000,"Men"));








        adapter = new CartAdapter(cartShoesList);
        recyclerView_cart.setAdapter(adapter);
    }
    private void showlistcart(){
        if (cartShoesList.isEmpty()) {
            tvEmptyMessage_cart.setVisibility(View.VISIBLE);
            imgEmpty_cart.setVisibility(View.VISIBLE);
            btnShopNowCart.setVisibility(View.VISIBLE);
            recyclerView_cart.setVisibility(View.GONE);
            Linear_total_cart.setVisibility(View.GONE);
            btn_Check_out.setVisibility(View.GONE);

        } else {
            tvEmptyMessage_cart.setVisibility(View.GONE);
            imgEmpty_cart.setVisibility(View.GONE);
            btnShopNowCart.setVisibility(View.GONE);
            recyclerView_cart.setVisibility(View.VISIBLE);
            Linear_total_cart.setVisibility(View.VISIBLE);
            btn_Check_out.setVisibility(View.VISIBLE);

            recyclerView_cart.setLayoutManager(new LinearLayoutManager(this));
            adapter = new CartAdapter(cartShoesList);
            recyclerView_cart.setAdapter(adapter);
        }
    }
}