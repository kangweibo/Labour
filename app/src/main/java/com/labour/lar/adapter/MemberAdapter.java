package com.labour.lar.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.labour.lar.BaseAdapter;
import com.labour.lar.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MemberAdapter extends BaseAdapter<MemberAdapter.ListItem, MemberAdapter.ItemHolder> {

    public MemberAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    protected View createView(int position, View convertView, ViewGroup parent) {
        View view = this.mInflater.inflate(R.layout.frag_member_item, null);
        ItemHolder holder = new ItemHolder(view);
        view.setTag(holder);
        return view;
    }

    @Override
    protected void fillView(int position, MemberAdapter.ListItem item, ItemHolder holder) {
        if (!TextUtils.isEmpty(item.name)){
            holder.txt_name.setText(item.name);
        } else {
            holder.txt_name.setText("");
        }
    }

    public static class ListItem {
        public int id;
        public String name;
    }

    class ItemHolder {
        public ItemHolder(View view){
            ButterKnife.bind(this, view);
        }

        @BindView(R.id.txt_name)
        TextView txt_name;
    }
}
