package com.example.lijing.cloudmanageplatform;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by lijing on 2019/2/28.
 */

public class Look_Course extends AppCompatActivity {
    private List list = new ArrayList<String>();
    ;
    ListView LV_1;
    String url ;
    String userID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.look_checked_courses);
        url= getString(R.string.url)+"/LookUserSelectCourse?";
        userID = getIntent().getStringExtra("UserID");
        LV_1 = (ListView) findViewById(R.id.LV_IC_1);
        ImageView IV=(ImageView)findViewById(R.id.IV_LCC_1);
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
            if (msg.what == 10) {
                JSONObject jsonObject = null;
                try {
                    Log.e("TAB", msg.getData().getString("mes"));
                    jsonObject = new JSONObject(msg.getData().getString("mes"));
                    if (jsonObject.getString("Result").equals("success")) {
                        list = Arrays.asList(jsonObject.getString("Data").split("\t"));
                        InsertCourseAdapter adapter = new InsertCourseAdapter(Look_Course.this, list);
                        LV_1.setAdapter(adapter);
                    } else {
                        Toast.makeText(Look_Course.this, jsonObject.getString("Data"), Toast.LENGTH_SHORT).show();
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
            String mes = infer.Net(url, params,Look_Course.this);
            if(mes.split(":").length<2){
                Looper.prepare();
                Toast.makeText(Look_Course.this,mes,Toast.LENGTH_SHORT).show();
                Looper.loop();
            }else {
                Message msg = Message.obtain();
                msg.what = 10;
                Bundle bundle = new Bundle();
                bundle.putString("mes", mes);
                msg.setData(bundle);
                myHandle.sendMessage(msg);
            }
        }
    });
}
