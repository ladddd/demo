package com.jincaizi.kuaiwin.buycenter;

import java.util.ArrayList;
import java.util.Collections;

import android.os.Bundle;

public class AnysevenFragment extends BaseElevenFiveFragment {
	public static final String TAG = "AnysevenFragment";

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
        selectNumber = 7;
        baiHint.setText("至少选择7个球");

		super.onActivityCreated(savedInstanceState);
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
								for (int index7 = n + 1; index7 < mLiuBall
										.size(); index7++) {
									result.add(mLiuBall.get(i) + " "
											+ mLiuBall.get(j) + " "+ mLiuBall.get(k) + " "
											+ mLiuBall.get(l) + " " + mLiuBall.get(m) + " "
											+ mLiuBall.get(n) + " "
											+ mLiuBall.get(index7));
								}
							}
						}
					}
				}
			}
		}
		return result;
	}
}