package com.tarek360.instacapture.exception;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Created by tarek360 on 12/7/16.
 */
public class ActivityNotRunningExceptionTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void shouldTestException() throws ActivityNotRunningException {
        thrown.expect(ActivityNotRunningException.class);
        throw new ActivityNotRunningException();
    }

    @Test
    public void shouldTestExceptionMessage() throws ActivityNotRunningException {
        thrown.expect(ActivityNotRunningException.class);
        thrown.expectMessage("Is your activity running?");
        throw new ActivityNotRunningException("Is your activity running?");
    }
}
