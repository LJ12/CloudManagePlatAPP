package com.example.lijing.cloudmanageplatform.StudentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lijing.cloudmanageplatform.ChangeEmil;
import com.example.lijing.cloudmanageplatform.ChangePassword;
import com.example.lijing.cloudmanageplatform.InsertCourse;
import com.example.lijing.cloudmanageplatform.Look_Course;
import com.example.lijing.cloudmanageplatform.R;
import com.example.lijing.cloudmanageplatform.UserDeleteCourse;
import com.example.lijing.cloudmanageplatform.infer;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lijing on 2019/2/18.
 */

public class Stu_Home_Page extends AppCompatActivity implements View.OnClickListener {
    boolean flag1 = false, flag2 = false, flag3 = false, flag4 = false;
    String userID;
    String userName;
    String userEmil;
    String url;
    int sum, lated_num, un_attendance_num;
    PieChart PC_1, PC_2;
    TextView TV_1, TV_2, TV_3;
    String content;
    double Latitude;
    double Longitude;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stu_home_page);
        url = getString(R.string.url)+"/StudentSign?";
        userID = getIntent().getStringExtra("ID");
        userName = getIntent().getStringExtra("Name");
        userEmil = getIntent().getStringExtra("Emil");
        sum = Integer.parseInt(getIntent().getStringExtra("Sum"));
        lated_num = Integer.parseInt(getIntent().getStringExtra("lated_num"));
        un_attendance_num = Integer.parseInt(getIntent().getStringExtra("un_attendance_num"));

        TextView TV_name = (TextView) findViewById(R.id.TV_H_name);
        TV_name.setText(userName + "，您好");

        TextView TV;
        TV_1 = (TextView) findViewById(R.id.TV_H_data1);
        TV_1.setText("应出勤课程数目：" + sum);
        TV_2 = (TextView) findViewById(R.id.TV_H_data2);
        TV_2.setText("已出勤课程数目：" + (sum - un_attendance_num));
        TV_3 = (TextView) findViewById(R.id.TV_H_data3);
        TV_3.setText("已出勤课程数目：" + (sum - un_attendance_num));
        TV = (TextView) findViewById(R.id.TV_H_data4);
        TV.setText("迟到的课程数目：" + lated_num);

        TV = (TextView) findViewById(R.id.TV_H1);
        TV.setOnClickListener(this);

        TV = (TextView) findViewById(R.id.TV_H2);
        TV.setOnClickListener(this);

        TV = (TextView) findViewById(R.id.TV_H3);
        TV.setOnClickListener(this);

        TV = (TextView) findViewById(R.id.TV_H4);
        TV.setOnClickListener(this);

        Button B_1 = (Button) findViewById(R.id.B_H1);
        B_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        PC_1 = (PieChart) findViewById(R.id.PC_H1);
        PC_1.setUsePercentValues(true);
        if (sum == 0)
            paintPieChat(PC_1, (float) (100.00), "出勤率", (float) (0), "缺课率");
        else
            paintPieChat(PC_1, (float) (1.0 - 1.0 * un_attendance_num / sum), "出勤率", (float) (1.0 * un_attendance_num / sum), "缺课率");

        PC_2 = (PieChart) findViewById(R.id.PC_H2);
        PC_2.setUsePercentValues(true);
        if (sum - un_attendance_num == 0)
            paintPieChat(PC_2, (float) (100.00), "未迟到率", (float) (0), "迟到率");
        else
            paintPieChat(PC_2, (float) (1.0 - 1.0 * lated_num / (sum - un_attendance_num)), "未迟到率", (float) (1.0 * lated_num / (sum - un_attendance_num)), "迟到率");
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        TextView TV_temp;
        switch (v.getId()) {
            case R.id.TV_H1:  //个人信息
                if (flag1) {
                    flag1 = false;
                    TV_temp = (TextView) findViewById(R.id.TV_H1_2);
                    TV_temp.setVisibility(View.GONE);
                    TV_temp = (TextView) findViewById(R.id.TV_H1_3);
                    TV_temp.setVisibility(View.GONE);
                } else {
                    flag1 = true;
                    TV_temp = (TextView) findViewById(R.id.TV_H1_2);
                    TV_temp.setVisibility(View.VISIBLE);
                    TV_temp.setOnClickListener(this);
                    TV_temp = (TextView) findViewById(R.id.TV_H1_3);
                    TV_temp.setVisibility(View.VISIBLE);
                    TV_temp.setOnClickListener(this);
                }
                break;
            case R.id.TV_H2:  //课程管理
                if (flag2) {
                    flag2 = false;
                    TV_temp = (TextView) findViewById(R.id.TV_H2_1);
                    TV_temp.setVisibility(View.GONE);
                    TV_temp = (TextView) findViewById(R.id.TV_H2_2);
                    TV_temp.setVisibility(View.GONE);
                    TV_temp = (TextView) findViewById(R.id.TV_H2_3);
                    TV_temp.setVisibility(View.GONE);

                } else {
                    flag2 = true;
                    TV_temp = (TextView) findViewById(R.id.TV_H2_1);
                    TV_temp.setVisibility(View.VISIBLE);
                    TV_temp.setOnClickListener(this);
                    TV_temp = (TextView) findViewById(R.id.TV_H2_2);
                    TV_temp.setVisibility(View.VISIBLE);
                    TV_temp.setOnClickListener(this);
                    TV_temp = (TextView) findViewById(R.id.TV_H2_3);
                    TV_temp.setVisibility(View.VISIBLE);
                    TV_temp.setOnClickListener(this);
                }
                break;
            case R.id.TV_H3:  //签到
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                            GetLocation getLocation = new GetLocation(this);
                            if (getLocation.getLocation() != null) {
                                intent = new Intent(this, Stu_Sign_Activity.class);
                                Latitude=getLocation.getLocation().getLatitude();
                                Longitude=getLocation.getLocation().getLongitude();
                                startActivityForResult(intent, 30);
                            } else {
                                Toast.makeText(Stu_Home_Page.this, "手机GPS功能未打开，请先打开GPS功能", Toast.LENGTH_SHORT).show();
                            }
                        } else
                            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 3);
                    } else
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 2);

                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
                }
                break;
            case R.id.TV_H4:  //考勤情况
                intent = new Intent(this, Stu_Attendance.class);
                intent.putExtra("UserID", userID);
                intent.putExtra("Type", "Student");
                startActivity(intent);
                break;

            case R.id.TV_H1_2: //更改密码
                intent = new Intent(this, ChangePassword.class);
                intent.putExtra("UserID", userID);
                intent.putExtra("UserType", "Student");
                startActivity(intent);
                break;
            case R.id.TV_H1_3: //更改邮箱
                intent = new Intent(this, ChangeEmil.class);
                intent.putExtra("UserID", userID);
                intent.putExtra("UserEmil", userEmil);
                intent.putExtra("UserType", "Student");
                startActivityForResult(intent, 100);
                break;
            case R.id.TV_H2_1: //添加课程
                intent = new Intent(this, InsertCourse.class);
                intent.putExtra("UserID", userID);
                intent.putExtra("UserType", "Student");
                startActivity(intent);
                break;
            case R.id.TV_H2_2:
                intent = new Intent(this, Look_Course.class);
                intent.putExtra("UserID", userID);
                startActivity(intent);
                break;
            case R.id.TV_H2_3:
                intent = new Intent(this, UserDeleteCourse.class);
                intent.putExtra("UserID", userID);
                startActivity(intent);
                break;

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Intent intent;
        switch (requestCode) {
            case 1:
                if (permissions[0].equals(Manifest.permission.CAMERA)) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                                GetLocation getLocation = new GetLocation(this);
                                if (getLocation.getLocation() != null) {
                                    intent = new Intent(this, Stu_Sign_Activity.class);
                                    Latitude=getLocation.getLocation().getLatitude();
                                    Longitude=getLocation.getLocation().getLongitude();
                                    startActivityForResult(intent, 30);
                                } else {
                                    Toast.makeText(Stu_Home_Page.this, "手机GPS功能未打开，请先打开GPS功能", Toast.LENGTH_SHORT).show();
                                }
                            } else
                                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 3);
                        } else
                            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 2);
                    } else {
                        Toast.makeText(Stu_Home_Page.this, "手机GPS功能未打开，请先打开GPS功能", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Stu_Home_Page.this, "未获得打开相机权限", Toast.LENGTH_SHORT).show();
                }
                break;
            case 2:
                if (permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        GetLocation getLocation = new GetLocation(this);
                        if (getLocation.getLocation() != null) {
                            intent = new Intent(this, Stu_Sign_Activity.class);
                            Latitude=getLocation.getLocation().getLatitude();
                            Longitude=getLocation.getLocation().getLongitude();
                            startActivityForResult(intent, 30);
                        } else {
                            Toast.makeText(Stu_Home_Page.this, "手机GPS功能未打开，请先打开GPS功能", Toast.LENGTH_SHORT).show();
                        }
                    } else
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 3);
                }
                else
                    Toast.makeText(Stu_Home_Page.this, "未获得获得位置权限", Toast.LENGTH_SHORT).show();
                break;
            case 3:
                if (permissions[0].equals(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                    GetLocation getLocation = new GetLocation(this);
                    if (getLocation.getLocation() != null) {
                        intent = new Intent(this, Stu_Sign_Activity.class);
                        Latitude=getLocation.getLocation().getLatitude();
                        Longitude=getLocation.getLocation().getLongitude();
                        startActivityForResult(intent, 30);
                    } else {
                        Toast.makeText(Stu_Home_Page.this, "手机GPS功能未打开，请先打开GPS功能", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(Stu_Home_Page.this, "未获得获得位置权限", Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 30 && resultCode == RESULT_OK) {
            if (data != null) {
                content = data.getStringExtra("codedContent");
                sign();
            }
        }
        if (requestCode == 100 && resultCode == RESULT_OK) {
            if (data != null) {
                userEmil = data.getStringExtra("ChangedEmil");
            }
        }
    }

    void paintPieChat(PieChart PC, float num_1, String name_1, float num_2, String name_2) {
        List<PieEntry> strings = new ArrayList<>();
        strings.add(new PieEntry(num_1, name_1));
        strings.add(new PieEntry(num_2, name_2));

        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(getResources().getColor(R.color.colorBule));
        colors.add(getResources().getColor(R.color.colorGreen));

        PieDataSet dataSet = new PieDataSet(strings, "");
        dataSet.setColors(colors);
        PieData pieData = new PieData(dataSet);

        Legend mLegend = PC.getLegend();
        mLegend.setEnabled(false);//设置禁用比例块

        PC.setData(pieData);
        PC.setUsePercentValues(true);
        PC.getDescription().setEnabled(false);
        PC.setEntryLabelTextSize(7);
        PC.setEntryLabelColor(R.color.colorBlack);
        //  PC.setDrawHoleEnabled(false);
        PC.invalidate();
    }

    protected Handler myHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            JSONObject jsonObject = null;
            String data = "";
            if (msg.what == 17) {
                try {
                    jsonObject = new JSONObject(msg.getData().getString("mes"));
                    Toast.makeText(Stu_Home_Page.this, jsonObject.getString("Data"), Toast.LENGTH_SHORT).show();
                    if (jsonObject.getString("Data").equals("签到成功")) {
                        sum = sum + 1;
                    } else if (jsonObject.getString("Data").equals("签到成功，您已经迟到")) {
                        sum = sum + 1;
                        lated_num = lated_num + 1;
                    }
                    if (sum == 0)
                        paintPieChat(PC_1, (float) (100.00), "出勤率", (float) (0), "缺课率");
                    else
                        paintPieChat(PC_1, (float) (1.0 - 1.0 * un_attendance_num / sum), "出勤率", (float) (1.0 * un_attendance_num / sum), "缺课率");
                    if (sum - un_attendance_num == 0)
                        paintPieChat(PC_2, (float) (100.00), "未迟到率", (float) (0), "迟到率");
                    else
                        paintPieChat(PC_2, (float) (1.0 - 1.0 * lated_num / (sum - un_attendance_num)), "未迟到率", (float) (1.0 * lated_num / (sum - un_attendance_num)), "迟到率");
                    TV_1.setText("应出勤课程数目：" + sum);
                    TV_2.setText("已出勤课程数目：" + (sum - un_attendance_num));
                    TV_3.setText("已出勤课程数目：" + (sum - un_attendance_num));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    };

    private void sign(){
        Runnable runnable = new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void run() {
                String params = "UserID=" + userID + "&Latitude="+Latitude+"&Longitude="+Longitude+content;
                String mes = infer.Net(url, params, Stu_Home_Page.this);
                if (mes.split(":").length < 2) {
                    Looper.prepare();
                    Toast.makeText(Stu_Home_Page.this, mes, Toast.LENGTH_SHORT).show();
                    Looper.loop();
                } else {
                    Message msg = Message.obtain();
                    msg.what = 17;
                    Bundle bundle = new Bundle();
                    bundle.putString("mes", mes);
                    msg.setData(bundle);
                    myHandle.sendMessage(msg);
                }
            }
        };
        Thread thread=new Thread(runnable);
        thread.start();
    }

}
