package com.tarek360.sample

import android.graphics.Bitmap
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v7.app.AppCompatActivity
import android.view.View
import butterknife.ButterKnife
import com.tarek360.instacapture.Instacapture
import com.tarek360.instacapture.listener.SimpleScreenCapturingListener
import com.tarek360.sample.uncapturableViews.AlertDialogFragment
import com.tarek360.sample.utility.Utility
import rx.android.schedulers.AndroidSchedulers

abstract class BaseSampleActivity : AppCompatActivity(), AlertDialogFragment.OnAlertDialogListener {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    Instacapture.enableLogging(true)
  }

  override fun setContentView(@LayoutRes layoutResID: Int) {
    super.setContentView(layoutResID)
    ButterKnife.bind(this)
  }

  protected fun showAlertDialog() {
    AlertDialogFragment.newInstance(R.string.dialog_title, R.string.dialog_message)
        .show(supportFragmentManager, "dialogFragment")
  }

  protected fun captureScreenshot(vararg ignoredViews: View) {
    Instacapture.capture(this, object : SimpleScreenCapturingListener() {
      override fun onCaptureComplete(bitmap: Bitmap) {

        Utility.getScreenshotFileObservable(this@BaseSampleActivity, bitmap)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { file ->
              startActivity(ShowScreenShotActivity.buildIntent(this@BaseSampleActivity,
                  file.absolutePath))
            }
      }
    }, *ignoredViews)

  }

  override fun onPositiveButtonClick() {
    captureScreenshot()
  }
}
