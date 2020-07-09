package com.labour.lar.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.labour.lar.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class TrainAdapter extends BaseExpandableAdapter<TrainAdapter.ListGroupItem,
        TrainAdapter.ListItem, TrainAdapter.GroupHolder, TrainAdapter.ItemHolder> {

    public TrainAdapter(Context mContext) {
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
        View view = this.mInflater.inflate(R.layout.activity_train_item, null);
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
        holder.img_icon.setImageResource(item.resId);
        holder.txt_field1.setText(item.field1);

        if (item.field2 != null) {
            holder.txt_field2.setText(item.field2);
        } else {
            holder.txt_field2.setVisibility(View.GONE);
        }

        if (item.field3 != null) {
            holder.txt_field3.setText(item.field3);
        } else {
            holder.txt_field3.setVisibility(View.GONE);
        }

        if (item.field4 != null) {
            holder.txt_field4.setText(item.field4);
        } else {
            holder.txt_field4.setVisibility(View.GONE);
        }

        if (item.field5 != null) {
            holder.txt_field5.setText(item.field5);
        } else {
            holder.txt_field5.setVisibility(View.GONE);
        }

        if (item.field6 != null) {
            holder.txt_field6.setText(item.field6);
        } else {
            holder.txt_field6.setVisibility(View.GONE);
        }

        if (item.field7 != null) {
            holder.txt_field7.setText(item.field7);
        } else {
            holder.txt_field7.setVisibility(View.GONE);
        }

        holder.txt_arraw.setText(item.arraw);
    }

    public static class ListGroupItem {
        public String field1;
        public int resId;
    }

    public static class ListItem {
        public int resId;
        public String field1;
        public String field2;
        public String field3;
        public String field4;
        public String field5;
        public String field6;
        public String field7;
        public String arraw;

        public boolean isShowArraw;
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

        @BindView(R.id.img_icon)
        ImageView img_icon;
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

        @BindView(R.id.txt_arraw)
        TextView txt_arraw;
    }
}
