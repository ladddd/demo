package com.jincaizi.requesters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.jincaizi.kuaiwin.utils.Utils;
import com.jincaizi.vendor.http.AsyncHttpClient;
import com.jincaizi.vendor.http.AsyncHttpResponseHandler;
import com.jincaizi.vendor.http.RequestParams;
import org.apache.http.Header;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * Created by chenweida on 2015/3/16.
 */
public class LotteryLeakRequester {

    private Context context;
    private String lottery;
    private AsyncHttpClient asyncHttpClient;

    public LotteryLeakRequester(Context context, String lottery)
    {
        this.context = context;
        this.lottery = lottery;

        asyncHttpClient = new AsyncHttpClient(); }


    public void query()
    {
        RequestParams params = new RequestParams();
        params.add("act", "yilou");
        params.add("lotterytype", lottery);
        params.add("datatype", "json");
        params.add("jsoncallback",
                "jsonp" + String.valueOf(System.currentTimeMillis()));
        params.add("_", String.valueOf(System.currentTimeMillis()));

        String url = AsyncHttpClient.getUrlWithQueryString(false, "http://m.jincaizi.com/kuaiwinmobile.aspx", params);
        asyncHttpClient.post(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Intent intent = new Intent("leak");
                if (responseBody != null) {
                    try {
                        String charset;
                        if(Utils.isCmwapNet(context)) {
                            charset = "utf-8";
                        } else {
                            charset = "gb2312";
                        }
                        String jsonData = new String(responseBody, charset);
                        intent.putExtra("success", true);
                        intent.putExtra("JSON", jsonData);
                        Log.e("haha", jsonData);
                    }
                    catch (UnsupportedEncodingException e)
                    {
                        e.printStackTrace();
                        intent.putExtra("success", false);
                    }
                }
                context.sendBroadcast(intent);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e("leak", "failed" + error.toString());
                Intent intent = new Intent("leak");
                intent.putExtra("success", false);
                context.sendBroadcast(intent);
            }
        });
    }
}
