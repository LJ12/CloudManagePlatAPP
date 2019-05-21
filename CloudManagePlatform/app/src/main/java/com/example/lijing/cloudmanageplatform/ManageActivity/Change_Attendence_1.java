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

import static android.widget.Toast.LENGTH_SHORT;

/**
 * Created by lijing on 2019/4/17.
 */

public class Change_Attendence_1 extends AppCompatActivity {
    EditText ET_1,ET_2;
    String url;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_attendence_situation);
        url=getString(R.string.url)+"/ChenckStudentCourse?";
        ET_1=(EditText)findViewById(R.id.ET_CAS_1);
        ET_2=(EditText)findViewById(R.id.ET_CAS_2);
        Button B=(Button)findViewById(R.id.B_CAS_1);
        B.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ET_1.getText().toString().equals("")||ET_2.getText().toString().equals("")){
                    Toast.makeText(Change_Attendence_1.this,"编辑框不能为空，请输入ID",Toast.LENGTH_SHORT).show();
                }else{
                    Runnable runnable = new Runnable() {
                        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                        @Override
                        public void run() {
                            String params = "StudentID=" + ET_1.getText().toString() + "&CourseID=" + ET_2.getText().toString() ;
                            String mes = infer.Net(url, params, Change_Attendence_1.this);
                            if (mes.split(":").length < 2) {
                                Looper.prepare();
                                Toast.makeText(Change_Attendence_1.this, mes, LENGTH_SHORT).show();
                                Looper.loop();
                            } else {
                                Message msg = Message.obtain();
                                msg.what = 64;
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
        });
        B=(Button)findViewById(R.id.B_CAS_2);
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
            if (msg.what == 64) {
                try {
                    jsonObject = new JSONObject(msg.getData().getString("mes"));
                    String content = jsonObject.getString("Data");
                    if (jsonObject.getString("Result").equals("success")) {
                        Intent intent=new Intent(Change_Attendence_1.this,Change_Attendence_2.class);
                        intent.putExtra("StudentID",ET_1.getText().toString());
                        intent.putExtra("CourseID",ET_2.getText().toString());
                        intent.putExtra("CourseName",content);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(Change_Attendence_1.this,content, LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };


}
