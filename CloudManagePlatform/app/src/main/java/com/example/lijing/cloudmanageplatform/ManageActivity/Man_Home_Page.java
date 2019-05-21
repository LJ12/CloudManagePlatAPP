package com.example.lijing.cloudmanageplatform.ManageActivity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.lijing.cloudmanageplatform.R;

/**
 * Created by lijing on 2019/2/27.
 */

public class Man_Home_Page extends FragmentActivity {
    private String userID;
    private String userEmil;
    private LinearLayout LL_1,LL_2,LL_3;
    private Fragment F_1,F_2,F_3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.man_home_page);
        userID=getIntent().getStringExtra("ID");
        userEmil=getIntent().getStringExtra("Emil");
        Button B=(Button)findViewById(R.id.B_MHP_1);
        B.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        LL_1=(LinearLayout)findViewById(R.id.LL_MHP_1);
        LL_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTabs(0);
            }
        });
        LL_2=(LinearLayout)findViewById(R.id.LL_MHP_2);
        LL_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTabs(1);
            }
        });
        LL_3=(LinearLayout)findViewById(R.id.LL_MHP_3);
        LL_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTabs(2);
            }
        });
        setTabs(0);
    }
    private void setTabs(int select){
        FragmentManager manager=getSupportFragmentManager();
        FragmentTransaction transaction=manager.beginTransaction();
        hideFragments(transaction);
        switch (select){
            case 0:
                LL_1.setBackgroundResource(R.color.colorPink);
                if(F_1==null){
                    F_1=new Man_Home_Page_Attebdence();
                    transaction.add(R.id.FL_MHP_1,F_1);
                }
                else{
                    transaction.show(F_1);
                }
                break;
            case 1:
                LL_2.setBackgroundResource(R.color.colorPink);
                if(F_2==null){
                    F_2=new Man_Home_Page_Message();
                    transaction.add(R.id.FL_MHP_1,F_2);
                }
                else{
                    transaction.show(F_2);
                }
                break;
            case 2:
                LL_3.setBackgroundResource(R.color.colorPink);
                if(F_3==null){
                    F_3=new Man_Home_Page_Own(userID,userEmil);
                    transaction.add(R.id.FL_MHP_1,F_3);
                }
                else{
                    transaction.show(F_3);
                }
                break;
        }
        transaction.commit();
    }
    private void hideFragments(FragmentTransaction transaction) {
        LL_1.setBackgroundResource(R.color.colorWhite);
        LL_2.setBackgroundResource(R.color.colorWhite);
        LL_3.setBackgroundResource(R.color.colorWhite);
        if (F_1 != null) {
            transaction.hide(F_1);
        }
        if (F_2!= null) {
            transaction.hide(F_2);
        }
        if (F_3 != null) {
            transaction.hide(F_3);
        }
    }
}
