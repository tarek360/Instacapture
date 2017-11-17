package com.tarek360.sample;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by tarek on 5/27/17.
 */

public class Utility {

    public static String getScreenshot(Context context, String fileName) {

        try {
            return read(getInputStream(context, fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static InputStream getInputStream(Context context, String fileName) throws IOException {
        return context.getAssets().open(fileName);
    }

    private static String read(InputStream inputStream) throws IOException {

        BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder total = new StringBuilder();
        String line;
        while ((line = r.readLine()) != null) {
            total.append(line).append('\n');
        }
        return total.toString();
    }

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
