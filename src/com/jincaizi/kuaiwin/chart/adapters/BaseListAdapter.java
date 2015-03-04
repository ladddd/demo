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
 * Created by chenweida on 2015/2/3.
 */
public class BaseListAdapter extends BaseAdapter {

    private Context context;

    private boolean showMiss = true;
    private boolean showCount = true;

    private ResponseData responseData;

    public BaseListAdapter(Context context, ResponseData responseData)
    {
        super();

        this.context = context;

        this.responseData = responseData;
    }

    public void setShowMiss(boolean showMiss)
    {
        this.showMiss = showMiss;
    }

    public void setShowCount(boolean showCount)
    {
        this.showCount = showCount;
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
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ItemViewHolder holder;
        if (convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.base_list_item, null);
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

            holder.normalTableHeader.setVisibility(View.VISIBLE);
            holder.subTableHeader.setVisibility(View.GONE);

            holder.issue.setTextColor(context.getResources().getColor(R.color.chart_black));
            holder.issue.setText(StringUtil.getIssue(i + 1));
            holder.result.setTextColor(context.getResources().getColor(R.color.chart_black));
            holder.result.setText(responseData.getTotalResultList()[i] + "");
            holder.sum.setTextColor(context.getResources().getColor(R.color.chart_black));
            holder.sum.setText(getResultSum(responseData.getTotalResultList()[i]) + "");
            holder.span.setTextColor(context.getResources().getColor(R.color.chart_black));
            holder.span.setText(getResultSpan(responseData.getTotalResultList()[i]) + "");

            holder.number1.setTextColor(Color.BLACK);
            holder.number1.setText(showMiss? responseData.getBaseResultMiss(i, 0) + "": "");
            holder.number2.setTextColor(Color.BLACK);
            holder.number2.setText(showMiss? responseData.getBaseResultMiss(i, 1) + "": "");
            holder.number3.setTextColor(Color.BLACK);
            holder.number3.setText(showMiss? responseData.getBaseResultMiss(i, 2) + "": "");
            holder.number4.setTextColor(Color.BLACK);
            holder.number4.setText(showMiss? responseData.getBaseResultMiss(i, 3) + "": "");
            holder.number5.setTextColor(Color.BLACK);
            holder.number5.setText(showMiss? responseData.getBaseResultMiss(i, 4) + "": "");
            holder.number6.setTextColor(Color.BLACK);
            holder.number6.setText(showMiss? responseData.getBaseResultMiss(i, 5) + "": "");

            Integer number = responseData.getBaseResultCount(i, 0);
            holder.numberBg1.setVisibility(number == 0 ? View.GONE : View.VISIBLE);
            holder.numberCount1.setText(number + "");
            holder.numberCount1.setVisibility(number > 1 ? View.VISIBLE : View.GONE);

            number = responseData.getBaseResultCount(i, 1);
            holder.numberBg2.setVisibility(number == 0 ? View.GONE : View.VISIBLE);
            holder.numberCount2.setText(number + "");
            holder.numberCount2.setVisibility(number > 1 ? View.VISIBLE : View.GONE);

            number = responseData.getBaseResultCount(i, 2);
            holder.numberBg3.setVisibility(number == 0 ? View.GONE : View.VISIBLE);
            holder.numberCount3.setText(number + "");
            holder.numberCount3.setVisibility(number > 1 ? View.VISIBLE : View.GONE);

            number = responseData.getBaseResultCount(i, 3);
            holder.numberBg4.setVisibility(number == 0 ? View.GONE : View.VISIBLE);
            holder.numberCount4.setText(number + "");
            holder.numberCount4.setVisibility(number > 1 ? View.VISIBLE : View.GONE);

            number = responseData.getBaseResultCount(i, 4);
            holder.numberBg5.setVisibility(number == 0 ? View.GONE : View.VISIBLE);
            holder.numberCount5.setText(number + "");
            holder.numberCount5.setVisibility(number > 1 ? View.VISIBLE : View.GONE);

            number = responseData.getBaseResultCount(i, 5);
            holder.numberBg6.setVisibility(number == 0 ? View.GONE : View.VISIBLE);
            holder.numberCount6.setText(number + "");
            holder.numberCount6.setVisibility(number > 1 ? View.VISIBLE : View.GONE);
        }
        else {
            if (i == responseData.getTotalIssueCount())
            {
                holder.divider.setVisibility(View.VISIBLE);

                holder.subTableHeader.setTextColor(context.getResources().getColor(R.color.counter_first));
                holder.subTableHeader.setText("出现次数");

                holder.number1.setTextColor(context.getResources().getColor(R.color.counter_first));
                holder.number1.setText(responseData.getResultCount()[0] + "");
                holder.number2.setTextColor(context.getResources().getColor(R.color.counter_first));
                holder.number2.setText(responseData.getResultCount()[1] + "");
                holder.number3.setTextColor(context.getResources().getColor(R.color.counter_first));
                holder.number3.setText(responseData.getResultCount()[2] + "");
                holder.number4.setTextColor(context.getResources().getColor(R.color.counter_first));
                holder.number4.setText(responseData.getResultCount()[3] + "");
                holder.number5.setTextColor(context.getResources().getColor(R.color.counter_first));
                holder.number5.setText(responseData.getResultCount()[4] + "");
                holder.number6.setTextColor(context.getResources().getColor(R.color.counter_first));
                holder.number6.setText(responseData.getResultCount()[5] + "");
            }
            else if (i == responseData.getTotalIssueCount() + 1)
            {
                holder.subTableHeader.setTextColor(context.getResources().getColor(R.color.counter_second));
                holder.subTableHeader.setText("平均遗漏");

                holder.number1.setTextColor(context.getResources().getColor(R.color.counter_second));
                holder.number1.setText(responseData.getMissSum()[0]/responseData.getTotalIssueCount() + "");
                holder.number2.setTextColor(context.getResources().getColor(R.color.counter_second));
                holder.number2.setText(responseData.getMissSum()[1]/responseData.getTotalIssueCount() + "");
                holder.number3.setTextColor(context.getResources().getColor(R.color.counter_second));
                holder.number3.setText(responseData.getMissSum()[2]/responseData.getTotalIssueCount() + "");
                holder.number4.setTextColor(context.getResources().getColor(R.color.counter_second));
                holder.number4.setText(responseData.getMissSum()[3]/responseData.getTotalIssueCount() + "");
                holder.number5.setTextColor(context.getResources().getColor(R.color.counter_second));
                holder.number5.setText(responseData.getMissSum()[4]/responseData.getTotalIssueCount() + "");
                holder.number6.setTextColor(context.getResources().getColor(R.color.counter_second));
                holder.number6.setText(responseData.getMissSum()[5]/responseData.getTotalIssueCount() + "");
            }
            else if (i == responseData.getTotalIssueCount() + 2)
            {
                holder.subTableHeader.setTextColor(context.getResources().getColor(R.color.counter_third));
                holder.subTableHeader.setText("最大遗漏");

                holder.number1.setTextColor(context.getResources().getColor(R.color.counter_third));
                holder.number1.setText(responseData.getMaxMiss()[0] + "");
                holder.number2.setTextColor(context.getResources().getColor(R.color.counter_third));
                holder.number2.setText(responseData.getMaxMiss()[1] + "");
                holder.number3.setTextColor(context.getResources().getColor(R.color.counter_third));
                holder.number3.setText(responseData.getMaxMiss()[2] + "");
                holder.number4.setTextColor(context.getResources().getColor(R.color.counter_third));
                holder.number4.setText(responseData.getMaxMiss()[3] + "");
                holder.number5.setTextColor(context.getResources().getColor(R.color.counter_third));
                holder.number5.setText(responseData.getMaxMiss()[4] + "");
                holder.number6.setTextColor(context.getResources().getColor(R.color.counter_third));
                holder.number6.setText(responseData.getMaxMiss()[5] + "");
            }
            else if (i == responseData.getTotalIssueCount() + 3)
            {
                holder.subTableHeader.setTextColor(context.getResources().getColor(R.color.counter_forth));
                holder.subTableHeader.setText("当前遗漏");

                holder.number1.setTextColor(context.getResources().getColor(R.color.counter_forth));
                holder.number1.setText(responseData.getMissCount()[0] + "");
                holder.number2.setTextColor(context.getResources().getColor(R.color.counter_forth));
                holder.number2.setText(responseData.getMissCount()[1] + "");
                holder.number3.setTextColor(context.getResources().getColor(R.color.counter_forth));
                holder.number3.setText(responseData.getMissCount()[2] + "");
                holder.number4.setTextColor(context.getResources().getColor(R.color.counter_forth));
                holder.number4.setText(responseData.getMissCount()[3] + "");
                holder.number5.setTextColor(context.getResources().getColor(R.color.counter_forth));
                holder.number5.setText(responseData.getMissCount()[4] + "");
                holder.number6.setTextColor(context.getResources().getColor(R.color.counter_forth));
                holder.number6.setText(responseData.getMissCount()[5] + "");
            }
            holder.normalTableHeader.setVisibility(View.GONE);
            holder.subTableHeader.setVisibility(View.VISIBLE);

            holder.numberBg1.setVisibility(View.GONE);
            holder.numberCount1.setVisibility(View.GONE);
            holder.numberBg2.setVisibility(View.GONE);
            holder.numberCount2.setVisibility(View.GONE);
            holder.numberBg3.setVisibility(View.GONE);
            holder.numberCount3.setVisibility(View.GONE);
            holder.numberBg4.setVisibility(View.GONE);
            holder.numberCount4.setVisibility(View.GONE);
            holder.numberBg5.setVisibility(View.GONE);
            holder.numberCount5.setVisibility(View.GONE);
            holder.numberBg6.setVisibility(View.GONE);
            holder.numberCount6.setVisibility(View.GONE);
        }

