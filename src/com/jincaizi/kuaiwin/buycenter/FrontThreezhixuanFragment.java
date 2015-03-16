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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jincaizi.R;
import com.jincaizi.kuaiwin.buycenter.adapter.ElevenFiveCommonAdapter;
import com.jincaizi.common.StringUtil;
import com.jincaizi.kuaiwin.tool.ShiyiyunRandom;
import com.jincaizi.kuaiwin.widget.ExpandableHeightGridView;
import com.jincaizi.kuaiwin.widget.ShakeListener;
import com.jincaizi.kuaiwin.widget.ShakeListener.OnShakeListener;

public class FrontThreezhixuanFragment extends Fragment {
	public static final String TAG = "FrontThreezhixuanFragment";
	private String[] r_content = { "1", "2", "3", "4", "5", "6", "7", "8", "9",
			"10", "11" };

    private ArrayList<Boolean> boolFront = new ArrayList<Boolean>();

    private ArrayList<Boolean> boolBehind = new ArrayList<Boolean>();
    private ArrayList<Boolean> boolLast = new ArrayList<Boolean>();
    private ExpandableHeightGridView front_ball_group;
    private Activity mActivity;
    private ShakeListener mShakeListener;
    private ArrayList<String> mFrontBall = new ArrayList<String>();
    private ArrayList<String> mbehindBall = new ArrayList<String>();
    private ArrayList<String> mLastBall = new ArrayList<String>();
    public int mZhushu = 0;
    private ExpandableHeightGridView behind_ball_group;
    private ExpandableHeightGridView last_ball_group;
    private ElevenFiveCommonAdapter mFrontAdapter;
    private ElevenFiveCommonAdapter mBehindAdapter;
    private ElevenFiveCommonAdapter mLastAdapter;
    private String mLocalClassName;

    private TextView tipsFirst;
    private TextView tipsProfit;

    private LinearLayout firstTitle;

    protected RelativeLayout randomSelect;
    protected Vibrator vibrator;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// _initData();
		_initLiuBool();
        vibrator = (Vibrator) getActivity().getApplication()
                .getSystemService(Service.VIBRATOR_SERVICE);
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

		mFrontAdapter = new ElevenFiveCommonAdapter(this,
				boolFront, ElevenFiveCommonAdapter.FIRST);
		mBehindAdapter = new ElevenFiveCommonAdapter(this,
				boolBehind, ElevenFiveCommonAdapter.SECOND);
		mLastAdapter = new ElevenFiveCommonAdapter(this,
				boolLast, ElevenFiveCommonAdapter.THIRD);

		mZhushu = mFrontBall.size() * mbehindBall.size()* mLastBall.size();

		front_ball_group.setAdapter(mFrontAdapter);
		behind_ball_group.setAdapter(mBehindAdapter);
		last_ball_group.setAdapter(mLastAdapter);
		mLocalClassName = mActivity.getLocalClassName();
//		if ( mLocalClassName.equals("buycenter.Syiyunjin")) {
//			((Syiyunjin) mActivity).setTouzhuResult(mZhushu);
//		} else {
//			((Syxw) mActivity).setTouzhuResult(mZhushu);
//		}

        updateCount();
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
		front_ball_group = (ExpandableHeightGridView) view
				.findViewById(R.id.pls_bai_ball_group);
		behind_ball_group = (ExpandableHeightGridView) view
				.findViewById(R.id.pls_shi_ball_group);
		last_ball_group = (ExpandableHeightGridView) view
				.findViewById(R.id.pls_ge_ball_group);

        front_ball_group.setExpanded(true);
        behind_ball_group.setExpanded(true);
        last_ball_group.setExpanded(true);

        tipsFirst = (TextView) view.findViewById(R.id.text_first);
        tipsProfit = (TextView) view.findViewById(R.id.text_second);

        tipsFirst.setText("每位至少选1个号，按位猜对前3个开奖号即中");
        tipsProfit.setText("1170");

        firstTitle = (LinearLayout) view.findViewById(R.id.first_gridview_title);
        firstTitle.setVisibility(View.VISIBLE);

        randomSelect = (RelativeLayout) view.findViewById(R.id.shake_random_layout);
	}

	private void _setListeners() {

        randomSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateBallData();
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
		vibrator.vibrate(new long[] { 0, 30 }, -1);
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
//		mZhushu = 1;
//		if ( mLocalClassName.equals("buycenter.Syiyunjin")) {
//			((Syiyunjin) mActivity).setTouzhuResult(mZhushu);
//		} else {
//			((Syxw) mActivity).setTouzhuResult(mZhushu);
//		}
        updateCount();
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
//		mZhushu = 0;
//		if ( mLocalClassName.equals("buycenter.Syiyunjin")) {
//			((Syiyunjin) mActivity).setTouzhuResult(mZhushu);
//		} else {
//			((Syxw) mActivity).setTouzhuResult(mZhushu);
//		}
        updateCount();
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
            vibrator.vibrate(new long[] { 0, 30 }, -1);
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

//        if ( mLocalClassName.equals("buycenter.Syiyunjin")) {
//            ((Syiyunjin) mActivity).setTouzhuResult(mZhushu);
//        } else {
            ((Syxw) mActivity).setTouzhuResult(mZhushu);
//        }
        ((Syxw) mActivity).setBuyTips(1170, 1170, mZhushu);
    }

    public void updateSelection(int position, int type)
    {
        vibrator.vibrate(new long[] { 0, 30 }, -1);

        String ballStr = StringUtil.getResultNumberString(position + 1);

        if (type == ElevenFiveCommonAdapter.FIRST) {
            if (boolFront.get(position)) {
                boolFront.set(position, false);
                mFrontBall.remove(ballStr);
            } else {
                boolFront.set(position, true);
                mFrontBall.add(ballStr);
            }
        }
        else if (type == ElevenFiveCommonAdapter.SECOND)
        {
            if (boolBehind.get(position)) {
                boolBehind.set(position, false);
                mbehindBall.remove(ballStr);
            } else {
                boolBehind.set(position, true);
                mbehindBall.add(ballStr);
            }
        }
        else if (type == ElevenFiveCommonAdapter.THIRD)
        {
            if (boolLast.get(position)) {
                boolLast.set(position, false);
                mLastBall.remove(ballStr);
            } else {
                boolLast.set(position, true);
                mLastBall.add(ballStr);
            }
        }

        updateCount();
    }
}