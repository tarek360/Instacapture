package com.tarek360.instacapture.screenshot

import android.app.Activity
import android.graphics.*
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.os.Build
import android.support.annotation.RequiresApi
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.tarek360.instacapture.exception.ScreenCapturingFailedException
import com.tarek360.instacapture.utility.Logger
import java.nio.IntBuffer
import java.util.*
import java.util.concurrent.CountDownLatch
import javax.microedition.khronos.egl.EGL10
import javax.microedition.khronos.egl.EGLContext
import javax.microedition.khronos.opengles.GL10

/**
 * Created by tarek on 5/17/16.
 */
object ScreenshotTaker {

    /**
     * Capture screenshot for the current activity and return bitmap of it.

     * @param activity     current activity.
     * *
     * @param ignoredViews from the screenshot.
     * *
     * @return Bitmap of screenshot.
     * *
     * @throws ScreenCapturingFailedException if unexpected error is occurred during capturing
     * *                                        screenshot
     */
    fun getScreenshotBitmap(activity: Activity?, ignoredViews: Array<out View>?): Bitmap? {
        if (activity == null) {
            throw IllegalArgumentException("Parameter activity cannot be null.")
        }

        val paramsObject = Any()

        if (paramsObject is List<*>) {

        }

        val viewRoots = FieldHelper.getRootViews(activity)

        val main = activity.window.decorView

        val bitmap: Bitmap
        try {
            bitmap = Bitmap.createBitmap(main.width, main.height, Bitmap.Config.ARGB_8888)
        } catch (e: IllegalArgumentException) {
            return null
        }

        drawRootsToBitmap(viewRoots, bitmap, ignoredViews)

        return bitmap
    }

    //static int count = 0 ;
    private fun drawRootsToBitmap(viewRoots: List<RootViewInfo>, bitmap: Bitmap,
                                  ignoredViews: Array<out View>?) {
        //count = 0;
        for (rootData in viewRoots) {
            drawRootToBitmap(rootData, bitmap, ignoredViews)
        }
    }

    private fun drawRootToBitmap(rootViewInfo: RootViewInfo, bitmap: Bitmap,
                                 ignoredViews: Array<out View>?) {

        // support dim screen
        if (rootViewInfo.layoutParams!!.flags and WindowManager.LayoutParams.FLAG_DIM_BEHIND
                == WindowManager.LayoutParams.FLAG_DIM_BEHIND) {
            val dimCanvas = Canvas(bitmap)

            val alpha = (255 * rootViewInfo.layoutParams.dimAmount).toInt()
            dimCanvas.drawARGB(alpha, 0, 0, 0)
        }

        val canvas = Canvas(bitmap)
        canvas.translate(rootViewInfo.left.toFloat(), rootViewInfo.top.toFloat())

        if (ignoredViews != null) {

            val ignoredViewsVisibility = IntArray(ignoredViews.size)

            for (i in ignoredViews.indices) {
                ignoredViewsVisibility[i] = ignoredViews[i].visibility
                ignoredViews[i].visibility = View.INVISIBLE
            }

            rootViewInfo.view.draw(canvas)
            //Draw undrawable views
            drawUnDrawableViews(rootViewInfo.view, canvas)

            for (i in ignoredViews.indices) {
                ignoredViews[i].visibility = ignoredViewsVisibility[i]

            }

        }
    }

    private fun drawUnDrawableViews(v: View, canvas: Canvas): ArrayList<View> {

        if (v !is ViewGroup) {
            val viewArrayList = ArrayList<View>()
            viewArrayList.add(v)
            return viewArrayList
        }

        val result = ArrayList<View>()

        val viewGroup = v
        for (i in 0 until viewGroup.childCount) {

            val child = viewGroup.getChildAt(i)

            val viewArrayList = ArrayList<View>()
            viewArrayList.add(v)
            viewArrayList.addAll(drawUnDrawableViews(child, canvas))

            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH && child is TextureView) {
                drawTextureView(child, canvas)
            }

            if (child is GLSurfaceView) {
                drawGLSurfaceView(child, canvas)
            }

            result.addAll(viewArrayList)
        }
        return result
    }

    private fun drawGLSurfaceView(surfaceView: GLSurfaceView, canvas: Canvas) {
        Logger.d("Drawing GLSurfaceView")

        if (surfaceView.windowToken != null) {
            val location = IntArray(2)

            surfaceView.getLocationOnScreen(location)
            val width = surfaceView.width
            val height = surfaceView.height

            val x = 0
            val y = 0
            val b = IntArray(width * (y + height))

            val ib = IntBuffer.wrap(b)
            ib.position(0)

            //To wait for the async call to finish before going forward
            val countDownLatch = CountDownLatch(1)
            surfaceView.queueEvent {
                val egl = EGLContext.getEGL() as EGL10
                egl.eglWaitGL()
                val gl = egl.eglGetCurrentContext().gl as GL10

                gl.glFinish()

                try {
                    Thread.sleep(200)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

                gl.glReadPixels(x, 0, width, y + height, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, ib)
                countDownLatch.countDown()
            }

            try {
                countDownLatch.await()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

            val bt = IntArray(width * height)
            var i = 0
            var k = 0
            while (i < height) {
                for (j in 0 until width) {
                    val pix = b[i * width + j]
                    val pb = pix shr 16 and 0xFF
                    val pr = pix shl 16 and 0xFF0000
                    val pix1 = pix and 0xFF00FF00.toInt() or pr or pb
                    bt[(height - k - 1) * width + j] = pix1
                }
                i++
                k++
            }

            val sb = Bitmap.createBitmap(bt, width, height, Bitmap.Config.ARGB_8888)
            val paint = Paint()
            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_ATOP)
            canvas.drawBitmap(sb, location[0].toFloat(), location[1].toFloat(), paint)
            sb.recycle()
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private fun drawTextureView(textureView: TextureView, canvas: Canvas) {
        Logger.d("Drawing TextureView")

        val textureViewLocation = IntArray(2)
        textureView.getLocationOnScreen(textureViewLocation)
        val textureViewBitmap = textureView.bitmap
        if (textureViewBitmap != null) {
            val paint = Paint()
            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_ATOP)
            canvas.drawBitmap(textureViewBitmap, textureViewLocation[0].toFloat(), textureViewLocation[1].toFloat(), paint)
            textureViewBitmap.recycle()
        }
    }
}

