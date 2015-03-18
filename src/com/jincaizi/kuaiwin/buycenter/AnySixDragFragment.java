package com.jincaizi.kuaiwin.buycenter;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jincaizi.R;
import com.jincaizi.kuaiwin.utils.Utils;

public class AnySixDragFragment extends ElevenFiveBaseDragFragment {
	public static final String TAG = "AnySixDragFragment";

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		maxFirst = 5;
		super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected void updateCount(boolean vibrate) {
        int redDanFactor = mRedDanBall.size() > 0 ? 1 : 0;
        mZhushu = redDanFactor
                * Utils.getZuHeNum(mRedTuoBall.size(), 6-mRedDanBall.size());

        min = 90;
        max = (11 - mRedDanBall.size() - mRedTuoBall.size()) * 90;

        super.updateCount(vibrate);
    }

    @Override
    protected void _findViews(View view) {
        super._findViews(view);

        ((TextView) view.findViewById(R.id.text_first)).setText("选号包含5个开奖号即中");
        ((TextView) view.findViewById(R.id.text_second)).setText("90");
    }
}