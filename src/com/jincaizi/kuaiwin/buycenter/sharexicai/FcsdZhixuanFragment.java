package com.jincaizi.kuaiwin.buycenter.sharexicai;

import java.util.ArrayList;

import android.app.Service;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.jincaizi.R;
import com.jincaizi.kuaiwin.tool.PlsRandom;
import com.jincaizi.kuaiwin.utils.Utils;
import com.jincaizi.kuaiwin.widget.MyGridView;
import com.jincaizi.kuaiwin.widget.ShakeListener;
import com.jincaizi.kuaiwin.widget.ShakeListener.OnShakeListener;

public class FcsdZhixuanFragment extends Fragment {
 public static final String TAG = "FcsdZhixuanFragment";
 private String[] bai_content = { "0", "1", "2", "3", "4", "5", "6",
   "7", "8","9"};
 private String[] shi_content = { "0", "1", "2", "3", "4", "5", "6",
   "7", "8","9"};
 private String[] ge_content = { "0", "1", "2", "3", "4", "5", "6",
   "7", "8","9"};
 private ArrayList<Boolean> boolBai = new ArrayList<Boolean>();
 private ArrayList<Boolean> boolShi = new ArrayList<Boolean>();
 private ArrayList<Boolean> boolGe = new ArrayList<Boolean>();
 private MyGridView bai_ball_group, shi_ball_group, ge_ball_group;
 private Fcsd mActivity;
 private XicaiBallGridViewAdapter mBaiAdapter;
 private XicaiBallGridViewAdapter mShiAdapter;
 private XicaiBallGridViewAdapter mGeAdapter;
 private ShakeListener mShakeListener;
 private ArrayList<String> mBaiBall = new ArrayList<String>();
 private ArrayList<String> mShiBall = new ArrayList<String>();
 private ArrayList<String> mGeBall = new ArrayList<String>();
 public int mZhushu = 0;


 public ArrayList<String> getmBaiBall() {
  return mBaiBall;
 }

 public void setmBaiBall(ArrayList<String> mBaiBall) {
  this.mBaiBall = mBaiBall;
 }

 public ArrayList<String> getmShiBall() {
  return mShiBall;
 }

 public void setmShiBall(ArrayList<String> mShiBall) {
  this.mShiBall = mShiBall;
 }

 public ArrayList<String> getmGeBall() {
  return mGeBall;
 }

 public void setmGeBall(ArrayList<String> mGeBall) {
  this.mGeBall = mGeBall;
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
  boolBai.clear();
  boolShi.clear();
  boolGe.clear();
 }
 private void _initBool() {
	 boolBai.clear();
	  boolShi.clear();
	  boolGe.clear();
  for (int i = 0; i < 10; i++) {
   boolBai.add(false);
   boolShi.add(false);
   boolGe.add(false);
  }
 }

 @Override
 public void onActivityCreated(Bundle savedInstanceState) {
  // TODO Auto-generated method stub
  super.onActivityCreated(savedInstanceState);

  mActivity = (Fcsd)getActivity();
  mBaiAdapter = new XicaiBallGridViewAdapter(getActivity(), bai_content,
    boolBai, true);
  mShiAdapter = new XicaiBallGridViewAdapter(getActivity(), shi_content,
    boolShi, true);
  mGeAdapter = new XicaiBallGridViewAdapter(getActivity(), ge_content,
    boolGe, true);
  if(mActivity.startType == 2 && !TextUtils.isEmpty(mActivity.mRepickZhixuanStr)) {
  	updateBallData(mActivity.mRepickZhixuanStr);
  	mActivity.mRepickZhixuanStr = "";
  }
  bai_ball_group.setAdapter(mBaiAdapter);
  shi_ball_group.setAdapter(mShiAdapter);
  ge_ball_group.setAdapter(mGeAdapter);
  mZhushu = Utils.getZuHeNum(mBaiBall.size(), 1) *Utils.getZuHeNum(mShiBall.size(), 1) * Utils.getZuHeNum(mGeBall.size(), 1);
        ((Fcsd) mActivity).setTouzhuResult(mZhushu);
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
  bai_ball_group = (MyGridView) view.findViewById(R.id.pls_bai_ball_group);
  shi_ball_group = (MyGridView) view.findViewById(R.id.pls_shi_ball_group);
  ge_ball_group = (MyGridView) view.findViewById(R.id.pls_ge_ball_group);
 }

