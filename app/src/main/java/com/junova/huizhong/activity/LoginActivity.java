package com.junova.huizhong.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.junova.huizhong.AppApplication;
import com.junova.huizhong.AppConfig;
import com.junova.huizhong.BuildConfig;
import com.junova.huizhong.R;
import com.junova.huizhong.common.FunctionUtils;
import com.junova.huizhong.common.Logger;
import com.junova.huizhong.common.http.AsyncHttpConnection;
import com.junova.huizhong.common.http.AsyncHttpConnection.CallbackListener;
import com.junova.huizhong.common.http.HttpMethod;
import com.junova.huizhong.widget.LoadingDialog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import okhttp3.Call;

public class LoginActivity extends Activity {
    EditText userEdt;
    EditText psdEdt;
    TextView forgetPWD;
    LoadingDialog dialog;
    Button btChooseIp;
    //  Spinner spinner;
    Dialog updataDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        for (Activity i : ((AppApplication) getApplication()).getActivities()) {
            i.finish();
        }
        try {
            com.orhanobut.logger.Logger.e("start accept");
            String userId = getIntent().getStringExtra("USERID");
            com.orhanobut.logger.Logger.e("aaaaaaaaaaaaa");
            String phone = getIntent().getStringExtra("PHONE");
            String part = getIntent().getStringExtra("PART");
            com.orhanobut.logger.Logger.e("bbbbbbbbbbbb");
            String userName = getIntent().getStringExtra("USERNAME");
            String partId = getIntent().getStringExtra("PARTID");
            String station = getIntent().getStringExtra("STATION");
            String numberCode = getIntent().getStringExtra("NUMBERCODE");
            AppConfig.prefs.edit().putString("uuid", UUID.randomUUID().toString().replace("-", "")).commit();
            com.orhanobut.logger.Logger.e("cccccccccccccccccc");
            startMainActivity(userId, userName, phone, part, partId, Integer.parseInt(station), userName,numberCode);

        } catch (Exception e) {
            e.printStackTrace();
            com.orhanobut.logger.Logger.e(e.getMessage());
        }

        initViews();
    }

    private void initViews() {


        //   spinner = (Spinner) findViewById(R.id.login_sp);
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                String ip = "";
//                switch (position) {
//                    // 安亭
//                    case 0:
//                        ip = "10.188.186.221:7001";
//                        AppConfig.appConfig.edit().putInt("position", position).apply();
//                        break;
//                    //金桥
//                    case 1:
//                        ip = "10.188.186.222:7001";
//                        AppConfig.appConfig.edit().putInt("position", position).apply();
//                        break;
//                    //安保部
//                    case 4:
//                        ip = "10.188.186.222:7001";
//                        AppConfig.appConfig.edit().putInt("position", position).apply();
//                        break;
//                    //安亭北
//                    case 3:
//                        ip = "10.188.186.226:7001";
//                        AppConfig.appConfig.edit().putInt("position", position).apply();
//                        break;
//                    //烟台
//                    case 2:
//                        ip = "10.188.186.225:7001";
//                        AppConfig.appConfig.edit().putInt("position", position).apply();
//                        break;
//                    default:
//                        break;
//                }
//                if (!TextUtils.isEmpty(ip)) {
//                    AppConfig.ip = ip;
//                    AppConfig.MyHost = "http://" + ip + "/SEWS/api/";//http://10.188.184.188:7001/SEWS/api/ http://10.188.184.191:80/SEWS/api/
//                    AppConfig.IMAGEURL = "http://" + ip + "/SEWS/uploadFiles/uploadImgs//";
//                    AppConfig.prefs.edit().putString("currentIp", ip).commit();
//                    updataCongig(AppConfig.MyHost);
//                }
//
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
        //     spinner.setSelection(AppConfig.appConfig.getInt("position", 0));
        btChooseIp = (Button) findViewById(R.id.login_bt_choseip);
        userEdt = (EditText) findViewById(R.id.userEdt);
        psdEdt = (EditText) findViewById(R.id.psdEdt);
        forgetPWD = (TextView) findViewById(R.id.forgetPWD);
        forgetPWD.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        forgetPWD.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,
                        FindPasswordActivity.class);
                startActivity(intent);

            }
        });
        if (!BuildConfig.DEBUG) {
            btChooseIp.setVisibility(View.INVISIBLE);
        }
        Button login = (Button) findViewById(R.id.loginBtn);
        login.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
