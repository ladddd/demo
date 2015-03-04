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

public class PlsHeZhiFragment extends Fragment {
	public static final String TAG = "PlsHeZhiFragment";
	private String[] zhixuan_hezhi = { "00", "01", "02", "03", "04", "05",
			"06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16",
			"17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27" };
	private String[] zusan_hezhi = { "01", "02", "03", "04", "05", "06", "07",
			"08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18",
			"19", "20", "21", "22", "23", "24", "25", "26" };
	private String[] zuliu_hezhi = { "03", "04", "05", "06", "07", "08", "09",
			"10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
			"21", "22", "23", "24" };
	private ArrayList<Boolean> boolXuan = new ArrayList<Boolean>();
	private ArrayList<Boolean> boolSan = new ArrayList<Boolean>();
	private ArrayList<Boolean> boolLiu = new ArrayList<Boolean>();
	private MyGridView front_ball_group;
	private Pls mActivity;
	private XicaiBallGridViewAdapter mRedAdapter;
	private ShakeListener mShakeListener;
	private ArrayList<String> mXuanBall = new ArrayList<String>();
	private ArrayList<String> mSanBall = new ArrayList<String>();
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
		boolXuan.clear();
		boolLiu.clear();
		boolSan.clear();
	}

	private void _initBool() {
		boolXuan.clear();
		boolLiu.clear();
		boolSan.clear();
		for (int i = 0; i < 28; i++) {
			boolXuan.add(false);
			boolLiu.add(false);
			boolSan.add(false);
		}
	}

