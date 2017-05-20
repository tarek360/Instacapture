package com.tarek360.instacapture.listener

import android.graphics.Bitmap

/**
 * Created by tarek on 5/17/16.
 */

/**
 * A convenient class to extend when you only want to listen for a subset of all the screen
 * capturing
 * events. This implements all methods in the
 * [ScreenCaptureListener] but does nothing.
 */

open class SimpleScreenCapturingListener : ScreenCaptureListener {

    /**
     * Is called when screen capturing task was started
     */
    override fun onCaptureStarted() {
        // Empty implementation
    }

    /**
     * Is called when an error was occurred during screen capturing.
     */
    override fun onCaptureFailed(e: Throwable) {
        // Empty implementation
    }

    /**
     * Is called when screen  is captured successfully.

     * @param bitmap Captured screen bitmap
     */
    override fun onCaptureComplete(bitmap: Bitmap) {
        // Empty implementation
    }
}
