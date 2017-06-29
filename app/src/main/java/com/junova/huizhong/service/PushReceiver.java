package com.junova.huizhong.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * @ClassName: PushReceiver
 * @Description: TODO
 * @author longfei_deng@loongjoy.com
 * @date 2015年9月15日 上午10:18:46
 * 
 */

public class PushReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
		} else if (Intent.ACTION_PACKAGE_ADDED.equals(intent.getAction())) {
			System.out.println("新安装了应用程序....pakageAdded!");
		} else if (Intent.ACTION_PACKAGE_REMOVED.equals(intent.getAction())) {
			System.out.println("应用程序被卸载了....pakageRemoved!");
		} else if (Intent.ACTION_USER_PRESENT.equals(intent.getAction())) {
			System.out.println("手机被唤醒了.....userPresent");
			Intent service = new Intent();
			service.setAction("com.loongjoy.huizhong.service.PushService");
			service.setClass(context, SendService.class);
			service.putExtra(SendService.START_TAG, SendService.PIC_TAG);
			context.startService(service);
		}

	}

}
