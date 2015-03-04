package com.jincaizi.kuaiwin.buycenter.sharexicai;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;

import org.apache.http.Header;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.stream.JsonReader;
import com.jincaizi.R;
import com.jincaizi.adapters.PopViewAdapter;
import com.jincaizi.http.JinCaiZiHttpClient;
import com.jincaizi.kuaiwin.utils.IntentAction;
import com.jincaizi.kuaiwin.utils.Utils;
import com.jincaizi.vendor.http.AsyncHttpClient;
import com.jincaizi.vendor.http.AsyncHttpResponseHandler;
import com.jincaizi.vendor.http.RequestHandle;
import com.jincaizi.vendor.http.RequestParams;

public class Ssq extends FragmentActivity implements OnClickListener {

	protected static final String TAG = "Ssq";
	private boolean isPopWindowShow = false;
	public TextView bet_zhushu;
	public TextView amount;
	private TextView clear_pick;
	private TextView right_footer_btn;
	private ArrayList<Boolean> mChecked = new ArrayList<Boolean>();
	private TextView mTitleView;
	private TextView mShakePick;
	private ImageView mBack;
	ArrayList<String> mRedBall;
	private ArrayList<String> mBlueBall;
	private TextView mZhuShuView;
	private boolean mIsNormalType = true;
	private int startType = 0; // 0, normal; 1, continuePiack; 2, selectedAgain
	private TextView mQihaoView;
	private TextView mTimeDiffView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ssq);
		
		mChecked.add(true);
		mChecked.add(false);
		_findViews();
		_setListner();
		_loadFragment();
		_requestData();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
	}
