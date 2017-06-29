package com.junova.huizhong.adapter;

import java.util.List;

import com.junova.huizhong.R;
import com.junova.huizhong.common.http.AsyncImageLoaderLocal;
import com.junova.huizhong.common.http.AsyncImageLoaderLocal.ImageCallback;
import com.junova.huizhong.model.MeterParam;
import com.junova.huizhong.viewCache.MeterViewCache;
import com.junova.huizhong.widget.PullRefreshListView;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PatrolRecordAdapter extends BaseAdapter {

	Activity activity;
	List<MeterParam> list;
	PullRefreshListView listView;

	private TextView titleTextView;
	private TextView summaryTextView;
	private TextView noTextView;
	private TextView statusTextView;
	private ImageView imgImageView;

	public PatrolRecordAdapter(Activity activity, List<MeterParam> list,
			PullRefreshListView listView) {
		// TODO Auto-generated constructor stub
		this.activity = activity;
		this.list = list;
		this.listView = listView;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int arg0, View covertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		MeterViewCache viewCache = null;
		if (covertView == null) {
			covertView = LayoutInflater.from(activity).inflate(
					R.layout.patrol_item, null);
			viewCache = new MeterViewCache(covertView);
			covertView.setTag(viewCache);
		} else {
			viewCache = (MeterViewCache) covertView.getTag();
		}
		titleTextView = viewCache.getTitleTextView();
		summaryTextView = viewCache.getSummaryTextView();
		noTextView = viewCache.getNoTextView();
		statusTextView = viewCache.getStatusTextView();
		imgImageView = viewCache.getImgImageView();
		MeterParam param = list.get(arg0);
		titleTextView.setText(param.getTitle());
		summaryTextView.setText(param.getDetail());
		noTextView.setText(param.getNo());
		statusTextView.setText("日期：");
		statusTextView.setTextColor(activity.getResources().getColor(
				R.color.yichang));
//		
//		Drawable drawable = activity.getResources().getDrawable(
//				R.drawable.hz_img_ic_yc);
//		// 这一步必须要做,否则不会显示.
//		drawable.setBounds(0, 0, drawable.getMinimumWidth(),
//				drawable.getMinimumHeight());
//		Drawable drawable1 = activity.getResources().getDrawable(
//				R.drawable.hz_img_ic_zc);
//		// 这一步必须要做,否则不会显示.
//		drawable1.setBounds(0, 0, drawable1.getMinimumWidth(),
//				drawable1.getMinimumHeight());
//		statusTextView.setTag(param.getTitle() + param.getDetail());
//		switch (param.getStatus()) {
//
//		case 0:
//			statusTextView.setText("待检查");
//			statusTextView.setTextColor(activity.getResources().getColor(
//					R.color.daijian));
//			statusTextView.setCompoundDrawables(null, null, null, null);
//			break;
//		case 1:
//			statusTextView.setText("抽查正常");
//			statusTextView.setTextColor(activity.getResources().getColor(
//					R.color.zhengchang));
//			statusTextView.setCompoundDrawables(null, null, drawable1, null);
//			break;
//		case 2:
//			statusTextView.setText("抽查异常");
//			statusTextView.setTextColor(activity.getResources().getColor(
//					R.color.yichang));
//			statusTextView.setCompoundDrawables(null, null, drawable, null);
//			break;
//		case 3:
//			statusTextView.setText("已整改");
//			statusTextView.setTextColor(activity.getResources().getColor(
//					R.color.daijian));
//			statusTextView.setCompoundDrawables(null, null, null, null);
//			break;
//
//		default:
//			break;
//		}

		if (param.getLogo() != null || !param.getLogo().equals("")) {
			imgImageView.setTag(param.getLogo());
			Drawable cacheDrawable = AsyncImageLoaderLocal.getInstance()
					.loadDrawable(param.getLogo(), new ImageCallback() {

						@Override
						public void imageLoaded(Drawable imageDrawable,
								String imageUrl) {
							// TODO Auto-generated method stub
							ImageView img = (ImageView) listView
									.findViewWithTag(imageUrl);
							if (imageDrawable != null) {
								img.setImageDrawable(imageDrawable);
							}
						}
					});
			if (cacheDrawable != null) {
				imgImageView.setImageDrawable(cacheDrawable);
			}
		}

		return covertView;
	}

}
