package com.example.lijing.cloudmanageplatform.TeacherActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.lijing.cloudmanageplatform.R;
import com.example.lijing.cloudmanageplatform.StudentActivity.GetLocation;
import com.example.lijing.cloudmanageplatform.StudentActivity.Stu_Attendance;

/**
 * Created by lijing on 2019/3/25.
 */

public class Tea_Home_Page_Attendence extends Fragment {
    LinearLayout LL_1,LL_2,LL_3;
    String ID;

    public Tea_Home_Page_Attendence(String ID){
        this.ID=ID;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tea_home_page_attendence, container, false);
        LL_1=(LinearLayout)view.findViewById(R.id.LL_THPA_2);
        LL_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        GetLocation getLocation = new GetLocation(getContext());
                        if (getLocation.getLocation() != null) {
                            Intent intent=new Intent(getContext(), Tea_Creat.class);
                            intent.putExtra("UserID",ID);
                            intent.putExtra("Latitude",getLocation.getLocation().getLatitude());
                            intent.putExtra("Longitude",getLocation.getLocation().getLongitude());
                            intent.putExtra("option",1);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getContext(), "手机GPS功能未打开，请先打开GPS功能", Toast.LENGTH_SHORT).show();
                        }
                    } else
                        ActivityCompat.requestPermissions((Activity) getContext(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 5);
                } else
                    ActivityCompat.requestPermissions((Activity) getContext(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 4);

            }
        });
        LL_2=(LinearLayout)view.findViewById(R.id.LL_THPA_3);
        LL_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), Stu_Attendance.class);
                intent.putExtra("UserID",ID);
                intent.putExtra("Type","Teacher");
                startActivity(intent);
            }
        });
        LL_3=(LinearLayout)view.findViewById(R.id.LL_THPA_4);
        LL_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), Tea_Creat.class);
                intent.putExtra("UserID",ID);
                intent.putExtra("option",2);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 4:
                if (permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        GetLocation getLocation = new GetLocation(getContext());
                        if (getLocation.getLocation() != null) {
                            Intent intent=new Intent(getContext(), Tea_Creat.class);
                            intent.putExtra("UserID",ID);
                            intent.putExtra("Latitude",getLocation.getLocation().getLatitude());
                            intent.putExtra("Longitude",getLocation.getLocation().getLongitude());
                            intent.putExtra("option",1);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getContext(), "手机GPS功能未打开，请先打开GPS功能", Toast.LENGTH_SHORT).show();
                        }
                    } else
                        ActivityCompat.requestPermissions((Activity) getContext(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 5);
                }
                else
                    Toast.makeText(getContext(), "未获得获得位置权限", Toast.LENGTH_SHORT).show();
                break;
            case 5:
                if (permissions[0].equals(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                    GetLocation getLocation = new GetLocation(getContext());
                    if (getLocation.getLocation() != null) {
                        Intent intent=new Intent(getContext(), Tea_Creat.class);
                        intent.putExtra("UserID",ID);
                        intent.putExtra("Latitude",getLocation.getLocation().getLatitude());
                        intent.putExtra("Longitude",getLocation.getLocation().getLongitude());
                        intent.putExtra("option",1);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getContext(), "手机GPS功能未打开，请先打开GPS功能", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getContext(), "未获得获得位置权限", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
