package com.tarek360.instacapture.listener

import android.graphics.Bitmap

/**
 * Created by tarek on 5/17/16.
 */

/**
 * Listener for image loading process.<br></br>
 * You can use [SimpleScreenCapturingListener] for implementing only needed methods.

 * @see SimpleScreenCapturingListener
 */

interface ScreenCaptureListener {

    /**
     * Is called when screen capturing task was started
     */
    fun onCaptureStarted()

    /**
     * Is called when an error was occurred during screen capturing
     */
    fun onCaptureFailed(e: Throwable)

    /**
     * Is called when image is loaded successfully (and displayed in View if one was specified)

     * @param bitmap Captured screen bitmap
     */
    fun onCaptureComplete(bitmap: Bitmap)
}
