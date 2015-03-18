package com.jincaizi.kuaiwin.buycenter;

import java.util.ArrayList;
import java.util.Collections;

import android.os.Bundle;
import com.jincaizi.kuaiwin.utils.Utils;

public class AnyfourFragment extends BaseElevenFiveFragment {
	public static final String TAG = "AnyfourFragment";

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        selectNumber = 4;

        hintFirst.setText("至少选4个号，猜对任意4个开奖号即中");
        hintPrice.setText("78");

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
        //C5 4最大
        ((Syxw) mActivity).setBuyTips(78 * Utils.getZuHeNum(Math.min(selectedCount, 5), 4),
                78 * Utils.getZuHeNum(Math.max(5 - (11 - selectedCount), 4), 4), mZhushu);
    }

	public ArrayList<String> getPlsResultList() {
		ArrayList<String> result = new ArrayList<String>();
		Collections.sort(mLiuBall);
		for (int i = 0; i < mLiuBall.size(); i++) {
			for (int j = i + 1; j < mLiuBall.size(); j++) {
				for (int k = j + 1; k < mLiuBall.size(); k++) {
					for (int l = k + 1; l < mLiuBall.size(); l++) {
						result.add(mLiuBall.get(i) +" " + mLiuBall.get(j) + " "
								+ mLiuBall.get(k) +" " +  mLiuBall.get(l));
					}
				}
			}
		}
		return result;
	}
}