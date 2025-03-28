package com.example.nike.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.nike.Activity.Admin.AddEditProductActivity;
import com.example.nike.DatabaseHelper;
import com.example.nike.Model.ProductAdmin;
import com.example.nike.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ProductAdminAdapter extends RecyclerView.Adapter<ProductAdminAdapter.ViewHolder> {
    private List<ProductAdmin> productList;
    private Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(ProductAdmin product);
        void onEditClick(ProductAdmin product);
        void onDeleteClick(ProductAdmin product);
    }

    public ProductAdminAdapter(List<ProductAdmin> productList, Context context, OnItemClickListener listener) {
        this.productList = productList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_admin_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductAdmin product = productList.get(position);

        holder.nameProduct.setText(product.getName());
        holder.priceProduct.setText(String.format("%.2f VNĐ", product.getPrice()));
        holder.sizeProduct.setText("Size: " + String.join(", ", product.getSizes()));
        holder.categoryProduct.setText("Danh mục: " + product.getCategory());

        Glide.with(context).load(product.getImageUrl()).into(holder.imgProduct);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, AddEditProductActivity.class);
            intent.putExtra("productId", product.getId());
            context.startActivity(intent);
        });

        holder.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, AddEditProductActivity.class);
            intent.putExtra("productId", product.getId());
            context.startActivity(intent);
        });

        // Hiển thị hộp thoại xác nhận trước khi xóa
        holder.btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Xóa sản phẩm")
                    .setMessage("Bạn có chắc muốn xóa sản phẩm này không?")
                    .setPositiveButton("Xóa", (dialog, which) -> {
                        FirebaseFirestore.getInstance().collection("products")
                                .document(product.getId())
                                .delete()
                                .addOnSuccessListener(aVoid -> {
                                    productList.remove(position);
                                    notifyItemRemoved(position);
                                    Toast.makeText(context, "Xóa sản phẩm thành công!", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e ->
                                        Toast.makeText(context, "Lỗi khi xóa sản phẩm!", Toast.LENGTH_SHORT).show()
                                );
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
        });
    }



    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameProduct, priceProduct, sizeProduct, categoryProduct;
        ImageView imgProduct, btnEdit, btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameProduct = itemView.findViewById(R.id.name_admin_product);
            priceProduct = itemView.findViewById(R.id.price_admin_product);
            sizeProduct = itemView.findViewById(R.id.size_admin_product);
            categoryProduct = itemView.findViewById(R.id.category_admin_product);
            imgProduct = itemView.findViewById(R.id.img_admin_product);
            btnEdit = itemView.findViewById(R.id.btn_edit_product);
            btnDelete = itemView.findViewById(R.id.btn_delete_product);
        }
    }
}

