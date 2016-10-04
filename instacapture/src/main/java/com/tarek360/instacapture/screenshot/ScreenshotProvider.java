package com.tarek360.instacapture.screenshot;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.view.View;
import rx.Observable;

/**
 * Created by tarek on 5/17/16.
 */
public interface ScreenshotProvider {

  Observable<Bitmap> getScreenshotBitmap(@NonNull final Activity activity, View[] removedViews);
}
