package com.example.lijing.cloudmanageplatform;

import android.app.TabActivity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;

/**
 * Created by lijing on 2019/2/26.
 */

public class InsertCourse extends TabActivity {
    String userID, Type;
    EditText ET_courseID, ET_courseName;
    String url;
    String url_GetAllCourse;
    String url_GetSelectedCourse ;
    ListView LV_1;
    List list = new ArrayList<String>();
    String params;
    boolean isFirst = true;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.insert_course_message);
        url = getString(R.string.url)+"/InsertCourse?";
        url_GetAllCourse= getString(R.string.url)+"/GetAllCourses?";
        url_GetSelectedCourse= getString(R.string.url)+"/LookUserSelectCourse?";
        TextView TV;
        Button button;
        Type = getIntent().getStringExtra("UserType");
        userID = getIntent().getStringExtra("UserID");

        ImageView IV = (ImageView) findViewById(R.id.IV_ICM_1);
        IV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TabHost TH = getTabHost();        //获取TabHost对象
        TH.setup();
        TH.addTab(TH.newTabSpec("LL_ICM_1")
                .setIndicator("选择课程")//设置Tab标签和图标
                .setContent(R.id.LL_ICM_1));        //设置Tab内容

        TH.addTab(TH.newTabSpec("LL_ICM_2")
                .setIndicator("按照课程ID添加")
                .setContent(R.id.LL_ICM_2));
        for (int i = 0; i < TH.getTabWidget().getChildCount(); i++) {
            View vvv = TH.getTabWidget().getChildAt(i);
            vvv.setBackground(getResources().getDrawable(R.drawable.selector_tabhost_sign));
        }
        TH.setCurrentTab(0);

        //第一个界面
        LV_1 = (ListView) findViewById(R.id.LV_ICM_1);

        button = (Button) findViewById(R.id.B_ICM_3);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i;
                String CoursesID = "", CoursesName = "";
                for (i = 0; i < LV_1.getChildCount(); i++) {
                    LinearLayout ll = (LinearLayout) LV_1.getChildAt(i);// 获得子级
                    CheckBox CB = (CheckBox) ll.findViewById(R.id.CB_DCI_1);// 从子级中获得控件
                    if (CB.isChecked()) {
                        if (CoursesID.equals("")) {
                            CoursesID = list.get(i).toString().split(" ")[0];
                            CoursesName = list.get(i).toString().split(" ")[1];
                        } else {
                            CoursesID = CoursesID + "," + list.get(i).toString().split(" ")[0];
                            CoursesName = CoursesName + "," + list.get(i).toString().split(" ")[1];
                        }
                    }
                }
                if (CoursesID.equals("")) {
                    Toast.makeText(InsertCourse.this, "未选择课程，请先选择要添加的课程", LENGTH_SHORT).show();
                    return;
                } else {
                    try {
                        params = "ID=" + URLEncoder.encode(userID, "UTF-8") + "&CourseID=" + URLEncoder.encode(CoursesID, "UTF-8") + "&CourseName=" +
                                URLEncoder.encode(CoursesName, "UTF-8") + "&Type=" + URLEncoder.encode(Type, "UTF-8") + "&addType=1";
                        isFirst = true;
                        thread.start();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        //第二个界面
        ET_courseID = (EditText) findViewById(R.id.ET_ICM_1);
        ET_courseName = (EditText) findViewById(R.id.ET_ICM_2);

        button = (Button) findViewById(R.id.B_ICM_1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ET_courseID.getText().toString().equals("") && !ET_courseName.getText().toString().equals("")) {
                    try {
                        params = "ID=" + URLEncoder.encode(userID, "UTF-8") + "&CourseID=" + URLEncoder.encode(ET_courseID.getText().toString(), "UTF-8") + "&CourseName=" +
                                URLEncoder.encode(ET_courseName.getText().toString(), "UTF-8") + "&Type=" + URLEncoder.encode(Type, "UTF-8") + "&addType=2";
                        isFirst = false;
                        thread.start();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(InsertCourse.this, "信息填写不全,请检查信息", LENGTH_SHORT).show();

                }
            }
        });

        button = (Button) findViewById(R.id.B_ICM_2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        thread_LookAllCourses.start();
    }

    protected Handler myHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 9) {
                JSONObject jsonObject = null;
                try {
                    Log.e("TAB", msg.getData().getString("mes"));
                    jsonObject = new JSONObject(msg.getData().getString("mes"));
                    if (jsonObject.getString("Result").equals("success")) {
                        Toast.makeText(InsertCourse.this, "增加课程成功", Toast.LENGTH_SHORT).show();
                        if (isFirst) {
                            String[] temp=jsonObject.getString("Data").split("\n");
                            for(int i=0;i<temp.length;i++){
                                if(temp[i]!=""&&temp[i].split(" ")[4].equals("成功")) {
                                    setCheckBoxEnble(temp[i].split(" ")[1]);
                                }
                            }
                        } else {
                            setCheckBoxEnble(ET_courseID.getText().toString());
                            ET_courseID.setText("");
                            ET_courseName.setText("");
                        }
                    } else {
                        if (!isFirst) {
                            ET_courseID.setText("");
                            ET_courseName.setText("");
                        }
                        Toast.makeText(InsertCourse.this, jsonObject.getString("Data"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                finish();
            } else if (msg.what == 40) {
                JSONObject jsonObject = null;
                try {
                    Log.e("TAB", msg.getData().getString("mes"));
                    jsonObject = new JSONObject(msg.getData().getString("mes"));
                    if (jsonObject.getString("Result").equals("success")) {
                        thread_GetSelectedCourses.start();
                        list = Arrays.asList(jsonObject.getString("Data").split("\t"));
                        UserDeleteCourseAdapter adapter = new UserDeleteCourseAdapter(InsertCourse.this, list);
                        LV_1.setAdapter(adapter);
                    } else {
                        Toast.makeText(InsertCourse.this, jsonObject.getString("Data"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (msg.what == 41) {
                JSONObject jsonObject = null;
                try {
                    Log.e("TAB", msg.getData().getString("mes"));
                    jsonObject = new JSONObject(msg.getData().getString("mes"));
                    if (jsonObject.getString("Result").equals("success")) {
                        String[] temp=jsonObject.getString("Data").split("\t");
                        int h;
                        for(h = 0;h<temp.length;h++){
                            setCheckBoxEnble(temp[h].split(" ")[0]);
                        }
                    } else {
                        Toast.makeText(InsertCourse.this, jsonObject.getString("Data"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };
    private void setCheckBoxEnble(String ID){
        int i;
        for (i = 0; i < LV_1.getChildCount(); i++) {
            LinearLayout ll = (LinearLayout) LV_1.getChildAt(i);// 获得子级
            CheckBox CB = (CheckBox) ll.findViewById(R.id.CB_DCI_1);// 从子级中获得控件
            TextView TV=(TextView)ll.findViewById(R.id.TV_DCI_1);
            if(TV.getText().toString().equals(ID)) {
                CB.setChecked(false);
                CB.setEnabled(false);
                CB.setClickable(false);
                break;
            }
        }
    }
    Thread thread_LookAllCourses = new Thread(new Runnable() {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void run() {
            String mes = infer.Net(url_GetAllCourse, "", InsertCourse.this);
            if (mes.split(":").length < 2) {
                Looper.prepare();
                Toast.makeText(InsertCourse.this, mes, Toast.LENGTH_SHORT).show();
                Looper.loop();
            } else {
                Message msg = Message.obtain();
                msg.what = 40;
                Bundle bundle = new Bundle();
                bundle.putString("mes", mes);
                msg.setData(bundle);
                myHandle.sendMessage(msg);
            }
        }
    });
    Thread thread_GetSelectedCourses = new Thread(new Runnable() {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void run() {
            String mes = infer.Net(url_GetSelectedCourse, "ID=" + userID,InsertCourse.this);
            if(mes.split(":").length<2){
                Looper.prepare();
                Toast.makeText(InsertCourse.this,mes,Toast.LENGTH_SHORT).show();
                Looper.loop();
            }else {
                Message msg = Message.obtain();
                msg.what = 41;
                Bundle bundle = new Bundle();
                bundle.putString("mes", mes);
                msg.setData(bundle);
                myHandle.sendMessage(msg);
            }
        }
    });

    Thread thread = new Thread(new Runnable() {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void run() {
            String mes = infer.Net(url, params,InsertCourse.this);
            if(mes.split(":").length<2){
                Looper.prepare();
                Toast.makeText(InsertCourse.this,mes,Toast.LENGTH_SHORT).show();
                Looper.loop();
            }else {
                Message msg = Message.obtain();
                msg.what = 9;
                Bundle bundle = new Bundle();
                bundle.putString("mes", mes);
                msg.setData(bundle);
                myHandle.sendMessage(msg);
            }

        }
    });
}
