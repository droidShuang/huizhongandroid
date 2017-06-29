package com.junova.huizhong.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.junova.huizhong.R;
import com.junova.huizhong.common.http.AsyncImageLoaderLocal;
import com.junova.huizhong.common.http.AsyncImageLoaderLocal.ImageCallback;
import com.junova.huizhong.model.AccidentParam;

public class AccidentAdapter extends BaseAdapter {
    Context context;
    ArrayList<AccidentParam> list;
    ListView listView;

    public AccidentAdapter(Context context, ArrayList<AccidentParam> list,
                           ListView listView) {
        super();
        this.context = context;
        this.list = list;
        this.listView = listView;
    }

    public void updata(ArrayList<AccidentParam> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public AccidentParam getItem(int arg0) {
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
        if (arg1 == null) {
            arg1 = LayoutInflater.from(context).inflate(R.layout.item_accident,
                    null);
        }
        TextView type = (TextView) arg1.findViewById(R.id.accident_type);
        switch (list.get(arg0).getLevel()) {
            case 3:
                type.setText("事故类型：损失工作日事故");
                type.setTextColor(context.getResources().getColor(
                        R.color.accident_red));
                break;
            case 2:
                type.setText("事故类型：可记录事故");
                type.setTextColor(context.getResources().getColor(
                        R.color.accident_yellow));
                break;
            case 1:
                type.setText("事故类型：险肇事故");
                type.setTextColor(context.getResources().getColor(R.color.accident_blue));
                break;
            case -1:
                type.setText("事故类型：安全无事故");
                type.setTextColor(context.getResources().getColor(
                        R.color.accident_green));
                break;
            default:
                break;
        }
        TextView summray = (TextView) arg1.findViewById(R.id.accident_summray);
        summray.setText(list.get(arg0).getDesc());
        TextView time = (TextView) arg1.findViewById(R.id.text_tims);
        time.setText("发生时间：" + list.get(arg0).getTime());
        ImageView img = (ImageView) arg1.findViewById(R.id.accident_img);
        String s = list.get(arg0).getUrl() + ",";
        String url = list.get(arg0).getUrl();
        if (s.split(",").length > 0) {
            url = s.split(",")[0];
        }
        img.setTag(url);
        AsyncImageLoaderLocal asyncImageLoader = new AsyncImageLoaderLocal();
        Drawable cachedImage = AsyncImageLoaderLocal.getInstance().loadDrawable(url,
                new ImageCallback() {
                    public void imageLoaded(Drawable imageDrawable,
                                            String imageUrl) {
                        ImageView ivImageView = (ImageView) listView
                                .findViewWithTag(imageUrl);
                        if (ivImageView != null && imageDrawable != null) {
                            ivImageView.setImageDrawable(imageDrawable);
                        }
                    }
                });

        if (cachedImage != null) {
            img.setImageDrawable(cachedImage);
        }

        return arg1;
    }

}
