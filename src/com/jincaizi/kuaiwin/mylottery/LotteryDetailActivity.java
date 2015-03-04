package com.jincaizi.kuaiwin.mylottery;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
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
import org.apache.http.Header;

import com.google.gson.stream.JsonReader;
import com.jincaizi.bean.MyLotteryRecordEntity;
import com.jincaizi.http.JinCaiZiHttpClient;
import com.jincaizi.R;
import com.jincaizi.kuaiwin.BuyCenter;
import com.jincaizi.kuaiwin.utils.Constants;
import com.jincaizi.kuaiwin.utils.SafeAsyncTask;
import com.jincaizi.kuaiwin.utils.Utils;
import com.jincaizi.kuaiwin.widget.JinCaiZiProgressDialog;
import com.jincaizi.vendor.http.AsyncHttpClient;
import com.jincaizi.vendor.http.AsyncHttpResponseHandler;
import com.jincaizi.vendor.http.RequestHandle;
import com.jincaizi.vendor.http.RequestParams;

public class LotteryDetailActivity extends Activity implements OnClickListener {
	
	private static final String TAG = "LotteryDetailActivity";
	private ImageView mBackBtn;
	private TextView mTitle;
	private String userID;

	private String daiheType;
	private ImageView mIvLogo;
	private TextView mTvName;
	private TextView mQihao;
	private TextView mTime;
	private TextView mGoumaiType;
	private TextView mFaqiren;
	private TextView mAwardStatus;
	private TextView mJineAmount;
	private TextView mJineAverage;
	private TextView mTicheng;
	private TextView mProcedureStatus;
	private TextView mBeishu;
	private TextView mFenshuAmount;
	private TextView mBaodi;
	private TextView mJindu;

