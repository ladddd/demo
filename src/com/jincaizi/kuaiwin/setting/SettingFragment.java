package com.jincaizi.kuaiwin.setting;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jincaizi.kuaiwin.FragmentCallbacks;
import com.jincaizi.kuaiwin.MainActivity;
import com.jincaizi.R;
import com.jincaizi.kuaiwin.widget.JinCaiZiProgressDialog;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;

/**
 * 设置界面
 *
 * @author
 */
public class SettingFragment extends Fragment implements OnClickListener, FragmentCallbacks {
    public static final String TAG = "SettingFragment";
    private LinearLayout mHelpCenter;
    private LinearLayout mPlayIntro;
    private LinearLayout mShare;
    private LinearLayout mCall;
    private LinearLayout mUpdate;
    private LinearLayout mAbout;
    private LinearLayout like;
    private Button mLogout;
    private Dialog myDialog;
    private TextView dialogCancel;
    private TextView dialogOK;
    private JinCaiZiProgressDialog mProgressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tools, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        _findView(view);
        _setOnClickLitener();
    }

    private void _findView(View view) {
        mHelpCenter = (LinearLayout) view.findViewById(R.id.tv_helpcenter);
        mPlayIntro = (LinearLayout) view.findViewById(R.id.tv_playintro);
        mShare = (LinearLayout) view.findViewById(R.id.tv_share);
        mCall = (LinearLayout) view.findViewById(R.id.tv_call);
        mUpdate = (LinearLayout) view.findViewById(R.id.tv_update);
        mAbout = (LinearLayout) view.findViewById(R.id.tv_about);
        like = (LinearLayout) view.findViewById(R.id.like);
        mLogout = (Button) view.findViewById(R.id.btn_logout);
        TextView title = (TextView)view.findViewById(R.id.current_lottery);
        title.setText("设置中心");
        view.findViewById(R.id.sumbit_group_buy).setVisibility(View.GONE);
        view.findViewById(R.id.left_layout).setVisibility(View.GONE);
        //call dialog
        myDialog = new Dialog(getActivity(), R.style.Theme_dialog);
        View dialogView = LayoutInflater.from(getActivity()).inflate(
                R.layout.dialog_submit_bet, null);
        TextView dialogTitle = (TextView) dialogView
                .findViewById(R.id.submit_dialog_title);
        dialogTitle.setText("提示");
        TextView dialogContent = (TextView) dialogView
                .findViewById(R.id.submit_dialog_content);
        dialogContent.setText("拨打4006-999-721客服电话");
        dialogCancel = (TextView) dialogView.findViewById(R.id.tv_submit_cancel);
        dialogOK = (TextView) dialogView.findViewById(R.id.tv_submit_ok);
        dialogOK.setText("确定");
        myDialog.setContentView(dialogView);
        Window dialogWindow = myDialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = 250 * (int)getResources().getDisplayMetrics().density; // 宽度
        dialogWindow.setAttributes(lp);
    }

    private void _setOnClickLitener() {
        mHelpCenter.setOnClickListener(this);
        mPlayIntro.setOnClickListener(this);
        mShare.setOnClickListener(this);
        mCall.setOnClickListener(this);
        mUpdate.setOnClickListener(this);
        mAbout.setOnClickListener(this);
        mLogout.setOnClickListener(this);
        dialogCancel.setOnClickListener(this);
        like.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_about:
                Intent aboutIntent = new Intent(getActivity(), AboutActivity.class);
                startActivity(aboutIntent);
                break;
            case R.id.tv_submit_cancel:
                myDialog.dismiss();
                break;
            case R.id.tv_helpcenter:
                Intent helpIntent = new Intent(getActivity(), HelpCenterActivity.class);
                startActivity(helpIntent);
                break;
            case R.id.tv_playintro:
                Intent wfIntent = new Intent(getActivity(), WanFaIntroActivity.class);
                startActivity(wfIntent);
                break;
            case R.id.tv_share:
                Intent it = new Intent(Intent.ACTION_VIEW);
                it.putExtra("sms_body", "最近使用快赢网彩票手机客户端购买彩票，既方便又快捷，你也试试吧！应用下载地址：http://www.kuaiwin.com/Mobile/kuaiwin.apk");
                it.setType("vnd.android-dir/mms-sms");
                startActivity(it);
                break;
            case R.id.tv_call:
                dialogOK.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.CALL");
                        intent.setData(Uri.parse("tel:4006999721"));//mobile为你要拨打的电话号码，模拟器中为模拟器编号也可
                        startActivity(intent);
                        myDialog.dismiss();
                    }
                });
                myDialog.show();
                break;
            case R.id.tv_update:
                showProgress();
                UmengUpdateAgent.setUpdateOnlyWifi(false);
                UmengUpdateAgent.setUpdateAutoPopup(false);
                UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
                    @Override
                    public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
                        // TODO Auto-generated method stub
                        _hideProgress();
                        switch (updateStatus) {
                            case 0: // has update
                                UmengUpdateAgent.showUpdateDialog(getActivity(), updateInfo);
                                break;
                            case 1: // has no update
                                Toast.makeText(getActivity(), "没有更新", Toast.LENGTH_SHORT)
                                        .show();
                                break;
                            // case 2: // none wifi
//				                Toast.makeText(getActivity(), "没有wifi连接， 只在wifi下更新", Toast.LENGTH_SHORT)
//				                        .show();

                            //   break;
                            case 3: // time out
                                Toast.makeText(getActivity(), "超时", Toast.LENGTH_SHORT)
                                        .show();
                                break;
                            default:
                                Toast.makeText(getActivity(), "更新状态"+ updateStatus, Toast.LENGTH_SHORT)
                                        .show();
                                break;
                        }
                    }
                });
                UmengUpdateAgent.forceUpdate(getActivity());
                break;

            case R.id.btn_logout:
                Log.i(TAG, "[onClick] > logout");
                ((MainActivity) getActivity()).showExitDialog();
                break;

            case R.id.like:
                goEvaluate();
                break;
            default:
                break;
        }
    }


    public void showProgress() {
        mProgressDialog = JinCaiZiProgressDialog.show(getActivity(), "正在检查更新");
    }

    private void _hideProgress() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onShow() {
        // TODO Auto-generated method stub

    }

    private void goEvaluate() {
        try {
            Uri uri = Uri.parse("market://details?id="
                    + getActivity().getApplicationContext().getPackageName());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
			/*Toast.makeText(getActivity(), "不能启动应用市场，请检查手机中是否安装，如应用宝，360手机助手",
					Toast.LENGTH_SHORT).show();*/
            Uri newuri = Uri.parse("http://android.myapp.com/myapp/detail.htm?apkName=com.jincaizi ");
            Intent newintent = new Intent(Intent.ACTION_VIEW, newuri);
            startActivity(newintent);
        }
    }

}
