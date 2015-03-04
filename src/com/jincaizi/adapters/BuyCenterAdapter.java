package com.jincaizi.adapters;

import com.jincaizi.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * 购彩大厅列表适配器
 */
public class BuyCenterAdapter extends BaseAdapter {
    private final LayoutInflater mInflater;
    private boolean mIsDelMode;
    private String[]mLotteryType;
    private int[]mLotteryImage;
    private String[]mDescription;
    private Context mContext;

    public BuyCenterAdapter(Context context, String[]types, int[]images, String[]description) {
        mInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.mLotteryType = types;
        this.mLotteryImage = images;
        this.mDescription = description;
    }

    public boolean ismIsDelMode() {
        return mIsDelMode;
    }

    public void setmIsDelMode(boolean mIsDelMode) {
        this.mIsDelMode = mIsDelMode;
    }

    @Override
    public int getCount() {
        return mLotteryType.length;
    }
    @Override
    public Object getItem(int position) {
        return mLotteryType[position];
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.buycenter_listview_item, null);
            holder.typeImage = (ImageView) convertView.findViewById(R.id.iv_lottery_type);
            holder.typeName = (TextView) convertView.findViewById(R.id.tv_lottery_type);
            holder.description = (TextView)convertView.findViewById(R.id.tv_description);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
       
        holder.typeName.setText(mLotteryType[position]);
        holder.typeImage.setBackgroundResource(mLotteryImage[position]);
        holder.description.setText(mDescription[position]);
        switch(position) {
        case 8:
        case 9:
        case 10:
        	holder.typeName.setCompoundDrawablesWithIntrinsicBounds(0, 0,R.drawable.k3_hall_image, 0);
       	    holder.typeName.setCompoundDrawablePadding(1);
       	    convertView.setBackgroundResource(R.drawable.huo);
       	    break;
        case 11:
        	 holder.typeName.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        	convertView.setBackgroundResource(R.drawable.nighticon);
        	break;
        case 7:
        case 12:
        case 20:
        	 holder.typeName.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        	convertView.setBackgroundResource(R.drawable.tingshou_image);
        	break;
        case 13:
        case 14:
        	 holder.typeName.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        	convertView.setBackgroundResource(R.drawable.renqi_image);
        	break;
        case 15:
        case 16:
        case 17:
        case 18:
        case 19:
        	 holder.typeName.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        	convertView.setBackgroundResource(R.drawable.hall_nor_bg);
        	break;
        	default:
        		 holder.typeName.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        		 convertView.setBackgroundResource(R.drawable.huo);
        		 break;
        }
        return convertView;
    }
    static class ViewHolder {
        public ImageView typeImage;
        public TextView typeName;
        public TextView description;
    }
}
