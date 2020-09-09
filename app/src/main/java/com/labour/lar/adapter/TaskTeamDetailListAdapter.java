package com.labour.lar.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.labour.lar.BaseAdapter;
import com.labour.lar.R;

public class TaskTeamDetailListAdapter extends BaseAdapter<TaskTeamListItemWarp.ListItem, TaskTeamListItemWarp.ItemHolder> {

    public TaskTeamDetailListAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    protected View createView(int position, View convertView, ViewGroup parent) {
        View view = this.mInflater.inflate(R.layout.frag_taskteam_detail_list_item, null);
        TaskTeamListItemWarp.ItemHolder holder = new TaskTeamListItemWarp.ItemHolder(view);
        view.setTag(holder);
        return view;
    }

    @Override
    protected void fillView(int position, TaskTeamListItemWarp.ListItem item, TaskTeamListItemWarp.ItemHolder holder) {
        switch (item.type) {
            case 1:
                TaskTeamListItemWarp.showOne(holder, item);
                break;
            case 2:
                TaskTeamListItemWarp.showTwo(holder, item);
                break;
            case 3:
                TaskTeamListItemWarp.showThree(holder, item);
                break;
            case 4:
                TaskTeamListItemWarp.showFour(holder, item);
                break;
            case 5:
                TaskTeamListItemWarp.showFive(holder, item);
                break;
        }
    }
}
