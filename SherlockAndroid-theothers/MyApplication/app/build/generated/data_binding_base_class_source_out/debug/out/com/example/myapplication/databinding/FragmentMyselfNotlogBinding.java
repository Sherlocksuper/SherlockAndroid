// Generated by view binder compiler. Do not edit!
package com.example.myapplication.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public final class FragmentMyselfNotlogBinding implements ViewBinding {
  @NonNull
  private final RelativeLayout rootView;

  @NonNull
  public final TextView myNotlog;

  @NonNull
  public final TextView myselfNotlogged;

  private FragmentMyselfNotlogBinding(@NonNull RelativeLayout rootView, @NonNull TextView myNotlog,
      @NonNull TextView myselfNotlogged) {
    this.rootView = rootView;
    this.myNotlog = myNotlog;
    this.myselfNotlogged = myselfNotlogged;
  }

  @Override
  @NonNull
  public RelativeLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentMyselfNotlogBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentMyselfNotlogBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_myself_notlog, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentMyselfNotlogBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.my_notlog;
      TextView myNotlog = ViewBindings.findChildViewById(rootView, id);
      if (myNotlog == null) {
        break missingId;
      }

      id = R.id.myself_notlogged;
      TextView myselfNotlogged = ViewBindings.findChildViewById(rootView, id);
      if (myselfNotlogged == null) {
        break missingId;
      }

      return new FragmentMyselfNotlogBinding((RelativeLayout) rootView, myNotlog, myselfNotlogged);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
