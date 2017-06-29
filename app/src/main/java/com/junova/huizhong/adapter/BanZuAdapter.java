package com.junova.huizhong.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.junova.huizhong.R;
import com.junova.huizhong.model.PartParam;

public class BanZuAdapter extends BaseAdapter {
	Context context;
	List<PartParam> date;
	int layout;
	private int position;

	public BanZuAdapter(Context context, List<PartParam> date, int layout) {
		super();
		this.context = context;
		this.date = date;
		this.layout = layout;
	}

	public void updata(List<PartParam> date) {
		this.date = date;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return date.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return date.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		arg1 = LayoutInflater.from(context).inflate(layout, null);
		TextView name = (TextView) arg1.findViewById(R.id.text);
		name.setText(date.get(arg0).getName());
		arg1.setBackgroundColor(context.getResources().getColor(R.color.banzu_blue));
		if(position == arg0){
			arg1.setBackgroundColor(context.getResources().getColor(R.color.white));
			name.setTextColor(context.getResources().getColor(R.color.xuncha_name));
		}
		return arg1;
	}

	/**
	  * 设置点击颜色
	  */
	public void setcolor(int arg2) {
		position = arg2;
		notifyDataSetChanged();
	}

}
