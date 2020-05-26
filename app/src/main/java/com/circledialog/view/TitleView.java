package com.circledialog.view;

import android.content.Context;

import com.circledialog.params.CircleParams;
import com.circledialog.params.DialogParams;
import com.circledialog.params.TitleParams;
import com.circledialog.res.drawable.CircleDrawable;
import com.circledialog.res.values.CircleColor;


/**
 * 对话框标题
 * Created by hupei on 2017/3/29.
 */
class TitleView extends ScaleTextView {

    public TitleView(Context context, CircleParams params) {
        super(context);
        init(params);
    }

    private void init(CircleParams params) {
        DialogParams dialogParams = params.getDialogParams();
        TitleParams titleParams = params.getTitleParams();

        //如果标题没有背景色，则使用默认色
        int backgroundColor = titleParams.backgroundColor != 0 ? titleParams.backgroundColor : CircleColor.bgDialog;

        //有内容则顶部圆角
        if (params.getTextParams() != null || params.getItemsParams() != null || params.getProgressParams() != null
                || params.getInputParams() != null) {
            setBackground(new CircleDrawable(backgroundColor, dialogParams.radius, dialogParams.radius, 0, 0));
        }
        //无内容则全部圆角
        else setBackground(new CircleDrawable(backgroundColor, dialogParams.radius));

        setHeight(titleParams.height);
        setTextColor(titleParams.textColor);
        setTextSize(titleParams.textSize);
        setText(titleParams.text);
    }
}
