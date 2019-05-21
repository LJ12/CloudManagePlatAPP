package com.example.lijing.cloudmanageplatform.StudentActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.lijing.cloudmanageplatform.R;
import com.example.lijing.cloudmanageplatform.UserDeleteCourseAdapter;
import com.example.lijing.cloudmanageplatform.infer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;

/**
 * Created by lijing on 2019/2/28.
 */

public class Stu_Attendance extends AppCompatActivity {
    private List list = new ArrayList<String>();
    ListView LV;
    String url;
    String userID;
    String temp="";
    String Type;
     String jieshu_sum="0";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_delete_course);
        url =getString(R.string.url)+"/LookUserSelectCourse?";
        userID = getIntent().getStringExtra("UserID");
        Type =  getIntent().getStringExtra("Type");
        LV = (ListView) findViewById(R.id.LV_UDC_1);
        Button button = (Button) findViewById(R.id.B_UDC_1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i ,select;
                temp="";
                CheckBox CB;
                String Name="";
                for (i = 0; i < LV.getChildCount(); i++) {
                    LinearLayout ll = (LinearLayout)LV.getChildAt(i);// 获得子级
                     CB = (CheckBox) ll.findViewById(R.id.CB_DCI_1);// 从子级中获得控件
                    if(CB.isChecked()){
                        if(temp.equals("")){
                            select=i;
                            temp=list.get(i).toString().split(" ")[0];
                            Name=list.get(i).toString().split(" ")[1];
                            jieshu_sum  = list.get(i).toString().split(" ")[3];
                        }else {
                            temp="";
                            Toast.makeText(Stu_Attendance.this,"每次查询只能选择一个课程",LENGTH_SHORT).show();
                            return;
                        }
                    }
                }
                if(temp.isEmpty())
                    Toast.makeText(Stu_Attendance.this,"您还未选择课程",LENGTH_SHORT).show();
                else if(!temp.isEmpty()&&i==LV.getChildCount()){
                    Intent intent = new Intent(Stu_Attendance.this,Stu_Attendance_Show_Result.class);
                    intent.putExtra("UserID",userID);
                    intent.putExtra("CourseID",temp);
                    intent.putExtra("CourseName",Name);
                    intent.putExtra("Type",Type);
                    intent.putExtra("jieshu_sum",jieshu_sum);
                    startActivity(intent);
                }
            }
        });

        button = (Button) findViewById(R.id.B_UDC_2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        thread.start();
    }

    protected Handler myHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            JSONObject jsonObject = null;
            if (msg.what == 13) {
                try {
                    jsonObject = new JSONObject(msg.getData().getString("mes"));
                    if (jsonObject.getString("Result").equals("success")) {
                        list = Arrays.asList(jsonObject.getString("Data").split("\t"));
                        UserDeleteCourseAdapter adapter = new UserDeleteCourseAdapter(Stu_Attendance.this, list);
                        LV.setAdapter(adapter);
                    } else {
                        Toast.makeText(Stu_Attendance.this, jsonObject.getString("Data"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    };

    Thread thread = new Thread(new Runnable() {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void run() {

            String params = null;
            params = "ID=" + userID;
            String mes = infer.Net(url, params,Stu_Attendance.this);
            if(mes.split(":").length<2){
                Looper.prepare();
                Toast.makeText(Stu_Attendance.this,mes,Toast.LENGTH_SHORT).show();
                Looper.loop();
            }else {
                Message msg = Message.obtain();
                msg.what = 13;
                Bundle bundle = new Bundle();
                bundle.putString("mes", mes);
                msg.setData(bundle);
                myHandle.sendMessage(msg);
            }
        }
    });
}
