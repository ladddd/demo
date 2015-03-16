package com.jincaizi.kuaiwin.buycenter;

import java.util.ArrayList;
import java.util.Collections;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jincaizi.R;
import com.jincaizi.kuaiwin.buycenter.adapter.ElevenFiveDragAdapter;
import com.jincaizi.kuaiwin.tool.DoubleColorFromMachine;

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
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        mRedDanAdapter = new ElevenFiveDragAdapter(this, boolReddan,
                ElevenFiveDragAdapter.DRAG_FIRST, 1);
        mRedTuoAdapter = new ElevenFiveDragAdapter(this, boolRedtuo,
                ElevenFiveDragAdapter.DRAG_SECOND, 1);
        red_dan_group.setAdapter(mRedDanAdapter);
        red_tuo_group.setAdapter(mRedTuoAdapter);
        updateCount();
    }

    @Override
    protected void updateCount() {
        int redDanFactor = mRedDanBall.size() > 0 ? 1 : 0;
        mZhushu = redDanFactor
                * mRedTuoBall.size();

         super.updateCount();
    }

    public ArrayList<String> getPlsResultList() {
		ArrayList<String> result = new ArrayList<String>();
		Collections.sort(mRedDanBall);
		Collections.sort(mRedTuoBall);
		StringBuilder builder = new StringBuilder("(");
		for(int i=0; i< mRedDanBall.size(); i++) {
			builder.append(" " + mRedDanBall.get(i));
		}
		builder.append(" )");
		ArrayList<String[]> resultBehind = DoubleColorFromMachine.combine(mRedTuoBall, 2-mRedDanBall.size());
		for(int i=0; i<resultBehind.size(); i++) {
			StringBuilder builderIn = new StringBuilder();
			String[] resultIn = resultBehind.get(i);
			for(int j=0; j <resultIn.length; j++) {
				builderIn.append(" " + resultIn[j] );
			}
			result.add(builder.toString() + builderIn.toString());
		}
		return result;
	}
}