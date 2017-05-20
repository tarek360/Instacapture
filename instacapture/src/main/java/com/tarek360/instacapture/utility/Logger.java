package com.tarek360.instacapture.utility;

import android.support.annotation.NonNull;
import android.util.Log;

/**
 * Created by tarek on 5/17/16.
 */
public final class Logger {

    private static final String TAG = "Instacapture";

    private static boolean enable;

    private Logger() {
    }

    public static void enable() {
        enable = true;
    }

    public static void disable() {
        enable = false;
    }

    public static void d(@NonNull final CharSequence message) {
        if (enable) {
            Log.d(TAG, message.toString());
        }
    }

    public static void w(@NonNull final CharSequence message) {
        if (enable) {
            Log.w(TAG, message.toString());
        }
    }

    public static void e(@NonNull final CharSequence message) {
        if (enable) {
            Log.e(TAG, message.toString());
        }
    }

    public static void printStackTrace(@NonNull final Throwable throwable) {
        if (enable) {
            Log.e(TAG, "Logging caught exception", throwable);
        }
    }
}
