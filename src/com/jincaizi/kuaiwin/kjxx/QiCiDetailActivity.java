package com.jincaizi.kuaiwin.kjxx;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

import org.apache.http.Header;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.stream.JsonReader;
import com.jincaizi.R;
import com.jincaizi.http.JinCaiZiHttpClient;
import com.jincaizi.kuaiwin.utils.SafeAsyncTask;
import com.jincaizi.kuaiwin.utils.Utils;
import com.jincaizi.kuaiwin.widget.JinCaiZiProgressDialog;
import com.jincaizi.vendor.http.AsyncHttpClient;
import com.jincaizi.vendor.http.AsyncHttpResponseHandler;
import com.jincaizi.vendor.http.RequestHandle;
import com.jincaizi.vendor.http.RequestParams;

public class QiCiDetailActivity extends Activity implements OnClickListener{
	private static final String TAG = "QiCiDetailActivity";
	private ImageView mBackBtn;
	private String mLotteryType;
	private String mLotteryName;
	private String mQihao;
	private JinCaiZiProgressDialog mProgressDialog;
	private RequestHandle myRequestHandle;
	private Activity mActivity;
	private LinkedList<HashMap<String, String>>mDetail = new LinkedList<HashMap<String,String>>();
	private LinearLayout mLvJiangChi;
	private LinearLayout mLvJiangXiang;
	private Button mCheckMatch;
	private String mTime;
	private String mContent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.qicai_detail_layout);
		this.mActivity = this;
		_showProgress();
		_initData(getIntent());
		_findViews();
		_setListener();
		_requestData(false, mQihao);
	}
	private void _initData(Intent intent) {
		mLotteryType = intent.getStringExtra("lotterytype");
		mLotteryName = intent.getStringExtra("lotteryname");
		mQihao = intent.getStringExtra("qihao");
		mTime = intent.getStringExtra("time");
		mContent= intent.getStringExtra("content");
	}
	private void _findViews() {
		mBackBtn = (ImageView) findViewById(R.id.touzhu_leftmenu);
		TextView mTitle = (TextView) findViewById(R.id.current_lottery);
		mTitle.setText("期次详情");
		findViewById(R.id.right_divider).setVisibility(View.GONE);
		findViewById(R.id.sumbit_group_buy).setVisibility(View.GONE);
		mLvJiangChi = (LinearLayout)findViewById(R.id.jiangchi_panel);
		mLvJiangXiang = (LinearLayout)findViewById(R.id.jiangxiang_panel);
		mCheckMatch = (Button)findViewById(R.id.checkMatchDetail);
		
		TextView mLotteryTitle = (TextView)findViewById(R.id.qici_lottery_name);
		mLotteryTitle.setText(mLotteryName);
		TextView mLotteryQihao = (TextView)findViewById(R.id.qici_qihao);
		mLotteryQihao.setText("第"+mQihao+"期");
		TextView mLotteryTime = (TextView)findViewById(R.id.qici_time);
		mLotteryTime.setText(mTime);
		TextView mLotteryContent = (TextView)findViewById(R.id.qici_result);
		mLotteryContent.setText(mContent);
//		stubDlt = (ViewStub) findViewById(R.id.viewstub_qici_dlt);
//		stubSsq = (ViewStub) findViewById(R.id.viewstub_qici_ssq);
		//stubSsq.inflate();
	}
	private void _setListener() {
		mBackBtn.setOnClickListener(this);
		mCheckMatch.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()) {
		case R.id.touzhu_leftmenu:
			finish();
			break;
			default:
				break;
		}
	}
	private void _requestData(final boolean isClearList, String qihao) {
		RequestParams params = new RequestParams();
	    params.add("act", "kjDetail");
	    params.add("lotterytype", mLotteryType);
	    params.add("qihao", qihao);
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
							if(Utils.isCmwapNet(QiCiDetailActivity.this)) {
								charset = "utf-8";
							} else {
								charset = "gb2312";
							}
							String jsonData = new String(responseBody, charset);
							Log.d(TAG, "期次详情 detail = " + jsonData);
							//_jsonReadKJObject(jsonData, isClearList);
							//SSQ， DLT，PL5，QXC， QLC
                            if(mLotteryType.equals("SSQ") ||
                            		mLotteryType.equals("DLT") ||
                            		mLotteryType.equals("PL5") ||
                            		mLotteryType.equals("QXC") ||
                            				mLotteryType.equals("QLC")) {
                            	_jsonReadDLT(jsonData);
                            }  else if(mLotteryType.equals("FC3D") ||
                            		mLotteryType.equals("PL3")) {
                            	_jsonReadFC3D(jsonData);
                            }
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
							_hideProgress();
							
						} catch (Exception e) {
							e.printStackTrace();
							_hideProgress();
						} finally {
							// FIXME reader.close should be here
							
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							byte[] responseBody, Throwable error) {
						Log.d(TAG, "failure = " + error.toString());
						_hideProgress();
						Toast.makeText(mActivity, "更新失败", Toast.LENGTH_SHORT).show();
					}
				});
	}
	
	
	/**
	 * SSQ， DLT，PL5，QXC， QLC
	 * @param jsonData
	 */
	private void _jsonReadDLT(String jsonData) {
		final JsonReader reader = new JsonReader(new StringReader(jsonData));
		SafeAsyncTask<Void> getAllDaigouTask = new SafeAsyncTask<Void>() {
			@Override
			public Void call() throws Exception {
				int returnResult = -2;
				reader.beginObject();
				while (reader.hasNext()) {
					String tagName = reader.nextName();
					if (tagName.equals("result")) {
						returnResult = reader.nextInt();
						Log.d(TAG, "result--->" + returnResult);

					} else if (tagName.equals("BetAmount")) {
						HashMap<String, String>ba= new HashMap<String, String>();
						ba.put("BetAmount", reader.nextString());
						mDetail.add(ba);
					} else if (tagName.equals("Jiangchi")) {
						HashMap<String, String>jc= new HashMap<String, String>();
						jc.put("Jiangchi", reader.nextString());
						mDetail.add(jc);
					} else if(tagName.equals("FirstCount")) {
						HashMap<String, String>maptitle= new HashMap<String, String>();
						maptitle.put("FirstTitle", "一等奖");
						mDetail.add(maptitle);
						HashMap<String, String>map= new HashMap<String, String>();
						map.put("FirstCount", reader.nextString());
						mDetail.add(map);
					} else if(tagName.equals("FirstBonus")) {
						HashMap<String, String>map= new HashMap<String, String>();
						map.put("FirstBonus", reader.nextString());
						mDetail.add(map);
					} else if(tagName.equals("FirstZjCount")) {
						HashMap<String, String>maptitle= new HashMap<String, String>();
						maptitle.put("FirstZjTitle", "一等奖，追加");
						mDetail.add(maptitle);
						HashMap<String, String>map= new HashMap<String, String>();
						map.put("FirstZjCount", reader.nextString());
						mDetail.add(map);
					} else if(tagName.equals("FirstZjBonus")) {
						HashMap<String, String>map= new HashMap<String, String>();
						map.put("FirstZjBonus", reader.nextString());
						mDetail.add(map);
					} else if(tagName.equals("SecondCount")) {
						HashMap<String, String>maptitle= new HashMap<String, String>();
						maptitle.put("SecondTitle", "二等奖");
						mDetail.add(maptitle);
						HashMap<String, String>map= new HashMap<String, String>();
						map.put("SecondCount", reader.nextString());
						mDetail.add(map);
					} else if(tagName.equals("SecondBonus")) {
						HashMap<String, String>map= new HashMap<String, String>();
						map.put("SecondBonus", reader.nextString());
						mDetail.add(map);
					} else if(tagName.equals("SecondZjCount")) {
						HashMap<String, String>maptitle= new HashMap<String, String>();
						maptitle.put("SecondZjTitle", "二等奖，追加");
						mDetail.add(maptitle);
						HashMap<String, String>map= new HashMap<String, String>();
						map.put("SecondZjCount", reader.nextString());
						mDetail.add(map);
					} else if(tagName.equals("SecondZjBonus")) {
						HashMap<String, String>map= new HashMap<String, String>();
						map.put("SecondZjBonus", reader.nextString());
						mDetail.add(map);
					}else if(tagName.equals("ThirdCount")) {
						HashMap<String, String>maptitle= new HashMap<String, String>();
						maptitle.put("ThirdTitle", "三等奖");
						mDetail.add(maptitle);
						HashMap<String, String>map= new HashMap<String, String>();
						map.put("ThirdCount", reader.nextString());
						mDetail.add(map);
					} else if(tagName.equals("ThirdBonus")) {
						HashMap<String, String>map= new HashMap<String, String>();
						map.put("ThirdBonus", reader.nextString());
						mDetail.add(map);
					}else if(tagName.equals("ThirdZjCount")) {
						HashMap<String, String>maptitle= new HashMap<String, String>();
						maptitle.put("ThirdZjTitle", "三等奖，追加");
						mDetail.add(maptitle);
						HashMap<String, String>map= new HashMap<String, String>();
						map.put("ThirdZjCount", reader.nextString());
						mDetail.add(map);
					} else if(tagName.equals("ThirdZjBonus")) {
						HashMap<String, String>map= new HashMap<String, String>();
						map.put("ThirdZjBonus", reader.nextString());
						mDetail.add(map);
					} else if(tagName.equals("ForthZjCount")) {
						HashMap<String, String>maptitle= new HashMap<String, String>();
						maptitle.put("ForthZjTitle", "四等奖，追加");
						mDetail.add(maptitle);
						HashMap<String, String>map= new HashMap<String, String>();
						map.put("ForthZjCount", reader.nextString());
						mDetail.add(map);
					} else if(tagName.equals("ForthZjBonus")) {
						HashMap<String, String>map= new HashMap<String, String>();
						map.put("ForthZjBonus", reader.nextString());
						mDetail.add(map);
					} 
					else if(tagName.equals("ForthCount")) {
						HashMap<String, String>maptitle= new HashMap<String, String>();
						maptitle.put("ForthTitle", "四等奖");
						mDetail.add(maptitle);
						HashMap<String, String>map= new HashMap<String, String>();
						map.put("ForthCount", reader.nextString());
						mDetail.add(map);
					} else if(tagName.equals("ForthBonus")) {
						HashMap<String, String>map= new HashMap<String, String>();
						map.put("ForthBonus", reader.nextString());
						mDetail.add(map);
					} else if(tagName.equals("FifthCount")) {
						HashMap<String, String>maptitle= new HashMap<String, String>();
						maptitle.put("FifthTitle", "五等奖");
						mDetail.add(maptitle);
						HashMap<String, String>map= new HashMap<String, String>();
						map.put("FifthCount", reader.nextString());
						mDetail.add(map);
					} else if(tagName.equals("FifthBonus")) {
						HashMap<String, String>map= new HashMap<String, String>();
						map.put("FifthBonus", reader.nextString());
						mDetail.add(map);
					} else if(tagName.equals("FifthZjCount")) {
						HashMap<String, String>maptitle= new HashMap<String, String>();
						maptitle.put("FifthZjTitle", "五等奖，追加");
						mDetail.add(maptitle);
						HashMap<String, String>map= new HashMap<String, String>();
						map.put("FifthZjCount", reader.nextString());
						mDetail.add(map);
					} else if(tagName.equals("FifthZjBonus")) {
						HashMap<String, String>map= new HashMap<String, String>();
						map.put("FifthZjBonus", reader.nextString());
						mDetail.add(map);
					}else if(tagName.equals("SixthCount")) {
						HashMap<String, String>maptitle= new HashMap<String, String>();
						maptitle.put("SixthTitle", "六等奖");
						mDetail.add(maptitle);
						HashMap<String, String>map= new HashMap<String, String>();
						map.put("SixthCount", reader.nextString());
						mDetail.add(map);
					} else if(tagName.equals("SixthBonus")) {
						HashMap<String, String>map= new HashMap<String, String>();
						map.put("SixthBonus", reader.nextString());
						mDetail.add(map);
					}else if(tagName.equals("SixthZjCount")) {
						HashMap<String, String>maptitle= new HashMap<String, String>();
						maptitle.put("SixthZjTitle", "六等奖，追加");
						mDetail.add(maptitle);
						HashMap<String, String>map= new HashMap<String, String>();
						map.put("SixthZjCount", reader.nextString());
						mDetail.add(map);
					} else if(tagName.equals("SixthZjBonus")) {
						HashMap<String, String>map= new HashMap<String, String>();
						map.put("SixthZjBonus", reader.nextString());
						mDetail.add(map);
					}else if(tagName.equals("SeventhCount")) {
						HashMap<String, String>maptitle= new HashMap<String, String>();
						maptitle.put("SeventhTitle", "七等奖");
						mDetail.add(maptitle);
						HashMap<String, String>map= new HashMap<String, String>();
						map.put("SeventhCount", reader.nextString());
						mDetail.add(map);
					} else if(tagName.equals("SeventhBonus")) {
						HashMap<String, String>map= new HashMap<String, String>();
						map.put("SeventhBonus", reader.nextString());
						mDetail.add(map);
					}else if(tagName.equals("SeventhZjCount")) {
						HashMap<String, String>maptitle= new HashMap<String, String>();
						maptitle.put("SeventhZjTitle", "七等奖，追加");
						mDetail.add(maptitle);
						HashMap<String, String>map= new HashMap<String, String>();
						map.put("SeventhZjCount", reader.nextString());
						mDetail.add(map);
					} else if(tagName.equals("SeventhZjBonus")) {
						HashMap<String, String>map= new HashMap<String, String>();
						map.put("SeventhZjBonus", reader.nextString());
						mDetail.add(map);
					}else if(tagName.equals("EighthCount")) {
						HashMap<String, String>maptitle= new HashMap<String, String>();
						maptitle.put("EighthTitle", "八等奖");
						mDetail.add(maptitle);
						HashMap<String, String>map= new HashMap<String, String>();
						map.put("EighthCount", reader.nextString());
						mDetail.add(map);
					} else if(tagName.equals("EighthBonus")) {
						HashMap<String, String>map= new HashMap<String, String>();
						map.put("EighthBonus", reader.nextString());
						mDetail.add(map);
					}
				}
				reader.endObject();
				return null;
			}

			@Override
			protected void onSuccess(Void t) throws Exception {
				// TODO Auto-generated method stub
				super.onSuccess(t);
				_drawTable();
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
	/**
	 * FC3D， PL3
	 * @param jsonData
	 */
	private void _jsonReadFC3D(String jsonData) {
		final JsonReader reader = new JsonReader(new StringReader(jsonData));
		SafeAsyncTask<Void> getAllDaigouTask = new SafeAsyncTask<Void>() {
			@Override
			public Void call() throws Exception {
				int returnResult = -2;
				reader.beginObject();
				while (reader.hasNext()) {
					String tagName = reader.nextName();
					if (tagName.equals("result")) {
						returnResult = reader.nextInt();
						Log.d(TAG, "result--->" + returnResult);

					} else if (tagName.equals("BetAmount")) {
						HashMap<String, String>ba= new HashMap<String, String>();
						ba.put("BetAmount", reader.nextString());
						mDetail.add(ba);
					} else if(tagName.equals("Z1Count")) {
						
						HashMap<String, String>maptitle= new HashMap<String, String>();
						maptitle.put("Z1Title", "直选");
						mDetail.add(maptitle);
						HashMap<String, String>map= new HashMap<String, String>();
						map.put("Z1Count", reader.nextString());
						mDetail.add(map);
					} else if(tagName.equals("Z1Bonus")) {
						HashMap<String, String>map= new HashMap<String, String>();
						map.put("Z1Bonus", reader.nextString());
						mDetail.add(map);
					} else if(tagName.equals("Z3Count")) {
						HashMap<String, String>maptitle= new HashMap<String, String>();
						maptitle.put("Z3Title", "组选3");
						mDetail.add(maptitle);
						HashMap<String, String>map= new HashMap<String, String>();
						map.put("Z3Count", reader.nextString());
						mDetail.add(map);
					} else if(tagName.equals("Z3Bonus")) {
						HashMap<String, String>map= new HashMap<String, String>();
						map.put("Z3Bonus", reader.nextString());
						mDetail.add(map);
					} else if(tagName.equals("Z6Count")) {
						HashMap<String, String>maptitle= new HashMap<String, String>();
						maptitle.put("Z6Title", "组选6");
						mDetail.add(maptitle);
						HashMap<String, String>map= new HashMap<String, String>();
						map.put("Z6Count", reader.nextString());
						mDetail.add(map);
					} else if(tagName.equals("Z6Bonus")) {
						HashMap<String, String>map= new HashMap<String, String>();
						map.put("Z6Bonus", reader.nextString());
						mDetail.add(map);
					} 
				}
				reader.endObject();
				return null;
			}

			@Override
			protected void onSuccess(Void t) throws Exception {
				// TODO Auto-generated method stub
				super.onSuccess(t);
				_drawTable();
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
	private void _drawTable() {
		for(int i=0; i<mDetail.size(); ) {
			if(mDetail.get(i).containsKey("BetAmount")) {		
				LinearLayout lv = new LinearLayout(mActivity);
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,1);
				lv.setLayoutParams(lp);
				lv.setOrientation(LinearLayout.VERTICAL);
				TextView tv = new TextView(mActivity);
				tv.setGravity(Gravity.CENTER);
				tv.setPadding(10, 5, 10, 5);
				tv.setText("本期销量");
				tv.setTextColor(mActivity.getResources().getColor(R.color.tc_default));
				tv.setBackgroundResource(R.drawable.fangxing_bg);
				lv.addView(tv);
				TextView xl = new TextView(mActivity);
				xl.setText(mDetail.get(i).get("BetAmount"));
				xl.setPadding(10, 5, 10, 5);
				xl.setGravity(Gravity.CENTER);
				xl.setTextColor(mActivity.getResources().getColor(R.color.red));
				xl.setBackgroundResource(R.drawable.fangxing_bg);
				lv.addView(xl);
				mLvJiangChi.addView(lv);
				i++;
			} else if(mDetail.get(i).containsKey("Jiangchi")) {
				LinearLayout lv = new LinearLayout(mActivity);
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,1);
				lv.setLayoutParams(lp);
				lv.setOrientation(LinearLayout.VERTICAL);
				TextView tv = new TextView(mActivity);
				tv.setText("奖池奖金");
				tv.setPadding(10, 5, 10, 5);
				tv.setGravity(Gravity.CENTER);
				tv.setTextColor(mActivity.getResources().getColor(R.color.tc_default));
				tv.setBackgroundResource(R.drawable.fangxing_bg);
				lv.addView(tv);
				TextView xl = new TextView(mActivity);
				xl.setPadding(10, 5, 10, 5);
				xl.setGravity(Gravity.CENTER);
				xl.setText(mDetail.get(i).get("Jiangchi"));
				xl.setTextColor(mActivity.getResources().getColor(R.color.red));
				xl.setBackgroundResource(R.drawable.fangxing_bg);
				lv.addView(xl);
				mLvJiangChi.addView(lv);
				i++;
			} else if(mDetail.get(i).containsKey("FirstTitle") || mDetail.get(i).containsKey("Z1Title")) {
				//绘制奖项表格的表头
				LinearLayout lv = new LinearLayout(mActivity);
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				lv.setLayoutParams(lp);
				lv.setOrientation(LinearLayout.HORIZONTAL);
				TextView tv = new TextView(mActivity);
				tv.setText("奖项");
				tv.setPadding(10, 5, 10, 5);
				tv.setGravity(Gravity.CENTER);
				tv.setTextColor(mActivity.getResources().getColor(R.color.tc_default));
				tv.setBackgroundResource(R.drawable.fangxing_bg);
				lv.addView(tv, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT,1));
				TextView tv1 = new TextView(mActivity);
				tv1.setText("中奖注数");
				tv1.setPadding(10, 5, 10, 5);
				tv1.setGravity(Gravity.CENTER);
				tv1.setTextColor(mActivity.getResources().getColor(R.color.tc_default));
				tv1.setBackgroundResource(R.drawable.fangxing_bg);
				lv.addView(tv1, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT,1));
				TextView tv2 = new TextView(mActivity);
				tv2.setText("每注金额");
				tv2.setPadding(10, 5, 10, 5);
				tv2.setGravity(Gravity.CENTER);
				tv2.setTextColor(mActivity.getResources().getColor(R.color.tc_default));
				tv2.setBackgroundResource(R.drawable.fangxing_bg);
				lv.addView(tv2, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT,1));
				mLvJiangXiang.addView(lv);			
				LinearLayout lv1 = new LinearLayout(mActivity);
				//绘制奖项表格的第一行
				String valueStr="";
				Set<HashMap.Entry<String, String>> entryseSet=mDetail.get(i++).entrySet();
				  for (HashMap.Entry<String, String> entry:entryseSet) {
				        // keyStr[index] = entry.getKey();
				         valueStr= entry.getValue();
				         break;
				  }
				  
				LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				lv1.setLayoutParams(lp1);
				lv1.setOrientation(LinearLayout.HORIZONTAL);
				TextView detailtv = new TextView(mActivity);
				detailtv.setText(valueStr);
				detailtv.setPadding(10, 5, 10, 5);
				detailtv.setGravity(Gravity.CENTER);
				detailtv.setTextColor(mActivity.getResources().getColor(R.color.tc_default));
				detailtv.setBackgroundResource(R.drawable.fangxing_bg);
				lv1.addView(detailtv, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT,1));
				
				entryseSet=mDetail.get(i++).entrySet();
				  for (HashMap.Entry<String, String> entry:entryseSet) {
				        // keyStr[index] = entry.getKey();
				         valueStr= entry.getValue();
				         break;
				  }
				TextView detailtv1 = new TextView(mActivity);
				detailtv1.setText(valueStr);
				detailtv1.setPadding(10, 5, 10, 5);
				detailtv1.setGravity(Gravity.CENTER);
				detailtv1.setTextColor(mActivity.getResources().getColor(R.color.black));
				detailtv1.setBackgroundResource(R.drawable.fangxing_bg);
				lv1.addView(detailtv1, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT,1));
				
				entryseSet=mDetail.get(i++).entrySet();
				  for (HashMap.Entry<String, String> entry:entryseSet) {
				        // keyStr[index] = entry.getKey();
				         valueStr= entry.getValue();
				         break;
				  }
				  
				TextView detailtv2 = new TextView(mActivity);
				detailtv2.setText(valueStr);
				detailtv2.setPadding(10, 5, 10, 5);
				detailtv2.setGravity(Gravity.CENTER);
				detailtv2.setTextColor(mActivity.getResources().getColor(R.color.red));
				detailtv2.setBackgroundResource(R.drawable.fangxing_bg);
				lv1.addView(detailtv2, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT,1));
				mLvJiangXiang.addView(lv1);
			} else {
				//绘制奖项表格的剩余行
				//String[]keyStr = new String[3];
				String valueStr="";
				Set<HashMap.Entry<String, String>> entryseSet=mDetail.get(i++).entrySet();
				  for (HashMap.Entry<String, String> entry:entryseSet) {
				        // keyStr[index] = entry.getKey();
				         valueStr= entry.getValue();
				         break;
				  }

				LinearLayout lv1 = new LinearLayout(mActivity);
				LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				lv1.setLayoutParams(lp1);
				lv1.setOrientation(LinearLayout.HORIZONTAL);
				TextView detailtv = new TextView(mActivity);
				detailtv.setText(valueStr);
				detailtv.setPadding(10, 5, 10, 5);
				detailtv.setGravity(Gravity.CENTER);
				detailtv.setTextColor(mActivity.getResources().getColor(R.color.tc_default));
				detailtv.setBackgroundResource(R.drawable.fangxing_bg);
				lv1.addView(detailtv, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT,1));
				entryseSet.clear();
				entryseSet=mDetail.get(i++).entrySet();
				  for (HashMap.Entry<String, String> entry:entryseSet) {
				        // keyStr[index] = entry.getKey();
				         valueStr= entry.getValue();
				         break;
				  }
				TextView detailtv1 = new TextView(mActivity);
				detailtv1.setText(valueStr);
				detailtv1.setPadding(10, 5, 10, 5);
				detailtv1.setGravity(Gravity.CENTER);
				detailtv1.setTextColor(mActivity.getResources().getColor(R.color.black));
				detailtv1.setBackgroundResource(R.drawable.fangxing_bg);
				lv1.addView(detailtv1, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT,1));
				entryseSet.clear();
				entryseSet=mDetail.get(i++).entrySet();
				  for (HashMap.Entry<String, String> entry:entryseSet) {
				        // keyStr[index] = entry.getKey();
				         valueStr= entry.getValue();
				         break;
				  }
				TextView detailtv2 = new TextView(mActivity);
				detailtv2.setText(valueStr);
				detailtv2.setPadding(10, 5, 10, 5);
				detailtv2.setGravity(Gravity.CENTER);
				detailtv2.setTextColor(mActivity.getResources().getColor(R.color.red));
				detailtv2.setBackgroundResource(R.drawable.fangxing_bg);
				lv1.addView(detailtv2, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT,1));
				mLvJiangXiang.addView(lv1);
			}
		}
	}
	
	protected void _showProgress() {
		mProgressDialog = JinCaiZiProgressDialog.show(this, "正在更新......");
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
}
