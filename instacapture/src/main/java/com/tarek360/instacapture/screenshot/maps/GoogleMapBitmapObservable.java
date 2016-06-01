package com.tarek360.instacapture.screenshot.maps;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.tarek360.instacapture.exception.MapSnapshotFailedException;
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
      @Override public void call(final Subscriber<? super GoogleMapBitmap> subscriber) {

        mapView.getMapAsync(new OnMapReadyCallback() {
          @Override public void onMapReady(@NonNull final GoogleMap gMap) {

            gMap.snapshot(new GoogleMap.SnapshotReadyCallback() {
              @Override public void onSnapshotReady(@Nullable final Bitmap bitmap) {
                if (bitmap != null) {
                  subscriber.onNext(new GoogleMapBitmap(screenPosition, bitmap));
                  subscriber.onCompleted();
                } else {
                  subscriber.onError(new MapSnapshotFailedException());
                }
              }
            });
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
      @Override public void call(final Subscriber<? super GoogleMapBitmap> subscriber) {

        mapFragment.getMapAsync(new OnMapReadyCallback() {
          @Override public void onMapReady(@NonNull final GoogleMap gMap) {

            gMap.snapshot(new GoogleMap.SnapshotReadyCallback() {
              @Override public void onSnapshotReady(@Nullable final Bitmap bitmap) {
                if (bitmap != null) {
                  subscriber.onNext(new GoogleMapBitmap(screenPosition, bitmap));
                  subscriber.onCompleted();
                } else {
                  subscriber.onError(new MapSnapshotFailedException());
                }
              }
            });
          }
        });
      }
    });
  }
}