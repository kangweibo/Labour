package com.labour.lar.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.labour.lar.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProjectListItemWarp {

    public static void showOne(ItemHolder itemHolder,ListItem item){
        itemHolder.field_r2.setVisibility(View.GONE);
        itemHolder.field_r3.setVisibility(View.GONE);
        itemHolder.field_r4.setVisibility(View.GONE);
        itemHolder.field_r5.setVisibility(View.GONE);

        itemHolder.field_1_1_tv.setText(item.field_1_1);
        itemHolder.field_1_2_tv.setText(item.field_1_2);
        itemHolder.field_1_3_tv.setText(item.field_1_3);

        itemHolder.arraw_iv.setVisibility(item.isShowArraw?View.VISIBLE:View.GONE);
    }
    public static void showTwo(ItemHolder itemHolder,ListItem item){
        itemHolder.field_r3.setVisibility(View.GONE);
        itemHolder.field_r4.setVisibility(View.GONE);
        itemHolder.field_r5.setVisibility(View.GONE);

        itemHolder.field_1_1_tv.setText(item.field_1_1);
        itemHolder.field_1_2_tv.setText(item.field_1_2);
        itemHolder.field_1_3_tv.setText(item.field_1_3);
        itemHolder.field_2_1_tv.setText(item.field_2_1);
        itemHolder.field_2_2_tv.setText(item.field_2_2);

        itemHolder.arraw_iv.setVisibility(item.isShowArraw?View.VISIBLE:View.GONE);
    }

    public static void showThree(ItemHolder itemHolder,ListItem item){
        itemHolder.field_r4.setVisibility(View.GONE);
        itemHolder.field_r5.setVisibility(View.GONE);

        itemHolder.field_1_1_tv.setText(item.field_1_1);
        itemHolder.field_1_2_tv.setText(item.field_1_2);
        itemHolder.field_1_3_tv.setText(item.field_1_3);
        itemHolder.field_2_1_tv.setText(item.field_2_1);
        itemHolder.field_2_2_tv.setText(item.field_2_2);
        itemHolder.field_3_1_tv.setText(item.field_3_1);
        itemHolder.field_3_2_tv.setText(item.field_3_2);

        itemHolder.arraw_iv.setVisibility(item.isShowArraw?View.VISIBLE:View.GONE);
    }

    public static void showFour(ItemHolder itemHolder,ListItem item){
        itemHolder.field_r5.setVisibility(View.GONE);

        itemHolder.field_1_1_tv.setText(item.field_1_1);
        itemHolder.field_1_2_tv.setText(item.field_1_2);
        itemHolder.field_1_3_tv.setText(item.field_1_3);
        itemHolder.field_2_1_tv.setText(item.field_2_1);
        itemHolder.field_2_2_tv.setText(item.field_2_2);
        itemHolder.field_3_1_tv.setText(item.field_3_1);
        itemHolder.field_3_2_tv.setText(item.field_3_2);
        itemHolder.field_4_1_tv.setText(item.field_4_1);
        itemHolder.field_4_2_tv.setText(item.field_4_2);

        itemHolder.arraw_iv.setVisibility(item.isShowArraw?View.VISIBLE:View.GONE);
    }

    public static void showFive(ItemHolder itemHolder,ListItem item){
        itemHolder.field_1_1_tv.setText(item.field_1_1);
        itemHolder.field_1_2_tv.setText(item.field_1_2);
        itemHolder.field_1_3_tv.setText(item.field_1_3);
        itemHolder.field_2_1_tv.setText(item.field_2_1);
        itemHolder.field_2_2_tv.setText(item.field_2_2);
        itemHolder.field_3_1_tv.setText(item.field_3_1);
        itemHolder.field_3_2_tv.setText(item.field_3_2);
        itemHolder.field_4_1_tv.setText(item.field_4_1);
        itemHolder.field_4_2_tv.setText(item.field_4_2);
        itemHolder.field_5_1_tv.setText(item.field_5_1);
        itemHolder.field_5_2_tv.setText(item.field_5_2);

        itemHolder.arraw_iv.setVisibility(item.isShowArraw?View.VISIBLE:View.GONE);
    }

    public static class ListItem {
        public String field_1_1;
        public String field_1_2;
        public String field_1_3;

        public String field_2_1;
        public String field_2_2;
        //public String field_2_3;

        public String field_3_1;
        public String field_3_2;
        //public String field_3_3;

        public String field_4_1;
        public String field_4_2;
        //public String field_4_3;

        public String field_5_1;
        public String field_5_2;

        public boolean isShowArraw;
        public int type;
    }

    public static class ItemHolder {
        public ItemHolder(View view){
            ButterKnife.bind(this, view);
        }

        @BindView(R.id.field_rl)
        View field_rl;
        @BindView(R.id.field_1_1_tv)
        TextView field_1_1_tv;
        @BindView(R.id.field_1_2_tv)
        TextView field_1_2_tv;
        @BindView(R.id.field_1_3_tv)
        TextView field_1_3_tv;

        @BindView(R.id.field_r2)
        View field_r2;
        @BindView(R.id.field_2_1_tv)
        TextView field_2_1_tv;
        @BindView(R.id.field_2_2_tv)
        TextView field_2_2_tv;

        @BindView(R.id.field_r3)
        View field_r3;
        @BindView(R.id.field_3_1_tv)
        TextView field_3_1_tv;
        @BindView(R.id.field_3_2_tv)
        TextView field_3_2_tv;

        @BindView(R.id.field_r4)
        View field_r4;
        @BindView(R.id.field_4_1_tv)
        TextView field_4_1_tv;
        @BindView(R.id.field_4_2_tv)
        TextView field_4_2_tv;

        @BindView(R.id.field_r5)
        View field_r5;
        @BindView(R.id.field_5_1_tv)
        TextView field_5_1_tv;
        @BindView(R.id.field_5_2_tv)
        TextView field_5_2_tv;

        @BindView(R.id.arraw_iv)
        ImageView arraw_iv;
    }
}
