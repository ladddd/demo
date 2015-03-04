package com.jincaizi.kuaiwin.buycenter;

import java.util.ArrayList;
import java.util.Collections;

import android.app.Activity;
import android.app.Service;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.jincaizi.R;
import com.jincaizi.kuaiwin.tool.ShiyiyunRandom;
import com.jincaizi.kuaiwin.widget.MyGridView;
import com.jincaizi.kuaiwin.widget.ShakeListener;
import com.jincaizi.kuaiwin.widget.ShakeListener.OnShakeListener;
import com.jincaizi.adapters.BallGridViewAdapter;

public class FrontTwozhixuanFragment extends Fragment {
	public static final String TAG = "FrontTwozhixuanFragment";
	private String[] r_content = { "1", "2", "3", "4", "5", "6", "7", "8", "9",
			"10", "11" };
	private ArrayList<Boolean> boolFront = new ArrayList<Boolean>();
	private ArrayList<Boolean> boolBehind = new ArrayList<Boolean>();
	private MyGridView front_ball_group;
	private Activity mActivity;
	private ShakeListener mShakeListener;
	private ArrayList<String> mFrontBall = new ArrayList<String>();
	private ArrayList<String> mbehindBall = new ArrayList<String>();
	public int mZhushu = 0;
	private MyGridView behind_ball_group;
	private BallGridViewAdapter mFrontAdapter;
	private BallGridViewAdapter mBehindAdapter;
	private String mLocalClassName;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// _initData();
		_initLiuBool();
	}

	private void _initData() {
		_clearLiuData();
		_initLiuBool();

	}

	private void _clearLiuData() {
		boolFront.clear();
		boolBehind.clear();
	}

	private void _initLiuBool() {
		for (int i = 0; i < 11; i++) {
			boolFront.add(false);
			boolBehind.add(false);
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		mActivity = getActivity();

		mFrontAdapter = new BallGridViewAdapter(getActivity(), r_content,
				boolFront, true);
		mBehindAdapter = new BallGridViewAdapter(getActivity(), r_content,
				boolBehind, true);

		mZhushu = mFrontBall.size()*  mbehindBall.size();

		front_ball_group.setAdapter(mFrontAdapter);
		behind_ball_group.setAdapter(mBehindAdapter);
		mLocalClassName = mActivity.getLocalClassName();
		if ( mLocalClassName.equals("buycenter.Syiyunjin")) {
			((Syiyunjin) mActivity).setTouzhuResult(mZhushu);
		} else {
			((Syxw) mActivity).setTouzhuResult(mZhushu);
		}
		mShakeListener = new ShakeListener(getActivity());
		mShakeListener.setOnShakeListener(new shakeLitener());
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mShakeListener.stop();
		_initData();
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		_findViews(view);
		_setListeners();
		super.onViewCreated(view, savedInstanceState);
	}

	private void _findViews(View view) {
		front_ball_group = (MyGridView) view
				.findViewById(R.id.pls_bai_ball_group);
		behind_ball_group = (MyGridView) view
				.findViewById(R.id.pls_shi_ball_group);
		view.findViewById(R.id.lv_pls_ge).setVisibility(View.GONE);
	}

	private void _setListeners() {
		front_ball_group.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				TextView ballview = (TextView) arg1
						.findViewById(R.id.tv_ssq_ball);
				String ballStr;
			    if(arg2<9) {
			    	ballStr= "0" + String.valueOf(arg2 + 1);
			       } else {
			    	   ballStr= String.valueOf(arg2 + 1);
			       }
				if (boolFront.get(arg2)) {
					ballview.setTextColor(getActivity().getResources()
							.getColor(R.color.blue));
					ballview.setBackgroundResource(R.drawable.ball_gray);
					boolFront.set(arg2, false);
					mFrontBall.remove(ballStr);
				} else {
					if(mbehindBall.contains(ballStr)) {
						mbehindBall.remove(ballStr);
						boolBehind.set(arg2, false);
						mBehindAdapter.notifyDataSetChanged();
					}
					ballview.setTextColor(getActivity().getResources()
							.getColor(android.R.color.white));
					ballview.setBackgroundResource(R.drawable.ball_blue);
					boolFront.set(arg2, true);
					mFrontBall.add(ballStr);
				}
				mZhushu = mFrontBall.size()*  mbehindBall.size();

				if ( mLocalClassName.equals("buycenter.Syiyunjin")) {
					((Syiyunjin) mActivity).setTouzhuResult(mZhushu);
				} else {
					((Syxw) mActivity).setTouzhuResult(mZhushu);
				}
			}
		});

		behind_ball_group.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				TextView ballview = (TextView) arg1
						.findViewById(R.id.tv_ssq_ball);
				String ballStr;
			    if(arg2<9) {
			    	ballStr= "0" + String.valueOf(arg2 + 1);
			       } else {
			    	   ballStr= String.valueOf(arg2 + 1);
			       }
				if (boolBehind.get(arg2)) {
					ballview.setTextColor(getActivity().getResources()
							.getColor(R.color.blue));
					ballview.setBackgroundResource(R.drawable.ball_gray);
					boolBehind.set(arg2, false);
					mbehindBall.remove(ballStr);
				} else {
					if(mFrontBall.contains(ballStr)) {
						mFrontBall.remove(ballStr);
						boolFront.set(arg2, false);
						mFrontAdapter.notifyDataSetChanged();
					}
					ballview.setTextColor(getActivity().getResources()
							.getColor(android.R.color.white));
					ballview.setBackgroundResource(R.drawable.ball_blue);
					boolBehind.set(arg2, true);
					mbehindBall.add(ballStr);
				}
				mZhushu = mFrontBall.size()*  mbehindBall.size();

				if ( mLocalClassName.equals("buycenter.Syiyunjin")) {
					((Syiyunjin) mActivity).setTouzhuResult(mZhushu);
				} else {
					((Syxw) mActivity).setTouzhuResult(mZhushu);
				}
			}
		});

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.pls_fragment_layout, container, false);
	}

	class shakeLitener implements OnShakeListener {

		@Override
		public void onShake() {
			// TODO Auto-generated method stub
			updateBallData();
			// mShakeListener.stop();
		}

	}

	public void updateBallData() {
		Vibrator vibrator = (Vibrator) getActivity().getApplication()
				.getSystemService(Service.VIBRATOR_SERVICE);
		vibrator.vibrate(new long[] { 0, 200 }, -1);
		// _initData();
		ArrayList<String> shakeResult = new ArrayList<String>();
		shakeResult = ShiyiyunRandom.getSyyBallNoRePeat(2);
		_clearLiuData();
		_initLiuBool();
		mFrontBall.clear();
		mbehindBall.clear();
		boolFront.set(Integer.valueOf(shakeResult.get(0)) - 1, true);
		mFrontBall.add(shakeResult.get(0));
		boolBehind.set(Integer.valueOf(shakeResult.get(1)) - 1, true);
		mbehindBall.add(shakeResult.get(1));
		mZhushu = 1;

		if ( mLocalClassName.equals("buycenter.Syiyunjin")) {
			((Syiyunjin) mActivity).setTouzhuResult(mZhushu);
		} else {
			((Syxw) mActivity).setTouzhuResult(mZhushu);
		}
		mFrontAdapter.notifyDataSetChanged();
		mBehindAdapter.notifyDataSetChanged();
	}

	public void updateBallData(String ball) {
		_clearLiuData();
		_initLiuBool();
		mFrontBall.clear();
		mbehindBall.clear();
		String[]result = ball.split(" ");
		mFrontBall.add(result[0]);
		boolFront.set(Integer.valueOf(result[0]) - 1, true);
		mbehindBall.add(result[1]);
		boolBehind.set(Integer.valueOf(result[1]) - 1, true);
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		// TODO Auto-generated method stub
		super.onHiddenChanged(hidden);
		if (hidden) {
			mShakeListener.stop();
		} else {
			mShakeListener.start();
			// mRedAdapter = new BallGridViewAdapter(getActivity(), r_content,
			// boolLiu, true);
			// front_ball_group.setAdapter(mRedAdapter);
			// mRedAdapter.notifyDataSetChanged();
		}
	}

	public ArrayList<String> getPlsResultList() {
		ArrayList<String> result = new ArrayList<String>();
		Collections.sort(mFrontBall);
		Collections.sort(mbehindBall);
		for (int i = 0; i < mFrontBall.size(); i++)
			for (int j = 0; j < mbehindBall.size(); j++) {
				result.add(mFrontBall.get(i) +" " + mbehindBall.get(j));
			}
		return result;
	}

	public void clearChoose() {
		_initData();
		mFrontBall.clear();
		mbehindBall.clear();
		mFrontAdapter.notifyDataSetChanged();
		mBehindAdapter.notifyDataSetChanged();
		mZhushu = 0;

		if ( mLocalClassName.equals("buycenter.Syiyunjin")) {
			((Syiyunjin) mActivity).setTouzhuResult(mZhushu);
		} else {
			((Syxw) mActivity).setTouzhuResult(mZhushu);
		}
	}
}