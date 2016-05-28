package com.tarek360.sample;

import butterknife.OnClick;

public class MapAndDialogSampleActivity extends MapFragmentSampleActivity {

  @OnClick(R.id.fab) @Override public void onClickFAB() {
    showAlertDialog();
  }
}
