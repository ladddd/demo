package com.jincaizi.kuaiwin.hemaicenter;

import java.util.ArrayList;
import java.util.Arrays;

import android.widget.*;
import com.jincaizi.adapters.PopViewAdapter;
import com.jincaizi.kuaiwin.FragmentCallbacks;
import com.jincaizi.R;
import com.jincaizi.kuaiwin.utils.Constants.LotteryType;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView.OnItemClickListener;

public class HemaiCenter extends Fragment implements OnClickListener,
		OnGestureListener,FragmentCallbacks {
	public static final String TAG = "HemaiCenter";
	private String[] mItemName = { "全部彩种","十一运夺金","江西11选5" ,"广东11选5","安徽11选5", "重庆11选5",
            "辽宁11选5", "上海11选5","黑龙江11选5", "江苏快3", "吉林快3","安徽快3", "湖北快3",
		"重庆时时彩",   "江西时时彩"};
	private TextView mTitleView;
	private ArrayList<Boolean> mChecked = new ArrayList<Boolean>();
	private boolean isPopWindowShow = false;
	private PopupWindow mPopWindow;
	private GridView mGridView;
	private PopViewAdapter mMyAdapter;
	private Fragment mCurrentFragment;
	public LotteryType mLotteryType = LotteryType.ALLTYPE; // 当前合买显示的彩票类型
	private RadioButton mJinduSort;
	private RadioButton mZongjineSort;
	public boolean isJinduSortUp = false; // 是否咱进度升序排列
	public boolean isZonjineSortUp = true; // 是否按总金额升序排列
	public String sortFlag = "desc"; // 是否咱进度升序排列
	public String sortId = "Progress"; // 是否按总金额升序排列
	private GestureDetector mMyGestureDetector;
	private FrameLayout mHemaiContent;

    private View firstIndicator;
    private View secondIndicator;

    private ImageView search;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mMyGestureDetector = new GestureDetector(getActivity(), this);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.group, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		_findViews(view);
		_setListener();
		_showFragments(HemaiJinduFragment.TAG);
	}

	private void _findViews(View view) {
		// RoundProgressBar progressView =
		// (RoundProgressBar)view.findViewById(R.id.roundProgressBar2);
		// progressView.setProgress(20);
		mChecked.add(true);
		for (int i = 1; i < mItemName.length; i++) {
			mChecked.add(false);
		}
		mTitleView = (TextView) view.findViewById(R.id.current_lottery);
		mTitleView.setText("全部彩种");
		mJinduSort = (RadioButton) view.findViewById(R.id.sort_jindu);
		mZongjineSort = (RadioButton) view.findViewById(R.id.sort_jine);
		mHemaiContent = (FrameLayout) view.findViewById(R.id.hemai_child_fragment_content);
		mJinduSort.performClick();

        firstIndicator = view.findViewById(R.id.first_indicator);
        secondIndicator = view.findViewById(R.id.second_indicator);

        search = (ImageView) view.findViewById(R.id.search);
	}

	private void _setListener() {
		mTitleView.setOnClickListener(this);
		mJinduSort.setOnClickListener(this);
		mZongjineSort.setOnClickListener(this);
		mHemaiContent.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				Log.d(TAG, "onTouch");
				return mMyGestureDetector.onTouchEvent(event);
			}
		});

        //TODO 合买人搜索
