package com.example.nike.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nike.Adapter.ProductAdapter;
import com.example.nike.Model.ProductAdmin;
import com.example.nike.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FavoriteFragment extends Fragment {
    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<ProductAdmin> favoriteProducts;
    private FirebaseFirestore db;
    private SharedPreferences sharedPreferences;
    private ImageView emptyImg;
    private TextView emptyMsg;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recycler_favorite);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        emptyImg = view.findViewById(R.id.empty_img_favorite);
        emptyMsg = view.findViewById(R.id.empty_msg_favorite);

        db = FirebaseFirestore.getInstance();
        sharedPreferences = getContext().getSharedPreferences("FavoritePrefs", Context.MODE_PRIVATE);

        loadFavoriteProducts();
    }

    private void loadFavoriteProducts() {
        Set<String> favoriteSet = sharedPreferences.getStringSet("favorites", new HashSet<>());
        favoriteProducts = new ArrayList<>();

        db.collection("products").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    ProductAdmin product = document.toObject(ProductAdmin.class);
                    if (favoriteSet.contains(product.getId())) {
                        favoriteProducts.add(product);
                    }
                }

                // Ẩn/hiện giao diện rỗng
                if (favoriteProducts.isEmpty()) {
                    emptyImg.setVisibility(View.VISIBLE);
                    emptyMsg.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    emptyImg.setVisibility(View.GONE);
                    emptyMsg.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }

                productAdapter = new ProductAdapter(getContext(), favoriteProducts);
                recyclerView.setAdapter(productAdapter);
            }
        });
    }
}
