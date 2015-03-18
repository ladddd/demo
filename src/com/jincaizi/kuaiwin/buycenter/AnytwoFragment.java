package com.jincaizi.kuaiwin.buycenter;

import java.util.ArrayList;
import java.util.Collections;

import android.os.Bundle;
import com.jincaizi.kuaiwin.utils.Utils;

public class AnytwoFragment extends BaseElevenFiveFragment {
	public static final String TAG = "AnytwoFragment";

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
        selectNumber = 2;
//        baiHint.setText("至少选择2个球");
        hintFirst.setText("至少选2个号，猜对任意2个开奖号即中");
        hintPrice.setText("6");

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
        //C5 2最大
        ((Syxw) mActivity).setBuyTips(6 * Utils.getZuHeNum(Math.min(selectedCount, 5), 2),
                6 * Utils.getZuHeNum(Math.max(5 - (11 - selectedCount), 2), 2),
                mZhushu);
    }

    public ArrayList<String> getPlsResultList() {
		ArrayList<String> result = new ArrayList<String>();
		Collections.sort(mLiuBall);
		for (int j = 0; j < mLiuBall.size(); j++) {
			for (int k = j + 1; k < mLiuBall.size(); k++) {
				result.add(mLiuBall.get(j) + " " + mLiuBall.get(k));
			}
		}
		return result;
	}
}