/**
 * @Title: LoginOutDialog.java
 * @Package com.loongjoy.huizhong.widget
 * @Description: TODO
 * Copyright: Copyright 2015 loongjoy.inc
 * Company:上海龙照电子有限公司
 * 
 * @author hao_mo@loongjoy.com
 * @date 2015-9-17 下午2:56:55
 * @version V1.0
 */

package com.junova.huizhong.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.junova.huizhong.AppConfig;
import com.junova.huizhong.R;
import com.junova.huizhong.activity.LoginActivity;
import com.junova.huizhong.db.CheckDataBase;
import com.junova.huizhong.db.DeviceDataBase;

/**
 * @ClassName: LoginOutDialog
 * @Description: TODO
 * @author hao_mo@loongjoy.com
 * @date 2015-9-17 下午2:56:55
 * 
 */

public class LoginOutDialog extends Dialog {
	Context context;

	/**
	 * 
	 * 创建一个新的实例 LoginOutDialog.
	 * <p>
	 * Title:
	 * </p>
	 * <p>
	 * Description:
	 * </p>
	 * 
	 * @param context
	 */

	public LoginOutDialog(Context context) {
		super(context);
		this.context = context;

	}

	/*
	 * <p>Title: onCreate</p> <p>Description: </p>
	 * 
	 * @param savedInstanceState
	 * 
	 * @see android.app.Dialog#onCreate(android.os.Bundle)
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_longin_out);
		Button cancell = (Button) findViewById(R.id.cancell);
		Button sure = (Button) findViewById(R.id.sure);
		cancell.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dismiss();
			}
		});
		sure.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				AppConfig.uploadState = 0;
				CheckDataBase dataBase = new CheckDataBase(context);
				dataBase.clear();
				DeviceDataBase deviceDataBase = new DeviceDataBase(context);
				deviceDataBase.clear();
				AppConfig.prefs.edit().clear().commit();
				Intent intent = new Intent(context, LoginActivity.class);
				context.startActivity(intent);
				((Activity) context).finish();
			}
		});
	}
}
