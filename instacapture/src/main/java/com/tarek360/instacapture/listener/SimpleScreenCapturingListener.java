package com.tarek360.instacapture.listener;

import java.io.File;

/**
 * Created by tarek on 5/17/16.
 */

/**
 * A convenient class to extend when you only want to listen for a subset of all the screen
 * capturing
 * events. This implements all methods in the
 * {@link ScreenCaptureListener} but does nothing.
 */

public class SimpleScreenCapturingListener implements ScreenCaptureListener {

  /**
   * Is called when screen capturing task was started
   */
  @Override public void onCaptureStarted() {
    // Empty implementation
  }

  /**
   * Is called when an error was occurred during screen capturing.
   */
  @Override public void onCaptureFailed(Throwable e) {
    // Empty implementation
  }

  /**
   * Is called when screen  is captured successfully.
   *
   * @param file Captured screen file
   */
  @Override public void onCaptureComplete(File file) {
    // Empty implementation
  }
}
