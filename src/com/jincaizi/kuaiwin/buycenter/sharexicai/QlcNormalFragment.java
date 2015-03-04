package com.jincaizi.kuaiwin.buycenter.sharexicai;

import java.util.ArrayList;

import android.app.Service;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.jincaizi.R;
import com.jincaizi.kuaiwin.tool.QlcRandom;
import com.jincaizi.kuaiwin.utils.Utils;
import com.jincaizi.kuaiwin.widget.MyGridView;
import com.jincaizi.kuaiwin.widget.ShakeListener;
import com.jincaizi.kuaiwin.widget.ShakeListener.OnShakeListener;

public class QlcNormalFragment extends Fragment {
 public static final String TAG = "QlcNormalFragment";
 private String[] r_content = { "01", "02", "03", "04", "05", "06", "07",
   "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18",
   "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29",
   "30" };
 private ArrayList<Boolean> boolRed = new ArrayList<Boolean>();

 private MyGridView front_ball_group;
 private FragmentActivity mActivity;
 private XicaiBallGridViewAdapter mRedAdapter;
 private ShakeListener mShakeListener;
 private ArrayList<String> mRedBall = new ArrayList<String>();
 public int mZhushu = 0;
 public ArrayList<String> getmRedBall() {
  return mRedBall;
 }

 public void setmRedBall(ArrayList<String> mRedBall) {
  this.mRedBall = mRedBall;
 }

 @Override
 public void onCreate(Bundle savedInstanceState) {
  // TODO Auto-generated method stub
  super.onCreate(savedInstanceState);
  //_initData();
  _initBool();
 }

 private void _initData() {
  _clearData();
  _initBool();
  
 }
 private  void _clearData() {
  boolRed.clear();
 }
 private void _initBool() {
  for (int i = 0; i < 35; i++) {
   boolRed.add(false);
  }
 }

 @Override
 public void onActivityCreated(Bundle savedInstanceState) {
  // TODO Auto-generated method stub
  super.onActivityCreated(savedInstanceState);

  mActivity = getActivity();
  mRedAdapter = new XicaiBallGridViewAdapter(getActivity(), r_content,
    boolRed, true);
  front_ball_group.setAdapter(mRedAdapter);
  mZhushu = Utils.getZuHeNum(mRedBall.size(), 7) ;
        ((Qlc) mActivity).setTouzhuResult(mZhushu);
  mShakeListener = new ShakeListener(getActivity());
  mShakeListener.setOnShakeListener(new shakeLitener());
 }

 @Override
 public void onDestroy() {
  // TODO Auto-generated method stub
  super.onDestroy();
  mShakeListener.stop();
  _initData();
 }

 @Override
 public void onViewCreated(View view, Bundle savedInstanceState) {
  _findViews(view);
  _setListeners();
  super.onViewCreated(view, savedInstanceState);
 }

 private void _findViews(View view) {
  front_ball_group = (MyGridView) view.findViewById(R.id.red_ball_group);
	((TextView) view.findViewById(R.id.front_info)).setText("至少选择7个球");
	view.findViewById(R.id.behind_ball_area).setVisibility(View.GONE);
	
	 TextView frontName = (TextView)view.findViewById(R.id.front_name);
	  frontName.setTextColor(getActivity().getResources().getColor(R.color.red));
 }

 private void _setListeners() {
  front_ball_group.setOnItemClickListener(new OnItemClickListener() {

   @Override
   public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
     long arg3) {
    TextView ballview = (TextView) arg1
      .findViewById(R.id.tv_ssq_ball);
    String ballStr;
    if(arg2<9) {
     ballStr= "0" + String.valueOf(arg2 + 1);
    } else {
     ballStr= String.valueOf(arg2 + 1);
    }
    if (boolRed.get(arg2)) {
     ballview.setTextColor(getActivity().getResources()
       .getColor(android.R.color.black));
     ballview.setBackgroundResource(R.drawable.ball_gray);
     boolRed.set(arg2, false);
     mRedBall.remove(ballStr);
    } else {
     ballview.setTextColor(getActivity().getResources()
       .getColor(android.R.color.white));
     ballview.setBackgroundResource(R.drawable.ball_red);
     boolRed.set(arg2, true);
     mRedBall.add(ballStr);
    }
    mZhushu = Utils.getZuHeNum(mRedBall.size(), 7) ;
    ((Qlc) mActivity).setTouzhuResult(mZhushu);
   }
  });

 }

 @Override
 public View onCreateView(LayoutInflater inflater, ViewGroup container,
   Bundle savedInstanceState) {
  // TODO Auto-generated method stub
  return inflater.inflate(R.layout.doublecolor_nor_layout, container,
    false);
 }

 class shakeLitener implements OnShakeListener {

  @Override
  public void onShake() {
   // TODO Auto-generated method stub
   updateBallData();
   // mShakeListener.stop();
  }

 }

 public void updateBallData() {
  Vibrator vibrator = (Vibrator) getActivity().getApplication()
    .getSystemService(Service.VIBRATOR_SERVICE);
  vibrator.vibrate(new long[] { 0, 200 }, -1);
  _initData();
  mRedBall.clear();
  String[] shakeResult = QlcRandom.getQlcBall();
  Log.d(TAG, "shakeResult lenght = " + shakeResult.length);
  for (int i = 0; i < shakeResult.length; i++) {
   boolRed.set(Integer.valueOf(shakeResult[i]) - 1, true);
   mRedBall.add(shakeResult[i]);
  }
  mRedAdapter.notifyDataSetChanged();
  mZhushu = Utils.getZuHeNum(mRedBall.size(), 7) ;
  ((Qlc) mActivity).setTouzhuResult(mZhushu);
 }
 public void updateBallData(ArrayList<String>redBall, ArrayList<String>blueBall) {
  _initData();
  mRedBall.clear();
  for (int i = 0; i < redBall.size(); i++) {
   boolRed.set(Integer.valueOf(redBall.get(i)) - 1, true);
   mRedBall.add(redBall.get(i));
  }
 }

 @Override
 public void onHiddenChanged(boolean hidden) {
  // TODO Auto-generated method stub
  super.onHiddenChanged(hidden);
  if (hidden) {
   mShakeListener.stop();
  } else {
   mShakeListener.start();
  }
 }
    public void clearChoose() {
     _initData();
     mRedBall.clear();
     mRedAdapter.notifyDataSetChanged();
     mZhushu = 0;
        ((Qlc) mActivity).setTouzhuResult(mZhushu);
    }
 
}