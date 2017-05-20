package com.tarek360.instacapture.utility

import android.util.Log

/**
 * Created by tarek on 5/17/16.
 */
object Logger {

    private val TAG = "Instacapture"

    private var enable: Boolean = false

    fun enable() {
        enable = true
    }

    fun disable() {
        enable = false
    }

    fun d(message: CharSequence) {
        if (enable) {
            Log.d(TAG, message.toString())
        }
    }

    fun w(message: CharSequence) {
        if (enable) {
            Log.w(TAG, message.toString())
        }
    }

    fun e(message: CharSequence) {
        if (enable) {
            Log.e(TAG, message.toString())
        }
    }

    fun printStackTrace(throwable: Throwable) {
        if (enable) {
            Log.e(TAG, "Logging caught exception", throwable)
        }
    }
}
