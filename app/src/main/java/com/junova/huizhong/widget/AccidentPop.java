package com.junova.huizhong.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.junova.huizhong.R;
import com.junova.huizhong.common.http.ImageLoader;

/**
 * Created by junova on 2017-02-09.
 */

public class AccidentPop extends PopupWindow {
    TextView txTitle;
    TextView txContent;
    ImageView ivAccident;
    Context context;

    public AccidentPop(Context context) {
        super(context);
        this.context = context;
        View contentView = LayoutInflater.from(context).inflate(R.layout.pop_accident, null);
        setContentView(contentView);
        txTitle = (TextView) contentView.findViewById(R.id.tx_type);
        txContent = (TextView) contentView.findViewById(R.id.tx_content);
        ivAccident = (ImageView) contentView.findViewById(R.id.iv_accident);
        setBackgroundDrawable(new ColorDrawable(Color.WHITE));
    }

    public void showView(int level, String content, String imageUrl) {
        ImageLoader.load(context, imageUrl, ivAccident);
       // txTitle.setText(title);
        switch (level) {
            case 3:
                txTitle.setText("事故类型：损失工作日事故");
                txTitle.setTextColor(context.getResources().getColor(
                        R.color.accident_red));
                break;
            case 2:
                txTitle.setText("事故类型：可记录事故");
                txTitle.setTextColor(context.getResources().getColor(
                        R.color.accident_yellow));
                break;
            case 1:
                txTitle.setText("事故类型：险肇事故");
                txTitle.setTextColor(context.getResources().getColor(R.color.accident_blue));
                break;
            case -1:
                txTitle.setText("事故类型：安全无事故");
                txTitle.setTextColor(context.getResources().getColor(
                        R.color.accident_green));
                break;
            default:
                break;
        }
        txContent.setText(content);
    }
}
