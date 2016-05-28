package com.tarek360.instacapture;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.View;
import com.tarek360.instacapture.listener.ScreenCaptureListener;
import com.tarek360.instacapture.screenshot.ScreenshotProvider;
import com.tarek360.instacapture.screenshot.ScreenshotProviderImpl;
import com.tarek360.instacapture.utility.Logger;
import java.io.File;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by tarek on 5/17/16.
 */
public final class InstaCapture {

  private static final String MESSAGE_IS_ACTIVITY_RUNNING = "Is your activity running?";
  private static final String MESSAGE_BUSY = "InstaCapture is busy, please wait!";
  private static final String ERROR_INIT_WITH_DESTROYED_ACTIVITY = "Your Activity may be destroyed";
  private static final String ERROR_SCREENSHOT_CAPTURE_FAILED = "Screenshot capture failed";

  private static InstaCapture instance;

  @NonNull private ActivityReferenceManager activityReferenceManager;

  @NonNull private ScreenshotProvider screenshotProvider;

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

  /** Returns singleton class instance */
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

  public InstaCapture setScreenCapturingListener(ScreenCaptureListener listener) {
    mScreenCapturingListener = listener;
    return this;
  }

  /**
   * Capture the current screen.
   */
  public void capture() {
    capture(null, null);
  }

  /**
   * Capture the current screen.
   *
   * @param file to save screenshot .
   */
  public void capture(File file) {
    capture(file, null);
  }

  /**
   * Capture the current screen.
   *
   * @param ignoredViews from screenshot .
   */
  public void capture(View... ignoredViews) {
    capture(null, ignoredViews);
  }

  /**
   * Capture the current screen.
   *
   * @param file to save screenshot .
   * @param ignoredViews from screenshot .
   */
  public void capture(File file, View... ignoredViews) {

    final Activity activity = activityReferenceManager.getValidatedActivity();
    if (activity == null) {
      Logger.e(MESSAGE_IS_ACTIVITY_RUNNING);
      return;
    }

    if (mScreenCapturingListener != null) {
      mScreenCapturingListener.onCaptureStarted();
    }

    screenshotProvider.getScreenshotFile(activity, file, ignoredViews)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<File>() {
          @Override public void onCompleted() {
          }

          @Override public void onError(final Throwable e) {
            Logger.e(ERROR_SCREENSHOT_CAPTURE_FAILED);
            Logger.printStackTrace(e);

            if (mScreenCapturingListener != null) {
              mScreenCapturingListener.onCaptureFailed(e);
            }
          }

          @Override public void onNext(final File file) {
            if (mScreenCapturingListener != null) {
              mScreenCapturingListener.onCaptureComplete(file);
            }
          }
        });
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

    return new ScreenshotProviderImpl() {
    };
  }
}
