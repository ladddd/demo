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
import com.jincaizi.kuaiwin.chart.adapters.FormListAdapter;
import com.jincaizi.kuaiwin.chart.requesters.LotteryDataRequester;
import com.jincaizi.kuaiwin.chart.requesters.LotteryTimeRequester;
import com.jincaizi.data.ResponseData;

import java.util.ArrayList;

/**
 * Created by chenweida on 2015/2/9.
 */
public class TestTableTwo extends BaseTableActivity {

    public static final int TYPE_THREE_EQUAL = 0x01;
    public static final int TYPE_THREE_NOT_EQUAL = 0x02;

    private ResponseData responseData;

    private LotteryDataRequester requester;
    private LotteryTimeRequester timeRequester;

    private RelativeLayout tableMainLayout;
    private LinearLayout bottomLayout;
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
    private FormListAdapter formListAdapter;

    private ImageView topShadow;
    private ImageView bottomShadow;
    private ImageView formTopShadow;
    private ImageView formBottomShadow;

    private boolean showMiss = true;
    private boolean showCount = true;

    private boolean[] selectedNumber;
    private boolean selectSpecial;

    private int type = TYPE_THREE_EQUAL;

    private String lotteryType;

    private android.os.Handler handler = new android.os.Handler();

    private MyBroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.test_table_second);
        super.onCreate(savedInstanceState);

        initLotteryType();

        requester = new LotteryDataRequester(this, lotteryType);
        timeRequester = new LotteryTimeRequester(this, lotteryType);

        selectedNumber = new boolean[6];

        tableMainLayout = (RelativeLayout) findViewById(R.id.table_main);
        refreshBtn = (Button) findViewById(R.id.refresh);
        refreshLayout = (RelativeLayout) findViewById(R.id.refresh_layout);
        loadingLayout = (RelativeLayout) findViewById(R.id.loading);
        baseBtn = (RelativeLayout) findViewById(R.id.title_btn_base);
        subBtn = (RelativeLayout) findViewById(R.id.title_btn_sub);
        baseSelector = (ImageView) findViewById(R.id.title_selector_base);
        subSelector = (ImageView) findViewById(R.id.title_selector_sub);
        menuBtn = (RelativeLayout) findViewById(R.id.menu);
        bottomLayout = (LinearLayout) findViewById(R.id.base_list_bottom);

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

        initBottom();
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

    private void initBottom()
    {
        boolean isDrag = getIntent().getBooleanExtra(IntentData.DRAG, true);
        type = getIntent().getIntExtra(IntentData.THREE_NUMBER_TYPE, TYPE_THREE_EQUAL);
        if (isDrag && type != TYPE_THREE_EQUAL)
        {
            bottomLayout.setVisibility(View.GONE);
            return;
        }
        ArrayList<Boolean> selectionList = (ArrayList<Boolean>) getIntent().getSerializableExtra(IntentData.SELECT_NUMBERS);

        if (type == TYPE_THREE_EQUAL)
        {
            ((TextView) findViewById(R.id.chose_title)).setText("选号(三同号)");
            ((TextView) findViewById(R.id.bottom_special)).setText("三同号通选");

            ((TextView)findViewById(R.id.bottom_one_text)).setTextSize(12);
            ((TextView)findViewById(R.id.bottom_two_text)).setTextSize(12);
            ((TextView)findViewById(R.id.bottom_three_text)).setTextSize(12);
            ((TextView)findViewById(R.id.bottom_four_text)).setTextSize(12);
            ((TextView)findViewById(R.id.bottom_five_text)).setTextSize(12);
            ((TextView)findViewById(R.id.bottom_six_text)).setTextSize(12);
            ((TextView)findViewById(R.id.bottom_one_text)).setText("111");
            ((TextView)findViewById(R.id.bottom_two_text)).setText("222");
            ((TextView)findViewById(R.id.bottom_three_text)).setText("333");
            ((TextView)findViewById(R.id.bottom_four_text)).setText("444");
            ((TextView)findViewById(R.id.bottom_five_text)).setText("555");
            ((TextView)findViewById(R.id.bottom_six_text)).setText("666");
        }
        else
        {
            ((TextView) findViewById(R.id.chose_title)).setText("选号(三不同号)");
            ((TextView) findViewById(R.id.bottom_special)).setText("三连号通选");
        }

        if (selectionList != null && selectionList.size() >= 7)
        {
            for (int i = 0; i < 6; i++) {
                selectedNumber[i] = selectionList.get(i);
            }
            selectSpecial = selectionList.get(6);

            findViewById(R.id.special_layout).setSelected(selectionList.get(6));
            findViewById(R.id.bottom_one).setSelected(selectionList.get(0));
            findViewById(R.id.bottom_two).setSelected(selectionList.get(1));
            findViewById(R.id.bottom_three).setSelected(selectionList.get(2));
            findViewById(R.id.bottom_four).setSelected(selectionList.get(3));
            findViewById(R.id.bottom_five).setSelected(selectionList.get(4));
            findViewById(R.id.bottom_six).setSelected(selectionList.get(5));
        }
        findViewById(R.id.special_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setSelected(!view.isSelected());
                selectSpecial = view.isSelected();
            }
        });

        findViewById(R.id.bottom_one).setOnClickListener(new BottomItemClickListener(0));
        findViewById(R.id.bottom_two).setOnClickListener(new BottomItemClickListener(1));
        findViewById(R.id.bottom_three).setOnClickListener(new BottomItemClickListener(2));
        findViewById(R.id.bottom_four).setOnClickListener(new BottomItemClickListener(3));
        findViewById(R.id.bottom_five).setOnClickListener(new BottomItemClickListener(4));
        findViewById(R.id.bottom_six).setOnClickListener(new BottomItemClickListener(5));
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
                        baseListAdapter = new BaseListAdapter(TestTableTwo.this, responseData);
                        baseListView.setAdapter(baseListAdapter);
                    }
                    else
                    {
                        baseListAdapter.notifyDataSetChanged();
                    }

                    if (formListAdapter == null)
                    {
                        formListAdapter = new FormListAdapter(TestTableTwo.this, responseData);
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
                    tableMainLayout.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    @Override
    public void onFinished() {
        ArrayList<Boolean> resultList = new ArrayList<Boolean>(7);

        for (int i = 0; i < 6; i++) {
            resultList.add(i, selectedNumber[i]);
        }
        resultList.add(6, selectSpecial);

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
