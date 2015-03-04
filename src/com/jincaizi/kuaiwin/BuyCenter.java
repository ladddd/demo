package com.jincaizi.kuaiwin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jincaizi.R;
import com.jincaizi.adapters.BuyCenterAdapter;
import com.jincaizi.kuaiwin.buycenter.K3;
import com.jincaizi.kuaiwin.buycenter.SSC_CQ;
import com.jincaizi.kuaiwin.buycenter.Syxw;
import com.jincaizi.kuaiwin.buycenter.sharexicai.Dlt;
import com.jincaizi.kuaiwin.buycenter.sharexicai.Fcsd;
import com.jincaizi.kuaiwin.buycenter.sharexicai.Pls;
import com.jincaizi.kuaiwin.buycenter.sharexicai.Plw;
import com.jincaizi.kuaiwin.buycenter.sharexicai.Qlc;
import com.jincaizi.kuaiwin.buycenter.sharexicai.Qxc;
import com.jincaizi.kuaiwin.buycenter.sharexicai.Ssq;
import com.jincaizi.kuaiwin.utils.Constants;
import com.jincaizi.kuaiwin.utils.IntentAction;

public class BuyCenter extends Fragment implements FragmentCallbacks{
	public static final String TAG = "BuyCenter";
	public static String[] itemName = { "十一运夺金","江西11选5" ,"广东11选5","安徽11选5", "重庆11选5", "辽宁11选5", "上海11选5","黑龙江11选5", "江苏快3",  "安徽快3", "内蒙古快3",
			"重庆时时彩",   "江西时时彩","双色球", "大乐透","排列3", "排列5",  "福彩3D", "七星彩",
			"七乐彩","22选5"};
	public static int[] itemImage = {R.drawable.logo_sd_11x5,
			R.drawable.logo_jx_11x5 , R.drawable.logo_gd_11x5, R.drawable.logo_ah_11x5, R.drawable.logo_ah_11x5,R.drawable.logo_ah_11x5,
			R.drawable.logo_sh_11x5,R.drawable.logo_ah_11x5, R.drawable.logo_js_k3,  R.drawable.logo_js_k3,
			R.drawable.logo_js_k3, R.drawable.logo_ssc, R.drawable.logo_jx_ssc, R.drawable.logo_ssq, R.drawable.logo_dlt, R.drawable.logo_pl3,
			R.drawable.logo_pl5, R.drawable.logo_fc3d, R.drawable.logo_qxc, R.drawable.logo_qlc, R.drawable.logo_tc22x5};
	private String[]mDescription = {"10分钟一期，返奖率高达59%", "10分钟一期，轻松赢千元","10分钟一期，玩法多，中奖快","10分钟一期，轻松赢大奖",
			"10分钟一期，轻松赢千元","10分钟一期，返奖率高达59%","一天开奖90次，好玩易中过把瘾","10分钟一期，玩法多，中间快", "10分钟一期，好玩易中",
			"骰子摇一摇，轻松好玩易中奖", "一看就会，趣味性高，易中奖","独有夜间版，单注最高赢十万", "一天开奖84次，10分钟赢11.6万",
			"彩民的最爱，每期销量过亿","奖金最丰厚，3元赢1600万","天天开奖，轻松赢千元", "天天开奖，2元赢10万", "简单三位数，轻松赢千元",
			"2元赢取大奖500万", "2元赢取大奖500万","好玩易中,最高奖金500万"};
	
