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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import static android.widget.Toast.LENGTH_SHORT;

/**
 * Created by lijing on 2019/2/26.
 */

public class ChangePassword extends AppCompatActivity {
    String ID,Type;
    EditText ET_old,ET_new1,ET_new2;
    String url;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password);
        url = getString(R.string.url)+"/ChangePassword?";
        TextView TV;
        Button button;
        ID = getIntent().getStringExtra("UserID");
        Type = getIntent().getStringExtra("UserType");
        ET_old = (EditText) findViewById(R.id.ET_CP_1);
        ET_new1 = (EditText) findViewById(R.id.ET_CP_2);
        ET_new2 = (EditText) findViewById(R.id.ET_CP_3);
        button = (Button) findViewById(R.id.B_CP_1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!ET_old.getText().toString().equals("")&& !ET_new1.getText().toString().equals("")&&!ET_new2.getText().toString().equals("") ){
                    if(ET_new1.getText().toString().equals(ET_new2.getText().toString()))
                         thread.start();
                    else {
                        ET_new1.setText("");
                        ET_new2.setText("");
                        Toast.makeText(ChangePassword.this, "新密码的两次输入不同，请检查信息", LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(ChangePassword.this,"信息填写不全,请检查信息",LENGTH_SHORT).show();

                }
            }
        });

        button = (Button) findViewById(R.id.B_CP_2);
        button.setOnClickListener(new View.OnClickListener() {
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
            if (msg.what == 3) {
                JSONObject jsonObject = null;
                try {
                    Log.e("TAB",msg.getData().getString("mes"));
                    jsonObject = new JSONObject(msg.getData().getString("mes"));
                    if (jsonObject.getString("Result").equals("success")) {
                        Toast.makeText(ChangePassword.this, "修改密码成功", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        ET_old.setText("");
                        ET_new1.setText("");
                        ET_new2.setText("");
                        Toast.makeText(ChangePassword.this, jsonObject.getString("Data"), Toast.LENGTH_SHORT).show();
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

            String params = "Type="+Type+"&ID=" + ID  + "&Old=" + ET_old.getText().toString() + "&New=" +ET_new1.getText().toString();
            String mes = infer.Net(url, params,ChangePassword.this);
            if(mes.split(":").length<2){
                Looper.prepare();
                Toast.makeText(ChangePassword.this,mes,Toast.LENGTH_SHORT).show();
                Looper.loop();
            }else {
                Message msg = Message.obtain();
                msg.what = 3;
                Bundle bundle = new Bundle();
                bundle.putString("mes", mes);
                msg.setData(bundle);
                myHandle.sendMessage(msg);
            }
        }
    });

}
