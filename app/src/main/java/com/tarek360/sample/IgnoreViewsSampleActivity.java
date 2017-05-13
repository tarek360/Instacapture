package com.tarek360.sample;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class IgnoreViewsSampleActivity extends BaseSampleActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.textViewCheckBox)
    CheckBox textViewCheckBox;
    @BindView(R.id.buttonCheckBox)
    CheckBox buttonCheckBox;
    @BindView(R.id.imageViewCheckBox)
    CheckBox imageViewCheckBox;

    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.button)
    Button button;
    @BindView(R.id.imageView)
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ignore_views_sample);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @OnClick(R.id.fab)
    public void onClickFAB(View view) {

        List<View> views = new ArrayList<>();

        views.add(view);

        if (!textViewCheckBox.isChecked()) {
            views.add(textView);
        }
        if (!buttonCheckBox.isChecked()) {
            views.add(button);
        }
        if (!imageViewCheckBox.isChecked()) {
            views.add(imageView);
        }

        View[] ignoredViews = new View[views.size()];
        ignoredViews = views.toArray(ignoredViews);

        captureScreenshot(ignoredViews);
    }

    @OnClick(R.id.textView)
    public void onClickTextView() {
        textViewCheckBox.setChecked(!textViewCheckBox.isChecked());
    }

    @OnClick(R.id.button)
    public void onClickButton() {
        buttonCheckBox.setChecked(!buttonCheckBox.isChecked());
    }

    @OnClick(R.id.imageView)
    public void onClickImageView() {
        imageViewCheckBox.setChecked(!imageViewCheckBox.isChecked());
    }
}
