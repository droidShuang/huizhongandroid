package com.junova.huizhong.viewCache;

import android.view.View;
import android.widget.ImageView;

import com.junova.huizhong.R;

public class CheckGridViewCache {
	private View baseView;
	
	private ImageView imageView;
	private ImageView cancelBtn;
	
	public CheckGridViewCache(View baseView) {  
        this.baseView = baseView;  
    }

	public ImageView getImageView() {
		if (imageView == null) {
			imageView = (ImageView) baseView.findViewById(R.id.image);
		}
		return imageView;
	}

	public ImageView getCancelBtn(){
		if (cancelBtn == null) {
			cancelBtn = (ImageView) baseView.findViewById(R.id.cancel);
		}
		return cancelBtn;
	}
	
}