	private ListView mListView;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		_findViews(view);
		super.onViewCreated(view, savedInstanceState);
	}

	private void _findViews(View view) {
		mListView = (ListView) view.findViewById(R.id.buycenter_listView);
		mListView.setAdapter(new BuyCenterAdapter(getActivity()
				.getApplicationContext(), itemName, itemImage,mDescription));
		view.findViewById(R.id.touzhu_leftmenu).setVisibility(View.GONE);
		view.findViewById(R.id.left_divider).setVisibility(View.GONE);
		view.findViewById(R.id.right_divider).setVisibility(View.GONE);
		view.findViewById(R.id.sumbit_group_buy).setVisibility(View.GONE);
		TextView title = (TextView)view.findViewById(R.id.current_lottery);
		title.setText(getActivity().getResources().getString(R.string.navigation_buycenter));
		mListView.setOnItemClickListener(new LocalListViewClick());
	}

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.buycenter, container, false);
	}
	class LocalListViewClick implements OnItemClickListener  {
  
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			switch(arg2) {
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
				//Intent syxw_hlj_Intent = new Intent(getActivity(), Syxw.class);
				//syxw_hlj_Intent.putExtra(Syxw.CITY, Constants.City.heilongjiang.toString());
				//startActivity(syxw_hlj_Intent);
				break;
			case 8:
				Intent k3_js_Intent = new Intent(getActivity(), K3.class);
				k3_js_Intent.putExtra(Syxw.CITY, Constants.City.jiangsu.toString());
				startActivity(k3_js_Intent);
				break;	
			case 9:
				Intent k3_ah_Intent = new Intent(getActivity(), K3.class);
				k3_ah_Intent.putExtra(Syxw.CITY, Constants.City.anhui.toString());
				startActivity(k3_ah_Intent);
				break;
			case 10:
				Intent k3_nmg_Intent = new Intent(getActivity(), K3.class);
				k3_nmg_Intent.putExtra(Syxw.CITY, Constants.City.neimenggu.toString());
				startActivity(k3_nmg_Intent);
				break;
			case 11:
				Intent ssc_cq_Intent = new Intent(getActivity(), SSC_CQ.class);
				ssc_cq_Intent.putExtra(Syxw.CITY, Constants.City.chongqing.toString());
				startActivity(ssc_cq_Intent);
				break;
			case 12:
				//Toast.makeText(getActivity(), "停售", Toast.LENGTH_SHORT).show();
//				Intent ssc_jx_Intent = new Intent(getActivity(), SSC_JX.class);
//				ssc_jx_Intent.putExtra(Syxw.CITY, Constants.City.jiangxi.toString());
//				startActivity(ssc_jx_Intent);
				break;
			case 13:
				Intent mSsqIntent = new Intent(getActivity(), Ssq.class);
				mSsqIntent.setAction(IntentAction.FIRSTPICKBALL);
				startActivity(mSsqIntent);
				break;
			case 14:
				Intent mPlsIntent = new Intent(getActivity(), Dlt.class);
				mPlsIntent.setAction(IntentAction.FIRSTPICKBALL);
				startActivity(mPlsIntent);
				break;
			case 15:
				Intent mPlwIntent = new Intent(getActivity(), Pls.class);
				mPlwIntent.setAction(IntentAction.FIRSTPICKBALL);
				startActivity(mPlwIntent);
				break;
			case 16:
				Intent mDltIntent = new Intent(getActivity(), Plw.class);
				mDltIntent.setAction(IntentAction.FIRSTPICKBALL);
				startActivity(mDltIntent);
				break;
			case 17:
				Intent mFcsdIntent = new Intent(getActivity(), Fcsd.class);
				mFcsdIntent.setAction(IntentAction.FIRSTPICKBALL);
				startActivity(mFcsdIntent);
				break;
			case 18:
				Intent mQxcIntent = new Intent(getActivity(), Qxc.class);
				mQxcIntent.setAction(IntentAction.FIRSTPICKBALL);
				startActivity(mQxcIntent);
				break;
			case 19:
				Intent mQlcIntent = new Intent(getActivity(), Qlc.class);
				mQlcIntent.setAction(IntentAction.FIRSTPICKBALL);
				startActivity(mQlcIntent);
				break;
			} 
			
				
			
		}
		
	}
	@Override
	public void onShow() {
		// TODO Auto-generated method stub
		
	}
    

}
