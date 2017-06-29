package com.junova.huizhong.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.junova.huizhong.AppApplication;
import com.junova.huizhong.AppConfig;
import com.junova.huizhong.R;
import com.junova.huizhong.adapter.XunChaAdapter;
import com.junova.huizhong.common.FunctionUtils;
import com.junova.huizhong.common.Logger;
import com.junova.huizhong.common.http.AsyncHttpConnection;
import com.junova.huizhong.common.http.AsyncHttpConnection.CallbackListener;
import com.junova.huizhong.common.http.HttpMethod;
import com.junova.huizhong.db.CheckDataBase;
import com.junova.huizhong.db.DeviceDataBase;
import com.junova.huizhong.model.DeviceParam;
import com.junova.huizhong.widget.LoadingDialog;

public class XunChaActivity extends Activity implements OnClickListener {
    ListView listView;
    XunChaAdapter adapter;
    Button back;
    public TextView times;
    EditText searchText;
    Button searchBtn;
    RadioGroup radioGroup;
    RadioButton radioButton1;
    RadioButton radioButton2;
    public List<DeviceParam> data = new ArrayList<DeviceParam>();
    int type = 0;
    int pageIndex = 1;
    LoadingDialog dialog;
    public RadioButton childAt;
    int checkCount;
    private int allCount = 0;
    private int unCheckCount = 1;
    Timer timer;
    TimerTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xuncha);
        Date curdate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        checkCount = AppConfig.prefs.getInt(sdf.format(curdate), 1);


        ((AppApplication) getApplication()).addActivity(this);
        initViews();
        getDeviceList(type, pageIndex);
        SharedPreferences prfs = getSharedPreferences(AppConfig.prfsName, MODE_MULTI_PROCESS);
        String partIds = prfs.getString("checkedId", "");
        if (!partIds.contains(prfs.getString("partId", "0"))) {
            partIds = partIds + "," + prfs.getString("partId", "0");
            prfs.edit().putString("checkedId", partIds).commit();
        }

        final android.os.Handler handler = new android.os.Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0:
                        getDeviceList(type, pageIndex);
                        AppConfig.uploadState = 0;
                        radioButton2.setEnabled(true);
                        radioButton1.setEnabled(true);
                        //   radioGroup.setClickable(true);
                        break;
                    case 1:
