package com.tarek360.sample;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.io.ByteArrayOutputStream;

/**
 * Created by tarek on 5/25/17.
 */

public class ScreenshotMatcher extends TypeSafeMatcher<View> {

    private final static String NOT_EMPTY = "not_empty";
    private final String expectedBase64;
    private String textDescription;

    public ScreenshotMatcher() {
        this(NOT_EMPTY);
    }

    public ScreenshotMatcher(String expectedBase64) {
        super(View.class);
        this.expectedBase64 = expectedBase64;
    }

    @Override
    protected boolean matchesSafely(View target) {
        if (!(target instanceof ImageView)) {
            return false;
        }
        ImageView imageView = (ImageView) target;


        if (expectedBase64 == null) {
            textDescription = "the expectedBase64 is null";
            return false;
        }

        if (expectedBase64.equals(NOT_EMPTY)) {
            if (imageView.getDrawable() != null) {
                textDescription = "imageView has a drawable";
                return true;
            } else {
                textDescription = "imageView hasn't drawable";
                return false;
            }
        }

        BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();

        if (bitmapDrawable == null) {
            textDescription = "imageView hasn't drawable";
            return false;
        }
        Bitmap bitmap = bitmapDrawable.getBitmap();

        if (bitmap == null) {
            textDescription = "bitmapDrawable .getBitmap() return null";
            return false;
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
        byte[] bytes = bos.toByteArray();
        String image = Base64.encodeToString(bytes, Base64.DEFAULT);


        if (image.equals(expectedBase64)) {
            textDescription = "expectedBase64 match the imageView drawable";
            return true;
        } else {
            textDescription = "expectedBase64 doesn't match the imageView drawable";
            return false;
        }
    }


    @Override
    public void describeTo(Description description) {
        description.appendText(textDescription);
    }
}