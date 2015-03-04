package com.jincaizi.kuaiwin.buycenter;

import android.app.Activity;
import android.app.Service;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
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

public class Ssc_cq_fragment extends Fragment{
	public static final String TAG_FIVE_ZX = "fivestar_zhixuan";
	public static final String TAG_FIVE_TX = "fivestar_tongxuan";
	public static final String TAG_FIVE_FX = "fivestar_fuxuan";
	public static final String TAG_THREE_ZX = "threedstar_zhixuan";
	public static final String TAG_THREE_FX = "threestar_fuxuan";
	public static final String TAG_TWO_ZHIXUAN = "twostar_zhixuan";
	public static final String TAG_TWO_FX = "twostar_fuxuan";
	public static final String TAG_TWO_ZHIXUAN_HZ = "twostar_zhixuan_hezhi";
	public static final String TAG_TWO_ZUXUAN = "twostar_zuxuan";
	public static final String TAG_TWO_ZUXUAN_HZ = "twostar_zuxuan_hezhi";
	public static final String TAG_ONE_ZX = "onestar_zhixuan";
	public static final String TAG_DXDS = "dxds";
	public static final String TAG_TWO_ZUXUAN_BH = "twostar_zuxuan_baohao";
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
	private SSC_CQ mActivity;
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
		mActivity = (SSC_CQ)getActivity();
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
        mRuleHintView.loadDataWithBaseURL("",SscType.getSscHint(getActivity(), mGameType), "text/html", "utf-8", "");
		//mRuleHintView.setText(SscType.getSscHint(getActivity(), mGameType));
		if(mGameType.equals(SscType.fivestar_zhixuan.toString()) 
				|| mGameType.equals(SscType.fivestar_fuxuan.toString())
				|| mGameType.equals(SscType.fivestar_tongxuan.toString())) {
			mLvWan.setVisibility(View.VISIBLE);
			mLvQian.setVisibility(View.VISIBLE);
			
			boolWan = _initBool(10);
			mWanAdapter = new SscGriViewAdapter( this,mContent,
					boolWan, mActivity.mCity);
			
			boolQian = _initBool(10);
			mQianAdapter = new SscGriViewAdapter(this,mContent,
					boolQian, mActivity.mCity);
			
			boolBai = _initBool(10);
			mBaiAdapter = new SscGriViewAdapter(this, mContent,
					boolBai, mActivity.mCity);
			
			boolShi = _initBool(10);
			mShiAdapter = new SscGriViewAdapter(this, mContent,
					boolShi, mActivity.mCity);
			
			boolGe = _initBool(10);
			mGeAdapter = new SscGriViewAdapter(this, mContent,
					boolGe, mActivity.mCity);
			mWanGV.setAdapter(mWanAdapter);
			mQianGV.setAdapter(mQianAdapter);
			mBaiGV.setAdapter(mBaiAdapter);
			mShiGV.setAdapter(mShiAdapter);
			mGeGV.setAdapter(mGeAdapter);
			
		} else if(mGameType.equals(SscType.twostar_fuxuan.toString())
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
					boolGe, mActivity.mCity);
			mGeGV.setAdapter(mGeAdapter);
		} else if(mGameType.equals(SscType.twostar_zuxuan_hezhi.toString())) {
			mLvBai.setVisibility(View.GONE);
			mLvShi.setVisibility(View.GONE);
			mGeweiHint.setVisibility(View.GONE);
			mGeweiMark.setVisibility(View.GONE);
			boolGe = _initBool(17);
			mGeAdapter = new SscGriViewAdapter(this, mTwoStarZuxuan_hezhi,
					boolGe, mActivity.mCity);
			mGeGV.setAdapter(mGeAdapter);
		}
		else if(mGameType.equals(SscType.onestar_zhixuan.toString())
				|| mGameType.equals(SscType.twostar_zuxuan_baohao.toString())) {
			mLvBai.setVisibility(View.GONE);
			mLvShi.setVisibility(View.GONE);
			if(mGameType.equals(SscType.twostar_zuxuan_baohao.toString())) {
				mGeweiHint.setVisibility(View.GONE);
				mGeweiMark.setVisibility(View.GONE);
			}
			boolGe = _initBool(10);
			mGeAdapter = new SscGriViewAdapter(this, mContent,
					boolGe, mActivity.mCity);
			mGeGV.setAdapter(mGeAdapter);
		} else if(mGameType.equals(SscType.dxds.toString())) {
			mLvBai.setVisibility(View.GONE);
			boolShi = _initBool(4);
			mShiAdapter = new SscGriViewAdapter(this, mDxdsContent,
					boolShi, mActivity.mCity);
			
			boolGe = _initBool(4);
			mGeAdapter = new SscGriViewAdapter(this, mDxdsContent,
					boolGe, mActivity.mCity);
			mShiGV.setAdapter(mShiAdapter);
			mGeGV.setAdapter(mGeAdapter);
		} else {
			boolBai = _initBool(10);
			mBaiAdapter = new SscGriViewAdapter(this, mContent,
					boolBai, mActivity.mCity);
			
			boolShi = _initBool(10);
			mShiAdapter = new SscGriViewAdapter(this, mContent,
					boolShi, mActivity.mCity);
			
			boolGe = _initBool(10);
			mGeAdapter = new SscGriViewAdapter(this, mContent,
					boolGe, mActivity.mCity);
			mBaiGV.setAdapter(mBaiAdapter);
			mShiGV.setAdapter(mShiAdapter);
			mGeGV.setAdapter(mGeAdapter);
		}
	}
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
				mZhushu = wanSelectNum*qianSelectNum*baiSelectNum*shiSelectNum*geSelectNum*4;
			} else {
				 mZhushu = wanSelectNum*qianSelectNum*baiSelectNum*shiSelectNum*geSelectNum;
			}
		} else if(mGameType.equals(SscType.twostar_fuxuan.toString())
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
		} else {
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
		}
		mActivity.setFooterBetValues(mZhushu);
	}
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
		} else if(mGameType.equals(SscType.onestar_zhixuan.toString())) {
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
		} else {
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
		}
		computeZhuShu();
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
			betResult = wBuilder.toString().trim().replace(" ", ",") + "|"+ qBuilder.toString().trim().replace(" ", ",")
					+"|" +bBuilder.toString().trim().replace(" ", ",") + "|" +sBuilder.toString().trim().replace(" ", ",")
					+ "|" +gBuilder.toString().trim().replace(" ", ",");
		} else if(mGameType.equals(SscType.twostar_fuxuan.toString())
				||mGameType.equals(SscType.twostar_zhixuan.toString())) {
			StringBuilder sBuilder = new StringBuilder();
			StringBuilder gBuilder = new StringBuilder();
			for(int i=0; i<boolGe.length; i++) {
				if(boolShi[i]) {
					sBuilder.append(i + " ");
				}
				if(boolGe[i]) {
					gBuilder.append(i + " ");
				}
			}
			betResult = sBuilder.toString().trim().replace(" ", ",") +"|" + gBuilder.toString().trim().replace(" ", ",");
		} else if(mGameType.equals(SscType.twostar_zuxuan.toString())) {
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
			betResult = gBuilder.toString().trim().trim().replace(" ", ",");
		}else if(mGameType.equals(SscType.twostar_zuxuan_baohao.toString())) {
			StringBuilder gBuilder = new StringBuilder();
			for(int i=0; i<boolGe.length; i++) {
				if(boolGe[i]) {
					gBuilder.append(i+ " ");
				}
			}
			betResult = gBuilder.toString().trim().replace(" ", ",");
		} else if(mGameType.equals(SscType.onestar_zhixuan.toString())) {
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
			betResult = sBuilder.toString().trim().replace(" ", ",") +"|" + gBuilder.toString().trim().replace(" ", ",");
		} else {
			StringBuilder bBuilder = new StringBuilder();
			StringBuilder sBuilder = new StringBuilder();
			StringBuilder gBuilder = new StringBuilder();
			for(int i=0; i<boolGe.length; i++) {
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
			betResult = bBuilder.toString().trim().replace(" ", ",") + "|" +sBuilder.toString().trim().replace(" ", ",") + "|" +gBuilder.toString().trim().replace(" ", ",");
		}
	}
}
