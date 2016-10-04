package com.tarek360.sample;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

  @BindView(R.id.sample_texture_view) Button sampleTextureView;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);

    if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
      sampleTextureView.setVisibility(View.GONE);
    }
  }

  @OnClick(R.id.sample_google_map_fragment) public void sampleGoogleMapFragment() {
    startActivity(new Intent(this, MapFragmentSampleActivity.class));
  }

  @OnClick(R.id.sample_google_map_view) public void sampleGoogleMapView() {
    startActivity(new Intent(this, MapViewSampleActivity.class));
  }

  @OnClick(R.id.sample_dialog) public void sampleDialog() {
    startActivity(new Intent(this, DialogSampleActivity.class));
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

  @OnClick(R.id.sample_texture_view) public void sampleTextureView() {
    startActivity(new Intent(this, TextureViewSampleActivity.class));
  }
}
