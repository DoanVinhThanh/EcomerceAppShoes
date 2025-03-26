package com.example.nike.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nike.Model.ProductColor;
import com.example.nike.R;

import java.util.List;

public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ViewHolder> {
    private Context context;
    private List<ProductColor> colorList;
    private OnColorSelectedListener listener;
    private int selectedPosition = -1; // Đánh dấu màu đang được chọn

    public interface OnColorSelectedListener {
        void onColorSelected(ProductColor color);
    }

    public ColorAdapter(Context context, List<ProductColor> colorList, OnColorSelectedListener listener) {
        this.context = context;
        this.colorList = colorList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_color, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductColor color = colorList.get(position);
        holder.imgColor.setImageResource(color.getImageResource());
        holder.tvColorName.setText(color.getColorName());

        // Kiểm tra nếu item này đang được chọn
        if (selectedPosition == holder.getAdapterPosition()) {
            holder.imgColor.setAlpha(1.0f); // Làm rõ hình ảnh
        } else {
            holder.imgColor.setAlpha(0.5f); // Làm mờ nếu chưa chọn
        }

        holder.itemView.setOnClickListener(v -> {
            int newPosition = holder.getAdapterPosition(); // Lấy vị trí mới nhất
            if (newPosition != RecyclerView.NO_POSITION) {
                selectedPosition = newPosition;
                listener.onColorSelected(colorList.get(newPosition));
                notifyDataSetChanged(); // Cập nhật giao diện
            }
        });
    }


    @Override
    public int getItemCount() {
        return colorList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgColor;
        TextView tvColorName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgColor = itemView.findViewById(R.id.imgColor);
            tvColorName = itemView.findViewById(R.id.tvColorName);
        }
    }
}


