package com.labour.lar.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseExpandableAdapter<GI,I,GH,H>  extends BaseExpandableListAdapter {
    protected Context mContext;
    protected LayoutInflater mInflater;

    private List<GI> mGroup = new ArrayList<>();
    private List<ArrayList<I>> mItemList = new ArrayList<>();;

    public BaseExpandableAdapter(Context context) {
        this.mContext = context;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setGroupList(List<GI> headers){
        if(mGroup != null){
            this.mGroup.clear();
            this.mGroup = headers;
        }
    }

    public void setChildList(List<ArrayList<I>> datas){
        if(mItemList != null){
            this.mItemList.clear();
            this.mItemList = datas;
        }
    }

    @Override
    public int getGroupCount() {
        return mGroup.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mItemList.get(groupPosition).size();
    }

    @Override
    public GI getGroup(int groupPosition) {
        return mGroup.get(groupPosition);
    }

    @Override
    public I getChild(int groupPosition, int childPosition) {
        return mItemList.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = createGroupView(groupPosition, isExpanded, convertView, parent);
        }

        GH holder = (GH)convertView.getTag();
        GI item = mGroup.get(groupPosition);

        fillGroupView(groupPosition,isExpanded,item, holder);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = createChildView(groupPosition, childPosition, convertView, parent);
        }

        H holder = (H)convertView.getTag();
        I item = mItemList.get(groupPosition).get(childPosition);

        fillChildView(groupPosition, childPosition,item, holder);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    /**
     *
     * @param groupPosition
     * @param convertView
     * @param parent
     * @return
     */
    abstract protected View createGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent);

    /**
     *
     * @param groupPosition
     * @param childPosition
     * @param convertView
     * @param parent
     * @return
     */
    abstract protected View createChildView(int groupPosition, int childPosition, View convertView, ViewGroup parent);

    /**
     * @param item
     * @param holder
     */
    abstract protected void fillGroupView(int groupPosition, boolean isExpanded ,GI item, GH holder);

    /**
     * @param item
     * @param holder
     */
    abstract protected void fillChildView(int groupPosition, int childPosition, I item, H holder);

}
