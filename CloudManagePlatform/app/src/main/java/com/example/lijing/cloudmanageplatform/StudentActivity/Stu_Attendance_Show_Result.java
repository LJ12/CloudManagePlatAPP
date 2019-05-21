package com.example.lijing.cloudmanageplatform.StudentActivity;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lijing.cloudmanageplatform.R;
import com.example.lijing.cloudmanageplatform.infer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lijing on 2019/2/28.
 */

public class Stu_Attendance_Show_Result extends AppCompatActivity {
    String url;
    String userID;
    String courseID;
    String content;
    String Type;
    TextView TV_ID;
    TextView TV_Name;
    TextView TV_result;
    TextView TV_jieshu;
    String courseName;
    int jieshu_sum = 0;
    int jieshu_select = 0;
    int attendance_select = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_look_attendance);
        url = getString(R.string.url)+"/StudentAttendance?";
        userID = getIntent().getStringExtra("UserID");
        courseID = getIntent().getStringExtra("CourseID");
        courseName = getIntent().getStringExtra("CourseName");
        jieshu_sum = Integer.parseInt(getIntent().getStringExtra("jieshu_sum"));
        Type = getIntent().getStringExtra("Type");
        TV_ID = (TextView) findViewById(R.id.TV_SLA_1);
        TV_ID.setText("课程ID:" + courseID);
        TV_Name = (TextView) findViewById(R.id.TV_SLA_2);
        TV_Name.setText("课程名：" + courseName);
        TV_jieshu = (TextView) findViewById(R.id.TV_SLA_4);
        TV_jieshu.setText("课时:全部");

        TV_result = (TextView) findViewById(R.id.TV_SLA_3);
        TV_result.setMovementMethod(ScrollingMovementMethod.getInstance());
        Button button;
        button = (Button) findViewById(R.id.B_SLA_2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Spinner S_1 = (Spinner) findViewById(R.id.S_SLA_1);
        List list_jieshu = new ArrayList<String>();
        list_jieshu.add("全部");
        for (int j = 1; j <= jieshu_sum; j++) {
            list_jieshu.add("第" + j + "节");
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list_jieshu);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        S_1.setAdapter(adapter);
        S_1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                jieshu_select = position;
                if (jieshu_select == 0)
                    TV_jieshu.setText("课时:全部");
                else
                    TV_jieshu.setText("课时:第" + jieshu_select + "节课");
                show_result();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        RadioGroup RG_1 = (RadioGroup) findViewById(R.id.RG_SLA_1);
        RG_1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.RB_SLA_0:
                        attendance_select = 0;
                        break;
                    case R.id.RB_SLA_1:
                        attendance_select = 1;
                        break;
                    case R.id.RB_SLA_2:
                        attendance_select = 2;
                        break;
                    case R.id.RB_SLA_3:
                        attendance_select = 3;
                        break;
                }
                show_result();
            }
        });

        thread.start();
    }

    private void show_result() {
        if (content == null || content == "")
            TV_result.setText("暂无记录");
        else if (attendance_select == 0 && jieshu_select == 0)
            TV_result.setText(content);
        else {
            String show = "";
            String[] temp = content.split("\n\n");
            for (int i = 0; i < temp.length; i++) {
                if (jieshu_select == 0) {
                    if (attendance_select == 1 && temp[i].indexOf("签到时间：未签到") == -1 && temp[i].indexOf("是否迟到：0") != -1) {
                        show = show + temp[i] + "\n\n";
                    } else if (attendance_select == 2 && temp[i].indexOf("签到时间：未签到") == -1 && temp[i].indexOf("是否迟到：1") != -1) {
                        show = show + temp[i] + "\n\n";
                    } else if (attendance_select == 3 && temp[i].indexOf("签到时间：未签到") != -1) {
                        show = show + temp[i] + "\n\n";
                    }
                } else if (attendance_select == 0) {
                    if (temp[i].indexOf("第" + jieshu_select + "节课") != -1) {
                        show = show + temp[i] + "\n\n";
                    }
                } else {
                    if (temp[i].indexOf("第" + jieshu_select + "节课") != -1 && attendance_select == 1 && temp[i].indexOf("签到时间：未签到") == -1 && temp[i].indexOf("是否迟到：0") != -1) {
                        show = show + temp[i] + "\n\n";
                    } else if (temp[i].indexOf("第" + jieshu_select + "节课") != -1 && attendance_select == 2 && temp[i].indexOf("签到时间：未签到") == -1 && temp[i].indexOf("是否迟到：1") != -1) {
                        show = show + temp[i] + "\n\n";
                    } else if (temp[i].indexOf("第" + jieshu_select + "节课") != -1 && attendance_select == 3 && temp[i].indexOf("签到时间：未签到") != -1) {
                        show = show + temp[i] + "\n\n";
                    }
                }
            }
            if (show == null || show == "")
                TV_result.setText("暂无记录");
            else
                TV_result.setText(show);
        }
    }

    protected Handler myHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            JSONObject jsonObject = null;
            if (msg.what == 14) {
                try {
                    jsonObject = new JSONObject(msg.getData().getString("mes"));
                    if (jsonObject.getString("Result").equals("success")) {
                        content = jsonObject.getString("Data");
                        if (content.isEmpty())
                            TV_result.setText("暂无签到记录");
                        else
                            TV_result.setText(content);
                    } else {
                        Toast.makeText(Stu_Attendance_Show_Result.this, "查看失败，请稍后重试", Toast.LENGTH_SHORT).show();
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
            params = "UserID=" + userID + "&CourseID=" + courseID + "&Type=" + Type;
            String mes = infer.Net(url, params, Stu_Attendance_Show_Result.this);
            if (mes.split(":").length < 2) {
                Looper.prepare();
                Toast.makeText(Stu_Attendance_Show_Result.this, mes, Toast.LENGTH_SHORT).show();
                Looper.loop();
            } else {
                Message msg = Message.obtain();
                msg.what = 14;
                Bundle bundle = new Bundle();
                bundle.putString("mes", mes);
                msg.setData(bundle);
                myHandle.sendMessage(msg);
            }
        }
    });
}
