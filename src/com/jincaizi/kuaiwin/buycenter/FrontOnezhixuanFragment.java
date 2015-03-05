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
        baiHint.setText("至少选择1个球");

        super.onActivityCreated(savedInstanceState);
	}

	public ArrayList<String> getPlsResultList() {
		ArrayList<String> result = new ArrayList<String>();
			Collections.sort(mLiuBall);
				for (int j = 0; j < mLiuBall.size(); j++) {
						result.add(mLiuBall.get(j));
				}
		return result;
	}
}