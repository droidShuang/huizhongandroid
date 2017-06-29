package com.junova.huizhong.viewCache;

import com.junova.huizhong.R;
import com.junova.huizhong.widget.PicGridView;

import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CheckViewCache {

	private View baseView;
	
	private TextView titleText;
	private CheckBox statusCk;
	private EditText ycEdt;
	private LinearLayout ycLayout;
	private PicGridView gridView;

	public CheckViewCache(View basView) {
		// TODO Auto-generated constructor stub
		this.baseView = basView;
	}
	
	public TextView getTitleText() {
		if (titleText == null) {
			titleText = (TextView) baseView.findViewById(R.id.title);
		}
		return titleText;
	}

	public CheckBox getStatusCk() {
		if (statusCk == null) {
			statusCk = (CheckBox) baseView.findViewById(R.id.statusCheckBox);
		}
		return statusCk;
	}

	public EditText getYcEdt() {
		if (ycEdt == null) {
			ycEdt = (EditText) baseView.findViewById(R.id.yichangSummaryEdt);
		}
		return ycEdt;
	}

	public LinearLayout getYcLayout() {
		if (ycLayout == null) {
			ycLayout = (LinearLayout) baseView.findViewById(R.id.yichangLayout);
		}
		return ycLayout;
	}

	public PicGridView getGridView() {
		if (gridView == null) {
			gridView = (PicGridView) baseView.findViewById(R.id.gridView);
		}
		return gridView;
	}

}