private void _loadFragment() {
	Intent intent = getIntent();
	if (!TextUtils.isEmpty(intent.getAction())
			&& intent.getAction().equals(IntentAction.FIRSTPICKBALL)) {
		startType = 0;
		_showFragments(DoubleColorNormal.TAG);
	} else if (!TextUtils.isEmpty(intent.getAction())
			&& intent.getAction().equals(IntentAction.CONTINUEPICKBALL)) {
		startType = 1;
		_showFragments(DoubleColorNormal.TAG);
	} else if (!TextUtils.isEmpty(intent.getAction())
			&& intent.getAction().equals(IntentAction.RETRYPICKBALL)) {
		startType = 2;
		ArrayList<String> redBall = intent
				.getStringArrayListExtra(SsqPick.REDBALL);
		ArrayList<String> blueBall = intent
				.getStringArrayListExtra(SsqPick.BLUEBALL);
		if (intent.getBooleanExtra(SsqPick.BETTYPE, true)) {
			_showFragments(DoubleColorNormal.TAG);
			DoubleColorNormal normalFragment = ((DoubleColorNormal) mCurrentFragment);
			normalFragment.updateBallData(redBall, blueBall);
			mShakePick.setVisibility(View.VISIBLE);
			mTitleView.setText("双色球-普通投注");
			mChecked.set(0, true);
			mChecked.set(1, false);
			mIsNormalType = true;
		} else {
			mIsNormalType = false;
			_showFragments(DoubleColorDrag.TAG);
			mShakePick.setVisibility(View.GONE);
			mTitleView.setText("双色球-托胆投注");
			mChecked.set(0, false);
			mChecked.set(1, true);
			int index = redBall.indexOf(")");
			DoubleColorDrag dragFragment = ((DoubleColorDrag) mCurrentFragment);
			ArrayList<String> dragRedDan = new ArrayList<String>(
					redBall.subList(0, index));
			ArrayList<String> dragRedTuo = new ArrayList<String>(
					redBall.subList(index + 1, redBall.size()));
			dragFragment.updateBallData(dragRedDan, dragRedTuo, blueBall);
		}
	}
}
	private void _findViews() {
		clear_pick = (TextView) findViewById(R.id.left_footer_btn);
		right_footer_btn = (TextView) findViewById(R.id.right_footer_btn);
		mTitleView = (TextView) findViewById(R.id.current_lottery);
		mTitleView.setText("双色球-普通投注");
		mShakePick = (TextView) findViewById(R.id.my_shake_pick);
		mBack = (ImageView) findViewById(R.id.touzhu_leftmenu);
		mZhuShuView = (TextView) findViewById(R.id.bet_zhushu);
		mQihaoView = (TextView)findViewById(R.id.pre_num_str);
		mTimeDiffView = (TextView)findViewById(R.id.pre_win_num);

	}

	public void setTouzhuResult(int count) {
		mZhuShuView.setText(String.valueOf(count) + "注" + 2*count + "元");
	}

	private void _setListner() {
		clear_pick.setOnClickListener(this);
		right_footer_btn.setOnClickListener(this);
		mTitleView.setOnClickListener(this);
		mShakePick.setOnClickListener(this);
		mBack.setOnClickListener(this);
		mQihaoView.setOnClickListener(this);
	}

	public void setTotalAndAmountText(String str0, String str1) {
		bet_zhushu.setText(str0);
		amount.setText(str1);
	}

	private Fragment mCurrentFragment;
	private PopupWindow mPopWindow;
	private GridView mGridView;
	private PopViewAdapter mMyAdapter;
	private int mCount = 0;
	private ArrayList<String> mRedDan;
	private ArrayList<String> mRedTuo;
	private RequestHandle myRequestHandle;
	private boolean isCanSale = true;
	private String mQihao = "";
	private MyCount mc = null;

	private void _showFragments(String fragmentTag) {
		FragmentManager mFragManager = getSupportFragmentManager();
		FragmentTransaction mFragTransaction = mFragManager.beginTransaction();
		Fragment mFragment = (Fragment) mFragManager
				.findFragmentByTag(fragmentTag);
		if (mFragment == null) {
			if (fragmentTag.equals(DoubleColorNormal.TAG)) {
				mFragment = new DoubleColorNormal();
			} else {
				mFragment = new DoubleColorDrag();
			}
			mFragTransaction.add(R.id.fl_doublecolor_pickarea, mFragment,
					fragmentTag);
		}
		if (mCurrentFragment != null) {
			mFragTransaction.hide(mCurrentFragment);
		}
		mFragTransaction.show(mFragment);
		mCurrentFragment = mFragment;
		mFragTransaction.commit();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.pre_num_str:
			if(TextUtils.isEmpty(mQihaoView.getText()) || !mQihaoView.getText().toString().equals("正在获取当前期号")) {
			_requestData();
			}
			break;
		case R.id.left_footer_btn:
			if (mCurrentFragment.getTag().equals(DoubleColorNormal.TAG)) {
				((DoubleColorNormal) mCurrentFragment).clearChoose();
			} else {
				((DoubleColorDrag) mCurrentFragment).clearChoose();
			}
			break;
		case R.id.current_lottery:
			if (!isPopWindowShow) {
				_setPopWindow((int) (v.getWidth() * 1.5 + 0.5f));
				isPopWindowShow = true;
			}
			mPopWindow.showAsDropDown(v, -(v.getWidth() / 4), v.getTop());
			break;
		case R.id.my_shake_pick:
			if (mCurrentFragment.getTag().equals(DoubleColorNormal.TAG)) {
				((DoubleColorNormal) mCurrentFragment).updateBallData();
			}
			break;
		case R.id.touzhu_leftmenu:
			if(mc != null) {
				mc.cancel();
			}
			finish();
			break;
		case R.id.right_footer_btn:
			if(!isCanSale) {
				Toast.makeText(getApplicationContext(), "本期销售已停止",Toast.LENGTH_SHORT).show();
				break;
			}
			if(TextUtils.isEmpty(mQihao)) {
				Toast.makeText(getApplicationContext(), "未获取到当前期号",Toast.LENGTH_SHORT).show();
				break;
			}
			_initBallStr();
			if (mCount < 1) {
				Toast.makeText(getApplicationContext(), "请至少选1注",
						Toast.LENGTH_LONG).show();
			} else {

				Intent startSsqPick = new Intent();
				Collections.sort(mBlueBall);
				startSsqPick.putStringArrayListExtra(SsqPick.BLUEBALL,
						mBlueBall);
				startSsqPick.putExtra(SsqPick.COUNT, mCount);
				startSsqPick.putExtra(SsqPick.BETTYPE, mIsNormalType);
				startSsqPick.putExtra("qihao", mQihao);
				if (mIsNormalType) {
					Collections.sort(mRedBall);
					startSsqPick.putStringArrayListExtra(SsqPick.REDBALL,
							mRedBall);
				} else {
					Collections.sort(mRedDan);
					Collections.sort(mRedTuo);
					startSsqPick.putStringArrayListExtra(SsqPick.REDDANBALL,
							mRedDan);
					startSsqPick.putStringArrayListExtra(SsqPick.REDTUOBALL,
							mRedTuo);
				}
				if (startType == 0) {
					startSsqPick.setClass(Ssq.this, SsqPick.class);
					startActivity(startSsqPick);
				} else if (startType == 1) {
					setResult(RESULT_OK, startSsqPick);
				} else if (startType == 2) {
					setResult(RESULT_OK, startSsqPick);
				}
				if(mc != null) {
					mc.cancel();
				}
				finish();
			}
			break;
		default:
			break;
		}
	}

	private void _initBallStr() {
		if (mCurrentFragment.getTag().equals(DoubleColorNormal.TAG)) {
			mIsNormalType = true;
			DoubleColorNormal normalFragment = ((DoubleColorNormal) mCurrentFragment);
			mRedBall = normalFragment.getmRedBall();
			mBlueBall = normalFragment.getmBlueBall();
			mCount = normalFragment.mZhushu;
			// mCount = Utils.getZuHeNum(mRedBall.size(), 6) * mBlueBall.size();
		} else {
			mIsNormalType = false;
			DoubleColorDrag dragFragment = ((DoubleColorDrag) mCurrentFragment);
			mRedBall = dragFragment.getmRedAllBall();
			mRedDan = dragFragment.getmRedDanBall();
			mRedTuo = dragFragment.getmRedTuoBall();
			mBlueBall = dragFragment.getmBlueTuoBall();
			// int redDanNum = dragFragment.getmRedDanBall().size();
			mCount = dragFragment.mZhushu;
			// mCount = redDanNum *
			// Utils.getZuHeNum(mRedBall.size() - redDanNum, 6-redDanNum) *
			// Utils.getZuHeNum(mBlueBall.size(), 1);
		}
	}

	private void _setPopWindow(int width) {
		View view = LayoutInflater.from(this).inflate(R.layout.popview, null);
		mPopWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		mGridView = (GridView) view.findViewById(R.id.pop_gridview);
//		mGridView.setBackgroundColor(this.getResources().getColor(
//				android.R.color.transparent));
		mGridView.setNumColumns(2);
		ArrayList<String> list = new ArrayList<String>();
		list.add("普通投注");
		list.add("托胆投注");

		mMyAdapter = new PopViewAdapter(this, list, mChecked);
		mGridView.setAdapter(mMyAdapter);
		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (arg2 == 0) {
					_showFragments(DoubleColorNormal.TAG);
					mTitleView.setText("双色球-普通投注");
					setTouzhuResult(((DoubleColorNormal) mCurrentFragment).mZhushu);
					mShakePick.setVisibility(View.VISIBLE);
					mChecked.set(0, true);
					mChecked.set(1, false);
				} else {
					_showFragments(DoubleColorDrag.TAG);
					setTouzhuResult(((DoubleColorDrag) mCurrentFragment).mZhushu);
					mTitleView.setText("双色球-托胆投注");
					mChecked.set(0, false);
					mChecked.set(1, true);
					mShakePick.setVisibility(View.GONE);
				}
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
	private void _requestData() {
		mQihaoView.setText("正在获取当前期号");
		mTimeDiffView.setText("");
		RequestParams params = new RequestParams();
	    params.add("act", "sellqihao");
	    params.add("lotterytype", "SSQ");
		params.add("datatype", "json");
		params.add("jsoncallback",
				"jsonp" + String.valueOf(System.currentTimeMillis()));
		params.add("_", String.valueOf(System.currentTimeMillis()));
		String url = AsyncHttpClient.getUrlWithQueryString(false,
				JinCaiZiHttpClient.BASE_URL, params);
		myRequestHandle = JinCaiZiHttpClient.post(this, url,
				new AsyncHttpResponseHandler() {
					

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							byte[] responseBody) {
						try {
							String charset;
							if(Utils.isCmwapNet(Ssq.this)) {
								charset = "utf-8";
							} else {
								charset = "gb2312";
							}
							String jsonData = new String(responseBody, charset);
							Log.d(TAG, "ssq qihao detail = " + jsonData);
							_readQihaoFromJson(jsonData);
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
							mQihaoView.setText("获取期号失败，点击重新获取");
							mTimeDiffView.setText("");
						} catch (Exception e) {
							e.printStackTrace();
							mQihaoView.setText("获取期号失败，点击重新获取");
							mTimeDiffView.setText("");
						} finally {
							// FIXME reader.close should be here
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							byte[] responseBody, Throwable error) {
						Log.d(TAG, "failure = " + error.toString());
						mQihaoView.setText("获取期号失败，点击重新获取");
						mTimeDiffView.setText("");
						Toast.makeText(Ssq.this, "期号获取失败", Toast.LENGTH_SHORT).show();
					}
				});
	}
	private void _readQihaoFromJson(String jsonData) throws IOException {
		JsonReader reader = new JsonReader(new StringReader(jsonData));
		reader.beginObject();
		String diff = "-1";
		int result = -1;
		while(reader.hasNext()) {
			String tagName = reader.nextName();
			if(tagName.equals("msg")) {
				mQihao  = reader.nextString();
			} else if(tagName.equals("Diff")){
			    diff = reader.nextString();	
			} else if(tagName.equals("result")) {
				result = reader.nextInt();
			}
		}
		reader.endObject();
		reader.close();
		if(result == 0) {
			mQihaoView.setText("距" + mQihao + "期还有");
			if(diff.startsWith("-")) {
				isCanSale = false;
			} else {
				isCanSale = true;
				mc  = new MyCount(Long.valueOf(diff)*1000, 1000);  
		        mc.start(); 
			}
		}
	}
	   /*定义一个倒计时的内部类*/  
 class MyCount extends CountDownTimer {     
     public MyCount(long millisInFuture, long countDownInterval) {     
         super(millisInFuture, countDownInterval);     
     }     
     @Override     
     public void onFinish() {     
    	 //mQihaoView.setText("正在获取当前期号"); 
         _requestData();
     }     
     @Override     
     public void onTick(long millisUntilFinished) {  
     	//Log.d("test", millisUntilFinished+"");
    	 mTimeDiffView.setText(Utils.formatDuring(millisUntilFinished));     
        
     }    
 }
@Override
public void onBackPressed() {
	// TODO Auto-generated method stub
	super.onBackPressed();
	if(mc != null) {
		mc.cancel();
	}
	finish();
}
 
 
}
