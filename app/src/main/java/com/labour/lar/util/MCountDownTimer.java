package com.labour.lar.util;

import android.os.CountDownTimer;

public class MCountDownTimer extends CountDownTimer {

	private static long remainSecond = 60 * 1000;// second
	private static long intervalSecond = 1000;

	private OnCountDownListener onCountDownListener;

	public MCountDownTimer(long millisInFuture, long countDownInterval) {
		super(millisInFuture, countDownInterval);
	}

	public MCountDownTimer() {
		this(remainSecond, intervalSecond);
	}

	@Override
	public void onTick(long millisUntilFinished) {
		if (onCountDownListener != null) {
			onCountDownListener.onTick(millisUntilFinished / 1000);
		}
	}

	@Override
	public void onFinish() {
		if (onCountDownListener != null) {
			onCountDownListener.onStop();
		}
	}

	public OnCountDownListener getOnCountDownListener() {
		return onCountDownListener;
	}

	public void setOnCountDownListener(OnCountDownListener onCountDownListener) {
		this.onCountDownListener = onCountDownListener;
	}

	public static interface OnCountDownListener {
		public void onStop();
		public void onTick(long second);
	}
}
