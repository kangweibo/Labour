package com.labour.lar.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.labour.lar.BaseAdapter;
import com.labour.lar.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProjectAdapter extends BaseAdapter<ProjectAdapter.ListItem, ProjectAdapter.ItemHolder> {

    public ProjectAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    protected View createView(int position, View convertView, ViewGroup parent) {
        View view = this.mInflater.inflate(R.layout.frag_project_item, null);
        ItemHolder holder = new ItemHolder(view);
        view.setTag(holder);
        return view;
    }

    @Override
    protected void fillView(int position, ProjectAdapter.ListItem item, ItemHolder holder) {
        if (item.type == 3){
            showThree(holder, item);
        }

        if (item.type == 5){
            showFive(holder, item);
        }
    }

    public static void showThree(ProjectAdapter.ItemHolder itemHolder, ProjectAdapter.ListItem item){
        itemHolder.field_r4.setVisibility(View.GONE);
        itemHolder.field_r5.setVisibility(View.GONE);

        itemHolder.field_1_1_tv.setText(item.field_1_1);
        itemHolder.field_1_2_tv.setText(item.field_1_2);
        itemHolder.field_2_1_tv.setText(item.field_2_1);
        itemHolder.field_2_2_tv.setText(item.field_2_2);
        itemHolder.field_3_1_tv.setText(item.field_3_1);
        itemHolder.field_3_2_tv.setText(item.field_3_2);
    }

    public static void showFive(ProjectAdapter.ItemHolder itemHolder, ProjectAdapter.ListItem item){
        itemHolder.field_1_1_tv.setText(item.field_1_1);
        itemHolder.field_1_2_tv.setText(item.field_1_2);
        itemHolder.field_2_1_tv.setText(item.field_2_1);
        itemHolder.field_2_2_tv.setText(item.field_2_2);
        itemHolder.field_3_1_tv.setText(item.field_3_1);
        itemHolder.field_3_2_tv.setText(item.field_3_2);
        itemHolder.field_4_1_tv.setText(item.field_4_1);
        itemHolder.field_4_2_tv.setText(item.field_4_2);
        itemHolder.field_5_1_tv.setText(item.field_5_1);
        itemHolder.field_5_2_tv.setText(item.field_5_2);
    }

    public static class ListItem {
        public String field_1_1;
        public String field_1_2;

        public String field_2_1;
        public String field_2_2;

        public String field_3_1;
        public String field_3_2;

        public String field_4_1;
        public String field_4_2;

        public String field_5_1;
        public String field_5_2;

        public int type;
    }

    class ItemHolder {
        public ItemHolder(View view){
            ButterKnife.bind(this, view);
        }

        @BindView(R.id.field_1_1_tv)
        TextView field_1_1_tv;
        @BindView(R.id.field_1_2_tv)
        TextView field_1_2_tv;

        @BindView(R.id.field_2_1_tv)
        TextView field_2_1_tv;
        @BindView(R.id.field_2_2_tv)
        TextView field_2_2_tv;

        @BindView(R.id.field_3_1_tv)
        TextView field_3_1_tv;
        @BindView(R.id.field_3_2_tv)
        TextView field_3_2_tv;

        @BindView(R.id.field_r4)
        View field_r4;
        @BindView(R.id.field_4_1_tv)
        TextView field_4_1_tv;
        @BindView(R.id.field_4_2_tv)
        TextView field_4_2_tv;

        @BindView(R.id.field_r5)
        View field_r5;
        @BindView(R.id.field_5_1_tv)
        TextView field_5_1_tv;
        @BindView(R.id.field_5_2_tv)
        TextView field_5_2_tv;
    }
}
