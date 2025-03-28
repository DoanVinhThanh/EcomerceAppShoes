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

public final class ItemCartBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final TextView colorCart;

  @NonNull
  public final ImageView imageCart;

  @NonNull
  public final LinearLayout linearQuantityCart;

  @NonNull
  public final TextView nameCart;

  @NonNull
  public final TextView quantityCart;

  @NonNull
  public final TextView sexCart;

  @NonNull
  public final TextView sizeCart;

  private ItemCartBinding(@NonNull LinearLayout rootView, @NonNull TextView colorCart,
      @NonNull ImageView imageCart, @NonNull LinearLayout linearQuantityCart,
      @NonNull TextView nameCart, @NonNull TextView quantityCart, @NonNull TextView sexCart,
      @NonNull TextView sizeCart) {
    this.rootView = rootView;
    this.colorCart = colorCart;
    this.imageCart = imageCart;
    this.linearQuantityCart = linearQuantityCart;
    this.nameCart = nameCart;
    this.quantityCart = quantityCart;
    this.sexCart = sexCart;
    this.sizeCart = sizeCart;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ItemCartBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ItemCartBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.item_cart, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ItemCartBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.color_cart;
      TextView colorCart = ViewBindings.findChildViewById(rootView, id);
      if (colorCart == null) {
        break missingId;
      }

      id = R.id.image_cart;
      ImageView imageCart = ViewBindings.findChildViewById(rootView, id);
      if (imageCart == null) {
        break missingId;
      }

      id = R.id.linear_quantity_cart;
      LinearLayout linearQuantityCart = ViewBindings.findChildViewById(rootView, id);
      if (linearQuantityCart == null) {
        break missingId;
      }

      id = R.id.name_cart;
      TextView nameCart = ViewBindings.findChildViewById(rootView, id);
      if (nameCart == null) {
        break missingId;
      }

      id = R.id.quantity_cart;
      TextView quantityCart = ViewBindings.findChildViewById(rootView, id);
      if (quantityCart == null) {
        break missingId;
      }

      id = R.id.sex_cart;
      TextView sexCart = ViewBindings.findChildViewById(rootView, id);
      if (sexCart == null) {
        break missingId;
      }

      id = R.id.size_cart;
      TextView sizeCart = ViewBindings.findChildViewById(rootView, id);
      if (sizeCart == null) {
        break missingId;
      }

      return new ItemCartBinding((LinearLayout) rootView, colorCart, imageCart, linearQuantityCart,
          nameCart, quantityCart, sexCart, sizeCart);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
