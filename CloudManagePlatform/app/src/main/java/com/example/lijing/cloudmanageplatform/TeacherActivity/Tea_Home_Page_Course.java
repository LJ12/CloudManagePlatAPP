package com.example.lijing.cloudmanageplatform.TeacherActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.lijing.cloudmanageplatform.InsertCourse;
import com.example.lijing.cloudmanageplatform.Look_Course;
import com.example.lijing.cloudmanageplatform.R;
import com.example.lijing.cloudmanageplatform.UserDeleteCourse;

/**
 * Created by lijing on 2019/3/25.
 */

public class Tea_Home_Page_Course extends Fragment {
    LinearLayout LL_1,LL_2,LL_3;
    String ID;
    public Tea_Home_Page_Course(String ID){
        this.ID=ID;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.tea_home_page_course, container, false);
        LL_1=(LinearLayout)view.findViewById(R.id.LL_THPC_2);
        LL_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), InsertCourse.class);
                intent.putExtra("UserID",ID);
                intent.putExtra("UserType","Teacher");
                startActivity(intent);
            }
        });
        LL_2=(LinearLayout)view.findViewById(R.id.LL_THPC_3);
        LL_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), Look_Course.class);
                intent.putExtra("UserID",ID);
                startActivity(intent);
            }
        });
        LL_3=(LinearLayout)view.findViewById(R.id.LL_THPC_4);
        LL_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent  intent=new Intent(getContext(), UserDeleteCourse.class);
                intent.putExtra("UserID",ID);
                startActivity(intent);
            }
        });
        return view;
    }
}
