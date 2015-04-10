package com.jincaizi.kuaiwin.mylottery;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;
import com.jincaizi.kuaiwin.buycenter.adapter.OtherLotteryAdapter;
import com.jincaizi.kuaiwin.tool.CheckLogin;
import org.apache.http.Header;

import com.google.gson.stream.JsonReader;
import com.jincaizi.http.JinCaiZiHttpClient;
import com.jincaizi.kuaiwin.FragmentCallbacks;
import com.jincaizi.kuaiwin.utils.Utils;
import com.jincaizi.R;
import com.jincaizi.vendor.http.AsyncHttpClient;
import com.jincaizi.vendor.http.AsyncHttpResponseHandler;
import com.jincaizi.vendor.http.RequestParams;

public class MyLotteryFragment extends Fragment implements OnClickListener, FragmentCallbacks{
    private static final int LOGINREQUESTCODE = 4;
    public static final String TAG = "MyLotteryFragment";
    public static final int loginRequestCode = 1;

    private static final String[] type ={"代购记录", "追号记录","合买记录", "账户明细",  "充值", "提款",
            "修改密码","银行卡绑定","身份认证"};
    private static final int[] imageResource = {R.drawable.daigou_pic, R.drawable.zhuihao_pic,
            R.drawable.hemai_pic, R.drawable.zhanghu_pic, R.drawable.chongchi_pic, R.drawable.tikuan_pic,
            R.drawable.pwd_pic, R.drawable.card_pic, R.drawable.id_auth};

    private TextView mTvLogin;
    private SharedPreferences sp;
    private TextView loginHint;
    private int userid;
    private String mUpk;
    private TextView money;

