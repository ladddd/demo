package com.jincaizi.kuaiwin.hemaicenter;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.http.Header;

import com.google.gson.stream.JsonReader;
import com.jincaizi.adapters.HemaiDetailAdapter;
import com.jincaizi.bean.HemaiCenterRecordEntity;
import com.jincaizi.http.JinCaiZiHttpClient;
import com.jincaizi.R;
import com.jincaizi.kuaiwin.mylottery.Login;
import com.jincaizi.kuaiwin.tool.CheckLogin;
import com.jincaizi.kuaiwin.utils.SafeAsyncTask;
import com.jincaizi.kuaiwin.utils.Utils;
import com.jincaizi.kuaiwin.widget.JinCaiZiProgressDialog;
import com.jincaizi.vendor.http.AsyncHttpClient;
import com.jincaizi.vendor.http.AsyncHttpResponseHandler;
import com.jincaizi.vendor.http.RequestHandle;
import com.jincaizi.vendor.http.RequestParams;

public class HemaiDetailActivity extends Activity implements OnClickListener {
	private static final String TAG = "HemaiDetailActivity";
	private static final int LOGINREQUESTCODE = 4;
	private ListView mListView;
	private ImageView mBackBtn;
	private LinkedList<HashMap<String, String>> mRecordList = new LinkedList<HashMap<String, String>>();
	private LinkedList<HashMap<String, String>> mDetailList = new LinkedList<HashMap<String, String>>();
	
