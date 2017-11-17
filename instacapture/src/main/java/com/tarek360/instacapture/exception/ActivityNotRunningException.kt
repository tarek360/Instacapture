package com.tarek360.instacapture.exception

/**
 * Created by tarek on 5/31/16.
 */

/**
 * This exception is thrown when the Activity is finished or destroyed.
 */
class ActivityNotRunningException : RuntimeException {

    constructor()

    constructor(name: String) : super(name)
}