//                        radioButton2.setEnabled(false);
//                        radioButton1.setEnabled(false);

                        break;
                }
            }
        };
        task = new TimerTask() {
            @Override
            public void run() {
                //  com.orhanobut.logger.Logger.d("state:" + AppConfig.uploadState);
                if (AppConfig.uploadState == 2) {
                    handler.sendEmptyMessage(0);
                    com.orhanobut.logger.Logger.d("state:" + AppConfig.uploadState);

                } else if (AppConfig.uploadState == 1) {
                    handler.sendEmptyMessage(1);
                }
            }
        };
        timer = new Timer();

        timer.schedule(task, 1000, 2000);


    }

    private void initViews() {
        listView = (ListView) findViewById(R.id.list_xuncha);
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {


//                if (data.get(arg2).getStatus() != 0) {
//                    Toast toast = Toast.makeText(XunChaActivity.this, "已检查", Toast.LENGTH_SHORT);
//                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
//                    toast.show();
//                    return;
//                }
                if (!AppConfig.prefs.getBoolean("canUp", false)) {
                    Intent intent = new Intent(XunChaActivity.this, YinHuanDetailActivity.class);
                    Log.d("clearDevice", "xuncha getSummray    " + type);
                    intent.putExtra("id", data.get(arg2).getId());
                    intent.putExtra("postion", arg2);
                    intent.putExtra("type", type);
                    intent.putExtra("leader", false);
                    intent.putExtra("partId", AppConfig.prefs.getString("partId", "0"));
                    intent.putExtra("title", type == 0 ? "仪器检测" : "隐患排查");
                    intent.putExtra("sts", data.get(arg2).getStatus());
                    intent.putExtra("SUMMARY", data.get(arg2).getSummray());
                    intent.putExtra("INSTTYPE", data.get(arg2).getType());
                    intent.putExtra("TITLE", data.get(arg2).getName());
                    intent.putExtra("SPECLD", data.get(arg2).getSpecId());

                    startActivityForResult(intent, 1001);
                } else {

                    Toast toast = Toast.makeText(XunChaActivity.this, "正在上传，请稍后", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    toast.show();
                }

            }
        });
        Logger.getInstance().e("size", "大小" + data.size());
        adapter = new XunChaAdapter(getApplicationContext(), data);
        listView.setAdapter(adapter);
        back = (Button) findViewById(R.id.back);
        back.setOnClickListener(this);
        times = (TextView) findViewById(R.id.xuncha_times);
        SharedPreferences prfs = getSharedPreferences(AppConfig.xunchaprfs,
                MODE_MULTI_PROCESS);
        String uid = AppConfig.prefs.getString("userId", "0");
        times.setText("（第" + checkCount + "次）");
        searchText = (EditText) findViewById(R.id.edt_search);
        // 设置输入法搜索监听
        searchText.setOnEditorActionListener(new OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    search();
//					Toast.makeText(XunChaActivity.this, "搜索", Toast.LENGTH_LONG).show();
                }
                return true;
            }
        });
        radioButton1 = (RadioButton) findViewById(R.id.rg_paicha);
        radioButton2 = (RadioButton) findViewById(R.id.rg_jiance);
        searchBtn = (Button) findViewById(R.id.btn_search);
        searchBtn.setOnClickListener(this);
        radioGroup = (RadioGroup) findViewById(R.id.rg_xuncha);
        childAt = (RadioButton) radioGroup.getChildAt(0);

        radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                if (AppConfig.prefs.getBoolean("canUp", false)) {
                    Toast toast = Toast.makeText(XunChaActivity.this, "请等待上传完毕之后，重试", Toast.LENGTH_SHORT);

                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    toast.show();
                    return;
                }
                switch (arg1) {
                    case R.id.rg_paicha:
                        type = 0;
                        break;
                    case R.id.rg_jiance:
                        type = 1;
                        break;
                    default:
                        break;
                }
                //         com.orhanobut.logger.Logger.d(AppConfig.uploadState);
                if (AppConfig.uploadState == 1) {

                    Toast toast = Toast.makeText(XunChaActivity.this, "正在上传，请稍后", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    toast.show();

                } else {
                    getDeviceList(type, pageIndex);
                }
            }
        });
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.back:
                if (getUnCheckedDevicesCount() == 0 & getDeviceCount() > 0) {
                    AppConfig.prefs.edit().putBoolean("canCheck", false).commit();
                }
                finish();
                break;
            case R.id.btn_search:

                search();
                break;
            default:
                break;
        }
    }

    /**
     * 搜索设备
     */
    private void search() {

        DeviceDataBase db = new DeviceDataBase(getApplicationContext());
        List<DeviceParam> dbData = db.search(AppConfig.prefs.getString("partId", "0"),
                AppConfig.prefs.getString("userId", "0"), searchText.getText().toString());
        if (dbData != null & dbData.size() > 0) {
            data.clear();
            for (int i = 0; i < dbData.size(); i++) {
                DeviceParam param = dbData.get(i);
                if (type == param.getType()) {
                    data.add(param);
                }
            }

            adapter.notifyDataSetChanged();
        } else {

            Toast toast = Toast.makeText(XunChaActivity.this, "搜索不到内容", Toast.LENGTH_SHORT);

            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.show();
        }
    }

    public void getDeviceList(int type, int pageIndex) {
        //清空当前list中的数据
        data.clear();
        allCount = 0;
        unCheckCount = 1;
        // adapter.notifyDataSetChanged();
        //从数据库中读取数据
        DeviceDataBase db = new DeviceDataBase(getApplicationContext());
        List<DeviceParam> dbData = db.getDevices(AppConfig.prefs.getString("partId", "0"),
                AppConfig.prefs.getString("userId", "0"), type);
        com.orhanobut.logger.Logger.d("data size" + dbData.size());
        if (dbData != null & dbData.size() > 0) {
            com.orhanobut.logger.Logger.d("从数据库中获取数据");
            for (int i = 0; i < dbData.size(); i++) {
                com.orhanobut.logger.Logger.d("sql " + dbData.get(i).getType() + " current" + type);
                if (dbData.get(i).getType() == type) {
                    DeviceParam param = dbData.get(i);
                    data.add(param);
                }
            }
            com.orhanobut.logger.Logger.d("从数据库中获取数据的长度" + data.size());
            adapter.notifyDataSetChanged();
        } else {
            //如果数据空中没有数据则从网络上获取
            com.orhanobut.logger.Logger.d("从网络中获取数据");
            dialog = FunctionUtils.showDialog(this);
            Map<Object, Object> op = new HashMap<Object, Object>();
            op.put("PARTID", AppConfig.prefs.getString("partId", "0"));
            op.put("STATION", AppConfig.prefs.getInt("station", 0));
            new AsyncHttpConnection().post(AppConfig.GET_DEVICE_LIST,
                    HttpMethod.getParams(getApplicationContext(), op), new CallbackListener() {

                        @Override
                        public void callBack(String result) {
                            FunctionUtils.closeDialog(dialog);


                            if (result != null) {
                                try {
                                    JSONObject obj = new JSONObject(result);
                                    int status = obj.getInt("status");
                                    if (status == 01) {
                                        jsonDeviceList(obj);
                                        AppConfig.uploadState = 0;
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


    }

    private void jsonDeviceList(JSONObject obj) {
        try {

            List<DeviceParam> list = new ArrayList<DeviceParam>();
            JSONArray array = obj.getJSONArray("data");
            AppConfig.prefs.edit().putInt("DeviceCount", array.length()).commit();
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                String id = object.getString("ID");
                int status = object.getInt("STATUS");
                String name = object.getString("NAME");
                String specId = object.getString("SPECLD");
                String summary = object.getString("SUMMARY");
                int type = object.getInt("TYPE");
                DeviceParam dp = new DeviceParam(id, type, name, summary, specId, status);
                if (type == this.type) {
                    data.add(dp);
                }
                list.add(dp);
            }
            if (list.size() > 0) {
                DeviceDataBase db = new DeviceDataBase(getApplicationContext());
                String partId = AppConfig.prefs.getString("partId", "0");
                db.clearDevice(AppConfig.prefs.getString("userId", "0"));
                for (int i = 0; i < list.size(); i++) {
                    DeviceParam param = list.get(i);
                    long result = db.addDevice(partId, param.getName(), param.getSpecId(), param.getId(), param.getType(),
                            param.getSummray(), AppConfig.prefs.getString("userId", "0"), param.getStatus());
                    Logger.getInstance().e("db.size", result + "   ");
                }
                Logger.getInstance().e("db.size",
                        db.getDevices(partId, AppConfig.prefs.getString("userId", "0"), type).size() + "  s");
            }
            Date preCurdate = new Date();
            SimpleDateFormat preSdf = new SimpleDateFormat("yyyy-MM-dd");

            times.setText("（第" + AppConfig.prefs.getInt(preSdf.format(preCurdate), 1) + "次）");
            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * setStatus 作用 检测详情页传入设备ID 和 状态 更新设备列表页的检测状态 TODO(描述)
     *
     * @param @param deviceId
     * @param @param status
     * @return void
     * @throws
     * @Title: setStatus
     * @Description: TODO
     * @author hao_mo@loongjoy.com
     */
    public void setStatus(String deviceId, int status) {
//        Logger.getInstance().e("ssssss", deviceId + "    " + status);
//        DeviceDataBase db = new DeviceDataBase(getApplicationContext());
//        long result = db.updateStatus(deviceId, status, AppConfig.prefs.getString("userId", "0"));
//        Logger.getInstance().e("status", result + " set status ");
        getDeviceList(type, pageIndex);

    }

    /*
     * <p>Title: onActivityResult</p> <p>Description: </p>
     *
     * @param requestCode
     *
     * @param resultCode
     *
     * @param data
     *
     * @see android.app.Activity#onActivityResult(int, int,
     * android.content.Intent)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 1001) {
            setStatus(data.getStringExtra("deviceId"), data.getIntExtra("status", 0));
            for (int i = 0; i < this.data.size(); i++) {
                if (this.data.get(i).getStatus() != 0) {
                    allCount++;
                }
            }
            com.orhanobut.logger.Logger.d("allcount:" + allCount + "    data size" + this.data.size());
            //    unCheckCount = getUnCheckedDevicesCount();
//            if (unCheckCount == 0) {
//                radioButton2.setEnabled(false);
//                radioButton1.setEnabled(false);
//                radioGroup.setOnClickListener(new OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        Toast toast = Toast.makeText(XunChaActivity.this, "请等待上传完毕之后，点击", Toast.LENGTH_SHORT);
//                        ;
//                        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
//                        toast.show();
//                    }
//                });
//            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (data.size() == 0) {// 数据为0 说明是第一次进入页面 不作其他处理
            return;
        }
        if (adapter != null) {// 如果是从设备详情页返回来 则更新listview的设备检测状态；

            adapter.updata(data);
        }

        Date preCurdate = new Date();
        SimpleDateFormat preSdf = new SimpleDateFormat("yyyy-MM-dd");
        checkCount = AppConfig.prefs.getInt(preSdf.format(preCurdate), 1);
        times.setText("（第" + checkCount + "次）");
    }

    /**
     * getUnCheckedDevicesCount 作用 获取未检测设备数 TODO(描述)
     *
     * @param @return
     * @return int
     * @throws
     * @Title: getUnCheckedDevicesCount
     * @Description: TODO
     * @author hao_mo@loongjoy.com
     */
    public int getUnCheckedDevicesCount() {
        int count = 0;
        DeviceDataBase db = new DeviceDataBase(getApplicationContext());
        List<DeviceParam> list = db
                .getDevices(AppConfig.prefs.getString("partId", "0"), AppConfig.prefs.getString("userId", "0"));
        for (int i = 0; i < list.size(); i++) {
            DeviceParam param = list.get(i);
            if (param.getStatus() == 0 || param.getStatus() == 10) {
                count++;
            }
        }
        Logger.getInstance().e("count", count);
        return count;
    }

    /**
     * @author rider
     * Created on 2016/8/11 0011 9:41
     * Description :获取检测设备数
     */
    public int getDeviceCount() {
        int count = 0;
        CheckDataBase db = new CheckDataBase(getApplicationContext());
        count = db.getCheckCount();
        return count;
    }


}
