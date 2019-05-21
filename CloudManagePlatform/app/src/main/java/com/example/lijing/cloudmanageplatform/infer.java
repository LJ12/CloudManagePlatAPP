package com.example.lijing.cloudmanageplatform;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.support.annotation.RequiresApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.content.Context.CONNECTIVITY_SERVICE;

/**
 * Created by lijing on 2019/2/26.
 */

public class infer {

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static String Net(String uri, String params, Context context) {
        try {
            ConnectivityManager mConnectivityManager=(ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
            NetworkCapabilities networkCapabilities = mConnectivityManager.getNetworkCapabilities(mConnectivityManager.getActiveNetwork());
            if(networkCapabilities==null||!networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)){
                return "网络连接不可用，请检查网络";
            }
            URL url = new URL(uri);
            HttpURLConnection coon = (HttpURLConnection) url.openConnection();
            coon.setRequestMethod("POST");
            coon.setDoOutput(true);
            coon.setReadTimeout(10000);
            coon.setConnectTimeout(10000);
            coon.setDoInput(true);
            OutputStream outputStream = coon.getOutputStream();
            outputStream.write(params.getBytes());
            coon.connect();
            outputStream.flush();
            outputStream.close();
            if (coon.getResponseCode() == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(coon.getInputStream(), "UTF-8"));
                String line, s = "";
                while ((line = in.readLine()) != null) {
                    s = s + line;
                }
                JSONObject jsonObject = new JSONObject(s);
                String msg = jsonObject.getString("params");
                in.close();
                return msg;
            } else {
                return "网页链接失败";
            }
        }  catch (IOException e) {
           e.printStackTrace();
            return "数据解析失败，请重试";
        } catch (JSONException e) {
            e.printStackTrace();
            return "数据解析失败，请重试";
        }
    }
}
