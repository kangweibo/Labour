package com.labour.lar.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import java.lang.reflect.Field;

public class CustomDurationScroller extends Scroller {

	private double scrollFactor = 1;
	
	public CustomDurationScroller(Context context) {
		super(context);
	}

	public CustomDurationScroller(Context context, Interpolator interpolator) {
		super(context, interpolator);
	}
	
//	 /**
//     * not exist in android 2.3
//     *
//     * @param context
//     * @param interpolator
//     * @param flywheel
//     */
    // @SuppressLint("NewApi")
    // public CustomDurationScroller(Context context, Interpolator interpolator, boolean flywheel){
    // super(context, interpolator, flywheel);
    // }
	
	@Override
	public void startScroll(int startX, int startY, int dx, int dy, int duration) {
		super.startScroll(startX, startY, dx, dy, (int)(duration * scrollFactor));
	}

	public double getScrollDurationFactor() {
		return scrollFactor;
	}

	public void setScrollDurationFactor(double mDuration) {
		this.scrollFactor = mDuration;
	}

	public static void attach(ViewPager mViewPager) {
		/* 主要代码段 */
		try {
			Field mField = ViewPager.class.getDeclaredField("mScroller");
			mField.setAccessible(true);
			// 设置加速度
			// ，通过改变FixedSpeedScroller这个类中的mDuration来改变动画时间（如mScroller.setmDuration(mMyDuration);）
			CustomDurationScroller mScroller = new CustomDurationScroller(mViewPager.getContext(),
					new AccelerateInterpolator());
			mScroller.setScrollDurationFactor(0);
			
			mField.set(mViewPager, mScroller);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