        return convertView;
    }

    private int getResultSum(int result)
    {
        if (result < 111 || result > 666)
        {
            return 0;
        }

        return result % 10 + (result/10)%10 + (result/100);
    }

    private int getResultSpan(int result)
    {
        if (result < 111 || result > 666)
        {
            return 0;
        }

        int max = Math.max(Math.max(result % 10, (result/10)%10), (result/100));
        int min = Math.min(Math.min(result % 10, (result / 10) % 10), (result/100));

        return max - min;
    }

    private static class ItemViewHolder
    {
        public LinearLayout normalTableHeader;
        public TextView subTableHeader;

        public View divider;

        public TextView issue;
        public TextView result;
        public TextView sum;
        public TextView span;
        public TextView number1;
        public TextView number2;
        public TextView number3;
        public TextView number4;
        public TextView number5;
        public TextView number6;
        public TextView numberBg1;
        public TextView numberBg2;
        public TextView numberBg3;
        public TextView numberBg4;
        public TextView numberBg5;
        public TextView numberBg6;
        public TextView numberCount1;
        public TextView numberCount2;
        public TextView numberCount3;
        public TextView numberCount4;
        public TextView numberCount5;
        public TextView numberCount6;

        public ItemViewHolder(View view)
        {
            if (view == null)
            {
                return;
            }

            normalTableHeader = (LinearLayout) view.findViewById(R.id.normal_table_header);
            subTableHeader = (TextView) view.findViewById(R.id.sub_table_header);

            divider = view.findViewById(R.id.divide);

            issue = (TextView) view.findViewById(R.id.issue);
            result = (TextView) view.findViewById(R.id.result);
            sum = (TextView) view.findViewById(R.id.sum);
            span = (TextView) view.findViewById(R.id.span);
            number1 = (TextView) view.findViewById(R.id.number1);
            number2 = (TextView) view.findViewById(R.id.number2);
            number3 = (TextView) view.findViewById(R.id.number3);
            number4 = (TextView) view.findViewById(R.id.number4);
            number5 = (TextView) view.findViewById(R.id.number5);
            number6 = (TextView) view.findViewById(R.id.number6);

            numberBg1 = (TextView) view.findViewById(R.id.number1_bg);
            numberBg2 = (TextView) view.findViewById(R.id.number2_bg);
            numberBg3 = (TextView) view.findViewById(R.id.number3_bg);
            numberBg4 = (TextView) view.findViewById(R.id.number4_bg);
            numberBg5 = (TextView) view.findViewById(R.id.number5_bg);
            numberBg6 = (TextView) view.findViewById(R.id.number6_bg);

            numberCount1 = (TextView) view.findViewById(R.id.number1_count);
            numberCount2 = (TextView) view.findViewById(R.id.number2_count);
            numberCount3 = (TextView) view.findViewById(R.id.number3_count);
            numberCount4 = (TextView) view.findViewById(R.id.number4_count);
            numberCount5 = (TextView) view.findViewById(R.id.number5_count);
            numberCount6 = (TextView) view.findViewById(R.id.number6_count);
        }
    }
}
