package com.labour.lar.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.labour.lar.R;

public class MyGridViewAdapter extends BaseAdapter {
    private Context context;

    private Integer [] imgs = {};

    private  String [] strs = {};

    public MyGridViewAdapter(Context context) {
        super();
        this.context = context;
    }

    @Override
    public int getCount() {
        return imgs.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.mine_item,parent,false);
            holder = new ViewHolder();
            holder.imageView = (ImageView)convertView.findViewById(R.id.item_img);
            holder.textView = (TextView)convertView.findViewById(R.id.item_txt);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder)convertView.getTag();
        }
        holder.imageView.setBackgroundResource(imgs[position]);
        holder.textView.setText(strs[position]);

        return convertView;
    }
    class ViewHolder{
        ImageView imageView;
        TextView textView;
    }

    public void setImgs(Integer[] imgs) {
        this.imgs = imgs;
    }

    public void setStrings(String[] strs) {
        this.strs = strs;
    }
}
