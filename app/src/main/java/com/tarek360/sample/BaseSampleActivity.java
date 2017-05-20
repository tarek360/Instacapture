package com.tarek360.sample;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.tarek360.instacapture.Instacapture;
import com.tarek360.instacapture.listener.SimpleScreenCapturingListener;
import com.tarek360.sample.uncapturableViews.AlertDialogFragment;
import com.tarek360.sample.utility.Utility;

import java.io.File;

import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public abstract class BaseSampleActivity extends AppCompatActivity
        implements AlertDialogFragment.OnAlertDialogListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Instacapture.enableLogging(true);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }

    protected void showAlertDialog() {
        AlertDialogFragment.newInstance(R.string.dialog_title, R.string.dialog_message)
                .show(getSupportFragmentManager(), "dialogFragment");
    }

    protected void captureScreenshot(@Nullable View... ignoredViews) {

        Instacapture.capture(this, new SimpleScreenCapturingListener() {
            @Override
            public void onCaptureComplete(Bitmap bitmap) {

                Utility.getScreenshotFileObservable(BaseSampleActivity.this, bitmap)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<File>() {
                            @Override
                            public void call(File file) {

                                startActivity(ShowScreenShotActivity.buildIntent(BaseSampleActivity.this,
                                        file.getAbsolutePath()));
                            }
                        });
            }
        }, ignoredViews);


        Instacapture.captureRx(this, ignoredViews).subscribe(new Action1<Bitmap>() {
            @Override
            public void call(Bitmap bitmap) {

            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void OnPositiveButtonClick() {
        captureScreenshot();
    }
}
