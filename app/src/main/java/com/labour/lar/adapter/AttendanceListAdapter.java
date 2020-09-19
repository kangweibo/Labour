package com.labour.lar.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.labour.lar.BaseAdapter;
import com.labour.lar.R;
import com.labour.lar.module.Attendance;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AttendanceListAdapter extends BaseAdapter<Attendance, AttendanceListAdapter.ItemHolder> {

    public AttendanceListAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    protected View createView(int position, View convertView, ViewGroup parent) {
        View view = this.mInflater.inflate(R.layout.activity_attendance_list_item, null);
        ItemHolder holder = new ItemHolder(view);
        view.setTag(holder);
        return view;
    }

    @Override
    protected void fillView(int position, Attendance item, ItemHolder holder) {
        if (!TextUtils.isEmpty(item.getClockdate())){
            holder.txt_title.setText("考勤日期：" + item.getClockdate());
        } else {
            holder.txt_title.setText("考勤日期：");
        }

        if (!TextUtils.isEmpty(item.getClockintime())){
            holder.txt_option1.setText("签到时间：" + item.getClockintime());
        } else {
            holder.txt_option1.setText("签到时间：");
        }

        if (!TextUtils.isEmpty(item.getClockingeo())){
            holder.txt_option2.setText("签到位置：" + item.getInaddr());
        } else {
            holder.txt_option2.setText("签到位置：");
        }

        if (!TextUtils.isEmpty(item.getClockouttime())){
            holder.txt_option3.setText("签退时间：" + item.getClockouttime());
        } else {
            holder.txt_option3.setText("签退时间：");
        }

        if (!TextUtils.isEmpty(item.getClockoutgeo())){
            holder.txt_option4.setText("签退位置：" + item.getOutaddr());
        } else {
            holder.txt_option4.setText("签退位置：");
        }
    }

    class ItemHolder {
        public ItemHolder(View view){
            ButterKnife.bind(this, view);
        }

        @BindView(R.id.txt_title)
        TextView txt_title;
        @BindView(R.id.txt_option1)
        TextView txt_option1;
        @BindView(R.id.txt_option2)
        TextView txt_option2;
        @BindView(R.id.txt_option3)
        TextView txt_option3;
        @BindView(R.id.txt_option4)
        TextView txt_option4;
    }
}
