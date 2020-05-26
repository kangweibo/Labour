package com.labour.lar.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.CheckedTextView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.labour.lar.R;
import com.labour.lar.util.ScreenUtils;

import java.util.ArrayList;

/**
 * Author: lx
 * CreateDate: 2019/6/15 21:04
 * Company Hebei Xiaoxiong Technology Co., Ltd.
 * Description:
 */
public class TabBarView extends LinearLayout {

	private String tag = TabBarView.class.getName();
	
	private LazyViewPager viewPager;
	
	private Context context;
	private OnTabSelectedListener onTabSelectedListener;
	private OnTabClickListener onTabClickListener;

	private ArrayList<BadgeView> badgeViews = new ArrayList<BadgeView>();
	private ArrayList<FrameLayout> tabViews = new ArrayList<FrameLayout>();

	private int layoutResId;
	private int tabBarBg;
	private int selectedTabIndex;
	private int selectedMode;//1 点击tab页，2滑动viewpager选中

	private int CLICK_SELECTED_MODE = 1;
	private int SLIDE_SELECTED_MODE = 2;
	private int noPagerIndex;//没有对应的fragment
	public TabBarView(Context context) {
		super(context);
		initView(context);
		initEvent();
	}

	public TabBarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initAttributeSet(context,attrs);
		initView(context);
		initEvent();
	}

	private void initAttributeSet(Context context, AttributeSet attrs){
		TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.TabBarView);
		layoutResId = array.getResourceId(R.styleable.TabBarView_layout, 0);
		tabBarBg = array.getResourceId(R.styleable.TabBarView_tabbar_bg, 0);
		selectedTabIndex = array.getInteger(R.styleable.TabBarView_selectedIndex, 0);
		noPagerIndex = array.getInteger(R.styleable.TabBarView_noPagerIndex, -1);

		array.recycle();
	}
	
	public LazyViewPager getViewPager() {
		return viewPager;
	}

	public void setViewPager(LazyViewPager viewPager) {
		this.viewPager = viewPager;
		if(this.viewPager != null){
			setViewPagerEvent(this.viewPager);
		}
	}

	public void setNoPagerIndex(int noPagerIndex) {
		this.noPagerIndex = noPagerIndex;
	}

	private void initView(final Context context){
		this.context = context;
		this.setOrientation(LinearLayout.HORIZONTAL);
		if(tabBarBg > 0){
			setBackgroundColor(context.getResources().getColor(tabBarBg));
		}
		
		ViewGroup viewGroup = (ViewGroup) View.inflate(context, layoutResId, null);
		int childCount = viewGroup.getChildCount();
		if(childCount > 0){
			ArrayList<View> views = new ArrayList<View>();
			for(int i=0;i< childCount;i++){
				View childView = viewGroup.getChildAt(i);
				views.add(childView);
			}

			LayoutParams lp = new LayoutParams(0, LayoutParams.WRAP_CONTENT);
			lp.weight = 1;

			FrameLayout.LayoutParams lp2 = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
			lp2.gravity = Gravity.CENTER;

			int count = 0;
			for(int i=0;i< childCount;i++){
				View childView = views.get(i);
				ViewParent vp = childView.getParent();
				if(vp != null){
					((ViewGroup)vp).removeView(childView);
				}

				if(childView instanceof CheckedTextView){
					FrameLayout fl = new FrameLayout(context);
					fl.setId(childView.getId());
					fl.addView(childView,lp2);
					addView(fl,lp);

					BadgeView bv = new BadgeView(context,childView);
					badgeViews.add(bv);

					childView.setId(0);

					if(count == noPagerIndex){
						fl.setTag(noPagerIndex);
					}
					tabViews.add(fl);
					count++;
				} else {
					addView(childView);
				}
			}
		}
	}
	
	private void initEvent(){
		OnClickListener onClickListener = new OnClickListener(){
			@Override
			public void onClick(View v) {
				boolean f = false;
				if(onTabClickListener != null){
					f = onTabClickListener.onTabClick(v);
				}

				if(!f){
					clickedSelectedTab(v);
				}
			}
		};
		for(int i=0;i<tabViews.size();i++){
			ViewGroup childView = tabViews.get(i);
			childView.setOnClickListener(onClickListener);
		}
	}


	private void setViewPagerEvent(LazyViewPager viewPager){
		viewPager.setOnPageChangeListener(new LazyViewPager.OnPageChangeListener(){

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				selectedMode = SLIDE_SELECTED_MODE;
			}
			
			@Override
			public void onPageSelected(int arg0) {
				Log.i(tag, "viewPager selected.");
				if(selectedMode != CLICK_SELECTED_MODE){
					Log.i(tag, "viewPager selected...selectedMode=SLIDE_SELECTED_MODE");
					slideSelectedTab(arg0);
				}
			}
		});
	}

	private CheckedTextView getChildView(ViewGroup childView){
		FrameLayout fl = (FrameLayout)childView.getChildAt(0);
		CheckedTextView checkedChildView = (CheckedTextView)fl.getChildAt(0);
		return  checkedChildView;
	}

	private void clickedSelectedTab(View v){
		selectedMode = CLICK_SELECTED_MODE;
		
		boolean isChecked = false;
		for(int i=0;i<tabViews.size();i++){
			ViewGroup childView = tabViews.get(i);
			CheckedTextView checkedChildView = getChildView(childView);
			if(childView.getId() == v.getId()){
				isChecked = true;
				selectedTabIndex = i;
				checkedChildView.setChecked(true);
			} else {
				checkedChildView.setChecked(false);
			}
		}
		if(isChecked && viewPager != null){
			viewPager.setCurrentItem(selectedTabIndex);
		}
		if(isChecked && onTabSelectedListener != null){
			onTabSelectedListener.onTabSelected(selectedTabIndex);
		}
	}
	
	private void slideSelectedTab(int index){
		selectedMode = SLIDE_SELECTED_MODE;
		
		boolean isChecked = false;
		for(int i=0;i<tabViews.size();i++){
			ViewGroup childView = tabViews.get(i);
			CheckedTextView checkedChildView =getChildView(childView);
			if(index == i){
				isChecked = true;
				selectedTabIndex = i;
				checkedChildView.setChecked(true);
			} else {
				checkedChildView.setChecked(false);
			}
		}
		if(isChecked && onTabSelectedListener != null){
			onTabSelectedListener.onTabSelected(selectedTabIndex);
		}
	}
	
	public void showNum(int index,int num){
		for(int i=0;i<tabViews.size();i++){
			if(i == index){
				BadgeView bv = badgeViews.get(index);
				bv.setBadgePosition(BadgeView.POSITION_TOP_LEFT);
				bv.setBadgeMargin((int) ScreenUtils.dpToPx(context, 30),0);
				bv.setText(Integer.toString(num));
				bv.show();
			}
		}
	}
	
	public void hideNum(int index){
		for(int i=0;i<tabViews.size();i++){
			if(i == index){
				BadgeView bv = badgeViews.get(index);
				bv.hide();
			}
		}
	}
 
	public int getSelectedTabIndex() {
		return selectedTabIndex;
	}

	public void selectedTab(int selectedTabIndex){
		this.selectedTabIndex = selectedTabIndex;
		for(int i=0;i<tabViews.size();i++){
			ViewGroup childView = tabViews.get(i);
			CheckedTextView checkedChildView = getChildView(childView);
			if(selectedTabIndex == i){
				checkedChildView.setChecked(true);
			} else {
				checkedChildView.setChecked(false);
			}
		}

		if(viewPager != null){
			selectedMode = SLIDE_SELECTED_MODE;
			viewPager.setCurrentItem(selectedTabIndex);
			if(selectedTabIndex == 0){ //viewPager 默认第一屏为0 不回调OnPageChangeListener接口，这里增加默认设置
				slideSelectedTab(0);


			}
		}
	}

	public OnTabSelectedListener getOnTabSelectedListener() {
		return onTabSelectedListener;
	}

	public void setOnTabSelectedListener(OnTabSelectedListener onTabSelectedListener) {
		this.onTabSelectedListener = onTabSelectedListener;
	}

	public void setOnTabClickListener(OnTabClickListener onTabClickListener,int... position) {
		this.onTabClickListener = onTabClickListener;
	}

	public static interface OnTabSelectedListener {
		public void onTabSelected(int position);
	}

	public static interface OnTabClickListener {
		public boolean onTabClick(View v);
	}

}
