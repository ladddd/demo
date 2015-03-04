package com.jincaizi.kuaiwin.buycenter.sharexicai;

import java.util.ArrayList;

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
import com.jincaizi.kuaiwin.utils.Utils;
import com.jincaizi.kuaiwin.widget.MyGridView;

public class DltDragFragment extends Fragment {
	public static final String TAG = "DltDragFragment";
	private String[] r_dan_content = { "01", "02", "03", "04", "05", "06",
			"07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17",
			"18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28",
			"29", "30", "31", "32", "33" };
	private String[] b_dan_content = { "01", "02", "03", "04", "05", "06",
			"07", "08", "09", "10", "11", "12"};
	private String[] r_tuo_content = { "01", "02", "03", "04", "05", "06",
			"07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17",
			"18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28",
			"29", "30", "31", "32", "33" };
	private String[] b_tuo_content = { "01", "02", "03", "04", "05", "06",
			"07", "08", "09", "10", "11", "12"};
	private ArrayList<Boolean> boolBluedan = new ArrayList<Boolean>();
	private ArrayList<Boolean> boolBluetuo = new ArrayList<Boolean>();
	private ArrayList<Boolean> boolReddan = new ArrayList<Boolean>();
	private ArrayList<Boolean> boolRedtuo = new ArrayList<Boolean>();
	private MyGridView red_dan_group;
	private MyGridView red_tuo_group;
	private MyGridView blue_tuo_group;
	private MyGridView blue_dan_group;
	private XicaiBallGridViewAdapter mRedDanAdapter, mRedTuoAdapter,
			mBlueTuoAdapter, mBlueDanAdapter;
	private ArrayList<String> mRedTuoBall = new ArrayList<String>();
	private ArrayList<String> mRedDanBall = new ArrayList<String>();
	private ArrayList<String> mBlueTuoBall = new ArrayList<String>();
	private ArrayList<String> mBlueDanBall = new ArrayList<String>();
	private ArrayList<String> mRedAllBall = new ArrayList<String>();
	private ArrayList<String> mBlueAllBall = new ArrayList<String>();
	public int mZhushu;

	public ArrayList<String> getmBlueDanBall() {
		return mBlueDanBall;
	}

	public void setmBlueDanBall(ArrayList<String> mBlueDanBall) {
		this.mBlueDanBall = mBlueDanBall;
	}

	public ArrayList<String> getmRedAllBall() {
		return mRedAllBall;
	}

	public void setmRedAllBall(ArrayList<String> mRedAllBall) {
		this.mRedAllBall = mRedAllBall;
	}

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

	public ArrayList<String> getmBlueTuoBall() {
		return mBlueTuoBall;
	}

	public void setmBlueTuoBall(ArrayList<String> mBlueTuoBall) {
		this.mBlueTuoBall = mBlueTuoBall;
	}
	

	public ArrayList<String> getmBlueAllBall() {
		return mBlueAllBall;
	}

	public void setmBlueAllBall(ArrayList<String> mBlueAllBall) {
		this.mBlueAllBall = mBlueAllBall;
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
		boolBluedan.clear();
		boolBluetuo.clear();
	}

