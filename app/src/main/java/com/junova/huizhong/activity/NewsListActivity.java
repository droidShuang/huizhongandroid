package com.junova.huizhong.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.junova.huizhong.AppConfig;
import com.junova.huizhong.R;
import com.junova.huizhong.adapter.NewsListAdapter;
import com.junova.huizhong.common.FunctionUtils;
import com.junova.huizhong.common.Logger;
import com.junova.huizhong.common.http.AsyncHttpConnection;
import com.junova.huizhong.common.http.HttpMethod;
import com.junova.huizhong.common.http.AsyncHttpConnection.CallbackListener;
import com.junova.huizhong.model.NewsParam;
import com.junova.huizhong.widget.LoadingDialog;

public class NewsListActivity extends Activity {
    Button back;
    ListView newsList;
    NewsListAdapter adapter;
    int pageIndex = 1;
    List<NewsParam> newsDate;
    LoadingDialog dialog;
    int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        initViews();
    }

    private void initViews() {
        back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                finish();
            }
        });
        newsList = (ListView) findViewById(R.id.list_news);
        adapter = new NewsListAdapter(getApplicationContext(),
                new ArrayList<NewsParam>());
        newsList.setAdapter(adapter);
        newsList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                Intent intent = new Intent(NewsListActivity.this,
                        NewsDetailAcyivity.class);
                intent.putExtra("id", newsDate.get(arg2).getId());
                startActivity(intent);
            }
        });
        getNewsList();
    }

    private void getNewsList() {
        Map<Object, Object> op = new HashMap<Object, Object>();

        op.put("TYPE", type);
        //	op.put("pageIndex", pageIndex);
        dialog = FunctionUtils.showDialog(this);
        new AsyncHttpConnection().post(AppConfig.GET_ARTICLE_LIST,
                HttpMethod.getParams(getApplicationContext(), op),
                new CallbackListener() {

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
                                    jsonNewsList(obj);
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

    private void jsonNewsList(JSONObject obj) {
        try {
            JSONArray articles = obj.getJSONArray("data");
            newsDate = new ArrayList<NewsParam>();
            for (int i = 0; i < articles.length(); i++) {
                JSONObject article = articles.getJSONObject(i);
                String title = article.getString("TITLE");
                String summary = article.getString("SUMMARY");
                String createTime = article.getString("CREATETIME");

                NewsParam param = new NewsParam(title, summary, createTime);
                param.setId(article.getString("ID"));
                newsDate.add(param);
            }
            adapter.update(newsDate);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_allow:
                type = 1;
                break;
            case R.id.bt_event:
                type = 3;
                break;
            case R.id.bt_meet:
                type = 2;
                break;
            case R.id.bt_other:
                type = 4;
                break;
            default:
                break;
        }
        getNewsList();
    }

}
