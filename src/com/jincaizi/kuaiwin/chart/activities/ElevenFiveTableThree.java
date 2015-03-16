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
import com.jincaizi.kuaiwin.chart.adapters.ElevenFiveFigureAdapter;
import com.jincaizi.kuaiwin.chart.adapters.ElevenFiveFormAdapter;
import com.jincaizi.kuaiwin.chart.requesters.LotteryDataRequester;
import com.jincaizi.kuaiwin.chart.requesters.LotteryTimeRequester;
import com.jincaizi.kuaiwin.chart.views.ElevenFiveLineView;
import com.jincaizi.data.ElevenFiveData;

import java.util.ArrayList;

/**
 * Created by chenweida on 2015/2/12.
 */
public class ElevenFiveTableThree extends BaseTableActivity {

    private ElevenFiveData responseData;

    private LotteryDataRequester requester;
    private LotteryTimeRequester timeRequester;

    private RelativeLayout tableMainLayout;
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
    private RelativeLayout menuBtn;

    private ElevenFiveLineView firstLineView;

    private ElevenFiveFigureAdapter baseListAdapter;
    private ElevenFiveFormAdapter formListAdapter;

    private ImageView topShadow;
    private ImageView bottomShadow;
    private ImageView formTopShadow;
    private ImageView formBottomShadow;

    private String lotteryType;

    private boolean showLine = true;
    private boolean showMiss = true;
    private boolean showCount = true;

    private boolean[] firstChosenNumber;

    private MyBroadcastReceiver broadcastReceiver;

