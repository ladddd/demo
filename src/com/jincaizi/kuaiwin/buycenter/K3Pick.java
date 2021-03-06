package com.jincaizi.kuaiwin.buycenter;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import android.text.InputType;
import android.view.*;
import com.jincaizi.common.IntentData;
import com.jincaizi.common.NumberUtil;
import com.jincaizi.common.StringUtil;
import com.jincaizi.kuaiwin.chart.views.CircularProgress;
import org.apache.http.Header;

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
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.stream.JsonReader;
import com.jincaizi.adapters.K3TouZhuAdapter;
import com.jincaizi.R;
import com.jincaizi.http.JinCaiZiHttpClient;
import com.jincaizi.kuaiwin.mylottery.Login;
import com.jincaizi.kuaiwin.tool.BeiTouGenerator;
import com.jincaizi.kuaiwin.tool.CheckLogin;
import com.jincaizi.kuaiwin.tool.QiHaoTool;
import com.jincaizi.kuaiwin.utils.Constants;
import com.jincaizi.kuaiwin.utils.IntentAction;
import com.jincaizi.kuaiwin.utils.SafeAsyncTask;
import com.jincaizi.kuaiwin.utils.UiHelper;
import com.jincaizi.kuaiwin.utils.Utils;
import com.jincaizi.kuaiwin.widget.JinCaiZiProgressDialog;
import com.jincaizi.vendor.http.AsyncHttpClient;
import com.jincaizi.vendor.http.AsyncHttpResponseHandler;
import com.jincaizi.vendor.http.RequestParams;

public class K3Pick extends Activity implements OnClickListener, AdapterView.OnItemClickListener{
    public static final String BALL = "redBall";
    public static final String BETTYPE = "betType";
    public static final String CITY = "city";
    public static final String ZHUAMOUNT = "zhushuamount";
    public static final int LOGINREQUESTCODE = 2;
    public static final int MAXBEISHU = 9999;
    public static final int MAXZHUIHAO = 200;
    public static final int MINBEISHU = 1;
    public static final int MINZHUIHAO = 1;
    public static final int CONTINUEPICK = 0;
    public static final int REPICK = 1;
    protected static final String TAG = null;

    private ListView mListView;
    private RelativeLayout mBackView;
    private RelativeLayout mRandomOne;
    private RelativeLayout backToSelect;
    private RelativeLayout mClearList;
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
    private LinkedList<Integer> mZhuList = new LinkedList<Integer>();
    private String mBetType;
    private String mBall;
    private int mZhuiHaoNums = 1;
    private int mBeiShu = 1;
    private int mUserid;
    private String mUpk;
    private SharedPreferences sp;
    private K3TouZhuAdapter mMyAdpater;
    //    private TextView totalInFooter;
    private String mQihao = "";
    private int perDayTermCount;
    private String lotterytype;
    private CheckBox mZhuiCheckbox;
    private int mCurZhuShu;
    private String currentBetType;

    //元
    private TextView price;
    //注
    private TextView stakes;
    //期
    private TextView issue;
    //倍
    private TextView times;

    private RelativeLayout tipsLayout;

    private LinkedList<ArrayList<Integer>> selectedNumberList = new LinkedList<ArrayList<Integer>>();

    private int memoryPosition;

    private View footerView;

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
        mMyAdpater = new K3TouZhuAdapter(this, mBallList, mTypeList, mZhuList, true);
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

