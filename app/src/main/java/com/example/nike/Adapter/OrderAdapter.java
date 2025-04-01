package com.example.nike.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.nike.Model.CartItem;
import com.example.nike.R;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    private List<CartItem> cartItemList;

    public OrderAdapter(List<CartItem> cartItemList) {
        this.cartItemList = cartItemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order, parent, false); // Tạo layout mới
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartItem item = cartItemList.get(position);

        holder.productName.setText(item.getProductName());
        holder.size.setText("Size: " + item.getSize());
        holder.quantity.setText("Số lượng: " + item.getQuantity());
        holder.price.setText(String.format("%,.0f VNĐ", item.getPrice()));
        Glide.with(holder.itemView.getContext())
                .load(item.getImageUrl())
                .into(holder.imageView);

        // Không có nút tăng/giảm hoặc xóa
    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView productName, size, quantity, price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_order);
            productName = itemView.findViewById(R.id.name_order);
            size = itemView.findViewById(R.id.size_order);
            quantity = itemView.findViewById(R.id.quantity_order);
            price = itemView.findViewById(R.id.price_order);
        }
    }
}