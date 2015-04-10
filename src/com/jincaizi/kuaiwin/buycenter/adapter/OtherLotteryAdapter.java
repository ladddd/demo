package com.jincaizi.kuaiwin.buycenter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.jincaizi.R;

/**
 * Created by chenweida on 2015/3/31.
 */
public class OtherLotteryAdapter extends BaseAdapter {

    private Context context;

    private String[] type;
    private int[] imageResource;

    public OtherLotteryAdapter(Context context, String[] type, int[] imageResource)
    {
        super();

        this.context = context;

        this.type = type;
        this.imageResource = imageResource;
    }

    @Override
    public int getCount() {
        return type.length;
    }

    @Override
    public Object getItem(int i) {
        return type[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ItemViewHolder holder;
        if (convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.other_lottery_item, null);
            holder = new ItemViewHolder(convertView);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ItemViewHolder) convertView.getTag();
        }

        holder.imageView.setImageResource(imageResource[i]);
        holder.textView.setText(type[i]);

        return convertView;
    }

    private class ItemViewHolder
    {
        public ImageView imageView;
        public TextView textView;

        public ItemViewHolder(View view)
        {
            if (view == null)
            {
                return;
            }

            imageView = (ImageView) view.findViewById(R.id.lottery_image);
            textView = (TextView) view.findViewById(R.id.lootery_name);
        }
    }
}
