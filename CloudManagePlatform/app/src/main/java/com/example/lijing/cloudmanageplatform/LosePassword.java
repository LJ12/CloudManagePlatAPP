package com.example.lijing.cloudmanageplatform;

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

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lijing on 2019/3/2.
 */

public class LosePassword extends AppCompatActivity {
    EditText ET_ID,ET_Emil;
    String url;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loose_password);
        url = getString(R.string.url)+"/LosePassword?";
        ET_ID=(EditText)findViewById(R.id.ET_LP_1);
        ET_Emil=(EditText)findViewById(R.id.ET_LP_2);
        Button button;
        button=(Button)findViewById(R.id.B_LP_1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ET_ID.getText().toString().isEmpty()||ET_Emil.getText().toString().isEmpty()){
                    Toast.makeText(LosePassword.this,"信息填写不全，请补全信息",Toast.LENGTH_SHORT);
                }
                else {
                    thread.start();
                }
            }
        });
        button=(Button)findViewById(R.id.B_LP_2);
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
            JSONObject jsonObject = null;
            if (msg.what == 19) {
                try {
                    jsonObject = new JSONObject(msg.getData().getString("mes"));
                    Toast.makeText(LosePassword.this, jsonObject.getString("Data"), Toast.LENGTH_SHORT).show();
                    if(jsonObject.getString("Result").equals("sucess"))
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

            String params = null;
            params = "ID=" + ET_ID.getText().toString()+"&Emil="+ET_Emil.getText().toString();
            String mes = infer.Net(url, params,LosePassword.this);
            if(mes.split(":").length<2){
                Looper.prepare();
                Toast.makeText(LosePassword.this,mes,Toast.LENGTH_SHORT).show();
                Looper.loop();
            }else {
                Message msg = Message.obtain();
                msg.what = 19;
                Bundle bundle = new Bundle();
                bundle.putString("mes", mes);
                msg.setData(bundle);
                myHandle.sendMessage(msg);
            }
        }
    });
}
