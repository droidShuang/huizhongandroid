/**
 * @Title: AcyivityFindPassword.java
 * @Package com.loongjoy.huizhong.activity
 * @Description: TODO
 * Copyright: Copyright 2015 loongjoy.inc
 * Company:上海龙照电子有限公司
 * 
 * @author hao_mo@loongjoy.com
 * @date 2015-9-1 下午3:39:03
 * @version V1.0
 */

package com.junova.huizhong.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.junova.huizhong.AppConfig;
import com.junova.huizhong.R;
import com.junova.huizhong.common.Logger;
import com.junova.huizhong.common.http.AsyncHttpConnection;
import com.junova.huizhong.common.http.AsyncHttpConnection.CallbackListener;
import com.junova.huizhong.common.http.HttpMethod;

/**
 * @ClassName: AcyivityFindPassword
 * @Description: TODO 找回密码
 * @author hao_mo@loongjoy.com
 * @date 2015-9-1 下午3:39:03
 * 
 */

public class FindPasswordActivity extends Activity implements OnClickListener {

	EditText numberEdt;
	EditText codeEdt;
	EditText newPwdEdt;
	Button getCode;
	Button sumbit;
	Button back;
	String code;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_find_psd);
		initView();
	}

	private void initView() {
		numberEdt = (EditText) findViewById(R.id.number);
		codeEdt = (EditText) findViewById(R.id.code);
		newPwdEdt = (EditText) findViewById(R.id.new_psd);
		getCode = (Button) findViewById(R.id.get_code);
		sumbit = (Button) findViewById(R.id.btn_sure);
		back = (Button) findViewById(R.id.back);
		sumbit.setOnClickListener(this);
		back.setOnClickListener(this);
		getCode.setOnClickListener(this);
	}

	/*
	 * <p>Title: onClick</p> <p>Description: </p>
	 * 
	 * @param arg0
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */

	@Override
	public void onClick(View arg0) {
		String phone;
		switch (arg0.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.get_code:
			phone = numberEdt.getText().toString().trim();
			if (phone.length() == 11) {
				sendSmsCode(phone);
			}
			break;
		case R.id.btn_sure:
			phone = numberEdt.getText().toString().trim();
			String pwd = newPwdEdt.getText().toString().trim();
			if (phone.length() != 11) {
				break;
			}
			if (!code.equals(codeEdt.getText().toString().trim())) {
				break;
			}
			if ("".equals(pwd)) {
				break;
			}
			resetPwd(phone, pwd);
			break;
		default:
			break;
		}

	}

	private void sendSmsCode(String phone) {
		Map<Object, Object> op = new HashMap<Object, Object>();
		op.put("phone", phone);
		op.put("type", 0);
		new AsyncHttpConnection().post(AppConfig.SEND_SMS_CODE,
				HttpMethod.getParams(getApplicationContext(), op),
				new CallbackListener() {

					@Override
					public void callBack(String result) {
						if (result != null) {
							try {
								JSONObject obj = new JSONObject(result);
								int status = obj.getInt("status");
								if (status == 0) {

								} else {
									String msg = obj.getString("message");
									Logger.getInstance().e("login error", msg);
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					}
				});
	}

	private void resetPwd(String phone, String pwd) {
		Map<Object, Object> op = new HashMap<Object, Object>();
		op.put("phone", phone);
		op.put("smsCode", code);
		op.put("newPwd", pwd);
		new AsyncHttpConnection().post(AppConfig.RESET_PWD,
				HttpMethod.getParams(getApplicationContext(), op),
				new CallbackListener() {

					@Override
					public void callBack(String result) {
						if (result != null) {
							try {
								JSONObject obj = new JSONObject(result);
								int status = obj.getInt("status");
								if (status == 0) {

								} else {
									String msg = obj.getString("message");
									Logger.getInstance().e("login error", msg);
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					}
				});
	}

}
