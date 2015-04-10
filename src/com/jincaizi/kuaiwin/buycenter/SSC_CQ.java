package com.jincaizi.kuaiwin.buycenter;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import android.widget.*;
import com.jincaizi.common.IntentData;
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
import android.widget.AdapterView.OnItemClickListener;

import com.google.gson.stream.JsonReader;
import com.jincaizi.adapters.PopViewAdapter;
import com.jincaizi.R;
import com.jincaizi.http.JinCaiZiHttpClient;
import com.jincaizi.kuaiwin.utils.IntentAction;
import com.jincaizi.kuaiwin.utils.Utils;
import com.jincaizi.kuaiwin.utils.Constants.SscType;
import com.jincaizi.kuaiwin.widget.ShakeListener;
import com.jincaizi.kuaiwin.widget.ShakeListener.OnShakeListener;
import com.jincaizi.vendor.http.AsyncHttpClient;
import com.jincaizi.vendor.http.AsyncHttpResponseHandler;
import com.jincaizi.vendor.http.RequestParams;

public class SSC_CQ extends FragmentActivity implements OnClickListener{
	public static final String CITY = "city";
	protected static final String TAG = "SSC_CQ";
	private boolean isPopWindowShow = false;
	private ArrayList<Boolean> mChecked = new ArrayList<Boolean>();
	public String mCity;
	private Fragment mCurrentFragment;
	private TextView mTitleView;
	private TextView mShakeBtn;
	private TextView mZhuShuView;
	private TextView right_footer_btn;
	private RelativeLayout clearPick;
	private RelativeLayout mBack;
	private TextView mPsInfoView;
	private TextView mYilouView;
	private PopupWindow mPopWindow;
	private SscType mType;
	private ShakeListener mShakeListener;
	private int startType = 0; // 0, normal; 1, continuePick; 2, selectedAgain
	private TextView mQihaoView;
	private TextView mTimeDiffView;
    private TextView priceTextView;
	private String lotterytype = "CQSSC";

    private PopViewAdapter popViewAdapter;

