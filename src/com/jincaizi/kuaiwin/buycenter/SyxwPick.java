package com.jincaizi.kuaiwin.buycenter;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedList;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.http.Header;

import com.google.gson.stream.JsonReader;
import com.jincaizi.adapters.SyyTouZhuAdapter;
import com.jincaizi.http.JinCaiZiHttpClient;
import com.jincaizi.R;
import com.jincaizi.kuaiwin.mylottery.Login;
import com.jincaizi.kuaiwin.tool.BeiTouGenerator;
import com.jincaizi.kuaiwin.tool.CheckLogin;
import com.jincaizi.kuaiwin.tool.QiHaoTool;
import com.jincaizi.kuaiwin.tool.ShiyiyunRandom;
import com.jincaizi.kuaiwin.utils.Constants;
import com.jincaizi.kuaiwin.utils.Constants.ShiyiyunType;
import com.jincaizi.kuaiwin.utils.IntentAction;
import com.jincaizi.kuaiwin.utils.SafeAsyncTask;
import com.jincaizi.kuaiwin.utils.UiHelper;
import com.jincaizi.kuaiwin.utils.Utils;
import com.jincaizi.kuaiwin.widget.JinCaiZiProgressDialog;
import com.jincaizi.vendor.http.AsyncHttpClient;
import com.jincaizi.vendor.http.AsyncHttpResponseHandler;
import com.jincaizi.vendor.http.RequestParams;


