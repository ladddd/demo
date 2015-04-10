package com.jincaizi.kuaiwin.buycenter;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.Header;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.stream.JsonReader;
import com.jincaizi.R;
import com.jincaizi.adapters.PopViewAdapter;
import com.jincaizi.http.JinCaiZiHttpClient;
import com.jincaizi.kuaiwin.mylottery.Login;
import com.jincaizi.kuaiwin.tool.CheckLogin;
import com.jincaizi.kuaiwin.utils.Constants;
import com.jincaizi.kuaiwin.utils.Utils;
import com.jincaizi.kuaiwin.widget.JinCaiZiProgressDialog;
import com.jincaizi.vendor.http.AsyncHttpClient;
import com.jincaizi.vendor.http.AsyncHttpResponseHandler;
import com.jincaizi.vendor.http.RequestParams;

public class SumbitGroupBuy extends Activity implements OnClickListener {
	public static final int LOGINREQUESTCODE = 2;
	public final static String FENSHU = "sum_fenshu";
	protected static final String TAG = "SumbitGroupBuy";
	
    public static final String ZHUIHAOCOUNT = "zhuihaoshu";
    public static final String STARTTIMES = "qishibeishu";
    public static final String ZHUSHU = "zhushu";
    public static final String LOTTERYTYPE = "lotterytype";
    public static final String CITY = "city";
    public static final String MUTIPLES = "multiples";
    public static final String ISZHUIHAO = "iszhuihao";
    public static final String QIHAOS = "qihaos";
    public static final String ITEMCONTENT = "itemcontent";
    public static final String TYPECONTENT = "typecontent";
    public static final String BETCOUNT = "betcount";
    public static final String STOPZHUIHAO = "stopzhuihao";
	
