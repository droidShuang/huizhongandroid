/**   
* @{#} AppBuilderAlertDialog.java Create on 2014-1-11 PM 5:36:16   
* @author tuxiaohui
* Copyright (c) 2013 by loongjoy.
* Effect: The Default Alert Dialog
*/
package com.junova.huizhong.widget;

import com.junova.huizhong.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View.OnClickListener;

/**
 * @{#} AppBuilderAlertDialog.java Create on 2014-1-11 下午5:36:16
 * @author xiaohui_tu
 * Effect: The Default Alert Dialog
 */
public class AppDialog extends Dialog implements OnClickListener {
	private LinearLayout alertLayout;
	private TextView alertTitleView;
	private TextView alertContentView;
	private EditText alertContentEdit;
	private Button positiveButton;	//confirm button
	private Button negativeButton;	//cancel button
	private AppAlertDialogListener listener;	//the buttons click listener
	private String alertTitleStr;
	private String alertContentStr;
	private String positiveButtonStr;
	private String negativeButtonStr;
	private Context context;
	private boolean canInputText;
	
	/**
	 * @param context
	 * @param alertTitleStr
	 * @param alertContentStr
	 * @param positiveButtonStr
	 * @param negativeButtonStr
	 * @param theme
	 * @param listener
	 */
	public AppDialog(Context context, boolean canInputText, String alertTitleStr, String alertContentStr, 
			String positiveButtonStr, String negativeButtonStr, int theme, AppAlertDialogListener listener) {
		super(context, theme);
		this.context = context;
		this.canInputText = canInputText;
		this.listener = listener;
		this.alertTitleStr = alertTitleStr;
		this.alertContentStr = alertContentStr;
		this.positiveButtonStr = positiveButtonStr;
        this.negativeButtonStr = negativeButtonStr;
	}
	
	@Override   
    protected void onCreate(Bundle savedInstanceState) {   
        super.onCreate(savedInstanceState);   
        setContentView(R.layout.common_dialog);   
        initViewIds();
    }
	
	private void initViewIds(){
		//click other place for missing dialog
		this.setCanceledOnTouchOutside(true);
		alertLayout = (LinearLayout)findViewById(R.id.alertLayout);
		alertTitleView = (TextView)findViewById(R.id.alertTitleView);
		alertContentView = (TextView)findViewById(R.id.alertContentView);
		alertContentEdit = (EditText) findViewById(R.id.editText1);
		positiveButton = (Button)findViewById(R.id.positiveButton);   
		negativeButton = (Button)findViewById(R.id.negativeButton);
		negativeButton.setVisibility(View.VISIBLE);
		
		if(alertTitleStr != null){
			alertTitleView.setText(alertTitleStr);
		}
		
		if(canInputText) {
			alertContentView.setVisibility(View.GONE);
		}else {
			alertContentEdit.setVisibility(View.GONE);
		}
		if(alertContentStr != null){
			alertContentView.setText(alertContentStr);
		}
		
		if(positiveButtonStr != null){
			positiveButton.setText(positiveButtonStr);
		}
		
		if(negativeButtonStr != null){
			negativeButton.setText(negativeButtonStr);
		}
		
		positiveButton.setOnClickListener(this);   
		negativeButton.setOnClickListener(this);
	}

	//Dialog listener interface
	public interface AppAlertDialogListener{   
		void onClick(View view, String str);
	}
	
	public void onClick(View v) {
		String text = alertContentEdit.getText().toString();
		listener.onClick(v,text);
		if (!text.equals("")) {
			this.dismiss();
		}else if(v.getId() == R.id.negativeButton){
			this.dismiss();
		}
	}
}
