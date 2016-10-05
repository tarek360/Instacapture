package com.tarek360.sample;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by tarek on 10/4/16.
 */

public class SurfaceViewSampleActivity extends BaseSampleActivity {

  @BindView(R.id.toolbar) Toolbar toolbar;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_surface_view_sample);

    setSupportActionBar(toolbar);

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
  }

  @OnClick(R.id.fab) public void onClickFAB(View view) {
    captureScreenshot(view);
  }
}
