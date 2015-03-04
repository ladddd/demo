package com.jincaizi.kuaiwin.buycenter.yilou;

import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.jincaizi.R;

public class CustomYilou extends Activity implements OnClickListener{
	private ImageView mBackBtn;
	private Activity mActivity;
	private LinearLayout mLvYilou;
	private String[]titleFunc = {"号码", "出现/理论次数", "出现频率", "平均遗漏", "最大遗漏","上次遗漏", "本次遗漏",
			"欲出几率", "投资价值", "回补几率"};
	private ArrayList<String> mNumber = new ArrayList<String>();//号码
	private ArrayList<String> mTimes = new ArrayList<String>();//出现次数
	private ArrayList<String> mFrequent = new ArrayList<String>();//出现频率
	private ArrayList<String> mPerYilou = new ArrayList<String>();//平均遗漏
	private ArrayList<String> mMaxYilou = new ArrayList<String>();//最大遗漏
	private ArrayList<String> mLastYiloou = new ArrayList<String>();//上次遗漏
	private ArrayList<String> mCurYilou = new ArrayList<String>();//本次遗漏
	private ArrayList<String> mProperty = new ArrayList<String>();//欲出几率
	private ArrayList<String> mInvestValue = new ArrayList<String>();//投资价值
	private ArrayList<String> mAnaplerosis = new ArrayList<String>();//回补几率

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.custom_yilou);
		mActivity = this;
		_fakeData();
		_findViews();
		_drawTable();
	}
	private void _fakeData() {
	Random random = new Random();
	for(int i=0; i<30; i++) {
		mNumber .add((i+1)+"");
		mTimes.add(random.nextInt(10)+"");
		mFrequent.add(random.nextInt(10)+"");
		mPerYilou.add(random.nextInt(10)+"");
		mMaxYilou.add(random.nextInt(100)+"");
		mLastYiloou.add(random.nextInt(10)+"");
		mCurYilou.add(random.nextInt(10)+"");
		mProperty.add(random.nextInt(10)+"");
		mInvestValue.add(random.nextInt(10)+"");
		mAnaplerosis.add(random.nextInt(10)+"");
		
		
	}
   }
	private void _findViews() {
		mBackBtn = (ImageView) findViewById(R.id.touzhu_leftmenu);
		mBackBtn.setOnClickListener(this);
		TextView mTitle = (TextView) findViewById(R.id.current_lottery);
		mTitle.setText( "-基本号码遗漏");
		mLvYilou = (LinearLayout)findViewById(R.id.custom_yilou_panel);
		
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	private void _drawTable() {
		LinearLayout lv = new LinearLayout(mActivity);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		lv.setLayoutParams(lp);
		lv.setOrientation(LinearLayout.HORIZONTAL);
		for(int i=0; i<titleFunc.length; i++) {
			TextView tv = new TextView(mActivity);
			tv.setText(titleFunc[i]);
			tv.setPadding(10, 5, 10, 5);
			tv.setGravity(Gravity.CENTER);
			tv.setWidth(160);
			tv.setTextColor(mActivity.getResources().getColor(R.color.tc_default));
			tv.setBackgroundResource(R.drawable.fangxing_bg);
			lv.addView(tv, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
		}
		mLvYilou.addView(lv);
		String[]strs = new String[titleFunc.length];
		for(int i=0; i<mCurYilou.size(); i++) {
			strs[0] = mNumber.get(i);
			strs[1] = mTimes.get(i);
			strs[2] = mFrequent.get(i);
			strs[3] = mPerYilou.get(i);
			strs[4] = mMaxYilou.get(i);
			strs[5] = mLastYiloou.get(i);
			strs[6] = mCurYilou.get(i);
			strs[7] = mProperty.get(i);
			strs[8] = mInvestValue.get(i);
			strs[9] = mAnaplerosis.get(i);
			LinearLayout matchlv = new LinearLayout(mActivity);
			LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			matchlv.setLayoutParams(lp1);
			matchlv.setOrientation(LinearLayout.HORIZONTAL);
			for(int index=0; index<titleFunc.length; index++) {
				TextView tv = new TextView(mActivity);
				tv.setText(strs[index]);
				tv.setPadding(10, 5, 10, 5);
				tv.setGravity(Gravity.CENTER);
				tv.setWidth(160);
				tv.setTextColor(mActivity.getResources().getColor(R.color.tc_default));
				tv.setBackgroundResource(R.drawable.fangxing_bg);
				matchlv.addView(tv, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
			}
			mLvYilou.addView(matchlv);
		}
		
		
	}
}
