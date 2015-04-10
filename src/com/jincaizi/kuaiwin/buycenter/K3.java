package com.jincaizi.kuaiwin.buycenter;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import com.jincaizi.common.IntentData;
import com.jincaizi.kuaiwin.chart.activities.TableFour;
import com.jincaizi.kuaiwin.chart.activities.TableThree;
import com.jincaizi.kuaiwin.chart.activities.TestTable;
import com.jincaizi.kuaiwin.chart.activities.TestTableTwo;
import com.jincaizi.kuaiwin.utils.*;
import com.jincaizi.requesters.LotteryLeakRequester;
import org.apache.http.Header;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.gson.stream.JsonReader;
import com.jincaizi.R;
import com.jincaizi.adapters.PopViewAdapter;
import com.jincaizi.http.JinCaiZiHttpClient;
import com.jincaizi.kuaiwin.utils.Constants.K3Type;
import com.jincaizi.kuaiwin.widget.ShakeListener;
import com.jincaizi.kuaiwin.widget.ShakeListener.OnShakeListener;
import com.jincaizi.vendor.http.AsyncHttpClient;
import com.jincaizi.vendor.http.AsyncHttpResponseHandler;
import com.jincaizi.vendor.http.RequestParams;


public class K3 extends FragmentActivity implements OnClickListener {
    public static final String CITY = "city";
    protected static final String TAG = "K3";
    private TextView mTitleView;
    private RelativeLayout titleSelectorLayout;
    private PopupWindow mPopWindow;
    private boolean isPopWindowShow = false;
    private Fragment mCurrentFragment;
    //	private TextView mShakeBtn;
    public K3Type syyType = K3Type.hezhi;
    int lastIndex = 0;
    private TextView mZhuShuView;
    private TextView priceTextView;
    private TextView right_footer_btn;
    private RelativeLayout clearPick;
    private RelativeLayout mBack;
    private GridView mGridNormalView;
    private GridView mGridDragView;
    private PopViewAdapter mMyNormalAdapter, mMyDragAdapter;
    private ArrayList<Boolean> mNormalChecked = new ArrayList<Boolean>();
    private ArrayList<Boolean> mDragChecked = new ArrayList<Boolean>();
    private String mCity;
    private TextView mPsInfoView;
    //	private TextView mYilouView;
    private TextView chart;
    public FrameLayout mParentFrameLayout;
    public int mCaseIndex = 0;
    public ToggleButton[] tbArrays = new ToggleButton[100];
    public ToggleButton[] tbDragArrays_two = new ToggleButton[12];
    public ToggleButton[] tbDragArrays_three = new ToggleButton[12];
    private ShakeListener mShakeListener;
    private int startType = 0; // 0, normal; 1, continuePick; 2, selectedAgain
    private TextView mQihaoView;
    private TextView mTimeDiffView;
    private String lotterytype;

    private LinearLayout buyTipsLayout;
    private TextView lotteryMoney;
    private TextView profit;

    private ArrayList<String> currentMiss;
    private ArrayList<Integer> selectedNumbers;
    private String prevBetType;

