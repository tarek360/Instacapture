package com.tarek360.instacapture.screenshot.maps;

import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.View;
import java.lang.reflect.Method;
import rx.Subscriber;

/**
 * Created by tarek on 10/7/16.
 */

public class GoogleMapReflection {

  public static final String CLASS_NAME_MAP_FRAGMENT =
      "com.google.android.gms.maps.SupportMapFragment";
  public static final String CLASS_NAME_MAP_VIEW = "com.google.android.gms.maps.MapView";

  public static final String CLASS_NAME_SNAPSHOT_READY_CALLBACK =
      "com.google.android.gms.maps.GoogleMap$SnapshotReadyCallback";
  public static final String CLASS_NAME_ON_MAP_READY_CALLBACK =
      "com.google.android.gms.maps.OnMapReadyCallback";

  private final Object map;
  private final Subscriber<? super GoogleMapBitmap> subscriber;
  private final int[] screenPosition;
  private boolean isSnapshotReady = false;

  public GoogleMapReflection(Object map, final Subscriber<? super GoogleMapBitmap> subscriber) {
    this.map = map;
    this.subscriber = subscriber;

    screenPosition = new int[2];

    if (map instanceof View) {

      View mapView = (View) map;
      mapView.getLocationOnScreen(screenPosition);
    } else if (map instanceof Fragment) {
      Fragment mapFragment = (Fragment) map;

      View view = mapFragment.getView();
      if (view != null) {
        view.getLocationOnScreen(screenPosition);
      }
    }
  }

  public void reflect() {
    try {

      Class onMapReadyCallbackClass = Class.forName(CLASS_NAME_ON_MAP_READY_CALLBACK);

      Object onMapReadyCallbackImpl = createOnMapReadyCallbackImpl(onMapReadyCallbackClass);

      Class mapFragmentClass = map.getClass();

      Method getMapAsyncMethod = mapFragmentClass.getDeclaredMethod("getMapAsync",
          new Class[] { onMapReadyCallbackClass });

      getMapAsyncMethod.invoke(map, onMapReadyCallbackImpl);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private Object createSnapshotReadyCallbackImpl(Class snapshotReadyCallbackClass) {
    return InterfaceReflection.createInterfaceImpl(snapshotReadyCallbackClass,
        new InterfaceReflection.MethodInvocationCallback() {
          @Override public void invoke(Method method, Object[] args) {
            if (method.getName().equals("onSnapshotReady")) {

              if (args != null) {

                isSnapshotReady = true;

                subscriber.onNext(new GoogleMapBitmap(screenPosition, (Bitmap) args[0]));
                subscriber.onCompleted();
              }
            }
          }
        });
  }

  private Object createOnMapReadyCallbackImpl(Class onMapReadyCallbackClass) {

    return InterfaceReflection.createInterfaceImpl(onMapReadyCallbackClass,
        new InterfaceReflection.MethodInvocationCallback() {
          @Override public void invoke(Method method, Object[] args) {
            if (method.getName().equals("onMapReady")) {
              if (args != null) {

                Object googleMap = args[0];
                Class googleMapClass = googleMap.getClass();

                try {
                  Class snapshotReadyCallbackClass =
                      Class.forName(CLASS_NAME_SNAPSHOT_READY_CALLBACK);

                  Object snapshotReadyCallbackImpl =
                      createSnapshotReadyCallbackImpl(snapshotReadyCallbackClass);

                  Method onSnapshotReadyMethod = googleMapClass.getDeclaredMethod("snapshot",
                      new Class[] { snapshotReadyCallbackClass });

                  onSnapshotReadyMethod.invoke(googleMap, snapshotReadyCallbackImpl);

                  new Handler().postDelayed(new Runnable() {
                    @Override public void run() {

                      if (!isSnapshotReady) {
                        subscriber.onNext(new GoogleMapBitmap(screenPosition, null));
                        subscriber.onCompleted();
                      }
                    }
                  }, 300);
                } catch (Exception e) {
                  e.printStackTrace();
                }
              }
            }
          }
        });
  }
}
