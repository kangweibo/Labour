package com.labour.lar.widget;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.labour.lar.R;

/**
 * <com.sale.widget.LoadingView
 *           android:id="@+id/loadingView"
 *           android:layout_width="match_parent"
 *           android:layout_height="match_parent" />
 * @author js9
 *
 */
public class LoadingView extends RelativeLayout {

	public LoadingView(Context context) {
		super(context);
		initView(context);
	}
	
	public LoadingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}
	
	private void initView(Context context){
		LoadingImageView loadingImageView = new LoadingImageView(context);
		this.setBackgroundResource(R.drawable.transparent_bg);
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.addRule(RelativeLayout.CENTER_IN_PARENT);
		this.addView(loadingImageView,lp);
		this.setFocusable(true);
		this.setClickable(true);
	}
 
	class LoadingImageView extends android.support.v7.widget.AppCompatImageView {

		private AnimationDrawable animDrawable;
		
		public LoadingImageView(Context context) {
			super(context);
			init();
		}
		
		public LoadingImageView(Context context, AttributeSet attrs) {
			super(context, attrs);
			init();
		}
		private void init(){
			setImageResource(R.drawable.anim_loading_bg);
			animDrawable = (AnimationDrawable)getDrawable();
			if(animDrawable.isRunning()){
				animDrawable.stop();
			}
			animDrawable.start();
		}
		@Override
		protected void onDetachedFromWindow() {
			if(animDrawable != null){
				animDrawable.stop();
			}
			setImageBitmap(null);
			super.onDetachedFromWindow();
		}
	}
}
