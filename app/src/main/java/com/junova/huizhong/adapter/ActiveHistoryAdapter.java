package com.junova.huizhong.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.junova.huizhong.R;
import com.junova.huizhong.model.ActiveHistory;
import com.junova.huizhong.widget.PicGridView;
import com.orhanobut.logger.Logger;

import java.util.List;

/**
 * Created by rider on 2016/9/21 0021 14:08.
 * Description :
 */
public class ActiveHistoryAdapter extends BaseAdapter {
    List<ActiveHistory> activeHistories;

    public ActiveHistoryAdapter(List<ActiveHistory> activeHistories) {
        super();
        this.activeHistories = activeHistories;
    }

    @Override
    public int getCount() {
        return activeHistories.size();
    }

    @Override
    public ActiveHistory getItem(int position) {
        return activeHistories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HistoryHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), R.layout.item_active_history, null);
            holder = new HistoryHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (HistoryHolder) convertView.getTag();
        }
        holder.onBind(position, getItem(position));
        return convertView;
    }

    private class HistoryHolder {
        TextView txTitle, txDate, txDescription;
        PicGridView gvImage;

        public HistoryHolder(View view) {
            super();
            txTitle = (TextView) view.findViewById(R.id.item_tx_title);
            txDate = (TextView) view.findViewById(R.id.item_tx_date);
            txDescription = (TextView) view.findViewById(R.id.item_tx_describe);
            gvImage = (PicGridView) view.findViewById(R.id.item_photo_grid);
        }

        private void onBind(int position, ActiveHistory activeHistory) {
            String title = activeHistory.getTITLE2();
            txTitle.setText((1 + position) + ". " + title.substring(1));
            txDate.setText(activeHistory.getTIME());
            txDescription.setText("活动描述：" + activeHistory.getDESCRIPTION());
            if (!activeHistory.getIMAGES().isEmpty()) {
                String images[] = activeHistory.getIMAGES().split(":");
                ShowPicAdapter adapter = new ShowPicAdapter(activeHistory.getIMAGES().split(":"));
                gvImage.setAdapter(adapter);
            } else {
                gvImage.setVisibility(View.GONE);
            }


        }
    }
}
