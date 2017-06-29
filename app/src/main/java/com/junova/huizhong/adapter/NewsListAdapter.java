package com.junova.huizhong.adapter;

import java.util.List;

import com.junova.huizhong.R;
import com.junova.huizhong.model.NewsParam;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class NewsListAdapter extends BaseAdapter {
	Context context;
	List<NewsParam> list;

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	public NewsListAdapter(Context context, List<NewsParam> list) {
		super();
		this.context = context;
		this.list = list;
	}

	public void update(List<NewsParam> list) {
		this.list = list;

		notifyDataSetChanged();
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
		// TODO Auto-generated method stubout
		arg1 = LayoutInflater.from(context).inflate(R.layout.news_list_item,
				null);
		NewsParam param = list.get(arg0);
		TextView title = (TextView) arg1.findViewById(R.id.title);
		TextView content = (TextView) arg1.findViewById(R.id.content);
		TextView date = (TextView) arg1.findViewById(R.id.date);
		title.setText(param.getTitle());
		content.setText(param.getContent());
		date.setText(param.getDate());
		Log.d("YANGSHUANG", "getView: lllllllllllllllllllllllllllll");
		return arg1;
	}

}
