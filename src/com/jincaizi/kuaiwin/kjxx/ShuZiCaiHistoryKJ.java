package com.jincaizi.kuaiwin.kjxx;


import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.LinkedList;

import org.apache.http.Header;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.stream.JsonReader;
import com.jincaizi.R;
import com.jincaizi.adapters.KjxxAdapter;
import com.jincaizi.bean.KjxxRecordEntity;
import com.jincaizi.http.JinCaiZiHttpClient;
import com.jincaizi.kuaiwin.utils.SafeAsyncTask;
import com.jincaizi.kuaiwin.utils.Utils;
import com.jincaizi.kuaiwin.widget.JinCaiZiProgressDialog;
import com.jincaizi.kuaiwin.widget.datepicker.ScreenInfo;
import com.jincaizi.kuaiwin.widget.datepicker.WheelMain;
import com.jincaizi.vendor.http.AsyncHttpClient;
import com.jincaizi.vendor.http.AsyncHttpResponseHandler;
import com.jincaizi.vendor.http.RequestHandle;
import com.jincaizi.vendor.http.RequestParams;

public class ShuZiCaiHistoryKJ extends Activity implements OnClickListener{
	private static final String TAG = "ShuZiCaiHistoryKJ";
	private static final String PAGESIZE = "10";
	private int pageIndex = 1;
	private LinkedList<KjxxRecordEntity>mList = new LinkedList<KjxxRecordEntity>();
	private ListView mListView;
	private ImageView mBackBtn;
	private KjxxAdapter adapter;
	private RequestHandle myRequestHandle;
	private Activity mActivity;
	private JinCaiZiProgressDialog mProgressDialog;
	private String mLotteryType;
	private String mLotteryName;
	private ImageView mDataPicker;
	private WheelMain wheelMain;
	private String mQihao="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shuzicaikj_history_layout);
		_showProgress();
		this.mActivity = this;
		_initData(getIntent());
		_findViews();
		_setListener();
		if (mLotteryType.equals("SSQ") || mLotteryType.equals("DLT")
				|| mLotteryType.equals("PL5") || mLotteryType.equals("QXC")
				|| mLotteryType.equals("QLC") || mLotteryType.equals("FC3D")
				|| mLotteryType.equals("PL3")) {
			View mFootView = LayoutInflater.from(this).inflate(
					R.layout.pulltorefreshlistview_footer, null);
			mListView.addFooterView(mFootView, null, true);
			mFootView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					pageIndex++;
					_showProgress();
					_requestData(false, mQihao);
				}
			});
		} else {
			findViewById(R.id.right_divider).setVisibility(View.VISIBLE);
			mDataPicker.setVisibility(View.VISIBLE);
			mDataPicker.setOnClickListener(this);
		}
		adapter = new KjxxAdapter(this, mList, true);
		adapter.setmLotteryType(mLotteryType);
		mListView.setAdapter(adapter);
		_requestData(false, mQihao);
	}
	private void _initData(Intent intent) {
		mLotteryType = intent.getStringExtra("lotterytype");
		mLotteryName = intent.getStringExtra("lotteryname");
	}
	   private void _findViews() {
		   mListView = (ListView) findViewById(R.id.shuzicaikj_history_list);
		   TextView tv = (TextView) findViewById(R.id.empty_mylotterylistview);
		   mListView.setEmptyView(tv);
		   tv.setOnClickListener(this);
			mBackBtn = (ImageView) findViewById(R.id.touzhu_leftmenu);
			TextView mTitle = (TextView) findViewById(R.id.current_lottery);
			mTitle.setText(mLotteryName);
			mDataPicker = (ImageView)findViewById(R.id.dataPicker);
			
	   }

	private void _setListener() {
		mBackBtn.setOnClickListener(this);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if (mLotteryType.equals("SSQ") || mLotteryType.equals("DLT")
						|| mLotteryType.equals("PL5")
						|| mLotteryType.equals("QXC")
						|| mLotteryType.equals("QLC")
						|| mLotteryType.equals("FC3D")
						|| mLotteryType.equals("PL3")) {
					Intent hi = new Intent(ShuZiCaiHistoryKJ.this,
							QiCiDetailActivity.class);
					hi.putExtra("lotterytype", mLotteryType);
					hi.putExtra("lotteryname", mLotteryName);
					hi.putExtra("qihao", mList.get(arg2).getQiHao());
					hi.putExtra("time", mList.get(arg2).getTime());
					hi.putExtra("content", mList.get(arg2).getKjResult());
					startActivity(hi);
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()) {
		case R.id.dataPicker:
			_showDateDialog();
			break;
		case R.id.empty_mylotterylistview:
			pageIndex = 1;
			_showProgress();
			_requestData(true, mQihao);
			break;
		case R.id.touzhu_leftmenu:
			finish();
			break;
			default:
				break;
		}
	}
	private void _requestData(final boolean isClearList, String qihao) {
		RequestParams params = new RequestParams();
	    params.add("act", "kjlist");
	    params.add("lotterytype", mLotteryType);
	    params.add("pageIndex", String.valueOf(pageIndex));
	    params.add("pageSize", PAGESIZE);
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
							if(Utils.isCmwapNet(ShuZiCaiHistoryKJ.this)) {
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

					} else if (tagName.equals("detail")) {

						list.addAll(_jsonReadDataArray(reader));
                        
					}
				}
				reader.endObject();
				return list;
			}

			@Override
			protected void onSuccess(LinkedList<KjxxRecordEntity> t) throws Exception {
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
		
		return kjEntity;
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
	
	private void _showDateDialog() {
		LayoutInflater inflater=LayoutInflater.from(ShuZiCaiHistoryKJ.this);
		final View timepickerview=inflater.inflate(R.layout.timepicker, null);
		ScreenInfo screenInfo = new ScreenInfo(ShuZiCaiHistoryKJ.this);
		wheelMain = new WheelMain(timepickerview);
		wheelMain.screenheight = screenInfo.getHeight();
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		wheelMain.initDateTimePicker(year,month,day);
		final Dialog myDialog = new Dialog(this, R.style.Theme_dialog);
		myDialog.setCanceledOnTouchOutside(true);
		TextView dialogCancel = (TextView) timepickerview.findViewById(R.id.date_cancel);
		dialogCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				myDialog.dismiss();
			}
		});
		TextView dialogOK = (TextView) timepickerview.findViewById(R.id.date_ok);
		dialogOK.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Toast.makeText(JincaiZLhistory.this, "您选择了 "+ wheelMain.getTime(), Toast.LENGTH_SHORT).show();
				_showProgress();
				mQihao = wheelMain.getYear()+ "-" +  wheelMain.getMonth() + "-"+ wheelMain.getDay();
				_requestData(true, mQihao);
				myDialog.dismiss();
			}
		});
		myDialog.setContentView(timepickerview);
		Window dialogWindow = myDialog.getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		lp.width = 400; // 宽度
		dialogWindow.setAttributes(lp);
		myDialog.show();
	}
}
