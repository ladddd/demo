package com.jincaizi.kuaiwin.buycenter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.jincaizi.R;
import com.jincaizi.kuaiwin.buycenter.BuyFragment;

/**
 * Created by chenweida on 2015/3/30.
 */
public class ElevenFiveAdapter extends BaseAdapter {

    private String[] lotteryType;
    private int[] lotteryImage;
    private String[] description;
    private Context context;
    private int type;

    public ElevenFiveAdapter(Context context, int type)
    {
        this.context = context;
        this.type = type;

        switch (type)
        {
            case BuyFragment.ELEVEN_FIVE:
                lotteryType = new String[]{ "十一运夺金","江西11选5" ,"广东11选5","安徽11选5", "重庆11选5",
                        "辽宁11选5", "上海11选5","黑龙江11选5", "浙江11选5"};
                lotteryImage = new int[]{R.drawable.logo_sd_11x5, R.drawable.logo_jx_11x5,
                        R.drawable.logo_gd_11x5, R.drawable.logo_ah_11x5, R.drawable.logo_cq_11x5,
                        R.drawable.logo_ln_11x5, R.drawable.logo_sh_11x5, R.drawable.logo_hlj_11x5,
                        R.drawable.logo_zj_11x5,
                };
                description = new String[]{"10分钟一期，返奖率高达59%", "10分钟一期，轻松赢千元","10分钟一期，玩法多，中奖快","10分钟一期，轻松赢大奖",
                        "10分钟一期，轻松赢千元","10分钟一期，返奖率高达59%","一天开奖90次，好玩易中过把瘾",
                        "10分钟一期，玩法多，中奖快", "10分钟一期，轻松赢大奖"};
                break;
            case BuyFragment.QUIKE_THREE:
                lotteryType = new String[]{"江苏快3", "吉林快3", "安徽快3", "湖北快3", "内蒙古快3"};
                lotteryImage = new int[]{R.drawable.logo_js_k3, R.drawable.logo_jl_k3, R.drawable.logo_ah_k3,
                R.drawable.logo_hb_k3, R.drawable.logo_nmg_k3,};
                description = new String[]{"10分钟一期，好玩易中", "骰子摇一摇，轻松好玩易中奖", "一看就会，趣味性高，易中奖",
                        "骰子摇一摇，轻松好玩易中奖", "10分钟一期，好玩易中"};
                break;
            case BuyFragment.OTHER:
                lotteryType = new String[]{"重庆时时彩", "江西时时彩"};
                lotteryImage = new int[]{R.drawable.logo_ssc, R.drawable.logo_jx_ssc,};
                description = new String[]{"独有夜间版，单注最高赢十万", "一天开奖84次，10分钟赢11.6万"};
                break;
            default:
                break;
        }
    }
    @Override
    public int getCount() {
        return lotteryType.length;
    }

    @Override
    public Object getItem(int i) {
        return lotteryType[i];
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
            convertView = LayoutInflater.from(context).inflate(R.layout.lottery_type_item, null);
            holder = new ItemViewHolder(convertView);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ItemViewHolder) convertView.getTag();
        }

        holder.firstDivider.setVisibility((i == 0)? View.VISIBLE: View.GONE);
        holder.lastDivider.setVisibility((i == getCount()-1)?View.VISIBLE: View.GONE);

        holder.imageView.setImageResource(lotteryImage[i]);
        holder.type.setText(lotteryType[i]);
        holder.description.setText(description[i]);

        return convertView;
    }

    private class ItemViewHolder
    {
        public ImageView imageView;
        public TextView type;
        public TextView description;
        public View firstDivider;
        public View lastDivider;

        public ItemViewHolder(View view)
        {
            if (view == null)
            {
                return;
            }
            imageView = (ImageView)view.findViewById(R.id.iv_lottery_type);
            type = (TextView)view.findViewById(R.id.tv_lottery_type);
            description = (TextView)view.findViewById(R.id.tv_description);
            firstDivider = view.findViewById(R.id.first_diveider);
            lastDivider = view.findViewById(R.id.last_diveider);
        }
    }
}
