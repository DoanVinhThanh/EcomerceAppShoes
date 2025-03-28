package com.example.nike.Adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.nike.Model.AdminCategory;
import com.example.nike.R;

import java.util.List;

public class CategoryAdminAdapter extends RecyclerView.Adapter<CategoryAdminAdapter.AdminCategoryViewHolder> {
    private List<AdminCategory> categories;
    private OnCategoryClickListener listener;

    public interface OnCategoryClickListener {
        void onEditClick(AdminCategory category);
        void onDeleteClick(AdminCategory category);
    }

    public CategoryAdminAdapter(List<AdminCategory> categories, OnCategoryClickListener listener) {
        this.categories = categories;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AdminCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin_category, parent, false);
        return new AdminCategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminCategoryViewHolder holder, int position) {
        AdminCategory category = categories.get(position);


        // Kiểm tra imageUrl trước khi load ảnh
        if (category.getImageUrl() != null && !category.getImageUrl().isEmpty()) {
            holder.tvName.setText(category.getName());
            Glide.with(holder.itemView.getContext())
                    .load(category.getImageUrl())
                    .into(holder.imgCategory);
        } else {
            // Nếu không có ảnh, đặt ảnh mặc định
            holder.imgCategory.setImageResource(R.drawable.ic_launcher_foreground);
        }

        holder.btnEdit.setOnClickListener(v -> listener.onEditClick(category));
        holder.btnDelete.setOnClickListener(v -> listener.onDeleteClick(category));
    }



    @Override
    public int getItemCount() {
        return categories.size();
    }

    public static class AdminCategoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        ImageView imgCategory, btnEdit, btnDelete;

        public AdminCategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvCategoryName);
            imgCategory = itemView.findViewById(R.id.imgCategory);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}