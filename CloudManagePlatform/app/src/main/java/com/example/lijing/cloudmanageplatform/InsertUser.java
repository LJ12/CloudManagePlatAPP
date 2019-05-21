package com.example.lijing.cloudmanageplatform;

import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by lijing on 2019/2/27.
 */

public class InsertUser extends AppCompatActivity implements View.OnClickListener {
    EditText ET_1, ET_2, ET_3;
    String Type = "";
    String url;
    Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;
        setContentView(R.layout.manage_insert_user);
        url = getString(R.string.url)+"/InsertUser?";
        Type = getIntent().getStringExtra("Type");
        ET_1 = (EditText) findViewById(R.id.ET_MIU_1);
        ET_2 = (EditText) findViewById(R.id.ET_MIU_2);
        ET_3 = (EditText) findViewById(R.id.ET_MIU_3);
        Button button = (Button) findViewById(R.id.B_MIU_1);
        button.setOnClickListener(this);
        button = (Button) findViewById(R.id.B_MIU_2);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.B_MIU_1:
                thread.start();
                break;
            case R.id.B_MIU_2:
                finish();
                break;
        }
    }

    protected Handler myHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 5) {
                JSONObject jsonObject = null;
                try {
                    Log.e("TAB", msg.getData().getString("mes"));
                    jsonObject = new JSONObject(msg.getData().getString("mes"));
                    if (jsonObject.getString("Result").equals("success")) {
                        Toast.makeText(InsertUser.this, "添加用户成功", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        ET_1.setText("");
                        ET_2.setText("");
                        ET_3.setText("");
                        Toast.makeText(InsertUser.this, jsonObject.getString("Data"), Toast.LENGTH_SHORT).show();
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
                Log.e("TABSW",ET_2.getText().toString()+"   "+URLEncoder.encode(ET_2.getText().toString(), "UTF-8"));
                params = "Type=" + Type + "&ID=" + ET_1.getText().toString() + "&Name=" + URLEncoder.encode(ET_2.getText().toString(), "UTF-8") + "&Emil=" + ET_3.getText().toString();
                String mes = infer.Net(url, params,InsertUser.this);
                if(mes.split(":").length<2){
                    Looper.prepare();
                    Toast.makeText(InsertUser.this,mes,Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }else {
                    Message msg = Message.obtain();
                    msg.what = 5;
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
