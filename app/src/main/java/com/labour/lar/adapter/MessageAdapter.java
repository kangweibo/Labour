package com.labour.lar.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.labour.lar.BaseAdapter;
import com.labour.lar.R;
import com.labour.lar.module.FZMessage;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MessageAdapter extends BaseAdapter<FZMessage, MessageAdapter.ItemHolder> {

    public MessageAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    protected View createView(int position, View convertView, ViewGroup parent) {
        View view = this.mInflater.inflate(R.layout.frag_message_item, null);
        ItemHolder holder = new ItemHolder(view);
        view.setTag(holder);
        return view;
    }

    @Override
    protected void fillView(int position, FZMessage item, ItemHolder holder) {
        holder.txt_title.setText(item.getTitle());
        holder.txt_content.setText(item.getContent());
        holder.txt_time.setText(item.getCreated_at());
    }

    class ItemHolder {
        public ItemHolder(View view){
            ButterKnife.bind(this, view);
        }

        @BindView(R.id.txt_title)
        TextView txt_title;
        @BindView(R.id.txt_content)
        TextView txt_content;
        @BindView(R.id.txt_time)
        TextView txt_time;

    }
}
