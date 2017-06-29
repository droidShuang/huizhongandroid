package com.junova.huizhong.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.junova.huizhong.R;

public class ChejianAdapter extends BaseAdapter {

	Context con;
	List<String> list;

	public ChejianAdapter(Context con, List<String> list) {
		super();
		this.con = con;
		this.list = list;
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
		arg1 = LayoutInflater.from(con).inflate(R.layout.chejian_item, null);
		TextView tv = (TextView)arg1.findViewById(R.id.chejian_text) ;
		tv.setText(list.get(arg0));
		return arg1;
	}

}
