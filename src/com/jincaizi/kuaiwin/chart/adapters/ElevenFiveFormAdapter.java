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
public class ElevenFiveFormAdapter extends BaseAdapter {

    private Context context;
    private ElevenFiveData responseData;

    private boolean showCount = true;

    private boolean showMiss = true;
    public ElevenFiveFormAdapter(Context context, ElevenFiveData responseData)
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
            convertView = LayoutInflater.from(context).inflate(R.layout.eleven_five_form_item, null);
            holder = new ItemViewHolder(convertView);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ItemViewHolder) convertView.getTag();
        }

        convertView.setBackgroundColor(i % 2 == 0 ? context.getResources().getColor(R.color.row_bg) :
                context.getResources().getColor(R.color.chart_white));
        holder.divider.setVisibility(View.GONE);
        if (i < responseData.getTotalIssueCount())
        {
            holder.issue.setTextColor(Color.BLACK);
            holder.issue.setText(StringUtil.getIssue(i + 1));
            for (int j = 0; j < 7; j++) {
                setFormResultData(i, j, holder);
            }
        }
        else if (i == responseData.getTotalIssueCount())
        {
            holder.divider.setVisibility(View.VISIBLE);
            holder.issue.setTextColor(context.getResources().getColor(R.color.counter_first));
            holder.issue.setText("出现次数");
            for (int j = 0; j < 7; j++) {
                holder.getTextViewAtIndex(j).setBackgroundColor(Color.TRANSPARENT);
                holder.getTextViewAtIndex(j).setTextColor(context.getResources().getColor(R.color.counter_first));
                holder.getTextViewAtIndex(j).setText(responseData.getFormResultCount()[j] + "");
            }
        }
        else if (i == responseData.getTotalIssueCount() + 1)
        {
            holder.issue.setTextColor(context.getResources().getColor(R.color.counter_second));
            holder.issue.setText("平均遗漏");
            for (int j = 0; j < 7; j++) {
                holder.getTextViewAtIndex(j).setBackgroundColor(Color.TRANSPARENT);
                holder.getTextViewAtIndex(j).setTextColor(context.getResources().getColor(R.color.counter_second));
                holder.getTextViewAtIndex(j).setText(responseData.getFormMissSum()[j]
                        /responseData.getTotalIssueCount() + "");
            }
        }
        else if (i == responseData.getTotalIssueCount() + 2)
        {
            holder.issue.setTextColor(context.getResources().getColor(R.color.counter_third));
            holder.issue.setText("最大遗漏");
            for (int j = 0; j < 7; j++) {
                holder.getTextViewAtIndex(j).setBackgroundColor(Color.TRANSPARENT);
                holder.getTextViewAtIndex(j).setTextColor(context.getResources().getColor(R.color.counter_third));
                holder.getTextViewAtIndex(j).setText(responseData.getFormMaxMiss()[j] + "");
            }
        }
        else if (i == responseData.getTotalIssueCount() + 3)
        {
            holder.issue.setTextColor(context.getResources().getColor(R.color.counter_forth));
            holder.issue.setText("当前遗漏");
            for (int j = 0; j < 7; j++) {
                holder.getTextViewAtIndex(j).setBackgroundColor(Color.TRANSPARENT);
                holder.getTextViewAtIndex(j).setTextColor(context.getResources().getColor(R.color.counter_forth));
                holder.getTextViewAtIndex(j).setText(responseData.getFormMissCount()[j] + "");
            }
        }
        return convertView;
    }

    private void setFormResultData(int row, int column, ItemViewHolder holder)
    {
        int missCount = responseData.getFormResult(row, column);
        if (missCount == 0)
        {
            holder.getTextViewAtIndex(column).setBackgroundColor(getItemColor(column));
            holder.getTextViewAtIndex(column).setTextColor(Color.WHITE);
            holder.getTextViewAtIndex(column).setText(getItemText(column));
        }
        else
        {
            holder.getTextViewAtIndex(column).setBackgroundColor(Color.TRANSPARENT);
            holder.getTextViewAtIndex(column).setTextColor(Color.BLACK);
            holder.getTextViewAtIndex(column).setText(showMiss?missCount + "": "");
        }
    }

    private String getItemText(int column)
    {
        switch (column)
        {
            case 0:return "奇";
            case 1:return "偶";
            case 2:return "质";
            case 3:return "合";
            case 4:return "0路";
            case 5:return "1路";
            case 6:return "2路";
            default:return "";
        }
    }

    private int getItemColor(int column)
    {
        switch (column)
        {
            case 0:
            case 1:return context.getResources().getColor(R.color.three_equal_bg);
            case 2:
            case 3:return context.getResources().getColor(R.color.counter_first);
            case 4:
            case 5:
            case 6:return context.getResources().getColor(R.color.counter_third);
            default:return Color.TRANSPARENT;
        }
    }

    public void setShowCount(boolean showCount) {
        this.showCount = showCount;
    }

    public void setShowMiss(boolean showMiss) {
        this.showMiss = showMiss;
    }

    private class ItemViewHolder
    {
        public View divider;
        public TextView issue;
        public TextView odd;
        public TextView even;
        public TextView prime;
        public TextView composite;
        public TextView zero;
        public TextView one;
        public TextView two;

        public ItemViewHolder(View view)
        {
            if (view == null)
            {
                return;
            }

            divider = view.findViewById(R.id.divide);
            issue = (TextView) view.findViewById(R.id.issue);
            odd = (TextView) view.findViewById(R.id.odd);
            even = (TextView) view.findViewById(R.id.even);
            prime = (TextView) view.findViewById(R.id.prime);
            composite = (TextView) view.findViewById(R.id.composite);
            zero = (TextView) view.findViewById(R.id.zero);
            one = (TextView) view.findViewById(R.id.one);
            two = (TextView) view.findViewById(R.id.two);
        }

        public TextView getTextViewAtIndex(int i)
        {
            switch (i)
            {
                case 0:return odd;
                case 1:return even;
                case 2:return prime;
                case 3:return composite;
                case 4:return zero;
                case 5:return one;
                case 6:return two;
                default:return null;
            }
        }
    }
}
