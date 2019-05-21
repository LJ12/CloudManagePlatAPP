package com.example.lijing.cloudmanageplatform.ManageActivity;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
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

import com.example.lijing.cloudmanageplatform.R;
import com.example.lijing.cloudmanageplatform.UserDeleteCourseAdapter;
import com.example.lijing.cloudmanageplatform.infer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

/**
 * Created by lijing on 2019/4/23.
 */

public class Man_Look_Course extends TabActivity {
    int option;
    ListView LV;
    TextView TV_1;
    EditText ET;
    String [] courseMessage;
    String DeleteIDs;
    String url;
    String url_delete;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.look_course_message);
        url=getString(R.string.url)+"/GetAllCourses?";
        url_delete = getString(R.string.url)+"/DeleteUser?";
        option = Integer.parseInt(getIntent().getStringExtra("option"));
        ImageView IV_2 = (ImageView) findViewById(R.id.IV_LCM_2);
        IV_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        thread.start();
        TabHost TH = getTabHost();
        TH.setup();
        TH.addTab(TH.newTabSpec("LL_LCM_1")
                .setIndicator("选择课程ID")//设置Tab标签和图标
                .setContent(R.id.LL_LCM_1));        //设置Tab内容

        TH.addTab(TH.newTabSpec("LL_LCM_2")
                .setIndicator("按照课程ID查询")
                .setContent(R.id.LL_LCM_2));

        for (int i = 0; i < TH.getTabWidget().getChildCount(); i++) {
            View vvv = TH.getTabWidget().getChildAt(i);
            vvv.setBackground(getResources().getDrawable(R.drawable.selector_tabhost_sign));
        }
        TH.setCurrentTab(0);

        //第一个界面
        LV = (ListView) findViewById(R.id.LV_LCM_1);
        Button button = (Button) findViewById(R.id.B_LCM_1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temp = "";
                int i;
                int j = 0;
                for (i = 0; i < LV.getChildCount(); i++) {
                    LinearLayout ll = (LinearLayout) LV.getChildAt(i);// 获得子级
                    CheckBox CB = (CheckBox) ll.findViewById(R.id.CB_DCI_1);// 从子级中获得控件
                    TextView TV = (TextView) ll.findViewById(R.id.TV_DCI_1);
                    if (CB.isChecked()) {
                        if (!temp.isEmpty() && option != 3) {
                            Toast.makeText(Man_Look_Course.this, "每次只能选择一个课程", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (temp.isEmpty()) {
                            j = i;
                            temp = TV.getText().toString();
                        } else
                            temp = temp + "," + TV.getText().toString();
                    }
                }
                if (temp.isEmpty()) {
                    Toast.makeText(Man_Look_Course.this, "您还未选择要查询的课程", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (option == 1)
                    showDialog(temp);
                else if (option == 2) {
                    Intent intent = new Intent(Man_Look_Course.this, ChangeCourseMessage.class);
                    intent.putExtra("message", courseMessage[j]);
                    startActivity(intent);
                    finish();
                } else if (option == 3) {
                    DeleteIDs = temp;
                    thread_delete.start();
                }
            }
        });

        //第二个界面
        ET = (EditText) findViewById(R.id.ET_LCM_1);
        TV_1 = (TextView) findViewById(R.id.TV_LCM_1);
        ImageView IV_1 = (ImageView) findViewById(R.id.IV_LCM_1);
        IV_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TV_1.setText("");
                if (ET.getText().toString().equals("")) {
                    TV_1.setText("请输入用户ID");
                } else {
                    check(ET.getText().toString());
                }
            }
        });
        if (option != 1) {
            Button B_temp = (Button) findViewById(R.id.B_LCM_2);
            B_temp.setVisibility(View.VISIBLE);
            B_temp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (option == 2) {
                        int i;
                        for (i = 0; i < courseMessage.length; i++) {
                            if (courseMessage[i].split(" ")[0].equals(ET.getText().toString()))
                                break;
                        }
                        if (i < courseMessage.length) {
                            Intent intent = new Intent(Man_Look_Course.this, ChangeCourseMessage.class);
                            intent.putExtra("message", courseMessage[i]);
                            startActivity(intent);
                            finish();
                        } else {
                            Looper.prepare();
                            Toast.makeText(Man_Look_Course.this, "课程ID不存在，请检查输入", Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }
                    } else if (option == 3) {
                        int i;
                        for (i = 0; i < courseMessage.length; i++) {
                            if (courseMessage[i].split(" ")[0].equals(ET.getText().toString()))
                                break;
                        }
                        if (i < courseMessage.length) {
                            DeleteIDs = ET.getText().toString();
                            thread_delete.start();
                        } else {
                            Looper.prepare();
                            Toast.makeText(Man_Look_Course.this, "课程ID不存在，请检查输入", Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }
                    }
                }
            });
        }
    }

    private void check(String id) {
        int i;
        for (i = 0; i < courseMessage.length; i++) {
            if (courseMessage[i].split(" ")[0].equals(id))
                break;
        }
        if (i < courseMessage.length) {
            TV_1.setText("课程ID：" + courseMessage[i].split(" ")[0] + "\n" + "课程名字：" + courseMessage[i].split(" ")[1] + "\n"
                    + "课程总次数： " + courseMessage[i].split(" ")[2] + "\n");
        } else
            TV_1.setText("数据库无此用户信息");
    }

    private void showDialog(String id) {
        int i;
        for (i = 0; i < courseMessage.length; i++) {
            if (courseMessage[i].split(" ")[0].equals(id))
                break;
        }
        final AlertDialog.Builder dialog = new AlertDialog.Builder(Man_Look_Course.this);
        dialog.setTitle("此课程的信息为：");
        dialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        if (i < courseMessage.length) {
                dialog.setMessage( "课程ID：" + courseMessage[i].split(" ")[0] + "\n" + "课程名字：" + courseMessage[i].split(" ")[1] + "\n"
                        + "课程总次数： " + courseMessage[i].split(" ")[2] + "\n");
        } else {
            dialog.setMessage("数据库无此课程信息");
        }
        if (option == 1)
            dialog.show();
    }
    protected Handler myHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            JSONObject jsonObject = null;
            if (msg.what == 68) {
                try {
                    jsonObject = new JSONObject(msg.getData().getString("mes"));
                    if (jsonObject.getString("Result").equals("success")) {
                        courseMessage = jsonObject.getString("Data").split("\t");
                        UserDeleteCourseAdapter adapter = new UserDeleteCourseAdapter(Man_Look_Course.this, Arrays.asList(courseMessage));
                        LV.setAdapter(adapter);
                    } else {
                        Toast.makeText(Man_Look_Course.this, jsonObject.getString("Data"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (msg.what == 69) {
                try {
                    jsonObject = new JSONObject(msg.getData().getString("mes"));
                    if (jsonObject.getString("Result").equals("success")) {
                        String temp = jsonObject.getString("Data");
                        Toast.makeText(Man_Look_Course.this, jsonObject.getString("Data"), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Man_Look_Course.this, jsonObject.getString("Data"), Toast.LENGTH_SHORT).show();
                    }
                    finish();
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
            String mes = infer.Net(url,"", Man_Look_Course.this);
            if (mes.split(":").length < 2) {
                Looper.prepare();
                Toast.makeText(Man_Look_Course.this, mes, Toast.LENGTH_SHORT).show();
                finish();
                Looper.loop();
            } else {
                Message msg = Message.obtain();
                msg.what = 68;
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
            String params = "ID=" + DeleteIDs +"&Type=Course";
            String mes = infer.Net(url_delete, params, Man_Look_Course.this);
            if (mes.split(":").length < 2) {
                Looper.prepare();
                Toast.makeText(Man_Look_Course.this, mes, Toast.LENGTH_SHORT).show();
                finish();
                Looper.loop();
            } else {
                Message msg = Message.obtain();
                msg.what = 69;
                Bundle bundle = new Bundle();
                bundle.putString("mes", mes);
                msg.setData(bundle);
                myHandle.sendMessage(msg);
            }
        }
    });
}
