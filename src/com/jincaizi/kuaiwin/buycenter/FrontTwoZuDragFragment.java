package com.jincaizi.kuaiwin.buycenter;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jincaizi.R;

public class FrontTwoZuDragFragment extends ElevenFiveBaseDragFragment {
	public static final String TAG = "FrontTwoZuDragFragment";

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		maxFirst = 1;
        showLeak = false;
		super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected void updateCount(boolean vibrate) {
        int redDanFactor = mRedDanBall.size() > 0 ? 1 : 0;
        mZhushu = redDanFactor
                * mRedTuoBall.size();

        min = 65;
        max = min;
        super.updateCount(vibrate);
    }

    @Override
    protected void _findViews(View view) {
        super._findViews(view);

        ((TextView) view.findViewById(R.id.text_first)).setText("猜对前2个开奖号即中");
        ((TextView) view.findViewById(R.id.text_second)).setText("65");
    }
}