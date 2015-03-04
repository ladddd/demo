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
 * Created by chenweida on 2015/2/10.
 */
public class FormListAdapter extends BaseAdapter {

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

    public FormListAdapter(Context context, ResponseData responseData)
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
            convertView = LayoutInflater.from(context).inflate(R.layout.form_list_title, null);
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

            if (responseData.getFormResult(i, 0) == 0) {
                holder.threeEqual.setBackgroundColor(context.getResources().getColor(R.color.three_equal_bg));
                holder.threeEqual.setTextColor(Color.WHITE);
                holder.threeEqual.setText("三同号");
            }
            else
            {
                holder.threeEqual.setBackgroundColor(Color.TRANSPARENT);
                holder.threeEqual.setTextColor(Color.BLACK);
                holder.threeEqual.setText(showMiss?responseData.getFormResult(i, 0) + "":"");
            }
            if (responseData.getFormResult(i, 1) == 0) {
                holder.threeNotEqual.setBackgroundColor(context.getResources().getColor(R.color.three_not_equal_bg));
                holder.threeNotEqual.setTextColor(Color.WHITE);
                holder.threeNotEqual.setText("三不同");
            }
            else
            {
                holder.threeNotEqual.setBackgroundColor(Color.TRANSPARENT);
                holder.threeNotEqual.setTextColor(Color.BLACK);
                holder.threeNotEqual.setText(showMiss?responseData.getFormResult(i, 1) + "": "");
            }
            if (responseData.getFormResult(i, 2) == 0) {
                holder.twoEqual.setBackgroundColor(context.getResources().getColor(R.color.counter_forth));
                holder.twoEqual.setTextColor(Color.WHITE);
                holder.twoEqual.setText("二同号");
            }
            else
            {
                holder.twoEqual.setBackgroundColor(Color.TRANSPARENT);
                holder.twoEqual.setTextColor(Color.BLACK);
                holder.twoEqual.setText(showMiss?responseData.getFormResult(i, 2)  + "": "");
            }
            if (responseData.getFormResult(i, 3) == 0) {
                holder.twoNotEqual.setBackgroundColor(context.getResources().getColor(R.color.counter_third));
                holder.twoNotEqual.setTextColor(Color.WHITE);
                holder.twoNotEqual.setText("二不同");
            }
            else
            {
                holder.twoNotEqual.setBackgroundColor(Color.TRANSPARENT);
                holder.twoNotEqual.setTextColor(Color.BLACK);
                holder.twoNotEqual.setText(showMiss?responseData.getFormResult(i, 3)  + "": "");
            }
        }
        else
        {
            holder.threeEqual.setBackgroundColor(Color.TRANSPARENT);
            holder.threeNotEqual.setBackgroundColor(Color.TRANSPARENT);
            holder.twoEqual.setBackgroundColor(Color.TRANSPARENT);
            holder.twoNotEqual.setBackgroundColor(Color.TRANSPARENT);

            if (i == responseData.getTotalIssueCount())
            {
                holder.divider.setVisibility(View.VISIBLE);
                holder.countHeader.setTextColor(context.getResources().getColor(R.color.counter_first));
                holder.threeEqual.setTextColor(context.getResources().getColor(R.color.counter_first));
                holder.countHeader.setText("出现次数");
                holder.threeEqual.setText(responseData.getFormResultCount()[0] + "");
                holder.threeNotEqual.setTextColor(context.getResources().getColor(R.color.counter_first));
                holder.threeNotEqual.setText(responseData.getFormResultCount()[1] + "");
                holder.twoEqual.setTextColor(context.getResources().getColor(R.color.counter_first));
                holder.twoEqual.setText(responseData.getFormResultCount()[2] + "");
                holder.twoNotEqual.setTextColor(context.getResources().getColor(R.color.counter_first));
                holder.twoNotEqual.setText(responseData.getFormResultCount()[3] + "");
            }
            else if (i == responseData.getTotalIssueCount() + 1)
            {
                holder.countHeader.setTextColor(context.getResources().getColor(R.color.counter_second));
                holder.countHeader.setText("平均遗漏");
                holder.threeEqual.setTextColor(context.getResources().getColor(R.color.counter_second));
                holder.threeEqual.setText(responseData.getFormMissSum()[0] / responseData.getTotalIssueCount() + "");
                holder.threeNotEqual.setTextColor(context.getResources().getColor(R.color.counter_second));
                holder.threeNotEqual.setText(responseData.getFormMissSum()[1] / responseData.getTotalIssueCount() + "");
                holder.twoEqual.setTextColor(context.getResources().getColor(R.color.counter_second));
                holder.twoEqual.setText(responseData.getFormMissSum()[2] / responseData.getTotalIssueCount() + "");
                holder.twoNotEqual.setTextColor(context.getResources().getColor(R.color.counter_second));
                holder.twoNotEqual.setText(responseData.getFormMissSum()[3]/responseData.getTotalIssueCount() + "");
            }
            else if (i == responseData.getTotalIssueCount() + 2)
            {
                holder.countHeader.setTextColor(context.getResources().getColor(R.color.counter_third));
                holder.countHeader.setText("最大遗漏");
                holder.threeEqual.setTextColor(context.getResources().getColor(R.color.counter_third));
                holder.threeEqual.setText(responseData.getFormMaxMiss()[0] + "");
                holder.threeNotEqual.setTextColor(context.getResources().getColor(R.color.counter_third));
                holder.threeNotEqual.setText(responseData.getFormMaxMiss()[1] + "");
                holder.twoEqual.setTextColor(context.getResources().getColor(R.color.counter_third));
                holder.twoEqual.setText(responseData.getFormMaxMiss()[2] + "");
                holder.twoNotEqual.setTextColor(context.getResources().getColor(R.color.counter_third));
                holder.twoNotEqual.setText(responseData.getFormMaxMiss()[3] + "");
            }
            else if (i == responseData.getTotalIssueCount() + 3)
            {
                holder.countHeader.setTextColor(context.getResources().getColor(R.color.counter_forth));
                holder.countHeader.setText("当前遗漏");
                holder.threeEqual.setTextColor(context.getResources().getColor(R.color.counter_forth));
                holder.threeEqual.setText(responseData.getFormMissCount()[0] + "");
                holder.threeNotEqual.setTextColor(context.getResources().getColor(R.color.counter_forth));
                holder.threeNotEqual.setText(responseData.getFormMissCount()[1] + "");
                holder.twoEqual.setTextColor(context.getResources().getColor(R.color.counter_forth));
                holder.twoEqual.setText(responseData.getFormMissCount()[2] + "");
                holder.twoNotEqual.setTextColor(context.getResources().getColor(R.color.counter_forth));
                holder.twoNotEqual.setText(responseData.getFormMissCount()[3] + "");
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

        public TextView threeEqual;
        public TextView threeNotEqual;
        public TextView twoEqual;
        public TextView twoNotEqual;

        public ItemViewHolder(View view)
        {
            convertView = view;

            normalHeaderLayout = (LinearLayout) view.findViewById(R.id.normal_table_header);
            countHeader = (TextView) view.findViewById(R.id.sub_table_header);
            issue = (TextView) view.findViewById(R.id.issue);
            result = (TextView) view.findViewById(R.id.result);
            divider = view.findViewById(R.id.divide);

            threeEqual = (TextView) view.findViewById(R.id.three_equal);
            threeNotEqual = (TextView) view.findViewById(R.id.three_not_equal);
            twoEqual = (TextView) view.findViewById(R.id.two_equal);
            twoNotEqual = (TextView) view.findViewById(R.id.two_not_equal);
        }
    }
}
