package com.tarek360.sample;

import android.view.View;
import butterknife.OnClick;

public class MapAndDialogSampleActivity extends MapFragmentSampleActivity {

  @OnClick(R.id.fab) @Override public void onClickFAB(View view) {
    showAlertDialog();
  }
}
