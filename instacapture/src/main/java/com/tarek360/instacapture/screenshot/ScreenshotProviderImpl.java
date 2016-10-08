package com.tarek360.instacapture.screenshot;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import com.tarek360.instacapture.screenshot.maps.GoogleMapBitmap;
import com.tarek360.instacapture.screenshot.maps.GoogleMapBitmapObservable;
import com.tarek360.instacapture.screenshot.maps.GoogleMapReflection;
import com.tarek360.instacapture.screenshot.nonMaps.NonMapViewsBitmapObservable;
import com.tarek360.instacapture.utility.Logger;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * Created by tarek on 5/17/16.
 */
public class ScreenshotProviderImpl implements ScreenshotProvider {

  private static Paint MAP_PAINT;
  /**
   * This Func to combine Non-MapViews Bitmap and GoogleMap Bitmap.
   */
  private final static Func2<Bitmap, List<GoogleMapBitmap>, Bitmap> BITMAP_COMBINING_FUNCTION =
      new Func2<Bitmap, List<GoogleMapBitmap>, Bitmap>() {
        @Override public Bitmap call(final Bitmap baseLocatedBitmap,
            final List<GoogleMapBitmap> googleMapBitmaps) {

          final Canvas canvas = new Canvas(baseLocatedBitmap);

          for (final GoogleMapBitmap googleMapBitmap : googleMapBitmaps) {
            final int[] position = googleMapBitmap.getPosition();

            if (googleMapBitmap.getBitmap() != null) {
              canvas.drawBitmap(googleMapBitmap.getBitmap(), position[0], position[1], MAP_PAINT);
            } else {
              Logger.e("Google map which is not ready, couldn't be captured");
            }
          }
          return baseLocatedBitmap;
        }
      };

  public ScreenshotProviderImpl() {

    if (MAP_PAINT == null) {
      MAP_PAINT = new Paint();
      MAP_PAINT.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP));
    }
  }

  @Override public Observable<Bitmap> getScreenshotBitmap(@NonNull final Activity activity,
      final View[] removedViews) {

    Observable<Bitmap> screenshotBitmapObservable =
        getScreenshotBitmapObservable(activity, removedViews);

    return screenshotBitmapObservable;
  }

  private Observable<Bitmap> getScreenshotBitmapObservable(@NonNull final Activity activity,
      View[] removedViews) {

    final Observable<Bitmap> nonMapViewsBitmapObservable =
        NonMapViewsBitmapObservable.get(activity, removedViews);

    final View rootView = activity.getWindow().getDecorView().getRootView();
    final List<View> mapViews = getMapViews(rootView);
    final List<Fragment> mapFragments = getMapFragments(activity);

    if (mapViews.isEmpty() && mapFragments.isEmpty()) {

      return nonMapViewsBitmapObservable;
    } else {

      if (!mapFragments.isEmpty()) {

        final Observable<List<GoogleMapBitmap>> mapViewBitmapsObservable =
            getMapBitmapsObservable(mapFragments);

        return Observable.zip(nonMapViewsBitmapObservable, mapViewBitmapsObservable,
            BITMAP_COMBINING_FUNCTION);
      } else {

        final Observable<List<GoogleMapBitmap>> mapViewBitmapsObservable =
            getMapBitmapsObservable(mapViews);

        return Observable.zip(nonMapViewsBitmapObservable, mapViewBitmapsObservable,
            BITMAP_COMBINING_FUNCTION);
      }
    }
  }

  private List<Fragment> getMapFragments(@NonNull final Activity activity) {
    final List<Fragment> mapFragments = new ArrayList<>();

    if (activity instanceof FragmentActivity) {
      List<Fragment> fragments =
          ((FragmentActivity) activity).getSupportFragmentManager().getFragments();
      if (fragments != null) {
        for (Fragment fragment : fragments) {
          if (GoogleMapReflection.CLASS_NAME_MAP_FRAGMENT.equals(fragment.getClass().getName())) {
            mapFragments.add(fragment);
          }
        }
      }
    }
    return mapFragments;
  }

  private List<View> getMapViews(@NonNull final View rootView) {
    final List<View> mapViews = new ArrayList<>();

    final Queue<View> views = new LinkedList<>();
    views.add(rootView);

    while (!views.isEmpty()) {
      final View view = views.remove();

      if (GoogleMapReflection.CLASS_NAME_MAP_VIEW.equals(view.getClass().getName())
          && view.getVisibility() == View.VISIBLE) {
        mapViews.add(view);
      } else if (view instanceof ViewGroup) {
        final ViewGroup viewGroup = (ViewGroup) view;

        for (int i = 0; i < viewGroup.getChildCount(); i++) {
          views.add(viewGroup.getChildAt(i));
        }
      }
    }

    return mapViews;
  }

  private <T> Observable<List<GoogleMapBitmap>> getMapBitmapsObservable(
      @NonNull final List<T> maps) {

    return Observable.from(maps).concatMap(new Func1<T, Observable<GoogleMapBitmap>>() {

      @Override public Observable<GoogleMapBitmap> call(@NonNull final T map) {

        return GoogleMapBitmapObservable.getMapObservable(map);
      }
    }).toList();
  }
}