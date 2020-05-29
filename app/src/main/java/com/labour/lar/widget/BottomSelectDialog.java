package com.labour.lar.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import com.labour.lar.R;

public class BottomSelectDialog extends PopupWindow {

    private Context context;
    private BottomSelectDialogListener listener;
    private View rootView;

    public interface BottomSelectDialogListener {
        int getLayout();
        void initView(View view);
        void onClick(Dialog dialog,int rate);
    }
    public BottomSelectDialog(Context context, BottomSelectDialogListener listener){
        super(context);
        this.context=context;
        this.listener=listener;
        rootView = View.inflate(this.context,listener.getLayout(),null);
        this.setContentView(rootView);
         setFocusable(true); //设置PopupWindow可获得焦点
        setAnimationStyle(R.style.dialogWindowAnim);
        this.setWidth(LayoutParams.FILL_PARENT);
        this.setHeight(LayoutParams.WRAP_CONTENT);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(Color.parseColor("#98939380"));//0xb0000000
        setBackgroundDrawable(dw);//new BitmapDrawable()
        rootView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                int height = rootView.findViewById(R.id.pop_layout).getTop();
                int y=(int) event.getY();
                if(event.getAction()==MotionEvent.ACTION_UP){
                    if(y<height){
                        dismiss();
                    }
                }
                return true;
            }
        });
        rootView.setOnKeyListener(new View.OnKeyListener(){
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dismiss();
                    return true;
                }
                return false;
            }
        });
        iniView();
    }

    private void iniView(){
        listener.initView(rootView);
    }
}
