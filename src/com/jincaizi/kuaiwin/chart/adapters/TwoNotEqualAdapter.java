package com.jincaizi.kuaiwin.chart.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.jincaizi.R;
import com.jincaizi.common.StringUtil;
import com.jincaizi.data.ResponseData;
import com.jincaizi.kuaiwin.chart.TableFixHeaders.adapters.BaseTableAdapter;

/**
 * Created by chenweida on 2015/2/12.
 */
public class TwoNotEqualAdapter extends BaseTableAdapter {

    ResponseData responseData;

    private boolean showMiss = true;
    private boolean showCount = true;

    private Context context;

    private final float density;

    public TwoNotEqualAdapter(Context context, ResponseData responseData)
    {
        this.context = context;
        this.responseData = responseData;

        density = context.getResources().getDisplayMetrics().density;
    }

    private final String headers[] = {
            "", "开奖号", "12", "13", "14", "15", "16", "23", "24", "25", "26", "34", "35", "36",
            "45", "46", "56",
    };

    @Override
    public int getRowCount() {
        if (showCount && responseData.getTotalIssueCount() != 0)
        {
            return responseData.getTotalIssueCount() + 4;
        }
        return responseData.getTotalIssueCount();
    }

    @Override
    public int getColumnCount() {
        return 15;
    }

    @Override
    public View getView(int row, int column, View convertView, ViewGroup parent) {
        final View view;
        switch (getItemViewType(row, column)) {
            case 0:
                view = getFirstHeader(row, column, convertView, parent);
                break;
            case 1:
                view = getHeader(row, column, convertView, parent);
                break;
            case 2:
                view = getFirstBody(row, column, convertView, parent);
                break;
            case 3:
                view = getBody(row, column, convertView, parent);
                break;
            case 4:
                view = getCountFirst(row, column, convertView, parent);
                break;
            default:
                throw new RuntimeException("wtf?");
        }
        return view;
    }

    private View getFirstHeader(int row, int column, View convertView, ViewGroup parent)
    {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.two_not_equal_header, parent, false);
        }
        ((TextView) convertView.findViewById(R.id.issue)).setText(headers[0]);
        ((TextView) convertView.findViewById(R.id.result)).setText(headers[1]);
        return convertView;
    }

    private View getHeader(int row, int column, View convertView, ViewGroup parent)
    {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_table_header, parent, false);
        }
        ((TextView) convertView.findViewById(android.R.id.text1)).setText(headers[column + 2]);
        return convertView;
    }

    private View getCountFirst(int row, int column, View convertView, ViewGroup parent)
    {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_table, parent, false);
        }

        convertView.setBackgroundColor(row % 2 == 1 ? Color.WHITE : context.getResources().getColor(R.color.row_bg));

        TextView textView = (TextView) convertView.findViewById(android.R.id.text1);
        View divider = convertView.findViewById(R.id.divide);

        divider.setVisibility(View.GONE);
        if (row == responseData.getTotalIssueCount())
        {
            divider.setVisibility(View.VISIBLE);
            textView.setTextColor(context.getResources().getColor(R.color.counter_first));
            textView.setText("出现次数");
        }
        else if (row == responseData.getTotalIssueCount() + 1)
        {
            textView.setTextColor(context.getResources().getColor(R.color.counter_second));
            textView.setText("平均遗漏");
        }
        else if (row == responseData.getTotalIssueCount() + 2)
        {
            textView.setTextColor(context.getResources().getColor(R.color.counter_third));
            textView.setText("最大遗漏");
        }
        else if (row == responseData.getTotalIssueCount() + 3)
        {
            textView.setTextColor(context.getResources().getColor(R.color.counter_forth));
            textView.setText("当前遗漏");
        }
        return convertView;
    }

    private View getFirstBody(int row, int column, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.two_not_equal_header, parent, false);
        }
        convertView.setBackgroundColor(row % 2 == 1 ? Color.WHITE : context.getResources().getColor(R.color.row_bg));

        TextView issue = (TextView) convertView.findViewById(R.id.issue);
        TextView result = (TextView) convertView.findViewById(R.id.result);

        issue.setText(StringUtil.getIssue(row + 1));
        result.setTextColor(Color.BLACK);
        result.setText(responseData.getTotalResultList()[row] + "");

        return convertView;
    }

    private View getBody(int row, int column, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_table, parent, false);
        }
        convertView.setBackgroundColor(row % 2 == 1 ? Color.WHITE : context.getResources().getColor(R.color.row_bg));

        View divider = convertView.findViewById(R.id.divide);
        TextView textView = (TextView) convertView.findViewById(android.R.id.text1);

        divider.setVisibility(View.GONE);
        textView.setTextColor(Color.BLACK);
        if (row < responseData.getTotalIssueCount())
        {
            textView.setTextColor(Color.BLACK);
            if (responseData.getTwoNotEqualResult(row, column) == 0)
            {
                textView.setTextColor(Color.WHITE);
                convertView.setBackgroundColor(context.getResources().getColor(R.color.mark_green));
                textView.setText(headers[column + 2]);
            }
            else
            {
                textView.setText(showMiss? responseData.getTwoNotEqualResult(row, column) + "" : "");
            }
        }
        else if (row == responseData.getTotalIssueCount())
        {
            divider.setVisibility(View.VISIBLE);
            textView.setTextColor(context.getResources().getColor(R.color.counter_first));
            textView.setText(responseData.getTwoNotEqualResultCount()[column] + "");
        }
        else if (row == responseData.getTotalIssueCount() + 1)
        {
            textView.setTextColor(context.getResources().getColor(R.color.counter_second));
            textView.setText(responseData.getTwoNotEqualMissSum()[column]/
                    responseData.getTotalIssueCount() + "");
        }
        else if (row == responseData.getTotalIssueCount() + 2)
        {
            textView.setTextColor(context.getResources().getColor(R.color.counter_third));
            textView.setText(responseData.getTwoNotEqualMaxMiss()[column] + "");
        }
        else if (row == responseData.getTotalIssueCount() + 3)
        {
            textView.setTextColor(context.getResources().getColor(R.color.counter_forth));
            textView.setText(responseData.getTwoNotEqualMissCount()[column] + "");
        }

        return convertView;
    }

    @Override
    public int getWidth(int column) {
        final int width;
        if (column == -1){
            width = 125;
        }
        else {
            width = 30;
        }
        return Math.round(width * density);
    }

    @Override
    public int getHeight(int row) {
        final int height = 30;
        return Math.round(height * density);
    }

    @Override
    public int getItemViewType(int row, int column) {
        final int itemViewType;
        if (row == -1 && column == -1)
        {
            itemViewType = 0;
        }
        else if (row == -1)
        {
            itemViewType = 1;
        }
        else if (column == -1 && row < responseData.getTotalIssueCount())
        {
            itemViewType = 2;
        }
        else if (column == -1 && row >= responseData.getTotalIssueCount())
        {
            itemViewType = 4;
        }
        else
        {
            itemViewType = 3;
        }
        return itemViewType;
    }

    @Override
    public int getViewTypeCount() {
        return 5;
    }

    @Override
    public boolean hasLineView() {
        return false;
    }

    public void setShowCount(boolean showCount) {
        this.showCount = showCount;
    }

    public void setShowMiss(boolean showMiss) {
        this.showMiss = showMiss;
    }
}
