package com.tarek360.sample;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DialogAndMenuSampleActivity extends BaseSampleActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_dialog_and_menu_sample);
    ButterKnife.bind(this);
  }

  @OnClick(R.id.show_dialog) protected void showDialog() {
    showAlertDialog();
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu, menu);

    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {

    captureScreenshot();
    return true;
  }
}
