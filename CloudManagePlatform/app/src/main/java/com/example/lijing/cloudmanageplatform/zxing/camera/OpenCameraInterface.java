package com.example.lijing.cloudmanageplatform.zxing.camera;

import android.annotation.SuppressLint;
import android.hardware.Camera;
import android.util.Log;

import static android.hardware.Camera.getNumberOfCameras;

/**
 * Created by lijing on 2019/2/20.
 */

public final class OpenCameraInterface {

    private static final String TAG = OpenCameraInterface.class.getName();

    private OpenCameraInterface() {
    }

    @SuppressLint("NewApi")
    public static Camera open(int cameraId) {

        int numCameras = getNumberOfCameras();
        Log.e("TAB",""+numCameras);
        if (numCameras == 0) {
            Log.w(TAG, "No cameras!");
            return null;
        }

        boolean explicitRequest = cameraId >= 0;
        Log.e("TAB","11111111111111"+"  qqqqqqq");
        int index = 0;
        if (!explicitRequest) {
            // Select a camera if no explicit camera requested
            Log.e("TAB","11111111111111"+"  "+index);
            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
            for (index=0;index < numCameras;index++) {
                Camera.getCameraInfo(index, cameraInfo);
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                    break;
                }
            }
            cameraId = index;
        }
        Log.e("TAB","2222222222222"+"    "+cameraId);
        Camera camera=null;
        if (cameraId < numCameras) {
            Log.i(TAG, "Opening camera #" + cameraId);
            camera = Camera.open(cameraId);
;           //camera = Camera.open();
        } else {
            if (explicitRequest) {
                Log.w(TAG, "Requested camera does not exist: " + cameraId);
                camera = null;
            } else {
                Log.i(TAG, "No camera facing back; returning camera #0");
                camera = Camera.open(0);
            }
        }

        return camera;
    }


    /**
     * Opens a rear-facing camera with {@link Camera#open(int)}, if one exists, or opens camera 0.
     *
     * @return handle to {@link Camera} that was opened
     */
    public static Camera open() {
        return open(-1);
    }

}
