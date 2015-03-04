package com.jincaizi.kuaiwin.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jincaizi.R;

/**
 * 自定义 ProgressDialog
 * 
 * @author 
 */
public class JinCaiZiProgressDialog extends Dialog {

    private TextView mMessageView;
    private LinearLayout mRootLayout;

    public JinCaiZiProgressDialog(Context context) {
        this(context, true);
    }

    public JinCaiZiProgressDialog(Context context, boolean noTitleBar) {
        super(context, R.style.jczDialog);
        setContentView(R.layout.jcz_progress_dialog);
        // 设置点击对话框之外能消失
        setCanceledOnTouchOutside(false);
        // 设置window属性
        LayoutParams lp = getWindow().getAttributes();
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        lp.alpha = 1.0f;
        lp.dimAmount = 0.5f;
        getWindow().setAttributes(lp);
        if (noTitleBar) {
            mRootLayout = (LinearLayout) findViewById(R.id.progress_dialog_root);
            FrameLayout.LayoutParams rlp = (android.widget.FrameLayout.LayoutParams) mRootLayout.getLayoutParams();
            rlp.setMargins(0, 0, 0, 0);
            mRootLayout.setLayoutParams(rlp);
        }
        mMessageView = (TextView) findViewById(R.id.message);
    }

    public void setMessage(CharSequence message) {
        mMessageView.setText(message);
    }

    /**
     * 显示
     * 
     * @param context
     * @param message
     * @return
     */
    public static JinCaiZiProgressDialog show(Context context, CharSequence message) {
        return show(context, message, true, false);
    }

    /**
     *  显示
     * 
     * @param context
     * @param message
     * @param noTitleBar
     * @return
     */
    public static JinCaiZiProgressDialog show(Context context, CharSequence message, boolean noTitleBar) {
        return show(context, message, true, noTitleBar);
    }

    /**
     * 显示
     * 
     * @param context
     * @param message
     * @param cancelable
     * @param noTitleBar
     * @return
     */
    public static JinCaiZiProgressDialog show(Context context, CharSequence message, boolean cancelable, boolean noTitleBar) {
        JinCaiZiProgressDialog dialog = new JinCaiZiProgressDialog(context, noTitleBar);
        dialog.setMessage(message);
        dialog.setCancelable(cancelable);
        // dialog.setIndeterminate(indeterminate);
        dialog.show();
        return dialog;
    }

}
