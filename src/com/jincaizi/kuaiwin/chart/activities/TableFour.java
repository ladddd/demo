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

import java.util.ArrayList;

/**
 * Created by chenweida on 2015/1/26.
 */
public class TableFour extends BaseTableActivity {

    private ResponseData responseData;

    private RelativeLayout tableMainLayout;
    private ListView baseListView;
    private BaseListAdapter baseListAdapter;
    private TableFixHeaders tableFixHeaders;
    private TwoNotEqualAdapter sumTableAdapter;
//    private TwoNotEqualBottomAdapter bottomAdapter;
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
//    private TableFixHeaders bottomTable;
    private RelativeLayout menuBtn;

    private boolean showMiss = true;
    private boolean showCount = true;

    private String lotteryType;

    private boolean[] selectedNumber;

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
                menuBtn.setClickable(false);
                tableMainLayout.setVisibility(View.GONE);
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

                    menuBtn.setClickable(true);
                    loadingLayout.setVisibility(View.GONE);
                    sumTableLayout.setVisibility(subBtn.isEnabled()?View.GONE:View.VISIBLE);
                    baseListLayout.setVisibility(baseBtn.isEnabled()?View.GONE:View.VISIBLE);
                    tableMainLayout.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.test_table_four);
        super.onCreate(savedInstanceState);

        initLotteryType();

        selectedNumber = new boolean[6];
        requester = new LotteryDataRequester(this, lotteryType);
        timeRequester = new LotteryTimeRequester(this, lotteryType);

        tableMainLayout = (RelativeLayout) findViewById(R.id.table_main);
        refreshBtn = (Button) findViewById(R.id.refresh);
        refreshLayout = (RelativeLayout) findViewById(R.id.refresh_layout);
        loadingLayout = (RelativeLayout) findViewById(R.id.loading);
        baseBtn = (RelativeLayout) findViewById(R.id.title_btn_base);
        subBtn = (RelativeLayout) findViewById(R.id.title_btn_sub);
        baseSelector = (ImageView) findViewById(R.id.title_selector_base);
        subSelector = (ImageView) findViewById(R.id.title_selector_sub);

        subBtnTxt = (TextView) findViewById(R.id.sub_btn_txt);
        menuBtn = (RelativeLayout) findViewById(R.id.menu);

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
//        bottomTable = (TableFixHeaders) findViewById(R.id.bottom_layout);

        initBottom();
        initListShadow();

        broadcastReceiver = new MyBroadcastReceiver();
        registerReceiver(broadcastReceiver, new IntentFilter("haha"));
        requester.query();
        timeRequester.query();
    }

    private void initBottom()
    {
        LinearLayout bottomLayout = (LinearLayout) findViewById(R.id.bottom_layout);

        boolean drag = getIntent().getBooleanExtra(IntentData.DRAG, true);
        if (drag)
        {
//            bottomTable.setVisibility(View.GONE);
            bottomLayout.setVisibility(View.GONE);
            return;
        }

        ArrayList<Boolean> selectionList = (ArrayList<Boolean>) getIntent().getSerializableExtra(IntentData.SELECT_NUMBERS);

        if (selectionList != null && selectionList.size() >= 6)
        {
            for (int i = 0; i < 6; i++) {
                selectedNumber[i] = selectionList.get(i);
            }

            findViewById(R.id.bottom_one).setSelected(selectionList.get(0));
            findViewById(R.id.bottom_two).setSelected(selectionList.get(1));
            findViewById(R.id.bottom_three).setSelected(selectionList.get(2));
            findViewById(R.id.bottom_four).setSelected(selectionList.get(3));
            findViewById(R.id.bottom_five).setSelected(selectionList.get(4));
            findViewById(R.id.bottom_six).setSelected(selectionList.get(5));
        }

        findViewById(R.id.bottom_one).setOnClickListener(new BottomItemClickListener(0));
        findViewById(R.id.bottom_two).setOnClickListener(new BottomItemClickListener(1));
        findViewById(R.id.bottom_three).setOnClickListener(new BottomItemClickListener(2));
        findViewById(R.id.bottom_four).setOnClickListener(new BottomItemClickListener(3));
        findViewById(R.id.bottom_five).setOnClickListener(new BottomItemClickListener(4));
        findViewById(R.id.bottom_six).setOnClickListener(new BottomItemClickListener(5));

//        bottomAdapter = new TwoNotEqualBottomAdapter(TableFour.this);
//        bottomTable.setAdapter(bottomAdapter);
//        tableFixHeaders.setDelegateTable(bottomTable);
//        bottomTable.setDelegateTable(tableFixHeaders);
    }

    private class BottomItemClickListener implements View.OnClickListener
    {
        private int index;

        public BottomItemClickListener(int index)
        {
            this.index = index;
        }

        @Override
        public void onClick(View view) {
            view.setSelected(!view.isSelected());

            selectedNumber[index] = view.isSelected();
        }
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
//                    bottomAdapter.notifyDataSetChanged();
                }
            }
        });

        popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
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
    public void onFinished() {
        ArrayList<Boolean> resultList = new ArrayList<Boolean>(7);

        for (int i = 0; i < 6; i++) {
            resultList.add(i, selectedNumber[i]);
        }

        Intent intent = new Intent();
        intent.putExtra(IntentData.SELECT_NUMBERS, resultList);
        setResult(RESULT_OK, intent);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(broadcastReceiver);

        super.onDestroy();
    }
}
