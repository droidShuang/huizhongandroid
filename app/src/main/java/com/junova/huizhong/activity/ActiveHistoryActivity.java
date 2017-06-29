package com.junova.huizhong.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.junova.huizhong.AppConfig;
import com.junova.huizhong.R;
import com.junova.huizhong.adapter.ActiveHistoryAdapter;
import com.junova.huizhong.common.FunctionUtils;
import com.junova.huizhong.common.Logger;
import com.junova.huizhong.common.http.AsyncHttpConnection;
import com.junova.huizhong.common.http.HttpMethod;
import com.junova.huizhong.model.ActiveHistory;
import com.junova.huizhong.model.NewsParam;
import com.junova.huizhong.widget.LoadingDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActiveHistoryActivity extends Activity {
    private ListView lvActive;
    LoadingDialog dialog;
    private ActiveHistoryAdapter adapter;
    private List<ActiveHistory> histories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_history);
        init();
        getHistory();
    }

    private void init() {
        lvActive = (ListView) this.findViewById(R.id.history_lv_active);
        histories = new ArrayList<>();
        adapter = new ActiveHistoryAdapter(histories);
        lvActive.setAdapter(adapter);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                ActiveHistoryActivity.this.finish();
                break;
            case R.id.history_bt_choose_date:
                break;
        }

    }

    private void getHistory() {
        Map<Object, Object> params = new HashMap<>();

        params.put("ID", AppConfig.prefs.getString("partId", "0"));
        dialog = FunctionUtils.showDialog(this);
        new AsyncHttpConnection().post(AppConfig.GET_ACTIVITY_HISTORY_URL,
                HttpMethod.getParams(getApplicationContext(), params),
                new AsyncHttpConnection.CallbackListener() {

                    @Override
                    public void callBack(String result) {
                        FunctionUtils.closeDialog(dialog);
                        Log.d("YANGSHUANG", "GET_ARTICLE_LIST	" + result);
                        Logger.getInstance().e("result", result);
                        if (result != null && !"fail".equals(result)) {
                            try {
                                JSONObject obj = new JSONObject(result);
                                int status = obj.getInt("status");
                                if (status == 01) {
                                    jsonHistoryList(obj);
                                } else {
                                    String msg = obj.getString("message");
                                    Logger.getInstance().e("login error", msg);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                });
    }

    private void jsonHistoryList(JSONObject obj) {
        try {
            JSONArray articles = obj.getJSONArray("data");
            List<ActiveHistory> list = new ArrayList<ActiveHistory>();
            for (int i = 0; i < articles.length(); i++) {
                JSONObject article = articles.getJSONObject(i);
                com.orhanobut.logger.Logger.d(article);
                String title1 = article.getString("TITLE1");
                String title2 = article.getString("TITLE2");
                String descrbletion = article.getString("DESCRIPTION");
                String time = article.getString("TIME");
                String images = article.getString("IMAGES");

                ActiveHistory param = new ActiveHistory(time, title1, descrbletion, title2, images);

                list.add(param);
            }
            histories.clear();
            histories.addAll(list);
            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
