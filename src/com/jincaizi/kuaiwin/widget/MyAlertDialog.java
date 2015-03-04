package com.jincaizi.kuaiwin.widget;

import android.app.AlertDialog;
import android.content.Context;

public class MyAlertDialog extends AlertDialog{

	protected MyAlertDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public MyAlertDialog(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setButton(int whichButton, CharSequence text,
			OnClickListener listener) {
		// TODO Auto-generated method stub
		super.setButton(whichButton, text, listener);
	}

	@Override
	public void setTitle(CharSequence title) {
		// TODO Auto-generated method stub
		super.setTitle(title);
	}


}