//        search.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.current_lottery:
			if (!isPopWindowShow) {
				_setPopWindow();
				isPopWindowShow = true;
			}
			mPopWindow.showAsDropDown(v, -(v.getWidth() / 4), v.getTop());
			break;
		case R.id.sort_jindu:

			_updateJinduSortUI(true);

			break;
		case R.id.sort_jine:
			_updateZongjineSortUI();

			break;
		default:
			break;
		}
	}

	private void _updateJinduSortUI(boolean resetHere) {
		//是否由总金额Fragment切换至进度fragment
		if(mZongjineSort.isChecked()) {
			isZonjineSortUp = !isZonjineSortUp;
		}
		mJinduSort.setChecked(true);
		mZongjineSort.setChecked(false);
        firstIndicator.setBackgroundColor(getResources().getColor(R.color.widget_orange));
        secondIndicator.setBackgroundColor(getResources().getColor(R.color.box_gray));

		if (isJinduSortUp) {
			mJinduSort.setCompoundDrawablesWithIntrinsicBounds(0, 0,
					R.drawable.sort_down, 0);
			isJinduSortUp = false;
			sortFlag = "desc";
		} else {
			mJinduSort.setCompoundDrawablesWithIntrinsicBounds(0, 0,
					R.drawable.sort_up, 0);
			isJinduSortUp = true;
			sortFlag = "asc";
		}
		mZongjineSort.setCompoundDrawablesWithIntrinsicBounds(0, 0,
				R.drawable.sort_null, 0);
		
		if(resetHere) {
			
			if (sortId.equals("TotalAmount")) {
				_showFragments(HemaiJinduFragment.TAG);
				sortId = "Progress";
			} else {
				sortId = "Progress";
				_resetJinduFragment();
			}
		} else {
			sortId = "Progress";
		}
		
	}

	private void _updateZongjineSortUI() {
		//是否由进度fragment切换至总金额Fragment
		if(mJinduSort.isChecked()) {
			isJinduSortUp = !isJinduSortUp;
		}
		mJinduSort.setChecked(false);
		mZongjineSort.setChecked(true);
        firstIndicator.setBackgroundColor(getResources().getColor(R.color.box_gray));
        secondIndicator.setBackgroundColor(getResources().getColor(R.color.widget_orange));

		if (isZonjineSortUp) {
			mZongjineSort.setCompoundDrawablesWithIntrinsicBounds(0, 0,
					R.drawable.sort_down, 0);
			isZonjineSortUp = false;
			sortFlag = "desc";
		} else {
			mZongjineSort.setCompoundDrawablesWithIntrinsicBounds(0, 0,
					R.drawable.sort_up, 0);
			isZonjineSortUp = true;
			sortFlag = "asc";
		}
		mJinduSort.setCompoundDrawablesWithIntrinsicBounds(0, 0,
				R.drawable.sort_null, 0);
		
		if (sortId.equals("Progress")) {
			_showFragments(HemaiZongjineFragment.TAG);
			sortId = "TotalAmount";
		} else {
			sortId = "TotalAmount";
			_resetZongjineFragment();
		}
	}

	private void _setPopWindow() {
		View view = LayoutInflater.from(getActivity()).inflate(
				R.layout.popview, null);
		mPopWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		mGridView = (GridView) view.findViewById(R.id.pop_gridview);
//		mGridView.setBackgroundColor(this.getResources().getColor(
//				android.R.color.transparent));
		mGridView.setNumColumns(3);
		ArrayList<String> list = new ArrayList<String>();
		list.addAll(Arrays.asList(mItemName));
		mMyAdapter = new PopViewAdapter(getActivity(), list, mChecked);
		mGridView.setAdapter(mMyAdapter);
		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				mTitleView.setText(mItemName[arg2]);
				int lastIndex = mChecked.indexOf(true);
				if(lastIndex != -1 && lastIndex != arg2) {
				    mChecked.set(lastIndex, false);
				}
				mChecked.set(arg2, true);
				mLotteryType = LotteryType.getLotteryType(mItemName[arg2]);
				if (lastIndex != arg2) {
					isJinduSortUp= true;
					isZonjineSortUp = true;
					sortId = "Progress";
					_updateJinduSortUI(false);
					_resetFragment();
				}
				mMyAdapter.notifyDataSetChanged();
				mPopWindow.dismiss();
			}
		});
		mPopWindow.setFocusable(true);
		mPopWindow.setOutsideTouchable(true);
		mPopWindow.update();
		mPopWindow.setBackgroundDrawable(new BitmapDrawable(getResources(),
				Bitmap.createBitmap(1, 1, Config.ARGB_8888)));
	}

	private void _resetFragment() {
		FragmentManager mFragManager = getChildFragmentManager();
		FragmentTransaction mFragTransaction = mFragManager.beginTransaction();
		Fragment mFragment = (Fragment) mFragManager
				.findFragmentByTag(HemaiJinduFragment.TAG);
		if (mFragment != null) {
			Fragment fragment = new HemaiJinduFragment();
			mFragTransaction.remove(mFragment);
			mFragTransaction.add(R.id.hemai_child_fragment_content, fragment,
					HemaiJinduFragment.TAG);
			mCurrentFragment = fragment;
		}
		mFragment = (Fragment) mFragManager
				.findFragmentByTag(HemaiZongjineFragment.TAG);
		if (mFragment != null) {
			mFragTransaction.remove(mFragment);
		}
		mFragTransaction.commit();
	}

	private void _resetJinduFragment() {
		FragmentManager mFragManager = getChildFragmentManager();
		FragmentTransaction mFragTransaction = mFragManager.beginTransaction();
		Fragment mFragment = (Fragment) mFragManager
				.findFragmentByTag(HemaiJinduFragment.TAG);
		if (mFragment != null) {
			Fragment fragment = new HemaiJinduFragment();
			mFragTransaction.remove(mFragment);
			mFragTransaction.add(R.id.hemai_child_fragment_content, fragment,
					HemaiJinduFragment.TAG);
			mCurrentFragment = fragment;
		}
		mFragTransaction.commit();
	}

	private void _resetZongjineFragment() {
		FragmentManager mFragManager = getChildFragmentManager();
		FragmentTransaction mFragTransaction = mFragManager.beginTransaction();
		Fragment mFragment = (Fragment) mFragManager
				.findFragmentByTag(HemaiZongjineFragment.TAG);
		if (mFragment != null) {
			Fragment fragment = new HemaiZongjineFragment();
			mFragTransaction.remove(mFragment);
			mFragTransaction.add(R.id.hemai_child_fragment_content, fragment,
					HemaiZongjineFragment.TAG);
			mCurrentFragment = fragment;
		}
		mFragTransaction.commit();
	}

	private void _showFragments(String fragmentTag) {
		FragmentManager mFragManager = getChildFragmentManager();
		FragmentTransaction mFragTransaction = mFragManager.beginTransaction();
		Fragment mFragment = (Fragment) mFragManager
				.findFragmentByTag(fragmentTag);
		if (mFragment == null) {
			if (fragmentTag.equals(HemaiJinduFragment.TAG)) {
				mFragment = new HemaiJinduFragment();
			} else {
				mFragment = new HemaiZongjineFragment();
			}
			mFragTransaction.add(R.id.hemai_child_fragment_content, mFragment,
					fragmentTag);
		}
		if (mCurrentFragment != null) {

			mFragTransaction.hide(mCurrentFragment);
		}
		mFragTransaction.show(mFragment);
		mCurrentFragment = mFragment;
		mFragTransaction.commit();
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		Log.d(TAG,
				"min velocity >>>"
						+ ViewConfiguration.get(
								getActivity().getApplicationContext())
								.getScaledMinimumFlingVelocity()
						+ " current velocity>>" + velocityX);
		if (Math.abs(velocityX) > ViewConfiguration.get(
				getActivity().getApplicationContext())
				.getScaledMinimumFlingVelocity()) {
			if (velocityX > 0) {
				// fling to right
				Log.d(TAG, "fling to right");
				_showFragments(HemaiJinduFragment.TAG);
			} else {
				// fling to right
				Log.d(TAG, "fling to left");
				_showFragments(HemaiJinduFragment.TAG);
			}
		}
		return true;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShow() {
		// TODO Auto-generated method stub
		
	}
}
