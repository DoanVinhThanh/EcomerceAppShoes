package com.example.nike.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nike.Activity.User.ProductDetailActivity;
import com.example.nike.Activity.User.SearchProductActivity;
import com.example.nike.Adapter.CategoryAdapter;
import com.example.nike.Adapter.ProductAdapter;
import com.example.nike.FirebaseHelper;
import com.example.nike.Model.AdminCategory;
import com.example.nike.Model.Category;
import com.example.nike.Model.Product;
import com.example.nike.Model.ProductAdmin;
import com.example.nike.R;

import java.util.ArrayList;
import java.util.List;


public class MenFragment extends Fragment {
    private RecyclerView recyclerViewCategory;
    private CategoryAdapter categoryAdapter;
    private List<AdminCategory> categoryList;

    private RecyclerView recyclerViewNewProducts;
    private ProductAdapter newProductAdapter;
    private List<ProductAdmin> productList;
    private FirebaseHelper firebaseHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_men, container, false);
        Anhxa(view);

        recyclerViewCategory.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewCategory.setAdapter(categoryAdapter);

        recyclerViewNewProducts.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerViewNewProducts.setAdapter(newProductAdapter);

        categoryAdapter.setOnItemClickListener(category -> {
            Intent intent = new Intent(getContext(), SearchProductActivity.class);
            intent.putExtra("category_name", category.getName());
            startActivity(intent);
        });

        newProductAdapter.setOnItemClickListener(product -> {
            Intent intent = new Intent(getContext(), ProductDetailActivity.class);
            intent.putExtra("productId", product.getId());
            startActivity(intent);
        });

        loadCategoriesFromFirestore();
        loadProductsFromFirestore();

        return view;
    }

    private void loadCategoriesFromFirestore() {
        firebaseHelper.getCategories(categories -> {
            categoryList.clear();
            categoryList.addAll(categories);
            categoryAdapter.notifyDataSetChanged();
        });
    }

    private void loadProductsFromFirestore() {
        firebaseHelper.getProducts(products -> {
            productList.clear();
            productList.addAll(products);
            newProductAdapter.notifyDataSetChanged();
        });
    }

    private void Anhxa(View view) {
        recyclerViewCategory = view.findViewById(R.id.recyclerView_category);
        recyclerViewNewProducts = view.findViewById(R.id.recyclerViewNewProducts);
        firebaseHelper = new FirebaseHelper();

        categoryList = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(getContext(), categoryList);

        productList = new ArrayList<>();
        newProductAdapter = new ProductAdapter(getContext(), productList);
    }
}

