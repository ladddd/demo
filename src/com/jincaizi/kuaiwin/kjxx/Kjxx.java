package com.jincaizi.kuaiwin.kjxx;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.http.Header;

import com.google.gson.stream.JsonReader;
import com.jincaizi.adapters.KjxxAdapter;
import com.jincaizi.bean.KjxxRecordEntity;
import com.jincaizi.http.JinCaiZiHttpClient;
import com.jincaizi.kuaiwin.FragmentCallbacks;
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

public class Kjxx extends Fragment implements OnRefreshListener, FragmentCallbacks{
	public static final String TAG = "Kjxx";
	private PullToRefreshListView mKjxxListView;
	private LinkedList<KjxxRecordEntity>mList = new LinkedList<KjxxRecordEntity>();
	private KjxxAdapter adapter;
	private RequestHandle myRequestHandle;
	private JinCaiZiProgressDialog mProgressDialog;
	private KjxxRecordEntity mSFCR9Entity;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		_showProgress();
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		_requestData(false);
		//_fakeData();
		adapter = new KjxxAdapter(getActivity(), mList, false);
		mKjxxListView.setAdapter(adapter);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		_findViews(view);
		_setListener();
		super.onViewCreated(view, savedInstanceState);
	}

	private void _findViews(View view) {
		mKjxxListView = (PullToRefreshListView) view.findViewById(R.id.kjxx_list);
		TextView tv = (TextView) view.findViewById(R.id.empty_mylotterylistview);
		mKjxxListView.setEmptyView(tv);
		tv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				_showProgress();
				_requestData(true);
				
			}
		});
	}

	private void _setListener() {
		mKjxxListView.setOnRefreshListener(this);
		mKjxxListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				String lotteryType = mList.get(arg2 - 1).getLotteryType();
					Intent hi = new Intent(getActivity(), ShuZiCaiHistoryKJ.class);
					hi.putExtra("lotterytype", lotteryType);
					hi.putExtra("lotteryname", mList.get(arg2 - 1).getLotteryName());
					startActivity(hi);

			}
		});
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.main_reward_num, container, false);
	}
	private void _requestData(final boolean isClearList) {
		RequestParams params = new RequestParams();
	    params.add("act", "kaijiang");
		params.add("datatype", "json");
		params.add("jsoncallback",
				"jsonp" + String.valueOf(System.currentTimeMillis()));
		params.add("_", String.valueOf(System.currentTimeMillis()));
		String url = AsyncHttpClient.getUrlWithQueryString(false,
				JinCaiZiHttpClient.BASE_URL, params);
		myRequestHandle = JinCaiZiHttpClient.post(getActivity(), url,
				new AsyncHttpResponseHandler() {
					

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							byte[] responseBody) {
						try {
							String charset;
							if(Utils.isCmwapNet(getActivity())) {
								charset = "utf-8";
							} else {
								charset = "gb2312";
							}
							String jsonData = new String(responseBody, charset);
							Log.d(TAG, "开奖信息 detail = " + jsonData);
							_jsonReadKJObject(jsonData, isClearList);
                           
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
							_hideProgress();
							mKjxxListView.onRefreshComplete();
						} catch (Exception e) {
							e.printStackTrace();
							_hideProgress();
							mKjxxListView.onRefreshComplete();
						} finally {
							// FIXME reader.close should be here
							//test
							//_hideProgress();
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							byte[] responseBody, Throwable error) {
						Log.d(TAG, "failure = " + error.toString());
						_hideProgress();
						mKjxxListView.onRefreshComplete();
						Toast.makeText(getActivity(), "更新失败", Toast.LENGTH_SHORT).show();
					}
				});
	}
	private void _jsonReadKJObject(String jsonData, final boolean isClearList) throws IOException {
		final JsonReader reader = new JsonReader(new StringReader(jsonData));
		SafeAsyncTask<LinkedList<KjxxRecordEntity>> getAllDaigouTask = new SafeAsyncTask<LinkedList<KjxxRecordEntity>>() {
			@Override
			public LinkedList<KjxxRecordEntity> call() throws Exception {
				int returnSize = 0;
				int returnResult = -2;
				LinkedList<KjxxRecordEntity>list = new LinkedList<KjxxRecordEntity>();
				reader.beginObject();
				while (reader.hasNext()) {
					String tagName = reader.nextName();
					if (tagName.equals("result")) {
						returnResult = reader.nextInt();
						Log.d(TAG, "result--->" + returnResult);

					} else if (tagName.equals("size")) {
						returnSize = reader.nextInt();
						Log.d(TAG, "size--->" + returnSize);

					} else if (tagName.equals("zhdetail")) {
						list.addAll(_jsonReadDataArray(reader));
                        
					}
				}
				reader.endObject();
				return list;
			}

			@Override
			protected void onSuccess(LinkedList<KjxxRecordEntity> list) throws Exception {
				// TODO Auto-generated method stub
				super.onSuccess(list);
				if(list == null || list.size() == 0) {
					return;
				}
				if(isClearList) {
					mList.clear();
				}
				mList.addAll(list);
				if(mSFCR9Entity != null) {
                	mList.add(mSFCR9Entity);
                }
				adapter.notifyDataSetChanged();
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
				mKjxxListView.onRefreshComplete();
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

	private LinkedList<KjxxRecordEntity> _jsonReadDataArray(JsonReader reader) throws IOException {
        LinkedList<KjxxRecordEntity>entityList = new LinkedList<KjxxRecordEntity>();
		reader.beginArray();
		while (reader.hasNext()) {
			entityList.add(_jsonReadDataObject(reader));
		}
		reader.endArray();
		return entityList;
	}

	private KjxxRecordEntity _jsonReadDataObject(JsonReader reader) throws IOException {
		KjxxRecordEntity kjEntity = new KjxxRecordEntity();
		reader.beginObject();
       
		while (reader.hasNext()) {
			String tagName = reader.nextName();
			if (tagName.equals("LotteryType")) {
				kjEntity.setLotteryType(reader.nextString());
			} else if (tagName.equals("LotteryDes")) {
				kjEntity.setLotteryName(reader.nextString());
			} else if(tagName.equals("Qihao")) {
				kjEntity.setQiHao(reader.nextString());
			}else if(tagName.equals("Haoma")) {
				kjEntity.setKjResult(reader.nextString());
			}else if(tagName.equals("JiangChi")) {
				reader.nextString();
			}else if(tagName.equals("DayTime")) {
				kjEntity.setTime(reader.nextString());
			}
		}
		reader.endObject();
		if(kjEntity.getLotteryType().equals("SFC")) {
			mSFCR9Entity = new KjxxRecordEntity();
			mSFCR9Entity.setLotteryType("SFCR9");
			mSFCR9Entity.setLotteryName("任选9场");
			mSFCR9Entity.setQiHao(kjEntity.getQiHao());
			mSFCR9Entity.setKjResult(kjEntity.getKjResult());
			mSFCR9Entity.setTime(kjEntity.getTime());
		}
		return kjEntity;
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
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		_requestData(true);
	}
	@Override
	public void onShow() {
		// TODO Auto-generated method stub
		
	}
}
