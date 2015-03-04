package com.jincaizi.adapters;

import java.util.ArrayList;

import com.jincaizi.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

/**
 * 投注适配器
 *
 * @author yj
 */
public class BallGridViewAdapter extends BaseAdapter {
    private final Context mContext;
    private final String[] mData;
    private final ArrayList<Boolean>mChecked;
    private final boolean mIsRed;

    public BallGridViewAdapter(Context context,String[] data, ArrayList<Boolean>checked, boolean isRed) {
        this.mContext = context;
        this.mData = data;
        this.mChecked = checked;
        this.mIsRed = isRed;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mData.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mData[position];
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.ball, null, false);
            convertView.setLayoutParams(new GridView.LayoutParams(67, 67));//重点行

            holder.type = (TextView) convertView.findViewById(R.id.tv_ssq_ball);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.type.setText(mData[position]);
        //int checkedBg = mIsRed?R.drawable.ball_red:R.drawable.ball_blue;
        if(mChecked.get(position)) {
         holder.type.setBackgroundResource(R.drawable.ball_blue);
         holder.type.setTextColor(mContext.getResources().getColor(android.R.color.white));
        } else {
         holder.type.setBackgroundResource(R.drawable.ball_gray);
         holder.type.setTextColor(mContext.getResources().getColor(R.color.blue));
        }
        return convertView;
    }

    private static class ViewHolder {
        TextView type;
    }
    
}
