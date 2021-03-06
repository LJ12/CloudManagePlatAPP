package com.example.lijing.cloudmanageplatform.ManageActivity;

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
import android.widget.TextView;
import android.widget.Toast;

import com.example.lijing.cloudmanageplatform.R;
import com.example.lijing.cloudmanageplatform.infer;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lijing on 2019/4/23.
 */

public class ChangeCourseMessage extends AppCompatActivity {
    String message;
    EditText ET_1,ET_2;
    String url;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_course_message);
        url=getString(R.string.url)+"/ChangeCourse?";
        message=getIntent().getStringExtra("message");
        TextView TV=(TextView)findViewById(R.id.TV_CUM_1);
        TV.setText("课程ID："+message.split(" ")[0]);
        TV=(TextView)findViewById(R.id.TV_CUM_3);
        TV.setText("课程名："+message.split(" ")[1]);
        TV=(TextView)findViewById(R.id.TV_CUM_4);
        TV.setText("课程总次数："+message.split(" ")[2]);

        ET_1=(EditText)findViewById( R.id.ET_CUM_1);
        ET_1.setText(message.split(" ")[1]);
        ET_2=(EditText)findViewById( R.id.ET_CUM_2);
        ET_2.setText(message.split(" ")[2]);

        Button B=(Button)findViewById(R.id.B_CUM_1);
        B.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ET_1.getText().toString().equals("")||ET_2.getText().toString().equals("")){
                    Toast.makeText(ChangeCourseMessage.this,"编辑框不能为空，请检查输入",Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    thread.start();
                }
            }
        });
        B=(Button)findViewById(R.id.B_CUM_2);
        B.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    protected Handler myHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            JSONObject jsonObject = null;
            if (msg.what == 71) {
                try {
                    jsonObject = new JSONObject(msg.getData().getString("mes"));
                    if (jsonObject.getString("Result").equals("success")) {
                        Toast.makeText(ChangeCourseMessage.this, "修改成功", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(ChangeCourseMessage.this, jsonObject.getString("Data"), Toast.LENGTH_SHORT).show();
                        finish();
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
            String params = "CourseID="+message.split(" ")[0]+"&Name="+ET_1.getText().toString()+"&Sum="+ET_2.getText().toString();
            String mes = infer.Net(url, params,ChangeCourseMessage.this);
            if(mes.split(":").length<2){
                Looper.prepare();
                Toast.makeText(ChangeCourseMessage.this,mes,Toast.LENGTH_SHORT).show();
                finish();
                Looper.loop();
            }else {
                Message msg = Message.obtain();
                msg.what = 71;
                Bundle bundle = new Bundle();
                bundle.putString("mes", mes);
                msg.setData(bundle);
                myHandle.sendMessage(msg);
            }
        }
    });
}
