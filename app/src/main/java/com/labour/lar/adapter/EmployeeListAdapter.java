package com.labour.lar.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.labour.lar.BaseAdapter;
import com.labour.lar.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class EmployeeListAdapter extends BaseAdapter<EmployeeListAdapter.ListItem, EmployeeListAdapter.ItemHolder> {

    private OnButtonClickListener onButtonClickListener;

    public EmployeeListAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    protected View createView(int position, View convertView, ViewGroup parent) {
        View view = this.mInflater.inflate(R.layout.frag_employee_list_item, null);
        ItemHolder holder = new ItemHolder(view);
        view.setTag(holder);
        return view;
    }

    @Override
    protected void fillView(int position, ListItem item, ItemHolder holder) {
        holder.field1_tv.setText(item.field1);
        holder.field1_content_tv.setText(item.field1Content);
        holder.field2_tv.setText(item.field2);
        holder.field2_content_tv.setText(item.field2Content);

        if (item.type == 1){
            holder.field2_rl.setVisibility(View.GONE);
        } else {
            holder.field2_rl.setVisibility(View.VISIBLE);
        }

        if (item.status == 1){
            holder.field1_tv.setTextColor(mContext.getResources().getColor(R.color.common_blue));
        } else {
            holder.field1_tv.setTextColor(mContext.getResources().getColor(R.color.common_red));
        }

        holder.arraw_iv.setVisibility(item.isShowArraw?View.VISIBLE:View.GONE);
//        holder.btn_examine.setVisibility(item.isShowPass?View.VISIBLE:View.GONE);

        holder.btn_examine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onButtonClickListener != null) {
                    onButtonClickListener.onItemClick(v, position);
                }
            }
        });
    }

    public static class ListItem {
        public String field1;
        public String field1Content;
        public String field2;
        public String field2Content;

        public boolean isShowArraw;
        public boolean isShowPass;
        public int type;
        public int status;
    }

    class ItemHolder {
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

        @BindView(R.id.btn_examine)
        Button btn_examine;

        @BindView(R.id.arraw_iv)
        ImageView arraw_iv;
    }

    public void setOnButtonClickListener(OnButtonClickListener onButtonClickListener) {
        this.onButtonClickListener = onButtonClickListener;
    }

    public interface OnButtonClickListener {
        void onItemClick(View view, int position);
    }
}
