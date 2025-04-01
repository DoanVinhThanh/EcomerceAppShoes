package com.example.nike.Activity.User;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nike.Adapter.CartAdapter;
import com.example.nike.Model.CartItem;
import com.example.nike.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView_cart;
    private AppCompatButton btnShopNowCart, btn_Check_out;
    private LinearLayout Linear_total_cart, Linear_btn_bag_to_home, LinearEmptyLayout;
    private TextView tvTotalPrice, tvShippingPrice, tvEstimatedTotal;
    private CartAdapter adapter;
    private List<CartItem> cartItemList;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        Anhxa();

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        cartItemList = new ArrayList<>();
        recyclerView_cart.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CartAdapter(cartItemList);
        recyclerView_cart.setAdapter(adapter);

        // Thêm ItemTouchHelper để xử lý swipe-to-delete
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false; // Không hỗ trợ di chuyển
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                CartItem itemToDelete = cartItemList.get(position);

                // Xóa item khỏi Firestore
                db.collection("cart")
                        .document(itemToDelete.getId())
                        .delete()
                        .addOnSuccessListener(aVoid -> {
                            cartItemList.remove(position);
                            adapter.notifyItemRemoved(position);
                            updateUI();
                            updateTotalPrices();
                            Toast.makeText(CartActivity.this, "Đã xóa sản phẩm khỏi giỏ hàng", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            adapter.notifyItemChanged(position); // Khôi phục nếu xóa thất bại
                            Toast.makeText(CartActivity.this, "Lỗi khi xóa: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView_cart);

        adapter.setOnQuantityChangedListener(this::updateTotalPrices);

        loadCartItems();

        btnShopNowCart.setOnClickListener(v -> {
            Intent intent = new Intent(CartActivity.this, TrangChuActivity.class);
            startActivity(intent);
            finishAffinity();
        });

        Linear_btn_bag_to_home.setOnClickListener(v -> {
            Intent intent = new Intent(CartActivity.this, TrangChuActivity.class);
            startActivity(intent);
            finishAffinity();
        });

        btn_Check_out.setOnClickListener(v -> {
            Intent intent = new Intent(CartActivity.this, SuccessPaymentActivity.class);
            intent.putExtra("cartItems", new ArrayList<>(cartItemList)); // Truyền danh sách
            startActivity(intent);
            finishAffinity();
        });
    }

    private void Anhxa() {
        recyclerView_cart = findViewById(R.id.recycler_view_cart);
        LinearEmptyLayout = findViewById(R.id.lin_empty_layout);
        btnShopNowCart = findViewById(R.id.btnShopnow_cart);
        Linear_total_cart = findViewById(R.id.total_cart);
        Linear_btn_bag_to_home = findViewById(R.id.btn_bag_to_home);
        btn_Check_out = findViewById(R.id.btn_check_out);
        tvTotalPrice = findViewById(R.id.total_price);
        tvShippingPrice = findViewById(R.id.shipping_price);
        tvEstimatedTotal = findViewById(R.id.estimated_total);
    }

    private void loadCartItems() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "Vui lòng đăng nhập để xem giỏ hàng", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = currentUser.getUid();

        db.collection("cart")
                .whereEqualTo("userId", userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        cartItemList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            CartItem cartItem = new CartItem();
                            cartItem.setId(document.getId());
                            cartItem.setProductId(document.getString("productId"));
                            cartItem.setSize(document.getString("size"));
                            cartItem.setQuantity(document.getLong("quantity").intValue());
                            cartItem.setImageUrl(document.getString("imageUrl"));
                            cartItem.setProductName(document.getString("productName"));
                            cartItem.setPrice(document.getDouble("price"));

                            cartItemList.add(cartItem);
                        }
                        updateUI();
                        updateTotalPrices();
                    } else {
                        Toast.makeText(CartActivity.this,
                                "Lỗi khi tải giỏ hàng: " + task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateUI() {
        if (cartItemList.isEmpty()) {
            LinearEmptyLayout.setVisibility(View.VISIBLE);
            btnShopNowCart.setVisibility(View.VISIBLE);
            recyclerView_cart.setVisibility(View.GONE);
            Linear_total_cart.setVisibility(View.GONE);
            btn_Check_out.setVisibility(View.GONE);
        } else {
            LinearEmptyLayout.setVisibility(View.GONE);
            btnShopNowCart.setVisibility(View.GONE);
            recyclerView_cart.setVisibility(View.VISIBLE);
            Linear_total_cart.setVisibility(View.VISIBLE);
            btn_Check_out.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();
        }
    }

    private void updateTotalPrices() {
        double totalPrice = 0;
        for (CartItem item : cartItemList) {
            totalPrice += item.getPrice() * item.getQuantity();
        }

        double shippingPrice = totalPrice * 0.01; // 2% của totalPrice
        double estimatedTotal = totalPrice + shippingPrice;

        DecimalFormat decimalFormat = new DecimalFormat("#,### VNĐ");
        tvTotalPrice.setText(decimalFormat.format(totalPrice));
        tvShippingPrice.setText(decimalFormat.format(shippingPrice));
        tvEstimatedTotal.setText(decimalFormat.format(estimatedTotal));
    }
}