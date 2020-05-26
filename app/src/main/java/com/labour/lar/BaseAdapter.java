package com.labour.lar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseAdapter<I,H> extends android.widget.BaseAdapter {

	protected List<I> items = new ArrayList<I>();
	protected LayoutInflater mInflater;
	protected Context mContext;

	protected BaseAdapter(Context mContext){
		this.mContext = mContext;
		this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	public void setList(List<I> datas){
		if(items != null){
			this.items.clear();
			this.items = datas;
		}
	}
	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public I getItem(int position) {
		return items.get(position);
	}

	public void addAll(List<I> items){
		if(items != null){
			this.items.addAll(items);
		}
	}
	public void add(I item){
		if(item != null){
			this.items.add(item);
		}
	}

	public void insert(int position,I item){
		if(item != null){
			this.items.add(position,item);
		}
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public List<I> getList(){
		return this.items;
	}
	public void remove(int position){
		items.remove(position);
	}
	public void remove(I item){
		items.remove(item);
	}
	public void clear(){
		items.clear();
	}
	@SuppressWarnings("unchecked")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if(convertView == null){
			convertView = createView(position, convertView, parent);
		}
		
		H holder = (H)convertView.getTag();
		I item = items.get(position);
		
		fillView(position,item, holder);
		
		return convertView;
	}
	/**
	 *
	 * @param position
	 * @param convertView
	 * @param parent
	 * @return
	 */
	abstract protected View createView(int position, View convertView, ViewGroup parent);
	/**
	 * @param item
	 * @param holder
	 */
	abstract protected void fillView(int position,I item, H holder);
}
