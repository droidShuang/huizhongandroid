package com.junova.huizhong.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.junova.huizhong.R;
import com.junova.huizhong.model.HistoryRecord;

import java.util.List;

/**
 * Created by rider on 2016/8/8 0008 17:01.
 * Description :
 */
public class HistoryAdapter extends BaseAdapter {
    private Context context;
    public List<HistoryRecord> historyRecords;

    public HistoryAdapter(Context context, List<HistoryRecord> historyRecords) {
        this.context = context;
        this.historyRecords = historyRecords;

    }

    public void addData(List<HistoryRecord> historyRecords) {
        this.historyRecords.clear();
        this.historyRecords.addAll(historyRecords);
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return historyRecords.size();
    }

    @Override
    public HistoryRecord getItem(int position) {
        return historyRecords.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HistoryHolder historyHolder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_history, null);
            historyHolder = new HistoryHolder(convertView);

            convertView.setTag(historyHolder);
        } else {
            historyHolder = (HistoryHolder) convertView.getTag();

        }
        historyHolder.onBind(position);
        return convertView;
    }

    class HistoryHolder {
        TextView txStatus, txNumber, txDescrible, txError, txDate;

        public HistoryHolder(View view) {
            txStatus = (TextView) view.findViewById(R.id.item_tx_status);
            txNumber = (TextView) view.findViewById(R.id.item_tx_batchnumber);
            txDescrible = (TextView) view.findViewById(R.id.item_tx_describle);
            txError = (TextView) view.findViewById(R.id.item_tx_error);
            txDate = (TextView) view.findViewById(R.id.item_tx_date);

        }

        public void onBind(int position) {
            HistoryRecord historyRecord = historyRecords.get(position);

            txStatus.setTextColor(Color.RED);
            if (historyRecord.getSTATUS().equals("04")) {
                txStatus.setTextColor(Color.GREEN);
            }
            int sta = Integer.parseInt(historyRecord.getSTATUS());
            switch (sta) {
                case 1:
                    txStatus.setText("未处理");
                    break;
                case 2:
                    txStatus.setText("已上报");
                    break;
                case 3:
                    txStatus.setText("已处理");
                    break;
                case 4:
                    txStatus.setText("已关闭");
                    break;
            }
            txNumber.setText("批次：" + historyRecord.getISSUECODE());
            txDate.setText("日期：" + historyRecord.getCREATED());
        }
    }
}
