package com.junova.huizhong.common;

import android.widget.Toast;

import com.junova.huizhong.AppConfig;

public class MyToast {

	public static void showToast(String title) {
		Toast.makeText(AppConfig.context, title, Toast.LENGTH_SHORT).show();
	}

}
