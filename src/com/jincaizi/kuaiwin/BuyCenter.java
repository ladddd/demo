package com.jincaizi.kuaiwin;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.stream.JsonReader;
import com.jincaizi.R;
import com.jincaizi.http.JinCaiZiHttpClient;
import com.jincaizi.kuaiwin.buycenter.adapter.BuyCenterViewAdapter;
import com.jincaizi.kuaiwin.utils.SafeAsyncTask;
import com.jincaizi.kuaiwin.utils.Utils;
import com.jincaizi.kuaiwin.widget.SlidingTextView;
import com.jincaizi.kuaiwin.widget.UnderlinePageIndicator;
import com.jincaizi.vendor.http.AsyncHttpClient;
import com.jincaizi.vendor.http.AsyncHttpResponseHandler;
import com.jincaizi.vendor.http.RequestParams;
import org.apache.http.Header;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class BuyCenter extends Fragment implements FragmentCallbacks,ViewPager.OnPageChangeListener,
        View.OnClickListener{
	public static final String TAG = "BuyCenter";
	public static String[] itemName = { "十一运夺金","江西11选5" ,"广东11选5","安徽11选5", "重庆11选5", "辽宁11选5", "上海11选5","黑龙江11选5", "江苏快3",  "安徽快3", "内蒙古快3",
			"重庆时时彩",   "江西时时彩","双色球", "大乐透","排列3", "排列5",  "福彩3D", "七星彩",
			"七乐彩","22选5", "浙江11选5", "吉林快3", "湖北快3"};
	public static int[] itemImage = {R.drawable.logo_sd_11x5,
			R.drawable.logo_jx_11x5 , R.drawable.logo_gd_11x5, R.drawable.logo_ah_11x5, R.drawable.logo_cq_11x5,R.drawable.logo_ln_11x5,
			R.drawable.logo_sh_11x5,R.drawable.logo_hlj_11x5, R.drawable.logo_js_k3,  R.drawable.logo_ah_k3,
			R.drawable.logo_nmg_k3, R.drawable.logo_ssc, R.drawable.logo_jx_ssc, R.drawable.logo_ssq, R.drawable.logo_dlt, R.drawable.logo_pl3,
			R.drawable.logo_pl5, R.drawable.logo_fc3d, R.drawable.logo_qxc, R.drawable.logo_qlc, R.drawable.logo_tc22x5,
            R.drawable.logo_zj_11x5, R.drawable.logo_jl_k3, R.drawable.logo_hb_k3};
	private String[]mDescription = {"10分钟一期，返奖率高达59%", "10分钟一期，轻松赢千元","10分钟一期，玩法多，中奖快","10分钟一期，轻松赢大奖",
			"10分钟一期，轻松赢千元","10分钟一期，返奖率高达59%","一天开奖90次，好玩易中过把瘾","10分钟一期，玩法多，中间快", "10分钟一期，好玩易中",
			"骰子摇一摇，轻松好玩易中奖", "一看就会，趣味性高，易中奖","独有夜间版，单注最高赢十万", "一天开奖84次，10分钟赢11.6万",
			"彩民的最爱，每期销量过亿","奖金最丰厚，3元赢1600万","天天开奖，轻松赢千元", "天天开奖，2元赢10万", "简单三位数，轻松赢千元",
			"2元赢取大奖500万", "2元赢取大奖500万","好玩易中,最高奖金500万"};

    private TextView elevenFiveTitle;
    private TextView quickThreeTitle;
    private TextView otherFrequentTitle;
    private TextView otherTypeTitle;

    private ArrayList<String> links = new ArrayList<String>();
    private ArrayList<String> slidingTexts = new ArrayList<String>();

    private ViewPager viewPager;
    private BuyCenterViewAdapter adapter;
    private SlidingTextView slidingTextView;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		_findViews(view);
		super.onViewCreated(view, savedInstanceState);

        requestData();
	}

	private void _findViews(View view) {
        viewPager = (ViewPager) view.findViewById(R.id.pager);
        adapter = new BuyCenterViewAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapter);

        UnderlinePageIndicator indicator = (UnderlinePageIndicator)view.findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);
        indicator.setFades(false);

        slidingTextView = (SlidingTextView) view.findViewById(R.id.sliding_txt);

        indicator.setOnPageChangeListener(this);

        elevenFiveTitle = (TextView) view.findViewById(R.id.eleven_five_title);
        elevenFiveTitle.setSelected(true);
        quickThreeTitle = (TextView) view.findViewById(R.id.quick_three_title);
        otherFrequentTitle = (TextView) view.findViewById(R.id.other_frequent_title);
        otherTypeTitle = (TextView) view.findViewById(R.id.other_type_title);

        elevenFiveTitle.setOnClickListener(this);
        quickThreeTitle.setOnClickListener(this);
        otherFrequentTitle.setOnClickListener(this);
        otherTypeTitle.setOnClickListener(this);
	}

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.eleven_five_title:
                viewPager.setCurrentItem(0);
                elevenFiveTitle.setSelected(true);
                quickThreeTitle.setSelected(false);
                otherFrequentTitle.setSelected(false);
                otherTypeTitle.setSelected(false);
                break;
            case R.id.quick_three_title:
                viewPager.setCurrentItem(1);
                elevenFiveTitle.setSelected(false);
                quickThreeTitle.setSelected(true);
                otherFrequentTitle.setSelected(false);
                otherTypeTitle.setSelected(false);
                break;
            case R.id.other_frequent_title:
                viewPager.setCurrentItem(2);
                elevenFiveTitle.setSelected(false);
                quickThreeTitle.setSelected(false);
                otherFrequentTitle.setSelected(true);
                otherTypeTitle.setSelected(false);
                break;
            case R.id.other_type_title:
                viewPager.setCurrentItem(3);
                elevenFiveTitle.setSelected(false);
                quickThreeTitle.setSelected(false);
                otherFrequentTitle.setSelected(false);
                otherTypeTitle.setSelected(true);
                break;

        }
    }

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.buycenter, container, false);
	}

	@Override
	public void onShow() {
		// TODO Auto-generated method stub
		
	}

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        switch (i)
        {
            case 0:
                elevenFiveTitle.setSelected(true);
                quickThreeTitle.setSelected(false);
                otherFrequentTitle.setSelected(false);
                otherTypeTitle.setSelected(false);
                break;
            case 1:
                elevenFiveTitle.setSelected(false);
                quickThreeTitle.setSelected(true);
                otherFrequentTitle.setSelected(false);
                otherTypeTitle.setSelected(false);
                break;
            case 2:
                elevenFiveTitle.setSelected(false);
                quickThreeTitle.setSelected(false);
                otherFrequentTitle.setSelected(true);
                otherTypeTitle.setSelected(false);
                break;
            case 3:
                elevenFiveTitle.setSelected(false);
                quickThreeTitle.setSelected(false);
                otherFrequentTitle.setSelected(false);
                otherTypeTitle.setSelected(true);
                break;
            default:
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    public void requestData() {
        RequestParams params = new RequestParams();
        params.add("act", "active");
        params.add("datatype", "json");
        params.add("jsoncallback",
                "jsonp" + String.valueOf(System.currentTimeMillis()));
        params.add("_", String.valueOf(System.currentTimeMillis()));
        //TODO 现用的是请求喜彩app公告，等快赢对应接口完成
        String url = AsyncHttpClient.getUrlWithQueryString(false,
                "http://m.jincaizi.com/XicaiMobile.aspx", params);
        JinCaiZiHttpClient.post(getActivity(), url, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String charset;
                    if (Utils.isCmwapNet(getActivity())) {
                        charset = "utf-8";
                    } else {
                        charset = "gb2312";
                    }
                    String jsonData = new String(responseBody, charset);
                    Log.d(TAG, "pls qihao detail = " + jsonData);
                    slidingTexts.clear();
                    links.clear();
                    _readActiveFromJson(jsonData);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    // FIXME reader.close should be here
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  byte[] responseBody, Throwable error) {
                Log.d(TAG, "failure = " + error.toString());
            }
        });
    }
    private void _readActiveFromJson(String jsonData) throws IOException {
        final JsonReader reader = new JsonReader(new StringReader(jsonData));
        SafeAsyncTask<Integer> getAllDaigouTask = new SafeAsyncTask<Integer>() {
            @Override
            public Integer call() throws Exception {
                int returnResult = -2;
                int returnSize = 0;
                reader.beginObject();
                while (reader.hasNext()) {
                    String tagName = reader.nextName();
                    if (tagName.equals("result")) {
                        returnResult = reader.nextInt();
                        Log.d(TAG, "result--->" + returnResult);

                    } else if (tagName.equals("size")) {
                        returnSize = reader.nextInt();
                        Log.d(TAG, "size--->" + returnSize);

                    } else if (tagName.equals("data")) {

                        _jsonReadDataArray(reader);

                    }else {
                        reader.nextString();
                    }
                }
                reader.endObject();
                return returnSize;
            }

            @Override
            protected void onSuccess(Integer t) throws Exception {
                // TODO Auto-generated method stub
                super.onSuccess(t);
                if(t>0) {
                    String[] temp = new String[t];
                    for(int i=0; i<t; i++) {
                        temp[i] = slidingTexts.get(i);
                    }
                    slidingTextView.setShowText(temp);

                    //TODO 点击进入公告界面
                    //slidingTextView.doAnimationOpen();
//                    slidingTextView.setOnClickListener(new OnClickListener() {
//
//                        @Override
//                        public void onClick(View v) {
//                            Intent intent = new Intent(getActivity(), WebSizeAnouncement.class);
//                            intent.putStringArrayListExtra("activename", slidingTexts);
//                            intent.putStringArrayListExtra("link", links);
//                            startActivity(intent);
//                        }
//                    });
//                    ((LotteryIndexFragment)mCurrentFragment).onRefreshComplete();
                }
            }

            @Override
            protected void onThrowable(Throwable t) {
                // TODO Auto-generated method stub
                super.onThrowable(t);
            }

            @Override
            protected void onFinally() {
                // TODO Auto-generated method stub
                super.onFinally();

            }
        };
        getAllDaigouTask.execute();
    }
    private void _jsonReadDataArray(JsonReader reader) throws IOException {
        reader.beginArray();
        while (reader.hasNext()) {
            _jsonReadDataObject(reader);
        }
        reader.endArray();
    }
    private void _jsonReadDataObject(JsonReader reader) throws IOException {
        reader.beginObject();
        while (reader.hasNext()) {
            String tagName = reader.nextName();

            if (tagName.equals("ActiveName")) {
                slidingTexts.add(reader.nextString());
            } else if (tagName.equals("Link")) {
                links.add(reader.nextString());
            } else {
                reader.nextString();
            }

        }
        reader.endObject();
    }
}
