package com.jincaizi.kuaiwin.buycenter;

import java.util.ArrayList;
import java.util.Collections;

import android.os.Bundle;
import com.jincaizi.kuaiwin.utils.Utils;

public class AnysixFragment extends BaseElevenFiveFragment {
	public static final String TAG = "AnysixFragment";

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
        selectNumber = 6;
        hintFirst.setText("至少选6个号，选号包含5个开奖号即中");
        hintPrice.setText("90");

        super.onActivityCreated(savedInstanceState);
	}

    @Override
    protected void updateCount() {
        super.updateCount();

        //选择号码的个数
        int selectedCount = 0;
        for (int i = 0; i < boolLiu.size(); i++) {
            selectedCount += boolLiu.get(i)?1:0;
        }
        int profit = 90 * Utils.getZuHeNum(selectedCount - 5, 6 - 5);
        ((Syxw) mActivity).setBuyTips(profit, profit, mZhushu);
    }

	public ArrayList<String> getPlsResultList() {
		ArrayList<String> result = new ArrayList<String>();
		Collections.sort(mLiuBall);
		for (int i = 0; i < mLiuBall.size(); i++) {
			for (int j = i + 1; j < mLiuBall.size(); j++) {
				for (int k = j + 1; k < mLiuBall.size(); k++) {
					for (int l = k + 1; l < mLiuBall.size(); l++) {
						for (int m = l + 1; m < mLiuBall.size(); m++) {
							for (int n = m + 1; n < mLiuBall.size(); n++) {
								result.add(mLiuBall.get(i) + " "+ mLiuBall.get(j) + " "
										+ mLiuBall.get(k) +" " +  mLiuBall.get(l) + " "
										+ mLiuBall.get(m) + " " + mLiuBall.get(n));
							}
						}
					}
				}
			}
		}
		return result;
	}
}