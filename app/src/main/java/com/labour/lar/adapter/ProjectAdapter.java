package com.labour.lar.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.labour.lar.BaseAdapter;
import com.labour.lar.Constants;
import com.labour.lar.R;
import com.labour.lar.module.Project;
import com.labour.lar.widget.RoundImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProjectAdapter extends BaseAdapter<Project, ProjectAdapter.ItemHolder> {

    public ProjectAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    protected View createView(int position, View convertView, ViewGroup parent) {
        View view = this.mInflater.inflate(R.layout.frag_project_item, null);
        ItemHolder holder = new ItemHolder(view);
        view.setTag(holder);
        return view;
    }

    @Override
    protected void fillView(int position, Project item, ItemHolder holder) {
        //Glide.with(mContext).load(Constants.IMAGE_HTTP_BASE + item.getAvatar()).into(holder.photo_iv);
        holder.photo_iv.setImageResource(R.mipmap.picture);
        if (!TextUtils.isEmpty(item.getName())){
            holder.name_tv.setText(item.getName());
        } else {
            holder.name_tv.setText("");
        }

        if (!TextUtils.isEmpty(item.getStartdate())){
            holder.txt_start_date.setText("开工日期：" + item.getStartdate());
        } else {
            holder.txt_start_date.setText("开工日期：无");
        }
        if (!TextUtils.isEmpty(item.getStartdate())){
            holder.txt_start_date.setText("结束日期：" + item.getStartdate());
        } else {
            holder.txt_start_date.setText("结束日期：无");
        }

        if (!TextUtils.isEmpty(item.getEntname())){
            holder.txt_time_scale.setText("比例：" + "86%100");
        } else {
            holder.txt_time_scale.setText("比例：");
        }

        if (!TextUtils.isEmpty(item.getBuildaera())){
            holder.txt_number_people.setText("上岗人数：" + item.getOndutynum() + "(" + item.getOnjobnum() + ")");
        } else {
            holder.txt_number_people.setText("上岗人数：");
        }

        if (!TextUtils.isEmpty(item.getBuildaera())){
            holder.txt_work_hours.setText(item.getBuildaera());
        } else {
            holder.txt_work_hours.setText("");
        }

        if (!TextUtils.isEmpty(item.getBuildaera())){
            holder.txt_number_people.setText(item.getBuildaera());
        } else {
            holder.txt_number_people.setText("");
        }
//        holder.mianji_tv.setVisibility(View.GONE);
    }

    class ItemHolder {
        public ItemHolder(View view){
            ButterKnife.bind(this, view);
        }

        @BindView(R.id.photo_iv)
        RoundImageView photo_iv;
        @BindView(R.id.name_tv)
        TextView name_tv;
        @BindView(R.id.state_tv)
        TextView state_tv;

        @BindView(R.id.txt_start_date)
        TextView txt_start_date;
        @BindView(R.id.txt_end_date)
        TextView txt_end_date;
        @BindView(R.id.txt_time_scale)
        TextView txt_time_scale;
        @BindView(R.id.txt_number_people)
        TextView txt_number_people;
        @BindView(R.id.txt_work_hours)
        TextView txt_work_hours;
        @BindView(R.id.txt_money)
        TextView txt_money;
        @BindView(R.id.txt_money_total)
        TextView txt_money_total;
        @BindView(R.id.txt_money_scale)
        TextView txt_money_scale;
    }
}
