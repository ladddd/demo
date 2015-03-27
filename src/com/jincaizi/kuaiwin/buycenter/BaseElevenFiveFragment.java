package com.jincaizi.kuaiwin.buycenter;

import android.app.Activity;
import android.app.Service;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.jincaizi.R;
import com.jincaizi.kuaiwin.buycenter.adapter.ElevenFiveCommonAdapter;
import com.jincaizi.common.StringUtil;
import com.jincaizi.kuaiwin.tool.ShiyiyunRandom;
import com.jincaizi.kuaiwin.utils.Utils;
import com.jincaizi.kuaiwin.widget.ExpandableHeightGridView;
import com.jincaizi.kuaiwin.widget.ShakeListener;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by chenweida on 2015/3/4.
 */
public class BaseElevenFiveFragment extends Fragment{
    protected ExpandableHeightGridView front_ball_group;
    protected Activity mActivity;
    protected ElevenFiveCommonAdapter mRedAdapter;
    protected ShakeListener mShakeListener;
    protected ArrayList<String> mLiuBall = new ArrayList<String>();
    public int mZhushu = 0;
    protected TextView baiHint;
    protected TextView hintFirst;
    protected TextView hintPrice;
    protected String mLocalClassName;
    protected ArrayList<Boolean> boolLiu = new ArrayList<Boolean>(11);
    protected RelativeLayout randomSelect;
    protected Vibrator vibrator;
    protected boolean showLeak = true;

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
        vibrator = (Vibrator) getActivity().getApplication()
                .getSystemService(Service.VIBRATOR_SERVICE);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);

        mActivity = getActivity();

        mRedAdapter = new ElevenFiveCommonAdapter(this, boolLiu, showLeak);
        mRedAdapter.setCurrentMiss(((Syxw)mActivity).getCurrentMiss());

        front_ball_group.setAdapter(mRedAdapter);
        mLocalClassName = mActivity.getLocalClassName();

        updateCount(false);

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
            updateCount(false);
        }
    }

    private void _findViews(View view) {
        front_ball_group = (ExpandableHeightGridView) view
                .findViewById(R.id.pls_bai_ball_group);
        front_ball_group.setExpanded(true);
        TextView baiTitle = (TextView) view.findViewById(R.id.pls_bai_title);
        baiTitle.setText("选号");
        baiHint = (TextView) view.findViewById(R.id.pls_bai_hint);
        hintFirst = (TextView) view.findViewById(R.id.text_first);
        hintPrice = (TextView) view.findViewById(R.id.text_second);
        randomSelect = (RelativeLayout) view.findViewById(R.id.shake_random_layout);
        view.findViewById(R.id.lv_pls_shi).setVisibility(View.GONE);
        view.findViewById(R.id.lv_pls_ge).setVisibility(View.GONE);
    }

    private void _setListeners() {

        randomSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateBallData();
            }
        });
    }

    protected void updateCount(boolean vibrate)
    {
        if (vibrate) {
            vibrator.vibrate(new long[]{0, 30}, -1);
        }

        mZhushu = Utils.getZuHeNum(mLiuBall.size(), selectNumber);

        ((Syxw) mActivity).setTouzhuResult(mZhushu);
    }

    private void updateBallString()
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
    }

    //更新父activity底部的投注数
    private void updateBottom()
    {
        updateBallString();

        updateCount(true);
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
        vibrator.vibrate(new long[] { 0, 30 }, -1);
        _initData();
        ArrayList<String> shakeResult = ShiyiyunRandom.getSyyBallNoRePeat(selectNumber);
        mLiuBall.clear();
        for (int i = 0; i < shakeResult.size(); i++) {
            boolLiu.set(Integer.valueOf(shakeResult.get(i)) - 1, true);
            mLiuBall.add(shakeResult.get(i));
        }
        mZhushu = 1;
        mRedAdapter.notifyDataSetChanged();
        updateCount(true);
    }

    public void updateBallData(ArrayList<Integer> indexList) {
        if (indexList == null || indexList.size() == 0)
        {
            return;
        }

        _initData();

        for (int i = 0; i < boolLiu.size(); i++) {
            boolLiu.set(i, indexList.contains(i));
        }
        updateBallString();
    }

    public void clearChoose() {
        _initData();
        mLiuBall.clear();
        mRedAdapter.notifyDataSetChanged();
        mZhushu = 0;
        updateCount(true);
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
            vibrator.vibrate(new long[] { 0, 30 }, -1);
        }
        mRedAdapter.notifyDataSetChanged();

        updateBottom();
    }

    public void updateSelection(int position)
    {
        vibrator.vibrate(new long[] { 0, 30 }, -1);

        String ballStr = StringUtil.getResultNumberString(position + 1);

        if (boolLiu.get(position)) {
            boolLiu.set(position, false);
            mLiuBall.remove(ballStr);
        } else {
            boolLiu.set(position, true);
            mLiuBall.add(ballStr);
        }
        updateCount(true);
    }

    public void notifyLeakUpdate()
    {
        ArrayList<String> currentMiss = ((Syxw)getActivity()).getCurrentMiss();

        mRedAdapter.setCurrentMiss(currentMiss);
        mRedAdapter.notifyDataSetChanged();
    }

    public String getPlsResultList() {
        StringBuilder builder = new StringBuilder();
        Collections.sort(mLiuBall);
        for (int i = 0; i < mLiuBall.size(); i++) {
            builder.append(mLiuBall.get(i));
            builder.append(" ");
        }
        return builder.toString().trim();
    }

    public ArrayList<Integer> getSelectedIndex()
    {
        ArrayList<Integer> indexList = new ArrayList<Integer>();

        for (int i = 0; i < boolLiu.size(); i++) {
            if (boolLiu.get(i))
            {
                indexList.add(i);
            }
        }

        return indexList;
    }
}
