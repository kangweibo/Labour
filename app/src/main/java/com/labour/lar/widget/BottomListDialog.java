package com.labour.lar.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.labour.lar.R;

import java.util.List;

public class BottomListDialog {

    private BottomSelectDialog dialog;
    private View popupWindow_view;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public BottomListDialog(Context context, String title, List<String> date, OnItemClickListener listener){
        this.onItemClickListener = listener;

        dialog = new BottomSelectDialog(context, new BottomSelectDialog.BottomSelectDialogListener() {
            @Override
            public int getLayout() {
                popupWindow_view = LayoutInflater.from(context).inflate(R.layout.menu_list, null);
                return R.layout.menu_list;
            }

            @Override
            public void initView(View view) {
                View.OnClickListener onClickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                };

                TextView txt_title = view.findViewById(R.id.txt_title);
                TextView txt_cancel = view.findViewById(R.id.txt_cancel);
                txt_title.setText(title);
                txt_cancel.setOnClickListener(onClickListener);

                ListView listView = view.findViewById(R.id.lv_data);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.menu_list_item, date);
                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (onItemClickListener != null){
                            onItemClickListener.onItemClick(position);
                        }
                        dialog.dismiss();
                    }
                });
            }

            @Override
            public void onClick(Dialog dialog, int rate) {

            }
        });
    }

    public void showAtLocation(int gravity, int x, int y) {
        dialog.showAtLocation(popupWindow_view, gravity, x, y);
    }
}
