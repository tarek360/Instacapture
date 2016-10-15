package com.tarek360.instacapture.screenshot;

import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.WindowManager;
import com.tarek360.instacapture.exception.ScreenCapturingFailedException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tarek on 5/18/16.
 */
class FieldHelper {

  private final static String FIELD_NAME_WINDOW_MANAGER = "mWindowManager";
  private final static String FIELD_NAME_GLOBAL = "mGlobal";
  private final static String FIELD_NAME_ROOTS = "mRoots";
  private final static String FIELD_NAME_PARAMS = "mParams";
  private final static String FIELD_NAME_VIEW = "mView";

  private FieldHelper() {
  }

  public static List<RootViewInfo> getRootViews(Activity activity) {
    List<RootViewInfo> rootViews = new ArrayList<>();

    Object windowManager;
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
      windowManager = getFieldValue(FIELD_NAME_GLOBAL, activity.getWindowManager());
    } else {
      windowManager = getFieldValue(FIELD_NAME_WINDOW_MANAGER, activity.getWindowManager());
    }

    Object rootObjects = getFieldValue(FIELD_NAME_ROOTS, windowManager);
    Object paramsObject = getFieldValue(FIELD_NAME_PARAMS, windowManager);

    Object[] viewRoots;
    WindowManager.LayoutParams[] params;

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      viewRoots = ((List) rootObjects).toArray();
      List<WindowManager.LayoutParams> paramsList = (List<WindowManager.LayoutParams>) paramsObject;
      params = paramsList.toArray(new WindowManager.LayoutParams[paramsList.size()]);
    } else {
      viewRoots = (Object[]) rootObjects;
      params = (WindowManager.LayoutParams[]) paramsObject;
    }

    for (int i = 0; i < viewRoots.length; i++) {
      View view = (View) getFieldValue(FIELD_NAME_VIEW, viewRoots[i]);

      rootViews.add(new RootViewInfo(view, params[i]));
    }

    return rootViews;
  }

  private static Object getFieldValue(String fieldName, Object target) {

    try {
      Field field = target.getClass().getDeclaredField(fieldName);
      field.setAccessible(true);
      return field.get(target);
    } catch (Exception e) {
      throw new ScreenCapturingFailedException(e);
    }
  }
}
