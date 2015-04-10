package com.jincaizi.adapters;

import java.util.HashMap;
import java.util.LinkedList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jincaizi.R;
import com.jincaizi.kuaiwin.buycenter.K3Pick;
import com.jincaizi.kuaiwin.buycenter.SscPick;
import com.jincaizi.kuaiwin.utils.Constants.K3Type;
import com.jincaizi.kuaiwin.utils.Constants.SscType;


/**
 * k3投注详情适配器
 * 
 * @author yj
 */
public class K3TouZhuAdapter extends BaseAdapter {
	private final Context mContext;
	private final LinkedList<String> mBall;
	private final LinkedList<String> mType;
	private LinkedList<Integer>mZhushu;
	private HashMap<String, String>typeName = new HashMap<String, String>();

	public K3TouZhuAdapter(Context context, LinkedList<String> ball,
			LinkedList<String> type, LinkedList<Integer>zhushu, boolean isK3) {
		this.mContext = context;
		this.mBall = ball;
		this.mType = type;
		this.mZhushu = zhushu;
		if(isK3) {
		    generateK3TypeName();
		} else {
			generateSscTypeName();
		}
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
			convertView.setTag(holder);
            holder.delete = (ImageView) convertView.findViewById(R.id.pick_delete);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv_ball.setText(mBall.get(position));
		holder.tv_type.setText(typeName.get(mType.get(position)) + " " + mZhushu.get(position) + "注"+mZhushu.get(position)*2 + "元");

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mContext instanceof  K3Pick) {
                    ((K3Pick) mContext).deleteItemAtIndex(position);
                }
                else if (mContext instanceof SscPick)
                {
                    ((SscPick) mContext).deleteItemAtIndex(position);
                }
            }
        });

		return convertView;
	}

	private static class ViewHolder {
		public TextView tv_ball;
		public TextView tv_type;
        public ImageView delete;
	}
	private void generateK3TypeName() {
		typeName.put(K3Type.hezhi.toString(), "和值");
		typeName.put(K3Type.threesamesingle.toString(), "三同号单选");
		typeName.put(K3Type.twosamesingle.toString(), "两同号单选");
		typeName.put(K3Type.threedifsingle.toString(), "三不同号单选");
		typeName.put(K3Type.twodif.toString(), "两不同号");
		typeName.put(K3Type.dragthree.toString(), "三不同号胆拖");
		typeName.put(K3Type.dragtwo.toString(), "两不同号胆拖");
		typeName.put(K3Type.threesamedouble.toString(), "三同号通选");
		typeName.put(K3Type.twosamedouble.toString(), "两同号复选");
		typeName.put(K3Type.threedifdouble.toString(), "三不同号通选");
	}
	private void generateSscTypeName() {
		typeName.put(SscType.dxds.toString(), "大小单双");
		typeName.put(SscType.fivestar_fuxuan.toString(), "五星复选");
		typeName.put(SscType.fivestar_tongxuan.toString(), "五星通选");
		typeName.put(SscType.fivestar_zhixuan.toString(), "五星直选");
		typeName.put(SscType.fourstar_fuxuan.toString(), "四星复选");
		typeName.put(SscType.fourstar_zhixuan.toString(), "四星直选");
		typeName.put(SscType.onestar_zhixuan.toString(), "一星直选");
		typeName.put(SscType.renxuan_one.toString(), "任选一");
		typeName.put(SscType.renxuan_two.toString(), "任选二");
		typeName.put(SscType.threestar_fuxuan.toString(), "三星复选");
		typeName.put(SscType.threestar_zhixuan.toString(), "三星直选");
		typeName.put(SscType.threestar_zhixuan_hezhi.toString(), "三星直选和值");
		typeName.put(SscType.threestar_zuliu.toString(), "三星组六");
		typeName.put(SscType.threestar_zuliu_baohao.toString(), "三星组六包号");
		typeName.put(SscType.threestar_zuliu_hezhi.toString(), "三星组六和值");
		typeName.put(SscType.threestar_zusan.toString(), "三星组三");
		typeName.put(SscType.threestar_zusan_baohao.toString(), "三星组三包号");
		typeName.put(SscType.threestar_zusan_hezhi.toString(), "三星组三和值");
		typeName.put(SscType.twostar_fuxuan.toString(), "二星复选");
		typeName.put(SscType.twostar_zhixuan.toString(), "二星直选");
		typeName.put(SscType.twostar_zhixuan_hezhi.toString(), "二星直选和值");
		typeName.put(SscType.twostar_zuxuan.toString(), "二星组选");
		typeName.put(SscType.twostar_zuxuan_baohao.toString(), "二星组选包号");
		typeName.put(SscType.twostar_zuxuan_hezhi.toString(), "二星组选和值");
	}
}
