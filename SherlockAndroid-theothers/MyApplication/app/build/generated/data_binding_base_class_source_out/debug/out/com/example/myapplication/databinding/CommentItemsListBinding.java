// Generated by view binder compiler. Do not edit!
package com.example.myapplication.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.myapplication.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class CommentItemsListBinding implements ViewBinding {
  @NonNull
  private final RelativeLayout rootView;

  @NonNull
  public final TextView commentFavorPeople;

  @NonNull
  public final TextView commentUserContent;

  @NonNull
  public final TextView commentUserGrade;

  @NonNull
  public final TextView commentUserName;

  @NonNull
  public final ImageView commentUserPic;

  @NonNull
  public final ImageView contentvotePicture;

  private CommentItemsListBinding(@NonNull RelativeLayout rootView,
      @NonNull TextView commentFavorPeople, @NonNull TextView commentUserContent,
      @NonNull TextView commentUserGrade, @NonNull TextView commentUserName,
      @NonNull ImageView commentUserPic, @NonNull ImageView contentvotePicture) {
    this.rootView = rootView;
    this.commentFavorPeople = commentFavorPeople;
    this.commentUserContent = commentUserContent;
    this.commentUserGrade = commentUserGrade;
    this.commentUserName = commentUserName;
    this.commentUserPic = commentUserPic;
    this.contentvotePicture = contentvotePicture;
  }

  @Override
  @NonNull
  public RelativeLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static CommentItemsListBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static CommentItemsListBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.comment_items_list, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static CommentItemsListBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.commentFavorPeople;
      TextView commentFavorPeople = ViewBindings.findChildViewById(rootView, id);
      if (commentFavorPeople == null) {
        break missingId;
      }

      id = R.id.commentUserContent;
      TextView commentUserContent = ViewBindings.findChildViewById(rootView, id);
      if (commentUserContent == null) {
        break missingId;
      }

      id = R.id.commentUserGrade;
      TextView commentUserGrade = ViewBindings.findChildViewById(rootView, id);
      if (commentUserGrade == null) {
        break missingId;
      }

      id = R.id.commentUserName;
      TextView commentUserName = ViewBindings.findChildViewById(rootView, id);
      if (commentUserName == null) {
        break missingId;
      }

      id = R.id.commentUserPic;
      ImageView commentUserPic = ViewBindings.findChildViewById(rootView, id);
      if (commentUserPic == null) {
        break missingId;
      }

      id = R.id.contentvotePicture;
      ImageView contentvotePicture = ViewBindings.findChildViewById(rootView, id);
      if (contentvotePicture == null) {
        break missingId;
      }

      return new CommentItemsListBinding((RelativeLayout) rootView, commentFavorPeople,
          commentUserContent, commentUserGrade, commentUserName, commentUserPic,
          contentvotePicture);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
