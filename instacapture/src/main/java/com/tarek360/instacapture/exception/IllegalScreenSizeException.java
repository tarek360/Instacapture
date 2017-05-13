package com.tarek360.instacapture.exception;

/**
 * Created by tarek on 5/17/16.
 */

/**
 * Throw this exception to know thatActivity width or height are <= 0.
 */
public final class IllegalScreenSizeException extends Exception {

    public IllegalScreenSizeException() {
        super("Activity width or height are <= 0");
    }
}