package com.junova.huizhong.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.junova.huizhong.R;
import com.junova.huizhong.activity.TextAdapter;

import java.util.List;

/**
 * Created by junova on 2016/10/27 0027.
 */

public class TextPop extends PopupWindow {
    TextPop pop;
    int chosePosition;

    public TextPop(Context context, List<String> texts) {
        super(context);
        pop = this;
        View convertView = View.inflate(context, R.layout.pop_text, null);
        setContentView(convertView);
        setBackgroundDrawable(new ColorDrawable(0x00000000));
        setOutsideTouchable(true);
        setFocusable(true);
        setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        ListView textListView = (ListView) convertView.findViewById(R.id.pop_lv_text);
        TextAdapter textAdapter = new TextAdapter(texts);
        textListView.setAdapter(textAdapter);
        textListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                chosePosition = position;
                pop.dismiss();
            }
        });

    }

    public int getChosePosition() {
        return chosePosition;
    }
}
