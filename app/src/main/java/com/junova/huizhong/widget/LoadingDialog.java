/**
 * @Title: loading_dialog.java
 * @Package com.loongjoy.huizhong.widget
 * @Description: TODO
 * Copyright: Copyright 2015 loongjoy.inc
 * Company:上海龙照电子有限公司
 * 
 * @author deng_long_fei(longfei_deng@loongjoy.com) 
* @date 2015年9月23日 上午10:51:26
 * @version V1.0
 */

package com.junova.huizhong.widget;

import com.junova.huizhong.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

/**
 * @ClassName: loading_dialog
 * @Description: TODO
 * @author longfei_deng@loongjoy.com
 * @date 2015年9月23日 上午10:51:26
 *
 */

public class LoadingDialog extends Dialog{

	/**
	
	  * 创建一个新的实例 loading_dialog. 
	  * <p>Title: </p>
	  * <p>Description: </p>
	  * @param context
	  * @param theme
	  */
	
	public LoadingDialog(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.loading_dialog);
		super.onCreate(savedInstanceState);
		setCanceledOnTouchOutside(false);
	}


}
