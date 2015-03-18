package com.jincaizi.kuaiwin.buycenter;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jincaizi.R;
import com.jincaizi.kuaiwin.utils.Utils;

public class AnySevenDragFragment extends ElevenFiveBaseDragFragment {
	public static final String TAG = "AnySevenDragFragment";

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		maxFirst = 6;
		super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected void updateCount(boolean vibrate) {
        int redDanFactor = mRedDanBall.size() > 0 ? 1 : 0;
        mZhushu = redDanFactor
                * Utils.getZuHeNum(mRedTuoBall.size(), 7-mRedDanBall.size());

        if (mRedTuoBall.size() - 5 > 0 &&
                7 - 5 - mRedDanBall.size() <= mRedTuoBall.size() - 5)
        {
            min = Utils.getZuHeNum(mRedTuoBall.size() - 5, 7 - 5 - mRedDanBall.size()) * 26;
        }
        else
        {
            min = 26;
        }

        max = Utils.getZuHeNum(mRedTuoBall.size() - (5 - mRedDanBall.size()), 7 - 5) * 26;

        super.updateCount(vibrate);
    }

    @Override
    protected void _findViews(View view) {
        super._findViews(view);

        ((TextView) view.findViewById(R.id.text_first)).setText("包含5个开奖号即中");
        ((TextView) view.findViewById(R.id.text_second)).setText("26");
    }
}