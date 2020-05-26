package com.labour.lar.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.AccelerateInterpolator;

import java.lang.reflect.Field;

public class MainScrollViewPager extends LazyViewPager {
	private boolean isCanScroll = false;

	public MainScrollViewPager(Context context) {
		super(context);
		init();
	}

	public MainScrollViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		attach(this);
	}

	public void setCanScroll(boolean isCanScroll) {
		this.isCanScroll = isCanScroll;
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		if (isCanScroll) {
			try {
				return super.onTouchEvent(arg0);
			} catch(Exception e){
				e.printStackTrace();
				return false;
			}
		} else {
			return false;
		}
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		if (isCanScroll) {
			try {
				return super.onInterceptTouchEvent(arg0);
			} catch(Exception e){
				e.printStackTrace();
				return false;
			}
		} else {
			return false;
		}

		// if (isCanScroll) {
		// return super.onInterceptTouchEvent(arg0);
		// } else {
		// return false;
		// }
	}

	public void attach(LazyViewPager mViewPager) {
		/* 主要代码段 */
		try {
			Field mField = LazyViewPager.class.getDeclaredField("mScroller");
			mField.setAccessible(true);
			// 设置加速度
			// ，通过改变FixedSpeedScroller这个类中的mDuration来改变动画时间（如mScroller.setmDuration(mMyDuration);）
			CustomDurationScroller mScroller = new CustomDurationScroller(mViewPager.getContext(), new AccelerateInterpolator());
			mScroller.setScrollDurationFactor(0);

			mField.set(mViewPager, mScroller);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
