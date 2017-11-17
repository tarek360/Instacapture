package com.tarek360.instacapture

import android.app.Activity
import android.os.Build

import java.lang.ref.WeakReference

/**
 * Created by tarek on 5/17/16.
 */
class ActivityReferenceManager {

    private var activity: WeakReference<Activity>? = null

    fun setActivity(activity: Activity) {
        this.activity = WeakReference(activity)
    }

    val validatedActivity: Activity?
        get() {
            if (activity == null) {
                return null
            }

            val activity = activity!!.get()
            if (!isActivityValid(activity)) {
                return null
            }

            return activity
        }

    private fun isActivityValid(activity: Activity?): Boolean {
        if (activity == null) {
            return false
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return !activity.isFinishing && !activity.isDestroyed
        } else {
            return !activity.isFinishing
        }
    }
}
