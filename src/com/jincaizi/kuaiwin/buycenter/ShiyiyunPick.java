package com.jincaizi.kuaiwin.buycenter;

import java.util.ArrayList;
import java.util.LinkedList;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jincaizi.adapters.SyyTouZhuAdapter;
import com.jincaizi.R;
import com.jincaizi.kuaiwin.tool.ShiyiyunRandom;
import com.jincaizi.kuaiwin.utils.Constants;
import com.jincaizi.kuaiwin.utils.Constants.ShiyiyunType;
import com.jincaizi.kuaiwin.utils.IntentAction;

public class ShiyiyunPick extends Activity implements OnClickListener,
		OnItemClickListener, OnItemLongClickListener {
	public static final String BALL = "redBall";
	public static final String BETTYPE = "betType";

	public static final int CONTINUEPICK = 0;
	public static final int RETRYPICK = 1;
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
	private TextView mSubmitDaigou;
	private TextView mFootLeftBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.common_pick_list);
		_initData(getIntent(), -1);
		_findViews();
		_setListener();
		mMyAdpater = new SyyTouZhuAdapter(this, mBallList, mTypeList);
		mListView.setAdapter(mMyAdpater);
	}

	private void _clearBetListData() {
		mBallList.clear();
		mTypeList.clear();
	}

	private void _initData(Intent intent, int memoryPostion) {
		mBetType = intent.getStringExtra(BETTYPE);
		mBall = intent.getStringArrayListExtra(BALL);
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
		((TextView) findViewById(R.id.current_lottery)).setText("十一运夺金投注");
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
		mSubmitDaigou.setOnClickListener(this);
	}

	private int _getBetMoney() {
		return mBallList.size() * 2;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.sumbit_group_buy:
			int betMoney = _getBetMoney();
			if (betMoney > 8) {
				Intent submitGroupBuy = new Intent(ShiyiyunPick.this,
						GroupBuy.class);
				submitGroupBuy.putExtra(GroupBuy.FENSHU, betMoney);
				startActivity(submitGroupBuy);
			} else {
				Toast.makeText(getApplicationContext(), "投注金额超过8元方可发起合买",
						Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.tv_ssq_clearlist:
			mBallList.clear();
			mMyAdpater.notifyDataSetChanged();
			break;
		case R.id.tv_submit_cancel:
			myDialog.dismiss();
			break;
		case R.id.touzhu_leftmenu:
			finish();
			break;
		case R.id.random_select_1:
			ArrayList<String> result;
			ShiyiyunType recentType = mTypeList.get(0);
			if (recentType == Constants.ShiyiyunType.ANYTWO) {
				result = ShiyiyunRandom.getSyyBallNoRePeat(2);
			} else if (recentType == Constants.ShiyiyunType.ANYTHREE) {
				result = ShiyiyunRandom.getSyyBallNoRePeat(3);
			} else if (recentType == Constants.ShiyiyunType.ANYFOUR) {
				result = ShiyiyunRandom.getSyyBallNoRePeat(4);
			} else if (recentType == Constants.ShiyiyunType.ANYFIVE) {
				result = ShiyiyunRandom.getSyyBallNoRePeat(5);
			} else if (recentType == Constants.ShiyiyunType.ANYSIX) {
				result = ShiyiyunRandom.getSyyBallNoRePeat(6);
			} else if (recentType == Constants.ShiyiyunType.ANYSEVEN) {
				result = ShiyiyunRandom.getSyyBallNoRePeat(7);
			} else if (recentType == Constants.ShiyiyunType.ANYEIGHT) {
				result = ShiyiyunRandom.getSyyBallNoRePeat(8);
			} else if (recentType == Constants.ShiyiyunType.FRONTONEZHI) {
				result = ShiyiyunRandom.getSyyBallNoRePeat(1);
			} else if (recentType == Constants.ShiyiyunType.FRONTTWOZHI) {
				result = ShiyiyunRandom.getSyyBallNoRePeat(2);
			} else if (recentType == Constants.ShiyiyunType.FRONTTWOZU) {
				result = ShiyiyunRandom.getSyyBallNoRePeat(2);
			} else if (recentType == Constants.ShiyiyunType.FRONTTHREEZHI) {
				result = ShiyiyunRandom.getSyyBallNoRePeat(3);
			} else{
				result = ShiyiyunRandom.getSyyBallNoRePeat(3);
			}

			StringBuilder builder = new StringBuilder(result.get(0));
			for (int i = 1; i < result.size(); i++) {
				builder.append(" " + result.get(i));
			}
			mBallList.addFirst(builder.toString());
			mTypeList.addFirst(recentType);
			mMyAdpater.notifyDataSetChanged();
			break;
		case R.id.continue_pick:
			Intent continuePick = new Intent(ShiyiyunPick.this, Syiyunjin.class);
			continuePick.setAction(IntentAction.CONTINUEPICKBALL);
			startActivityForResult(continuePick, CONTINUEPICK);
			break;
		case R.id.right_footer_btn:
			myDialog.show();
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
			break;
		case RETRYPICK:
			_initData(data, mMemoryPostion);
			mMyAdpater.notifyDataSetChanged();
			mMemoryPostion = -1;
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
			Intent retryPick = new Intent(ShiyiyunPick.this, Syiyunjin.class);
			retryPick.setAction(IntentAction.RETRYPICKBALL);
			retryPick.putExtra(BETTYPE, mTypeList.get(arg2).toString());
			retryPick.putExtra(BALL, mBallList.get(arg2));
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
				myDialog.dismiss();
			}
		});
		myDialog.show();
		return false;
	}

}
