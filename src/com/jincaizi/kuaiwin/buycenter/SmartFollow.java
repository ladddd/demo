package com.jincaizi.kuaiwin.buycenter;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;
import android.widget.CompoundButton.OnCheckedChangeListener;
import org.apache.http.Header;

import com.google.gson.stream.JsonReader;
import com.jincaizi.adapters.SmartFollowAdapter;
import com.jincaizi.adapters.SmartFollowAdapter.SmartFollowElement;
import com.jincaizi.http.JinCaiZiHttpClient;
import com.jincaizi.R;
import com.jincaizi.kuaiwin.mylottery.Login;
import com.jincaizi.kuaiwin.tool.BeiTouGenerator;
import com.jincaizi.kuaiwin.tool.CheckLogin;
import com.jincaizi.kuaiwin.utils.Constants;
import com.jincaizi.kuaiwin.utils.UiHelper;
import com.jincaizi.kuaiwin.utils.Utils;
import com.jincaizi.kuaiwin.widget.JinCaiZiProgressDialog;
import com.jincaizi.vendor.http.AsyncHttpClient;
import com.jincaizi.vendor.http.AsyncHttpResponseHandler;
import com.jincaizi.vendor.http.RequestParams;

public class SmartFollow extends Activity implements OnClickListener, OnCheckedChangeListener{
    public static final String ZHUIHAOCOUNT = "zhuihaoshu";
    public static final String STARTTIMES = "qishibeishu";
    public static final String ZHUSHU = "zhushu";
    public static final String LOTTERYTYPE = "lotterytype";
    public static final String CITY = "city";
    public static final String BETTYPE = "bettype";
    public static final String MAXBONUS = "maxbonus";
    public static final String MINBONUS = "minbonus";
    public static final String ITEMCONTENT = "itemcontent";
    public static final String TYPECONTENT = "typecontent";
    public static final String BETCOUNT = "betcount";
    public static final String STOPZHUIHAO = "stopzhuihao";
    protected static final String TAG = "SmartFollow";
    private static final int LOGINREQUESTCODE = 2;

    public static final int MAXBEISHU = 9999;
    public static final int MAXZHUIHAO = 200;
    public static final int MINBEISHU = 1;
    public static final int MINZHUIHAO = 1;

    private boolean isKeyboardShown = false;

    public ListView mListView;
    private TextView pickBtn;
    public TextView mBetMoney;
    private TextView mSettingBtn;
    private SmartFollowAdapter mAdapter;
    private  ArrayList<SmartFollowElement>mData = new ArrayList<SmartFollowAdapter.SmartFollowElement>();
    private int[] perAward;
    private Dialog myDialog;
    private ImageView periodSub;
    private ImageView periodAdd;
    private EditText periodValue;
    private ImageView timeStartSub;
    private ImageView timeStartAdd;
    private EditText timeStartValue;
    private RadioButton profitRateRadio;
    private EditText profitRateTv;
    private RadioButton profitSecondRateRadio;
    private EditText profitFisrtPeriodTv;
    private EditText profitFisrtRateTv;
    private EditText profitSecondRateTv;
    private RadioButton profitBonusRadio;
    private EditText profitBonusTv;
//    private ToggleButton followMode;
    private Button btnCancel;
    private Button btnYes;
    public int profitMode = 0; //0 firstRadio, 1 secondRadio, 2 thirdRadio
    private int mZhuiHaoNums = 0;
    private int mBeiShu = 0;
    private int mZhushu = 0;
    public double mMinProfitRate = 0;//全程最低收益
    private TextView mPlanInfo;
    public int profitFirstPeriodValue = 0;
    public double profitFirstRateValue = 0;
    public double profitSecondRateValue = 0;
    public double profitBonusValue = 0;
    private boolean mIsPlanOK = true;
    private String mCity;
    public String mLotteryType;
    private int mMaxBonus = 0;
    private int mMinBonus = 0;
    private String lotterytype;
    private int perDayTermCount;
    private TextView mQihaoView;
    private TextView mTimeDiffView;
    private String mTitleStr;
    private TextView mTitleView;
    private String itemContent;
    private String typeContent;
    private String betCount;
    private boolean isStopZhui;
    private int mUserid;
    private String mUpk;
    private SharedPreferences sp;
    private RelativeLayout backBtn;
    private TextView mGroupBuyView;

    private EditText issueEdit;

    private TextView betCountTxt;
    private TextView issueCountTxt;

    private CheckBox stop;

