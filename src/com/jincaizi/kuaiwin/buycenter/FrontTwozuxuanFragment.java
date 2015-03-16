package com.jincaizi.kuaiwin.buycenter;

import java.util.ArrayList;
import java.util.Collections;

import android.os.Bundle;

public class FrontTwozuxuanFragment extends BaseElevenFiveFragment {
	public static final String TAG = "FrontTwozuxuanFragment";

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
        selectNumber = 2;
        hintFirst.setText("至少选2个号，猜前2个开奖号即中");
        hintPrice.setText("65");

		super.onActivityCreated(savedInstanceState);
	}

    @Override
    protected void updateCount() {
        super.updateCount();

        ((Syxw) mActivity).setBuyTips(65, 65, mZhushu);
    }

	public ArrayList<String> getPlsResultList() {
		ArrayList<String> result = new ArrayList<String>();
			Collections.sort(mLiuBall);
				for (int j = 0; j < mLiuBall.size(); j++) {
					for (int k = j + 1; k < mLiuBall.size(); k++) {
						result.add(mLiuBall.get(j) +" " + mLiuBall.get(k));
					}
				}
		return result;
	}
}