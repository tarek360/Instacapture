package com.tarek360.instacapture.screenshot

import android.view.View
import android.view.WindowManager

/**
 * Created by tarek on 5/18/16.
 */
class RootViewInfo(val view: View, val layoutParams: WindowManager.LayoutParams?) {
    val top: Int
    val left: Int

    init {
        val onScreenPosition = IntArray(2)
        view.getLocationOnScreen(onScreenPosition)
        left = onScreenPosition[0]
        top = onScreenPosition[1]
    }
}