    private boolean keyboardListenersAttached = false;
    private ViewGroup rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.smartfollowmain);
        sp = this.getSharedPreferences("loginStatus", Context.MODE_PRIVATE);
        _getDataFromIntent();
        _getTouZhuAttribute();
        _findViews();
        _setListener();
        _setAdapter();
        _requestData();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub

        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        switch (requestCode) {
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
    private void _getDataFromIntent() {
        mZhuiHaoNums = getIntent().getIntExtra(ZHUIHAOCOUNT, 10);
        mBeiShu = getIntent().getIntExtra(STARTTIMES, 1);
        mZhushu = getIntent().getIntExtra(ZHUSHU, 1);
        mCity = getIntent().getStringExtra(CITY);
        mLotteryType = getIntent().getStringExtra(LOTTERYTYPE);
        mMaxBonus = getIntent().getIntExtra(MAXBONUS, 0);
        mMinBonus = getIntent().getIntExtra(MINBONUS, 0);
        itemContent = getIntent().getStringExtra(ITEMCONTENT);
        typeContent = getIntent().getStringExtra(TYPECONTENT);
        betCount = getIntent().getStringExtra(BETCOUNT);
        isStopZhui = getIntent().getBooleanExtra(STOPZHUIHAO, false);

        if(mZhushu > 1 && mMaxBonus != mMinBonus) {
            perAward = new int[2];
            perAward[0] = mMinBonus;
            perAward[1] = mMaxBonus;
        } else {
            perAward = new int[1];
            perAward[0] = mMinBonus;
        }
    }
    private void _initData() {

        //HashMap<String,Integer> map = BeiTouGenerator.generateBettype(City.getCityBetType(mCity, mLotteryType));
        mData.clear();
        String[]qihaos = BeiTouGenerator.generateQihaos(perDayTermCount, mQihao, mZhuiHaoNums, mLotteryType);
        for(int i=0; i<mZhuiHaoNums; i++) {
            SmartFollowElement element = new SmartFollowElement();
            element.beishu =  mBeiShu;
            element.zhushu = mZhushu;
            if(i ==0) {
                element.touruAmount =  element.beishu * element.zhushu*2;
            } else {
                element.touruAmount =  element.beishu * element.zhushu*2 + mData.get(i-1).touruAmount;

            }
            element.qihao = qihaos[i];
            int[] yinli = new int[perAward.length];
            double[] percent = new double[perAward.length];
            StringBuilder yinliStr = new StringBuilder("");
            StringBuilder percentStr = new StringBuilder("");
            for(int index=0; index< perAward.length; index++) {
                yinli[index] = element.beishu *perAward[index] - element.touruAmount ;
                percent[index] = (yinli[index]*100.0)/(element.touruAmount*1.0);
                if(index !=0) {
                    yinliStr.append("至");
                    percentStr.append("至");
                }
                yinliStr.append(yinli[index]);
                try {
                    percentStr.append(Utils.formatDoubleForTwo(percent[index]) +"%");
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    percentStr.append("--%");
                }
            }
            element.yingliAmount = yinliStr.toString();
            element.yingliProfit = percentStr.toString();
            if(i ==0) {
                mMinProfitRate = percent[0];
            } else {
                if(percent[0] < mMinProfitRate) {
                    mMinProfitRate = percent[0];
                }
            }

            mData.add(element) ;
        }
        mAdapter.notifyDataSetChanged();
        _updateFooterValue();
        updatePlanInfo();
    }
    private void _getTouZhuAttribute() {
        if(mCity.equals(Constants.City.shandong.toString())) {
            mTitleStr = "山东";
            lotterytype = "SD"+mLotteryType;
            perDayTermCount = 78;
        } else if(mCity.equals(Constants.City.jiangxi.toString())) {
            mTitleStr = "江西";
            lotterytype = "JX"+mLotteryType;
            if(mLotteryType.equals("11x5")) {
                perDayTermCount = 78;
            } else {
                perDayTermCount = 84;
            }
        }else if(mCity.equals(Constants.City.guangdong.toString())) {
            mTitleStr = "广东";
            lotterytype = "GD"+mLotteryType;
            perDayTermCount = 84;
        }else if(mCity.equals(Constants.City.anhui.toString())) {
            mTitleStr = "安徽";
            lotterytype = "AH"+mLotteryType;
            if(mLotteryType.equals("11x5")) {
                perDayTermCount = 81;
            } else {
                perDayTermCount = 80;
            }
        }else if(mCity.equals(Constants.City.chongqing.toString())) {
            mTitleStr = "重庆";
            lotterytype = "CQ"+mLotteryType;
            if(mLotteryType.equals("11x5")) {
                perDayTermCount = 85;
            } else {
                perDayTermCount = 120;
            }
        }else if(mCity.equals(Constants.City.liaoning.toString())) {
            mTitleStr = "辽宁";
            lotterytype = "LN"+mLotteryType;
            perDayTermCount = 83;
        }else if(mCity.equals(Constants.City.shanghai.toString())) {
            mTitleStr = "上海";
            lotterytype = "SH"+mLotteryType;
            perDayTermCount = 90;
        }else if(mCity.equals(Constants.City.heilongjiang.toString())) {
            mTitleStr = "黑龙江";
            lotterytype = "HLJ"+mLotteryType;
            perDayTermCount = 73;
        } else if(mCity.equals(Constants.City.jiangsu.toString())) {
            mTitleStr = "江苏";
            lotterytype = "JS"+mLotteryType;
            perDayTermCount = 82;
        }else if(mCity.equals(Constants.City.neimenggu.toString())) {
            mTitleStr = "内蒙古";
            lotterytype = "NMG"+mLotteryType;
            perDayTermCount = 73;
        }
        if(mLotteryType.equals("11x5")) {
            mTitleStr += "11选5";
        } else if(mLotteryType.equals("K3")) {
            mTitleStr += "快3";
        } else {
            mTitleStr += "时时彩";
        }
        mTitleStr = mTitleStr.replace("山东11选5", "十一运夺金");
        mTitleStr += "-智能追号";
    }
    public void updatePlanInfo() {
        try {
            switch(profitMode) {
                case 0:
                    mPlanInfo.setText("全程最低盈利率"+Utils.formatDoubleForTwo(mMinProfitRate)+"%");
                    break;
                case 1:
                    mPlanInfo.setText("前"+profitFirstPeriodValue+"期"+ Utils.formatDoubleForTwo(profitFirstRateValue)+"%之后盈利率"+
                            Utils.formatDoubleForTwo(profitSecondRateValue));
                    break;
                case 2:
                    mPlanInfo.setText("全程最低盈利"+profitBonusValue+"元");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void _findViews() {
        mTitleView = (TextView)findViewById(R.id.current_lottery);
        mTitleView.setText(mTitleStr);
        mListView = (ListView)findViewById(R.id.follow_plan_list);
//    	findViewById(R.id.left_footer_btn).setVisibility(View.GONE);
        pickBtn = (TextView)findViewById(R.id.pick_btn);
        backBtn = (RelativeLayout)findViewById(R.id.left_layout);
        mBetMoney = (TextView)findViewById(R.id.bet_txt_5);
        mSettingBtn = (TextView)findViewById(R.id.smartfollow_bt);

        stop = (CheckBox) findViewById(R.id.tips_checkbox);
        stop.setChecked(isStopZhui);

        issueEdit = (EditText)findViewById(R.id.issue_edit);
        issueEdit.setInputType(InputType.TYPE_CLASS_NUMBER);
        issueEdit.setText(String.valueOf(mZhuiHaoNums));

        betCountTxt = (TextView) findViewById(R.id.bet_txt_1);
        betCountTxt.setText(String.valueOf(mZhushu));
        issueCountTxt = (TextView) findViewById(R.id.bet_txt_3);
        issueCountTxt.setText(String.valueOf(mZhuiHaoNums));

        mGroupBuyView = (TextView)findViewById(R.id.sumbit_group_buy);
        //findViewById(R.id.right_divider).setVisibility(View.GONE);

        mQihaoView = (TextView)findViewById(R.id.period_qihao_tv);
        mTimeDiffView = (TextView)findViewById(R.id.period_time_tv);

        mPlanInfo = (TextView)findViewById(R.id.tv_plan_info);
        updatePlanInfo();
    }
    private void _setListener() {
        mSettingBtn.setOnClickListener(this);
        pickBtn.setOnClickListener(this);
        mQihaoView.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        mGroupBuyView.setOnClickListener(this);

        stop.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                isStopZhui = checked;
            }
        });

        issueEdit.addTextChangedListener(new MyIssueWatcher());
        //失去焦点更新计算期数
        issueEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focus) {
                if (!focus)
                {
                    if (myDialog != null) {
                        _generatePlan();
                    }
                    else
                    {
                        _initData();
                    }
                }
            }
        });

        attachKeyboardListeners();
    }


    private ViewTreeObserver.OnGlobalLayoutListener keyboardLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            int heightDiff = rootLayout.getRootView().getHeight() - rootLayout.getHeight();
            int contentViewTop = getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();

            if(heightDiff <= contentViewTop){
                onHideKeyboard();
            } else {
                int keyboardHeight = heightDiff - contentViewTop;
                onShowKeyboard(keyboardHeight);
            }
        }
    };

    protected void onShowKeyboard(int keyboardHeight) {}

    protected void onHideKeyboard() {
        issueEdit.clearFocus();
    }

    protected void attachKeyboardListeners() {
        if (keyboardListenersAttached) {
            return;
        }

        rootLayout = (ViewGroup) findViewById(R.id.sf_root_view);
        rootLayout.getViewTreeObserver().addOnGlobalLayoutListener(keyboardLayoutListener);

        keyboardListenersAttached = true;
    }


    private void _setAdapter() {
        mListView.setItemsCanFocus(true);
        mAdapter = new SmartFollowAdapter(this, mData, perAward);
        mListView.setAdapter(mAdapter);
        _updateFooterValue();
    }
    private void _updateFooterValue() {
        if(mData.size() == 0) {
            mBetMoney.setText("0");
        } else {
            mBetMoney.setText(mData.get(mData.size()-1).touruAmount+"");
        }
    }
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
//            case R.id.follow_mode:
//                isStopZhui = followMode.isChecked();
//                break;
            case R.id.sumbit_group_buy:
                if(mData.size() < 1) {
                    Toast.makeText(this, "请至少选择一注", Toast.LENGTH_SHORT).show();
                    break;
                }

                StringBuilder qhBuilder = new StringBuilder(mData.get(0).qihao+"");
                StringBuilder msBuilder = new StringBuilder(mData.get(0).beishu + "");
                for(int i = 1; i< mData.size() ; i++) {
                    qhBuilder.append(";"+mData.get(i).qihao);
                    msBuilder.append(";"+mData.get(i).beishu);
                }
                UiHelper.startGroupBuy(this, mQihao, mData.get(mData.size()-1).touruAmount, true, mCity, mLotteryType, qhBuilder.toString(),
                        msBuilder.toString(), itemContent, typeContent, betCount, isStopZhui);
                if(mc != null) {
                    mc.cancel();
                }
                finish();

                break;
            case R.id.period_qihao_tv:
                _requestData();
                break;
            case R.id.left_layout:
                if(mc != null) {
                    mc.cancel();
                }
                finish();
                break;
            case R.id.pick_btn:
                if(mData.size() < 1) {
                    Toast.makeText(this, "请至少选择一注", Toast.LENGTH_SHORT).show();
                    break;
                }
                //_postFormData();
                _showDaigouDialog();
                break;
            case R.id.smartfollow_bt:
                _showSettinDialog();
                break;
            case R.id.period_add:
                int period = Integer.parseInt(periodValue.getText().toString()) + 1;
