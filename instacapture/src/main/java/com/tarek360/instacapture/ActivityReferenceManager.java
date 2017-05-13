package com.tarek360.instacapture;

import android.app.Activity;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.lang.ref.WeakReference;

/**
 * Created by tarek on 5/17/16.
 */
public final class ActivityReferenceManager {

    @Nullable
    private WeakReference<Activity> mActivity;

    public void setActivity(@NonNull final Activity activity) {
        this.mActivity = new WeakReference<>(activity);
    }

    @Nullable
    public Activity getValidatedActivity() {
        if (mActivity == null) {
            return null;
        }

        final Activity activity = mActivity.get();
        if (!isActivityValid(activity)) {
            return null;
        }

        return activity;
    }

    private boolean isActivityValid(@Nullable final Activity activity) {
        if (activity == null) {
            return false;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return !activity.isFinishing() && !activity.isDestroyed();
        } else {
            return !activity.isFinishing();
        }
    }
}
