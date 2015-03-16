package com.jincaizi.kuaiwin.buycenter.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.jincaizi.R;
import com.jincaizi.kuaiwin.buycenter.K3_hz_fragment;

/**
 * Created by chenweida on 2015/3/11.
 */
public class TwoSameAdapter extends BaseAdapter {

    public static final int SINGLE_SELECT_SAME = 0;
    public static final int SINGLE_SELECT_DIFF = 1;
    public static final int MULTI_SELECT = 2;

    private K3_hz_fragment fragment;

    private boolean[] selectedNumber;

    private int type;

    public TwoSameAdapter(K3_hz_fragment fragment, boolean[] selectedNumber, int type)
    {
        this.fragment = fragment;
        this.selectedNumber = selectedNumber;
        this.type = type;
    }

    @Override
    public int getCount() {
        return 6;
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

        holder.selectCube.setSelected(selectedNumber[i + type*6]);
        switch (type)
        {
            case SINGLE_SELECT_SAME:
                holder.type.setText(String.valueOf((i+1)*10 + (i+1)));
                break;
            case SINGLE_SELECT_DIFF:
                holder.type.setText(String.valueOf((i+1)));
                break;
            case MULTI_SELECT:
                holder.type.setText(String.valueOf((i+1)*10 + (i+1)) + "*");
                break;
            default:
                break;
        }

        holder.tips.setVisibility(View.GONE);

        holder.selectCube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setSelected(!view.isSelected());
                fragment.updateTwoEqualChoice(i+type*6, view.isSelected());
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
