package com.jincaizi.kuaiwin.buycenter;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jincaizi.R;
import com.jincaizi.kuaiwin.utils.Utils;

public class AnyFiveDragFragment extends ElevenFiveBaseDragFragment {
	public static final String TAG = "AnyFiveDragFragment";

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		maxFirst = 4;
		super.onActivityCreated(savedInstanceState);
	}

    @Override
    protected void updateCount(boolean vibrate) {
        int redDanFactor = mRedDanBall.size() > 0 ? 1 : 0;
        mZhushu = redDanFactor
                * Utils.getZuHeNum(mRedTuoBall.size(), 5-mRedDanBall.size());

        min = 540;
        max = min;

        super.updateCount(vibrate);
    }

    @Override
    protected void _findViews(View view) {
        super._findViews(view);

        ((TextView) view.findViewById(R.id.text_first)).setText("猜对全部5个开奖号即中");
        ((TextView) view.findViewById(R.id.text_second)).setText("540");
    }
}