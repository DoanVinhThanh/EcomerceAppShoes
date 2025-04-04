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
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.nike.R;
import de.hdodenhof.circleimageview.CircleImageView;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityProfileBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final Toolbar backHome;

  @NonNull
  public final AppCompatButton editProfile;

  @NonNull
  public final CircleImageView profileImage;

  @NonNull
  public final ImageView settingsProfile;

  @NonNull
  public final TextView txtUsername;

  private ActivityProfileBinding(@NonNull LinearLayout rootView, @NonNull Toolbar backHome,
      @NonNull AppCompatButton editProfile, @NonNull CircleImageView profileImage,
      @NonNull ImageView settingsProfile, @NonNull TextView txtUsername) {
    this.rootView = rootView;
    this.backHome = backHome;
    this.editProfile = editProfile;
    this.profileImage = profileImage;
    this.settingsProfile = settingsProfile;
    this.txtUsername = txtUsername;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityProfileBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityProfileBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_profile, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityProfileBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.back_home;
      Toolbar backHome = ViewBindings.findChildViewById(rootView, id);
      if (backHome == null) {
        break missingId;
      }

      id = R.id.edit_profile;
      AppCompatButton editProfile = ViewBindings.findChildViewById(rootView, id);
      if (editProfile == null) {
        break missingId;
      }

      id = R.id.profile_image;
      CircleImageView profileImage = ViewBindings.findChildViewById(rootView, id);
      if (profileImage == null) {
        break missingId;
      }

      id = R.id.settings_profile;
      ImageView settingsProfile = ViewBindings.findChildViewById(rootView, id);
      if (settingsProfile == null) {
        break missingId;
      }

      id = R.id.txtUsername;
      TextView txtUsername = ViewBindings.findChildViewById(rootView, id);
      if (txtUsername == null) {
        break missingId;
      }

      return new ActivityProfileBinding((LinearLayout) rootView, backHome, editProfile,
          profileImage, settingsProfile, txtUsername);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
