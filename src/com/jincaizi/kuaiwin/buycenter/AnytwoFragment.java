package com.jincaizi.kuaiwin.buycenter;

import java.util.ArrayList;
import java.util.Collections;

import android.os.Bundle;

public class AnytwoFragment extends BaseElevenFiveFragment {
	public static final String TAG = "AnytwoFragment";

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
        selectNumber = 2;
        baiHint.setText("至少选择2个球");

		super.onActivityCreated(savedInstanceState);
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