//                int[] tests = {1, 1};
//                int j = tests[10];
                doLogin();
            }
        });

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_bt_choseip:

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                View dialogView = View.inflate(this, R.layout.dialog_ip, null);
                final RadioGroup rgIp = (RadioGroup) dialogView.findViewById(R.id.rg_ip);
                final EditText etIp = (EditText) dialogView.findViewById(R.id.et_ip);
                builder.setView(dialogView);
                builder.setTitle("选择IP");
                builder.setPositiveButton("保存", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String ip = "";
                        String strIp = etIp.getText().toString();
                        //10.124.87.208 安亭//10.124.87.188金桥

                        if (!strIp.isEmpty()) {
                            ip = strIp;
                        } else {
                            switch (rgIp.getCheckedRadioButtonId()) {
                                case R.id.rb_anting:
                                    ip = "10.188.186.221:7001";
                                    break;
                                case R.id.rb_jinqiao:
                                    ip = "10.188.186.222:7001";
                                    break;
                                case R.id.rb_junova:
                                    ip = "10.188.186.214:7001";
                                    break;
                            }
                        }
                        AppConfig.ip = ip;
                        AppConfig.prefs.edit().putString("currentIp", ip).commit();
                        AppConfig.MyHost = "http://" + ip + "/SEWSD/api/";//http://10.188.184.188:7001/SEWS/api/ http://10.188.184.191:80/SEWS/api/
                        AppConfig.IMAGEURL = "http://" + ip + "/SEWSD/uploadFiles/uploadImgs//";
                        updataCongig(AppConfig.MyHost);
                        Toast.makeText(LoginActivity.this, "IP:" + AppConfig.ip + "设置成功", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.create().show();
        }
    }

    private void doLogin() {
        // TODO Auto-generated method stub
        final String user = userEdt.getText().toString().trim();
        final String psd = psdEdt.getText().toString().trim();
        Map<String, String> params = new HashMap<>();
        com.orhanobut.logger.Logger.e("版本号：" + AppConfig.version);
        params.put("versionCode", AppConfig.version + "");

//        OkHttpUtils.post().url(AppConfig.UPDATA).params(params).build().execute(new StringCallback() {
//            @Override
//            public void onError(Call call, Exception e) {
//                e.printStackTrace();
//            }
//
//            @Override
//            public void onResponse(String response) {
//                com.orhanobut.logger.Logger.json(response);
//                try {
//                    JSONObject jsonObject = new JSONObject(response);
//                    int status = jsonObject.getInt("status");
//                    if (status == 06) {
//                        getUserInfo(user, psd);
//                    } else if (status == 07) {
//                        downloadApp(jsonObject.getString("url"));
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    getUserInfo(user, psd);
//                    FunctionUtils.closeDialog(dialog);
//                }
//            }
//        });
        getUserInfo(user, psd);

    }

    private void getUserInfo(String user, String psd) {
        dialog = FunctionUtils.showDialog(this);
        Map<Object, Object> op = new HashMap<Object, Object>();
        op.put("NUMBERCODE", user);
        op.put("PASSWORD", psd);
        new AsyncHttpConnection().post(AppConfig.GET_USER_INFO,
                HttpMethod.getParams(getApplicationContext(), op),
                new CallbackListener() {

                    @Override
                    public void callBack(String result) {
                        FunctionUtils.closeDialog(dialog);
                        Log.d("YANGSHUANG", "GET_USER_INFO	" + result);

                        Logger.getInstance().e("mohao", result);
                        if (result != null) {
                            try {
                                JSONObject obj = new JSONObject(result);
                                int state = obj.getInt("status");
                                if (state == 01) {


                                    JSONObject date = obj.getJSONObject("data");
                                    int station = date.getInt("STATION");
//                                    if (spinner.getSelectedItemPosition() == 4) {
//                                        if (station != 0) {
//                                            Toast.makeText(LoginActivity.this, "登录用户没有权限", Toast.LENGTH_SHORT).show();
//                                            return;
//                                        }
//                                    }
                                    AppConfig.prefs.edit().putString("uuid", UUID.randomUUID().toString().replace("-", "")).commit();
                                    String userId = date.getString("USERID");
                                    //      String name = date.getString("name");
                                    String phone = date.getString("PHONE");
                                    String part = date.getString("PART");
                                    String realname = date.getString("USERNAME");
                                    String partId = date.getString("PARTID");


                                    startMainActivity(userId, realname, phone,
                                            part, partId, station, realname,userEdt.getText().toString());
                                } else {
                                    String msg = obj.getString("message");
                                    Toast.makeText(getApplicationContext(),
                                            msg, Toast.LENGTH_SHORT).show();
                                    Logger.getInstance().e("login error", msg);
                                }

                            } catch (JSONException e) {
                                Toast.makeText(getApplicationContext(), "网络异常",
                                        Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "网络异常",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**
     * startMainActivity 作用 保存用户信息，并启动主页 TODO(描述)
     *
     * @param realname
     * @param @param   userId
     * @param @param   name
     * @param @param   phone
     * @param @param   part
     * @param @param   partId
     * @param @param   station
     * @return void
     * @throws
     * @Title: startMainActivity
     * @Description: TODO
     * @author hao_mo@loongjoy.com
     */
    private void startMainActivity(String userId, String name, String phone,
                                   String part, String partId, int station, String realname,String numberCode) {
        com.orhanobut.logger.Logger.e("11111111111111111111111");
        Logger.getInstance().e("save", "-------------");
        Editor editor = AppConfig.prefs.edit();
        com.orhanobut.logger.Logger.e("22222222222");
        editor.putString("numberCode", numberCode);
        com.orhanobut.logger.Logger.e("33333333333333");
        editor.putString("psd", "");
        editor.putString("userId", userId);
        editor.putString("name", name);
        editor.putString("phone", phone);
        editor.putString("part", part);
        editor.putString("partId", partId);
        com.orhanobut.logger.Logger.e("22222222222");
        editor.putInt("station", station);
        com.orhanobut.logger.Logger.e("33333333333333");
        editor.putString("realname", realname);
        editor.putBoolean("canCheck", true);
        editor.apply();
//        if (station == 0) {
//            Intent intent = new Intent(this, ChooseFactoryActivity.class);
//            startActivity(intent);
//        } else {
        com.orhanobut.logger.Logger.e("ddddddddddddd");
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        //    }

        LoginActivity.this.finish();
    }

	/*
     * <p>Title: onBackPressed</p> <p>Description: </p>
	 * 
	 * @see android.app.Activity#onBackPressed()
	 */

    @Override
    public void onBackPressed() {

    }

    private void updataCongig(String MyHost) {
        AppConfig.GET_ACTIVITY_HISTORY_URL = MyHost + "getActifityHistory";
        AppConfig.GET_INDEX = MyHost + "getIndex";
        AppConfig.GET_USER_INFO = MyHost + "getUserInfo";
        AppConfig.GET_ARTICLE_LIST = MyHost + "getTypeArticle";
        AppConfig.GET_ARTICLE_DETAIL = MyHost + "getArticleDetail";
        AppConfig.GET_SUBLEVEL_LIST = MyHost + "getSubLevelList";
        AppConfig.GET_DEVICE_LIST = MyHost + "getDeviceList";
        AppConfig.GET_DEVICE_DETAIL = MyHost + "getDeviceDetail";
        AppConfig.GET_ACTIVITY_LIST = MyHost + "getActivityList";
        AppConfig.GET_ACTIVITY_DETAIL = MyHost + "getActivityDetail";
        AppConfig.GET_ACCIDENT_LIST = MyHost + "getAccidentList";
        AppConfig.UP_DATA_USER_PWD = MyHost + "updateUserPwd";
        AppConfig.SEND_SMS_CODE = MyHost + "sendSmsCode";
        AppConfig.GET_OTHER_DETAIL = MyHost + "getOtherDetail";
        AppConfig.RESET_PWD = MyHost + "resetPwd";
        AppConfig.SUBMIN_ACTIVITY = MyHost + "submitActivity";
        AppConfig.UPLOAD_IMAGES = MyHost + "uploadImaes";
        AppConfig.GET_BATCH_NUMBER = MyHost + "getBatchNumber";
        AppConfig.SUBMIN_DEVICE_RECORD = MyHost + "submitDeviceRecord";
        AppConfig.SUBMIT_OTHEREXCEPTION = MyHost + "submitOtherException";

        AppConfig.ERROR_LOG = MyHost + "errorLog";
    }

    public void downloadApp(String url) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = View.inflate(this, R.layout.dialog_updata, null);
        final ProgressBar pb = (ProgressBar) view.findViewById(R.id.item_pb);
        OkHttpUtils.get().url(AppConfig.ARTICALDOWNLOAD + url).build().execute(new FileCallBack(Environment.getExternalStorageDirectory().getAbsolutePath(), "助手" + ".apk") {
            @Override
            public void inProgress(float progress, long total) {
                pb.setProgress((int) (progress * 100));

            }

            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
                com.orhanobut.logger.Logger.d("progress " + e.getMessage());
            }

            @Override
            public void onResponse(File response) {
                updataDialog.setCancelable(true);
                updataDialog.cancel();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setDataAndType(Uri.fromFile(response),
                        "application/vnd.android.package-archive");
                com.orhanobut.logger.Logger.e("安装app");
                startActivity(intent);
            }
        });
        builder.setView(view);
        builder.setTitle("更新");
        builder.setCancelable(false);
        updataDialog = builder.create();
        updataDialog.show();
    }
}
