package com.labour.lar.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.labour.lar.BaseAdapter;
import com.labour.lar.Constants;
import com.labour.lar.R;
import com.labour.lar.module.Employee;
import com.labour.lar.widget.RoundImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VerifyAdapter extends BaseAdapter<Employee, VerifyAdapter.ItemHolder> {

    private boolean isShowVerify;

    public void setList(List<Employee> datas) {
        super.setList(datas);
    }

    public VerifyAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    protected View createView(int position, View convertView, ViewGroup parent) {
        View view = this.mInflater.inflate(R.layout.frag_verify_item, null);
        ItemHolder holder = new ItemHolder(view);
        view.setTag(holder);
        return view;
    }

    @Override
    protected void fillView(int position, Employee item, ItemHolder holder) {
        Glide.with(mContext).load(Constants.IMAGE_HTTP_BASE + item.getPic().getUrl()).into(holder.photo_iv);

        if (!TextUtils.isEmpty(item.getName())){
            holder.txt_field1.setText(item.getName());
        } else {
            holder.txt_field1.setText("");
        }
        if (!TextUtils.isEmpty(item.getProlename())){
            holder.txt_field2.setText(item.getProlename());
        } else {
            holder.txt_field2.setText("");
        }

        holder.txt_field3.setText("手机:");

        if (!TextUtils.isEmpty(item.getPhone())){
            holder.txt_field4.setText(item.getPhone());
        } else {
            holder.txt_field4.setText("");
        }
        if (!TextUtils.isEmpty(item.getIdcard())){
            holder.txt_field5.setText("身份证号：" + item.getIdcard());
        } else {
            holder.txt_field5.setText("身份证号：");
        }
        if (!TextUtils.isEmpty(item.getBankcard())){
            holder.txt_field6.setText("银行卡号：" + item.getBankcard());
        } else {
            holder.txt_field6.setText("银行卡号：");
        }
        if (!TextUtils.isEmpty(item.getExamstatus())){
            holder.txt_field7.setText("培训考试：" + item.getExamstatus());
        } else {
            holder.txt_field7.setText("培训考试：");
        }

        holder.btn_examine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onButtonClickListener != null) {
                    onButtonClickListener.onItemClick(v, position);
                }
            }
        });

        if (!isShowVerify) {
            holder.btn_examine.setVisibility(View.GONE);
        }
    }

    class ItemHolder {
        public ItemHolder(View view){
            ButterKnife.bind(this, view);
        }

        @BindView(R.id.photo_iv)
        RoundImageView photo_iv;
        @BindView(R.id.txt_field1)
        TextView txt_field1;
        @BindView(R.id.txt_field2)
        TextView txt_field2;
        @BindView(R.id.txt_field3)
        TextView txt_field3;
        @BindView(R.id.txt_field4)
        TextView txt_field4;
        @BindView(R.id.txt_field5)
        TextView txt_field5;
        @BindView(R.id.txt_field6)
        TextView txt_field6;
        @BindView(R.id.txt_field7)
        TextView txt_field7;
        @BindView(R.id.btn_examine)
        Button btn_examine;
    }

    public void setShowVerify(boolean showVerify) {
        isShowVerify = showVerify;
    }

    private OnButtonClickListener onButtonClickListener;

    public void setOnButtonClickListener(OnButtonClickListener onButtonClickListener) {
        this.onButtonClickListener = onButtonClickListener;
    }

    public interface OnButtonClickListener {
        void onItemClick(View view, int position);
    }
}
