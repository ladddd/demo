package com.jincaizi.kuaiwin.buycenter.yilou;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Random;

import org.apache.http.Header;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.jincaizi.vendor.http.RequestParams;

public class SyxwYilou extends Activity implements OnClickListener{
	public static final String LOTTERYTYPE = "lotterytype";
	public static final String TITLE = "title";
	protected static final String TAG = "SyxwYilou";
    private Activity mActivity;
	private ImageView mBackBtn;
	private LinearLayout mLvYilou;
	private ArrayList<String>mNumber = new ArrayList<String>();
	private ArrayList<String>mCurYilou = new ArrayList<String>();
	private ArrayList<String>mPerYilou = new ArrayList<String>();
	private ArrayList<String>mMaxYilou = new ArrayList<String>();
	private String lotteryType;
	private String titleStr;
	private JinCaiZiProgressDialog mProgressDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.yilou_layout);
		this.mActivity = this;
		//_initData(getIntent());
		lotteryType = getIntent().getStringExtra(LOTTERYTYPE);
		titleStr = getIntent().getStringExtra(TITLE);
		_requestData();
		//_fakeData();
		_findViews();
		//_drawTable();
	}
	
//	private void _fakeData() {
//		Random random = new Random();
//		for(int i=0; i<11; i++) {
//			mNumber.add((i+1)+"");
//			mCurYilou.add(random.nextInt(10)+"");
//			mPerYilou.add(random.nextInt(10)+"");
//			mMaxYilou.add(random.nextInt(100)+"");
//		}
//	}
	private void _findViews() {
		mBackBtn = (ImageView) findViewById(R.id.touzhu_leftmenu);
		mBackBtn.setOnClickListener(this);
		TextView mTitle = (TextView) findViewById(R.id.current_lottery);
		mTitle.setText(titleStr + "-基本号码遗漏");
		findViewById(R.id.right_divider).setVisibility(View.GONE);
		findViewById(R.id.sumbit_group_buy).setVisibility(View.GONE);
		mLvYilou = (LinearLayout)findViewById(R.id.yilou_panel);
		
	}
	private void _drawTable() {
		LinearLayout lv = new LinearLayout(mActivity);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		lv.setLayoutParams(lp);
		lv.setOrientation(LinearLayout.HORIZONTAL);
		TextView tv = new TextView(mActivity);
		tv.setText("号码");
		tv.setPadding(10, 5, 10, 5);
		tv.setGravity(Gravity.CENTER);
		tv.setWidth(80);
		tv.setTextColor(mActivity.getResources().getColor(R.color.tc_default));
		tv.setBackgroundResource(R.drawable.fangxing_bg);
		lv.addView(tv, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
		TextView tv1 = new TextView(mActivity);
		tv1.setText("当前遗漏");
		tv1.setPadding(10, 5, 10, 5);
		tv1.setGravity(Gravity.CENTER);
		tv1.setTextColor(mActivity.getResources().getColor(R.color.tc_default));
		tv1.setBackgroundResource(R.drawable.fangxing_bg);
		lv.addView(tv1, new LayoutParams(0, LayoutParams.MATCH_PARENT,1));
		TextView tv2 = new TextView(mActivity);
		tv2.setText("平均遗漏");
		tv2.setWidth(80);
		tv2.setPadding(10, 5, 10, 5);
		tv2.setGravity(Gravity.CENTER);
		tv2.setTextColor(mActivity.getResources().getColor(R.color.tc_default));
		tv2.setBackgroundResource(R.drawable.fangxing_bg);
		lv.addView(tv2, new LayoutParams(0, LayoutParams.MATCH_PARENT,1));
		TextView tv3 = new TextView(mActivity);
		tv3.setText("最大遗漏");
		tv3.setPadding(10, 5, 10, 5);
		tv3.setGravity(Gravity.CENTER);
		tv3.setTextColor(mActivity.getResources().getColor(R.color.tc_default));
		tv3.setBackgroundResource(R.drawable.fangxing_bg);
		lv.addView(tv3, new LayoutParams(0, LayoutParams.MATCH_PARENT,1));
		mLvYilou.addView(lv);
		for(int i=0; i<mCurYilou.size(); i++) {
			LinearLayout matchlv = new LinearLayout(mActivity);
			LinearLayout.LayoutParams matchlp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			lv.setLayoutParams(matchlp);
			lv.setOrientation(LinearLayout.HORIZONTAL);
			TextView matchtv = new TextView(mActivity);
			matchtv.setText(mNumber.get(i));
			matchtv.setWidth(80);
			matchtv.setPadding(10, 5, 10, 5);
			matchtv.setGravity(Gravity.CENTER);
			matchtv.setTextColor(mActivity.getResources().getColor(R.color.tc_default));
			matchtv.setBackgroundResource(R.drawable.fangxing_bg);
			matchlv.addView(matchtv, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
			TextView matchtv1 = new TextView(mActivity);
			matchtv1.setText(mCurYilou.get(i));
			matchtv1.setPadding(10, 5, 10, 5);
			matchtv1.setGravity(Gravity.CENTER);
			matchtv1.setTextColor(mActivity.getResources().getColor(R.color.tc_default));
			matchtv1.setBackgroundResource(R.drawable.fangxing_bg);
			matchlv.addView(matchtv1, new LayoutParams(0, LayoutParams.MATCH_PARENT,1));
			TextView matchtv2 = new TextView(mActivity);
			matchtv2.setText(mPerYilou.get(i));
			matchtv2.setWidth(80);
			matchtv2.setPadding(10, 5, 10, 5);
			matchtv2.setGravity(Gravity.CENTER);
			matchtv2.setTextColor(mActivity.getResources().getColor(R.color.red));
			matchtv2.setBackgroundResource(R.drawable.fangxing_bg);
			matchlv.addView(matchtv2, new LayoutParams(0, LayoutParams.MATCH_PARENT,1));
			TextView matchtv3 = new TextView(mActivity);
			matchtv3.setText(mMaxYilou.get(i));
			matchtv3.setPadding(10, 5, 10, 5);
			matchtv3.setGravity(Gravity.CENTER);
			matchtv3.setTextColor(mActivity.getResources().getColor(R.color.tc_default));
			matchtv3.setBackgroundResource(R.drawable.fangxing_bg);
			matchlv.addView(matchtv3, new LayoutParams(0, LayoutParams.MATCH_PARENT,1));
			mLvYilou.addView(matchlv);
		}
		
		
	}
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.touzhu_leftmenu:
			finish();
			break;
			default:
				break;
		}
	}
	
	private void _requestData() {
		_showProgress();
		JinCaiZiHttpClient.closeExpireConnection();
		RequestParams params = new RequestParams();
	    params.add("act", "yilou");
	    params.add("lotterytype", lotteryType);
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
							if(Utils.isCmwapNet(SyxwYilou.this)) {
								charset = "utf-8";
							} else {
								charset = "gb2312";
							}
							String jsonData = new String(responseBody, charset);
							Log.d(TAG, "yilou detail = " + jsonData);
							_readyilouFromJson(jsonData);
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
							Toast.makeText(SyxwYilou.this, "遗漏数据获取失败", Toast.LENGTH_SHORT).show();
							_hideProgress();
						} catch (Exception e) {
							e.printStackTrace();
							Toast.makeText(SyxwYilou.this, "遗漏数据获取失败", Toast.LENGTH_SHORT).show();
							_hideProgress();
						} finally {
							// FIXME reader.close should be here
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							byte[] responseBody, Throwable error) {
						Log.d(TAG, "failure = " + error.toString());
						Toast.makeText(SyxwYilou.this, "遗漏数据获取失败", Toast.LENGTH_SHORT).show();
						_hideProgress();
					}
				});
	}
	private void _readyilouFromJson(String jsonData) {
		final JsonReader reader = new JsonReader(new StringReader(jsonData));
		SafeAsyncTask<Boolean> getAllDaigouTask = new SafeAsyncTask<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				reader.beginArray();
				while (reader.hasNext()) {
					reader.beginObject();    
					while (reader.hasNext()) {
						String tagName = reader.nextName();	
						if (tagName.equals("haoma")) {
							mNumber.add(reader.nextString());
						} else if(tagName.equals("nowyilou")) {
							mCurYilou.add(reader.nextString());
						} else if(tagName.equals("pjcount")) {
							mPerYilou.add(reader.nextString());
						} else if(tagName.equals("maxyilou")) {
							mMaxYilou.add(reader.nextString());
						} 
						
					}
					reader.endObject();
				}
				reader.endArray();
				return true;
			}

			@Override
			protected void onSuccess(Boolean t) throws Exception {
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
	public void _showProgress() {
		mProgressDialog = JinCaiZiProgressDialog.show(this, "正在更新......");
		
	}

	private void _hideProgress() {
		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
		}
	}
}
