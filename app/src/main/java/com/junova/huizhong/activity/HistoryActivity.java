package com.junova.huizhong.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.junova.huizhong.AppConfig;
import com.junova.huizhong.R;
import com.junova.huizhong.adapter.HistoryAdapter;
import com.junova.huizhong.adapter.HistoryDetailAdapter;
import com.junova.huizhong.common.FunctionUtils;
import com.junova.huizhong.common.http.AsyncHttpConnection;
import com.junova.huizhong.common.http.HttpMethod;
import com.junova.huizhong.model.DetailBean;
import com.junova.huizhong.model.HistoryRecord;
import com.junova.huizhong.widget.LoadingDialog;
import com.orhanobut.logger.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HistoryActivity extends Activity {
    private Button btDate;
    private Spinner spStatus;
    private ListView listRecord;
    private DatePickerDialog dialog;
    private Calendar c;
    private String[] errorKind;
    private LoadingDialog loadingDialog;
    private String status = "";
    private String dateString = "";
    private List<HistoryRecord> historys;
    List<DetailBean> detailBeanList = new ArrayList<>();
    private HistoryAdapter adapter;
    private Boolean first;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        first = true;
        init();
    }

    private void init() {
        historys = new ArrayList<>();
        adapter = new HistoryAdapter(this, historys);
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        errorKind = getResources().getStringArray(R.array.errorkind);
        btDate = (Button) this.findViewById(R.id.history_bt_date);
        btDate.setText(dateFormat.format(date));
        spStatus = (Spinner) this.findViewById(R.id.history_sp_status);
        listRecord = (ListView) this.findViewById(R.id.history_list_record);
        listRecord.setAdapter(adapter);
        spStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                status = (position + 1) + "";
                getHistory();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        c = Calendar.getInstance();
        dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {


                String month = "";
                String day = "";
                if (monthOfYear < 9) {
                    month = "0" + (monthOfYear + 1);
                } else {
                    month = (monthOfYear + 1) + "";
                }
                if (dayOfMonth < 10) {
                    day = "0" + dayOfMonth;
                } else {
                    day = dayOfMonth + "";
                }

                dateString = year + "-" + month + "-" + day;
                btDate.setText(dateString);
                getHistory();
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        dialog.getDatePicker().setCalendarViewShown(false);
        listRecord.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                View dialogView = View.inflate(HistoryActivity.this, R.layout.item_dialog_detail, null);
                ListView listView = (ListView) dialogView.findViewById(R.id.item_lv_detail);
                HistoryDetailAdapter historyDetailAdapter = new HistoryDetailAdapter(HistoryActivity.this, adapter.historyRecords.get(position).getDETAIL());
                Logger.d("size  " + adapter.historyRecords.get(position).getDETAIL().size());
                listView.setAdapter(historyDetailAdapter);
                AlertDialog.Builder builder = new AlertDialog.Builder(HistoryActivity.this);
                builder.setView(dialogView);

                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.create().show();
            }
        });

    }

    public void onClick(View view) {
        switch (view.getId()) {
            //选择日期
            case R.id.history_bt_date:
                if (dialog != null) {
                    dialog.show();
                }
                break;
            case R.id.back:
                this.finish();
                break;
        }

    }

    private void getHistory() {
        Map<Object, Object> params = new HashMap<>();
        params.put("USERID", AppConfig.prefs.getString("userId", "0"));
        if (first) {
            params.put("STATUS", "0");
        } else {
            params.put("STATUS", status);
        }

        params.put("DATE", dateString);
        loadingDialog = FunctionUtils.showDialog(this);
        Logger.d(params);
        new AsyncHttpConnection().post(AppConfig.GET_OTHER_DETAIL, HttpMethod.getParams(getApplicationContext(), params), new AsyncHttpConnection.CallbackListener() {
            @Override
            public void callBack(String result) {
                FunctionUtils.closeDialog(loadingDialog);

                if (!TextUtils.isEmpty(result)) {
                    try {
                        FunctionUtils.closeDialog(loadingDialog);
                        Logger.json(result);
                        JSONObject jsonObject = new JSONObject(result);
                        int status = jsonObject.getInt("status");
                        if (status == 01) {
                            jsonHistory(jsonObject);
                        } else {
                            Logger.d(jsonObject);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        first = false;
    }

    private void jsonHistory(JSONObject jsonObject) throws JSONException {
        JSONArray historysJson = jsonObject.getJSONArray("data");
        Logger.d(historysJson);
        historys.clear();
        for (int i = 0; i < historysJson.length(); i++) {
            JSONObject jsonObject1 = historysJson.getJSONObject(i);

            HistoryRecord historyRecord = new HistoryRecord();
            historyRecord.setCREATED(jsonObject1.getString("CREATED"));
            historyRecord.setISSUECODE(jsonObject1.getString("ISSUECODE"));
            historyRecord.setSTATUS(jsonObject1.getString("STATUS"));
            historyRecord.setPATROLRECORD_ID(jsonObject1.getString("PATROLRECORD_ID"));
            List<DetailBean> listDetail = new ArrayList<>();
            JSONArray jsonArray = jsonObject1.getJSONArray("DETAIL");
            for (int j = 0; j < jsonArray.length(); j++) {
                Logger.d("position  " + j + "   " + jsonArray.getJSONObject(j));
                JSONObject detailJson = jsonArray.getJSONObject(j);
                DetailBean detailBean = new DetailBean();
                detailBean.setDETDESC(detailJson.getString("DETDESC"));
                detailBean.setNAME(detailJson.getString("NAME"));
                listDetail.add(detailBean);
            }
            historyRecord.setDETAIL(listDetail);

            historys.add(historyRecord);
        }
        adapter.notifyDataSetChanged();
    }
}
