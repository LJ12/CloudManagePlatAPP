package com.example.lijing.cloudmanageplatform.zxing.camera;

/**
 * Created by lijing on 2019/2/20.
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.hardware.Camera;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.example.lijing.cloudmanageplatform.zxing.android.PreferencesActivity;

import java.lang.reflect.Method;

/**
 * Camera参数配置
 */
final class CameraConfigurationManager {

    private static final String TAG = "CameraConfiguration";

    private final Context context;
    private Point screenResolution;
    private Point cameraResolution;

    CameraConfigurationManager(Context context) {
        this.context = context;
    }

    /**
     * Reads, one time, values from the camera that are needed by the app.
     */
    @SuppressLint("NewApi")
    void initFromCameraParameters(Camera camera) {
        Camera.Parameters parameters = camera.getParameters();
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point theScreenResolution = new Point(display.getWidth(),
                display.getHeight());
        screenResolution = theScreenResolution;
        Log.i(TAG, "Screen resolution: " + screenResolution);

        /************** 竖屏更改4 ******************/
        Point screenResolutionForCamera = new Point();
        screenResolutionForCamera.x = screenResolution.x;
        screenResolutionForCamera.y = screenResolution.y;

        // preview size is always something like 480*320, other 320*480
        if (screenResolution.x < screenResolution.y) {
            screenResolutionForCamera.x = screenResolution.y;
            screenResolutionForCamera.y = screenResolution.x;
        }

        cameraResolution = CameraConfigurationUtils.findBestPreviewSizeValue(
                parameters, screenResolutionForCamera);
        Log.i(TAG, "Camera resolution: " + cameraResolution);

    }

    void setDesiredCameraParameters(Camera camera, boolean safeMode) {
        Camera.Parameters parameters = camera.getParameters();

        if (parameters == null) {
            Log.w(TAG,
                    "Device error: no camera parameters are available. Proceeding without configuration.");
            return;
        }

        Log.i(TAG, "Initial camera parameters: " + parameters.flatten());

        if (safeMode) {
            Log.w(TAG,
                    "In camera config safe mode -- most settings will not be honored");
        }

        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);

        CameraConfigurationUtils.setFocus(parameters, prefs.getBoolean(
                PreferencesActivity.KEY_AUTO_FOCUS, true), prefs.getBoolean(
                PreferencesActivity.KEY_DISABLE_CONTINUOUS_FOCUS, true),
                safeMode);

        parameters.setPreviewSize(cameraResolution.x, cameraResolution.y);
        /****************** 竖屏更改2 *********************/
        setDisplayOrientation(camera, 90);

        Log.i(TAG, "Final camera parameters: " + parameters.flatten());

        camera.setParameters(parameters);

        Camera.Parameters afterParameters = camera.getParameters();
        Camera.Size afterSize = afterParameters.getPreviewSize();
        if (afterSize != null
                && (cameraResolution.x != afterSize.width || cameraResolution.y != afterSize.height)) {
            Log.w(TAG, "Camera said it supported preview size "
                    + cameraResolution.x + 'x' + cameraResolution.y
                    + ", but after setting it, preview size is "
                    + afterSize.width + 'x' + afterSize.height);
            cameraResolution.x = afterSize.width;
            cameraResolution.y = afterSize.height;
        }
    }

    void setDisplayOrientation(Camera camera, int angle) {

        Method method;
        try {
            method = camera.getClass().getMethod("setDisplayOrientation",
                    new Class[] { int.class });
            if (method != null)
                method.invoke(camera, new Object[] { angle });
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    Point getCameraResolution() {
        return cameraResolution;
    }

    Point getScreenResolution() {
        return screenResolution;
    }

}
