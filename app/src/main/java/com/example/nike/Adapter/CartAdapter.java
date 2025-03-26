package com.example.nike.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nike.Model.Product;
import com.example.nike.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.VHcart> {
    private List<Product> products;

    public CartAdapter(List<Product> products) {
        this.products = products;
    }

    @NonNull
    @Override
    public VHcart onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new VHcart(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VHcart holder, int position) {
        Product cartProduct = products.get(position);
        holder.imageView.setImageResource(cartProduct.getImageResIdNewProduct());
        holder.textView.setText(cartProduct.getNameNewProduct());
        holder.Quantity_cart.setText("1");

        holder.lin_quantity_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(holder.itemView.getContext());
                View bottomSheetView = LayoutInflater.from(holder.itemView.getContext())
                        .inflate(R.layout.bottom_sheet_quality, (ViewGroup) v.getParent(), false);

                ListView listView = bottomSheetView.findViewById(R.id.quantity_list);
                final String[] quantities = {"Remove","1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
                ArrayAdapter<String> adapter = new ArrayAdapter<>(holder.itemView.getContext(),
                        android.R.layout.simple_list_item_1, quantities);
                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String selectedQuantity = quantities[position];
                        if ("Remove".equals(selectedQuantity)) {

                            Toast.makeText(holder.itemView.getContext(), "Sản phẩm đã được xóa khỏi giỏ hàng", Toast.LENGTH_SHORT).show();
                        } else {
                            holder.Quantity_cart.setText(selectedQuantity);
                        }
                        bottomSheetDialog.dismiss();
                    }
                });
                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public static class VHcart extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView, Quantity_cart;
        LinearLayout lin_quantity_cart;

        public VHcart(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_cart);
            textView = itemView.findViewById(R.id.name_cart);
            Quantity_cart = itemView.findViewById(R.id.quantity_cart);
            lin_quantity_cart = itemView.findViewById(R.id.linear_quantity_cart);
        }
    }
}
