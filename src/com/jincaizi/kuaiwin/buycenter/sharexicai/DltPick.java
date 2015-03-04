package com.jincaizi.kuaiwin.buycenter.sharexicai;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

import org.apache.http.Header;

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
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.stream.JsonReader;
import com.jincaizi.R;
import com.jincaizi.adapters.TouZhuDetailListViewAdapter;
import com.jincaizi.http.JinCaiZiHttpClient;
import com.jincaizi.kuaiwin.buycenter.SumbitGroupBuy;
import com.jincaizi.kuaiwin.mylottery.Login;
import com.jincaizi.kuaiwin.tool.CheckLogin;
import com.jincaizi.kuaiwin.tool.DltRandom;
import com.jincaizi.kuaiwin.tool.QiHaoTool;
import com.jincaizi.kuaiwin.utils.Constants;
import com.jincaizi.kuaiwin.utils.IntentAction;
import com.jincaizi.kuaiwin.utils.Utils;
import com.jincaizi.kuaiwin.widget.JinCaiZiProgressDialog;
import com.jincaizi.vendor.http.AsyncHttpClient;
import com.jincaizi.vendor.http.AsyncHttpResponseHandler;
import com.jincaizi.vendor.http.RequestParams;

public class DltPick extends Activity implements OnClickListener,
		OnItemClickListener, OnItemLongClickListener {
	private static final int MAXBEISHU = 9999;
	private static final int MINBEISHU = 1;
	private static final int MAXQISHU = 13;
	private static final int MINQISHU = 0;
	public static final String REDBALL = "redBall";
	public static final String REDDANBALL = "redDanBall";
	public static final String REDTUOBALL = "redTuoBall";
	public static final String BLUEBALL = "blueBall";
	public static final String COUNT = "count";
	public static final String BETTYPE = "betType";
	public static final String BLUEDANMARK = "bluedanmark";//蓝色胆码是否被选择

	public static final int CONTINUEPICK = 0;
	public static final int RETRYPICK = 1;
	public static final int LOGINREQUESTCODE = 2;
	protected static final String TAG = "DltPick";

	private ArrayList<String> mRedBall = new ArrayList<String>();
	private ArrayList<String> mRedDanBall = new ArrayList<String>();
	private ArrayList<String> mRedTuoBall = new ArrayList<String>();
	private ArrayList<String> mBlueBall = new ArrayList<String>();
	private int mCount;
	private LinkedList<String> mRedBallList = new LinkedList<String>(),
			mBlueBallList = new LinkedList<String>();
	private LinkedList<Integer> mCountList = new LinkedList<Integer>();
	private TouZhuDetailListViewAdapter mMyAdpater;
	private ListView mListView;
	private ImageView mBackView;
	private boolean mBetType = true;
	private TextView mRandomOne;
	private TextView mContinuePick;
	private int mMemoryPostion = -1;
	private Dialog myDialog;
	private TextView dialogCancel;
	private TextView dialogOK;
	private TextView mClearList;
	private TextView mSubmitGroupBuy;
	private LinkedList<Boolean> mCanBlueDan = new LinkedList<Boolean>();
	private TextView totalInFooter;
	private int mCurZhuShu = 0;
	private int mCurBeiShu = 1;
	private int mCurQiShu = 0;
	private EditText mBeiTouView;
	private ImageView mSubBeiView;
	private ImageView mAddBeiView;
	private ImageView mSubQiView;
	private ImageView mAddQiView;
	private EditText mQiTouView;
	private CheckBox mZhuiCheckbox;
	private String mQihao = "";
	private TextView mPayMoneyView;
	private int mUserid;
	private String mUpk;
	private CheckBox mZJiaCheckbox;
	private int factor = 2;//追加所影响的每注金额，不追加，factor =2， 追加 factor = 3；
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.common_pick_list);
		 sp = this.getSharedPreferences("loginStatus", Context.MODE_PRIVATE);
			mUserid = sp.getInt("userid", 0);
			mUpk = sp.getString("upk", "");
		_initData(getIntent(), -1);
		_findViews();
		_setListener();
		mMyAdpater = new TouZhuDetailListViewAdapter(this, mRedBallList,
				mBlueBallList, mCountList);
		mListView.setAdapter(mMyAdpater);
	}

	private void _clearBallData() {
		mRedBall.clear();
		mRedDanBall.clear();
		mRedTuoBall.clear();
		mBlueBall.clear();
	}

	private void _clearBetListData() {
		mRedBallList.clear();
		mBlueBallList.clear();
		mCountList.clear();
		mCanBlueDan.clear();
	}

	private void _initData(Intent intent, int memoryPostion) {
		_clearBallData();
		mBetType = intent.getBooleanExtra(BETTYPE, true);
		mQihao = intent.getStringExtra("qihao");
		StringBuilder redBuilder = new StringBuilder();
		if (mBetType) {
			mRedBall = intent.getStringArrayListExtra(REDBALL);
			if (mRedBall == null || mRedBall.size() == 0) {
				return;
			}
			redBuilder.append(mRedBall.get(0));
			for (int i = 1; i < mRedBall.size(); i++) {
				redBuilder.append(" " + mRedBall.get(i));
			}
		} else {
			mRedDanBall = intent.getStringArrayListExtra(REDDANBALL);
			mRedTuoBall = intent.getStringArrayListExtra(REDTUOBALL);
			if (mRedDanBall == null || mRedDanBall.size() == 0) {
				return;
			}
			redBuilder.append("( " + mRedDanBall.get(0));
			for (int i = 1; i < mRedDanBall.size(); i++) {
				redBuilder.append(" " + mRedDanBall.get(i));
			}
			if (mRedTuoBall == null || mRedTuoBall.size() == 0) {
				return;
			}
			redBuilder.append(" ) " + mRedTuoBall.get(0));
			for (int i = 1; i < mRedTuoBall.size(); i++) {
				redBuilder.append(" " + mRedTuoBall.get(i));
			}
		}
		mBlueBall = intent.getStringArrayListExtra(BLUEBALL);
		mCount = intent.getIntExtra(COUNT, 0);
		if (mBlueBall == null || mBlueBall.size() == 0) {
			return;
		}
		StringBuilder blueBuilder;
		if(intent.getBooleanExtra(BLUEDANMARK, false)) {
			blueBuilder = new StringBuilder("( "+mBlueBall.get(0)+" )");
		} else {
			blueBuilder = new StringBuilder(mBlueBall.get(0));
		}
		//StringBuilder blueBuilder = new StringBuilder(mBlueBall.get(0));
		for (int i = 1; i < mBlueBall.size(); i++) {
			blueBuilder.append(" " + mBlueBall.get(i));
		}
		if (memoryPostion == -1) {
			mRedBallList.addFirst(redBuilder.toString().trim());
			mBlueBallList.addFirst(blueBuilder.toString().trim());
			mCountList.addFirst(mCount);
			mCanBlueDan.addFirst(intent.getBooleanExtra(BLUEDANMARK, false));
		} else {
			
			mRedBallList.remove(memoryPostion);
			mBlueBallList.remove(memoryPostion);
			mCountList.remove(memoryPostion);
			mCanBlueDan.remove(memoryPostion);
			mRedBallList.addFirst(redBuilder.toString().trim());
			mBlueBallList.addFirst(blueBuilder.toString().trim());
			mCountList.addFirst(mCount);
			mCanBlueDan.addFirst(intent.getBooleanExtra(BLUEDANMARK, false));
		}

	}

	private void _findViews() {
		((TextView)findViewById(R.id.current_lottery)).setText("大乐透投注");
		mListView = (ListView) findViewById(R.id.touzhu_detail_list);
		TextView tv = (TextView) findViewById(R.id.empty_list_view);
		mListView.setEmptyView(tv);
		mBackView = (ImageView) findViewById(R.id.touzhu_leftmenu);
		mRandomOne = (TextView) findViewById(R.id.random_select_1);
		mContinuePick = (TextView) findViewById(R.id.continue_pick);
		mClearList = (TextView) findViewById(R.id.tv_ssq_clearlist);
		mSubmitGroupBuy = (TextView) findViewById(R.id.sumbit_group_buy);
		
		mSubQiView = (ImageView)findViewById(R.id.sub_issue);
		mAddQiView = (ImageView)findViewById(R.id.add_issue);
		mQiTouView = (EditText)findViewById(R.id.trace_issue);
		
		mBeiTouView = (EditText)findViewById(R.id.trace_times);
		mSubBeiView = (ImageView)findViewById(R.id.sub_times);
		mAddBeiView = (ImageView)findViewById(R.id.add_times);
		totalInFooter = (TextView)findViewById(R.id.bet_zhushu);
		
		mZhuiCheckbox = (CheckBox)findViewById(R.id.zhui_checkbox);
		mZJiaCheckbox = (CheckBox)findViewById(R.id.zjia_checkbox);//追加投注
		mZJiaCheckbox.setVisibility(View.VISIBLE);
		
		mPayMoneyView = (TextView)findViewById(R.id.right_footer_btn);
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
		
		findViewById(R.id.left_footer_btn).setVisibility(View.GONE);
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
		
		mAddBeiView.setOnClickListener(this);
		mSubBeiView.setOnClickListener(this);
		mBeiTouView.addTextChangedListener(new MyBeiWatcher());
		
		mAddQiView.setOnClickListener(this);
		mSubQiView.setOnClickListener(this);
		mQiTouView.addTextChangedListener(new MyQiWatcher());
		
		mPayMoneyView.setOnClickListener(this);
		
		mZJiaCheckbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				setTotalInFooter();
			}
		});
	}
	private void setTotalInFooter() {
		if(mZJiaCheckbox.isChecked()) {
			factor = 3;
		} else {
			factor = 2;
		}
		if(mCurQiShu >0) {
			//追号  此时期数=mCurQiShu
			totalInFooter.setText(mCurZhuShu+"注"+mCurQiShu + "期" + mCurBeiShu + "倍" + factor*mCurZhuShu * mCurBeiShu *mCurQiShu+"元");
		} else {
			//代购 此时期数=1
			totalInFooter.setText(mCurZhuShu+"注"+"1期" + mCurBeiShu + "倍" + factor*mCurZhuShu * mCurBeiShu*1+"元");
		}
	}
	private void setZhushuValue() {
		mCurZhuShu= _getBetZhushu();
		 setTotalInFooter();
	}
	private int _getBetZhushu() {
		int sum = 0;
		for (int i = 0; i < mCountList.size(); i++) {
			sum += mCountList.get(i);
		}
		return sum;
	}
	private void _showDaigouDialog() {
		   int myBuy;
		   if(mCurQiShu >0) {
				//追号  此时期数=mCurQiShu
			     myBuy = factor*mCurZhuShu * mCurBeiShu *mCurQiShu;
			} else {
				//代购 此时期数=1
				 myBuy = factor*mCurZhuShu * mCurBeiShu*1;
			}
		final Dialog localDialog = new Dialog(DltPick.this, R.style.Theme_dialog);
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
		case R.id.add_issue:
			mCurQiShu++;
			if(mCurQiShu> MAXQISHU) {
				mCurQiShu = MAXQISHU;
			}
	        mQiTouView.setText(mCurQiShu+"");
			break;
		case R.id.sub_issue:
			mCurQiShu--;
			if(mCurQiShu<MINQISHU) {
				mCurQiShu = MINQISHU;
			}
			mQiTouView.setText(mCurQiShu+"");
			break;
		case R.id.add_times:
			mCurBeiShu++;
			if(mCurBeiShu> MAXBEISHU) {
				mCurBeiShu = MAXBEISHU;
			}
	        mBeiTouView.setText(mCurBeiShu+"");
			break;
		case R.id.sub_times:
			mCurBeiShu--;
			if(mCurBeiShu<MINBEISHU) {
				mCurBeiShu = MINBEISHU;
			}
			mBeiTouView.setText(mCurBeiShu+"");
			break;
		case R.id.sumbit_group_buy:
			//int betMoney = _getBetMoney();
			int allMoney = factor*mCurZhuShu * mCurBeiShu*1;
			if (allMoney >= 2) {
				if(mCurQiShu >0) {
					doGroupBuyHint();
				} else {
					
					startGroupBuy(allMoney);
				}
			} else {
				Toast.makeText(getApplicationContext(), "请至少选择1注",
						Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.tv_ssq_clearlist:
			mRedBallList.clear();
			mBlueBallList.clear();
			mCountList.clear();
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
			String[] randomResult = DltRandom.getDltBall();
			StringBuilder redBuilder = new StringBuilder();
			redBuilder.append(randomResult[0]);
			for (int i = 1; i < randomResult.length - 2; i++) {
				redBuilder.append(" " + randomResult[i]);
			}
			StringBuilder blueBuilder = new StringBuilder(
					randomResult[randomResult.length - 2]);
			blueBuilder.append(" " + randomResult[randomResult.length - 1]);
			mRedBallList.addFirst(redBuilder.toString());
			mBlueBallList.addFirst(blueBuilder.toString());
			mCountList.addFirst(1);
			mMyAdpater.notifyDataSetChanged();
			setZhushuValue();
			break;
		case R.id.continue_pick:
			Intent continuePick = new Intent(DltPick.this, Dlt.class);
			continuePick.setAction(IntentAction.CONTINUEPICKBALL);
			startActivityForResult(continuePick, CONTINUEPICK);
			break;
		default:
			break;
		}
	}
	private void startGroupBuy(int betMoney) {
		String[] itemContents = new String[mRedBallList.size()];
		   for(int i=0; i<mRedBallList.size(); i++) {
			   String redBall = mRedBallList.get(i);
			   redBall = redBall.replace("( ", "");
			   redBall = redBall.replace(" ) ", "#");
			   redBall = redBall.replace(" ", ",");
			   String blueBall = mBlueBallList.get(i);
			   
			   blueBall = blueBall.replace("( ", "");
			   blueBall = blueBall.replace(" ) ", "#");
			   blueBall = blueBall.replace(" ", ",");
			   String value = redBall + "|" + blueBall;
			   itemContents[i] = value;
		   }
		Intent submitGroupBuy = new Intent(DltPick.this,
				GroupBuyXiCai.class);
		submitGroupBuy.putExtra(GroupBuyXiCai.FENSHU, betMoney);
		submitGroupBuy.putExtra("lotterytype", Constants.LotteryType.DLT.toString());
		submitGroupBuy.putExtra("qihao", mQihao);
		submitGroupBuy.putExtra("itemcontents", itemContents);
		submitGroupBuy.putExtra("beishu", mCurBeiShu+"");
		submitGroupBuy.putExtra("zhuijia", mZJiaCheckbox.isChecked());
		startActivity(submitGroupBuy);
	}
    private void doGroupBuyHint() {
    	final Dialog groupBuyDialog = new Dialog(DltPick.this, R.style.Theme_dialog);
		View view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.dialog_submit_bet, null);
		TextView dialogTitle = (TextView) view
				.findViewById(R.id.submit_dialog_title);
		dialogTitle.setText("提示");
		TextView dialogContent = (TextView) view
				.findViewById(R.id.submit_dialog_content);
		dialogContent.setText("发起合买时不能追号，是否停止追号并发起合买？");
		TextView localCancel = (TextView) view.findViewById(R.id.tv_submit_cancel);
		TextView localOK = (TextView) view.findViewById(R.id.tv_submit_ok);
		localOK.setText("确定");
		groupBuyDialog.setContentView(view);
		Window dialogWindow = groupBuyDialog.getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		lp.width = 300; // 宽度
		dialogWindow.setAttributes(lp);
		localOK.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mCurQiShu = 0;
				mQiTouView.setText(mCurQiShu+"");
				int allMoney = factor*mCurZhuShu * mCurBeiShu*1;
				startGroupBuy(allMoney);
				groupBuyDialog.cancel();
				
			}
		});
		localCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				groupBuyDialog.cancel();
			}
		});
		groupBuyDialog.show();
    }
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub

		super.onActivityResult(requestCode, resultCode, data);
		
		switch (requestCode) {
		case CONTINUEPICK:
			if (data == null) {
				break;
			}
			_initData(data, -1);
			mMyAdpater.notifyDataSetChanged();
			setZhushuValue();
			break;
		case RETRYPICK:
			if (data == null) {
				break;
			}
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
		_clearBallData();
		_clearBetListData();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		TextView itemRed = (TextView) arg1
				.findViewById(R.id.tv_red_ball_number);
		TextView itemBlue = (TextView) arg1
				.findViewById(R.id.tv_blue_ball_number);
		Intent retrySelect = new Intent(DltPick.this, Dlt.class);
		String[] redArray = itemRed.getText().toString().split(" ");
		ArrayList<String> redList = new ArrayList<String>();
		if (redArray[0].equals("(")) {
			retrySelect.putExtra(BETTYPE, false);
			for (int i = 0; i < redArray.length; i++) {
				if (redArray[i].equals("(")) {
					continue;
				}
				redList.add(redArray[i]);
			}
		} else {
			retrySelect.putExtra(BETTYPE, true);
			redList.addAll(Arrays.asList(redArray));
		}
		String blueStr = itemBlue.getText().toString();
        if(mCanBlueDan.get(arg2)) {
			blueStr = blueStr.replace("( ", "");
			blueStr = blueStr.replace(" ) ", " ");
		} 
		String[] blueArray = (blueStr.trim()).split(" ");
		ArrayList<String> blueList = new ArrayList<String>(
				Arrays.asList(blueArray));

		retrySelect.setAction(IntentAction.RETRYPICKBALL);
		mMemoryPostion = arg2;
		retrySelect.putStringArrayListExtra(REDBALL, redList);
		retrySelect.putStringArrayListExtra(BLUEBALL, blueList);
		retrySelect.putExtra(BLUEDANMARK, mCanBlueDan.get(arg2));
		retrySelect.setAction(IntentAction.RETRYPICKBALL);
		startActivityForResult(retrySelect, RETRYPICK);

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
				mRedBallList.remove(position);
				mBlueBallList.remove(position);
				mCountList.remove(position);
				mMyAdpater.notifyDataSetChanged();
				setZhushuValue();
				myDialog.dismiss();
			}
		});
		myDialog.show();
		return false;
	}
   class MyBeiWatcher implements TextWatcher {

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
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if(TextUtils.isEmpty(s)) {
			mBeiTouView.setText(MINBEISHU+ "");	
			mCurBeiShu = 1;
			return;
		}
		int curValue = Integer.valueOf(s.toString());
		if(curValue > MAXBEISHU) {
			mCurBeiShu = MAXBEISHU;
		    mBeiTouView.setText(mCurBeiShu+"");
		} else if(curValue<MINBEISHU) {			 
			mCurBeiShu = MINBEISHU;
			mBeiTouView.setText(mCurBeiShu+"");
		} else {
			mCurBeiShu = curValue;
		}
		setTotalInFooter();
	}
		
	}
   class MyQiWatcher implements TextWatcher {

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
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		if(TextUtils.isEmpty(s)) {
			mQiTouView.setText(MINQISHU+ "");	
			mCurQiShu = 0;
			return;
		}
		int curValue = Integer.valueOf(s.toString());
		if(curValue > MAXQISHU) {
			mCurQiShu = MAXQISHU;
			mQiTouView.setText(mCurQiShu+"");
		} else if(curValue<MINQISHU) {			 
			mCurQiShu = MINQISHU;
			mQiTouView.setText(mCurQiShu+"");
		} else {
			mCurQiShu = curValue;
		}
		if(mCurQiShu>0) {
			mZhuiCheckbox.setVisibility(View.VISIBLE);
		} else {
			mZhuiCheckbox.setVisibility(View.GONE);
			mZhuiCheckbox.setChecked(false);
		}
		setTotalInFooter();
		
	}
	   
   }
   
   private void _postFormData() {
	   _showTouzhuProgress();
	   RequestParams rawParams = new RequestParams();
	   if(mZJiaCheckbox.isChecked()) {
		   rawParams.add("add", "true");//是否追加 
	   } else {
		   rawParams.add("add", "false");//是否追加
	   }
	  
	   rawParams.add("brokerage", "0");
	   rawParams.add("currentQihao", mQihao);
	   rawParams.add("hemaiDescription", "喜彩网，有彩生活更精彩！");
	   rawParams.add("hemaiTitle", "大乐透第"+mQihao+"期合买");
	   rawParams.add("insureCount", "0");
	   if(mCurQiShu <= 0) {
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
	   for(int i=0; i<mRedBallList.size(); i++) {
		   String redBall = mRedBallList.get(i);
		   redBall = redBall.replace("( ", "");
		   redBall = redBall.replace(" ) ", "#");
		   redBall = redBall.replace(" ", ",");
		   String blueBall = mBlueBallList.get(i);
		   blueBall = blueBall.replace("( ", "");
		   blueBall = blueBall.replace(" ) ", "#");
		   blueBall = blueBall.replace(" ", ",");
		   String value = redBall + "|" + blueBall;
		   if(i != 0) {
			   itemBuilder.append(";");
			   typeBuilder.append(";");
		   }
		   itemBuilder.append(value);
		   typeBuilder.append("S");
		   
	   }
	    rawParams.add("itemsContent", itemBuilder.toString());
	    rawParams.add("itemsSelectType", typeBuilder.toString());
	    
	   rawParams.add("multiple", mCurBeiShu + "");
	   
	   String nextQihao = mQihao;
	   StringBuilder qhBuilder = new StringBuilder(mQihao+"");
	   StringBuilder msBuilder = new StringBuilder(mCurBeiShu + "");
	   for(int i = 1; i< mCurQiShu ; i++) {
		   nextQihao = QiHaoTool.getNextQihao(nextQihao, perYearTermCount);
		   qhBuilder.append(";"+nextQihao);
		   msBuilder.append(";"+mCurBeiShu);
	   }
	   rawParams.add("qihaos", qhBuilder.toString());
	   rawParams.add("multiples", msBuilder.toString());
	   
	   int myBuy;
	   if(mCurQiShu >0) {
			//追号  此时期数=mCurQiShu
		     myBuy = factor*mCurZhuShu * mCurBeiShu *mCurQiShu;
		} else {
			//代购 此时期数=1
			 myBuy = factor*mCurZhuShu * mCurBeiShu*1;
		}
	   rawParams.add("myBuy", String.valueOf(myBuy));
	   rawParams.add("secretType", "0");
	   rawParams.add("shoptype", "Daigou");
	   rawParams.add("src", "");
	   rawParams.add("totalShare", "");
	   rawParams.add("type", "DLT");
	   rawParams.add("zhuihaoTitle", "DLT");
	   rawParams.add("userid", String.valueOf(mUserid));

	            
	    		RequestParams paramsUrl = new RequestParams();
	    		paramsUrl.add("act", "buy");
	    		paramsUrl.add("lotterytype", "DLT");
	    		paramsUrl.add("datatype", "json");
	    		paramsUrl.add("userid", String.valueOf(mUserid));
	    		paramsUrl.add("upk", mUpk);
	    		paramsUrl.add("jsoncallback","jsonp" + String.valueOf(System.currentTimeMillis()));
	    		paramsUrl.add("_", String.valueOf(System.currentTimeMillis()));
	    		String url = AsyncHttpClient.getUrlWithQueryString(false,
	    				JinCaiZiHttpClient.BASE_URL, paramsUrl);
	            
	  JinCaiZiHttpClient.postFormData(this, url, rawParams, new AsyncHttpResponseHandler() {
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			try {
				String charset;
				if(Utils.isCmwapNet(DltPick.this)) {
					charset = "utf-8";
				} else {
					charset = "gb2312";
				}
				String jsonData = new String(responseBody, charset);
				Log.d(TAG, "post ssq form data = " + jsonData);
				parseTouzhuJson(jsonData);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				Toast.makeText(DltPick.this, "数据可能异常，请检查数据请求是否成功，以免重复请求造成损失", Toast.LENGTH_LONG).show();
			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(DltPick.this, "数据可能异常，请检查数据请求是否成功，以免重复请求造成损失", Toast.LENGTH_LONG).show();
			} finally {
				// FIXME reader.close should be here
				final Dialog localDialog = new Dialog(DltPick.this, R.style.Theme_dialog);
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
							 Intent mSsqIntent = new Intent(DltPick.this, Dlt.class);
							 mSsqIntent.setAction(IntentAction.FIRSTPICKBALL);
							 startActivity(mSsqIntent);
						     finish();
						 } else if(!TextUtils.isEmpty(responseMsg) && responseMsg.equals("未登录或登录过期！")) {
							 CheckLogin.clearLoginStatus(sp);
							 Intent loginIntent = new Intent(DltPick.this, Login.class);
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
			Toast.makeText(DltPick.this, responseMsg, Toast.LENGTH_SHORT).show();
			//finish();
		}
	});  

   }
   private int perYearTermCount=153;
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
