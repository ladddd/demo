package com.jincaizi.adapters;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.Map;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.http.Header;

import com.google.gson.stream.JsonReader;
import com.jincaizi.bean.ZuihaoRecordEntity;
import com.jincaizi.http.JinCaiZiHttpClient;
import com.jincaizi.R;
import com.jincaizi.kuaiwin.mylottery.ZhuiHaoXiangqingList;
import com.jincaizi.kuaiwin.utils.SafeAsyncTask;
import com.jincaizi.kuaiwin.utils.Utils;
import com.jincaizi.vendor.http.AsyncHttpClient;
import com.jincaizi.vendor.http.AsyncHttpResponseHandler;
import com.jincaizi.vendor.http.RequestHandle;
import com.jincaizi.vendor.http.RequestParams;

/**
 * 追号详情适配器
 * 
 * @author yj
 */
public class ZhuihaoDetailAdapter extends BaseAdapter implements
		OnClickListener {
	private static final String TAG = "ZhuihaoDetailAdapter";
	private final Context mContext;
	private LinkedList<Map<String, String>> mList;
	@SuppressWarnings("unused")
	private RequestHandle myRequestHandle;
	private ZuihaoRecordEntity mEntity;
	private String userID;
	private String mUpk;

	public ZhuihaoDetailAdapter(Context context,
			LinkedList<Map<String, String>> list, ZuihaoRecordEntity entity, String userid, String upk) {
		this.mContext = context;
		this.mList = list;
		this.mEntity = entity;
		this.userID = userid;
		this.mUpk = upk;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final int location = position;
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.zhuihao_detail_item, null, false);
			holder.tv_qihao = (TextView) convertView
					.findViewById(R.id.zhuihao_detail_qihao);
			holder.tv_zhuistatus = (TextView) convertView
					.findViewById(R.id.zhuihao_status);
			holder.btn_quxiao = (Button) convertView
					.findViewById(R.id.zhuihao_detail_quxiao);
			holder.tv_winstatus = (TextView)convertView.findViewById(R.id.kaijiang_status);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv_qihao.setText(mList.get(position).get(
				ZhuiHaoXiangqingList.QIHAO));
		String status = mList.get(position).get(ZhuiHaoXiangqingList.ZHUIHAOSTATUS);
		if (!TextUtils.isEmpty(status) && status.equals("已完成")) {
			holder.btn_quxiao.setVisibility(View.GONE);
			holder.tv_zhuistatus.setVisibility(View.VISIBLE);
		} else if (!TextUtils.isEmpty(status) && status.equals("进行中")) {
			holder.btn_quxiao.setVisibility(View.VISIBLE);
			holder.tv_zhuistatus.setVisibility(View.GONE);
		} else {// 已终止或title
			holder.btn_quxiao.setVisibility(View.GONE);
			holder.tv_zhuistatus.setVisibility(View.VISIBLE);
		}
		holder.tv_zhuistatus.setText(status);
		final TextView quxiaoView = holder.btn_quxiao;
		final TextView statusView = holder.tv_zhuistatus;
		holder.btn_quxiao.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				_doCancelZhuiHao(mList.get(location).get(ZhuiHaoXiangqingList.ZHUIHAOQIHAOID), quxiaoView, statusView);
			}
		});
		if(position%2 == 0) {
			convertView.setBackgroundResource(R.color.half_orange);
		} else {
			convertView.setBackgroundResource(R.color.white);
		}
		String winstatus = mList.get(position).get(ZhuiHaoXiangqingList.WINSTATUS);
		if(!TextUtils.isEmpty(winstatus)) {
		holder.tv_winstatus.setText(winstatus);
		} else {
			holder.tv_winstatus.setText("---");
		}
		return convertView;
	}

	private static class ViewHolder {
		TextView tv_qihao;
		TextView tv_zhuistatus;
		Button btn_quxiao;
		TextView tv_winstatus;

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.zhuihao_detail_qihao:
			
			break;
		default:
			break;
		}
	}
	private void _doCancelZhuiHao(String qihaoId, final TextView cancelZhuihaoView, final TextView zhuihaoStatusView) {
		RequestParams params = new RequestParams();
		params.add("act", "userstopzh");
		params.add("datatype", "json");
		params.add("stopflag", "0");
		params.add("qihaoId", qihaoId);
		params.add("ZhuihaoId", mEntity.getId());
		params.add("userid",userID);
		params.add("upk", mUpk);
		params.add("jsoncallback",
				"jsonp" + String.valueOf(System.currentTimeMillis()));
		params.add("_", String.valueOf(System.currentTimeMillis()));
		String url = AsyncHttpClient.getUrlWithQueryString(false,
				JinCaiZiHttpClient.BASE_URL, params);
		Log.d(TAG, "params = " + params.toString() );
		myRequestHandle = JinCaiZiHttpClient.post(mContext, url, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
				try {
					// Object jsonResponse = parseResponse(responseBody);
					String charset;
					if(Utils.isCmwapNet(mContext)) {
						charset = "utf-8";
					} else {
						charset = "gb2312";
					}
					String jsonData = new String(responseBody, charset);
					Log.d(TAG, "success 取消追号 = " + jsonData);
					
					// 如果需要解析JSON数据，首要要生成一个JsonReader对象
					
					_jsonReadObject(jsonData, cancelZhuihaoView, zhuihaoStatusView);
					
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					Toast.makeText(mContext, "数据可能异常，请检查数据请求是否成功，以免重复请求造成损失", Toast.LENGTH_LONG).show();
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Toast.makeText(mContext, "数据可能异常，请检查数据请求是否成功，以免重复请求造成损失", Toast.LENGTH_LONG).show();
					e.printStackTrace();
				} finally {
					//FIXME reader.close should be here
				}	
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				Log.d(TAG, "failure = " + error.toString());
				Toast.makeText(mContext, "取消追号失败，请重试", Toast.LENGTH_SHORT).show();
			}
		});
	}
	private void _jsonReadObject(String jsonData, final TextView cancelZhuihaoView, final TextView zhuihaoStatusView) throws IOException {
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
					Toast.makeText(mContext, "取消追号成功", Toast.LENGTH_SHORT).show();
					cancelZhuihaoView.setVisibility(View.GONE);
					zhuihaoStatusView.setVisibility(View.VISIBLE);
					zhuihaoStatusView.setText("已停止");
				} else {
					Toast.makeText(mContext, "取消追号失败", Toast.LENGTH_SHORT).show();
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
