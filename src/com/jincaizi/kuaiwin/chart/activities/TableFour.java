package com.jincaizi.kuaiwin.chart.activities;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.jincaizi.R;
import com.jincaizi.common.IntentData;
import com.jincaizi.kuaiwin.chart.TableFixHeaders.TableFixHeaders;
import com.jincaizi.kuaiwin.chart.adapters.BaseListAdapter;
import com.jincaizi.kuaiwin.chart.adapters.TwoNotEqualAdapter;
import com.jincaizi.kuaiwin.chart.adapters.TwoNotEqualBottomAdapter;
import com.jincaizi.kuaiwin.chart.requesters.LotteryDataRequester;
import com.jincaizi.kuaiwin.chart.requesters.LotteryTimeRequester;
import com.jincaizi.data.ResponseData;

/**
 * Created by chenweida on 2015/1/26.
 */
public class TableFour extends BaseTableActivity {

    ResponseData responseData;

    private ListView baseListView;
    private BaseListAdapter baseListAdapter;
    private TableFixHeaders tableFixHeaders;
    private TwoNotEqualAdapter sumTableAdapter;
    private TwoNotEqualBottomAdapter bottomAdapter;
    private RelativeLayout loadingLayout;
    private RelativeLayout refreshLayout;
    private Button refreshBtn;
    private RelativeLayout baseListLayout;
    private RelativeLayout sumTableLayout;
    private RelativeLayout baseBtn;
    private RelativeLayout subBtn;
    private ImageView baseSelector;
    private ImageView subSelector;
    private TextView subBtnTxt;
    private PopupWindow popupWindow;
    private TableFixHeaders bottomTable;

    private boolean showMiss = true;
    private boolean showCount = true;

    private String lotteryType;

    private LotteryDataRequester requester;
    private LotteryTimeRequester timeRequester;

    private android.os.Handler handler = new android.os.Handler();

    private MyBroadcastReceiver broadcastReceiver;