    private GridView gridView;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        return inflater.inflate(R.layout.mylottery_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onViewCreated(view, savedInstanceState);
        _findViews(view);
        _setListener();
        _getLoginStatus();
        _requestData();
    }
    private void _findViews(View view) {
        mTvLogin = (TextView)view.findViewById(R.id.sumbit_group_buy);
        mTvLogin.setText("登录");
        TextView title = (TextView)view.findViewById(R.id.current_lottery);
        title.setText("我的彩票");
        loginHint = (TextView)view.findViewById(R.id.login_hint_view);
        view.findViewById(R.id.left_layout).setVisibility(View.GONE);
        money = (TextView) view.findViewById(R.id.money);
        gridView = (GridView) view.findViewById(R.id.gridview);
    }
    private void _setListener() {
        mTvLogin.setOnClickListener(this);

        gridView.setAdapter(new OtherLotteryAdapter(getActivity(), type, imageResource));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                //如果未登录或登录超时，提示登录。
                if (!mTvLogin.getText().toString().equals("注销"))
                {
                    _showHintDialog();
                    return;
                }

                Intent intent = null;
                switch (i)
                {
                    case 0:
                        intent = new Intent(getActivity(), MyLotteryRecordDaigou.class);
                        break;
                    case 1:
                        intent = new Intent(getActivity(), MyLotteryRecordHemai.class);
                        break;
                    case 2:
                        intent = new Intent(getActivity(), MyLotteryRecordZuihao.class);
                        break;
                    case 3:
                        intent = new Intent(getActivity(), MyAccountDetail.class);
                        break;
                    case 4:
                        //TODO 充值逻辑
                        Toast.makeText(getActivity(), "暂未开放", Toast.LENGTH_SHORT).show();
                        break;
                    case 5:
                        intent = new Intent(getActivity(), MyTikuan.class);
                        break;
                    case 6:
                        intent = new Intent(getActivity(), ModifyPwdActivity.class);
                        break;
                    case 7:
                        intent = new Intent(getActivity(), CardBinding.class);
                        break;
                    case 8:
                        intent = new Intent(getActivity(), IdAuthActivity.class);
                        break;
                    default:
                        break;
                }
                if (intent != null)
                {
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        _getLoginStatus();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if(userid == 0) {
            Intent loginIntent = new Intent(getActivity(), Login.class);
            startActivityForResult(loginIntent, loginRequestCode);
            return;
        }
        switch(v.getId()) {
            case R.id.sumbit_group_buy:
                if (mTvLogin.getText().toString().equals("注销"))
                {
                    SharedPreferences sharedPreferences =
                            getActivity().getSharedPreferences("userinfo", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("uname", null);
                    editor.putString("upswd", null);
                    editor.putBoolean("auto", false);
                    editor.commit();
                    editor.clear();
                    editor = sp.edit();
                    editor.putInt("userid", 0);
                    editor.putString("username", null);
                    editor.putString("upk", null);
                    editor.commit();

                    mTvLogin.setText("登录");
                    loginHint.setText("请登录");
                    money.setVisibility(View.GONE);
                }
                else {
                    Intent loginIntent = new Intent(getActivity(), Login.class);
                    startActivityForResult(loginIntent, loginRequestCode);
                }
                break;
            default:
                Toast.makeText(getActivity(), "暂未开放", Toast.LENGTH_SHORT).show();
                break;

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        _getLoginStatus();
        _requestData();
    }

    private void _getLoginStatus() {
        sp = getActivity().getSharedPreferences("loginStatus", Context.MODE_PRIVATE);
        userid = sp.getInt("userid", 0);
        mUpk = sp.getString("upk", "");
        if(userid != 0) {
            loginHint.setText("欢迎您 " + sp.getString("username", ""));
            loginHint.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            mTvLogin.setText("注销");
        } else {
            mTvLogin.setText("登录");
            loginHint.setText("请登录");
            money.setVisibility(View.GONE);
        }
    }

    private  void _requestData() {
        if(userid == 0) {
            return;
        }
        RequestParams params = new RequestParams();
        params.add("act", "useraccount");
        params.add("userid", userid + "");
        params.add("upk", mUpk);
        params.add("datatype", "json");
        params.add("jsoncallback",
                "jsonp" + String.valueOf(System.currentTimeMillis()));
        params.add("_", String.valueOf(System.currentTimeMillis()));
        String url = AsyncHttpClient.getUrlWithQueryString(false,
                JinCaiZiHttpClient.BASE_URL, params);
        JinCaiZiHttpClient.post(getActivity(), url,
                new AsyncHttpResponseHandler() {


                    private String totalAmount;
                    private String availableAmount;
                    private String freezeAmount;
                    private String resMsg = "数据获取失败";

                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          byte[] responseBody) {
                        int result = -1;
                        JsonReader reader = null;
                        try {
                            String charset;
                            if(Utils.isCmwapNet(getActivity())) {
                                charset = "utf-8";
                            } else {
                                charset = "gb2312";
                            }
                            String jsonData = new String(responseBody, charset);
                            Log.d(TAG, "账户余额 = " + jsonData);
                            reader = new JsonReader(new StringReader(jsonData));
                            reader.beginObject();
                            while(reader.hasNext()) {
                                String tagName = reader.nextName();
                                if(tagName.equals("msg")) {
                                    resMsg = reader.nextString();
                                } else if(tagName.equals("result")) {
                                    result = reader.nextInt();
                                } else if(tagName.equals("TotalAmount")) {
                                    totalAmount = reader.nextString();
                                } else if(tagName.equals("AvailableAmount")) {
                                    availableAmount = reader.nextString();
                                } else if(tagName.equals("FreezeAmount")) {
                                    freezeAmount = reader.nextString();
                                }
                            }
                            reader.endObject();

                            if(result == 0) {
                                SpannableString ss = new SpannableString("当前余额:" + totalAmount+ "元" + " 可用余额:" + availableAmount + "元"
                                        + " 冻结余额:" + freezeAmount + "元");
                                ss.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.buyer_red)), 5,
                                        5 + totalAmount.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                ss.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.buyer_red)), 12 + totalAmount.length(),
                                        12 + totalAmount.length()+availableAmount.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                ss.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.buyer_red)), 19 + totalAmount.length() + availableAmount.length(),
                                        ss.length()-1,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                money.setText(ss);
                                money.setVisibility(View.VISIBLE);
                            } else {
                                _setMoneyGetResultValue(resMsg);
                            }

                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                            resMsg = "数据获取失败";
                            _setMoneyGetResultValue(resMsg);
                        } catch (Exception e) {
                            e.printStackTrace();
                            resMsg = "数据获取失败";
                            _setMoneyGetResultValue(resMsg);

                        } finally {

                            try {
                                if(reader != null) {
                                    reader.close();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          byte[] responseBody, Throwable error) {
                        resMsg = "数据获取失败";
                        _setMoneyGetResultValue(resMsg);
                    }
                });
    }
    private void _setMoneyGetResultValue(String resMsg) {
        mTvLogin.setText("重新登录");
        money.setVisibility(View.GONE);
    }

    @Override
    public void onShow() {
        Log.i(TAG, "onshow in mylotteryfragment");
        _getLoginStatus();
        _requestData();

    }

    private void _showHintDialog() {
        final Dialog localDialog = new Dialog(getActivity(), R.style.Theme_dialog);
        View view = LayoutInflater.from(getActivity()).inflate(
                R.layout.dialog_submit_bet, null);
        TextView dialogTitle = (TextView) view
                .findViewById(R.id.submit_dialog_title);
        dialogTitle.setText("提示");
        TextView dialogContent = (TextView) view
                .findViewById(R.id.submit_dialog_content);
        TextView dialogCancel = (TextView) view.findViewById(R.id.tv_submit_cancel);
        dialogCancel.setVisibility(View.GONE);
        TextView dialogOK = (TextView) view.findViewById(R.id.tv_submit_ok);
        dialogOK.setText("确定");
        localDialog.setContentView(view);
        Window dialogWindow = localDialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int)(250 * getResources().getDisplayMetrics().density); // 宽度
        dialogWindow.setAttributes(lp);
        dialogOK.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                localDialog.cancel();
                CheckLogin.clearLoginStatus(sp);
                Intent loginIntent = new Intent(getActivity(), Login.class);
                startActivityForResult(loginIntent, LOGINREQUESTCODE);
            }
        });
        dialogContent.setText("未登录或登录过期！");
        localDialog.show();
    }
}