	private Button mCheckDetailBtn;
	private MyLotteryRecordEntity mEntity;
	private TextView allBonusView;
	private RequestHandle myRequestHandle;
	private String mUpk;
	private LinearLayout mLvMatch;
	private TextView mEmptyView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lottery_detail_layout);
		
		SharedPreferences sp = getSharedPreferences("loginStatus", Context.MODE_PRIVATE);
		userID = sp.getInt("userid", 0) +"";
		mUpk = sp.getString("upk", "");
		_initData(getIntent());
		_findViews();
		_setListener();
		_setValues();
		String lotteryType = mEntity.getLotteryType();
		if(lotteryType.equals("竞彩足球") || lotteryType.equals("竞彩篮球")|| lotteryType.equals("北京单场")) {
			_showProgress();
		    _getGameList();
		}
		
	}

	private void _initData(Intent intent) {
		mEntity = (MyLotteryRecordEntity) intent
				.getSerializableExtra(Constants.ENTITY);
		daiheType = intent.getStringExtra(Constants.LOTTERYDETAILTYPE);
	}

	private void _findViews() {
		mBackBtn = (ImageView) findViewById(R.id.touzhu_leftmenu);
		mTitle = (TextView) findViewById(R.id.current_lottery);
		mTitle.setText("方案详情");
		findViewById(R.id.right_divider).setVisibility(View.GONE);
		findViewById(R.id.sumbit_group_buy).setVisibility(View.GONE);
		mIvLogo = (ImageView) findViewById(R.id.lottery_detail_logo);
		mTvName = (TextView) findViewById(R.id.lottery_detail_name);
		mQihao = (TextView) findViewById(R.id.lottery_detail_qishu);
		mTime = (TextView) findViewById(R.id.lottery_detail_time);
		mGoumaiType = (TextView) findViewById(R.id.lottery_detail_type);
		mFaqiren = (TextView) findViewById(R.id.faqiren_content);
		mAwardStatus = (TextView) findViewById(R.id.zhongjiang_content);
		mJineAmount = (TextView) findViewById(R.id.jine_content);
		mJineAverage = (TextView) findViewById(R.id.average_content);
		mTicheng = (TextView) findViewById(R.id.ticheng_content);
		mProcedureStatus = (TextView) findViewById(R.id.mark_content);
		mBeishu = (TextView) findViewById(R.id.beishu_content);
		mFenshuAmount = (TextView) findViewById(R.id.zongfenshu_content);
		mBaodi = (TextView) findViewById(R.id.baodi_content);
		mJindu = (TextView) findViewById(R.id.jindu_content);
		
		mCheckDetailBtn = (Button)findViewById(R.id.check_detail_content);
		
		allBonusView = (TextView)findViewById(R.id.allbonus_view);//合买方案中奖
		
		mLvMatch = (LinearLayout)findViewById(R.id.my_touzhu_panel);
		
		mEmptyView = (TextView)findViewById(R.id.empty_mylotterylistview);
	}

	private void _setValues() {
		_setLotteryLogo(mEntity.getLotteryType(), mIvLogo, mTvName);
		mQihao.setText("第"+mEntity.getQiHao()+"期");
		mTime.setText(mEntity.getTime());
		if (daiheType.equals(Constants.LotteryDetailType.DAIGOU.toString())) {
			mGoumaiType.setText("代购");
			mJineAmount.setText(mEntity.getMoney());
		} else if (daiheType.equals(Constants.LotteryDetailType.HEMAI
				.toString())) {
			mGoumaiType.setText("合买");
			mJineAmount.setText(mEntity.getZongjine());
			mJineAverage.setText(mEntity.getBetAverage());
			mTicheng.setText(mEntity.getTicheng());
			findViewById(R.id.average_title).setVisibility(View.VISIBLE);
			mJineAverage.setVisibility(View.VISIBLE);
			findViewById(R.id.ticheng_title).setVisibility(View.VISIBLE);
			mTicheng.setVisibility(View.VISIBLE);
			findViewById(R.id.zongfenshu_title).setVisibility(View.VISIBLE);
			mFenshuAmount.setVisibility(View.VISIBLE);
			findViewById(R.id.jindu_title).setVisibility(View.VISIBLE);
			mJindu.setVisibility(View.VISIBLE);
			findViewById(R.id.baodi_title).setVisibility(View.VISIBLE);
			mBaodi.setVisibility(View.VISIBLE);

		} else {
			mGoumaiType.setText("");
		}
		mFaqiren.setText(mEntity.getFaqiren());
		mAwardStatus.setText(mEntity.getAwardStatus());
		mProcedureStatus.setText(mEntity.getProcedureStatus());
		mBeishu.setText(mEntity.getBeishu());
		mFenshuAmount.setText(mEntity.getBetAmount());
		mBaodi.setText(mEntity.getBaodi());
		mJindu.setText(mEntity.getJindu());
		
		if(!TextUtils.isEmpty(mEntity.getAllBonus())) {
			allBonusView.setVisibility(View.VISIBLE);
			allBonusView.setText("方案中奖："+mEntity.getAllBonus());
		}
	}

	private void _setLotteryLogo(String lotteryType, ImageView iv, TextView tv) {
		int index = 0;
    	for(int i=0; i<BuyCenter.itemName.length; i++) {
    		if(lotteryType.equals(BuyCenter.itemName[i])) {
    			index = i;
    			break;
    		}
    	}
    	iv.setBackgroundResource(BuyCenter.itemImage[index]);
    	tv.setText(lotteryType);
	}

	private void _setListener() {
		mBackBtn.setOnClickListener(this);
		mCheckDetailBtn.setOnClickListener(this);
		mEmptyView.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.empty_mylotterylistview:
			_showProgress();
		    _getGameList();
			break;
		case R.id.check_detail_content:
			if(httpResponseResult == -1) {
				Toast.makeText(LotteryDetailActivity.this, "方案保密", Toast.LENGTH_SHORT).show();
				break;
			}
			Intent detailIntent = new Intent(this, MySeftChooseActivity.class);
			detailIntent.putExtra(Constants.ENTITY, mEntity);
			detailIntent.putExtra(Constants.USERID, userID);
			detailIntent.putExtra(Constants.LOTTERYDETAILTYPE,daiheType);
			startActivity(detailIntent);
			break;
		case R.id.touzhu_leftmenu:
			finish();
			break;
		default:
			break;
		}
	}
	
	private void _getGameList() {
		RequestParams params = new RequestParams();
		if (daiheType.equals(Constants.LotteryDetailType.HEMAI
				.toString())){
			params.add("act", "hmcontent");
			params.add("hemaiId", mEntity.getId());
		} else {
			params.add("act", "dgcontent");
			params.add("DaigouId", mEntity.getId());
		}
		params.add("userid", userID + "");
		params.add("upk", mUpk);
		
		params.add("datatype", "json");
		params.add("jsoncallback",
				"jsonp" + String.valueOf(System.currentTimeMillis()));
		params.add("_", String.valueOf(System.currentTimeMillis()));
		String url = AsyncHttpClient.getUrlWithQueryString(false,
				JinCaiZiHttpClient.BASE_URL, params);
		myRequestHandle = JinCaiZiHttpClient.post(this, url, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
				try {
					// Object jsonResponse = parseResponse(responseBody);
					String charset;
					if(Utils.isCmwapNet(LotteryDetailActivity.this)) {
						charset = "utf-8";
					} else {
						charset = "gb2312";
					}
					String jsonData = new String(responseBody, charset);
					Log.d(TAG, "lotteryDetail all = " + jsonData);
					
					// 如果需要解析JSON数据，首要要生成一个JsonReader对象
					
					_jsonReadDaigouObject(jsonData);
				
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
					_hideProgress();
					mEmptyView.setVisibility(View.VISIBLE);
				} catch (Exception e) {
					e.printStackTrace();
					mEmptyView.setVisibility(View.VISIBLE);
					_hideProgress();
				} 	
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				Log.d(TAG, "failure = " + error.toString());
				_hideProgress();
				mEmptyView.setVisibility(View.VISIBLE);
				Toast.makeText(LotteryDetailActivity.this, "更新失败", Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	private StringBuilder touzhuInfo;
	private ArrayList<String> changciList = new ArrayList<String>();
	private ArrayList<String> homeTeam = new ArrayList<String>();
	private ArrayList<String> awayTeam = new ArrayList<String>();
	private ArrayList<String> betContent = new ArrayList<String>();
	private ArrayList<String> caiguo = new ArrayList<String>();
	private JinCaiZiProgressDialog mProgressDialog;
	private int httpResponseResult = -2;
    private void _jsonReadDaigouObject(String jsonData) {
    	final JsonReader reader = new JsonReader(new StringReader(
				jsonData));
        SafeAsyncTask<Void> getAllDaigouTask = new SafeAsyncTask<Void>() {
           
			@Override
            public Void call() throws Exception {
				HashMap<String, String>map = Utils.getBetType();
        		httpResponseResult = -2;
        		touzhuInfo = new StringBuilder();
        		reader.beginObject();
        		while (reader.hasNext()) {
        			String tagName = reader.nextName();
        			if (tagName.equals("result")) {
        				httpResponseResult = reader.nextInt();
        				Log.d(TAG, "result--->" + httpResponseResult);
        				
        			} else if (tagName.equals("MatchCount")) {
        				if(TextUtils.isEmpty(touzhuInfo.toString())) {
        					touzhuInfo.append(reader.nextString()+"场" );
        				} else {
        					touzhuInfo.append("," + reader.nextString()+"场");
        				}
        				
        			} else if(tagName.equals("BetType")) {
        				String str = map.get(reader.nextString());
        				if(!TextUtils.isEmpty(str)) {
        				    if(TextUtils.isEmpty(touzhuInfo.toString())) {
        					    touzhuInfo.append(str);
        				    } else {
        					    touzhuInfo.append("," + str);
        				    }
        				}
        			} else if(tagName.equals("PassWay")) {
        				 if(TextUtils.isEmpty(touzhuInfo.toString())) {
     					    touzhuInfo.append(reader.nextString());
     				    } else {
     					    touzhuInfo.append("," + reader.nextString());
     				    }
        			} else if(tagName.equals("Multiple")) {
        				 if(TextUtils.isEmpty(touzhuInfo.toString())) {
      					    touzhuInfo.append(reader.nextString() +"倍" );
      				    } else {
      					    touzhuInfo.append("," + reader.nextString()+"倍" );
      				    }
        			} else if (tagName.equals("Detail")) {
        				_jsonReadDataArray(reader); 
        				
        			}
        		}
        		reader.endObject();
                return null;
            }

			@Override
			protected void onSuccess(Void t) throws Exception {
				// TODO Auto-generated method stub
				super.onSuccess(t);
				if(httpResponseResult == 0) {
					mEmptyView.setVisibility(View.GONE);
				    _drawTable();
				} 
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
				if(httpResponseResult == -2) {
					Toast.makeText(LotteryDetailActivity.this, "获取彩果失败", Toast.LENGTH_SHORT).show();
					mEmptyView.setVisibility(View.VISIBLE);
				} else if(httpResponseResult == -1) {
					Toast.makeText(LotteryDetailActivity.this, "方案保密", Toast.LENGTH_SHORT).show();
					mEmptyView.setVisibility(View.GONE);
					
				}
			}

       
        };
        getAllDaigouTask.execute();
    }
    private void _jsonReadDataArray(JsonReader reader) throws Exception{
    	reader.beginArray();
    	while(reader.hasNext()) {
    		reader.beginObject();
    		StringBuilder builder = new StringBuilder();
    		while(reader.hasNext()) {
    			String tagName = reader.nextName();
    			if(tagName.equals("Qihao")) {
    				builder.append("第"+reader.nextString()+"期");
    			} else if(tagName.equals("Week")) {
    				builder.append("\n" + reader.nextString());
    			} else if(tagName.equals("Queue")) {
    				builder.append("\n" + reader.nextString());
    			} else if(tagName.equals("HomeTeamName")) {
    				homeTeam.add(reader.nextString());
    			} else if(tagName.equals("AwayTeamName")) {
    				awayTeam.add(reader.nextString());
    			} else if(tagName.equals("BetContent")) {
    				String str = reader.nextString();
    				str = str.replace(",", "\n");
    				betContent.add(str);
    			} else if(tagName.equals("Result")) {
    				String str = reader.nextString();
    				str = str.replace(",", "\n");
    				caiguo.add(str);
    			}
    		}
    		changciList.add(builder.toString());
    		reader.endObject();
    	}
    	reader.endArray();
    }
	protected void _showProgress() {
		mProgressDialog = JinCaiZiProgressDialog.show(this, "正在获取信息");
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
	
	private void _drawTable() {
		TextView changci = new TextView(this);
		changci.setText(touzhuInfo.toString());
		changci.setPadding(10, 5, 10, 5);
		changci.setGravity(Gravity.CENTER);
		changci.setTextColor(this.getResources().getColor(R.color.black));
		mLvMatch.addView(changci);
		
		LinearLayout lv = new LinearLayout(this);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		lv.setLayoutParams(lp);
		lv.setOrientation(LinearLayout.HORIZONTAL);
		TextView tv = new TextView(this);
		tv.setText("场次");
		tv.setPadding(10, 5, 10, 5);
		tv.setGravity(Gravity.CENTER);
		tv.setWidth(80);
		tv.setTextColor(this.getResources().getColor(R.color.tc_default));
		tv.setBackgroundResource(R.drawable.fangxing_bg);
		lv.addView(tv, new LayoutParams(0, LayoutParams.MATCH_PARENT,1));
		TextView tv1 = new TextView(this);
		tv1.setText("主队vs客队");
		tv1.setPadding(10, 5, 10, 5);
		tv1.setGravity(Gravity.CENTER);
		tv1.setTextColor(this.getResources().getColor(R.color.tc_default));
		tv1.setBackgroundResource(R.drawable.fangxing_bg);
		lv.addView(tv1, new LayoutParams(0, LayoutParams.MATCH_PARENT,2));
		TextView tv2 = new TextView(this);
		tv2.setText("投注内容");
		tv2.setWidth(80);
		tv2.setPadding(10, 5, 10, 5);
		tv2.setGravity(Gravity.CENTER);
		tv2.setTextColor(this.getResources().getColor(R.color.tc_default));
		tv2.setBackgroundResource(R.drawable.fangxing_bg);
		lv.addView(tv2, new LayoutParams(0, LayoutParams.MATCH_PARENT,2));
		TextView tv3 = new TextView(this);
		tv3.setText("彩果");
		tv3.setPadding(10, 5, 10, 5);
		tv3.setGravity(Gravity.CENTER);
		tv3.setTextColor(this.getResources().getColor(R.color.tc_default));
		tv3.setBackgroundResource(R.drawable.fangxing_bg);
		lv.addView(tv3, new LayoutParams(0, LayoutParams.MATCH_PARENT,1));
		mLvMatch.addView(lv);
		for(int i=0; i<homeTeam.size(); i++) {
			LinearLayout matchlv = new LinearLayout(this);
			LinearLayout.LayoutParams matchlp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			lv.setLayoutParams(matchlp);
			lv.setOrientation(LinearLayout.HORIZONTAL);
			TextView matchtv = new TextView(this);
			matchtv.setText(changciList.get(i));
			matchtv.setWidth(80);
			matchtv.setPadding(10, 5, 10, 5);
			matchtv.setGravity(Gravity.CENTER);
			matchtv.setTextColor(this.getResources().getColor(R.color.tc_default));
			matchtv.setBackgroundResource(R.drawable.fangxing_bg);
			matchlv.addView(matchtv, new LayoutParams(0, LayoutParams.MATCH_PARENT,1));
			TextView matchtv1 = new TextView(this);
			matchtv1.setText(homeTeam.get(i)+"\nvs\n" + awayTeam.get(i));
			matchtv1.setPadding(10, 5, 10, 5);
			matchtv1.setGravity(Gravity.CENTER);
			matchtv1.setTextColor(this.getResources().getColor(R.color.tc_default));
			matchtv1.setBackgroundResource(R.drawable.fangxing_bg);
			matchlv.addView(matchtv1, new LayoutParams(0, LayoutParams.MATCH_PARENT,2));
			TextView matchtv2 = new TextView(this);
			matchtv2.setText(betContent.get(i));
			matchtv2.setWidth(80);
			matchtv2.setPadding(10, 5, 10, 5);
			matchtv2.setGravity(Gravity.CENTER);
			matchtv2.setTextColor(this.getResources().getColor(R.color.red));
			matchtv2.setBackgroundResource(R.drawable.fangxing_bg);
			matchlv.addView(matchtv2, new LayoutParams(0, LayoutParams.MATCH_PARENT,2));
			TextView matchtv3 = new TextView(this);
			matchtv3.setText(caiguo.get(i));
			matchtv3.setPadding(10, 5, 10, 5);
			matchtv3.setGravity(Gravity.CENTER);
			matchtv3.setTextColor(this.getResources().getColor(R.color.tc_default));
			matchtv3.setBackgroundResource(R.drawable.fangxing_bg);
			matchlv.addView(matchtv3, new LayoutParams(0, LayoutParams.MATCH_PARENT,1));
			mLvMatch.addView(matchlv);
		}
		
		
	}
}
