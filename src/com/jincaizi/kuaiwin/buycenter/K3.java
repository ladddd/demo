package com.jincaizi.kuaiwin.buycenter;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.IllegalFormatCodePointException;
import java.util.List;

import com.jincaizi.common.IntentData;
import com.jincaizi.kuaiwin.chart.activities.TableFour;
import com.jincaizi.kuaiwin.chart.activities.TableThree;
import com.jincaizi.kuaiwin.chart.activities.TestTable;
import com.jincaizi.kuaiwin.chart.activities.TestTableTwo;
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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.gson.stream.JsonReader;
import com.jincaizi.R;
import com.jincaizi.adapters.PopViewAdapter;
import com.jincaizi.http.JinCaiZiHttpClient;
import com.jincaizi.kuaiwin.utils.Constants;
import com.jincaizi.kuaiwin.utils.Constants.City;
import com.jincaizi.kuaiwin.utils.Constants.K3Type;
import com.jincaizi.kuaiwin.utils.IntentAction;
import com.jincaizi.kuaiwin.utils.UiHelper;
import com.jincaizi.kuaiwin.utils.Utils;
import com.jincaizi.kuaiwin.widget.ShakeListener;
import com.jincaizi.kuaiwin.widget.ShakeListener.OnShakeListener;
import com.jincaizi.vendor.http.AsyncHttpClient;
import com.jincaizi.vendor.http.AsyncHttpResponseHandler;
import com.jincaizi.vendor.http.RequestParams;