    private String prevBetType;
    private HashMap<String, ArrayList<Boolean>> selectedNumbersMap;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pls);
		mCity = getIntent().getStringExtra(CITY);
		mChecked.add(true);
		for (int i = 1; i < 13; i++) {
			mChecked.add(false);
		}
		_isForStartForResult();
		_findViews();
		_setListener();
		_registerSensor();
		_requestData(true);
	}
	private void _isForStartForResult() {
        Intent intent = getIntent();

        if (TextUtils.isEmpty(intent.getAction()))
        {
            mType = SscType.fivestar_zhixuan;
            prevBetType = Ssc_cq_fragment.TAG_FIVE_ZX;
            _showFragments(prevBetType);
        }
        else {
            if (intent.getAction().equals(IntentAction.CONTINUEPICKBALL)) {
                startType = 1;
            } else if (intent.getAction().equals(IntentAction.RETRYPICKBALL)) {
                startType = 2;
            }

            String betType = intent.getStringExtra(IntentData.BET_TYPE);
            selectedNumbersMap = (HashMap<String, ArrayList<Boolean>>)intent.
                    getSerializableExtra(IntentData.SELECT_NUMBERS);

            if (betType.equals(SscType.fivestar_zhixuan.toString()))
            {
                updatePopWindow(0);
                mType = SscType.fivestar_zhixuan;
                prevBetType = Ssc_cq_fragment.TAG_FIVE_ZX;
            }
            else if (betType.equals(SscType.fivestar_fuxuan.toString()))
            {
                updatePopWindow(1);
                mType = SscType.fivestar_fuxuan;
                prevBetType = Ssc_cq_fragment.TAG_FIVE_FX;
            }
            else if (betType.equals(SscType.fivestar_tongxuan.toString()))
            {
                updatePopWindow(2);
                mType = SscType.fivestar_tongxuan;
                prevBetType = Ssc_cq_fragment.TAG_FIVE_TX;
            }
            else if (betType.equals(SscType.threestar_zhixuan.toString()))
            {
                updatePopWindow(3);
                mType = SscType.threestar_zhixuan;
                prevBetType = Ssc_cq_fragment.TAG_THREE_ZX;
            }
            else if (betType.equals(SscType.threestar_fuxuan.toString()))
            {
                updatePopWindow(4);
                mType = SscType.threestar_fuxuan;
                prevBetType = Ssc_cq_fragment.TAG_THREE_FX;
            }
            else if(betType.equals(SscType.twostar_zhixuan.toString()))
            {
                updatePopWindow(5);
                mType = SscType.twostar_zhixuan;
                prevBetType = Ssc_cq_fragment.TAG_TWO_ZHIXUAN;
            }
            else if(betType.equals(SscType.twostar_fuxuan.toString()))
            {
                updatePopWindow(6);
                mType = SscType.twostar_fuxuan;
                prevBetType = Ssc_cq_fragment.TAG_TWO_FX;
            }
            else if(betType.equals(SscType.twostar_zhixuan_hezhi.toString()))
            {
                updatePopWindow(7);
                mType = SscType.twostar_zhixuan_hezhi;
                prevBetType = Ssc_cq_fragment.TAG_TWO_ZHIXUAN_HZ;
            }
            else if(betType.equals(SscType.twostar_zuxuan.toString()))
            {
                updatePopWindow(8);
                mType = SscType.twostar_zuxuan;
                prevBetType = Ssc_cq_fragment.TAG_TWO_ZUXUAN;
            }
            else if(betType.equals(SscType.twostar_zuxuan_baohao.toString()))
            {
                updatePopWindow(9);
                mType = SscType.twostar_zuxuan_baohao;
                prevBetType = Ssc_cq_fragment.TAG_TWO_ZUXUAN_BH;
            }
            else if(betType.equals(SscType.twostar_zuxuan_hezhi.toString()))
            {
                updatePopWindow(10);
                mType = SscType.twostar_zuxuan_hezhi;
                prevBetType = Ssc_cq_fragment.TAG_TWO_ZUXUAN_HZ;
            }
            else if(betType.equals(SscType.onestar_zhixuan.toString()))
            {
                updatePopWindow(11);
                mType = SscType.onestar_zhixuan;
                prevBetType = Ssc_cq_fragment.TAG_ONE_ZX;
            }
            else if(betType.equals(SscType.dxds.toString()))
            {
                updatePopWindow(12);
                mType = SscType.dxds;
                prevBetType = Ssc_cq_fragment.TAG_DXDS;
            }
            _showFragments(prevBetType);
        }
	}

    private void _initNormalChecked() {
        for (int i = 0; i < 13; i++) {
            mChecked.set(i, false);
        }
    }

    private void updatePopWindow(int selectIndex)
    {
        _initNormalChecked();
        mChecked.set(selectIndex, true);
        if (popViewAdapter != null)
        {
            popViewAdapter.notifyDataSetChanged();
        }
    }

	private void _registerSensor() {
		mShakeListener = new ShakeListener(this);
		mShakeListener.setOnShakeListener(new shakeLitener());
		mShakeListener.setSPEED_SHRESHOLD(2000);
		mShakeListener.setUPTATE_INTERVAL_TIME(160);
	}
	class shakeLitener implements OnShakeListener {

		@Override
		public void onShake() {
			// TODO Auto-generated method stub
			((Ssc_cq_fragment)mCurrentFragment).updateBallData();
            ((Ssc_cq_fragment)mCurrentFragment).vibrate();
		}

	}
	 @Override
	 public void onDestroy() {
	  // TODO Auto-generated method stub
	  super.onDestroy();
	  mShakeListener.stop();
	 }
	private void _findViews() {
		mTitleView = (TextView) findViewById(R.id.current_lottery);
        setTitle();
		mShakeBtn = (TextView) findViewById(R.id.my_pls_shake_pick);
        mZhuShuView = (TextView) findViewById(R.id.bet_txt_2);
        priceTextView = (TextView) findViewById(R.id.bet_txt_4);
		right_footer_btn = (TextView) findViewById(R.id.right_footer_btn);
		clearPick = (RelativeLayout) findViewById(R.id.left_footer_btn);
		mBack = (RelativeLayout) findViewById(R.id.left_layout);
		mYilouView = (TextView)findViewById(R.id.sumbit_group_buy);
		mYilouView.setText("走势图");
		
		mQihaoView = (TextView)findViewById(R.id.pre_num_str);
		mTimeDiffView = (TextView)findViewById(R.id.pre_win_num);
		
	}

    private void setTitle()
    {
        if (mType == SscType.fivestar_zhixuan)
        {
           mTitleView.setText("时时彩-五星直选");
        }
        else if (mType == SscType.fivestar_fuxuan)
        {
            mTitleView.setText("时时彩-五星复选");
        }
        else if (mType == SscType.fivestar_tongxuan)
        {
            mTitleView.setText("时时彩-五星通选");
        }
        else if (mType == SscType.threestar_zhixuan)
        {
            mTitleView.setText("时时彩-三星直选");
        }
        else if (mType == SscType.threestar_fuxuan)
        {
            mTitleView.setText("时时彩-三星复选");
        }
        else if(mType == SscType.twostar_zhixuan)
        {
            mTitleView.setText("时时彩-二星直选");
        }
        else if(mType == SscType.twostar_fuxuan)
        {
            mTitleView.setText("时时彩-二星复选");
        }
        else if(mType == SscType.twostar_zhixuan_hezhi)
        {
            mTitleView.setText("时时彩-二星直选和值");
        }
        else if(mType == SscType.twostar_zuxuan)
        {
            mTitleView.setText("时时彩-二星组选");
        }
        else if(mType == SscType.twostar_zuxuan_baohao)
        {
            mTitleView.setText("时时彩-二星组选包号");
        }
        else if(mType == SscType.twostar_zuxuan_hezhi)
        {
            mTitleView.setText("时时彩-二星组选和值");
        }
        else if(mType == SscType.onestar_zhixuan)
        {
            mTitleView.setText("时时彩-一星直选");
        }
        else if(mType == SscType.dxds)
        {
            mTitleView.setText("时时彩-大小单双");
        }
    }

	private void _setListener() {
		mTitleView.setOnClickListener(this);
		mBack.setOnClickListener(this);
		clearPick.setOnClickListener(this);
		mShakeBtn.setOnClickListener(this);
		right_footer_btn.setOnClickListener(this);
		mYilouView.setOnClickListener(this);
	}
	public void setFooterBetValues(int zhushu)
    {
        mZhuShuView.setText(String.valueOf(zhushu));
        priceTextView.setText(String.valueOf(zhushu * 2));
	}
	private void _showFragments(String fragmentTag) {
		FragmentManager mFragManager = getSupportFragmentManager();
		FragmentTransaction mFragTransaction = mFragManager.beginTransaction();
		Fragment mFragment = mFragManager.findFragmentByTag(fragmentTag);

		if (mFragment == null) {
            mFragment = new Ssc_cq_fragment();
            Bundle bundle = new Bundle();
			if (fragmentTag.equals(Ssc_cq_fragment.TAG_FIVE_ZX))
            {
				bundle.putString(Ssc_cq_fragment.BETTYPE, SscType.fivestar_zhixuan.toString());
			}
            else if(fragmentTag.equals(Ssc_cq_fragment.TAG_FIVE_TX))
            {
				bundle.putString(Ssc_cq_fragment.BETTYPE, SscType.fivestar_tongxuan.toString());
			}
            else if(fragmentTag.equals(Ssc_cq_fragment.TAG_FIVE_FX))
            {
				bundle.putString(Ssc_cq_fragment.BETTYPE, SscType.fivestar_fuxuan.toString());
			}
            else if(fragmentTag.equals(Ssc_cq_fragment.TAG_THREE_FX))
            {
				bundle.putString(Ssc_cq_fragment.BETTYPE, SscType.threestar_fuxuan.toString());
			}
            else if(fragmentTag.equals(Ssc_cq_fragment.TAG_THREE_ZX))
            {
				bundle.putString(Ssc_cq_fragment.BETTYPE, SscType.threestar_zhixuan.toString());
			}
            else if(fragmentTag.equals(Ssc_cq_fragment.TAG_TWO_FX))
            {
				bundle.putString(Ssc_cq_fragment.BETTYPE, SscType.twostar_fuxuan.toString());
			}
            else if(fragmentTag.equals(Ssc_cq_fragment.TAG_TWO_ZHIXUAN))
            {
				bundle.putString(Ssc_cq_fragment.BETTYPE, SscType.twostar_zhixuan.toString());
			}
            else if(fragmentTag.equals(Ssc_cq_fragment.TAG_TWO_ZHIXUAN_HZ))
            {
				bundle.putString(Ssc_cq_fragment.BETTYPE, SscType.twostar_zhixuan_hezhi.toString());
			}
            else if(fragmentTag.equals(Ssc_cq_fragment.TAG_TWO_ZUXUAN))
            {
				bundle.putString(Ssc_cq_fragment.BETTYPE, SscType.twostar_zuxuan.toString());
			}
            else if(fragmentTag.equals(Ssc_cq_fragment.TAG_TWO_ZUXUAN_HZ))
            {
				bundle.putString(Ssc_cq_fragment.BETTYPE, SscType.twostar_zuxuan_hezhi.toString());
			}
            else if(fragmentTag.equals(Ssc_cq_fragment.TAG_ONE_ZX))
            {
				bundle.putString(Ssc_cq_fragment.BETTYPE, SscType.onestar_zhixuan.toString());
			}
            else if(fragmentTag.equals(Ssc_cq_fragment.TAG_DXDS))
            {
				bundle.putString(Ssc_cq_fragment.BETTYPE, SscType.dxds.toString());
			}
            else
            {
				bundle.putString(Ssc_cq_fragment.BETTYPE, SscType.twostar_zuxuan_baohao.toString());

			}
            if (startType == 2 && prevBetType != null
                    && prevBetType.equals(fragmentTag))
            {
                bundle.putSerializable(IntentData.SELECT_NUMBERS, selectedNumbersMap);
            }
            mFragment.setArguments(bundle);
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
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sumbit_group_buy:
			Toast.makeText(this, "暂无", Toast.LENGTH_SHORT).show();
			break;
		case R.id.left_layout:
			if(mc != null) {
				mc.cancel();
			}
			finish();
			break;
		case R.id.left_footer_btn:
			((Ssc_cq_fragment)mCurrentFragment).clearSelectResult();
			break;
		case R.id.my_pls_shake_pick:
			_shake();
			break;
		case R.id.current_lottery:
			if (!isPopWindowShow) {
				_setPopWindow();
				isPopWindowShow = true;
			}
			mPopWindow.showAsDropDown(v, -(v.getWidth() / 4), v.getTop());
			break;
		case R.id.right_footer_btn:
			Ssc_cq_fragment fragment = (Ssc_cq_fragment)mCurrentFragment;
            //测试注释
//			if(!isCanSale) {
//				Toast.makeText(getApplicationContext(), "本期销售已停止",Toast.LENGTH_SHORT).show();
//				break;
//			}
//			if(TextUtils.isEmpty(mQihao)) {
//				Toast.makeText(getApplicationContext(), "未获取到当前期号",Toast.LENGTH_SHORT).show();
//				break;
//			}
			if(_isReadyStart(fragment)) {
			    _startSscPick(fragment);
			}
			break;
		default:
			break;
		}
	}
	private boolean _isReadyStart(Ssc_cq_fragment fragment) {
		if(fragment.mZhushu < 1) {
			Toast.makeText(this, "请至少选择1注", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}
	private void _startSscPick(Ssc_cq_fragment fragment) {
		fragment.getBetResult();
		Intent intent = new Intent();
		intent.putExtra(SscPick.CITY, mCity);
		intent.putExtra(SscPick.BETTYPE, fragment.mGameType);
		intent.putExtra(SscPick.BALL,fragment.betResult);
		intent.putExtra(SscPick.ZHUAMOUNT,fragment.mZhushu);
        intent.putExtra(IntentData.SELECT_NUMBERS, fragment.getResultMap());
		intent.putExtra("qihao", mQihao);
		if (startType == 0) {
			intent.setClass(this, SscPick.class);
			startActivity(intent);
		}
        else if (startType == 1 || startType == 2)
        {
			setResult(RESULT_OK, intent);
		}
		if(mc != null) {
			mc.cancel();
		}
		finish();
	}
	private void _setPopWindow() {
		View view = LayoutInflater.from(this).inflate(R.layout.popview, null);
		mPopWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		GridView mGridView = (GridView) view.findViewById(R.id.pop_gridview);
        mGridView.setNumColumns(3);
		final ArrayList<String> list = new ArrayList<String>();
		list.add("五星直选");
		list.add("五星复选");
		list.add("五星通选");
		list.add("三星直选");
		list.add("三星复选");
		list.add("二星直选");
		list.add("二星复选");
		list.add("二星直选和值");
		list.add("二星组选");
		list.add("二星组选包号");
		list.add("二星组选和值");
		list.add("一星直选");
		list.add("大小单双");
		popViewAdapter = new PopViewAdapter(this, list, mChecked);
		mGridView.setAdapter(popViewAdapter);
		mGridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				mTitleView.setText("时时彩-" + list.get(arg2));
				if (arg2 == 0) {			
					mType = SscType.fivestar_zhixuan;
					_showFragments(Ssc_cq_fragment.TAG_FIVE_ZX);
					
				} else if (arg2 == 1) {
                    mType = SscType.fivestar_fuxuan;
					_showFragments(Ssc_cq_fragment.TAG_FIVE_FX);
				} else if (arg2 == 2) {
					mType = SscType.fivestar_tongxuan;
					_showFragments(Ssc_cq_fragment.TAG_FIVE_TX);
				} else if (arg2 == 3) {
					mType = SscType.threestar_zhixuan;
					_showFragments(Ssc_cq_fragment.TAG_THREE_ZX);
				} else if (arg2 == 4) {
					mType = SscType.threestar_fuxuan;
					_showFragments(Ssc_cq_fragment.TAG_THREE_FX);
				} else if(arg2 == 5){
					mType = SscType.twostar_zhixuan;
					_showFragments(Ssc_cq_fragment.TAG_TWO_ZHIXUAN);
				}
				else if(arg2 == 6){
					mType = SscType.twostar_fuxuan;
					_showFragments(Ssc_cq_fragment.TAG_TWO_FX);
				}
				else if(arg2 == 7){
					mType = SscType.twostar_zhixuan_hezhi;
					_showFragments(Ssc_cq_fragment.TAG_TWO_ZHIXUAN_HZ);
				}else if(arg2 == 8){
					mType = SscType.twostar_zuxuan;
					_showFragments(Ssc_cq_fragment.TAG_TWO_ZUXUAN);
				}else if(arg2 == 9){
					mType = SscType.twostar_zuxuan_baohao;
					_showFragments(Ssc_cq_fragment.TAG_TWO_ZUXUAN_BH);
				}else if(arg2 == 10){
					mType = SscType.twostar_zuxuan_hezhi;
					_showFragments(Ssc_cq_fragment.TAG_TWO_ZUXUAN_HZ);
				}else if(arg2 == 11){
					mType = SscType.onestar_zhixuan;
					_showFragments(Ssc_cq_fragment.TAG_ONE_ZX);
				}else if(arg2 == 12){
					mType = SscType.dxds;
					_showFragments(Ssc_cq_fragment.TAG_DXDS);
				}
				int lastIndex = mChecked.indexOf(true);
				if(lastIndex != -1) {
				    mChecked.set(lastIndex, false);
				}
				mChecked.set(arg2, true);
				popViewAdapter.notifyDataSetChanged();
				mPopWindow.dismiss();
			}
		});
		mPopWindow.setFocusable(true);
		mPopWindow.setOutsideTouchable(true);
		mPopWindow.update();
		mPopWindow.setBackgroundDrawable(new BitmapDrawable(getResources(),
				Bitmap.createBitmap(1, 1, Config.ARGB_8888)));
	}
	private void _shake() {
		((Ssc_cq_fragment)mCurrentFragment).updateBallData();
	}
	@Override
	public void onBackPressed() {
		if(mc != null) {
			mc.cancel();
		}
		finish();
	}
	
	private void _requestData(boolean showText) {
		JinCaiZiHttpClient.closeExpireConnection();
        if (showText) {
            mQihaoView.setText("正在获取当前期号");
            mTimeDiffView.setText("");
        }
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
							if(Utils.isCmwapNet(SSC_CQ.this)) {
								charset = "utf-8";
							} else {
								charset = "gb2312";
							}
							String jsonData = new String(responseBody, charset);
							Log.d(TAG, "ssc qihao detail = " + jsonData);
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
                _requestData(false);
            } else {
                mQihaoView.setText("距" + mQihao + "期还有");
                if (TextUtils.isEmpty(diff))
                {
                    diff = "540";
                }
                mc = new MyCount(Long.valueOf(diff)*1000, 1000);
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
         _requestData(false);
     }     
     @Override     
     public void onTick(long millisUntilFinished) {
    	 mTimeDiffView.setText(Utils.formatDuring(millisUntilFinished));
     }    
 }
	
}
