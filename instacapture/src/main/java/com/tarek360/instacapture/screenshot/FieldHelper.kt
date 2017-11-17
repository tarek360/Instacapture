package com.tarek360.instacapture.screenshot

import android.app.Activity
import android.os.Build
import android.view.View
import android.view.WindowManager
import java.util.*

/**
 * Created by tarek on 5/18/16.
 */
internal object FieldHelper {

    private val FIELD_NAME_WINDOW_MANAGER = "mWindowManager"
    private val FIELD_NAME_GLOBAL = "mGlobal"
    private val FIELD_NAME_ROOTS = "mRoots"
    private val FIELD_NAME_PARAMS = "mParams"
    private val FIELD_NAME_VIEW = "mView"
    private val FIELD_NAME_VIEWS = "mViews"

    fun getRootViews(activity: Activity): List<RootViewInfo> {
        val rootViews = ArrayList<RootViewInfo>()

        val windowManager: Any
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            windowManager = getFieldValue(FIELD_NAME_GLOBAL, activity.windowManager)!!
        } else {
            windowManager = getFieldValue(FIELD_NAME_WINDOW_MANAGER, activity.windowManager)!!
        }

        val rootObjects = getFieldValue(FIELD_NAME_ROOTS, windowManager)
        val paramsObject = getFieldValue(FIELD_NAME_PARAMS, windowManager)

        val viewRoots: Array<Any?>
        val params: Array<WindowManager.LayoutParams?>

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            viewRoots = (rootObjects as List<*>).toTypedArray()
            val paramsList = paramsObject as List<WindowManager.LayoutParams>
            params = paramsList.toTypedArray()
        } else {
            viewRoots = rootObjects as Array<Any?>
            params = paramsObject as Array<WindowManager.LayoutParams?>
        }
        for (i in viewRoots.indices) {
            val view = getFieldValue(FIELD_NAME_VIEW, viewRoots[i])
            if (view == null) {
                val views = getFieldValue(FIELD_NAME_VIEWS, windowManager) as ArrayList<View>
                if (views != null && views.size > 0) {
                    views.filter { it.visibility == View.VISIBLE }
                            .mapTo(rootViews) { RootViewInfo(it, params[i]) }
                }
            } else {
                val view = view as View
                if (view.visibility == View.VISIBLE) {
                    rootViews.add(RootViewInfo(view, params[i]))
                }
            }

        }

        return rootViews
    }

    @Throws(NoSuchFieldException::class, IllegalAccessException::class)
    private fun getFieldValue(fieldName: String, target: Any?): Any? {
        try {
            val field = target?.javaClass?.getDeclaredField(fieldName)
            field?.isAccessible = true
            return field?.get(target)
        } catch (e: Exception) {
            e.printStackTrace()
            return null;
        }
    }
}