public class K3 extends FragmentActivity implements OnClickListener {
	public static final String CITY = "city";
	protected static final String TAG = "K3";
	private TextView mTitleView;
	private PopupWindow mPopWindow;
	private boolean isPopWindowShow = false;
	private Fragment mCurrentFragment;
	private TextView mShakeBtn;
	public K3Type syyType = K3Type.hezhi;
	int lastIndex = 0;
	private TextView mZhuShuView;
	private TextView right_footer_btn;
	private TextView clearPick;
	private ImageView mBack;
	private GridView mGridNormalView;
	private GridView mGridDragView;
	private PopViewAdapter mMyNormalAdapter, mMyDragAdapter;
	private ArrayList<Boolean> mNormalChecked = new ArrayList<Boolean>();
	private ArrayList<Boolean> mDragChecked = new ArrayList<Boolean>();
	private String mCity;
	private TextView mPsInfoView;
//	private TextView mYilouView;
    private TextView chart;
	public FrameLayout mParentFrameLayout;
	public int mCaseIndex = 0;
	public ToggleButton[] tbArrays = new ToggleButton[100];
	public ToggleButton[] tbDragArrays_two = new ToggleButton[12];
	public ToggleButton[] tbDragArrays_three = new ToggleButton[12];
	private ShakeListener mShakeListener;
	private int startType = 0; // 0, normal; 1, continuePick; 2, selectedAgain
	private TextView mQihaoView;
	private TextView mTimeDiffView;
	private String lotterytype;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pls);
		mCity = getIntent().getStringExtra(CITY);
		mNormalChecked.add(true);
		for (int i = 1; i < 5; i++) {
			mNormalChecked.add(false);
		}
		for(int i=0; i<2; i++) {
			mDragChecked.add(false);
		}
		_getLotteryType();
		_isForStartForResult();
		_findViews();
		_setListner();
		_showFragments(K3_hz_fragment.TAG_HZ);
		_registerSensorListener();
		_requestData();
	}
	private void _getLotteryType() {
	    if(mCity.equals(Constants.City.jiangsu.toString())) {
	    	lotterytype = "JSK3";
		} else if(mCity.equals(Constants.City.anhui.toString())) {
			lotterytype = "AHK3";
		}else if(mCity.equals(Constants.City.neimenggu.toString())) {
			lotterytype = "NMGK3";
		}
}
	private void _isForStartForResult() {
		Intent intent = getIntent();
		if (!TextUtils.isEmpty(intent.getAction())
				&& intent.getAction().equals(IntentAction.CONTINUEPICKBALL)) {
			startType = 1;
			
		} 
	}
	private void _registerSensorListener() {
		
		mShakeListener = new ShakeListener(this);
		mShakeListener.setOnShakeListener(new shakeLitener());
		mShakeListener.setSPEED_SHRESHOLD(2000);
		mShakeListener.setUPTATE_INTERVAL_TIME(160);
	}
	class shakeLitener implements OnShakeListener {

		@Override
		public void onShake() {
			// TODO Auto-generated method stub
			if(mCaseIndex == 5 ||mCaseIndex == 6) {
				return;
			}
			if(mCurrentFragment == null) {
				return;
			}
			K3_hz_fragment fragment = (K3_hz_fragment)mCurrentFragment;
			
			if(fragment.myDialog != null && fragment.myDialog.isShowing()) {
				return;
			}
			fragment.updateBallData();
			// mShakeListener.stop();
		}

	}
	 @Override
	 public void onDestroy() {
	  // TODO Auto-generated method stub
	  super.onDestroy();
	  mShakeListener.stop();
	 }
    private void _initNormalChecked() {
    	mNormalChecked.clear();
    	for (int i = 0; i < 5; i++) {
			mNormalChecked.add(false);
		}
    }
    private void _initDragChecked() {
    	mDragChecked.clear();
    	for(int i=0; i<2; i++) {
			mDragChecked.add(false);
		}
    }
	private void _setListner() {
		// TODO Auto-generated method stub
		mTitleView.setOnClickListener(this);
		mShakeBtn.setOnClickListener(this);
		right_footer_btn.setOnClickListener(this);
		clearPick.setOnClickListener(this);
		mBack.setOnClickListener(this);
//		mYilouView.setOnClickListener(this);
        chart.setOnClickListener(this);
		mQihaoView.setOnClickListener(this);
	}

	private void _findViews() {
		// TODO Auto-generated method stub
		RelativeLayout mHeaderBar = (RelativeLayout)findViewById(R.id.pl3_head_bar);
		mHeaderBar.setBackgroundColor(this.getResources().getColor(R.color.k3_orange));
		findViewById(R.id.left_divider).setBackgroundResource(R.drawable.k3_divider);
		findViewById(R.id.right_divider).setBackgroundResource(R.drawable.k3_divider);
		mTitleView = (TextView) findViewById(R.id.current_lottery);
		mTitleView.setText("快3-和值");
		mTitleView.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
		mTitleView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.triangle_white, 0);
		mTitleView.setCompoundDrawablePadding(4);
		mShakeBtn = (TextView) findViewById(R.id.my_pls_shake_pick);
		mZhuShuView = (TextView) findViewById(R.id.bet_zhushu);
		right_footer_btn = (TextView) findViewById(R.id.right_footer_btn);
		clearPick = (TextView) findViewById(R.id.left_footer_btn);
		mBack = (ImageView) findViewById(R.id.touzhu_leftmenu);
		
		mPsInfoView = (TextView)findViewById(R.id.ps_info);
		mPsInfoView.setVisibility(View.VISIBLE);
		mPsInfoView.setText("("+City.getCityName(mCity)+")");
		
