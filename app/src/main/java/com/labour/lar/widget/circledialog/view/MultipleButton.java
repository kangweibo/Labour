package com.labour.lar.widget.circledialog.view;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.labour.lar.widget.circledialog.params.ButtonParams;
import com.labour.lar.widget.circledialog.params.CircleParams;
import com.labour.lar.widget.circledialog.res.drawable.SelectorBtn;
import com.labour.lar.widget.circledialog.res.values.CircleColor;

/**
 * 对话框确定按钮与取消的视图
 * Created by hupei on 2017/3/30.
 */
class MultipleButton extends ScaleLinearLayout {
    private ButtonParams mNegativeParams;
    private ButtonParams mPositiveParams;
    private ScaleTextView mNegativeButton;
    private ScaleTextView mPositiveButton;

    public MultipleButton(Context context, CircleParams params) {
        super(context);
        init(params);
    }

    private void init(CircleParams params) {
        setOrientation(HORIZONTAL);

        mNegativeParams = params.getNegativeParams();
        mPositiveParams = params.getPositiveParams();

        int radius = params.getDialogParams().radius;

        //取消按钮
        createNegative(radius);

        //添加二人按钮之间的分隔线
        DividerView dividerView = new DividerView(getContext());
        addView(dividerView);

        //确定按钮
        createPositive( radius);
    }

    private void createNegative(int radius) {
        mNegativeButton = new ScaleTextView(getContext());
        mNegativeButton.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup
                .LayoutParams.WRAP_CONTENT, 1));

        mNegativeButton.setText(mNegativeParams.text);
        mNegativeButton.setTextSize(mNegativeParams.textSize);
        mNegativeButton.setTextColor(mNegativeParams.textColor);
        mNegativeButton.setHeight(mNegativeParams.height);

        int backgroundNegative = mNegativeParams.backgroundColor != 0 ? mNegativeParams.backgroundColor : CircleColor
                .bgDialog;//如果取消按钮没有背景色，则使用默认色
        mNegativeButton.setBackground(new SelectorBtn(backgroundNegative, 0, 0, 0, radius));//按钮左下方为圆角

        regNegativeListener();

        addView(mNegativeButton);
    }

    private void createPositive(int radius) {

        mPositiveButton = new ScaleTextView(getContext());
        mPositiveButton.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup
                .LayoutParams.WRAP_CONTENT, 1));

        mPositiveButton.setText(mPositiveParams.text);
        mPositiveButton.setTextSize(mPositiveParams.textSize);
        mPositiveButton.setTextColor(mPositiveParams.textColor);
        mPositiveButton.setHeight(mPositiveParams.height);

        int backgroundPositive = mPositiveParams.backgroundColor != 0 ? mPositiveParams.backgroundColor : CircleColor
                .bgDialog;//如果取消按钮没有背景色，则使用默认色
        mPositiveButton.setBackground(new SelectorBtn(backgroundPositive, 0, 0, radius, 0));//取消按钮右下方为圆角

        regPositiveListener();

        addView(mPositiveButton);
    }

    private void regNegativeListener() {
        mNegativeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mNegativeParams.dismiss();
                if (mNegativeParams.listener != null) mNegativeParams.listener.onClick(v);
            }
        });
    }

    private void regPositiveListener() {
        mPositiveButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mPositiveParams.dismiss();
                if (mPositiveParams.listener != null) mPositiveParams.listener.onClick(v);
            }
        });
    }

    public void regOnInputClickListener(final EditText input) {
        mPositiveButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = input.getText().toString();
                if (!TextUtils.isEmpty(text))
                    mPositiveParams.dismiss();
                if (mPositiveParams.inputListener != null)
                    mPositiveParams.inputListener.onClick(text, v);
            }
        });
    }
}
