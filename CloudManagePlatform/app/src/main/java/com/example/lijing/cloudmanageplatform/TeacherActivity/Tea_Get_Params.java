package com.example.lijing.cloudmanageplatform.TeacherActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.lijing.cloudmanageplatform.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * Created by lijing on 2019/3/1.
 */

public class Tea_Get_Params extends AppCompatActivity {
    String userID;
    String courseID;
    String courseName;
    String jieshu;
    Spinner S_1;
    TimePicker TP_1;
    List<String> time = new ArrayList<String>( Arrays.asList("1h","2h","3h","4h","5h","6h","7h","8h","9h","10h"));
    TextView TV_1;
    String data;
    double La,Lo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_qrcode_params);
        userID=getIntent().getStringExtra("UserID");
        courseID=getIntent().getStringExtra("CourseID");
        courseName=getIntent().getStringExtra("CourseName");
        jieshu=getIntent().getStringExtra("jieshu");
        La=getIntent().getDoubleExtra("La",0);
        Lo=getIntent().getDoubleExtra("Lo",0);

        TV_1=(TextView)findViewById(R.id.TV_CQP_5);
        TextView TV;
        TV=(TextView)findViewById(R.id.TV_CQP_1);
        TV.setText("课程ID:"+courseID);
        TV=(TextView)findViewById(R.id.TV_CQP_2);
        TV.setText("课程名"+courseName);
        TV=(TextView)findViewById(R.id.TV_CQP_3);
        int temp_jieshu = Integer.parseInt(jieshu) + 1;
        jieshu=""+temp_jieshu;
        TV.setText("第"+  jieshu +"节课");
        TV=(TextView)findViewById(R.id.TV_CQP_4);
        Calendar calendar = Calendar.getInstance(); //获取系统的日期
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        data=""+year+"-"+month+"-"+day;
        TV.setText("上课日期为："+data);
        TP_1=(TimePicker)findViewById(R.id.TP_CQP_1);
        TP_1.setIs24HourView(true);

        S_1=(Spinner)findViewById(R.id.S_CQP_1);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, time);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        S_1.setAdapter(adapter);
        S_1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TV_1.setText(time.get(position));
            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Button button;
        button=(Button)findViewById(R.id.B_CQP_1);
        button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if(TV_1.getText().toString().isEmpty())
                    Toast.makeText(Tea_Get_Params.this,"请选择本次课程时间",Toast.LENGTH_SHORT).show();
                else{
                    Intent intent=new Intent(Tea_Get_Params.this,Tea_Create_QRcode.class);
                    String content = "&CourseID="+courseID+"&jieshu="+jieshu;
                    Log.e("TAB",content);
                    intent.putExtra("qrcodeContant",content);
                    intent.putExtra("params", "TeacherID="+userID+"&Latitude="+La+"&Longitude="+Lo+"&CourseID="+courseID+"&jieshu="+jieshu+"&StartTimeHour="+TP_1.getHour()+
                            "&StartTimeMinute="+ TP_1.getMinute()+"&LastTime="+TV_1.getText().toString().split("h")[0]);
                    startActivity(intent);
                    finish();
                }
            }
        });
        button=(Button)findViewById(R.id.B_CQP_2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
