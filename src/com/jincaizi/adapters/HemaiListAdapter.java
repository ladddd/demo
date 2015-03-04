package com.jincaizi.adapters;

import java.util.LinkedList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jincaizi.bean.HemaiCenterRecordEntity;
import com.jincaizi.R;
import com.jincaizi.kuaiwin.utils.Constants.LotteryType;
import com.jincaizi.kuaiwin.widget.RoundProgressBar;

/**
 * 合买中心适配器
 *
 * @author yj
 */
public class HemaiListAdapter extends BaseAdapter {
    private final Context mContext;
    private final LinkedList<HemaiCenterRecordEntity> mRecordList;
    private LotteryType  mLotteryType;

    public LotteryType getmLotteryType() {
		return mLotteryType;
	}

	public void setmLotteryType(LotteryType mLotteryType) {
		this.mLotteryType = mLotteryType;
	}

	public HemaiListAdapter(Context context,LinkedList<HemaiCenterRecordEntity>list) {
        this.mContext = context;
        this.mRecordList = list;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mRecordList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mRecordList.get(position);
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.group_listview_item, null, false);
            holder.tv_lotteryname = (TextView)convertView.findViewById(R.id.group_lottery_name);
            holder.progress_jindu = (RoundProgressBar)convertView.findViewById(R.id.group_roundProgressBar);
            holder.tv_faqiren = (TextView)convertView.findViewById(R.id.group_faqiren);
            holder.tv_zongjine = (TextView)convertView.findViewById(R.id.group_zongjine_content);
            holder.tv_jineAverage = (TextView)convertView.findViewById(R.id.group_jineaverag_content);
            holder.tv_left = (TextView)convertView.findViewById(R.id.group_left_content);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
       // if(mLotteryType != LotteryType.ALLTYPE && !TextUtils.isEmpty(mRecordList.get(position).getBetType())) {
       // 	holder.tv_lotteryname.setText(mRecordList.get(position).getBetType());
       // } else { 
        	 holder.tv_lotteryname.setText(mRecordList.get(position).getLotteryType());
       // }
        float jindu = Float.valueOf(mRecordList.get(position).getBetJindu());
        holder.progress_jindu.setProgress((int)jindu);
        holder.progress_jindu.setBaodi(Integer.valueOf(mRecordList.get(position).getBetBaodi()));
        holder.tv_faqiren.setText(mRecordList.get(position).getBetHost());
        holder.tv_zongjine.setText(mRecordList.get(position).getBetAmount());
        holder.tv_jineAverage.setText(mRecordList.get(position).getBetAverage());
        holder.tv_left.setText(mRecordList.get(position).getBetLeft());
        return convertView;
    }

    private static class ViewHolder {
        TextView tv_lotteryname;
        RoundProgressBar progress_jindu;
        TextView tv_faqiren;
        TextView tv_zongjine;
        TextView tv_jineAverage;
        TextView tv_left;
        
    }
    
}
