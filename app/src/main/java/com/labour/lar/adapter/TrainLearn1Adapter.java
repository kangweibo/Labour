package com.labour.lar.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.labour.lar.BaseAdapter;
import com.labour.lar.R;
import com.labour.lar.module.Question1;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TrainLearn1Adapter extends BaseAdapter<Question1, TrainLearn1Adapter.ItemHolder> {

    public TrainLearn1Adapter(Context mContext) {
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
    protected void fillView(int position, Question1 item, ItemHolder holder) {
        holder.ly_options.setVisibility(View.GONE);
        holder.rg_options.setVisibility(View.GONE);

        if (!TextUtils.isEmpty(item.getQuestion())){
            holder.txt_question.setText(item.getQuestion());
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
        @BindView(R.id.txt_show)
        TextView txt_show;

        @BindView(R.id.txt_result)
        TextView txt_result;
    }
}