	private void _initBool() {
		for (int i = 0; i < 35; i++) {
			boolReddan.add(false);
			boolRedtuo.add(false);
		}
		for (int i = 0; i < 12; i++) {
			boolBluedan.add(false);
			boolBluetuo.add(false);
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		mRedDanAdapter = new XicaiBallGridViewAdapter(getActivity(), r_dan_content,
				boolReddan, true);
		mRedTuoAdapter = new XicaiBallGridViewAdapter(getActivity(), r_tuo_content,
				boolRedtuo, true);
		mBlueTuoAdapter = new XicaiBallGridViewAdapter(getActivity(), b_tuo_content,
				boolBluetuo, false);
		mBlueDanAdapter = new XicaiBallGridViewAdapter(getActivity(), b_dan_content,
				boolBluedan, false);
		red_dan_group.setAdapter(mRedDanAdapter);
		red_tuo_group.setAdapter(mRedTuoAdapter);
		blue_tuo_group.setAdapter(mBlueTuoAdapter);
		blue_dan_group.setAdapter(mBlueDanAdapter);
		int redDanFactor = mRedDanBall.size() > 0 ? 1 : 0;
		int blueTuoFactor = mBlueTuoBall.size() > 1?1:0;
		int redZhushu = redDanFactor 
				* Utils.getZuHeNum(mRedTuoBall.size(), 5 - mRedDanBall.size());
		mZhushu = redZhushu * blueTuoFactor* Utils.getZuHeNum(mBlueTuoBall.size(), 2- mBlueDanBall.size());
		
		//((Dlt) getActivity()).setTouzhuResult(mZhushu);
		if(redZhushu <= 1) {
			  ((Dlt) getActivity()).setTouzhuResult(0);
			  mZhushu = 0;
		  } else {
			  ((Dlt) getActivity()).setTouzhuResult(mZhushu);
		  }
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		_findViews(view);
		_setListeners();
		super.onViewCreated(view, savedInstanceState);
	}

	private void _findViews(View view) {
		red_dan_group = (MyGridView) view.findViewById(R.id.gv_red_dan_drag);
		red_tuo_group = (MyGridView) view.findViewById(R.id.gv_red_tuo_group);
		blue_tuo_group = (MyGridView) view.findViewById(R.id.gv_blue_tuo_group);
		blue_dan_group = (MyGridView) view.findViewById(R.id.gv_blue_dan_group);
		view.findViewById(R.id.lv_dan_blue).setVisibility(View.VISIBLE);
		TextView front_red_hint = (TextView)view.findViewById(R.id.front_red＿name);
		TextView front_blue_hint = (TextView)view.findViewById(R.id.front_blue＿name);
		TextView behind_red_hint = (TextView)view.findViewById(R.id.behand_red＿name);
		TextView behind_blue_hint = (TextView)view.findViewById(R.id.behand_blue＿name);
		front_red_hint.setText("红色胆码-至少选择1个，最多4个");
		front_red_hint.setTextColor(getActivity().getResources().getColor(R.color.red));
		front_blue_hint.setText("蓝色胆码-至多选择1个");
		behind_red_hint.setText("红色托码");
		behind_red_hint.setTextColor(getActivity().getResources().getColor(R.color.red));
		behind_blue_hint.setText("蓝色托码-至少选择2个");
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
							.getColor(android.R.color.black));
					boolReddan.set(arg2, false);
					mRedDanBall.remove(ball);
					mRedAllBall.remove(ball);
				} else {
					if (mRedDanBall.size() >= 4) {
						Toast.makeText(getActivity(), "最多只能选择4个红色胆码",
								Toast.LENGTH_SHORT).show();
						return;
					}
					ballview.setTextColor(getActivity().getResources()
							.getColor(android.R.color.white));
					if (mRedAllBall.contains(ball)) {
						mRedTuoBall.remove(ball);
						boolRedtuo.set(arg2, false);
						mRedTuoAdapter.notifyDataSetChanged();
					} else {
						mRedAllBall.add(ball);
					}
					ballview.setBackgroundResource(R.drawable.ball_red);
					boolReddan.set(arg2, true);
					mRedDanBall.add(ball);

				}
				int redDanFactor = mRedDanBall.size() > 0 ? 1 : 0;
				int blueTuoFactor = mBlueTuoBall.size() > 1?1:0;
				int redZhushu = redDanFactor* Utils.getZuHeNum(mRedTuoBall.size(), 5 - mRedDanBall.size());
				mZhushu =  redZhushu*blueTuoFactor 	
						* Utils.getZuHeNum(mBlueTuoBall.size(), 2- mBlueDanBall.size());
				//((Dlt) getActivity()).setTouzhuResult(mZhushu);
				if(redZhushu <= 1) {
					  ((Dlt) getActivity()).setTouzhuResult(0);
					  mZhushu = 0;
				  } else {
					  ((Dlt) getActivity()).setTouzhuResult(mZhushu);
				  }
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
							.getColor(android.R.color.black));
					boolRedtuo.set(arg2, false);
					mRedTuoBall.remove(ball);
					mRedAllBall.remove(ball);
				} else {
					if (mRedAllBall.contains(ball)) {
						mRedDanBall.remove(ball);
						boolReddan.set(arg2, false);
						mRedDanAdapter.notifyDataSetChanged();
					} else {
						mRedAllBall.add(ball);
					}
					ballview.setTextColor(getActivity().getResources()
							.getColor(android.R.color.white));
					ballview.setBackgroundResource(R.drawable.ball_red);
					boolRedtuo.set(arg2, true);
					mRedTuoBall.add(ball);
				}
				int redDanFactor = mRedDanBall.size() > 0 ? 1 : 0;
				int blueTuoFactor = mBlueTuoBall.size() > 1?1:0;
				int redZhushu = redDanFactor
						* Utils.getZuHeNum(mRedTuoBall.size(), 5 - mRedDanBall.size());
				mZhushu = redZhushu *blueTuoFactor 
						* Utils.getZuHeNum(mBlueTuoBall.size(), 2- mBlueDanBall.size());
				//((Dlt) getActivity()).setTouzhuResult(mZhushu);
				if(redZhushu <= 1) {
					  ((Dlt) getActivity()).setTouzhuResult(0);
					  mZhushu = 0;
				  } else {
					  ((Dlt) getActivity()).setTouzhuResult(mZhushu);
				  }
			}
		});
		blue_tuo_group.setOnItemClickListener(new OnItemClickListener() {

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
				if (boolBluetuo.get(arg2)) {
					ballview.setTextColor(getActivity().getResources()
							.getColor(android.R.color.black));
					ballview.setBackgroundResource(R.drawable.ball_gray);
					boolBluetuo.set(arg2, false);
					mBlueTuoBall.remove(ball);
				} else {
					if(mBlueAllBall.contains(ball)) {
						boolBluedan.set(arg2, false);
						mBlueDanBall.remove(ball);
						mBlueDanAdapter.notifyDataSetChanged();
					} else {
						mBlueAllBall.add(ball);
					}
					ballview.setTextColor(getActivity().getResources()
							.getColor(android.R.color.white));
					ballview.setBackgroundResource(R.drawable.ball_blue);
					boolBluetuo.set(arg2, true);
					mBlueTuoBall.add(ball);
				}
				int redDanFactor = mRedDanBall.size() > 0 ? 1 : 0;
				int blueTuoFactor = mBlueTuoBall.size() > 1?1:0;
				int redZhushu =  redDanFactor 
						* Utils.getZuHeNum(mRedTuoBall.size(), 5 - mRedDanBall.size());
				mZhushu = redZhushu *blueTuoFactor
						* Utils.getZuHeNum(mBlueTuoBall.size(), 2- mBlueDanBall.size());
				//((Dlt) getActivity()).setTouzhuResult(mZhushu);
				if(redZhushu <= 1) {
					  ((Dlt) getActivity()).setTouzhuResult(0);
					  mZhushu = 0;
				  } else {
					  ((Dlt) getActivity()).setTouzhuResult(mZhushu);
				  }
			}
		});
		blue_dan_group.setOnItemClickListener(new OnItemClickListener() {

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
				if (boolBluedan.get(arg2)) {
					ballview.setTextColor(getActivity().getResources()
							.getColor(android.R.color.black));
					ballview.setBackgroundResource(R.drawable.ball_gray);
					boolBluedan.set(arg2, false);
					mBlueDanBall.remove(ball);
				} else {
					if (mBlueDanBall.size() >= 1) {
						Toast.makeText(getActivity(), "最多只能选择1个蓝色胆码",
								Toast.LENGTH_SHORT).show();
						return;
					}
					if(mBlueAllBall.contains(ball)) {
						boolBluetuo.set(arg2, false);
						mBlueTuoBall.remove(ball);
						mBlueTuoAdapter.notifyDataSetChanged();
					} else {
						mBlueAllBall.add(ball);
					}
					ballview.setTextColor(getActivity().getResources()
							.getColor(android.R.color.white));
					ballview.setBackgroundResource(R.drawable.ball_blue);
					boolBluedan.set(arg2, true);
					mBlueDanBall.add(ball);
				}
				int redDanFactor = mRedDanBall.size() > 0 ? 1 : 0;
				int blueTuoFactor = mBlueTuoBall.size() > 1?1:0;
				int redZhushu =  redDanFactor
						* Utils.getZuHeNum(mRedTuoBall.size(), 5 - mRedDanBall.size());
				mZhushu = redZhushu *blueTuoFactor 
						* Utils.getZuHeNum(mBlueTuoBall.size(), 2- mBlueDanBall.size());
				//((Dlt) getActivity()).setTouzhuResult(mZhushu);
				if(redZhushu <= 1) {
					  ((Dlt) getActivity()).setTouzhuResult(0);
					  mZhushu = 0;
				  } else {
					  ((Dlt) getActivity()).setTouzhuResult(mZhushu);
				  }
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
			ArrayList<String> redTuoBall, ArrayList<String> blueBall, boolean blueDanMark) {
		_initData();
		mRedTuoBall.clear();
		mRedDanBall.clear();
		mBlueTuoBall.clear();
		mBlueDanBall.clear();
		mRedAllBall.clear();
		mRedAllBall.addAll(redTuoBall);
		mRedAllBall.addAll(redDanBall);
		mBlueAllBall.clear();
		mBlueAllBall.addAll(blueBall);
		for (int i = 0; i < redDanBall.size(); i++) {
			boolReddan.set(Integer.valueOf(redDanBall.get(i)) - 1, true);
			mRedDanBall.add(redDanBall.get(i));
		}
		for (int i = 0; i < redTuoBall.size(); i++) {
			boolRedtuo.set(Integer.valueOf(redTuoBall.get(i)) - 1, true);
			mRedTuoBall.add(redTuoBall.get(i));
		}
		if(blueDanMark) {
			mBlueDanBall.add(blueBall.get(0));
			boolBluedan.set(Integer.valueOf(blueBall.get(0)) - 1, true);
			for (int i = 1; i < blueBall.size(); i++) {
				boolBluetuo.set(Integer.valueOf(blueBall.get(i)) - 1, true);
				mBlueTuoBall.add(blueBall.get(i));
			}
		} else {
		for (int i = 0; i < blueBall.size(); i++) {
			boolBluetuo.set(Integer.valueOf(blueBall.get(i)) - 1, true);
			mBlueTuoBall.add(blueBall.get(i));
		}
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
		mBlueTuoBall.clear();
		mBlueDanBall.clear();
		mRedDanAdapter.notifyDataSetChanged();
		mRedTuoAdapter.notifyDataSetChanged();
		mBlueTuoAdapter.notifyDataSetChanged();
		mBlueDanAdapter.notifyDataSetChanged();
		mZhushu = 0;
		((Dlt) getActivity()).setTouzhuResult(mZhushu);
		
	}
}