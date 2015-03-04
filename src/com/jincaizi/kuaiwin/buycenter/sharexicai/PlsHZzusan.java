package com.jincaizi.kuaiwin.buycenter.sharexicai;

import java.util.ArrayList;

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

public class PlsHZzusan extends Fragment {
	public static final String TAG = "PlsHZzusan";
	private String[] zusan_hezhi = { "01", "02", "03", "04", "05", "06", "07",
			"08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18",
			"19", "20", "21", "22", "23", "24", "25", "26" };
	private ArrayList<Boolean> boolSan = new ArrayList<Boolean>();
	private MyGridView front_ball_group;
	private Pls mActivity;
	private XicaiBallGridViewAdapter mRedAdapter;
	private ShakeListener mShakeListener;
	private ArrayList<String> mSanBall = new ArrayList<String>();
	public int mZhushu = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		 _initData();
	}

	private void _initData() {
		_clearData();
		_initBool();

	}

	private void _clearData() {
		boolSan.clear();
	}

	private void _initBool() {
		boolSan.clear();
		for (int i = 0; i < 28; i++) {
			boolSan.add(false);
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		mActivity = (Pls) getActivity();
		 if (mActivity.plsType == Constants.PlsType.HEZHIZUSAN) {
			mRedAdapter = new XicaiBallGridViewAdapter(getActivity(), zusan_hezhi,
					boolSan, true);
		} 

		front_ball_group.setAdapter(mRedAdapter);
		mZhushu = getPlsResultList().size();
		((Pls) mActivity).setTouzhuResult(mZhushu);
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

		if (mActivity.plsType == Constants.PlsType.HEZHIZUSAN) {
					index = arg2 + 1;
					ballStr = String.valueOf(index);
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
				} 
				mZhushu = getPlsResultList().size();
				((Pls) mActivity).setTouzhuResult(mZhushu);
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
		if (mActivity.plsType == Constants.PlsType.HEZHIZUSAN) {
			shakeResult = PlsRandom.getPlsHZZS();
			mSanBall.clear();
			_initData();
			boolSan.set(Integer.valueOf(shakeResult) - 1, true);
			mSanBall.add(shakeResult);
		} 

		mRedAdapter.notifyDataSetChanged();
		mZhushu = getPlsResultList().size();
		((Pls) mActivity).setTouzhuResult(mZhushu);
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		// TODO Auto-generated method stub
		super.onHiddenChanged(hidden);
		if (hidden) {
			mShakeListener.stop();
		} else {
			mShakeListener.start();
			if (mActivity.plsType == Constants.PlsType.HEZHIZUSAN) {
				mRedAdapter = new XicaiBallGridViewAdapter(getActivity(),
						zusan_hezhi, boolSan, true);
			}
			front_ball_group.setAdapter(mRedAdapter);
			mRedAdapter.notifyDataSetChanged();
			mZhushu = getPlsResultList().size();
			((Pls) mActivity).setTouzhuResult(mZhushu);
		}
	}
	public ArrayList<String>mZhushuList = new ArrayList<String>();
	
	public ArrayList<String> getPlsResultList() {
		ArrayList<String> result = new ArrayList<String>();
		if (mActivity.plsType == Constants.PlsType.HEZHIZUSAN) {
			for (int index = 0; index < mSanBall.size(); index++) {
				int xuanValue = Integer.valueOf(mSanBall.get(index));
				for (int first = 0; first < 10; first++) {
					for (int second = 0; second < 10; second++) {
						if (first == second) {
							continue;
						}
						if (first + second + first == xuanValue) {
							result.add(String.valueOf(first)
									+ String.valueOf(first)
									+ String.valueOf(second));
						}
					}
				}
			}
		}
		return result;
	}
	//不拆分
	public ArrayList<String> getPlsResultHezhiList() {
		mZhushuList.clear();
		ArrayList<String> result = new ArrayList<String>();
		if (mActivity.plsType == Constants.PlsType.HEZHIZUSAN) {
			for (int index = 0; index < mSanBall.size(); index++) {
				result.add(mSanBall.get(index));
				int xuanValue = Integer.valueOf(mSanBall.get(index));
				int count = 0;
				for (int first = 0; first < 10; first++) {
					for (int second = 0; second < 10; second++) {
						if (first == second) {
							continue;
						}
						if (first + second + first == xuanValue) {
							count++;
						}
					}
				}
				mZhushuList.add(count+"");
			}
		}
		return result;
	}
	public void clearChoose() {
		_initData();
		if (mActivity.plsType == Constants.PlsType.HEZHIZUSAN) {
			mSanBall.clear();
		}
		mRedAdapter.notifyDataSetChanged();
		mZhushu = 0;
		((Pls) mActivity).setTouzhuResult(mZhushu);
	}
}