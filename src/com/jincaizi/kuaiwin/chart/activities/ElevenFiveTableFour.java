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
import com.jincaizi.kuaiwin.chart.adapters.ElevenFiveCountAdapter;
import com.jincaizi.kuaiwin.chart.adapters.ElevenFiveResultAdapter;
import com.jincaizi.kuaiwin.chart.requesters.LotteryDataRequester;
import com.jincaizi.kuaiwin.chart.requesters.LotteryTimeRequester;
import com.jincaizi.data.ElevenFiveData;

/**
 * Created by chenweida on 2015/2/12.
 */
public class ElevenFiveTableFour extends BaseTableActivity {

    private ElevenFiveData responseData;

    private LotteryDataRequester requester;
    private LotteryTimeRequester timeRequester;

    private RelativeLayout loadingLayout;
    private RelativeLayout refreshLayout;
    private Button refreshBtn;
    private RelativeLayout baseBtn;
    private RelativeLayout subBtn;
    private ImageView baseSelector;
    private ImageView subSelector;
    private RelativeLayout baseListLayout;
    private RelativeLayout formListLayout;
    private PopupWindow popupWindow;
    private ListView baseListView;
    private ListView formListView;

    private ElevenFiveResultAdapter baseListAdapter;
    private ElevenFiveCountAdapter formListAdapter;

    private ImageView topShadow;
    private ImageView bottomShadow;
    private ImageView formTopShadow;
    private ImageView formBottomShadow;

    private String lotteryType;

    private boolean showMiss = true;
    private boolean showCount = true;

    private boolean[] chosenNumber;

    private MyBroadcastReceiver broadcastReceiver;

    private android.os.Handler handler = new android.os.Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.eleven_five_table_four);
        super.onCreate(savedInstanceState);

        initLotteryType();

        chosenNumber = new boolean[11];

        requester = new LotteryDataRequester(this, lotteryType);
        timeRequester = new LotteryTimeRequester(this, lotteryType);

        refreshBtn = (Button) findViewById(R.id.refresh);
        loadingLayout = (RelativeLayout) findViewById(R.id.loading);
        refreshLayout = (RelativeLayout) findViewById(R.id.refresh_layout);
        baseBtn = (RelativeLayout) findViewById(R.id.first_btn);
        subBtn = (RelativeLayout) findViewById(R.id.second_btn);
        RelativeLayout menuBtn = (RelativeLayout) findViewById(R.id.menu);

        baseSelector = (ImageView) findViewById(R.id.title_selector_base);
        subSelector = (ImageView) findViewById(R.id.title_selector_sub);

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
                    formListLayout.setVisibility(View.VISIBLE);
                    baseListLayout.setVisibility(View.GONE);
                }
                baseBtn.setEnabled(true);
                subBtn.setEnabled(false);
                baseSelector.setVisibility(View.GONE);
                subSelector.setVisibility(View.VISIBLE);
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

        initBottom();

        formListView = (ListView) findViewById(R.id.form_list);
        baseListView = (ListView) findViewById(R.id.base_list);
        baseListLayout = (RelativeLayout) findViewById(R.id.base_list_layout);
        formListLayout = (RelativeLayout) findViewById(R.id.form_list_layout);

        initList();

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

    private void initBottom()
    {
        LinearLayout bottomLayout = (LinearLayout) findViewById(R.id.base_list_bottom);

        ((TextView)bottomLayout.findViewById(R.id.chose_title)).setText("选号(任选)");

        bottomLayout.findViewById(R.id.bottom_one).setOnClickListener(new BottomItemClickListener(0));
        bottomLayout.findViewById(R.id.bottom_two).setOnClickListener(new BottomItemClickListener(1));
        bottomLayout.findViewById(R.id.bottom_three).setOnClickListener(new BottomItemClickListener(2));
        bottomLayout.findViewById(R.id.bottom_four).setOnClickListener(new BottomItemClickListener(3));
        bottomLayout.findViewById(R.id.bottom_five).setOnClickListener(new BottomItemClickListener(4));
        bottomLayout.findViewById(R.id.bottom_six).setOnClickListener(new BottomItemClickListener(5));
        bottomLayout.findViewById(R.id.bottom_seven).setOnClickListener(new BottomItemClickListener(6));
        bottomLayout.findViewById(R.id.bottom_eight).setOnClickListener(new BottomItemClickListener(7));
        bottomLayout.findViewById(R.id.bottom_nine).setOnClickListener(new BottomItemClickListener(8));
        bottomLayout.findViewById(R.id.bottom_ten).setOnClickListener(new BottomItemClickListener(9));
        bottomLayout.findViewById(R.id.bottom_eleven).setOnClickListener(new BottomItemClickListener(10));
    }

    private class BottomItemClickListener implements View.OnClickListener {
        private int itemIndex;

        public BottomItemClickListener(int itemIndex) {
            this.itemIndex = itemIndex;
        }

        @Override
        public void onClick(View view) {
            view.setSelected(!view.isSelected());

            chosenNumber[itemIndex] = view.isSelected();
        }
    }

    private void initPopupWindow()
    {
        View contentView = getLayoutInflater().inflate(R.layout.table_setting, null);

        TextView cancelBtn = (TextView)contentView.findViewById(R.id.cancel_btn);
        TextView okBtn = (TextView)contentView.findViewById(R.id.ok_btn);

        RelativeLayout showLineLayout = (RelativeLayout) contentView.findViewById(R.id.line_setting_layout);
        showLineLayout.setVisibility(View.GONE);

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
                boolean needUpdate = (showMiss != showMissBtn.isChecked()) || (showCount != showCountBtn.isChecked());
                showMiss = showMissBtn.isChecked();
                showCount = showCountBtn.isChecked();
                popupWindow.dismiss();
                if (needUpdate) {
                    baseListAdapter.setShowMiss(showMiss);
                    baseListAdapter.setShowCount(showCount);
                    baseListAdapter.notifyDataSetChanged();
                }
            }
        });

        popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
