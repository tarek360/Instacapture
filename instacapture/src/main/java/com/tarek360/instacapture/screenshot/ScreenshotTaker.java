package com.tarek360.instacapture.screenshot;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.tarek360.instacapture.exception.ScreenCapturingFailedException;
import com.tarek360.instacapture.utility.Logger;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by tarek on 5/17/16.
 */
public final class ScreenshotTaker {

    private ScreenshotTaker() {
    }

    /**
     * Capture screenshot for the current activity and return bitmap of it.
     *
     * @param activity     current activity.
     * @param ignoredViews from the screenshot.
     * @return Bitmap of screenshot.
     * @throws ScreenCapturingFailedException if unexpected error is occurred during capturing
     *                                        screenshot
     */
    public static Bitmap getScreenshotBitmap(Activity activity, View[] ignoredViews) {
        if (activity == null) {
            throw new IllegalArgumentException("Parameter activity cannot be null.");
        }

        final List<RootViewInfo> viewRoots = FieldHelper.getRootViews(activity);
        Logger.d("viewRoots count: " + viewRoots.size());
        View main = activity.getWindow().getDecorView();

        final Bitmap bitmap;
        try {
            bitmap = Bitmap.createBitmap(main.getWidth(), main.getHeight(), Bitmap.Config.ARGB_8888);
        } catch (final IllegalArgumentException e) {
            return null;
        }

        drawRootsToBitmap(viewRoots, bitmap, ignoredViews);

        return bitmap;
    }

    //static int count = 0 ;
    private static void drawRootsToBitmap(List<RootViewInfo> viewRoots, Bitmap bitmap,
                                          View[] ignoredViews) {
        //count = 0;
        for (RootViewInfo rootData : viewRoots) {
            drawRootToBitmap(rootData, bitmap, ignoredViews);
        }
    }

    private static void drawRootToBitmap(final RootViewInfo rootViewInfo, Bitmap bitmap,
                                         View[] ignoredViews) {

        // support dim screen
        if ((rootViewInfo.getLayoutParams().flags & WindowManager.LayoutParams.FLAG_DIM_BEHIND)
                == WindowManager.LayoutParams.FLAG_DIM_BEHIND) {
            Canvas dimCanvas = new Canvas(bitmap);

            int alpha = (int) (255 * rootViewInfo.getLayoutParams().dimAmount);
            dimCanvas.drawARGB(alpha, 0, 0, 0);
        }

        final Canvas canvas = new Canvas(bitmap);
        canvas.translate(rootViewInfo.getLeft(), rootViewInfo.getTop());

        int[] ignoredViewsVisibility = null;
        if (ignoredViews != null) {
            ignoredViewsVisibility = new int[ignoredViews.length];
        }

        if (ignoredViews != null) {
            for (int i = 0; i < ignoredViews.length; i++) {
                if (ignoredViews[i] != null) {
                    ignoredViewsVisibility[i] = ignoredViews[i].getVisibility();
                    ignoredViews[i].setVisibility(View.INVISIBLE);
                }
            }
        }

        rootViewInfo.getView().draw(canvas);
        //Draw undrawable views
        drawUnDrawableViews(rootViewInfo.getView(), canvas);

        if (ignoredViews != null) {
            for (int i = 0; i < ignoredViews.length; i++) {
                if (ignoredViews[i] != null) {
                    ignoredViews[i].setVisibility(ignoredViewsVisibility[i]);
                }
            }
        }
    }

    private static ArrayList<View> drawUnDrawableViews(View v, Canvas canvas) {

        if (!(v instanceof ViewGroup)) {
            ArrayList<View> viewArrayList = new ArrayList<>();
            viewArrayList.add(v);
            return viewArrayList;
        }

        ArrayList<View> result = new ArrayList<>();

        ViewGroup viewGroup = (ViewGroup) v;
        for (int i = 0; i < viewGroup.getChildCount(); i++) {

            View child = viewGroup.getChildAt(i);

            ArrayList<View> viewArrayList = new ArrayList<>();
            viewArrayList.add(v);
            viewArrayList.addAll(drawUnDrawableViews(child, canvas));

            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH
                    && child instanceof TextureView) {
                drawTextureView((TextureView) child, canvas);
            }

            if (child instanceof GLSurfaceView) {
                drawGLSurfaceView((GLSurfaceView) child, canvas);
            }

            result.addAll(viewArrayList);
        }
        return result;
    }

    private static void drawGLSurfaceView(GLSurfaceView surfaceView, Canvas canvas) {
        Logger.d("Drawing GLSurfaceView");

        if (surfaceView.getWindowToken() != null) {
            int[] location = new int[2];

            surfaceView.getLocationOnScreen(location);
            final int width = surfaceView.getWidth();
            final int height = surfaceView.getHeight();

            final int x = 0;
            final int y = 0;
            int[] b = new int[width * (y + height)];

            final IntBuffer ib = IntBuffer.wrap(b);
            ib.position(0);

            //To wait for the async call to finish before going forward
            final CountDownLatch countDownLatch = new CountDownLatch(1);
            surfaceView.queueEvent(new Runnable() {
                @Override
                public void run() {
                    EGL10 egl = (EGL10) EGLContext.getEGL();
                    egl.eglWaitGL();
                    GL10 gl = (GL10) egl.eglGetCurrentContext().getGL();

                    gl.glFinish();

                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    gl.glReadPixels(x, 0, width, y + height, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, ib);
                    countDownLatch.countDown();
                }
            });

            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int[] bt = new int[width * height];
            int i = 0;
            for (int k = 0; i < height; k++) {
                for (int j = 0; j < width; j++) {
                    int pix = b[(i * width + j)];
                    int pb = pix >> 16 & 0xFF;
                    int pr = pix << 16 & 0xFF0000;
                    int pix1 = pix & 0xFF00FF00 | pr | pb;
                    bt[((height - k - 1) * width + j)] = pix1;
                }
                i++;
            }

            Bitmap sb = Bitmap.createBitmap(bt, width, height, Bitmap.Config.ARGB_8888);
            Paint paint = new Paint();
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP));
            canvas.drawBitmap(sb, location[0], location[1], paint);
            sb.recycle();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private static void drawTextureView(TextureView textureView, Canvas canvas) {
        Logger.d("Drawing TextureView");

        int[] textureViewLocation = new int[2];
        textureView.getLocationOnScreen(textureViewLocation);
        Bitmap textureViewBitmap = textureView.getBitmap();
        if (textureViewBitmap != null) {
            Paint paint = new Paint();
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP));
            canvas.drawBitmap(textureViewBitmap, textureViewLocation[0], textureViewLocation[1], paint);
            textureViewBitmap.recycle();
        }
    }
}

