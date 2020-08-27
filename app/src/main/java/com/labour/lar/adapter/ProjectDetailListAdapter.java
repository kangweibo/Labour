package com.labour.lar.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.labour.lar.BaseAdapter;
import com.labour.lar.R;

public class ProjectDetailListAdapter extends BaseAdapter<ProjectListItemWarp.ListItem, ProjectListItemWarp.ItemHolder> {

    public ProjectDetailListAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    protected View createView(int position, View convertView, ViewGroup parent) {
        View view = this.mInflater.inflate(R.layout.frag_project_detail_list_item, null);
        ProjectListItemWarp.ItemHolder holder = new ProjectListItemWarp.ItemHolder(view);
        view.setTag(holder);
        return view;
    }

    @Override
    protected void fillView(int position, ProjectListItemWarp.ListItem item, ProjectListItemWarp.ItemHolder holder) {
        switch (item.type) {
            case 1:
                ProjectListItemWarp.showOne(holder, item);
                break;
            case 2:
                ProjectListItemWarp.showTwo(holder, item);
                break;
            case 3:
                ProjectListItemWarp.showThree(holder, item);
                break;
            case 4:
                ProjectListItemWarp.showFour(holder, item);
                break;
        }
    }
}
