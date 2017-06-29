package com.junova.huizhong.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.junova.huizhong.AppConfig;
import com.junova.huizhong.R;
import com.orhanobut.logger.Logger;

import java.util.List;

/**
 * Created by junova on 2016/10/27 0027.
 */

public class ShowPicAdapter extends BaseAdapter {
    private String[] urls;

    public ShowPicAdapter(String[] urls) {
        super();
        this.urls = urls;
    }

    @Override
    public int getCount() {
        return urls.length;
    }

    @Override
    public String getItem(int position) {
        return AppConfig.IMAGEURL + urls[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(parent.getContext(), R.layout.item_show_pics, null);
        ImageView iv = (ImageView) view.findViewById(R.id.item_iv);
        Logger.d(getItem(position));
        Glide.with(parent.getContext()).load(getItem(position)).into(iv);

        return view;
    }
}
