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
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lijing.cloudmanageplatform.R;
import com.example.lijing.cloudmanageplatform.infer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;

/**
 * Created by lijing on 2019/4/17.
 */

public class Change_Attendence_2 extends AppCompatActivity {
    ListView LV;
    String StudentID, CourseID, jieshu="",name;
    List list = new ArrayList();
    String url;
    String url_change;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_attendence_check);
        url = getString(R.string.url)+"/StudentAttendance?";
        url_change = getString(R.string.url)+"/ChangeStudentAttendance?";
        LV = (ListView) findViewById(R.id.LV_CAC_1);
        StudentID = getIntent().getStringExtra("StudentID");
        CourseID = getIntent().getStringExtra("CourseID");
        name=getIntent().getStringExtra("CourseName");
        TextView TV = (TextView) findViewById(R.id.TV_CAC_1);
        TV.setText("学生ID：" + StudentID);
        TV = (TextView) findViewById(R.id.TV_CAC_2);
        TV.setText("课程ID：" + CourseID);
        Button B = (Button) findViewById(R.id.B_CAC_1);
        B.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i;
                jieshu="";
                for (i = 0; i < LV.getChildCount(); i++) {
                    LinearLayout ll = (LinearLayout) LV.getChildAt(i);// 获得子级
                    CheckBox CB = (CheckBox) ll.findViewById(R.id.CB_CAI_1);// 从子级中获得控件
                    TextView TV=(TextView)ll.findViewById(R.id.TV_CAI_2);
                    if (CB.isChecked()) {
                        if (jieshu.equals("")) {
                            jieshu = TV.getText().toString();
                        } else {
                            jieshu = jieshu + "," + TV.getText().toString();
                        }
                    }
                }
                if (jieshu.equals("")) {
                    Looper.prepare();
                    Toast.makeText(Change_Attendence_2.this, "未选择课程，请先选择要删除的课程", LENGTH_SHORT).show();
                    Looper.loop();
                    return;
                } else
                    changeAttendence();
            }
        });
        B = (Button) findViewById(R.id.B_CAC_2);
        B.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        thread.start();
    }
    private void changeAttendence(){
        Runnable runnable = new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void run() {
                String params = null;
                params = "StudentID=" + StudentID + "&CourseID=" + CourseID + "&jieshu=" + jieshu;
                String mes = infer.Net(url_change, params, Change_Attendence_2.this);
                if (mes.split(":").length < 2) {
                    Looper.prepare();
                    Toast.makeText(Change_Attendence_2.this, mes, LENGTH_SHORT).show();
                    Looper.loop();
                } else {
                    Message msg = Message.obtain();
                    msg.what = 61;
                    Bundle bundle = new Bundle();
                    bundle.putString("mes", mes);
                    msg.setData(bundle);
                    myHandle.sendMessage(msg);
                }
            }
        };
        Thread thread2=new Thread(runnable);
        thread2.start();
    }
    protected Handler myHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            JSONObject jsonObject = null;
            if (msg.what == 60) {
                try {
                    jsonObject = new JSONObject(msg.getData().getString("mes"));
                    if (jsonObject.getString("Result").equals("success")) {
                        String content = jsonObject.getString("Data");
                        if (content.isEmpty())
                            Toast.makeText(Change_Attendence_2.this, "暂无签到记录", LENGTH_SHORT).show();
                        else {
                            list = Arrays.asList(content.split("\n\n"));
                            ChangeStudentAttendenceAdapter adapter = new ChangeStudentAttendenceAdapter(Change_Attendence_2.this, list,name);
                            LV.setAdapter(adapter);
                        }
                    } else {
                        Toast.makeText(Change_Attendence_2.this, "查看失败，请稍后重试", LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (msg.what == 61) {
                try {
                    jsonObject = new JSONObject(msg.getData().getString("mes"));
                    if (jsonObject.getString("Result").equals("success")) {
                        Toast.makeText(Change_Attendence_2.this, "修改成功", LENGTH_SHORT).show();
                        int i;
                        for (i = 0; i < LV.getChildCount(); i++) {
                            LinearLayout ll = (LinearLayout) LV.getChildAt(i);// 获得子级
                            CheckBox CB = (CheckBox) ll.findViewById(R.id.CB_CAI_1);// 从子级中获得控件
                            TextView TV_temp=(TextView)ll.findViewById(R.id.TV_CAI_3);
                            if (CB.isChecked()) {
                                TV_temp.setText("准时签到");
                                CB.setChecked(false);
                            }
                        }
                    } else {
                        Toast.makeText(Change_Attendence_2.this, jsonObject.getString("Data"), LENGTH_SHORT).show();
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
            params = "UserID=" + StudentID + "&CourseID=" + CourseID + "&Type=Student";
            String mes = infer.Net(url, params, Change_Attendence_2.this);
            if (mes.split(":").length < 2) {
                Looper.prepare();
                Toast.makeText(Change_Attendence_2.this, mes, LENGTH_SHORT).show();
                Looper.loop();
            } else {
                Message msg = Message.obtain();
                msg.what = 60;
                Bundle bundle = new Bundle();
                bundle.putString("mes", mes);
                msg.setData(bundle);
                myHandle.sendMessage(msg);
            }
        }
    });

}
