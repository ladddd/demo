package com.jincaizi.kuaiwin.hemaicenter;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
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
import com.jincaizi.adapters.HemaiListAdapter;
import com.jincaizi.bean.HemaiCenterRecordEntity;
import com.jincaizi.http.JinCaiZiHttpClient;
import com.jincaizi.R;
import com.jincaizi.kuaiwin.utils.Constants.LotteryType;
import com.jincaizi.kuaiwin.utils.SafeAsyncTask;
import com.jincaizi.kuaiwin.utils.Utils;
import com.jincaizi.kuaiwin.widget.JinCaiZiProgressDialog;
import com.jincaizi.kuaiwin.widget.PullToRefreshListView;
import com.jincaizi.kuaiwin.widget.PullToRefreshListView.OnRefreshListener;
import com.jincaizi.vendor.http.AsyncHttpClient;
import com.jincaizi.vendor.http.AsyncHttpResponseHandler;
import com.jincaizi.vendor.http.RequestHandle;
import com.jincaizi.vendor.http.RequestParams;
/**
 * 按进度排序的合买清单
 * @author yj
 *
 */
public class HemaiJinduFragment extends Fragment implements OnRefreshListener{
	public static final String TAG = "HemaiJinduFragment";
	private PullToRefreshListView mListView;
	 private final LinkedList<HemaiCenterRecordEntity> mList = new LinkedList<HemaiCenterRecordEntity>();
	private HemaiListAdapter adapter;
	private HemaiCenter mParentFragment;
	private RequestHandle myRequestHandle;
	private static final String PAGESIZE = "10";
	private int pageIndex = 1;
	private JinCaiZiProgressDialog mProgressDialog;
	private String mLotteryType="";
	private String mSortFlag = "asc";
	private String mSortId = "Progress";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        mParentFragment = (HemaiCenter)getParentFragment();
        mLotteryType = LotteryType.getLotteryTypeString(mParentFragment.mLotteryType);
        mSortFlag = mParentFragment.sortFlag;
        mSortId = mParentFragment.sortId;
        _showProgress();
        _requestData(true, mLotteryType);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        return inflater.inflate(R.layout.hemai_child_pulllistview_layout, container, false);
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
		//_fakeData();
		View mFootView = LayoutInflater.from(getActivity()).inflate(R.layout.pulltorefreshlistview_footer, null);
        mListView.addFooterView(mFootView, null, true);
        mFootView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				pageIndex++;
				 _showProgress();
				 _requestData(false, mLotteryType);
			}
		});
        mListView.setOnRefreshListener(this);
		adapter = new HemaiListAdapter(getActivity(), mList);
		adapter.setmLotteryType(mParentFragment.mLotteryType);
		mListView.setAdapter(adapter);
	}

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        _findViews(view);
        //_setListeners();
        super.onViewCreated(view, savedInstanceState);

    }
    private void _findViews(View view) {
    	mListView = (PullToRefreshListView)view.findViewById(R.id.hemai_pulltorefresh_list);
    	TextView tv = (TextView) view.findViewById(R.id.empty_mylotterylistview);
    	mListView.setEmptyView(tv);
    	tv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pageIndex = 1;
				_showProgress();
				_requestData(true, mLotteryType);
			}
		});
    	mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(mParentFragment.getActivity(), HemaiDetailActivity.class);
				intent.putExtra("hemaientity", mList.get(arg2 - 1));
				intent.putExtra("lotterykind", "shuzicai");
				startActivity(intent);
			}
		});
    	
    }

	private void _requestData(final boolean isClearList, String lotteryType) {
		RequestParams params = new RequestParams();
	    params.add("act", "hemai");
	    params.add("pageIndex", String.valueOf(pageIndex));
	    params.add("pageSize", PAGESIZE);
	    params.add("sortId", mSortId);
	    params.add("sortflag", mSortFlag);
		params.add("datatype", "json");
		if(!TextUtils.isEmpty(lotteryType)) {
			params.add("lotterytype", lotteryType);
		}
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
							Log.d(TAG, "合买中心 detail = " + jsonData);
							_jsonReadKJObject(jsonData, isClearList);
                           
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
							_hideProgress();
							mListView.onRefreshComplete();
						} catch (Exception e) {
							e.printStackTrace();
							_hideProgress();
							mListView.onRefreshComplete();
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
						mListView.onRefreshComplete();
						Toast.makeText(getActivity(), "更新失败", Toast.LENGTH_SHORT).show();
					}
				});
	}
	private void _jsonReadKJObject(String jsonData, final boolean isClearList) throws IOException {
		final JsonReader reader = new JsonReader(new StringReader(jsonData));
		SafeAsyncTask<LinkedList<HemaiCenterRecordEntity>> getAllDaigouTask = new SafeAsyncTask<LinkedList<HemaiCenterRecordEntity>>() {
			@Override
			public LinkedList<HemaiCenterRecordEntity> call() throws Exception {
				int returnSize = 0;
				int returnResult = -2;
				LinkedList<HemaiCenterRecordEntity>list = new LinkedList<HemaiCenterRecordEntity>();
				reader.beginObject();
				while (reader.hasNext()) {
					String tagName = reader.nextName();
					if (tagName.equals("result")) {
						returnResult = reader.nextInt();
						Log.d(TAG, "result--->" + returnResult);

					} else if (tagName.equals("size")) {
						returnSize = reader.nextInt();
						Log.d(TAG, "size--->" + returnSize);

					} else if (tagName.equals("detail")) {
						list.addAll(_jsonReadDataArray(reader));
                        
					}
				}
				reader.endObject();
				return list;
			}

			@Override
			protected void onSuccess(LinkedList<HemaiCenterRecordEntity> t) throws Exception {
				// TODO Auto-generated method stub
				super.onSuccess(t);
				if(t == null || t.size() == 0) {
					return;
				}
				if(isClearList) {
					mList.clear();
				}
				mList.addAll(t);
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
				_hideProgress();
				mListView.onRefreshComplete();
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

	private LinkedList<HemaiCenterRecordEntity> _jsonReadDataArray(JsonReader reader) throws IOException {
        LinkedList<HemaiCenterRecordEntity>entityList = new LinkedList<HemaiCenterRecordEntity>();
		reader.beginArray();
		while (reader.hasNext()) {
			entityList.add(_jsonReadDataObject(reader));
		}
		reader.endArray();
		return entityList;
	}

	private HemaiCenterRecordEntity _jsonReadDataObject(JsonReader reader) throws IOException {
		HemaiCenterRecordEntity kjEntity = new HemaiCenterRecordEntity();
		reader.beginObject();
        Double totalShare = 1.0;
        Double insureCount = 0.0;
        String totolAmount = "0";
        String hadByAmount = "0";
        String perAmount = "1";
		while (reader.hasNext()) {
			String tagName = reader.nextName();
			if (tagName.equals("HemaiId")) {
				kjEntity.setHemaiId(reader.nextString());
			} else if(tagName.equals("UserId")) {
				reader.nextString();
			} else if(tagName.equals("UserName")) {
				kjEntity.setBetHost(reader.nextString());
			} else if(tagName.equals("LotteryType")) {
				kjEntity.setLotteryType(reader.nextString());
			} else if(tagName.equals("TotalAmount")) {
				totolAmount = reader.nextString();
				kjEntity.setBetAmount(totolAmount);
			} else if(tagName.equals("HadByAmount")) {
				hadByAmount = reader.nextString();
			} else if(tagName.equals("PerAmount")) {
				perAmount = reader.nextString();
				kjEntity.setBetAverage(perAmount);
			} else if(tagName.equals("BrokerageAmount")) {
				reader.nextString();
			} else if(tagName.equals("TotalShare")) {
				totalShare = Double.valueOf(reader.nextString());
			} else if(tagName.equals("InsureCount")) {
				insureCount = Double.valueOf(reader.nextString());
			} else if(tagName.equals("Progress")) {
				kjEntity.setBetJindu(reader.nextString());
			}
		}
		double baodi = (insureCount*1.0/totalShare*1.0)*100.0;
		float left = (Float.valueOf(totolAmount)-Float.valueOf(hadByAmount))/Float.valueOf(perAmount);
		kjEntity.setBetBaodi(String.valueOf((int)baodi));
		kjEntity.setBetLeft(String.valueOf((int)left));
		reader.endObject();
		
		return kjEntity;
	}
	@Override
	public void onHiddenChanged(boolean hidden) {
		// TODO Auto-generated method stub
		super.onHiddenChanged(hidden);
		if(!hidden) {
			pageIndex = 1;
			String temp = LotteryType.getLotteryTypeString(mParentFragment.mLotteryType);
			adapter.setmLotteryType(mParentFragment.mLotteryType);
	        mSortFlag = mParentFragment.sortFlag;
	        mSortId = mParentFragment.sortId;
			if(!temp.equals(mLotteryType)) {
				mLotteryType = temp;
				_requestData(true, mLotteryType);
			}
		}
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
		pageIndex = 1;
		//_showProgress();
		_requestData(true, mLotteryType);	
	}
	
}
