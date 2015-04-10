package com.jincaizi.kuaiwin.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.jincaizi.R;

@SuppressLint("HandlerLeak")
public class SlidingTextView extends LinearLayout {

	private String[] showTexts = new String[] {"快赢网欢迎您"};
	//private String[] showTexts;
	private int count = 0;
	private int mDuration;
	private TextView text;
	private int textHeight = 20;

	@SuppressLint("Recycle")
	public SlidingTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.SlidingText);
		mDuration = a
				.getInteger(R.styleable.SlidingText_animationDuration, 750);
	}
	
	public void setShowText(String[] showTexts){
		this.showTexts=showTexts;
	}

	protected void onFinishInflate() {
		super.onFinishInflate();

		text = (TextView) this.getChildAt(0);

		mHandler.postDelayed(appear, 1000);
	}


	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		text.getWidth();
		textHeight = text.getHeight();
	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.arg1) {
			case 1:
				doAnimationOpen();
				break;
			case 2:
				doAnimationClose();
				break;
			}
		}
	};

	public void doAnimationOpen() {
		post(appear);
	}

	Runnable appear = new Runnable() {
		public void run() {
			TranslateAnimation animation;
			int fromXDelta = 0, toXDelta = 0, fromYDelta = 0, toYDelta = 0;
			int calculatedDuration = 0;
			
			fromYDelta = textHeight;
			toYDelta = 0;
			calculatedDuration = mDuration * Math.abs(toYDelta - fromYDelta)
					/ textHeight;

			animation = new TranslateAnimation(fromXDelta, toXDelta,
					fromYDelta, toYDelta);
			animation.setDuration(calculatedDuration);
			animation.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationStart(Animation animation) {
					if(showTexts!=null && showTexts.length!=0){
						count = (count + 1) % showTexts.length;
						text.setText(showTexts[count]);
					}
					text.setVisibility(VISIBLE);
				}

				@Override
				public void onAnimationRepeat(Animation animation) {

				}

				@Override
				public void onAnimationEnd(Animation animation) {
					mHandler.postDelayed(hide, 2500);
				}
			});
			startAnimation(animation);

		}
	};

	public void doAnimationClose() {
		post(hide);
	}
	
	Runnable hide = new Runnable() {
		public void run() {
			TranslateAnimation animation;
			int fromXDelta = 0, toXDelta = 0, fromYDelta = 0, toYDelta = 0;
			int calculatedDuration;

			toYDelta = -1 * textHeight;

			calculatedDuration = mDuration * Math.abs(toYDelta - fromYDelta)
					/ textHeight;
			animation = new TranslateAnimation(fromXDelta, toXDelta,
					fromYDelta, toYDelta);
			animation.setDuration(calculatedDuration);
			animation.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationStart(Animation animation) {

				}

				@Override
				public void onAnimationRepeat(Animation animation) {

				}

				@Override
				public void onAnimationEnd(Animation animation) {
					mHandler.postDelayed(appear, 100);
					text.setVisibility(INVISIBLE);
				}
			});
			startAnimation(animation);
		}
	};
}
