package com.labour.lar.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.labour.lar.BaseAdapter;
import com.labour.lar.R;
import com.labour.lar.module.Salary;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SalaryListAdapter extends BaseAdapter<Salary, SalaryListAdapter.ItemHolder> {

    public SalaryListAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    protected View createView(int position, View convertView, ViewGroup parent) {
        View view = this.mInflater.inflate(R.layout.activity_salary_list_item, null);
        ItemHolder holder = new ItemHolder(view);
        view.setTag(holder);
        return view;
    }

    @Override
    protected void fillView(int position, Salary item, ItemHolder holder) {

        if (!TextUtils.isEmpty(item.getCreated_at())){
            holder.txt_option1.setText("日期：" + item.getCreated_at());
        }

        if (!TextUtils.isEmpty(item.getCardno())){
            holder.txt_option2.setText("卡号：" + item.getCardno());
        }

        if (!TextUtils.isEmpty(item.getAmount())){
            holder.txt_option3.setText("金额：" + item.getAmount());
        }

        if (!TextUtils.isEmpty(item.getMemo())){
            holder.txt_option4.setText("备注：" + item.getMemo());
        }
    }

    class ItemHolder {
        public ItemHolder(View view){
            ButterKnife.bind(this, view);
        }

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
