package com.jincaizi.kuaiwin.chart.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.jincaizi.R;
import com.jincaizi.kuaiwin.chart.TableFixHeaders.adapters.BaseTableAdapter;
import com.jincaizi.kuaiwin.chart.views.LineView;
import com.jincaizi.data.ResponseData;

/**
 * Created by chenweida on 2015/2/6.
 */
public class QuickThreeSumTableAdapter extends BaseTableAdapter {

    ResponseData responseData;

    private boolean showLine = true;
    private boolean showMiss = true;
    private boolean showCount = true;

    private Context context;

    private final String headers[] = {
            "", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15",
            "16", "17", "18",
    };

    private final float density;

    public QuickThreeSumTableAdapter(Context context, ResponseData responseData)
    {
        this.context = context;
        this.responseData = responseData;
        density = context.getResources().getDisplayMetrics().density;
    }

    public void setShowLine(boolean showLine) {
        this.showLine = showLine;
    }

    public void setShowMiss(boolean showMiss)
    {
        this.showMiss = showMiss;
    }

    public void setShowCount(boolean showCount) {
        this.showCount = showCount;
    }

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
        return 16;
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
                view = getLineView(parent);
                break;
            default:
                throw new RuntimeException("wtf?");
        }
        return view;
    }

    @Override
    public boolean hasLineView() {
        return true;
    }

    private View getFirstHeader(int row, int column, View convertView, ViewGroup parent)
    {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_table_header_first, parent, false);
        }
        ((TextView) convertView.findViewById(android.R.id.text1)).setText(headers[0]);
        return convertView;
    }

    private View getHeader(int row, int column, View convertView, ViewGroup parent)
    {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_table_header, parent, false);
        }
        ((TextView) convertView.findViewById(android.R.id.text1)).setText(headers[column + 1]);
        return convertView;
    }

    private View getFirstBody(int row, int column, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_table_first, parent, false);
        }
        convertView.setBackgroundColor(row % 2 == 0 ? context.getResources().getColor(R.color.row_bg) :
                context.getResources().getColor(R.color.chart_white));

        View divider = convertView.findViewById(R.id.divide);
        TextView textView = (TextView) convertView.findViewById(android.R.id.text1);

        divider.setVisibility(View.GONE);
        if (row < responseData.getTotalIssueCount())
        {
            textView.setTextColor(Color.BLACK);
            textView.setText(responseData.getSumResult(row, column + 1));
        }
        else if (row == responseData.getTotalIssueCount())
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

    private View getBody(int row, int column, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_table, parent, false);
        }
        convertView.setBackgroundColor(row % 2 == 0 ? context.getResources().getColor(R.color.row_bg) :
                context.getResources().getColor(R.color.chart_white));

        View divider = convertView.findViewById(R.id.divide);
        TextView textView = (TextView) convertView.findViewById(android.R.id.text1);

        divider.setVisibility(View.GONE);
        if (row < responseData.getTotalIssueCount())
        {
            textView.setTextColor(Color.BLACK);
            textView.setText(showMiss? responseData.getSumResult(row, column + 1): "");
        }
        else if (row == responseData.getTotalIssueCount())
        {
            divider.setVisibility(View.VISIBLE);
            textView.setTextColor(context.getResources().getColor(R.color.counter_first));
            textView.setText(responseData.getSumResultCount()[column] + "");
        }
        else if (row == responseData.getTotalIssueCount() + 1)
        {
            textView.setTextColor(context.getResources().getColor(R.color.counter_second));
            textView.setText(responseData.getSumMissSum()[column]/
                    responseData.getTotalIssueCount() + "");
        }
        else if (row == responseData.getTotalIssueCount() + 2)
        {
            textView.setTextColor(context.getResources().getColor(R.color.counter_third));
            textView.setText(responseData.getSumMaxMiss()[column] + "");
        }
        else if (row == responseData.getTotalIssueCount() + 3)
        {
            textView.setTextColor(context.getResources().getColor(R.color.counter_forth));
            textView.setText(responseData.getSumMissCount()[column] + "");
        }

        return convertView;
    }

    private View getLineView(ViewGroup parent)
    {
        LineView view = (LineView) LayoutInflater.from(context).inflate(R.layout.line, parent, false);
        //TODO 将行高和列宽设置为一个静态常量
        view.setParam(30, 30, responseData.getTotalSum(), responseData.getTotalIssueCount(), showLine);
        return view;
    }

    @Override
    public int getWidth(int column) {
        final int width;
        if (column == -1){
            width = 60;
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
        //rowTag是-2， 表示是lineview;
        if (row == -2 && column == -2)
        {
            itemViewType = 4;
        }
        else if (row == -1 && column == -1)
        {
            itemViewType = 0;
        }
        else if (row == -1)
        {
            itemViewType = 1;
        }
        else if (column == -1)
        {
            itemViewType = 2;
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

}
