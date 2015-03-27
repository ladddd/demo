package com.jincaizi.kuaiwin.buycenter;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import com.jincaizi.common.IntentData;
import com.jincaizi.kuaiwin.chart.activities.ElevenFiveTableFour;
import com.jincaizi.kuaiwin.chart.activities.ElevenFiveTableOne;
import com.jincaizi.kuaiwin.chart.activities.ElevenFiveTableThree;
import com.jincaizi.kuaiwin.chart.activities.ElevenFiveTableTwo;
import com.jincaizi.kuaiwin.utils.SafeAsyncTask;
import com.jincaizi.requesters.LotteryLeakRequester;
import org.apache.http.Header;

import com.google.gson.stream.JsonReader;
import com.jincaizi.adapters.PopViewAdapter;
import com.jincaizi.http.JinCaiZiHttpClient;
import com.jincaizi.R;
import com.jincaizi.kuaiwin.utils.Constants;
import com.jincaizi.kuaiwin.utils.Constants.ShiyiyunType;
import com.jincaizi.kuaiwin.utils.IntentAction;
import com.jincaizi.kuaiwin.utils.Utils;
import com.jincaizi.vendor.http.AsyncHttpClient;
import com.jincaizi.vendor.http.AsyncHttpResponseHandler;
import com.jincaizi.vendor.http.RequestParams;


public class Syxw extends FragmentActivity implements OnClickListener {
    public static final String CITY = "city";
    protected static final String TAG = "Syxw";
    private TextView mTitleView;
    private RelativeLayout titleSelectorLayout;
    private PopupWindow mPopWindow;
    private boolean isPopWindowShow = false;
    private Fragment mCurrentFragment;
//    private TextView mShakeBtn;
    public ShiyiyunType syyType = Constants.ShiyiyunType.ANYTWO;
    int lastIndex = 0;
    private TextView mZhuShuView;
    private int mCount = 0;
    private TextView right_footer_btn;
    private int startType = 0; // 0, normal; 1, continuePiack; 2, selectedAgain
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
    private String prefix = "11选5";
    private TextView mQihaoView;
    private TextView mTimeDiffView;
    private String lotterytype;

    private TextView lotteryCount;
    private TextView price;

    private LinearLayout buyTipsLayout;
    private TextView lotteryMoney;
    private TextView profit;

    private ArrayList<String> currentMiss;

