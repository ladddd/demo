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

public class FrontThreezhixuanFragment extends Fragment {
	public static final String TAG = "FrontThreezhixuanFragment";
	private String[] r_content = { "1", "2", "3", "4", "5", "6", "7", "8", "9",
			"10", "11" };

    private ArrayList<Boolean> boolFront = new ArrayList<Boolean>();

    private ArrayList<Boolean> boolBehind = new ArrayList<Boolean>();
    private ArrayList<Boolean> boolLast = new ArrayList<Boolean>();
    private MyGridView front_ball_group;
    private Activity mActivity;
    private ShakeListener mShakeListener;
    private ArrayList<String> mFrontBall = new ArrayList<String>();
    private ArrayList<String> mbehindBall = new ArrayList<String>();
    private ArrayList<String> mLastBall = new ArrayList<String>();
    public int mZhushu = 0;
    private MyGridView behind_ball_group, last_ball_group;
    private BallGridViewAdapter mFrontAdapter;
    private BallGridViewAdapter mBehindAdapter;
    private BallGridViewAdapter mLastAdapter;
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
		boolLast.clear();
	}

	private void _initLiuBool() {
		for (int i = 0; i < 11; i++) {
			boolFront.add(false);
			boolBehind.add(false);
			boolLast.add(false);
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
		mLastAdapter = new BallGridViewAdapter(getActivity(), r_content,
				boolLast, true);

		mZhushu = mFrontBall.size() * mbehindBall.size()* mLastBall.size();

		front_ball_group.setAdapter(mFrontAdapter);
		behind_ball_group.setAdapter(mBehindAdapter);
		last_ball_group.setAdapter(mLastAdapter);
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
		last_ball_group = (MyGridView) view
				.findViewById(R.id.pls_ge_ball_group);
	}

	private void _setListeners() {
		front_ball_group.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				TextView ballview = (TextView) arg1
						.findViewById(R.id.tv_ssq_ball);
				String ballStr;
				if (arg2 < 9) {
					ballStr = "0" + String.valueOf(arg2 + 1);
				} else {
					ballStr = String.valueOf(arg2 + 1);
				}
				if (boolFront.get(arg2)) {
					ballview.setTextColor(getActivity().getResources()
							.getColor(R.color.blue));
					ballview.setBackgroundResource(R.drawable.ball_gray);
					boolFront.set(arg2, false);
					mFrontBall.remove(ballStr);
				} else {
//					if (mbehindBall.contains(ballStr)) {
//						mbehindBall.remove(ballStr);
//						boolBehind.set(arg2, false);
//						mBehindAdapter.notifyDataSetChanged();
//					} else if (mLastBall.contains(ballStr)) {
//						mLastBall.remove(ballStr);
//						boolLast.set(arg2, false);
//						mLastAdapter.notifyDataSetChanged();
//					}

					ballview.setTextColor(getActivity().getResources()
							.getColor(android.R.color.white));
					ballview.setBackgroundResource(R.drawable.ball_blue);
					boolFront.set(arg2, true);
					mFrontBall.add(ballStr);
				}
                updateCount();
			}
		});

		behind_ball_group.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				TextView ballview = (TextView) arg1
						.findViewById(R.id.tv_ssq_ball);
				String ballStr;
				if (arg2 < 9) {
					ballStr = "0" + String.valueOf(arg2 + 1);
				} else {
					ballStr = String.valueOf(arg2 + 1);
				}
				if (boolBehind.get(arg2)) {
					ballview.setTextColor(getActivity().getResources()
							.getColor(R.color.blue));
					ballview.setBackgroundResource(R.drawable.ball_gray);
					boolBehind.set(arg2, false);
					mbehindBall.remove(ballStr);
				} else {
//					if (mFrontBall.contains(ballStr)) {
//						mFrontBall.remove(ballStr);
//						boolFront.set(arg2, false);
//						mFrontAdapter.notifyDataSetChanged();
//					} else if (mLastBall.contains(ballStr)) {
//						mLastBall.remove(ballStr);
//						boolLast.set(arg2, false);
//						mLastAdapter.notifyDataSetChanged();
//					}
					ballview.setTextColor(getActivity().getResources()
							.getColor(android.R.color.white));
					ballview.setBackgroundResource(R.drawable.ball_blue);
					boolBehind.set(arg2, true);
					mbehindBall.add(ballStr);
				}
				mZhushu = mFrontBall.size() * mbehindBall.size()* mLastBall.size();

				if ( mLocalClassName.equals("buycenter.Syiyunjin")) {
					((Syiyunjin) mActivity).setTouzhuResult(mZhushu);
				} else {
					((Syxw) mActivity).setTouzhuResult(mZhushu);
				}
			}
		});
		last_ball_group.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				TextView ballview = (TextView) arg1
						.findViewById(R.id.tv_ssq_ball);
				String ballStr;
				if (arg2 < 9) {
					ballStr = "0" + String.valueOf(arg2 + 1);
				} else {
					ballStr = String.valueOf(arg2 + 1);
				}
				if (boolLast.get(arg2)) {
					ballview.setTextColor(getActivity().getResources()
							.getColor(R.color.blue));
					ballview.setBackgroundResource(R.drawable.ball_gray);
					boolLast.set(arg2, false);
					mLastBall.remove(ballStr);
				} else {
//					if (mFrontBall.contains(ballStr)) {
//						mFrontBall.remove(ballStr);
//						boolFront.set(arg2, false);
//						mFrontAdapter.notifyDataSetChanged();
//					} else if (mbehindBall.contains(ballStr)) {
//						mbehindBall.remove(ballStr);
//						boolBehind.set(arg2, false);
//						mBehindAdapter.notifyDataSetChanged();
//					}
					ballview.setTextColor(getActivity().getResources()
							.getColor(android.R.color.white));
					ballview.setBackgroundResource(R.drawable.ball_blue);
					boolLast.set(arg2, true);
					mLastBall.add(ballStr);
				}
				mZhushu = mFrontBall.size() * mbehindBall.size()* mLastBall.size();

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
		shakeResult = ShiyiyunRandom.getSyyBallNoRePeat(3);
		_clearLiuData();
		_initLiuBool();
		mFrontBall.clear();
		mbehindBall.clear();
		mLastBall.clear();
		boolFront.set(Integer.valueOf(shakeResult.get(0)) - 1, true);
		mFrontBall.add(shakeResult.get(0));
		boolBehind.set(Integer.valueOf(shakeResult.get(1)) - 1, true);
		mbehindBall.add(shakeResult.get(1));
		boolLast.set(Integer.valueOf(shakeResult.get(2)) - 1, true);
		mLastBall.add(shakeResult.get(2));
		mZhushu = 1;
		if ( mLocalClassName.equals("buycenter.Syiyunjin")) {
			((Syiyunjin) mActivity).setTouzhuResult(mZhushu);
		} else {
			((Syxw) mActivity).setTouzhuResult(mZhushu);
		}
		mFrontAdapter.notifyDataSetChanged();
		mBehindAdapter.notifyDataSetChanged();
		mLastAdapter.notifyDataSetChanged();
	}

	public void updateBallData(String ball) {
		_clearLiuData();
		_initLiuBool();
		String[] result = ball.split(" ");
		mFrontBall.clear();
		mbehindBall.clear();
		mLastBall.clear();
		mFrontBall.add(result[0]);
		boolFront.set(Integer.valueOf(result[0]) - 1, true);
		mbehindBall.add(result[1]);
		boolBehind.set(Integer.valueOf(result[1]) - 1, true);
		mLastBall.add(result[2]);
		boolLast.set(Integer.valueOf(result[2]) - 1, true);
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
			for (int j = 0; j < mbehindBall.size(); j++)
				for (int k = 0; k < mLastBall.size(); k++) {
					result.add(mFrontBall.get(i) + " " + mbehindBall.get(j)
							+ " " + mLastBall.get(k));
				}
		return result;
	}

	public void clearChoose() {
		_initData();
		mFrontBall.clear();
		mbehindBall.clear();
		mLastBall.clear();
		mFrontAdapter.notifyDataSetChanged();
		mBehindAdapter.notifyDataSetChanged();
		mLastAdapter.notifyDataSetChanged();
		mZhushu = 0;
		if ( mLocalClassName.equals("buycenter.Syiyunjin")) {
			((Syiyunjin) mActivity).setTouzhuResult(mZhushu);
		} else {
			((Syxw) mActivity).setTouzhuResult(mZhushu);
		}
	}

    public ArrayList<Boolean> getBoolFront() {
        return boolFront;
    }

    public ArrayList<Boolean> getBoolBehind() {
        return boolBehind;
    }

    public ArrayList<Boolean> getBoolLast() {
        return boolLast;
    }

    public void updateChoice(ArrayList<Boolean> firstUpdateData, ArrayList<Boolean> secondUpdateData,
                             ArrayList<Boolean> thirdUpdateData)
    {
        boolean chosen = false;
        boolean changed = false;

        for (int i = 0; i < firstUpdateData.size(); i++) {
            if (boolFront.get(i) != firstUpdateData.get(i))
            {
                changed = true;
            }

            boolFront.set(i, firstUpdateData.get(i));
            chosen = chosen || boolFront.get(i);
        }

        for (int i = 0; i < secondUpdateData.size(); i++) {
            if (boolBehind.get(i) != secondUpdateData.get(i))
            {
                changed = true;
            }

            boolBehind.set(i, secondUpdateData.get(i));
            chosen = chosen || boolBehind.get(i);
        }

        for (int i = 0; i < thirdUpdateData.size(); i++) {
            if (boolLast.get(i) != thirdUpdateData.get(i))
            {
                changed = true;
            }

            boolLast.set(i, thirdUpdateData.get(i));
            chosen = chosen || boolLast.get(i);
        }

        changed = changed && chosen;

        if (changed)
        {
            Vibrator vibrator = (Vibrator) getActivity().getApplication()
                    .getSystemService(Service.VIBRATOR_SERVICE);
            vibrator.vibrate(new long[] { 0, 200 }, -1);
        }
        mFrontAdapter.notifyDataSetChanged();
        mBehindAdapter.notifyDataSetChanged();
        mLastAdapter.notifyDataSetChanged();

        updateBottom();
    }

    //更新父activity底部的投注数
    private void updateBottom()
    {
        mFrontBall.clear();
        mbehindBall.clear();
        mLastBall.clear();
        for (int i = 0; i < 11; i++) {
            String ballStr;
            if(i<9) {
                ballStr= "0" + String.valueOf(i + 1);
            } else {
                ballStr= String.valueOf(i + 1);
            }
            if (boolFront.get(i))
            {
                mFrontBall.add(ballStr);
            }
            else
            {
                mFrontBall.remove(ballStr);
            }
            if (boolBehind.get(i))
            {
                mbehindBall.add(ballStr);
            }
            else
            {
                mbehindBall.remove(ballStr);
            }
            if (boolLast.get(i))
            {
                mLastBall.add(ballStr);
            }
            else
            {
                mLastBall.remove(ballStr);
            }
        }

        updateCount();
    }

    private void updateCount()
    {
        mZhushu = mFrontBall.size() * mbehindBall.size()* mLastBall.size();

        if ( mLocalClassName.equals("buycenter.Syiyunjin")) {
            ((Syiyunjin) mActivity).setTouzhuResult(mZhushu);
        } else {
            ((Syxw) mActivity).setTouzhuResult(mZhushu);
        }
    }
}