	private LinkedList<HashMap<String, String>> mTempRecord = new LinkedList<HashMap<String, String>>();
	private LinkedList<String> mType = new LinkedList<String>();
	private LinkedList<String> mTempType = new LinkedList<String>();
	private HemaiDetailAdapter mMyAdapter;
	private String mLotteryKind;// 彩票种类，数字彩 or竞彩
	public ArrayList<String> mContent = new ArrayList<String>();
	public ArrayList<String> mChuan = new ArrayList<String>();
	public ArrayList<String> mZhu = new ArrayList<String>();
	public ArrayList<String> mResult = new ArrayList<String>();
	private JinCaiZiProgressDialog mProgressDialog;
	private RequestHandle myRequestHandle;
	public HemaiCenterRecordEntity mEntity;
	private HemaiDetailActivity mActivity;
	private SharedPreferences sp;
	private String mUserId = "";
	private String mUpk = "";
	private Integer privacyLevel;
	private TextView mMoneyToPay;
	private TextView mSubmit;
	private EditText mNumToBuy;
	private int mLeftToBuy = 0;
	private TextView mTvSub;
	private TextView mTvAdd;
	protected float mLeftFenshu;
	private TextView mLeftView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.group_buy_detail_layout);
		this.mActivity = this;
		_showProgress("正在更新......");
		_getUserId();
		_initData(getIntent());
		// _fakeData();
		_findViews();
		_setListener();
		mMyAdapter = new HemaiDetailAdapter(this, mRecordList, mType,
				mLotteryKind);
		mListView.setAdapter(mMyAdapter);
		_requestData();
	}

	private void _getUserId() {
		sp = getSharedPreferences("loginStatus", Context.MODE_PRIVATE);
		int userid = sp.getInt("userid", 0);
		mUpk = sp.getString("upk", "");
		if (userid == 0) {
			mUserId = "";
		} else {
			mUserId = String.valueOf(userid);
		}
	}

	private void _initData(Intent intent) {
		mLotteryKind = intent.getStringExtra("lotterykind");
		mEntity = (HemaiCenterRecordEntity) intent
				.getSerializableExtra("hemaientity");
		if(TextUtils.isEmpty(mEntity.getBetLeft())) {
			mLeftToBuy = 0;
		} else {
			mLeftToBuy = Integer.valueOf(mEntity.getBetLeft());
		}
        
	}

	private void _findViews() {
		mBackBtn = (ImageView) findViewById(R.id.touzhu_leftmenu);
		TextView mTitle = (TextView) findViewById(R.id.current_lottery);
		mTitle.setText("方案详情");
		//findViewById(R.id.left_divider).setVisibility(View.GONE);
		findViewById(R.id.sumbit_group_buy).setVisibility(View.GONE);
		mListView = (ListView) findViewById(R.id.group_buy_detail_list);

		TextView tv = (TextView) findViewById(R.id.empty_mylotterylistview);
		mListView.setEmptyView(tv);
		tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				_showProgress("正在更新......");
				_requestData();
			}
		});

		mLeftView = (TextView) findViewById(R.id.tv_remain);
		mLeftView.setText(" (剩余" + mEntity.getBetLeft() + "份)");
		mMoneyToPay = (TextView) findViewById(R.id.money_to_pay);
		mSubmit = (TextView) findViewById(R.id.sumbit_insure);
		float jindu = Float.valueOf(mEntity.getBetJindu());
		if ((int) jindu == 100) {
			mMoneyToPay.setText("亲，本期已满员，下次下手要快哦");
			mSubmit.setVisibility(View.INVISIBLE);
		} else {
			if(!TextUtils.isEmpty(mEntity.getBetAverage())) {
				Float money = Float.valueOf(mEntity.getBetAverage());
				mMoneyToPay.setText(money+ "元");
			}
			
		}
		mNumToBuy = (EditText)findViewById(R.id.number_to_buy);
		mNumToBuy.addTextChangedListener(new MyWatcher());
		mTvSub  = (TextView)findViewById(R.id.tv_sub);
		mTvAdd = (TextView)findViewById(R.id.tv_add);
	}
    class MyWatcher implements TextWatcher {

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
			Float betAverage = Float.valueOf(mEntity.getBetAverage());
			int num = Integer.valueOf(s.toString());
			if(num > mLeftToBuy) {
				mNumToBuy.setText(mEntity.getBetLeft());
				Toast.makeText(mActivity, "您最多可认购" + mLeftToBuy + "份", Toast.LENGTH_SHORT).show();
				mMoneyToPay.setText((mLeftToBuy*betAverage) + "元");
			} else {
				mMoneyToPay.setText((num*betAverage) + "元");
			}
		}
    	
    }
	private void _setListener() {
		mBackBtn.setOnClickListener(this);
		mSubmit.setOnClickListener(this);
		mTvAdd.setOnClickListener(this);
		mTvSub.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_add:
			int addNum = 1;
			if(!TextUtils.isEmpty(mNumToBuy.getText())) {
				addNum = Integer.valueOf(mNumToBuy.getText().toString());
			} 
			mNumToBuy.setText(String.valueOf(addNum+1));
			break;
		case R.id.tv_sub:
			int subNum = 1;
			if(!TextUtils.isEmpty(mNumToBuy.getText())) {
				subNum = Integer.valueOf(mNumToBuy.getText().toString());
			} 
			if(subNum - 1 <= 0) {
				mNumToBuy.setText(String.valueOf(1));
			} else {
				mNumToBuy.setText(String.valueOf(subNum-1));
			}
			
			break;
		case R.id.touzhu_leftmenu:
			finish();
			break;
		case R.id.sumbit_insure:
			if (mUserId.equals("0")) {
				Intent loginIntent = new Intent(this, Login.class);
				startActivityForResult(loginIntent, LOGINREQUESTCODE);
			} else {
				_showHemaiDialog();
			}
			break;
		default:
			break;
		}
	}
	private void _showHemaiDialog() {   
		final Dialog localDialog = new Dialog(HemaiDetailActivity.this, R.style.Theme_dialog);
		View view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.dialog_submit_bet, null);
		TextView dialogTitle = (TextView) view
				.findViewById(R.id.submit_dialog_title);
		dialogTitle.setText("提示");
		TextView dialogContent = (TextView) view
				.findViewById(R.id.submit_dialog_content);
