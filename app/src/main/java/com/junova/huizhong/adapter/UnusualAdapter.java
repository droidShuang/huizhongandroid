package com.junova.huizhong.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.junova.huizhong.R;
import com.junova.huizhong.model.ExceptionParam;

public class UnusualAdapter extends BaseAdapter {

	List<ExceptionParam> list;
	Context context;

	public UnusualAdapter(List<ExceptionParam> list, Context context) {
		// TODO Auto-generated constructor stub
		this.list = list;
		this.context = context;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int arg0, View covertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		if (covertView == null) {
			covertView = LayoutInflater.from(context).inflate(
					R.layout.yc_type_item, null);
		}
		TextView name = (TextView) covertView.findViewById(R.id.text);
		name.setText(list.get(arg0).getName());
		return covertView;
	}

}
