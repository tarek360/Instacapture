package com.tarek360.instacapture.screenshot.nonMaps;

import android.graphics.Rect;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by tarek on 5/18/16.
 */
public class RootViewInfo {

  private final View view;
  private final Rect rect;
  private final WindowManager.LayoutParams layoutParams;

  public RootViewInfo(View view, Rect rect, WindowManager.LayoutParams layoutParams) {
    this.view = view;
    this.rect = rect;
    this.layoutParams = layoutParams;
  }

  public View getView() {
    return view;
  }

  public Rect getRect() {
    return rect;
  }

  public WindowManager.LayoutParams getLayoutParams() {
    return layoutParams;
  }
}
