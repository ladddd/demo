package com.jincaizi.adapters;

import java.util.HashMap;
import java.util.LinkedList;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jincaizi.R;
import com.jincaizi.kuaiwin.hemaicenter.HemaiDetailActivity;
import com.jincaizi.kuaiwin.widget.RoundProgressBar;

/**
 * 合买方案详情适配器
 * 
 * @author yj
 */
public class HemaiDetailAdapter extends BaseAdapter {
	private final HemaiDetailActivity mActivity;
	private final LinkedList<HashMap<String, String>> mRecordList;
	private final LinkedList<String> mType;
	private final LinkedList<String> mXunhaoType = new LinkedList<String>();
	private String mLotteryKind;
	private boolean hiddenXuanHao = false;
	private final LinkedList<HashMap<String, String>> mXuanhaoList = new LinkedList<HashMap<String, String>>();

	public HemaiDetailAdapter(HemaiDetailActivity activity,
			LinkedList<HashMap<String, String>> list, LinkedList<String> type,
			String lotteryKind) {
		this.mActivity = activity;
		this.mRecordList = list;
		this.mLotteryKind = lotteryKind;
		this.mType = type;
		for (int i = 0; i < list.size(); i++) {
			if (mType.get(i).equals("xuanhaocontent")) {
				mXuanhaoList.add(list.get(i));
				mXunhaoType.add("xuanhaocontent");
			}
		}
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
		final int location = position;
		if (mType.get(position).equals("title")) {
			convertView = LayoutInflater.from(mActivity).inflate(
					R.layout.hemai_type_name, null, false);
			TextView name = (TextView) convertView
					.findViewById(R.id.hemai_detail_title);
			TextView qihao = (TextView) convertView
					.findViewById(R.id.hemai_detail_qihao);

			name.setText((String) mRecordList.get(position).get("title"));
			qihao.setText((String) mRecordList.get(position).get("qihao"));
		} else if (mType.get(position).equals("jindu")) {
			convertView = LayoutInflater.from(mActivity).inflate(
					R.layout.hemai_type_jindu, null, false);
			RoundProgressBar jindu = (RoundProgressBar) convertView
					.findViewById(R.id.hmtj_roundProgressBar);
			TextView zongjine = (TextView) convertView
					.findViewById(R.id.hmtj_zongjine_content);
			TextView jineAverage = (TextView) convertView
					.findViewById(R.id.hmtj_jineaverag_content);
			TextView left = (TextView) convertView
					.findViewById(R.id.hmtj_left_content);
			// jindu.setProgress(30);
			 float formatJindu = Float.valueOf(mRecordList.get(position).get(
						"jindu"));
			jindu.setProgress((int)formatJindu);
			jindu.setBaodi(Integer.valueOf(mRecordList.get(position).get(
					"baodi")));
			zongjine.setText((String) mRecordList.get(position).get("zongjine"));
			jineAverage.setText((String) mRecordList.get(position).get(
					"jineaverage"));
			left.setText((String) mRecordList.get(position).get("left"));
		} else if (mType.get(position).equals("faqizheinfo")) {
			convertView = LayoutInflater.from(mActivity).inflate(
					R.layout.hemai_type_faqizhe, null, false);
			TextView faqiren_title = (TextView) convertView
					.findViewById(R.id.info_title);
			TextView faqiren_content = (TextView) convertView
					.findViewById(R.id.info_content);

			faqiren_title.setText((String) mRecordList.get(position).get(
					"faqizhetitle"));
			faqiren_content.setText((String) mRecordList.get(position).get(
					"faqizhecontent"));
		} else if (mType.get(position).equals("fangan")) {
			convertView = LayoutInflater.from(mActivity).inflate(
					R.layout.hemai_type_faqizhe, null, false);
			TextView faqiren_title = (TextView) convertView
					.findViewById(R.id.info_title);
			TextView faqiren_content = (TextView) convertView
					.findViewById(R.id.info_content);

			faqiren_title.setText((String) mRecordList.get(position).get(
					"infotitle"));
			faqiren_content.setText((String) mRecordList.get(position).get(
					"infocontent"));
		} else if (mType.get(position).equals("xuanhaotitle")) {
			convertView = LayoutInflater.from(mActivity).inflate(
					R.layout.hemai_type_xuanhao, null, false);
			TextView xuanhao_title = (TextView) convertView
					.findViewById(R.id.xuanhao_title);
			TextView xuanhao_content = (TextView) convertView
					.findViewById(R.id.xuanhao_content);

			xuanhao_title.setText((String) mRecordList.get(position).get(
					"listtitle0"));
			xuanhao_content.setText((String) mRecordList.get(position).get(
					"listtitle1"));
			convertView.findViewById(R.id.xuanhao_expand).setVisibility(
					View.VISIBLE);
			convertView.findViewById(R.id.rv_xuanhao).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							if (mType.get(location).equals("xuanhaotitle")) {
								if (mLotteryKind.equals("shuzicai")) {
									if (hiddenXuanHao) {
										mRecordList.addAll(location + 1,
												mXuanhaoList);
										mType.addAll(location + 1, mXunhaoType);
									} else {
										mType.removeAll(mXunhaoType);
										mRecordList.removeAll(mXuanhaoList);
									}
									hiddenXuanHao = !hiddenXuanHao;

									notifyDataSetChanged();
								} else {
//									if(mRecordList.get(location).get(
//											"listtitle1").equals("点击查看")) {
//									Intent intent = new Intent(mActivity,
//											JinCaiTouzhuDetail.class);
//									intent.putExtra("hemaiid",
//											mActivity.mEntity.getHemaiId());
//									mActivity.startActivity(intent);
//									}
								}
							}
						}
					});
		} else if (mType.get(position).equals("xuanhaocontent")) {
			convertView = LayoutInflater.from(mActivity).inflate(
					R.layout.hemai_type_xuanhao, null, false);
			convertView.findViewById(R.id.rv_xuanhao).setBackgroundColor(
					mActivity.getResources().getColor(R.color.gray_111));
			TextView xuanhao_title = (TextView) convertView
					.findViewById(R.id.xuanhao_title);
			TextView xuanhao_content = (TextView) convertView
					.findViewById(R.id.xuanhao_content);

			xuanhao_title.setText((String) mRecordList.get(position).get(
					"listcontent0"));
			xuanhao_content.setText((String) mRecordList.get(position).get(
					"listcontent1"));
			convertView.findViewById(R.id.xuanhao_expand).setVisibility(
					View.GONE);
		} else if (mType.get(position).equals("jingcaititle")) {
			convertView = LayoutInflater.from(mActivity).inflate(
					R.layout.hemai_type_jincai, null, false);
			TextView changci = (TextView) convertView
					.findViewById(R.id.hemai_changci);
			TextView vs = (TextView) convertView.findViewById(R.id.hemai_VS);
			TextView touzhu = (TextView) convertView
					.findViewById(R.id.hemai_touzhu);
			TextView caiguo = (TextView) convertView
					.findViewById(R.id.hemai_caiguo);
			changci.setText(mRecordList.get(position).get("changci"));
			vs.setText(mRecordList.get(position).get("vs"));
			touzhu.setText(mRecordList.get(position).get("touzhu"));
			caiguo.setText(mRecordList.get(position).get("caiguo"));
		} else if (mType.get(position).equals("jingcaicontent")) {
			convertView = LayoutInflater.from(mActivity).inflate(
					R.layout.hemai_type_jincai, null, false);
			TextView changci = (TextView) convertView
					.findViewById(R.id.hemai_changci);
			TextView vs = (TextView) convertView.findViewById(R.id.hemai_VS);
			TextView touzhu = (TextView) convertView
					.findViewById(R.id.hemai_touzhu);
			TextView caiguo = (TextView) convertView
					.findViewById(R.id.hemai_caiguo);
			changci.setText(mRecordList.get(position).get("changci"));
			vs.setText(mRecordList.get(position).get("vs"));
			touzhu.setText(mRecordList.get(position).get("touzhu"));
			caiguo.setText(mRecordList.get(position).get("caiguo"));
		}

		return convertView;
	}

}
