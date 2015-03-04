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
import com.jincaizi.kuaiwin.tool.DoubleColorFromMachine;
import com.jincaizi.kuaiwin.utils.Utils;
import com.jincaizi.kuaiwin.widget.MyGridView;
import com.jincaizi.kuaiwin.widget.ShakeListener;
import com.jincaizi.kuaiwin.widget.ShakeListener.OnShakeListener;

public class DoubleColorNormal extends Fragment {
 public static final String TAG = "DoubleColorNormal";
 private String[] r_content = { "01", "02", "03", "04", "05", "06", "07",
   "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18",
   "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29",
   "30", "31", "32", "33" };
 private String[] b_content = { "01", "02", "03", "04", "05", "06", "07",
   "08", "09", "10", "11", "12", "13", "14", "15", "16" };
 private ArrayList<Boolean> boolBlue = new ArrayList<Boolean>();
 private ArrayList<Boolean> boolRed = new ArrayList<Boolean>();

 private MyGridView front_ball_group, behind_ball_group;
 private FragmentActivity mActivity;
 private XicaiBallGridViewAdapter mRedAdapter;
 private XicaiBallGridViewAdapter mBlueAdapter;
 private ShakeListener mShakeListener;
 private ArrayList<String> mRedBall = new ArrayList<String>();
 private ArrayList<String> mBlueBall = new ArrayList<String>();
 public int mZhushu = 0;
 public ArrayList<String> getmRedBall() {
  return mRedBall;
 }

 public void setmRedBall(ArrayList<String> mRedBall) {
  this.mRedBall = mRedBall;
 }

 public ArrayList<String> getmBlueBall() {
  return mBlueBall;
 }

 public void setmBlueBall(ArrayList<String> mBlueBall) {
  this.mBlueBall = mBlueBall;
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
  boolBlue.clear();
 }
 private void _initBool() {
  for (int i = 0; i < 33; i++) {
   boolRed.add(false);
  }
  for (int i = 0; i < 16; i++) {
   boolBlue.add(false);
  }
 }

 @Override
 public void onActivityCreated(Bundle savedInstanceState) {
  // TODO Auto-generated method stub
  super.onActivityCreated(savedInstanceState);

  mActivity = getActivity();
  mRedAdapter = new XicaiBallGridViewAdapter(getActivity(), r_content,
    boolRed, true);
  mBlueAdapter = new XicaiBallGridViewAdapter(getActivity(), b_content,
    boolBlue, false);
  front_ball_group.setAdapter(mRedAdapter);
  behind_ball_group.setAdapter(mBlueAdapter);
  mZhushu = Utils.getZuHeNum(mRedBall.size(), 6) * Utils.getZuHeNum(mBlueBall.size(), 1);
        ((Ssq) mActivity).setTouzhuResult(mZhushu);
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
  behind_ball_group = (MyGridView) view
    .findViewById(R.id.blue_ball_group);
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
                mZhushu = Utils.getZuHeNum(mRedBall.size(), 6) * Utils.getZuHeNum(mBlueBall.size(), 1);
                ((Ssq) mActivity).setTouzhuResult(mZhushu);
   }
  });
  behind_ball_group.setOnItemClickListener(new OnItemClickListener() {

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
    if (boolBlue.get(arg2)) {
     ballview.setTextColor(getActivity().getResources()
       .getColor(android.R.color.black));
     ballview.setBackgroundResource(R.drawable.ball_gray);
     boolBlue.set(arg2, false);
     mBlueBall.remove(ballStr);
    } else {
     ballview.setBackgroundResource(R.drawable.ball_blue);
     ballview.setTextColor(getActivity().getResources()
       .getColor(android.R.color.white));
     boolBlue.set(arg2, true);
     mBlueBall.add(ballStr);
    }
    mZhushu = Utils.getZuHeNum(mRedBall.size(), 6) * Utils.getZuHeNum(mBlueBall.size(), 1);
                ((Ssq) mActivity).setTouzhuResult(mZhushu);
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
  mBlueBall.clear();
  String[] shakeResult = DoubleColorFromMachine.getDoubleBall();
  Log.d(TAG, "shakeResult lenght = " + shakeResult.length);
  for (int i = 0; i < shakeResult.length - 1; i++) {
   boolRed.set(Integer.valueOf(shakeResult[i]) - 1, true);
   mRedBall.add(shakeResult[i]);
  }
  boolBlue.set(Integer.valueOf(shakeResult[shakeResult.length - 1]) - 1,
    true);
  mBlueBall.add(shakeResult[shakeResult.length - 1]);
  mRedAdapter.notifyDataSetChanged();
  mBlueAdapter.notifyDataSetChanged();
  mZhushu = Utils.getZuHeNum(mRedBall.size(), 6) * Utils.getZuHeNum(mBlueBall.size(), 1);
        ((Ssq) mActivity).setTouzhuResult(mZhushu);
 }
 public void updateBallData(ArrayList<String>redBall, ArrayList<String>blueBall) {
  _initData();
  mRedBall.clear();
  mBlueBall.clear();
  for (int i = 0; i < redBall.size(); i++) {
   boolRed.set(Integer.valueOf(redBall.get(i)) - 1, true);
   mRedBall.add(redBall.get(i));
  }
  for (int i = 0; i < blueBall.size(); i++) {
   boolBlue.set(Integer.valueOf(blueBall.get(i)) - 1, true);
   mBlueBall.add(blueBall.get(i));
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
     mBlueBall.clear();
     mRedAdapter.notifyDataSetChanged();
     mBlueAdapter.notifyDataSetChanged();
     mZhushu = 0;
        ((Ssq) mActivity).setTouzhuResult(mZhushu);
    }
 
}