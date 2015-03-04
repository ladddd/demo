package com.jincaizi.kuaiwin.chart.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.jincaizi.R;
import com.jincaizi.common.StringUtil;
import com.jincaizi.data.ElevenFiveData;

/**
 * Created by chenweida on 2015/2/14.
 */
public class ElevenFiveResultAdapter extends BaseAdapter {

    private Context context;
    private ElevenFiveData responseData;

    private boolean showCount = true;
    private boolean showMiss = true;

    public void setShowCount(boolean showCount) {
        this.showCount = showCount;
    }

    public void setShowMiss(boolean showMiss) {
        this.showMiss = showMiss;
    }

    public ElevenFiveResultAdapter(Context context, ElevenFiveData responseData)
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
            convertView = LayoutInflater.from(context).inflate(R.layout.eleven_five_figure_item, null);
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
        if (i < responseData.getTotalIssueCount())
        {
            holder.issue.setTextColor(Color.BLACK);
            holder.issue.setText(StringUtil.getIssue(i + 1));
            for (int j = 0; j < 11; j++) {
                setResult(i, j, holder);
            }
        }
        else if (i == responseData.getTotalIssueCount())
        {
            holder.divider.setVisibility(View.VISIBLE);
            holder.issue.setTextColor(context.getResources().getColor(R.color.counter_first));
            holder.issue.setText("出现次数");
            for (int j = 0; j < 11; j++) {
                holder.getTextViewAtIndex(j).setBackgroundColor(Color.TRANSPARENT);
                holder.getTextViewAtIndex(j).setTextColor(context.getResources().getColor(R.color.counter_first));
                holder.getTextViewAtIndex(j).setText(responseData.getTotalResultCount()[j] + "");
            }
        }
        else if (i == responseData.getTotalIssueCount() + 1)
        {
            holder.issue.setTextColor(context.getResources().getColor(R.color.counter_second));
            holder.issue.setText("平均遗漏");
            for (int j = 0; j < 11; j++) {
                holder.getTextViewAtIndex(j).setBackgroundColor(Color.TRANSPARENT);
                holder.getTextViewAtIndex(j).setTextColor(context.getResources().getColor(R.color.counter_second));
                holder.getTextViewAtIndex(j).setText(responseData.getTotalMissSum()[j]
                        /responseData.getTotalIssueCount() + "");
            }
        }
        else if (i == responseData.getTotalIssueCount() + 2)
        {
            holder.issue.setTextColor(context.getResources().getColor(R.color.counter_third));
            holder.issue.setText("最大遗漏");
            for (int j = 0; j < 11; j++) {
                holder.getTextViewAtIndex(j).setBackgroundColor(Color.TRANSPARENT);
                holder.getTextViewAtIndex(j).setTextColor(context.getResources().getColor(R.color.counter_third));
                holder.getTextViewAtIndex(j).setText(responseData.getTotalMaxMiss()[j] + "");
            }
        }
        else if (i == responseData.getTotalIssueCount() + 3)
        {
            holder.issue.setTextColor(context.getResources().getColor(R.color.counter_forth));
            holder.issue.setText("当前遗漏");
            for (int j = 0; j < 11; j++) {
                holder.getTextViewAtIndex(j).setBackgroundColor(Color.TRANSPARENT);
                holder.getTextViewAtIndex(j).setTextColor(context.getResources().getColor(R.color.counter_forth));
                holder.getTextViewAtIndex(j).setText(responseData.getTotalMissCount()[j] + "");
            }
        }

        return convertView;
    }

    private void setResult(int row, int column, ItemViewHolder holder)
    {
        int missCount = responseData.getTotalResult(row, column);
        if (missCount == 0)
        {
            holder.getTextViewAtIndex(column).setBackgroundResource(R.drawable.round_bg);
            holder.getTextViewAtIndex(column).setTextColor(Color.WHITE);
            holder.getTextViewAtIndex(column).setText(StringUtil.getResultNumberString(column+1));
        }
        else
        {
            holder.getTextViewAtIndex(column).setBackgroundColor(Color.TRANSPARENT);
            holder.getTextViewAtIndex(column).setTextColor(Color.BLACK);
            holder.getTextViewAtIndex(column).setText(showMiss?missCount + "": "");
        }
    }

    private class ItemViewHolder
    {
        public TextView issue;
        public TextView one;
        public TextView two;
        public TextView three;
        public TextView four;
        public TextView five;
        public TextView six;
        public TextView seven;
        public TextView eight;
        public TextView nine;
        public TextView ten;
        public TextView eleven;
        public View divider;


        public ItemViewHolder(View view)
        {
            if (view == null)
            {
                return;
            }

            issue = (TextView) view.findViewById(R.id.issue);
            one = (TextView) view.findViewById(R.id.one);
            two = (TextView) view.findViewById(R.id.two);
            three = (TextView) view.findViewById(R.id.three);
            four = (TextView) view.findViewById(R.id.four);
            five = (TextView) view.findViewById(R.id.five);
            six = (TextView) view.findViewById(R.id.six);
            seven = (TextView) view.findViewById(R.id.seven);
            eight = (TextView) view.findViewById(R.id.eight);
            nine = (TextView) view.findViewById(R.id.nine);
            ten = (TextView) view.findViewById(R.id.ten);
            eleven = (TextView) view.findViewById(R.id.eleven);

            divider = view.findViewById(R.id.divide);
        }

        public TextView getTextViewAtIndex(int index)
        {
            switch (index)
            {
                case 0:return one;
                case 1:return two;
                case 2:return three;
                case 3:return four;
                case 4:return five;
                case 5:return six;
                case 6:return seven;
                case 7:return eight;
                case 8:return nine;
                case 9:return ten;
                case 10:return eleven;
                default:return null;
            }
        }
    }
}
