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
public class ElevenFiveCountAdapter extends BaseAdapter {

    private Context context;
    private ElevenFiveData responseData;

    public ElevenFiveCountAdapter(Context context, ElevenFiveData responseData)
    {
        this.context = context;
        this.responseData = responseData;
    }

    @Override
    public int getCount() {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.eleven_five_count_item, null);
            holder = new ItemViewHolder(convertView);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ItemViewHolder) convertView.getTag();
        }

        convertView.setBackgroundColor(i % 2 == 0 ? context.getResources().getColor(R.color.row_bg) :
                context.getResources().getColor(R.color.chart_white));

        holder.issue.setTextColor(Color.BLACK);
        holder.issue.setText(StringUtil.getIssue(i + 1));
        holder.sum.setTextColor(Color.BLACK);
        holder.sum.setText(responseData.getTotalCount(i, 0));
        holder.span.setTextColor(Color.BLACK);
        holder.span.setText(responseData.getTotalCount(i, 1));
        holder.bigSmallRation.setTextColor(Color.BLACK);
        holder.bigSmallRation.setText(responseData.getTotalCount(i, 2));
        holder.oddEvenRation.setTextColor(Color.BLACK);
        holder.oddEvenRation.setText(responseData.getTotalCount(i, 3));
        holder.primeCompositeRation.setTextColor(Color.BLACK);
        holder.primeCompositeRation.setText(responseData.getTotalCount(i, 4));


        return convertView;
    }

    private class ItemViewHolder
    {
        private TextView issue;
        private TextView sum;
        private TextView span;
        private TextView bigSmallRation;
        private TextView oddEvenRation;
        private TextView primeCompositeRation;

        public ItemViewHolder(View view)
        {
            if (view == null)
            {
                return;
            }

            issue = (TextView) view.findViewById(R.id.issue);
            sum = (TextView) view.findViewById(R.id.sum);
            span = (TextView) view.findViewById(R.id.span);
            bigSmallRation = (TextView) view.findViewById(R.id.big_small_ration);
            oddEvenRation = (TextView) view.findViewById(R.id.odd_even_ration);
            primeCompositeRation = (TextView) view.findViewById(R.id.prime_composite_ration);
        }
    }
}
