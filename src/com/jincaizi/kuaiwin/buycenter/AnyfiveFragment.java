package com.jincaizi.kuaiwin.buycenter;

import java.util.ArrayList;
import java.util.Collections;

import android.os.Bundle;

public class AnyfiveFragment extends BaseElevenFiveFragment {
	public static final String TAG = "AnyfiveFragment";

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
        selectNumber = 5;
        hintFirst.setText("至少选5个号，猜对全部5个开奖号即中");
        hintPrice.setText("540");
        
		super.onActivityCreated(savedInstanceState);
	}

    @Override
    protected void updateCount(boolean vibrate) {
        super.updateCount(vibrate);

        ((Syxw) mActivity).setBuyTips(540, 540, mZhushu);
    }

	public ArrayList<String> getNumPerSelList() {
		ArrayList<String> result = new ArrayList<String>();
		Collections.sort(mLiuBall);
		for (int i = 0; i < mLiuBall.size(); i++) {
			for (int j = i + 1; j < mLiuBall.size(); j++) {
				for (int k = j + 1; k < mLiuBall.size(); k++) {
					for (int l = k + 1; l < mLiuBall.size(); l++) {
						for (int m = l + 1; m < mLiuBall.size(); m++) {
							result.add(mLiuBall.get(i) + " "+ mLiuBall.get(j) + " "
									+ mLiuBall.get(k) +  " " + mLiuBall.get(l) + " "
									+ mLiuBall.get(m));
						}
					}
				}
			}
		}
		return result;
	}
}