	private RelativeLayout mRvSecurityLevel, mRvSalaryScale;
	private PopupWindow mPerMoneyPop, mSalaryScalePop, mSecurityLevelPop;
	private ArrayList<String> mPerMoneyData, mSalaryScaleData,
			mSecurityLevelData;
	private ArrayList<Boolean> mPerMoneyChecked, mSalaryScaleChecked,
			mSecurityLevelChecked;
	private OnItemClickListener mPerMoneyListener, mSalaryScaleListener,
			mSecurityLevelListener;
	private TextView mTvPerMoney;
	private TextView mTvSalaryScale;
	private TextView mTvSecurityLevel;
	private EditText mEtBaoDi;
	private EditText mEtRenGou;
	private EditText mEtTheme;
	private TextView mTvAmount;
	private TextView mTvPay;
	private TextView mTvCount;
	private int mSumFenshu = 0;
	private Button mBtnSubmit;
	private ImageView mIvBack;
	private String mLotteryType = "";
	private String[] itemContents;
	private String mQihao = "";
	//private String mBeishu = "1";
	private int mUserid;
	private String mUpk;
	private SharedPreferences sp;
	private int minRengou = 1;//最小认购份数  总份数*5%
	private EditText mEtTitle;
	private String mCity;
	private String mTitleStr;
	private String lotterytype;
	private int perDayTermCount;
	private String itemContent;
	private String typeContent;
	private String betCount;
	private boolean isStopZhui;
	private boolean isZhuiHao;
	private String qihaos;
	private String mutiples;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.build_group_buy);
		sp = this.getSharedPreferences("loginStatus", Context.MODE_PRIVATE);
		mUserid = sp.getInt("userid", 0);
		mUpk = sp.getString("upk", "");
		_getDataFromIntent(getIntent());
		_getTouZhuAttribute();
		_findViews();
		_setListeners();
		_initData();
		_initPopListener();
		_initTvPopItem();
		// _initPopWindow();
	}

	private void _getDataFromIntent(Intent intent) {
		mSumFenshu = intent.getIntExtra(FENSHU, 0);
		minRengou = (int) Math.ceil(mSumFenshu*0.05);
		mLotteryType = intent.getStringExtra("lotterytype");
		
		mQihao = intent.getStringExtra("qihao");
		//mBeishu = intent.getStringExtra("beishu");
		
		mCity = intent.getStringExtra(CITY);
		itemContent = intent.getStringExtra(ITEMCONTENT);
		typeContent = intent.getStringExtra(TYPECONTENT);
		betCount = intent.getStringExtra(BETCOUNT);
		isStopZhui = intent.getBooleanExtra(STOPZHUIHAO, false);
		isZhuiHao = intent.getBooleanExtra(ISZHUIHAO, false);
		qihaos = intent.getStringExtra(QIHAOS);
		mutiples = intent.getStringExtra(MUTIPLES);
	}

	private void _initTvPopItem() {
		// TODO Auto-generated method stub
		int lastChecked = mSalaryScaleChecked.indexOf(true);

		mTvSalaryScale.setText(mSalaryScaleData.get(lastChecked));

		lastChecked = mPerMoneyChecked.indexOf(true);
		mTvPerMoney.setText(mPerMoneyData.get(lastChecked));
		lastChecked = mSecurityLevelChecked.indexOf(true);
		mTvSecurityLevel.setText(mSecurityLevelData.get(lastChecked));
	}

	private void _initData() {
		mPerMoneyData = new ArrayList<String>();
		mPerMoneyChecked = new ArrayList<Boolean>();
		mSecurityLevelChecked = new ArrayList<Boolean>();
		mSecurityLevelData = new ArrayList<String>();
		mSalaryScaleChecked = new ArrayList<Boolean>();
		mSalaryScaleData = new ArrayList<String>();
		// mPerMoneyData.add("0.5元");
		mPerMoneyData.add("1.0元");
		mPerMoneyData.add("2.0元");
		for (int i = 0; i < 11; i++) {
			mSalaryScaleData.add(i + "%");
			if (i == 0) {
				mSalaryScaleChecked.add(true);
			} else {
				mSalaryScaleChecked.add(false);
			}
		}
		mSecurityLevelData.add("公开");
		mSecurityLevelData.add("跟单可见");
		mSecurityLevelData.add("完全保密");
		mPerMoneyChecked.add(true);
		mPerMoneyChecked.add(false);
		mPerMoneyChecked.add(false);
		mSecurityLevelChecked.add(true);
		mSecurityLevelChecked.add(false);
		mSecurityLevelChecked.add(false);

	}

	private void _initPopListener() {

		mSalaryScaleListener = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				int lastChecked = mSalaryScaleChecked.indexOf(true);
				mSalaryScaleChecked.set(lastChecked, false);
				mSalaryScaleChecked.set(arg2, true);
				mTvSalaryScale.setText(mSalaryScaleData.get(arg2));
				mSalaryScalePop.dismiss();
			}
		};
		mSecurityLevelListener = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				int lastChecked = mSecurityLevelChecked.indexOf(true);
				mSecurityLevelChecked.set(lastChecked, false);
				mSecurityLevelChecked.set(arg2, true);
				mTvSecurityLevel.setText(mSecurityLevelData.get(arg2));
				mSecurityLevelPop.dismiss();
			}
		};
	}

	private void _findViews() {
		mRvSalaryScale = (RelativeLayout) findViewById(R.id.rv_salary_scale);
		mRvSecurityLevel = (RelativeLayout) findViewById(R.id.rv_security_level);
		mTvPerMoney = (TextView) findViewById(R.id.group_buy_permoney_content);
		mTvSalaryScale = (TextView) findViewById(R.id.group_buy_salary);
		mTvSecurityLevel = (TextView) findViewById(R.id.group_buy_security);

		mEtBaoDi = (EditText) findViewById(R.id.group_buy_baodi_content);
		mEtRenGou = (EditText) findViewById(R.id.group_buy_mybuy_content);
		mEtRenGou.setHint("至少认购" + Math.max(minRengou, 1) + "元");
		mEtTheme = (EditText) findViewById(R.id.group_buy_theme_content);
		mEtTitle = (EditText)findViewById(R.id.group_buy_title_content);
		mTvPay = (TextView) findViewById(R.id.group_buy_pay);
		mTvAmount = (TextView) findViewById(R.id.group_buy_amount);
		mTvCount = (TextView) findViewById(R.id.group_buy_count);

		mTvAmount.setText(String.valueOf(mSumFenshu));
		mTvCount.setText(String.valueOf(mSumFenshu));

		mBtnSubmit = (Button) findViewById(R.id.group_buy_right_footer_btn);
		mIvBack = (ImageView) findViewById(R.id.groupBuy_leftmenu);
		TextView titleView = (TextView)findViewById(R.id.tv_groupBuy_title);
        titleView.setText(mTitleStr);
	}

	 private void _getTouZhuAttribute() {
		    if(mCity.equals(Constants.City.shandong.toString())) {
		    	mTitleStr = "山东";
		    	lotterytype = "SD"+mLotteryType;
		    	perDayTermCount = 78;
			} else if(mCity.equals(Constants.City.jiangxi.toString())) {
				mTitleStr = "江西";
				lotterytype = "JX"+mLotteryType;
				if(mLotteryType.equals("11x5")) {
				    perDayTermCount = 78;
				} else {
					perDayTermCount = 84;
				}
			}else if(mCity.equals(Constants.City.guangdong.toString())) {
				mTitleStr = "广东";
				lotterytype = "GD"+mLotteryType;
				perDayTermCount = 84;
			}else if(mCity.equals(Constants.City.anhui.toString())) {
				mTitleStr = "安徽";
				lotterytype = "AH"+mLotteryType;
				if(mLotteryType.equals("11x5")) {
				    perDayTermCount = 81;
				} else {
					perDayTermCount = 80;
				}
			}else if(mCity.equals(Constants.City.chongqing.toString())) {
				mTitleStr = "重庆";
				lotterytype = "CQ"+mLotteryType;
				if(mLotteryType.equals("11x5")) {
				    perDayTermCount = 85;
				} else {
					perDayTermCount = 120;
				}
			}else if(mCity.equals(Constants.City.liaoning.toString())) {
				mTitleStr = "辽宁";
				lotterytype = "LN"+mLotteryType;
				perDayTermCount = 83;
			}else if(mCity.equals(Constants.City.shanghai.toString())) {
				mTitleStr = "上海";
				lotterytype = "SH"+mLotteryType;
				perDayTermCount = 90;
			}else if(mCity.equals(Constants.City.heilongjiang.toString())) {
				mTitleStr = "黑龙江";
				lotterytype = "HLJ"+mLotteryType;
				perDayTermCount = 73;
			} else if(mCity.equals(Constants.City.jiangsu.toString())) {
				mTitleStr = "江苏";
				lotterytype = "JS"+mLotteryType;
				perDayTermCount = 82;
			}else if(mCity.equals(Constants.City.neimenggu.toString())) {
				mTitleStr = "内蒙古";
				lotterytype = "NMG"+mLotteryType;
				perDayTermCount = 73;
			}else if(mCity.equals(Constants.City.zhejiang.toString())) {
                mTitleStr = "浙江";
                lotterytype = "ZJ"+mLotteryType;
                perDayTermCount = 80;
            }else if(mCity.equals(Constants.City.hubei.toString())) {
                mTitleStr = "湖北";
                lotterytype = "HB"+mLotteryType;
                perDayTermCount = 78;
            }else if(mCity.equals(Constants.City.jilin.toString())) {
                mTitleStr = "吉林";
                lotterytype = "JL"+mLotteryType;
                perDayTermCount = 79;
            }
		    if(mLotteryType.equals("11x5")) {
		    	mTitleStr += "11选5";
		    } else if(mLotteryType.equals("K3")) {
		    	mTitleStr += "快3";
		    } else {
		    	mTitleStr += "时时彩";
		    }
		    mTitleStr = mTitleStr.replace("山东11选5", "十一运夺金");
		    mTitleStr += "-合买";
	    }

	private Integer baodiInput = 0;
	private Integer rengouInput = 0;

	private void _setListeners() {
		mRvSalaryScale.setOnClickListener(this);
		mRvSecurityLevel.setOnClickListener(this);
		mBtnSubmit.setOnClickListener(this);
		mIvBack.setOnClickListener(this);
		mEtBaoDi.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				if (TextUtils.isEmpty(s)) {
					baodiInput = 0;
				} else {
					baodiInput = Integer.valueOf(s.toString());
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

				int selectionEnd = mEtBaoDi.getSelectionEnd();
				if (baodiInput + Math.max(rengouInput, minRengou) > mSumFenshu) {
					s.delete(selectionEnd - 1, selectionEnd);
					int tempSelection = selectionEnd - 1;
					if (TextUtils.isEmpty(s)) {
						baodiInput = 0;
					} else {
						baodiInput = Integer.valueOf(s.toString());
					}
					mEtBaoDi.setSelection(tempSelection);
					Toast.makeText(getApplicationContext(),
							"不能超过" + (mSumFenshu - Math.max(rengouInput, minRengou)) + "元",
							Toast.LENGTH_SHORT).show();
				}
				mTvPay.setText(String.valueOf(baodiInput + rengouInput));
			}
		});
		mEtRenGou.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				if (TextUtils.isEmpty(s)) {
					rengouInput = 0;
				} else {
					rengouInput = Integer.valueOf(s.toString());

				}

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
                 
				int selectionEnd = mEtRenGou.getSelectionEnd();
				if (baodiInput + rengouInput > mSumFenshu) {
					Toast.makeText(getApplicationContext(),
							"不能超过" + (mSumFenshu - baodiInput) + "元",
							Toast.LENGTH_SHORT).show();
					s.delete(selectionEnd - 1, selectionEnd);
					int tempSelection = selectionEnd - 1;
					if (TextUtils.isEmpty(s)) {
						rengouInput = 0;
					} else {
						rengouInput = Integer.valueOf(s.toString());
					}
					mEtRenGou.setSelection(tempSelection);
				}
				mTvPay.setText(String.valueOf(baodiInput + rengouInput));
			}
		});
	}
	private void _showHemaiDialog() {   
		final Dialog localDialog = new Dialog(SumbitGroupBuy.this, R.style.Theme_dialog);
		View view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.dialog_submit_bet, null);
		TextView dialogTitle = (TextView) view
				.findViewById(R.id.submit_dialog_title);
		dialogTitle.setText("提示");
		TextView dialogContent = (TextView) view
				.findViewById(R.id.submit_dialog_content);
		dialogContent.setText("方案总金额 " + mSumFenshu + "元！\n您的认购金额"+rengouInput+"元！\n您保底了" + baodiInput+"元！\n您确定要发起合买吗？");
		TextView localCancel = (TextView) view.findViewById(R.id.tv_submit_cancel);
		TextView localOK = (TextView) view.findViewById(R.id.tv_submit_ok);
		localOK.setText("确定");
		localDialog.setContentView(view);
		Window dialogWindow = localDialog.getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		lp.width = 250 * (int)getResources().getDisplayMetrics().density; // 宽度
		dialogWindow.setAttributes(lp);
		localOK.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				 localDialog.cancel();
				 _doPostFormData();
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
		case R.id.groupBuy_leftmenu:
			finish();
			break;
		case R.id.group_buy_right_footer_btn:
			if (rengouInput < Math.max(minRengou, 1)) {
				Toast.makeText(getApplicationContext(), "至少购买"+Math.max(minRengou, 1)+"元",
						Toast.LENGTH_SHORT).show();
				break;
			}
			if (mUserid == 0) {
				Intent loginIntent = new Intent(this, Login.class);
				startActivityForResult(loginIntent, LOGINREQUESTCODE);
			} else {
				_showHemaiDialog();
			}
			break;
		case R.id.rv_per_money:
			if (mPerMoneyPop == null) {
				mPerMoneyPop = _getPopWindow(v.getWidth(), mPerMoneyData,
						mPerMoneyChecked, mPerMoneyListener,4);
			}
			mPerMoneyPop.showAsDropDown(v);
			break;
		case R.id.rv_security_level:
			if (mSecurityLevelPop == null) {
				mSecurityLevelPop = _getPopWindow(v.getWidth(),
						mSecurityLevelData, mSecurityLevelChecked,
						mSecurityLevelListener,3);
			}
			mSecurityLevelPop.showAsDropDown(v);
			break;
		case R.id.rv_salary_scale:
			if (mSalaryScalePop == null) {
				mSalaryScalePop = _getPopWindow(v.getWidth(), mSalaryScaleData,
						mSalaryScaleChecked, mSalaryScaleListener,4);
			}
			mSalaryScalePop.showAsDropDown(v);
			break;
		default:
			break;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub

		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case LOGINREQUESTCODE:
			Log.i(TAG, "登录成功");
			mUserid = sp.getInt("userid", 0);
			mUpk = sp.getString("upk", "");
			// _postFormData();
			break;
		default:
			break;
		}
	}

	private void _doPostFormData() {
			_postSSQFormData();
		
	}

	private PopupWindow _getPopWindow(int width, ArrayList<String> list,
			ArrayList<Boolean> checked, OnItemClickListener listener, int numClums) {
		View view = LayoutInflater.from(this).inflate(R.layout.popview, null);
		final PopupWindow mPopWindow = new PopupWindow(view, width,
				LayoutParams.WRAP_CONTENT);
		GridView mGridView = (GridView) view.findViewById(R.id.pop_gridview);
		mGridView.setNumColumns(numClums);
//		mGridView.setBackgroundColor(this.getResources().getColor(
//				android.R.color.transparent));
		PopViewAdapter mMyAdapter = new PopViewAdapter(this, list, checked);
		mGridView.setAdapter(mMyAdapter);
		mGridView.setOnItemClickListener(listener);
		mPopWindow.setFocusable(true);
		mPopWindow.setOutsideTouchable(true);
		mPopWindow.update();
		mPopWindow.setBackgroundDrawable(new BitmapDrawable(getResources(),
				Bitmap.createBitmap(1, 1, Config.ARGB_8888)));
		return mPopWindow;
	}

	private void _postSSQFormData() {
		RequestParams rawParams = new RequestParams();
		String brokerage = mTvSalaryScale.getText().toString();
		brokerage = brokerage.substring(0, brokerage.length() - 1);
		rawParams.add("brokerage", brokerage);
		rawParams.add("currentQihao", mQihao);
		String hemaiDescription = mEtTheme.getText().toString();
		rawParams.add("hemaiDescription", hemaiDescription);
		rawParams.add("hemaiTitle", mEtTitle.getText().toString());
		rawParams.add("insureCount", baodiInput + "");
		if(isZhuiHao) {
			rawParams.add("isZhuihao", "true");
		} else {
			rawParams.add("isZhuihao", "false");
		}
		if(isStopZhui) {
			rawParams.add("autoStopZhuihao", "true");
		} else {
			rawParams.add("autoStopZhuihao", "false");
		}
		
		
		
		rawParams.add("itemsContent", itemContent);
		rawParams.add("itemsSelectType", typeContent);
		rawParams.add("itemsBetCount", betCount);
		rawParams.add("qihaos", qihaos);
		rawParams.add("multiples", mutiples);

		rawParams.add("myBuy", rengouInput + "");
		String secretType = "0";
		if (mTvSecurityLevel.getText().toString()
				.equals(mSecurityLevelData.get(0))) {
			secretType = "0";
		} else if (mTvSecurityLevel.getText().toString()
				.equals(mSecurityLevelData.get(1))) {
			secretType = "1";
		} else {
			secretType = "2";
		}
		rawParams.add("secretType", secretType);
		rawParams.add("shoptype", "Hemai");
		rawParams.add("totalShare", mSumFenshu + "");
		rawParams.add("type", lotterytype);
		rawParams.add("zhuihaoTitle", mTitleStr);
		rawParams.add("userid", String.valueOf(mUserid));

		RequestParams paramsUrl = new RequestParams();
		paramsUrl.add("act", "buy");
		paramsUrl.add("lotterytype", lotterytype);
		paramsUrl.add("datatype", "json");
		paramsUrl.add("userid", String.valueOf(mUserid));
		paramsUrl.add("upk", mUpk);
		paramsUrl.add("jsoncallback",
				"jsonp" + String.valueOf(System.currentTimeMillis()));
		paramsUrl.add("_", String.valueOf(System.currentTimeMillis()));
		String url = AsyncHttpClient.getUrlWithQueryString(false,
				JinCaiZiHttpClient.BASE_URL, paramsUrl);

		_doHttpFormRequest(url, rawParams);

	}

	private String responseMsg = "投注失败！";
	private String successR = "";
	private String balance = "";
	private JinCaiZiProgressDialog mProgressDialog;

	private void parseTouzhuJson(String jsonData) throws IOException {
		JsonReader reader = new JsonReader(new StringReader(jsonData));
		reader.beginObject();

		while (reader.hasNext()) {
			String tagName = reader.nextName();
			if (tagName.equals("msg")) {
				responseMsg = reader.nextString();
			} else if (tagName.equals("orderId")) {
				reader.nextInt();
			} else if (tagName.equals("balance")) {
				balance = reader.nextString();
			} else if (tagName.equals("redirect")) {
				reader.nextInt();
			} else if (tagName.equals("result")) {
				successR = reader.nextString();
			}
		}
		reader.endObject();
		reader.close();

	}

	private void _doHttpFormRequest(String url,RequestParams requestparams) {
		      _showTouzhuProgress();
		 	  JinCaiZiHttpClient.postFormData(this, url, requestparams, new AsyncHttpResponseHandler() {
				@Override
				public void onSuccess(int statusCode, Header[] headers,
						byte[] responseBody) {
					try {
						String charset;
						if(Utils.isCmwapNet(SumbitGroupBuy.this)) {
							charset = "utf-8";
						} else {
							charset = "gb2312";
						}
						String jsonData = new String(responseBody, charset);
						Log.d(TAG, "post groupbuy form data = " + jsonData);
						parseTouzhuJson(jsonData);
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
						Toast.makeText(SumbitGroupBuy.this, "数据可能异常，请检查数据请求是否成功，以免重复请求造成损失", Toast.LENGTH_LONG).show();
					} catch (Exception e) {
						e.printStackTrace();
						Toast.makeText(SumbitGroupBuy.this, "数据可能异常，请检查数据请求是否成功，以免重复请求造成损失", Toast.LENGTH_LONG).show();
					} finally {
						// FIXME reader.close should be here
						final Dialog localDialog = new Dialog(SumbitGroupBuy.this, R.style.Theme_dialog);
					View view = LayoutInflater.from(getApplicationContext()).inflate(
								R.layout.dialog_submit_bet, null);
						TextView dialogTitle = (TextView) view
								.findViewById(R.id.submit_dialog_title);
						dialogTitle.setText("提示");
						TextView dialogContent = (TextView) view
								.findViewById(R.id.submit_dialog_content);
						TextView dialogCancel = (TextView) view.findViewById(R.id.tv_submit_cancel);
						dialogCancel.setVisibility(View.GONE);
						////view.findViewById(R.id.dialog_divider).setVisibility(View.GONE);
						TextView dialogOK = (TextView) view.findViewById(R.id.tv_submit_ok);
						dialogOK.setText("确定");
						localDialog.setContentView(view);
						Window dialogWindow = localDialog.getWindow();
						WindowManager.LayoutParams lp = dialogWindow.getAttributes();
						lp.width = 300; // 宽度
						dialogWindow.setAttributes(lp);
						dialogOK.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								 localDialog.cancel();
								 if(!TextUtils.isEmpty(successR) && successR.equals("success")) {
								     finish();
								 } else if(!TextUtils.isEmpty(responseMsg) && responseMsg.equals("未登录或登录过期！")) {
									 CheckLogin.clearLoginStatus(sp);
									 Intent loginIntent = new Intent(SumbitGroupBuy.this, Login.class);
									 startActivityForResult(loginIntent, LOGINREQUESTCODE);
								 }
								
							}
						});
						if(!TextUtils.isEmpty(successR) && successR.equals("success")) {
							dialogContent.setText(responseMsg + "\n" + "当前余额 " + balance + "元");
							
						} else {
							dialogContent.setText(responseMsg);
						}
						localDialog.show();
					}
					_hideTouzhuProgress();
				}

				@Override
				public void onFailure(int statusCode, Header[] headers,
						byte[] responseBody, Throwable error) {
					//Log.d(TAG, "failure = " + error.toString());
					_hideTouzhuProgress();
					responseMsg = "投注失败, 请重试！";
					Toast.makeText(SumbitGroupBuy.this, responseMsg, Toast.LENGTH_SHORT).show();
					//finish();
				}
			});  
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
