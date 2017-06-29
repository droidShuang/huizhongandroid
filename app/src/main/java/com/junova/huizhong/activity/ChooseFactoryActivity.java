package com.junova.huizhong.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.junova.huizhong.AppConfig;
import com.junova.huizhong.R;
import com.junova.huizhong.common.FunctionUtils;
import com.junova.huizhong.common.Logger;
import com.junova.huizhong.common.http.AsyncHttpConnection;
import com.junova.huizhong.common.http.HttpMethod;
import com.junova.huizhong.db.CheckDataBase;
import com.junova.huizhong.db.DeviceDataBase;
import com.junova.huizhong.widget.LoadingDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ChooseFactoryActivity extends Activity {
    RadioGroup rg;
    Button bt;
    String ip = "";
    Button btCancle;
    LoadingDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_factory);
        rg = (RadioGroup) this.findViewById(R.id.choose_rg);
        bt = (Button) this.findViewById(R.id.choose_bt);
        btCancle = (Button) this.findViewById(R.id.choose_bt_cancel);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (rg.getCheckedRadioButtonId()) {
                    case R.id.choose_jinqiao:
                        ip = "10.188.186.222:7001";
                        AppConfig.ip = ip;
                        AppConfig.MyHost = "http://" + ip + "/SEWS/api/";//http://10.188.184.188:7001/SEWS/api/ http://10.188.184.191:80/SEWS/api/
                        AppConfig.IMAGEURL = "http://" + ip + "/SEWS/uploadFiles/uploadImgs//";
                        updataCongig(AppConfig.MyHost);
                        break;
                    case R.id.choose_anting:
                        ip = "10.188.186.221:7001";
                        AppConfig.ip = ip;
                        AppConfig.MyHost = "http://" + ip + "/SEWS/api/";//http://10.188.184.188:7001/SEWS/api/ http://10.188.184.191:80/SEWS/api/
                        AppConfig.IMAGEURL = "http://" + ip + "/SEWS/uploadFiles/uploadImgs//";
                        updataCongig(AppConfig.MyHost);
                        break;
                    case R.id.choose_antingbei:
                        ip = "10.188.186.226:7001";
                        AppConfig.ip = ip;
                        AppConfig.MyHost = "http://" + ip + "/SEWS/api/";//http://10.188.184.188:7001/SEWS/api/ http://10.188.184.191:80/SEWS/api/
                        AppConfig.IMAGEURL = "http://" + ip + "/SEWS/uploadFiles/uploadImgs//";
                        updataCongig(AppConfig.MyHost);
                        break;
                    case R.id.choose_yantai:
                        ip = "10.188.186.225:7001";
                        AppConfig.ip = ip;
                        AppConfig.MyHost = "http://" + ip + "/SEWS/api/";//http://10.188.184.188:7001/SEWS/api/ http://10.188.184.191:80/SEWS/api/
                        AppConfig.IMAGEURL = "http://" + ip + "/SEWS/uploadFiles/uploadImgs//";
                        updataCongig(AppConfig.MyHost);
                        break;

                    default:
//                        AlertDialog.Builder builder = new AlertDialog.Builder(ChooseFactoryActivity.this);
//                        builder.setTitle("提示");
//                        builder.setMessage("请先选择工厂");
//                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//
//                                dialog.cancel();
//
//                            }
//                        });
//                        builder.create().show();
                        break;
                }
                AppConfig.prefs.edit().putString("currentIp", ip).commit();
                if (TextUtils.isEmpty(ip)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ChooseFactoryActivity.this);
                    builder.setTitle("提示");
                    builder.setMessage("请先选择工厂");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.create().show();
                } else {
                    AppConfig.uploadState = 0;
                    CheckDataBase dataBase = new CheckDataBase(ChooseFactoryActivity.this);
                    dataBase.clear();
                    DeviceDataBase deviceDataBase = new DeviceDataBase(ChooseFactoryActivity.this);
                    deviceDataBase.clear();

                    String numberCode = AppConfig.prefs.getString("numberCode", "0");
                    String psd = AppConfig.prefs.getString("psd", "0");
                    getUserInfo(numberCode, psd);

                }
            }
        });
        btCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(ChooseFactoryActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
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

    private void getUserInfo(String user, String psd) {
        dialog = FunctionUtils.showDialog(this);
        Map<Object, Object> op = new HashMap<Object, Object>();
        op.put("NUMBERCODE", user);
        op.put("PASSWORD", psd);
        new AsyncHttpConnection().post(AppConfig.GET_USER_INFO,
                HttpMethod.getParams(getApplicationContext(), op),
                new AsyncHttpConnection.CallbackListener() {

                    @Override
                    public void callBack(String result) {
                        FunctionUtils.closeDialog(dialog);

                        if (result != null) {
                            try {
                                JSONObject obj = new JSONObject(result);
                                int state = obj.getInt("status");
                                if (state == 01) {
                                    AppConfig.prefs.edit().putString("uuid", UUID.randomUUID().toString().replace("-", "")).commit();
                                    JSONObject date = obj.getJSONObject("data");
                                    String userId = date.getString("USERID");
                                    //      String name = date.getString("name");
                                    String phone = date.getString("PHONE");
                                    String part = date.getString("PART");
                                    String realname = date.getString("USERNAME");
                                    String partId = date.getString("PARTID");
                                    int station = date.getInt("STATION");

                                    startMainActivity(userId, realname, phone,
                                            part, partId, station, realname);
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

    private void startMainActivity(String userId, String name, String phone,
                                   String part, String partId, int station, String realname) {
        Logger.getInstance().e("save", "-------------");
        SharedPreferences.Editor editor = AppConfig.prefs.edit();
        editor.putString("userId", userId);
        editor.putString("name", name);
        editor.putString("phone", phone);
        editor.putString("part", part);
        editor.putString("partId", partId);
        editor.putInt("station", station);
        editor.putString("realname", realname);
        editor.putBoolean("canCheck", true);
        editor.putString("checkedId", "");
        editor.commit();
        if (station == 0) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

        ChooseFactoryActivity.this.finish();
    }

}
