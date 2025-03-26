package com.example.nike.Adapter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.nike.Activity.Admin.AddEditProductActivity;
import com.example.nike.Activity.Admin.QuanLySanPhamActivity;
import com.example.nike.DatabaseHelper;
import com.example.nike.Model.AdminCategory;
import com.example.nike.Model.ProductAdmin;
import com.example.nike.R;

import java.util.List;

public class ProductAdminAdapter extends RecyclerView.Adapter<ProductAdminAdapter.ViewHolder> {
    private List<ProductAdmin> productList;
    private Context context;
    private DatabaseHelper dbHelper;

    public ProductAdminAdapter(List<ProductAdmin> productList, Context context) {
        this.productList = productList;
        this.context = context;
        this.dbHelper = new DatabaseHelper(context);
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
        holder.nameProduct.setText(product.getNameProduct());
        holder.priceProduct.setText(product.getPriceProduct() + " VND");
        holder.sizeProduct.setText("Size: " + String.join(", ", product.getSizeProduct()));

        // Lấy tên danh mục từ DatabaseHelper
        AdminCategory category = dbHelper.getCategoryById(product.getIdCategory());
        holder.categoryProduct.setText(category != null ? "Danh mục: " + category.getName() : "Danh mục: Không xác định");

        // Kiểm tra quyền truy cập ảnh
        boolean hasPermission = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            hasPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_IMAGES)
                    == PackageManager.PERMISSION_GRANTED;
        } else {
            hasPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED;
        }

        // Hiển thị hình ảnh sản phẩm nếu có quyền
        if (hasPermission) {
            if (product.getImageUrlProduct() != null && !product.getImageUrlProduct().isEmpty()) {
                String imageUri = product.getImageUrlProduct();
                Log.d("Product Image URI", "Image URI: " + imageUri);
                if (imageUri.startsWith("http") || imageUri.startsWith("content") || imageUri.startsWith("file")) {
                    Glide.with(context)
                            .load(Uri.parse(imageUri))
                            .placeholder(R.drawable.ic_launcher_foreground)
                            .error(R.drawable.ic_launcher_background)
                            .into(holder.imgProduct);
                } else {
                    Log.e("Product Image", "Invalid URI: " + imageUri);
                    holder.imgProduct.setImageResource(R.drawable.ic_launcher_background);
                }
            } else {
                Log.e("Product Image", "Image URI is null or empty");
                holder.imgProduct.setImageResource(R.drawable.ic_launcher_background);
            }
        } else {
            Log.e("Permission", "Không có quyền truy cập ảnh!");
            holder.imgProduct.setImageResource(R.drawable.ic_launcher_background);
        }

        // Sự kiện khi nhấn nút sửa
        holder.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, AddEditProductActivity.class);
            intent.putExtra("productId", product.getIdProduct());
            context.startActivity(intent);
        });

        // Sự kiện khi nhấn nút xóa
        holder.btnDelete.setOnClickListener(v -> {
            if (context instanceof QuanLySanPhamActivity) {
                ((QuanLySanPhamActivity) context).deleteProduct(product.getIdProduct());
            }
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
