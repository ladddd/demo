package com.jincaizi.kuaiwin.buycenter;

import android.app.Activity;
import android.app.Service;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jincaizi.adapters.SscGriViewAdapter;
import com.jincaizi.R;
import com.jincaizi.kuaiwin.tool.SscRandom;
import com.jincaizi.kuaiwin.utils.Constants.SscType;
import com.jincaizi.kuaiwin.utils.Utils;
import com.jincaizi.kuaiwin.widget.MyGridView;

public class Ssc_jx_fragment extends Fragment{
	public static final String TAG_FIVE_ZX = "fivestar_zhixuan";
	public static final String TAG_FIVE_TX = "fivestar_tongxuan";
	public static final String TAG_FIVE_FX = "fivestar_fuxuan";
	
	public static final String TAG_TWO_ZHIXUAN = "twostar_zhixuan";
	public static final String TAG_TWO_FX = "twostar_fuxuan";
	public static final String TAG_TWO_ZHIXUAN_HZ = "twostar_zhixuan_hezhi";
	public static final String TAG_TWO_ZUXUAN = "twostar_zuxuan";
	public static final String TAG_TWO_ZUXUAN_HZ = "twostar_zuxuan_hezhi";
	public static final String TAG_ONE_ZX = "onestar_zhixuan";
	public static final String TAG_DXDS = "dxds";
	public static final String TAG_TWO_ZUXUAN_BH = "twostar_zuxuan_baohao";
	public static final String TAG_FOUR_ZHIXUAN = "fourstar_zhixuan";
	public static final String TAG_FOUR_FUXUAN="fourstar_fuxuan";
	
	public static final String TAG_THREE_ZX = "threedstar_zhixuan";
	public static final String TAG_THREE_FX = "threestar_fuxuan";
	public static final String TAG_THREE_ZX_HEZHI = "threestar_zhixuan_hezhi";
	public static final String TAG_THREE_ZUSAN = "threestar_zusan";
	public static final String TAG_THREE_ZUSAN_BH = "threestar_zusan_baohao";
	public static final String TAG_THREE_ZUSAN_HZ = "threestar_zusan_hezhi";
	public static final String TAG_THREE_ZULIU="threestar_zuliu";
	public static final String TAG_THREE_ZULIU_BH = "threestar_zuliu_baohao";
	public static final String TAG_THREE_ZULIU_HZ = "threestar_zuliu_hezhi";
	
	public static final String TAG_RENXUAN_TWO = "renxuan_two";
	public static final String TAG_RENXUAN_ONE = "renxuan_one";
	
