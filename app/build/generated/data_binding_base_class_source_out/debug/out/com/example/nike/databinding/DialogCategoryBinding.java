// Generated by view binder compiler. Do not edit!
package com.example.nike.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.nike.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class DialogCategoryBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final Button btnChooseImage;

  @NonNull
  public final EditText edtCategoryName;

  @NonNull
  public final ImageView imgPreview;

  private DialogCategoryBinding(@NonNull LinearLayout rootView, @NonNull Button btnChooseImage,
      @NonNull EditText edtCategoryName, @NonNull ImageView imgPreview) {
    this.rootView = rootView;
    this.btnChooseImage = btnChooseImage;
    this.edtCategoryName = edtCategoryName;
    this.imgPreview = imgPreview;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static DialogCategoryBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static DialogCategoryBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.dialog_category, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static DialogCategoryBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.btnChooseImage;
      Button btnChooseImage = ViewBindings.findChildViewById(rootView, id);
      if (btnChooseImage == null) {
        break missingId;
      }

      id = R.id.edtCategoryName;
      EditText edtCategoryName = ViewBindings.findChildViewById(rootView, id);
      if (edtCategoryName == null) {
        break missingId;
      }

      id = R.id.imgPreview;
      ImageView imgPreview = ViewBindings.findChildViewById(rootView, id);
      if (imgPreview == null) {
        break missingId;
      }

      return new DialogCategoryBinding((LinearLayout) rootView, btnChooseImage, edtCategoryName,
          imgPreview);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
