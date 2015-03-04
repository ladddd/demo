package com.jincaizi.kuaiwin.chart.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.jincaizi.R;
import com.jincaizi.common.StringUtil;
import com.jincaizi.data.ResponseData;

/**
 * Created by chenweida on 2015/2/11.
 */
public class TwoEqualAdapter extends BaseAdapter {

    private Context context;

    private ResponseData responseData;

    private boolean showCount = true;
    private boolean showMiss = true;

    public void setShowCount(boolean showCount) {
        this.showCount = showCount;
    }

    public void setShowMiss(boolean showMiss) {
        this.showMiss = showMiss;
    }

    public TwoEqualAdapter(Context context, ResponseData responseData)
    {
        this.context = context;
        this.responseData = responseData;
    }

    @Override
    public int getCount() {
        if (showCount && responseData.getTotalIssueCount() != 0)
        {
            return responseData.getTotalIssueCount() + 4;
        }
        return responseData.getTotalIssueCount();
    }

    @Override
    public Object getItem(int i) {
        if (i < responseData.getTotalIssueCount()) {
            return responseData.getTotalResultList()[i];
        }
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        ItemViewHolder holder;
        if (convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.two_equal_item, null);
            holder = new ItemViewHolder(convertView);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ItemViewHolder)convertView.getTag();
        }
        convertView.setBackgroundColor(i % 2 == 0 ? context.getResources().getColor(R.color.row_bg) :
                context.getResources().getColor(R.color.chart_white));
        holder.divider.setVisibility(View.GONE);

        if (i < responseData.getTotalIssueCount()) {
            holder.issue.setText(StringUtil.getIssue(i + 1));
            holder.result.setTextColor(Color.BLACK);
            holder.result.setText(responseData.getTotalResultList()[i] + "");

            holder.normalHeaderLayout.setVisibility(View.VISIBLE);
            holder.countHeader.setVisibility(View.GONE);
            for (int j = 0; j < 6; j++) {
                if (responseData.getTwoEqualResult(i, j) == 0)
                {
                    holder.getTextViewAtIndex(j).setTextColor(Color.WHITE);
                    holder.getTextViewAtIndex(j).setBackgroundColor(
                            context.getResources().getColor(R.color.mark_green));
                    holder.getTextViewAtIndex(j).setText(StringUtil.getTwoEqualString(j+1));
                }
                else
                {
                    holder.getTextViewAtIndex(j).setTextColor(Color.BLACK);
                    holder.getTextViewAtIndex(j).setBackgroundColor(Color.TRANSPARENT);
                    holder.getTextViewAtIndex(j).setText(showMiss?responseData.getTwoEqualResult(i, j) + "": "");
                }
            }
        }
        else
        {
            if (i == responseData.getTotalIssueCount())
            {
                holder.divider.setVisibility(View.VISIBLE);
                holder.countHeader.setTextColor(context.getResources().getColor(R.color.counter_first));
                holder.countHeader.setText("出现次数");
                for (int j = 0; j < 6; j++) {
                    holder.getTextViewAtIndex(j).setTextColor(context.getResources().getColor(R.color.counter_first));
                    holder.getTextViewAtIndex(j).setBackgroundColor(Color.TRANSPARENT);
                    holder.getTextViewAtIndex(j).setText(responseData.getTwoEqualResultCount()[j] + "");
                }
            }
            else if (i == responseData.getTotalIssueCount() + 1)
            {
                holder.countHeader.setTextColor(context.getResources().getColor(R.color.counter_second));
                holder.countHeader.setText("平均遗漏");
                for (int j = 0; j < 6; j++) {
                    holder.getTextViewAtIndex(j).setTextColor(context.getResources().getColor(R.color.counter_second));
                    holder.getTextViewAtIndex(j).setBackgroundColor(Color.TRANSPARENT);
                    holder.getTextViewAtIndex(j).setText(responseData.getTwoEqualMissSum()[j]
                            /responseData.getTotalIssueCount() + "");
                }
            }
            else if (i == responseData.getTotalIssueCount() + 2)
            {
                holder.countHeader.setTextColor(context.getResources().getColor(R.color.counter_third));
                holder.countHeader.setText("最大遗漏");
                for (int j = 0; j < 6; j++) {
                    holder.getTextViewAtIndex(j).setTextColor(context.getResources().getColor(R.color.counter_third));
                    holder.getTextViewAtIndex(j).setBackgroundColor(Color.TRANSPARENT);
                    holder.getTextViewAtIndex(j).setText(responseData.getTwoEqualMaxMiss()[j] + "");
                }
            }
            else if (i == responseData.getTotalIssueCount() + 3)
            {
                holder.countHeader.setTextColor(context.getResources().getColor(R.color.counter_forth));
                holder.countHeader.setText("当前遗漏");
                for (int j = 0; j < 6; j++) {
                    holder.getTextViewAtIndex(j).setTextColor(context.getResources().getColor(R.color.counter_forth));
                    holder.getTextViewAtIndex(j).setBackgroundColor(Color.TRANSPARENT);
                    holder.getTextViewAtIndex(j).setText(responseData.getTwoEqualMissCount()[j] + "");
                }
            }
            holder.normalHeaderLayout.setVisibility(View.GONE);
            holder.countHeader.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    private class ItemViewHolder
    {
        public View convertView;
        public View divider;
        public LinearLayout normalHeaderLayout;
        public TextView countHeader;
        public TextView issue;
        public TextView result;

        public TextView oneOne;
        public TextView twoTwo;
        public TextView threeThree;
        public TextView fourFour;
        public TextView fiveFive;
        public TextView sixSix;

        public ItemViewHolder(View view)
        {
            convertView = view;

            normalHeaderLayout = (LinearLayout) view.findViewById(R.id.normal_table_header);
            countHeader = (TextView) view.findViewById(R.id.sub_table_header);
            issue = (TextView) view.findViewById(R.id.issue);
            result = (TextView) view.findViewById(R.id.result);
            divider = view.findViewById(R.id.divide);

            oneOne = (TextView) view.findViewById(R.id.one_one);
            twoTwo = (TextView) view.findViewById(R.id.two_two);
            threeThree = (TextView) view.findViewById(R.id.three_three);
            fourFour = (TextView) view.findViewById(R.id.four_four);
            fiveFive = (TextView) view.findViewById(R.id.five_five);
            sixSix = (TextView) view.findViewById(R.id.six_six);
        }

        public TextView getTextViewAtIndex(int index)
        {
            switch (index)
            {
                case 0:
                    return oneOne;
                case 1:
                    return twoTwo;
                case 2:
                    return threeThree;
                case 3:
                    return fourFour;
                case 4:
                    return fiveFive;
                case 5:
                    return sixSix;
                default:
                    return null;
            }
        }
    }
}
