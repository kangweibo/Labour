package com.labour.lar.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.labour.lar.BaseAdapter;
import com.labour.lar.R;
import com.labour.lar.module.Project;
import com.labour.lar.widget.RoundImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

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
        ProjectListItemWarp.showTwo(holder,item);

    }
}
