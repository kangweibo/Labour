package com.labour.lar.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.labour.lar.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TrainPaperAdapter extends BaseExpandableAdapter<TrainPaperAdapter.ListGroupItem,
        TrainPaperAdapter.ListItem, TrainPaperAdapter.GroupHolder, TrainPaperAdapter.ItemHolder> {

    public TrainPaperAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    protected View createGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View view = this.mInflater.inflate(R.layout.activity_train_header_item, null);
        GroupHolder holder = new GroupHolder(view);
        view.setTag(holder);
        return view;
    }

    @Override
    protected View createChildView(int groupPosition, int childPosition, View convertView, ViewGroup parent) {
        View view = this.mInflater.inflate(R.layout.activity_train_learn_item, null);
        ItemHolder holder = new ItemHolder(view);
        view.setTag(holder);
        return view;
    }

    @Override
    protected void fillGroupView(int groupPosition, boolean isExpanded, ListGroupItem item, GroupHolder holder) {
        holder.item_txt.setText(item.field1);
        holder.item_img.setImageResource(R.mipmap.vertical_line);
    }

    @Override
    protected void fillChildView(int groupPosition, int childPosition, ListItem item, ItemHolder holder) {

        holder.txt_show.setVisibility(View.GONE);
        holder.rg_options.setVisibility(View.VISIBLE);

        if (!TextUtils.isEmpty(item.question)){
            holder.txt_question.setText(item.question);
        }

        if (item.type == 0){
            holder.ly_options.setVisibility(View.GONE);
            holder.radioa.setText("正确");
            holder.radiob.setText("错误");
            holder.radioa.setVisibility(View.VISIBLE);
            holder.radiob.setVisibility(View.VISIBLE);
            holder.radioc.setVisibility(View.GONE);
            holder.radiod.setVisibility(View.GONE);
        } else {
            holder.ly_options.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(item.choicea)){
                holder.txt_option1.setText(item.choicea);
                holder.radioa.setText("A");
                holder.radioa.setVisibility(View.VISIBLE);
            } else {
                holder.txt_option1.setVisibility(View.GONE);
                holder.radioa.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(item.choiceb)){
                holder.txt_option2.setText(item.choiceb);
                holder.radiob.setText("B");
                holder.radiob.setVisibility(View.VISIBLE);
            } else {
                holder.txt_option2.setVisibility(View.GONE);
                holder.radiob.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(item.choicec)){
                holder.txt_option3.setText(item.choicec);
                holder.radioc.setText("C");
                holder.radioc.setVisibility(View.VISIBLE);
            } else {
                holder.txt_option3.setVisibility(View.GONE);
                holder.radioc.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(item.choiced)){
                holder.txt_option4.setText(item.choiced);
                holder.radiod.setText("D");
                holder.radiod.setVisibility(View.VISIBLE);
            } else {
                holder.txt_option4.setVisibility(View.GONE);
                holder.radiod.setVisibility(View.GONE);
            }
        }
    }

    public static class ListGroupItem {
        public String field1;
    }

    public static class ListItem {
        public int type;

        public String question;//": 问题
        public String choicea;//": A、重大事故2小时内上报",
        public String choiceb;//": B、一般事故3小时内必须上报",
        public String choicec;//": C、现场人员应立即报告"
        public String choiced;//": D、现场人员应立即报告"
    }

    class GroupHolder {
        public GroupHolder(View view){
            ButterKnife.bind(this, view);
        }

        @BindView(R.id.item_img)
        ImageView item_img;
        @BindView(R.id.item_txt)
        TextView item_txt;
    }

    class ItemHolder {
        public ItemHolder(View view){
            ButterKnife.bind(this, view);
        }

        @BindView(R.id.ly_options)
        View ly_options;
        @BindView(R.id.rg_options)
        RadioGroup rg_options;
        @BindView(R.id.radioa)
        RadioButton radioa;
        @BindView(R.id.radiob)
        RadioButton radiob;
        @BindView(R.id.radioc)
        RadioButton radioc;
        @BindView(R.id.radiod)
        RadioButton radiod;

        @BindView(R.id.txt_question)
        TextView txt_question;
        @BindView(R.id.txt_option1)
        TextView txt_option1;
        @BindView(R.id.txt_option2)
        TextView txt_option2;
        @BindView(R.id.txt_option3)
        TextView txt_option3;
        @BindView(R.id.txt_option4)
        TextView txt_option4;

        @BindView(R.id.txt_show)
        TextView txt_show;

        @BindView(R.id.txt_result)
        TextView txt_result;
    }
}
