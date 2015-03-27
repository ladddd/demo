package com.jincaizi.kuaiwin.buycenter;

import java.util.ArrayList;
import java.util.Collections;

import android.app.Service;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jincaizi.R;
import com.jincaizi.common.StringUtil;
import com.jincaizi.kuaiwin.buycenter.adapter.ElevenFiveDragAdapter;
import com.jincaizi.kuaiwin.tool.DoubleColorFromMachine;
import com.jincaizi.kuaiwin.widget.ExpandableHeightGridView;

public class ElevenFiveBaseDragFragment extends Fragment {

    protected ArrayList<Boolean> boolReddan = new ArrayList<Boolean>();
    protected ArrayList<Boolean> boolRedtuo = new ArrayList<Boolean>();
    protected ExpandableHeightGridView red_dan_group;
    protected ExpandableHeightGridView red_tuo_group;
    protected ElevenFiveDragAdapter mRedDanAdapter;
    protected ElevenFiveDragAdapter mRedTuoAdapter;
    protected ArrayList<String> mRedTuoBall = new ArrayList<String>();
    protected ArrayList<String> mRedDanBall = new ArrayList<String>();
    public int mZhushu;

    protected int min = 0;
    protected int max = 0;

    protected int maxFirst;
    protected int minSelectedCount;

    protected boolean showLeak = true;

    public ArrayList<String> getmRedTuoBall() {
        return mRedTuoBall;
    }

    public void setmRedTuoBall(ArrayList<String> mRedTuoBall) {
        this.mRedTuoBall = mRedTuoBall;
    }

    public ArrayList<String> getmRedDanBall() {
        return mRedDanBall;
    }

    public void setmRedDanBall(ArrayList<String> mRedDanBall) {
        this.mRedDanBall = mRedDanBall;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        // _initData();
        _initBool();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        minSelectedCount = maxFirst + 1;
        mRedDanAdapter = new ElevenFiveDragAdapter(this,
                boolReddan, ElevenFiveDragAdapter.DRAG_FIRST, maxFirst, showLeak);
        mRedTuoAdapter = new ElevenFiveDragAdapter(this,
                boolRedtuo, ElevenFiveDragAdapter.DRAG_SECOND, maxFirst, showLeak);
        ArrayList<String> currentMiss = ((Syxw) getActivity()).getCurrentMiss();
        mRedDanAdapter.setCurrentMiss(currentMiss);
        mRedTuoAdapter.setCurrentMiss(currentMiss);
        red_dan_group.setAdapter(mRedDanAdapter);
        red_tuo_group.setAdapter(mRedTuoAdapter);
        updateCount(false);
    }

    private void _initData() {
        _clearData();
        _initBool();
    }

    private void _clearData() {
        boolReddan.clear();
        boolRedtuo.clear();
    }

    private void _initBool() {
        for (int i = 0; i < 11; i++) {
            boolReddan.add(false);
            boolRedtuo.add(false);
        }
    }

    protected void updateCount(boolean vibrate)
    {
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getActivity().getApplication()
                    .getSystemService(Service.VIBRATOR_SERVICE);
            vibrator.vibrate(new long[]{0, 30}, -1);
        }

