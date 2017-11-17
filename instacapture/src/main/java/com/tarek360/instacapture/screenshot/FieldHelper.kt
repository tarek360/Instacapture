package com.tarek360.instacapture.screenshot

import android.app.Activity
import android.os.Build
import android.view.View
import android.view.WindowManager
import java.lang.reflect.Field
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

        val windowManager = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            getFieldValue(FIELD_NAME_GLOBAL, activity.windowManager)!!
        } else {
            getFieldValue(FIELD_NAME_WINDOW_MANAGER, activity.windowManager)!!
        }

        val rootObjects = getFieldValue(FIELD_NAME_ROOTS, windowManager)
        val paramsObject = getFieldValue(FIELD_NAME_PARAMS, windowManager)

        val viewRoots: Array<Any>
        val params: Array<WindowManager.LayoutParams?>

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            viewRoots = toArray(rootObjects as List<*>)
            val paramsList = paramsObject as List<WindowManager.LayoutParams>
            params = paramsList.toTypedArray()
        } else {
            viewRoots = rootObjects as Array<Any>
            params = paramsObject as Array<WindowManager.LayoutParams?>
        }

        for (i in viewRoots.indices) {
            val any = getFieldValue(FIELD_NAME_VIEW, viewRoots[i])
            if (any != null) {
                val view = any as View
                if (view.visibility == View.VISIBLE) {
                    rootViews.add(RootViewInfo(view, params[i]))
                }
            }
        }

        if(rootViews.isEmpty()){
            val viewObjects = getFieldValue(FIELD_NAME_VIEWS, windowManager)

            val views = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                (viewObjects as ArrayList<View>).toTypedArray()
            } else {
                viewObjects as Array<View>
            }

            if (views.isNotEmpty()) {
                views.mapIndexedTo(rootViews) { i, view ->
                            RootViewInfo(view, params[i]) }
            }
        }

        return rootViews
    }


    @Throws(NoSuchFieldException::class, IllegalAccessException::class)
    private fun getFieldValue(fieldName: String, target: Any?): Any? {
        return try {

            val field = if (fieldName == FIELD_NAME_WINDOW_MANAGER) {
                findField(fieldName, target!!.javaClass)
            } else {
                target?.javaClass?.getDeclaredField(fieldName)
            }
            field?.isAccessible = true
            field?.get(target)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    @Throws(NoSuchFieldException::class)
    private fun findField(name: String, clazz: Class<*>): Field {
        var currentClass: Class<*> = clazz
        while (currentClass != Any::class.java) {
            currentClass.declaredFields
                    .filter { name == it.name }
                    .forEach { return it }

            currentClass = currentClass.superclass
        }
        throw NoSuchFieldException(
                "Field: " + name + " is not found in class: " + clazz.toString())
    }

    private inline fun <reified T> toArray(list: List<*>): Array<T> {
        return (list as List<T>).toTypedArray()
    }
}
