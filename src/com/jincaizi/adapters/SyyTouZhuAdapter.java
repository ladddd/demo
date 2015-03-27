package com.jincaizi.adapters;

import java.util.LinkedList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jincaizi.R;
import com.jincaizi.kuaiwin.buycenter.SyxwPick;
import com.jincaizi.kuaiwin.utils.Constants;
import com.jincaizi.kuaiwin.utils.Constants.ShiyiyunType;


/**
 * 十一运夺金投注详情适配器
 * 
 * @author yj
 */
public class SyyTouZhuAdapter extends BaseAdapter {
	private final Context mContext;
	private final LinkedList<String> mBall;
	private final LinkedList<ShiyiyunType> mType;
    private final LinkedList<Integer> betCountList;

	public SyyTouZhuAdapter(Context context, LinkedList<String> ball,
			LinkedList<ShiyiyunType> type, LinkedList<Integer> betCountList) {
		this.mContext = context;
		this.mBall = ball;
		this.mType = type;
        this.betCountList = betCountList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mBall.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mBall.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.pls_touzhu_item, null, false);
			holder.tv_ball = (TextView) convertView
					.findViewById(R.id.tv_pls_item);
			holder.tv_type = (TextView) convertView
					.findViewById(R.id.tv_pls_type);
            holder.delete = (ImageView) convertView.findViewById(R.id.pick_delete);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv_ball.setText(mBall.get(position));

        StringBuilder builder = new StringBuilder();
		if (mType.get(position) == Constants.ShiyiyunType.ANYTWO) {
			builder.append("任选二");
		} else if (mType.get(position) == Constants.ShiyiyunType.ANYTHREE) {
            builder.append("任选三");
		} else if (mType.get(position) == Constants.ShiyiyunType.ANYFOUR) {
            builder.append("任选四");
		} else if (mType.get(position) == Constants.ShiyiyunType.ANYFIVE) {
            builder.append("任选五");
		} else if (mType.get(position) == Constants.ShiyiyunType.ANYSIX) {
            builder.append("任选六");
		} else if (mType.get(position) == Constants.ShiyiyunType.ANYSEVEN) {
            builder.append("任选七");
		} else if (mType.get(position) == Constants.ShiyiyunType.ANYEIGHT) {
            builder.append("任选五");
		} else if (mType.get(position) == Constants.ShiyiyunType.FRONTONEZHI) {
            builder.append("前一直选");
		} else if (mType.get(position) == Constants.ShiyiyunType.FRONTTWOZHI) {
            builder.append("前二直选");
		} else if (mType.get(position) == Constants.ShiyiyunType.FRONTTWOZU) {
            builder.append("前二组选");
		} else if (mType.get(position) == Constants.ShiyiyunType.FRONTTHREEZHI) {
            builder.append("前三直选");
		} else if (mType.get(position) == Constants.ShiyiyunType.FRONTTHREEZU) {
			builder.append("前三组选");
		} else if (mType.get(position) == Constants.ShiyiyunType.ANYTWODRAG) {
			builder.append("任选二胆拖");
		} else if (mType.get(position) == Constants.ShiyiyunType.ANYTHREEDRAG) {
			builder.append("任选三胆拖");
		} else if (mType.get(position) == Constants.ShiyiyunType.ANYFOURDRAG) {
			builder.append("任选四胆拖");
		} else if (mType.get(position) == Constants.ShiyiyunType.ANYFIVEDRAG) {
			builder.append("任选五胆拖");
		} else if (mType.get(position) == Constants.ShiyiyunType.ANYSIXDRAG) {
			builder.append("任选六胆拖");
		} else if (mType.get(position) == Constants.ShiyiyunType.ANYSEVENDRAG) {
			builder.append("任选七胆拖");
		} else if (mType.get(position) == Constants.ShiyiyunType.ANYEIGHTDRAG) {
			builder.append("任选八胆拖");
		} else if (mType.get(position) == Constants.ShiyiyunType.FRONTTWOZUDRAG) {
			builder.append("前二组选胆拖");
		} else if (mType.get(position) == Constants.ShiyiyunType.FRONTTHREEZUDRAG) {
			builder.append("前三组选胆拖");
		}
        builder.append(" ");
        builder.append(String.valueOf(betCountList.get(position)));
        builder.append("注");
        builder.append(String.valueOf(betCountList.get(position) * 2));
        builder.append("元");

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((SyxwPick)mContext).deleteItemAtPosition(position);
            }
        });

        holder.tv_type.setText(builder);
		return convertView;
	}

	private static class ViewHolder {
		public TextView tv_ball;
		public TextView tv_type;
        public ImageView delete;
	}
}
