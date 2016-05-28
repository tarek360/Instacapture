package com.tarek360.sample;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import butterknife.Bind;
import butterknife.OnClick;
import com.squareup.picasso.Picasso;

public class CustomViewSampleActivity extends BaseSampleActivity {

  @Bind(R.id.toolbar) Toolbar toolbar;
  @Bind(R.id.header) ImageView header;
  @Bind(R.id.chart) ImageView chart;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_custom_view_sample);

    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    Picasso.with(this).load(R.drawable.header).into(header);
    Picasso.with(this).load(R.drawable.chart).into(chart);
  }

  @OnClick(R.id.fab) public void onClickFAB() {
    captureScreenshot();
  }
}
