package com.tarek360.instacapture.screenshot.maps;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

/**
 * Created by tarek on 5/17/16.
 */
public final class GoogleMapBitmap {

  private final int[] position;
  private final Bitmap bitmap;

  GoogleMapBitmap(@NonNull final int[] position, final Bitmap bitmap) {
    this.bitmap = bitmap;
    this.position = position;
  }

  public int[] getPosition() {
    return position;
  }

  public Bitmap getBitmap() {
    return bitmap;
  }
}