        setZhushuValue();
    }
    private void _getTouZhuAttribute() {
        if(mCity.equals(Constants.City.jiangsu.toString())) {
            lotterytype = "JSK3";
            perDayTermCount = 82;
        } else if(mCity.equals(Constants.City.anhui.toString())) {
            lotterytype = "AHK3";
            perDayTermCount = 80;
        }else if(mCity.equals(Constants.City.neimenggu.toString())) {
            lotterytype = "NMGK3";
            perDayTermCount = 73;
        }
    }
    private void _initData(Intent intent, int memoryPostion) {
        mCity = intent.getStringExtra(Syxw.CITY);
        mBetType = intent.getStringExtra(BETTYPE);
        mBall = intent.getStringExtra(BALL);
        mQihao = intent.getStringExtra("qihao");
        ArrayList<Integer> selectedNumbers =
                (ArrayList<Integer>)intent.getSerializableExtra(IntentData.SELECT_NUMBERS);
        int zhushuAmount = intent.getIntExtra(ZHUAMOUNT, 1);
        String[] types = mBetType.split(" ");
        String[] results = mBall.split("-");
        currentBetType = types[0];
        if (memoryPostion == -1) {
            if(types.length > 1) {
                mTypeList.add(0, types[0]);
                mTypeList.add(0, types[1]);
                mBallList.add(0, results[0]);
                mBallList.add(0, results[1]);
                mZhuList.add(0, zhushuAmount - results[1].split(" ").length);
                mZhuList.add(0, results[1].split(" ").length);

                if (selectedNumbers != null) {
                    ArrayList<Integer> second = new ArrayList<Integer>();
                    if (types[0].equals(Constants.K3Type.twosamesingle.toString()))
                    {
                        for (int i = selectedNumbers.size()-1; i >= 0; i--) {
                            if (selectedNumbers.get(i) > 11)
                            {
                                second.add(selectedNumbers.get(i));
                                selectedNumbers.remove(i);
                            }
                        }
                    }
                    else {
                        second.add(selectedNumbers.get(selectedNumbers.size() - 1));
                        selectedNumbers.remove(selectedNumbers.size() - 1);
                    }
                    selectedNumberList.addFirst(selectedNumbers);
                    selectedNumberList.addFirst(second);
                }
            } else {
                mTypeList.add(types[0]);
                mBallList.add(results[0]);
                mZhuList.add(zhushuAmount);

                if (selectedNumbers != null)
                {
                    selectedNumberList.addFirst(selectedNumbers);
                }
            }
        } else {
            if(types.length > 1)
            {
                mBallList.set(memoryPostion, results[1]);
                mTypeList.set(memoryPostion, types[1]);
                mZhuList.set(memoryPostion, results[1].split(" ").length);
                mBallList.add(memoryPostion, results[0]);
                mTypeList.add(memoryPostion, types[0]);
                mZhuList.add(memoryPostion, zhushuAmount - results[1].split(" ").length);

                if (selectedNumbers != null) {
                    ArrayList<Integer> second = new ArrayList<Integer>();
                    if (types[0].equals(Constants.K3Type.twosamesingle.toString()))
                    {
                        for (int i = selectedNumbers.size()-1; i >= 0; i--) {
                            if (selectedNumbers.get(i) > 11)
                            {
                                second.add(selectedNumbers.get(i));
                                selectedNumbers.remove(i);
                            }
                        }
                    }
                    else {
                        second.add(selectedNumbers.get(selectedNumbers.size() - 1));
                        selectedNumbers.remove(selectedNumbers.size() - 1);
                    }
                    selectedNumberList.set(memoryPostion, second);
                    selectedNumberList.add(memoryPostion, selectedNumbers);
                }
            }
            else
            {
                mBallList.set(memoryPostion, results[0]);
                mTypeList.set(memoryPostion, types[0]);
                mZhuList.set(memoryPostion, zhushuAmount);
                if (selectedNumbers != null) {
                    selectedNumberList.set(memoryPostion, selectedNumbers);
                }
            }
        }

    }
    private void _findViews() {
        ((TextView) findViewById(R.id.current_lottery)).setText(Constants.City.getCityName(mCity)+"-快3投注");
        mListView = (ListView) findViewById(R.id.touzhu_detail_list);
        TextView tv = (TextView) findViewById(R.id.empty_list_view);
        mListView.setEmptyView(tv);
        mBackView = (RelativeLayout) findViewById(R.id.left_layout);
        mRandomOne = (RelativeLayout) findViewById(R.id.random_layout);
        backToSelect = (RelativeLayout) findViewById(R.id.select_layout);
        mClearList = (RelativeLayout) findViewById(R.id.clear_layout);
        mSubmitGroupBuy = (TextView) findViewById(R.id.sumbit_group_buy);

        mSubmitDaigou = (TextView)findViewById(R.id.right_footer_btn);
        mFootLeftBtn = (TextView)findViewById(R.id.left_footer_btn);
        mZhuiCheckbox = (CheckBox)findViewById(R.id.tips_checkbox);

        footerView = LayoutInflater.from(this).inflate(R.layout.pick_footer_view, null);

        mTraceTime = (EditText)findViewById(R.id.times_edit);
        mTraceTime.setInputType(InputType.TYPE_CLASS_NUMBER);
        mTraceIssue = (EditText)findViewById(R.id.issue_edit);
        mTraceIssue.setInputType(InputType.TYPE_CLASS_NUMBER);
        mTimeSub = (ImageView)findViewById(R.id.sub_times);
        mTimeAdd = (ImageView)findViewById(R.id.add_times);
        mIssueSub = (ImageView)findViewById(R.id.sub_issue);
        mIssueAdd = (ImageView)findViewById(R.id.add_issue);

        price = (TextView) findViewById(R.id.price_2);
        stakes = (TextView) findViewById(R.id.bet_txt_1);
        issue = (TextView) findViewById(R.id.bet_txt_5);
        times = (TextView) findViewById(R.id.bet_txt_3);

        tipsLayout = (RelativeLayout) findViewById(R.id.tips);

        mSmartFollowPB = (CircularProgress)findViewById(R.id.progress);
        setZhushuValue();

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
        lp.width = 300; // 宽度
        dialogWindow.setAttributes(lp);
    }

    private void _setListener() {
        mRandomOne.setOnClickListener(this);
        mBackView.setOnClickListener(this);
        backToSelect.setOnClickListener(this);
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

        mListView.setOnItemClickListener(this);
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

                //mSmartFollowPB.setVisibility(View.VISIBLE);
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
                    redBall = redBall.replace(" ", ",");
                    if(i != 0) {
                        itemBuilder.append(";");
                        typeBuilder.append(";");
                        betCountBuilder.append(";");
                    }
                    itemBuilder.append(redBall);
                    typeBuilder.append(Constants.K3Type.getSelectedType(mTypeList.get(i)));
                    betCountBuilder.append(mZhuList.get(i));
                }

                String nextQihao = mQihao;
                StringBuilder qhBuilder = new StringBuilder(mQihao+"");
                StringBuilder msBuilder = new StringBuilder(mBeiShu + "");
                for(int i = 1; i< mZhuiHaoNums ; i++) {
                    nextQihao = QiHaoTool.getK3NextQihao(nextQihao, perDayTermCount);
                    if(TextUtils.isEmpty(nextQihao)) {
                        Toast.makeText(this, "期号计算错误", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    qhBuilder.append(";"+nextQihao);
                    msBuilder.append(";"+mBeiShu);
                }
                UiHelper.startGroupBuy(this, mQihao, fenshu, isZhuihao, mCity, "K3", qhBuilder.toString(),
                        msBuilder.toString(), itemBuilder.toString(), typeBuilder.toString(), betCountBuilder.toString(), mZhuiCheckbox.isChecked());
                break;
            case R.id.clear_layout:
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
            case R.id.select_layout:
                Intent continuePick = new Intent(this, K3.class);
                continuePick.setAction(IntentAction.CONTINUEPICKBALL);
                continuePick.putExtra(Syxw.CITY, mCity);
                continuePick.putExtra(IntentData.BET_TYPE, currentBetType);
                startActivityForResult(continuePick, CONTINUEPICK);
                break;
            case R.id.random_layout:
                addOneRandom();
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
            case REPICK:
                //修改原先item
                _initData(data, memoryPosition);
                mMyAdpater.notifyDataSetChanged();
                setZhushuValue();
                break;
            case CONTINUEPICK:
                _initData(data, -1);
                mMyAdpater.notifyDataSetChanged();
                if (mListView.getFooterViewsCount() == 0 && mBallList.size() > 0)
                {
                    mListView.addFooterView(footerView);
                }
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
                Toast.makeText(K3Pick.this ,"最大输入" + MAXBEISHU, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(K3Pick.this ,"最大输入" + MAXZHUIHAO, Toast.LENGTH_SHORT).show();
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
        final Dialog localDialog = new Dialog(K3Pick.this, R.style.Theme_dialog);
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
        lp.width = 300; // 宽度
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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (i >= mBallList.size())
        {
            //TODO 解释用户购彩服务协议
            return;
        }

        memoryPosition = i;

        Intent repick = new Intent(this, K3.class);
        repick.setAction(IntentAction.RETRYPICKBALL);
        repick.putExtra(IntentData.SELECT_NUMBERS, selectedNumberList.get(i));
        repick.putExtra(IntentData.BET_TYPE, mTypeList.get(i));
        repick.putExtra(Syxw.CITY, mCity);
        startActivityForResult(repick, REPICK);
    }

    /**
     * 判断是否是单一的彩票玩法
     */
    private boolean isSingleBetType() {
        for(int i=0; i< mTypeList.size() - 1; i++) {
            if(!mTypeList.get(i).equals(mTypeList.get(i+1))) {
                return false;
            }
        }
        return true;
    }
    private void getMaxBonus(){
        SafeAsyncTask<Boolean> getAllDaigouTask = new SafeAsyncTask<Boolean>() {
            private int[]result;
            @Override
            public  Boolean call() throws Exception {
                result = BeiTouGenerator.getK3Bonus(mBallList, mBetType);
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
                    redBall = redBall.replace(" ", ",");
                    if(i != 0) {
                        itemBuilder.append(";");
                        typeBuilder.append(";");
                        betCountBuilder.append(";");
                    }
                    itemBuilder.append(redBall);
                    typeBuilder.append(Constants.K3Type.getSelectedType(mTypeList.get(i)));
                    betCountBuilder.append(mZhuList.get(i));
                }
                UiHelper.startSmartFollow(K3Pick.this, mZhuiHaoNums, mBeiShu,zhushu, mTypeList.get(0).toString(), mCity, "K3", result[1], result[0],
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
            redBall = redBall.replace(" ", ",");
            if(i != 0) {
                itemBuilder.append(";");
                typeBuilder.append(";");
                betCountBuilder.append(";");
            }
            itemBuilder.append(redBall);
            typeBuilder.append(Constants.K3Type.getSelectedType(mTypeList.get(i)));
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
            nextQihao = QiHaoTool.getK3NextQihao(nextQihao, perDayTermCount);
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
        rawParams.add("zhuihaoTitle", "K3");
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
                    if(Utils.isCmwapNet(K3Pick.this)) {
                        charset = "utf-8";
                    } else {
                        charset = "gb2312";
                    }
                    String jsonData = new String(responseBody, charset);
                    Log.d(TAG, "post ssq form data = " + jsonData);
                    parseTouzhuJson(jsonData);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Toast.makeText(K3Pick.this, "数据可能异常，请检查数据请求是否成功，以免重复请求造成损失", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(K3Pick.this, "数据可能异常，请检查数据请求是否成功，以免重复请求造成损失", Toast.LENGTH_LONG).show();
                } finally {
                    // FIXME reader.close should be here
                    final Dialog localDialog = new Dialog(K3Pick.this, R.style.Theme_dialog);
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
                    lp.width = 300; // 宽度
                    dialogWindow.setAttributes(lp);
                    localOK.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            localDialog.cancel();
                            if(!TextUtils.isEmpty(successR) && successR.equals("success")) {
                                Intent mSsqIntent = new Intent(K3Pick.this, K3.class);
                                mSsqIntent.putExtra(Syxw.CITY, mCity);
                                mSsqIntent.setAction(IntentAction.FIRSTPICKBALL);
                                startActivity(mSsqIntent);
                                finish();
                            } else if(!TextUtils.isEmpty(responseMsg) && responseMsg.equals("未登录或登录过期！")) {
                                CheckLogin.clearLoginStatus(sp);
                                Intent loginIntent = new Intent(K3Pick.this, Login.class);
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
                //Log.d(TAG, "failure = " + error.toString());
                _hideTouzhuProgress();
                responseMsg = "投注失败, 请重试！";
                Toast.makeText(K3Pick.this, responseMsg, Toast.LENGTH_SHORT).show();
                //finish();
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


    public void deleteItemAtIndex(int position)
    {
        mBallList.remove(position);
        mTypeList.remove(position);
        mZhuList.remove(position);
        selectedNumberList.remove(position);
        mMyAdpater.notifyDataSetChanged();
        if (mListView.getFooterViewsCount() > 0 && mBallList.size() == 0)
        {
            mListView.removeFooterView(footerView);
        }
        setZhushuValue();
    }

    private void addOneRandom()
    {
        ArrayList<Integer> tempList = new ArrayList<Integer>();

        if (currentBetType.equals(Constants.K3Type.hezhi.toString()))
        {
            int random = NumberUtil.getSumRandom();
            tempList.add(random);
            mBallList.addFirst(StringUtil.SUM_CONTENT[random]);
            mTypeList.addFirst(currentBetType);
        }
        else if (currentBetType.equals(Constants.K3Type.threesamesingle.toString()) ||
                currentBetType.equals(Constants.K3Type.threesamedouble.toString()))
        {
            int random = new Random().nextInt(7);
            tempList.add(random);
            mBallList.add(0, StringUtil.THREE_SAME_CONTENT[random]);
            mTypeList.add(0, (random < 6)?currentBetType:
                    Constants.K3Type.threesamedouble.toString());
        }
        else if (currentBetType.equals(Constants.K3Type.twosamesingle.toString()) ||
                currentBetType.equals(Constants.K3Type.twosamedouble.toString()))
        {
            int random = new Random().nextInt(18);
            if (random > 12)
            {
                tempList.add(random);
                mBallList.addFirst(StringUtil.TWO_SAME_CONTENT[random]);
                mTypeList.addFirst(Constants.K3Type.twosamedouble.toString());
            }
            else if (random > 6)
            {
                int otherRandom = new Random().nextInt(6);
                while (otherRandom == random - 6)
                {
                    otherRandom = new Random().nextInt(6);
                }
                StringBuilder builder = new StringBuilder();
                builder.append(StringUtil.TWO_SAME_CONTENT[otherRandom]);
                builder.append("#");
                builder.append(StringUtil.TWO_SAME_CONTENT[random]);
                mBallList.addFirst(builder.toString());
                mTypeList.addFirst(Constants.K3Type.twosamesingle.toString());
                tempList.add(random);
                tempList.add(otherRandom);
            }
            else
            {
                int otherRandom = new Random().nextInt(6);
                while (otherRandom == random)
                {
                    otherRandom = new Random().nextInt(6);
                }
                StringBuilder builder = new StringBuilder();
                builder.append(StringUtil.TWO_SAME_CONTENT[random]);
                builder.append("#");
                builder.append(StringUtil.TWO_SAME_CONTENT[otherRandom + 6]);
                mBallList.addFirst(builder.toString());
                mTypeList.addFirst(Constants.K3Type.twosamesingle.toString());
                tempList.add(random);
                tempList.add(otherRandom);
            }
        }
        else if (currentBetType.equals(Constants.K3Type.threedifsingle.toString()) ||
                currentBetType.equals(Constants.K3Type.threedifdouble.toString()) ||
                currentBetType.equals(Constants.K3Type.dragthree.toString()))
        {
            int random = new Random().nextInt(7);

            if (random < 6)
            {
                int secondRandom = new Random().nextInt(6);
                int thirdRandom = new Random().nextInt(6);
                while (secondRandom == random)
                {
                    secondRandom = new Random().nextInt(6);
                }
                while (thirdRandom == secondRandom || thirdRandom == random)
                {
                    thirdRandom = new Random().nextInt(6);
                }
                tempList.add(random);
                tempList.add(secondRandom);
                tempList.add(thirdRandom);
                int max = Math.max(random, Math.max(secondRandom, thirdRandom));
                int min = Math.min(random, Math.min(secondRandom, thirdRandom));
                int mid = random + secondRandom + thirdRandom - max - min;
                mBallList.addFirst(StringUtil.THREE_DIFF_CONTENT[min] + " "
                        + StringUtil.THREE_DIFF_CONTENT[mid] + " "
                        + StringUtil.THREE_DIFF_CONTENT[max]);
                mTypeList.addFirst(Constants.K3Type.threedifsingle.toString());
            }
            else {
                tempList.add(random);
                mBallList.addFirst(StringUtil.THREE_DIFF_CONTENT[random]);
                mTypeList.addFirst(Constants.K3Type.threedifdouble.toString());
            }

        }
        else if (currentBetType.equals(Constants.K3Type.twodif.toString()) ||
                currentBetType.equals(Constants.K3Type.dragtwo.toString()))
        {
            int random = new Random().nextInt(6);
            int secondRandom = new Random().nextInt(6);
            while (secondRandom == random)
            {
                secondRandom = new Random().nextInt(6);
            }
            tempList.add(random);
            tempList.add(secondRandom);
            int max = Math.max(random, secondRandom);
            int min = Math.min(random, secondRandom);
            mBallList.addFirst(StringUtil.THREE_DIFF_CONTENT[min] + " "
                    + StringUtil.THREE_DIFF_CONTENT[max]);
            mTypeList.addFirst(Constants.K3Type.twodif.toString());
        }

        mZhuList.addFirst(1);
        selectedNumberList.addFirst(tempList);
        mMyAdpater.notifyDataSetChanged();
        if (mListView.getFooterViewsCount() == 0 && mBallList.size() > 0)
        {
            mListView.addFooterView(footerView);
        }
        setZhushuValue();
    }
}
