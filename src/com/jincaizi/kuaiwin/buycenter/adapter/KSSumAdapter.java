package com.jincaizi.kuaiwin.buycenter.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.jincaizi.R;
import com.jincaizi.common.NumberUtil;
import com.jincaizi.kuaiwin.buycenter.K3_hz_fragment;

/**
 * Created by chenweida on 2015/3/10.
 */
public class KSSumAdapter extends BaseAdapter{

    private K3_hz_fragment fragment;

    private boolean[] selectedNumber;

    public KSSumAdapter(K3_hz_fragment fragment, boolean[] selectedNumber)
    {
        this.fragment = fragment;
        this.selectedNumber = selectedNumber;
    }

    @Override
    public int getCount() {
        return 16;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        ItemViewHolder holder;
        if (convertView == null)
        {
            convertView = LayoutInflater.from(fragment.getActivity().getApplicationContext()).inflate(R.layout.choice_item, null);
            holder = new ItemViewHolder(convertView);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ItemViewHolder) convertView.getTag();
        }

        holder.selectCube.setSelected(selectedNumber[i]);
        holder.type.setText((i+3) + "");
        holder.tips.setText(fragment.getActivity().getApplicationContext()
                .getString(R.string.number_tips, NumberUtil.getSumLotteryMoney(i)));

        holder.selectCube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setSelected(!view.isSelected());
                fragment.updateSumChoice(i, view.isSelected());
            }
        });

        return convertView;
    }

    private class ItemViewHolder
    {
        public TextView type;
        public TextView tips;
        public TextView leak;

        public RelativeLayout selectCube;

        public ItemViewHolder(View view)
        {
            if (view == null)
            {
                return;
            }

            type = (TextView) view.findViewById(R.id.type);
            tips = (TextView) view.findViewById(R.id.tips);
            leak = (TextView) view.findViewById(R.id.leak);
            selectCube = (RelativeLayout) view.findViewById(R.id.select_cube);
        }
    }
}