//	private void _changeBoolValues(int index, boolean value) {
//		if (mActivity.plsType == Constants.PlsType.HEZHIZHIXUAN) {
//			boolXuan.set(index, value);
//		} else if (mActivity.plsType == Constants.PlsType.HEZHIZUSAN) {
//			boolSan.set(index, value);
//		} else {
//			boolLiu.set(index, value);
//		}
//	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		mActivity = (Pls) getActivity();
		if (mActivity.plsType == Constants.PlsType.HEZHIZHIXUAN) {
			mRedAdapter = new XicaiBallGridViewAdapter(getActivity(), zhixuan_hezhi,
					boolXuan, true);
		} else if (mActivity.plsType == Constants.PlsType.HEZHIZUSAN) {
			mRedAdapter = new XicaiBallGridViewAdapter(getActivity(), zusan_hezhi,
					boolSan, true);
		} else {
			mRedAdapter = new XicaiBallGridViewAdapter(getActivity(), zuliu_hezhi,
					boolLiu, true);
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

				if (mActivity.plsType == Constants.PlsType.HEZHIZHIXUAN) {

					index = arg2;
					ballStr = String.valueOf(index);
					if (boolXuan.get(index)) {
						ballview.setTextColor(getActivity().getResources()
								.getColor(android.R.color.black));
						ballview.setBackgroundResource(R.drawable.ball_gray);
						boolXuan.set(index, false);
						mXuanBall.remove(ballStr);
					} else {
						ballview.setTextColor(getActivity().getResources()
								.getColor(android.R.color.white));
						ballview.setBackgroundResource(R.drawable.ball_red);
						boolXuan.set(index, true);
						mXuanBall.add(ballStr);
					}
				} else if (mActivity.plsType == Constants.PlsType.HEZHIZUSAN) {
					index = arg2 + 1;
					ballStr = String.valueOf(index);
					if (boolSan.get(index)) {
						ballview.setTextColor(getActivity().getResources()
								.getColor(android.R.color.black));
						ballview.setBackgroundResource(R.drawable.ball_gray);
						boolSan.set(index, false);
						mSanBall.remove(ballStr);
					} else {
						ballview.setTextColor(getActivity().getResources()
								.getColor(android.R.color.white));
						ballview.setBackgroundResource(R.drawable.ball_red);
						boolSan.set(index, true);
						mSanBall.add(ballStr);
					}
				} else {
					index = arg2 + 3;
					ballStr = String.valueOf(index);
					if (boolLiu.get(index)) {
						ballview.setTextColor(getActivity().getResources()
								.getColor(android.R.color.black));
						ballview.setBackgroundResource(R.drawable.ball_gray);
						boolLiu.set(index, false);
						mLiuBall.remove(ballStr);
					} else {
						ballview.setTextColor(getActivity().getResources()
								.getColor(android.R.color.white));
						ballview.setBackgroundResource(R.drawable.ball_red);
						boolLiu.set(index, true);
						mLiuBall.add(ballStr);
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
		if (mActivity.plsType == Constants.PlsType.HEZHIZHIXUAN) {
			shakeResult = PlsRandom.getPlsHZZX();
			mXuanBall.clear();
			boolXuan.clear();
			for (int i = 0; i < 28; i++) {
				boolXuan.add(false);
			}
			boolXuan.set(Integer.valueOf(shakeResult), true);
			mXuanBall.add(shakeResult);
		} else if (mActivity.plsType == Constants.PlsType.HEZHIZUSAN) {
			shakeResult = PlsRandom.getPlsHZZS();
			mSanBall.clear();
			boolSan.clear();
			for (int i = 0; i < 28; i++) {
				boolSan.add(false);
			}
			boolSan.set(Integer.valueOf(shakeResult) - 1, true);
			mSanBall.add(shakeResult);
		} else {
			shakeResult = PlsRandom.getPlsHZZL();
			mLiuBall.clear();
			boolLiu.clear();
			for (int i = 0; i < 28; i++) {
				boolLiu.add(false);
			}
			boolLiu.set(Integer.valueOf(shakeResult) - 3, true);
			mLiuBall.add(shakeResult);
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
			if (mActivity.plsType == Constants.PlsType.HEZHIZHIXUAN) {
				mRedAdapter = new XicaiBallGridViewAdapter(getActivity(),
						zhixuan_hezhi, boolXuan, true);
			} else if (mActivity.plsType == Constants.PlsType.HEZHIZUSAN) {
				mRedAdapter = new XicaiBallGridViewAdapter(getActivity(),
						zusan_hezhi, boolSan, true);
			} else {
				mRedAdapter = new XicaiBallGridViewAdapter(getActivity(),
						zuliu_hezhi, boolLiu, true);
			}
			front_ball_group.setAdapter(mRedAdapter);
			mRedAdapter.notifyDataSetChanged();
			mZhushu = getPlsResultList().size();
			((Pls) mActivity).setTouzhuResult(mZhushu);
		}
	}

	public ArrayList<String> getPlsResultList() {
		ArrayList<String> result = new ArrayList<String>();
		if (mActivity.plsType == Constants.PlsType.HEZHIZHIXUAN) {
			for (int index = 0; index < mXuanBall.size(); index++) {
				int xuanValue = Integer.valueOf(mXuanBall.get(index));
				for (int first = 0; first < 10; first++) {
					for (int second = 0; second < 10; second++) {
						for (int third = 0; third < 10; third++) {
							if (first + second + third == xuanValue) {
								result.add(String.valueOf(first)
										+ String.valueOf(second)
										+ String.valueOf(third));
							}
						}
					}
				}
			}
		} else if (mActivity.plsType == Constants.PlsType.HEZHIZUSAN) {
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
		} else {
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
		}
		return result;
	}

	public void clearChoose() {
		_initData();
		if (mActivity.plsType == Constants.PlsType.HEZHIZHIXUAN) {
			mXuanBall.clear();
		} else if (mActivity.plsType == Constants.PlsType.HEZHIZUSAN) {
			mSanBall.clear();
		} else {
			mLiuBall.clear();
		}
		mRedAdapter.notifyDataSetChanged();
		mZhushu = 0;
		((Pls) mActivity).setTouzhuResult(mZhushu);
	}
}