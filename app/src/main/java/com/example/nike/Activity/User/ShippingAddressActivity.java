package com.example.nike.Activity.User;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nike.Model.ShippingAddress;
import com.example.nike.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShippingAddressActivity extends AppCompatActivity {

    private RecyclerView recyclerViewShipping;
    private AppCompatButton btnAddAddress;
    private ImageView btnBackToPayment; // Nút quay lại
    private ShippingAddressAdapter adapter;
    private List<ShippingAddress> addressList;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping_address);

        recyclerViewShipping = findViewById(R.id.recycler_view_shipping);
        btnAddAddress = findViewById(R.id.btn_add_address);
        btnBackToPayment = findViewById(R.id.btn_back_to_payment); // Ánh xạ nút quay lại

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        addressList = new ArrayList<>();

        recyclerViewShipping.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ShippingAddressAdapter(addressList);
        recyclerViewShipping.setAdapter(adapter);

        loadAddresses();

        btnAddAddress.setOnClickListener(v -> showAddAddressDialog(null));

        // Sự kiện nút quay lại
        btnBackToPayment.setOnClickListener(v -> {
            finish(); // Quay lại SuccessPaymentActivity
        });
    }

    private void loadAddresses() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) return;

        String userId = user.getUid();
        db.collection("shipping_addresses")
                .whereEqualTo("userId", userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        addressList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            ShippingAddress address = document.toObject(ShippingAddress.class);
                            address.setId(document.getId());
                            addressList.add(address);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    private void showAddAddressDialog(ShippingAddress addressToEdit) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_add_address, null);

        EditText etFullName = view.findViewById(R.id.et_full_name);
        EditText etPhoneNumber = view.findViewById(R.id.et_phone_number);
        EditText etAddress = view.findViewById(R.id.et_address);
        AppCompatButton btnSave = view.findViewById(R.id.btn_save);

        if (addressToEdit != null) {
            etFullName.setText(addressToEdit.getFullName());
            etPhoneNumber.setText(addressToEdit.getPhoneNumber());
            etAddress.setText(addressToEdit.getAddress());
        }

        builder.setView(view);
        AlertDialog dialog = builder.create();

        btnSave.setOnClickListener(v -> {
            String fullName = etFullName.getText().toString();
            String phoneNumber = etPhoneNumber.getText().toString();
            String address = etAddress.getText().toString();

            if (fullName.isEmpty() || phoneNumber.isEmpty() || address.isEmpty()) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            FirebaseUser user = mAuth.getCurrentUser();
            if (user != null) {
                Map<String, Object> addressData = new HashMap<>();
                addressData.put("userId", user.getUid());
                addressData.put("fullName", fullName);
                addressData.put("phoneNumber", phoneNumber);
                addressData.put("address", address);
                addressData.put("isSelected", false);

                if (addressToEdit == null) {
                    db.collection("shipping_addresses").add(addressData)
                            .addOnSuccessListener(doc -> loadAddresses());
                } else {
                    db.collection("shipping_addresses").document(addressToEdit.getId())
                            .set(addressData)
                            .addOnSuccessListener(aVoid -> loadAddresses());
                }
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    class ShippingAddressAdapter extends RecyclerView.Adapter<ShippingAddressAdapter.ViewHolder> {
        private List<ShippingAddress> addresses;

        public ShippingAddressAdapter(List<ShippingAddress> addresses) {
            this.addresses = addresses;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_shipping_address, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            ShippingAddress address = addresses.get(position);
            holder.tvFullName.setText(address.getFullName());
            holder.tvPhoneNumber.setText(address.getPhoneNumber());
            holder.tvAddress.setText(address.getAddress());
            holder.rbSelect.setChecked(address.isSelected());

            holder.rbSelect.setOnClickListener(v -> {
                for (ShippingAddress addr : addresses) {
                    addr.setSelected(false);
                    db.collection("shipping_addresses").document(addr.getId())
                            .update("isSelected", false);
                }
                address.setSelected(true);
                db.collection("shipping_addresses").document(address.getId())
                        .update("isSelected", true);
                notifyDataSetChanged();

                Intent resultIntent = new Intent();
                resultIntent.putExtra("selectedAddress", address);
                setResult(RESULT_OK, resultIntent);
            });

            holder.btnEdit.setOnClickListener(v -> showAddAddressDialog(address));
            holder.btnDelete.setOnClickListener(v -> {
                db.collection("shipping_addresses").document(address.getId())
                        .delete()
                        .addOnSuccessListener(aVoid -> loadAddresses());
            });
        }

        @Override
        public int getItemCount() {
            return addresses.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvFullName, tvPhoneNumber, tvAddress;
            RadioButton rbSelect;
            AppCompatButton btnEdit, btnDelete;

            ViewHolder(View itemView) {
                super(itemView);
                tvFullName = itemView.findViewById(R.id.tv_full_name);
                tvPhoneNumber = itemView.findViewById(R.id.tv_phone_number);
                tvAddress = itemView.findViewById(R.id.tv_address);
                rbSelect = itemView.findViewById(R.id.rb_select);
                btnEdit = itemView.findViewById(R.id.btn_edit);
                btnDelete = itemView.findViewById(R.id.btn_delete);
            }
        }
    }
}