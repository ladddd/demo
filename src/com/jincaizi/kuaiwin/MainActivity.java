package com.jincaizi.kuaiwin;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.jincaizi.R;
import com.jincaizi.kuaiwin.tool.CheckLogin;
import com.umeng.update.UmengUpdateAgent;



public class MainActivity extends FragmentActivity {
	private RadioButton mFootbarCenter;
	private RadioButton mFootbarKjxx;
	private RadioButton mFootbarJoinBuy;
	private RadioButton mFootbarMyLottery;
	private RadioButton mFooterMore;
	private Activity mActivity;
    // 接口返回键
    private FragmentCallbacks[] mFragments;
    // 模块集合
    private FrameLayout[] mFrameLayouts;
    // RadioButton集合
    private RadioButton[] mButtons;
    // 当前选中的模块
    private int mCurSel = -1;
    // 上次选中的模块
    private int mLastSel = -1;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFormat(PixelFormat.RGBA_8888);
		setContentView(R.layout.activity_main);
	
		UmengUpdateAgent.setUpdateOnlyWifi(false);
		UmengUpdateAgent.update(this);
		
		_registerView();
		//_showFragments(BuyCenter.TAG);
		mActivity = this;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	private void _registerView() {

		mFootbarCenter = (RadioButton) findViewById(R.id.radio_button0);
		mFootbarKjxx = (RadioButton) findViewById(R.id.radio_button1);
		mFootbarJoinBuy = (RadioButton) findViewById(R.id.radio_button2);
		mFootbarMyLottery = (RadioButton) findViewById(R.id.radio_button3);
		mFooterMore = (RadioButton) findViewById(R.id.radio_button4);

		   mFragments = new FragmentCallbacks[5];
	        mFrameLayouts = new FrameLayout[5];
	        mButtons = new RadioButton[5];
	        mButtons[0] = mFootbarCenter;
	        mButtons[1] = mFootbarJoinBuy;
	        mButtons[2] = mFootbarKjxx;
	        mButtons[3] = mFootbarMyLottery;
	        mButtons[4] = mFooterMore;
		
		mFootbarCenter.setOnClickListener(mBootbarOnClick);
		mFootbarKjxx.setOnClickListener(mBootbarOnClick);
		mFootbarMyLottery.setOnClickListener(mBootbarOnClick);
		mFootbarJoinBuy.setOnClickListener(mBootbarOnClick);
		mFooterMore.setOnClickListener(mBootbarOnClick);
		
		mFootbarCenter.performClick();
	}

