// Generated by view binder compiler. Do not edit!
package com.example.nike.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.nike.R;
import com.google.android.material.textfield.TextInputEditText;
import de.hdodenhof.circleimageview.CircleImageView;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityEditProfileBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final AppCompatButton btnSave;

  @NonNull
  public final TextInputEditText edtDob;

  @NonNull
  public final TextInputEditText edtName;

  @NonNull
  public final TextInputEditText edtPhone;

  @NonNull
  public final CircleImageView profileImage;

  @NonNull
  public final Toolbar toolbar;

  @NonNull
  public final TextView tvChangeImage;

  private ActivityEditProfileBinding(@NonNull LinearLayout rootView,
      @NonNull AppCompatButton btnSave, @NonNull TextInputEditText edtDob,
      @NonNull TextInputEditText edtName, @NonNull TextInputEditText edtPhone,
      @NonNull CircleImageView profileImage, @NonNull Toolbar toolbar,
      @NonNull TextView tvChangeImage) {
    this.rootView = rootView;
    this.btnSave = btnSave;
    this.edtDob = edtDob;
    this.edtName = edtName;
    this.edtPhone = edtPhone;
    this.profileImage = profileImage;
    this.toolbar = toolbar;
    this.tvChangeImage = tvChangeImage;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityEditProfileBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityEditProfileBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_edit_profile, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityEditProfileBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.btn_save;
      AppCompatButton btnSave = ViewBindings.findChildViewById(rootView, id);
      if (btnSave == null) {
        break missingId;
      }

      id = R.id.edt_dob;
      TextInputEditText edtDob = ViewBindings.findChildViewById(rootView, id);
      if (edtDob == null) {
        break missingId;
      }

      id = R.id.edt_name;
      TextInputEditText edtName = ViewBindings.findChildViewById(rootView, id);
      if (edtName == null) {
        break missingId;
      }

      id = R.id.edt_phone;
      TextInputEditText edtPhone = ViewBindings.findChildViewById(rootView, id);
      if (edtPhone == null) {
        break missingId;
      }

      id = R.id.profile_image;
      CircleImageView profileImage = ViewBindings.findChildViewById(rootView, id);
      if (profileImage == null) {
        break missingId;
      }

      id = R.id.toolbar;
      Toolbar toolbar = ViewBindings.findChildViewById(rootView, id);
      if (toolbar == null) {
        break missingId;
      }

      id = R.id.tv_change_image;
      TextView tvChangeImage = ViewBindings.findChildViewById(rootView, id);
      if (tvChangeImage == null) {
        break missingId;
      }

      return new ActivityEditProfileBinding((LinearLayout) rootView, btnSave, edtDob, edtName,
          edtPhone, profileImage, toolbar, tvChangeImage);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
