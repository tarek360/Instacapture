package com.tarek360.instacapture.exception

/**
 * Created by tarek on 5/17/16.
 */

/**
 * Throw this exception to know that screen capturing failed.
 */
class ScreenCapturingFailedException(e: Exception) : RuntimeException(e)
