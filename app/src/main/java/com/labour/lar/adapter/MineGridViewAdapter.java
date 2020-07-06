package com.labour.lar.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.labour.lar.R;

public class MineGridViewAdapter extends BaseAdapter {
    private Context context;

    private int [] imgs = {R.mipmap.personal_icon,R.mipmap.xiangmu_icon,R.mipmap.kaoqinbaobiao_icon,R.mipmap.xiaoxitongzhi_icon,
            R.mipmap.seting_icon,R.mipmap.help_icon,R.mipmap.xiangmu_icon
    };

    private  String [] strs = {"个人信息","工程项目","考勤报表","加入班组","设置","帮助","银行卡管理"
    };

    public MineGridViewAdapter(Context context) {
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

    public void setStrings(String[] strs) {
        this.strs = strs;
    }
}