    private LotteryLeakRequester requester;
    private MyBroadcastReceiver broadcastReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pls);
        mCity = getIntent().getStringExtra(CITY);
        mNormalChecked.add(true);
        for (int i = 1; i < 5; i++) {
            mNormalChecked.add(false);
        }
        for(int i=0; i<2; i++) {
            mDragChecked.add(false);
        }
        initCurrentMiss();
        selectedNumbers = new ArrayList<Integer>();
        _getLotteryType();
        _isForStartForResult();
        _findViews();
        _setListner();
        _registerSensorListener();
        _requestData(true);
    }

    private void initCurrentMiss()
    {
        if (currentMiss == null)
        {
            currentMiss = new ArrayList<String>(11);
            for (int i = 0; i < 11; i++) {
                currentMiss.add(i, "0");
            }
        }
        else
        {
            for (int i = 0; i < 11; i++) {
                currentMiss.set(i, "0");
            }
        }
    }

    private void _getLotteryType() {
        if(mCity.equals(Constants.City.jiangsu.toString())) {
            lotterytype = "JSK3";
        } else if(mCity.equals(Constants.City.anhui.toString())) {
            lotterytype = "AHK3";
        }else if(mCity.equals(Constants.City.jilin.toString())) {
            lotterytype = "JLK3";
        }else if (mCity.equals(Constants.City.hubei.toString())){
            lotterytype = "HBK3";
        } else if (mCity.equals(Constants.City.neimenggu.toString())){
            lotterytype = "NMGK3";
        }
    }
    private void _isForStartForResult() {
        Intent intent = getIntent();

        if (TextUtils.isEmpty(intent.getAction()))
        {
            _showFragments(K3_hz_fragment.TAG_HZ);
        }
        else
        {
            if (intent.getAction().equals(IntentAction.CONTINUEPICKBALL))
            {
                startType = 1;
            }
            else if (intent.getAction().equals(IntentAction.RETRYPICKBALL))
            {
                startType = 2;
                selectedNumbers = (ArrayList<Integer>)intent.getSerializableExtra(IntentData.SELECT_NUMBERS);
            }

            String betType = intent.getStringExtra(IntentData.BET_TYPE);

            if (betType.equals(K3Type.hezhi.toString()))
            {
                prevBetType = K3_hz_fragment.TAG_HZ;
                syyType = K3Type.hezhi;
                mCaseIndex = 0;
                updatePopWindow(0);
                _showFragments(K3_hz_fragment.TAG_HZ);
            }
            else if (betType.equals(K3Type.threesamesingle.toString()) ||
                    betType.equals(K3Type.threesamedouble.toString()))
            {
                prevBetType = K3_hz_fragment.TAG_THREE_SAME;
                syyType = K3Type.threesamesingle;
                mCaseIndex = 1;
                updatePopWindow(1);
                _showFragments(K3_hz_fragment.TAG_THREE_SAME);
            }
            else if (betType.equals(K3Type.twosamesingle.toString()) ||
                    betType.equals(K3Type.twosamedouble.toString()))
            {
                prevBetType = K3_hz_fragment.TAG_TWO_SAME;
                syyType = K3Type.twosamesingle;
                mCaseIndex = 2;
                updatePopWindow(2);
                _showFragments(K3_hz_fragment.TAG_TWO_SAME);
            }
            else if (betType.equals(K3Type.threedifsingle.toString()) ||
                    betType.equals(K3Type.threedifdouble.toString()))
            {
                prevBetType = K3_hz_fragment.TAG_THREE_DIF;
                syyType = K3Type.threedifsingle;
                mCaseIndex = 3;
                updatePopWindow(3);
                _showFragments(K3_hz_fragment.TAG_THREE_DIF);
            }
            else if (betType.equals(K3Type.twodif.toString()))
            {
                prevBetType = K3_hz_fragment.TAG_TWO_DIF;
                syyType = K3Type.twodif;
                mCaseIndex = 4;
                updatePopWindow(4);
                _showFragments(K3_hz_fragment.TAG_TWO_DIF);
            }
            else if (betType.equals(K3Type.dragthree.toString()))
            {
                prevBetType = K3_hz_fragment.TAG_THREE_DIF_DRAG;
                syyType = K3Type.dragthree;
                mCaseIndex = 5;
                updatePopWindow(5);
                _showFragments(K3_hz_fragment.TAG_THREE_DIF_DRAG);
            }
            else if (betType.equals(K3Type.dragtwo.toString()))
            {
                prevBetType = K3_hz_fragment.TAG_TWO_DIF_DRAG;
                syyType = K3Type.dragtwo;
                mCaseIndex = 6;
                updatePopWindow(6);
                _showFragments(K3_hz_fragment.TAG_TWO_DIF_DRAG);
            }
        }
    }

    private void setTitle()
    {
        switch (mCaseIndex)
        {
            case 0:
                mTitleView.setText("快3-和值");
                break;
            case 1:
                mTitleView.setText("快3-三同号");
                break;
            case 2:
                mTitleView.setText("快3-二同号");
                break;
            case 3:
                mTitleView.setText("快3-三不同号");
                break;
            case 4:
                mTitleView.setText("快3-二不同号");
                break;
            case 5:
                mTitleView.setText("快3-三不同号(胆拖)");
                break;
            case 6:
                mTitleView.setText("快3-二不同号(胆拖)");
                break;
            default:
                break;
        }
    }

    private void updatePopWindow(int selectIndex)
    {
        _initNormalChecked();
        mNormalChecked.set(selectIndex, true);
        if (mMyDragAdapter != null)
        {
            mMyDragAdapter.notifyDataSetChanged();
        }
    }

    private void _registerSensorListener() {

        mShakeListener = new ShakeListener(this);
        mShakeListener.setOnShakeListener(new shakeLitener());
        mShakeListener.setSPEED_SHRESHOLD(2000);
        mShakeListener.setUPTATE_INTERVAL_TIME(160);
    }
    class shakeLitener implements OnShakeListener {
        @Override
        public void onShake() {
            // TODO Auto-generated method stub
            if(mCaseIndex == 5 ||mCaseIndex == 6) {
                return;
            }
            if(mCurrentFragment == null) {
                return;
            }
            K3_hz_fragment fragment = (K3_hz_fragment)mCurrentFragment;

            if(fragment.myDialog != null && fragment.myDialog.isShowing()) {
                return;
            }
            fragment._showDiceFrame(mCaseIndex);
            // mShakeListener.stop();
        }

    }

    //在pause里停止震动响应
    @Override
    protected void onPause() {
        super.onPause();
        mShakeListener.stop();
    }

    private void _initNormalChecked() {
        mNormalChecked.clear();
        for (int i = 0; i < 5; i++) {
            mNormalChecked.add(false);
        }
    }

    private void _initDragChecked() {
        mDragChecked.clear();
        for(int i=0; i<2; i++) {
            mDragChecked.add(false);
        }
    }
    private void _setListner() {
        // TODO Auto-generated method stub
        titleSelectorLayout.setOnClickListener(this);
//		mShakeBtn.setOnClickListener(this);
        right_footer_btn.setOnClickListener(this);
        clearPick.setOnClickListener(this);
        mBack.setOnClickListener(this);
//		mYilouView.setOnClickListener(this);
        chart.setOnClickListener(this);
        mQihaoView.setOnClickListener(this);
    }
    private void _findViews() {
        // TODO Auto-generated method stub
        RelativeLayout mHeaderBar = (RelativeLayout)findViewById(R.id.pl3_head_bar);
        mHeaderBar.setBackgroundColor(this.getResources().getColor(R.color.k3_orange));
        titleSelectorLayout = (RelativeLayout) findViewById(R.id.title_selector);
        mTitleView = (TextView) findViewById(R.id.current_lottery);
        setTitle();
        mTitleView.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        mZhuShuView = (TextView) findViewById(R.id.bet_txt_2);
        priceTextView = (TextView) findViewById(R.id.bet_txt_4);
        right_footer_btn = (TextView) findViewById(R.id.right_footer_btn);
        clearPick = (RelativeLayout) findViewById(R.id.left_footer_btn);
        mBack = (RelativeLayout) findViewById(R.id.left_layout);

        buyTipsLayout = (LinearLayout) findViewById(R.id.buy_tips_layout);
        lotteryMoney = (TextView) findViewById(R.id.lottery_money);
        profit = (TextView) findViewById(R.id.profit);

        chart = (TextView) findViewById(R.id.sumbit_group_buy);
        chart.setText(getResources().getString(R.string.chart));

        mParentFrameLayout = (FrameLayout)findViewById(R.id.fl_pls_pickarea);

        mQihaoView = (TextView)findViewById(R.id.pre_num_str);
        mTimeDiffView = (TextView)findViewById(R.id.pre_win_num);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        mShakeListener.start();
    }

    public void setTouzhuResult(int count) {
        mZhuShuView.setText(String.valueOf(count));
        priceTextView.setText(String.valueOf(count * 2));
    }


    public void setBuyTips(int max, int min, int count)
    {
        if (count <= 0)
        {
            buyTipsLayout.setVisibility(View.GONE);
            return;
        }

        buyTipsLayout.setVisibility(View.VISIBLE);
        if (count == 1 || min == max) {
            lotteryMoney.setText(String.valueOf(min));
            profit.setText(String.valueOf(min - 2 * count));
        }
        else
        {
            lotteryMoney.setText(String.valueOf(min) + "~" + String.valueOf(max));
            profit.setText(String.valueOf(min - 2 * count) + "~" + String.valueOf(max - 2 * count));
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.pre_num_str:
                if(TextUtils.isEmpty(mQihaoView.getText()) ||
                        mQihaoView.getText().toString().equals("获取期号失败，点击重新获取")) {
                    _requestData(true);
                }
                break;
            case R.id.sumbit_group_buy:
//			UiHelper.startSyxwYilou(this, lotterytype,Constants.City.getCityName(mCity)+"快3");
                goToChart();
                break;
            case R.id.left_layout:
                if(mc != null) {
                    mc.cancel();
                }
                finish();
                break;
            case R.id.left_footer_btn:
                ((K3_hz_fragment)mCurrentFragment).clearTouzhu();
                break;
            case R.id.title_selector:
                if (!isPopWindowShow) {
                    _setPopWindow();
                    isPopWindowShow = true;
                }
                mPopWindow.showAsDropDown(v, -(v.getWidth() / 4), v.getTop());
                break;
            case R.id.right_footer_btn:
                K3_hz_fragment fragment = (K3_hz_fragment)mCurrentFragment;
                if(!isCanSale) {
                    Toast.makeText(getApplicationContext(), "本期销售已停止",Toast.LENGTH_SHORT).show();
                    break;
                }
                if(TextUtils.isEmpty(mQihao)) {
                    Toast.makeText(getApplicationContext(), "未获取到当前期号",Toast.LENGTH_SHORT).show();
                    break;
                }
                if(_isReadyStart(fragment)) {
                    _startK3Pick(fragment);
                }
                break;
            default:
                break;
        }
    }

    private void goToChart()
    {
        Intent chartActivity = new Intent();

        //和值
        if (syyType == K3Type.hezhi)
        {
            chartActivity.setClass(K3.this, TestTable.class);
            if (mCurrentFragment instanceof K3_hz_fragment)
            {
                chartActivity.putExtra(IntentData.SELECT_NUMBERS,
                        ((K3_hz_fragment) mCurrentFragment).getSelectionList(K3Type.hezhi.toString()));
            }
        }
        //三同号 三不同号 三不胆拖 --> 形态走势
        else if (syyType == K3Type.threesamesingle)
        {
            chartActivity.setClass(K3.this, TestTableTwo.class);
            chartActivity.putExtra(IntentData.THREE_NUMBER_TYPE, TestTableTwo.TYPE_THREE_EQUAL);
            if (mCurrentFragment instanceof K3_hz_fragment)
            {
                chartActivity.putExtra(IntentData.SELECT_NUMBERS,
                        ((K3_hz_fragment) mCurrentFragment).getSelectionList(K3Type.threesamesingle.toString()));
            }
        }
        else if (syyType == K3Type.threedifsingle || syyType == K3Type.dragthree)
        {
            chartActivity.setClass(K3.this, TestTableTwo.class);
            chartActivity.putExtra(IntentData.THREE_NUMBER_TYPE, TestTableTwo.TYPE_THREE_NOT_EQUAL);
            chartActivity.putExtra(IntentData.DRAG, syyType == K3Type.dragthree);
            if (syyType == K3Type.threedifsingle && mCurrentFragment instanceof K3_hz_fragment)
            {
                chartActivity.putExtra(IntentData.SELECT_NUMBERS,
                        ((K3_hz_fragment) mCurrentFragment).getSelectionList(K3Type.threedifsingle.toString()));
            }
        }
        //二同号
        else if (syyType == K3Type.twosamesingle)
        {
            chartActivity.setClass(K3.this, TableThree.class);
        }
        //二不同号+胆拖
        else if (syyType == K3Type.twodif || syyType == K3Type.dragtwo)
        {
            chartActivity.setClass(K3.this, TableFour.class);
            chartActivity.putExtra(IntentData.DRAG, syyType == K3Type.dragtwo);
            if (syyType == K3Type.twodif && mCurrentFragment instanceof K3_hz_fragment)
            {
                chartActivity.putExtra(IntentData.SELECT_NUMBERS,
                        ((K3_hz_fragment) mCurrentFragment).getSelectionList(K3Type.twodif.toString()));
            }
        }

        chartActivity.putExtra(IntentData.CITY, lotterytype);
        startActivityForResult(chartActivity, syyType.ordinal());
    }

    private boolean _isReadyStart(K3_hz_fragment fragment) {
        if(fragment.mZhushu < 1) {
            Toast.makeText(this, "请至少选择1注", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void _startK3Pick(K3_hz_fragment fragment) {
        fragment.getBetResult();
        Intent intent = new Intent();
        intent.putExtra(K3Pick.CITY, mCity);
        intent.putExtra(K3Pick.BETTYPE, fragment.betTypes);
        intent.putExtra(K3Pick.BALL,fragment.betResult);
        intent.putExtra(K3Pick.ZHUAMOUNT,fragment.mZhushu);
        intent.putExtra(IntentData.SELECT_NUMBERS, fragment.selectedNumber);
        intent.putExtra("qihao", mQihao);
        if (startType == 0) {
            intent.setClass(this, K3Pick.class);
            startActivity(intent);
        } else if (startType == 1 || startType == 2) {
            setResult(RESULT_OK, intent);
        }
        if(mc != null) {
            mc.cancel();
        }
        finish();
    }
    private void _setPopWindow() {
        View view = LayoutInflater.from(this).inflate(R.layout.syxw_popview_layout, null);
        mPopWindow = new PopupWindow(view, LayoutParams.WRAP_CONTENT,
                LayoutParams.MATCH_PARENT);
        mGridNormalView = (GridView) view.findViewById(R.id.pop_normal_gridview);
        mGridNormalView.setBackgroundColor(this.getResources().getColor(
                android.R.color.transparent));
        mGridNormalView.setNumColumns(3);
        mGridDragView = (GridView) view.findViewById(R.id.pop_drag_gridview);
        mGridDragView.setBackgroundColor(this.getResources().getColor(
                android.R.color.transparent));
        mGridDragView.setNumColumns(3);
        final ArrayList<String> listNormal = new ArrayList<String>();
        listNormal.add("和值");
        listNormal.add("三同号");
        listNormal.add("二同号");
        listNormal.add("三不同号");
        listNormal.add("二不同号");

        final ArrayList<String> listTips = new ArrayList<String>();
        listTips.add("奖金9-240元");
        listTips.add("奖金40-240元");
        listTips.add("奖金15-80元");
        listTips.add("奖金10-40元");
        listTips.add("奖金8元");
        mMyNormalAdapter = new PopViewAdapter(this, listNormal, listTips, mNormalChecked);
        mGridNormalView.setAdapter(mMyNormalAdapter);
        mGridNormalView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                mCaseIndex = arg2;
                switch(arg2) {
                    case 0:
                        _showFragments(K3_hz_fragment.TAG_HZ);
                        syyType = K3Type.hezhi;
                        break;
                    case 1:
                        _showFragments(K3_hz_fragment.TAG_THREE_SAME);
                        syyType = K3Type.threesamesingle;
                        break;
                    case 2:
                        _showFragments(K3_hz_fragment.TAG_TWO_SAME);
                        syyType = K3Type.twosamesingle;
                        break;
                    case 3:
                        _showFragments(K3_hz_fragment.TAG_THREE_DIF);
                        syyType = K3Type.threedifsingle;
                        break;
                    case 4:
                        _showFragments(K3_hz_fragment.TAG_TWO_DIF);
                        syyType = K3Type.twodif;
                        break;
                    default:
                        break;
                }
                int index = mNormalChecked.indexOf(true);
                if(index != -1) {
                    mNormalChecked.set(index, false);
                }
                mNormalChecked.set(arg2, true);
                _initDragChecked();
                mMyDragAdapter.notifyDataSetChanged();
                mMyNormalAdapter.notifyDataSetChanged();
                mTitleView.setText("快3-"+listNormal.get(arg2));
                mPopWindow.dismiss();

            }
        });
        final ArrayList<String>mDragList = new ArrayList<String>();
        mDragList.add("三不同号");
        mDragList.add("二不同号");

        final ArrayList<String> dragTips = new ArrayList<String>();
        dragTips.add("奖金40元");
        dragTips.add("奖金8元");
        mMyDragAdapter = new PopViewAdapter(this, mDragList, dragTips, mDragChecked);
        mGridDragView.setAdapter(mMyDragAdapter);
        mGridDragView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                mCaseIndex = arg2 + 5;
                switch(arg2) {
                    case 0:
                        _showFragments(K3_hz_fragment.TAG_THREE_DIF_DRAG);
                        syyType = K3Type.dragthree;
                        break;
                    case 1:
                        _showFragments(K3_hz_fragment.TAG_TWO_DIF_DRAG);
                        syyType = K3Type.dragtwo;
                        break;
                    default:
                        break;
                }
                int index = mDragChecked.indexOf(true);
                if(index != -1) {
                    mDragChecked.set(index, false);
                }
                mDragChecked.set(arg2, true);
                _initNormalChecked();
                mMyNormalAdapter.notifyDataSetChanged();
                mMyDragAdapter.notifyDataSetChanged();
                mTitleView.setText("快3-"+mDragList.get(arg2)+"(胆拖)");
                mPopWindow.dismiss();
            }
        });

        RelativeLayout bottomShadow = (RelativeLayout)view.findViewById(R.id.popup_bottom);
        bottomShadow.setSoundEffectsEnabled(false);
        bottomShadow.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopWindow.dismiss();
            }
        });

        mPopWindow.setFocusable(true);
        mPopWindow.setOutsideTouchable(true);
        mPopWindow.update();
        mPopWindow.setBackgroundDrawable(new BitmapDrawable(getResources(),
                Bitmap.createBitmap(1, 1, Config.ARGB_8888)));
    }


    private void _showFragments(String fragmentTag) {
        FragmentManager mFragManager = getSupportFragmentManager();
        FragmentTransaction mFragTransaction = mFragManager.beginTransaction();
        Fragment mFragment = mFragManager.findFragmentByTag(fragmentTag);

        if (mFragment == null) {
            mFragment = new K3_hz_fragment();
            Bundle bundle = new Bundle();
            if (fragmentTag.equals(K3_hz_fragment.TAG_HZ))
            {
                bundle.putString(K3_hz_fragment.BETTYPE, K3Type.hezhi.toString());
            }
            else if(fragmentTag.equals(K3_hz_fragment.TAG_THREE_SAME))
            {
                bundle.putString(K3_hz_fragment.BETTYPE, K3Type.threesamesingle.toString());
            }
            else if(fragmentTag.equals(K3_hz_fragment.TAG_TWO_SAME))
            {
                bundle.putString(K3_hz_fragment.BETTYPE, K3Type.twosamesingle.toString());
            }
            else if(fragmentTag.equals(K3_hz_fragment.TAG_THREE_DIF))
            {
                bundle.putString(K3_hz_fragment.BETTYPE, K3Type.threedifsingle.toString());
            }
            else if(fragmentTag.equals(K3_hz_fragment.TAG_TWO_DIF))
            {
                bundle.putString(K3_hz_fragment.BETTYPE, K3Type.twodif.toString());
            }
            else if(fragmentTag.equals(K3_hz_fragment.TAG_THREE_DIF_DRAG))
            {
                bundle.putString(K3_hz_fragment.BETTYPE, K3Type.dragthree.toString());
            }
            else
            {
                bundle.putString(K3_hz_fragment.BETTYPE, K3Type.dragtwo.toString());
            }
            if (startType == 2 && prevBetType != null
                    && prevBetType.equals(fragmentTag))
            {
                bundle.putSerializable(IntentData.SELECT_NUMBERS, selectedNumbers);
            }
            mFragment.setArguments(bundle);

            mFragTransaction.add(R.id.fl_pls_pickarea, mFragment, fragmentTag);
        }
        if (mCurrentFragment != null) {
            mFragTransaction.hide(mCurrentFragment);
        }
        mFragTransaction.show(mFragment);
        mCurrentFragment = mFragment;
        mFragTransaction.commit();

    }
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        if(mc != null) {
            mc.cancel();
        }
        finish();
    }
    private void _requestData(boolean showText) {
        JinCaiZiHttpClient.closeExpireConnection();
        if (showText) {
            mQihaoView.setText("正在获取当前期号");
            mTimeDiffView.setText("");
        }
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
                                          byte[] responseBody) {
                        try {
                            String charset;
                            if (Utils.isCmwapNet(K3.this)) {
                                charset = "utf-8";
                            } else {
                                charset = "gb2312";
                            }
                            String jsonData = new String(responseBody, charset);
                            Log.d(TAG, "k3 qihao detail = " + jsonData);
                            _readQihaoFromJson(jsonData);
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
                    }
                });


        broadcastReceiver = new MyBroadcastReceiver();
        registerReceiver(broadcastReceiver, new IntentFilter("leak"));
        requester = new LotteryLeakRequester(this, lotterytype);
        requester.query();
    }

    private String mQihao = "";
    private boolean isCanSale = true;
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
                isCanSale = false;
                mQihaoView.setText(mQihao + "期代购截止");
                _requestData(false);
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK)
        {
            return;
        }
        ArrayList<Boolean> result = (ArrayList<Boolean>)data.getSerializableExtra(IntentData.SELECT_NUMBERS);
        if (mCurrentFragment instanceof K3_hz_fragment)
        {
            ((K3_hz_fragment) mCurrentFragment).updateChoice(result, syyType.toString());
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
            _requestData(false);
        }
        @Override
        public void onTick(long millisUntilFinished) {
            //Log.d("test", millisUntilFinished+"");
            mTimeDiffView.setText(Utils.formatDuring(millisUntilFinished));

        }
    }
    private class MyBroadcastReceiver extends BroadcastReceiver
    {

        @Override
        public void onReceive(Context context, Intent intent) {
            String type = intent.getStringExtra("type");
            if (type == null || !type.equals(lotterytype))
            {
                return;
            }

            if (intent.getBooleanExtra("success",false))
            {
                _readyilouFromJson(intent.getStringExtra("JSON"));
            }
        }
    }
    private void _readyilouFromJson(String jsonData) {
        final JsonReader reader = new JsonReader(new StringReader(jsonData));
        SafeAsyncTask<Boolean> getAllDaigouTask = new SafeAsyncTask<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                initCurrentMiss();
                int count = 0;
                reader.beginArray();
                while (reader.hasNext() && count < 11) {
                    reader.beginObject();
                    while (reader.hasNext()) {
                        String tagName = reader.nextName();
                        if (tagName.equals("nowyilou")) {
                            currentMiss.set(count, reader.nextString());
                        }
                        else
                        {
                            reader.nextString();
                        }
                    }
                    reader.endObject();
                    count++;
                }
                reader.endArray();
                return true;
            }

            @Override
            protected void onSuccess(Boolean t) throws Exception {
                // TODO Auto-generated method stub
                super.onSuccess(t);
                if (mCurrentFragment != null) {
                    ((K3_hz_fragment) mCurrentFragment).notifyLeakUpdate();
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

    public ArrayList<String> getCurrentMiss() {
        return currentMiss;
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }
}
