package com.tarek360.instacapture.screenshot;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import com.tarek360.instacapture.utility.Logger;
import com.tarek360.instacapture.utility.Utility;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by tarek on 5/17/16.
 */
final class ScreenshotFileObservable {

  private static final int JPEG_COMPRESSION_QUALITY = 75;

  private ScreenshotFileObservable() {
  }

  static Observable<File> get(@NonNull Observable<Bitmap> screenshotBitmapObservable,
      @NonNull final Context context, final File file) {

    return screenshotBitmapObservable.observeOn(Schedulers.io())
        .switchMap(new Func1<Bitmap, Observable<File>>() {
          @Override public Observable<File> call(Bitmap bitmap) {

            File screenshotFile;

            OutputStream outputStream = null;
            Observable<File> observable;
            try {
              if (file == null) {
                screenshotFile = Utility.getScreenshotFile(context);
              } else {
                screenshotFile = file;
              }

              outputStream = new BufferedOutputStream(new FileOutputStream(screenshotFile));

              bitmap.compress(Bitmap.CompressFormat.JPEG, JPEG_COMPRESSION_QUALITY, outputStream);

              outputStream.flush();
              observable = Observable.just(screenshotFile);

              Logger.d("Screenshot saved to " + screenshotFile.getAbsolutePath());
            } catch (final IOException e) {
              observable = Observable.error(e);
            } finally {
              if (outputStream != null) {
                try {
                  outputStream.close();
                } catch (final IOException ignored) {
                  Logger.e("Failed to close OutputStream.");
                }
              }
            }
            return observable;
          }
        });
  }
}