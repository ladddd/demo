package com.jincaizi.kuaiwin.mylottery;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.http.Header;

import com.google.gson.stream.JsonReader;
import com.jincaizi.bean.ZuihaoRecordEntity;
import com.jincaizi.http.JinCaiZiHttpClient;
import com.jincaizi.R;
import com.jincaizi.kuaiwin.utils.Constants;
import com.jincaizi.kuaiwin.utils.SafeAsyncTask;
import com.jincaizi.kuaiwin.utils.Utils;
import com.jincaizi.vendor.http.AsyncHttpClient;
import com.jincaizi.vendor.http.AsyncHttpResponseHandler;
import com.jincaizi.vendor.http.RequestParams;

public class ZuiHaoDetailActivity extends Activity implements OnClickListener{
	public static final String TAG = "ZuiHaoDetailActivity";

	private ZuihaoRecordEntity mEntity;
	private String userID;
	private ImageView mBackBtn;
	private TextView mCheckFangList;
	private TextView mCheckXiangList;

	private TextView zhuihaoOp;

	private TextView zhuihaoStatus;
	private Activity mActivity;

	private String mUpk;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zuihao_detail_layout);
		mActivity = this;
		_initData(getIntent());
		_findViews();
		_setListener();
	}
	private void _findViews() {
		mBackBtn = (ImageView) findViewById(R.id.touzhu_leftmenu);
		TextView mTitle = (TextView) findViewById(R.id.current_lottery);
		mTitle.setText("追号详情");
		findViewById(R.id.right_divider).setVisibility(View.GONE);
		findViewById(R.id.sumbit_group_buy).setVisibility(View.GONE);

		TextView betStartTime = (TextView)findViewById(R.id.zuihao_time_content);
		betStartTime.setText(mEntity.getTime());
		TextView jineSum = (TextView)findViewById(R.id.zuihao_jine_content);
		jineSum.setText(mEntity.getMoneyAmount());
		TextView qishuSum = (TextView)findViewById(R.id.zhuihao_zongqishu_content);
		qishuSum.setText(mEntity.getQihaoAmount());
		TextView qishuCurrent = (TextView)findViewById(R.id.yizhui_qishu_content);
		qishuCurrent.setText(mEntity.getQihaoCurrent());
		TextView hasBetAmount = (TextView)findViewById(R.id.yizui_jine_content);
		hasBetAmount.setText(mEntity.getHasBetAmount());
		TextView autoStop = (TextView)findViewById(R.id.zhuihao_shifouStop_content);
		autoStop.setText(mEntity.getAutoStop());
		
		TextView zhuihaoUser = (TextView)findViewById(R.id.zuihao_user_content);
		SharedPreferences sp = getSharedPreferences("loginStatus", Context.MODE_PRIVATE);
		mUpk = sp.getString("upk", "");
		zhuihaoUser.setText(sp.getString("username", ""));
		mCheckFangList = (TextView)findViewById(R.id.zhuihao_fangan_content);
		mCheckXiangList = (TextView)findViewById(R.id.zhuihao_xiangqing_content);
		
		zhuihaoStatus = (TextView)findViewById(R.id.zongzhuihao_status);
		zhuihaoStatus.setText(mEntity.getStatus());
		zhuihaoOp = (TextView)findViewById(R.id.btn_zongzhuihao_op);
		if(!mEntity.getStatus().equals("进行中"))  {
			zhuihaoOp.setVisibility(View.GONE);
		}
	}
	private void _setListener() {
		mBackBtn.setOnClickListener(this);
		mCheckFangList.setOnClickListener(this);
		mCheckXiangList.setOnClickListener(this);
		zhuihaoOp.setOnClickListener(this);
	}
	private void _initData(Intent intent) {
		mEntity = (ZuihaoRecordEntity) intent
				.getSerializableExtra(Constants.ENTITY);
		userID = intent.getStringExtra(Constants.USERID);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()) {
		case R.id.btn_zongzhuihao_op:
			_doCancelZhuiHao();
			break;
		case R.id.touzhu_leftmenu:
			finish();
			break;
		case R.id.zhuihao_fangan_content:
			Intent fangIntent = new Intent(this, ZhuiHaoFangAList.class);
			fangIntent.putExtra(Constants.ENTITY, mEntity);
			fangIntent.putExtra(Constants.USERID, userID);
			startActivity(fangIntent);
			break;
		case R.id.zhuihao_xiangqing_content:
			Intent xiangIntent = new Intent(this, ZhuiHaoXiangqingList.class);
			xiangIntent.putExtra(Constants.ENTITY, mEntity);
			xiangIntent.putExtra(Constants.USERID, userID);
			startActivity(xiangIntent);
			break;
			default:
				break;
		}
	}
	private void _doCancelZhuiHao() {
		RequestParams params = new RequestParams();
		params.add("act", "userstopzh");
		params.add("datatype", "json");
		params.add("stopflag", "1");
		params.add("ZhuihaoId", mEntity.getId());
		params.add("userid",userID);
		params.add("upk", mUpk);
		params.add("jsoncallback",
				"jsonp" + String.valueOf(System.currentTimeMillis()));
		params.add("_", String.valueOf(System.currentTimeMillis()));
		String url = AsyncHttpClient.getUrlWithQueryString(false,
				JinCaiZiHttpClient.BASE_URL, params);
		JinCaiZiHttpClient.post(this, url, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
				try {
					// Object jsonResponse = parseResponse(responseBody);
					String charset;
					if(Utils.isCmwapNet(ZuiHaoDetailActivity.this)) {
						charset = "utf-8";
					} else {
						charset = "gb2312";
					}
					String jsonData = new String(responseBody, charset);
					Log.d(TAG, "success 取消追号 = " + jsonData);
					
					// 如果需要解析JSON数据，首要要生成一个JsonReader对象
					
					_jsonReadObject(jsonData);
					
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					//FIXME reader.close should be here
				}	
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				Log.d(TAG, "failure = " + error.toString());
				Toast.makeText(mActivity, "取消追号失败， 请重试", Toast.LENGTH_SHORT).show();
			}
		});
	}
	private void _jsonReadObject(String jsonData) throws IOException {
		final JsonReader reader = new JsonReader(new StringReader(
				jsonData));
        SafeAsyncTask<Integer> getAllDaigouTask = new SafeAsyncTask<Integer>() {
            @Override
            public Integer call() throws Exception {
        		String returnMsg = "";
        		int returnResult = -2;
        		reader.beginObject();
        		while (reader.hasNext()) {
        			String tagName = reader.nextName();
        			if (tagName.equals("msg")) {
        				returnMsg = reader.nextString();
        				Log.d(TAG, "result--->" + returnMsg);
        				
        			} else if (tagName.equals("result")) {
        				returnResult= reader.nextInt();
        				Log.d(TAG, "size--->" + returnResult);
        				
        			} else if (tagName.equals("data")) {
        				
        			}
        		}
        		reader.endObject();
                return returnResult;
            }

			@Override
			protected void onSuccess(Integer result) throws Exception {
				// TODO Auto-generated method stub
				super.onSuccess(result);
				if(result == 0) {
					Toast.makeText(getApplicationContext(), "取消追号成功", Toast.LENGTH_SHORT).show();
					zhuihaoStatus.setText("已全部取消");
					zhuihaoOp.setVisibility(View.GONE);
				} else {
					Toast.makeText(getApplicationContext(), "取消追号失败", Toast.LENGTH_SHORT).show();
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
}
