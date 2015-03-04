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
import com.jincaizi.kuaiwin.utils.Utils;
import com.jincaizi.kuaiwin.widget.MyGridView;
import com.jincaizi.kuaiwin.widget.ShakeListener;
import com.jincaizi.kuaiwin.widget.ShakeListener.OnShakeListener;
import com.jincaizi.adapters.BallGridViewAdapter;

public class FrontTwozuxuanFragment extends Fragment {
	public static final String TAG = "FrontTwozuxuanFragment";
	private String[] r_content = { "1", "2", "3", "4", "5", "6", "7", "8",
			"9" , "10","11"};
	private ArrayList<Boolean> boolLiu = new ArrayList<Boolean>();
	private MyGridView front_ball_group;
	private Activity mActivity;
	private BallGridViewAdapter mRedAdapter;
	private ShakeListener mShakeListener;
	private ArrayList<String> mLiuBall = new ArrayList<String>();
	public int mZhushu = 0;
	private TextView baiHint;
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
		boolLiu.clear();
	}


	private void _initLiuBool() {
		for (int i = 0; i < 11; i++) {
			boolLiu.add(false);
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		mActivity = getActivity();

			mRedAdapter = new BallGridViewAdapter(getActivity(), r_content,
					boolLiu, true);
			baiHint.setText("至少选择2个球");
			mZhushu = Utils.getZuHeNum(mLiuBall.size(), 2);

		front_ball_group.setAdapter(mRedAdapter);
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
		TextView baiTitle = (TextView) view.findViewById(R.id.pls_bai_title);
		baiTitle.setText("选号");
		baiHint = (TextView) view.findViewById(R.id.pls_bai_hint);
		view.findViewById(R.id.lv_pls_shi).setVisibility(View.GONE);
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
					if (boolLiu.get(arg2)) {
						ballview.setTextColor(getActivity().getResources()
								.getColor(R.color.blue));
						ballview.setBackgroundResource(R.drawable.ball_gray);
						boolLiu.set(arg2, false);
						mLiuBall.remove(ballStr);
					} else {
						ballview.setTextColor(getActivity().getResources()
								.getColor(android.R.color.white));
						ballview.setBackgroundResource(R.drawable.ball_blue);
						boolLiu.set(arg2, true);
						mLiuBall.add(ballStr);
					}
					mZhushu = Utils.getZuHeNum(mLiuBall.size(), 2);

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
			mLiuBall.clear();
			for (int i = 0; i < shakeResult.size(); i++) {
				boolLiu.set(Integer.valueOf(shakeResult.get(i)) - 1, true);
				mLiuBall.add(shakeResult.get(i));
			}
			mZhushu = 1;
			if ( mLocalClassName.equals("buycenter.Syiyunjin")) {
				((Syiyunjin) mActivity).setTouzhuResult(mZhushu);
			} else {
				((Syxw) mActivity).setTouzhuResult(mZhushu);
			}
		mRedAdapter.notifyDataSetChanged();
	}

	public void updateBallData(String ball) {
		_clearLiuData();
		_initLiuBool();
		String[]result = ball.split(" ");
		mLiuBall.clear();
		for (int i = 0; i < result.length; i++) {
			mLiuBall.add(result[i]);
			boolLiu.set(Integer.valueOf(result[i]) - 1, true);
		}
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		// TODO Auto-generated method stub
		super.onHiddenChanged(hidden);
		if (hidden) {
			mShakeListener.stop();
		} else {
			mShakeListener.start();
//			mRedAdapter = new BallGridViewAdapter(getActivity(), r_content,
//						boolLiu, true);
//			front_ball_group.setAdapter(mRedAdapter);
//			mRedAdapter.notifyDataSetChanged();
		}
	}

	public ArrayList<String> getPlsResultList() {
		ArrayList<String> result = new ArrayList<String>();
			Collections.sort(mLiuBall);
				for (int j = 0; j < mLiuBall.size(); j++) {
					for (int k = j + 1; k < mLiuBall.size(); k++) {
						result.add(mLiuBall.get(j) +" " + mLiuBall.get(k));
					}
				}
		return result;
	}

	public void clearChoose() {
		_initData();
	    mLiuBall.clear();
		mRedAdapter.notifyDataSetChanged();
		mZhushu = 0;
		if ( mLocalClassName.equals("buycenter.Syiyunjin")) {
			((Syiyunjin) mActivity).setTouzhuResult(mZhushu);
		} else {
			((Syxw) mActivity).setTouzhuResult(mZhushu);
		}
	}
}