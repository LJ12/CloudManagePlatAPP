package com.example.lijing.cloudmanageplatform.TeacherActivity;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.lijing.cloudmanageplatform.R;
import com.example.lijing.cloudmanageplatform.infer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by lijing on 2019/4/22.
 */

public class History_QRcode extends AppCompatActivity {
    String teacherID,CourseID,CourseName;
    String url;
    List<String> list=new ArrayList<>();
    ListView LV;
    int jieshu=0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_qrcode);
        url=getString(R.string.url)+"/GetCourseJieshuSum?";
        teacherID=getIntent().getStringExtra("UserID");
        CourseID=getIntent().getStringExtra("CourseID");
        CourseName=getIntent().getStringExtra("CourseName");
        LV=(ListView)findViewById(R.id.LV_HQ_1);
        ImageView IV=(ImageView)findViewById(R.id.IV_HQ_1);
        IV.setOnClickListener(new View.OnClickListener() {
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
            if (msg.what == 62) {
                try {
                    jsonObject = new JSONObject(msg.getData().getString("mes"));
                    if (jsonObject.getString("Result").equals("success")) {
                        list= Arrays.asList(jsonObject.getString("Data").split("\n"));
                        History_QRcode_Adapter adapter=new History_QRcode_Adapter(History_QRcode.this,list);
                        LV.setAdapter(adapter);
                    } else {
                         Toast.makeText(History_QRcode.this,jsonObject.getString("Data"),Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    };
    Thread thread=new Thread(new Runnable() {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void run() {
            String mes= infer.Net(url,"teacherID="+teacherID+"&CourseID="+CourseID+"&CourseName="+CourseName,History_QRcode.this);
            if(mes.split(":").length<2){
                Looper.prepare();
                Toast.makeText(History_QRcode.this,mes,Toast.LENGTH_SHORT).show();
                Looper.loop();
            }else {
                Message msg = Message.obtain();
                msg.what = 62;
                Bundle bundle = new Bundle();
                bundle.putString("mes", mes);
                msg.setData(bundle);
                myHandle.sendMessage(msg);
            }
        }
    });
}
