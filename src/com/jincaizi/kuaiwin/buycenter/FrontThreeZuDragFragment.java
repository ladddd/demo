package com.jincaizi.kuaiwin.buycenter;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jincaizi.R;
import com.jincaizi.kuaiwin.utils.Utils;

public class FrontThreeZuDragFragment extends ElevenFiveBaseDragFragment {
	public static final String TAG = "FrontThreeZuDragFragment";

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
        maxFirst = 2;
        showLeak = false;
		super.onActivityCreated(savedInstanceState);
	}

    @Override
    protected void updateCount(boolean vibrate) {
        int redDanFactor = mRedDanBall.size() > 0 ? 1 : 0;
        mZhushu = redDanFactor
                * Utils.getZuHeNum(mRedTuoBall.size(), 3-mRedDanBall.size());

        min = 195;
        max = min;
        super.updateCount(vibrate);
    }

    @Override
    protected void _findViews(View view) {
        super._findViews(view);

        ((TextView) view.findViewById(R.id.text_first)).setText("猜对前3个开奖号即中");
        ((TextView) view.findViewById(R.id.text_second)).setText("195");
    }
}