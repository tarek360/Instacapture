package com.tarek360.sample;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import butterknife.BindView;
import butterknife.OnClick;
import com.google.android.gms.maps.MapView;

public class MapViewSampleActivity extends BaseSampleActivity {

  @BindView(R.id.toolbar) public Toolbar toolbar;
  @BindView(R.id.mapView) public MapView mapView;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_map_view);

    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    mapView.onCreate(savedInstanceState);
  }

  @OnClick(R.id.fab) public void onClickFAB(View view) {
    captureScreenshot(view);
  }

  @Override protected void onResume() {
    super.onResume();
    mapView.onResume();
  }

  @Override protected void onPause() {
    mapView.onPause();
    super.onPause();
  }

  @Override protected void onDestroy() {
    mapView.onDestroy();
    super.onDestroy();
  }

  @Override protected void onSaveInstanceState(final Bundle outState) {
    if (mapView != null) {
      mapView.onSaveInstanceState(outState);
    }

    super.onSaveInstanceState(outState);
  }

  @Override public void onLowMemory() {
    if (mapView != null) {
      mapView.onLowMemory();
    }

    super.onLowMemory();
  }
}
