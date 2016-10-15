package com.tarek360.instacapture.screenshot;

import android.view.View;
import android.view.WindowManager;

/**
 * Created by tarek on 5/18/16.
 */
public class RootViewInfo {

  private final View view;
  private final WindowManager.LayoutParams layoutParams;
  private final int top;
  private final int left;

  public RootViewInfo(View view, WindowManager.LayoutParams layoutParams) {
    this.view = view;
    int[] onScreenPosition = new int[2];
    view.getLocationOnScreen(onScreenPosition);
    left = onScreenPosition[0];
    top = onScreenPosition[1];

    this.layoutParams = layoutParams;
  }

  public View getView() {
    return view;
  }

  public int getTop() {
    return top;
  }

  public int getLeft() {
    return left;
  }

  public WindowManager.LayoutParams getLayoutParams() {
    return layoutParams;
  }
}
