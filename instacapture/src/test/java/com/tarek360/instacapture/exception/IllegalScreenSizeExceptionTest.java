package com.tarek360.instacapture.exception;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Created by tarek360 on 12/7/16.
 */
public class IllegalScreenSizeExceptionTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void shouldTestException() throws IllegalScreenSizeException {
        thrown.expect(IllegalScreenSizeException.class);
        thrown.expectMessage("Activity width or height are <= 0");
        throw new IllegalScreenSizeException();
    }
}