//		mYilouView = (TextView)findViewById(R.id.sumbit_group_buy);
//		mYilouView.setText(this.getResources().getString(R.string.yilou));

        chart = (TextView) findViewById(R.id.sumbit_group_buy);
        chart.setText(getResources().getString(R.string.chart));

		mParentFrameLayout = (FrameLayout)findViewById(R.id.fl_pls_pickarea);
		
		mQihaoView = (TextView)findViewById(R.id.pre_num_str);
		mTimeDiffView = (TextView)findViewById(R.id.pre_win_num);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}
	

	public void setTouzhuResult(int count) {
		mZhuShuView.setText(String.valueOf(count) + "注" + 2*count + "元");
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
		case R.id.sumbit_group_buy:
//			UiHelper.startSyxwYilou(this, lotterytype,Constants.City.getCityName(mCity)+"快3");
            goToChart();
			break;
		case R.id.touzhu_leftmenu:
			if(mc != null) {
				mc.cancel();
			}
			finish();
			break;
		case R.id.left_footer_btn:
			((K3_hz_fragment)mCurrentFragment).clearTouzhu();
			break;
		case R.id.my_pls_shake_pick:
			((K3_hz_fragment)mCurrentFragment).updateBallData();
			break;
		case R.id.current_lottery:
			if (!isPopWindowShow) {
				_setPopWindow((int) (v.getWidth() * 1.5 + 0.5f));
				isPopWindowShow = true;
			}
			mPopWindow.showAsDropDown(v, -(v.getWidth() / 4), v.getTop());
			break;
		case R.id.right_footer_btn:
			K3_hz_fragment fragment = (K3_hz_fragment)mCurrentFragment;
			if(!isCanSale) {
				Toast.makeText(getApplicationContext(), "本期销售已停止",Toast.LENGTH_SHORT).show();
				break;
			}
			if(TextUtils.isEmpty(mQihao)) {
				Toast.makeText(getApplicationContext(), "未获取到当前期号",Toast.LENGTH_SHORT).show();
				break;
			}
			if(_isReadyStart(fragment)) {
				_startK3Pick(fragment);
		    }
			break;
		default:
			break;
		}
	}

    private void goToChart()
    {
        Intent chartActivity = new Intent();

        //和值
        if (syyType == K3Type.hezhi)
        {
            chartActivity.setClass(K3.this, TestTable.class);
        }
        //三同号 三不同号 三不胆拖 --> 形态走势
        else if (syyType == K3Type.threesamesingle)
        {
            chartActivity.setClass(K3.this, TestTableTwo.class);
            chartActivity.putExtra(IntentData.THREE_NUMBER_TYPE, TestTableTwo.TYPE_THREE_EQUAL);
        }
        else if (syyType == K3Type.threedifsingle || syyType == K3Type.dragthree)
        {
            chartActivity.setClass(K3.this, TestTableTwo.class);
            chartActivity.putExtra(IntentData.THREE_NUMBER_TYPE, TestTableTwo.TYPE_THREE_NOT_EQUAL);
            chartActivity.putExtra(IntentData.DRAG, syyType == K3Type.dragthree);
        }
        //二同号
        else if (syyType == K3Type.twosamesingle)
        {
            chartActivity.setClass(K3.this, TableThree.class);
        }
        //二不同号+胆拖
        else if (syyType == K3Type.twodif || syyType == K3Type.dragtwo)
        {
            chartActivity.setClass(K3.this, TableFour.class);
            chartActivity.putExtra(IntentData.DRAG, syyType == K3Type.dragtwo);
        }

        chartActivity.putExtra(IntentData.CITY, lotterytype);
        startActivityForResult(chartActivity, syyType.ordinal());
    }

	private boolean _isReadyStart(K3_hz_fragment fragment) {
		if(fragment.mZhushu < 1) {
			Toast.makeText(this, "请至少选择1注", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}
	private void _startK3Pick(K3_hz_fragment fragment) {
		fragment.getBetResult();
		Intent intent = new Intent();
		intent.putExtra(K3Pick.CITY, mCity);
		intent.putExtra(K3Pick.BETTYPE, fragment.betTypes);
		intent.putExtra(K3Pick.BALL,fragment.betResult);
		intent.putExtra(K3Pick.ZHUAMOUNT,fragment.mZhushu);
		intent.putExtra("qihao", mQihao);
		if (startType == 0) {
			intent.setClass(this, K3Pick.class);
			startActivity(intent);
		} else if (startType == 1) {
			setResult(RESULT_OK, intent);
		} 
		if(mc != null) {
			mc.cancel();
		}
		finish();
	}
	

	private void _setPopWindow(int width) {
		View view = LayoutInflater.from(this).inflate(R.layout.syxw_popview_layout, null);
		LinearLayout popLayout = (LinearLayout)view.findViewById(R.id.syxw_popview_lv);
		popLayout.setBackgroundResource(R.drawable.k3_pop_bg);
		mPopWindow = new PopupWindow(view, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		mGridNormalView = (GridView) view.findViewById(R.id.pop_normal_gridview);
		mGridNormalView.setBackgroundColor(this.getResources().getColor(
				android.R.color.transparent));
		mGridNormalView.setNumColumns(3);
		mGridDragView = (GridView) view.findViewById(R.id.pop_drag_gridview);
		mGridDragView.setBackgroundColor(this.getResources().getColor(
				android.R.color.transparent));
		mGridDragView.setNumColumns(3);
		final ArrayList<String> listNormal = new ArrayList<String>();
		listNormal.add("和值");
		listNormal.add("三同号");
		listNormal.add("二同号");
		listNormal.add("三不同号");
		listNormal.add("二不同号");
		mMyNormalAdapter = new PopViewAdapter(this, listNormal, mNormalChecked);
		mGridNormalView.setAdapter(mMyNormalAdapter);
		mGridNormalView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				mCaseIndex = arg2;
				switch(arg2) {
				case 0:
					_showFragments(K3_hz_fragment.TAG_HZ);
                    syyType = K3Type.hezhi;
					break;
				case 1:
					_showFragments(K3_hz_fragment.TAG_THREE_SAME);
                    syyType = K3Type.threesamesingle;
					break;
				case 2:
					_showFragments(K3_hz_fragment.TAG_TWO_SAME);
                    syyType = K3Type.twosamesingle;
					break;
				case 3:
					_showFragments(K3_hz_fragment.TAG_THREE_DIF);
                    syyType = K3Type.threedifsingle;
					break;
				case 4:
					_showFragments(K3_hz_fragment.TAG_TWO_DIF);
                    syyType = K3Type.twodif;
					break;
					default:
						break;
				}
				int index = mNormalChecked.indexOf(true);
				if(index != -1) {
					mNormalChecked.set(index, false);
				}
				mNormalChecked.set(arg2, true);
				_initDragChecked();
				mMyDragAdapter.notifyDataSetChanged();
				mMyNormalAdapter.notifyDataSetChanged();
				mTitleView.setText("快3-"+listNormal.get(arg2));
				mPopWindow.dismiss();
				
			}
		});
		final ArrayList<String>mDragList = new ArrayList<String>();
		mDragList.add("三不同号");
		mDragList.add("二不同号");
		mMyDragAdapter = new PopViewAdapter(this, mDragList, mDragChecked);
		mGridDragView.setAdapter(mMyDragAdapter);
		mGridDragView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				mShakeBtn.setVisibility(View.GONE);
				mCaseIndex = arg2 + 5;
				switch(arg2) {
				case 0:
					_showFragments(K3_hz_fragment.TAG_THREE_DIF_DRAG);
                    syyType = K3Type.dragthree;
					break;
				case 1:
					_showFragments(K3_hz_fragment.TAG_TWO_DIF_DRAG);
                    syyType = K3Type.dragtwo;
					break;
					default:
						break;
				}
				int index = mDragChecked.indexOf(true);
				if(index != -1) {
					mDragChecked.set(index, false);
				}
				mDragChecked.set(arg2, true);
				_initNormalChecked();
				mMyNormalAdapter.notifyDataSetChanged();
				mMyDragAdapter.notifyDataSetChanged();
				mTitleView.setText("快3-"+mDragList.get(arg2)+"(胆拖)");
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
			if (fragmentTag.equals(K3_hz_fragment.TAG_HZ)) {
				mFragment = new K3_hz_fragment();
				Bundle bundle = new Bundle();
				bundle.putString(K3_hz_fragment.BETTYPE, K3Type.hezhi.toString());
				mFragment.setArguments(bundle);
			} else if(fragmentTag.equals(K3_hz_fragment.TAG_THREE_SAME)){
				mFragment = new K3_hz_fragment();
				Bundle bundle = new Bundle();
				bundle.putString(K3_hz_fragment.BETTYPE, K3Type.threesamesingle.toString());
				mFragment.setArguments(bundle);
			}else if(fragmentTag.equals(K3_hz_fragment.TAG_TWO_SAME)){
				mFragment = new K3_hz_fragment();
				Bundle bundle = new Bundle();
				bundle.putString(K3_hz_fragment.BETTYPE, K3Type.twosamesingle.toString());
				mFragment.setArguments(bundle);
			}else if(fragmentTag.equals(K3_hz_fragment.TAG_THREE_DIF)){
				mFragment = new K3_hz_fragment();
				Bundle bundle = new Bundle();
				bundle.putString(K3_hz_fragment.BETTYPE, K3Type.threedifsingle.toString());
				mFragment.setArguments(bundle);
			}else if(fragmentTag.equals(K3_hz_fragment.TAG_TWO_DIF)){
				mFragment = new K3_hz_fragment();
				Bundle bundle = new Bundle();
				bundle.putString(K3_hz_fragment.BETTYPE, K3Type.twodif.toString());
				mFragment.setArguments(bundle);
			}else if(fragmentTag.equals(K3_hz_fragment.TAG_THREE_DIF_DRAG)){
				mFragment = new K3_hz_fragment();
				Bundle bundle = new Bundle();
				bundle.putString(K3_hz_fragment.BETTYPE, K3Type.dragthree.toString());
				mFragment.setArguments(bundle);
			}else {
				mFragment = new K3_hz_fragment();
				Bundle bundle = new Bundle();
				bundle.putString(K3_hz_fragment.BETTYPE, K3Type.dragtwo.toString());
				mFragment.setArguments(bundle);
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
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if(mc != null) {
			mc.cancel();
		}
		finish();
	}
		
	private void _requestData() {
		JinCaiZiHttpClient.closeExpireConnection();
		mQihaoView.setText("正在获取当前期号");
		mTimeDiffView.setText("");
		RequestParams params = new RequestParams();
	    params.add("act", "sellqihao");
	  
	    params.add("lotterytype", lotterytype);
		params.add("datatype", "json");
		params.add("jsoncallback",
				"jsonp" + String.valueOf(System.currentTimeMillis()));
		params.add("_", String.valueOf(System.currentTimeMillis()));
		String url = AsyncHttpClient.getUrlWithQueryString(false,
				JinCaiZiHttpClient.BASE_URL, params);
		JinCaiZiHttpClient.post(this, url,
				new AsyncHttpResponseHandler() {
					

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							byte[] responseBody) {
						try {
							String charset;
							if(Utils.isCmwapNet(K3.this)) {
								charset = "utf-8";
							} else {
								charset = "gb2312";
							}
							String jsonData = new String(responseBody, charset);
							Log.d(TAG, "k3 qihao detail = " + jsonData);
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
						//Toast.makeText(K3.this, "期号获取失败", Toast.LENGTH_SHORT).show();
						_requestData();
					}
				});
	}
	private String mQihao = "";
	private boolean isCanSale = true;
	private MyCount mc = null;
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
			
			if(diff.startsWith("-")) {
				isCanSale = false;
				mQihaoView.setText(mQihao + "期代购截止");
				_requestData();
			} else {
				mQihaoView.setText("距" + mQihao + "期还有");
				isCanSale = true;
				mc  = new MyCount(Long.valueOf(diff)*1000, 1000);  
		        mc.start(); 
			}
		}
	}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK)
        {
            return;
        }
        if (requestCode == K3Type.hezhi.ordinal())
        {
            ArrayList<Boolean> result = (ArrayList<Boolean>)data.getSerializableExtra(IntentData.SELECT_NUMBERS);
            if (mCurrentFragment instanceof K3_hz_fragment)
            {
                ((K3_hz_fragment) mCurrentFragment).updateChoice(result, syyType.toString());
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
	
	
}
