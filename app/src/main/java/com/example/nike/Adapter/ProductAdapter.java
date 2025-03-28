package com.example.nike.Adapter;

import android.content.Context;
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
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private List<ProductAdmin> products;
    private Context context;
    private OnItemClickListener onItemClickListener;

    // Interface xử lý sự kiện click vào sản phẩm
    public interface OnItemClickListener {
        void onItemClick(ProductAdmin product);
    }

    // Setter để thiết lập sự kiện click
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public ProductAdapter(Context context, List<ProductAdmin> products) {
        this.context = context;
        this.products = products;
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

        // Load ảnh từ Firestore bằng Glide
        Glide.with(context).load(product.getImageUrl()).into(holder.productImage);

        holder.productName.setText(product.getName());

        // Format giá tiền

        DecimalFormat decimalFormat = new DecimalFormat("#,### VNĐ");
        holder.productPrice.setText(decimalFormat.format(product.getPrice()));


        // Khi nhấn vào sản phẩm -> Gọi sự kiện onItemClick
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
        ImageView productImage;
        TextView productName, productPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.image_product);
            productName = itemView.findViewById(R.id.name_product);
            productPrice = itemView.findViewById(R.id.price_product);
        }
    }
}
