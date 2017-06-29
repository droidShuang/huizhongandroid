package com.junova.huizhong.common.http;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by rider on 2016/9/21 0021 14:01.
 * Description :
 */
public class ImageLoader {
    public static void load(Context context, String url, ImageView view) {
        Glide.with(context).load(url).into(view);
    }
}
