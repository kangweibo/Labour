package com.labour.lar.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.labour.lar.R;
import com.labour.lar.widget.RoundImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProjectListItemWarp {

    public static void showOne(ItemHolder itemHolder,ListItem item){
        itemHolder.field3_rl.setVisibility(View.GONE);
        itemHolder.field1_tv.setText(item.field1);
        itemHolder.field1_content_tv.setText(item.field1Content);
        itemHolder.field2_tv.setText(item.field2);
        itemHolder.field2_content_tv.setText(item.field2Content);
        itemHolder.arraw_iv.setVisibility(item.isShowArraw?View.VISIBLE:View.GONE);
    }
    public static void showTwo(ItemHolder itemHolder,ListItem item){
        itemHolder.field1_tv.setText(item.field1);
        itemHolder.field1_content_tv.setText(item.field1Content);
        itemHolder.field2_tv.setText(item.field2);
        itemHolder.field2_content_tv.setText(item.field2Content);
        itemHolder.field3_tv.setText(item.field3);
        itemHolder.field3_content_tv.setText(item.field3Content);
        itemHolder.arraw_iv.setVisibility(item.isShowArraw?View.VISIBLE:View.GONE);
    }
    public static class ListItem {
        public String field1;
        public String field1Content;
        public String field2;
        public String field2Content;
        public String field3;
        public String field3Content;

        public boolean isShowArraw;
        public int type;
    }
    public static class ItemHolder {
        public ItemHolder(View view){
            ButterKnife.bind(this, view);
        }

        @BindView(R.id.field1_rl)
        RelativeLayout field1_rl;
        @BindView(R.id.field1_tv)
        TextView field1_tv;
        @BindView(R.id.field1_content_tv)
        TextView field1_content_tv;

        @BindView(R.id.field2_rl)
        RelativeLayout field2_rl;
        @BindView(R.id.field2_tv)
        TextView field2_tv;
        @BindView(R.id.field2_content_tv)
        TextView field2_content_tv;

        @BindView(R.id.field3_rl)
        RelativeLayout field3_rl;
        @BindView(R.id.field3_tv)
        TextView field3_tv;
        @BindView(R.id.field3_content_tv)
        TextView field3_content_tv;

        @BindView(R.id.arraw_iv)
        ImageView arraw_iv;
    }
}
