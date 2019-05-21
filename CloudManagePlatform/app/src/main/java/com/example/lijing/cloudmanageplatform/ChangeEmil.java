package com.example.lijing.cloudmanageplatform;

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
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import static android.widget.Toast.LENGTH_SHORT;

/**
 * Created by lijing on 2019/2/26.
 */

public class ChangeEmil extends AppCompatActivity {
    String ID;
    String Emil;
    String Type;
    EditText ET_password, ET_Emil;
    String url;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_emil);
        url = getString(R.string.url)+"/ChangeEmil?";
        TextView TV;
        Button button;

        ID = getIntent().getStringExtra("UserID");
        Emil = getIntent().getStringExtra("UserEmil");
        Type = getIntent().getStringExtra("UserType");
        TV = (TextView) findViewById(R.id.TV_CE_1);
        TV.setText(Emil);
        ET_password = (EditText) findViewById(R.id.ET_CE_2);
        ET_Emil = (EditText) findViewById(R.id.ET_CE_1);
        button = (Button) findViewById(R.id.B_CE_1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!ET_Emil.getText().toString().equals("")&&!ET_Emil.getText().toString().equals("请在此输入")&&
                        !ET_password.getText().toString().equals("")&&!ET_password.getText().toString().equals("请在此输入") ){
                    thread.start();
                }else{
                    Toast.makeText(ChangeEmil.this,"信息填写不全,请检查信息",LENGTH_SHORT).show();

                }
            }
        });

        button = (Button) findViewById(R.id.B_CE_2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });

    }

    protected Handler myHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 2) {
                JSONObject jsonObject = null;
                try {
                    Log.e("TAB",msg.getData().getString("mes"));
                    jsonObject = new JSONObject(msg.getData().getString("mes"));
                    if (jsonObject.getString("Result").equals("success")) {
                        Toast.makeText(ChangeEmil.this, "修改邮箱地址成功", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.putExtra("ChangedEmil", ET_Emil.getText().toString());
                        setResult(RESULT_OK, intent);
                        finish();
                    } else {
                        ET_password.setText(null);
                        ET_Emil.setText(null);
                        Toast.makeText(ChangeEmil.this, jsonObject.getString("Data"), Toast.LENGTH_SHORT).show();
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

            String params = "Type="+Type+"&ID=" + ID  + "&Password=" + ET_password.getText().toString() + "&Emil=" +ET_Emil.getText().toString();
            String mes = infer.Net(url, params,ChangeEmil.this);
            if(mes.split(":").length<2){
                Looper.prepare();
                Toast.makeText(ChangeEmil.this,mes,Toast.LENGTH_SHORT).show();
                Looper.loop();
            }else {
                Message msg = Message.obtain();
                msg.what = 2;
                Bundle bundle = new Bundle();
                bundle.putString("mes", mes);
                msg.setData(bundle);
                myHandle.sendMessage(msg);
            }
        }
    });
}
