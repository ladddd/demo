package com.jincaizi.kuaiwin.buycenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.jincaizi.R;
import com.jincaizi.common.IntentData;
import com.jincaizi.kuaiwin.buycenter.adapter.ElevenFiveAdapter;
import com.jincaizi.kuaiwin.utils.Constants;

/**
 * Created by chenweida on 2015/3/30.
 */
public class BuyFragment extends Fragment {

    public static final int ELEVEN_FIVE = 0;
    public static final int QUIKE_THREE = 1;
    public static final int OTHER  = 2;

    private int type;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        type = arguments.getInt(IntentData.BUY_TYPE, ELEVEN_FIVE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout layout = (LinearLayout)inflater.inflate(R.layout.buy_center_list, null);
        ListView listView = (ListView)layout.findViewById(R.id.buy_listview);

        listView.setAdapter(new ElevenFiveAdapter(getActivity(), type));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (type)
                {
                    case ELEVEN_FIVE:
                        handleElevenFiveItemClick(i);
                        break;
                    case QUIKE_THREE:
                        HandleQuickThreeItemClick(i);
                        break;
                    case OTHER:
                        HandleOtherItemClick(i);
                        break;
                    default:
                        break;
                }
            }
        });

        return layout;
    }

    private void handleElevenFiveItemClick(int position)
    {
        switch (position) {
            case 0:
                Intent syyjIntent = new Intent();
                syyjIntent.setClass(getActivity(), Syxw.class);
                syyjIntent.putExtra(Syxw.CITY, Constants.City.shandong.toString());
                startActivity(syyjIntent);
                break;
            case 1:
                Intent syxw_jx_Intent = new Intent(getActivity(), Syxw.class);
                syxw_jx_Intent.putExtra(Syxw.CITY, Constants.City.jiangxi.toString());
                startActivity(syxw_jx_Intent);
                break;
            case 2:
                Intent syxw_gd_Intent = new Intent(getActivity(), Syxw.class);
                syxw_gd_Intent.putExtra(Syxw.CITY, Constants.City.guangdong.toString());
                startActivity(syxw_gd_Intent);
                break;
            case 3:
                Intent syxw_ah_Intent = new Intent(getActivity(), Syxw.class);
                syxw_ah_Intent.putExtra(Syxw.CITY, Constants.City.anhui.toString());
                startActivity(syxw_ah_Intent);
                break;
            case 4:
                Intent syxw_cq_Intent = new Intent(getActivity(), Syxw.class);
                syxw_cq_Intent.putExtra(Syxw.CITY, Constants.City.chongqing.toString());
                startActivity(syxw_cq_Intent);
                break;
            case 5:
                Intent syxw_ln_Intent = new Intent(getActivity(), Syxw.class);
                syxw_ln_Intent.putExtra(Syxw.CITY, Constants.City.liaoning.toString());
                startActivity(syxw_ln_Intent);
                break;
            case 6:
                Intent syxw_sh_Intent = new Intent(getActivity(), Syxw.class);
                syxw_sh_Intent.putExtra(Syxw.CITY, Constants.City.shanghai.toString());
                startActivity(syxw_sh_Intent);
                break;
            case 7:
                Intent syxw_hlj_Intent = new Intent(getActivity(), Syxw.class);
                syxw_hlj_Intent.putExtra(Syxw.CITY, Constants.City.heilongjiang.toString());
                startActivity(syxw_hlj_Intent);
                break;
            case 8:
                Intent syxw_zj_Intent = new Intent(getActivity(), Syxw.class);
                syxw_zj_Intent.putExtra(Syxw.CITY, Constants.City.zhejiang.toString());
                startActivity(syxw_zj_Intent);
                break;
            default:
                break;
        }
    }

    private void HandleQuickThreeItemClick(int position)
    {
        switch (position)
        {
            case 0:
                Intent k3_js_Intent = new Intent(getActivity(), K3.class);
                k3_js_Intent.putExtra(Syxw.CITY, Constants.City.jiangsu.toString());
                startActivity(k3_js_Intent);
                break;
            case 1:
                Intent k3_jl_Intent = new Intent(getActivity(), K3.class);
                k3_jl_Intent.putExtra(Syxw.CITY, Constants.City.jilin.toString());
                startActivity(k3_jl_Intent);
                break;
            case 2:
                Intent k3_ah_Intent = new Intent(getActivity(), K3.class);
                k3_ah_Intent.putExtra(Syxw.CITY, Constants.City.anhui.toString());
                startActivity(k3_ah_Intent);
                break;
            case 3:
                Intent k3_hb_Intent = new Intent(getActivity(), K3.class);
                k3_hb_Intent.putExtra(Syxw.CITY, Constants.City.hubei.toString());
                startActivity(k3_hb_Intent);
                break;
            case 4:
                Intent k3_nmg_Intent = new Intent(getActivity(), K3.class);
                k3_nmg_Intent.putExtra(Syxw.CITY, Constants.City.neimenggu.toString());
                startActivity(k3_nmg_Intent);
                break;
            default:
                break;
        }
    }

    private void HandleOtherItemClick(int position)
    {
        switch (position)
        {
            case 0:
                Intent ssc_cq_Intent = new Intent(getActivity(), SSC_CQ.class);
                ssc_cq_Intent.putExtra(Syxw.CITY, Constants.City.chongqing.toString());
                startActivity(ssc_cq_Intent);
                break;
            case 1:
                Intent ssc_jx_Intent = new Intent(getActivity(), SSC_JX.class);
                ssc_jx_Intent.putExtra(Syxw.CITY, Constants.City.jiangxi.toString());
                startActivity(ssc_jx_Intent);
                break;
            default:
                break;
        }
    }
}
