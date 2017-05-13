package com.tarek360.instacapture;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.tarek360.instacapture.exception.ActivityNotRunningException;
import com.tarek360.instacapture.listener.ScreenCaptureListener;
import com.tarek360.instacapture.screenshot.ScreenshotProvider;
import com.tarek360.instacapture.utility.Logger;

import org.reactivestreams.Subscriber;

import hu.akarnokd.rxjava.interop.RxJavaInterop;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import rx.Observer;
import rx.SingleSubscriber;
import rx.Subscription;

/**
 * Created by tarek on 5/17/16.
 */
public final class InstaCapture {
    public static long startTime;

    private static final String MESSAGE_IS_ACTIVITY_RUNNING = "Is your activity running?";
    private static final String MESSAGE_BUSY = "InstaCapture is busy, please wait!";
    private static final String ERROR_INIT_WITH_DESTROYED_ACTIVITY = "Your Activity may be destroyed";
    private static final String ERROR_SCREENSHOT_CAPTURE_FAILED = "Screenshot capture failed";

    private static InstaCapture instance;
    private static Listener listener;

    @NonNull
    private ActivityReferenceManager activityReferenceManager;

    @NonNull
    private ScreenshotProvider screenshotProvider;

    private ScreenCaptureListener mScreenCapturingListener;

    private InstaCapture(@NonNull final Activity activity) {

        this.activityReferenceManager = new ActivityReferenceManager();
        this.activityReferenceManager.setActivity(activity);
        this.screenshotProvider = getScreenshotProvider();
    }

    /**
     * Get single tone instance.
     *
     * @param activity .
     * @return InstaCapture single tone instance.
     */

    /**
     * Set configuration.
     *
     * @param configuration InstaCaptureConfiguration.
     */
    public static void setConfiguration(InstaCaptureConfiguration configuration) {
        if (configuration.logging) {
            Logger.enable();
        } else {
            Logger.disable();
        }
    }

    /**
     * Returns singleton class instance
     */
    public static InstaCapture getInstance(@NonNull final Activity activity) {

        synchronized (InstaCapture.class) {
            if (instance == null) {
                instance = new InstaCapture(activity);
            } else {
                instance.setActivity(activity);
            }
        }

        return instance;
    }

    private void setActivity(@NonNull final Activity activity) {
        this.activityReferenceManager.setActivity(activity);
    }

    /**
     * Capture the current screen.
     */
    public Listener capture() {
        capture(null);
        return getListener();
    }

    /**
     * Capture the current screen.
     *
     * @param ignoredViews from screenshot .
     */
    public Listener capture(View... ignoredViews) {

        captureRx(ignoredViews).subscribe(new SingleObserver<Bitmap>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(Bitmap bitmap) {
                if (mScreenCapturingListener != null) {
                    mScreenCapturingListener.onCaptureComplete(bitmap);
                }
            }

            @Override
            public void onError(Throwable e) {
                Logger.e(ERROR_SCREENSHOT_CAPTURE_FAILED);
                Logger.printStackTrace(e);

                if (mScreenCapturingListener != null) {
                    mScreenCapturingListener.onCaptureFailed(e);
                }
            }
        });
        return getListener();
    }

    /**
     * Capture the current screen.
     *
     * @return a Single<Bitmap> from RxJava 2.
     */
    public Single<Bitmap> captureRx() {
        return captureRx(null);
    }

    /**
     * add support for RxJava version 1.
     *
     * @return a Single<Bitmap> from RxJava 1.
     */
    public rx.Single<Bitmap> captureRxV1() {
        return RxJavaInterop.toV1Single(captureRx(null));
    }

    /**
     * Capture the current screen.
     *
     * @param ignoredViews from screenshot.
     * @return a Single<Bitmap> from RxJava 2.
     */
    public Single<Bitmap> captureRx(@Nullable View... ignoredViews) {

        InstaCapture.startTime = System.currentTimeMillis();
        final Activity activity = activityReferenceManager.getValidatedActivity();
        if (activity == null) {
            return Single.error(new ActivityNotRunningException(MESSAGE_IS_ACTIVITY_RUNNING));
        }

        if (mScreenCapturingListener != null) {
            mScreenCapturingListener.onCaptureStarted();
        }

        return screenshotProvider.getScreenshotBitmap(activity, ignoredViews)
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * add support for RxJava 1.
     *
     * @param ignoredViews from screenshot.
     * @return Single<Bitmap> from RxJava version 1.
     */
    public rx.Single<Bitmap> captureRxV1(@Nullable View... ignoredViews) {
        return RxJavaInterop.toV1Single(captureRx(ignoredViews));
    }
    

    /**
     * @return a ScreenshotProvider
     */
    private ScreenshotProvider getScreenshotProvider() {

        final Activity activity = activityReferenceManager.getValidatedActivity();
        if (activity == null) {
            Logger.e(MESSAGE_IS_ACTIVITY_RUNNING);
            throw new IllegalArgumentException(ERROR_INIT_WITH_DESTROYED_ACTIVITY);
        }

        return new ScreenshotProvider();
    }

    private Listener getListener() {

        synchronized (Listener.class) {
            if (listener == null) {
                listener = new Listener();
            }
        }

        return listener;
    }

    public final class Listener {

        private Listener() {
        }

        public void setScreenCapturingListener(@NonNull ScreenCaptureListener listener) {
            mScreenCapturingListener = listener;
        }
    }
}
