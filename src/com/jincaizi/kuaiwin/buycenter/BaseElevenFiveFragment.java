package com.jincaizi.kuaiwin.buycenter;

import android.app.Activity;
import android.app.Service;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import com.jincaizi.R;
import com.jincaizi.adapters.BallGridViewAdapter;
import com.jincaizi.kuaiwin.tool.ShiyiyunRandom;
import com.jincaizi.kuaiwin.utils.Utils;
import com.jincaizi.kuaiwin.widget.MyGridView;
import com.jincaizi.kuaiwin.widget.ShakeListener;

import java.util.ArrayList;

/**
 * Created by chenweida on 2015/3/4.
 */
public class BaseElevenFiveFragment extends Fragment{

    protected String[] r_content = { "1", "2", "3", "4", "5", "6", "7", "8", "9",
            "10", "11" };
    protected MyGridView front_ball_group;
    protected Activity mActivity;
    protected BallGridViewAdapter mRedAdapter;
    protected ShakeListener mShakeListener;
    protected ArrayList<String> mLiuBall = new ArrayList<String>();
    public int mZhushu = 0;
    protected TextView baiHint;
    protected String mLocalClassName;
    protected ArrayList<Boolean> boolLiu = new ArrayList<Boolean>();

    protected int selectNumber = 8;

    public ArrayList<Boolean> getBoolLiu()
    {
        return boolLiu;
    }

    protected void _initLiuBool() {
        for (int i = 0; i < 11; i++) {
            boolLiu.add(false);
        }
    }

    protected void _clearLiuData() {
        boolLiu.clear();
    }

    protected void _initData() {
        _clearLiuData();
        _initLiuBool();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _initLiuBool();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);

        mActivity = getActivity();

        mRedAdapter = new BallGridViewAdapter(getActivity(), r_content,
                boolLiu, true);

        front_ball_group.setAdapter(mRedAdapter);
        mLocalClassName = mActivity.getLocalClassName();

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        return inflater.inflate(R.layout.pls_fragment_layout, container, false);
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
        front_ball_group.setOnItemClickListener(new AdapterView.OnItemClickListener() {

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
                updateCount();
            }
        });

    }

    private void updateCount()
    {
        mZhushu = Utils.getZuHeNum(mLiuBall.size(), selectNumber);

        if ( mLocalClassName.equals("buycenter.Syiyunjin")) {
            ((Syiyunjin) mActivity).setTouzhuResult(mZhushu);
        } else {
            ((Syxw) mActivity).setTouzhuResult(mZhushu);
        }
    }

    //更新父activity底部的投注数
    private void updateBottom()
    {
        mLiuBall.clear();
        for (int i = 0; i < 11; i++) {
            String ballStr;
            if(i<9) {
                ballStr= "0" + String.valueOf(i + 1);
            } else {
                ballStr= String.valueOf(i + 1);
            }
            if (boolLiu.get(i))
            {
                mLiuBall.add(ballStr);
            }
            else
            {
                mLiuBall.remove(ballStr);
            }
        }

        updateCount();
    }

    class shakeLitener implements ShakeListener.OnShakeListener {

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
        shakeResult = ShiyiyunRandom.getSyyBallNoRePeat(selectNumber);
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
        mLiuBall.clear();
        String[]result = ball.split(" ");
        for (int i = 0; i < result.length; i++) {
            mLiuBall.add(result[i]);
            boolLiu.set(Integer.valueOf(result[i]) - 1, true);
        }
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

    public void updateChoice(ArrayList<Boolean> updateData)
    {
        boolean chosen = false;
        boolean changed = false;
        for (int i = 0; i < updateData.size(); i++) {
            if (boolLiu.get(i) != updateData.get(i))
            {
                changed = true;
            }

            boolLiu.set(i, updateData.get(i));
            chosen = chosen || boolLiu.get(i);
        }
        changed = changed && chosen;

        if (changed)
        {
            Vibrator vibrator = (Vibrator) getActivity().getApplication()
                    .getSystemService(Service.VIBRATOR_SERVICE);
            vibrator.vibrate(new long[] { 0, 200 }, -1);
        }
        mRedAdapter.notifyDataSetChanged();

        updateBottom();
    }
}
