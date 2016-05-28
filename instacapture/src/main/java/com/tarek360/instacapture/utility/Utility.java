package com.tarek360.instacapture.utility;

import android.content.Context;
import android.support.annotation.NonNull;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by tarek on 5/18/16.
 */
public class Utility {

  private static final String SCREENSHOTS_DIRECTORY_NAME = "screenshots";

  public static File getScreenshotFile(@NonNull final Context applicationContext) {

    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss.SS", Locale.getDefault());

    String fileName = "screenshot-" + dateFormat.format(new Date()) + ".jpg";

    final File screenshotsDir =
        new File(applicationContext.getFilesDir(), SCREENSHOTS_DIRECTORY_NAME);
    screenshotsDir.mkdirs();
    return new File(screenshotsDir, fileName);
  }
}
