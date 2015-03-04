package com.jincaizi.kuaiwin.buycenter.sharexicai;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.Header;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.stream.JsonReader;
import com.jincaizi.R;
import com.jincaizi.http.JinCaiZiHttpClient;
import com.jincaizi.kuaiwin.utils.Constants;
import com.jincaizi.kuaiwin.utils.Constants.PlsType;
import com.jincaizi.kuaiwin.utils.IntentAction;
import com.jincaizi.kuaiwin.utils.Utils;
import com.jincaizi.vendor.http.AsyncHttpClient;
import com.jincaizi.vendor.http.AsyncHttpResponseHandler;
import com.jincaizi.vendor.http.RequestHandle;
import com.jincaizi.vendor.http.RequestParams;

public class Qxc extends FragmentActivity implements OnClickListener {
	protected static final String TAG = "Qxc";
	private TextView mTitleView;
	private Fragment mCurrentFragment;
	private TextView mShakeBtn;
	public PlsType plsType = Constants.PlsType.OTHER;
	int lastIndex = 0;
	private TextView mZhuShuView;
	private TextView right_footer_btn;
	private TextView clearPick;
	private ImageView mBack;
	private int mCount = 0;
	private int startType = 0; // 0, normal; 1, continuePiack; 2, selectedAgain
    private TextView mQihaoView;
	private TextView mTimeDiffView;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.pls);
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
				&& intent.getAction().equals(IntentAction.CONTINUEPICKBALL)) {
			startType = 1;
			_showFragments(QxcNormalFragment.TAG);
			plsType = PlsType.OTHER;
		} else if (!TextUtils.isEmpty(intent.getAction())
				&& intent.getAction().equals(IntentAction.RETRYPICKBALL)) {
			startType = 2;
			String ballStr = intent.getStringExtra(QxcPick.BALL);
				_showFragments(QxcNormalFragment.TAG);
				plsType = PlsType.OTHER;
				QxcNormalFragment zuFragment = ((QxcNormalFragment) mCurrentFragment);
				zuFragment.updateBallData(ballStr);
		} else {
			startType = 0;
			_showFragments(QxcNormalFragment.TAG);
			plsType = PlsType.OTHER;
		}
	}
	private void _setListner() {
		// TODO Auto-generated method stub
		mTitleView.setOnClickListener(this);
		mShakeBtn.setOnClickListener(this);
		right_footer_btn.setOnClickListener(this);
		clearPick.setOnClickListener(this);
		mBack.setOnClickListener(this);
		mQihaoView.setOnClickListener(this);
	}

	private void _findViews() {
		// TODO Auto-generated method stub
		mTitleView = (TextView) findViewById(R.id.current_lottery);
		mTitleView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
		mTitleView.setText("七星彩");
		mShakeBtn = (TextView) findViewById(R.id.my_pls_shake_pick);
		mZhuShuView = (TextView) findViewById(R.id.bet_zhushu);
		right_footer_btn = (TextView) findViewById(R.id.right_footer_btn);
		clearPick = (TextView) findViewById(R.id.left_footer_btn);
		mBack = (ImageView) findViewById(R.id.touzhu_leftmenu);
			mQihaoView = (TextView)findViewById(R.id.pre_num_str);
		mTimeDiffView = (TextView)findViewById(R.id.pre_win_num);
		
		findViewById(R.id.sumbit_group_buy).setVisibility(View.GONE);
		findViewById(R.id.right_divider).setVisibility(View.GONE);
	}

	public void setTouzhuResult(int count) {
		mZhuShuView.setText(String.valueOf(count) + "注" + 2*count + "元");
	}

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
			if (fragmentTag.equals(QxcNormalFragment.TAG)) {
				mFragment = new QxcNormalFragment();
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

	private void _initBallStr() {
		QxcNormalFragment zhixuanFragment = ((QxcNormalFragment) mCurrentFragment);
		mCount = zhixuanFragment.mZhushu;

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
		case R.id.my_pls_shake_pick:
			QxcNormalFragment shakeFragment = ((QxcNormalFragment) mCurrentFragment);
			shakeFragment.updateBallData();
			break;
		case R.id.left_footer_btn:
			QxcNormalFragment zhixuanFragment = ((QxcNormalFragment) mCurrentFragment);
			zhixuanFragment.clearChoose();
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
				Intent qxcPick = new Intent();
				ArrayList<String>result = ((QxcNormalFragment) mCurrentFragment)
						.getPlsResultList();
				qxcPick.putExtra(QxcPick.BETTYPE, plsType.toString());
qxcPick.putExtra("qihao", mQihao);
					qxcPick.putStringArrayListExtra(QxcPick.BALL,
							result);

				if (startType == 0) {
					qxcPick.setClass(Qxc.this, QxcPick.class);
					startActivity(qxcPick);
				} else if (startType == 1) {
					setResult(RESULT_OK, qxcPick);
				} else if (startType == 2) {
					setResult(RESULT_OK, qxcPick);
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
	private void _requestData() {
		mQihaoView.setText("正在获取当前期号");
		mTimeDiffView.setText("");
		RequestParams params = new RequestParams();
	    params.add("act", "sellqihao");
	    params.add("lotterytype", "QXC");
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
							if(Utils.isCmwapNet(Qxc.this)) {
								charset = "utf-8";
							} else {
								charset = "gb2312";
							}
							String jsonData = new String(responseBody, charset);
							Log.d(TAG, "qxc qihao detail = " + jsonData);
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
						Toast.makeText(Qxc.this, "期号获取失败", Toast.LENGTH_SHORT).show();
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