//		String myBuy = "1";
//		if(!TextUtils.isEmpty(mNumToBuy.getText())) {
//			myBuy = mNumToBuy.getText().toString();
//		}
		dialogContent.setText("您的认购金额"+ mMoneyToPay.getText().toString() +"！\n您确定要参与合买吗？");
		TextView localCancel = (TextView) view.findViewById(R.id.tv_submit_cancel);
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub

		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case LOGINREQUESTCODE:
			Log.i(TAG, "登录成功");
			mUserId = sp.getInt("userid", 0)+"";
			mUpk = sp.getString("upk", "");
			// _postFormData();
			break;
		default:
			break;
		}
	}
	private void _requestData() {
		RequestParams params = new RequestParams();
		params.add("act", "hmdetail");
		params.add("hemaiId", mEntity.getHemaiId());
		params.add("userid", mUserId);
		params.add("upk", mUpk);
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
							if(Utils.isCmwapNet(HemaiDetailActivity.this)) {
								charset = "utf-8";
							} else {
								charset = "gb2312";
							}
							String jsonData = new String(responseBody, charset);
							Log.d(TAG, "合买方案详情 detail = " + jsonData);
							mTempRecord.clear();
							mTempType.clear();
							if (mLotteryKind.equals("shuzicai")) {
								_jsonSZCDetailObject(jsonData);
							}

						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
							_hideProgress();
						} catch (Exception e) {
							e.printStackTrace();
							_hideProgress();
						} finally {
							// FIXME reader.close should be here
							// test
							// _hideProgress();
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							byte[] responseBody, Throwable error) {
						Log.d(TAG, "failure = " + error.toString());
						_hideProgress();
						Toast.makeText(mActivity, "更新失败", Toast.LENGTH_SHORT)
								.show();
					}
				});
	}
