/*
 * common method for views 
 * author : xiaohui_tu@loongjoy.com
 */

package com.junova.huizhong.common;

import android.view.View;
import android.view.animation.TranslateAnimation;

public class ViewUtils {
	/*
	 * tab move method 
	 * @param v : the current move view
	 * @param startX : start X location
	 * @param startY : start Y location
	 * @param toX : end X location
	 * @param toY : end Y location
	 */
	public static void tabMoveAnim(View v, int startX, int toX, int startY, int toY) {
		TranslateAnimation anim = new TranslateAnimation(startX, toX, startY, toY);
		anim.setDuration(200);
		anim.setFillAfter(true);
		v.startAnimation(anim);
	}
}
