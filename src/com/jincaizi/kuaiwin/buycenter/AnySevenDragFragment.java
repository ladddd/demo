package com.jincaizi.kuaiwin.buycenter;

import java.util.ArrayList;
import java.util.Collections;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.jincaizi.R;
import com.jincaizi.kuaiwin.tool.DoubleColorFromMachine;
import com.jincaizi.kuaiwin.utils.Utils;
import com.jincaizi.kuaiwin.widget.MyGridView;
import com.jincaizi.adapters.BallGridViewAdapter;

public class AnySevenDragFragment extends Fragment {
	public static final String TAG = "AnySevenDragFragment";
	private String[] r_dan_content = { "01", "02", "03", "04", "05", "06",
			"07", "08", "09", "10", "11"};
	private ArrayList<Boolean> boolReddan = new ArrayList<Boolean>();
	private ArrayList<Boolean> boolRedtuo = new ArrayList<Boolean>();
	private MyGridView red_dan_group;
	private MyGridView red_tuo_group;
	private BallGridViewAdapter mRedDanAdapter, mRedTuoAdapter;
	private ArrayList<String> mRedTuoBall = new ArrayList<String>();
	private ArrayList<String> mRedDanBall = new ArrayList<String>();
	public int mZhushu;

	public ArrayList<String> getmRedTuoBall() {
		return mRedTuoBall;
	}

	public void setmRedTuoBall(ArrayList<String> mRedTuoBall) {
		this.mRedTuoBall = mRedTuoBall;
	}

	public ArrayList<String> getmRedDanBall() {
		return mRedDanBall;
	}

	public void setmRedDanBall(ArrayList<String> mRedDanBall) {
		this.mRedDanBall = mRedDanBall;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// _initData();
		_initBool();
	}

	private void _initData() {
		_clearData();
		_initBool();
	}

	private void _clearData() {
		boolReddan.clear();
		boolRedtuo.clear();
	}

