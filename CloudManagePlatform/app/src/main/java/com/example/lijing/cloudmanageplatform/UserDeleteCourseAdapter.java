package com.example.lijing.cloudmanageplatform;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lijing on 2019/2/28.
 */

public class UserDeleteCourseAdapter extends BaseAdapter {
    private Context context = null;
    private List list = new ArrayList<String>();
    ;

    public UserDeleteCourseAdapter(Context context, List list) {
        this.context = context;
        this.list = list;
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
        ViewHolder mHolder;
        if (!list.get(position).toString().equals("")) {
            if (convertView == null) {
                mHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(context);
                convertView = inflater.inflate(R.layout.delete_course_item, null, true);
                mHolder.TV_ID = (TextView) convertView.findViewById(R.id.TV_DCI_1);
                mHolder.TV_Name = (TextView) convertView.findViewById(R.id.TV_DCI_2);
                CheckBox CB=(CheckBox) convertView.findViewById(R.id.CB_DCI_1);
            //    LinearLayout LL=(LinearLayout) convertView.findViewById(R.id.LL_DCI_1);
           //     LL.setOnClickListener(v -> CB.setChecked(!CB.isChecked()));
                convertView.setTag(mHolder);
            } else {
                mHolder = (ViewHolder) convertView.getTag();
            }

            String ID = list.get(position).toString().split(" ")[0];
            String Name = list.get(position).toString().split(" ")[1];
            mHolder.TV_ID.setText(ID);
            mHolder.TV_Name.setText(Name);
        }
        return convertView;
    }

    class ViewHolder {
        private TextView TV_ID;
        private TextView TV_Name;
    }
}
