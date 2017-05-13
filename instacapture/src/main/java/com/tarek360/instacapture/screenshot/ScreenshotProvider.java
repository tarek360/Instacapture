package com.tarek360.instacapture.screenshot;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.view.View;

import io.reactivex.Single;

/**
 * Created by tarek on 5/17/16.
 */
public class ScreenshotProvider {

    public Single<Bitmap> getScreenshotBitmap(@NonNull final Activity activity,
                                              final View[] removedViews) {
        return ViewsBitmapObservable.get(activity, removedViews);
    }
}
