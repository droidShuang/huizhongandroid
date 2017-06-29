package com.junova.huizhong.activity;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.junova.huizhong.AppConfig;
import com.junova.huizhong.R;
import com.junova.huizhong.adapter.AddYichangGridAdapter;
import com.junova.huizhong.adapter.AddYichangGridAdapter.ClickItemListener;
import com.junova.huizhong.adapter.BanZuAdapter;
import com.junova.huizhong.adapter.XunChaAdapter;
import com.junova.huizhong.common.FunctionUtils;
import com.junova.huizhong.common.Logger;
import com.junova.huizhong.common.http.AsyncHttpConnection;
import com.junova.huizhong.common.http.AsyncHttpConnection.CallbackListener;
import com.junova.huizhong.common.http.HttpMethod;
import com.junova.huizhong.db.CheckDataBase;
import com.junova.huizhong.db.DeviceDataBase;
import com.junova.huizhong.model.DeviceParam;
import com.junova.huizhong.widget.CheJianPop;
import com.junova.huizhong.widget.GongChangPop;
import com.junova.huizhong.widget.GongDuanPop;
import com.junova.huizhong.widget.GongSiPop;
import com.junova.huizhong.widget.LoadingDialog;
import com.junova.huizhong.widget.PicGridView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

public class ChouChaActivity extends Activity implements OnClickListener {
    private int allCount = 0;
    String desc = "";
    int station = 0;
    String fileName = "";
    Button back;
    EditText searchEdt;
    Button searchBtn;
    TextView chooseTxt;
    RadioGroup radioGroup;
    RadioButton radioButton1;//隐患
    RadioButton radioButton2;//排查
    ListView ccList;
    BanZuAdapter bzAdapter;
    String id = "0";// partId
    // private List<Integer> partId = new ArrayList<Integer>();
    int lastId; // 比较两次的ID 若ID一样 就不重新请求设备列表
    public List<DeviceParam> data = new ArrayList<DeviceParam>();
    XunChaAdapter adapter;
    int type;// 0 隐患排查 ； 1 仪器检测
    int pageIndex = 1;
    LoadingDialog dialog;
    private Button otherBtn;
    private OtherPop otherpop; // 其他异常popwindow
    public String fileNsmes = ""; // 文件名称集合
    TimerTask task;
    Timer timer;
    private int unCheckCount = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choucha);
        initViews();
    }

    private void initViews() {
        back = (Button) findViewById(R.id.back);
        searchEdt = (EditText) findViewById(R.id.edt_search);
        searchEdt.setOnEditorActionListener(new OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    search();

                }
                return true;
            }
        });
        otherBtn = (Button) findViewById(R.id.otherbtn);
        otherBtn.setOnClickListener(this);
        searchBtn = (Button) findViewById(R.id.btn_search);
        searchBtn.setOnClickListener(this);
        chooseTxt = (TextView) findViewById(R.id.group_choose);
        back.setOnClickListener(this);
        chooseTxt.setOnClickListener(this);
        radioGroup = (RadioGroup) findViewById(R.id.rg_choucha);
        radioButton1 = (RadioButton) findViewById(R.id.yinhuan);
        radioButton2 = (RadioButton) findViewById(R.id.paicha);
        radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                if (id.equals("0")) {

                    Toast toast = Toast.makeText(ChouChaActivity.this, "请先选择班组", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    toast.show();
                    return;
                }
                if (AppConfig.prefs.getBoolean("canUp", false)) {
                    Toast toast = Toast.makeText(ChouChaActivity.this, "请等待上传完毕后，操作", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    toast.show();
                    return;
                }
                switch (arg1) {
                    case R.id.yinhuan:
                        type = 0;
                        break;
                    case R.id.paicha:
                        type = 1;
                        break;
                    default:
                        break;
                }
                getDeviceList(type, pageIndex);
            }
        });
        ccList = (ListView) findViewById(R.id.list_chouCha);
        adapter = new XunChaAdapter(getApplicationContext(),
                new ArrayList<DeviceParam>());
        ccList.setAdapter(adapter);
        ccList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                com.orhanobut.logger.Logger.d("state:" + data.get(arg2).getStatus());
