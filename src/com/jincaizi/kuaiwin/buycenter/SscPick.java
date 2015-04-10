package com.jincaizi.kuaiwin.buycenter;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.*;

import android.text.InputType;
import android.widget.*;
import com.jincaizi.common.IntentData;
import com.jincaizi.common.StringUtil;
import com.jincaizi.kuaiwin.chart.views.CircularProgress;
import com.jincaizi.kuaiwin.tool.SscRandom;
import org.apache.http.Header;

import com.google.gson.stream.JsonReader;
import com.jincaizi.adapters.K3TouZhuAdapter;
import com.jincaizi.R;
import com.jincaizi.http.JinCaiZiHttpClient;
import com.jincaizi.kuaiwin.mylottery.Login;
import com.jincaizi.kuaiwin.tool.BeiTouGenerator;
import com.jincaizi.kuaiwin.tool.CheckLogin;
import com.jincaizi.kuaiwin.tool.QiHaoTool;
import com.jincaizi.kuaiwin.utils.Constants;
import com.jincaizi.kuaiwin.utils.Constants.City;
import com.jincaizi.kuaiwin.utils.Constants.SscType;
import com.jincaizi.kuaiwin.utils.IntentAction;
import com.jincaizi.kuaiwin.utils.SafeAsyncTask;
import com.jincaizi.kuaiwin.utils.UiHelper;
import com.jincaizi.kuaiwin.utils.Utils;
import com.jincaizi.kuaiwin.widget.JinCaiZiProgressDialog;
import com.jincaizi.vendor.http.AsyncHttpClient;
import com.jincaizi.vendor.http.AsyncHttpResponseHandler;
import com.jincaizi.vendor.http.RequestParams;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;

public class SscPick extends Activity implements OnClickListener{
    public static final String BALL = "redBall";
    public static final String BETTYPE = "betType";
    public static final String CITY = "city";
    public static final String ZHUAMOUNT = "zhushuamount";
    public static final int LOGINREQUESTCODE = 2;

    public static final int MAXBEISHU = 9999;
    public static final int MAXZHUIHAO = 200;
    public static final int MINBEISHU = 1;
    public static final int MINZHUIHAO = 0;
    public static final int CONTINUEPICK = 0;
    public static final int REPICK = 1;
    protected static final String TAG = null;

    private int[] zushuOfZhixuanHezhi = {1,3, 6, 10, 15, 21,28, 36, 45,
            55,63,69,73,75,75,73,69,63, 55,
            45,36,28,21,15,10,6,3,1};
    private int[]zushuOfZuliuHezhi={ 1, 1, 2, 3, 4, 5,
            7,8,9,10,10,10,10,9,8, 7,
            5,4,3,2,1,1};
    private int[]zushuOfZusanHezhi={1, 2, 1, 3, 3, 3, 4, 5,
            4,5,5,4,5,5,4,5,5, 4,
            5,4,3,3,3,1,2,1};

    private ListView mListView;
    private RelativeLayout mBackView;
    private TextView mRandomOne;
    private TextView mContinuePick;
    private TextView mClearList;
    private TextView mSubmitGroupBuy;
    private TextView mSubmitDaigou;
    private TextView mFootLeftBtn;
    private EditText mTraceTime;
    private EditText mTraceIssue;
    private ImageView mTimeSub;
    private ImageView mTimeAdd;
    private ImageView mIssueSub;
    private ImageView mIssueAdd;
    private CircularProgress mSmartFollowPB;
    private Dialog myDialog;
    private TextView dialogCancel;
    private TextView dialogOK;
    private String mCity;
    private LinkedList<String> mBallList = new LinkedList<String>();
    private LinkedList<String> mTypeList = new LinkedList<String>();
    private LinkedList<Integer>mZhuList = new LinkedList<Integer>();
    private LinkedList<HashMap<String, ArrayList<Boolean>>> selectedNumberList =
            new LinkedList<HashMap<String, ArrayList<Boolean>>>();
    private String mBetType;
    private String mBall;
    private int mZhuiHaoNums = 1;
    private int mBeiShu = 1;
    private K3TouZhuAdapter mMyAdpater;

    private int mUserid;
    private String mUpk;
    private SharedPreferences sp;
    private String mQihao = "";
    private int perDayTermCount;
    private String lotterytype;
    private CheckBox mZhuiCheckbox;
    private int mCurZhuShu;

    private View footerView;

    //元
    private TextView price;
    //注
    private TextView stakes;
    //期
    private TextView issue;
    //倍
    private TextView times;

    private RelativeLayout tipsLayout;

