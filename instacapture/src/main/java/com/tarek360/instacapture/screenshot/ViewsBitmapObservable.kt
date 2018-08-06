package com.tarek360.instacapture.screenshot

import android.app.Activity
import android.graphics.Bitmap
import android.view.View

import com.tarek360.instacapture.exception.IllegalScreenSizeException

import rx.Observable

/**
 * Created by tarek on 5/17/16.
 */
class ViewsBitmapObservable {

    fun get(activity: Activity,
                     removedViews: Array<out View>?): Observable<Bitmap> {

        return Observable.defer {
            val screenBitmap = ScreenshotTaker.getScreenshotBitmap(activity, removedViews)
            if (screenBitmap != null) {
                Observable.just(screenBitmap)
            } else {
                Observable.error<Bitmap>(IllegalScreenSizeException())
            }
        }
    }
}
