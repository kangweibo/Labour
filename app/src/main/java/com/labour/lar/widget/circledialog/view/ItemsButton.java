package com.labour.lar.widget.circledialog.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.labour.lar.widget.circledialog.params.ButtonParams;
import com.labour.lar.widget.circledialog.params.CircleParams;
import com.labour.lar.widget.circledialog.res.drawable.SelectorBtn;
import com.labour.lar.widget.circledialog.res.values.CircleColor;
import com.labour.lar.widget.circledialog.res.values.CircleDimen;
import com.labour.lar.widget.circledialog.scale.ScaleUtils;

/**
 * 列表对话框的取消按钮视图
 * Created by hupei on 2017/3/30.
 */
class ItemsButton extends ScaleTextView {

    public ItemsButton(Context context, CircleParams params) {
        super(context);
        init(params);
    }

    private void init(CircleParams params) {
        ButtonParams negativeParams = params.getNegativeParams();
        final ButtonParams buttonParams = negativeParams != null ? negativeParams : params.getPositiveParams();
        //为列表显示时，设置列表与按钮之间的距离
        if (params.getItemsParams() != null) buttonParams.topMargin = CircleDimen.BUTTON_ITEMS_MARGIN;

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, 1);
        layoutParams.topMargin = ScaleUtils.scaleValue(buttonParams.topMargin);
        setLayoutParams(layoutParams);

        setClickable(true);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonParams.dismiss();
                if (buttonParams.listener != null) buttonParams.listener.onClick(v);
            }
        });
        setText(buttonParams.text);
        setTextSize(buttonParams.textSize);
        setTextColor(buttonParams.textColor);
        setHeight(buttonParams.height);

        //如果取消按钮没有背景色，则使用默认色
        int backgroundColor = buttonParams.backgroundColor != 0 ? buttonParams.backgroundColor : CircleColor.bgDialog;
        int radius = params.getDialogParams().radius;
        setBackground(new SelectorBtn(backgroundColor, radius, radius, radius, radius));
    }
}
