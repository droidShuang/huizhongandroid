package com.junova.huizhong.viewCache;

import com.junova.huizhong.R;

import android.view.View;
import android.widget.TextView;

public class CategoryViewCache {
	
	private TextView categoryTitle;
	
	private View baseView;
	
	public CategoryViewCache(View baseView){
		this.baseView = baseView;
	}

	public TextView getCategoryTitle() {
		if (categoryTitle == null) {
			categoryTitle = (TextView) baseView.findViewById(R.id.categoryTitle);
		}
		return categoryTitle;
	}
	
	
	
	
}