//                    popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_launcher));
        popupWindow.update();
    }

    private void initList()
    {
        setAlpha(topShadow, 0);
        setAlpha(formTopShadow, 0);
        setAlpha(bottomShadow, 0);
        setAlpha(formBottomShadow, 0);

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

        formListView.setOnScrollListener(new AbsListView.OnScrollListener() {
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
                    setAlpha(formTopShadow, alpha);
                }
                else
                {
                    setAlpha(formTopShadow, 1);
                }
                if (i + i1 == i2)
                {
                    View child = absListView.getChildAt(i1-1);
                    float alpha = (child.getBottom() - absListView.getBottom()) * 1.0f / child.getMeasuredHeight();
                    setAlpha(formBottomShadow, alpha);
                }
                else
                {
                    setAlpha(formBottomShadow, 1);
                }
            }
        });
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

    private boolean isLoaded()
    {
        return refreshLayout.getVisibility() != View.VISIBLE && loadingLayout.getVisibility() != View.VISIBLE;
    }

    private class MyBroadcastReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!intent.getBooleanExtra("success",false) && baseListLayout.getVisibility() == View.GONE &&
            formListLayout.getVisibility() == View.GONE)
            {
                loadingLayout.setVisibility(View.GONE);
                refreshLayout.setVisibility(View.VISIBLE);
                return;
            }

            final String data = intent.getStringExtra("JSON");
            handler.post(new Runnable() {
                @Override
                public void run() {

                    responseData = new ElevenFiveData(data);

                    if (baseListAdapter == null)
                    {
                        baseListAdapter = new ElevenFiveResultAdapter(ElevenFiveTableFour.this, responseData);
                        baseListView.setAdapter(baseListAdapter);
                    }
                    else
                    {
                        baseListAdapter.notifyDataSetChanged();
                    }

                    if (formListAdapter == null)
                    {
                        formListAdapter = new ElevenFiveCountAdapter(ElevenFiveTableFour.this, responseData);
                        formListView.setAdapter(formListAdapter);
                    }
                    else
                    {
                        formListAdapter.notifyDataSetChanged();
                    }

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
