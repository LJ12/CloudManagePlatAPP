package com.example.lijing.cloudmanageplatform.StudentActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

/**
 * Created by lijing on 2019/4/14.
 */

public class GetLocation implements LocationListener {
    Context context;
    private int GPS_REQUEST_CODE = 10;

    public GetLocation(Context context) {
        this.context = context;
    }

    public Location getLocation() {
        LocationManager LM = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }
        boolean isGPSOpen = LM.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (isGPSOpen) {
            LM.requestLocationUpdates(LocationManager.GPS_PROVIDER, 6000, 1, this);
            Location location = LM.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            return location;
        } else {
            return null;
        }
    }
        //检测GPS是否打开
    private boolean checkGPSIsOpen() {
        boolean isOpen;
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        isOpen = locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER);
        return isOpen;
    }
    /*
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 1:
                if (permissions[0].equals(Manifest.permission.CAMERA)) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    } else {
                        Toast.makeText(context,"未获得打开相机权限",Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }
*/
    /**
     * 跳转GPS设置
     */
 /*   private void openGPSSettings() {
        if (checkGPSIsOpen()) {
            LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            LocationManager LM = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            LM.requestLocationUpdates(LocationManager.GPS_PROVIDER, 6000, 10, this);
            Location location = LM.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            return location;
        } else {
            //没有打开则弹出对话框
            new AlertDialog.Builder(context)
                    .setTitle("权限申请")
                    .setMessage("请打开手机的GPS功能")
                    // 拒绝, 退出应用
                    .setNegativeButton("取消",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                 // return null;
                                }
                            })

                    .setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //跳转GPS设置界面
                                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    //startActivityForResult(intent, GPS_REQUEST_CODE);
                                }
                            })

                    .setCancelable(false)
                    .show();

        }
    }*/
    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
