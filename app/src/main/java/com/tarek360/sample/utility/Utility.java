package com.tarek360.sample.utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Log;

import com.tarek360.instacapture.utility.Logger;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by tarek on 10/4/16.
 */

public final class Utility {

    private static final String SCREENSHOTS_DIRECTORY_NAME = "screenshots";
    private static final String TAG = Utility.class.getSimpleName();
    private static final int JPEG_COMPRESSION_QUALITY = 75;

    private Utility() throws InstantiationException {
        throw new InstantiationException("This utility class is not created for instantiation");
    }

    private static File getScreenshotFile(@NonNull final Context applicationContext) {

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss.SS", Locale.getDefault());

        String fileName = "screenshot-" + dateFormat.format(new Date()) + ".jpg";

        final File screenshotsDir =
                new File(applicationContext.getFilesDir(), SCREENSHOTS_DIRECTORY_NAME);
        screenshotsDir.mkdirs();
        return new File(screenshotsDir, fileName);
    }

    private static File saveBitmapToFile(@NonNull final Context context,
                                         @NonNull final Bitmap bitmap) {

        OutputStream outputStream = null;
        File screenshotFile = getScreenshotFile(context);

        try {
            outputStream = new BufferedOutputStream(new FileOutputStream(screenshotFile));

            bitmap.compress(Bitmap.CompressFormat.JPEG, JPEG_COMPRESSION_QUALITY, outputStream);

            outputStream.flush();

            Log.d(TAG, "Screenshot saved to " + screenshotFile.getAbsolutePath());
        } catch (final IOException e) {
            Log.e(TAG, e.getMessage());
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (final IOException ignored) {
                    Log.e(TAG, "Failed to close OutputStream.");
                }
            }
        }
        return screenshotFile;
    }

    public static Observable<File> getScreenshotFileObservable(@NonNull final Context context,
                                                               @NonNull final Bitmap bitmap) {

        return Observable.create(new Observable.OnSubscribe<File>() {
            @Override
            public void call(final Subscriber<? super File> subscriber) {
                OutputStream outputStream = null;
                try {
                    File screenshotFile = Utility.getScreenshotFile(context);

                    outputStream = new BufferedOutputStream(new FileOutputStream(screenshotFile));

                    bitmap.compress(Bitmap.CompressFormat.JPEG, JPEG_COMPRESSION_QUALITY, outputStream);

                    outputStream.flush();
                    Log.i("zxzx", "2Thread.currentThread(): " + Thread.currentThread());

                    subscriber.onNext(screenshotFile);
                    subscriber.onCompleted();

                    Logger.INSTANCE.d("Screenshot saved to " + screenshotFile.getAbsolutePath());
                } catch (final IOException e) {
                    subscriber.onError(e);
                } finally {
                    if (outputStream != null) {
                        try {
                            outputStream.close();
                        } catch (final IOException ignored) {
                            Log.e(TAG, "Failed to close OutputStream.");
                        }
                    }
                }
            }
        }).subscribeOn(Schedulers.io());
    }
}