package com.jincaizi.kuaiwin.buycenter.sharexicai;

import java.util.ArrayList;
import java.util.Collections;

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
import com.jincaizi.kuaiwin.tool.PlsRandom;
import com.jincaizi.kuaiwin.utils.Constants;
import com.jincaizi.kuaiwin.widget.MyGridView;
import com.jincaizi.kuaiwin.widget.ShakeListener;
import com.jincaizi.kuaiwin.widget.ShakeListener.OnShakeListener;

public class FcsdZusanFragment extends Fragment {
	public static final String TAG = "PlsZusanFragment";
	private String[] r_content = { "0", "1", "2", "3", "4", "5", "6", "7", "8",
			"9" };
	private ArrayList<Boolean> boolSan = new ArrayList<Boolean>();
	private MyGridView front_ball_group;
	private Fcsd mActivity;
	private XicaiBallGridViewAdapter mRedAdapter;
	private ShakeListener mShakeListener;
	private ArrayList<String> mSanBall = new ArrayList<String>();
	public int mZhushu = 0;
	private TextView baiHint;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// _initData();
		_initSanBool();
	}

	private void _initData() {
		_clearSanData();
		_initSanBool();

	}

	private void _clearSanData() {
		boolSan.clear();
	}

	private void _initSanBool() {
		boolSan.clear();
		for (int i = 0; i < 10; i++) {
			boolSan.add(false);
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		mActivity = (Fcsd) getActivity();

		if (mActivity.plsType == Constants.PlsType.ZUSAN) {
			mRedAdapter = new XicaiBallGridViewAdapter(getActivity(), r_content,
					boolSan, true);
			baiHint.setText("至少选择2个球");
			int size = mSanBall.size();
			mZhushu = size * (size - 1);
		} 

		front_ball_group.setAdapter(mRedAdapter);
		((Fcsd) mActivity).setTouzhuResult(mZhushu);
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
				ballStr = String.valueOf(arg2);
				if (mActivity.plsType == Constants.PlsType.ZUSAN) {
					if (boolSan.get(arg2)) {
						ballview.setTextColor(getActivity().getResources()
								.getColor(android.R.color.black));
						ballview.setBackgroundResource(R.drawable.ball_gray);
						boolSan.set(arg2, false);
						mSanBall.remove(ballStr);
					} else {
						ballview.setTextColor(getActivity().getResources()
								.getColor(android.R.color.white));
						ballview.setBackgroundResource(R.drawable.ball_red);
						boolSan.set(arg2, true);
						mSanBall.add(ballStr);
					}
					int size = mSanBall.size();
					mZhushu = size * (size - 1);
				} 

				((Fcsd) mActivity).setTouzhuResult(mZhushu);
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
		if (mActivity.plsType == Constants.PlsType.ZUSAN) {
			shakeResult = PlsRandom.getPLSBallNoRePeat(2);
			mSanBall.clear();
			_initSanBool();
			for (int i = 0; i < shakeResult.size(); i++) {
				boolSan.set(Integer.valueOf(shakeResult.get(i)), true);
				mSanBall.add(shakeResult.get(i));
			}
			mZhushu = 2;
		} 
		((Fcsd) mActivity).setTouzhuResult(mZhushu);
		mRedAdapter.notifyDataSetChanged();
	}


	@Override
	public void onHiddenChanged(boolean hidden) {
		// TODO Auto-generated method stub
		super.onHiddenChanged(hidden);
		if (hidden) {
			mShakeListener.stop();
		} else {
			mShakeListener.start();
			if (mActivity.plsType == Constants.PlsType.ZUSAN) {
				mRedAdapter = new XicaiBallGridViewAdapter(getActivity(), r_content,
						boolSan, true);
			} 
			front_ball_group.setAdapter(mRedAdapter);
			mRedAdapter.notifyDataSetChanged();
		}
	}
	public ArrayList<String>mZhushuList = new ArrayList<String>();
	public ArrayList<String> getPlsResultList() {
		mZhushuList.clear();
		ArrayList<String> result = new ArrayList<String>();
		if (mActivity.plsType == Constants.PlsType.ZUSAN) {
			Collections.sort(mSanBall);
			for (int i = 0; i < mSanBall.size(); i++) {
				String tempStr = mSanBall.get(i);
				for (int j = 0; j < mSanBall.size(); j++) {
					if (tempStr.equals(mSanBall.get(j))) {
						continue;
					}
					if (Integer.valueOf(tempStr) > Integer.valueOf(mSanBall
							.get(j))) {
						result.add(mSanBall.get(j) + tempStr + tempStr);
					} else {
						result.add(tempStr + tempStr + mSanBall.get(j));
					}
					mZhushuList.add("1");
				}
			}
		} 
		return result;
	}

	public void clearChoose() {
		_initData();
		if (mActivity.plsType == Constants.PlsType.ZUSAN) {
			mSanBall.clear();
		} 
		mRedAdapter.notifyDataSetChanged();
		mZhushu = 0;
		((Fcsd) mActivity).setTouzhuResult(mZhushu);
	}
}