package com.tarek360.instacapture.screenshot;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.tarek360.instacapture.exception.IllegalScreenSizeException;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;

/**
 * Created by tarek on 5/17/16.
 */
public final class ViewsBitmapObservable {

    private ViewsBitmapObservable() {

    }

    @NonNull
    public static Single<Bitmap> get(@NonNull final Activity activity,
                                         @Nullable final View[] removedViews) {

        return Single.create(new SingleOnSubscribe<Bitmap>() {
            @Override
            public void subscribe(SingleEmitter<Bitmap> e) throws Exception {
                Bitmap screenBitmap = ScreenshotTaker.getScreenshotBitmap(activity, removedViews);
                if (screenBitmap != null) {
                    e.onSuccess(screenBitmap);
                } else {
                    e.onError(new IllegalScreenSizeException());
                }
            }
        });
    }
}
