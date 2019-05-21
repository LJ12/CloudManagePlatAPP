package com.example.lijing.cloudmanageplatform;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;

/**
 * Created by lijing on 2019/2/28.
 */

public class UserDeleteCourse extends AppCompatActivity {
    private List list = new ArrayList<String>();
    ListView LV;
    String url;
    String url_delete ;
    String userID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_delete_course);
        url = getString(R.string.url)+"/LookUserSelectCourse?";
        url_delete= getString(R.string.url)+"/UserDeleteSeleteCourse?";
        userID = getIntent().getStringExtra("UserID");

        LV=(ListView)findViewById(R.id.LV_UDC_1);
        Button button = (Button) findViewById(R.id.B_UDC_1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    thread_delete.start();
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
            if (msg.what == 11) {
                try {
                    jsonObject = new JSONObject(msg.getData().getString("mes"));
                    if (jsonObject.getString("Result").equals("success")) {
                        list = Arrays.asList(jsonObject.getString("Data").split("\t"));
                        UserDeleteCourseAdapter adapter = new UserDeleteCourseAdapter(UserDeleteCourse.this, list);
                        LV.setAdapter(adapter);
                    } else {
                        Toast.makeText(UserDeleteCourse.this, jsonObject.getString("Data"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else  if (msg.what == 12) {
                try {
                    jsonObject = new JSONObject(msg.getData().getString("mes"));
                    if (jsonObject.getString("Result").equals("sucess")) {
                        Toast.makeText(UserDeleteCourse.this, "删除课程成功", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(UserDeleteCourse.this, jsonObject.getString("删除课程失败"), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };
    ;

    Thread thread = new Thread(new Runnable() {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void run() {

            String params = null;
            params = "ID=" + userID;
            String mes = infer.Net(url, params,UserDeleteCourse.this);
            if(mes.split(":").length<2){
                Looper.prepare();
                Toast.makeText(UserDeleteCourse.this,mes,Toast.LENGTH_SHORT).show();
                Looper.loop();
            }else {
                Message msg = Message.obtain();
                msg.what = 11;
                Bundle bundle = new Bundle();
                bundle.putString("mes", mes);
                msg.setData(bundle);
                myHandle.sendMessage(msg);
            }
        }
    });
    Thread thread_delete = new Thread(new Runnable() {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void run() {
            String params = null;
            String temp ="";
            int i ;
            for (i = 0; i < LV.getChildCount(); i++) {
                LinearLayout ll = (LinearLayout)LV.getChildAt(i);// 获得子级
                CheckBox CB = (CheckBox) ll.findViewById(R.id.CB_DCI_1);// 从子级中获得控件
                if(CB.isChecked()){
                    if(temp.equals("")){
                        temp=list.get(i).toString().split(" ")[0];
                    }else {
                        temp=temp+","+list.get(i).toString().split(" ")[0];
                    }
                }
            }
            if(temp.equals("")){
                Looper.prepare();
                Toast.makeText(UserDeleteCourse.this, "未选择课程，请先选择要删除的课程", LENGTH_SHORT).show();
                Looper.loop();
                return;
            }
            params = "ID=" + userID+"&Select="+temp;
            String mes = infer.Net(url_delete, params,UserDeleteCourse.this);
            if(mes.split(":").length<2){
                Looper.prepare();
                Toast.makeText(UserDeleteCourse.this,mes,Toast.LENGTH_SHORT).show();
                Looper.loop();
            }else {
                Message msg = Message.obtain();
                msg.what = 12;
                Bundle bundle = new Bundle();
                bundle.putString("mes", mes);
                msg.setData(bundle);
                myHandle.sendMessage(msg);
            }
        }
    });
}