/**
 * 数字彩，请求的json数据解析
 * @param jsonData
 * @throws IOException
 */
	private void _jsonSZCDetailObject(final String jsonData) throws IOException {
		final JsonReader reader = new JsonReader(new StringReader(jsonData));
		SafeAsyncTask<LinkedList<HemaiCenterRecordEntity>> getAllDaigouTask = new SafeAsyncTask<LinkedList<HemaiCenterRecordEntity>>() {

			private String progressStr;
			private String totalAmountStr;
			private String hadByStr;
			private String perAmount;

			@Override
			public LinkedList<HemaiCenterRecordEntity> call() throws Exception {
				int returnResult = -2;
				String title = "";
				String xuanhaoTitle = "";
				reader.beginObject();
				while (reader.hasNext()) {
					String tagName = reader.nextName();
					if (tagName.equals("result")) {
						returnResult = reader.nextInt();
						Log.d(TAG, "result--->" + returnResult);

					} else if (tagName.equals("UserId")) {
						reader.nextString();

					} else if (tagName.equals("LotteryType")) {
						title = reader.nextString();
					} else if (tagName.equals("Qihao")) {
						HashMap<String, String> map0 = new HashMap<String, String>();
						map0.put("title", title);
						map0.put("qihao", "第" + reader.nextString() + "期");
						mTempRecord.add(map0);
						mTempType.add("title");

						

					} else if(tagName.equals("Progress")) {
						progressStr = reader.nextString();
					} else if(tagName.equals("TotalAmount")) {
						totalAmountStr = reader.nextString();
					} else if(tagName.equals("HadByAmount")) {
						hadByStr = reader.nextString();
					} else if(tagName.equals("PerAmount")) {
						perAmount = reader.nextString();
						HashMap<String, String> map1 = new HashMap<String, String>();
						map1.put("jindu", progressStr);
						map1.put("zongjine", totalAmountStr);
						map1.put("jineaverage", perAmount);
						mLeftFenshu = (Float.valueOf(totalAmountStr)-Float.valueOf(hadByStr))/Float.valueOf(perAmount);
						map1.put("left", String.valueOf((int)mLeftFenshu));
						map1.put("baodi", mEntity.getBetBaodi());
						mTempRecord.add(map1);
						mTempType.add("jindu");
					}
					
					else if (tagName.equals("UserName")) {
						HashMap<String, String> map2 = new HashMap<String, String>();
						map2.put("faqizhetitle", "发起者");
						map2.put("faqizhecontent", reader.nextString());
						mTempRecord.add(map2);
						mTempType.add("faqizheinfo");
					} else if (tagName.equals("Brokerage")) {
						HashMap<String, String> map4 = new HashMap<String, String>();
						map4.put("faqizhetitle", "佣金");
						map4.put("faqizhecontent", reader.nextString() + "%");
						mTempRecord.add(map4);
						mTempType.add("faqizheinfo");
					} else if (tagName.equals("PrivacyLevel")) {
						privacyLevel = Integer.valueOf(reader.nextString());
						if (privacyLevel == 0) {// 公开
							xuanhaoTitle = "";
						} else if (privacyLevel == 1) {// 跟单可见
							xuanhaoTitle = "跟单可见";
						} else {// 完全保密，开奖后公开
							xuanhaoTitle = "完全保密，开奖后可见";
						}

					} else if (tagName.equals("Detail")) {

						if (privacyLevel == 0) {
							_jsonReadDataArray(reader);
							HashMap<String, String> map7 = new HashMap<String, String>();
							map7.put("listtitle0", "选号详情");
							map7.put("listtitle1",
									String.valueOf(mDetailList.size()) + " 条");
							mTempRecord.add(map7);
							mTempType.add("xuanhaotitle");
							if (mLotteryKind.equals("shuzicai")) {
								for (int i = 0; i < mDetailList.size(); i++) {
									mTempRecord.add(mDetailList.get(i));
									mTempType.add("xuanhaocontent");
								}
							}
						} else {
							int index = jsonData.indexOf("\"Detail\":[");
							if(index != -1) {
								_jsonReadDataArray(reader);
								HashMap<String, String> map7 = new HashMap<String, String>();
								map7.put("listtitle0", "选号详情");
								map7.put("listtitle1",
										String.valueOf(mDetailList.size()) + " 条");
								mTempRecord.add(map7);
								mTempType.add("xuanhaotitle");
								if (mLotteryKind.equals("shuzicai")) {
									for (int i = 0; i < mDetailList.size(); i++) {
										mTempRecord.add(mDetailList.get(i));
										mTempType.add("xuanhaocontent");
									}
								}
							} else {
							reader.nextString();
							HashMap<String, String> map7 = new HashMap<String, String>();
							map7.put("listtitle0", "选号详情");
							map7.put("listtitle1", xuanhaoTitle);
							mTempRecord.add(map7);
							mTempType.add("xuanhaotitle");
							}
						}
					} else if (tagName.equals("Title")) {
						HashMap<String, String> map5 = new HashMap<String, String>();
						map5.put("infotitle", "方案标题");
						map5.put("infocontent", reader.nextString());
						mTempRecord.add(map5);
						mTempType.add("fangan");

					} else if (tagName.equals("Description")) {
						HashMap<String, String> map6 = new HashMap<String, String>();
						map6.put("infotitle", "方案描述");
						map6.put("infocontent", reader.nextString());
						mTempRecord.add(map6);
						mTempType.add("fangan");
					} else if (tagName.equals("HemaiEndTime")) {
						reader.nextString();
					}
				}
				reader.endObject();
				return null;
			}

			@Override
			protected void onSuccess(LinkedList<HemaiCenterRecordEntity> t)
					throws Exception {
				// TODO Auto-generated method stub
				super.onSuccess(t);
				mRecordList.clear();
				mType.clear();
				for(int i=0; i< mTempRecord.size(); i++) {
					mRecordList.add(mTempRecord.get(i));
					mType.add(mTempType.get(i));
				}
				mMyAdapter = new HemaiDetailAdapter(mActivity, mRecordList,
						mType, mLotteryKind);
				mListView.setAdapter(mMyAdapter);
				mLeftView.setText(" (剩余" + (int)mLeftFenshu + "份)");
			}

			@Override
			protected void onThrowable(Throwable t) {
				// TODO Auto-generated method stub
				super.onThrowable(t);
			}

			@Override
			protected void onFinally() {
				// TODO Auto-generated method stub
				super.onFinally();
				_hideProgress();
				try {
					reader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		};
		getAllDaigouTask.execute();
	}
	

	private void _jsonReadDataArray(JsonReader reader) throws IOException {
		reader.beginArray();
		while (reader.hasNext()) {
			if (mLotteryKind.equals("shuzicai")) {
				_jsonReadDataObject(reader);
			}
		}
		reader.endArray();
	}

/**
 * 数字彩，除去BJDC，JCZQ，JCLQ，投注明细解析
 * @param reader
 * @throws IOException
 */
	private void _jsonReadDataObject(JsonReader reader) throws IOException {
		mDetailList.clear();
		reader.beginObject();
		String betType = "";
		String betContent = "";

		while (reader.hasNext()) {
			String tagName = reader.nextName();
			if (tagName.equals("DetailId")) {
				reader.nextString();
			} else if (tagName.equals("BetType")) {
				betType = reader.nextString();
			} else if (tagName.equals("BetContent")) {
				betContent = reader.nextString();
			} else if (tagName.equals("BetCount")) {
				reader.nextString();
			} else if (tagName.equals("Multiple")) {
				reader.nextString();
			}
		}
		reader.endObject();
		HashMap<String, String> map8 = new HashMap<String, String>();
		map8.put("listcontent0", betType);
		map8.put("listcontent1", betContent);
		mDetailList.add(map8);
	}

	protected void _showProgress(String hint) {
		mProgressDialog = JinCaiZiProgressDialog.show(this, hint);
		mProgressDialog.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				try {
					Log.d(TAG, "主动取消");
					if (myRequestHandle != null) {
						myRequestHandle.cancel(true);
					}
				} catch (Exception e) {
					Log.e(TAG, "无效返回", e);
				} finally {
				}
			}
		});
	}

	private void _hideProgress() {
		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
		}
	}
	
	private void _doPostFormData() {
		_showProgress("正在提交，请稍等！");
		RequestParams params = new RequestParams();
		params.add("act", "canyu");
		params.add("hemaiId", mEntity.getHemaiId());
		params.add("userid", mUserId);
		params.add("upk", mUpk);
		String myBuy = "1";
		if(!TextUtils.isEmpty(mNumToBuy.getText())) {
			myBuy = mNumToBuy.getText().toString();
		}
		params.add("buycount", myBuy);
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
							if(Utils.isCmwapNet(HemaiDetailActivity.this)) {
								charset = "utf-8";
							} else {
								charset = "gb2312";
							}
							String jsonData = new String(responseBody, charset);
							Log.d(TAG, "参与合买详情 detail = " + jsonData);
							JsonReader reader = new JsonReader(new StringReader(jsonData));
							reader.beginObject();
							while(reader.hasNext()) {
								String tagName = reader.nextName();
								if(tagName.equals("msg")) {
									canYuResponseMsg = reader.nextString();
								} else if(tagName.equals("result")) {
									canYuResponseResult = reader.nextInt();
								} else if(tagName.equals("bouns")) {
									canYuResonseBonus = reader.nextDouble();
								}
							}
							reader.endObject();
							reader.close();
							
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
							Toast.makeText(HemaiDetailActivity.this, "数据可能异常，请检查数据请求是否成功，以免重复请求造成损失", Toast.LENGTH_LONG).show();
						} catch (Exception e) {
							e.printStackTrace();
							Toast.makeText(HemaiDetailActivity.this, "数据可能异常，请检查数据请求是否成功，以免重复请求造成损失", Toast.LENGTH_LONG).show();
						} finally {
							 _hideProgress();
							 final Dialog localDialog = new Dialog(HemaiDetailActivity.this, R.style.Theme_dialog);
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
											 if(canYuResponseResult != 0 && canYuResponseMsg.equals("未登录或登录过期！")) {
												 CheckLogin.clearLoginStatus(sp);
												 //Intent loginIntent = new Intent(HemaiDetailActivity.this, Login.class);
												 //startActivityForResult(loginIntent, LOGINREQUESTCODE);
											 } else if(canYuResponseResult == 0) {
												 _showProgress("刷新中。。。");
												 _requestData();
											 }
											 
										}
									});
									if(canYuResponseResult == 0) {
										dialogContent.setText(canYuResponseMsg + "\n" + "当前余额 " + canYuResonseBonus + "元");
										
									} else {
										dialogContent.setText(canYuResponseMsg);
									}
									localDialog.show();
							 
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							byte[] responseBody, Throwable error) {
						Log.d(TAG, "failure = " + error.toString());
						_hideProgress();
						Toast.makeText(mActivity, "参与合买失败， 请重试", Toast.LENGTH_SHORT)
								.show();
					}
				});
	}
	private String canYuResponseMsg = "参与合买失败";
	private int canYuResponseResult = -1;
	private double canYuResonseBonus = 0;
}
