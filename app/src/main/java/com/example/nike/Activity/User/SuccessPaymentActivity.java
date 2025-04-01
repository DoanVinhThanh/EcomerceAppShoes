package com.example.nike.Activity.User;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nike.Adapter.OrderAdapter;
import com.example.nike.Model.CartItem;
import com.example.nike.Model.ShippingAddress;
import com.example.nike.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SuccessPaymentActivity extends AppCompatActivity {

    private RecyclerView recyclerViewOrder;
    private LinearLayout btnSettingShopToHome;
    private TextView tvSubtotalOrder, tvShippingOrder, tvDiscountOrder, tvEstimatedOrder;
    private TextView tvNameShip, tvPhoneShip, tvAddressShip;
    private AppCompatButton btnThanhToan, btnApplyDiscount, btnChangeAddress;
    private EditText edtDiscount;
    private RadioGroup rgPaymentMethod;
    private RadioButton rbPaypal, rbCod;
    private OrderAdapter adapter;
    private List<CartItem> cartItemList;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private double discountAmount = 0;
    private boolean isFreeShipping = false;
    private static final int REQUEST_CODE_ADDRESS = 1;
    private String selectedPaymentMethod = "";
    private ShippingAddress selectedAddress; // Lưu địa chỉ giao hàng được chọn

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_payment);

        Anhxa();

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        cartItemList = (ArrayList<CartItem>) getIntent().getSerializableExtra("cartItems");
        if (cartItemList == null) {
            cartItemList = new ArrayList<>();
        }

        recyclerViewOrder.setLayoutManager(new LinearLayoutManager(this));
        adapter = new OrderAdapter(cartItemList);
        recyclerViewOrder.setAdapter(adapter);

        updateOrderPrices();
        loadDefaultAddress();

        rgPaymentMethod.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rb_paypal) {
                selectedPaymentMethod = "Paypal";
            } else if (checkedId == R.id.rb_cod) {
                selectedPaymentMethod = "Thanh toán khi nhận hàng";
            }
        });

        btnSettingShopToHome.setOnClickListener(v -> {
            Intent intent = new Intent(SuccessPaymentActivity.this, TrangChuActivity.class);
            startActivity(intent);
            finishAffinity();
        });

        btnApplyDiscount.setOnClickListener(v -> applyDiscountCode());

        btnChangeAddress.setOnClickListener(v -> {
            Intent intent = new Intent(SuccessPaymentActivity.this, ShippingAddressActivity.class);
            startActivityForResult(intent, REQUEST_CODE_ADDRESS);
        });

        btnThanhToan.setOnClickListener(v -> {
            if (selectedPaymentMethod.isEmpty()) {
                Toast.makeText(this, "Vui lòng chọn phương thức thanh toán!", Toast.LENGTH_SHORT).show();
            } else if (selectedAddress == null) {
                Toast.makeText(this, "Vui lòng chọn địa chỉ giao hàng!", Toast.LENGTH_SHORT).show();
            } else {
                createOrder();

            }
        });
    }

    private void Anhxa() {
        recyclerViewOrder = findViewById(R.id.recycler_view_order);
        btnSettingShopToHome = findViewById(R.id.btn_setting_shop_to_home);
        tvSubtotalOrder = findViewById(R.id.suntotal_order);
        tvShippingOrder = findViewById(R.id.shipping_order);
        tvDiscountOrder = findViewById(R.id.discpunt_order);
        tvEstimatedOrder = findViewById(R.id.estimated_order);
        tvNameShip = findViewById(R.id.name_ship);
        tvPhoneShip = findViewById(R.id.phone_ship);
        tvAddressShip = findViewById(R.id.address_ship);
        btnThanhToan = findViewById(R.id.btn_Thanh_Toan);
        btnApplyDiscount = findViewById(R.id.btn_apply_discount);
        btnChangeAddress = findViewById(R.id.btn_change_address);
        edtDiscount = findViewById(R.id.edt_discount);
        rgPaymentMethod = findViewById(R.id.rg_payment_method);
        rbPaypal = findViewById(R.id.rb_paypal);
        rbCod = findViewById(R.id.rb_cod);
    }

    private void loadDefaultAddress() {
        db.collection("shipping_addresses")
                .whereEqualTo("isSelected", true)
                .limit(1)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            selectedAddress = document.toObject(ShippingAddress.class);
                            updateAddressUI(selectedAddress);
                            break;
                        }
                    }
                });
    }

    private void updateAddressUI(ShippingAddress address) {
        tvNameShip.setText(address.getFullName());
        tvPhoneShip.setText(address.getPhoneNumber());
        tvAddressShip.setText(address.getAddress());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADDRESS && resultCode == RESULT_OK && data != null) {
            selectedAddress = (ShippingAddress) data.getSerializableExtra("selectedAddress");
            if (selectedAddress != null) {
                updateAddressUI(selectedAddress);
            }
        }
    }

    private void applyDiscountCode() {
        String discountCode = edtDiscount.getText().toString().trim();
        if (discountCode.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập mã giảm giá", Toast.LENGTH_SHORT).show();
            return;
        }

        db.collection("discount_codes")
                .whereEqualTo("code", discountCode)
                .whereEqualTo("isActive", true)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String type = document.getString("type");
                            if ("percentage".equals(type)) {
                                long discountPercentage = document.getLong("discountPercentage");
                                double subtotal = calculateSubtotal();
                                discountAmount = subtotal * (discountPercentage / 100.0);
                                isFreeShipping = false;
                                Toast.makeText(this, "Áp dụng mã giảm giá " + discountPercentage + "% thành công!", Toast.LENGTH_SHORT).show();
                            } else if ("free_shipping".equals(type)) {
                                discountAmount = 0;
                                isFreeShipping = true;
                                Toast.makeText(this, "Áp dụng mã miễn phí vận chuyển thành công!", Toast.LENGTH_SHORT).show();
                            }
                            updateOrderPrices();
                        }
                    } else {
                        discountAmount = 0;
                        isFreeShipping = false;
                        Toast.makeText(this, "Mã giảm giá không hợp lệ hoặc đã hết hạn", Toast.LENGTH_SHORT).show();
                        updateOrderPrices();
                    }
                });
    }

    private double calculateSubtotal() {
        double subtotal = 0;
        for (CartItem item : cartItemList) {
            subtotal += item.getPrice() * item.getQuantity();
        }
        return subtotal;
    }

    private void updateOrderPrices() {
        double subtotal = calculateSubtotal();
        double shipping = isFreeShipping ? 0 : subtotal * 0.02;
        double estimatedTotal = subtotal + shipping - discountAmount;

        DecimalFormat decimalFormat = new DecimalFormat("#,### VNĐ");
        tvSubtotalOrder.setText(decimalFormat.format(subtotal));
        tvShippingOrder.setText(isFreeShipping ? "Miễn phí" : decimalFormat.format(shipping));
        tvDiscountOrder.setText(decimalFormat.format(discountAmount));
        tvEstimatedOrder.setText(decimalFormat.format(estimatedTotal));
    }

    private void createOrder() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "Vui lòng đăng nhập để đặt hàng!", Toast.LENGTH_SHORT).show();
            return;
        }

        double subtotal = calculateSubtotal();
        double shippingFee = isFreeShipping ? 0 : subtotal * 0.02;
        double total = subtotal + shippingFee - discountAmount;

        // Chuẩn bị dữ liệu đơn hàng
        Map<String, Object> orderData = new HashMap<>();
        orderData.put("userId", user.getUid());

        // Danh sách sản phẩm
        List<Map<String, Object>> items = new ArrayList<>();
        for (CartItem item : cartItemList) {
            Map<String, Object> itemData = new HashMap<>();
            itemData.put("productId", item.getProductId());
            itemData.put("productName", item.getProductName());
            itemData.put("price", item.getPrice());
            itemData.put("quantity", item.getQuantity());
            itemData.put("size", item.getSize());
            itemData.put("imageUrl", item.getImageUrl());
            items.add(itemData);
        }
        orderData.put("items", items);

        // Địa chỉ giao hàng
        Map<String, Object> shippingAddress = new HashMap<>();
        shippingAddress.put("fullName", selectedAddress.getFullName());
        shippingAddress.put("phoneNumber", selectedAddress.getPhoneNumber());
        shippingAddress.put("address", selectedAddress.getAddress());
        orderData.put("shippingAddress", shippingAddress);

        // Thông tin thanh toán
        orderData.put("subtotal", subtotal);
        orderData.put("shippingFee", shippingFee);
        orderData.put("discount", discountAmount);
        orderData.put("total", total);
        orderData.put("paymentMethod", selectedPaymentMethod);
        orderData.put("status", "Pending"); // Trạng thái ban đầu
        orderData.put("createdAt", FieldValue.serverTimestamp()); // Thời gian tạo

        // Lưu đơn hàng vào Firestore
        db.collection("orders")
                .add(orderData)
                .addOnSuccessListener(documentReference -> {
                    String orderId = documentReference.getId();
                    db.collection("orders").document(orderId)
                            .update("orderId", orderId); // Cập nhật orderId vào document

                    // Xóa giỏ hàng sau khi đặt hàng thành công
                    clearCart();

                    Toast.makeText(this, "Đặt hàng thành công! Mã đơn: " + orderId, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(SuccessPaymentActivity.this, SuccessfulPaymentActivity.class);
                    startActivity(intent);
                    finishAffinity();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Lỗi khi đặt hàng: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void clearCart() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            db.collection("cart")
                    .whereEqualTo("userId", user.getUid())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                document.getReference().delete();
                            }
                        }
                    });
        }
    }
}