//                if(period < 1) {
//                    periodValue.setText("1");
//                } else if(period > 200) {
//                    periodValue.setText("200");
//                } else {
//                    periodValue.setText(period+"");
//                }
//                periodValue.setSelection(periodValue.getText().length()-1);
                periodValue.setText(period + "");
                periodValue.setSelection(periodValue.getText().length()-1);
                break;
            case R.id.period_sub:
                int periodsub = Integer.parseInt(periodValue.getText().toString()) - 1;
                periodValue.setText(periodsub + "");
                periodValue.setSelection(periodValue.getText().length()-1);
                break;
            case R.id.times_start_add:
                int timestartadd = Integer.parseInt(timeStartValue.getText().toString()) + 1;
                timeStartValue.setText(timestartadd + "");
                timeStartValue.setSelection(timeStartValue.getText().length()-1);
                break;
            case R.id.times_start_sub:
                int timestartsub = Integer.parseInt(timeStartValue.getText().toString()) - 1;
                timeStartValue.setText(timestartsub+"");
                timeStartValue.setSelection(timeStartValue.getText().length()-1);
                break;
            case R.id.profit_rate_plan://first radio
                if(profitRateRadio.isChecked()) {
                    profitSecondRateRadio.setChecked(false);
                    profitBonusRadio.setChecked(false);
                    profitMode = 0;
                }
                break;
            case R.id.profit_rate_second_plan://second radio
                if(profitSecondRateRadio.isChecked()) {
                    profitRateRadio.setChecked(false);
                    profitBonusRadio.setChecked(false);
                    profitMode = 1;
                }
                break;
            case R.id.profit_bonus_plan://third radio
                if(profitBonusRadio.isChecked()) {
                    profitRateRadio.setChecked(false);
                    profitSecondRateRadio.setChecked(false);
                    profitMode = 2;
                }
                break;
            case R.id.bt_cancel:
                myDialog.dismiss();
                break;
            case R.id.bt_yes:
                _generatePlan();
                if(mIsPlanOK) {
                    myDialog.dismiss();
                }
                break;
            default:
                break;
        }

    }
    private void _showSettinDialog() {
        myDialog = new Dialog(this, R.style.jczDialog);
        View view = LayoutInflater.from(getApplicationContext()).inflate(
                R.layout.smartfollow_panel, null);
        _findDialogViews(view);
        _setDialogValues();
        _setDialogListeners();
        myDialog.setContentView(view);
        myDialog.setCanceledOnTouchOutside(false);
        Window dialogWindow = myDialog.getWindow();

        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int)(360 * getResources().getDisplayMetrics().density);;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        lp.dimAmount = 0.5f;
        dialogWindow.setAttributes(lp);
        myDialog.show();
    }

    private void _findDialogViews(View view) {
        periodSub = (ImageView)view.findViewById(R.id.period_sub);
        periodAdd = (ImageView)view.findViewById(R.id.period_add);
        periodValue = (EditText)view.findViewById(R.id.period_time_tv);

        timeStartSub = (ImageView)view.findViewById(R.id.times_start_sub);
        timeStartAdd = (ImageView)view.findViewById(R.id.times_start_add);
        timeStartValue = (EditText)view.findViewById(R.id.times_start_tv);

        profitRateRadio = (RadioButton)view.findViewById(R.id.profit_rate_plan);
        profitRateTv = (EditText)view.findViewById(R.id.profit_rate_tv);

        profitSecondRateRadio = (RadioButton)view.findViewById(R.id.profit_rate_second_plan);
        profitFisrtPeriodTv = (EditText)view.findViewById(R.id.profit_rate_first_period_tv);
        profitFisrtRateTv = (EditText)view.findViewById(R.id.profit_rate_first_rate_tv);
        profitSecondRateTv = (EditText)view.findViewById(R.id.profit_rate_second_rate_tv);

        profitBonusRadio = (RadioButton)view.findViewById(R.id.profit_bonus_plan);
        profitBonusTv = (EditText)view.findViewById(R.id.profit_bonus_tv);

//        followMode = (ToggleButton)view.findViewById(R.id.follow_mode);
//        followMode.setChecked(isStopZhui);

        btnCancel = (Button)view.findViewById(R.id.bt_cancel);
        btnYes = (Button)view.findViewById(R.id.bt_yes);
    }

    private void _setDialogValues() {
        periodValue.setText(mZhuiHaoNums + "");
        timeStartValue.setText(mBeiShu + "");
        switch (profitMode) {
            case 0:
                profitRateRadio.setChecked(true);
                profitRateTv.setEnabled(true);
                break;
            case 1:
                profitSecondRateRadio.setChecked(true);
                profitFisrtPeriodTv.setEnabled(true);
                profitFisrtRateTv.setEnabled(true);
                profitSecondRateTv.setEnabled(true);
                break;
            case 2:
                profitBonusRadio.setChecked(true);
                profitBonusTv.setEnabled(true);
                break;
        }
//        try {
//			profitRateTv.setText(Utils.formatDoubleForTwo(mMinProfitRate));
//			profitFisrtPeriodTv.setText(profitFirstPeriodValue+"");
//			profitFisrtRateTv.setText(Utils.formatDoubleForTwo(profitFirstRateValue));
//			profitSecondRateTv.setText(Utils.formatDoubleForTwo(profitSecondRateValue));
//			profitBonusTv.setText(Utils.formatDoubleForTwo(profitBonusValue));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
    }
    private void _setDialogListeners() {
        periodSub.setOnClickListener(this);
        periodAdd.setOnClickListener(this);
        timeStartAdd.setOnClickListener(this);
        timeStartSub.setOnClickListener(this);
        periodValue.addTextChangedListener(new MyPeriodWatcher(periodValue));
        timeStartValue.addTextChangedListener(new MyTimeStartWatcher(timeStartValue));
        profitRateTv.addTextChangedListener(new MyTimeStartWatcher(profitRateTv));
        profitFisrtPeriodTv.addTextChangedListener(new SubIssueWather());
        profitFisrtRateTv.addTextChangedListener(new MyTimeStartWatcher(profitFisrtRateTv));
        profitSecondRateTv.addTextChangedListener(new MyTimeStartWatcher(profitSecondRateTv));
        profitBonusTv.addTextChangedListener(new MyTimeStartWatcher(profitBonusTv));

        profitRateRadio.setOnClickListener(this);
        profitSecondRateRadio.setOnClickListener(this);
        profitBonusRadio.setOnClickListener(this);

        profitRateRadio.setOnCheckedChangeListener(this);
        profitSecondRateRadio.setOnCheckedChangeListener(this);
        profitBonusRadio.setOnCheckedChangeListener(this);

        btnCancel.setOnClickListener(this);
        btnYes.setOnClickListener(this);
//        followMode.setOnClickListener(this);

    }

    private class SubIssueWather implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            int max = Integer.parseInt(periodValue.getText().toString());
            if(TextUtils.isEmpty(charSequence)) {
                profitFisrtPeriodTv.setText("1");
            }else if(Integer.parseInt(charSequence.toString()) > max) {
                profitFisrtPeriodTv.setText(max + "");
            } else if(Integer.parseInt(charSequence.toString()) < 1 ) {
                profitFisrtPeriodTv.setText("1");
            }
            profitFisrtPeriodTv.setSelection(profitFisrtPeriodTv.getText().length());
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }

    class MyPeriodWatcher implements TextWatcher {

        private EditText editText;

        public MyPeriodWatcher(EditText editText)
        {
            this.editText = editText;
        }

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
            if (editText == null)
            {
                return;
            }

            if(TextUtils.isEmpty(s)) {
                editText.setText("1");
            }else if(Integer.parseInt(s.toString()) > 200) {
                editText.setText("200");
            } else if(Integer.parseInt(s.toString()) < 1 ) {
                editText.setText("1");
            }
//            mZhuiHaoNums = Integer.parseInt(editText.getText().toString());
            editText.setSelection(editText.getText().length());
        }

    }
    class MyTimeStartWatcher implements TextWatcher {

        private EditText editText;

        public MyTimeStartWatcher(EditText editText)
        {
            this.editText = editText;
        }

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
            if (editText == null)
            {
                return;
            }

            if(TextUtils.isEmpty(s)) {
                editText.setText("1");
            }else if(Integer.parseInt(s.toString()) > 9999) {
                editText.setText("9999");
            } else if(Integer.parseInt(s.toString()) < 1 ) {
                editText.setText("1");
            }
            editText.setSelection(editText.getText().length());
        }

    }
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch(buttonView.getId()) {
            case R.id.profit_rate_plan://first radio
                if(isChecked) {
                    profitRateTv.setEnabled(true);
                } else {
                    profitRateTv.setEnabled(false);
                }
                break;
            case R.id.profit_rate_second_plan://second radio
                if(isChecked) {
                    profitFisrtPeriodTv.setEnabled(true);
                    profitFisrtRateTv.setEnabled(true);
                    profitSecondRateTv.setEnabled(true);
                } else {
                    profitFisrtPeriodTv.setEnabled(false);
                    profitFisrtRateTv.setEnabled(false);
                    profitSecondRateTv.setEnabled(false);
                }
                break;
            case R.id.profit_bonus_plan://third radio
                if(isChecked) {
                    profitBonusTv.setEnabled(true);
                } else {
                    profitBonusTv.setEnabled(false);
                }
                break;
            default:
                break;
        }

    }
    private void _generatePlan() {
        switch(profitMode) {
            case 0:
                _generateSubPlanA();
                break;
            case 1:
                _generateSubPlanB();
                break;
            case 2:
                _generateSubPlanC();
                break;
            default:
                break;
        }
    }
    //全程最低收益率
    private void _generateSubPlanA() {
        try {
            mMinProfitRate = Double.parseDouble(profitRateTv.getText().toString());
            mZhuiHaoNums = Integer.parseInt(periodValue.getText().toString());
            mBeiShu = Integer.parseInt(timeStartValue.getText().toString());
        } catch (Exception ee) {
            Toast.makeText(this, "预期盈利设置格式不正确，请检查", Toast.LENGTH_SHORT).show();
            mIsPlanOK = false;
            return;
        }
        mData.clear();
        String[]qihaos = BeiTouGenerator.generateQihaos(perDayTermCount, mQihao, mZhuiHaoNums, mLotteryType);
        for(int i=0; i<mZhuiHaoNums; i++) {
            SmartFollowElement element = new SmartFollowElement();
            element.beishu =  mBeiShu;
            element.zhushu = mZhushu;
            if(i ==0) {
                element.touruAmount =  element.beishu * element.zhushu*2;
            } else {
                element.touruAmount =  element.beishu * element.zhushu*2 + mData.get(i-1).touruAmount;

            }
            element.qihao = qihaos[i];
            int[] yinli = new int[perAward.length];
            double[] percent = new double[perAward.length];
            StringBuilder yinliStr = new StringBuilder();
            StringBuilder percentStr = new StringBuilder();
            for(int index=0; index< perAward.length; index++) {
                yinli[index] = element.beishu *perAward[index] - element.touruAmount ;
                percent[index] = (yinli[index]*100.0)/(element.touruAmount*1.0);
                if(index !=0) {
                    yinliStr.append("至");
                    percentStr.append("至");
                }
                yinliStr.append(yinli[index]);
                try {
                    percentStr.append(Utils.formatDoubleForTwo(percent[index]) +"%");
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    percentStr.append("--%");
                }
            }
            element.yingliAmount = yinliStr.toString();
            element.yingliProfit = percentStr.toString();
            boolean isRecompute = false;
            int targetAward = 0;
            if(percent.length == 1 && percent[0] < mMinProfitRate) {
                isRecompute = true;
                targetAward = perAward[0];
            }else if(percent.length == 2 && Math.min(percent[0], percent[1]) < mMinProfitRate) {
                isRecompute = true;
                targetAward = perAward[0];
            }
            if(isRecompute) {
                int newBeishu = 0;
                if(i ==0) {
                    Toast.makeText(this, "第"+qihaos[i]+"期开始无法计算方案，可重新设置", Toast.LENGTH_SHORT).show();
                    updateIssue(i + 1);
                    break;
                } else {
                    newBeishu = (int) (((mMinProfitRate/100+1) *mData.get(i-1).touruAmount)/(targetAward - element.beishu * element.zhushu*2*(mMinProfitRate/100+1))) + 1;
                    if(newBeishu >9999|| newBeishu < element.beishu) {
                        Toast.makeText(this, "第"+qihaos[i]+"期开始无法计算方案，可重新设置", Toast.LENGTH_SHORT).show();
                        updateIssue(i + 1);
                        break;
                    }
                    element.touruAmount =  newBeishu * element.zhushu*2 + mData.get(i-1).touruAmount;
                    element.beishu = newBeishu;
                    StringBuilder yinliStr1 = new StringBuilder();
                    StringBuilder percentStr1 = new StringBuilder();
                    for(int index=0; index< perAward.length; index++) {
                        yinli[index] = element.beishu *perAward[index] - element.touruAmount ;
                        percent[index] = (yinli[index]*100.0)/(element.touruAmount*1.0);
                        if(index !=0) {
                            yinliStr1.append("至");
                            percentStr1.append("至");
                        }
                        yinliStr1.append(yinli[index]);
                        try {
                            percentStr1.append(Utils.formatDoubleForTwo(percent[index]) +"%");
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                            percentStr1.append("--%");
                        }
                    }
                    element.yingliAmount = yinliStr1.toString();
                    element.yingliProfit = percentStr1.toString();
                }
            }


            mData.add(element) ;
        }
        mAdapter.notifyDataSetChanged();
        if(mData.size() == 0) {
            mBetMoney.setText("0");
        } else {
            issueEdit.setText(mZhuiHaoNums + "");
            issueCountTxt.setText(mZhuiHaoNums + "");
            mBetMoney.setText(mData.get(mData.size()-1).touruAmount+"");
        }
        mIsPlanOK = true;
        updatePlanInfo();
    }

    //前x期收益率xx，之后收益率yy
    private void _generateSubPlanB() {
        try{
            mZhuiHaoNums = Integer.parseInt(periodValue.getText().toString());
            mBeiShu = Integer.parseInt(timeStartValue.getText().toString());
            profitFirstPeriodValue = Integer.parseInt(profitFisrtPeriodTv.getText().toString());
            profitFirstRateValue = Double.parseDouble(profitFisrtRateTv.getText().toString());
            profitSecondRateValue = Double.parseDouble(profitSecondRateTv.getText().toString());
        }catch(Exception ee){
            Toast.makeText(this, "预期盈利设置格式不正确，请检查", Toast.LENGTH_SHORT).show();
            mIsPlanOK = false;
            return;
        }
        mData.clear();
        String[]qihaos = BeiTouGenerator.generateQihaos(perDayTermCount, mQihao, mZhuiHaoNums, mLotteryType);
        for(int i=0; i<mZhuiHaoNums; i++) {
            SmartFollowElement element = new SmartFollowElement();
            element.beishu =  mBeiShu;
            element.zhushu = mZhushu;
            if(i ==0) {
                element.touruAmount =  element.beishu * element.zhushu*2;
            } else {
                element.touruAmount =  element.beishu * element.zhushu*2 + mData.get(i-1).touruAmount;

            }
            element.qihao = qihaos[i];
            int[] yinli = new int[perAward.length];
            double[] percent = new double[perAward.length];
            StringBuilder yinliStr = new StringBuilder();
            StringBuilder percentStr = new StringBuilder();
            for(int index=0; index< perAward.length; index++) {
                yinli[index] = element.beishu *perAward[index] - element.touruAmount ;
                percent[index] = (yinli[index]*100.0)/(element.touruAmount*1.0);
                if(index !=0) {
                    yinliStr.append("至");
                    percentStr.append("至");
                }
                yinliStr.append(yinli[index]);
                try {
                    percentStr.append(Utils.formatDoubleForTwo(percent[index]) +"%");
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    percentStr.append("--%");
                }
            }
            element.yingliAmount = yinliStr.toString();
            element.yingliProfit = percentStr.toString();
            double targetMinRate;
            if(i<= profitFirstPeriodValue-1) {
                targetMinRate = profitFirstRateValue;

            } else {
                targetMinRate = profitSecondRateValue;
            }

            boolean isRecompute = false;
            int targetAward = 0;
            if(percent.length == 1 && percent[0] < targetMinRate) {
                isRecompute = true;
                targetAward = perAward[0];
            }else if(percent.length == 2 && Math.min(percent[0], percent[1]) < targetMinRate) {
                isRecompute = true;
                targetAward = perAward[0];
            }
            if(isRecompute) {
                int newBeishu = 0;
                if(i ==0) {
                    Toast.makeText(this, "第"+qihaos[i]+"期开始无法计算方案，可重新设置", Toast.LENGTH_SHORT).show();
                    updateIssue(i + 1);
                    break;
                } else {
                    newBeishu = (int) (((targetMinRate/100+1) *mData.get(i-1).touruAmount)/(targetAward - element.beishu * element.zhushu*2*(targetMinRate/100+1))) + 1;
                    if(newBeishu >9999 || newBeishu < element.beishu) {
                        Toast.makeText(this, "第"+qihaos[i]+"期开始无法计算方案，可重新设置", Toast.LENGTH_SHORT).show();
                        updateIssue(i + 1);
                        break;
                    }
                    element.touruAmount =  newBeishu * element.zhushu*2 + mData.get(i-1).touruAmount;
                    element.beishu = newBeishu;
                    StringBuilder yinliStr1 = new StringBuilder();
                    StringBuilder percentStr1 = new StringBuilder();
                    for(int index=0; index< perAward.length; index++) {
                        yinli[index] = element.beishu *perAward[index] - element.touruAmount ;
                        percent[index] = (yinli[index]*100.0)/(element.touruAmount*1.0);
                        if(index !=0) {
                            yinliStr1.append("至");
                            percentStr1.append("至");
                        }
                        yinliStr1.append(yinli[index]);
                        try {
                            percentStr1.append(Utils.formatDoubleForTwo(percent[index]) +"%");
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                            percentStr1.append("--%");
                        }
                    }
                    element.yingliAmount = yinliStr1.toString();
                    element.yingliProfit = percentStr1.toString();
                }
            }

            mData.add(element) ;
        }
        mAdapter.notifyDataSetChanged();
        if(mData.size() == 0) {
            mBetMoney.setText("0");
        } else {
            issueEdit.setText(mZhuiHaoNums + "");
            issueCountTxt.setText(mZhuiHaoNums + "");
            mBetMoney.setText(mData.get(mData.size()-1).touruAmount+"");
        }
        mIsPlanOK = true;
        updatePlanInfo();
    }
    //全程最低收益xx元
    private void _generateSubPlanC() {
        try{
            mZhuiHaoNums = Integer.parseInt(periodValue.getText().toString());
            mBeiShu = Integer.parseInt(timeStartValue.getText().toString());
            profitBonusValue = Double.parseDouble(profitBonusTv.getText().toString());
        }catch(Exception ee){
            Toast.makeText(this, "预期盈利设置格式不正确，请检查", Toast.LENGTH_SHORT).show();
            mIsPlanOK = false;
            return;
        }
        mData.clear();
        String[]qihaos = BeiTouGenerator.generateQihaos(perDayTermCount, mQihao, mZhuiHaoNums, mLotteryType);
        for(int i=0; i<mZhuiHaoNums; i++) {
            SmartFollowElement element = new SmartFollowElement();
            element.beishu =  mBeiShu;
            element.zhushu = mZhushu;
            if(i ==0) {
                element.touruAmount =  element.beishu * element.zhushu*2;
            } else {
                element.touruAmount =  element.beishu * element.zhushu*2 + mData.get(i-1).touruAmount;

            }
            element.qihao = qihaos[i];
            int[] yinli = new int[perAward.length];
            double[] percent = new double[perAward.length];
            StringBuilder yinliStr = new StringBuilder();
            StringBuilder percentStr = new StringBuilder();
            for(int index=0; index< perAward.length; index++) {
                yinli[index] = element.beishu *perAward[index] - element.touruAmount ;
                percent[index] = (yinli[index]*100.0)/(element.touruAmount*1.0);
                if(index !=0) {
                    yinliStr.append("至");
                    percentStr.append("至");
                }
                yinliStr.append(yinli[index]);
                try {
                    percentStr.append(Utils.formatDoubleForTwo(percent[index]) +"%");
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    percentStr.append("--%");
                }
            }
            element.yingliAmount = yinliStr.toString();
            element.yingliProfit = percentStr.toString();
            boolean isRecompute = false;
            int targetAward = 0;
            int preTouru = 0;//前提投入
            if(i > 0) {
                preTouru = mData.get(i-1).touruAmount;
            }
            if(percent.length == 1 && yinli[0] < profitBonusValue) {
                isRecompute = true;
                targetAward = perAward[0];
            }else if(percent.length == 2 && Math.max(yinli[0], yinli[1]) < profitBonusValue) {
                isRecompute = true;
                targetAward = perAward[0];
            }
            if(isRecompute) {
                int newBeishu = 0;
                newBeishu = (int) ((profitBonusValue + preTouru)/(targetAward - element.beishu*element.zhushu*2)) + 1;
                if(newBeishu >9999|| newBeishu < element.beishu) {
                    Toast.makeText(this, "第"+qihaos[i]+"期开始无法计算方案，可重新设置", Toast.LENGTH_SHORT).show();
                    updateIssue(i + 1);
                    break;
                }
                element.touruAmount =  newBeishu * element.zhushu*2 + preTouru;
                element.beishu = newBeishu;
                StringBuilder yinliStr1 = new StringBuilder();
                StringBuilder percentStr1 = new StringBuilder();
                for(int index=0; index< perAward.length; index++) {
                    yinli[index] = element.beishu *perAward[index] - element.touruAmount ;
                    percent[index] = (yinli[index]*100.0)/(element.touruAmount*1.0);
                    if(index !=0) {
                        yinliStr1.append("至");
                        percentStr1.append("至");
                    }
                    yinliStr1.append(yinli[index]);
                    try {
                        percentStr1.append(Utils.formatDoubleForTwo(percent[index]) +"%");
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        percentStr1.append("--%");
                    }
                }
                element.yingliAmount = yinliStr1.toString();
                element.yingliProfit = percentStr1.toString();
            }
            mData.add(element) ;
        }
        mAdapter.notifyDataSetChanged();
        if(mData.size() == 0) {
            mBetMoney.setText("0");
        } else {
            issueEdit.setText(mZhuiHaoNums + "");
            issueCountTxt.setText(mZhuiHaoNums + "");
            mBetMoney.setText(mData.get(mData.size()-1).touruAmount+"");
        }
        mIsPlanOK = true;
        updatePlanInfo();
    }


    private void _requestData() {
        JinCaiZiHttpClient.closeExpireConnection();
        mQihaoView.setText("正在获取当前期号");
        mTimeDiffView.setText("");
        RequestParams params = new RequestParams();
        params.add("act", "sellqihao");

        params.add("lotterytype", lotterytype);
        params.add("datatype", "json");
        params.add("jsoncallback",
                "jsonp" + String.valueOf(System.currentTimeMillis()));
        params.add("_", String.valueOf(System.currentTimeMillis()));
        String url = AsyncHttpClient.getUrlWithQueryString(false,
                JinCaiZiHttpClient.BASE_URL, params);
        JinCaiZiHttpClient.post(this, url,
                new AsyncHttpResponseHandler() {


                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          final byte[] responseBody) {


                        try {
                            String charset;
                            if(Utils.isCmwapNet(SmartFollow.this)) {
                                charset = "utf-8";
                            } else {
                                charset = "gb2312";
                            }
                            String jsonData = new String(responseBody, charset);
                            Log.d(TAG, "syxw qihao detail = " + jsonData);
                            _readQihaoFromJson(jsonData);
                            _initData();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                            mQihaoView.setText("获取期号失败，点击重新获取");
                            mTimeDiffView.setText("");
                        } catch (Exception e) {
                            e.printStackTrace();
                            mQihaoView.setText("获取期号失败，点击重新获取");
                            mTimeDiffView.setText("");
                        } finally {
                            // FIXME reader.close should be here
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          byte[] responseBody, Throwable error) {
                        Log.d(TAG, "failure = " + error.toString());
                        mQihaoView.setText("获取期号失败，点击重新获取");
                        mTimeDiffView.setText("");
                        Toast.makeText(SmartFollow.this, "期号获取失败", Toast.LENGTH_SHORT).show();
                        _requestData();
                    }
                });
    }
    private String mQihao = "";
    private MyCount mc = null;
    private void _readQihaoFromJson(String jsonData) throws IOException {
        JsonReader reader = new JsonReader(new StringReader(jsonData));
        reader.beginObject();
        String diff = "-1";
        int result = -1;
        while(reader.hasNext()) {
            String tagName = reader.nextName();
            if(tagName.equals("msg")) {
                mQihao = reader.nextString();
            } else if(tagName.equals("Diff")){
                diff = reader.nextString();
            } else if(tagName.equals("result")) {
                result = reader.nextInt();
            }
        }
        reader.endObject();
        reader.close();
        if(result == 0) {

            if(diff.startsWith("-")) {
                mQihaoView.setText(mQihao + "期代购截止");
                _requestData();
            } else {
                mQihaoView.setText("距" + mQihao + "期还有");
                if (TextUtils.isEmpty(diff))
                {
                    diff = "540";
                }
                mc = new MyCount(Long.valueOf(diff)*1000, 1000);
                mc.start();
            }
        }
    }
    /*定义一个倒计时的内部类*/
    class MyCount extends CountDownTimer {
        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }
        @Override
        public void onFinish() {
            //mQihaoView.setText("正在获取当前期号");
            Toast.makeText(SmartFollow.this, mQihao+"已过期， 将自动切换至下一期", Toast.LENGTH_SHORT).show();
            _requestData();

        }
        @Override
        public void onTick(long millisUntilFinished) {
            //Log.d("test", millisUntilFinished+"");
            mTimeDiffView.setText(Utils.formatDuring(millisUntilFinished));

        }
    }

    private void _postFormData() {
        _showTouzhuProgress();

        RequestParams rawParams = new RequestParams();

        rawParams.add("currentQihao", mQihao);
        rawParams.add("isZhuihao", "true");
        if (isStopZhui) {
            rawParams.add("autoStopZhuihao", "true");
        } else {
            rawParams.add("autoStopZhuihao", "false");
        }
        rawParams.add("itemsContent", itemContent);
        rawParams.add("itemsSelectType", typeContent);
        rawParams.add("itemsBetCount", betCount);

        StringBuilder qhBuilder = new StringBuilder(mData.get(0).qihao+"");
        StringBuilder msBuilder = new StringBuilder(mData.get(0).beishu + "");
        for(int i = 1; i< mData.size() ; i++) {
            qhBuilder.append(";"+mData.get(i).qihao);
            msBuilder.append(";"+mData.get(i).beishu);
        }
        // Log.d(TAG, "qihaos = " + qhBuilder.toString());
        rawParams.add("qihaos", qhBuilder.toString());
        rawParams.add("multiples", msBuilder.toString());
        rawParams.add("shoptype", "Daigou");

        rawParams.add("type", lotterytype);
        rawParams.add("zhuihaoTitle", mTitleStr);
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
                    if(Utils.isCmwapNet(SmartFollow.this)) {
                        charset = "utf-8";
                    } else {
                        charset = "gb2312";
                    }
                    String jsonData = new String(responseBody, charset);
                    Log.d(TAG, "post ssq form data = " + jsonData);
                    parseTouzhuJson(jsonData);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Toast.makeText(SmartFollow.this, "数据可能异常，请检查数据请求是否成功，以免重复请求造成损失", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(SmartFollow.this, "数据可能异常，请检查数据请求是否成功，以免重复请求造成损失", Toast.LENGTH_LONG).show();
                } finally {
                    // FIXME reader.close should be here
                    final Dialog localDialog = new Dialog(SmartFollow.this, R.style.Theme_dialog);
                    View view = LayoutInflater.from(getApplicationContext()).inflate(
                            R.layout.dialog_submit_bet, null);
                    TextView dialogTitle = (TextView) view
                            .findViewById(R.id.submit_dialog_title);
                    dialogTitle.setText("提示");
                    TextView dialogContent = (TextView) view
                            .findViewById(R.id.submit_dialog_content);
                    TextView localCancel = (TextView) view.findViewById(R.id.tv_submit_cancel);
                    localCancel.setVisibility(View.GONE);
                    ////view.findViewById(R.id.dialog_divider).setVisibility(View.GONE);
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
                                if(mc != null) {
                                    mc.cancel();
                                }
                                finish();
                            } else if(!TextUtils.isEmpty(responseMsg) && responseMsg.equals("未登录或登录过期！")) {
                                CheckLogin.clearLoginStatus(sp);
                                Intent loginIntent = new Intent(SmartFollow.this, Login.class);
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
                Toast.makeText(SmartFollow.this, responseMsg, Toast.LENGTH_SHORT).show();
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
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        if(mc != null) {
            mc.cancel();
        }
        finish();

    }

    private void _showDaigouDialog() {
        int myBuy = mData.get(mData.size()-1).touruAmount;
        final Dialog localDialog = new Dialog(SmartFollow.this, R.style.Theme_dialog);
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

    private class MyIssueWatcher implements TextWatcher {

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
                issueEdit.setText("" + MINZHUIHAO);
            } else if(Integer.parseInt(s.toString()) > MAXZHUIHAO) {
                issueEdit.setText(""+MAXZHUIHAO);
            }

            mZhuiHaoNums = Integer.parseInt(issueEdit.getText().toString());
            issueCountTxt.setText("" + mZhuiHaoNums);
            issueEdit.setSelection(issueEdit.getText().length());
        }
    }

    private void updateIssue(int issueCount)
    {
        mZhuiHaoNums = issueCount;

        issueEdit.setText(mZhuiHaoNums + "");
        issueCountTxt.setText(mZhuiHaoNums + "");
        periodValue.setText(mZhuiHaoNums + "");
    }
}
