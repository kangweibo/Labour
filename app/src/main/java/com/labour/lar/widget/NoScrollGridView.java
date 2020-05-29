package com.labour.lar.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridView;

public class NoScrollGridView extends GridView {

	public NoScrollGridView(Context context) {
		super(context);
		init(context);
	}

	public NoScrollGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public NoScrollGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}
	private void init(Context context){
		this.setVerticalFadingEdgeEnabled(false);
		this.setOverScrollMode(View.OVER_SCROLL_NEVER);
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if (getLayoutParams().height == LayoutParams.WRAP_CONTENT) {
			heightMeasureSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
}