	public static final String BETTYPE ="bettype";
	private String[] mContent = { "0","1", "2", "3", "4", "5", "6", "7", "8",
			"9" };
	private String[] mTwoStarZhixuan_hezhi={"0","1", "2", "3", "4", "5", "6", "7", "8",
			"9","10","11","12","13","14","15","16","17","18"};
	private String[]mTwoStarZuxuan_hezhi={"1", "2", "3", "4", "5", "6", "7", "8",
			"9","10","11","12","13","14","15","16","17"};
	private String[] mDxdsContent = { "大","小", "单", "双"};
	public boolean[] boolWan, boolQian, boolBai, boolShi, boolGe;
	public String mGameType;
	private LinearLayout mLvWan;
	private LinearLayout mLvQian;
	private LinearLayout mLvBai;
	private LinearLayout mLvShi;
	private LinearLayout mLvGe;
	private SscGriViewAdapter mWanAdapter;
	private SscGriViewAdapter mQianAdapter;
	private SscGriViewAdapter mBaiAdapter;
	private SscGriViewAdapter mShiAdapter;
	private SscGriViewAdapter mGeAdapter;
	private MyGridView mWanGV;
	private MyGridView mQianGV;
	private MyGridView mBaiGV;
	private MyGridView mShiGV;
	private MyGridView mGeGV;
	public int mZhushu = 0;
	private SSC_JX mActivity;
	private TextView mGeweiMark;
	private TextView mGeweiHint;
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		Bundle bundle = getArguments();
		mGameType = bundle.getString(BETTYPE);
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mActivity = (SSC_JX)getActivity();
	}
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		_findViews(view);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.pls_fragment_layout, container, false);
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		
		computeZhuShu();
	}


	private void _findViews(View view) {
		mLvWan = (LinearLayout)view.findViewById(R.id.lv_pls_wan);
		mLvQian = (LinearLayout)view.findViewById(R.id.lv_pls_qian);
		mLvBai = (LinearLayout)view.findViewById(R.id.lv_pls_bai);
		mLvShi = (LinearLayout)view.findViewById(R.id.lv_pls_shi);
		mLvGe = (LinearLayout)view.findViewById(R.id.lv_pls_ge);
		mWanGV = (MyGridView)view.findViewById(R.id.pls_wan_ball_group);
		mQianGV = (MyGridView)view.findViewById(R.id.pls_qian_ball_group);
		mBaiGV = (MyGridView)view.findViewById(R.id.pls_bai_ball_group);
		mShiGV = (MyGridView)view.findViewById(R.id.pls_shi_ball_group);
		mGeGV = (MyGridView)view.findViewById(R.id.pls_ge_ball_group);
		mGeweiMark = (TextView)view.findViewById(R.id.gewei_mark);
		mGeweiHint = (TextView)view.findViewById(R.id.gewei_hintview);
		
		WebView mRuleHintView = (WebView)view.findViewById(R.id.ssc_touzhu_rule);
		mRuleHintView.setVisibility(View.VISIBLE);
		 view.setVerticalScrollBarEnabled(false);
        mRuleHintView.loadDataWithBaseURL("",SscType.getJxSscHint(getActivity(), mGameType), "text/html", "utf-8", "");
		//mRuleHintView.setText(SscType.getSscHint(getActivity(), mGameType));
		if(mGameType.equals(SscType.fivestar_zhixuan.toString()) 
				|| mGameType.equals(SscType.fivestar_fuxuan.toString())
				|| mGameType.equals(SscType.fivestar_tongxuan.toString())
				|| mGameType.equals(SscType.renxuan_one.toString())
				|| mGameType.equals(SscType.renxuan_two.toString())) {
			mLvWan.setVisibility(View.VISIBLE);
			mLvQian.setVisibility(View.VISIBLE);
			
			boolWan = _initBool(10);
			mWanAdapter = new SscGriViewAdapter( this,mContent,
					boolWan, mActivity.mCity,4);
			
			boolQian = _initBool(10);
			mQianAdapter = new SscGriViewAdapter(this,mContent,
					boolQian, mActivity.mCity,3);
			
			boolBai = _initBool(10);
			mBaiAdapter = new SscGriViewAdapter(this, mContent,
					boolBai, mActivity.mCity,2);
			
			boolShi = _initBool(10);
			mShiAdapter = new SscGriViewAdapter(this, mContent,
					boolShi, mActivity.mCity,1);
			
			boolGe = _initBool(10);
			mGeAdapter = new SscGriViewAdapter(this, mContent,
					boolGe, mActivity.mCity,0);
			mWanGV.setAdapter(mWanAdapter);
			mQianGV.setAdapter(mQianAdapter);
			mBaiGV.setAdapter(mBaiAdapter);
			mShiGV.setAdapter(mShiAdapter);
			mGeGV.setAdapter(mGeAdapter);
			
		}  else if(mGameType.equals(SscType.fourstar_zhixuan.toString())
				||mGameType.equals(SscType.fourstar_fuxuan.toString())) {
            mLvQian.setVisibility(View.VISIBLE);
			
			boolQian = _initBool(10);
			mQianAdapter = new SscGriViewAdapter(this,mContent,
					boolQian, mActivity.mCity,3);
			
			boolBai = _initBool(10);
			mBaiAdapter = new SscGriViewAdapter(this, mContent,
					boolBai, mActivity.mCity,2);
			
			boolShi = _initBool(10);
			mShiAdapter = new SscGriViewAdapter(this, mContent,
					boolShi, mActivity.mCity,1);
			
			boolGe = _initBool(10);
			mGeAdapter = new SscGriViewAdapter(this, mContent,
					boolGe, mActivity.mCity,0);
			mQianGV.setAdapter(mQianAdapter);
			mBaiGV.setAdapter(mBaiAdapter);
			mShiGV.setAdapter(mShiAdapter);
			mGeGV.setAdapter(mGeAdapter);
		}else if(mGameType.equals(SscType.twostar_fuxuan.toString())
				||mGameType.equals(SscType.twostar_zhixuan.toString())
				||mGameType.equals(SscType.twostar_zuxuan.toString())
				) {
			mLvBai.setVisibility(View.GONE);
			boolShi = _initBool(10);
			mShiAdapter = new SscGriViewAdapter(this, mContent,
					boolShi, mActivity.mCity,1);
			
			boolGe = _initBool(10);
			mGeAdapter = new SscGriViewAdapter(this, mContent,
					boolGe, mActivity.mCity,0);
			mShiGV.setAdapter(mShiAdapter);
			mGeGV.setAdapter(mGeAdapter);
		} else if(mGameType.equals(SscType.twostar_zhixuan_hezhi.toString())) {
			mLvBai.setVisibility(View.GONE);
			mLvShi.setVisibility(View.GONE);
			mGeweiHint.setVisibility(View.GONE);
			mGeweiMark.setVisibility(View.GONE);
			boolGe = _initBool(19);
			mGeAdapter = new SscGriViewAdapter(this, mTwoStarZhixuan_hezhi,
					boolGe, mActivity.mCity,0);
			mGeGV.setAdapter(mGeAdapter);
		} else if(mGameType.equals(SscType.twostar_zuxuan_hezhi.toString())) {
			mLvBai.setVisibility(View.GONE);
			mLvShi.setVisibility(View.GONE);
			mGeweiHint.setVisibility(View.GONE);
			mGeweiMark.setVisibility(View.GONE);
			boolGe = _initBool(17);
			mGeAdapter = new SscGriViewAdapter(this, mTwoStarZuxuan_hezhi,
					boolGe, mActivity.mCity,0);
			mGeGV.setAdapter(mGeAdapter);
		} else if(mGameType.equals(SscType.onestar_zhixuan.toString())
				|| mGameType.equals(SscType.twostar_zuxuan_baohao.toString())
				|| mGameType.equals(SscType.threestar_zuliu_baohao.toString())
				|| mGameType.equals(SscType.threestar_zusan_baohao.toString())
				) {
			mLvBai.setVisibility(View.GONE);
			mLvShi.setVisibility(View.GONE);
			if(mGameType.equals(SscType.twostar_zuxuan_baohao.toString())) {
				mGeweiHint.setVisibility(View.GONE);
				mGeweiMark.setVisibility(View.GONE);
			}
			boolGe = _initBool(10);
			mGeAdapter = new SscGriViewAdapter(this, mContent,
					boolGe, mActivity.mCity,0);
			mGeGV.setAdapter(mGeAdapter);
		} else if(mGameType.equals(SscType.dxds.toString())) {
			mLvBai.setVisibility(View.GONE);
			boolShi = _initBool(4);
			mShiAdapter = new SscGriViewAdapter(this, mDxdsContent,
					boolShi, mActivity.mCity,1);
			
			boolGe = _initBool(4);
			mGeAdapter = new SscGriViewAdapter(this, mDxdsContent,
					boolGe, mActivity.mCity,0);
			mShiGV.setAdapter(mShiAdapter);
			mGeGV.setAdapter(mGeAdapter);
		} else if(mGameType.equals(SscType.threestar_fuxuan.toString())
				|| mGameType.equals(SscType.threestar_zhixuan.toString())
				|| mGameType.equals(SscType.threestar_zusan.toString())
				|| mGameType.equals(SscType.threestar_zuliu.toString())
				){
			boolBai = _initBool(10);
			mBaiAdapter = new SscGriViewAdapter(this, mContent,
					boolBai, mActivity.mCity,2);	
			boolShi = _initBool(10);
           // if(mGameType.equals(SscType.threestar_zusan.toString())) {
            //	mShiAdapter = new SscGriViewAdapter(this, mContent,
            //			boolBai, mActivity.mCity,1);
			//} else {
				mShiAdapter = new SscGriViewAdapter(this, mContent,
						boolShi, mActivity.mCity,1);
			//}
			
			boolGe = _initBool(10);
			mGeAdapter = new SscGriViewAdapter(this, mContent,
					boolGe, mActivity.mCity,0);
			mBaiGV.setAdapter(mBaiAdapter);
			mShiGV.setAdapter(mShiAdapter);
			mGeGV.setAdapter(mGeAdapter);
		} else if(mGameType.equals(SscType.threestar_zhixuan_hezhi.toString())){
			//size = 28(0-27)
			
			mLvBai.setVisibility(View.GONE);
			mLvShi.setVisibility(View.GONE);
			mGeweiHint.setVisibility(View.GONE);
			mGeweiMark.setVisibility(View.GONE);
			boolGe = _initBool(28);
			mGeAdapter = new SscGriViewAdapter(this, mThreeStarZhixuan_hezhi,
					boolGe, mActivity.mCity,0);
			mGeGV.setAdapter(mGeAdapter);
		}else if(mGameType.equals(SscType.threestar_zusan_hezhi.toString())){
			//size = 26(1-26)
			
			mLvBai.setVisibility(View.GONE);
			mLvShi.setVisibility(View.GONE);
			mGeweiHint.setVisibility(View.GONE);
			mGeweiMark.setVisibility(View.GONE);
			boolGe = _initBool(26);
			mGeAdapter = new SscGriViewAdapter(this, mThreeStarZusan_hezhi,
					boolGe, mActivity.mCity,0);
			mGeGV.setAdapter(mGeAdapter);
		}else if(mGameType.equals(SscType.threestar_zuliu_hezhi.toString())){
			//size = 22(3-24)
			
			mLvBai.setVisibility(View.GONE);
			mLvShi.setVisibility(View.GONE);
			mGeweiHint.setVisibility(View.GONE);
			mGeweiMark.setVisibility(View.GONE);
			boolGe = _initBool(24);
			mGeAdapter = new SscGriViewAdapter(this, mThreeStarZuliu_hezhi,
					boolGe, mActivity.mCity,0);
			mGeGV.setAdapter(mGeAdapter);
		}
	}
	private String[]mThreeStarZuliu_hezhi={ "3", "4", "5", "6", "7", "8",
			"9","10","11","12","13","14","15","16","17", "18",
			"19","20","21","22","23","24"};
	private String[]mThreeStarZusan_hezhi={"1", "2", "3", "4", "5", "6", "7", "8",
			"9","10","11","12","13","14","15","16","17", "18",
			"19","20","21","22","23","24","25","26"};
	private String[]mThreeStarZhixuan_hezhi={"0","1", "2", "3", "4", "5", "6", "7", "8",
			"9","10","11","12","13","14","15","16","17", "18",
			"19","20","21","22","23","24","25","26","27"};
	private boolean[] _initBool(int size) {
		boolean[]bool = new boolean[size];
    	for(int i=0; i<size; i++) {
    		bool[i] = false;
    	}
    	return bool;
    }
	public void computeZhuShu() {
		if(mGameType.equals(SscType.fivestar_zhixuan.toString()) 
				|| mGameType.equals(SscType.fivestar_fuxuan.toString())
				|| mGameType.equals(SscType.fivestar_tongxuan.toString())) {
			int wanSelectNum = 0;
			int qianSelectNum = 0;
			int baiSelectNum = 0;
			int shiSelectNum = 0;
			int geSelectNum = 0;
			for(int i=0; i<10; i++) {
				if(boolWan[i]) {
					wanSelectNum++;
				}
				if(boolQian[i]) {
					qianSelectNum++;
				}
				if(boolBai[i]) {
					baiSelectNum++;
				}
				if(boolShi[i]) {
					shiSelectNum++;
				}
				if(boolGe[i]) {
					geSelectNum++;
				}
			}
			if(mGameType.equals(SscType.fivestar_fuxuan.toString())) {
				mZhushu = wanSelectNum*qianSelectNum*baiSelectNum*shiSelectNum*geSelectNum*5;
			} else {
				 mZhushu = wanSelectNum*qianSelectNum*baiSelectNum*shiSelectNum*geSelectNum;
			}
		}  else if(mGameType.equals(SscType.fourstar_zhixuan.toString()) 
				|| mGameType.equals(SscType.fourstar_fuxuan.toString())) {
			int qianSelectNum = 0;
			int baiSelectNum = 0;
			int shiSelectNum = 0;
			int geSelectNum = 0;
			for(int i=0; i<10; i++) {
				if(boolQian[i]) {
					qianSelectNum++;
				}
				if(boolBai[i]) {
					baiSelectNum++;
				}
				if(boolShi[i]) {
					shiSelectNum++;
				}
				if(boolGe[i]) {
					geSelectNum++;
				}
			}
			if(mGameType.equals(SscType.fourstar_fuxuan.toString())) {
				mZhushu = qianSelectNum*baiSelectNum*shiSelectNum*geSelectNum*4;
			} else {
				 mZhushu = qianSelectNum*baiSelectNum*shiSelectNum*geSelectNum;
			}
		}
		
		else if(mGameType.equals(SscType.twostar_fuxuan.toString())
				||mGameType.equals(SscType.twostar_zhixuan.toString())) {
			int shiSelectNum = 0;
			int geSelectNum = 0;
			for(int i=0; i<10; i++) {
				if(boolShi[i]) {
					shiSelectNum++;
				}
				if(boolGe[i]) {
					geSelectNum++;
				}
			}
			if(mGameType.equals(SscType.twostar_fuxuan.toString())) {
				mZhushu = shiSelectNum*geSelectNum*2;
			} else {
			   mZhushu = shiSelectNum*geSelectNum;
			}
		} else if(mGameType.equals(SscType.twostar_zuxuan.toString())) {
			int count = 0;
			for(int i=0; i<10; i++) {
				for(int j=0; j<10; j++) {
					if(i !=j && boolShi[i] && boolGe[j]) {
						count++;
					}
				}
			}
			mZhushu = count;
		} else if(mGameType.equals(SscType.twostar_zhixuan_hezhi.toString())) {
			int geSelectNum = 0;
			for(int i=0; i<19; i++) {
				if(boolGe[i]) {
					geSelectNum+=_computeTwoStarZhiHZ(Integer.parseInt(mTwoStarZhixuan_hezhi[i]));
				}
			}
			mZhushu = geSelectNum;
		}else if(mGameType.equals(SscType.twostar_zuxuan_hezhi.toString())) {
			int geSelectNum = 0;
			for(int i=0; i<17; i++) {
				if(boolGe[i]) {
					geSelectNum+=_computeTwoStarZuHZ(Integer.parseInt(mTwoStarZuxuan_hezhi[i]));
				}
			}
			mZhushu = geSelectNum;
		} else if(mGameType.equals(SscType.twostar_zuxuan_baohao.toString())) {
			int geSelectNum = 0;
			for(int i=0; i<10; i++) {
				if(boolGe[i]) {
					geSelectNum++;
				}
			}
			mZhushu = Utils.getZuHeNum(geSelectNum, 2);;
		}else if(mGameType.equals(SscType.onestar_zhixuan.toString())) {
			int geSelectNum = 0;
			for(int i=0; i<10; i++) {
				if(boolGe[i]) {
					geSelectNum++;
				}
			}
			mZhushu = geSelectNum;
		} else if(mGameType.equals(SscType.dxds.toString())) {
			
			int shiSelectNum = 0;
			int geSelectNum = 0;
			for(int i=0; i<4; i++) {
				if(boolShi[i]) {
					shiSelectNum++;
				}
				if(boolGe[i]) {
					geSelectNum++;
				}
			}
			mZhushu = shiSelectNum*geSelectNum;
		} else if(mGameType.equals(SscType.threestar_fuxuan.toString())
				|| mGameType.equals(SscType.threestar_zhixuan.toString())){
			int baiSelectNum = 0;
			int shiSelectNum = 0;
			int geSelectNum = 0;
			for(int i=0; i<10; i++) {
				if(boolBai[i]) {
					baiSelectNum++;
				}
				if(boolShi[i]) {
					shiSelectNum++;
				}
				if(boolGe[i]) {
					geSelectNum++;
				}
			}
			if(mGameType.equals(SscType.threestar_fuxuan.toString())) {
				mZhushu = baiSelectNum*shiSelectNum*geSelectNum*3;
			} else {
			mZhushu = baiSelectNum*shiSelectNum*geSelectNum;
			}
		}else if(mGameType.equals(SscType.threestar_zhixuan_hezhi.toString())) {
			int geSelectNum = 0;
			for(int i=0; i<zushuOfZhixuanHezhi.length; i++) {
				if(boolGe[i]) {
					geSelectNum += zushuOfZhixuanHezhi[i];
				}
			}
			mZhushu = geSelectNum;
		} else if(mGameType.equals(SscType.threestar_zusan.toString())) {//zusan
			int baiSelectNum = 0;
			int shiSelectNum = 0;
			int geSelectNum = 0;
			for(int i=0; i<10; i++) {
				if(boolBai[i]) {
					baiSelectNum++;
				}
				if(boolShi[i]) {
					shiSelectNum++;
				}
				if(boolGe[i]) {
					geSelectNum++;
				}
			}
			if(baiSelectNum != 0 && shiSelectNum != 0 && geSelectNum != 0) {
			mZhushu = 1;
			} else {
				mZhushu = 1;
			}
		}else if(mGameType.equals(SscType.threestar_zusan_baohao.toString())) {//zusan baohao
			int geSelectNum = 0;
			for(int i=0; i<10; i++) {
				if(boolGe[i]) {
					geSelectNum++;
				}
			}
			mZhushu = geSelectNum*(geSelectNum-1);
		}else if(mGameType.equals(SscType.threestar_zusan_hezhi.toString())) {//zusan hezhi
			int geSelectNum = 0;
			for(int i=0; i<zushuOfZusanHezhi.length; i++) {
				if(boolGe[i]) {
					geSelectNum += zushuOfZusanHezhi[i];
				}
			}
			mZhushu = geSelectNum;
		}else if(mGameType.equals(SscType.threestar_zuliu.toString())) {//zuliu
			int baiSelectNum = 0;
			int shiSelectNum = 0;
			int geSelectNum = 0;
			for(int i=0; i<10; i++) {
				if(boolBai[i]) {
					baiSelectNum++;
				}
				if(boolShi[i]) {
					shiSelectNum++; 
				}
				if(boolGe[i]) {
					geSelectNum++;
				}
			}
			if(baiSelectNum != 0 && shiSelectNum != 0 && geSelectNum != 0) {
			mZhushu = 1;
			} else {
				mZhushu = 1;
			}
		}else if(mGameType.equals(SscType.threestar_zuliu_baohao.toString())) {//zuliu baohao
			int geSelectNum = 0;
			for(int i=0; i<10; i++) {
				if(boolGe[i]) {
					geSelectNum++;
				}
			}
			mZhushu = Utils.getZuHeNum(geSelectNum, 3);
		}else if(mGameType.equals(SscType.threestar_zuliu_hezhi.toString())) {//zuliu hezhi
			int geSelectNum = 0;
			for(int i=0; i<zushuOfZuliuHezhi.length; i++) {
				if(boolGe[i]) {
					geSelectNum += zushuOfZuliuHezhi[i];
				}
			}
			mZhushu = geSelectNum;
		} else if(mGameType.equals(SscType.renxuan_two.toString())) {
			int wanSelectNum = 0;
			int qianSelectNum = 0;
			int baiSelectNum = 0;
			int shiSelectNum = 0;
			int geSelectNum = 0;
			for(int i=0; i<10; i++) {
				if(boolWan[i]) {
					wanSelectNum++;
				}
				if(boolQian[i]) {
					qianSelectNum++;
				}
				if(boolBai[i]) {
					baiSelectNum++;
				}
				if(boolShi[i]) {
					shiSelectNum++;
				}
				if(boolGe[i]) {
					geSelectNum++;
				}
			}
			mZhushu = Utils.getZuHeNum(wanSelectNum+qianSelectNum+baiSelectNum+shiSelectNum+geSelectNum, 2)
					- Utils.getZuHeNum(wanSelectNum, 2) - Utils.getZuHeNum(qianSelectNum, 2)
					- Utils.getZuHeNum(baiSelectNum, 2) - Utils.getZuHeNum(shiSelectNum, 2)
					- Utils.getZuHeNum(geSelectNum, 2);
		} else if(mGameType.equals(SscType.renxuan_one.toString())) {
			int geSelectNum = 0;
			for(int i=0; i<10; i++) {
				if(boolWan[i]) {
					geSelectNum++;
				}
				if(boolQian[i]) {
					geSelectNum++;
				}
				if(boolBai[i]) {
					geSelectNum++;
				}
				if(boolShi[i]) {
					geSelectNum++;
				}
				if(boolGe[i]) {
					geSelectNum++;
				}
			}
			mZhushu =geSelectNum;
		}
		mActivity.setFooterBetValues(mZhushu);
	}
	private int[] zushuOfZhixuanHezhi = {1,3, 6, 10, 15, 21,28, 36, 45,
			55,63,69,73,75,75,73,69,63, 55,
			45,36,28,21,15,10,6,3,1};
	private int[]zushuOfZuliuHezhi={ 1, 1, 2, 3, 4, 5,
			7,8,9,10,10,10,10,9,8, 7,
			5,4,3,2,1,1};
	private int[]zushuOfZusanHezhi={1, 2, 1, 3, 3, 3, 4, 5,
			4,5,5,4,5,5,4,5,5, 4,
			5,4,3,3,3,1,2,1};
	private int _computeTwoStarZhiHZ(int sum) {
		int count = 0;
		for(int i=0; i<10; i++) {
			for(int j=0; j<10; j++) {
				if(i+j==sum) {
					count++;
				}
			}
		}
		return count;
	}
	private int _computeTwoStarZuHZ(int sum) {
		int count = 0;
		for(int i=0; i<10; i++) {
			for(int j=0; j<10; j++) {
				if(i+j==sum && i<j) {
					count++;
				}
			}
		}
		return count;
	}
	public void clearSelectResult() {
		if(boolWan != null) {
			for(int i=0; i<boolWan.length; i++) {
				boolWan[i] = false;
			}
			mWanAdapter.notifyDataSetChanged();
		}
		if(boolQian != null) {
			for(int i=0; i<boolQian.length; i++) {
				boolQian[i] = false;
			}
			mQianAdapter.notifyDataSetChanged();
		}
		if(boolBai != null) {
			for(int i=0; i<boolBai.length; i++) {
				boolBai[i] = false;
			}
			mBaiAdapter.notifyDataSetChanged();
		}
		if(boolShi != null) {
			for(int i=0; i<boolShi.length; i++) {
				boolShi[i] = false;
			}
			mShiAdapter.notifyDataSetChanged();
		}
		if(boolGe != null) {
			for(int i=0; i<boolGe.length; i++) {
				boolGe[i] = false;
			}
			mGeAdapter.notifyDataSetChanged();
		}
		computeZhuShu();
	}
	
	public void updateBallData() {
		Vibrator vibrator = (Vibrator) getActivity().getApplication()
				.getSystemService(Service.VIBRATOR_SERVICE);
		vibrator.vibrate(new long[] { 0, 200 }, -1);
		if(mGameType.equals(SscType.fivestar_zhixuan.toString()) 
				|| mGameType.equals(SscType.fivestar_fuxuan.toString())
				|| mGameType.equals(SscType.fivestar_tongxuan.toString())) {
			for(int i=0; i<boolGe.length; i++) {
				boolWan[i] = false;
				boolQian[i] = false;
				boolBai[i] = false;
				boolShi[i] = false;
				boolGe[i] = false;
			}
			int[]result = SscRandom.getFiveStar();	
			boolWan[result[0]] = true;
			
			boolQian[result[1]] = true;
			
			boolBai[result[2]] = true;
			
			boolShi[result[3]] = true;
			
			boolGe[result[4]] = true;
			mWanAdapter.notifyDataSetChanged();
			mQianAdapter.notifyDataSetChanged();
			mBaiAdapter.notifyDataSetChanged();
			mShiAdapter.notifyDataSetChanged();
			mGeAdapter.notifyDataSetChanged();
			
		} else if(mGameType.equals(SscType.twostar_fuxuan.toString())
				||mGameType.equals(SscType.twostar_zhixuan.toString())) {
			for(int i=0; i<boolGe.length; i++) {
				boolShi[i] = false;
				boolGe[i] = false;
			}
			int[]result = SscRandom.getTwoStarSame();
            boolShi[result[0]] = true;
			boolGe[result[1]] = true;
			mShiAdapter.notifyDataSetChanged();
			mGeAdapter.notifyDataSetChanged();
		} else if(mGameType.equals(SscType.twostar_zuxuan.toString())) {
			for(int i=0; i<boolGe.length; i++) {
				boolShi[i] = false;
				boolGe[i] = false;
			}
			int[]result = SscRandom.getTwoStarDiff();
            boolShi[result[0]] = true;
			boolGe[result[1]] = true;
			mShiAdapter.notifyDataSetChanged();
			mGeAdapter.notifyDataSetChanged();
		}else if(mGameType.equals(SscType.twostar_zhixuan_hezhi.toString())) {
			for(int i=0; i<boolGe.length; i++) {
				boolGe[i] = false;
			}
			boolGe[SscRandom.getTwoStarZhiHZ()] = true;
			mGeAdapter.notifyDataSetChanged();
		} else if(mGameType.equals(SscType.twostar_zuxuan_hezhi.toString())) {
			for(int i=0; i<boolGe.length; i++) {
				boolGe[i] = false;
			}
			boolGe[SscRandom.getTwoStarZuHZ()-1] = true;
			mGeAdapter.notifyDataSetChanged();
		}else if(mGameType.equals(SscType.twostar_zuxuan_baohao.toString())) {
			for(int i=0; i<boolGe.length; i++) {
				boolGe[i] = false;
			}
			int[]result = SscRandom.getTwoStarDiff();
			boolGe[result[0]] = true;
			boolGe[result[1]] = true;
			mGeAdapter.notifyDataSetChanged();
		}else if(mGameType.equals(SscType.onestar_zhixuan.toString())) {
			for(int i=0; i<boolGe.length; i++) {
				boolGe[i] = false;
			}
			boolGe[SscRandom.getOneStar()] = true;
			mGeAdapter.notifyDataSetChanged();
		} else if(mGameType.equals(SscType.dxds.toString())) {
			for(int i=0; i<boolGe.length; i++) {
				boolShi[i] = false;
				boolGe[i] = false;
			}
			int[]result = SscRandom.getDXDS();
			boolShi[result[0]] = true;
			boolGe[result[1]] = true;
			mShiAdapter.notifyDataSetChanged();
			mGeAdapter.notifyDataSetChanged();
		} else if(mGameType.equals(SscType.threestar_fuxuan.toString())
				|| mGameType.equals(SscType.threestar_zhixuan.toString())){
			for(int i=0; i<boolGe.length; i++) {
				boolBai[i] = false;
				boolShi[i] = false;
				boolGe[i] = false;
			}
			int[]result = SscRandom.getThreeStar();	
			boolBai[result[0]] = true;
            boolShi[result[1]] = true;
			boolGe[result[2]] = true;
			mBaiAdapter.notifyDataSetChanged();
			mShiAdapter.notifyDataSetChanged();
			mGeAdapter.notifyDataSetChanged();
		} else if(mGameType.equals(SscType.threestar_zhixuan_hezhi.toString())) {
			for(int i=0; i<boolGe.length; i++) {
				boolGe[i] = false;
			}
			boolGe[SscRandom.getThreeStarHZOfZhixuan()] = true;
			mGeAdapter.notifyDataSetChanged();
		} else if(mGameType.equals(SscType.threestar_zusan_hezhi.toString())) {
			for(int i=0; i<boolGe.length; i++) {
				boolGe[i] = false;
			}
			boolGe[SscRandom.getThreeStarHZOfZusan()] = true;
			mGeAdapter.notifyDataSetChanged();
		} else if(mGameType.equals(SscType.threestar_zuliu_hezhi.toString())) {
			for(int i=0; i<boolGe.length; i++) {
				boolGe[i] = false;
			}
			boolGe[SscRandom.getThreeStarHZOfZuliu()] = true;
			mGeAdapter.notifyDataSetChanged();
		} else if(mGameType.equals(SscType.threestar_zusan_baohao.toString())) {
			for(int i=0; i<boolGe.length; i++) {
				boolGe[i] = false;
			}
			int[] result = SscRandom.getThreeStarBHOfZusan();
			boolGe[result[0]] = true;
			boolGe[result[1]] = true;
			mGeAdapter.notifyDataSetChanged();
		} else if(mGameType.equals(SscType.threestar_zuliu_baohao.toString())) {
			for(int i=0; i<boolGe.length; i++) {
				boolGe[i] = false;
			}
			int[] result = SscRandom.getThreeStarBHOfZuliu();
			boolGe[result[0]] = true;
			boolGe[result[1]] = true;
			boolGe[result[2]] = true;
			mGeAdapter.notifyDataSetChanged();
		}else if(mGameType.equals(SscType.threestar_zuliu.toString())) {
			for(int i=0; i<boolGe.length; i++) {
				boolBai[i] = false;
				boolShi[i] = false;
				boolGe[i] = false;
			}
			int[]result = SscRandom.getThreeStarOfZuliu();	
			boolBai[result[0]] = true;
            boolShi[result[1]] = true;
			boolGe[result[2]] = true;
			mBaiAdapter.notifyDataSetChanged();
			mShiAdapter.notifyDataSetChanged();
			mGeAdapter.notifyDataSetChanged();
		}else if(mGameType.equals(SscType.threestar_zusan.toString())) {
			for(int i=0; i<boolGe.length; i++) {
				boolBai[i] = false;
				boolShi[i] = false;
				boolGe[i] = false;
			}
			int[]result = SscRandom.getThreeStarOfZusan();	
			boolBai[result[0]] = true;
            boolShi[result[0]] = true;
			boolGe[result[1]] = true;
			mBaiAdapter.notifyDataSetChanged();
			mShiAdapter.notifyDataSetChanged();
			mGeAdapter.notifyDataSetChanged();
		} else if(mGameType.equals(SscType.fourstar_fuxuan.toString())
				||mGameType.equals(SscType.fourstar_zhixuan.toString())) {
			for(int i=0; i<boolGe.length; i++) {
				boolQian[i] = false;
				boolBai[i] = false;
				boolShi[i] = false;
				boolGe[i] = false;
			}
			int[]result = SscRandom.getFourStar();	
			boolQian[result[0]] = true;
			
			boolBai[result[1]] = true;
			
			boolShi[result[2]] = true;
			
			boolGe[result[3]] = true;
			mQianAdapter.notifyDataSetChanged();
			mBaiAdapter.notifyDataSetChanged();
			mShiAdapter.notifyDataSetChanged();
			mGeAdapter.notifyDataSetChanged();
		} else if(mGameType.equals(SscType.renxuan_two.toString())) {
			int[] result = SscRandom.getRenXuanTwo();
			for(int i=0; i<boolGe.length; i++) {
				boolWan[i] = false;
				boolQian[i] = false;
				boolBai[i] = false;
				boolShi[i] = false;
				boolGe[i] = false;
			}
			switch(result[0]) {
			case 0:
				boolWan[result[2]] = true;
				break;
			case 1:
				boolQian[result[2]] = true;
				break;
			case 2:
				boolBai[result[2]] = true;
				break;
			case 3:
				boolShi[result[2]] = true;
				break;
			case 4:
				boolGe[result[2]] = true;
				break;
			}
			switch(result[1]) {
			case 0:
				boolWan[result[3]] = true;
				break;
			case 1:
				boolQian[result[3]] = true;
				break;
			case 2:
				boolBai[result[3]] = true;
				break;
			case 3:
				boolShi[result[3]] = true;
				break;
			case 4:
				boolGe[result[3]] = true;
				break;
			}
			mWanAdapter.notifyDataSetChanged();
			mQianAdapter.notifyDataSetChanged();
			mBaiAdapter.notifyDataSetChanged();
			mShiAdapter.notifyDataSetChanged();
			mGeAdapter.notifyDataSetChanged();
		} else if(mGameType.equals(SscType.renxuan_one.toString())) {
			int[] result = SscRandom.getRenXuanOne();
			for(int i=0; i<boolGe.length; i++) {
				boolWan[i] = false;
				boolQian[i] = false;
				boolBai[i] = false;
				boolShi[i] = false;
				boolGe[i] = false;
			}
			switch(result[0]) {
			case 0:
				boolWan[result[1]] = true;
				break;
			case 1:
				boolQian[result[1]] = true;
				break;
			case 2:
				boolBai[result[1]] = true;
				break;
			case 3:
				boolShi[result[1]] = true;
				break;
			case 4:
				boolGe[result[1]] = true;
				break;
			}
			mWanAdapter.notifyDataSetChanged();
			mQianAdapter.notifyDataSetChanged();
			mBaiAdapter.notifyDataSetChanged();
			mShiAdapter.notifyDataSetChanged();
			mGeAdapter.notifyDataSetChanged();
		}
		computeZhuShu();
	}
	public void threeZusanNotifyAdapter() {
		mBaiAdapter.notifyDataSetChanged();
		mShiAdapter.notifyDataSetChanged();
		mGeAdapter.notifyDataSetChanged();
	}
	@Override
	public void onHiddenChanged(boolean hidden) {
		if(!hidden) {
			computeZhuShu();
		}
	}
	
	public String betResult = "";
	public void  getBetResult() {
		if(mGameType.equals(SscType.fivestar_zhixuan.toString()) 
				|| mGameType.equals(SscType.fivestar_fuxuan.toString())
				|| mGameType.equals(SscType.fivestar_tongxuan.toString())) {
			StringBuilder wBuilder = new StringBuilder();
			StringBuilder qBuilder = new StringBuilder();
			StringBuilder bBuilder = new StringBuilder();
			StringBuilder sBuilder = new StringBuilder();
			StringBuilder gBuilder = new StringBuilder();
			for(int i=0; i<boolGe.length; i++) {
				if(boolWan[i]) {
					wBuilder.append(i + " ");
				}
				if(boolQian[i]) { 
					qBuilder.append(i + " ");
				}
				if(boolBai[i]) {
					bBuilder.append(i + " ");
				}
				if(boolShi[i]) {
					sBuilder.append(i + " ");
				}
				if(boolGe[i]) {
					gBuilder.append(i + " ");
				}
			}
			betResult = wBuilder.toString().trim().replace(" ", ",") + "|"+ qBuilder.toString().trim().replace(" ", ",") +"|" 
			+bBuilder.toString().trim().replace(" ", ",") + "|" +sBuilder.toString().trim().replace(" ", ",")
			+ "|" +gBuilder.toString().trim().replace(" ", ",");
			
		} else if(mGameType.equals(SscType.twostar_fuxuan.toString())
				||mGameType.equals(SscType.twostar_zhixuan.toString())) {
			StringBuilder sBuilder = new StringBuilder();
			StringBuilder gBuilder = new StringBuilder();
			for(int i=0; i<boolGe.length; i++) {
				if(boolShi[i]) {
					sBuilder.append(i+ " ");
				}
				if(boolGe[i]) {
					gBuilder.append(i+ " ");
				}
			}
			betResult = sBuilder.toString().trim().replace(" ", ",") + "|" +gBuilder.toString().trim().replace(" ", ",");
		} else if(mGameType.equals(SscType.twostar_zuxuan.toString())) {
			StringBuilder sBuilder = new StringBuilder();
			StringBuilder gBuilder = new StringBuilder();
			for(int i=0; i<boolGe.length; i++) {
				if(boolShi[i]) {
					sBuilder.append(i+ " ");
				}
				if(boolGe[i]) {
					gBuilder.append(i + " ");
				}
			}
			betResult = sBuilder.toString().trim().replace(" ", ",") + "|" +gBuilder.toString().trim().replace(" ", ",");
		}else if(mGameType.equals(SscType.twostar_zhixuan_hezhi.toString())) {
			StringBuilder gBuilder = new StringBuilder();
			for(int i=0; i<boolGe.length; i++) {
				if(boolGe[i]) {
					gBuilder.append(i + " ");
				}
			}
			betResult = gBuilder.toString().trim().replace(" ", ",");
		} else if(mGameType.equals(SscType.twostar_zuxuan_hezhi.toString())) {
			StringBuilder gBuilder = new StringBuilder();
			for(int i=0; i<boolGe.length; i++) {
				if(boolGe[i]) {
					gBuilder.append((i+1) + " ");
				}
			}
			betResult = gBuilder.toString().trim().replace(" ", ",");
		}else if(mGameType.equals(SscType.twostar_zuxuan_baohao.toString())) {
			StringBuilder gBuilder = new StringBuilder();
			for(int i=0; i<boolGe.length; i++) {
				if(boolGe[i]) {
					gBuilder.append(i + " ");
				}
			}
			betResult = gBuilder.toString().trim().replace(" ", ",");
		}else if(mGameType.equals(SscType.onestar_zhixuan.toString())) {
			StringBuilder gBuilder = new StringBuilder();
			for(int i=0; i<boolGe.length; i++) {
				if(boolGe[i]) {
					gBuilder.append(i + " ");
				}
			}
			betResult = gBuilder.toString().trim().replace(" ", ",");
		} else if(mGameType.equals(SscType.dxds.toString())) {
			StringBuilder sBuilder = new StringBuilder();
			StringBuilder gBuilder = new StringBuilder();
			for(int i=0; i<boolGe.length; i++) {
				if(boolShi[i]) {
					sBuilder.append(mDxdsContent[i] + " ");
				}
				if(boolGe[i]) {
					gBuilder.append(mDxdsContent[i] + " ");
				}
			}
			betResult = sBuilder.toString().trim().replace(" ", ",") + "|" +gBuilder.toString().trim().replace(" ", ",");
		} else if(mGameType.equals(SscType.threestar_fuxuan.toString())
				|| mGameType.equals(SscType.threestar_zhixuan.toString())){
			StringBuilder bBuilder = new StringBuilder();
			StringBuilder sBuilder = new StringBuilder();
			StringBuilder gBuilder = new StringBuilder();
			for(int i=0; i<boolGe.length; i++) {
				if(boolBai[i]) {
					bBuilder.append(i+ " ");
				}
				if(boolShi[i]) {
					sBuilder.append(i+ " ");
				}
				if(boolGe[i]) {
					gBuilder.append(i + " ");
				}
			}
			betResult = bBuilder.toString().trim().replace(" ", ",") + "|" +sBuilder.toString().trim().replace(" ", ",") + "|" +gBuilder.toString().trim().replace(" ", ",");
		} else if(mGameType.equals(SscType.threestar_zhixuan_hezhi.toString())) {
			StringBuilder gBuilder = new StringBuilder();
			for(int i=0; i<boolGe.length; i++) {
				if(boolGe[i]) {
					gBuilder.append(i + " ");
				}
			}
			betResult = gBuilder.toString().trim().replace(" ", ",");
		} else if(mGameType.equals(SscType.threestar_zusan_hezhi.toString())) {
			StringBuilder gBuilder = new StringBuilder();
			for(int i=0; i<boolGe.length; i++) {
				if(boolGe[i]) {
					gBuilder.append((i+1) + " ");
				}
			}
			betResult = gBuilder.toString().trim().replace(" ", ",");
		} else if(mGameType.equals(SscType.threestar_zuliu_hezhi.toString())) {
			StringBuilder gBuilder = new StringBuilder();
			for(int i=0; i<boolGe.length; i++) {
				if(boolGe[i]) {
					gBuilder.append((i+3) + " ");
				}
			}
			betResult = gBuilder.toString().trim().replace(" ", ",");
		} else if(mGameType.equals(SscType.threestar_zusan_baohao.toString())) {
			StringBuilder gBuilder = new StringBuilder();
			for(int i=0; i<boolGe.length; i++) {
				if(boolGe[i]) {
					gBuilder.append(i+ " ");
				}
			}
			betResult = gBuilder.toString().trim().replace(" ", ",");
		} else if(mGameType.equals(SscType.threestar_zuliu_baohao.toString())) {
			StringBuilder gBuilder = new StringBuilder();
			for(int i=0; i<boolGe.length; i++) {
				if(boolGe[i]) {
					gBuilder.append(i+ " ");
				}
			}
			betResult = gBuilder.toString().trim().replace(" ", ",");
		}else if(mGameType.equals(SscType.threestar_zuliu.toString())) {
			StringBuilder bBuilder = new StringBuilder();
			StringBuilder sBuilder = new StringBuilder();
			StringBuilder gBuilder = new StringBuilder();
			for(int i=0; i<boolGe.length; i++) {
				if(boolBai[i]) {
					bBuilder.append(i+ " ");
				}
				if(boolShi[i]) {
					sBuilder.append(i + " ");
				}
				if(boolGe[i]) {
					gBuilder.append(i + " ");
				}
			}
			betResult = bBuilder.toString().trim().replace(" ", ",") + "|" +sBuilder.toString().trim().replace(" ", ",") + "|" +gBuilder.toString().trim().replace(" ", ",");
		}else if(mGameType.equals(SscType.threestar_zusan.toString())) {
			StringBuilder bBuilder = new StringBuilder();
			StringBuilder sBuilder = new StringBuilder();
			StringBuilder gBuilder = new StringBuilder();
			for(int i=0; i<boolGe.length; i++) {
				if(boolBai[i]) {
					bBuilder.append(i+ " ");
				}
				if(boolShi[i]) {
					sBuilder.append(i + " ");
				}
				if(boolGe[i]) {
					gBuilder.append(i + " ");
				}
			}
			betResult = bBuilder.toString().trim().replace(" ", ",") + "|" +sBuilder.toString().trim().replace(" ", ",") + "|" +gBuilder.toString().trim().replace(" ", ",");
		} else if(mGameType.equals(SscType.fourstar_fuxuan.toString())
				||mGameType.equals(SscType.fourstar_zhixuan.toString())) {
			StringBuilder qBuilder = new StringBuilder();
			StringBuilder bBuilder = new StringBuilder();
			StringBuilder sBuilder = new StringBuilder();
			StringBuilder gBuilder = new StringBuilder();
			for(int i=0; i<boolGe.length; i++) {
				if(boolQian[i]) { 
					qBuilder.append(i+ " ");
				}
				if(boolBai[i]) {
					bBuilder.append(i + " ");
				}
				if(boolShi[i]) {
					sBuilder.append(i + " ");
				}
				if(boolGe[i]) {
					gBuilder.append(i + " ");
				}
			}
			betResult = qBuilder.toString().trim().replace(" ", ",") +"|" +bBuilder.toString().trim().replace(" ", ",") 
					+ "|" +sBuilder.toString().trim().replace(" ", ",") + "|" +gBuilder.toString().trim().replace(" ", ",");
		} else if(mGameType.equals(SscType.renxuan_two.toString())) {
			StringBuilder wBuilder = new StringBuilder();
			StringBuilder qBuilder = new StringBuilder();
			StringBuilder bBuilder = new StringBuilder();
			StringBuilder sBuilder = new StringBuilder();
			StringBuilder gBuilder = new StringBuilder();
			for(int i=0; i<boolGe.length; i++) {
				if(boolWan[i]) {
					wBuilder.append(i + " ");
				}
				if(boolQian[i]) { 
					qBuilder.append(i + " ");
				}
				if(boolBai[i]) {
					bBuilder.append(i +  " ");
				}
				if(boolShi[i]) {
					sBuilder.append(i + " ");
				}
				if(boolGe[i]) {
					gBuilder.append(i + " ");
				}
			}
			if(TextUtils.isEmpty(wBuilder.toString())) {
				wBuilder.append("# ");
			}
			if(TextUtils.isEmpty(qBuilder.toString())) {
				qBuilder.append("# ");
			}
			if(TextUtils.isEmpty(bBuilder.toString())) {
				bBuilder.append("# ");
			}
			if(TextUtils.isEmpty(sBuilder.toString())) {
				sBuilder.append("# ");
			}
			if(TextUtils.isEmpty(gBuilder.toString())) {
				gBuilder.append("# ");
			}
			betResult = wBuilder.toString().trim().replace(" ", ",") + "|"+ qBuilder.toString().trim().replace(" ", ",")
					+"|" +bBuilder.toString().trim().replace(" ", ",") + "|" +sBuilder.toString().trim().replace(" ", ",")
					+ "|" +gBuilder.toString().trim().replace(" ", ",");
		} else if(mGameType.equals(SscType.renxuan_one.toString())) {
			StringBuilder wBuilder = new StringBuilder();
			StringBuilder qBuilder = new StringBuilder();
			StringBuilder bBuilder = new StringBuilder();
			StringBuilder sBuilder = new StringBuilder();
			StringBuilder gBuilder = new StringBuilder();
			for(int i=0; i<boolGe.length; i++) {
				if(boolWan[i]) {
					wBuilder.append(i + " ");
				}
				if(boolQian[i]) { 
					qBuilder.append(i + " ");
				}
				if(boolBai[i]) {
					bBuilder.append(i + " ");
				}
				if(boolShi[i]) {
					sBuilder.append(i + " ");
				}
				if(boolGe[i]) {
					gBuilder.append(i + " ");
				}
			}
			if(TextUtils.isEmpty(wBuilder.toString())) {
				wBuilder.append("#" + " ");
			}
			if(TextUtils.isEmpty(qBuilder.toString())) {
				qBuilder.append("#" + " ");
			}
			if(TextUtils.isEmpty(bBuilder.toString())) {
				bBuilder.append("#" + " ");
			}
			if(TextUtils.isEmpty(sBuilder.toString())) {
				sBuilder.append("#" + " ");
			}
			if(TextUtils.isEmpty(gBuilder.toString())) {
				gBuilder.append("#" + " ");
			}
			betResult = wBuilder.toString().trim().replace(" ", ",") + "|"+ qBuilder.toString().trim().replace(" ", ",")
					+"|" +bBuilder.toString().trim().replace(" ", ",") + "|" +sBuilder.toString().trim().replace(" ", ",")
					+ "|" +gBuilder.toString().trim().replace(" ", ",");
		}
	}
}