public class SyxwPick extends Activity implements OnClickListener,
		OnItemClickListener, OnItemLongClickListener {
	public static final int MAXBEISHU = 9999;
	public static final int MAXZHUIHAO = 200;
	public static final int MINBEISHU = 1;
	public static final int MINZHUIHAO = 0;
	public static final int LOGINREQUESTCODE = 2;
	public static final String BALL = "redBall";
	public static final String BETTYPE = "betType";

	public static final int CONTINUEPICK = 0;
	public static final int RETRYPICK = 1;
	protected static final String TAG = "SyxwPick";
	private LinkedList<String> mBallList = new LinkedList<String>();
	private LinkedList<ShiyiyunType> mTypeList = new LinkedList<ShiyiyunType>();
	private SyyTouZhuAdapter mMyAdpater;
	private ListView mListView;
	private ImageView mBackView;
	private String mBetType;
	private TextView mRandomOne;
	private TextView mContinuePick;
	private int mMemoryPostion = -1;
	private Dialog myDialog;
	private TextView dialogCancel;
	private TextView dialogOK;
	private TextView mClearList;
	private TextView mSubmitGroupBuy;
	private ArrayList<String> mBall = new ArrayList<String>();
	private String mCity;
	private TextView mSubmitDaigou;
	private TextView mFootLeftBtn;
	private EditText mTraceTime;
	private EditText mTraceIssue;
	private int mZhuiHaoNums = 0;
	private int mBeiShu = 1;
	private ImageView mTimeSub;
	private ImageView mTimeAdd;
	private ImageView mIssueSub;
	private ImageView mIssueAdd;
	private ProgressBar mSmartFollowPB;
	private CheckBox mZhuiCheckbox;
	private int mCurZhuShu = 0;
	private int mUserid;
	private String mUpk;
	private SharedPreferences sp;
	private TextView totalInFooter;
	private String mQihao = "";
	private int perDayTermCount;
	private String lotterytype;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.common_pick_list);
		 sp = this.getSharedPreferences("loginStatus", Context.MODE_PRIVATE);
		 
		_initData(getIntent(), -1);
		_getTouZhuAttribute();
		_findViews();
		_setListener();
		mMyAdpater = new SyyTouZhuAdapter(this, mBallList, mTypeList);
		mListView.setAdapter(mMyAdpater);
	}
    private void _getTouZhuAttribute() {
	    if(mCity.equals(Constants.City.shandong.toString())) {
	    	lotterytype = "SD11x5";
	    	perDayTermCount = 78;
		} else if(mCity.equals(Constants.City.jiangxi.toString())) {
			lotterytype = "JX11x5";
			perDayTermCount = 78;
		}else if(mCity.equals(Constants.City.guangdong.toString())) {
			lotterytype = "GD11x5";
			perDayTermCount = 84;
		}else if(mCity.equals(Constants.City.anhui.toString())) {
			lotterytype = "AH11x5";
			perDayTermCount = 81;
		}else if(mCity.equals(Constants.City.chongqing.toString())) {
			lotterytype = "CQ11x5";
			perDayTermCount = 85;
		}else if(mCity.equals(Constants.City.liaoning.toString())) {
			lotterytype = "LN11x5";
			perDayTermCount = 83;
		}else if(mCity.equals(Constants.City.shanghai.toString())) {
			lotterytype = "SH11x5";
			perDayTermCount = 90;
		}else if(mCity.equals(Constants.City.heilongjiang.toString())) {
			lotterytype = "HLJ11x5";
			perDayTermCount = 73;
		}
    }
	private void _clearBetListData() {
		mBallList.clear();
		mTypeList.clear();
	}
	

	private void _initData(Intent intent, int memoryPostion) {
		mCity = intent.getStringExtra(Syxw.CITY);
		mBetType = intent.getStringExtra(BETTYPE);
		mBall = intent.getStringArrayListExtra(BALL);
		mQihao  = intent.getStringExtra("qihao");
		if (memoryPostion == -1) {
			mTypeList.addFirst(Constants.ShiyiyunType.getSyyType(mBetType));
			mBallList.addFirst(mBall.get(0));
			for (int i = 1; i < mBall.size(); i++) {
				mTypeList.addFirst(Constants.ShiyiyunType.getSyyType(mBetType));
				mBallList.addFirst(mBall.get(i));
			}
		} else {
			mBallList.remove(memoryPostion);
			mTypeList.remove(memoryPostion);
			mTypeList.addFirst(Constants.ShiyiyunType.getSyyType(mBetType));
			mBallList.addFirst(mBall.get(0));
			for (int i = 1; i < mBall.size(); i++) {
				mTypeList.addFirst(Constants.ShiyiyunType.getSyyType(mBetType));
				mBallList.addFirst(mBall.get(i));
			}
		}

	}

	private void _findViews() {
		((TextView) findViewById(R.id.current_lottery)).setText(Constants.City.getCityName(mCity)+"-11选5投注");
		mListView = (ListView) findViewById(R.id.touzhu_detail_list);
		TextView tv = (TextView) findViewById(R.id.empty_list_view);
		mListView.setEmptyView(tv);
		mBackView = (ImageView) findViewById(R.id.touzhu_leftmenu);
		mRandomOne = (TextView) findViewById(R.id.random_select_1);
		mContinuePick = (TextView) findViewById(R.id.continue_pick);
		mClearList = (TextView) findViewById(R.id.tv_ssq_clearlist);
		mSubmitGroupBuy = (TextView) findViewById(R.id.sumbit_group_buy);
		
		mSubmitDaigou = (TextView)findViewById(R.id.right_footer_btn);
		mFootLeftBtn = (TextView)findViewById(R.id.left_footer_btn);
		mFootLeftBtn.setBackgroundResource(R.drawable.popitem_bg);
		mFootLeftBtn.setTextColor(this.getResources().getColorStateList(R.color.footer_black));
		mFootLeftBtn.setPadding(10, 10, 10, 10);
		mFootLeftBtn.setText(this.getResources().getString(R.string.smartzhuihao));
		
		mTraceTime = (EditText)findViewById(R.id.trace_times);
		mTraceIssue = (EditText)findViewById(R.id.trace_issue);
		mTimeSub = (ImageView)findViewById(R.id.sub_times);
		mTimeAdd = (ImageView)findViewById(R.id.add_times);
		mIssueSub = (ImageView)findViewById(R.id.sub_issue);
		mIssueAdd = (ImageView)findViewById(R.id.add_issue);
		
		mSmartFollowPB = (ProgressBar)findViewById(R.id.smartfollow_pb);
		mZhuiCheckbox = (CheckBox)findViewById(R.id.zhui_checkbox);
		totalInFooter = (TextView)findViewById(R.id.bet_zhushu);
		setZhushuValue();
		
		myDialog = new Dialog(this, R.style.Theme_dialog);
		View view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.dialog_submit_bet, null);
		TextView dialogTitle = (TextView) view
				.findViewById(R.id.submit_dialog_title);
		dialogTitle.setText("提示");
		TextView dialogContent = (TextView) view
				.findViewById(R.id.submit_dialog_content);
		dialogContent.setText("确定删除该选号?");
		dialogCancel = (TextView) view.findViewById(R.id.tv_submit_cancel);
		dialogOK = (TextView) view.findViewById(R.id.tv_submit_ok);
		dialogOK.setText("确定");
		myDialog.setContentView(view);
		Window dialogWindow = myDialog.getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		lp.width = 300; // 宽度
		dialogWindow.setAttributes(lp);
	}

	private void _setListener() {
		mRandomOne.setOnClickListener(this);
		mBackView.setOnClickListener(this);
		mContinuePick.setOnClickListener(this);
		mListView.setOnItemClickListener(this);
		mListView.setOnItemLongClickListener(this);
		dialogCancel.setOnClickListener(this);
		mClearList.setOnClickListener(this);
		mSubmitGroupBuy.setOnClickListener(this);
		mFootLeftBtn.setOnClickListener(this);
		mSubmitDaigou.setOnClickListener(this);
		
		mTraceTime.addTextChangedListener(new MyTimeWatcher());
		mTraceIssue.addTextChangedListener(new MyIssueWatcher());
		mTimeAdd.setOnClickListener(this);
		mTimeSub.setOnClickListener(this);
		mIssueAdd.setOnClickListener(this);
		mIssueSub.setOnClickListener(this);
	}
	class MyTimeWatcher implements TextWatcher {

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			if(TextUtils.isEmpty(s)) {
				mTraceTime.setText("" + MINBEISHU);
				mBeiShu = MINBEISHU;
			} else if(Integer.parseInt(s.toString()) < MINBEISHU) {
				mTraceTime.setText("" + MINBEISHU);
				mBeiShu = MINBEISHU;
			} else if(Integer.parseInt(s.toString()) > MAXBEISHU) {
				mTraceTime.setText(""+MAXBEISHU);
				mBeiShu = MAXBEISHU;
			} else {
				mBeiShu = Integer.parseInt(s.toString());
			}
			setZhushuValue();
		}
		
	}
	class MyIssueWatcher implements TextWatcher {

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			if(TextUtils.isEmpty(s)) {
				mTraceIssue.setText("" + MINZHUIHAO);
				mZhuiHaoNums = MINZHUIHAO;
			} else if(Integer.parseInt(s.toString()) < MINZHUIHAO) {
				mTraceIssue.setText("" + MINZHUIHAO);
				mZhuiHaoNums = MINZHUIHAO;
			} else if(Integer.parseInt(s.toString()) > MAXZHUIHAO) {
				mTraceIssue.setText(""+MAXZHUIHAO);
				mZhuiHaoNums = MAXZHUIHAO;
			} else {
				mZhuiHaoNums = Integer.parseInt(s.toString());
			}
			if(mZhuiHaoNums>0) {
				mZhuiCheckbox.setVisibility(View.VISIBLE);
			} else {
				mZhuiCheckbox.setVisibility(View.GONE);
				mZhuiCheckbox.setChecked(false);
			}
			setZhushuValue();
		}
		
	}
	private void setTotalInFooter() {
		int factor = 2;
		if(mZhuiHaoNums >0) {
			//追号  此时期数=mCurQiShu
			totalInFooter.setText(mCurZhuShu+"注"+mZhuiHaoNums + "期" + mBeiShu + "倍" + factor*mCurZhuShu * mBeiShu *mZhuiHaoNums+"元");
		} else {
			//代购 此时期数=1
			totalInFooter.setText(mCurZhuShu+"注"+"1期" + mBeiShu + "倍" + factor*mCurZhuShu * mBeiShu*1+"元");
		}
	}
	private void setZhushuValue() {
		mCurZhuShu = mBallList.size();
		 setTotalInFooter();
	}
	
	private int _getBetMoney() {
		return mCurZhuShu * 2 *mBeiShu*mZhuiHaoNums;
	}
	private void _showDaigouDialog() {
		   int myBuy;
		   int factor = 2;
		   if(mZhuiHaoNums >0) {
				//追号  此时期数=mCurQiShu
			     myBuy = factor*mCurZhuShu * mBeiShu *mZhuiHaoNums;
			} else {
				//代购 此时期数=1
				 myBuy = factor*mCurZhuShu * mBeiShu*1;
			}
		final Dialog localDialog = new Dialog(SyxwPick.this, R.style.Theme_dialog);
		View view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.dialog_submit_bet, null);
		TextView dialogTitle = (TextView) view
				.findViewById(R.id.submit_dialog_title);
		dialogTitle.setText("提示");
		TextView dialogContent = (TextView) view
				.findViewById(R.id.submit_dialog_content);
		dialogContent.setText("方案金额 " + myBuy + "元！\n请确定要投注吗？");
		TextView localCancel = (TextView) view.findViewById(R.id.tv_submit_cancel);
		TextView localOK = (TextView) view.findViewById(R.id.tv_submit_ok);
		localOK.setText("确定");
		localDialog.setContentView(view);
		Window dialogWindow = localDialog.getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		lp.width = 300; // 宽度
		dialogWindow.setAttributes(lp);
		localOK.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				 localDialog.cancel();
				 _postFormData();
			}
		});
		localCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				localDialog.cancel();
			}
		});
		localDialog.show();
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.sub_issue:
			if(mZhuiHaoNums - 1 >= MINZHUIHAO && mZhuiHaoNums - 1 <= MAXZHUIHAO ) {
				mTraceIssue.setText((mZhuiHaoNums - 1) + "");
			}
			break;
		case R.id.add_issue:
			if(mZhuiHaoNums + 1 >= MINZHUIHAO && mZhuiHaoNums + 1 <= MAXZHUIHAO ) {
				mTraceIssue.setText((mZhuiHaoNums + 1) + "");
			}
			break;
		case R.id.sub_times:
			if(mBeiShu - 1 >= MINBEISHU&& mBeiShu - 1 <= MAXBEISHU ) {
				mTraceTime.setText((mBeiShu - 1) + "");
			}
			break;
		case R.id.add_times:
			if(mBeiShu + 1 >= MINBEISHU&& mBeiShu + 1 <= MAXBEISHU ) {
				mTraceTime.setText((mBeiShu + 1) + "");
			}
			break;
		case R.id.right_footer_btn:
			if(mCurZhuShu<1) {
				Toast.makeText(getApplicationContext(), "请至少选择1注",
						Toast.LENGTH_LONG).show();
				break;
			}
			if(mUserid == 0) {
				Intent loginIntent = new Intent(this, Login.class);
				startActivityForResult(loginIntent, LOGINREQUESTCODE);
			} else {
			   _showDaigouDialog();
			}
			break;
		case R.id.left_footer_btn:
			
			if(mBallList.size() == 0) {
				Toast.makeText(this, "请至少选择1注", Toast.LENGTH_SHORT).show();
				break;
			}
			if(mZhuiHaoNums < 1) {
				Toast.makeText(this, "请选择追号期数", Toast.LENGTH_SHORT).show();
				break;
			}
			if(!isSingleBetType()) {
				Toast.makeText(this, "您的选号包含多种玩法，暂时无法进行智能追号", Toast.LENGTH_SHORT).show();
				break;
			}
			mSmartFollowPB.setVisibility(View.VISIBLE);
			getMaxBonus();
