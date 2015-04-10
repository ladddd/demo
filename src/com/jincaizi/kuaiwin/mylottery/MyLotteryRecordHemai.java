package com.jincaizi.kuaiwin.mylottery;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import org.apache.http.Header;

import com.google.gson.stream.JsonReader;
import com.jincaizi.adapters.MylotteryRecordAdapter;
import com.jincaizi.bean.MyLotteryRecordEntity;
import com.jincaizi.http.JinCaiZiHttpClient;
import com.jincaizi.R;
import com.jincaizi.kuaiwin.utils.Constants;
import com.jincaizi.kuaiwin.utils.Utils;
import com.jincaizi.kuaiwin.utils.Constants.LotteryDetailType;
import com.jincaizi.kuaiwin.utils.SafeAsyncTask;
import com.jincaizi.kuaiwin.widget.JinCaiZiProgressDialog;
import com.jincaizi.kuaiwin.widget.PullToRefreshListView;
import com.jincaizi.kuaiwin.widget.PullToRefreshListView.OnRefreshListener;
import com.jincaizi.vendor.http.AsyncHttpClient;
import com.jincaizi.vendor.http.AsyncHttpResponseHandler;
import com.jincaizi.vendor.http.RequestHandle;
import com.jincaizi.vendor.http.RequestParams;

