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
import com.jincaizi.kuaiwin.chart.adapters.BaseListAdapter;
import com.jincaizi.kuaiwin.chart.adapters.TwoEqualAdapter;
import com.jincaizi.requesters.LotteryDataRequester;
import com.jincaizi.requesters.LotteryTimeRequester;
import com.jincaizi.data.ResponseData;

/**
 * Created by chenweida on 2015/2/9.
 */
public class TableThree extends BaseTableActivity {

    ResponseData responseData;

    private LotteryDataRequester requester;
    private LotteryTimeRequester timeRequester;

    private RelativeLayout loadingLayout;
    private RelativeLayout refreshLayout;
    private Button refreshBtn;
    private RelativeLayout baseBtn;
    private RelativeLayout subBtn;
    private RelativeLayout baseListLayout;
    private RelativeLayout formListLayout;
    private ImageView baseSelector;
    private ImageView subSelector;
    private PopupWindow popupWindow;
    private ListView baseListView;
    private ListView formListView;
    private RelativeLayout menuBtn;

    private BaseListAdapter baseListAdapter;
    private TwoEqualAdapter formListAdapter;

    private ImageView topShadow;
    private ImageView bottomShadow;
    private ImageView formTopShadow;
    private ImageView formBottomShadow;

    private boolean showMiss = true;
    private boolean showCount = true;

    private String lotteryType;

    private android.os.Handler handler = new android.os.Handler();

    private MyBroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.test_table_third);
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
        menuBtn = (RelativeLayout) findViewById(R.id.menu);

        topShadow = (ImageView)findViewById(R.id.top_shadow);
        bottomShadow = (ImageView)findViewById(R.id.bottom_shadow);
        formTopShadow = (ImageView)findViewById(R.id.top_shadow_second);
        formBottomShadow = (ImageView)findViewById(R.id.bottom_shadow_second);

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
                    formListLayout.setVisibility(View.GONE);
                    baseListLayout.setVisibility(View.VISIBLE);
                    baseSelector.setVisibility(View.VISIBLE);
                    subSelector.setVisibility(View.GONE);
                }
                baseBtn.setEnabled(false);
                subBtn.setEnabled(true);
            }
        });
        baseBtn.setEnabled(false);
        baseSelector.setVisibility(View.VISIBLE);
        subSelector.setVisibility(View.GONE);

        subBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isLoaded()) {
                    formListLayout.setVisibility(View.VISIBLE);
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
        menuBtn.setClickable(false);

        formListView = (ListView) findViewById(R.id.form_list);
        baseListView = (ListView) findViewById(R.id.base_list);
        baseListLayout = (RelativeLayout) findViewById(R.id.base_list_layout);
        formListLayout = (RelativeLayout) findViewById(R.id.form_list_layout);

        initListShadow();

        broadcastReceiver = new MyBroadcastReceiver();
        registerReceiver(broadcastReceiver, new IntentFilter("haha"));
        requester.query();
        timeRequester.query();
    }

    private void initLotteryType()
    {
        lotteryType = getIntent().getStringExtra(IntentData.CITY);
        if (lotteryType == null || lotteryType.length() <= 0)
        {
            finish();
        }
    }

    private boolean isLoaded()
    {
        return refreshLayout.getVisibility() != View.VISIBLE && loadingLayout.getVisibility() != View.VISIBLE;
    }

    private void initPopupWindow()
    {
        View contentView = getLayoutInflater().inflate(R.layout.table_setting, null);

        TextView cancelBtn = (TextView)contentView.findViewById(R.id.cancel_btn);
        TextView okBtn = (TextView)contentView.findViewById(R.id.ok_btn);

        RelativeLayout lineSetting = (RelativeLayout)contentView.findViewById(R.id.line_setting_layout);
        lineSetting.setVisibility(View.GONE);

        final RadioGroup missRadioGroup = (RadioGroup) contentView.findViewById(R.id.miss_radio_group);
        final RadioButton showMissBtn = (RadioButton) contentView.findViewById(R.id.show_miss);
        final RadioGroup countRadioGroup = (RadioGroup) contentView.findViewById(R.id.count_radio_group);
        final RadioButton showCountBtn = (RadioButton) contentView.findViewById(R.id.show_count);

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
                boolean needUpdate = ((showMiss != showMissBtn.isChecked()) || (showCount != showCountBtn.isChecked()));
                showMiss = showMissBtn.isChecked();
                showCount = showCountBtn.isChecked();
                popupWindow.dismiss();
                //TODO notifydatachanged
                if (needUpdate) {
                    formListAdapter.setShowMiss(showMiss);
                    formListAdapter.setShowCount(showCount);
                    baseListAdapter.setShowMiss(showMiss);
                    baseListAdapter.setShowCount(showCount);
                    formListAdapter.notifyDataSetChanged();
                    baseListAdapter.notifyDataSetChanged();
                }
            }
        });

        popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.update();
    }

    private void initListShadow()
    {
        setAlpha(topShadow, 0);
        setAlpha(formTopShadow, 0);
        setAlpha(bottomShadow, 0);
        setAlpha(formBottomShadow, 0);

        AbsListView.OnScrollListener listener = new AbsListView.OnScrollListener() {
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
                    setTopShadowAlpha(alpha);
                }
                else
                {
                    setTopShadowAlpha(1);
                }
                if (i + i1 == i2)
                {
                    View child = absListView.getChildAt(i1-1);
                    float alpha = (child.getBottom() - absListView.getBottom()) * 1.0f / child.getMeasuredHeight();
                    setBottomShadowAlpha(alpha);
                }
                else
                {
                    setBottomShadowAlpha(1);
                }
            }
        };

        baseListView.setOnScrollListener(listener);
        formListView.setOnScrollListener(listener);
    }

    private void setTopShadowAlpha(float alpha)
    {
        if (baseBtn.isEnabled())
        {
            setAlpha(formTopShadow ,alpha);
        }
        else
        {
            setAlpha(topShadow, alpha);
        }
    }

    private void setBottomShadowAlpha(float alpha)
    {
        if (baseBtn.isEnabled())
        {
            setAlpha(formBottomShadow ,alpha);
        }
        else
        {
            setAlpha(bottomShadow, alpha);
        }
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

    private class MyBroadcastReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!intent.getBooleanExtra("success",false) && baseListLayout.getVisibility() == View.GONE &&
                    formListLayout.getVisibility() == View.GONE)
            {
                menuBtn.setClickable(false);
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
                        baseListAdapter = new BaseListAdapter(TableThree.this, responseData);
                        baseListView.setAdapter(baseListAdapter);
                    }
                    else
                    {
                        baseListAdapter.notifyDataSetChanged();
                    }

                    if (formListAdapter == null)
                    {
                        formListAdapter = new TwoEqualAdapter(TableThree.this, responseData);
                        formListView.setAdapter(formListAdapter);
                    }
                    else
                    {
                        formListAdapter.notifyDataSetChanged();
                    }

                    menuBtn.setClickable(true);
                    loadingLayout.setVisibility(View.GONE);
                    formListLayout.setVisibility(subBtn.isEnabled() ? View.GONE : View.VISIBLE);
                    baseListLayout.setVisibility(baseBtn.isEnabled()?View.GONE:View.VISIBLE);
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(broadcastReceiver);

        super.onDestroy();
    }
}
