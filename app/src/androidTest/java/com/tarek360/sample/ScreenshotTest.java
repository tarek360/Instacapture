package com.tarek360.sample;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import org.hamcrest.Matcher;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.tarek360.sample.Utility.sleep;
import static org.hamcrest.Matchers.allOf;

@Ignore
@RunWith(AndroidJUnit4.class)
public class ScreenshotTest {


    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity
            .class);

    @Test
    public void testIgnoreViewsScreenshot() {

        String expectedScreenshot = Utility.getScreenshot(InstrumentationRegistry.getContext(),
                "ignore_views_screenshot.txt");

        sleep(1000);

        ViewInteraction ignoreViewsButton = onView(
                allOf(withId(R.id.sample_ignore_views), isDisplayed()));
        ignoreViewsButton.perform(scrollTo(), click());

        ViewInteraction buttonCheckBox = onView(
                allOf(withId(R.id.buttonCheckBox), isDisplayed()));
        buttonCheckBox.perform(click());

        ViewInteraction imageViewCheckBox = onView(
                allOf(withId(R.id.imageViewCheckBox), isDisplayed()));
        imageViewCheckBox.perform(click());

        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.fab), isDisplayed()));
        floatingActionButton.perform(click());

        sleep(1500);

        ViewInteraction screenshotImageView = onView(allOf(withId(R.id.imageView), isDisplayed()));
        screenshotImageView.check(matches(withDrawable(expectedScreenshot)));

    }

    @Test
    public void testDialogScreenshot() {

        String expectedScreenshot = Utility.getScreenshot(InstrumentationRegistry.getContext(),
                "dialog_screenshot.txt");

        sleep(1000);

        ViewInteraction ignoreViewsButton = onView(
                allOf(withId(R.id.sample_dialog), isDisplayed()));
        ignoreViewsButton.perform(scrollTo(), click());

        ViewInteraction showDialogButton = onView(
                allOf(withId(R.id.show_dialog), withText("Show Dialog"), isDisplayed()));
        showDialogButton.perform(click());

        ViewInteraction okButton = onView(allOf(withId(android.R.id.button1), withText("OK")));
        okButton.perform(scrollTo(), click());

        sleep(1500);

        ViewInteraction screenshotImageView = onView(allOf(withId(R.id.imageView), isDisplayed()));
        screenshotImageView.check(matches(withDrawable(expectedScreenshot)));

    }

    @Test
    public void testOptionsMenuScreenshot() {

        String expectedScreenshot = Utility.getScreenshot(InstrumentationRegistry.getContext(),
                "options_menu_screenshot.txt");

        sleep(1000);

        ViewInteraction ignoreViewsButton = onView(
                allOf(withId(R.id.sample_dialog), isDisplayed()));
        ignoreViewsButton.perform(scrollTo(), click());

        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

        ViewInteraction captureButton = onView(
                allOf(withId(R.id.title), withText("capture 1"),
                        isDisplayed()));

        captureButton.perform(click());

        sleep(1500);

        ViewInteraction screenshotImageView = onView(allOf(withId(R.id.imageView), isDisplayed()));
        screenshotImageView.check(matches(withDrawable(expectedScreenshot)));

    }


    private static Matcher<View> withDrawable(final String base64) {
        return new ScreenshotMatcher(base64);
    }

}
