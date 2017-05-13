package com.tarek360.instacapture.listener;

import android.graphics.Bitmap;

/**
 * Created by tarek on 5/17/16.
 */

/**
 * Listener for image loading process.<br />
 * You can use {@link SimpleScreenCapturingListener} for implementing only needed methods.
 *
 * @see SimpleScreenCapturingListener
 */

public interface ScreenCaptureListener {

    /**
     * Is called when screen capturing task was started
     */
    void onCaptureStarted();

    /**
     * Is called when an error was occurred during screen capturing
     */
    void onCaptureFailed(Throwable e);

    /**
     * Is called when image is loaded successfully (and displayed in View if one was specified)
     *
     * @param bitmap Captured screen bitmap
     */
    void onCaptureComplete(Bitmap bitmap);
}
