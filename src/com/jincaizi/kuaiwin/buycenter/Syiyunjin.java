package com.jincaizi.kuaiwin.buycenter;

import java.util.ArrayList;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;

import com.jincaizi.adapters.PopViewAdapter;
import com.jincaizi.R;
import com.jincaizi.kuaiwin.utils.Constants;
import com.jincaizi.kuaiwin.utils.Constants.ShiyiyunType;
import com.jincaizi.kuaiwin.utils.IntentAction;


public class Syiyunjin extends FragmentActivity implements OnClickListener {
	private ArrayList<Boolean> mChecked = new ArrayList<Boolean>();
	private TextView mTitleView;
	private PopupWindow mPopWindow;
	private GridView mGridView;
	private PopViewAdapter mMyAdapter;
	private boolean isPopWindowShow = false;
	private Fragment mCurrentFragment;
	private TextView mShakeBtn;
	public ShiyiyunType syyType = Constants.ShiyiyunType.ANYTWO;
	int lastIndex = 0;
	private TextView mZhuShuView;
	private int mCount = 0;
	private TextView right_footer_btn;
	private int startType = 0; // 0, normal; 1, continuePiack; 2, selectedAgain
	private RelativeLayout clearPick;
	private ImageView mBack;
	private TextView mYilouView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pls);
		mChecked.add(true);
		for (int i = 1; i < 12; i++) {
			mChecked.add(false);
		}
		_findViews();
		_setListner();
		_loadFragment();
	}

	private void _setListner() {
		// TODO Auto-generated method stub
		mTitleView.setOnClickListener(this);
		mShakeBtn.setOnClickListener(this);
		right_footer_btn.setOnClickListener(this);
		clearPick.setOnClickListener(this);
		mBack.setOnClickListener(this);
		mYilouView.setOnClickListener(this);
	}

	private void _findViews() {
		// TODO Auto-generated method stub
		mTitleView = (TextView) findViewById(R.id.current_lottery);
		mTitleView.setText("十一运夺金-任选二");
		mTitleView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.triangle_white, 0);
		mTitleView.setCompoundDrawablePadding(4);
		mShakeBtn = (TextView) findViewById(R.id.my_pls_shake_pick);
		mZhuShuView = (TextView) findViewById(R.id.bet_zhushu);
		right_footer_btn = (TextView) findViewById(R.id.right_footer_btn);
		clearPick = (RelativeLayout) findViewById(R.id.left_footer_btn);
		mBack = (ImageView) findViewById(R.id.touzhu_leftmenu);
		mYilouView = (TextView)findViewById(R.id.sumbit_group_buy);
		mYilouView.setText(this.getResources().getString(R.string.yilou));
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}
	private void _loadFragment() {
		Intent intent = getIntent();
		if (!TextUtils.isEmpty(intent.getAction())
				&& intent.getAction().equals(IntentAction.CONTINUEPICKBALL)) {
			startType = 1;
			_showFragments(AnytwoFragment.TAG);
			syyType = ShiyiyunType.ANYTWO;
			mTitleView.setText("十一运夺金-任选二");
			int lastIndex = mChecked.indexOf(true);
			if(lastIndex != -1) {
			mChecked.set(lastIndex, false);
			}
			mChecked.set(0, true);
		} else if (!TextUtils.isEmpty(intent.getAction())
				&& intent.getAction().equals(IntentAction.RETRYPICKBALL)) {
			startType = 2;
			String ballStr = intent.getStringExtra(ShiyiyunPick.BALL);
			String betType = intent.getStringExtra(ShiyiyunPick.BETTYPE);
			if (betType.equals(ShiyiyunType.ANYTWO.toString())) {
				_showFragments(AnytwoFragment.TAG);
				AnytwoFragment zhixuanFragment = ((AnytwoFragment) mCurrentFragment);
				zhixuanFragment.updateBallData(ballStr);
				syyType = ShiyiyunType.ANYTWO;
				mTitleView.setText("十一运夺金-任选二");
				int lastIndex = mChecked.indexOf(true);
				if(lastIndex != -1) {
				mChecked.set(lastIndex, false);
				}
				mChecked.set(0, true);
			} else if (betType.equals(ShiyiyunType.ANYTHREE.toString())) {
				_showFragments(AnythreeFragment.TAG);
				AnythreeFragment zhixuanFragment = ((AnythreeFragment) mCurrentFragment);
				zhixuanFragment.updateBallData(ballStr);
				syyType = ShiyiyunType.ANYTHREE;
				mTitleView.setText("十一运夺金-任选三");
				int lastIndex = mChecked.indexOf(true);
				if(lastIndex != -1) {
				mChecked.set(lastIndex, false);
				}
				mChecked.set(1, true);
			}if (betType.equals(ShiyiyunType.ANYFOUR.toString())) {
				_showFragments(AnyfourFragment.TAG);
				AnyfourFragment zhixuanFragment = ((AnyfourFragment) mCurrentFragment);
				zhixuanFragment.updateBallData(ballStr);
				syyType = ShiyiyunType.ANYFOUR;
				mTitleView.setText("十一运夺金-任选四");
				int lastIndex = mChecked.indexOf(true);
				if(lastIndex != -1) {
				mChecked.set(lastIndex, false);
				}
				mChecked.set(2, true);
			}if (betType.equals(ShiyiyunType.ANYFIVE.toString())) {
				_showFragments(AnyfiveFragment.TAG);
				AnyfiveFragment zhixuanFragment = ((AnyfiveFragment) mCurrentFragment);
				zhixuanFragment.updateBallData(ballStr);
				syyType = ShiyiyunType.ANYFIVE;
				mTitleView.setText("十一运夺金-任选五");
				int lastIndex = mChecked.indexOf(true);
				if(lastIndex != -1) {
				mChecked.set(lastIndex, false);
				}
				mChecked.set(3, true);
			}if (betType.equals(ShiyiyunType.ANYSIX.toString())) {
				_showFragments(AnysixFragment.TAG);
				AnysixFragment zhixuanFragment = ((AnysixFragment) mCurrentFragment);
				zhixuanFragment.updateBallData(ballStr);
				syyType = ShiyiyunType.ANYSIX;
				mTitleView.setText("十一运夺金-任选六");
				int lastIndex = mChecked.indexOf(true);
				if(lastIndex != -1) {
				mChecked.set(lastIndex, false);
				}
				mChecked.set(4, true);
			}if (betType.equals(ShiyiyunType.ANYSEVEN.toString())) {
				_showFragments(AnysevenFragment.TAG);
				AnysevenFragment zhixuanFragment = ((AnysevenFragment) mCurrentFragment);
				zhixuanFragment.updateBallData(ballStr);
				syyType = ShiyiyunType.ANYSEVEN;
				mTitleView.setText("十一运夺金-任选七");
				int lastIndex = mChecked.indexOf(true);
				if(lastIndex != -1) {
				mChecked.set(lastIndex, false);
				}
				mChecked.set(5, true);
			}if (betType.equals(ShiyiyunType.ANYEIGHT.toString())) {
				_showFragments(AnyeightFragment.TAG);
				AnyeightFragment zhixuanFragment = ((AnyeightFragment) mCurrentFragment);
				zhixuanFragment.updateBallData(ballStr);
				syyType = ShiyiyunType.ANYEIGHT;
				mTitleView.setText("十一运夺金-任选八");
				int lastIndex = mChecked.indexOf(true);
				if(lastIndex != -1) {
				mChecked.set(lastIndex, false);
				}
				mChecked.set(6, true);
			}
			if (betType.equals(ShiyiyunType.FRONTONEZHI.toString())) {
				_showFragments(FrontOnezhixuanFragment.TAG);
				FrontOnezhixuanFragment zhixuanFragment = ((FrontOnezhixuanFragment) mCurrentFragment);
				zhixuanFragment.updateBallData(ballStr);
				syyType = ShiyiyunType.FRONTONEZHI;
				mTitleView.setText("十一运夺金-前一直选");
				int lastIndex = mChecked.indexOf(true);
				if(lastIndex != -1) {
				mChecked.set(lastIndex, false);
				}
				mChecked.set(7, true);
			}if (betType.equals(ShiyiyunType.FRONTTWOZHI.toString())) {
				_showFragments(FrontTwozhixuanFragment.TAG);
				FrontTwozhixuanFragment zhixuanFragment = ((FrontTwozhixuanFragment) mCurrentFragment);
				zhixuanFragment.updateBallData(ballStr);
				syyType = ShiyiyunType.FRONTTWOZHI;
				mTitleView.setText("十一运夺金-前二直选");
				int lastIndex = mChecked.indexOf(true);
				if(lastIndex != -1) {
				mChecked.set(lastIndex, false);
				}
				mChecked.set(8, true);
			}if (betType.equals(ShiyiyunType.FRONTTWOZU.toString())) {
				_showFragments(FrontTwozuxuanFragment.TAG);
				FrontTwozuxuanFragment zhixuanFragment = ((FrontTwozuxuanFragment) mCurrentFragment);
				zhixuanFragment.updateBallData(ballStr);
				syyType = ShiyiyunType.FRONTTWOZU;
				mTitleView.setText("十一运夺金-前二组选");
				int lastIndex = mChecked.indexOf(true);
				if(lastIndex != -1) {
				mChecked.set(lastIndex, false);
				}
				mChecked.set(9, true);
			}if (betType.equals(ShiyiyunType.FRONTTHREEZHI.toString())) {
				_showFragments(FrontThreezhixuanFragment.TAG);
				FrontThreezhixuanFragment zhixuanFragment = ((FrontThreezhixuanFragment) mCurrentFragment);
				zhixuanFragment.updateBallData(ballStr);
				syyType = ShiyiyunType.ANYTHREE;
				mTitleView.setText("十一运夺金-前三直选");
				int lastIndex = mChecked.indexOf(true);
				if(lastIndex != -1) {
				mChecked.set(lastIndex, false);
				}
				mChecked.set(10, true);
			}if (betType.equals(ShiyiyunType.FRONTTHREEZU.toString())) {
				_showFragments(FrontThreezuxuanFragment.TAG);
				FrontThreezuxuanFragment zhixuanFragment = ((FrontThreezuxuanFragment) mCurrentFragment);
				zhixuanFragment.updateBallData(ballStr);
				syyType = ShiyiyunType.FRONTTHREEZU;
				mTitleView.setText("十一运夺金-前三组选");
				int lastIndex = mChecked.indexOf(true);
				if(lastIndex != -1) {
				mChecked.set(lastIndex, false);
				}
				mChecked.set(11, true);
			}
		} else {
			startType = 0;
			_showFragments(AnytwoFragment.TAG);
			syyType = ShiyiyunType.ANYTWO;
			mTitleView.setText("十一运夺金-任选二");
			int lastIndex = mChecked.indexOf(true);
			if(lastIndex != -1) {
			mChecked.set(lastIndex, false);
			}
			mChecked.set(0, true);
		}
	}

	public void setTouzhuResult(int count) {
		mZhuShuView.setText(String.valueOf(count));
	}

	private void _initBallStr() {
		if (mCurrentFragment.getTag().equals(AnytwoFragment.TAG)) {
			AnytwoFragment zhixuanFragment = ((AnytwoFragment) mCurrentFragment);
			mCount = zhixuanFragment.mZhushu;
		} else if (mCurrentFragment.getTag().equals(AnythreeFragment.TAG)) {
			AnythreeFragment zusanFragment = ((AnythreeFragment) mCurrentFragment);
			mCount = zusanFragment.mZhushu;
		} else if (mCurrentFragment.getTag().equals(AnyfourFragment.TAG)) {
			AnyfourFragment zuliuFragment = ((AnyfourFragment) mCurrentFragment);
			mCount = zuliuFragment.mZhushu;
		} else if (mCurrentFragment.getTag().equals(AnyfiveFragment.TAG)) {
			AnyfiveFragment hzzxFragment = ((AnyfiveFragment) mCurrentFragment);
			mCount = hzzxFragment.mZhushu;
		} else if (mCurrentFragment.getTag().equals(AnysixFragment.TAG)) {
			AnysixFragment hzzsFragment = ((AnysixFragment) mCurrentFragment);
			mCount = hzzsFragment.mZhushu;
		} else if (mCurrentFragment.getTag().equals(AnysevenFragment.TAG)) {
			AnysevenFragment hzzsFragment = ((AnysevenFragment) mCurrentFragment);
			mCount = hzzsFragment.mZhushu;
		}  else if (mCurrentFragment.getTag().equals(AnyeightFragment.TAG)) {
			AnyeightFragment hzzsFragment = ((AnyeightFragment) mCurrentFragment);
			mCount = hzzsFragment.mZhushu;
		} else if (mCurrentFragment.getTag().equals(FrontOnezhixuanFragment.TAG)) {
			FrontOnezhixuanFragment hzzsFragment = ((FrontOnezhixuanFragment) mCurrentFragment);
			mCount = hzzsFragment.mZhushu;
		} else if (mCurrentFragment.getTag().equals(FrontTwozhixuanFragment.TAG)) {
			FrontTwozhixuanFragment hzzsFragment = ((FrontTwozhixuanFragment) mCurrentFragment);
			mCount = hzzsFragment.mZhushu;
		} else if (mCurrentFragment.getTag().equals(FrontTwozuxuanFragment.TAG)) {
			FrontTwozuxuanFragment hzzsFragment = ((FrontTwozuxuanFragment) mCurrentFragment);
			mCount = hzzsFragment.mZhushu;
		} else if (mCurrentFragment.getTag().equals(FrontThreezhixuanFragment.TAG)) {
			FrontThreezhixuanFragment hzzsFragment = ((FrontThreezhixuanFragment) mCurrentFragment);
			mCount = hzzsFragment.mZhushu;
		} else {
			FrontThreezuxuanFragment hzzlFragment = ((FrontThreezuxuanFragment) mCurrentFragment);
			mCount = hzzlFragment.mZhushu;
		}
		
	}

	private void _clearChoose() {
		if (mCurrentFragment.getTag().equals(AnytwoFragment.TAG)) {
			AnytwoFragment zhixuanFragment = ((AnytwoFragment) mCurrentFragment);
			zhixuanFragment.clearChoose();
		} else if (mCurrentFragment.getTag().equals(AnythreeFragment.TAG)) {
			AnythreeFragment zusanFragment = ((AnythreeFragment) mCurrentFragment);
			zusanFragment.clearChoose();
		} else if (mCurrentFragment.getTag().equals(AnyfourFragment.TAG)) {
			AnyfourFragment zuliuFragment = ((AnyfourFragment) mCurrentFragment);
			zuliuFragment.clearChoose();
		} else if (mCurrentFragment.getTag().equals(AnyfiveFragment.TAG)) {
			AnyfiveFragment hzzxFragment = ((AnyfiveFragment) mCurrentFragment);
			hzzxFragment.clearChoose();
		} else if (mCurrentFragment.getTag().equals(AnysixFragment.TAG)) {
			AnysixFragment hzzsFragment = ((AnysixFragment) mCurrentFragment);
			hzzsFragment.clearChoose();
		} else if (mCurrentFragment.getTag().equals(AnysevenFragment.TAG)) {
			AnysevenFragment hzzsFragment = ((AnysevenFragment) mCurrentFragment);
			hzzsFragment.clearChoose();
		}  else if (mCurrentFragment.getTag().equals(AnyeightFragment.TAG)) {
			AnyeightFragment hzzsFragment = ((AnyeightFragment) mCurrentFragment);
			hzzsFragment.clearChoose();
		} else if (mCurrentFragment.getTag().equals(FrontOnezhixuanFragment.TAG)) {
			FrontOnezhixuanFragment hzzsFragment = ((FrontOnezhixuanFragment) mCurrentFragment);
			hzzsFragment.clearChoose();
		} else if (mCurrentFragment.getTag().equals(FrontTwozhixuanFragment.TAG)) {
			FrontTwozhixuanFragment hzzsFragment = ((FrontTwozhixuanFragment) mCurrentFragment);
			hzzsFragment.clearChoose();
		} else if (mCurrentFragment.getTag().equals(FrontTwozuxuanFragment.TAG)) {
			FrontTwozuxuanFragment hzzsFragment = ((FrontTwozuxuanFragment) mCurrentFragment);
			hzzsFragment.clearChoose();
		} else if (mCurrentFragment.getTag().equals(FrontThreezhixuanFragment.TAG)) {
			FrontThreezhixuanFragment hzzsFragment = ((FrontThreezhixuanFragment) mCurrentFragment);
			hzzsFragment.clearChoose();
		} else {
			FrontThreezuxuanFragment hzzlFragment = ((FrontThreezuxuanFragment) mCurrentFragment);
			hzzlFragment.clearChoose();
		}
	}
	private void _updateShakePick() {
		if (mCurrentFragment.getTag().equals(AnytwoFragment.TAG)) {
			AnytwoFragment zhixuanFragment = ((AnytwoFragment) mCurrentFragment);
			zhixuanFragment.updateBallData();
		} else if (mCurrentFragment.getTag().equals(AnythreeFragment.TAG)) {
			AnythreeFragment zusanFragment = ((AnythreeFragment) mCurrentFragment);
			zusanFragment.updateBallData();
		} else if (mCurrentFragment.getTag().equals(AnyfourFragment.TAG)) {
			AnyfourFragment zuliuFragment = ((AnyfourFragment) mCurrentFragment);
			zuliuFragment.updateBallData();
		} else if (mCurrentFragment.getTag().equals(AnyfiveFragment.TAG)) {
			AnyfiveFragment hzzxFragment = ((AnyfiveFragment) mCurrentFragment);
			hzzxFragment.updateBallData();
		} else if (mCurrentFragment.getTag().equals(AnysixFragment.TAG)) {
			AnysixFragment hzzsFragment = ((AnysixFragment) mCurrentFragment);
			hzzsFragment.updateBallData();
		} else if (mCurrentFragment.getTag().equals(AnysevenFragment.TAG)) {
			AnysevenFragment hzzsFragment = ((AnysevenFragment) mCurrentFragment);
			hzzsFragment.updateBallData();
		}  else if (mCurrentFragment.getTag().equals(AnyeightFragment.TAG)) {
			AnyeightFragment hzzsFragment = ((AnyeightFragment) mCurrentFragment);
			hzzsFragment.updateBallData();
		} else if (mCurrentFragment.getTag().equals(FrontOnezhixuanFragment.TAG)) {
			FrontOnezhixuanFragment hzzsFragment = ((FrontOnezhixuanFragment) mCurrentFragment);
			hzzsFragment.updateBallData();
		} else if (mCurrentFragment.getTag().equals(FrontTwozhixuanFragment.TAG)) {
			FrontTwozhixuanFragment hzzsFragment = ((FrontTwozhixuanFragment) mCurrentFragment);
			hzzsFragment.updateBallData();
		} else if (mCurrentFragment.getTag().equals(FrontTwozuxuanFragment.TAG)) {
			FrontTwozuxuanFragment hzzsFragment = ((FrontTwozuxuanFragment) mCurrentFragment);
			hzzsFragment.updateBallData();
		} else if (mCurrentFragment.getTag().equals(FrontThreezhixuanFragment.TAG)) {
			FrontThreezhixuanFragment hzzsFragment = ((FrontThreezhixuanFragment) mCurrentFragment);
			hzzsFragment.updateBallData();
		} else {
			FrontThreezuxuanFragment hzzlFragment = ((FrontThreezuxuanFragment) mCurrentFragment);
			hzzlFragment.updateBallData();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.touzhu_leftmenu:
			finish();
			break;
		case R.id.left_footer_btn:
			_clearChoose();
			break;
		case R.id.my_pls_shake_pick:
			_updateShakePick();
			break;
		case R.id.current_lottery:
			if (!isPopWindowShow) {
				_setPopWindow((int) (v.getWidth() * 1.5 + 0.5f));
				isPopWindowShow = true;
			}
			mPopWindow.showAsDropDown(v, -(v.getWidth() / 4), v.getTop());
			break;
		case R.id.right_footer_btn:
			_initBallStr();
			if (mCount < 1) {
				Toast.makeText(getApplicationContext(), "请至少选1注",
						Toast.LENGTH_LONG).show();
			} else {
				_startActivity();
				finish();
			}
			break;
		default:
			break;
		}
	}
	private void _startActivity() {
		Intent syyPick = new Intent();
		syyPick.putExtra(ShiyiyunPick.BETTYPE, syyType.toString());
		if (syyType == ShiyiyunType.ANYTWO) {
			syyPick.putStringArrayListExtra(ShiyiyunPick.BALL,
					((AnytwoFragment) mCurrentFragment)
							.getPlsResultList());
		} else if (syyType == ShiyiyunType.ANYTHREE) {
			syyPick.putStringArrayListExtra(ShiyiyunPick.BALL,
					((AnythreeFragment) mCurrentFragment)
							.getPlsResultList());
		} else if (syyType == ShiyiyunType.ANYFOUR) {
			syyPick.putStringArrayListExtra(ShiyiyunPick.BALL,
					((AnyfourFragment) mCurrentFragment)
							.getPlsResultList());
		} else if (syyType == ShiyiyunType.ANYFIVE) {
			syyPick.putStringArrayListExtra(ShiyiyunPick.BALL,
					((AnyfiveFragment) mCurrentFragment)
							.getPlsResultList());
		} else if (syyType == ShiyiyunType.ANYSIX) {
			syyPick.putStringArrayListExtra(ShiyiyunPick.BALL,
					((AnysixFragment) mCurrentFragment)
							.getPlsResultList());
		} else if (syyType == ShiyiyunType.ANYSEVEN) {
			syyPick.putStringArrayListExtra(ShiyiyunPick.BALL,
					((AnysevenFragment) mCurrentFragment)
							.getPlsResultList());
		} else if (syyType == ShiyiyunType.ANYEIGHT) {
			syyPick.putStringArrayListExtra(ShiyiyunPick.BALL,
					((AnyeightFragment) mCurrentFragment)
							.getPlsResultList());
		} else if (syyType == ShiyiyunType.FRONTONEZHI) {
			syyPick.putStringArrayListExtra(ShiyiyunPick.BALL,
					((FrontOnezhixuanFragment) mCurrentFragment)
							.getPlsResultList());
		} else if (syyType == ShiyiyunType.FRONTTWOZHI) {
			syyPick.putStringArrayListExtra(ShiyiyunPick.BALL,
					((FrontTwozhixuanFragment) mCurrentFragment)
							.getPlsResultList());
		} else if (syyType == ShiyiyunType.FRONTTWOZU) {
			syyPick.putStringArrayListExtra(ShiyiyunPick.BALL,
					((FrontTwozuxuanFragment) mCurrentFragment)
							.getPlsResultList());
		} else if (syyType == ShiyiyunType.FRONTTHREEZHI) {
			syyPick.putStringArrayListExtra(ShiyiyunPick.BALL,
					((FrontThreezhixuanFragment) mCurrentFragment)
							.getPlsResultList());
		} 
		else {
			syyPick.putStringArrayListExtra(ShiyiyunPick.BALL,
					((FrontThreezuxuanFragment) mCurrentFragment)
							.getPlsResultList());
		}
		if (startType == 0) {
			syyPick.setClass(Syiyunjin.this, ShiyiyunPick.class);
			startActivity(syyPick);
		} else if (startType == 1) {
			setResult(RESULT_OK, syyPick);
		} else if (startType == 2) {
			setResult(RESULT_OK, syyPick);
		}
	}

	private void _setPopWindow(int width) {
		View view = LayoutInflater.from(this).inflate(R.layout.popview, null);
		mPopWindow = new PopupWindow(view, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		mGridView = (GridView) view.findViewById(R.id.pop_gridview);
		ArrayList<String> list = new ArrayList<String>();
		list.add("任二");
		list.add("任三");
		list.add("任四");
		list.add("任五");
		list.add("任六");
		list.add("任七");
		list.add("任八");
		list.add("前一直选");
		list.add("前二直选");
		list.add("前二组选");
		list.add("前三直选");
		list.add("前三组选");
		mMyAdapter = new PopViewAdapter(this, list, mChecked);
		mGridView.setAdapter(mMyAdapter);
		mGridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (arg2 == 0) {

					mTitleView.setText("十一运夺金-任二");
					syyType = Constants.ShiyiyunType.ANYTWO;
					_showFragments(AnytwoFragment.TAG);
					
				} else if (arg2 == 1) {

					mTitleView.setText("十一运夺金-任三");
					syyType = Constants.ShiyiyunType.ANYTHREE;
					_showFragments(AnythreeFragment.TAG);
				} else if (arg2 == 2) {

					mTitleView.setText("十一运夺金-任四");
					syyType = Constants.ShiyiyunType.ANYFOUR;
					_showFragments(AnyfourFragment.TAG);
				} else if (arg2 == 3) {

					mTitleView.setText("十一运夺金-任五");
					syyType = Constants.ShiyiyunType.ANYFIVE;
					_showFragments(AnyfiveFragment.TAG);
				} else if (arg2 == 4) {

					mTitleView.setText("十一运夺金-任六");
					syyType = Constants.ShiyiyunType.ANYSIX;
					_showFragments(AnysixFragment.TAG);
				} else if(arg2 == 5){

					mTitleView.setText("十一运夺金-任七");
					syyType = Constants.ShiyiyunType.ANYSEVEN;
					_showFragments(AnysevenFragment.TAG);
				}
				else if(arg2 == 6){

					mTitleView.setText("十一运夺金-任八");
					syyType = Constants.ShiyiyunType.ANYEIGHT;
					_showFragments(AnyeightFragment.TAG);
				}
				else if(arg2 == 7){

					mTitleView.setText("十一运夺金-前一直选");
					syyType = Constants.ShiyiyunType.FRONTONEZHI;
					_showFragments(FrontOnezhixuanFragment.TAG);
				}else if(arg2 == 8){

					mTitleView.setText("十一运夺金-前二直选");
					syyType = Constants.ShiyiyunType.FRONTTWOZHI;
					_showFragments(FrontTwozhixuanFragment.TAG);
				}else if(arg2 == 9){

					mTitleView.setText("十一运夺金-前二组选");
					syyType = Constants.ShiyiyunType.FRONTTWOZU;
					_showFragments(FrontTwozuxuanFragment.TAG);
				}else if(arg2 == 10){

					mTitleView.setText("十一运夺金-前三直选");
					syyType = Constants.ShiyiyunType.FRONTTHREEZHI;
					_showFragments(FrontThreezhixuanFragment.TAG);
				}else if(arg2 == 11){

					mTitleView.setText("十一运夺金-前三组选");
					syyType = Constants.ShiyiyunType.FRONTTHREEZU;
					_showFragments(FrontThreezuxuanFragment.TAG);
				}
				int lastIndex = mChecked.indexOf(true);
				if(lastIndex != -1) {
				    mChecked.set(lastIndex, false);
				}
				mChecked.set(arg2, true);
				mMyAdapter.notifyDataSetChanged();
				mPopWindow.dismiss();
			}
		});
		mPopWindow.setFocusable(true);
		mPopWindow.setOutsideTouchable(true);
		mPopWindow.update();
		mPopWindow.setBackgroundDrawable(new BitmapDrawable(getResources(),
				Bitmap.createBitmap(1, 1, Config.ARGB_8888)));
	}

	private void _showFragments(String fragmentTag) {
		FragmentManager mFragManager = getSupportFragmentManager();
		FragmentTransaction mFragTransaction = mFragManager.beginTransaction();
		Fragment mFragment = (Fragment) mFragManager
				.findFragmentByTag(fragmentTag);
		if (mFragment == null) {
			if (fragmentTag.equals(AnytwoFragment.TAG)) {
				mFragment = new AnytwoFragment();
			} else if (fragmentTag.equals(AnythreeFragment.TAG)) {
				mFragment = new AnythreeFragment();
			} else if (fragmentTag.equals(AnyfourFragment.TAG)) {
				mFragment = new AnyfourFragment();
			} else if (fragmentTag.equals(AnyfiveFragment.TAG)) {
				mFragment = new AnyfiveFragment();
			} else if (fragmentTag.equals(AnysixFragment.TAG)) {
				mFragment = new AnysixFragment();
			} else if (fragmentTag.equals(AnysevenFragment.TAG)) {
				mFragment = new AnysevenFragment();
			}else if (fragmentTag.equals(AnyeightFragment.TAG)) {
				mFragment = new AnyeightFragment();
			}else if (fragmentTag.equals(FrontOnezhixuanFragment.TAG)) {
				mFragment = new FrontOnezhixuanFragment();
			}else if (fragmentTag.equals(FrontTwozhixuanFragment.TAG)) {
				mFragment = new FrontTwozhixuanFragment();
			}else if (fragmentTag.equals(FrontTwozuxuanFragment.TAG)) {
				mFragment = new FrontTwozuxuanFragment();
			}else if (fragmentTag.equals(FrontThreezhixuanFragment.TAG)) {
				mFragment = new FrontThreezhixuanFragment();
			}
			else{
				mFragment = new FrontThreezuxuanFragment();
			}
			mFragTransaction.add(R.id.fl_pls_pickarea, mFragment, fragmentTag);
		}
		if (mCurrentFragment != null) {
			mFragTransaction.hide(mCurrentFragment);
		}
		mFragTransaction.show(mFragment);
		mCurrentFragment = mFragment;
		mFragTransaction.commit();
	}
}
