// Generated by view binder compiler. Do not edit!
package com.example.nike.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.nike.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ItemAdminCategoryBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final ImageView btnDelete;

  @NonNull
  public final ImageView btnEdit;

  @NonNull
  public final ImageView imgCategory;

  @NonNull
  public final TextView tvCategoryName;

  private ItemAdminCategoryBinding(@NonNull LinearLayout rootView, @NonNull ImageView btnDelete,
      @NonNull ImageView btnEdit, @NonNull ImageView imgCategory,
      @NonNull TextView tvCategoryName) {
    this.rootView = rootView;
    this.btnDelete = btnDelete;
    this.btnEdit = btnEdit;
    this.imgCategory = imgCategory;
    this.tvCategoryName = tvCategoryName;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ItemAdminCategoryBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ItemAdminCategoryBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.item_admin_category, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ItemAdminCategoryBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.btnDelete;
      ImageView btnDelete = ViewBindings.findChildViewById(rootView, id);
      if (btnDelete == null) {
        break missingId;
      }

      id = R.id.btnEdit;
      ImageView btnEdit = ViewBindings.findChildViewById(rootView, id);
      if (btnEdit == null) {
        break missingId;
      }

      id = R.id.imgCategory;
      ImageView imgCategory = ViewBindings.findChildViewById(rootView, id);
      if (imgCategory == null) {
        break missingId;
      }

      id = R.id.tvCategoryName;
      TextView tvCategoryName = ViewBindings.findChildViewById(rootView, id);
      if (tvCategoryName == null) {
        break missingId;
      }

      return new ItemAdminCategoryBinding((LinearLayout) rootView, btnDelete, btnEdit, imgCategory,
          tvCategoryName);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
