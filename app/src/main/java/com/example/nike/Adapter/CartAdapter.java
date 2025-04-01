package com.example.nike.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.nike.Model.CartItem;
import com.example.nike.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private List<CartItem> cartItemList;
    private OnQuantityChangedListener quantityChangedListener;

    public CartAdapter(List<CartItem> cartItemList) {
        this.cartItemList = cartItemList;
    }

    // Interface để thông báo khi số lượng thay đổi
    public interface OnQuantityChangedListener {
        void onQuantityChanged();
    }

    public void setOnQuantityChangedListener(OnQuantityChangedListener listener) {
        this.quantityChangedListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartItem item = cartItemList.get(position);

        holder.productName.setText(item.getProductName());
        holder.size.setText("Size: " + item.getSize());
        holder.quantity.setText(String.valueOf(item.getQuantity()));
        holder.price.setText(String.format("%,.0f VNĐ", item.getPrice()));
        Glide.with(holder.itemView.getContext())
                .load(item.getImageUrl())
                .into(holder.imageView);

        // Tính giá cho từng item (price * quantity)
        double itemTotal = item.getPrice() * item.getQuantity();
        holder.itemTotal.setText(String.format("%,.0f VNĐ", itemTotal));

        // Xử lý tăng số lượng
        holder.btnIncrease.setOnClickListener(v -> {
            int newQuantity = item.getQuantity() + 1;
            updateQuantityInFirestore(item, newQuantity, position);
        });

        // Xử lý giảm số lượng
        holder.btnDecrease.setOnClickListener(v -> {
            int newQuantity = item.getQuantity() - 1;
            if (newQuantity >= 1) { // Không cho phép nhỏ hơn 1
                updateQuantityInFirestore(item, newQuantity, position);
            }
        });
    }

    private void updateQuantityInFirestore(CartItem item, int newQuantity, int position) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("cart")
                .document(item.getId())
                .update("quantity", newQuantity)
                .addOnSuccessListener(aVoid -> {
                    item.setQuantity(newQuantity);
                    notifyItemChanged(position);
                    if (quantityChangedListener != null) {
                        quantityChangedListener.onQuantityChanged();
                    }
                })
                .addOnFailureListener(e -> {
                    // Xử lý lỗi nếu cần
                });
    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView productName, size, quantity, price, itemTotal;
        ImageView btnIncrease, btnDecrease;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_cart);
            productName = itemView.findViewById(R.id.name_cart);
            size = itemView.findViewById(R.id.size_cart);
            quantity = itemView.findViewById(R.id.quantity_cart);
            price = itemView.findViewById(R.id.price_cart);
            itemTotal = itemView.findViewById(R.id.total_cart); // Thêm TextView cho tổng tiền từng item
            btnIncrease = itemView.findViewById(R.id.btn_increase);
            btnDecrease = itemView.findViewById(R.id.btn_decrease);
        }
    }
}