	  private View.OnClickListener mBootbarOnClick = new View.OnClickListener() {

	        @Override
	        public void onClick(View v) {
	            mLastSel = mCurSel;
	            FragmentManager manager = getSupportFragmentManager();
	            switch (v.getId()) {
	                case R.id.radio_button0:
	                    mCurSel = 0;
	                    // 延迟加载
	                    if (mFrameLayouts[0] == null) {
	                        mFrameLayouts[0] = (FrameLayout) ((ViewStub) findViewById(R.id.vs_lotterycenter)).inflate();
	                        mFragments[0] = (FragmentCallbacks)manager.findFragmentById(R.id.lotterycenter);
	                        mFootbarCenter.setTag(true);
	                    }
	                    break;
	                case R.id.radio_button2:
	                    mCurSel = 1;
	                    // 延迟加载
	                    if (mFrameLayouts[1] == null) {
	                        mFrameLayouts[1] = (FrameLayout) ((ViewStub) findViewById(R.id.vs_hemaicenter)).inflate();
	                        mFragments[1] = (FragmentCallbacks)manager.findFragmentById(R.id.hemaicenter);
	                    }
	                    break;
	                case R.id.radio_button1:
	                    mCurSel = 2;
	                    // 延迟加载
	                    if (mFrameLayouts[2] == null) {
	                        mFrameLayouts[2] = (FrameLayout) ((ViewStub) findViewById(R.id.vs_kjcenter)).inflate();
	                        mFragments[2] = (FragmentCallbacks)manager.findFragmentById(R.id.kjcenter);
	                    }
	                    break;
	                case R.id.radio_button3:
	                    mCurSel = 3;
	                    // 延迟加载
	                    if (mFrameLayouts[3] == null) {
	                        mFrameLayouts[3] = (FrameLayout) ((ViewStub) findViewById(R.id.vs_personcenter)).inflate();
	                        mFragments[3] = (FragmentCallbacks)manager.findFragmentById(R.id.personcenter);
	                    }
	                    break;
	                case R.id.radio_button4:
	                    mCurSel = 4;
	                    // 延迟加载
	                    if (mFrameLayouts[4] == null) {
	                        mFrameLayouts[4] = (FrameLayout) ((ViewStub) findViewById(R.id.vs_morecenter)).inflate();
	                        mFragments[4] = (FragmentCallbacks)manager.findFragmentById(R.id.morecenter);
	                    }
	                    break;
	                default:
	                    break;
	            }
	            mFragments[mCurSel].onShow();
	            if (mLastSel == -1) {
	                return;
	            }
	            if (mLastSel == mCurSel) {
	                return;
	            }
	            mFrameLayouts[mCurSel].setVisibility(View.VISIBLE);
	            mFrameLayouts[mLastSel].setVisibility(View.GONE);
	            mButtons[mCurSel].setChecked(true);
	            mButtons[mLastSel].setChecked(false);
	        }
	    };

//	private Fragment mCurrentFragment;
//
//	private void _showFragments(String fragmentTag) {
//		FragmentManager mFragManager = getSupportFragmentManager();
//		FragmentTransaction mFragTransaction = mFragManager.beginTransaction();
//		Fragment mFragment = (Fragment) mFragManager
//				.findFragmentByTag(fragmentTag);
//		if (mFragment == null) {
//			if (fragmentTag.equals(BuyCenter.TAG)) {
//				mFragment = new BuyCenter();
//			} else if (fragmentTag.equals(BuyCenter.TAG)) {
//				mFragment = new BuyCenter();
//			} else if (fragmentTag.equals(BuyCenter.TAG)) {
//				mFragment = new BuyCenter();
//			} else if(fragmentTag.equals(BuyCenter.TAG)) {
//				mFragment = new BuyCenter();
//			}else {
//				mFragment = new BuyCenter();
//			}
//			mFragTransaction.add(R.id.samddyFragmentContainer, mFragment,
//					fragmentTag);
//		}
//		if (mCurrentFragment != null) {
//			mFragTransaction.hide(mCurrentFragment);
//		}
//		mFragTransaction.show(mFragment);
//		mCurrentFragment = mFragment;
//		mFragTransaction.commit();
//	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResumeFragments() {
		// TODO Auto-generated method stub
		super.onResumeFragments();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	public void onBackPressed() {
		showExitDialog();
	}
	public void showExitDialog() {
		final Dialog localDialog = new Dialog(MainActivity.this,
				R.style.Theme_dialog);
		View view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.dialog_submit_bet, null);
		TextView dialogTitle = (TextView) view
				.findViewById(R.id.submit_dialog_title);
		dialogTitle.setText("提示");
		TextView dialogContent = (TextView) view
				.findViewById(R.id.submit_dialog_content);
		dialogContent.setText("您确定要退出应用吗？");
		TextView localCancel = (TextView) view
				.findViewById(R.id.tv_submit_cancel);
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
                CheckLogin.clearLoginStatus(mActivity.getSharedPreferences("loginStatus", Context.MODE_PRIVATE));
                finish();
                ActivityManager activityMgr = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
    			String packName = getPackageName();
    			activityMgr.killBackgroundProcesses(packName);
    			android.os.Process.killProcess(android.os.Process.myPid());
    			System.exit(10);
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
}
