package com.jincaizi.kuaiwin.buycenter.sharexicai;

import java.util.ArrayList;

import android.app.Service;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.jincaizi.R;
import com.jincaizi.kuaiwin.tool.PlsRandom;
import com.jincaizi.kuaiwin.widget.MyGridView;
import com.jincaizi.kuaiwin.widget.ShakeListener;
import com.jincaizi.kuaiwin.widget.ShakeListener.OnShakeListener;

public class FcsdHZzuliuFragment extends Fragment {
	public static final String TAG = "FcsdHZzuliuFragment";
	private String[] zuliu_hezhi = { "03", "04", "05", "06", "07", "08", "09",
			"10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
			"21", "22", "23", "24" };
	private ArrayList<Boolean> boolLiu = new ArrayList<Boolean>();
	private MyGridView front_ball_group;
	private Fcsd mActivity;
	private XicaiBallGridViewAdapter mRedAdapter;
	private ShakeListener mShakeListener;
	private ArrayList<String> mLiuBall = new ArrayList<String>();
	public int mZhushu = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// _initData();
		_initBool();
	}

	private void _initData() {
		_clearData();
		_initBool();

	}

	private void _clearData() {
		boolLiu.clear();
	}

	private void _initBool() {
		for (int i = 0; i < 28; i++) {
			boolLiu.add(false);
		}
	}


	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		mActivity = (Fcsd) getActivity();
			mRedAdapter = new XicaiBallGridViewAdapter(getActivity(), zuliu_hezhi,
					boolLiu, true);
			if(mActivity.startType == 2 && !TextUtils.isEmpty(mActivity.mRepickZuliuStr)) {
	        	updateBallData(mActivity.mRepickZuliuStr);
	        	mActivity.mRepickZuliuStr = "";
	        }
		front_ball_group.setAdapter(mRedAdapter);
		mZhushu = getPlsResultList().size();
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
		view.findViewById(R.id.lv_pls_shi).setVisibility(View.GONE);
		view.findViewById(R.id.lv_pls_ge).setVisibility(View.GONE);
		TextView title = (TextView) view.findViewById(R.id.pls_bai_title);
		title.setText("选号");
	}

	private void _setListeners() {
		front_ball_group.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				TextView ballview = (TextView) arg1
						.findViewById(R.id.tv_ssq_ball);
				int index;
				String ballStr;

					index = arg2 + 3;
					ballStr = String.valueOf(index);
					if (boolLiu.get(arg2)) {
						ballview.setTextColor(getActivity().getResources()
								.getColor(android.R.color.black));
						ballview.setBackgroundResource(R.drawable.ball_gray);
						boolLiu.set(arg2, false);
						mLiuBall.remove(ballStr);
					} else {
						ballview.setTextColor(getActivity().getResources()
								.getColor(android.R.color.white));
						ballview.setBackgroundResource(R.drawable.ball_red);
						boolLiu.set(arg2, true);
						mLiuBall.add(ballStr);
					}

				mZhushu = getPlsResultList().size();
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
		String shakeResult;

			shakeResult = PlsRandom.getPlsHZZL();
			mLiuBall.clear();
			boolLiu.clear();
			for (int i = 0; i < 28; i++) {
				boolLiu.add(false);
			}
			boolLiu.set(Integer.valueOf(shakeResult) - 3, true);
			mLiuBall.add(shakeResult);

		mRedAdapter.notifyDataSetChanged();
		mZhushu = getPlsResultList().size();
		((Fcsd) mActivity).setTouzhuResult(mZhushu);
	}
	public void updateBallData(String ball) {

			mLiuBall.clear();
			boolLiu.clear();
			for (int i = 0; i < 28; i++) {
				boolLiu.add(false);
			}
			boolLiu.set(Integer.valueOf(ball) - 3, true);
			mLiuBall.add(ball);
			mRedAdapter.notifyDataSetChanged();
			mZhushu = getPlsResultList().size();
			((Fcsd) mActivity).setTouzhuResult(mZhushu);
	}
	@Override
	public void onHiddenChanged(boolean hidden) {
		// TODO Auto-generated method stub
		super.onHiddenChanged(hidden);
		if (hidden) {
			mShakeListener.stop();
		} else {
			mShakeListener.start();
				mRedAdapter = new XicaiBallGridViewAdapter(getActivity(),
						zuliu_hezhi, boolLiu, true);
			front_ball_group.setAdapter(mRedAdapter);
			mRedAdapter.notifyDataSetChanged();
			mZhushu = getPlsResultList().size();
			((Fcsd) mActivity).setTouzhuResult(mZhushu);
		}
	}
//拆分和值
	public ArrayList<String> getPlsResultList() {
		ArrayList<String> result = new ArrayList<String>();
			for (int index = 0; index < mLiuBall.size(); index++) {
				int xuanValue = Integer.valueOf(mLiuBall.get(index));
				for (int first = 0; first < 10; first++) {
					for (int second = first + 1; second < 10; second++) {
						for (int third = second + 1; third < 10; third++) {
							if (first + second + third == xuanValue) {
								result.add(String.valueOf(first)
										+ String.valueOf(second)
										+ String.valueOf(third));
							}
						}
					}
				}
			}
		return result;
	}
	//不拆分，直接传递和值
	public ArrayList<String>mZhushuList = new ArrayList<String>();
	public ArrayList<String> getPlsResultHezhiList() {
		mZhushuList.clear();
		ArrayList<String> result = new ArrayList<String>();
			for (int index = 0; index < mLiuBall.size(); index++) {
				int xuanValue = Integer.valueOf(mLiuBall.get(index));
				result.add(mLiuBall.get(index));
				int count = 0;
				for (int first = 0; first < 10; first++) {
					for (int second = first + 1; second < 10; second++) {
						for (int third = second + 1; third < 10; third++) {
							if (first + second + third == xuanValue) {
								count++;
							}
						}
					}
				}
				mZhushuList.add(count+"");
			}
		return result;
	}
	public void clearChoose() {
		_initData();
	     mLiuBall.clear();
		mRedAdapter.notifyDataSetChanged();
		mZhushu = 0;
		((Fcsd) mActivity).setTouzhuResult(mZhushu);
	}
}