//			UiHelper.startSmartFollow(this, mZhuiHaoNums, mBeiShu, mBallList.size(), mTypeList.get(0).toString(), mCity, "11x5");
			break;
		case R.id.sumbit_group_buy:
			if(mBallList.size() == 0) {
				Toast.makeText(this, "请至少选择1注", Toast.LENGTH_SHORT).show();
				break;
			}
			int factor = 2;
			int fenshu;
			boolean isZhuihao;
			if(mZhuiHaoNums >0) {
				//追号  此时期数=mCurQiShu
				fenshu = factor*mCurZhuShu * mBeiShu *mZhuiHaoNums;
				isZhuihao = true;
			} else {
				//代购 此时期数=1
				fenshu = factor*mCurZhuShu * mBeiShu*1;
				isZhuihao = false;
			}
			   StringBuilder itemBuilder = new StringBuilder();
			   StringBuilder typeBuilder = new StringBuilder();
			   StringBuilder betCountBuilder = new StringBuilder();
			   for(int i=0; i<mBallList.size(); i++) {
				   String redBall = mBallList.get(i);
				   redBall = redBall.replace("( ", "");
				   redBall = redBall.replace(" ) ", "#");
				   if(mTypeList.get(i) == ShiyiyunType.FRONTTWOZHI ||
						   mTypeList.get(i) == ShiyiyunType.FRONTTHREEZHI) {
					   redBall = redBall.replace(" ", "|");
				   } else {
				   redBall = redBall.replace(" ", ",");
				   }
				   if(i != 0) {
					   itemBuilder.append(";");
					   typeBuilder.append(";");
					   betCountBuilder.append(";");
				   }
				   itemBuilder.append(redBall);
				   typeBuilder.append(Constants.ShiyiyunType.getSelectedType(mTypeList.get(i)));
				   betCountBuilder.append("1");
			   }
			  // rawParams.add("multiple", mBeiShu + "");
			   
			   String nextQihao = mQihao;
			   StringBuilder qhBuilder = new StringBuilder(mQihao+"");
			   StringBuilder msBuilder = new StringBuilder(mBeiShu + "");
			   for(int i = 1; i< mZhuiHaoNums ; i++) {
				   nextQihao = QiHaoTool.get11x5NextQihao(nextQihao, perDayTermCount);
				   if(TextUtils.isEmpty(nextQihao)) {
					   Toast.makeText(this, "期号计算错误", Toast.LENGTH_SHORT).show();
					   return;
				   }
				   qhBuilder.append(";"+nextQihao);
				   msBuilder.append(";"+mBeiShu);
			   }
			UiHelper.startGroupBuy(this, mQihao, fenshu, isZhuihao, mCity, "11x5", qhBuilder.toString(), 
					msBuilder.toString(), itemBuilder.toString(), typeBuilder.toString(), betCountBuilder.toString(), mZhuiCheckbox.isChecked());
			break;
		case R.id.tv_ssq_clearlist:
			mBallList.clear();
			mTypeList.clear();
			mMyAdpater.notifyDataSetChanged();
			setZhushuValue();
			break;
		case R.id.tv_submit_cancel:
			myDialog.dismiss();
			break;
		case R.id.touzhu_leftmenu:
			finish();
			break;
		case R.id.random_select_1:
			ArrayList<String> result;
			ShiyiyunType recentType;
			if(mTypeList.size() == 0) {
				recentType =  Constants.ShiyiyunType.ANYTWO;
			} else {
				recentType = mTypeList.get(0);
			}
			
			if (recentType == Constants.ShiyiyunType.ANYTWO || recentType == Constants.ShiyiyunType.ANYTWODRAG) {
				result = ShiyiyunRandom.getSyyBallNoRePeat(2);
				recentType = Constants.ShiyiyunType.ANYTWO;
			} else if (recentType == Constants.ShiyiyunType.ANYTHREE || recentType == Constants.ShiyiyunType.ANYTHREEDRAG) {
				result = ShiyiyunRandom.getSyyBallNoRePeat(3);
				recentType = Constants.ShiyiyunType.ANYTHREE;
			} else if (recentType == Constants.ShiyiyunType.ANYFOUR || recentType == Constants.ShiyiyunType.ANYFOURDRAG) {
				result = ShiyiyunRandom.getSyyBallNoRePeat(4);
				recentType = Constants.ShiyiyunType.ANYFOUR;
			} else if (recentType == Constants.ShiyiyunType.ANYFIVE || recentType == Constants.ShiyiyunType.ANYFIVEDRAG) {
				result = ShiyiyunRandom.getSyyBallNoRePeat(5);
				recentType = Constants.ShiyiyunType.ANYFIVE;
			} else if (recentType == Constants.ShiyiyunType.ANYSIX || recentType == Constants.ShiyiyunType.ANYSIXDRAG) {
				result = ShiyiyunRandom.getSyyBallNoRePeat(6);
				recentType = Constants.ShiyiyunType.ANYSIX;
			} else if (recentType == Constants.ShiyiyunType.ANYSEVEN || recentType == Constants.ShiyiyunType.ANYSEVENDRAG) {
				result = ShiyiyunRandom.getSyyBallNoRePeat(7);
				recentType = Constants.ShiyiyunType.ANYSEVEN;
			} else if (recentType == Constants.ShiyiyunType.ANYEIGHT || recentType == Constants.ShiyiyunType.ANYSEVENDRAG) {
				result = ShiyiyunRandom.getSyyBallNoRePeat(8);
				recentType = Constants.ShiyiyunType.ANYEIGHT;
			} else if (recentType == Constants.ShiyiyunType.FRONTONEZHI) {
				result = ShiyiyunRandom.getSyyBallNoRePeat(1);
			} else if (recentType == Constants.ShiyiyunType.FRONTTWOZHI) {
				result = ShiyiyunRandom.getSyyBallNoRePeat(2);
			} else if (recentType == Constants.ShiyiyunType.FRONTTWOZU || recentType == Constants.ShiyiyunType.FRONTTWOZUDRAG) {
				result = ShiyiyunRandom.getSyyBallNoRePeat(2);
				recentType = Constants.ShiyiyunType.FRONTTWOZU;
			} else if (recentType == Constants.ShiyiyunType.FRONTTHREEZHI) {
				result = ShiyiyunRandom.getSyyBallNoRePeat(3);
			} else{
				result = ShiyiyunRandom.getSyyBallNoRePeat(3);
				recentType = Constants.ShiyiyunType.FRONTTHREEZU;
			}

			StringBuilder builder = new StringBuilder(result.get(0));
			for (int i = 1; i < result.size(); i++) {
				builder.append(" " + result.get(i));
			}
			mBallList.addFirst(builder.toString());
			mTypeList.addFirst(recentType);
			mMyAdpater.notifyDataSetChanged();
			setZhushuValue();
			break;
		case R.id.continue_pick:
			Intent continuePick = new Intent(SyxwPick.this, Syxw.class);
			continuePick.setAction(IntentAction.CONTINUEPICKBALL);
			continuePick.putExtra(Syxw.CITY, mCity);
			startActivityForResult(continuePick, CONTINUEPICK);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub

		super.onActivityResult(requestCode, resultCode, data);
		if (data == null) {
			return;
		}
		switch (requestCode) {
		case CONTINUEPICK:
			_initData(data, -1);
			mMyAdpater.notifyDataSetChanged();
			setZhushuValue();
			break;
		case RETRYPICK:
			_initData(data, mMemoryPostion);
			mMyAdpater.notifyDataSetChanged();
			mMemoryPostion = -1;
			setZhushuValue();
			break;
		case LOGINREQUESTCODE:
			Log.i(TAG, "登录成功");
			 sp = this.getSharedPreferences("loginStatus", Context.MODE_PRIVATE);
				mUserid = sp.getInt("userid", 0);
				mUpk = sp.getString("upk", "");
			//_postFormData();
			break;
		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		_clearBetListData();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
			mMemoryPostion = arg2;
			Intent retryPick = new Intent(SyxwPick.this, Syxw.class);
			retryPick.setAction(IntentAction.RETRYPICKBALL);
			retryPick.putExtra(BETTYPE, mTypeList.get(arg2).toString());
			retryPick.putExtra(BALL, mBallList.get(arg2));
			retryPick.putExtra(Syxw.CITY, mCity);
			startActivityForResult(retryPick, RETRYPICK);
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		final int position = arg2;
		dialogOK.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mBallList.remove(position);
				mTypeList.remove(position);
				mMyAdpater.notifyDataSetChanged();
				setZhushuValue();
				myDialog.dismiss();
			}
		});
		myDialog.show();
		return false;
	}
	/**
	 * 判断是否是单一的彩票玩法
	 */
	private boolean isSingleBetType() {
		for(int i=0; i< mTypeList.size() - 1; i++) {
			if(mTypeList.get(i) != mTypeList.get(i+1)) {
				return false;
			}
		}
		return true;
	}
	private void getMaxBonus(){
		SafeAsyncTask<Integer> getAllDaigouTask = new SafeAsyncTask<Integer>() {
			private int perAward;
			@Override
			public  Integer call() throws Exception {
				 perAward = Constants.ShiyiyunType.getPerAward(mTypeList.get(0).toString());
				if(mTypeList.get(0) == Constants.ShiyiyunType.ANYTWO || mTypeList.get(0) == Constants.ShiyiyunType.ANYTWODRAG 
						|| mTypeList.get(0) == Constants.ShiyiyunType.ANYTHREE || mTypeList.get(0) == Constants.ShiyiyunType.ANYTHREEDRAG
						|| mTypeList.get(0) == Constants.ShiyiyunType.ANYFOUR || mTypeList.get(0) == Constants.ShiyiyunType.ANYFOURDRAG  
						|| mTypeList.get(0) == Constants.ShiyiyunType.ANYFIVE || mTypeList.get(0) == Constants.ShiyiyunType.ANYFIVEDRAG  ) {
				    return BeiTouGenerator.getBonusR2to5(mBallList, perAward);
				} else if(mTypeList.get(0) == Constants.ShiyiyunType.ANYSIX || mTypeList.get(0) == Constants.ShiyiyunType.ANYSIXDRAG
						|| mTypeList.get(0) == Constants.ShiyiyunType.ANYSEVEN || mTypeList.get(0) == Constants.ShiyiyunType.ANYSEVENDRAG
						|| mTypeList.get(0) == Constants.ShiyiyunType.ANYEIGHT || mTypeList.get(0) == Constants.ShiyiyunType.ANYEIGHTDRAG  ) {
					return BeiTouGenerator.getBonusR6to8(mBallList, perAward);
				} else {
					return BeiTouGenerator.getBonusQ1to3(mBallList, perAward);
				}
			}

			@Override
			protected void onSuccess(Integer t) throws Exception {
				// TODO Auto-generated method stub
				super.onSuccess(t);
				Log.i(TAG, "maxBonus = " + t);
				 StringBuilder itemBuilder = new StringBuilder();
				   StringBuilder typeBuilder = new StringBuilder();
				   StringBuilder betCountBuilder = new StringBuilder();
				   for(int i=0; i<mBallList.size(); i++) {
					   String redBall = mBallList.get(i);
					   redBall = redBall.replace("( ", "");
					   redBall = redBall.replace(" ) ", "#");
					   if(mTypeList.get(i) == ShiyiyunType.FRONTTWOZHI ||
							   mTypeList.get(i) == ShiyiyunType.FRONTTHREEZHI) {
						   redBall = redBall.replace(" ", "|");
					   } else {
					   redBall = redBall.replace(" ", ",");
					   }
					   if(i != 0) {
						   itemBuilder.append(";");
						   typeBuilder.append(";");
						   betCountBuilder.append(";");
					   }
					   itemBuilder.append(redBall);
					   typeBuilder.append(Constants.ShiyiyunType.getSelectedType(mTypeList.get(i)));
					   betCountBuilder.append("1");
				   }

				UiHelper.startSmartFollow(SyxwPick.this, mZhuiHaoNums, mBeiShu, mBallList.size(), mTypeList.get(0).toString(), mCity, "11x5", t, perAward,
						itemBuilder.toString(), typeBuilder.toString(), betCountBuilder.toString(), mZhuiCheckbox.isChecked()
						);
				mSmartFollowPB.setVisibility(View.GONE);
			}

			@Override
			protected void onThrowable(Throwable t) {
				// TODO Auto-generated method stub
				super.onThrowable(t);
			}

			@Override
			protected void onFinally() {
				// TODO Auto-generated method stub
				super.onFinally();
				

			}

		};
		getAllDaigouTask.execute();
	}
	 private void _postFormData() {
		   _showTouzhuProgress();

		   RequestParams rawParams = new RequestParams();
		  
		   rawParams.add("currentQihao", mQihao);
		   if(mZhuiHaoNums <= 0) {
		       rawParams.add("isZhuihao", "false");
		       rawParams.add("autoStopZhuihao", "true");
		   } else {
			   rawParams.add("isZhuihao", "true");  
			   if(mZhuiCheckbox.isChecked()) {
			       rawParams.add("autoStopZhuihao", "true");
			   } else {
				   rawParams.add("autoStopZhuihao", "false");
			   }
		   }
		   StringBuilder itemBuilder = new StringBuilder();
		   StringBuilder typeBuilder = new StringBuilder();
		   StringBuilder betCountBuilder = new StringBuilder();
		   for(int i=0; i<mBallList.size(); i++) {
			   String redBall = mBallList.get(i);
			   redBall = redBall.replace("( ", "");
			   redBall = redBall.replace(" ) ", "#");
			   if(mTypeList.get(i) == ShiyiyunType.FRONTTWOZHI ||
					   mTypeList.get(i) == ShiyiyunType.FRONTTHREEZHI) {
				   redBall = redBall.replace(" ", "|");
			   } else {
			   redBall = redBall.replace(" ", ",");
			   }
			   if(i != 0) {
				   itemBuilder.append(";");
				   typeBuilder.append(";");
				   betCountBuilder.append(";");
			   }
			   itemBuilder.append(redBall);
			   typeBuilder.append(Constants.ShiyiyunType.getSelectedType(mTypeList.get(i)));
			   betCountBuilder.append("1");
		   }
		    rawParams.add("itemsContent", itemBuilder.toString());
		    rawParams.add("itemsSelectType", typeBuilder.toString());
		    rawParams.add("itemsBetCount", betCountBuilder.toString());
		  // rawParams.add("multiple", mBeiShu + "");
		   
		   String nextQihao = mQihao;
		   StringBuilder qhBuilder = new StringBuilder(mQihao+"");
		   StringBuilder msBuilder = new StringBuilder(mBeiShu + "");
		   for(int i = 1; i< mZhuiHaoNums ; i++) {
			   nextQihao = QiHaoTool.get11x5NextQihao(nextQihao, perDayTermCount);
			   if(TextUtils.isEmpty(nextQihao)) {
				   Toast.makeText(this, "期号计算错误", Toast.LENGTH_SHORT).show();
				   return;
			   }
			   qhBuilder.append(";"+nextQihao);
			   msBuilder.append(";"+mBeiShu);
		   }
		   Log.d(TAG, "qihaos = " + qhBuilder.toString());
		   rawParams.add("qihaos", qhBuilder.toString());
		   rawParams.add("multiples", msBuilder.toString());
		   
		   int myBuy;
		   if(mZhuiHaoNums >0) {
				//追号  此时期数=mCurQiShu
			     myBuy = 2*mCurZhuShu * mBeiShu *mZhuiHaoNums;
			     //return;
			} else {
				//代购 此时期数=1
				 myBuy = 2*mCurZhuShu * mBeiShu*1;
			}
		  // rawParams.add("myBuy", String.valueOf(myBuy));
		  // rawParams.add("secretType", "0");
		   rawParams.add("shoptype", "Daigou");
	
		   rawParams.add("type", lotterytype);
		   rawParams.add("zhuihaoTitle", "11x5");
		   rawParams.add("userid", String.valueOf(mUserid));

		            
		    		RequestParams paramsUrl = new RequestParams();
		    		paramsUrl.add("act", "buy");
		    		paramsUrl.add("lotterytype", lotterytype);
		    		paramsUrl.add("datatype", "json");
		    		paramsUrl.add("userid", String.valueOf(mUserid));
		    		paramsUrl.add("upk", mUpk);
		    		paramsUrl.add("jsoncallback","jsonp" + String.valueOf(System.currentTimeMillis()));
		    		paramsUrl.add("_", String.valueOf(System.currentTimeMillis()));
		    		String url = AsyncHttpClient.getUrlWithQueryString(false,
		    				JinCaiZiHttpClient.BASE_URL, paramsUrl);
		  JinCaiZiHttpClient.closeExpireConnection();
		  JinCaiZiHttpClient.postFormData(this,  url, rawParams, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
				try {
					String charset;
					if(Utils.isCmwapNet(SyxwPick.this)) {
						charset = "utf-8";
					} else {
						charset = "gb2312";
					}
					String jsonData = new String(responseBody, charset);
					Log.d(TAG, "post ssq form data = " + jsonData);
					parseTouzhuJson(jsonData);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
					Toast.makeText(SyxwPick.this, "数据可能异常，请检查数据请求是否成功，以免重复请求造成损失", Toast.LENGTH_LONG).show();
				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(SyxwPick.this, "数据可能异常，请检查数据请求是否成功，以免重复请求造成损失", Toast.LENGTH_LONG).show();
				} finally {
					// FIXME reader.close should be here
					final Dialog localDialog = new Dialog(SyxwPick.this, R.style.Theme_dialog);
					View view = LayoutInflater.from(getApplicationContext()).inflate(
							R.layout.dialog_submit_bet, null);
					TextView dialogTitle = (TextView) view
							.findViewById(R.id.submit_dialog_title);
					dialogTitle.setText("提示");
					TextView dialogContent = (TextView) view
							.findViewById(R.id.submit_dialog_content);
					TextView localCancel = (TextView) view.findViewById(R.id.tv_submit_cancel);
					localCancel.setVisibility(View.GONE);
					////view.findViewById(R.id.dialog_divider).setVisibility(View.GONE);
					TextView localOK = (TextView) view.findViewById(R.id.tv_submit_ok);
					localOK.setText("确定");
					localDialog.setContentView(view);
					Window dialogWindow = localDialog.getWindow();
					WindowManager.LayoutParams lp = dialogWindow.getAttributes();
					lp.width = 300; // 宽度
					dialogWindow.setAttributes(lp);
					localOK.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							 localDialog.cancel();
							 if(!TextUtils.isEmpty(successR) && successR.equals("success")) {
								 Intent mSsqIntent = new Intent(SyxwPick.this, Syxw.class);
								 mSsqIntent.putExtra(Syxw.CITY, mCity);
								 mSsqIntent.setAction(IntentAction.FIRSTPICKBALL);
								 startActivity(mSsqIntent);
							     finish();
							 } else if(!TextUtils.isEmpty(responseMsg) && responseMsg.equals("未登录或登录过期！")) {
								 CheckLogin.clearLoginStatus(sp);
								 Intent loginIntent = new Intent(SyxwPick.this, Login.class);
								 startActivityForResult(loginIntent, LOGINREQUESTCODE);
							 }
							
						}
					});
					if(!TextUtils.isEmpty(successR) && successR.equals("success")) {
						dialogContent.setText(responseMsg + "\n" + "当前余额 " + balance + "元");
						
					} else {
						dialogContent.setText(responseMsg);
					}
					_hideTouzhuProgress();
					localDialog.show();
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				//Log.d(TAG, "failure = " + error.toString());
				_hideTouzhuProgress();
				responseMsg = "投注失败, 请重试！";
				Toast.makeText(SyxwPick.this, responseMsg, Toast.LENGTH_SHORT).show();
				//finish();
			}
		});  

	   }
	   private String responseMsg = "投注失败！";
	   private String successR = "";
	   private String balance = "";
	private JinCaiZiProgressDialog mProgressDialog;
	   private void parseTouzhuJson(String jsonData) throws IOException {
		   JsonReader reader = new JsonReader(new StringReader(jsonData));
			reader.beginObject();
			
			while(reader.hasNext()) {
				String tagName = reader.nextName();
				if(tagName.equals("msg")) {
					responseMsg  = reader.nextString();
				} else if(tagName.equals("orderId")){
				    reader.nextInt();	
				} else if(tagName.equals("balance")) {
					balance = reader.nextString();
				} else if(tagName.equals("redirect")) {
					reader.nextInt();
				} else if(tagName.equals("result")) {
						successR = reader.nextString();
				}
			}
			reader.endObject();
			reader.close();
			
	   }
	   @Override
	 	protected void onResume() {
	 		// TODO Auto-generated method stub
	 		super.onResume();
	 		mUserid = sp.getInt("userid", 0);
	 		mUpk = sp.getString("upk", "");
	 	}
		protected void _showTouzhuProgress() {
			mProgressDialog = JinCaiZiProgressDialog.show(this, "正在提交，请稍等......");
		}

		private void _hideTouzhuProgress() {
			if (mProgressDialog != null) {
				mProgressDialog.dismiss();
			}
		}

}
