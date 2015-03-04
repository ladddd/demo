package com.jincaizi.adapters;

import com.jincaizi.R;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ToggleButton;

/**
 * 投注适配器
 *
 * @author yj
 */
public class K3GridViewAdapter extends BaseAdapter {
    private final Context mContext;
    private final String[] mData;
    private final boolean[]mChecked;

    public K3GridViewAdapter(Context context,String[] data, boolean[]checked) {
        this.mContext = context;
        this.mData = data;
        this.mChecked = checked;
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.k3_gridview_item, null, false);
            convertView.setLayoutParams(new GridView.LayoutParams(67, 67));//重点行

            holder.type = (ToggleButton) convertView.findViewById(R.id.k3_toggle_btn);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        SpannableString ss = new SpannableString(mData[position]);
        int index = mData[position].indexOf(" ");
        ss.setSpan(new AbsoluteSizeSpan(12), index, mData[position].length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.type.setText(ss);
        holder.type.setTextOff(ss);
        holder.type.setTextOn(ss);
        holder.type.setOnClickListener(new MyClick(position));
        holder.type.setChecked(mChecked[position]);
        //if(mChecked[position]) {
         //holder.type.setBackgroundResource(R.drawable.k3_toggle_pre);
         //holder.type.setTextColor(mContext.getResources().getColor(android.R.color.white));
       // } else {
        // holder.type.setBackgroundResource(R.drawable.k3_toggle_nor);
        // holder.type.setTextColor(mContext.getResources().getColor(android.R.color.black));
       // }
        return convertView;
    }

    private static class ViewHolder {
        ToggleButton type;
    }
    class MyClick implements OnClickListener {
    	int position;

		public MyClick(int position) {
			super();
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			ToggleButton tb = (ToggleButton)v;
			mChecked[position] = tb.isChecked();
			
		}
    	
    }
    
}