    private int memoryPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_pick_list);
        sp = this.getSharedPreferences("loginStatus", Context.MODE_PRIVATE);
        _initData(getIntent(), -1);
        _getTouZhuAttribute();
        _findViews();
        _setListener();
        mMyAdpater = new K3TouZhuAdapter(this, mBallList, mTypeList, mZhuList, false);
        mListView.setAdapter(mMyAdpater);
        if (mBallList.size() > 0 && mListView.getFooterViewsCount() == 0)
        {
            mListView.addFooterView(footerView);
        }
    }
    private void _clearBetListData() {
        mBallList.clear();
        mTypeList.clear();
        mZhuList.clear();
    }

    private void _getTouZhuAttribute() {
        if(mCity.equals(Constants.City.jiangxi.toString())) {
            lotterytype = "JXSSC";
            perDayTermCount = 84;
        } else if(mCity.equals(Constants.City.chongqing.toString())) {
            lotterytype = "CQSSC";
            perDayTermCount = 120;
        }
    }

    private void _initData(Intent intent, int memoryPostion) {
        mCity = intent.getStringExtra(Syxw.CITY);
        mBetType = intent.getStringExtra(BETTYPE);
        mBall = intent.getStringExtra(BALL);
        mQihao = intent.getStringExtra("qihao");
        int zhushuAmount = intent.getIntExtra(ZHUAMOUNT, 1);
        HashMap<String, ArrayList<Boolean>> map = (HashMap<String, ArrayList<Boolean>>)intent.getSerializableExtra(IntentData.SELECT_NUMBERS);
        if (memoryPostion == -1) {
            mTypeList.addFirst(mBetType);
            mBallList.addFirst(mBall);
            mZhuList.addFirst(zhushuAmount);
            selectedNumberList.addFirst(map);
        } else {
            mBallList.set(memoryPostion, mBall);
            mTypeList.set(memoryPostion, mBetType);
            mZhuList.set(memoryPostion, zhushuAmount);
            selectedNumberList.set(memoryPostion, map);
        }

    }
    private void _findViews() {
        ((TextView) findViewById(R.id.current_lottery)).setText(Constants.City.getCityName(mCity)+"-时时彩投注");
        mListView = (ListView) findViewById(R.id.touzhu_detail_list);
        TextView tv = (TextView) findViewById(R.id.empty_list_view);
        mListView.setEmptyView(tv);
        mBackView = (RelativeLayout) findViewById(R.id.left_layout);
        mRandomOne = (TextView) findViewById(R.id.random_select_1);
        mContinuePick = (TextView) findViewById(R.id.continue_pick);
        mClearList = (TextView) findViewById(R.id.tv_ssq_clearlist);
        mSubmitGroupBuy = (TextView) findViewById(R.id.sumbit_group_buy);

        footerView = LayoutInflater.from(this).inflate(R.layout.pick_footer_view, null);

        mSubmitDaigou = (TextView)findViewById(R.id.right_footer_btn);
        mFootLeftBtn = (TextView)findViewById(R.id.left_footer_btn);

        price = (TextView) findViewById(R.id.price_2);
        stakes = (TextView) findViewById(R.id.bet_txt_1);
        issue = (TextView) findViewById(R.id.bet_txt_5);
        times = (TextView) findViewById(R.id.bet_txt_3);

        tipsLayout = (RelativeLayout) findViewById(R.id.tips);

        mTraceTime = (EditText)findViewById(R.id.times_edit);
        mTraceTime.setInputType(InputType.TYPE_CLASS_NUMBER);
        mTraceIssue = (EditText)findViewById(R.id.issue_edit);
        mTraceIssue.setInputType(InputType.TYPE_CLASS_NUMBER);
        mTimeSub = (ImageView)findViewById(R.id.sub_times);
        mTimeAdd = (ImageView)findViewById(R.id.add_times);
        mIssueSub = (ImageView)findViewById(R.id.sub_issue);
        mIssueAdd = (ImageView)findViewById(R.id.add_issue);

        mSmartFollowPB = (CircularProgress)findViewById(R.id.progress);
        setZhushuValue();

        mZhuiCheckbox = (CheckBox)findViewById(R.id.zhui_checkbox);

        myDialog = new Dialog(this, R.style.Theme_dialog);
        View view = LayoutInflater.from(getApplicationContext()).inflate(
                R.layout.dialog_submit_bet, null);
        TextView dialogTitle = (TextView) view
                .findViewById(R.id.submit_dialog_title);
        dialogTitle.setText("提示");
        TextView dialogContent = (TextView) view
                .findViewById(R.id.submit_dialog_content);
        dialogContent.setText("确定删除该选号?");
        dialogCancel = (TextView) view.findViewById(R.id.tv_submit_cancel);
        dialogOK = (TextView) view.findViewById(R.id.tv_submit_ok);
        dialogOK.setText("确定");
        myDialog.setContentView(view);
        Window dialogWindow = myDialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = 250 * (int)getResources().getDisplayMetrics().density ; // 宽度
        dialogWindow.setAttributes(lp);
    }

    private void _setListener() {
        mRandomOne.setOnClickListener(this);
        mBackView.setOnClickListener(this);
        mContinuePick.setOnClickListener(this);
        dialogCancel.setOnClickListener(this);
        mClearList.setOnClickListener(this);
        mSubmitGroupBuy.setOnClickListener(this);
        mFootLeftBtn.setOnClickListener(this);
        mSubmitDaigou.setOnClickListener(this);

        mTraceTime.addTextChangedListener(new MyTimeWatcher());
        mTraceIssue.addTextChangedListener(new MyIssueWatcher());
        mTimeAdd.setOnClickListener(this);
        mTimeSub.setOnClickListener(this);
        mIssueAdd.setOnClickListener(this);
        mIssueSub.setOnClickListener(this);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i >= mBallList.size())
                {
                    //TODO 解释用户购彩服务协议
                    return;
                }

                memoryPosition = i;

                Intent continuePick = new Intent();
                continuePick.setAction(IntentAction.RETRYPICKBALL);
                continuePick.putExtra(Syxw.CITY, mCity);
                continuePick.putExtra(IntentData.BET_TYPE, mTypeList.get(i));
                continuePick.putExtra(IntentData.SELECT_NUMBERS, selectedNumberList.get(i));
                if(mCity.equals(City.chongqing.toString())) {
                    continuePick.setClass(SscPick.this, SSC_CQ.class);
                } else {
                    continuePick.setClass(SscPick.this, SSC_JX.class);
                }
                startActivityForResult(continuePick, REPICK);
            }
        });
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.sub_issue:
                if(mZhuiHaoNums - 1 >= MINZHUIHAO && mZhuiHaoNums - 1 <= MAXZHUIHAO ) {
                    mTraceIssue.setText((mZhuiHaoNums - 1) + "");
                }
                break;
            case R.id.add_issue:
                if(mZhuiHaoNums + 1 >= MINZHUIHAO && mZhuiHaoNums + 1 <= MAXZHUIHAO ) {
                    mTraceIssue.setText((mZhuiHaoNums + 1) + "");
                }
                break;
            case R.id.sub_times:
                if(mBeiShu - 1 >= MINBEISHU&& mBeiShu - 1 <= MAXBEISHU ) {
                    mTraceTime.setText((mBeiShu - 1) + "");
                }
                break;
            case R.id.add_times:
                if(mBeiShu + 1 >= MINBEISHU&& mBeiShu + 1 <= MAXBEISHU ) {
                    mTraceTime.setText((mBeiShu + 1) + "");
                }
                break;
            case R.id.right_footer_btn:
                if(mCurZhuShu<1) {
                    Toast.makeText(getApplicationContext(), "请至少选择1注",
                            Toast.LENGTH_LONG).show();
                    break;
                }
                if(mUserid == 0) {
                    Intent loginIntent = new Intent(this, Login.class);
                    startActivityForResult(loginIntent, LOGINREQUESTCODE);
                } else {
                    _showDaigouDialog();
                }
                break;
            case R.id.left_footer_btn:

                if(mCurZhuShu<1) {
                    Toast.makeText(this, "请至少选择1注", Toast.LENGTH_SHORT).show();
                    break;
                }
                if(mZhuiHaoNums < 1) {
                    Toast.makeText(this, "请选择追号期数", Toast.LENGTH_SHORT).show();
                    break;
                }
                if(!isSingleBetType()) {
                    Toast.makeText(this, "您的选号包含多种玩法，暂时无法进行智能追号", Toast.LENGTH_SHORT).show();
                    break;
                }
                mSmartFollowPB.setVisibility(View.VISIBLE);
                getMaxBonus();
                break;
            case R.id.sumbit_group_buy:
                if(mCurZhuShu<1) {
                    Toast.makeText(this, "请至少选择1注", Toast.LENGTH_SHORT).show();
                    break;
                }
                int factor = 2;
                int fenshu;
                boolean isZhuihao;
                if(mZhuiHaoNums >0) {
                    //追号  此时期数=mCurQiShu
                    fenshu = factor*mCurZhuShu * mBeiShu *mZhuiHaoNums;
                    isZhuihao = true;
                } else {
                    //代购 此时期数=1
                    fenshu = factor*mCurZhuShu * mBeiShu*1;
                    isZhuihao = false;
                }
                StringBuilder itemBuilder = new StringBuilder();
                StringBuilder typeBuilder = new StringBuilder();
                StringBuilder betCountBuilder = new StringBuilder();
                for(int i=0; i<mBallList.size(); i++) {
                    String redBall = mBallList.get(i);
                    //redBall = redBall.replace(" ", ",");
                    if(i != 0) {
                        itemBuilder.append(";");
                        typeBuilder.append(";");
                        betCountBuilder.append(";");
                    }
                    itemBuilder.append(redBall);
                    typeBuilder.append(Constants.SscType.getSelectedType(mTypeList.get(i)));
                    betCountBuilder.append(mZhuList.get(i));
                }

                String nextQihao = mQihao;
                StringBuilder qhBuilder = new StringBuilder(mQihao+"");
                StringBuilder msBuilder = new StringBuilder(mBeiShu + "");
                for(int i = 1; i< mZhuiHaoNums ; i++) {
                    nextQihao = QiHaoTool.getSscNextQihao(nextQihao, perDayTermCount);
                    if(TextUtils.isEmpty(nextQihao)) {
                        Toast.makeText(this, "期号计算错误", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    qhBuilder.append(";"+nextQihao);
                    msBuilder.append(";"+mBeiShu);
                }
                UiHelper.startGroupBuy(this, mQihao, fenshu, isZhuihao, mCity, "SSC", qhBuilder.toString(),
                        msBuilder.toString(), itemBuilder.toString(), typeBuilder.toString(), betCountBuilder.toString(), mZhuiCheckbox.isChecked());
                break;
            case R.id.tv_ssq_clearlist:
                mBallList.clear();
                mTypeList.clear();
                mZhuList.clear();
                mMyAdpater.notifyDataSetChanged();
                if (mListView.getFooterViewsCount() > 0)
                {
                    mListView.removeFooterView(footerView);
                }
                setZhushuValue();
                break;
            case R.id.tv_submit_cancel:
                myDialog.dismiss();
                break;
            case R.id.left_layout:
                finish();
                break;
            case R.id.random_select_1:
                addRandomOne();
                break;
            case R.id.continue_pick:
                Intent continuePick = new Intent();
                continuePick.setAction(IntentAction.CONTINUEPICKBALL);
                continuePick.putExtra(Syxw.CITY, mCity);
                continuePick.putExtra(IntentData.BET_TYPE, mBetType);
                if(mCity.equals(City.chongqing.toString())) {
                    continuePick.setClass(this, SSC_CQ.class);
                } else {
                    continuePick.setClass(this, SSC_JX.class);
                }
                startActivityForResult(continuePick, CONTINUEPICK);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub

        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        switch (requestCode) {

            case CONTINUEPICK:
                _initData(data, -1);
                mMyAdpater.notifyDataSetChanged();
                if (mListView.getFooterViewsCount() == 0 && mBallList.size() > 0)
                {
                    mListView.addFooterView(footerView);
                }
                setZhushuValue();
                break;
            case REPICK:
                _initData(data, memoryPosition);
                mMyAdpater.notifyDataSetChanged();
                setZhushuValue();
                break;
            case LOGINREQUESTCODE:
                Log.i(TAG, "登录成功");
                sp = this.getSharedPreferences("loginStatus", Context.MODE_PRIVATE);
                mUserid = sp.getInt("userid", 0);
                mUpk = sp.getString("upk", "");
                //_postFormData();
                break;
            default:
                break;
        }
    }
    class MyTimeWatcher implements TextWatcher {

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            if(TextUtils.isEmpty(s) || s.toString().contains(".")) {
                mBeiShu = MINBEISHU;
            } else if(Integer.parseInt(s.toString()) < MINBEISHU) {
                mTraceTime.setText("" + MINBEISHU);
                mBeiShu = MINBEISHU;
            } else if(Integer.parseInt(s.toString()) > MAXBEISHU) {
                mTraceTime.setText(""+MAXBEISHU);
                Toast.makeText(SscPick.this ,"最大输入" + MAXBEISHU, Toast.LENGTH_SHORT).show();
                mBeiShu = MAXBEISHU;
            } else {
                mBeiShu = Integer.parseInt(s.toString());
            }
            mTraceTime.setSelection(mTraceTime.getText().length());
            setZhushuValue();
        }

    }
    class MyIssueWatcher implements TextWatcher {

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            if(TextUtils.isEmpty(s) || s.toString().contains(".")) {
                mZhuiHaoNums = MINZHUIHAO;
            } else if(Integer.parseInt(s.toString()) < MINZHUIHAO) {
                mTraceIssue.setText("" + MINZHUIHAO);
                mZhuiHaoNums = MINZHUIHAO;
            } else if(Integer.parseInt(s.toString()) > MAXZHUIHAO) {
                mTraceIssue.setText(""+MAXZHUIHAO);
                Toast.makeText(SscPick.this ,"最大输入" + MAXZHUIHAO, Toast.LENGTH_SHORT).show();
                mZhuiHaoNums = MAXZHUIHAO;
            } else {
                mZhuiHaoNums = Integer.parseInt(s.toString());
            }
            if(mZhuiHaoNums>1) {
                tipsLayout.setVisibility(View.VISIBLE);
                mZhuiCheckbox.setChecked(true);
            } else {
                tipsLayout.setVisibility(View.GONE);
            }
            mTraceIssue.setSelection(mTraceIssue.getText().length());
            setZhushuValue();
        }

    }
    private void setTotalInFooter() {
        int factor = 2;
        stakes.setText(String.valueOf(mCurZhuShu));
        //追号 期数=mCurQiShu; 代购 期数=1
        issue.setText(String.valueOf(mZhuiHaoNums > 0 ? mZhuiHaoNums : 1));
        times.setText(String.valueOf(mBeiShu));
        price.setText(String.valueOf(factor * mCurZhuShu * mBeiShu * (mZhuiHaoNums > 0 ? mZhuiHaoNums : 1)));
    }
    private void setZhushuValue() {
        int sum = 0;
        for(int i=0; i< mZhuList.size(); i++) {
            sum += mZhuList.get(i);
        }
        mCurZhuShu = sum;
        setTotalInFooter();
    }

    private void _showDaigouDialog() {
        int myBuy;
        int factor = 2;
        if(mZhuiHaoNums >0) {
            //追号  此时期数=mCurQiShu
            myBuy = factor*mCurZhuShu * mBeiShu *mZhuiHaoNums;
        } else {
            //代购 此时期数=1
            myBuy = factor*mCurZhuShu * mBeiShu*1;
        }
        final Dialog localDialog = new Dialog(SscPick.this, R.style.Theme_dialog);
        View view = LayoutInflater.from(getApplicationContext()).inflate(
                R.layout.dialog_submit_bet, null);
        TextView dialogTitle = (TextView) view
                .findViewById(R.id.submit_dialog_title);
        dialogTitle.setText("提示");
        TextView dialogContent = (TextView) view
                .findViewById(R.id.submit_dialog_content);
        dialogContent.setText("方案金额 " + myBuy + "元！\n请确定要投注吗？");
        TextView localCancel = (TextView) view.findViewById(R.id.tv_submit_cancel);
        TextView localOK = (TextView) view.findViewById(R.id.tv_submit_ok);
        localOK.setText("确定");
        localDialog.setContentView(view);
        Window dialogWindow = localDialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = 250 * (int)getResources().getDisplayMetrics().density; // 宽度
        dialogWindow.setAttributes(lp);
        localOK.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                localDialog.cancel();
                _postFormData();
            }
        });
        localCancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                localDialog.cancel();
            }
        });
        localDialog.show();
    }
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        _clearBetListData();
    }

    /**
     * 判断是否是单一的彩票玩法
     */
    private boolean isSingleBetType() {
        for(int i=0; i< mTypeList.size() - 1; i++) {
            if(!mTypeList.get(i).equals(mTypeList.get(i+1))) {
                return false;
            } else if(mTypeList.get(i).equals(SscType.fourstar_fuxuan.toString())
                    || mTypeList.get(i).equals(SscType.threestar_fuxuan.toString())
                    || mTypeList.get(i).equals(SscType.twostar_fuxuan.toString())
                    || mTypeList.get(i).equals(SscType.fivestar_fuxuan.toString())) {
                return false;
            }
        }
        if(mTypeList.get(mTypeList.size() - 1).equals(SscType.fourstar_fuxuan.toString())
                || mTypeList.get(mTypeList.size() - 1).equals(SscType.threestar_fuxuan.toString())
                || mTypeList.get(mTypeList.size() - 1).equals(SscType.twostar_fuxuan.toString())
                || mTypeList.get(mTypeList.size() - 1).equals(SscType.fivestar_fuxuan.toString())) {
            return false;
        }
        return true;
    }
    private void getMaxBonus(){
        SafeAsyncTask<Boolean> getAllDaigouTask = new SafeAsyncTask<Boolean>() {
            private int[]result;
            @Override
            public  Boolean call() throws Exception {
                result = BeiTouGenerator.getSscBonus(mBallList, mBetType, mCity);
                return true;
            }

            @Override
            protected void onSuccess(Boolean t) throws Exception {
                super.onSuccess(t);
                int zhushu = 0;
                for(int i=0; i< mZhuList.size(); i++) {
                    zhushu += mZhuList.get(i);
                }
                StringBuilder itemBuilder = new StringBuilder();
                StringBuilder typeBuilder = new StringBuilder();
                StringBuilder betCountBuilder = new StringBuilder();
                for(int i=0; i<mBallList.size(); i++) {
                    String redBall = mBallList.get(i);
                    //redBall = redBall.replace(" ", ",");
                    if(i != 0) {
                        itemBuilder.append(";");
                        typeBuilder.append(";");
                        betCountBuilder.append(";");
                    }
                    itemBuilder.append(redBall);
                    typeBuilder.append(Constants.SscType.getSelectedType(mTypeList.get(i)));
                    betCountBuilder.append(mZhuList.get(i));
                }
                UiHelper.startSmartFollow(SscPick.this, mZhuiHaoNums, mBeiShu,zhushu, mTypeList.get(0).toString(), mCity, "SSC", result[1], result[0],
                        itemBuilder.toString(), typeBuilder.toString(), betCountBuilder.toString(), mZhuiCheckbox.isChecked()
                );
                mSmartFollowPB.setVisibility(View.GONE);
            }

            @Override
            protected void onThrowable(Throwable t) {
                super.onThrowable(t);
            }

            @Override
            protected void onFinally() {
                super.onFinally();


            }

        };
        getAllDaigouTask.execute();
    }

    public void deleteItemAtIndex(int position)
    {
        mBallList.remove(position);
        mTypeList.remove(position);
        mZhuList.remove(position);

        mMyAdpater.notifyDataSetChanged();
        if (mListView.getFooterViewsCount() > 0 && mBallList.size() == 0)
        {
            mListView.removeFooterView(footerView);
        }
        setZhushuValue();
    }

    private void _postFormData() {
        _showTouzhuProgress();

        RequestParams rawParams = new RequestParams();

        rawParams.add("currentQihao", mQihao);
        if(mZhuiHaoNums <= 0) {
            rawParams.add("isZhuihao", "false");
            rawParams.add("autoStopZhuihao", "true");
        } else {
            rawParams.add("isZhuihao", "true");
            if(mZhuiCheckbox.isChecked()) {
                rawParams.add("autoStopZhuihao", "true");
            } else {
                rawParams.add("autoStopZhuihao", "false");
            }
        }
        StringBuilder itemBuilder = new StringBuilder();
        StringBuilder typeBuilder = new StringBuilder();
        StringBuilder betCountBuilder = new StringBuilder();
        for(int i=0; i<mBallList.size(); i++) {
            String redBall = mBallList.get(i);
            redBall = redBall.replace(" | ", "|");
            if(i != 0) {
                itemBuilder.append(";");
                typeBuilder.append(";");
                betCountBuilder.append(";");
            }
            itemBuilder.append(redBall);
            typeBuilder.append(Constants.SscType.getSelectedType(mTypeList.get(i)));
            betCountBuilder.append(mZhuList.get(i));
        }
        rawParams.add("itemsContent", itemBuilder.toString());
        rawParams.add("itemsSelectType", typeBuilder.toString());
        rawParams.add("itemsBetCount", betCountBuilder.toString());
        // rawParams.add("multiple", mBeiShu + "");

        String nextQihao = mQihao;
        StringBuilder qhBuilder = new StringBuilder(mQihao+"");
        StringBuilder msBuilder = new StringBuilder(mBeiShu + "");
        for(int i = 1; i< mZhuiHaoNums ; i++) {
            nextQihao = QiHaoTool.getSscNextQihao(nextQihao, perDayTermCount);
            if(TextUtils.isEmpty(nextQihao)) {
                Toast.makeText(this, "期号计算错误", Toast.LENGTH_SHORT).show();
                return;
            }
            qhBuilder.append(";"+nextQihao);
            msBuilder.append(";"+mBeiShu);
        }
        Log.d(TAG, "qihaos = " + qhBuilder.toString());
        rawParams.add("qihaos", qhBuilder.toString());
        rawParams.add("multiples", msBuilder.toString());


        rawParams.add("shoptype", "Daigou");

        rawParams.add("type", lotterytype);
        rawParams.add("zhuihaoTitle", "SSC");
        rawParams.add("userid", String.valueOf(mUserid));


        RequestParams paramsUrl = new RequestParams();
        paramsUrl.add("act", "buy");
        paramsUrl.add("lotterytype", lotterytype);
        paramsUrl.add("datatype", "json");
        paramsUrl.add("userid", String.valueOf(mUserid));
        paramsUrl.add("upk", mUpk);
        paramsUrl.add("jsoncallback","jsonp" + String.valueOf(System.currentTimeMillis()));
        paramsUrl.add("_", String.valueOf(System.currentTimeMillis()));
        String url = AsyncHttpClient.getUrlWithQueryString(false,
                JinCaiZiHttpClient.BASE_URL, paramsUrl);
        JinCaiZiHttpClient.closeExpireConnection();
        JinCaiZiHttpClient.postFormData(this,  url, rawParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  byte[] responseBody) {
                try {
                    String charset;
                    if(Utils.isCmwapNet(SscPick.this)) {
                        charset = "utf-8";
                    } else {
                        charset = "gb2312";
                    }
                    String jsonData = new String(responseBody, charset);
                    Log.d(TAG, "post ssq form data = " + jsonData);
                    parseTouzhuJson(jsonData);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Toast.makeText(SscPick.this, "数据可能异常，请检查数据请求是否成功，以免重复请求造成损失", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(SscPick.this, "数据可能异常，请检查数据请求是否成功，以免重复请求造成损失", Toast.LENGTH_LONG).show();
                } finally {
                    // FIXME reader.close should be here
                    final Dialog localDialog = new Dialog(SscPick.this, R.style.Theme_dialog);
                    View view = LayoutInflater.from(getApplicationContext()).inflate(
                            R.layout.dialog_submit_bet, null);
                    TextView dialogTitle = (TextView) view
                            .findViewById(R.id.submit_dialog_title);
                    dialogTitle.setText("提示");
                    TextView dialogContent = (TextView) view
                            .findViewById(R.id.submit_dialog_content);
                    TextView localCancel = (TextView) view.findViewById(R.id.tv_submit_cancel);
                    localCancel.setVisibility(View.GONE);
                    TextView localOK = (TextView) view.findViewById(R.id.tv_submit_ok);
                    localOK.setText("确定");
                    localDialog.setContentView(view);
                    Window dialogWindow = localDialog.getWindow();
                    WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                    lp.width = 250 * (int)getResources().getDisplayMetrics().density; // 宽度
                    dialogWindow.setAttributes(lp);
                    localOK.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            localDialog.cancel();
                            if(!TextUtils.isEmpty(successR) && successR.equals("success")) {
                                Intent mSsqIntent = new Intent();
                                mSsqIntent.putExtra(Syxw.CITY, mCity);
                                mSsqIntent.setAction(IntentAction.FIRSTPICKBALL);
                                if(mCity.equals(Constants.City.jiangxi.toString())) {
                                    mSsqIntent.setClass(SscPick.this, SSC_JX.class);
                                } else {
                                    mSsqIntent.setClass(SscPick.this, SSC_CQ.class);
                                }
                                startActivity(mSsqIntent);
                                finish();
                            } else if(!TextUtils.isEmpty(responseMsg) && responseMsg.equals("未登录或登录过期！")) {
                                CheckLogin.clearLoginStatus(sp);
                                Intent loginIntent = new Intent(SscPick.this, Login.class);
                                startActivityForResult(loginIntent, LOGINREQUESTCODE);
                            }

                        }
                    });
                    if(!TextUtils.isEmpty(successR) && successR.equals("success")) {
                        dialogContent.setText(responseMsg + "\n" + "当前余额 " + balance + "元");

                    } else {
                        dialogContent.setText(responseMsg);
                    }
                    _hideTouzhuProgress();
                    localDialog.show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  byte[] responseBody, Throwable error) {
                _hideTouzhuProgress();
                responseMsg = "投注失败, 请重试！";
                Toast.makeText(SscPick.this, responseMsg, Toast.LENGTH_SHORT).show();
            }
        });

    }
    private String responseMsg = "投注失败！";
    private String successR = "";
    private String balance = "";
    private JinCaiZiProgressDialog mProgressDialog;
    private void parseTouzhuJson(String jsonData) throws IOException {
        JsonReader reader = new JsonReader(new StringReader(jsonData));
        reader.beginObject();

        while(reader.hasNext()) {
            String tagName = reader.nextName();
            if(tagName.equals("msg")) {
                responseMsg  = reader.nextString();
            } else if(tagName.equals("orderId")){
                reader.nextInt();
            } else if(tagName.equals("balance")) {
                balance = reader.nextString();
            } else if(tagName.equals("redirect")) {
                reader.nextInt();
            } else if(tagName.equals("result")) {
                successR = reader.nextString();
            }
        }
        reader.endObject();
        reader.close();

    }
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        mUserid = sp.getInt("userid", 0);
        mUpk = sp.getString("upk", "");
    }
    protected void _showTouzhuProgress() {
        mProgressDialog = JinCaiZiProgressDialog.show(this, "正在提交，请稍等......");
    }

    private void _hideTouzhuProgress() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    private void addRandomOne()
    {
        HashMap<String, ArrayList<Boolean>> tempMap = new HashMap<String, ArrayList<Boolean>>();

        if (mBetType.equals(SscType.fivestar_zhixuan.toString()) ||
                mBetType.equals(SscType.fivestar_fuxuan.toString()) ||
                mBetType.equals(SscType.fivestar_tongxuan.toString()))
        {
            int wan = new Random().nextInt(10);
            int qian = new Random().nextInt(10);
            int bai = new Random().nextInt(10);
            int shi = new Random().nextInt(10);
            int ge = new Random().nextInt(10);

            tempMap.put(IntentData.WAN, getResultList(10, wan));
            tempMap.put(IntentData.QIAN, getResultList(10, qian));
            tempMap.put(IntentData.BAI, getResultList(10, bai));
            tempMap.put(IntentData.SHI, getResultList(10, shi));
            tempMap.put(IntentData.GE, getResultList(10, ge));

            String ballString = String.valueOf(wan) + " | " + String.valueOf(qian) + " | " +
                    String.valueOf(bai) + " | " + String.valueOf(shi) + " | " + String.valueOf(ge);

            selectedNumberList.addFirst(tempMap);
            mBallList.addFirst(ballString);

            int zhushu = 1;
            if (mBetType.equals(SscType.fivestar_fuxuan.toString()))
            {
                if (lotterytype.equals("JXSSC")) {
                    zhushu = 5;
                }
                else if (lotterytype.equals("CQSSC"))
                {
                    zhushu = 4;
                }
            }
            mZhuList.addFirst(zhushu);
        }
        else if(mBetType.equals(SscType.twostar_fuxuan.toString())
                ||mBetType.equals(SscType.twostar_zhixuan.toString())
                ||mBetType.equals(SscType.twostar_zuxuan.toString()))
        {
            int shi = new Random().nextInt(10);
            int ge = new Random().nextInt(10);

            tempMap.put(IntentData.SHI, getResultList(10, shi));
            tempMap.put(IntentData.GE, getResultList(10, ge));

            String ballString = String.valueOf(shi) + " | " + String.valueOf(ge);

            selectedNumberList.addFirst(tempMap);
            mBallList.addFirst(ballString);
            mZhuList.addFirst(1);
        }
        else if(mBetType.equals(SscType.twostar_zhixuan_hezhi.toString())) 
        {
            int ge = new Random().nextInt(19);

            tempMap.put(IntentData.GE, getResultList(19, ge));

            String ballString = String.valueOf(ge);

            selectedNumberList.addFirst(tempMap);
            mBallList.addFirst(ballString);
            mZhuList.addFirst(_computeTwoStarZhiHZ(ge));
        }
        else if(mBetType.equals(SscType.twostar_zuxuan_hezhi.toString()))
        {
            int ge = new Random().nextInt(17);

            tempMap.put(IntentData.GE, getResultList(17, ge));

            String ballString = String.valueOf(ge + 1);

            selectedNumberList.addFirst(tempMap);
            mBallList.addFirst(ballString);
            mZhuList.addFirst(_computeTwoStarZuHZ(ge));
        }
        else if(mBetType.equals(SscType.threestar_zhixuan_hezhi.toString()))
        {
            int ge = new Random().nextInt(28);

            tempMap.put(IntentData.GE, getResultList(28, ge));

            String ballString = String.valueOf(ge);

            selectedNumberList.addFirst(tempMap);
            mBallList.addFirst(ballString);
            mZhuList.addFirst(zushuOfZhixuanHezhi[ge]);
        }
        else if(mBetType.equals(SscType.threestar_zusan_hezhi.toString()))
        {
            int ge = new Random().nextInt(26);

            tempMap.put(IntentData.GE, getResultList(26, ge));

            String ballString = String.valueOf(ge + 1);

            selectedNumberList.addFirst(tempMap);
            mBallList.addFirst(ballString);
            mZhuList.addFirst(zushuOfZusanHezhi[ge]);
        }
        else if(mBetType.equals(SscType.threestar_zuliu_hezhi.toString()))
        {
            int ge = new Random().nextInt(22);

            tempMap.put(IntentData.GE, getResultList(22, ge));

            String ballString = String.valueOf(ge + 3);

            selectedNumberList.addFirst(tempMap);
            mBallList.addFirst(ballString);
            mZhuList.addFirst(zushuOfZuliuHezhi[ge]);
        }
        else if(mBetType.equals(SscType.onestar_zhixuan.toString()))
        {
            int ge = new Random().nextInt(10);

            tempMap.put(IntentData.GE, getResultList(10, ge));

            String ballString = String.valueOf(ge);

            selectedNumberList.addFirst(tempMap);
            mBallList.addFirst(ballString);
            mZhuList.addFirst(1);
        }
        else if (mBetType.equals(SscType.fourstar_fuxuan.toString()) ||
                mBetType.equals(SscType.fourstar_zhixuan.toString()))
        {
            int qian = new Random().nextInt(10);
            int bai = new Random().nextInt(10);
            int shi = new Random().nextInt(10);
            int ge = new Random().nextInt(10);

            tempMap.put(IntentData.QIAN, getResultList(10, qian));
            tempMap.put(IntentData.BAI, getResultList(10, bai));
            tempMap.put(IntentData.SHI, getResultList(10, shi));
            tempMap.put(IntentData.GE, getResultList(10, ge));

            String ballString = String.valueOf(qian) + " | " + String.valueOf(bai) + " | " +
                    String.valueOf(shi) + " | " + String.valueOf(ge);

            selectedNumberList.addFirst(tempMap);
            mBallList.addFirst(ballString);
            mZhuList.addFirst(mBetType.equals(SscType.fourstar_fuxuan.toString())?4:1);
        }
        else if (mBetType.equals(SscType.twostar_zuxuan_baohao.toString()) ||
                mBetType.equals(SscType.threestar_zusan_baohao.toString()))
        {
            ArrayList<Integer> list = getBaoHaoSelection(2);

            ArrayList<Boolean> tempList = new ArrayList<Boolean>();
            for (int j = 0; j < 10; j++) {
                tempList.add(list.contains(j));
            }

            tempMap.put(IntentData.GE, tempList);

            String ballString = String.valueOf(list.get(0)) + " " +
                    String.valueOf(list.get(1));

            selectedNumberList.addFirst(tempMap);
            mBallList.addFirst(ballString);
            mZhuList.addFirst(1);
        }
        else if (mBetType.equals(SscType.threestar_zuliu_baohao.toString()))
        {
            ArrayList<Integer> list = getBaoHaoSelection(3);

            ArrayList<Boolean> tempList = new ArrayList<Boolean>();
            for (int j = 0; j < 10; j++) {
                tempList.add(list.contains(j));
            }

            tempMap.put(IntentData.GE, tempList);

            String ballString = String.valueOf(list.get(0)) + " " +
                    String.valueOf(list.get(1)) + " " + String.valueOf(list.get(2));

            selectedNumberList.addFirst(tempMap);
            mBallList.addFirst(ballString);
            mZhuList.addFirst(1);
        }
        else if(mBetType.equals(SscType.dxds.toString())) {
            int shi = new Random().nextInt(4);
            int ge = new Random().nextInt(4);

            tempMap.put(IntentData.SHI, getResultList(4, shi));
            tempMap.put(IntentData.GE, getResultList(4, ge));

            String ballString = StringUtil.getDXDSString(shi) + " | " + StringUtil.getDXDSString(ge);

            selectedNumberList.addFirst(tempMap);
            mBallList.addFirst(ballString);
            mZhuList.addFirst(1);
        }
        else if (mBetType.equals(SscType.renxuan_two.toString()))
        {
            int[] result = SscRandom.getRenXuanTwo();
            StringBuilder builder = new StringBuilder();

            for (int i = 0; i < 5; i++) {
                if (result[0] == i)
                {
                    tempMap.put(getTagByIndex(i), getResultList(10, result[2]));
                    builder.append(String.valueOf(result[2]));
                    builder.append((i == 4)?"":" | ");
                }
                else if (result[1] == i)
                {
                    tempMap.put(getTagByIndex(i), getResultList(10, result[3]));
                    builder.append(String.valueOf(result[3]));
                    builder.append((i == 4)?"":" | ");
                }
                else
                {
                    builder.append((i == 4)?"#":"# | ");
                }
            }
            selectedNumberList.addFirst(tempMap);
            mBallList.addFirst(builder.toString().trim());
            mZhuList.addFirst(1);
        }
        else if (mBetType.equals(SscType.renxuan_one.toString()))
        {
            int[] result = SscRandom.getRenXuanOne();
            StringBuilder builder = new StringBuilder();

            for (int i = 0; i < 5; i++) {
                if (result[0] == i)
                {
                    tempMap.put(getTagByIndex(i), getResultList(10, result[1]));
                    builder.append(String.valueOf(result[1]));
                    builder.append(" | ");
                }
                else
                {
                    builder.append((i == 4)?"#":"# | ");
                }
            }
            selectedNumberList.addFirst(tempMap);
            mBallList.addFirst(builder.toString().trim());
            mZhuList.addFirst(1);

        } else
        {
            int bai = new Random().nextInt(10);
            int shi = new Random().nextInt(10);
            int ge = new Random().nextInt(10);

            tempMap.put(IntentData.BAI, getResultList(10, bai));
            tempMap.put(IntentData.SHI, getResultList(10, shi));
            tempMap.put(IntentData.GE, getResultList(10, ge));

            String ballString = String.valueOf(bai) + " | " + String.valueOf(shi)
                    + " | " + String.valueOf(ge);

            selectedNumberList.addFirst(tempMap);
            mBallList.addFirst(ballString);
            mZhuList.addFirst(mBetType.equals(SscType.threestar_fuxuan.toString())?3:1);
        }

        mTypeList.addFirst(mBetType);
        if (mListView.getFooterViewsCount() == 0 && mBallList.size() > 0)
        {
            mListView.addFooterView(footerView);
        }

        mMyAdpater.notifyDataSetChanged();
        setZhushuValue();
    }

    private ArrayList<Boolean> getResultList(int length, int random)
    {
        ArrayList<Boolean> tempList = new ArrayList<Boolean>();
        for (int j = 0; j < length; j++) {
            tempList.add(random == j);
        }

        return tempList;
    }

    private int _computeTwoStarZhiHZ(int sum) {
        int count = 0;
        for(int i=0; i<10; i++) {
            for(int j=0; j<10; j++) {
                if(i+j==sum) {
                    count++;
                }
            }
        }
        return count;
    }
    private int _computeTwoStarZuHZ(int sum) {
        int count = 0;
        for(int i=0; i<10; i++) {
            for(int j=0; j<10; j++) {
                if(i+j==sum && i<j) {
                    count++;
                }
            }
        }
        return count;
    }
    private ArrayList<Integer> getBaoHaoSelection(int selectCount) {
        // 2、创建生成随即数的对象
        Random random = new Random();
        // 3、创建空白数组，用于存放红球
        final int[] pool = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        ArrayList<Integer> result = new ArrayList<Integer>();
        // 4、从pool中随即产生红球
        int i = 0;
        while (i < selectCount) {
            int index = random.nextInt(10);
            if (result.contains(pool[index])) {
                continue;
            }
            result.add(pool[index]);
            i++;
        }

        Collections.sort(result);

        return result;
    }

    private String getTagByIndex(int index)
    {
        switch (index)
        {
            case 0:
                return IntentData.WAN;
            case 1:
                return IntentData.QIAN;
            case 2:
                return IntentData.BAI;
            case 3:
                return IntentData.SHI;
            case 4:
                return IntentData.GE;
            default:
                return "";
        }
    }
}
