package com.jincaizi.kuaiwin.buycenter;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jincaizi.R;
import com.jincaizi.kuaiwin.utils.Utils;

public class AnyThreeDragFragment extends ElevenFiveBaseDragFragment {
	public static final String TAG = "AnyThreeDragFragment";

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		maxFirst = 2;
		super.onActivityCreated(savedInstanceState);
	}

    @Override
    protected void updateCount(boolean vibrate) {
        int redDanFactor = mRedDanBall.size() > 0 ? 1 : 0;
        mZhushu = redDanFactor
                * Utils.getZuHeNum(mRedTuoBall.size(), 3-mRedDanBall.size());

        //mRedTuoBall.size() - 6 -----> Math.max(5 - mRedDanBall.size() - (11 - mRedDanBall.size() - mRedTuoBall.size())
        min = Utils.getZuHeNum(Math.max(mRedTuoBall.size() - 6, 3-mRedDanBall.size()),
                3-mRedDanBall.size()) * 19;
        max = Utils.getZuHeNum(Math.min(5-mRedDanBall.size(), mRedTuoBall.size()), 3 - mRedDanBall.size()) * 19;

        super.updateCount(vibrate);
    }

    @Override
    protected void _findViews(View view) {
        super._findViews(view);

        ((TextView) view.findViewById(R.id.text_first)).setText("猜对任意3个开奖号即中");
        ((TextView) view.findViewById(R.id.text_second)).setText("19");
    }
}