public class MyLotteryRecordHemai extends Activity implements OnClickListener, OnRefreshListener{
    private static final String TAG = "MyLotteryRecordHemai";
    private static final String PAGESIZE = "10";
    private RadioButton mylotteryUndone;
    private RadioButton mylotteryRecent;
    private RadioButton mylotteryAll;
    private PullToRefreshListView mylotteryListView;
    private LinkedList<MyLotteryRecordEntity>myList  = new LinkedList<MyLotteryRecordEntity>();
    private LinkedList<MyLotteryRecordEntity>undoneList  = new LinkedList<MyLotteryRecordEntity>();
    private LinkedList<MyLotteryRecordEntity>recentList  = new LinkedList<MyLotteryRecordEntity>();
    private LinkedList<MyLotteryRecordEntity>AllList  = new LinkedList<MyLotteryRecordEntity>();
    private MylotteryRecordAdapter mMyAdapter;
    private SharedPreferences sp;
    private int userid = 0;
    private String mUpk;
    private RelativeLayout leftMenu;
    private int pageindex = 1;
    private Activity mActivity;
    private RequestHandle myRequestHandle;
    private JinCaiZiProgressDialog mProgressDialog;
    private View mFootView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mylottery_record_layout);
        mActivity = this;
        sp = getSharedPreferences("loginStatus", Context.MODE_PRIVATE);
        userid = sp.getInt("userid", 0);
        mUpk = sp.getString("upk", "");
        _findViews();
        _setListener();
        mFootView = LayoutInflater.from(mActivity).inflate(R.layout.pulltorefreshlistview_footer, null);
        mylotteryListView.addFooterView(mFootView, null, true);
        mFootView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                _showProgress();
                if(mylotteryAll.isChecked()) {
                    _getMyRecordAll(false);
                } else if(mylotteryUndone.isChecked()) {
                    _getMyRecordUndone(false);
                } else if(mylotteryRecent.isChecked()) {
                    _getMyRecordRecent(false);
                }
            }
        });
        _getMyRecordUndone(true);
        mMyAdapter = new MylotteryRecordAdapter(this, myList);
        TextView tv = (TextView) findViewById(R.id.empty_mylotterylistview);
        mylotteryListView.setEmptyView(tv);
        tv.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                _showProgress();
                pageindex = 1;
                if(mylotteryAll.isChecked()) {
                    myList.clear();
                    myList.addAll(AllList);
                    mMyAdapter.notifyDataSetChanged();
                    _getMyRecordAll(true);

                } else if(mylotteryRecent.isChecked()) {
                    myList.clear();
                    myList.addAll(recentList);
                    mMyAdapter.notifyDataSetChanged();
                    _getMyRecordRecent(true);
                } else if(mylotteryUndone.isChecked()) {
                    myList.clear();
                    myList.addAll(undoneList);
                    mMyAdapter.notifyDataSetChanged();
                    _getMyRecordUndone(true);
                }
            }
        });
        mylotteryListView.setAdapter(mMyAdapter);
        _showProgress();
    }
    private void _findViews() {
        mylotteryUndone = (RadioButton)findViewById(R.id.mylottery_record_undone);
        mylotteryRecent = (RadioButton)findViewById(R.id.mylottery_record_recent);
        mylotteryAll = (RadioButton)findViewById(R.id.mylottery_record_all);
        mylotteryListView = (PullToRefreshListView)findViewById(R.id.mylottery_record_listView);
        TextView title = (TextView)findViewById(R.id.current_lottery);
        title.setText("我的合买记录");
        findViewById(R.id.sumbit_group_buy).setVisibility(View.GONE);
        leftMenu = (RelativeLayout) findViewById(R.id.left_layout);


    }
    private void _setListener() {
        mylotteryUndone.setOnClickListener(this);
        mylotteryRecent.setOnClickListener(this);
        mylotteryAll.setOnClickListener(this);
        leftMenu.setOnClickListener(this);
        mylotteryListView.setOnRefreshListener(this);
        mylotteryListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                Intent detailIntent = new Intent(mActivity, LotteryDetailActivity.class);
                detailIntent.putExtra(Constants.ENTITY, myList.get(arg2-1));
                detailIntent.putExtra(Constants.USERID, String.valueOf(userid));
                detailIntent.putExtra(Constants.LOTTERYDETAILTYPE, LotteryDetailType.HEMAI.toString());
                startActivity(detailIntent);
            }
        });
    }
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch(v.getId()) {
            case R.id.left_layout:
                finish();
                break;
            case R.id.mylottery_record_undone:
                _showProgress();
                pageindex = 1;
                myList.clear();
                myList.addAll(undoneList);
                mMyAdapter.notifyDataSetChanged();
                _getMyRecordUndone(true);
                mylotteryUndone.setChecked(true);
                mylotteryRecent.setChecked(false);
                mylotteryAll.setChecked(false);
                break;
            case R.id.mylottery_record_recent:
                _showProgress();
                pageindex = 1;
                myList.clear();
                myList.addAll(recentList);
                mMyAdapter.notifyDataSetChanged();
                _getMyRecordRecent(true);

                mylotteryUndone.setChecked(false);
                mylotteryRecent.setChecked(true);
                mylotteryAll.setChecked(false);
                break;
            case R.id.mylottery_record_all:
                _showProgress();
                pageindex = 1;
                myList.clear();
                myList.addAll(AllList);
                mMyAdapter.notifyDataSetChanged();
                _getMyRecordAll(true);
                mylotteryUndone.setChecked(false);
                mylotteryRecent.setChecked(false);
                mylotteryAll.setChecked(true);
                break;
            default:
                break;
        }
    }
    private void _getMyRecordUndone(final boolean isClearList) {
        RequestParams params = new RequestParams();
        params.add("act", "userhemai");
        params.add("datatype", "json");
        params.add("WinStatus", "0");
        params.add("pageindex", String.valueOf(pageindex));
        params.add("pagesize", PAGESIZE);
        params.add("userid", String.valueOf(userid));
        params.add("upk", mUpk);
        params.add("jsoncallback",
                "jsonp" + String.valueOf(System.currentTimeMillis()));
        params.add("_", String.valueOf(System.currentTimeMillis()));
        String url = AsyncHttpClient.getUrlWithQueryString(false,
                JinCaiZiHttpClient.BASE_URL, params);
        myRequestHandle = JinCaiZiHttpClient.post(this, url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  byte[] responseBody) {
                try {
                    // Object jsonResponse = parseResponse(responseBody);
                    String charset;
                    if(Utils.isCmwapNet(MyLotteryRecordHemai.this)) {
                        charset = "utf-8";
                    } else {
                        charset = "gb2312";
                    }
                    String jsonData = new String(responseBody, charset);
                    Log.d(TAG, "success undone = " + jsonData);

                    // 如果需要解析JSON数据，首要要生成一个JsonReader对象

                    _jsonReadDaigouObject(jsonData, isClearList, undoneList);
                    pageindex++;
                } catch (UnsupportedEncodingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    _hideProgress();
                    mylotteryListView.onRefreshComplete();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    _hideProgress();
                    mylotteryListView.onRefreshComplete();
                } finally {
                    //FIXME reader.close should be here
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  byte[] responseBody, Throwable error) {
                Log.d(TAG, "failure = " + error.toString());
                _hideProgress();
                mylotteryListView.onRefreshComplete();
                Toast.makeText(mActivity, "更新失败", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void _getMyRecordRecent(final boolean isClearList) {
        RequestParams params = new RequestParams();
        params.add("act", "userhemai");
        params.add("datatype", "json");
        params.add("WinStatus", "1");
        params.add("pageindex", String.valueOf(pageindex));
        params.add("pagesize", PAGESIZE);
        params.add("userid", String.valueOf(userid));
        params.add("upk", mUpk);
        params.add("jsoncallback",
                "jsonp" + String.valueOf(System.currentTimeMillis()));
        params.add("_", String.valueOf(System.currentTimeMillis()));
        String url = AsyncHttpClient.getUrlWithQueryString(false,
                JinCaiZiHttpClient.BASE_URL, params);
        myRequestHandle = JinCaiZiHttpClient.post(this, url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  byte[] responseBody) {
                try {
                    // Object jsonResponse = parseResponse(responseBody);
                    String charset;
                    if(Utils.isCmwapNet(MyLotteryRecordHemai.this)) {
                        charset = "utf-8";
                    } else {
                        charset = "gb2312";
                    }
                    String jsonData = new String(responseBody, charset);
                    Log.d(TAG, "success recent = " + jsonData);

                    // 如果需要解析JSON数据，首要要生成一个JsonReader对象

                    _jsonReadDaigouObject(jsonData, isClearList, recentList);
                    pageindex++;
                } catch (UnsupportedEncodingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    _hideProgress();
                    mylotteryListView.onRefreshComplete();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    _hideProgress();
                    mylotteryListView.onRefreshComplete();
                } finally {
                    //FIXME reader.close should be here
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  byte[] responseBody, Throwable error) {
                Log.d(TAG, "failure = " + error.toString());
                _hideProgress();
                mylotteryListView.onRefreshComplete();
                Toast.makeText(mActivity, "更新失败", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void _getMyRecordAll(final boolean isClearList) {
        RequestParams params = new RequestParams();
        params.add("act", "userhemai");
        params.add("datatype", "json");
        params.add("WinStatus", "2");
        params.add("pageindex", String.valueOf(pageindex));
        params.add("pagesize", PAGESIZE);
        params.add("userid", String.valueOf(userid));
        params.add("upk", mUpk);
        params.add("jsoncallback",
                "jsonp" + String.valueOf(System.currentTimeMillis()));
        params.add("_", String.valueOf(System.currentTimeMillis()));
        String url = AsyncHttpClient.getUrlWithQueryString(false,
                JinCaiZiHttpClient.BASE_URL, params);
        myRequestHandle = JinCaiZiHttpClient.post(this, url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  byte[] responseBody) {
                try {
                    // Object jsonResponse = parseResponse(responseBody);
                    String charset;
                    if(Utils.isCmwapNet(MyLotteryRecordHemai.this)) {
                        charset = "utf-8";
                    } else {
                        charset = "gb2312";
                    }
                    String jsonData = new String(responseBody, charset);
                    Log.d(TAG, "success all = " + jsonData);

                    // 如果需要解析JSON数据，首要要生成一个JsonReader对象

                    _jsonReadDaigouObject(jsonData, isClearList, AllList);
                    pageindex++;
                } catch (UnsupportedEncodingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    _hideProgress();
                    mylotteryListView.onRefreshComplete();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    _hideProgress();
                    mylotteryListView.onRefreshComplete();
                } finally {
                    //FIXME reader.close should be here
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  byte[] responseBody, Throwable error) {
                Log.d(TAG, "failure = " + error.toString());
                _hideProgress();
                mylotteryListView.onRefreshComplete();
                Toast.makeText(mActivity, "更新失败", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void _jsonReadDaigouObject(String jsonData, final boolean isClearList, final LinkedList<MyLotteryRecordEntity>paramList) throws IOException {
        final JsonReader reader = new JsonReader(new StringReader(
                jsonData));
        SafeAsyncTask<Void> getAllDaigouTask = new SafeAsyncTask<Void>() {
            private String httpResponseMsg = "更新失败";
            private int httpResponseResult = -2;

            @Override
            public Void call() throws Exception {
                int returnSize = 0;
                httpResponseResult = -2;
                reader.beginObject();
                while (reader.hasNext()) {
                    String tagName = reader.nextName();
                    if (tagName.equals("result")) {
                        httpResponseResult = reader.nextInt();
                        Log.d(TAG, "result--->" + httpResponseResult);

                    } else if (tagName.equals("size")) {
                        returnSize= reader.nextInt();
                        Log.d(TAG, "size--->" + returnSize);

                    } else if (tagName.equals("data")) {
                        if(isClearList) {
                            paramList.clear();
                        }

                        paramList.addAll(paramList.size(), _jsonReadDataArray(reader));

                    }else if(tagName.equals("msg")) {
                        httpResponseMsg = reader.nextString();
                    }
                }
                reader.endObject();
                return null;
            }

            @Override
            protected void onSuccess(Void t) throws Exception {
                // TODO Auto-generated method stub
                super.onSuccess(t);
                myList.clear();
                myList.addAll(paramList);
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
                mylotteryListView.onRefreshComplete();
                try {
                    reader.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                if(httpResponseResult == -2) {
                    Toast.makeText(mActivity, httpResponseMsg, Toast.LENGTH_SHORT).show();
                }
            }


        };
        getAllDaigouTask.execute();
    }
    private LinkedList<MyLotteryRecordEntity> _jsonReadDataArray(JsonReader reader) throws IOException {
        LinkedList<MyLotteryRecordEntity> entityList = new LinkedList<MyLotteryRecordEntity>();
        reader.beginArray();
        while(reader.hasNext()) {
            entityList.add(_jsonReadDataObject(reader));
        }
        reader.endArray();
        return entityList;
    }
    private MyLotteryRecordEntity _jsonReadDataObject(JsonReader reader) throws IOException {
        MyLotteryRecordEntity entity = new MyLotteryRecordEntity();
        reader.beginObject();
        int hadBuyCount = 0;
        int totalShare = 1;
        boolean isWin = false;
        while(reader.hasNext()) {
            String tagName = reader.nextName();
            if(tagName.equals("id")) {
                Log.d(TAG, "第" + reader.nextInt() + "项");
            } else if(tagName.equals("HemaiId")) {
                entity.setId(String.valueOf(reader.nextInt()));
            } else if(tagName.equals("UserName")) {
                entity.setFaqiren(reader.nextString());
            } else if(tagName.equals("LotteryType")) {
                entity.setLotteryType(reader.nextString());
            } else if(tagName.equals("Qihao")) {
                String qihao = reader.nextString();
                qihao = qihao.replace("-", "");
                entity.setQiHao(qihao);
            } else if(tagName.equals("BetCount")) {
                reader.nextInt();

            } else if(tagName.equals("TotalShare")) {
                totalShare = reader.nextInt();
                entity.setBetAmount(String.valueOf(totalShare));
            } else if(tagName.equals("Brokerage")) {
                entity.setTicheng(String.valueOf(reader.nextInt()) + "%");
            } else if(tagName.equals("InsureCount")) {
                entity.setBaodi(String.valueOf(reader.nextInt()));
            } else if(tagName.equals("HadBuyCount")) {
                hadBuyCount = reader.nextInt();
                //entity.setMoney(String.valueOf(reader.nextInt()));
            } else if(tagName.equals("Multiple")) {
                entity.setBeishu(String.valueOf(reader.nextInt()));
            } else if(tagName.equals("TotalAmount")) {
                entity.setZongjine("￥" + String.valueOf(reader.nextDouble()));
            } else if(tagName.equals("MyBuyAmount")) {
                entity.setMoney("￥" + String.valueOf(reader.nextDouble()));
            } else if(tagName.equals("CreateTime")) {
                entity.setTime(reader.nextString()) ;
            } else if(tagName.equals("BetStatus")) {
                entity.setProcedureStatus(reader.nextString());
            } else if(tagName.equals("WinStatus")) {
                String winStatus = reader.nextString();
                if(winStatus.equals("已中奖")) {
                    isWin = true;
                } else {
                    entity.setAwardStatus(winStatus);
                }
            } else if(tagName.equals("OriginalBonus")) {
                Log.d(TAG, "originalBonus = " + reader.nextDouble());
            } else if(tagName.equals("FinalBonus")) {
                double allBonus = reader.nextDouble();
                if(isWin) {
                    entity.setAllBonus("￥" + allBonus);
                }

            } else if(tagName.equals("PlusBonus")) {
                Log.d(TAG, "plusBonus = " + reader.nextDouble());
            } else if(tagName.equals("MyBonus")){
                double myBonus = reader.nextDouble();
                if(isWin) {
                    entity.setAwardStatus("￥" + myBonus);
                }
            }
        }
        reader.endObject();
        entity.setBetAverage("1");
        int jindu = (hadBuyCount/totalShare)*100;
        entity.setJindu(String.valueOf(jindu) + "%");
        return entity;
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
    @Override
    public void onRefresh() {
        // TODO Auto-generated method stub
        pageindex = 1;
        if(mylotteryAll.isChecked()) {
            myList.clear();
            myList.addAll(AllList);
            mMyAdapter.notifyDataSetChanged();
            _getMyRecordAll(true);

        } else if(mylotteryRecent.isChecked()) {
            myList.clear();
            myList.addAll(recentList);
            mMyAdapter.notifyDataSetChanged();
            _getMyRecordRecent(true);
        } else if(mylotteryUndone.isChecked()) {
            myList.clear();
            myList.addAll(undoneList);
            mMyAdapter.notifyDataSetChanged();
            _getMyRecordUndone(true);
        }
    }
}
