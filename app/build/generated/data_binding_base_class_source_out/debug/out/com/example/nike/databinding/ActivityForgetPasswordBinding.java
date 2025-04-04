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
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.nike.R;
import com.google.android.material.textfield.TextInputEditText;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityForgetPasswordBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final AppCompatButton btnForget;

  @NonNull
  public final TextView btnForgotSignin;

  @NonNull
  public final TextView btnForgotSignup;

  @NonNull
  public final TextInputEditText forgotEmail;

  private ActivityForgetPasswordBinding(@NonNull LinearLayout rootView,
      @NonNull AppCompatButton btnForget, @NonNull TextView btnForgotSignin,
      @NonNull TextView btnForgotSignup, @NonNull TextInputEditText forgotEmail) {
    this.rootView = rootView;
    this.btnForget = btnForget;
    this.btnForgotSignin = btnForgotSignin;
    this.btnForgotSignup = btnForgotSignup;
    this.forgotEmail = forgotEmail;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityForgetPasswordBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityForgetPasswordBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_forget_password, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityForgetPasswordBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.btn_forget;
      AppCompatButton btnForget = ViewBindings.findChildViewById(rootView, id);
      if (btnForget == null) {
        break missingId;
      }

      id = R.id.btn_forgot_signin;
      TextView btnForgotSignin = ViewBindings.findChildViewById(rootView, id);
      if (btnForgotSignin == null) {
        break missingId;
      }

      id = R.id.btn_forgot_signup;
      TextView btnForgotSignup = ViewBindings.findChildViewById(rootView, id);
      if (btnForgotSignup == null) {
        break missingId;
      }

      id = R.id.forgot_email;
      TextInputEditText forgotEmail = ViewBindings.findChildViewById(rootView, id);
      if (forgotEmail == null) {
        break missingId;
      }

      return new ActivityForgetPasswordBinding((LinearLayout) rootView, btnForget, btnForgotSignin,
          btnForgotSignup, forgotEmail);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
