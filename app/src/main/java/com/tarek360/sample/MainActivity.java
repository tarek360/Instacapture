package com.tarek360.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
  }

  @OnClick(R.id.sample_google_map_fragment) public void sampleGoogleMapFragment() {
    startActivity(new Intent(this, MapFragmentSampleActivity.class));
  }

  @OnClick(R.id.sample_google_map_view) public void sampleGoogleMapView() {
    startActivity(new Intent(this, MapViewSampleActivity.class));
  }

  @OnClick(R.id.sample_dialog) public void sampleDialog() {
    startActivity(new Intent(this, DialogAndMenuSampleActivity.class));
  }

  @OnClick(R.id.sample_map_dialog) public void sampleMapDialog() {
    startActivity(new Intent(this, MapAndDialogSampleActivity.class));
  }

  @OnClick(R.id.sample_ignore_views) public void sampleIgnoreViews() {
    startActivity(new Intent(this, IgnoreViewsSampleActivity.class));
  }

  @OnClick(R.id.sample_custom_view) public void sampleCustomView() {
    startActivity(new Intent(this, CustomViewSampleActivity.class));
  }

  @OnClick(R.id.sample_gl_surface_view) public void sampleGLSurfaceView() {
    startActivity(new Intent(this, GLSurfaceViewSampleActivity.class));
  }
}
