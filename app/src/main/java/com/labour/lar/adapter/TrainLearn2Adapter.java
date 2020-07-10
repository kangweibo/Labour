package com.labour.lar.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.labour.lar.BaseAdapter;
import com.labour.lar.R;
import com.labour.lar.module.Question2;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TrainLearn2Adapter extends BaseAdapter<Question2, TrainLearn2Adapter.ItemHolder> {

    public TrainLearn2Adapter(Context mContext) {
        super(mContext);
    }

    @Override
    protected View createView(int position, View convertView, ViewGroup parent) {
        View view = this.mInflater.inflate(R.layout.activity_train_learn_item, null);
        ItemHolder holder = new ItemHolder(view);
        view.setTag(holder);
        return view;
    }

    @Override
    protected void fillView(int position, Question2 item, ItemHolder holder) {
        holder.rg_options.setVisibility(View.GONE);

        if (!TextUtils.isEmpty(item.getQuestion())){
            holder.txt_question.setText(item.getQuestion());
        }
        if (!TextUtils.isEmpty(item.getChoicea())){
            holder.txt_option1.setText(item.getChoicea());
        } else {
            holder.txt_option1.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(item.getChoiceb())){
            holder.txt_option2.setText(item.getChoiceb());
        } else {
            holder.txt_option2.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(item.getChoicec())){
            holder.txt_option3.setText(item.getChoicec());
        } else {
            holder.txt_option3.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(item.getChoiced())){
            holder.txt_option4.setText(item.getChoiced());
        } else {
            holder.txt_option4.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(item.getAnswer())){
            holder.txt_result.setText(item.getAnswer());
        }

        if (item.isShowAnswer()){
            holder.txt_result.setVisibility(View.VISIBLE);
        }

        holder.txt_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.txt_result.setVisibility(View.VISIBLE);
                item.setShowAnswer(true);
            }
        });
    }

    class ItemHolder {
        public ItemHolder(View view){
            ButterKnife.bind(this, view);
        }

        @BindView(R.id.ly_options)
        View ly_options;
        @BindView(R.id.rg_options)
        RadioGroup rg_options;

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