        ((Syxw) getActivity()).setTouzhuResult(mZhushu);
        ((Syxw) getActivity()).setBuyTips(max, min, mZhushu);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        _findViews(view);
        super.onViewCreated(view, savedInstanceState);
    }

    protected void _findViews(View view) {

        red_dan_group = (ExpandableHeightGridView) view.findViewById(R.id.pls_bai_ball_group);
        red_tuo_group = (ExpandableHeightGridView) view.findViewById(R.id.pls_shi_ball_group);

        red_dan_group.setExpanded(true);
        red_tuo_group.setExpanded(true);

        view.findViewById(R.id.first_gridview_title).setVisibility(View.VISIBLE);
        ((TextView) view.findViewById(R.id.pls_bai_title)).setText("胆码");
        ((TextView) view.findViewById(R.id.second_gridview_text)).setText("拖码");

        view.findViewById(R.id.shake_random_layout).setVisibility(View.GONE);
        view.findViewById(R.id.lv_pls_ge).setVisibility(View.GONE);

        ((TextView) view.findViewById(R.id.text_first)).setText("猜对前2个开奖号即中");
        ((TextView) view.findViewById(R.id.text_second)).setText("65");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        return inflater.inflate(R.layout.pls_fragment_layout, container,
                false);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        // TODO Auto-generated method stub
        super.onHiddenChanged(hidden);
        if (!hidden) {
            updateCount(false);
        }
    }

    public void updateSelection(int position, int type)
    {
        Vibrator vibrator = (Vibrator) getActivity().getApplication()
                .getSystemService(Service.VIBRATOR_SERVICE);
        vibrator.vibrate(new long[] { 0, 30 }, -1);

        String ballStr = StringUtil.getResultNumberString(position + 1);

        if (type == ElevenFiveDragAdapter.DRAG_FIRST)
        {
            if (boolReddan.get(position))
            {
                boolReddan.set(position, false);
                mRedDanBall.remove(ballStr);
            }
            else
            {
                boolReddan.set(position, true);
                mRedDanBall.add(ballStr);
                //将拖码对应位重置
                boolRedtuo.set(position, false);
                mRedTuoBall.remove(ballStr);
                mRedTuoAdapter.notifyDataSetChanged();
            }
        }
        else if (type == ElevenFiveDragAdapter.DRAG_SECOND)
        {
            if (boolRedtuo.get(position))
            {
                boolRedtuo.set(position, false);
                mRedTuoBall.remove(ballStr);
            }
            else
            {
                boolRedtuo.set(position, true);
                mRedTuoBall.add(ballStr);
                boolReddan.set(position, false);
                mRedDanBall.remove(ballStr);
                mRedDanAdapter.notifyDataSetChanged();
            }
        }

        updateCount(true);
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        _initData();
    }

    public void clearChoose() {
        _initData();
        mRedTuoBall.clear();
        mRedDanBall.clear();
        mRedDanAdapter.notifyDataSetChanged();
        mRedTuoAdapter.notifyDataSetChanged();
        updateCount(true);
    }

    public void updateBallData(ArrayList<Integer> indexList) {
        if (indexList == null || indexList.size() == 0)
        {
            return;
        }

        _initData();
        mRedTuoBall.clear();
        mRedDanBall.clear();

        for (int i = 0; i < boolReddan.size(); i++) {
            boolReddan.set(i, indexList.contains(i));
            boolRedtuo.set(i, indexList.contains(i + boolReddan.size()));
        }
        for (int i = 0; i < boolReddan.size(); i++) {
            String ballStr = StringUtil.getResultNumberString(i + 1);

            if (boolReddan.get(i))
            {
                mRedDanBall.add(ballStr);
            }
            if (boolRedtuo.get(i))
            {
                mRedTuoBall.add(ballStr);
            }
        }
    }

    public void notifyLeakUpdate()
    {
        ArrayList<String> currentMiss = ((Syxw)getActivity()).getCurrentMiss();

        mRedDanAdapter.setCurrentMiss(currentMiss);
        mRedDanAdapter.notifyDataSetChanged();

        mRedTuoAdapter.setCurrentMiss(currentMiss);
        mRedTuoAdapter.notifyDataSetChanged();
    }

    public String getPlsResultList() {
        Collections.sort(mRedDanBall);
        Collections.sort(mRedTuoBall);
        StringBuilder builder = new StringBuilder();
        for (String aMRedDanBall : mRedDanBall) {
            builder.append(" ");
            builder.append(aMRedDanBall);
        }
        builder.append(" # ");
        for (String aMRedDanBall : mRedTuoBall) {
            builder.append(aMRedDanBall);
            builder.append(" ");
        }

        return builder.toString().trim();
    }

    public ArrayList<String> getNumPerSelList() {
        ArrayList<String> result = new ArrayList<String>();
        Collections.sort(mRedDanBall);
        Collections.sort(mRedTuoBall);
        StringBuilder builder = new StringBuilder("(");
        for(int i=0; i< mRedDanBall.size(); i++) {
            builder.append(" " + mRedDanBall.get(i));
        }
        builder.append(" )");
        ArrayList<String[]> resultBehind = DoubleColorFromMachine.combine(mRedTuoBall, minSelectedCount-mRedDanBall.size());
        for(int i=0; i<resultBehind.size(); i++) {
            StringBuilder builderIn = new StringBuilder();
            String[] resultIn = resultBehind.get(i);
            for(int j=0; j <resultIn.length; j++) {
                builderIn.append(" " + resultIn[j] );
            }
            result.add(builder.toString() + builderIn.toString());
        }
        return result;
    }

    public ArrayList<Integer> getSelectedIndex()
    {
        ArrayList<Integer> indexList = new ArrayList<Integer>();

        for (int i = 0; i < boolReddan.size(); i++) {
            if (boolReddan.get(i))
            {
                indexList.add(i);
            }
            if (boolRedtuo.get(i))
            {
                indexList.add(i + boolReddan.size());
            }
        }

        return indexList;
    }
}