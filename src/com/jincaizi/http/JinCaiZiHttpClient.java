package com.jincaizi.http;



import org.apache.http.Header;
import org.apache.http.HttpEntity;

import android.content.Context;

import com.jincaizi.kuaiwin.utils.Utils;
import com.jincaizi.vendor.http.AsyncHttpClient;
import com.jincaizi.vendor.http.AsyncHttpResponseHandler;
import com.jincaizi.vendor.http.RequestHandle;
import com.jincaizi.vendor.http.RequestParams;
import com.jincaizi.vendor.http.ResponseHandlerInterface;

public class JinCaiZiHttpClient {
	public static String BASE_URL = "http://m.jincaizi.com/KuaiwinMobile.aspx";
	//public static String BASE_URL_XICAI = "http://m.jincaizi.com/XicaiMobile.aspx";
	private static AsyncHttpClient client = new AsyncHttpClient(true,80,443);
    
	public static void closeExpireConnection() {
		client.getHttpClient().getConnectionManager().closeExpiredConnections();
	}
	public static String getAbsoluteUrl(String relativeUrl) {
		return BASE_URL + relativeUrl;
	}

	public static void get(String url, AsyncHttpResponseHandler responseHandler) {
		client.get(getAbsoluteUrl(url), responseHandler);
	}
	public static RequestHandle post(Context context, String url, AsyncHttpResponseHandler responseHandler) {
		if(Utils.isCmwapNet(context)) {
			//Log.i("test", "cmwap");
			client.setProxy("10.0.0.172", 80, "http");
		} 
		return client.post(url, responseHandler);
	}
	
	public static RequestHandle postFormData(Context context, String url, Header[] headers, HttpEntity entity, String contentType,
            ResponseHandlerInterface responseHandler) {
		return client.post(context, url, headers, entity, contentType, responseHandler);
	}
	public static RequestHandle postFormData(Context context, String url, RequestParams params, ResponseHandlerInterface responseHandler) {
		if(Utils.isCmwapNet(context)) {
		//Log.i("test", "cmwap");
			client.setProxy("10.0.0.172", 80, "http");
		} 
		return client.post(context, url, params, responseHandler);
	}
}
