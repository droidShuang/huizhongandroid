package com.junova.huizhong.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.junova.huizhong.AppConfig;
import com.junova.huizhong.R;
import com.junova.huizhong.widget.CircleImageView;

public class MyFragment extends Fragment implements OnClickListener {

	private Button spotcheck; // 巡查异常
	// 领导需要隐藏的布局
	private LinearLayout patrol_layout;

	private RelativeLayout patrol_record_Layout; // 巡查记录
	private LinearLayout rectification_layout; // 待整改审项
	// 班组长隐藏布局
	private LinearLayout abarbeitung_layout; // 审核整改

	private LinearLayout spotcheck_layout; // 抽查异常
	private View v;

	private CircleImageView iciImageView;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		v = inflater.inflate(R.layout.fragment_my, container, false);
		initViews(v);
		// Inflate the layout for this fragment
		return v;
	}

	public void initViews(View v) {

		patrol_layout = (LinearLayout) v.findViewById(R.id.patrol_layout);
		patrol_layout.setOnClickListener(this);

		patrol_record_Layout = (RelativeLayout) v
				.findViewById(R.id.patrol_record_Layout);
		abarbeitung_layout = (LinearLayout) v
				.findViewById(R.id.abarbeitung_layout);
		rectification_layout = (LinearLayout) v
				.findViewById(R.id.rectification_layout);

		spotcheck_layout = (LinearLayout) v.findViewById(R.id.spotcheck_layout);
		spotcheck_layout.setOnClickListener(this);

		iciImageView = (CircleImageView) v.findViewById(R.id.fragmenticon);
		iciImageView.setOnClickListener(this);

		if (AppConfig.prefs.getInt("userId", 0) == 1) {
			abarbeitung_layout.setVisibility(View.GONE);
		} else {
			patrol_layout.setVisibility(View.GONE);
			rectification_layout.setVisibility(View.GONE);
			patrol_record_Layout.setVisibility(View.GONE);
		}

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.patrol_layout:
			// Intent intent = new Intent(v.getContext(),
			// PatrolRecordActivity.class);
			// intent.putExtra("patrol", "patrol");
			// startActivity(intent);
			break;

		case R.id.spotcheck_layout:
//			Intent i = new Intent(v.getContext(), PatrolRecordActivity.class);
//			i.putExtra("patrol", "spotcheck");
//			startActivity(i);

			break;
		case R.id.fragmenticon:
			// startActivity(new Intent(v.getContext(), MyDataActivity.class));
			break;
		default:
			break;
		}

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (AppConfig.prefs.getString("icon", null) != null) {
			iciImageView.setImageBitmap(stringtoBitmap(AppConfig.prefs
					.getString("icon", null)));
		}
	}

	public Bitmap stringtoBitmap(String string) {
		// 将字符串转换成Bitmap类型
		Bitmap bitmap = null;
		try {
			byte[] bitmapArray;
			bitmapArray = Base64.decode(string, Base64.DEFAULT);
			bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0,
					bitmapArray.length);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return bitmap;
	}

}
