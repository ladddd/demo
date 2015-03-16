package com.jincaizi.kuaiwin.buycenter;

import java.util.ArrayList;
import java.util.Collections;

import android.os.Bundle;

public class FrontThreezuxuanFragment extends BaseElevenFiveFragment {
	public static final String TAG = "FrontThreezuxuanFragment";

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		selectNumber = 3;
        hintFirst.setText("至少选3个号，猜前3个开奖号即中");
        hintPrice.setText("195");

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected void updateCount() {
        super.updateCount();

        ((Syxw) mActivity).setBuyTips(195, 195, mZhushu);
    }

	public ArrayList<String> getPlsResultList() {
		ArrayList<String> result = new ArrayList<String>();
			Collections.sort(mLiuBall);
			for (int i = 0; i < mLiuBall.size(); i++) {
				for (int j = i + 1; j < mLiuBall.size(); j++) {
					for (int k = j + 1; k < mLiuBall.size(); k++) {
						result.add(mLiuBall.get(i) + " " + mLiuBall.get(j) + " "
								+ mLiuBall.get(k));
					}
				}
			}
		return result;
	}
}