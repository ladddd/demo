package com.jincaizi.kuaiwin.chart.requesters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.jincaizi.vendor.http.AsyncHttpClient;
import com.jincaizi.vendor.http.AsyncHttpResponseHandler;
import com.jincaizi.vendor.http.RequestParams;
import org.apache.http.Header;

import java.nio.charset.Charset;

/**
 * Created by chenweida on 2015/2/27.
 */
public class LotteryTimeRequester {

    private Context context;
    private String lottery;

    private AsyncHttpClient asyncHttpClient;

    public LotteryTimeRequester(Context context, String lottery)
    {
        this.context = context;
        this.lottery = lottery;

        asyncHttpClient = new AsyncHttpClient(); }


    public void query()
    {
        RequestParams params = new RequestParams();
        params.add("act", "sellqihao");
        params.add("lotterytype", lottery);
        params.add("datatype", "json");
        params.add("jsoncallback",
                "jsonp" + String.valueOf(System.currentTimeMillis()));
        params.add("_", String.valueOf(System.currentTimeMillis()));
//        params.add("qihao", "2015-02-14");

        String url = AsyncHttpClient.getUrlWithQueryString(false, "http://m.jincaizi.com/kuaiwinmobile.aspx", params);
        asyncHttpClient.post(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Intent intent = new Intent("qihao");
                if (responseBody != null) {
                    String jsonData = new String(responseBody, Charset.forName("gb2312"));
                    intent.putExtra("success", true);
                    intent.putExtra("JSON", jsonData);
                    Log.e("haha", jsonData);
                }
                context.sendBroadcast(intent);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e("haha", "failed" + error.toString());
                Intent intent = new Intent("haha");
                intent.putExtra("success", false);
                context.sendBroadcast(intent);
            }
        });
    }
}
