package com.jincaizi.kuaiwin.buycenter;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedList;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import com.jincaizi.common.IntentData;
import com.jincaizi.common.NumberUtil;
import com.jincaizi.common.StringUtil;
import com.jincaizi.kuaiwin.chart.views.CircularProgress;
import org.apache.http.Header;

import com.google.gson.stream.JsonReader;
import com.jincaizi.adapters.SyyTouZhuAdapter;
import com.jincaizi.http.JinCaiZiHttpClient;
import com.jincaizi.R;
import com.jincaizi.kuaiwin.mylottery.Login;
import com.jincaizi.kuaiwin.tool.BeiTouGenerator;
import com.jincaizi.kuaiwin.tool.CheckLogin;
import com.jincaizi.kuaiwin.tool.QiHaoTool;
import com.jincaizi.kuaiwin.utils.Constants;
import com.jincaizi.kuaiwin.utils.Constants.ShiyiyunType;
import com.jincaizi.kuaiwin.utils.IntentAction;
import com.jincaizi.kuaiwin.utils.SafeAsyncTask;
import com.jincaizi.kuaiwin.utils.UiHelper;
import com.jincaizi.kuaiwin.utils.Utils;
import com.jincaizi.kuaiwin.widget.JinCaiZiProgressDialog;
import com.jincaizi.vendor.http.AsyncHttpClient;
import com.jincaizi.vendor.http.AsyncHttpResponseHandler;
import com.jincaizi.vendor.http.RequestParams;


