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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.http.Header;

import com.google.gson.stream.JsonReader;
import com.jincaizi.adapters.ZhuihaoDetailAdapter;
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

public class ZhuiHaoXiangqingList extends Activity implements OnClickListener{
	public static final String TAG = "ZhuiHaoXiangqingList";
	public static final String QIHAO = "qihao";
	public static final String WINSTATUS = "windStatus";
	public static final String ZHUIHAOSTATUS = "zhuihaoStatus";
	public static final String ZHUIHAOQIHAOID= "zhuhaoQihaoId";
	private static final String PAGESIZE = "10";
	private int pageindex = 1;
	private ZhuihaoDetailAdapter mAdapter;
	private LinkedList<Map<String,String>>mList = new LinkedList<Map<String,String>>();
	private ListView mListView;
	private JinCaiZiProgressDialog mProgressDialog;
	private ZuihaoRecordEntity mEntity;
	private String userID;
	private RequestHandle myRequestHandle;
	private ImageView mBackBtn;
	private Activity mActivity;
	private String mUpk;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zhuihao_xiangqing_layout);
		SharedPreferences sp = getSharedPreferences("loginStatus", Context.MODE_PRIVATE);
		mUpk = sp.getString("upk", "");
		mActivity = this;
		pageindex = 1;
		_initData(getIntent());
		_showProgress();
		_findViews();
		_requestData();
	}
	private void _initData(Intent intent) {
		mEntity = (ZuihaoRecordEntity) intent
				.getSerializableExtra(Constants.ENTITY);
		userID = intent.getStringExtra(Constants.USERID);
	}
   private void _findViews() {
		mBackBtn = (ImageView) findViewById(R.id.touzhu_leftmenu);
		mBackBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		TextView mTitle = (TextView) findViewById(R.id.current_lottery);
		mTitle.setText("追号详情表");
		findViewById(R.id.right_divider).setVisibility(View.GONE);
		findViewById(R.id.sumbit_group_buy).setVisibility(View.GONE);
		mListView = (ListView)findViewById(R.id.zhuihao_detail_list);
		TextView emptyView = (TextView)findViewById(R.id.empty_mylotterylistview);
    	mListView.setEmptyView(emptyView);
    	emptyView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mList.clear();
				_showProgress();
				 _requestData();
				
			}
		});
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
		mAdapter = new ZhuihaoDetailAdapter(this, mList, mEntity, userID, mUpk);
		mListView.setAdapter(mAdapter);
   }
   private void _requestData() {
		RequestParams params = new RequestParams();
	    params.add("act", "userzhqihao");
		params.add("ZhuihaoId", mEntity.getId());
		params.add("datatype", "json");
		params.add("userid", userID);
		params.add("upk", mUpk);
		params.add("pageindex", String.valueOf(pageindex));
		params.add("pagesize", PAGESIZE);
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
							if(Utils.isCmwapNet(ZhuiHaoXiangqingList.this)) {
								charset = "utf-8";
							} else {
								charset = "gb2312";
							}
							String jsonData = new String(responseBody, charset);
							Log.d(TAG, "success zhuihao detail = " + jsonData);
							_jsonReadDaigouObject(jsonData);
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
	
	private void _jsonReadDaigouObject(String jsonData) throws IOException {
		final JsonReader reader = new JsonReader(new StringReader(jsonData));
		SafeAsyncTask<LinkedList<Map<String,String>>> getAllDaigouTask = new SafeAsyncTask<LinkedList<Map<String,String>>>() {
			@Override
			public LinkedList<Map<String,String>> call() throws Exception {
				int returnSize = 0;
				int returnResult = -2;
				LinkedList<Map<String,String>>list = new LinkedList<Map<String,String>>();
				reader.beginObject();
				while (reader.hasNext()) {
					String tagName = reader.nextName();
					if (tagName.equals("result")) {
						returnResult = reader.nextInt();
						Log.d(TAG, "result--->" + returnResult);

					} else if (tagName.equals("detailsize")) {
						returnSize = reader.nextInt();
						Log.d(TAG, "size--->" + returnSize);

					}  else if(tagName.equals("qhdetail")) {
						list.addAll(_jsonReadDataArray(reader));
					}
				}
				reader.endObject();
				return list;
			}

			@Override
			protected void onSuccess(LinkedList<Map<String,String>> t) throws Exception {
				// TODO Auto-generated method stub
				super.onSuccess(t);
				if(t == null || t.size() == 0) {
					return;
				}
				mList.addAll(t);
				mAdapter.notifyDataSetChanged();
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

	private LinkedList<Map<String,String>> _jsonReadDataArray(JsonReader reader) throws IOException {
		LinkedList<Map<String,String>>list = new LinkedList<Map<String,String>>();
		reader.beginArray();
		while (reader.hasNext()) {
			list.add(_jsonReadDataObject(reader));
		}
		reader.endArray();
		return list;
	}

	private Map<String, String> _jsonReadDataObject(JsonReader reader) throws IOException {
		reader.beginObject();
       Map<String, String> map = new HashMap<String, String>();
		while (reader.hasNext()) {
			String tagName = reader.nextName();
			if (tagName.equals("id")) {
				reader.nextInt();
			} else if (tagName.equals("Multiple")) {
				reader.nextInt();
			}  else if(tagName.equals("BetAmount")) {
				reader.nextDouble();
			} else if(tagName.equals("Qihao")) {
				map.put(QIHAO, reader.nextString());
			} else if(tagName.equals("ZhuihaoStatus")) {
				map.put(ZHUIHAOSTATUS, reader.nextString());
			} else if(tagName.equals("WinStatus")) {
				map.put(WINSTATUS, reader.nextString());
			} else if(tagName.equals("ZhuihaoQihaoId")) {
				map.put(ZHUIHAOQIHAOID, reader.nextString());
			}
		}
		reader.endObject();
		    //mList.add(map);
		return map;
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
