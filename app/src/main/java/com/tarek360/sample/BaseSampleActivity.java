package com.tarek360.sample;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import butterknife.ButterKnife;
import com.tarek360.instacapture.InstaCapture;
import com.tarek360.instacapture.InstaCaptureConfiguration;
import com.tarek360.instacapture.listener.SimpleScreenCapturingListener;
import com.tarek360.sample.dialog.AlertDialogFragment;
import com.tarek360.sample.utility.Utility;
import java.io.File;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public abstract class BaseSampleActivity extends AppCompatActivity
    implements AlertDialogFragment.OnAlertDialogListener {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Create new configuration and set Configuration to InstaCapture.
    final InstaCaptureConfiguration config =
        new InstaCaptureConfiguration.Builder().logging(true).build();
    InstaCapture.setConfiguration(config);
  }

  @Override public void setContentView(@LayoutRes int layoutResID) {
    super.setContentView(layoutResID);
    ButterKnife.bind(this);
  }

  protected void showAlertDialog() {
    AlertDialogFragment.newInstance(R.string.dialog_title, R.string.dialog_message)
        .show(getSupportFragmentManager(), "dialogFragment");
  }

  protected void captureScreenshot() {
    captureScreenshot(null);
  }

  protected void captureScreenshot(@Nullable View... views) {
    InstaCapture.getInstance(this)
        .capture(views)
        .setScreenCapturingListener(new SimpleScreenCapturingListener() {

          @Override public void onCaptureStarted() {
            super.onCaptureStarted();
          }

          @Override public void onCaptureComplete(Bitmap bitmap) {

            Utility.getScreenshotFileObservable(BaseSampleActivity.this, bitmap)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<File>() {
                  @Override public void call(File file) {

                    startActivity(ShowScreenShotActivity.buildIntent(BaseSampleActivity.this,
                        file.getAbsolutePath()));
                  }
                });
          }
        });
  }

  @Override protected void onStop() {
    super.onStop();
  }

  @Override public void OnPositiveButtonClick() {
    captureScreenshot();
  }
}
