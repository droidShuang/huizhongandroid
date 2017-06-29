package com.junova.huizhong.activity;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.junova.huizhong.R;

import java.util.List;

/**
 * Created by junova on 2016/10/27 0027.
 */

public class TextAdapter extends BaseAdapter {
    List<String> list;

    public TextAdapter(List<String> list) {
        super();
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public String getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(parent.getContext(), R.layout.item_text, null);
        TextView txIndex = (TextView) view.findViewById(R.id.item_tx_index);
        TextView txContent = (TextView) view.findViewById(R.id.item_tx_content);
        txIndex.setText(""+(position+1));
        txContent.setText(list.get(position));
        return view;
    }
}
