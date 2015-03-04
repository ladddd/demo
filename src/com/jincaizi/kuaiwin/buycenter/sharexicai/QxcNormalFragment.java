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

public class QxcNormalFragment extends Fragment {
	public static final String TAG = "QxcNormalFragment";
	private String[] content = { "0", "1", "2", "3", "4", "5", "6", "7", "8",
			"9" };
	private ArrayList<Boolean> boolOne = new ArrayList<Boolean>();
	private ArrayList<Boolean> boolTwo = new ArrayList<Boolean>();
	private ArrayList<Boolean> boolThree = new ArrayList<Boolean>();
	private ArrayList<Boolean> boolFour = new ArrayList<Boolean>();
	private ArrayList<Boolean> boolFive = new ArrayList<Boolean>();
	private ArrayList<Boolean> boolSix = new ArrayList<Boolean>();
	private ArrayList<Boolean> boolSeven = new ArrayList<Boolean>();
	private MyGridView one_ball_group, two_ball_group, three_ball_group,
			four_ball_group, five_ball_group, six_ball_group, seven_ball_group;
	private FragmentActivity mActivity;
	private XicaiBallGridViewAdapter mOneAdapter;
	private XicaiBallGridViewAdapter mTwoAdapter;
	private XicaiBallGridViewAdapter mThreeAdapter;
	private XicaiBallGridViewAdapter mFourAdapter;
	private XicaiBallGridViewAdapter mFiveAdapter;
	private XicaiBallGridViewAdapter mSixAdapter;
	private XicaiBallGridViewAdapter mSevenAdapter;
	private ShakeListener mShakeListener;
	private ArrayList<String> mOneBall = new ArrayList<String>();
	private ArrayList<String> mTwoBall = new ArrayList<String>();
	private ArrayList<String> mThreeBall = new ArrayList<String>();
	private ArrayList<String> mFourBall = new ArrayList<String>();
	private ArrayList<String> mFiveBall = new ArrayList<String>();
	private ArrayList<String> mSixBall = new ArrayList<String>();
	private ArrayList<String> mSevenBall = new ArrayList<String>();
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
		boolOne.clear();
		boolTwo.clear();
		boolThree.clear();
		boolFour.clear();
		boolFive.clear();
		boolSix.clear();
		boolSeven.clear();
	}

	private void _initBool() {
		for (int i = 0; i < 10; i++) {
			boolOne.add(false);
			boolTwo.add(false);
			boolThree.add(false);
			boolFour.add(false);
			boolFive.add(false);
			boolSix.add(false);
			boolSeven.add(false);
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		mActivity = getActivity();
		mOneAdapter = new XicaiBallGridViewAdapter(getActivity(), content, boolOne,
				true);
		mTwoAdapter = new XicaiBallGridViewAdapter(getActivity(), content, boolTwo,
				true);
		mThreeAdapter = new XicaiBallGridViewAdapter(getActivity(), content,
				boolThree, true);
		mFourAdapter = new XicaiBallGridViewAdapter(getActivity(), content,
				boolFour, true);
		mFiveAdapter = new XicaiBallGridViewAdapter(getActivity(), content,
				boolFive, true);
		mSixAdapter = new XicaiBallGridViewAdapter(getActivity(), content, boolFour,
				true);
		mSevenAdapter = new XicaiBallGridViewAdapter(getActivity(), content,
				boolFive, true);
		one_ball_group.setAdapter(mOneAdapter);
		two_ball_group.setAdapter(mTwoAdapter);
		three_ball_group.setAdapter(mThreeAdapter);
		four_ball_group.setAdapter(mFourAdapter);
		five_ball_group.setAdapter(mFiveAdapter);
		six_ball_group.setAdapter(mFourAdapter);
		seven_ball_group.setAdapter(mFiveAdapter);
		mZhushu = mOneBall.size() * mTwoBall.size() * mThreeBall.size()
				* mFourBall.size() * mFiveBall.size() * mSixBall.size()
				* mSevenBall.size();
		((Qxc) mActivity).setTouzhuResult(mZhushu);
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
		one_ball_group = (MyGridView) view
				.findViewById(R.id.pls_one_ball_group);
		two_ball_group = (MyGridView) view
				.findViewById(R.id.pls_two_ball_group);
		three_ball_group = (MyGridView) view
				.findViewById(R.id.pls_three_ball_group);
		four_ball_group = (MyGridView) view
				.findViewById(R.id.pls_four_ball_group);
		five_ball_group = (MyGridView) view
				.findViewById(R.id.pls_five_ball_group);
		six_ball_group = (MyGridView) view
				.findViewById(R.id.pls_six_ball_group);
		seven_ball_group = (MyGridView) view
				.findViewById(R.id.pls_seven_ball_group);
	}

	private void _setListeners() {
		one_ball_group.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				TextView ballview = (TextView) arg1
						.findViewById(R.id.tv_ssq_ball);
				String ballStr;
				ballStr = String.valueOf(arg2);
				if (boolOne.get(arg2)) {
					ballview.setTextColor(getActivity().getResources()
							.getColor(android.R.color.black));
					ballview.setBackgroundResource(R.drawable.ball_gray);
					boolOne.set(arg2, false);
					mOneBall.remove(ballStr);
				} else {
					ballview.setTextColor(getActivity().getResources()
							.getColor(android.R.color.white));
					ballview.setBackgroundResource(R.drawable.ball_red);
					boolOne.set(arg2, true);
					mOneBall.add(ballStr);
				}
				mZhushu = mOneBall.size() * mTwoBall.size() * mThreeBall.size()
						* mFourBall.size() * mFiveBall.size() * mSixBall.size()
						* mSevenBall.size();
				((Qxc) mActivity).setTouzhuResult(mZhushu);
			}
		});
		two_ball_group.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				TextView ballview = (TextView) arg1
						.findViewById(R.id.tv_ssq_ball);
				String ballStr;
				ballStr = String.valueOf(arg2);
				if (boolTwo.get(arg2)) {
					ballview.setTextColor(getActivity().getResources()
							.getColor(android.R.color.black));
					ballview.setBackgroundResource(R.drawable.ball_gray);
					boolTwo.set(arg2, false);
					mTwoBall.remove(ballStr);
				} else {
					ballview.setTextColor(getActivity().getResources()
							.getColor(android.R.color.white));
					ballview.setBackgroundResource(R.drawable.ball_red);
					boolTwo.set(arg2, true);
					mTwoBall.add(ballStr);
				}
				mZhushu = mOneBall.size() * mTwoBall.size() * mThreeBall.size()
						* mFourBall.size() * mFiveBall.size() * mSixBall.size()
						* mSevenBall.size();
				((Qxc) mActivity).setTouzhuResult(mZhushu);
			}
		});
		three_ball_group.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				TextView ballview = (TextView) arg1
						.findViewById(R.id.tv_ssq_ball);
				String ballStr;
				ballStr = String.valueOf(arg2);
				if (boolThree.get(arg2)) {
					ballview.setTextColor(getActivity().getResources()
							.getColor(android.R.color.black));
					ballview.setBackgroundResource(R.drawable.ball_gray);
					boolThree.set(arg2, false);
					mThreeBall.remove(ballStr);
				} else {
					ballview.setTextColor(getActivity().getResources()
							.getColor(android.R.color.white));
					ballview.setBackgroundResource(R.drawable.ball_red);
					boolThree.set(arg2, true);
					mThreeBall.add(ballStr);
				}
				mZhushu = mOneBall.size() * mTwoBall.size() * mThreeBall.size()
						* mFourBall.size() * mFiveBall.size() * mSixBall.size()
						* mSevenBall.size();
				((Qxc) mActivity).setTouzhuResult(mZhushu);
			}
		});
		four_ball_group.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				TextView ballview = (TextView) arg1
						.findViewById(R.id.tv_ssq_ball);
				String ballStr;
				ballStr = String.valueOf(arg2);
				if (boolFour.get(arg2)) {
					ballview.setTextColor(getActivity().getResources()
							.getColor(android.R.color.black));
					ballview.setBackgroundResource(R.drawable.ball_gray);
					boolFour.set(arg2, false);
					mFourBall.remove(ballStr);
				} else {
					ballview.setBackgroundResource(R.drawable.ball_red);
					ballview.setTextColor(getActivity().getResources()
							.getColor(android.R.color.white));
					boolFour.set(arg2, true);
					mFourBall.add(ballStr);
				}
				mZhushu = mOneBall.size() * mTwoBall.size() * mThreeBall.size()
						* mFourBall.size() * mFiveBall.size() * mSixBall.size()
						* mSevenBall.size();
				((Qxc) mActivity).setTouzhuResult(mZhushu);
			}

		});
		five_ball_group.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				TextView ballview = (TextView) arg1
						.findViewById(R.id.tv_ssq_ball);
				String ballStr;
				ballStr = String.valueOf(arg2);
				if (boolFive.get(arg2)) {
					ballview.setTextColor(getActivity().getResources()
							.getColor(android.R.color.black));
					ballview.setBackgroundResource(R.drawable.ball_gray);
					boolFive.set(arg2, false);
					mFiveBall.remove(ballStr);
				} else {
					ballview.setBackgroundResource(R.drawable.ball_red);
					ballview.setTextColor(getActivity().getResources()
							.getColor(android.R.color.white));
					boolFive.set(arg2, true);
					mFiveBall.add(ballStr);
				}
				mZhushu = mOneBall.size() * mTwoBall.size() * mThreeBall.size()
						* mFourBall.size() * mFiveBall.size() * mSixBall.size()
						* mSevenBall.size();
				((Qxc) mActivity).setTouzhuResult(mZhushu);
			}

		});
		six_ball_group.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				TextView ballview = (TextView) arg1
						.findViewById(R.id.tv_ssq_ball);
				String ballStr;
				ballStr = String.valueOf(arg2);
				if (boolSix.get(arg2)) {
					ballview.setTextColor(getActivity().getResources()
							.getColor(android.R.color.black));
					ballview.setBackgroundResource(R.drawable.ball_gray);
					boolSix.set(arg2, false);
					mSixBall.remove(ballStr);
				} else {
					ballview.setBackgroundResource(R.drawable.ball_red);
					ballview.setTextColor(getActivity().getResources()
							.getColor(android.R.color.white));
					boolSix.set(arg2, true);
					mSixBall.add(ballStr);
				}
				mZhushu = mOneBall.size() * mTwoBall.size() * mThreeBall.size()
						* mFourBall.size() * mFiveBall.size() * mSixBall.size()
						* mSevenBall.size();
				((Qxc) mActivity).setTouzhuResult(mZhushu);
			}

		});
		seven_ball_group.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				TextView ballview = (TextView) arg1
						.findViewById(R.id.tv_ssq_ball);
				String ballStr;
				ballStr = String.valueOf(arg2);
				if (boolSeven.get(arg2)) {
					ballview.setTextColor(getActivity().getResources()
							.getColor(android.R.color.black));
					ballview.setBackgroundResource(R.drawable.ball_gray);
					boolSeven.set(arg2, false);
					mSevenBall.remove(ballStr);
				} else {
					ballview.setBackgroundResource(R.drawable.ball_red);
					ballview.setTextColor(getActivity().getResources()
							.getColor(android.R.color.white));
					boolSeven.set(arg2, true);
					mSevenBall.add(ballStr);
				}
				mZhushu = mOneBall.size() * mTwoBall.size() * mThreeBall.size()
						* mFourBall.size() * mFiveBall.size() * mSixBall.size()
						* mSevenBall.size();
				((Qxc) mActivity).setTouzhuResult(mZhushu);
			}

		});
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.qxc, container, false);
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
		mOneBall.clear();
		mTwoBall.clear();
		mThreeBall.clear();
		mFourBall.clear();
		mFiveBall.clear();
		mSixBall.clear();
		mSevenBall.clear();
		ArrayList<String> shakeResult = PlsRandom.getPLSBallRePeat(7);
		boolOne.set(Integer.valueOf(shakeResult.get(0)), true);
		mOneBall.add(shakeResult.get(0));
		boolTwo.set(Integer.valueOf(shakeResult.get(1)), true);
		mTwoBall.add(shakeResult.get(1));
		boolThree.set(Integer.valueOf(shakeResult.get(2)), true);
		mThreeBall.add(shakeResult.get(2));
		boolFour.set(Integer.valueOf(shakeResult.get(3)), true);
		mFourBall.add(shakeResult.get(3));
		boolFive.set(Integer.valueOf(shakeResult.get(4)), true);
		mFiveBall.add(shakeResult.get(4));
		boolSix.set(Integer.valueOf(shakeResult.get(5)), true);
		mSixBall.add(shakeResult.get(5));
		boolSeven.set(Integer.valueOf(shakeResult.get(6)), true);
		mSevenBall.add(shakeResult.get(6));

		mOneAdapter.notifyDataSetChanged();
		mTwoAdapter.notifyDataSetChanged();
		mThreeAdapter.notifyDataSetChanged();
		mFourAdapter.notifyDataSetChanged();
		mFiveAdapter.notifyDataSetChanged();
		mSixAdapter.notifyDataSetChanged();
		mSevenAdapter.notifyDataSetChanged();
		mZhushu = mOneBall.size() * mTwoBall.size() * mThreeBall.size()
				* mFourBall.size() * mFiveBall.size() * mSixBall.size()
				* mSevenBall.size();
		((Qxc) mActivity).setTouzhuResult(mZhushu);
	}

	public void updateBallData(String ball) {
		_initData();
		mOneBall.clear();
		mTwoBall.clear();
		mThreeBall.clear();
		mFourBall.clear();
		mFiveBall.clear();
		mSixBall.clear();
		mSevenBall.clear();
		String oneStr = ball.substring(0, 1);
		mOneBall.add(oneStr);
		boolOne.set(Integer.valueOf(oneStr), true);

		String twoStr = ball.substring(1, 2);
		mTwoBall.add(twoStr);
		boolTwo.set(Integer.valueOf(twoStr), true);

		String threeStr = ball.substring(2, 3);
		mThreeBall.add(threeStr);
		boolThree.set(Integer.valueOf(threeStr), true);

		String fourStr = ball.substring(3, 4);
		mFourBall.add(fourStr);
		boolFour.set(Integer.valueOf(fourStr), true);

		String fiveStr = ball.substring(4, 5);
		mFiveBall.add(fiveStr);
		boolFive.set(Integer.valueOf(fiveStr), true);

		String sixStr = ball.substring(5, 6);
		mSixBall.add(sixStr);
		boolSix.set(Integer.valueOf(sixStr), true);

		String sevenStr = ball.substring(6, 7);
		mSevenBall.add(sevenStr);
		boolSeven.set(Integer.valueOf(sevenStr), true);

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
		for (int indexOne = 0; indexOne < mOneBall.size(); indexOne++) {
			for (int indexTwo = 0; indexTwo < mTwoBall.size(); indexTwo++) {
				for (int indexThree = 0; indexThree < mThreeBall.size(); indexThree++) {
					for (int indexFour = 0; indexFour < mFourBall.size(); indexFour++) {
						for (int indexFive = 0; indexFive < mFiveBall.size(); indexFive++) {
							for (int indexSix = 0; indexSix < mSixBall.size(); indexSix++) {
								for (int indexSeven = 0; indexSeven < mSevenBall
										.size(); indexSeven++) {
									result.add(mOneBall.get(indexOne)
											+ mTwoBall.get(indexTwo)
											+ mThreeBall.get(indexThree)
											+ mFourBall.get(indexFour)
											+ mFiveBall.get(indexFive) + mSixBall.get(indexSix) + mSevenBall.get(indexSeven));
								}
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
		mOneBall.clear();
		mTwoBall.clear();
		mThreeBall.clear();
		mFourBall.clear();
		mFiveBall.clear();
		mSixBall.clear();
		mSevenBall.clear();
		
		mOneAdapter.notifyDataSetChanged();
		mTwoAdapter.notifyDataSetChanged();
		mThreeAdapter.notifyDataSetChanged();
		mFourAdapter.notifyDataSetChanged();
		mFiveAdapter.notifyDataSetChanged();
		mSixAdapter.notifyDataSetChanged();
		mSevenAdapter.notifyDataSetChanged();
		
		mZhushu = 0;
		((Qxc) mActivity).setTouzhuResult(mZhushu);
	}

}