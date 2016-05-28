package com.tarek360.instacapture.screenshot.nonMaps;

import android.app.Activity;
import android.graphics.Rect;
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
  private final static String FIELD_NAME_ATTACH_INFO = "mAttachInfo";
  private final static String FIELD_NAME_WINDOW_TOP = "mWindowTop";
  private final static String FIELD_NAME_WINDOW_LEFT = "mWindowLeft";
  private final static String FIELD_NAME_WINDOW_FRAME = "mWinFrame";
  private final static String FIELD_NAME_VIEW = "mView";

  private FieldHelper() {
  }

  public static List<RootViewInfo> getRootViews(Activity activity) {
    List<RootViewInfo> rootViews = new ArrayList<>();

    Object globalWindowManager;
    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN) {
      globalWindowManager = getFieldValue(FIELD_NAME_WINDOW_MANAGER, activity.getWindowManager());
    } else {
      globalWindowManager = getFieldValue(FIELD_NAME_GLOBAL, activity.getWindowManager());
    }
    Object rootObjects = getFieldValue(FIELD_NAME_ROOTS, globalWindowManager);
    Object paramsObject = getFieldValue(FIELD_NAME_PARAMS, globalWindowManager);

    Object[] roots;
    WindowManager.LayoutParams[] params;

    //  There was a change to ArrayList implementation in 4.4
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      roots = ((List) rootObjects).toArray();

      List<WindowManager.LayoutParams> paramsList = (List<WindowManager.LayoutParams>) paramsObject;
      params = paramsList.toArray(new WindowManager.LayoutParams[paramsList.size()]);
    } else {
      roots = (Object[]) rootObjects;
      params = (WindowManager.LayoutParams[]) paramsObject;
    }

    for (int i = 0; i < roots.length; i++) {
      Object root = roots[i];

      Object attachInfo = getFieldValue(FIELD_NAME_ATTACH_INFO, root);
      int top = (int) getFieldValue(FIELD_NAME_WINDOW_TOP, attachInfo);
      int left = (int) getFieldValue(FIELD_NAME_WINDOW_LEFT, attachInfo);

      Rect winFrame = (Rect) getFieldValue(FIELD_NAME_WINDOW_FRAME, root);
      Rect area = new Rect(left, top, left + winFrame.width(), top + winFrame.height());

      View view = (View) getFieldValue(FIELD_NAME_VIEW, root);

      rootViews.add(new RootViewInfo(view, area, params[i]));
    }

    return rootViews;
  }

  private static Object getFieldValue(String fieldName, Object target) {

    try {
      Field field = findField(fieldName, target.getClass());
      field.setAccessible(true);
      return field.get(target);
    } catch (Exception e) {
      throw new ScreenCapturingFailedException(e);
    }
  }

  private static Field findField(String name, Class clazz) throws NoSuchFieldException {
    Class currentClass = clazz;
    while (currentClass != Object.class) {
      for (Field field : currentClass.getDeclaredFields()) {
        if (name.equals(field.getName())) {
          return field;
        }
      }

      currentClass = currentClass.getSuperclass();
    }

    throw new NoSuchFieldException("The field " + name + " isn't found for " + clazz.toString());
  }
}