public class SyxwPick extends Activity implements OnClickListener,
        OnItemClickListener {
    public static final int MAXBEISHU = 9999;
    public static final int MAXZHUIHAO = 200;
    public static final int MINBEISHU = 1;
    public static final int MINZHUIHAO = 1;
    public static final int LOGINREQUESTCODE = 2;
    public static final String BALL = "redBall";
    public static final String BETTYPE = "betType";

    public static final int CONTINUEPICK = 0;
    public static final int RETRYPICK = 1;
    protected static final String TAG = "SyxwPick";
    private LinkedList<String> displayList = new LinkedList<String>();
    private LinkedList<ArrayList<String>> mBallList = new LinkedList<ArrayList<String>>();
    private LinkedList<ShiyiyunType> mTypeList = new LinkedList<ShiyiyunType>();
    private LinkedList<Integer> mZhuList = new LinkedList<Integer>();
    private LinkedList<ArrayList<Integer>> selectedNumberList = new LinkedList<ArrayList<Integer>>();
    private SyyTouZhuAdapter mMyAdpater;
    private ListView mListView;
    private RelativeLayout mBackView;
    private String mBetType;
    private RelativeLayout mRandomOne;
    private TextView mContinuePick;
    private int mMemoryPostion = -1;
    private Dialog myDialog;
    private TextView dialogCancel;
    private TextView dialogOK;
    private TextView mClearList;
    private TextView mSubmitGroupBuy;
//    private ArrayList<String> mBall = new ArrayList<String>();
    private String mCity;
    private TextView mSubmitDaigou;
    private TextView mFootLeftBtn;
    private EditText mTraceTime;
    private EditText mTraceIssue;
    private int mZhuiHaoNums = 1;
    private int mBeiShu = 1;
    private ImageView mTimeSub;
    private ImageView mTimeAdd;
    private ImageView mIssueSub;
    private ImageView mIssueAdd;
    private CircularProgress mSmartFollowPB;
    private CheckBox mZhuiCheckbox;
    private int mCurZhuShu = 0;
    private int mUserid;
    private String mUpk;
    private SharedPreferences sp;
    private TextView totalInFooter;
    private String mQihao = "";
    private int perDayTermCount;
    private String lotterytype;

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
        mMyAdpater = new SyyTouZhuAdapter(this, displayList, mTypeList, mZhuList);
        mListView.setAdapter(mMyAdpater);
        if (displayList.size() > 0 && mListView.getFooterViewsCount() == 0)
        {
            mListView.addFooterView(footerView);
        }
    }
    private void _getTouZhuAttribute() {
        if(mCity.equals(Constants.City.shandong.toString())) {
            lotterytype = "SD11x5";
            perDayTermCount = 78;
        } else if(mCity.equals(Constants.City.jiangxi.toString())) {
            lotterytype = "JX11x5";
            perDayTermCount = 78;
        }else if(mCity.equals(Constants.City.guangdong.toString())) {
            lotterytype = "GD11x5";
            perDayTermCount = 84;
        }else if(mCity.equals(Constants.City.anhui.toString())) {
            lotterytype = "AH11x5";
            perDayTermCount = 81;
        }else if(mCity.equals(Constants.City.chongqing.toString())) {
            lotterytype = "CQ11x5";
            perDayTermCount = 85;
        }else if(mCity.equals(Constants.City.liaoning.toString())) {
            lotterytype = "LN11x5";
            perDayTermCount = 83;
        }else if(mCity.equals(Constants.City.shanghai.toString())) {
            lotterytype = "SH11x5";
            perDayTermCount = 90;
        }else if(mCity.equals(Constants.City.heilongjiang.toString())) {
            lotterytype = "HLJ11x5";
            perDayTermCount = 73;
        }
    }
    private void _clearBetListData() {
        displayList.clear();
        mTypeList.clear();
        selectedNumberList.clear();
        mBallList.clear();
    }


    private void _initData(Intent intent, int memoryPostion) {
        mCity = intent.getStringExtra(Syxw.CITY);
        mBetType = intent.getStringExtra(IntentData.BET_TYPE);
        String result = intent.getStringExtra(IntentData.SELECT_NUMBERS);
        ArrayList<Integer> indexList = (ArrayList<Integer>)intent.getSerializableExtra(IntentData.SELECTION_INDEX);
        ArrayList<String> ballList = intent.getStringArrayListExtra(IntentData.NUMBERS_PER_SEL);
        mQihao  = intent.getStringExtra("qihao");
        int betCount = intent.getIntExtra(IntentData.BET_COUNT, 1);
        if (memoryPostion == -1) {
            mTypeList.addFirst(Constants.ShiyiyunType.getSyyType(mBetType));
            displayList.addFirst(result);
            mZhuList.addFirst(betCount);
            selectedNumberList.addFirst(indexList);
			mBallList.addFirst(ballList);
        } else {
            mTypeList.set(memoryPostion, Constants.ShiyiyunType.getSyyType(mBetType));
            displayList.set(memoryPostion, result);
            mZhuList.set(memoryPostion, betCount);
            selectedNumberList.set(memoryPostion, indexList);
            mBallList.set(memoryPostion, ballList);
        }
    }

    private void _findViews() {
        ((TextView) findViewById(R.id.current_lottery)).setText(Constants.City.getCityName(mCity)+"-11选5投注");
        mListView = (ListView) findViewById(R.id.touzhu_detail_list);
        TextView tv = (TextView) findViewById(R.id.empty_list_view);
        mListView.setEmptyView(tv);
        mBackView = (RelativeLayout) findViewById(R.id.left_layout);
        mRandomOne = (RelativeLayout) findViewById(R.id.random_layout);
        mContinuePick = (TextView) findViewById(R.id.continue_pick);
        mClearList = (TextView) findViewById(R.id.tv_ssq_clearlist);
        mSubmitGroupBuy = (TextView) findViewById(R.id.sumbit_group_buy);

        mSubmitGroupBuy.setText("发起合买");

        mSubmitDaigou = (TextView)findViewById(R.id.right_footer_btn);
        mFootLeftBtn = (TextView)findViewById(R.id.left_footer_btn);

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
        mZhuiCheckbox = (CheckBox)findViewById(R.id.tips_checkbox);
        totalInFooter = (TextView)findViewById(R.id.bet_zhushu);
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
        lp.width = (int)(250 * getResources().getDisplayMetrics().density); // 宽度
        dialogWindow.setAttributes(lp);
    }

    private void _setListener() {
        mRandomOne.setOnClickListener(this);
        mBackView.setOnClickListener(this);
        mContinuePick.setOnClickListener(this);
        mListView.setOnItemClickListener(this);
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
//                mTraceTime.setText("" + MINBEISHU);
                mBeiShu = MINBEISHU;
            } else if(Integer.parseInt(s.toString()) < MINBEISHU) {
                mTraceTime.setText("" + MINBEISHU);
                mBeiShu = MINBEISHU;
            } else if(Integer.parseInt(s.toString()) > MAXBEISHU) {
                mTraceTime.setText(""+MAXBEISHU);
                Toast.makeText(SyxwPick.this ,"最大输入" + MAXBEISHU, Toast.LENGTH_SHORT).show();
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
//                mTraceIssue.setText("" + MINZHUIHAO);
                mZhuiHaoNums = MINZHUIHAO;
            } else if(Integer.parseInt(s.toString()) < MINZHUIHAO) {
                mTraceIssue.setText("" + MINZHUIHAO);
                mZhuiHaoNums = MINZHUIHAO;
            } else if(Integer.parseInt(s.toString()) > MAXZHUIHAO) {
                mTraceIssue.setText(""+MAXZHUIHAO);
                Toast.makeText(SyxwPick.this ,"最大输入" + MAXZHUIHAO, Toast.LENGTH_SHORT).show();
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
        final Dialog localDialog = new Dialog(SyxwPick.this, R.style.Theme_dialog);
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
        lp.width = (int)(250 * getResources().getDisplayMetrics().density); // 宽度
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

                if(displayList.size() == 0) {
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
//			UiHelper.startSmartFollow(this, mZhuiHaoNums, mBeiShu, displayList.size(), mTypeList.get(0).toString(), mCity, "11x5");
                break;
            case R.id.sumbit_group_buy:
                //TODO 修改合买UI
                if(displayList.size() == 0) {
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
                for(int i=0; i<displayList.size(); i++) {
                    String redBall = displayList.get(i);
                    redBall = redBall.replace("( ", "");
                    redBall = redBall.replace(" ) ", "#");
                    if(mTypeList.get(i) == ShiyiyunType.FRONTTWOZHI ||
                            mTypeList.get(i) == ShiyiyunType.FRONTTHREEZHI) {
                        redBall = redBall.replace(" ", "|");
                    } else {
                        redBall = redBall.replace(" ", ",");
                    }
                    if(i != 0) {
                        itemBuilder.append(";");
                        typeBuilder.append(";");
                        betCountBuilder.append(";");
                    }
                    itemBuilder.append(redBall);
                    typeBuilder.append(Constants.ShiyiyunType.getSelectedType(mTypeList.get(i)));
                    betCountBuilder.append("1");
                }
                // rawParams.add("multiple", mBeiShu + "");

                String nextQihao = mQihao;
                StringBuilder qhBuilder = new StringBuilder(mQihao+"");
                StringBuilder msBuilder = new StringBuilder(mBeiShu + "");
                for(int i = 1; i< mZhuiHaoNums ; i++) {
                    nextQihao = QiHaoTool.get11x5NextQihao(nextQihao, perDayTermCount);
                    if(TextUtils.isEmpty(nextQihao)) {
                        Toast.makeText(this, "期号计算错误", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    qhBuilder.append(";"+nextQihao);
                    msBuilder.append(";"+mBeiShu);
                }
                UiHelper.startGroupBuy(this, mQihao, fenshu, isZhuihao, mCity, "11x5", qhBuilder.toString(),
                        msBuilder.toString(), itemBuilder.toString(), typeBuilder.toString(), betCountBuilder.toString(), mZhuiCheckbox.isChecked());
                break;
            case R.id.tv_ssq_clearlist:
                displayList.clear();
                mTypeList.clear();
                selectedNumberList.clear();
                mBallList.clear();
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
            case R.id.random_layout:
                addRandomOne();
                break;
            case R.id.continue_pick:
                Intent continuePick = new Intent(SyxwPick.this, Syxw.class);
                continuePick.setAction(IntentAction.CONTINUEPICKBALL);
                continuePick.putExtra(Syxw.CITY, mCity);
                continuePick.putExtra(IntentData.BET_TYPE, mBetType);
                startActivityForResult(continuePick, CONTINUEPICK);
                break;
            default:
                break;
        }
    }

    private void addRandomAnyPick(int selectionCount)
    {
        ArrayList<Integer> result;
        result = NumberUtil.getPickAnySelection(selectionCount);
        String resultString = StringUtil.getPickAnyResultString(result);
        ArrayList<String> resultList = new ArrayList<String>();
        resultList.add(resultString);
        selectedNumberList.addFirst(result);
        displayList.addFirst(resultString);
        mBallList.addFirst(resultList);
    }

    private void addRandomOne()
    {
        ShiyiyunType type;

        if (mBetType.equals(ShiyiyunType.ANYTWO.toString())
                || mBetType.equals(ShiyiyunType.ANYTWODRAG.toString()))
        {
            addRandomAnyPick(2);
            type = ShiyiyunType.ANYTWO;
        }
        else if (mBetType.equals(ShiyiyunType.ANYTHREE.toString()) ||
                mBetType.equals(ShiyiyunType.ANYTHREEDRAG.toString()))
        {
            addRandomAnyPick(3);
            type = ShiyiyunType.ANYTHREE;
        }
        else if (mBetType.equals(ShiyiyunType.ANYFOUR.toString())
                || mBetType.equals(ShiyiyunType.ANYFOURDRAG.toString()))
        {
            addRandomAnyPick(4);
            type = ShiyiyunType.ANYFOUR;
        }
        else if (mBetType.equals(ShiyiyunType.ANYFIVE.toString())
                || mBetType.equals(ShiyiyunType.ANYFIVEDRAG.toString()))
        {
            addRandomAnyPick(5);
            type = ShiyiyunType.ANYFIVE;
        }
        else if (mBetType.equals(ShiyiyunType.ANYSIX.toString())
                || mBetType.equals(ShiyiyunType.ANYSIXDRAG.toString()))
        {
            addRandomAnyPick(6);
            type = ShiyiyunType.ANYSIX;
        }
        else if (mBetType.equals(ShiyiyunType.ANYSEVEN.toString())
                || mBetType.equals(ShiyiyunType.ANYSEVENDRAG.toString()))
        {
            addRandomAnyPick(7);
            type = ShiyiyunType.ANYSEVEN;
        }
        else if (mBetType.equals(ShiyiyunType.ANYEIGHT.toString())
                || mBetType.equals(ShiyiyunType.ANYSEVENDRAG.toString()))
        {
            addRandomAnyPick(8);
            type = ShiyiyunType.ANYEIGHT;
        }
        else if (mBetType.equals(ShiyiyunType.FRONTONEZHI.toString()))
        {
            addRandomAnyPick(1);
            type = ShiyiyunType.FRONTONEZHI;
        }
        else if (mBetType.equals(Constants.ShiyiyunType.FRONTTWOZHI.toString()))
        {
            ArrayList<Integer> result;
            result = NumberUtil.getPickAnySelection(2);
            result.set(1, result.get(1) + 11);
            String resultString = StringUtil.getFrontTwoResultString(result);
            ArrayList<String> resultList = new ArrayList<String>();
            resultList.add(resultString);
            selectedNumberList.addFirst(result);
            displayList.addFirst(resultString);
            mBallList.addFirst(resultList);
            type = ShiyiyunType.FRONTTWOZHI;
        }
        else if (mBetType.equals(ShiyiyunType.FRONTTWOZU.toString())
                || mBetType.equals(ShiyiyunType.FRONTTWOZUDRAG.toString()))
        {
            addRandomAnyPick(2);
            type = ShiyiyunType.FRONTTWOZU;
        }
        else if (mBetType.equals(ShiyiyunType.FRONTTHREEZHI.toString()))
        {
            ArrayList<Integer> result;
            result = NumberUtil.getPickAnySelection(3);
            result.set(1, result.get(1) + 11);
            result.set(2, result.get(2) + 22);
            String resultString = StringUtil.getFrontThreeResultString(result);
            ArrayList<String> resultList = new ArrayList<String>();
            resultList.add(resultString);
            selectedNumberList.addFirst(result);
            displayList.addFirst(resultString);
            mBallList.addFirst(resultList);
            type = ShiyiyunType.FRONTTHREEZHI;
        }
        else
        {
            addRandomAnyPick(3);
            type = ShiyiyunType.FRONTTHREEZU;
        }

        mTypeList.addFirst(type);
        mZhuList.addFirst(1);
        mMyAdpater.notifyDataSetChanged();
        if (mListView.getFooterViewsCount() == 0 && displayList.size() > 0)
        {
            mListView.addFooterView(footerView);
        }
        setZhushuValue();
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
                if (mListView.getFooterViewsCount() == 0 && displayList.size() > 0)
                {
                    mListView.addFooterView(footerView);
                }
                setZhushuValue();
                break;
            case RETRYPICK:
                _initData(data, mMemoryPostion);
                mMyAdpater.notifyDataSetChanged();
                mMemoryPostion = -1;
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

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        _clearBetListData();
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        if (arg2 >= displayList.size())
        {
            //TODO 解释用户购彩服务协议
            return;
        }

        mMemoryPostion = arg2;
        Intent retryPick = new Intent(SyxwPick.this, Syxw.class);
        retryPick.setAction(IntentAction.RETRYPICKBALL);
        retryPick.putExtra(IntentData.BET_TYPE, mTypeList.get(arg2).toString());
        retryPick.putExtra(IntentData.SELECTION_INDEX, selectedNumberList.get(arg2));
        retryPick.putExtra(Syxw.CITY, mCity);
        startActivityForResult(retryPick, RETRYPICK);
    }

    public void deleteItemAtPosition(int position)
    {
        displayList.remove(position);
        mTypeList.remove(position);
        selectedNumberList.remove(position);
        mBallList.remove(position);
        mMyAdpater.notifyDataSetChanged();
        if (mListView.getFooterViewsCount() > 0 && displayList.size() == 0)
        {
            mListView.removeFooterView(footerView);
        }
        setZhushuValue();
    }
    /**
     * 判断是否是单一的彩票玩法
     */
    private boolean isSingleBetType() {
        for(int i=0; i< mTypeList.size() - 1; i++) {
            if(mTypeList.get(i) != mTypeList.get(i+1)) {
                return false;
            }
        }
        return true;
    }

    private LinkedList<String> getBallListAll()
    {
        LinkedList<String> result = new LinkedList<String>();

        for (ArrayList<String> list : mBallList)
        {
            for (String data : list)
            {
                result.add(data);
            }
        }

        return result;
    }

    private void getMaxBonus(){
        SafeAsyncTask<Integer> getAllDaigouTask = new SafeAsyncTask<Integer>() {
            private int perAward;
            @Override
            public  Integer call() throws Exception {
                perAward = Constants.ShiyiyunType.getPerAward(mTypeList.get(0).toString());
                if(mTypeList.get(0) == Constants.ShiyiyunType.ANYTWO || mTypeList.get(0) == Constants.ShiyiyunType.ANYTWODRAG
                        || mTypeList.get(0) == Constants.ShiyiyunType.ANYTHREE || mTypeList.get(0) == Constants.ShiyiyunType.ANYTHREEDRAG
                        || mTypeList.get(0) == Constants.ShiyiyunType.ANYFOUR || mTypeList.get(0) == Constants.ShiyiyunType.ANYFOURDRAG
                        || mTypeList.get(0) == Constants.ShiyiyunType.ANYFIVE || mTypeList.get(0) == Constants.ShiyiyunType.ANYFIVEDRAG  ) {
                    return BeiTouGenerator.getBonusR2to5(getBallListAll(), perAward);
                } else if(mTypeList.get(0) == Constants.ShiyiyunType.ANYSIX || mTypeList.get(0) == Constants.ShiyiyunType.ANYSIXDRAG
                        || mTypeList.get(0) == Constants.ShiyiyunType.ANYSEVEN || mTypeList.get(0) == Constants.ShiyiyunType.ANYSEVENDRAG
                        || mTypeList.get(0) == Constants.ShiyiyunType.ANYEIGHT || mTypeList.get(0) == Constants.ShiyiyunType.ANYEIGHTDRAG  ) {
                    return BeiTouGenerator.getBonusR6to8(getBallListAll(), perAward);
                } else {
                    return BeiTouGenerator.getBonusQ1to3(getBallListAll(), perAward);
                }
            }

            @Override
            protected void onSuccess(Integer t) throws Exception {
                // TODO Auto-generated method stub
                super.onSuccess(t);
                Log.i(TAG, "maxBonus = " + t);
                StringBuilder itemBuilder = new StringBuilder();
                StringBuilder typeBuilder = new StringBuilder();
                StringBuilder betCountBuilder = new StringBuilder();

                for (int i = 0; i < mBallList.size(); i++)
                {
                    for (int j = 0; j < mBallList.get(i).size(); j++)
                    {
                        String data = mBallList.get(i).get(j);
                        data = data.replace("( ", "");
                        data = data.replace(" ) ", "#");
                        if(mTypeList.get(i) == ShiyiyunType.FRONTTWOZHI ||
                                mTypeList.get(i) == ShiyiyunType.FRONTTHREEZHI) {
                            data = data.replace(" ", "|");
                        } else {
                            data = data.replace(" ", ",");
                        }
                        if(i != 0 || j != 0) {
                            itemBuilder.append(";");
                            typeBuilder.append(";");
                            betCountBuilder.append(";");
                        }
                        itemBuilder.append(data);
                        typeBuilder.append(Constants.ShiyiyunType.getSelectedType(mTypeList.get(i)));
                        betCountBuilder.append("1");
                    }
                }

                UiHelper.startSmartFollow(SyxwPick.this, mZhuiHaoNums, mBeiShu, displayList.size(), mTypeList.get(0).toString(), mCity, "11x5", t, perAward,
                        itemBuilder.toString(), typeBuilder.toString(), betCountBuilder.toString(), mZhuiCheckbox.isChecked()
                );
                mSmartFollowPB.setVisibility(View.GONE);
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
        for (int i = 0; i < mBallList.size(); i++)
        {
            for (int j = 0; j < mBallList.get(i).size(); j++)
            {
                String data = mBallList.get(i).get(j);
                data = data.replace("( ", "");
                data = data.replace(" ) ", "#");
                if(mTypeList.get(i) == ShiyiyunType.FRONTTWOZHI ||
                        mTypeList.get(i) == ShiyiyunType.FRONTTHREEZHI) {
                    data = data.replace(" ", "|");
                } else {
                    data = data.replace(" ", ",");
                }
                if(i != 0 || j != 0) {
                    itemBuilder.append(";");
                    typeBuilder.append(";");
                    betCountBuilder.append(";");
                }
                itemBuilder.append(data);
                typeBuilder.append(Constants.ShiyiyunType.getSelectedType(mTypeList.get(i)));
                betCountBuilder.append("1");
            }
        }
        rawParams.add("itemsContent", itemBuilder.toString());
        rawParams.add("itemsSelectType", typeBuilder.toString());
        rawParams.add("itemsBetCount", betCountBuilder.toString());
        // rawParams.add("multiple", mBeiShu + "");

        String nextQihao = mQihao;
        StringBuilder qhBuilder = new StringBuilder(mQihao+"");
        StringBuilder msBuilder = new StringBuilder(mBeiShu + "");
        for(int i = 1; i< mZhuiHaoNums ; i++) {
            nextQihao = QiHaoTool.get11x5NextQihao(nextQihao, perDayTermCount);
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

        int myBuy;
        if(mZhuiHaoNums >0) {
            //追号  此时期数=mCurQiShu
            myBuy = 2*mCurZhuShu * mBeiShu *mZhuiHaoNums;
            //return;
        } else {
            //代购 此时期数=1
            myBuy = 2*mCurZhuShu * mBeiShu*1;
        }
        rawParams.add("shoptype", "Daigou");

        rawParams.add("type", lotterytype);
        rawParams.add("zhuihaoTitle", "11x5");
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
                    if(Utils.isCmwapNet(SyxwPick.this)) {
                        charset = "utf-8";
                    } else {
                        charset = "gb2312";
                    }
                    String jsonData = new String(responseBody, charset);
                    Log.d(TAG, "post ssq form data = " + jsonData);
                    parseTouzhuJson(jsonData);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Toast.makeText(SyxwPick.this, "数据可能异常，请检查数据请求是否成功，以免重复请求造成损失", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(SyxwPick.this, "数据可能异常，请检查数据请求是否成功，以免重复请求造成损失", Toast.LENGTH_LONG).show();
                } finally {
                    // FIXME reader.close should be here
                    final Dialog localDialog = new Dialog(SyxwPick.this, R.style.Theme_dialog);
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
                    lp.width = (int)(250 * getResources().getDisplayMetrics().density); // 宽度
                    dialogWindow.setAttributes(lp);
                    localOK.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            localDialog.cancel();
                            if(!TextUtils.isEmpty(successR) && successR.equals("success")) {
                                Intent mSsqIntent = new Intent(SyxwPick.this, Syxw.class);
                                mSsqIntent.putExtra(Syxw.CITY, mCity);
                                mSsqIntent.setAction(IntentAction.FIRSTPICKBALL);
                                startActivity(mSsqIntent);
                                finish();
                            } else if(!TextUtils.isEmpty(responseMsg) && responseMsg.equals("未登录或登录过期！")) {
                                CheckLogin.clearLoginStatus(sp);
                                Intent loginIntent = new Intent(SyxwPick.this, Login.class);
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
                Toast.makeText(SyxwPick.this, responseMsg, Toast.LENGTH_SHORT).show();
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
        mProgressDialog = JinCaiZiProgressDialog.show(this, "正在提交，请稍等...");
    }

    private void _hideTouzhuProgress() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }
}
