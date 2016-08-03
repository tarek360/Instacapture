package com.tarek360.sample;

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
import java.io.File;

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
    AlertDialogFragment mAlertDialogFragment = new AlertDialogFragment();
    mAlertDialogFragment.show(getSupportFragmentManager(), "dialogFragment");
  }

  protected void captureScreenshot() {
    captureScreenshot(null);
  }

  protected void captureScreenshot(@Nullable View[] views) {
    InstaCapture.getInstance(this)
        .capture(views)
        .setScreenCapturingListener(new SimpleScreenCapturingListener() {

          @Override public void onCaptureComplete(File file) {
            startActivity(ShowScreenShotActivity.buildIntent(BaseSampleActivity.this,
                file.getAbsolutePath()));
          }
        });
  }

  @Override protected void onStop() {
    super.onStop();
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    ButterKnife.unbind(this);
  }

  @Override public void OnPositiveButtonClick() {
    captureScreenshot();
  }
}
