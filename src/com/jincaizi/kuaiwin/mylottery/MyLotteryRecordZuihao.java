package com.jincaizi.kuaiwin.mylottery;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.http.Header;

import com.google.gson.stream.JsonReader;
import com.jincaizi.adapters.MyZuihaoRecordAdapter;
import com.jincaizi.bean.ZuihaoRecordEntity;
import com.jincaizi.http.JinCaiZiHttpClient;
import com.jincaizi.R;
import com.jincaizi.kuaiwin.utils.Constants;
import com.jincaizi.kuaiwin.utils.SafeAsyncTask;
import com.jincaizi.kuaiwin.utils.Utils;
import com.jincaizi.kuaiwin.widget.JinCaiZiProgressDialog;
import com.jincaizi.kuaiwin.widget.PullToRefreshListView;
import com.jincaizi.kuaiwin.widget.PullToRefreshListView.OnRefreshListener;
import com.jincaizi.vendor.http.AsyncHttpClient;
import com.jincaizi.vendor.http.AsyncHttpResponseHandler;
import com.jincaizi.vendor.http.RequestHandle;
import com.jincaizi.vendor.http.RequestParams;

public class MyLotteryRecordZuihao extends Activity implements OnClickListener, OnRefreshListener{
    private static final String TAG = "MyLotteryRecordZuihao";
    private static final String PAGESIZE = "10";
	private RadioButton mylotteryGoing;
	private RadioButton mylotteryDone;
	private RadioButton mylotteryStop;
	private PullToRefreshListView mylotteryListView;
	private LinkedList<ZuihaoRecordEntity> myList  = new LinkedList<ZuihaoRecordEntity>();
	private LinkedList<ZuihaoRecordEntity>goingList  = new LinkedList<ZuihaoRecordEntity>();
	private LinkedList<ZuihaoRecordEntity>doneList  = new LinkedList<ZuihaoRecordEntity>();
	private LinkedList<ZuihaoRecordEntity>stopList  = new LinkedList<ZuihaoRecordEntity>();
	private MyZuihaoRecordAdapter mMyAdapter;
	private SharedPreferences sp;
	private int userid = 0;
	private String mUpk;
	private ImageView leftMenu;
	private int pageindex = 1;
	private Activity mActivity;
	private RequestHandle myRequestHandle;
	private JinCaiZiProgressDialog mProgressDialog;
	private View mFootView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mylottery_record_layout);
		mActivity = this;
		sp = getSharedPreferences("loginStatus", Context.MODE_PRIVATE);
		userid = sp.getInt("userid", 0);
		mUpk = sp.getString("upk", "");
		_findViews();
		_setListener();
        mFootView = LayoutInflater.from(mActivity).inflate(R.layout.pulltorefreshlistview_footer, null);
        mylotteryListView.addFooterView(mFootView, null, true);
        mFootView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				 _showProgress();
				if(mylotteryStop.isChecked()) {
					_getMyRecordStop(false);
					
				} else if(mylotteryGoing.isChecked()) {
					_getMyRecordGoing(false);
				} else if(mylotteryDone.isChecked()) {
					_getMyRecordDone(false);
				}
			}
		});
        _getMyRecordGoing(true);
		mMyAdapter = new MyZuihaoRecordAdapter(this, myList);
		TextView tv = (TextView) findViewById(R.id.empty_mylotterylistview);
		mylotteryListView.setEmptyView(tv);
		tv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				 _showProgress();
					pageindex = 1;
					if(mylotteryStop.isChecked()) {
						myList.clear();
						myList.addAll(stopList);
						mMyAdapter.notifyDataSetChanged();
						_getMyRecordStop(true);
						
					} else if(mylotteryDone.isChecked()) {
						myList.clear();
						myList.addAll(doneList);
						mMyAdapter.notifyDataSetChanged();
						_getMyRecordDone(true);
					} else if(mylotteryGoing.isChecked()) {
						myList.clear();
						myList.addAll(goingList);
						mMyAdapter.notifyDataSetChanged();
						_getMyRecordGoing(true);
					}
			}
		});
		mylotteryListView.setAdapter(mMyAdapter);
		 _showProgress();
	}
	private void _findViews() {
		mylotteryGoing = (RadioButton)findViewById(R.id.mylottery_record_undone);
		mylotteryGoing.setText("进行中");
		mylotteryDone = (RadioButton)findViewById(R.id.mylottery_record_recent);
		mylotteryDone.setText("已完成");
		mylotteryStop = (RadioButton)findViewById(R.id.mylottery_record_all);
		mylotteryStop.setText("已终止");
		mylotteryListView = (PullToRefreshListView)findViewById(R.id.mylottery_record_listView);
		TextView title = (TextView)findViewById(R.id.current_lottery);
		  title.setText("我的追号记录");
		 findViewById(R.id.right_divider).setVisibility(View.GONE);
		 findViewById(R.id.sumbit_group_buy).setVisibility(View.GONE);
		 leftMenu = (ImageView) findViewById(R.id.touzhu_leftmenu);
		 
		  
	}
	private void _setListener() {
		mylotteryGoing.setOnClickListener(this);
		mylotteryDone.setOnClickListener(this);
		mylotteryStop.setOnClickListener(this);
		leftMenu.setOnClickListener(this);
		mylotteryListView.setOnRefreshListener(this);
		mylotteryListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent detailIntent = new Intent(mActivity, ZuiHaoDetailActivity.class);
				detailIntent.putExtra(Constants.ENTITY, myList.get(arg2-1));
				detailIntent.putExtra(Constants.USERID, String.valueOf(userid));
				startActivity(detailIntent);
			}
		});
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()) {
		case R.id.touzhu_leftmenu:
			finish();
			break;
		case R.id.mylottery_record_undone:
			 _showProgress();
			pageindex = 1;
			myList.clear();
			myList.addAll(goingList);
			mMyAdapter.notifyDataSetChanged();
			_getMyRecordGoing(true);
			mylotteryGoing.setChecked(true);
			mylotteryDone.setChecked(false);
			mylotteryStop.setChecked(false);
			break;
		case R.id.mylottery_record_recent:
			 _showProgress();
			pageindex = 1;
			myList.clear();
			myList.addAll(doneList);
			mMyAdapter.notifyDataSetChanged();
			_getMyRecordDone(true);
			
			mylotteryGoing.setChecked(false);
			mylotteryDone.setChecked(true);
			mylotteryStop.setChecked(false);
			break;
		case R.id.mylottery_record_all:
			 _showProgress();
			pageindex = 1;
			myList.clear();
			myList.addAll(stopList);
			mMyAdapter.notifyDataSetChanged();
			_getMyRecordStop(true);
			mylotteryGoing.setChecked(false);
			mylotteryDone.setChecked(false);
			mylotteryStop.setChecked(true);
			break;
			default:
				break;
		}
	}
	private void _getMyRecordGoing(final boolean isClearList) {
		RequestParams params = new RequestParams();
		params.add("act", "userzhuihao");
		params.add("datatype", "json");
		params.add("zhuiStatus", "0");
		params.add("pageindex", String.valueOf(pageindex));
		params.add("pagesize", PAGESIZE);
		params.add("userid", String.valueOf(userid));
		params.add("upk", mUpk);
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
					if(Utils.isCmwapNet(MyLotteryRecordZuihao.this)) {
						charset = "utf-8";
					} else {
						charset = "gb2312";
					}
					String jsonData = new String(responseBody, charset);
					Log.d(TAG, "success going = " + jsonData);
					
					// 如果需要解析JSON数据，首要要生成一个JsonReader对象
					
					_jsonReadDaigouObject(jsonData, isClearList, goingList);
					pageindex++;
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					_hideProgress();
					mylotteryListView.onRefreshComplete();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					_hideProgress();
					mylotteryListView.onRefreshComplete();
				} finally {
					//FIXME reader.close should be here
				}	
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				Log.d(TAG, "failure = " + error.toString());
				_hideProgress();
				mylotteryListView.onRefreshComplete();
				Toast.makeText(mActivity, "更新失败", Toast.LENGTH_SHORT).show();
			}
		});
	}
	private void _getMyRecordDone(final boolean isClearList) {
		RequestParams params = new RequestParams();
		params.add("act", "userzhuihao");
		params.add("datatype", "json");
		params.add("zhuiStatus", "1");
		params.add("pageindex", String.valueOf(pageindex));
		params.add("pagesize", PAGESIZE);
		params.add("userid", String.valueOf(userid));
		params.add("upk", mUpk);
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
					if(Utils.isCmwapNet(MyLotteryRecordZuihao.this)) {
						charset = "utf-8";
					} else {
						charset = "gb2312";
					}
					String jsonData = new String(responseBody, charset);
					Log.d(TAG, "success done = " + jsonData);
					
					// 如果需要解析JSON数据，首要要生成一个JsonReader对象
					
					_jsonReadDaigouObject(jsonData, isClearList, doneList);
					pageindex++;
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					_hideProgress();
					mylotteryListView.onRefreshComplete();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					_hideProgress();
					mylotteryListView.onRefreshComplete();
				} finally {
					//FIXME reader.close should be here
				}	
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				Log.d(TAG, "failure = " + error.toString());
				_hideProgress();
				mylotteryListView.onRefreshComplete();
				Toast.makeText(mActivity, "更新失败", Toast.LENGTH_SHORT).show();
			}
		});
	}
	private void _getMyRecordStop(final boolean isClearList) {
		RequestParams params = new RequestParams();
		params.add("act", "userzhuihao");
		params.add("datatype", "json");
		params.add("zhuiStatus", "2");
		params.add("pageindex", String.valueOf(pageindex));
		params.add("pagesize", PAGESIZE);
		params.add("userid", String.valueOf(userid));
		params.add("upk", mUpk);
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
					if(Utils.isCmwapNet(MyLotteryRecordZuihao.this)) {
						charset = "utf-8";
					} else {
						charset = "gb2312";
					}
					String jsonData = new String(responseBody, charset);
					Log.d(TAG, "success stop = " + jsonData);
					
					// 如果需要解析JSON数据，首要要生成一个JsonReader对象
					
					_jsonReadDaigouObject(jsonData, isClearList, stopList);
					pageindex++;
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					_hideProgress();
					mylotteryListView.onRefreshComplete();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					_hideProgress();
					mylotteryListView.onRefreshComplete();
				} finally {
					//FIXME reader.close should be here
				}	
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				Log.d(TAG, "failure = " + error.toString());
				_hideProgress();
				mylotteryListView.onRefreshComplete();
				Toast.makeText(mActivity, "更新失败", Toast.LENGTH_SHORT).show();
			}
		});
	}
	private void _jsonReadDaigouObject(String jsonData, final boolean isClearList, final LinkedList<ZuihaoRecordEntity>paramList) throws IOException {
		final JsonReader reader = new JsonReader(new StringReader(
				jsonData));
        SafeAsyncTask<Void> getAllDaigouTask = new SafeAsyncTask<Void>() {
            private int httpResponseResult = -2;
			private String httpResponseMsg = "更新失败";

			@Override
            public Void call() throws Exception {
        		int returnSize = 0;
        		httpResponseResult = -2;
        		reader.beginObject();
        		while (reader.hasNext()) {
        			String tagName = reader.nextName();
        			if (tagName.equals("result")) {
        				httpResponseResult = reader.nextInt();
        				Log.d(TAG, "result--->" + httpResponseResult);
        				
        			} else if (tagName.equals("size")) {
        				returnSize= reader.nextInt();
        				Log.d(TAG, "size--->" + returnSize);
        				
        			} else if (tagName.equals("data")) {
        					if(isClearList) {
        						paramList.clear();
        					}
        				
        					paramList.addAll(paramList.size(), _jsonReadDataArray(reader)); 
        				
        			}else if(tagName.equals("msg")) {
        				httpResponseMsg = reader.nextString();
        			}
        		}
        		reader.endObject();
                return null;
            }

			@Override
			protected void onSuccess(Void t) throws Exception {
				// TODO Auto-generated method stub
				super.onSuccess(t);
				myList.clear();
				myList.addAll(paramList);
				mMyAdapter.notifyDataSetChanged();
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
				mylotteryListView.onRefreshComplete();
				try {
					reader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(httpResponseResult == -2) {
					Toast.makeText(mActivity, httpResponseMsg , Toast.LENGTH_SHORT).show();
				}
			}

       
        };
        getAllDaigouTask.execute();
	}
	private LinkedList<ZuihaoRecordEntity> _jsonReadDataArray(JsonReader reader) throws IOException {
		LinkedList<ZuihaoRecordEntity> entityList = new LinkedList<ZuihaoRecordEntity>();
		reader.beginArray();
		while(reader.hasNext()) {
			entityList.add(_jsonReadDataObject(reader));
		}
		reader.endArray();
		return entityList;
	}
    private ZuihaoRecordEntity _jsonReadDataObject(JsonReader reader) throws IOException {
    	ZuihaoRecordEntity entity = new ZuihaoRecordEntity();
    	reader.beginObject();
    	while(reader.hasNext()) {
    		String tagName = reader.nextName();	
    		if(tagName.equals("id")) {
    			Log.d(TAG, "第" + reader.nextInt() + "项");
    		} else if(tagName.equals("ZhuihaoId")) {
    			entity.setId(String.valueOf(reader.nextInt()));
    		} else if(tagName.equals("UserName")) {
    			Log.d(TAG, "username = " + reader.nextString());
    		} else if(tagName.equals("LotteryType")) {
    			entity.setType(reader.nextString());
    		} else if(tagName.equals("BetCount")) {
    			reader.nextInt();
    		} else if(tagName.equals("TotalAmount")) {
    			entity.setMoneyAmount(String.valueOf(reader.nextDouble()));
    		} else if(tagName.equals("CreateTime")) {
    			entity.setTime(reader.nextString()) ;
    		} else if(tagName.equals("ZhuihaoStatus")) {
    			entity.setStatus(reader.nextString());
    		} else if(tagName.equals("WinStatus")) {
    			reader.nextString();
    		} else if(tagName.equals("OriginalBonus")) {
    			Log.d(TAG, "originalBonus = " + reader.nextDouble());
    		} else if(tagName.equals("FinalBonus")) {
    			reader.nextDouble();
    		} else if(tagName.equals("PlusBonus")) {
    			Log.d(TAG, "plusBonus = " + reader.nextDouble());
    		} else if(tagName.equals("QihaoCount")) {
    			entity.setQihaoAmount(String.valueOf(reader.nextInt()));
    		} else if(tagName.equals("QihaoNum")) {
    			entity.setQihaoCurrent(String.valueOf(reader.nextInt()));
    		} else if(tagName.equals("HasBetAmount")) {
    			entity.setHasBetAmount(String.valueOf(reader.nextDouble()));
    		} else if(tagName.equals("AutoStop")) {
    			entity.setAutoStop(reader.nextString());
    		}
    	}
    	reader.endObject();
    	return entity;
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
	public void onRefresh() {
		// TODO Auto-generated method stub
		 //_showProgress();
		pageindex = 1;
		if(mylotteryStop.isChecked()) {
			myList.clear();
			myList.addAll(stopList);
			mMyAdapter.notifyDataSetChanged();
			_getMyRecordStop(true);
			
		} else if(mylotteryDone.isChecked()) {
			myList.clear();
			myList.addAll(doneList);
			mMyAdapter.notifyDataSetChanged();
			_getMyRecordDone(true);
		} else if(mylotteryGoing.isChecked()) {
			myList.clear();
			myList.addAll(goingList);
			mMyAdapter.notifyDataSetChanged();
			_getMyRecordGoing(true);
		}
	}
}
