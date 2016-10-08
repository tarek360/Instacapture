package com.tarek360.instacapture.screenshot.maps;

import android.support.annotation.NonNull;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by tarek on 5/17/16.
 */
public final class GoogleMapBitmapObservable {

  private GoogleMapBitmapObservable() {

  }

  public static Observable<GoogleMapBitmap> getMapObservable(@NonNull final Object map) {

    return Observable.create(new Observable.OnSubscribe<GoogleMapBitmap>() {

      @Override public void call(final Subscriber<? super GoogleMapBitmap> subscriber) {

        GoogleMapReflection googleMapViewReflection = new GoogleMapReflection(map, subscriber);
        googleMapViewReflection.reflect();
      }
    });
  }
}