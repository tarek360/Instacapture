package com.tarek360.sample;

/**
 * Created by tarek on 10/3/16.
 */

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.Surface;
import android.view.TextureView;
import android.view.TextureView.SurfaceTextureListener;
import android.view.View;
import butterknife.BindView;
import butterknife.OnClick;
import java.io.IOException;

import static android.Manifest.permission.CAMERA;

public class TextureViewSampleActivity extends BaseSampleActivity {
  private final static int CAMERA_RESULT = 103;

  @BindView(R.id.textureView) TextureView textureView;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_texture_view_sample);

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      String[] permissions = new String[1];
      permissions[0] = CAMERA;
      requestPermissions(permissions, CAMERA_RESULT);
    } else {
      previewCamera();
    }
  }

  @OnClick(R.id.fab) public void onClickFAB(View view) {
    captureScreenshot();
  }
  private void previewCamera() {
    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
      textureView.setSurfaceTextureListener(new CameraSurfaceTextureListener(this));
    }
  }

  @Override public void onRequestPermissionsResult(int requestCode, String[] permissions,
      int[] grantResults) {

    switch (requestCode) {
      case CAMERA_RESULT:
        if (hasPermission(CAMERA)) {
          previewCamera();
        }
        break;
    }
  }

  /**
   * method that will return whether the permission is accepted. By default it is true if the user
   * is using a device below
   * version 23
   */
  private boolean hasPermission(String permission) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
    }
    return true;
  }

  @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH) class CameraSurfaceTextureListener
      implements SurfaceTextureListener {
    private Camera mCamera;
    private Activity mActivity;
    private CameraInfo mBackCameraInfo;

    public CameraSurfaceTextureListener(Activity activity) {
      mActivity = activity;
    }

    @Override public void onSurfaceTextureUpdated(SurfaceTexture surface) {
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
    }

    @Override public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
      if (mCamera != null) {
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
      }
      return true;
    }

    @Override public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {

      Pair<CameraInfo, Integer> backCamera = getBackCamera();
      final int backCameraId = backCamera.second;
      mBackCameraInfo = backCamera.first;
      mCamera = Camera.open(backCameraId);
      cameraDisplayRotation();

      try {
        mCamera.setPreviewTexture(surface);
        mCamera.startPreview();
      } catch (IOException ioe) {
        // Something bad happened
      }
    }

    public void cameraDisplayRotation() {
      final int rotation = mActivity.getWindowManager().getDefaultDisplay().getRotation();
      int degrees = 0;
      switch (rotation) {
        case Surface.ROTATION_0:
          degrees = 0;
          break;
        case Surface.ROTATION_90:
          degrees = 90;
          break;
        case Surface.ROTATION_180:
          degrees = 180;
          break;
        case Surface.ROTATION_270:
          degrees = 270;
          break;
      }

      final int displayOrientation = (mBackCameraInfo.orientation - degrees + 360) % 360;
      mCamera.setDisplayOrientation(displayOrientation);
    }

    private Pair<CameraInfo, Integer> getBackCamera() {
      CameraInfo cameraInfo = new CameraInfo();
      final int numberOfCameras = Camera.getNumberOfCameras();

      for (int i = 0; i < numberOfCameras; ++i) {
        Camera.getCameraInfo(i, cameraInfo);
        if (cameraInfo.facing == CameraInfo.CAMERA_FACING_BACK) {
          return new Pair<>(cameraInfo, i);
        }
      }
      return null;
    }
  }
}
