package com.junova.huizhong.viewCache;

import com.junova.huizhong.R;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MeterViewCache {
	private View baseView;
	private TextView titleTextView;
	private TextView summaryTextView;
	private TextView noTextView;
	private TextView statusTextView;
	private ImageView imgImageView;
	
	public TextView getTitleTextView() {
		if (titleTextView == null) {
			titleTextView = (TextView) baseView.findViewById(R.id.meterTitle);
		}
		return titleTextView;
	}

	public TextView getSummaryTextView() {
		if (summaryTextView == null) {
			summaryTextView = (TextView) baseView.findViewById(R.id.meterSummary);
		}
		return summaryTextView;
	}

	public TextView getNoTextView() {
		if (noTextView == null) {
			noTextView = (TextView) baseView.findViewById(R.id.meterNo);
		}
		return noTextView;
	}

	public TextView getStatusTextView() {
		if (statusTextView == null) {
			statusTextView = (TextView) baseView.findViewById(R.id.meterStatus);
		}
		return statusTextView;
	}

	public ImageView getImgImageView() {
		if (imgImageView == null) {
			imgImageView = (ImageView) baseView.findViewById(R.id.img);
		}
		return imgImageView;
	}

	public MeterViewCache(View baseView) {
		// TODO Auto-generated constructor stub
		this.baseView = baseView;
	}
	
	

}
