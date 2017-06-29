package com.junova.huizhong.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.junova.huizhong.R;
import com.junova.huizhong.model.ActiveParam;

import org.w3c.dom.Text;

public class ActiveAdapter extends BaseAdapter {
	List<ActiveParam> list;
	Context context;

	public ActiveAdapter(List<ActiveParam> list, Context context) {
		super();
		this.list = list;
		this.context = context;
	}

	public void updata(List<ActiveParam> list) {
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
		arg1 = LayoutInflater.from(context).inflate(R.layout.item_active, null);
		ImageView img = (ImageView) arg1.findViewById(R.id.img_month);
		ActiveParam ap = list.get(arg0);
		TextView title = (TextView) arg1.findViewById(R.id.text_title);
		TextView content = (TextView) arg1.findViewById(R.id.text_content);
		TextView count = (TextView) arg1.findViewById(R.id.text_count);
		TextView monthCount= (TextView) arg1.findViewById(R.id.text_monthcount);
		TextView finshedCount= (TextView) arg1.findViewById(R.id.text_finshcount);
		TextView date = (TextView) arg1.findViewById(R.id.text_date);
		if (ap.getType() == 1) {
			img.setVisibility(View.VISIBLE);
		//	count.setText("本月活动次数："+ap.getMonthTimes()+" 已完成："+ap.getCompleteTimes());
			count.setText("本月活动次数：");
			monthCount.setText(ap.getMonthTimes()+"");
			finshedCount.setText(ap.getCompleteTimes()+"");
			String year = ap.getDay().split("-")[0];
			String month = ap.getDay().split("-")[1];
			date.setText("活动月份："+year+"年"+month+"月");
		} else {
			img.setVisibility(View.GONE);

			count.setText("当日活动次数：");
			monthCount.setText(ap.getMonthTimes()+"");
			finshedCount.setText(ap.getCompleteTimes()+"");
			String year = ap.getDay().split("-")[0];
			String month = ap.getDay().split("-")[1];
		//	String day = ap.getDay().split("-")[2];
		//	date.setText("活动日期："+year+"年"+month+"月"+day+"日");
			date.setText("活动日期："+year+"年"+month+"月");
		}

		title.setText(ap.getTitle());
		content.setText(ap.getSummary());
		return arg1;
	}

}
