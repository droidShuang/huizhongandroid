package com.junova.huizhong.adapter;

import java.util.List;

import com.junova.huizhong.R;
import com.junova.huizhong.model.NewsParam;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MainNewsAdapter extends BaseAdapter {
	Context context;
	List<NewsParam> list;

	public MainNewsAdapter(Context context, List<NewsParam> list) {
		super();
		this.context = context;
		this.list = list;
	}

	public void update(List<NewsParam> list) {
		this.list = list;
		notifyDataSetChanged();
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
		arg1 = LayoutInflater.from(context).inflate(R.layout.main_list_item,
				null);
		TextView title = (TextView) arg1.findViewById(R.id.news_title);
		TextView content = (TextView) arg1.findViewById(R.id.news_content);
		TextView date = (TextView) arg1.findViewById(R.id.news_date);
		title.setText(list.get(arg0).getTitle());
		content.setText(list.get(arg0).getContent());
		date.setText(list.get(arg0).getDate());
		return arg1;
	}
}
