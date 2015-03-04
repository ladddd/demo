package com.jincaizi.adapters;

import java.util.LinkedList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jincaizi.bean.MyLotteryRecordEntity;
import com.jincaizi.kuaiwin.BuyCenter;
import com.jincaizi.R;

/**
 * 投注详情适配器
 *
 * @author yj
 */
public class MylotteryRecordAdapter extends BaseAdapter {
    private final Context mContext;
    private final LinkedList<MyLotteryRecordEntity> mRecordList;

    public MylotteryRecordAdapter(Context context,LinkedList<MyLotteryRecordEntity>list) {
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
    	//Log.d("test", "position = " + position);
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.mylottery_record_item, null, false);
            holder.tv_qiNum = (TextView)convertView.findViewById(R.id.tv_lottery_qi_num);
            holder.tv_time = (TextView)convertView.findViewById(R.id.tv_lottery_time);
            holder.tv_money = (TextView)convertView.findViewById(R.id.tv_lottery_money);
            holder.tv_buy_type = (TextView)convertView.findViewById(R.id.tv_lottery_buytype);
            holder.tv_award_status = (TextView)convertView.findViewById(R.id.tv_lottery_awardstatus);
            holder.tv_procedure_status = (TextView)convertView.findViewById(R.id.tv_lottery_procedurestatus);
            holder.iv_logo = (ImageView)convertView.findViewById(R.id.iv_lottery_logo);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_qiNum.setText(mRecordList.get(position).getQiHao() + "期");
        holder.tv_time.setText(mRecordList.get(position).getTime());
        holder.tv_money.setText(mRecordList.get(position).getMoney());
        holder.tv_buy_type.setText(mRecordList.get(position).getLotteryType());
        holder.tv_award_status.setText(mRecordList.get(position).getAwardStatus());
        holder.tv_procedure_status.setText(mRecordList.get(position).getProcedureStatus());
        _setLotteryLogo(mRecordList.get(position).getLotteryType(), holder.iv_logo);
        return convertView;
    }

    private static class ViewHolder {
        TextView tv_qiNum;
        TextView tv_time;
        TextView tv_money;
        TextView tv_buy_type;
        TextView tv_award_status;
        TextView tv_procedure_status;
        ImageView iv_logo;
        
    }
    private void _setLotteryLogo (String lotteryType, ImageView iv) {
    	int index = 0;
    	for(int i=0; i<BuyCenter.itemName.length; i++) {
    		if(lotteryType.equals(BuyCenter.itemName[i])) {
    			index = i;
    			break;
    		}
    	}
    	//Picasso.with(mContext).load(BuyCenter.itemImage[index]).into(iv);
    	iv.setBackgroundResource(BuyCenter.itemImage[index]);

    }
}
