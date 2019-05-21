package com.example.lijing.cloudmanageplatform.ManageActivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.lijing.cloudmanageplatform.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lijing on 2019/4/20.
 */

public class ChangeStudentAttendenceAdapter extends BaseAdapter {
    private Context context = null;
    private List list = new ArrayList<String>();
    private String name;

    public ChangeStudentAttendenceAdapter(Context context,List list,String name){
        this.context=context;
        this.list=list;
        this.name=name;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (!list.get(position).toString().equals("")) {
            if(convertView==null){
                viewHolder=new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(context);
                convertView = inflater.inflate(R.layout.change_attendence_item, null, true);
                viewHolder.courseName=(TextView) convertView.findViewById(R.id.TV_CAI_1);
                viewHolder.Jieshu=(TextView)convertView.findViewById(R.id.TV_CAI_2);
                viewHolder.situation=(TextView)convertView.findViewById(R.id.TV_CAI_3);
                convertView.setTag(viewHolder);
            }
            else{
                viewHolder = (ViewHolder) convertView.getTag();
            }
            String temp="";
            viewHolder.courseName.setText(name);
            temp=list.get(position).toString().split("\n")[0];
            temp=temp.split("第")[1];
            temp=temp.split("节课")[0];
            viewHolder.Jieshu.setText(temp);
            temp=list.get(position).toString().split("\n")[2];
            temp=temp.split("：")[1];
            if(temp.equals("未签到"))
                viewHolder.situation.setText(temp);
            else{
                temp=list.get(position).toString().split("\n")[3];
                temp=temp.split("：")[1];
                if(temp.equals("0"))
                    viewHolder.situation.setText("准时签到");
                else
                    viewHolder.situation.setText("迟到");
            }
        }
        return  convertView;
    }

    class ViewHolder {
        private TextView courseName;
        private TextView Jieshu;
        private TextView situation;
    }
}
