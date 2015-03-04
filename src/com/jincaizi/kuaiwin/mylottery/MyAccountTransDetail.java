package com.jincaizi.kuaiwin.mylottery;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.jincaizi.bean.AccountRecordEntity;
import com.jincaizi.R;

public class MyAccountTransDetail extends Activity implements OnClickListener{

	private ImageView mLeftMenu;
	private TextView mTransTime;
	private TextView mTransType;
	private TextView mCurLeftView;
	private TextView mIncomeView;
	private TextView mCostView;
	private TextView mFreezeView;
	private TextView mUnfreezeView;
	private TextView mFreezeAmount;
	private TextView mTransDescription;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account_detail_item);
		_findViews();
		_setListener();
		_setValues();
	}
	private void _findViews() {
		mLeftMenu = (ImageView) findViewById(R.id.touzhu_leftmenu);
		TextView title = (TextView) findViewById(R.id.current_lottery);
		title.setText("我的交易明细");
		findViewById(R.id.right_divider).setVisibility(View.GONE);
		findViewById(R.id.sumbit_group_buy).setVisibility(View.GONE);
		mTransTime = (TextView)findViewById(R.id.account_trans_time);
		mTransType = (TextView)findViewById(R.id.account_trans_type);
		mCurLeftView = (TextView)findViewById(R.id.account_left_total);
		mIncomeView = (TextView)findViewById(R.id.account_income);
		mCostView = (TextView)findViewById(R.id.account_cost);
		mFreezeView = (TextView)findViewById(R.id.account_freeze);
		mUnfreezeView = (TextView)findViewById(R.id.account_unfreeze);
		mFreezeAmount = (TextView)findViewById(R.id.account_freezeamount);
		mTransDescription = (TextView)findViewById(R.id.trans_description);
	}
	private void _setValues() {
		AccountRecordEntity entity = (AccountRecordEntity) getIntent().getSerializableExtra("accountrecord");
		mTransTime.setText("交易时间：" + entity.getTransTime());
		mTransType.setText("交易类型：" + entity.getTransType());
		mCurLeftView.setText("当前余额：" + entity.getTransLeft() + "元");
		mIncomeView.setText("进账金额：" + entity.getTransInCome() + "元");
		mCostView.setText("出账金额：" + entity.getTransCost() + "元");
		mFreezeView.setText("冻结金额：" + entity.getTransFreeze()+ "元");
		mUnfreezeView.setText("解冻金额：" + entity.getTransUnfreeze()+ "元");
		mFreezeAmount.setText("冻结总额：" + entity.getTransFreezeAmount()+ "元");
		mTransDescription.setText("交易描述：" + entity.getTransDescription());
	}
	private void _setListener() {
		mLeftMenu.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.touzhu_leftmenu:
			finish();
			break;
			default:
				break;
		}
		
	}

}