	private void _initBool() {
		for (int i = 0; i < 11; i++) {
			boolReddan.add(false);
			boolRedtuo.add(false);
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		mRedDanAdapter = new BallGridViewAdapter(getActivity(), r_dan_content,
				boolReddan, true);
		mRedTuoAdapter = new BallGridViewAdapter(getActivity(), r_dan_content,
				boolRedtuo, true);
		red_dan_group.setAdapter(mRedDanAdapter);
		red_tuo_group.setAdapter(mRedTuoAdapter);
		int redDanFactor = mRedDanBall.size() > 0 ? 1 : 0;
		mZhushu = redDanFactor
				* Utils.getZuHeNum(mRedTuoBall.size(), 7-mRedDanBall.size());
		((Syxw) getActivity()).setTouzhuResult(mZhushu);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		_findViews(view);
		_setListeners();
		super.onViewCreated(view, savedInstanceState);
	}

	private void _findViews(View view) {
		view.findViewById(R.id.lv_behind_blue_pickarea).setVisibility(View.GONE);
		red_dan_group = (MyGridView) view.findViewById(R.id.gv_red_dan_drag);
		red_tuo_group = (MyGridView) view.findViewById(R.id.gv_red_tuo_group);
		TextView front_red_hint = (TextView)view.findViewById(R.id.front_red＿name);
		TextView behind_red_hint = (TextView)view.findViewById(R.id.behand_red＿name);
		front_red_hint.setText("胆码-至少选择1个，最多6个");
		behind_red_hint.setText("托码-选出您认为可能的球");
	}

	private void _setListeners() {
		red_dan_group.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				TextView ballview = (TextView) arg1
						.findViewById(R.id.tv_ssq_ball);
				String ball;
				if (arg2 < 9) {
					ball = "0" + String.valueOf(arg2 + 1);
				} else {
					ball = String.valueOf(arg2 + 1);
				}
				if (boolReddan.get(arg2)) {
					ballview.setBackgroundResource(R.drawable.ball_gray);
					ballview.setTextColor(getActivity().getResources()
							.getColor(R.color.blue));
					boolReddan.set(arg2, false);
					mRedDanBall.remove(ball);
				} else {
					if (mRedDanBall.size() >= 6) {
						Toast.makeText(getActivity(), "最多只能选择6个胆码",
								Toast.LENGTH_SHORT).show();
						return;
					}
					ballview.setTextColor(getActivity().getResources()
							.getColor(android.R.color.white));
					if (mRedTuoBall.contains(ball)) {
						mRedTuoBall.remove(ball);
						boolRedtuo.set(arg2, false);
						mRedTuoAdapter.notifyDataSetChanged();
					} 
					ballview.setBackgroundResource(R.drawable.ball_blue);
					boolReddan.set(arg2, true);
					mRedDanBall.add(ball);

				}
				int redDanFactor = mRedDanBall.size() > 0 ? 1 : 0;
				mZhushu = redDanFactor
						* Utils.getZuHeNum(mRedTuoBall.size(), 7-mRedDanBall.size());
				((Syxw) getActivity()).setTouzhuResult(mZhushu);
			}
		});
		red_tuo_group.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				TextView ballview = (TextView) arg1
						.findViewById(R.id.tv_ssq_ball);
				String ball;
				if (arg2 < 9) {
					ball = "0" + String.valueOf(arg2 + 1);
				} else {
					ball = String.valueOf(arg2 + 1);
				}
				if (boolRedtuo.get(arg2)) {
					ballview.setBackgroundResource(R.drawable.ball_gray);
					ballview.setTextColor(getActivity().getResources()
							.getColor(R.color.blue));
					boolRedtuo.set(arg2, false);
					mRedTuoBall.remove(ball);
				} else {
					if (mRedDanBall.contains(ball)) {
						mRedDanBall.remove(ball);
						boolReddan.set(arg2, false);
						mRedDanAdapter.notifyDataSetChanged();
					} 
					ballview.setTextColor(getActivity().getResources()
							.getColor(android.R.color.white));
					ballview.setBackgroundResource(R.drawable.ball_blue);
					boolRedtuo.set(arg2, true);
					mRedTuoBall.add(ball);
				}
				int redDanFactor = mRedDanBall.size() > 0 ? 1 : 0;
				mZhushu = redDanFactor
						* Utils.getZuHeNum(mRedTuoBall.size(), 7-mRedDanBall.size());
				((Syxw) getActivity()).setTouzhuResult(mZhushu);
			}
		});
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.doublecolor_drag_layout, container,
				false);
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		// TODO Auto-generated method stub
		super.onHiddenChanged(hidden);
	}

	public void updateBallData(ArrayList<String> redDanBall,
			ArrayList<String> redTuoBall) {
		_initData();
		mRedTuoBall.clear();
		mRedDanBall.clear();
		for (int i = 0; i < redDanBall.size(); i++) {
			boolReddan.set(Integer.valueOf(redDanBall.get(i)) - 1, true);
			mRedDanBall.add(redDanBall.get(i));
		}
		for (int i = 0; i < redTuoBall.size(); i++) {
			boolRedtuo.set(Integer.valueOf(redTuoBall.get(i)) - 1, true);
			mRedTuoBall.add(redTuoBall.get(i));
		}
      
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		_initData();
	}

	public void clearChoose() {
		_initData();
		mRedTuoBall.clear();
		mRedDanBall.clear();
		mRedDanAdapter.notifyDataSetChanged();
		mRedTuoAdapter.notifyDataSetChanged();
		mZhushu = 0;
		((Syxw) getActivity()).setTouzhuResult(mZhushu);
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
		ArrayList<String[]> resultBehind = DoubleColorFromMachine.combine(mRedTuoBall, 7-mRedDanBall.size());
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
	public void updateBallData(String ball) {
		_initData();
		mRedTuoBall.clear();
		mRedDanBall.clear();
		String[] result = ball.split(" ");
		int index_right = 0;
		for (int i = 0; i < result.length; i++) {
			if(result[i].equals("(")) {
				continue;
			}
			if(result[i].equals(")")) {
				index_right = i;
				break;
			}
			mRedDanBall.add(result[i]);
			boolReddan.set(Integer.valueOf(result[i]) - 1, true);
		}
		for(int i = index_right + 1; i < result.length; i++) {
			mRedTuoBall.add(result[i]);
			boolRedtuo.set(Integer.valueOf(result[i]) - 1, true);
		}
	}
}