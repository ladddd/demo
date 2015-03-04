package com.jincaizi.kuaiwin.mylottery;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.jincaizi.R;

public class MyAccountDetail extends FragmentActivity implements OnClickListener{
    private RadioButton myAccountDetail;
	private RadioButton myAccountChongzhi;
	private RadioButton myAccountTikuan;
	//private SharedPreferences sp;
	private ImageView leftMenu;
	private Fragment mCurrentFragment;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myaccount_detail_layout);
		//sp = getSharedPreferences("loginStatus", Context.MODE_PRIVATE);
		//sp.getInt("userid", 0);
		_findViews();
		_setListener();
		_showFragments(MyAccountDetailFragment.TAG);

	}
	private void _findViews() {
		myAccountDetail = (RadioButton)findViewById(R.id.myaccount_detail);
		myAccountChongzhi = (RadioButton)findViewById(R.id.myaccount_chongzhi);
		myAccountTikuan = (RadioButton)findViewById(R.id.myaccount_tikuan);
		
		TextView title = (TextView)findViewById(R.id.current_lottery);
		  title.setText("我的账户明细");
		 findViewById(R.id.right_divider).setVisibility(View.GONE);
		 findViewById(R.id.sumbit_group_buy).setVisibility(View.GONE);
		 leftMenu = (ImageView) findViewById(R.id.touzhu_leftmenu);
		 
		  
	}
	private void _setListener() {
		myAccountDetail.setOnClickListener(this);
		myAccountChongzhi.setOnClickListener(this);
		myAccountTikuan.setOnClickListener(this);
		leftMenu.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()) {
		case R.id.touzhu_leftmenu:
			finish();
			break;
		case R.id.myaccount_detail:
			_showFragments(MyAccountDetailFragment.TAG);
	        myAccountDetail.setChecked(true);
			myAccountChongzhi.setChecked(false);
			myAccountTikuan.setChecked(false);
			break;
		case R.id.myaccount_chongzhi:
			_showFragments(MyAccountChongzhiFragment.TAG);
			 myAccountDetail.setChecked(false);
				myAccountChongzhi.setChecked(true);
				myAccountTikuan.setChecked(false);
			break;
		case R.id.myaccount_tikuan:
			_showFragments(MyAccountTikuanFragment.TAG);
			 myAccountDetail.setChecked(false);
				myAccountChongzhi.setChecked(false);
				myAccountTikuan.setChecked(true);
			break;
			default:
				break;
		}
	}
	private void _showFragments(String fragmentTag) {
		FragmentManager mFragManager = getSupportFragmentManager();
		FragmentTransaction mFragTransaction = mFragManager.beginTransaction();
		Fragment mFragment = (Fragment) mFragManager
				.findFragmentByTag(fragmentTag);
		if (mFragment == null) {
			if (fragmentTag.equals(MyAccountDetailFragment.TAG)) {
				mFragment = new MyAccountDetailFragment();
			} else if(fragmentTag.equals(MyAccountChongzhiFragment.TAG)){
				mFragment = new MyAccountChongzhiFragment();
			} else {
				mFragment = new MyAccountTikuanFragment();
			}
			mFragTransaction.add(R.id.myaccount_pickarea, mFragment,
					fragmentTag);
		}
		if (mCurrentFragment != null) {
			mFragTransaction.hide(mCurrentFragment);
		}
		mFragTransaction.show(mFragment);
		mCurrentFragment = mFragment;
		mFragTransaction.commit();
	}

}
