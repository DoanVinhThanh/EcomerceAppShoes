package com.example.nike.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.nike.Model.ProductAdmin;
import com.example.nike.R;

import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private List<ProductAdmin> products;
    private Context context;
    private OnItemClickListener onItemClickListener;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public interface OnItemClickListener {
        void onItemClick(ProductAdmin product);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public ProductAdapter(Context context, List<ProductAdmin> products) {
        this.context = context;
        this.products = products;
        sharedPreferences = context.getSharedPreferences("FavoritePrefs", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductAdmin product = products.get(position);

        // Load ảnh sản phẩm
        Glide.with(context).load(product.getImageUrl()).into(holder.productImage);
        holder.productName.setText(product.getName());

        // Format giá tiền
        DecimalFormat decimalFormat = new DecimalFormat("#,### VNĐ");
        holder.productPrice.setText(decimalFormat.format(product.getPrice()));

        // Kiểm tra xem sản phẩm có trong danh sách yêu thích không
        Set<String> favoriteSet = sharedPreferences.getStringSet("favorites", new HashSet<>());
        if (favoriteSet.contains(product.getId())) {
            holder.favoriteBtn.setImageResource(R.drawable.love); // Nếu sản phẩm yêu thích, tô màu đỏ
        } else {
            holder.favoriteBtn.setImageResource(R.drawable.heart); // Nếu chưa yêu thích, tô màu xám
        }

        // Xử lý sự kiện nhấn vào nút trái tim
        holder.favoriteBtn.setOnClickListener(v -> {
            Set<String> updatedFavorites = new HashSet<>(sharedPreferences.getStringSet("favorites", new HashSet<>()));

            if (updatedFavorites.contains(product.getId())) {
                updatedFavorites.remove(product.getId()); // Xóa khỏi yêu thích
                holder.favoriteBtn.setImageResource(R.drawable.heart);
            } else {
                updatedFavorites.add(product.getId()); // Thêm vào yêu thích
                holder.favoriteBtn.setImageResource(R.drawable.love);
            }

            // Lưu lại danh sách yêu thích
            editor.putStringSet("favorites", updatedFavorites);
            editor.apply();
        });

        // Sự kiện click vào sản phẩm
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(product);
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage, favoriteBtn;
        TextView productName, productPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.image_product);
            favoriteBtn = itemView.findViewById(R.id.btn_favorite);
            productName = itemView.findViewById(R.id.name_product);
            productPrice = itemView.findViewById(R.id.price_product);
        }
    }
}
