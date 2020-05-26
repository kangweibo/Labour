package com.labour.lar.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.labour.lar.R;
import com.labour.lar.util.StringUtils;

public class ProgressDialog extends Dialog {
    private static ProgressDialog customProgressDialog = null;  

    public ProgressDialog(Context context){
        super(context);  
    }
      
    public ProgressDialog(Context context, int theme) {
        super(context, theme);
    }

    public static ProgressDialog createDialog(Context context){
        return createDialog(context,null);
    }
    public static ProgressDialog createDialog(Context context, String msg){
        View view = View.inflate(context, R.layout.view_progress_dialog,null);
        FrameLayout dialog_fl = (FrameLayout) view.findViewById(R.id.dialog_fl);
        TextView msg_tv = (TextView) view.findViewById(R.id.msg_tv);
        if(StringUtils.isEmpty(msg)){
            msg_tv.setVisibility(View.GONE);
        } else {
            msg_tv.setText(msg);
        }

    	LoadingView loadingView = new LoadingView(context);
        dialog_fl.addView(loadingView);

        customProgressDialog = new ProgressDialog(context, R.style.CustomDialog);
        customProgressDialog.setContentView(view);
        customProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        customProgressDialog.setCanceledOnTouchOutside(false);
        customProgressDialog.setOnDismissListener(new OnDismissListener(){
			@Override
			public void onDismiss(DialogInterface dialog) {
			}
        });
        customProgressDialog.setOnShowListener(new OnShowListener(){
			@Override
			public void onShow(DialogInterface dialog) {
			}
        });
        
        return customProgressDialog;
    }
	public void onWindowFocusChanged(boolean hasFocus){
        if (customProgressDialog == null){
            return;
        }
    }
}
