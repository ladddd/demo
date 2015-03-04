package com.jincaizi.kuaiwin.mylottery;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.http.Header;

import com.google.gson.stream.JsonReader;
import com.jincaizi.adapters.MyAccountTkAdapter;
import com.jincaizi.bean.AccountTkRecordEntity;
import com.jincaizi.http.JinCaiZiHttpClient;
import com.jincaizi.R;
import com.jincaizi.kuaiwin.utils.SafeAsyncTask;
import com.jincaizi.kuaiwin.utils.Utils;
import com.jincaizi.kuaiwin.widget.JinCaiZiProgressDialog;
import com.jincaizi.kuaiwin.widget.PullToRefreshListView;
import com.jincaizi.kuaiwin.widget.PullToRefreshListView.OnRefreshListener;
import com.jincaizi.vendor.http.AsyncHttpClient;
import com.jincaizi.vendor.http.AsyncHttpResponseHandler;
import com.jincaizi.vendor.http.RequestHandle;
import com.jincaizi.vendor.http.RequestParams;

public class MyAccountTikuanFragment extends Fragment implements OnRefreshListener{
	public static final String TAG = "MyAccountTikuanFragment";
	private static final String PAGESIZE = "10";
	private int pageindex = 1;
	private MyAccountTkAdapter mMyAdapter;
	private LinkedList<AccountTkRecordEntity> myList  = new LinkedList<AccountTkRecordEntity>();
	private PullToRefreshListView mListView;
	private JinCaiZiProgressDialog mProgressDialog;
	private RequestHandle myRequestHandle;
	private int userid;
	private String mUpk;
	private SharedPreferences sp;
	private Activity mActivity;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        sp = getActivity().getSharedPreferences("loginStatus", Context.MODE_PRIVATE);
		userid = sp.getInt("userid", 0);
		mUpk = sp.getString("upk", "");
		_showProgress();
		_getAccountTikuan(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        return inflater.inflate(R.layout.accountdetail_childfragment, container, false);
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
        View mFootView = LayoutInflater.from(getActivity()).inflate(R.layout.pulltorefreshlistview_footer, null);
        mListView.addFooterView(mFootView, null, true);
        mFootView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				 _showProgress();
				 _getAccountTikuan(false);
				
			}
		});
		mMyAdapter = new MyAccountTkAdapter(getActivity(), myList);
		mListView.setAdapter(mMyAdapter);
	}

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        _findViews(view);
        //_setListeners();
        super.onViewCreated(view, savedInstanceState);

    }
    private void _findViews(View view) {
    	mListView = (PullToRefreshListView)view.findViewById(R.id.myaccount_listView);
    	TextView emptyView = (TextView)view.findViewById(R.id.empty_mylotterylistview);
    	mListView.setEmptyView(emptyView);
    	emptyView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				pageindex = 1;
				_showProgress();
				_getAccountTikuan(true);
			}
		});
    	mListView.setOnRefreshListener(this);
    	
    }

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		pageindex = 1;
		mMyAdapter.notifyDataSetChanged();
		_getAccountTikuan(true);
	}
	protected void _showProgress() {
		mProgressDialog = JinCaiZiProgressDialog.show(getActivity(), "正在更新......");
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

	private void _getAccountTikuan(final boolean isClearList) {
		RequestParams params = new RequestParams();
		params.add("act", "tkdetail");
		params.add("datatype", "json");
		params.add("pageIndex", String.valueOf(pageindex));
		params.add("pageSize", PAGESIZE);
		params.add("userid", String.valueOf(userid));
		params.add("upk", mUpk);
		params.add("jsoncallback",
				"jsonp" + String.valueOf(System.currentTimeMillis()));
		params.add("_", String.valueOf(System.currentTimeMillis()));
		String url = AsyncHttpClient.getUrlWithQueryString(false,
				JinCaiZiHttpClient.BASE_URL, params);
		myRequestHandle = JinCaiZiHttpClient.post(getActivity(), url, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
				try {
					// Object jsonResponse = parseResponse(responseBody);
					String charset;
					if(Utils.isCmwapNet(getActivity())) {
						charset = "utf-8";
					} else {
						charset = "gb2312";
					}
					String jsonData = new String(responseBody, charset);
					Log.d(TAG, "success done = " + jsonData);
					
					// 如果需要解析JSON数据，首要要生成一个JsonReader对象
					
					_jsonReadTikuanObject(jsonData, isClearList);
					pageindex++;
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					_hideProgress();
					mListView.onRefreshComplete();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					_hideProgress();
					mListView.onRefreshComplete();
				} finally {
					//FIXME reader.close should be here
				}	
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				Log.d(TAG, "failure = " + error.toString());
				_hideProgress();
				mListView.onRefreshComplete();
				Toast.makeText(mActivity, "更新失败", Toast.LENGTH_SHORT).show();
			}
		});
		
	}
	private void _jsonReadTikuanObject(String jsonData, final boolean isClearList) throws IOException {
		final JsonReader reader = new JsonReader(new StringReader(
				jsonData));
        SafeAsyncTask<LinkedList<AccountTkRecordEntity>> getAllDaigouTask = new SafeAsyncTask<LinkedList<AccountTkRecordEntity>>() {
            private int returnResult;
			private String httpResponseMsg = "更新失败";

			@Override
            public LinkedList<AccountTkRecordEntity> call() throws Exception {
        		int returnSize = 0;
        		returnResult = -2;
        		LinkedList<AccountTkRecordEntity> list  = new LinkedList<AccountTkRecordEntity>();
        		reader.beginObject();
        		while (reader.hasNext()) {
        			String tagName = reader.nextName();
        			if (tagName.equals("result")) {
        				returnResult = reader.nextInt();
        				Log.d(TAG, "result--->" + returnResult);
        				
        			} else if (tagName.equals("size")) {
        				returnSize= reader.nextInt();
        				Log.d(TAG, "size--->" + returnSize);
        				
        			} else if (tagName.equals("detail")) {
        				 
        					list.addAll( _jsonReadDataArray(reader)); 
        				
        			}else if(tagName.equals("msg")) {
        				httpResponseMsg  = reader.nextString();
        			}
        		}
        		reader.endObject();
                return list;
            }

			@Override
			protected void onSuccess(LinkedList<AccountTkRecordEntity> t) throws Exception {
				// TODO Auto-generated method stub
				super.onSuccess(t);
				if(t == null || t.size() == 0) {
					return;
				}
				if(isClearList) {
			    	myList.clear();
			    }
				myList.addAll(myList.size(), t); 
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
				mListView.onRefreshComplete();
				try {
					reader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(returnResult == -2) {
					Toast.makeText(mActivity, httpResponseMsg , Toast.LENGTH_SHORT).show();
				}
			}

       
        };
        getAllDaigouTask.execute();
	}
	private LinkedList<AccountTkRecordEntity> _jsonReadDataArray(JsonReader reader) throws IOException {
		LinkedList<AccountTkRecordEntity> entityList = new LinkedList<AccountTkRecordEntity>();
		reader.beginArray();
		while(reader.hasNext()) {
				entityList.add(_jsonReadDetailObject(reader));
			
		}
		reader.endArray();
		return entityList;
	}
    private AccountTkRecordEntity _jsonReadDetailObject(JsonReader reader) throws IOException {
    	AccountTkRecordEntity entity = new AccountTkRecordEntity();
    	reader.beginObject();
    	while(reader.hasNext()) {
    		String tagName = reader.nextName();	
    		if(tagName.equals("id")) {
    			Log.d(TAG, "第" + reader.nextInt() + "项");
    		} else if(tagName.equals("id")) {
    			reader.nextString();
    		} else if(tagName.equals("PayAmount")) {
    			entity.setTkJine(String.valueOf(reader.nextDouble()));
    		} else if(tagName.equals("Fee")) {
    			entity.setFee(String.valueOf(reader.nextDouble()));
    		} else if(tagName.equals("OpenSpace")) {
    			entity.setOpenSpace(reader.nextString());
    		} else if(tagName.equals("Bank")) {
    			entity.setBank(reader.nextString());
    		} else if(tagName.equals("BankCard")) {
    			entity.setBankCard(reader.nextString());
    		} else if(tagName.equals("AddTime")) {
    			entity.setTkTime(reader.nextString());
    		} else if(tagName.equals("Status")) {
    			entity.setTkStatus(reader.nextString());
    		} else if(tagName.equals("Memo")) {
    			reader.nextString();
    		}
    	
    	}
    	reader.endObject();
    	return entity;
    } 
}
