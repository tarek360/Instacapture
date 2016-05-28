package com.tarek360.instacapture.exception;

/**
 * Created by tarek on 5/17/16.
 */

/**
 * Throw this exception to know that GoogleMap capturing failed.
 */
public final class MapSnapshotFailedException extends Exception {

  public MapSnapshotFailedException() {
    super("GoogleMap screenshot capturing failed");
  }
}