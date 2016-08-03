package com.tarek360.sample.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import com.tarek360.sample.R;

/**
 * Created by tarek on 5/19/16.
 */
public class AlertDialogFragment extends DialogFragment {

  private OnAlertDialogListener mOnAlertDialogListener;

  public static AlertDialogFragment newInstance(@StringRes int titleResId, @StringRes int messageResId) {
    AlertDialogFragment alertDialog = new AlertDialogFragment();
    Bundle args = new Bundle();
    args.putInt("title", titleResId);
    args.putInt("message", messageResId);
    alertDialog.setArguments(args);
    return alertDialog;
  }

  @NonNull
  @Override public Dialog onCreateDialog(Bundle savedInstanceState) {

    return new AlertDialog.Builder(getActivity()).setTitle(getArguments().getInt("title"))
        .setMessage(getArguments().getInt("message"))
        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
          @Override public void onClick(DialogInterface dialog, int which) {
            if (mOnAlertDialogListener != null) {
              mOnAlertDialogListener.OnPositiveButtonClick();
            }
          }
        })
        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
          @Override public void onClick(DialogInterface dialog, int which) {
          }
        })
        .create();
  }

  @Override public void onAttach(Context context) {
    super.onAttach(context);
    if (context instanceof OnAlertDialogListener) {
      mOnAlertDialogListener = (OnAlertDialogListener) context;
    } else {
      throw new ClassCastException(context.getClass().getSimpleName()
          + " must implement "
          + OnAlertDialogListener.class.getSimpleName());
    }
  }

  public interface OnAlertDialogListener {
    void OnPositiveButtonClick();
  }
}