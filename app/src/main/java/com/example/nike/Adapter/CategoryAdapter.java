package com.example.nike.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.nike.Activity.User.SearchProductActivity;
import com.example.nike.Model.AdminCategory;
import com.example.nike.R;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private Context context;
    private List<AdminCategory> categories;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(AdminCategory category);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public CategoryAdapter(Context context, List<AdminCategory> categories) {
        this.context = context;
        this.categories = categories;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AdminCategory category = categories.get(position);

        // Kiểm tra nếu có URL ảnh, sử dụng Glide để load ảnh
        if (category.getImageUrl() != null && !category.getImageUrl().isEmpty()) {
            Glide.with(context)
                    .load(category.getImageUrl())
                    .placeholder(R.drawable.ic_launcher_foreground) // Ảnh mặc định khi đang load
                    .error(R.drawable.ic_launcher_background) // Ảnh nếu load thất bại
                    .into(holder.imageView);
        } else {
            holder.imageView.setImageResource(R.drawable.nike_vaporfly_3_electric);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, SearchProductActivity.class);
            intent.putExtra("category_name", category.getName()); // Gửi tên danh mục sang màn hình tìm kiếm
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_category);
        }
    }
}
