package com.labour.lar.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.labour.lar.R;
import com.multilevel.treelist.Node;
import com.multilevel.treelist.TreeListViewAdapter;

import java.util.List;

public class DepartmentTreeAdapter extends TreeListViewAdapter
{
    public DepartmentTreeAdapter(ListView mTree, Context context, List<Node> datas, int defaultExpandLevel, int iconExpand, int iconNoExpand) {
        super(mTree, context, datas, defaultExpandLevel, iconExpand, iconNoExpand);
    }

    public DepartmentTreeAdapter(ListView mTree, Context context, List<Node> datas,
                                 int defaultExpandLevel) {
        super(mTree, context, datas, defaultExpandLevel);
    }

    @Override
    public View getConvertView(final Node node , int position, View convertView, ViewGroup parent)
    {

        final ViewHolder viewHolder ;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.linkman, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.cb =  convertView
                    .findViewById(R.id.cb_select_tree);
            viewHolder.label = convertView
                    .findViewById(R.id.id_treenode_label);
            viewHolder.icon = convertView.findViewById(R.id.icon);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setChecked(node,viewHolder.cb.isChecked());
            }
        });

        if (node.isChecked()){
            viewHolder.cb.setChecked(true);
        }else {
            viewHolder.cb.setChecked(false);
        }

        if (node.getIcon() == -1) {
            viewHolder.icon.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.icon.setVisibility(View.VISIBLE);
            viewHolder.icon.setImageResource(node.getIcon());
        }

        viewHolder.label.setText(node.getName());

        return convertView;
    }

    private final class ViewHolder
    {
        ImageView icon;
        CheckBox cb;
        TextView label;
    }

    @Override
    protected void setChecked(Node node, boolean checked) {
        setAllChecked(false);
        node.setChecked(checked);
        notifyDataSetChanged();
    }

    private void setAllChecked(boolean checked){
        for (Node node: mAllNodes) {
            node.setChecked(checked);
        }
    }
}

