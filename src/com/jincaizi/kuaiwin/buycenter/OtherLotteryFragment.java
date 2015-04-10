package com.jincaizi.kuaiwin.buycenter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.*;
import android.widget.*;
import com.jincaizi.R;
import com.jincaizi.kuaiwin.buycenter.adapter.OtherLotteryAdapter;

/**
 * Created by chenweida on 2015/3/30.
 */
public class OtherLotteryFragment extends Fragment {

    private static final String[] type ={"双色球", "大乐透","排列3", "排列5",  "福彩3D", "七星彩",
            "七乐彩","竞彩足球","竞彩篮球","足彩胜负","足彩比分", "更多彩种"};
    private static final int[] imageResource = {R.drawable.logo_ssq, R.drawable.logo_dlt, R.drawable.logo_pl3,
            R.drawable.logo_pl5, R.drawable.logo_fc3d, R.drawable.logo_qxc,
            R.drawable.logo_qlc, R.drawable.logo_soccer, R.drawable.logo_bascketball,
            R.drawable.logo_sf, R.drawable.logo_bf, R.drawable.logo_gp_more};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.other_lottery_grid, null);

        GridView gridView = (GridView) layout.findViewById(R.id.other_gridview);
        gridView.setAdapter(new OtherLotteryAdapter(getActivity(), type, imageResource));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(checkApkExist(getActivity(), "com.sd.jincaizi")) {
                    PackageManager packageManager = getActivity().getPackageManager();
                    Intent intent = packageManager.getLaunchIntentForPackage("com.sd.jincaizi");
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), "您尚未安装快赢彩票客户端，请下载安装！", Toast.LENGTH_SHORT).show();
                }
            }
        });

        LinearLayout downloadLayout = (LinearLayout) layout.findViewById(R.id.download_layout);

        downloadLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkApkExist(getActivity(), "com.sd.jincaizi")) {
                    PackageManager packageManager = getActivity().getPackageManager();
                    Intent intent = packageManager.getLaunchIntentForPackage("com.sd.jincaizi");
                    startActivity(intent);
                } else {
                    showDownloadDialog();
                }
            }
        });

        return layout;
    }

    public void showDownloadDialog() {
        final Dialog localDialog = new Dialog(getActivity(),
                R.style.Theme_dialog);
        View view = LayoutInflater.from(getActivity()).inflate(
                R.layout.dialog_submit_bet, null);
        TextView dialogContent = (TextView) view
                .findViewById(R.id.submit_dialog_content);
        dialogContent.setText("您确定要下载快赢网客户端吗？");
        TextView localCancel = (TextView) view
                .findViewById(R.id.tv_submit_cancel);
        TextView localOK = (TextView) view.findViewById(R.id.tv_submit_ok);
        localOK.setText("确定");
        localDialog.setContentView(view);
        Window dialogWindow = localDialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int)(250 * getResources().getDisplayMetrics().density); // 宽度
        dialogWindow.setAttributes(lp);
        localOK.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                localDialog.cancel();
                Uri uri = Uri.parse("http://www.xicai.com/Mobile/xicai.apk");
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        localCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                localDialog.cancel();
            }
        });
        localDialog.show();
    }

    public boolean checkApkExist(Context context, String packageName) {
        if (packageName == null || "".equals(packageName))
            return false;
        try {
            ApplicationInfo info = context.getPackageManager()
                    .getApplicationInfo(packageName,
                            PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}
