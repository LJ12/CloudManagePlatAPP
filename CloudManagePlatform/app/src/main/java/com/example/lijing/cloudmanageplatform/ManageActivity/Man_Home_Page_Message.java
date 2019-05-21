package com.example.lijing.cloudmanageplatform.ManageActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.lijing.cloudmanageplatform.InsertUser;
import com.example.lijing.cloudmanageplatform.R;

/**
 * Created by lijing on 2019/3/27.
 */

public class Man_Home_Page_Message extends Fragment implements View.OnClickListener{
  //  String ID;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.man_home_page_message,container,false);
        LinearLayout LL;
        LL=(LinearLayout)view.findViewById(R.id.LL_MHPM_1);
        LL.setOnClickListener(this);
        LL=(LinearLayout)view.findViewById(R.id.LL_MHPM_2);
        LL.setOnClickListener(this);
        LL=(LinearLayout)view.findViewById(R.id.LL_MHPM_3);
        LL.setOnClickListener(this);
        LL=(LinearLayout)view.findViewById(R.id.LL_MHPM_4);
        LL.setOnClickListener(this);
        LL=(LinearLayout)view.findViewById(R.id.LL_MHPM_5);
        LL.setOnClickListener(this);
        LL=(LinearLayout)view.findViewById(R.id.LL_MHPM_6);
        LL.setOnClickListener(this);
        LL=(LinearLayout)view.findViewById(R.id.LL_MHPM_7);
        LL.setOnClickListener(this);
        LL=(LinearLayout)view.findViewById(R.id.LL_MHPM_8);
        LL.setOnClickListener(this);
        LL=(LinearLayout)view.findViewById(R.id.LL_MHPM_9);
        LL.setOnClickListener(this);
        LL=(LinearLayout)view.findViewById(R.id.LL_MHPM_10);
        LL.setOnClickListener(this);
        LL=(LinearLayout)view.findViewById(R.id.LL_MHPM_11);
        LL.setOnClickListener(this);
        LL=(LinearLayout)view.findViewById(R.id.LL_MHPM_12);
        LL.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.LL_MHPM_1:
                intent=new Intent(getContext(), InsertUser.class);
                intent.putExtra("Type","Teacher");
                startActivity(intent);
                break;
            case R.id.LL_MHPM_2:
                intent=new Intent(getContext(), Man_Look_User.class);
                intent.putExtra("Type","Teacher");
                intent.putExtra("option","1");
                startActivity(intent);
                break;
            case R.id.LL_MHPM_3:
                intent=new Intent(getContext(), Man_Look_User.class);
                intent.putExtra("Type","Teacher");
                intent.putExtra("option","2");
                startActivity(intent);
                break;
            case R.id.LL_MHPM_4:
                intent=new Intent(getContext(), Man_Look_User.class);
                intent.putExtra("Type","Teacher");
                intent.putExtra("option","3");
                startActivity(intent);
                break;
            case R.id.LL_MHPM_5:
                intent=new Intent(getContext(), InsertUser.class);
                intent.putExtra("Type","Student");
                startActivity(intent);
                break;
            case R.id.LL_MHPM_6:
                intent=new Intent(getContext(), Man_Look_User.class);
                intent.putExtra("Type","Student");
                intent.putExtra("option","1");
                startActivity(intent);
                break;
            case R.id.LL_MHPM_7:
                intent=new Intent(getContext(), Man_Look_User.class);
                intent.putExtra("Type","Student");
                intent.putExtra("option","2");
                startActivity(intent);
                break;
            case R.id.LL_MHPM_8:
                intent=new Intent(getContext(), Man_Look_User.class);
                intent.putExtra("Type","Student");
                intent.putExtra("option","3");
                startActivity(intent);
                break;
            case R.id.LL_MHPM_9:
                intent=new Intent(getContext(), Man_Insert_Course.class);
                startActivity(intent);
                break;
            case R.id.LL_MHPM_10:
                intent=new Intent(getContext(), Man_Look_Course.class);
                intent.putExtra("option","1");
                startActivity(intent);
                break;
            case R.id.LL_MHPM_11:
                intent=new Intent(getContext(), Man_Look_Course.class);
                intent.putExtra("option","2");
                startActivity(intent);
                break;
            case R.id.LL_MHPM_12:
                intent=new Intent(getContext(), Man_Look_Course.class);
                intent.putExtra("option","3");
                startActivity(intent);
                break;
        }
    }
}
