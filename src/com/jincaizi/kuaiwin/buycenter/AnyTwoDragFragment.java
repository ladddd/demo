package com.jincaizi.kuaiwin.buycenter;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jincaizi.R;

public class AnyTwoDragFragment extends ElevenFiveBaseDragFragment {

	public static final String TAG = "AnyTwoDragFragment";

    @Override
    protected void _findViews(View view) {
        super._findViews(view);

        ((TextView) view.findViewById(R.id.text_first)).setText("猜对任意2个开奖号即中");
        ((TextView) view.findViewById(R.id.text_second)).setText("6");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        maxFirst = 1;
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected void updateCount(boolean vibrate) {
        int redDanFactor = mRedDanBall.size() > 0 ? 1 : 0;
        mZhushu = redDanFactor
                * mRedTuoBall.size();


//        min = (mRedTuoBall.size()>7?mRedTuoBall.size()-7:1) * 6;
        min = Math.max(1, mRedTuoBall.size() - 6) * 6;
        max = Math.min(4, mRedTuoBall.size())*6;
        super.updateCount(vibrate);
    }
}