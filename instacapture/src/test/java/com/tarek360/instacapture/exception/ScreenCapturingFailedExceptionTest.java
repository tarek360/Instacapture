package com.tarek360.instacapture.exception;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Created by tarek360 on 12/7/16.
 */
public class ScreenCapturingFailedExceptionTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void shouldTestException() throws ScreenCapturingFailedException {
        thrown.expect(ScreenCapturingFailedException.class);
        throw new ScreenCapturingFailedException(new Exception());
    }
}
