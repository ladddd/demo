package com.jincaizi.kuaiwin.mylottery;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.http.Header;

import com.google.gson.stream.JsonReader;
import com.jincaizi.adapters.LotteryDetailExpandableListAdapter;
import com.jincaizi.bean.ZuihaoRecordEntity;
import com.jincaizi.http.JinCaiZiHttpClient;
import com.jincaizi.R;
import com.jincaizi.kuaiwin.utils.Constants;
import com.jincaizi.kuaiwin.utils.SafeAsyncTask;
import com.jincaizi.kuaiwin.utils.Utils;
import com.jincaizi.kuaiwin.widget.JinCaiZiProgressDialog;
import com.jincaizi.vendor.http.AsyncHttpClient;
import com.jincaizi.vendor.http.AsyncHttpResponseHandler;
import com.jincaizi.vendor.http.RequestHandle;
import com.jincaizi.vendor.http.RequestParams;

public class ZhuiHaoFangAList extends Activity implements OnClickListener{
	private static final String TAG = "ZhuiHaoFangAList";
	private static final String PAGESIZE = "10";
	private int pageindex = 1;
	private LinkedList<Map<String, String>> mParentList = new LinkedList<Map<String, String>>();
	private LinkedList<String[]> mChildList = new LinkedList<String[]>();
	private JinCaiZiProgressDialog mProgressDialog;
	private ExpandableListView mListView;
	private LotteryDetailExpandableListAdapter mMydapter;
	private RequestHandle myRequestHandle;
	private ZuihaoRecordEntity mEntity;
	private String userID;
	private ImageView mBackBtn;
	private Activity mActivity;
	private String mUpk;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lottery_selfchoice_layout);
		SharedPreferences sp = getSharedPreferences("loginStatus", Context.MODE_PRIVATE);
		mUpk = sp.getString("upk", "");
		mActivity = this;
		pageindex = 1;
		_initData(getIntent());
		_findViews();
		_setListener();
        View mFootView = LayoutInflater.from(this).inflate(R.layout.pulltorefreshlistview_footer, null);
        mListView.addFooterView(mFootView, null, true);
        mFootView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 _showProgress();
				 _requestData();
			}
		});
		mMydapter = new LotteryDetailExpandableListAdapter(
				getApplicationContext(), mParentList, mChildList);
		mListView.setGroupIndicator(this.getResources().getDrawable(
				R.drawable.expandable_icon_selector));
		mListView.setAdapter(mMydapter);
		 _showProgress();
		 _requestData();
	}
   private void _findViews() {
	   mListView = (ExpandableListView) findViewById(R.id.mylottery_detail_list);
	   TextView emptyView = (TextView)findViewById(R.id.empty_mylotterylistview);
   	mListView.setEmptyView(emptyView);
   	emptyView.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			mParentList.clear();
			mChildList.clear();
			_showProgress();
			 _requestData();
			
		}
	});
		mBackBtn = (ImageView) findViewById(R.id.touzhu_leftmenu);
		TextView mTitle = (TextView) findViewById(R.id.current_lottery);
		mTitle.setText("方案内容表");
		findViewById(R.id.right_divider).setVisibility(View.GONE);
		findViewById(R.id.sumbit_group_buy).setVisibility(View.GONE);
   }
   private void _setListener() {
	   mBackBtn.setOnClickListener(this);
   }
	private void _initData(Intent intent) {
		mEntity = (ZuihaoRecordEntity) intent
				.getSerializableExtra(Constants.ENTITY);
		userID = intent.getStringExtra(Constants.USERID);
	}
	private void _requestData() {
		RequestParams params = new RequestParams();
			params.add("act", "userzhdetail");
			params.add("ZhuihaoId", mEntity.getId());

		params.add("datatype", "json");
		params.add("pageindex", String.valueOf(pageindex));
		params.add("pagesize", PAGESIZE);
		params.add("userid", userID);
		params.add("upk", mUpk);
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
							// Object jsonResponse =
							// parseResponse(responseBody);
							String charset;
							if(Utils.isCmwapNet(ZhuiHaoFangAList.this)) {
								charset = "utf-8";
							} else {
								charset = "gb2312";
							}
							String jsonData = new String(responseBody, charset);
							Log.d(TAG, "success detail = " + jsonData);
							_jsonReadDaigouObject(jsonData);

						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							_hideProgress();
						} catch (Exception e) {
							// TODO Auto-generated catch block
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

	private void _jsonReadDaigouObject(String jsonData) throws IOException {
		final JsonReader reader = new JsonReader(new StringReader(jsonData));
		SafeAsyncTask<Void> getAllDaigouTask = new SafeAsyncTask<Void>() {
			@Override
			public Void call() throws Exception {
				int returnSize = 0;
				int returnResult = -2;
				reader.beginObject();
				while (reader.hasNext()) {
					String tagName = reader.nextName();
					if (tagName.equals("result")) {
						returnResult = reader.nextInt();
						Log.d(TAG, "result--->" + returnResult);

					} else if (tagName.equals("detailsize")) {
						returnSize = reader.nextInt();
						Log.d(TAG, "size--->" + returnSize);

					} else if (tagName.equals("zhdetail")) {
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
				mMydapter.notifyDataSetChanged();
				pageindex++;
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
			_jsonReadDataObject(reader);
		}
		reader.endArray();
	}

	private void _jsonReadDataObject(JsonReader reader) throws IOException {
		StringBuilder builder = new StringBuilder("(");
		String title = "明细";
		reader.beginObject();
       
		while (reader.hasNext()) {
			String tagName = reader.nextName();
			if (tagName.equals("id")) {
				reader.nextInt();
			} else if (tagName.equals("DetailId")) {
				reader.nextInt();
			} else if (tagName.equals("BetCount")) {
				builder.append(String.valueOf(reader.nextInt()) + "注");
			} else if (tagName.equals("BetAmount")) {
				double money = reader.nextDouble();
				
					builder.append(" / " + String.valueOf(money) + "元");
			} else if (tagName.equals("BetContent")) {
				String str = reader.nextString();
				String[] strArray = str.split(";");
				mChildList.add(strArray);
			} else if (tagName.equals("BetType")) {
				builder.append(reader.nextString() + " / " );
			}
		}
		reader.endObject();
		builder.append(")");
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("title", title + String.valueOf(mParentList.size() + 1));
		map.put("extra", builder.toString());
		mParentList.add(map);
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
}