    private android.os.Handler handler = new android.os.Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.eleven_five_table_three);
        super.onCreate(savedInstanceState);

        initLotteryType();

        firstChosenNumber = new boolean[11];

        requester = new LotteryDataRequester(this, lotteryType);
        timeRequester = new LotteryTimeRequester(this, lotteryType);

        tableMainLayout = (RelativeLayout) findViewById(R.id.table_main);
        refreshBtn = (Button) findViewById(R.id.refresh);
        loadingLayout = (RelativeLayout) findViewById(R.id.loading);
        refreshLayout = (RelativeLayout) findViewById(R.id.refresh_layout);
        baseBtn = (RelativeLayout) findViewById(R.id.first_btn);
        subBtn = (RelativeLayout) findViewById(R.id.second_btn);
        menuBtn = (RelativeLayout) findViewById(R.id.menu);

        baseSelector = (ImageView) findViewById(R.id.title_selector_base);
        subSelector = (ImageView) findViewById(R.id.title_selector_sub);

        topShadow = (ImageView)findViewById(R.id.top_shadow);
        bottomShadow = (ImageView)findViewById(R.id.bottom_shadow);
        formTopShadow = (ImageView)findViewById(R.id.top_shadow_second);
        formBottomShadow = (ImageView)findViewById(R.id.bottom_shadow_second);

        firstLineView = (ElevenFiveLineView)findViewById(R.id.first_Lineview);

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
        menuBtn.setClickable(false);

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

        ArrayList<Boolean> selectionList = (ArrayList<Boolean>) getIntent().getSerializableExtra(IntentData.SELECT_NUMBERS);

        if (selectionList != null && selectionList.size() >= 11)
        {
            for (int i = 0; i < 11; i++) {
                firstChosenNumber[i] = selectionList.get(i);
            }

            bottomLayout.findViewById(R.id.bottom_one).setSelected(selectionList.get(0));
            bottomLayout.findViewById(R.id.bottom_two).setSelected(selectionList.get(1));
            bottomLayout.findViewById(R.id.bottom_three).setSelected(selectionList.get(2));
            bottomLayout.findViewById(R.id.bottom_four).setSelected(selectionList.get(3));
            bottomLayout.findViewById(R.id.bottom_five).setSelected(selectionList.get(4));
            bottomLayout.findViewById(R.id.bottom_six).setSelected(selectionList.get(5));
            bottomLayout.findViewById(R.id.bottom_seven).setSelected(selectionList.get(6));
            bottomLayout.findViewById(R.id.bottom_eight).setSelected(selectionList.get(7));
            bottomLayout.findViewById(R.id.bottom_nine).setSelected(selectionList.get(8));
            bottomLayout.findViewById(R.id.bottom_ten).setSelected(selectionList.get(9));
            bottomLayout.findViewById(R.id.bottom_eleven).setSelected(selectionList.get(10));
        }

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

            firstChosenNumber[itemIndex] = view.isSelected();
        }
    }

    private void initPopupWindow()
    {
        View contentView = getLayoutInflater().inflate(R.layout.table_setting, null);

        TextView cancelBtn = (TextView)contentView.findViewById(R.id.cancel_btn);
        TextView okBtn = (TextView)contentView.findViewById(R.id.ok_btn);

        final RadioGroup lineRadioGroup = (RadioGroup) contentView.findViewById(R.id.line_radio_group);
        final RadioButton showLineBtn = (RadioButton) contentView.findViewById(R.id.show_line);
        final RadioGroup missRadioGroup = (RadioGroup) contentView.findViewById(R.id.miss_radio_group);
        final RadioButton showMissBtn = (RadioButton) contentView.findViewById(R.id.show_miss);
        final RadioGroup countRadioGroup = (RadioGroup) contentView.findViewById(R.id.count_radio_group);
        final RadioButton showCountBtn = (RadioButton) contentView.findViewById(R.id.show_count);

        lineRadioGroup.check(R.id.show_line);
        missRadioGroup.check(R.id.show_miss);
        countRadioGroup.check(R.id.show_count);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                lineRadioGroup.check(showLine?R.id.show_line:R.id.hide_line);
                missRadioGroup.check(showMiss?R.id.show_miss:R.id.hide_miss);
                countRadioGroup.check(showCount?R.id.show_count:R.id.hide_count);
            }
        });

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean needUpdate = (showLine != showLineBtn.isChecked()) ||
                        (showMiss != showMissBtn.isChecked()) || (showCount != showCountBtn.isChecked());
                showLine = showLineBtn.isChecked();
                showMiss = showMissBtn.isChecked();
                showCount = showCountBtn.isChecked();
                popupWindow.dismiss();
                if (needUpdate) {
                    firstLineView.setShowLine(showLine);
                    formListAdapter.setShowMiss(showMiss);
                    formListAdapter.setShowCount(showCount);
                    baseListAdapter.setShowMiss(showMiss);
                    baseListAdapter.setShowCount(showCount);
                    formListAdapter.notifyDataSetChanged();
                    baseListAdapter.notifyDataSetChanged();

                    firstLineView.invalidate();
                }
            }
        });

        popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
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

                firstLineView.scrollTo(0, -absListView.getChildAt(0).getTop() +
                        i * (absListView.getChildAt(0).getMeasuredHeight() - 1));

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

                    responseData = new ElevenFiveData(data);

                    if (baseListAdapter == null)
                    {
                        baseListAdapter = new ElevenFiveFigureAdapter(ElevenFiveTableThree.this, responseData, 0);
                        baseListView.setAdapter(baseListAdapter);
                    }
                    else
                    {
                        baseListAdapter.notifyDataSetChanged();
                    }

                    if (formListAdapter == null)
                    {
                        formListAdapter = new ElevenFiveFormAdapter(ElevenFiveTableThree.this, responseData);
                        formListView.setAdapter(formListAdapter);
                    }
                    else
                    {
                        formListAdapter.notifyDataSetChanged();
                    }

                    menuBtn.setClickable(true);
                    LinearLayout itemView = (LinearLayout) findViewById(R.id.base_list_title);
                    itemView.measure(0, 0);
                    firstLineView.setParam(itemView.getMeasuredHeight(), responseData.getFirstResultArray(), responseData.getTotalIssueCount(), showLine);
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
        ArrayList<Boolean> resultList = new ArrayList<Boolean>();

        for (int i = 0; i < 11; i++) {
            resultList.add(i, firstChosenNumber[i]);
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
