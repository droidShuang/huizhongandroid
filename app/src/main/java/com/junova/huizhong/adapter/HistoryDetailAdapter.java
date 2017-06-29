package com.junova.huizhong.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.junova.huizhong.R;
import com.junova.huizhong.model.DetailBean;
import com.orhanobut.logger.Logger;

import java.util.List;

/**
 * Created by rider on 2016/8/8 0008 17:12.
 * Description :
 */
public class HistoryDetailAdapter extends BaseAdapter {
    private Context context;
    private List<DetailBean> detailBeanList;

    public HistoryDetailAdapter(Context context, List<DetailBean> detailBeen) {
        this.context = context;
        this.detailBeanList = detailBeen;
    }

    @Override
    public int getCount() {
        return detailBeanList.size();
    }

    @Override
    public DetailBean getItem(int position) {
        return detailBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_detail, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.onBind(position);
        return convertView;
    }

    private class ViewHolder {
        TextView txDescribe, txName;

        public ViewHolder(View view) {
            txDescribe = (TextView) view.findViewById(R.id.item_tx_describe);
            txName = (TextView) view.findViewById(R.id.item_tx_name);

        }

        public void onBind(int position) {
            Logger.d(getItem(position).getDETDESC());
            txDescribe.setText("问题描述：" + getItem(position).getDETDESC());
            txName.setText("检测项：" + getItem(position).getNAME());
        }
    }
}
