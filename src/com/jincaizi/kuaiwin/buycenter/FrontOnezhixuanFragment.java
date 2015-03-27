package com.jincaizi.kuaiwin.buycenter;

import java.util.ArrayList;
import java.util.Collections;

import android.os.Bundle;

public class FrontOnezhixuanFragment extends BaseElevenFiveFragment {
	public static final String TAG = "FrontOnezhixuanFragment";

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
        selectNumber = 1;
        hintFirst.setText("至少选1个号，猜对第1个开奖号即中");
        hintPrice.setText("13");

        showLeak = false;
        super.onActivityCreated(savedInstanceState);
	}

    @Override
    protected void updateCount(boolean vibrate) {
        super.updateCount(vibrate);

        ((Syxw) mActivity).setBuyTips(13, 13, mZhushu);
    }

	public ArrayList<String> getNumPerSelList() {
		ArrayList<String> result = new ArrayList<String>();
			Collections.sort(mLiuBall);
				for (int j = 0; j < mLiuBall.size(); j++) {
						result.add(mLiuBall.get(j));
				}
		return result;
	}
}