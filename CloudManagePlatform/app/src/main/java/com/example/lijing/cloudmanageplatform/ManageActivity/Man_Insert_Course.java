package com.example.lijing.cloudmanageplatform.ManageActivity;

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
import android.widget.EditText;
import android.widget.Toast;

import com.example.lijing.cloudmanageplatform.R;
import com.example.lijing.cloudmanageplatform.infer;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by lijing on 2019/2/27.
 */

public class Man_Insert_Course extends AppCompatActivity implements View.OnClickListener{
    EditText ET_1, ET_2, ET_3;
    String url;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_insert_course_message);
        url = getString(R.string.url)+"/ManageInsertCourse?";
        ET_1 = (EditText) findViewById(R.id.ET_MICM_1);
        ET_2 = (EditText) findViewById(R.id.ET_MICM_2);
        ET_3 = (EditText) findViewById(R.id.ET_MICM_3);
        Button button ;
        button = (Button) findViewById(R.id.B_MICM_1);
        button.setOnClickListener(this);
        button = (Button) findViewById(R.id.B_MICM_2);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.B_MICM_1:
                thread.start();
                break;
            case R.id.B_MICM_2:
                finish();
                break;
        }
    }

    protected Handler myHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 6) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(msg.getData().getString("mes"));
                    if (jsonObject.getString("Result").equals("success")) {
                        Toast.makeText(Man_Insert_Course.this, "添加课程成功", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        ET_1.setText("");
                        ET_2.setText("");
                        ET_3.setText("");
                        Toast.makeText(Man_Insert_Course.this, jsonObject.getString("Data"), Toast.LENGTH_SHORT).show();
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
            try {
                params = "ID=" + ET_1.getText().toString() + "&Name=" + URLEncoder.encode(ET_2.getText().toString(), "UTF-8") +
                        "&Number=" + ET_3.getText().toString();
                String mes = infer.Net(url, params,Man_Insert_Course.this);
                if(mes.split(":").length<2){
                    Looper.prepare();
                    Toast.makeText(Man_Insert_Course.this,mes,Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }else {
                    Message msg = Message.obtain();
                    msg.what = 6;
                    Bundle bundle = new Bundle();
                    bundle.putString("mes", mes);
                    msg.setData(bundle);
                    myHandle.sendMessage(msg);
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    });
}