//                if (data.get(arg2).getStatus() != 0) {
//                    Toast.makeText(ChouChaActivity.this, "已检查", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                if (!AppConfig.prefs.getBoolean("canUp", false)) {
                    Intent intent = new Intent(ChouChaActivity.this, YinHuanDetailActivity.class);
                    intent.putExtra("id", data.get(arg2).getId());
                    intent.putExtra("type", type);
                    intent.putExtra("leader", true);
                    intent.putExtra("partId", id);
                    intent.putExtra("title", type == 0 ? "仪器检测" : "隐患排查");
                    intent.putExtra("sts", data.get(arg2).getStatus());
                    intent.putExtra("SUMMARY", data.get(arg2).getSummray());
                    intent.putExtra("INSTTYPE", data.get(arg2).getType());
                    intent.putExtra("TITLE", data.get(arg2).getName());
                    intent.putExtra("SPECLD", data.get(arg2).getSpecId());
                    AppConfig.prefs.edit().putString("checkPartId", id).apply();
                    AppConfig.prefs.edit().putString("checkPartName", chooseTxt.getText().toString()).apply();
                    startActivityForResult(intent, 1001);
                } else {
                    Toast.makeText(ChouChaActivity.this, "正在上传，请稍后", Toast.LENGTH_SHORT).show();
                }
            }
        });

        final android.os.Handler handler = new android.os.Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0:
                        com.orhanobut.logger.Logger.d("choucha update");
                        getDeviceList(type, pageIndex);
                        AppConfig.uploadState = 0;
                        radioButton1.setEnabled(true);
                        radioButton2.setEnabled(true);

                        break;
                    case 1:
//                        radioButton1.setEnabled(false);
//                        radioButton2.setEnabled(false);

                        break;
                }
            }
        };
        task = new TimerTask() {
            @Override
            public void run() {
                //     com.orhanobut.logger.Logger.d("state:" + AppConfig.uploadState);
                if (AppConfig.uploadState == 2) {
                    handler.sendEmptyMessage(0);

                } else if (AppConfig.uploadState == 1) {
                    handler.sendEmptyMessage(1);
                }
            }
        };

        timer = new Timer();
        timer.schedule(task, 1000, 3000);
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.back:
                com.orhanobut.logger.Logger.d("devicecount" + getDeviceCount());
                if (getUnCheckedDevicesCount() == 0 & getDeviceCount() > 0) {
                    AppConfig.prefs.edit().putBoolean("canCheck", false).commit();
                }
                finish();
                break;
            case R.id.btn_search:
                search();
                break;
            case R.id.group_choose:

                if (id.equals("0")) {
//                    if (!AppConfig.prefs.getString("checkPartId", "").equals("")) {
//                        String name = AppConfig.prefs.getString("checkPartName", "");
//                        Toast.makeText(this, "请先完成" + name + "班组的抽查", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
                } else {
                    if (id == AppConfig.prefs.getString("checkPartId", "")) {
                        String name = AppConfig.prefs.getString("checkPartName", "");
                        Toast.makeText(this, "请先完成" + name + "班组的抽查", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                int userType = AppConfig.prefs.getInt("station", -1);
                selectBanZu(userType);
                break;
            case R.id.otherbtn:

//			if (AppConfig.prefs.getInt("station", 0) == 4 ) {
//				startActivity(new Intent(this, OtherUnusualActivity.class));
//			} else {

                if (station == 4) {
                    otherpop = new OtherPop(id);
                    //设置popupWindow,当点击popupWindow外面的时候可以消失

                    otherpop.show();

                } else {

                    Toast toast = Toast.makeText(ChouChaActivity.this, "请选择班组",
                            Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    toast.show();
//				}
                }
                break;
            default:
                break;
        }
    }

    /**
     * 是不是檢查完了
     */
    // private long isChecke() {
    // DeviceDataBase dbs = new DeviceDataBase(this);
    // int userId = AppConfig.prefs.getInt("userId", 0);
    // int partId = AppConfig.prefs.getInt("checkedId", -1);
    // long count = AppConfig.prefs.getInt("station", 0) == 0 ? dbs.count(0,
    // userId, partId) : dbs.count(10,
    // userId, partId);
    // return count;
    // }
    private void selectBanZu(int userType) {
        switch (userType) {
            case 3:
                final GongDuanPop gd = new GongDuanPop(getApplicationContext());
                gd.setWidth(LayoutParams.MATCH_PARENT);
                gd.setHeight(LayoutParams.MATCH_PARENT);
                gd.setFocusable(true);
                // gd.setAnimationStyle(R.style.Gon
                // gDuanPop);
                gd.showAsDropDown(findViewById(R.id.chooseLayout), 0, 13);
                gd.setOnDismissListener(new OnDismissListener() {

                    @Override
                    public void onDismiss() {
                        if (gd.getName() != null) {
                            String tmp = AppConfig.prefs.getString("checkPartId", "");
                            if (!gd.getBanZuId().equals(tmp) & !tmp.equals("")) {
                                String name = AppConfig.prefs.getString("checkPartName", "");
                                Toast.makeText(ChouChaActivity.this, "请先完成" + name + "班组的抽查", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            id = gd.getBanZuId();
                            SharedPreferences prfs = getSharedPreferences(
                                    AppConfig.prfsName, MODE_MULTI_PROCESS);
                            String partIds = prfs.getString("checkedId", "");
                            if (!partIds.contains(id + "")) {
                                partIds = partIds + "," + id;
                                prfs.edit().putString("checkedId", partIds)
                                        .commit();
                            }
                            com.orhanobut.logger.Logger.d("station" + id);
                            chooseTxt.setText(gd.getName());
                            getDeviceList(type, pageIndex);
                            station = gd.getStation();

                        }
                    }
                });
                break;
            case 2:
                final CheJianPop cj = new CheJianPop(getApplicationContext());
                cj.setWidth(LayoutParams.MATCH_PARENT);
                cj.setHeight(LayoutParams.MATCH_PARENT);
                cj.setFocusable(true);
                cj.showAsDropDown(findViewById(R.id.chooseLayout));
                cj.setOnDismissListener(new OnDismissListener() {

                    @Override
                    public void onDismiss() {
                        if (cj.getName() != null) {
                            String tmp = AppConfig.prefs.getString("checkPartId", "");
                            if (!cj.getBanZuId().equals(tmp) & !tmp.equals("")) {

                                String name = AppConfig.prefs.getString("checkPartName", "");
                                Toast.makeText(ChouChaActivity.this, "请先完成" + name + "班组的抽查", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            id = cj.getBanZuId();
                            SharedPreferences prfs = getSharedPreferences(
                                    AppConfig.prfsName, MODE_MULTI_PROCESS);
                            String partIds = prfs.getString("checkedId", "");
                            if (!partIds.contains(id + "")) {
                                partIds = partIds + "," + id;
                                prfs.edit().putString("checkedId", partIds)
                                        .commit();
                            }
                            chooseTxt.setText(cj.getName());
                            getDeviceList(type, pageIndex);
                            station = cj.getStation();
                        }
                    }
                });
                break;
            case 0:
            case 1:
                final GongChangPop gc = new GongChangPop(getApplicationContext());
                gc.setWidth(LayoutParams.MATCH_PARENT);
                gc.setHeight(LayoutParams.MATCH_PARENT);
                gc.setFocusable(true);
                gc.showAsDropDown(findViewById(R.id.chooseLayout));
                gc.setOnDismissListener(new OnDismissListener() {

                    @Override
                    public void onDismiss() {
                        if (gc.getName() != null) {
                            String tmp = AppConfig.prefs.getString("checkPartId", "");
                            if (!gc.getBanZuId().equals(tmp) & !tmp.equals("")) {
                                String name = AppConfig.prefs.getString("checkPartName", "");
                                Toast.makeText(ChouChaActivity.this, "请先完成" + name + "班组的抽查", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            id = gc.getBanZuId();
                            SharedPreferences prfs = getSharedPreferences(
                                    AppConfig.prfsName, MODE_MULTI_PROCESS);
                            String partIds = prfs.getString("checkedId", "");
                            if (!partIds.contains(id + "")) {
                                partIds = partIds + "," + id;
                                prfs.edit().putString("checkedId", partIds)
                                        .commit();
                            }
                            com.orhanobut.logger.Logger.d("part id" + partIds);
                            getDeviceList(type, pageIndex);
                            chooseTxt.setText(gc.getName());
                            station = gc.getStation();
                        }
                    }
                });
                break;
//            case 0:
//                final GongSiPop gs = new GongSiPop(getApplicationContext());
//                gs.setWidth(LayoutParams.MATCH_PARENT);
//                gs.setHeight(LayoutParams.MATCH_PARENT);
//                gs.setFocusable(true);
//                gs.showAsDropDown(findViewById(R.id.chooseLayout));
//                gs.setOnDismissListener(new OnDismissListener() {
//
//                    @Override
//                    public void onDismiss() {
//                        if (gs.getName() != null) {
//                            id = gs.getBanZuId();
//                            SharedPreferences prfs = getSharedPreferences(
//                                    AppConfig.prfsName, MODE_MULTI_PROCESS);
//                            String partIds = prfs.getString("checkedId", "");
//                            if (!partIds.contains(id + "")) {
//                                partIds = partIds + "," + id;
//                                prfs.edit().putString("checkedId", partIds)
//                                        .commit();
//                            }
//                            station = gs.getStation();
//                            getDeviceList(type, pageIndex);
//                            chooseTxt.setText(gs.getName());
//                        }
//                    }
//                });
//                break;
            default:
                break;
        }

    }

    private void getDeviceList(int type, int pageIndex) {
        // 得到数据库数据
        allCount = 0;
        Logger.getInstance().e("getDeviceList", "  sssssss  " + id);
        // AppConfig.prefs.edit().putInt("checkedId", id).commit();
        data.clear();
        adapter.notifyDataSetChanged();
        DeviceDataBase db = new DeviceDataBase(getApplicationContext());
        List<DeviceParam> dbData = db.getDevices(id,
                AppConfig.prefs.getString("userId", "0"), type);
        if (dbData != null & dbData.size() > 0) {
            for (int i = 0; i < dbData.size(); i++) {
                if (dbData.get(i).getType() == type) {
                    DeviceParam param = dbData.get(i);
                    data.add(param);
                }
            }
            com.orhanobut.logger.Logger.d("从数据库中获取");
            adapter.updata(data);
            return;
        }

        Map<Object, Object> op = new HashMap<Object, Object>();
        //	op.put("USERID", AppConfig.prefs.getInt("userId", 0));
        op.put("PARTID", id);
        op.put("STATION", 4);
        // op.put("type", type);
        //op.put("pageIndex", pageIndex);
        //	op.put("pageSize", 20);
        new AsyncHttpConnection().post(AppConfig.GET_DEVICE_LIST,
                HttpMethod.getParams(getApplicationContext(), op),
                new CallbackListener() {

                    @Override
                    public void callBack(String result) {
                        com.orhanobut.logger.Logger.d("从网络中获取");

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

    private void jsonDeviceList(JSONObject obj) {
        unCheckCount = 1;
        try {
            data.clear();

            JSONArray array = obj.getJSONArray("data");
            List<DeviceParam> list = new ArrayList<DeviceParam>();
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                String deviceId = object.getString("ID");
                int status = object.getInt("STATUS");
                String name = object.getString("NAME");
                String specId = object.getString("SPECLD");
                String summary = object.getString("SUMMARY");
                int type = object.getInt("TYPE");
                DeviceParam dp = new DeviceParam(deviceId, type, name, summary,
                        specId, status);
                if (type == this.type) {
                    data.add(dp);
                }
                list.add(dp);
            }
            if (list.size() > 0) {
                DeviceDataBase db = new DeviceDataBase(getApplicationContext());
                db.clearDevice(AppConfig.prefs.getString("userId", "0"));
                for (int i = 0; i < list.size(); i++) {
                    DeviceParam param = list.get(i);
                    db.addDevice(id, param.getName(), param.getSpecId(),
                            param.getId(), param.getType(), param.getSummray(),
                            AppConfig.prefs.getString("userId", "0"),
                            param.getStatus());
                }
            }

            adapter.updata(data);


        } catch (JSONException e) {
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
        if (data == null) {
            return;
        }
        for (int i = 0; i < data.size(); i++) {
            DeviceParam param = data.get(i);
            if (param.getId().equals(deviceId)) {
                param.setStatus(status);
                DeviceDataBase db = new DeviceDataBase(getApplicationContext());
                db.updateStatus(deviceId, status,
                        AppConfig.prefs.getString("userId", "0"));
            }
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
        // if (getCount() == 0) {// 巡查完毕就开始上传
        // Intent intent = new Intent(this, SendService.class);
        // intent.putExtra(SendService.START_TAG, SendService.RECOEDS_TAG);
        // startService(intent);
        // }
        // if (otherpop != null) {
        // otherpop.showAtLocation(chooseTxt, Gravity.BOTTOM, 0, 270);
        // }

    }

    // /**
    // * 检查是否全部巡检完成
    // */
    // private long getCount() {
    // DeviceDataBase dbs = new DeviceDataBase(getApplicationContext());
    // int userId = AppConfig.prefs.getInt("userId", 0);
    // long count = AppConfig.prefs.getInt("station", 0) == 0 ? dbs.count(0,
    // userId,AppConfig.prefs.getInt("checkedId", 0)) : dbs.count(10,
    // userId,AppConfig.prefs.getInt("checkedId", 0));
    //
    // return count;
    // }

    /**
     * 回调
     */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 101:
                File temp = new File(otherpop.getFilePath(), fileName);
                Logger.getInstance().e("ssssss", temp.getPath());
                if (temp.length() > 0) {
                    initGridData(temp.getPath(), otherpop.adapter);
                }
                break;
            case 1001:

                break;
            default:
                break;
        }

        if (resultCode == 1001) {
            setStatus(data.getStringExtra("deviceId"),
                    data.getIntExtra("status", 0));
            for (int i = 0; i < this.data.size(); i++) {
                if (this.data.get(i).getStatus() != 0) {
                    allCount++;
                }
            }
            //  unCheckCount = getUnCheckedDevicesCount();
//            if (getUnCheckedDevicesCount() == 0) {
//                radioButton2.setEnabled(false);
//                radioButton1.setEnabled(false);
//            }

        }


    }

    public void initGridData(String path, AddYichangGridAdapter adapter) {
        if (!path.equals("")) {
            otherpop.picHashMap.add(path);
        }
        adapter.notifyDataSetChanged();
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
    private int getUnCheckedDevicesCount() {
        int count = 0;
        DeviceDataBase db = new DeviceDataBase(getApplicationContext());
        List<DeviceParam> list = db
                .getDevices(AppConfig.prefs.getString(id, "0"), AppConfig.prefs.getString("userId", "0"));
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
     * @author longfei_deng@loongjoy.com
     * @ClassName: OtherPop
     * @Description: TODO
     * @date 2015年10月13日 上午10:24:44
     */
    class OtherPop extends PopupWindow implements OnItemClickListener {

        public List<String> picHashMap = new ArrayList<String>();
        // 文件名称
        public AddYichangGridAdapter adapter;

        private String partId = "";

        public OtherPop(final String partId) {
            View contentView = View.inflate(ChouChaActivity.this,
                    R.layout.otherpop, null);
            setContentView(contentView);


            this.partId = partId;
            final EditText editText = (EditText) contentView.findViewById(R.id.other_edt);
//			editText.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//
//				}
//			});
            String cacheData = AppConfig.prefs.getString("otherCache", "");
            if (!cacheData.equals("")) {
                String otherData[] = cacheData.split(";");
                if (otherData[0].equals(partId)) {
                    editText.setText(otherData[1]);
                    picHashMap.add(otherData[2]);
                }
            }
            // 取消
            contentView.findViewById(R.id.cancel).setOnClickListener(
                    new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            otherBtn.setClickable(true);
                            // 删除本地图片
                            deleteLocal();
                            AppConfig.prefs.edit()
                                    .putBoolean("isotheruplod", false).commit();
                            AppConfig.prefs.edit().putString("otherCache", "").commit();
                            dismiss();
                            // 设置背景颜色恢复
                            WindowManager.LayoutParams lp = getWindow()
                                    .getAttributes();
                            lp.alpha = 1f;
                            getWindow().setAttributes(lp);
                        }
                    });
            //保存
            contentView.findViewById(R.id.save).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    otherBtn.setClickable(true);
                    if (!fileName.equals("") & !editText.getText().toString().equals("")) {
                        AppConfig.prefs.edit().putString("otherCache", partId + ";" + editText.getText().toString() + ";" + picHashMap.get(0)).commit();
                        AppConfig.prefs.edit()
                                .putBoolean("isotheruplod", false).commit();
                        dismiss();
                        // 设置背景颜色恢复
                        WindowManager.LayoutParams lp = getWindow()
                                .getAttributes();
                        lp.alpha = 1f;
                        getWindow().setAttributes(lp);
                    } else {
                        Toast.makeText(ChouChaActivity.this, "请先添加文字或图片", Toast.LENGTH_SHORT).show();
                    }


                }
            });
            // 上传
            contentView.findViewById(R.id.upload).setOnClickListener(
                    new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            desc = editText.getText().toString();
                            otherBtn.setClickable(true);
                            if (!fileNsmes.equals("") & !desc.equals("")) {
                                AppConfig.prefs.edit()
                                        .putBoolean("isotheruplod", true).commit();
                                AppConfig.prefs.edit().putString("otherCache", "").commit();
                                otherUplod();
                                dismiss();
                                // 设置背景颜色恢复
                                WindowManager.LayoutParams lp = getWindow()
                                        .getAttributes();
                                lp.alpha = 1f;
                                getWindow().setAttributes(lp);
                            } else {
                                Toast.makeText(ChouChaActivity.this, "请先添加文字或图片", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
            PicGridView gridView = (PicGridView) contentView
                    .findViewById(R.id.gridview);
            adapter = new AddYichangGridAdapter(ChouChaActivity.this,
                    picHashMap, gridView, true);
            gridView.setAdapter(adapter);
            adapter.setItemListener(new ClickItemListener() {

                @Override
                public void OnClickItem(int position) {
                    shartCameraAction();
                }
            });
        }

        /**
         * 删除本地图片
         */
        protected void deleteLocal() {
            String[] s = fileNsmes.split(",");
            for (String string : s) {
                File file = new File(Environment.getExternalStorageDirectory()
                        + File.separator + AppConfig.PIC_OTHER, string);
                file.delete();
            }

        }

        /**
         * 拍照
         */
        public void shartCameraAction() {
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            fileName = "other_" + dateFormat.format(date) + ".jpg";
            //	fileNsmes = fileNsmes + fileName + ",";
            fileNsmes = fileName;
            // 下面这句指定调用相机拍照后的照片存储的路径
            i.putExtra(MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(new File(getFilePath(), fileName)));
            startActivityForResult(i, 101);
        }

        /**
         * 路径
         */
        public String getFilePath() {
            File file = new File(Environment.getExternalStorageDirectory()
                    + File.separator + AppConfig.PIC_OTHER);
            file.mkdir();
            Logger.getInstance().e("path", file.getPath());
            return file.getPath();
        }

        /**
         * 显示wingpw
         */
        public void show() {
            setWidth(550);
            setHeight(526);
            setFocusable(true);
            // 设置背景颜色变暗
//			WindowManager.LayoutParams lp = getWindow().getAttributes();
//			lp.alpha = 0.3f;
//			getWindow().setAttributes(lp);

            // setAnimationStyle(R.style.GongDuanPop);
            setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
            setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            otherpop.setOutsideTouchable(false);
            //设置监听
            otherpop.setTouchInterceptor(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                        //如果焦点不在popupWindow上，且点击了外面，不再往下dispatch事件：
                        //不做任何响应,不 dismiss popupWindow
                        otherpop.dismiss();
                        return true;
                    }
                    //否则default，往下dispatch事件:关掉popupWindow，
                    return false;
                }
            });
            showAtLocation(chooseTxt, Gravity.BOTTOM, 0, 270);
        }

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                long arg3) {
            // TODO Auto-generated method stub

            shartCameraAction();
            if (position == adapter.getPos()) {

                shartCameraAction();

            }
        }

    }

    /**
     * 上传其他异常图片
     */
    public void otherUplod() {
        Map<String, String> op = new HashMap<String, String>();
        op.put("USERID", AppConfig.prefs.getString("userId", "0"));
        //	op.put("STATION", AppConfig.prefs.getInt("station", -1));
        op.put("PARTID", id);
        op.put("DESC", desc);
        op.put("PIC", fileNsmes);

        OkHttpUtils.post().url(AppConfig.SUBMIT_OTHEREXCEPTION).params(op).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                Logger.getInstance().e("submitOtherException", e.getMessage());
            }

            @Override
            public void onResponse(String response) {
                com.orhanobut.logger.Logger.d("result   " + response);
                Logger.getInstance().e("submitOtherException", response);
                try {
                    JSONObject obj = new JSONObject(response);
                    int status = obj.getInt("status");
                    if (status == 0) {
                        jsonDeviceList(obj);
                    } else {
                        String msg = obj.getString("message");
                        Logger.getInstance().e("login error", msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        //op.put("PIC", fileNsmes.substring(0, fileNsmes.length() - 1));
//        new AsyncHttpConnection().postOnce(AppConfig.SUBMIT_OTHEREXCEPTION,
//                HttpMethod.getParams(getApplicationContext(), op),
//                new CallbackListener() {
//
//                    @Override
//                    public void callBack(String result) {
//                        com.orhanobut.logger.Logger.d("result   " + result);
//                        Logger.getInstance().e("submitOtherException", result);
//                        try {
//                            JSONObject obj = new JSONObject(result);
//                            int status = obj.getInt("status");
//                            if (status == 0) {
//                                jsonDeviceList(obj);
//                            } else {
//                                String msg = obj.getString("message");
//                                Logger.getInstance().e("login error", msg);
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });

    }

    /**
     * 搜索设备
     */
    private void search() {
        com.orhanobut.logger.Logger.d("station" + id);
        if (station == 4) {
            data.clear();
            DeviceDataBase db = new DeviceDataBase(getApplicationContext());
            List<DeviceParam> dbData = db.search(id, AppConfig.prefs.getString(
                    "userId", "0"), searchEdt.getText().toString());
            if (dbData != null && dbData.size() > 0) {
                for (int i = 0; i < dbData.size(); i++) {
                    DeviceParam param = dbData.get(i);
                    if (type == param.getType()) {
                        data.add(param);
                    }
                }
            } else {

                Toast toast = Toast.makeText(ChouChaActivity.this, "没有找到相关数据",
                        Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                toast.show();
            }
            adapter.updata(data);
        } else {


            Toast toast = Toast.makeText(ChouChaActivity.this, "请选择班组", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.show();
        }
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
