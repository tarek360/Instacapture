package com.tarek360.instacapture.screenshot;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.View;
import java.io.File;
import rx.Observable;

/**
 * Created by tarek on 5/17/16.
 */
public interface ScreenshotProvider {

  Observable<File> getScreenshotFile(@NonNull final Activity activity, final File file,
      View[] removedViews);
}
