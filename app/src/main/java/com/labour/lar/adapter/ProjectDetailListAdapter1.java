package com.labour.lar.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.labour.lar.BaseAdapter;
import com.labour.lar.R;

public class ProjectDetailListAdapter1 extends BaseAdapter<ProjectListItemWarp1.ListItem, ProjectListItemWarp1.ItemHolder> {

    public ProjectDetailListAdapter1(Context mContext) {
        super(mContext);
    }

    @Override
    protected View createView(int position, View convertView, ViewGroup parent) {
        View view = this.mInflater.inflate(R.layout.frag_project_detail_list_item1, null);
        ProjectListItemWarp1.ItemHolder holder = new ProjectListItemWarp1.ItemHolder(view);
        view.setTag(holder);
        return view;
    }

    @Override
    protected void fillView(int position, ProjectListItemWarp1.ListItem item, ProjectListItemWarp1.ItemHolder holder) {
        ProjectListItemWarp1.showOneOrTwo(holder,item);

    }
}
