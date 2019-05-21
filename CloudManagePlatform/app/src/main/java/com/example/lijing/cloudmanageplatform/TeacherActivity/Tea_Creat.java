package com.example.lijing.cloudmanageplatform.TeacherActivity;

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
 * Created by lijing on 2019/3/1.
 */

public class Tea_Creat extends AppCompatActivity {
    private List list = new ArrayList<String>();
    ListView LV;
    String url;
    String userID;
    String temp="";
    double La,Lo;
    int option;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_delete_course);
        url =getString(R.string.url)+"/LookUserSelectCourse?";
        option=getIntent().getIntExtra("option",1);
        if(option==1) {
            La = getIntent().getDoubleExtra("Latitude", 0);
            Lo = getIntent().getDoubleExtra("Longitude", 0);
        }
        userID = getIntent().getStringExtra("UserID");
        LV = (ListView) findViewById(R.id.LV_UDC_1);
        Button button = (Button) findViewById(R.id.B_UDC_1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i ;
                temp="";
                String jieshu="";
                String name="";
                for (i = 0; i < LV.getChildCount(); i++) {
                    LinearLayout ll = (LinearLayout)LV.getChildAt(i);// 获得子级
                    CheckBox CB = (CheckBox) ll.findViewById(R.id.CB_DCI_1);// 从子级中获得控件
                    if(CB.isChecked()){
                        if(temp.equals("")){
                            temp=list.get(i).toString().split(" ")[0];
                            name=list.get(i).toString().split(" ")[1];
                            jieshu = list.get(i).toString().split(" ")[3];
                        }else {
                            temp="";
                            Toast.makeText(Tea_Creat.this,"每次查询只能选择一个课程",LENGTH_SHORT).show();
                            break;
                        }
                    }
                }
                if(temp.isEmpty())
                    Toast.makeText(Tea_Creat.this,"您还未选择课程",LENGTH_SHORT).show();
                else if(!temp.isEmpty()&&i==LV.getChildCount()&&option==1){
                    Intent intent = new Intent(Tea_Creat.this,Tea_Get_Params.class);
                    intent.putExtra("UserID",userID);
                    intent.putExtra("CourseID",temp);
                    intent.putExtra("CourseName",name);
                    intent.putExtra("jieshu",jieshu);
                    intent.putExtra("La",La);
                    intent.putExtra("Lo",Lo);
                    startActivity(intent);
                    finish();
                }
                else if(!temp.isEmpty()&&i==LV.getChildCount()&&option==2){
                    Intent intent=new Intent(Tea_Creat.this,History_QRcode.class);
                    intent.putExtra("UserID",userID);
                    intent.putExtra("CourseID",temp);
                    intent.putExtra("CourseName",name);
                    startActivity(intent);
                    finish();
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
            if (msg.what == 15) {
                try {
                    jsonObject = new JSONObject(msg.getData().getString("mes"));
                    if (jsonObject.getString("Result").equals("success")) {
                        list = Arrays.asList(jsonObject.getString("Data").split("\t"));
                        UserDeleteCourseAdapter adapter = new UserDeleteCourseAdapter(Tea_Creat.this, list);
                        LV.setAdapter(adapter);
                    } else {
                        Toast.makeText(Tea_Creat.this, jsonObject.getString("Data"), Toast.LENGTH_SHORT).show();
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
            String mes = infer.Net(url, params,Tea_Creat.this);
            if (mes.split(":").length < 2) {
                Looper.prepare();
                Toast.makeText(Tea_Creat.this, mes, Toast.LENGTH_SHORT).show();
                Looper.loop();
            } else {
                Message msg = Message.obtain();
                msg.what = 15;
                Bundle bundle = new Bundle();
                bundle.putString("mes", mes);
                msg.setData(bundle);
                myHandle.sendMessage(msg);
            }
        }
    });
}
