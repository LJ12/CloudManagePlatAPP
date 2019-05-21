package com.example.lijing.cloudmanageplatform.ManageActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.lijing.cloudmanageplatform.R;

/**
 * Created by lijing on 2019/3/27.
 */

public class Man_Home_Page_Attebdence extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.man_home_page_attendence,container,false);
        LinearLayout LL;
        LL=(LinearLayout)view.findViewById(R.id.LL_MHPA_1);
        LL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), Man_Look_User.class);
                intent.putExtra("Type","Student");
                intent.putExtra("option","4");
                startActivity(intent);
            }
        });
        LL=(LinearLayout)view.findViewById(R.id.LL_MHPA_2);
        LL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),Change_Attendence_1.class));
            }
        });
        return view;
    }
}
