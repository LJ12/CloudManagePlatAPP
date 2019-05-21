package com.example.lijing.cloudmanageplatform.TeacherActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lijing.cloudmanageplatform.R;

/**
 * Created by lijing on 2019/2/21.
 */

public class Tea_Home_Page extends FragmentActivity implements View.OnClickListener{
    String name;
    String emil;
    String ID;

    private LinearLayout LLattendence;
    private LinearLayout LLcourse;
    private LinearLayout LLown;

    private Fragment Fattendence=null;
    private Fragment Fcourse=null;
    private Fragment Fown=null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.tea_home_page);
        ID=getIntent().getStringExtra("ID");
        name=getIntent().getStringExtra("Name");
        emil=getIntent().getStringExtra("Emil");
        TextView TV;
        TV=(TextView)findViewById(R.id.TV_THP_1);
        TV.setText(name+",您好");

        Button button=(Button)findViewById(R.id.B_THP_1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        LLattendence = (LinearLayout) findViewById(R.id.LL_THP_1);
        LLattendence.setOnClickListener(this);
        LLcourse=(LinearLayout) findViewById(R.id.LL_THP_2);
        LLcourse.setOnClickListener(this);
        LLown=(LinearLayout) findViewById(R.id.LL_THP_3);
        LLown.setOnClickListener(this);

        selectTab(0);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.LL_THP_1:
                selectTab(0);
                break;
            case R.id.LL_THP_2:
                selectTab(1);
                break;
            case R.id.LL_THP_3:
                selectTab(2);
                break;
        }
    }
    protected void selectTab(int select){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        hideFragments(transaction);
        switch (select){
            case 0:
                if (Fattendence == null) {
                    Fattendence = new Tea_Home_Page_Attendence(ID);
                    transaction.add(R.id.FL_THP_1,Fattendence);
                } else {
                    transaction.show(Fattendence);
                }
                LLattendence.setBackgroundResource(R.color.colorPink);
                break;
            case 1:
                if (Fcourse == null) {
                    Fcourse = new Tea_Home_Page_Course(ID);
                    transaction.add(R.id.FL_THP_1,Fcourse);
                } else {
                    transaction.show(Fcourse);
                }
                LLcourse.setBackgroundResource(R.color.colorPink);
                break;
            case 2:
                if (Fown == null) {
                    Fown = new Tea_Home_Page_Own(ID,emil);
                    transaction.add(R.id.FL_THP_1,Fown);
                } else {
                    transaction.show(Fown);
                }
                LLown.setBackgroundResource(R.color.colorPink);
                break;
        }
        transaction.commit();
    }
    private void hideFragments(FragmentTransaction transaction) {
        LLattendence.setBackgroundResource(R.color.colorWhite);
        LLcourse.setBackgroundResource(R.color.colorWhite);
        LLown.setBackgroundResource(R.color.colorWhite);
        if (Fattendence != null) {
            transaction.hide(Fattendence);
        }
        if (Fcourse!= null) {
            transaction.hide(Fcourse);
        }
        if (Fown != null) {
            transaction.hide(Fown);
        }
    }

}
