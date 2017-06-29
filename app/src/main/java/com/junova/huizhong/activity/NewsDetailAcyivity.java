package com.junova.huizhong.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.junova.huizhong.AppConfig;
import com.junova.huizhong.R;
import com.junova.huizhong.common.FunctionUtils;
import com.junova.huizhong.common.Logger;

import com.junova.huizhong.common.http.AsyncHttpConnection;
import com.junova.huizhong.common.http.AsyncHttpConnection.CallbackListener;
import com.junova.huizhong.common.http.HttpMethod;
import com.junova.huizhong.widget.LoadingDialog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import okhttp3.Call;

public class NewsDetailAcyivity extends Activity implements OnClickListener {
    Button back;
    Button downloadBtn;
    TextView titleTxt;
    TextView date;
    WebView webView;
    String downloadUrl = "";
    String newsId;
    String title;
    LoadingDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acyivity_news_detail);
        initViews();
        getNewsDetail(getIntent().getStringExtra("id"));

    }

    private void initViews() {
        back = (Button) findViewById(R.id.back);
        downloadBtn = (Button) findViewById(R.id.downLoad);
        back.setOnClickListener(this);
        downloadBtn.setOnClickListener(this);
        titleTxt = (TextView) findViewById(R.id.news_title);
        date = (TextView) findViewById(R.id.news_date);
        webView = (WebView) findViewById(R.id.news_content);
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.downLoad:
//                if (downloadUrl == null || "".equals(downloadUrl)) {
//                    Toast.makeText(getApplicationContext(),
//                            getResources().getString(R.string.invailale_address),
//                            Toast.LENGTH_SHORT).show();
//                    return;
//                }
                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("下载中");
                progressDialog.setIndeterminate(false);
                progressDialog.setCancelable(false);
                progressDialog.show();
                com.orhanobut.logger.Logger.d(AppConfig.ARTICALDOWNLOAD + downloadUrl);
                OkHttpUtils.get().url(AppConfig.ARTICALDOWNLOAD + downloadUrl)
                        .build()
                        .execute(new FileCallBack(Environment.getExternalStorageDirectory() + File.separator + "pdf", title + "." + downloadUrl.split("\\.")[1]) {
                            @Override
                            public void inProgress(float progress, long total) {
                                com.orhanobut.logger.Logger.d("progress " + progress);
                                progressDialog.setProgress((int) progress);
                            }

                            @Override
                            public void onError(Call call, Exception e) {
                                progressDialog.setCancelable(true);
                                com.orhanobut.logger.Logger.d("exception " + e.getMessage());
                                progressDialog.cancel();
                            }

                            @Override
                            public void onResponse(File response) {
                                try {
                                    progressDialog.setCancelable(true);
                                    com.orhanobut.logger.Logger.d(" download complted");
                                    progressDialog.cancel();
                                    Intent intent = new Intent("android.intent.action.VIEW");
                                    intent.addCategory("android.intent.category.DEFAULT");
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    Uri uri = Uri.fromFile(response);
                                    String type = downloadUrl.split("\\.")[1];
                                    if (type.contains("doc")) {
                                        intent.setDataAndType(uri, "application/msword");
                                    } else if (type.contains("pdf")) {
                                        intent.setDataAndType(uri, "application/pdf");
                                    }

                                    startActivity(intent);
                                } catch (Exception e) {
                                    Toast.makeText(NewsDetailAcyivity.this, "请先安装相关应用，再进行查看", Toast.LENGTH_SHORT).show();
                                }


                            }
                        });
//                UpdateManager updateManager = new UpdateManager(NewsDetailAcyivity.this, downloadUrl.split("/")[downloadUrl.split("/").length - 1], downloadUrl, "是否下载附件？");
//                updateManager.startUpdate();
//			downloadArctil();
                break;
            default:
                break;
        }
    }

    private void getNewsDetail(String id) {
        Map<Object, Object> op = new HashMap<Object, Object>();
        op.put("ID", id);
        dialog = FunctionUtils.showDialog(this);
        new AsyncHttpConnection().post(AppConfig.GET_ARTICLE_DETAIL,
                HttpMethod.getParams(getApplicationContext(), op),
                new CallbackListener() {

                    @Override
                    public void callBack(String result) {
                        FunctionUtils.closeDialog(dialog);
                        Logger.getInstance().e("result", result);
                        Log.d("YANGSHUANG", "GET_ARTICLE_DETAIL	" + result);
                        if (result != null) {
                            try {
                                JSONObject obj = new JSONObject(result);
                                int status = obj.getInt("status");
                                if (status == 01) {
                                    jsonNewsDetail(obj);
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

    private void jsonNewsDetail(JSONObject obj) {
        try {
            com.orhanobut.logger.Logger.json(obj.toString());
            JSONObject data = obj.getJSONObject("data");
            newsId = data.getString("ID");
            title = data.getString("TITLE");
            titleTxt.setText(title);
            date.setText(data.getString("CREATETIME"));
            //  String url = data.getString("FILE");
            //      setDownUrl(url);
            downloadUrl = data.getString("ARTFILE");
            if (downloadUrl.equals("")) {
                downloadBtn.setVisibility(View.GONE);
            }
            String summary = data.getString("SUMMARY");
            String content = data.getString("CONTENT");
            loadHtml(content);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private void setDownUrl(String url) {
        downloadUrl = url;
        downloadBtn.setOnClickListener(this);
    }


    private void downloadArctil() {
        if (downloadUrl == null || "".equals(downloadUrl)) {
            Toast.makeText(getApplicationContext(),
                    getResources().getString(R.string.invailale_address),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread() {
            public void run() {
                HttpClient client = new DefaultHttpClient();
                HttpGet get = new HttpGet(downloadUrl);
                try {
                    HttpResponse response = client.execute(get);
                    if (response.getStatusLine().getStatusCode() == 200) {
                        InputStream is = response.getEntity().getContent();
                        File file = new File(getDownloadPathDir() + title
                                + ".pdf");
                        OutputStream os = new FileOutputStream(file);
                        byte buffer[] = new byte[1024];
                        while (is.read(buffer) != -1) {
                            os.write(buffer);
                        }
                        os.flush();
                    }
                } catch (ClientProtocolException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        }.start();
    }

    private String getDownloadPathDir() {

        if (Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().getPath()
                    + File.separator
                    + getApplication().getApplicationInfo().name
                    + File.separator;

        } else {
            return "";
        }
    }

    private void loadHtml(String html) {
        Log.d("YANGSHUANG", "loadHtml: ");
        webView.loadDataWithBaseURL(null, html, null, "utf-8", null);
    }
}
