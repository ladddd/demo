package com.jincaizi.kuaiwin.buycenter;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jincaizi.R;
import com.jincaizi.kuaiwin.utils.Utils;

public class AnyFourDragFragment extends ElevenFiveBaseDragFragment {
	public static final String TAG = "AnyFourDragFragment";

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
        maxFirst = 3;
		super.onActivityCreated(savedInstanceState);
	}

    @Override
    protected void updateCount(boolean vibrate) {
        int redDanFactor = mRedDanBall.size() > 0 ? 1 : 0;
        mZhushu = redDanFactor
                * Utils.getZuHeNum(mRedTuoBall.size(), 4 - mRedDanBall.size());

        min = Utils.getZuHeNum(Math.max(mRedTuoBall.size() - 6, 4-mRedDanBall.size()),
                4-mRedDanBall.size()) * 78;
        max = Utils.getZuHeNum(Math.min(5 - mRedDanBall.size(), mRedTuoBall.size()), 4 - mRedDanBall.size()) * 78;

        super.updateCount(vibrate);
    }

    @Override
    protected void _findViews(View view) {
        super._findViews(view);

        ((TextView) view.findViewById(R.id.text_first)).setText("猜对任意4个开奖号即中");
        ((TextView) view.findViewById(R.id.text_second)).setText("78");
    }
}