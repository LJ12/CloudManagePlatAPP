package com.example.lijing.cloudmanageplatform.ManageActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.lijing.cloudmanageplatform.ChangeEmil;
import com.example.lijing.cloudmanageplatform.ChangePassword;
import com.example.lijing.cloudmanageplatform.R;

import static android.app.Activity.RESULT_OK;

/**
 * Created by lijing on 2019/3/27.
 */

public class Man_Home_Page_Own extends Fragment {
    String ID,Emil;
    LinearLayout LL_1,LL_2;
    public Man_Home_Page_Own(String ID,String Emil){
        this.ID=ID;
        this.Emil=Emil;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.man_home_page_own, container, false);
        LL_1=(LinearLayout)view.findViewById(R.id.LL_MHPO_1);
        LL_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), ChangePassword.class);
                intent.putExtra("UserID",ID);
                intent.putExtra("UserType","Manage");
                startActivity(intent);
            }
        });
        LL_2=(LinearLayout)view.findViewById(R.id.LL_MHPO_2);
        LL_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), ChangeEmil.class);
                intent.putExtra("UserID",ID);
                intent.putExtra("UserType","Manage");
                intent.putExtra("UserEmil",Emil);
                startActivityForResult(intent,101);
            }
        });
        return view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101  && resultCode == RESULT_OK) {
            if (data != null) {
                Emil= data.getStringExtra("ChangedEmil");
            }
        }
    }
}
