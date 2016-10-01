package com.tarek360.instacapture.screenshot.maps;

import android.graphics.Bitmap;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by tarek on 5/17/16.
 */
public final class GoogleMapBitmapObservable {

  private GoogleMapBitmapObservable() {

  }

  public static Observable<GoogleMapBitmap> getMapViewObservable(@NonNull final MapView mapView) {
    final int[] screenPosition = new int[2];
    mapView.getLocationOnScreen(screenPosition);

    return Observable.create(new Observable.OnSubscribe<GoogleMapBitmap>() {
      private boolean isSnapshotReady = false;

      @Override public void call(final Subscriber<? super GoogleMapBitmap> subscriber) {

        mapView.getMapAsync(new OnMapReadyCallback() {
          @Override public void onMapReady(@NonNull final GoogleMap gMap) {

            gMap.snapshot(new GoogleMap.SnapshotReadyCallback() {
              @Override public void onSnapshotReady(@Nullable final Bitmap bitmap) {
                isSnapshotReady = true;

                subscriber.onNext(new GoogleMapBitmap(screenPosition, bitmap));
                subscriber.onCompleted();
              }
            });

            new Handler().postDelayed(new Runnable() {
              @Override public void run() {

                if (!isSnapshotReady) {
                  subscriber.onNext(new GoogleMapBitmap(screenPosition, null));
                  subscriber.onCompleted();
                }
              }
            }, 300);
          }
        });
      }
    });
  }

  public static Observable<GoogleMapBitmap> getMapFragmentObservable(
      @NonNull final SupportMapFragment mapFragment) {
    final int[] screenPosition = new int[2];
    View view = mapFragment.getView();
    if (view != null) {
      view.getLocationOnScreen(screenPosition);
    }

    return Observable.create(new Observable.OnSubscribe<GoogleMapBitmap>() {

      private boolean isSnapshotReady = false;

      @Override public void call(final Subscriber<? super GoogleMapBitmap> subscriber) {

        mapFragment.getMapAsync(new OnMapReadyCallback() {
          @Override public void onMapReady(@NonNull final GoogleMap gMap) {

            gMap.snapshot(new GoogleMap.SnapshotReadyCallback() {
              @Override public void onSnapshotReady(@Nullable final Bitmap bitmap) {
                isSnapshotReady = true;

                subscriber.onNext(new GoogleMapBitmap(screenPosition, bitmap));
                subscriber.onCompleted();
              }
            });

            new Handler().postDelayed(new Runnable() {
              @Override public void run() {

                if (!isSnapshotReady) {
                  subscriber.onNext(new GoogleMapBitmap(screenPosition, null));
                  subscriber.onCompleted();
                }
              }
            }, 300);
          }
        });
      }
    });
  }
}