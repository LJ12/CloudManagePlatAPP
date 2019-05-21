package com.example.lijing.cloudmanageplatform.TeacherActivity;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.lijing.cloudmanageplatform.R;
import com.example.lijing.cloudmanageplatform.infer;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Hashtable;

import static android.graphics.Color.BLACK;

/**
 * Created by lijing on 2019/2/21.
 */

public class Tea_Create_QRcode extends AppCompatActivity{
    private ImageView image=null;
    String qrcodeContant="";
    String url;
    String params;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tea_create_qrcode);
        url=getString(R.string.url)+"/CreatQRCode?";
        image=(ImageView)findViewById(R.id.IV_Tea_qrcode);
        qrcodeContant=getIntent().getStringExtra("qrcodeContant");
             params=getIntent().getStringExtra("params");
        ImageButton IB=(ImageButton)findViewById(R.id.IB_Tea_back);
        IB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
            thread.start();
    }
    private Bitmap createQRCode(String url, int widthAndHeight)
            throws WriterException {
        Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        BitMatrix matrix = new MultiFormatWriter().encode(url,
                BarcodeFormat.QR_CODE, widthAndHeight, widthAndHeight);

        int width = matrix.getWidth();
        int height = matrix.getHeight();
        int[] pixels = new int[width * height];
        //画黑点
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (matrix.get(x, y)) {
                    pixels[y * width + x] = BLACK;
                }
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }
    //生成二维码的方法
    private void Create2QR2(String urls,ImageView imageView) {
        String uri = urls;
        int mScreenWidth = 0;
        Bitmap bitmap;
        try {
            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            mScreenWidth = dm.widthPixels;

            bitmap = createQRCode(uri, mScreenWidth);

            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    protected Handler myHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            JSONObject jsonObject = null;
            if (msg.what == 16) {
                try {
                    jsonObject = new JSONObject(msg.getData().getString("mes"));
                    if (jsonObject.getString("Result").equals("success")) {
                        Create2QR2(qrcodeContant,image);
                    } else {
                        Toast.makeText(Tea_Create_QRcode.this,"二维码生成失败:"+jsonObject.getString("Data")+"请返回重新尝试", Toast.LENGTH_SHORT).show();
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
            String mes = infer.Net(url,params,Tea_Create_QRcode.this);
            if(mes.split(":").length<2){
                Looper.prepare();
                Toast.makeText(Tea_Create_QRcode.this,mes,Toast.LENGTH_SHORT).show();
                Looper.loop();
            }else {
                Message msg = Message.obtain();
                msg.what = 16;
                Bundle bundle = new Bundle();
                bundle.putString("mes", mes);
                msg.setData(bundle);
                myHandle.sendMessage(msg);
            }
        }
    });
}