    private class MyBroadcastReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!intent.getBooleanExtra("success",false) && baseListLayout.getVisibility() == View.GONE &&
                    sumTableLayout.getVisibility() == View.GONE)
            {
                loadingLayout.setVisibility(View.GONE);
                refreshLayout.setVisibility(View.VISIBLE);
                return;
            }

            final String data = intent.getStringExtra("JSON");
            handler.post(new Runnable() {
                @Override
                public void run() {

                    responseData = new ResponseData(data);

                    if (baseListAdapter == null)
                    {
                        baseListAdapter = new BaseListAdapter(TableFour.this, responseData);
                        baseListView.setAdapter(baseListAdapter);
                    }
                    else
                    {
                        baseListAdapter.notifyDataSetChanged();
                    }

                    if (sumTableAdapter == null)
                    {
                        sumTableAdapter = new TwoNotEqualAdapter(TableFour.this, responseData);
                        tableFixHeaders.setAdapter(sumTableAdapter);
                    }
                    else
                    {
                        sumTableAdapter.notifyDataSetChanged();
                    }

                    loadingLayout.setVisibility(View.GONE);
                    sumTableLayout.setVisibility(subBtn.isEnabled()?View.GONE:View.VISIBLE);
                    baseListLayout.setVisibility(baseBtn.isEnabled()?View.GONE:View.VISIBLE);
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.test_table_main);
        super.onCreate(savedInstanceState);

        initLotteryType();

        requester = new LotteryDataRequester(this, lotteryType);
        timeRequester = new LotteryTimeRequester(this, lotteryType);

        refreshBtn = (Button) findViewById(R.id.refresh);
        refreshLayout = (RelativeLayout) findViewById(R.id.refresh_layout);
        loadingLayout = (RelativeLayout) findViewById(R.id.loading);
        baseBtn = (RelativeLayout) findViewById(R.id.title_btn_base);
        subBtn = (RelativeLayout) findViewById(R.id.title_btn_sub);
        baseSelector = (ImageView) findViewById(R.id.title_selector_base);
        subSelector = (ImageView) findViewById(R.id.title_selector_sub);

        subBtnTxt = (TextView) findViewById(R.id.sub_btn_txt);
        RelativeLayout menuBtn = (RelativeLayout) findViewById(R.id.menu);

        subBtnTxt.setText("二不同号分布");

        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshLayout.setVisibility(View.GONE);
                loadingLayout.setVisibility(View.VISIBLE);
                requester.query();
            }
        });

        baseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isLoaded()) {
                    sumTableLayout.setVisibility(View.GONE);
                    baseListLayout.setVisibility(View.VISIBLE);
                }
                baseBtn.setEnabled(false);
                subBtn.setEnabled(true);
                baseSelector.setVisibility(View.VISIBLE);
                subSelector.setVisibility(View.GONE);
            }
        });
        baseBtn.setEnabled(false);
        baseSelector.setVisibility(View.VISIBLE);
        subSelector.setVisibility(View.GONE);

        subBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isLoaded()) {
                    sumTableLayout.setVisibility(View.VISIBLE);
                    baseListLayout.setVisibility(View.GONE);
                }
                baseBtn.setEnabled(true);
                subBtn.setEnabled(false);
                subSelector.setVisibility(View.VISIBLE);
                baseSelector.setVisibility(View.GONE);
            }
        });

        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popupWindow == null) {
                    initPopupWindow();
                }
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
            }
        });

        tableFixHeaders = (TableFixHeaders) findViewById(R.id.table);
        baseListView = (ListView) findViewById(R.id.base_list);
        baseListLayout = (RelativeLayout) findViewById(R.id.base_list_layout);
        sumTableLayout = (RelativeLayout) findViewById(R.id.sum_table_layout);
        bottomTable = (TableFixHeaders) findViewById(R.id.bottom_layout);

        initBottom();
        initListShadow();

        broadcastReceiver = new MyBroadcastReceiver();
        registerReceiver(broadcastReceiver, new IntentFilter("haha"));
        requester.query();
        timeRequester.query();
    }

    private void initBottom()
    {
        boolean drag = getIntent().getBooleanExtra(IntentData.DRAG, true);
        if (drag)
        {
            bottomTable.setVisibility(View.GONE);
            return;
        }

        bottomAdapter = new TwoNotEqualBottomAdapter(TableFour.this);
        bottomTable.setAdapter(bottomAdapter);
        tableFixHeaders.setDelegateTable(bottomTable);
        bottomTable.setDelegateTable(tableFixHeaders);
    }

    private void initLotteryType()
    {
        lotteryType = getIntent().getStringExtra(IntentData.CITY);
        if (lotteryType == null || lotteryType.length() <= 0)
        {
            finish();
        }
    }

    private void initListShadow()
    {
        final ImageView topShadow = (ImageView)findViewById(R.id.top_shadow);
        final ImageView bottomShadow = (ImageView)findViewById(R.id.bottom_shadow);
        setAlpha(topShadow, 0);
        setAlpha(bottomShadow, 0);

        baseListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

                if (absListView.getChildCount() <= 0)
                {
                    return;
                }

                if (i == 0)
                {
                    View child = absListView.getChildAt(0);
                    float alpha = -child.getTop() * 1.0f / child.getMeasuredHeight();
                    setAlpha(topShadow, alpha);
                }
                else
                {
                    setAlpha(topShadow, 1);
                }
                if (i + i1 == i2)
                {
                    View child = absListView.getChildAt(i1-1);
                    float alpha = (child.getBottom() - absListView.getBottom()) * 1.0f / child.getMeasuredHeight();
                    setAlpha(bottomShadow, alpha);
                }
                else
                {
                    setAlpha(bottomShadow, 1);
                }
            }
        });
    }

    private void initPopupWindow()
    {
        View contentView = getLayoutInflater().inflate(R.layout.table_setting, null);

        TextView cancelBtn = (TextView)contentView.findViewById(R.id.cancel_btn);
        TextView okBtn = (TextView)contentView.findViewById(R.id.ok_btn);

        final RadioGroup missRadioGroup = (RadioGroup) contentView.findViewById(R.id.miss_radio_group);
        final RadioButton showMissBtn = (RadioButton) contentView.findViewById(R.id.show_miss);
        final RadioGroup countRadioGroup = (RadioGroup) contentView.findViewById(R.id.count_radio_group);
        final RadioButton showCountBtn = (RadioButton) contentView.findViewById(R.id.show_count);

        RelativeLayout lineSetting = (RelativeLayout)contentView.findViewById(R.id.line_setting_layout);
        lineSetting.setVisibility(View.GONE);

        missRadioGroup.check(R.id.show_miss);
        countRadioGroup.check(R.id.show_count);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                missRadioGroup.check(showMiss?R.id.show_miss:R.id.hide_miss);
                countRadioGroup.check(showCount?R.id.show_count:R.id.hide_count);
            }
        });

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean needUpdate = showMiss != showMissBtn.isChecked() || showCount != showCountBtn.isChecked();
                showMiss = showMissBtn.isChecked();
                showCount = showCountBtn.isChecked();
                popupWindow.dismiss();
                //TODO notifydatachanged
                if (needUpdate) {
                    sumTableAdapter.setShowMiss(showMiss);
                    sumTableAdapter.setShowCount(showCount);
                    baseListAdapter.setShowMiss(showMiss);
                    baseListAdapter.setShowCount(showCount);
                    sumTableAdapter.notifyDataSetChanged();
                    baseListAdapter.notifyDataSetChanged();
                    bottomAdapter.notifyDataSetChanged();
                }
            }
        });

        popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.update();
    }

    private boolean isLoaded()
    {
        return refreshLayout.getVisibility() != View.VISIBLE && loadingLayout.getVisibility() != View.VISIBLE;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @SuppressWarnings("deprecation")
    private void setAlpha(ImageView imageView, float alpha) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            imageView.setAlpha(alpha);
        } else {
            imageView.setAlpha(Math.round(alpha * 255));
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(broadcastReceiver);

        super.onDestroy();
    }
}