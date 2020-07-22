package com.labour.lar.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.labour.lar.BaseAdapter;
import com.labour.lar.BaseApplication;
import com.labour.lar.Constants;
import com.labour.lar.R;
import com.labour.lar.widget.RoundImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyinfoAdapter extends BaseAdapter<MyinfoAdapter.ListItem, MyinfoAdapter.ItemHolder> {

    public MyinfoAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    protected View createView(int position, View convertView, ViewGroup parent) {
        View view = this.mInflater.inflate(R.layout.frag_myinfo_item, null);
        ItemHolder holder = new ItemHolder(view);
        view.setTag(holder);
        return view;
    }

    @Override
    protected void fillView(int position, MyinfoAdapter.ListItem item, ItemHolder holder) {
        if (!TextUtils.isEmpty(item.pic)) {
            holder.photo_iv.setVisibility(View.VISIBLE);
            Glide.with(BaseApplication.getInstance()).load(Constants.HTTP_BASE + item.pic).into(holder.photo_iv);
        } else {
            holder.photo_iv.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(item.type)){
            holder.txt_type.setText(item.type);
        } else {
            holder.txt_type.setText("");
        }

        if (!TextUtils.isEmpty(item.value)){
            holder.txt_value.setText(item.value);
        } else {
            holder.txt_value.setText("");
        }
    }

    public static class ListItem {
        public String pic;
        public String type;
        public String value;
    }

    class ItemHolder {
        public ItemHolder(View view){
            ButterKnife.bind(this, view);
        }

        @BindView(R.id.photo_iv)
        RoundImageView photo_iv;
        @BindView(R.id.txt_type)
        TextView txt_type;
        @BindView(R.id.txt_value)
        TextView txt_value;
    }
}
