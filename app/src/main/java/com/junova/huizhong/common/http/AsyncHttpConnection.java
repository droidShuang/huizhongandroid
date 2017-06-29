package com.junova.huizhong.common.http;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.junova.huizhong.AppConfig;
import com.junova.huizhong.common.Logger;

public class AsyncHttpConnection implements Runnable {

    public static final int DID_START = 0;
    public static final int DID_ERROR = 1;
    public static final int DID_SUCCEED = 2;

    private static final int GET = 0;
    private static final int POST = 1;
    private static final int PUT = 2;
    private static final int DELETE = 3;
    private static final int BITMAP = 4;

    private String url;
    private int method;
    private List<NameValuePair> data;
    private CallbackListener listener;

    public void create(int method, String url, List<NameValuePair> data,
                       CallbackListener listener) {
        this.method = method;
        this.url = url;
        this.data = data;
        this.listener = listener;
        Logger.getInstance().e("param", url + data);
        AsyncConnectionManager.getInstance().push(this);
    }

    public void createOnce(int method, String url, List<NameValuePair> data,
                           CallbackListener listener) {
        this.method = method;
        this.url = url;
        this.data = data;
        this.listener = listener;
        Logger.getInstance().e("param", url + data);
        AsyncConnectionManager.getInstance().pushOnce(this);
    }

    public void get(String url) {
        create(GET, url, null, listener);
    }

    public void post(String url, List<NameValuePair> data,
                     CallbackListener listener) {
        Log.d("aaaaaaaaaaaaa", data.toString());
        create(POST, url, data, listener);
    }

    public void postOnce(String url, List<NameValuePair> data,
                         CallbackListener listener) {
        create(POST, url, data, listener);
    }

    public void put(String url, List<NameValuePair> data) {
        create(PUT, url, data, listener);
    }

    public void delete(String url) {
        create(DELETE, url, null, listener);
    }

    public void bitmap(String url) {
        create(BITMAP, url, null, listener);
    }

    public interface CallbackListener {
        void callBack(String result) ;
    }

    private static final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case AsyncHttpConnection.DID_START: {
                    break;
                }
                case AsyncHttpConnection.DID_SUCCEED: {
                    CallbackListener listener = (CallbackListener) message.obj;
                    Object data = message.getData();
                    if (listener != null) {
                        if (data != null) {
                            Bundle bundle = (Bundle) data;
                            String result = bundle.getString("callbackkey");
                            listener.callBack(result);
                        }
                    }
                    break;
                }
                case AsyncHttpConnection.DID_ERROR: {
                    break;
                }
            }
        }
    };

    public void run() {
        AppConfig.httpAsyncClient = getHttpClient();
        AppConfig.currThreadId = Thread.currentThread().getId();
        try {
            HttpResponse httpResponse = null;
            switch (method) {
                case GET:
                    httpResponse = AppConfig.httpAsyncClient.execute(new HttpGet(
                            url));
                    break;
                case POST:
                    HttpPost httpPost = new HttpPost(url);

                    httpPost.setEntity(new UrlEncodedFormEntity(data, "UTF-8"));
                    httpPost.addHeader("Accept-Encoding", "gzip,deflate,sdch");

                    httpPost.addHeader("User-Agent", "AppBuilder_android/"
                            + AppConfig.version);
                    httpResponse = AppConfig.httpAsyncClient.execute(httpPost);

                    if (isHttpSuccessExecuted(httpResponse)) {

                        HttpEntity httpEntity = httpResponse.getEntity();

                        String result = null;

                        if (httpEntity.getContentEncoding() != null
                                && httpEntity.getContentEncoding().getValue() != null
                                && httpEntity.getContentEncoding().getValue()
                                .contains("gzip")) {
                            InputStream is = httpEntity.getContent();

                            is = new GZIPInputStream(is);
                            BufferedReader br = new BufferedReader(
                                    new InputStreamReader(is));
                            String line = null;
                            StringBuffer sb = new StringBuffer();
                            while ((line = br.readLine()) != null) {
                                sb.append(line);
                            }
                            result = sb.toString();
                        } else {
                            result = EntityUtils.toString(httpEntity);
                        }
                        this.sendMessage(result);
                    } else {
                        this.sendMessage("fail");
                    }
                    break;
            }
        } catch (Exception e) {
            Log.i("Asyn", e.toString());
            this.sendMessage("fail");
        }
        AsyncConnectionManager.getInstance().didComplete(this);
    }

    private void sendMessage(String result) {
        Message message = Message.obtain(handler, DID_SUCCEED, listener);
        Bundle data = new Bundle();
        data.putString("callbackkey", result);
        message.setData(data);
        handler.sendMessage(message);
    }

    public static DefaultHttpClient getHttpClient() {
        HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 25000);
        HttpConnectionParams.setSoTimeout(httpParams, 25000);
        HttpConnectionParams.setSocketBufferSize(httpParams, 8192);
        DefaultHttpClient httpClient = new DefaultHttpClient(httpParams);
        return httpClient;
    }

    public static boolean isHttpSuccessExecuted(HttpResponse response) {
        int statusCode = response.getStatusLine().getStatusCode();
        return (statusCode > 199) && (statusCode < 400);
    }
}
