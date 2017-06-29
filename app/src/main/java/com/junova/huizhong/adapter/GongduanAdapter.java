package com.junova.huizhong.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.junova.huizhong.AppConfig;
import com.junova.huizhong.R;
import com.junova.huizhong.widget.MoreGridView;

public class GongduanAdapter extends BaseAdapter {
	Context context;
	List<String> list;
	String date;

	public GongduanAdapter(Context context, List<String> list, String date) {
		super();
		this.context = context;
		this.list = list;
		this.date = date;
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
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		arg1 = LayoutInflater.from(context).inflate(R.layout.gongduan_item,
				null);
		TextView tv = (TextView) arg1.findViewById(R.id.gongduan_text);
		final ImageView img = (ImageView) arg1.findViewById(R.id.gongduan_img);
		tv.setText(list.get(arg0));

		final MoreGridView gv = (MoreGridView) arg1
				.findViewById(R.id.gongduan_grid);
		List<String> list1 = new ArrayList<String>();
		list1.add("班组01");
		list1.add("班组02");
		list1.add("班组03");
		list1.add("班组04");
		list1.add("班组05");
		list1.add("班组06");
		gv.setAdapter(new ChejianAdapter(context, list1));
		gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
//				Intent intent = new Intent(context,
//						AccidentRecordListActivity.class);
//				intent.putExtra("date", date);
//				intent.putExtra("isLeader", true);
//				context.startActivity(intent);

			}
		});

		final int postion = arg0;
		arg1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				int times = AppConfig.prefs.getInt("gongduanpsotion" + postion,
						1);
				switch (times) {
				case 1:
					img.setBackgroundResource(R.drawable.ins_up_arrow);
					gv.setVisibility(View.VISIBLE);
					AppConfig.prefs.edit()
							.putInt("gongduanpsotion" + postion, 2).commit();
					break;
				case 2:
					img.setBackgroundResource(R.drawable.ins_down_arrow);
					gv.setVisibility(View.GONE);
					AppConfig.prefs.edit()
							.putInt("gongduanpsotion" + postion, 1).commit();
					break;
				default:
					break;
				}
			}
		});
		return arg1;
	}
}
