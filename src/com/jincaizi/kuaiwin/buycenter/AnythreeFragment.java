package com.jincaizi.kuaiwin.buycenter;

import java.util.ArrayList;
import java.util.Collections;

import android.os.Bundle;
import com.jincaizi.kuaiwin.utils.Utils;

public class AnythreeFragment extends BaseElevenFiveFragment {
    public static final String TAG = "AnythreeFragment";

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        selectNumber = 3;
        hintFirst.setText("至少选3个号，猜对任意3个开奖号即中");
        hintPrice.setText("19");

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected void updateCount(boolean vibrate) {
        super.updateCount(vibrate);

        //选择号码的个数
        int selectedCount = 0;
        for (int i = 0; i < boolLiu.size(); i++) {
            selectedCount += boolLiu.get(i)?1:0;
        }
        //C5 3最大
        ((Syxw) mActivity).setBuyTips(19 * Utils.getZuHeNum(Math.min(selectedCount, 5), 3),
                19 * Utils.getZuHeNum(Math.max(5 - (11 - selectedCount), 3), 3),
                mZhushu);
    }

    public ArrayList<String> getNumPerSelList() {
        ArrayList<String> result = new ArrayList<String>();
        Collections.sort(mLiuBall);
        for (int i = 0; i < mLiuBall.size(); i++) {
            for (int j = i + 1; j < mLiuBall.size(); j++) {
                for (int k = j + 1; k < mLiuBall.size(); k++) {
                    result.add(mLiuBall.get(i)+ " " + mLiuBall.get(j) + " "
                            + mLiuBall.get(k));
                }
            }
        }
        return result;
    }
}