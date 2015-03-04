package com.jincaizi.kuaiwin.buycenter.sharexicai;

import java.util.ArrayList;

import android.app.Service;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
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

public class PlwNormalFragment extends Fragment {
	public static final String TAG = "PlwNormalFragment";
	private String[] content = { "0", "1", "2", "3", "4", "5", "6", "7", "8",
			"9" };
	private ArrayList<Boolean> boolWan = new ArrayList<Boolean>();
	private ArrayList<Boolean> boolQian = new ArrayList<Boolean>();
	private ArrayList<Boolean> boolBai = new ArrayList<Boolean>();
	private ArrayList<Boolean> boolShi = new ArrayList<Boolean>();
	private ArrayList<Boolean> boolGe = new ArrayList<Boolean>();
	private MyGridView wan_ball_group, qian_ball_group, bai_ball_group,
			shi_ball_group, ge_ball_group;
	private FragmentActivity mActivity;
	private XicaiBallGridViewAdapter mWanAdapter;
	private XicaiBallGridViewAdapter mQianAdapter;
	private XicaiBallGridViewAdapter mBaiAdapter;
	private XicaiBallGridViewAdapter mShiAdapter;
	private XicaiBallGridViewAdapter mGeAdapter;
	private ShakeListener mShakeListener;
	private ArrayList<String> mWanBall = new ArrayList<String>();
	private ArrayList<String> mQianBall = new ArrayList<String>();
	private ArrayList<String> mBaiBall = new ArrayList<String>();
	private ArrayList<String> mShiBall = new ArrayList<String>();
	private ArrayList<String> mGeBall = new ArrayList<String>();
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
		boolWan.clear();
		boolQian.clear();
		boolBai.clear();
		boolShi.clear();
		boolGe.clear();
	}

	private void _initBool() {
		for (int i = 0; i < 10; i++) {
			boolWan.add(false);
			boolQian.add(false);
			boolBai.add(false);
			boolShi.add(false);
			boolGe.add(false);
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		mActivity = getActivity();
		mWanAdapter = new XicaiBallGridViewAdapter(getActivity(), content, boolWan,
				true);
		mQianAdapter = new XicaiBallGridViewAdapter(getActivity(), content,
				boolQian, true);
		mBaiAdapter = new XicaiBallGridViewAdapter(getActivity(), content, boolBai,
				true);
		mShiAdapter = new XicaiBallGridViewAdapter(getActivity(), content, boolShi,
				true);
		mGeAdapter = new XicaiBallGridViewAdapter(getActivity(), content, boolGe,
				true);
		wan_ball_group.setAdapter(mWanAdapter);
		qian_ball_group.setAdapter(mQianAdapter);
		bai_ball_group.setAdapter(mBaiAdapter);
		shi_ball_group.setAdapter(mShiAdapter);
		ge_ball_group.setAdapter(mGeAdapter);
		mZhushu = mWanBall.size() * mQianBall.size() * mBaiBall.size()
				* mShiBall.size() * mGeBall.size();
		((Plw) mActivity).setTouzhuResult(mZhushu);
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
		view.findViewById(R.id.lv_pls_wan).setVisibility(View.VISIBLE);
		view.findViewById(R.id.lv_pls_qian).setVisibility(View.VISIBLE);
		wan_ball_group = (MyGridView) view
				.findViewById(R.id.pls_wan_ball_group);
		qian_ball_group = (MyGridView) view
				.findViewById(R.id.pls_qian_ball_group);
		bai_ball_group = (MyGridView) view
				.findViewById(R.id.pls_bai_ball_group);
		shi_ball_group = (MyGridView) view
				.findViewById(R.id.pls_shi_ball_group);
		ge_ball_group = (MyGridView) view.findViewById(R.id.pls_ge_ball_group);
	}

	private void _setListeners() {
		wan_ball_group.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				TextView ballview = (TextView) arg1
						.findViewById(R.id.tv_ssq_ball);
				String ballStr;
				ballStr = String.valueOf(arg2);
				if (boolWan.get(arg2)) {
					ballview.setTextColor(getActivity().getResources()
							.getColor(android.R.color.black));
					ballview.setBackgroundResource(R.drawable.ball_gray);
					boolWan.set(arg2, false);
					mWanBall.remove(ballStr);
				} else {
					ballview.setTextColor(getActivity().getResources()
							.getColor(android.R.color.white));
					ballview.setBackgroundResource(R.drawable.ball_red);
					boolWan.set(arg2, true);
					mWanBall.add(ballStr);
				}
				mZhushu = mWanBall.size() * mQianBall.size() * mBaiBall.size()
						* mShiBall.size() * mGeBall.size();
				((Plw) mActivity).setTouzhuResult(mZhushu);
			}
		});
		qian_ball_group.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				TextView ballview = (TextView) arg1
						.findViewById(R.id.tv_ssq_ball);
				String ballStr;
				ballStr = String.valueOf(arg2);
				if (boolQian.get(arg2)) {
					ballview.setTextColor(getActivity().getResources()
							.getColor(android.R.color.black));
					ballview.setBackgroundResource(R.drawable.ball_gray);
					boolQian.set(arg2, false);
					mQianBall.remove(ballStr);
				} else {
					ballview.setTextColor(getActivity().getResources()
							.getColor(android.R.color.white));
					ballview.setBackgroundResource(R.drawable.ball_red);
					boolQian.set(arg2, true);
					mQianBall.add(ballStr);
				}
				mZhushu = mWanBall.size() * mQianBall.size() * mBaiBall.size()
						* mShiBall.size() * mGeBall.size();
				((Plw) mActivity).setTouzhuResult(mZhushu);
			}
		});
		bai_ball_group.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				TextView ballview = (TextView) arg1
						.findViewById(R.id.tv_ssq_ball);
				String ballStr;
				ballStr = String.valueOf(arg2);
				if (boolBai.get(arg2)) {
					ballview.setTextColor(getActivity().getResources()
							.getColor(android.R.color.black));
					ballview.setBackgroundResource(R.drawable.ball_gray);
					boolBai.set(arg2, false);
					mBaiBall.remove(ballStr);
				} else {
					ballview.setTextColor(getActivity().getResources()
							.getColor(android.R.color.white));
					ballview.setBackgroundResource(R.drawable.ball_red);
					boolBai.set(arg2, true);
					mBaiBall.add(ballStr);
				}
				mZhushu = mWanBall.size() * mQianBall.size() * mBaiBall.size()
						* mShiBall.size() * mGeBall.size();
				((Plw) mActivity).setTouzhuResult(mZhushu);
			}
		});
		shi_ball_group.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				TextView ballview = (TextView) arg1
						.findViewById(R.id.tv_ssq_ball);
				String ballStr;
				ballStr = String.valueOf(arg2);
				if (boolShi.get(arg2)) {
					ballview.setTextColor(getActivity().getResources()
							.getColor(android.R.color.black));
					ballview.setBackgroundResource(R.drawable.ball_gray);
					boolShi.set(arg2, false);
					mShiBall.remove(ballStr);
				} else {
					ballview.setBackgroundResource(R.drawable.ball_red);
					ballview.setTextColor(getActivity().getResources()
							.getColor(android.R.color.white));
					boolShi.set(arg2, true);
					mShiBall.add(ballStr);
				}
				mZhushu = mWanBall.size() * mQianBall.size() * mBaiBall.size()
						* mShiBall.size() * mGeBall.size();
				((Plw) mActivity).setTouzhuResult(mZhushu);
			}

		});
		ge_ball_group.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				TextView ballview = (TextView) arg1
						.findViewById(R.id.tv_ssq_ball);
				String ballStr;
				ballStr = String.valueOf(arg2);
				if (boolGe.get(arg2)) {
					ballview.setTextColor(getActivity().getResources()
							.getColor(android.R.color.black));
					ballview.setBackgroundResource(R.drawable.ball_gray);
					boolGe.set(arg2, false);
					mGeBall.remove(ballStr);
				} else {
					ballview.setBackgroundResource(R.drawable.ball_red);
					ballview.setTextColor(getActivity().getResources()
							.getColor(android.R.color.white));
					boolGe.set(arg2, true);
					mGeBall.add(ballStr);
				}
				mZhushu = mWanBall.size() * mQianBall.size() * mBaiBall.size()
						* mShiBall.size() * mGeBall.size();
				((Plw) mActivity).setTouzhuResult(mZhushu);
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
		_initData();
		mWanBall.clear();
		mQianBall.clear();
		mBaiBall.clear();
		mShiBall.clear();
		mGeBall.clear();
		ArrayList<String> shakeResult = PlsRandom.getPLSBallRePeat(5);
		boolWan.set(Integer.valueOf(shakeResult.get(0)), true);
		mWanBall.add(shakeResult.get(0));
		boolQian.set(Integer.valueOf(shakeResult.get(1)), true);
		mQianBall.add(shakeResult.get(1));
		boolBai.set(Integer.valueOf(shakeResult.get(2)), true);
		mBaiBall.add(shakeResult.get(2));
		boolShi.set(Integer.valueOf(shakeResult.get(3)), true);
		mShiBall.add(shakeResult.get(3));
		boolGe.set(Integer.valueOf(shakeResult.get(4)), true);
		mGeBall.add(shakeResult.get(4));

		mWanAdapter.notifyDataSetChanged();
		mQianAdapter.notifyDataSetChanged();
		mBaiAdapter.notifyDataSetChanged();
		mShiAdapter.notifyDataSetChanged();
		mGeAdapter.notifyDataSetChanged();
		mZhushu = mWanBall.size() * mQianBall.size() * mBaiBall.size()
				* mShiBall.size() * mGeBall.size();
		((Plw) mActivity).setTouzhuResult(mZhushu);
	}

	public void updateBallData(String ball) {
		_initData();
		mWanBall.clear();
		mQianBall.clear();
		mBaiBall.clear();
		mShiBall.clear();
		mGeBall.clear();
		String wanStr = ball.substring(0, 1);
		mWanBall.add(wanStr);
		boolWan.set(Integer.valueOf(wanStr), true);
		
		String qianStr = ball.substring(1, 2);
		mQianBall.add(qianStr);
		boolQian.set(Integer.valueOf(qianStr), true);
		
		String baiStr = ball.substring(2, 3);
		mBaiBall.add(baiStr);
		boolBai.set(Integer.valueOf(baiStr), true);
		
		String shiStr = ball.substring(3, 4);
		mShiBall.add(shiStr);
		boolShi.set(Integer.valueOf(shiStr), true);

		String geStr = ball.substring(4,5);
		mGeBall.add(geStr);
		boolGe.set(Integer.valueOf(geStr), true);


	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		// TODO Auto-generated method stub
		super.onHiddenChanged(hidden);
		if (hidden) {
			mShakeListener.stop();
		} else {
			mShakeListener.start();
		}
	}

	public ArrayList<String> getPlsResultList() {
		ArrayList<String> result = new ArrayList<String>();
		for (int indexWan = 0; indexWan < mWanBall.size(); indexWan++) {
			for (int indexQian = 0; indexQian < mQianBall.size(); indexQian++) {
				for (int indexBai = 0; indexBai < mBaiBall.size(); indexBai++) {
					for (int indexShi = 0; indexShi < mShiBall.size(); indexShi++) {
						for (int indexGe = 0; indexGe < mGeBall.size(); indexGe++) {
							result.add(mWanBall.get(indexWan)
									+ mQianBall.get(indexQian)
									+ mBaiBall.get(indexBai)
									+ mShiBall.get(indexShi)
									+ mGeBall.get(indexGe));
						}
					}
				}
			}
		}
		return result;
	}

	public void clearChoose() {
		_initData();
		mWanBall.clear();
		mQianBall.clear();
		mBaiBall.clear();
		mShiBall.clear();
		mGeBall.clear();
		mWanAdapter.notifyDataSetChanged();
		mQianAdapter.notifyDataSetChanged();
		mBaiAdapter.notifyDataSetChanged();
		mShiAdapter.notifyDataSetChanged();
		mGeAdapter.notifyDataSetChanged();
		mZhushu = 0;
		((Plw) mActivity).setTouzhuResult(mZhushu);
	}

}