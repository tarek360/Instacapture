package com.tarek360.sample;

import android.os.Bundle;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DialogSampleActivity extends BaseSampleActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_dialog_sample);
    ButterKnife.bind(this);
  }

  @OnClick(R.id.show_dialog) protected void showDialog() {
    showAlertDialog();
  }
}
