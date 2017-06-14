package com.tarek360.instacapture

import android.app.Activity
import android.graphics.Bitmap
import android.view.View

import com.tarek360.instacapture.exception.ActivityNotRunningException
import com.tarek360.instacapture.listener.ScreenCaptureListener
import com.tarek360.instacapture.screenshot.ScreenshotProvider
import com.tarek360.instacapture.utility.Logger

import rx.Observable
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers

/**
 * Created by tarek on 5/20/17.
 */

object Instacapture {

    private val MESSAGE_IS_ACTIVITY_RUNNING = "Is your activity running?"
    private val ERROR_SCREENSHOT_CAPTURE_FAILED = "Screenshot capture failed"

    fun capture(activity: Activity,
                screenCaptureListener: ScreenCaptureListener,
                vararg ignoredViews: View) {

        screenCaptureListener.onCaptureStarted()

        captureRx(activity, *ignoredViews).subscribe(object : Subscriber<Bitmap>() {
            override fun onCompleted() {}

            override fun onError(e: Throwable) {
                Logger.e(ERROR_SCREENSHOT_CAPTURE_FAILED)
                Logger.printStackTrace(e)
                screenCaptureListener.onCaptureFailed(e)
            }

            override fun onNext(bitmap: Bitmap) {
                screenCaptureListener.onCaptureComplete(bitmap)
            }
        })

    }

    fun captureRx(activity: Activity,
                  vararg ignoredViews: View): Observable<Bitmap> {

        val activityReferenceManager = ActivityReferenceManager()
        activityReferenceManager.setActivity(activity)

        val validatedActivity = activityReferenceManager.validatedActivity ?:
                return Observable.error<Bitmap>(ActivityNotRunningException(MESSAGE_IS_ACTIVITY_RUNNING))

        val screenshotProvider = ScreenshotProvider()

        return screenshotProvider.getScreenshotBitmap(validatedActivity,
                ignoredViews).observeOn(AndroidSchedulers.mainThread())
    }

    /**
     * Get single tone instance.

     * @param activity .
     * *
     * @return Instacapture single tone instance.
     */

    /**
     * Enable logging or disable it.

     * @param enable set it true to enable logging.
     */
    fun enableLogging(enable: Boolean) {
        if (enable) {
            Logger.enable()
        } else {
            Logger.disable()
        }
    }

}
