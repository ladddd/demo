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
import com.jincaizi.adapters.MyAccountCzAdapter;
import com.jincaizi.bean.AccountCzRecordEntity;
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

public class MyAccountChongzhiFragment extends Fragment implements OnRefreshListener{
	public static final String TAG = "MyAccountChongzhiFragment";
	private static final String PAGESIZE = "10";
	private int pageindex = 1;
	private MyAccountCzAdapter mMyAdapter;
	private LinkedList<AccountCzRecordEntity> myList  = new LinkedList<AccountCzRecordEntity>();
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
        sp = getActivity().getSharedPreferences("loginStatus", Context.MODE_PRIVATE);
		userid = sp.getInt("userid", 0);
		mUpk = sp.getString("upk", "");
		mActivity = getActivity();
		_showProgress();
		_getAccountChongzhi(false);
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
				_getAccountChongzhi(false);
				
			}
		});
		mMyAdapter = new MyAccountCzAdapter(getActivity(), myList);
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
				// TODO Auto-generated method stub
				pageindex = 1;
				_showProgress();
				_getAccountChongzhi(true);
			}
		});
    	mListView.setOnRefreshListener(this);
    	
    }

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		pageindex = 1;
		mMyAdapter.notifyDataSetChanged();
		_getAccountChongzhi(true);
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

	private void _getAccountChongzhi(final boolean isClearList) {
		RequestParams params = new RequestParams();
		params.add("act", "czdetail");
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
					
					_jsonReadChongzhiObject(jsonData, isClearList);
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
	private void _jsonReadChongzhiObject(String jsonData, final boolean isClearList) throws IOException {
		final JsonReader reader = new JsonReader(new StringReader(
				jsonData));
        SafeAsyncTask<LinkedList<AccountCzRecordEntity>> getAllDaigouTask = new SafeAsyncTask<LinkedList<AccountCzRecordEntity>>() {
            private String httpResponseMsg = "更新失败";
			private int returnResult = -2;

			@Override
            public LinkedList<AccountCzRecordEntity> call() throws Exception {
        		int returnSize = 0;
        		returnResult  = -2;
        		LinkedList<AccountCzRecordEntity> list  = new LinkedList<AccountCzRecordEntity>();
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
			protected void onSuccess(LinkedList<AccountCzRecordEntity> t) throws Exception {
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
	private LinkedList<AccountCzRecordEntity> _jsonReadDataArray(JsonReader reader) throws IOException {
		LinkedList<AccountCzRecordEntity> entityList = new LinkedList<AccountCzRecordEntity>();
		reader.beginArray();
		while(reader.hasNext()) {
				entityList.add(_jsonReadDetailObject(reader));
			
		}
		reader.endArray();
		return entityList;
	}
    private AccountCzRecordEntity _jsonReadDetailObject(JsonReader reader) throws IOException {
    	AccountCzRecordEntity entity = new AccountCzRecordEntity();
    	reader.beginObject();
    	while(reader.hasNext()) {
    		String tagName = reader.nextName();	
    		if(tagName.equals("id")) {
    			Log.d(TAG, "第" + reader.nextInt() + "项");
    		} else if(tagName.equals("OrderId")) {
    			entity.setCzId(reader.nextString());
    		} else if(tagName.equals("PayAmount")) {
    			entity.setCzJine(String.valueOf(reader.nextDouble()));
    		} else if(tagName.equals("PayType")) {
    			entity.setCzType(reader.nextString());
    		} else if(tagName.equals("Status")) {
    			entity.setCzStatus(reader.nextString());
    		} else if(tagName.equals("AddTime")) {
    			entity.setCzTime(reader.nextString());
    		}
    	
    	}
    	reader.endObject();
    	return entity;
    } 
}
