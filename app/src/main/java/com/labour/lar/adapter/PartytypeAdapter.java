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

public class PartytypeAdapter extends BaseAdapter<PartytypeAdapter.ListItem, PartytypeAdapter.ItemHolder> {

    public PartytypeAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    protected View createView(int position, View convertView, ViewGroup parent) {
        View view = this.mInflater.inflate(R.layout.frag_partytype_item, null);
        ItemHolder holder = new ItemHolder(view);
        view.setTag(holder);
        return view;
    }

    @Override
    protected void fillView(int position, ListItem item, ItemHolder holder) {
        if (!TextUtils.isEmpty(item.title)){
            holder.txt_title.setText(item.title);
        }

        if (!TextUtils.isEmpty(item.content)){
            holder.txt_content.setText(item.content);
        }
    }

    class ItemHolder {
        public ItemHolder(View view){
            ButterKnife.bind(this, view);
        }

        @BindView(R.id.txt_title)
        TextView txt_title;
        @BindView(R.id.txt_content)
        TextView txt_content;
    }

    public static class ListItem {
        public String title;
        public String content;

        public boolean isShowArraw;
        public int type;
    }
}
