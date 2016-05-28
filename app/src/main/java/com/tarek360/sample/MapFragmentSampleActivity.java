package com.tarek360.sample;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by tarek on 5/28/16.
 */
public class MapFragmentSampleActivity extends BaseSampleActivity {

  @Bind(R.id.toolbar) public Toolbar toolbar;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_map_fragment);

    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
  }

  @OnClick(R.id.fab) public void onClickFAB() {
    captureScreenshot();
  }
}
