package com.example.lijing.cloudmanageplatform;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lijing.cloudmanageplatform.ManageActivity.Man_Home_Page;
import com.example.lijing.cloudmanageplatform.StudentActivity.Stu_Home_Page;
import com.example.lijing.cloudmanageplatform.TeacherActivity.Tea_Home_Page;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lijing on 2019/2/18.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    EditText ET_1, ET_2;
    TextView TV_2;
    String params;
    CheckBox CB_1;
    String url = "";
    List<String> username;
    List<String> password;
    Spinner spinner1;
    String name_temp, password_temp;
    Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        context = this;
        //初始化
        url = getString(R.string.url) + "/Login?";
        username = new ArrayList<>();
        password = new ArrayList<>();
        getUserMessage();

        ET_1 = (EditText) findViewById(R.id.ET_L1);
        ET_2 = (EditText) findViewById(R.id.ET_L2);
        TV_2 = (TextView) findViewById(R.id.TV_L2);
        Button B_1 = (Button) findViewById(R.id.B_L1);
        B_1.setOnClickListener(this);
        B_1 = (Button) findViewById(R.id.B_L2);
        B_1.setOnClickListener(this);

        TextView TV_1 = (TextView) findViewById(R.id.TV_L1);
        TV_1.setOnClickListener(this);

        CB_1 = (CheckBox) findViewById(R.id.CB_L1);

        //用户名密码
        spinner1 = (Spinner) findViewById(R.id.S_L1);
        SetSpinner();

        //身份类型
        Spinner spinner2 = (Spinner) findViewById(R.id.S_L2);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0)
                    TV_2.setText("学生");
                else if (position == 1)
                    TV_2.setText("教师");
                else if (position == 2)
                    TV_2.setText("管理员");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void SetSpinner() {
        if (username.size() > 0) {
            while(username.contains("清空保存"))
                 username.remove("清空保存");
            username.add(username.size(), "清空保存");
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, username);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == username.size() - 1) {
                    clear_user_password();
                    username.clear();
                    password.clear();
                    ET_1.setText("");
                    ET_2.setText("");
                } else {
                    ET_1.setText(username.get(position));
                    ET_2.setText(password.get(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void clear_user_password() {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = openFileOutput("UserMessage", MODE_PRIVATE);
            byte[] buffer = ("").getBytes();
            fileOutputStream.write(buffer);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.B_L1:
                if (!ET_1.getText().toString().equals("") && !ET_2.getText().toString().equals("") && !ET_1.getText().toString().equals("请输入用户名") && !ET_2.getText().toString().equals("请输入密码")) {
                    name_temp = ET_1.getText().toString();
                    password_temp = ET_2.getText().toString();
                    Runnable runnable = new Runnable() {
                        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                        @Override
                        public void run() {
                            if (TV_2.getText().toString().equals("学生"))
                                params = "Type=Student&ID=" + name_temp + "&Password=" + password_temp;
                            else if (TV_2.getText().toString().equals("教师"))
                                params = "Type=Teacher&ID=" + name_temp + "&Password=" + password_temp;
                            else
                                params = "Type=Manage&ID=" + name_temp + "&Password=" + password_temp;
                            String mes = infer.Net(url, params, LoginActivity.this);
                            if (mes.split(":").length < 2) {
                                Looper.prepare();
                                Toast.makeText(LoginActivity.this, mes, Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            } else {
                                Message msg = Message.obtain();
                                msg.what = 300;
                                Bundle bundle = new Bundle();
                                bundle.putString("mes", mes);
                                msg.setData(bundle);
                                myHandle.sendMessage(msg);
                            }
                        }
                    };
                    Thread thread = new Thread(runnable);
                    thread.start();
                } else {
                    Toast.makeText(this, "用户名或密码未填写", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.B_L2:
                finish();
                break;
            case R.id.TV_L1: //忘记密码
                Intent intent = new Intent(LoginActivity.this, LosePassword.class);
                startActivity(intent);
                break;
        }
    }

    protected Handler myHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 300) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(msg.getData().getString("mes"));
                    if (jsonObject.getString("Result").equals("success")) {
                        if (CB_1.isChecked()) {
                            saveUserMessage(name_temp, password_temp);
                            SetSpinner();
                        }
                        Intent intent = null;
                        if (TV_2.getText().toString().equals("学生"))
                            intent = new Intent(LoginActivity.this, Stu_Home_Page.class);
                        else if (TV_2.getText().toString().equals("教师"))
                            intent = new Intent(LoginActivity.this, Tea_Home_Page.class);
                        else if (TV_2.getText().toString().equals("管理员"))
                            intent = new Intent(LoginActivity.this, Man_Home_Page.class);
                        intent.putExtra("ID", name_temp);
                        intent.putExtra("Name", jsonObject.getString("Name"));
                        intent.putExtra("Emil", jsonObject.getString("Emil"));
                        if (TV_2.getText().toString().equals("学生")) {
                            intent.putExtra("Sum", jsonObject.getString("Sum"));
                            intent.putExtra("lated_num", jsonObject.getString("lated_num"));
                            intent.putExtra("un_attendance_num", jsonObject.getString("un_attendance_num"));
                        }
                        startActivity(intent);

                    } else {
                        Toast.makeText(LoginActivity.this, jsonObject.getString("Data"), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    public boolean saveUserMessage(String name, String pass) {
        int i;
        // judge weather the SDCard exits,and can be read and written
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return false;
        }
        boolean flag = false;

        //查重
        for (i = 0; i < username.size(); i++) {
            if (username.get(i).equals(name)) {
                if (password.get(i).equals(pass))
                    break;
                else {
                    password.set(i, pass);
                    flag = true;
                }
            }
        }
        //保存
        if (i == username.size() && !flag) {
            username.add(name);
            password.add(pass);
            if (username.size() > 0)
                username.remove("清空保存");
            try {
                FileOutputStream fileOutputStream = openFileOutput("UserMessage", MODE_APPEND);
                byte[] buffer = (name + "\t" + pass + "\n").getBytes();
                fileOutputStream.write(buffer);
                fileOutputStream.flush();
                fileOutputStream.close();
                SetSpinner();
                return true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        } else if (i == username.size() && flag) {
            try {
                if (username.size() > 0)
                    username.remove("清空保存");
                clear_user_password();
                FileOutputStream fileOutputStream = openFileOutput("UserMessage", MODE_PRIVATE);
                String str = "";
                for (int j = 0; j < username.size(); j++) {
                    str = str + username.get(j) + "\t" + password.get(j) + "\n";
                }
                byte[] buffer = (str).getBytes();
                fileOutputStream.write(buffer);
                fileOutputStream.flush();
                fileOutputStream.close();
                SetSpinner();
                return true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        } else {
            return true;
        }
    }

    public void getUserMessage() {
        String con = "";
        int len = 0;
        try {
            FileInputStream fiStream = openFileInput("UserMessage");
            len = fiStream.available();
            byte[] buffer = new byte[len];
            fiStream.read(buffer);
            con = new String(buffer);
            String[] temp = con.split("\n");
            for (int i = 0; i < temp.length; i++) {
                if (temp[i].length() > 1) {
                    username.add((temp[i].split("\t"))[0]);
                    password.add((temp[i].split("\t"))[1]);
                }
            }
            fiStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
