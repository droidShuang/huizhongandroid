package com.junova.huizhong.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.junova.huizhong.R;
import com.junova.huizhong.model.DeviceParam;
import com.orhanobut.logger.Logger;

public class XunChaAdapter extends BaseAdapter {
    Context context;
    List<DeviceParam> list;

    public XunChaAdapter(Context context, List<DeviceParam> list) {
        super();
        this.context = context;
        this.list = list;
    }

    public void updata(List<DeviceParam> list) {
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
        arg1 = LayoutInflater.from(context).inflate(R.layout.xuncha_item, null);
        TextView title = (TextView) arg1.findViewById(R.id.name);
        TextView id = (TextView) arg1.findViewById(R.id.id);
        TextView summary = (TextView) arg1.findViewById(R.id.summary);
        TextView status = (TextView) arg1.findViewById(R.id.status);
        DeviceParam dp = list.get(arg0);
        title.setText(dp.getName());
        id.setText(dp.getSpecId());
        summary.setText(dp.getSummray());
        int j = dp.getStatus();
        String text = null;
        Drawable top;
        int color = 0;
        switch (j) {
            case 0:
                text = context.getResources().getString(R.string.daijiancha);
                color = context.getResources().getColor(R.color.daijian);
                break;
            case 1:
                text = context.getResources().getString(R.string.xuncha_normal);
                color = context.getResources().getColor(R.color.bottom_sel);
                top = context.getResources().getDrawable(R.drawable.zhangchang);
                top.setBounds(0, 0, top.getMinimumWidth(), top.getMinimumHeight());
                status.setCompoundDrawables(null, top, null, null);
                break;
            case 2:
                text = context.getResources().getString(R.string.patrol);
                color = context.getResources().getColor(R.color.yichang);
                top = context.getResources().getDrawable(R.drawable.gantanhao);
                top.setBounds(0, 0, top.getMinimumWidth(), top.getMinimumHeight());
                status.setCompoundDrawables(null, top, null, null);
                break;
            case 10:
                text = context.getResources().getString(R.string.daichoucha);
                color = context.getResources().getColor(R.color.daijian);
                break;
            case 11:
                text = context.getResources().getString(R.string.choucha_normal);
                color = context.getResources().getColor(R.color.bottom_sel);
                top = context.getResources().getDrawable(R.drawable.zhangchang);
                top.setBounds(0, 0, top.getMinimumWidth(), top.getMinimumHeight());
                status.setCompoundDrawables(null, top, null, null);
                break;
            case 12:
                text = context.getResources().getString(R.string.spotcheck);
                color = context.getResources().getColor(R.color.yichang);
                top = context.getResources().getDrawable(R.drawable.gantanhao);
                top.setBounds(0, 0, top.getMinimumWidth(), top.getMinimumHeight());
                status.setCompoundDrawables(null, top, null, null);
                break;
            default:
                break;
        }
        status.setText(text);
        status.setTextColor(color);

        return arg1;
    }

}