 private void _setListeners() {
  bai_ball_group.setOnItemClickListener(new OnItemClickListener() {

   @Override
   public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
     long arg3) {
    TextView ballview = (TextView) arg1
      .findViewById(R.id.tv_ssq_ball);
    String ballStr;
     ballStr= String.valueOf(arg2);
    if (boolBai.get(arg2)) {
     ballview.setTextColor(getActivity().getResources()
       .getColor(android.R.color.black));
     ballview.setBackgroundResource(R.drawable.ball_gray);
     boolBai.set(arg2, false);
     mBaiBall.remove(ballStr);
    } else {
     ballview.setTextColor(getActivity().getResources()
       .getColor(android.R.color.white));
     ballview.setBackgroundResource(R.drawable.ball_red);
     boolBai.set(arg2, true);
     mBaiBall.add(ballStr);
    }
    mZhushu = Utils.getZuHeNum(mBaiBall.size(), 1) *Utils.getZuHeNum(mShiBall.size(), 1) * Utils.getZuHeNum(mGeBall.size(), 1);
          ((Fcsd) mActivity).setTouzhuResult(mZhushu);
   }
  });
  shi_ball_group.setOnItemClickListener(new OnItemClickListener() {

   @Override
   public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
     long arg3) {
    TextView ballview = (TextView) arg1
      .findViewById(R.id.tv_ssq_ball);
    String ballStr;
     ballStr= String.valueOf(arg2);
    if (boolShi.get(arg2)) {
     ballview.setTextColor(getActivity().getResources()
       .getColor(android.R.color.black));
     ballview.setBackgroundResource(R.drawable.ball_gray);
     boolShi.set(arg2, false);
     mShiBall.remove(ballStr);
    } else {
     ballview.setBackgroundResource(R.drawable.ball_red);
     ballview.setTextColor(getActivity().getResources()
       .getColor(android.R.color.white));
     boolShi.set(arg2, true);
     mShiBall.add(ballStr);
    }
    mZhushu = Utils.getZuHeNum(mBaiBall.size(), 1) *Utils.getZuHeNum(mShiBall.size(), 1) * Utils.getZuHeNum(mGeBall.size(), 1);
          ((Fcsd) mActivity).setTouzhuResult(mZhushu);
   }
   
  });
  ge_ball_group.setOnItemClickListener(new OnItemClickListener() {

   @Override
   public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
     long arg3) {
    TextView ballview = (TextView) arg1
      .findViewById(R.id.tv_ssq_ball);
    String ballStr;
     ballStr= String.valueOf(arg2);
    if (boolGe.get(arg2)) {
     ballview.setTextColor(getActivity().getResources()
       .getColor(android.R.color.black));
     ballview.setBackgroundResource(R.drawable.ball_gray);
     boolGe.set(arg2, false);
     mGeBall.remove(ballStr);
    } else {
     ballview.setBackgroundResource(R.drawable.ball_red);
     ballview.setTextColor(getActivity().getResources()
       .getColor(android.R.color.white));
     boolGe.set(arg2, true);
     mGeBall.add(ballStr);
    }
    mZhushu = Utils.getZuHeNum(mBaiBall.size(), 1) *Utils.getZuHeNum(mShiBall.size(), 1) * Utils.getZuHeNum(mGeBall.size(), 1);
          ((Fcsd) mActivity).setTouzhuResult(mZhushu);
   }
   
  });
 }

 @Override
 public View onCreateView(LayoutInflater inflater, ViewGroup container,
   Bundle savedInstanceState) {
  // TODO Auto-generated method stub
  return inflater.inflate(R.layout.pls_fragment_layout, container,
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
  mBaiBall.clear();
  mShiBall.clear();
  mGeBall.clear();
  ArrayList<String>shakeResult = PlsRandom.getPLSBallRePeat(3);
  boolBai.set(Integer.valueOf(shakeResult.get(0)), true);
  mBaiBall.add(shakeResult.get(0));
  boolShi.set(Integer.valueOf(shakeResult.get(1)), true);
  mShiBall.add(shakeResult.get(1));
  boolGe.set(Integer.valueOf(shakeResult.get(2)), true);
  mGeBall.add(shakeResult.get(2));
  mBaiAdapter.notifyDataSetChanged();
  mShiAdapter.notifyDataSetChanged();
  mGeAdapter.notifyDataSetChanged();
  mZhushu = Utils.getZuHeNum(mBaiBall.size(), 1) *Utils.getZuHeNum(mShiBall.size(), 1) * Utils.getZuHeNum(mGeBall.size(), 1);
        ((Fcsd) mActivity).setTouzhuResult(mZhushu);
 }
 public void updateBallData(String ball) {
  _initData();
  mBaiBall.clear();
  mShiBall.clear();
  mGeBall.clear();
  String baiStr = ball.substring(0, 1);
  mBaiBall.add(baiStr);
  boolBai.set(Integer.valueOf(baiStr), true);
  String shiStr = ball.substring(1, 2);
  mShiBall.add(shiStr);
  boolShi.set(Integer.valueOf(shiStr), true);
  String geStr = ball.substring(2, 3);
  mGeBall.add(geStr);
  boolGe.set(Integer.valueOf(geStr), true);
  
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
 public ArrayList<String>mZhushuList = new ArrayList<String>();
    public ArrayList<String> getPlsResultList() {
    	mZhushuList.clear();
     ArrayList<String>result = new ArrayList<String>();
     for(int indexBai = 0 ;indexBai <mBaiBall.size(); indexBai++) {
      for(int indexShi = 0; indexShi < mShiBall.size(); indexShi++) {
       for(int indexGe = 0; indexGe < mGeBall.size(); indexGe++) {
        result.add(mBaiBall.get(indexBai) +  mShiBall.get(indexShi) + mGeBall.get(indexGe));
        mZhushuList.add("1");
       }
      }
     }
     return result;
    }
    public void clearChoose() {
     _initData();
     mBaiBall.clear();
     mShiBall.clear();
     mGeBall.clear();
     mBaiAdapter.notifyDataSetChanged();
     mShiAdapter.notifyDataSetChanged();
     mGeAdapter.notifyDataSetChanged();
     mZhushu = 0;
        ((Fcsd) mActivity).setTouzhuResult(mZhushu);
    }
 
}