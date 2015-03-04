
package com.jincaizi.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jincaizi.R;

/**
 * PopViewAdapter适配器
 *
 * @author yj
 */
public class PopViewAdapter extends BaseAdapter {
    private final Context mContext;
    private final ArrayList<String> mData;
    private final ArrayList<Boolean>mChecked;

    public PopViewAdapter(Context context, ArrayList<String> data, ArrayList<Boolean>checked) {
        this.mContext = context;
        this.mData = data;
        this.mChecked = checked;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mData.get(position);
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.popview_item, null, false);
            holder.type = (TextView) convertView.findViewById(R.id.popwindow_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.type.setText(mData.get(position));
        if(mChecked.get(position)) {
        	holder.type.setBackgroundResource(R.drawable.popitem_checked);
        } else {
        	holder.type.setBackgroundResource(R.drawable.popitem_normal);
        }
        return convertView;
    }

    private static class ViewHolder {
    	TextView type;
    }
}