    private LotteryLeakRequester requester;
    private MyBroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pls);
        mCity = getIntent().getStringExtra(CITY);
        if(mCity.equals(Constants.City.shandong.toString())) {
            prefix = "十一运夺金";
        }

        mNormalChecked.add(true);
        for (int i = 1; i < 12; i++) {
            mNormalChecked.add(false);
        }
        for(int i=0; i<9; i++) {
            mDragChecked.add(false);
        }
        currentMiss = new ArrayList<String>();
        _findViews();
        _setListner();
        _loadFragment();
        _getLotteryType();
        _requestData();
    }
    private void _getLotteryType() {
        if(mCity.equals(Constants.City.shandong.toString())) {
            lotterytype = "SD11x5";
        } else if(mCity.equals(Constants.City.jiangxi.toString())) {
            lotterytype = "JX11x5";
        }else if(mCity.equals(Constants.City.guangdong.toString())) {
            lotterytype = "GD11x5";
        }else if(mCity.equals(Constants.City.anhui.toString())) {
            lotterytype = "AH11x5";
        }else if(mCity.equals(Constants.City.chongqing.toString())) {
            lotterytype = "CQ11x5";
        }else if(mCity.equals(Constants.City.liaoning.toString())) {
            lotterytype = "LN11x5";
        }else if(mCity.equals(Constants.City.shanghai.toString())) {
            lotterytype = "SH11x5";
        }else if(mCity.equals(Constants.City.heilongjiang.toString())) {
            lotterytype = "HLJ11x5";
        }
    }
    private void _initNormalChecked() {
        mNormalChecked.clear();
        for (int i = 0; i < 12; i++) {
            mNormalChecked.add(false);
        }
    }
    private void _initDragChecked() {
        mDragChecked.clear();
        for(int i=0; i<9; i++) {
            mDragChecked.add(false);
        }
    }
    private void _setListner() {
        // TODO Auto-generated method stub
        titleSelectorLayout.setOnClickListener(this);
        right_footer_btn.setOnClickListener(this);
        clearPick.setOnClickListener(this);
        mBack.setOnClickListener(this);
//		mYilouView.setOnClickListener(this);
        chart.setOnClickListener(this);
        mQihaoView.setOnClickListener(this);
    }

    private void _findViews() {
        // TODO Auto-generated method stub
        mTitleView = (TextView) findViewById(R.id.current_lottery);
        mTitleView.setText(prefix+"-任选二(普通)");
        mTitleView.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
        titleSelectorLayout = (RelativeLayout) findViewById(R.id.title_selector);
        mZhuShuView = (TextView) findViewById(R.id.bet_zhushu);
        right_footer_btn = (TextView) findViewById(R.id.right_footer_btn);
        clearPick = (RelativeLayout) findViewById(R.id.left_footer_btn);
        mBack = (RelativeLayout) findViewById(R.id.left_layout);

        chart = (TextView)findViewById(R.id.sumbit_group_buy);
        chart.setText(getResources().getString(R.string.chart));

        buyTipsLayout = (LinearLayout) findViewById(R.id.buy_tips_layout);
        lotteryMoney = (TextView) findViewById(R.id.lottery_money);
        profit = (TextView) findViewById(R.id.profit);

        mQihaoView = (TextView)findViewById(R.id.pre_num_str);
        mTimeDiffView = (TextView)findViewById(R.id.pre_win_num);

        lotteryCount = (TextView) findViewById(R.id.bet_txt_2);
        price = (TextView) findViewById(R.id.bet_txt_4);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

    }
    private void _loadFragment() {
        Intent intent = getIntent();
        if (!TextUtils.isEmpty(intent.getAction()) && (intent.getAction().equals(IntentAction.CONTINUEPICKBALL) ||
                        intent.getAction().equals(IntentAction.RETRYPICKBALL)))
        {
            String betType = intent.getStringExtra(IntentData.BET_TYPE);
            ArrayList<Integer> indexList = (ArrayList<Integer>)intent.getSerializableExtra(IntentData.SELECTION_INDEX);

            if (intent.getAction().equals(IntentAction.CONTINUEPICKBALL))
            {
                startType = 1;
            }
            else
            {
                startType = 2;
            }

            if (betType.equals(ShiyiyunType.ANYTWO.toString())) {
                _showFragments(AnytwoFragment.TAG);
                AnytwoFragment zhixuanFragment = ((AnytwoFragment) mCurrentFragment);
                zhixuanFragment.updateBallData(indexList);
                syyType = ShiyiyunType.ANYTWO;
                mTitleView.setText(prefix+"-任选二");
                int lastIndex = mNormalChecked.indexOf(true);
                if(lastIndex != -1) {
                    mNormalChecked.set(lastIndex, false);
                }
                mNormalChecked.set(0, true);
                _initDragChecked();
            } else if (betType.equals(ShiyiyunType.ANYTHREE.toString())) {
                _showFragments(AnythreeFragment.TAG);
                AnythreeFragment zhixuanFragment = ((AnythreeFragment) mCurrentFragment);
                zhixuanFragment.updateBallData(indexList);
                syyType = ShiyiyunType.ANYTHREE;
                mTitleView.setText(prefix+"-任选三");
                int lastIndex = mNormalChecked.indexOf(true);
                if(lastIndex != -1) {
                    mNormalChecked.set(lastIndex, false);
                }
                mNormalChecked.set(1, true);
                _initDragChecked();
            }else if (betType.equals(ShiyiyunType.ANYFOUR.toString())) {
                _showFragments(AnyfourFragment.TAG);
                AnyfourFragment zhixuanFragment = ((AnyfourFragment) mCurrentFragment);
                zhixuanFragment.updateBallData(indexList);
                syyType = ShiyiyunType.ANYFOUR;
                mTitleView.setText(prefix+"-任选四");
                int lastIndex = mNormalChecked.indexOf(true);
                if(lastIndex != -1) {
                    mNormalChecked.set(lastIndex, false);
                }
                mNormalChecked.set(2, true);
                _initDragChecked();
            } else if (betType.equals(ShiyiyunType.ANYFIVE.toString())) {
                _showFragments(AnyfiveFragment.TAG);
                AnyfiveFragment zhixuanFragment = ((AnyfiveFragment) mCurrentFragment);
                zhixuanFragment.updateBallData(indexList);
                syyType = ShiyiyunType.ANYFIVE;
                mTitleView.setText(prefix+"-任选五");
                int lastIndex = mNormalChecked.indexOf(true);
                if(lastIndex != -1) {
                    mNormalChecked.set(lastIndex, false);
                }
                mNormalChecked.set(3, true);
                _initDragChecked();
            }else if (betType.equals(ShiyiyunType.ANYSIX.toString())) {
                _showFragments(AnysixFragment.TAG);
                AnysixFragment zhixuanFragment = ((AnysixFragment) mCurrentFragment);
                zhixuanFragment.updateBallData(indexList);
                syyType = ShiyiyunType.ANYSIX;
                mTitleView.setText(prefix+"-任选六");
                int lastIndex = mNormalChecked.indexOf(true);
                if(lastIndex != -1) {
                    mNormalChecked.set(lastIndex, false);
                }
                mNormalChecked.set(4, true);
                _initDragChecked();
            }else if (betType.equals(ShiyiyunType.ANYSEVEN.toString())) {
                _showFragments(AnysevenFragment.TAG);
                AnysevenFragment zhixuanFragment = ((AnysevenFragment) mCurrentFragment);
                zhixuanFragment.updateBallData(indexList);
                syyType = ShiyiyunType.ANYSEVEN;
                mTitleView.setText(prefix+"-任选七");
                int lastIndex = mNormalChecked.indexOf(true);
                if(lastIndex != -1) {
                    mNormalChecked.set(lastIndex, false);
                }
                mNormalChecked.set(5, true);
                _initDragChecked();
            }else if (betType.equals(ShiyiyunType.ANYEIGHT.toString())) {
                _showFragments(AnyeightFragment.TAG);
                AnyeightFragment zhixuanFragment = ((AnyeightFragment) mCurrentFragment);
                zhixuanFragment.updateBallData(indexList);
                syyType = ShiyiyunType.ANYEIGHT;
                mTitleView.setText(prefix+"-任选八");
                int lastIndex = mNormalChecked.indexOf(true);
                if(lastIndex != -1) {
                    mNormalChecked.set(lastIndex, false);
                }
                mNormalChecked.set(6, true);
                _initDragChecked();
            }
            else if (betType.equals(ShiyiyunType.FRONTONEZHI.toString())) {
                _showFragments(FrontOnezhixuanFragment.TAG);
                FrontOnezhixuanFragment zhixuanFragment = ((FrontOnezhixuanFragment) mCurrentFragment);
                zhixuanFragment.updateBallData(indexList);
                syyType = ShiyiyunType.FRONTONEZHI;
                mTitleView.setText(prefix+"-前一直选");
                int lastIndex = mNormalChecked.indexOf(true);
                if(lastIndex != -1) {
                    mNormalChecked.set(lastIndex, false);
                }
                mNormalChecked.set(7, true);
                _initDragChecked();
            }else if (betType.equals(ShiyiyunType.FRONTTWOZHI.toString())) {
                _showFragments(FrontTwozhixuanFragment.TAG);
                FrontTwozhixuanFragment zhixuanFragment = ((FrontTwozhixuanFragment) mCurrentFragment);
                zhixuanFragment.updateBallData(indexList);
                syyType = ShiyiyunType.FRONTTWOZHI;
                mTitleView.setText(prefix+"-前二直选");
                int lastIndex = mNormalChecked.indexOf(true);
                if(lastIndex != -1) {
                    mNormalChecked.set(lastIndex, false);
                }
                mNormalChecked.set(8, true);
                _initDragChecked();
            }else if (betType.equals(ShiyiyunType.FRONTTWOZU.toString())) {
                _showFragments(FrontTwozuxuanFragment.TAG);
                FrontTwozuxuanFragment zhixuanFragment = ((FrontTwozuxuanFragment) mCurrentFragment);
                zhixuanFragment.updateBallData(indexList);
                syyType = ShiyiyunType.FRONTTWOZU;
                mTitleView.setText(prefix+"-前二组选");
                int lastIndex = mNormalChecked.indexOf(true);
                if(lastIndex != -1) {
                    mNormalChecked.set(lastIndex, false);
                }
                mNormalChecked.set(9, true);
                _initDragChecked();
            }else if (betType.equals(ShiyiyunType.FRONTTHREEZHI.toString())) {
                _showFragments(FrontThreezhixuanFragment.TAG);
                FrontThreezhixuanFragment zhixuanFragment = ((FrontThreezhixuanFragment) mCurrentFragment);
                zhixuanFragment.updateBallData(indexList);
                syyType = ShiyiyunType.FRONTTHREEZHI;
                mTitleView.setText(prefix+"-前三直选");
                int lastIndex = mNormalChecked.indexOf(true);
                if(lastIndex != -1) {
                    mNormalChecked.set(lastIndex, false);
                }
                mNormalChecked.set(10, true);
                _initDragChecked();
            }else if (betType.equals(ShiyiyunType.FRONTTHREEZU.toString())) {
                _showFragments(FrontThreezuxuanFragment.TAG);
                FrontThreezuxuanFragment zhixuanFragment = ((FrontThreezuxuanFragment) mCurrentFragment);
                zhixuanFragment.updateBallData(indexList);
                syyType = ShiyiyunType.FRONTTHREEZU;
                mTitleView.setText(prefix+"-前三组选");
                int lastIndex = mNormalChecked.indexOf(true);
                if(lastIndex != -1) {
                    mNormalChecked.set(lastIndex, false);
                }
                mNormalChecked.set(11, true);
                _initDragChecked();
            } else if (betType.equals(ShiyiyunType.ANYTWODRAG.toString())) {
                _showFragments(AnyTwoDragFragment.TAG);
                AnyTwoDragFragment zhixuanFragment = ((AnyTwoDragFragment) mCurrentFragment);
                zhixuanFragment.updateBallData(indexList);
                syyType = ShiyiyunType.ANYTWODRAG;
                mTitleView.setText(prefix+"-任选二(胆拖)");
                int lastIndex = mDragChecked.indexOf(true);
                if(lastIndex != -1) {
                    mDragChecked.set(lastIndex, false);
                }
                mDragChecked.set(0, true);
                _initNormalChecked();
            } else if (betType.equals(ShiyiyunType.ANYTHREEDRAG.toString())) {
                _showFragments(AnyThreeDragFragment.TAG);
                AnyThreeDragFragment zhixuanFragment = ((AnyThreeDragFragment) mCurrentFragment);
                zhixuanFragment.updateBallData(indexList);
                syyType = ShiyiyunType.ANYTHREEDRAG;
                mTitleView.setText(prefix+"-任选三(胆拖)");
                int lastIndex = mDragChecked.indexOf(true);
                if(lastIndex != -1) {
                    mDragChecked.set(lastIndex, false);
                }
                mDragChecked.set(1, true);
                _initNormalChecked();
            }else if (betType.equals(ShiyiyunType.ANYFOURDRAG.toString())) {
                _showFragments(AnyFourDragFragment.TAG);
                AnyFourDragFragment zhixuanFragment = ((AnyFourDragFragment) mCurrentFragment);
                zhixuanFragment.updateBallData(indexList);
                syyType = ShiyiyunType.ANYFOURDRAG;
                mTitleView.setText(prefix+"-任选四(胆拖)");
                int lastIndex = mDragChecked.indexOf(true);
                if(lastIndex != -1) {
                    mDragChecked.set(lastIndex, false);
                }
                mDragChecked.set(2, true);
                _initNormalChecked();
            } else if (betType.equals(ShiyiyunType.ANYFIVEDRAG.toString())) {
                _showFragments(AnyFiveDragFragment.TAG);
                AnyFiveDragFragment zhixuanFragment = ((AnyFiveDragFragment) mCurrentFragment);
                zhixuanFragment.updateBallData(indexList);
                syyType = ShiyiyunType.ANYFIVEDRAG;
                mTitleView.setText(prefix+"-任选五(胆拖)");
                int lastIndex = mDragChecked.indexOf(true);
                if(lastIndex != -1) {
                    mDragChecked.set(lastIndex, false);
                }
                mDragChecked.set(3, true);
                _initNormalChecked();
            }else if (betType.equals(ShiyiyunType.ANYSIXDRAG.toString())) {
                _showFragments(AnySixDragFragment.TAG);
                AnySixDragFragment zhixuanFragment = ((AnySixDragFragment) mCurrentFragment);
                zhixuanFragment.updateBallData(indexList);
                syyType = ShiyiyunType.ANYSIXDRAG;
                mTitleView.setText(prefix+"-任选六(胆拖)");
                int lastIndex = mDragChecked.indexOf(true);
                if(lastIndex != -1) {
                    mDragChecked.set(lastIndex, false);
                }
                mDragChecked.set(4, true);
                _initNormalChecked();
            }else if (betType.equals(ShiyiyunType.ANYSEVENDRAG.toString())) {
                _showFragments(AnySevenDragFragment.TAG);
                AnySevenDragFragment zhixuanFragment = ((AnySevenDragFragment) mCurrentFragment);
                zhixuanFragment.updateBallData(indexList);
                syyType = ShiyiyunType.ANYSEVENDRAG;
                mTitleView.setText(prefix+"-任选七(胆拖)");
                int lastIndex = mDragChecked.indexOf(true);
                if(lastIndex != -1) {
                    mDragChecked.set(lastIndex, false);
                }
                mDragChecked.set(5, true);
                _initNormalChecked();
            }else if (betType.equals(ShiyiyunType.ANYEIGHTDRAG.toString())) {
                _showFragments(AnyEightDragFragment.TAG);
                AnyEightDragFragment zhixuanFragment = ((AnyEightDragFragment) mCurrentFragment);
                zhixuanFragment.updateBallData(indexList);
                syyType = ShiyiyunType.ANYEIGHTDRAG;
                mTitleView.setText(prefix+"-任选八(胆拖)");
                int lastIndex = mDragChecked.indexOf(true);
                if(lastIndex != -1) {
                    mDragChecked.set(lastIndex, false);
                }
                mDragChecked.set(6, true);
                _initNormalChecked();
            }
            else if (betType.equals(ShiyiyunType.FRONTTHREEZUDRAG.toString())) {
                _showFragments(FrontThreeZuDragFragment.TAG);
                FrontThreeZuDragFragment zhixuanFragment = ((FrontThreeZuDragFragment) mCurrentFragment);
                zhixuanFragment.updateBallData(indexList);
                syyType = ShiyiyunType.FRONTONEZHI;
                mTitleView.setText(prefix+"-前三组选(胆拖)");
                int lastIndex = mDragChecked.indexOf(true);
                if(lastIndex != -1) {
                    mDragChecked.set(lastIndex, false);
                }
                mDragChecked.set(8, true);
                _initNormalChecked();
            }else if (betType.equals(ShiyiyunType.FRONTTWOZUDRAG.toString())) {
                _showFragments(FrontTwoZuDragFragment.TAG);
                FrontTwoZuDragFragment zhixuanFragment = ((FrontTwoZuDragFragment) mCurrentFragment);
                zhixuanFragment.updateBallData(indexList);
                syyType = ShiyiyunType.FRONTTWOZUDRAG;
                mTitleView.setText(prefix+"-前二组选(胆拖)");
                int lastIndex = mNormalChecked.indexOf(true);
                if(lastIndex != -1) {
                    mNormalChecked.set(lastIndex, false);
                }
                mNormalChecked.set(7, true);
                _initNormalChecked();
            }
        }
        else
        {
            startType = 0;
            _showFragments(AnytwoFragment.TAG);
            syyType = ShiyiyunType.ANYTWO;
            mTitleView.setText(prefix+"-任选二");
            int lastIndex = mNormalChecked.indexOf(true);
            if(lastIndex != -1) {
                mNormalChecked.set(lastIndex, false);
            }
            mNormalChecked.set(0, true);
        }
    }

    public void setTouzhuResult(int count) {
        lotteryCount.setText(String.valueOf(count));
        price.setText(String.valueOf(2*count));
//        mZhuShuView.setText(String.valueOf(count) + "注" + 2*count + "元");
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

    private void _initBallStr() {
        if (mCurrentFragment.getTag().equals(AnytwoFragment.TAG)) {
            AnytwoFragment zhixuanFragment = ((AnytwoFragment) mCurrentFragment);
            mCount = zhixuanFragment.mZhushu;
        } else if (mCurrentFragment.getTag().equals(AnythreeFragment.TAG)) {
            AnythreeFragment zusanFragment = ((AnythreeFragment) mCurrentFragment);
            mCount = zusanFragment.mZhushu;
        } else if (mCurrentFragment.getTag().equals(AnyfourFragment.TAG)) {
            AnyfourFragment zuliuFragment = ((AnyfourFragment) mCurrentFragment);
            mCount = zuliuFragment.mZhushu;
        } else if (mCurrentFragment.getTag().equals(AnyfiveFragment.TAG)) {
            AnyfiveFragment hzzxFragment = ((AnyfiveFragment) mCurrentFragment);
            mCount = hzzxFragment.mZhushu;
        } else if (mCurrentFragment.getTag().equals(AnysixFragment.TAG)) {
            AnysixFragment hzzsFragment = ((AnysixFragment) mCurrentFragment);
            mCount = hzzsFragment.mZhushu;
        } else if (mCurrentFragment.getTag().equals(AnysevenFragment.TAG)) {
            AnysevenFragment hzzsFragment = ((AnysevenFragment) mCurrentFragment);
            mCount = hzzsFragment.mZhushu;
        }  else if (mCurrentFragment.getTag().equals(AnyeightFragment.TAG)) {
            AnyeightFragment hzzsFragment = ((AnyeightFragment) mCurrentFragment);
            mCount = hzzsFragment.mZhushu;
        } else if (mCurrentFragment.getTag().equals(FrontOnezhixuanFragment.TAG)) {
            FrontOnezhixuanFragment hzzsFragment = ((FrontOnezhixuanFragment) mCurrentFragment);
            mCount = hzzsFragment.mZhushu;
        } else if (mCurrentFragment.getTag().equals(FrontTwozhixuanFragment.TAG)) {
            FrontTwozhixuanFragment hzzsFragment = ((FrontTwozhixuanFragment) mCurrentFragment);
            mCount = hzzsFragment.mZhushu;
        } else if (mCurrentFragment.getTag().equals(FrontTwozuxuanFragment.TAG)) {
            FrontTwozuxuanFragment hzzsFragment = ((FrontTwozuxuanFragment) mCurrentFragment);
            mCount = hzzsFragment.mZhushu;
        } else if (mCurrentFragment.getTag().equals(FrontThreezhixuanFragment.TAG)) {
            FrontThreezhixuanFragment hzzsFragment = ((FrontThreezhixuanFragment) mCurrentFragment);
            mCount = hzzsFragment.mZhushu;
        } else if(mCurrentFragment.getTag().equals(FrontThreezuxuanFragment.TAG)){
            FrontThreezuxuanFragment hzzlFragment = ((FrontThreezuxuanFragment) mCurrentFragment);
            mCount = hzzlFragment.mZhushu;
        }else if(mCurrentFragment.getTag().equals(AnyTwoDragFragment.TAG)) {
            AnyTwoDragFragment hzzlFragment = ((AnyTwoDragFragment) mCurrentFragment);
            mCount = hzzlFragment.mZhushu;
        } else if(mCurrentFragment.getTag().equals(AnyThreeDragFragment.TAG)) {
            AnyThreeDragFragment hzzlFragment = ((AnyThreeDragFragment) mCurrentFragment);
            mCount = hzzlFragment.mZhushu;
        }  else if(mCurrentFragment.getTag().equals(AnyFourDragFragment.TAG)) {
            AnyFourDragFragment hzzlFragment = ((AnyFourDragFragment) mCurrentFragment);
            mCount = hzzlFragment.mZhushu;
        } else if(mCurrentFragment.getTag().equals(AnyFiveDragFragment.TAG)) {
            AnyFiveDragFragment hzzlFragment = ((AnyFiveDragFragment) mCurrentFragment);
            mCount = hzzlFragment.mZhushu;
        } else if(mCurrentFragment.getTag().equals(AnySixDragFragment.TAG)) {
            AnySixDragFragment hzzlFragment = ((AnySixDragFragment) mCurrentFragment);
            mCount = hzzlFragment.mZhushu;
        } else if(mCurrentFragment.getTag().equals(AnySevenDragFragment.TAG)) {
            AnySevenDragFragment hzzlFragment = ((AnySevenDragFragment) mCurrentFragment);
            mCount = hzzlFragment.mZhushu;
        } else if(mCurrentFragment.getTag().equals(AnyEightDragFragment.TAG)) {
            AnyEightDragFragment hzzlFragment = ((AnyEightDragFragment) mCurrentFragment);
            mCount = hzzlFragment.mZhushu;
        } else if(mCurrentFragment.getTag().equals(FrontTwoZuDragFragment.TAG)) {
            FrontTwoZuDragFragment hzzlFragment = ((FrontTwoZuDragFragment) mCurrentFragment);
            mCount = hzzlFragment.mZhushu;
        } else {
            FrontThreeZuDragFragment hzzlFragment = ((FrontThreeZuDragFragment) mCurrentFragment);
            mCount = hzzlFragment.mZhushu;
        }

    }

    private void _clearChoose() {
        if (mCurrentFragment.getTag().equals(AnytwoFragment.TAG)) {
            AnytwoFragment zhixuanFragment = ((AnytwoFragment) mCurrentFragment);
            zhixuanFragment.clearChoose();
        } else if (mCurrentFragment.getTag().equals(AnythreeFragment.TAG)) {
            AnythreeFragment zusanFragment = ((AnythreeFragment) mCurrentFragment);
            zusanFragment.clearChoose();
        } else if (mCurrentFragment.getTag().equals(AnyfourFragment.TAG)) {
            AnyfourFragment zuliuFragment = ((AnyfourFragment) mCurrentFragment);
            zuliuFragment.clearChoose();
        } else if (mCurrentFragment.getTag().equals(AnyfiveFragment.TAG)) {
            AnyfiveFragment hzzxFragment = ((AnyfiveFragment) mCurrentFragment);
            hzzxFragment.clearChoose();
        } else if (mCurrentFragment.getTag().equals(AnysixFragment.TAG)) {
            AnysixFragment hzzsFragment = ((AnysixFragment) mCurrentFragment);
            hzzsFragment.clearChoose();
        } else if (mCurrentFragment.getTag().equals(AnysevenFragment.TAG)) {
            AnysevenFragment hzzsFragment = ((AnysevenFragment) mCurrentFragment);
            hzzsFragment.clearChoose();
        }  else if (mCurrentFragment.getTag().equals(AnyeightFragment.TAG)) {
            AnyeightFragment hzzsFragment = ((AnyeightFragment) mCurrentFragment);
            hzzsFragment.clearChoose();
        } else if (mCurrentFragment.getTag().equals(FrontOnezhixuanFragment.TAG)) {
            FrontOnezhixuanFragment hzzsFragment = ((FrontOnezhixuanFragment) mCurrentFragment);
            hzzsFragment.clearChoose();
        } else if (mCurrentFragment.getTag().equals(FrontTwozhixuanFragment.TAG)) {
            FrontTwozhixuanFragment hzzsFragment = ((FrontTwozhixuanFragment) mCurrentFragment);
            hzzsFragment.clearChoose();
        } else if (mCurrentFragment.getTag().equals(FrontTwozuxuanFragment.TAG)) {
            FrontTwozuxuanFragment hzzsFragment = ((FrontTwozuxuanFragment) mCurrentFragment);
            hzzsFragment.clearChoose();
        } else if (mCurrentFragment.getTag().equals(FrontThreezhixuanFragment.TAG)) {
            FrontThreezhixuanFragment hzzsFragment = ((FrontThreezhixuanFragment) mCurrentFragment);
            hzzsFragment.clearChoose();
        } else if(mCurrentFragment.getTag().equals(FrontThreezuxuanFragment.TAG)){
            FrontThreezuxuanFragment hzzlFragment = ((FrontThreezuxuanFragment) mCurrentFragment);
            hzzlFragment.clearChoose();
        }else if(mCurrentFragment.getTag().equals(AnyTwoDragFragment.TAG)) {
            AnyTwoDragFragment hzzlFragment = ((AnyTwoDragFragment) mCurrentFragment);
            hzzlFragment.clearChoose();
        } else if(mCurrentFragment.getTag().equals(AnyThreeDragFragment.TAG)) {
            AnyThreeDragFragment hzzlFragment = ((AnyThreeDragFragment) mCurrentFragment);
            hzzlFragment.clearChoose();
        }  else if(mCurrentFragment.getTag().equals(AnyFourDragFragment.TAG)) {
            AnyFourDragFragment hzzlFragment = ((AnyFourDragFragment) mCurrentFragment);
            hzzlFragment.clearChoose();;
        } else if(mCurrentFragment.getTag().equals(AnyFiveDragFragment.TAG)) {
            AnyFiveDragFragment hzzlFragment = ((AnyFiveDragFragment) mCurrentFragment);
            hzzlFragment.clearChoose();;
        } else if(mCurrentFragment.getTag().equals(AnySixDragFragment.TAG)) {
            AnySixDragFragment hzzlFragment = ((AnySixDragFragment) mCurrentFragment);
            hzzlFragment.clearChoose();
        } else if(mCurrentFragment.getTag().equals(AnySevenDragFragment.TAG)) {
            AnySevenDragFragment hzzlFragment = ((AnySevenDragFragment) mCurrentFragment);
            hzzlFragment.clearChoose();
        } else if(mCurrentFragment.getTag().equals(AnyEightDragFragment.TAG)) {
            AnyEightDragFragment hzzlFragment = ((AnyEightDragFragment) mCurrentFragment);
            hzzlFragment.clearChoose();
        } else if(mCurrentFragment.getTag().equals(FrontTwoZuDragFragment.TAG)) {
            FrontTwoZuDragFragment hzzlFragment = ((FrontTwoZuDragFragment) mCurrentFragment);
            hzzlFragment.clearChoose();
        } else {
            FrontThreeZuDragFragment hzzlFragment = ((FrontThreeZuDragFragment) mCurrentFragment);
            hzzlFragment.clearChoose();
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.pre_num_str:
                if(TextUtils.isEmpty(mQihaoView.getText()) || !mQihaoView.getText().toString().equals("正在获取当前期号")) {
                    _requestData();
                }
                break;
            case R.id.sumbit_group_buy:
//			if(mCity.equals(Constants.City.shandong.toString())) {
//				UiHelper.startSyxwYilou(this, lotterytype,prefix);
//			} else {
//				UiHelper.startSyxwYilou(this, lotterytype,Constants.City.getCityName(mCity)+prefix);
//			}
                //进入走势图
                goToChart();
                break;
            case R.id.left_layout:
                if(mc != null) {
                    mc.cancel();
                }
                finish();
                break;
            case R.id.left_footer_btn:
                _clearChoose();
                break;
            case R.id.title_selector:
                if (!isPopWindowShow) {
                    _setPopWindow((int) (v.getWidth() * 1.5 + 0.5f));
                    isPopWindowShow = true;
                }
                mPopWindow.showAsDropDown(v, -(v.getWidth() / 4), v.getTop());
                break;
            case R.id.right_footer_btn:
                if(!isCanSale) {
                    Toast.makeText(getApplicationContext(), "本期销售已停止",Toast.LENGTH_SHORT).show();
                    break;
                }
                //为测试注释，不要提交
                if(TextUtils.isEmpty(mQihao)) {
                    Toast.makeText(getApplicationContext(), "未获取到当前期号",Toast.LENGTH_SHORT).show();
                    break;
                }
                _initBallStr();
                if (mCount < 1) {
                    Toast.makeText(getApplicationContext(), "请至少选1注",
                            Toast.LENGTH_LONG).show();
                } else {
                    _startActivity();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 进入走势图
     */
    private void goToChart()
    {
        Intent chartActivity = new Intent();
        //前一， 开奖号分布+形态走势
        if (syyType == ShiyiyunType.FRONTONEZHI)
        {
            chartActivity.setClass(Syxw.this, ElevenFiveTableThree.class);
            if (mCurrentFragment instanceof BaseElevenFiveFragment)
            {
                chartActivity.putExtra(IntentData.SELECT_NUMBERS,
                        ((BaseElevenFiveFragment) mCurrentFragment).getBoolLiu());
            }
        }
        //前二直选、前二胆拖组选
        else if (syyType == ShiyiyunType.FRONTTWOZHI || syyType == ShiyiyunType.FRONTTWOZUDRAG)
        {
            chartActivity.setClass(Syxw.this, ElevenFiveTableOne.class);
            chartActivity.putExtra(IntentData.DRAG, syyType == ShiyiyunType.FRONTTWOZUDRAG);

            if (mCurrentFragment instanceof FrontTwozhixuanFragment)
            {
                chartActivity.putExtra(IntentData.SELECT_NUMBERS,
                        ((FrontTwozhixuanFragment) mCurrentFragment).getBoolFront());
                chartActivity.putExtra(IntentData.SECOND_SELECT_NUMBERS,
                        ((FrontTwozhixuanFragment) mCurrentFragment).getBoolBehind());
            }
        }
        //前三直选、前三胆拖组选
        else if (syyType == ShiyiyunType.FRONTTHREEZHI || syyType == ShiyiyunType.FRONTTHREEZUDRAG)
        {
            chartActivity.setClass(Syxw.this, ElevenFiveTableTwo.class);
            chartActivity.putExtra(IntentData.DRAG, syyType == ShiyiyunType.FRONTTHREEZUDRAG);

            if (mCurrentFragment instanceof FrontThreezhixuanFragment)
            {
                chartActivity.putExtra(IntentData.SELECT_NUMBERS,
                        ((FrontThreezhixuanFragment) mCurrentFragment).getBoolFront());
                chartActivity.putExtra(IntentData.SECOND_SELECT_NUMBERS,
                        ((FrontThreezhixuanFragment) mCurrentFragment).getBoolBehind());
                chartActivity.putExtra(IntentData.THIRD_SELECT_NUMBERS,
                        ((FrontThreezhixuanFragment) mCurrentFragment).getBoolLast());
            }
        }
        //任选X， 前二组选，前三组选
        else
        {
            chartActivity.setClass(Syxw.this, ElevenFiveTableFour.class);
            boolean isDrag = (syyType == ShiyiyunType.ANYEIGHTDRAG) ||
                    (syyType == ShiyiyunType.ANYFIVEDRAG) || (syyType == ShiyiyunType.ANYFOURDRAG) ||
                    (syyType == ShiyiyunType.ANYSEVENDRAG) || (syyType == ShiyiyunType.ANYSIXDRAG) ||
                    (syyType == ShiyiyunType.ANYTHREEDRAG) || (syyType == ShiyiyunType.ANYTWODRAG);

            chartActivity.putExtra(IntentData.DRAG, isDrag);
            chartActivity.putExtra(IntentData.TABLE_TYPE, syyType.ordinal());
            if (mCurrentFragment instanceof BaseElevenFiveFragment)
            {
                chartActivity.putExtra(IntentData.SELECT_NUMBERS,
                        ((BaseElevenFiveFragment) mCurrentFragment).getBoolLiu());
            }
        }
        chartActivity.putExtra(IntentData.CITY, lotterytype);
        startActivityForResult(chartActivity, syyType.ordinal());
    }

    private void _startActivity() {
        Intent syyPick = new Intent();
        syyPick.putExtra(IntentData.BET_TYPE, syyType.toString());
        syyPick.putExtra(CITY, mCity);
        syyPick.putExtra("qihao", mQihao);
        if (syyType == ShiyiyunType.ANYTWO) {
            syyPick.putStringArrayListExtra(IntentData.NUMBERS_PER_SEL,
                    ((AnytwoFragment) mCurrentFragment)
                            .getNumPerSelList());
        } else if (syyType == ShiyiyunType.ANYTHREE) {
            syyPick.putStringArrayListExtra(IntentData.NUMBERS_PER_SEL,
                    ((AnythreeFragment) mCurrentFragment)
                            .getNumPerSelList());
        } else if (syyType == ShiyiyunType.ANYFOUR) {
            syyPick.putStringArrayListExtra(IntentData.NUMBERS_PER_SEL,
                    ((AnyfourFragment) mCurrentFragment)
                            .getNumPerSelList());
        } else if (syyType == ShiyiyunType.ANYFIVE) {
            syyPick.putStringArrayListExtra(IntentData.NUMBERS_PER_SEL,
                    ((AnyfiveFragment) mCurrentFragment)
                            .getNumPerSelList());
        } else if (syyType == ShiyiyunType.ANYSIX) {
            syyPick.putStringArrayListExtra(IntentData.NUMBERS_PER_SEL,
                    ((AnysixFragment) mCurrentFragment)
                            .getNumPerSelList());
        } else if (syyType == ShiyiyunType.ANYSEVEN) {
            syyPick.putStringArrayListExtra(IntentData.NUMBERS_PER_SEL,
                    ((AnysevenFragment) mCurrentFragment)
                            .getNumPerSelList());
        } else if (syyType == ShiyiyunType.ANYEIGHT) {
            syyPick.putStringArrayListExtra(IntentData.NUMBERS_PER_SEL,
                    ((AnyeightFragment) mCurrentFragment)
                            .getNumPerSelList());
        } else if (syyType == ShiyiyunType.FRONTONEZHI) {
            syyPick.putStringArrayListExtra(IntentData.NUMBERS_PER_SEL,
                    ((FrontOnezhixuanFragment) mCurrentFragment)
                            .getNumPerSelList());
        } else if (syyType == ShiyiyunType.FRONTTWOZHI) {
            syyPick.putStringArrayListExtra(IntentData.NUMBERS_PER_SEL,
                    ((FrontTwozhixuanFragment) mCurrentFragment)
                            .getNumPerSelList());
        } else if (syyType == ShiyiyunType.FRONTTWOZU) {
            syyPick.putStringArrayListExtra(IntentData.NUMBERS_PER_SEL,
                    ((FrontTwozuxuanFragment) mCurrentFragment)
                            .getNumPerSelList());
        } else if (syyType == ShiyiyunType.FRONTTHREEZHI) {
            syyPick.putStringArrayListExtra(IntentData.NUMBERS_PER_SEL,
                    ((FrontThreezhixuanFragment) mCurrentFragment)
                            .getNumPerSelList());
        } else if (syyType == ShiyiyunType.FRONTTHREEZU) {
            syyPick.putStringArrayListExtra(IntentData.NUMBERS_PER_SEL,
                    ((FrontThreezuxuanFragment) mCurrentFragment)
                            .getNumPerSelList());
        } else {
            syyPick.putStringArrayListExtra(IntentData.NUMBERS_PER_SEL,
                    ((ElevenFiveBaseDragFragment) mCurrentFragment)
                            .getNumPerSelList());
        }

        if (mCurrentFragment instanceof BaseElevenFiveFragment) {
            syyPick.putExtra(IntentData.SELECT_NUMBERS,
                    ((BaseElevenFiveFragment) mCurrentFragment).getPlsResultList());
            syyPick.putExtra(IntentData.BET_COUNT, ((BaseElevenFiveFragment) mCurrentFragment).mZhushu);
            syyPick.putExtra(IntentData.SELECTION_INDEX,
                    ((BaseElevenFiveFragment) mCurrentFragment).getSelectedIndex());
        }
        else if (mCurrentFragment instanceof ElevenFiveBaseDragFragment)
        {
            syyPick.putExtra(IntentData.SELECT_NUMBERS,
                    ((ElevenFiveBaseDragFragment) mCurrentFragment).getPlsResultList());
            syyPick.putExtra(IntentData.BET_COUNT, ((ElevenFiveBaseDragFragment) mCurrentFragment).mZhushu);
            syyPick.putExtra(IntentData.SELECTION_INDEX,
                    ((ElevenFiveBaseDragFragment) mCurrentFragment).getSelectedIndex());
        }
        else if (mCurrentFragment instanceof FrontThreezhixuanFragment)
        {
            syyPick.putExtra(IntentData.SELECT_NUMBERS,
                    ((FrontThreezhixuanFragment) mCurrentFragment).getPlsResultList());
            syyPick.putExtra(IntentData.BET_COUNT, ((FrontThreezhixuanFragment) mCurrentFragment).mZhushu);
            syyPick.putExtra(IntentData.SELECTION_INDEX,
                    ((FrontThreezhixuanFragment) mCurrentFragment).getSelectedIndex());
        }
        else if (mCurrentFragment instanceof FrontTwozhixuanFragment)
        {
            syyPick.putExtra(IntentData.SELECT_NUMBERS,
                    ((FrontTwozhixuanFragment) mCurrentFragment).getPlsResultList());
            syyPick.putExtra(IntentData.BET_COUNT, ((FrontTwozhixuanFragment) mCurrentFragment).mZhushu);
            syyPick.putExtra(IntentData.SELECTION_INDEX,
                    ((FrontTwozhixuanFragment) mCurrentFragment).getSelectedIndex());
        }
        if (startType == 0) {
            syyPick.setClass(Syxw.this, SyxwPick.class);
            startActivity(syyPick);
        } else if (startType == 1) {
            setResult(RESULT_OK, syyPick);
        } else if (startType == 2) {
            setResult(RESULT_OK, syyPick);
        }
        if(mc != null) {
            mc.cancel();
        }
        finish();
    }

    private void _setPopWindow(int width) {
        View view = LayoutInflater.from(this).inflate(R.layout.syxw_popview_layout, null);
        mPopWindow = new PopupWindow(view, LayoutParams.WRAP_CONTENT,
                LayoutParams.MATCH_PARENT);
        mGridNormalView = (GridView) view.findViewById(R.id.pop_normal_gridview);
        mGridNormalView.setNumColumns(3);
        mGridDragView = (GridView) view.findViewById(R.id.pop_drag_gridview);
        mGridDragView.setNumColumns(3);
        ArrayList<String> listNormal = new ArrayList<String>();
        listNormal.add("任选二");
        listNormal.add("任选三");
        listNormal.add("任选四");
        listNormal.add("任选五");
        listNormal.add("任选六");
        listNormal.add("任选七");
        listNormal.add("任选八");
        listNormal.add("前一");
        listNormal.add("前二直选");
        listNormal.add("前二组选");
        listNormal.add("前三直选");
        listNormal.add("前三组选");
        mMyNormalAdapter = new PopViewAdapter(this, listNormal, mNormalChecked);
        mGridNormalView.setAdapter(mMyNormalAdapter);
        mGridNormalView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
//                mShakeBtn.setVisibility(View.VISIBLE);
                if (arg2 == 0) {

                    mTitleView.setText(prefix+"-任选二");
                    syyType = Constants.ShiyiyunType.ANYTWO;
                    _showFragments(AnytwoFragment.TAG);

                } else if (arg2 == 1) {

                    mTitleView.setText(prefix+"-任选三");
                    syyType = Constants.ShiyiyunType.ANYTHREE;
                    _showFragments(AnythreeFragment.TAG);
                } else if (arg2 == 2) {

                    mTitleView.setText(prefix+"-任选四");
                    syyType = Constants.ShiyiyunType.ANYFOUR;
                    _showFragments(AnyfourFragment.TAG);
                } else if (arg2 == 3) {

                    mTitleView.setText(prefix+"-任选五");
                    syyType = Constants.ShiyiyunType.ANYFIVE;
                    _showFragments(AnyfiveFragment.TAG);
                } else if (arg2 == 4) {

                    mTitleView.setText(prefix+"-任选六");
                    syyType = Constants.ShiyiyunType.ANYSIX;
                    _showFragments(AnysixFragment.TAG);
                } else if(arg2 == 5){

                    mTitleView.setText(prefix+"-任选七");
                    syyType = Constants.ShiyiyunType.ANYSEVEN;
                    _showFragments(AnysevenFragment.TAG);
                }
                else if(arg2 == 6){

                    mTitleView.setText(prefix+"-任选八");
                    syyType = Constants.ShiyiyunType.ANYEIGHT;
                    _showFragments(AnyeightFragment.TAG);
                }
                else if(arg2 == 7){

                    mTitleView.setText(prefix+"-前一");
                    syyType = Constants.ShiyiyunType.FRONTONEZHI;
                    _showFragments(FrontOnezhixuanFragment.TAG);
                }else if(arg2 == 8){

                    mTitleView.setText(prefix+"-前二直选");
                    syyType = Constants.ShiyiyunType.FRONTTWOZHI;
                    _showFragments(FrontTwozhixuanFragment.TAG);
                }else if(arg2 == 9){

                    mTitleView.setText(prefix+"-前二组选");
                    syyType = Constants.ShiyiyunType.FRONTTWOZU;
                    _showFragments(FrontTwozuxuanFragment.TAG);
                }else if(arg2 == 10){

                    mTitleView.setText(prefix+"-前三直选");
                    syyType = Constants.ShiyiyunType.FRONTTHREEZHI;
                    _showFragments(FrontThreezhixuanFragment.TAG);
                }else if(arg2 == 11){

                    mTitleView.setText(prefix+"-前三组选");
                    syyType = Constants.ShiyiyunType.FRONTTHREEZU;
                    _showFragments(FrontThreezuxuanFragment.TAG);
                }
                int lastIndex = mNormalChecked.indexOf(true);
                if(lastIndex != -1) {
                    mNormalChecked.set(lastIndex, false);
                }
                mNormalChecked.set(arg2, true);
                _initDragChecked();
                mMyNormalAdapter.notifyDataSetChanged();
                mMyDragAdapter.notifyDataSetChanged();
                mPopWindow.dismiss();
            }
        });
        ArrayList<String>mDragList = new ArrayList<String>();
        mDragList.add("任选二");
        mDragList.add("任选三");
        mDragList.add("任选四");
        mDragList.add("任选五");
        mDragList.add("任选六");
        mDragList.add("任选七");
        mDragList.add("任选八");
        mDragList.add("前二组选");
        mDragList.add("前三组选");
        mMyDragAdapter = new PopViewAdapter(this, mDragList, mDragChecked);
        mGridDragView.setAdapter(mMyDragAdapter);
        mGridDragView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
//                mShakeBtn.setVisibility(View.GONE);
                if (arg2 == 0) {

                    mTitleView.setText(prefix+"-任选二(胆拖)");
                    syyType = Constants.ShiyiyunType.ANYTWODRAG;
                    _showFragments(AnyTwoDragFragment.TAG);

                } else if (arg2 == 1) {

                    mTitleView.setText(prefix+"-任选三(胆拖)");
                    syyType = Constants.ShiyiyunType.ANYTHREEDRAG;
                    _showFragments(AnyThreeDragFragment.TAG);
                } else if (arg2 == 2) {

                    mTitleView.setText(prefix+"-任选四(胆拖)");
                    syyType = Constants.ShiyiyunType.ANYFOURDRAG;
                    _showFragments(AnyFourDragFragment.TAG);
                } else if (arg2 == 3) {

                    mTitleView.setText(prefix+"-任选五(胆拖)");
                    syyType = Constants.ShiyiyunType.ANYFIVEDRAG;
                    _showFragments(AnyFiveDragFragment.TAG);
                } else if (arg2 == 4) {

                    mTitleView.setText(prefix+"-任选六(胆拖)");
                    syyType = Constants.ShiyiyunType.ANYSIXDRAG;
                    _showFragments(AnySixDragFragment.TAG);
                } else if(arg2 == 5){

                    mTitleView.setText(prefix+"-任选七(胆拖)");
                    syyType = Constants.ShiyiyunType.ANYSEVENDRAG;
                    _showFragments(AnySevenDragFragment.TAG);
                }
                else if(arg2 == 6){

                    mTitleView.setText(prefix+"-任选八(胆拖)");
                    syyType = Constants.ShiyiyunType.ANYEIGHTDRAG;
                    _showFragments(AnyEightDragFragment.TAG);
                }
                else if(arg2 == 7){

                    mTitleView.setText(prefix+"-前二组选(胆拖)");
                    syyType = Constants.ShiyiyunType.FRONTTWOZUDRAG;
                    _showFragments(FrontTwoZuDragFragment.TAG);
                }else if(arg2 == 8){

                    mTitleView.setText(prefix+"-前三组选(胆拖");
                    syyType = Constants.ShiyiyunType.FRONTTHREEZUDRAG;
                    _showFragments(FrontThreeZuDragFragment.TAG);
                }
                int lastIndex = mDragChecked.indexOf(true);
                if(lastIndex != -1) {
                    mDragChecked.set(lastIndex, false);
                }
                mDragChecked.set(arg2, true);
                _initNormalChecked();
                mMyNormalAdapter.notifyDataSetChanged();
                mMyDragAdapter.notifyDataSetChanged();
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
        Fragment mFragment = (Fragment) mFragManager
                .findFragmentByTag(fragmentTag);
        if (mFragment == null) {
            if (fragmentTag.equals(AnytwoFragment.TAG)) {
                mFragment = new AnytwoFragment();
            } else if (fragmentTag.equals(AnythreeFragment.TAG)) {
                mFragment = new AnythreeFragment();
            } else if (fragmentTag.equals(AnyfourFragment.TAG)) {
                mFragment = new AnyfourFragment();
            } else if (fragmentTag.equals(AnyfiveFragment.TAG)) {
                mFragment = new AnyfiveFragment();
            } else if (fragmentTag.equals(AnysixFragment.TAG)) {
                mFragment = new AnysixFragment();
            } else if (fragmentTag.equals(AnysevenFragment.TAG)) {
                mFragment = new AnysevenFragment();
            }else if (fragmentTag.equals(AnyeightFragment.TAG)) {
                mFragment = new AnyeightFragment();
            }else if (fragmentTag.equals(FrontOnezhixuanFragment.TAG)) {
                mFragment = new FrontOnezhixuanFragment();
            }else if (fragmentTag.equals(FrontTwozhixuanFragment.TAG)) {
                mFragment = new FrontTwozhixuanFragment();
            }else if (fragmentTag.equals(FrontTwozuxuanFragment.TAG)) {
                mFragment = new FrontTwozuxuanFragment();
            }else if (fragmentTag.equals(FrontThreezhixuanFragment.TAG)) {
                mFragment = new FrontThreezhixuanFragment();
            } else if (fragmentTag.equals(FrontThreezuxuanFragment.TAG)){
                mFragment = new FrontThreezuxuanFragment();
            } else if(fragmentTag.equals(AnyTwoDragFragment.TAG)) {
                mFragment = new AnyTwoDragFragment();
            } else if(fragmentTag.equals(AnyThreeDragFragment.TAG)) {
                mFragment = new AnyThreeDragFragment();
            }  else if(fragmentTag.equals(AnyFourDragFragment.TAG)) {
                mFragment = new AnyFourDragFragment();
            } else if(fragmentTag.equals(AnyFiveDragFragment.TAG)) {
                mFragment = new AnyFiveDragFragment();
            } else if(fragmentTag.equals(AnySixDragFragment.TAG)) {
                mFragment = new AnySixDragFragment();
            } else if(fragmentTag.equals(AnySevenDragFragment.TAG)) {
                mFragment = new AnySevenDragFragment();
            } else if(fragmentTag.equals(AnyEightDragFragment.TAG)) {
                mFragment = new AnyEightDragFragment();
            } else if(fragmentTag.equals(FrontTwoZuDragFragment.TAG)) {
                mFragment = new FrontTwoZuDragFragment();
            } else {
                mFragment = new FrontThreeZuDragFragment();
            }
            mFragTransaction.add(R.id.fl_pls_pickarea, mFragment, fragmentTag);
        }
        if (mCurrentFragment != null) {
            mFragTransaction.hide(mCurrentFragment);
        }
        mFragTransaction.show(mFragment);
        mCurrentFragment = mFragment;
        mFragTransaction.commit();
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
                                          byte[] responseBody) {
                        try {
                            String charset;
                            if(Utils.isCmwapNet(Syxw.this)) {
                                charset = "utf-8";
                            } else {
                                charset = "gb2312";
                            }
                            String jsonData = new String(responseBody, charset);
                            Log.d(TAG, "syxw qihao detail = " + jsonData);
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
                        //Toast.makeText(Syxw.this, "期号获取失败", Toast.LENGTH_SHORT).show();
                        _requestData();
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
                mQihao  = reader.nextString();
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
                _requestData();
            } else {
                mQihaoView.setText("距" + mQihao + "期还有");
                isCanSale = true;
                mc  = new MyCount(Long.valueOf(diff)*1000, 1000);
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
            _requestData();
        }
        @Override
        public void onTick(long millisUntilFinished) {
            //Log.d("test", millisUntilFinished+"");
            mTimeDiffView.setText(Utils.formatDuring(millisUntilFinished));

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK)
        {
            return;
        }

        //前二直选
        if (requestCode == ShiyiyunType.FRONTTWOZHI.ordinal())
        {
            if (mCurrentFragment instanceof FrontTwozhixuanFragment)
            {
                ArrayList<Boolean> first = (ArrayList<Boolean>)data.getSerializableExtra(IntentData.SELECT_NUMBERS);
                ArrayList<Boolean> second = (ArrayList<Boolean>)data.getSerializableExtra(IntentData.SECOND_SELECT_NUMBERS);

                if (mCurrentFragment instanceof  FrontTwozhixuanFragment) {
                    ((FrontTwozhixuanFragment) mCurrentFragment).updateChoice(first, second);
                }
            }
        }
        //前三直选
        else if (requestCode == ShiyiyunType.FRONTTHREEZHI.ordinal())
        {
            ArrayList<Boolean> first = (ArrayList<Boolean>)data.getSerializableExtra(IntentData.SELECT_NUMBERS);
            ArrayList<Boolean> second = (ArrayList<Boolean>)data.getSerializableExtra(IntentData.SECOND_SELECT_NUMBERS);
            ArrayList<Boolean> third = (ArrayList<Boolean>)data.getSerializableExtra(IntentData.THIRD_SELECT_NUMBERS);

            if (mCurrentFragment instanceof FrontThreezhixuanFragment)
            {
                ((FrontThreezhixuanFragment) mCurrentFragment).updateChoice(first, second, third);
            }
        }
        else
        {
            ArrayList<Boolean> result = (ArrayList<Boolean>)data.getSerializableExtra(IntentData.SELECT_NUMBERS);

            if (mCurrentFragment instanceof BaseElevenFiveFragment)
            {
                ((BaseElevenFiveFragment) mCurrentFragment).updateChoice(result);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if(mc != null) {
            mc.cancel();
        }
        finish();
    }

    private class MyBroadcastReceiver extends BroadcastReceiver
    {

        @Override
        public void onReceive(Context context, Intent intent) {
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
                currentMiss.clear();
                reader.beginArray();
                while (reader.hasNext()) {
                    reader.beginObject();
                    while (reader.hasNext()) {
                        String tagName = reader.nextName();
                        if (tagName.equals("nowyilou")) {
                            currentMiss.add(reader.nextString());
                        }
                        else
                        {
                            reader.nextString();
                        }
                    }
                    reader.endObject();
                }
                reader.endArray();
                return true;
            }

            @Override
            protected void onSuccess(Boolean t) throws Exception {
                // TODO Auto-generated method stub
                super.onSuccess(t);
                if (mCurrentFragment != null) {
                    if (mCurrentFragment instanceof BaseElevenFiveFragment)
                    {
                        ((BaseElevenFiveFragment) mCurrentFragment).notifyLeakUpdate();
                    }
                    else if (mCurrentFragment instanceof ElevenFiveBaseDragFragment)
                    {
                        ((ElevenFiveBaseDragFragment) mCurrentFragment).notifyLeakUpdate();
                    }
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
