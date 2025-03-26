package com.example.nike.Activity.User;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nike.Adapter.ColorAdapter;
import com.example.nike.Model.ProductColor;
import com.example.nike.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProductDetailActivity extends AppCompatActivity {
    private ImageView imgProduct;
    private TextView tvProductPrice;
    private RecyclerView recyclerViewColors;
    private Button btnSelectSize;
    private List<ProductColor> colorList;
    private ColorAdapter colorAdapter;
    private List<String> currentSizes = new ArrayList<>();
    private String selectedSize = "Select Size ▼";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        imgProduct = findViewById(R.id.imgProduct);
        tvProductPrice = findViewById(R.id.tvProductPrice);
        recyclerViewColors = findViewById(R.id.recycler_view_colors);
        btnSelectSize = findViewById(R.id.btnSelectSize);

        // Dữ liệu sản phẩm theo màu
        colorList = new ArrayList<>();
        colorList.add(new ProductColor(R.drawable.nike_airjordan1, "Blue", "3.239.000 đ",
                Arrays.asList("Size 6", "Size 7", "Size 8", "Size 9")));
        colorList.add(new ProductColor(R.drawable.nike_airforce1, "White", "3.100.000 đ",
                Arrays.asList("Size 6", "Size 7", "Size 8")));
        colorList.add(new ProductColor(R.drawable.nike_blazer, "Cyan", "3.500.000 đ",
                Arrays.asList("Size 7", "Size 8", "Size 9", "Size 10")));

        // Thiết lập RecyclerView
        recyclerViewColors.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        colorAdapter = new ColorAdapter(this, colorList, color -> {
            imgProduct.setImageResource(color.getImageResource());
            tvProductPrice.setText(color.getPrice());
            currentSizes = color.getSizes();
            selectedSize = "Select Size ▼";
            btnSelectSize.setText(selectedSize);
        });
        recyclerViewColors.setAdapter(colorAdapter);

        // Xử lý chọn size
        btnSelectSize.setOnClickListener(v -> {
            if (currentSizes.isEmpty()) return;
            String[] sizesArray = currentSizes.toArray(new String[0]);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Select Size")
                    .setItems(sizesArray, (dialog, which) -> {
                        selectedSize = sizesArray[which];
                        btnSelectSize.setText(selectedSize);
                    })
                    .show();
        });
    }
}
