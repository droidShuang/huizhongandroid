package com.junova.huizhong.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.util.Base64;
import android.util.Log;

import com.junova.huizhong.AppApplication;
import com.junova.huizhong.AppConfig;
import com.junova.huizhong.activity.MainActivity;
import com.junova.huizhong.activity.XunChaActivity;
import com.junova.huizhong.common.Base64Coder;
import com.junova.huizhong.common.FunctionUtils;
import com.junova.huizhong.common.Logger;
import com.junova.huizhong.common.http.AsyncHttpConnection;
import com.junova.huizhong.common.http.AsyncHttpConnection.CallbackListener;
import com.junova.huizhong.common.http.HttpMethod;
import com.junova.huizhong.db.CheckDataBase;
import com.junova.huizhong.db.DeviceDataBase;
import com.junova.huizhong.model.DeviceParam;
import com.junova.huizhong.model.UpLoadCheckModel;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * @author longfei_deng@loongjoy.com
 * @ClassName: SendService
 * @Description: TODO
 * @date 2015年9月14日 下午4:45:43
 */

public class SendService extends Service {

    private static final String TAG = "SendService";
    public static final int RECOEDS_TAG = 1;
    public static final int PIC_TAG = 2;
    public static final String START_TAG = "tag";

    public String PARTID = "0";

    public String recordId;
    public int WHAT = 0;
    public int TIME = 10000;

    JSONArray upArray;

    List<DeviceParam> data;
    Context context;
    private int type;
    //  private SharedPreferences prfs;

    @Override
    public void onCreate() {
        Log.i(TAG, "SendService-onCreate");
        context = this;
        if (handler.hasMessages(0) || handler.hasMessages(1)
                || handler.hasMessages(3)) {

        } else {
            handler.sendEmptyMessageDelayed(WHAT, 0);
        }
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        flags = START_STICKY;
        /*
         * 这里返回状态有三个值，分别是:
		 * 1、START_STICKY：当服务进程在运行时被杀死，系统将会把它置为started状态，但是不保存其传递的Intent对象
		 * ，之后，系统会尝试重新创建服务;
		 * 2、START_NOT_STICKY：当服务进程在运行时被杀死，并且没有新的Intent对象传递过来的话，
		 * 系统将会把它置为started状态， 但是系统不会重新创建服务，直到startService(Intent intent)方法再次被调用;
		 * 3、START_REDELIVER_INTENT：当服务进程在运行时被杀死，它将会在隔一段时间后自动创建，
		 * 并且最后一个传递的Intent对象将会再次传递过来。
		 */
        // int tag = intent.getIntExtra(START_TAG, -1);
        // Logger.getInstance().e("tag", tag);
        // if (tag == RECOEDS_TAG) {
        // getBatchNumber();// 获取记录ID 准备上传记录
        // } else if (tag == PIC_TAG) {
        // upLoadPic(getPath());// 上传图片
        // }
        if (handler.hasMessages(0) || handler.hasMessages(1)) {

        } else {
            handler.sendEmptyMessageDelayed(WHAT, 0);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "SendService-onBind");
        return null;
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "SendService-onDestroy");
        Intent localIntent = new Intent();
        localIntent.setClass(this, SendService.class); // 销毁时重新启动Service
        this.startService(localIntent);
    }

    private void upLoadPic(ArrayList<String> pathList) {
        for (final String path :
                pathList) {

            if (path == null || "".equals(path)) {
                WHAT = 0;
                return;
            }

            HashMap<String, String> map = new HashMap<String, String>();
            File file = new File(path);
            map.put("FILENAME", file.getName());
            map.put("IMAGEURL", Base64.encodeToString(getSmallBitmap(path), Base64.DEFAULT));//Base64Coder.encode(getSmallBitmap(path)
            OkHttpUtils.post().url(AppConfig.UPLOAD_IMAGES).params(map).build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e) {
                    if (e != null) {

                        com.orhanobut.logger.Logger.e("上传图片异常" + e.getMessage());
                    } else {
                        com.orhanobut.logger.Logger.e("上传图片异常gang");
                    }
                }

                @Override
                public void onResponse(String response) {
                    Logger.getInstance().e("handle ing", "  图片上传  " + response);
                    com.orhanobut.logger.Logger.e("图片上传" + response);
                    if (response != null && !"fail".equals(response)) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            int status = obj.getInt("status");
                            if (status == 01) {
                                com.orhanobut.logger.Logger.e("上传图片成功");
                                File file = new File(path);
                                file.delete();
                                return;
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
            });
//            List<NameValuePair> params = HttpMethod.getParams(
//                    getApplicationContext(), map);
//            AsyncHttpConnection connection = new AsyncHttpConnection();
//
//            connection.postOnce(AppConfig.UPLOAD_IMAGES, params,
//                    new CallbackListener() {
//                        @Override
//                        public void callBack(String result) {
//                            Log.d("YANSGHUANG", "callBack: " + result);
//                            com.orhanobut.logger.Logger.e(result);
//                            if (result != null && !"fail".equals(result)) {
//                                try {
//                                    JSONObject obj = new JSONObject(result);
//                                    int status = obj.getInt("status");
//                                    if (status == 01) {
//                                        File file = new File(path);
//                                        file.delete();
//                                        return;
//                                    }
//                                } catch (JSONException e) {
//                                    // TODO Auto-generated catch block
//                                    e.printStackTrace();
//                                }
//                            }
//                        }
//                    });
        }


    }

    public byte[] getSmallBitmap(String filePath) {
        Bitmap photo = BitmapFactory.decodeFile(filePath);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        photo.compress(CompressFormat.JPEG, 100, stream);
        byte[] data = stream.toByteArray();
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(data, 0, data.length, options);

        options.inSampleSize = calculateInSampleSize(options, 300, 300);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        Bitmap map = BitmapFactory.decodeByteArray(data, 0, data.length,
                options);
        ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
        map.compress(CompressFormat.JPEG, 100, stream1);

        return stream1.toByteArray();
    }

    // 计算图片的缩放值
    public int calculateInSampleSize(BitmapFactory.Options options,
                                     int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    /**
     * 遍历图片文件夹，通过传入的 getPath 作用 TODO(描述)
     *
     * @param @return
     * @return String
     * @throws
     * @Title: getPath
     * @Description: TODO
     * @author hao_mo@loongjoy.com
     */
    private ArrayList<String> getPath() {

        File file = new File(Environment.getExternalStorageDirectory()
                + File.separator + AppConfig.PIC_FILEDIR);
        File[] list = file.listFiles();
        ArrayList<String> path = new ArrayList<>();
        if (list != null && list.length != 0) {
            for (int i = 0; i < list.length; i++) {
                File firstFile = list[i];
                String name = firstFile.getName();


                CheckDataBase db = new CheckDataBase(getApplicationContext());
                if (db.isNeedUpload(name)) {
                    path.add(firstFile.getPath());
                }

            }
            return path;

        } else {
            return null;
        }
    }


    private void submitRecord(JSONArray array) {
        Map<String, String> op = new HashMap<String, String>();
        op.put("USERID", AppConfig.prefs.getString("userId", "0"));
        op.put("STATION", AppConfig.prefs.getInt("station", 0) + "");
        op.put("PATROLRECORD_ID", AppConfig.prefs.getString("uuid", "0"));
        //抽查
        if (AppConfig.prefs.getInt("station", 0) < 4) {
            String partIds[] = AppConfig.prefs.getString("checkedId", "").split(",");

            op.put("PARTID", partIds[partIds.length - 1]);
            op.put("TYPE", 1 + "");
        } else {
            //巡查
            op.put("TYPE", 0 + "");
            op.put("PARTID", PARTID);
        }
        //op.put("PARTID", PARTID);


        if (array.length() == 0) {
            WHAT = 0;
            DeviceDataBase db = new DeviceDataBase(getApplicationContext());
            db.updateStatus(0, AppConfig.prefs.getString("userId", "0"));

            return;
        }
        op.put("DEVICERECORD", array.toString());
          AppConfig.prefs.edit().putBoolean("canCheck", false).commit();
        OkHttpUtils.post().url(AppConfig.SUBMIN_DEVICE_RECORD).params(op).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                WHAT = 0;
                AppConfig.uploadState = 0;
            }

            @Override
            public void onResponse(String response) {
                if (response != null) {
                    try {
                        com.orhanobut.logger.Logger.json(response);
                        JSONObject obj = new JSONObject(response);
                        int status = obj.getInt("status");
                        if (status == 01) {

                            AppConfig.prefs.edit().putBoolean("canCheck", true).commit();
                            AppConfig.prefs.edit().putBoolean("canUp", false).commit();
//                                    if (obj.getString("message").contains("数据已存在")) {
//                                        return;
//                                    }

                            onSubmitSuccess();
                        }
                    } catch (JSONException e) {
                        WHAT = 0;
                        AppConfig.uploadState = 0;
                        e.printStackTrace();
                    }
                } else {
                    AppConfig.uploadState = 0;
                    WHAT = 0;
                }
            }
        });
//        new AsyncHttpConnection().post(AppConfig.SUBMIN_DEVICE_RECORD,
//                HttpMethod.getParams(getApplicationContext(), op),
//                new CallbackListener() {
//                    @Override
//                    public void callBack(String result) {
//                        com.orhanobut.logger.Logger.e("result"+result);
//                        Logger.getInstance().e("result", result);
//                        if (result != null) {
//                            try {
//                                JSONObject obj = new JSONObject(result);
//                                int status = obj.getInt("status");
//                                if (status == 01) {
//
//                                    AppConfig.prefs.edit().putBoolean("canCheck", true).commit();
//                                    AppConfig.prefs.edit().putBoolean("canUp", false).commit();
////                                    if (obj.getString("message").contains("数据已存在")) {
////                                        return;
////                                    }
//
//                                    onSubmitSuccess();
//                                }
//                            } catch (JSONException e) {
//                                WHAT = 0;
//                                e.printStackTrace();
//                            }
//                        } else {
//                            WHAT = 0;
//                        }
//                    }
//                });
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
        List<DeviceParam> list = db.getDevices(AppConfig.prefs.getString("partId", "0"),
                AppConfig.prefs.getString("userId", "0"));
        for (int i = 0; i < list.size(); i++) {
            DeviceParam param = list.get(i);
            if (param.getStatus() == 0) {
                count++;
            }
        }
        Logger.getInstance().e("count", count);
        return count;
    }

    /**
     * 内循环，检查是否需要上传
     */
    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
//            if (!FunctionUtils.isNetworkAvailable(context)) {
//                return;
//            }

            switch (WHAT) {
                case 0:
                    if (AppConfig.uploadState != 1) {
                        com.orhanobut.logger.Logger.d("uploadstate 0  2");
                        if (AppConfig.prefs.getBoolean("canUp", false)) {
                            CheckDataBase db = new CheckDataBase(context);

                            DeviceDataBase dbs = new DeviceDataBase(context);
//                        prfs = getSharedPreferences(AppConfig.prfsName,
//                                MODE_MULTI_PROCESS);
                            String userId = AppConfig.prefs.getString("userId", "0");
                            String partIds = AppConfig.prefs.getString("partId", "");//checkedId
                            String[] partId = partIds.split(",");
                            PARTID = partIds;
                            Logger.getInstance().e("pa", "" + partIds);
                            if (partId != null) {
                                long count = getUnCheckedDevicesCount();

                                List<UpLoadCheckModel> upList = db
                                        .getUnSubmitedCheck(PARTID);

                                if (upList.size() != 0) {
                                    com.orhanobut.logger.Logger.d("开始上传");
                                    doUpload(upList, count, userId, PARTID);


                                }

                            }
                        }
                    }
                    // 上传其他异常图片
                    if (AppConfig.prefs.getBoolean("isotheruplod", false)) {
                        upOtherLoadPic(getOtherPath());
                        AppConfig.prefs.edit().putBoolean("isotheruplod", false).commit();
                    }
                    Logger.getInstance().e("handle ing", " doing nothing  " + WHAT);
                    handler.sendEmptyMessageDelayed(WHAT, TIME);

                    break;

                case 1:
                    Logger.getInstance().e("handle ing", " do  " + WHAT);
                    WHAT = 0;
                    handler.sendEmptyMessageDelayed(WHAT, TIME);

                    break;

                case 2:
                    Logger.getInstance().e("handle ing", " do  " + WHAT);
                    handler.sendEmptyMessageDelayed(WHAT, TIME);
                    break;
                default:
                    break;
            }
        }

    };

    /**
     * doUpload
     *
     * @param
     * @return void
     * @author xialong-long_xia@loongjoy.com
     */
    private void doUpload(List<UpLoadCheckModel> upList, long count,
                          String userId, String banid) {
        // TODO Auto-generated method stub

        if (upList.size() > 0 & count == 0 & !userId.equals("0")) {
            AppConfig.uploadState = 1;

            for (int i = 0; i < upList.size(); i++) {
                if (!upList.get(i).getRecordsId().equals("")) {
                    recordId = upList.get(i).getRecordsId();

                    break;
                } else {

                    recordId = null;
                }

            }


            upArray = new JSONArray();

            if (recordId != null) {
                for (int j = 0; j < upList.size(); j++) {

                    if (recordId.equals(upList.get(j).getRecordsId())) {

                        ///
                        JSONObject object = new JSONObject();
                        try {
                            object.put("ID", upList.get(j).getDeviceId());
                            object.put("CHECKID", upList.get(j).getCheckId());
                            if (upList.get(j).getStatus() == 2) {
                                object.put("STATUS", 0);
                            } else if (upList.get(j).getStatus() == 1) {
                                object.put("STATUS", upList.get(j).getStatus());
                            }

                            object.put("NAME", upList.get(j).getName());
                            object.put("ERRORID", upList.get(j).getErrorId());
                            object.put("PIC", upList.get(j).getPics());
                            object.put("PATROLRECORDDETAILID", upList.get(j).getPartRecordId());
                            object.put("DETTYPE", upList.get(j).getType());
                            object.put("ERRODESC", upList.get(j).getErroDesc());
                            upArray.put(object);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }

                if (upArray.length() > 0) {

                    WHAT = 1;
                    AppConfig.uploadState = 1;
                    submitRecord(upArray);
                    //上传图片
                    if (getPath() != null) {
                        upLoadPic(getPath());
                    }

                }


//                Date curdate = new Date();
//                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//                int checkCount = AppConfig.prefs.getInt(sdf.format(curdate), 1) + 1;
//                AppConfig.prefs.edit().putInt(sdf.format(curdate), checkCount).commit();

            } else {
                if (getUnCheckedDevicesCount() == 0) {
                    //     WHAT = 2;
                    WHAT = 0;

                }

                Logger.getInstance().e("handle ing",
                        " do  " + WHAT + "getBatchNumber ");
            }

        } else {
            AppConfig.uploadState = 0;
        }
    }

    /**
     * 便利其他异常
     */
    private String getOtherPath() {
        File file = new File(Environment.getExternalStorageDirectory()
                + File.separator + AppConfig.PIC_OTHER);
        File[] list = file.listFiles();
        if (list != null && list.length != 0) {
//            for (int i = 0; i < list.length; i++) {
//                File firstFile = list[i];
//                return firstFile.getPath();
//            }
//
//            return null;
            int i = list.length - 1;
            return list[i].getPath();

        } else {
            return null;
        }
    }

    /**
     * 上传其他异常图片
     */
    private void upOtherLoadPic(final String path) {
        if (path == null || "".equals(path)) {
            WHAT = 0;
            return;
        }

        HashMap<String, String> map = new HashMap<String, String>();
        File file = new File(path);
        map.put("FILENAME", file.getName());
        map.put("IMAGEURL", Base64Coder.encodeLines(getSmallBitmap(path)));
//        List<NameValuePair> params = HttpMethod.getParams(
//                getApplicationContext(), map);
        OkHttpUtils.post().url(AppConfig.UPLOAD_IMAGES).params(map).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                if (response != null && !"fail".equals(response)) {
                    try {
                        JSONObject obj = new JSONObject(response);
                        int status = obj.getInt("status");
                        if (status == 0) {
                            File file = new File(path);
                            file.delete();
                            AppConfig.prefs.edit().putBoolean("isotheruplod", false).commit();
                            //      upLoadPic(getOtherPath());
                            return;
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        });
//        AsyncHttpConnection connection = new AsyncHttpConnection();
//        connection.postOnce(AppConfig.UPLOAD_IMAGES, params,
//                new CallbackListener() {
//                    @Override
//                    public void callBack(String result) {
//                        Logger.getInstance().e("otherpicresult", result);
//                        if (result != null && !"fail".equals(result)) {
//                            try {
//                                JSONObject obj = new JSONObject(result);
//                                int status = obj.getInt("status");
//                                if (status == 0) {
//                                    File file = new File(path);
//                                    file.delete();
//                                    AppConfig.prefs.edit().putBoolean("isotheruplod", false).commit();
//                                    //      upLoadPic(getOtherPath());
//                                    return;
//                                }
//                            } catch (JSONException e) {
//                                // TODO Auto-generated catch block
//                                e.printStackTrace();
//                            }
//                        }
//                        //   upLoadPic(getOtherPath());
//                    }
//                });

    }

    //上传批次码
    private void uploadPatchNumber() {
        HashMap<Object, Object> map = new HashMap<Object, Object>();
        List<NameValuePair> params = HttpMethod.getParams(
                getApplicationContext(), map);
        AsyncHttpConnection connection = new AsyncHttpConnection();
        connection.postOnce("", params, new CallbackListener() {
            @Override
            public void callBack(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getInt("status") == 01) {
                        onSubmitSuccess();
                    } else {
                        CheckDataBase db = new CheckDataBase(context);

                        DeviceDataBase dbs = new DeviceDataBase(context);
                        AppConfig.prefs = getSharedPreferences(AppConfig.prfsName,
                                MODE_MULTI_PROCESS);
                        String userId = AppConfig.prefs.getString("userId", "0");
                        String partIds = AppConfig.prefs.getString("partId", "");//checkedId
                        String[] partId = partIds.split(",");
                        PARTID = partIds;
                        Logger.getInstance().e("pa", "" + partIds);
                        if (partId != null) {
                            long count = getUnCheckedDevicesCount();

                            List<UpLoadCheckModel> upList = db
                                    .getUnSubmitedCheck(PARTID);

                            if (upList.size() != 0) {

                                doUpload(upList, count, userId, PARTID);


                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void onSubmitSuccess() {
        AppConfig.upCount++;
        CheckDataBase db = new CheckDataBase(getApplicationContext());
        DeviceDataBase dbs = new DeviceDataBase(getApplicationContext());

        db.clear();
        dbs.clear();
        WHAT = 0;
        Date curdate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        AppConfig.prefs.edit().putInt(sdf.format(curdate), AppConfig.prefs.getInt(sdf.format(curdate), 1) + 1).commit();
        AppConfig.prefs.edit().putString("uuid", UUID.randomUUID().toString().replace("-", "")).commit();
        AppConfig.prefs.edit().putStringSet("checkDevice", new HashSet<String>()).commit();
        AppConfig.prefs.edit().putString("checkPartId", "").apply();
        AppConfig.prefs.edit().putString("checkPartName", "").apply();

        if (AppConfig.prefs.getInt("station", 0) <= 0) {

            SharedPreferences prfs = getSharedPreferences(
                    AppConfig.xunchaprfs,
                    MODE_MULTI_PROCESS);
            String uid = AppConfig.prefs.getString(
                    "userId", "0");
            String count = prfs.getString(
                    "counts" + uid, "1") + "1";
            prfs.edit()
                    .putString("counts" + uid, count)
                    .commit();
            // 重新给首页添加数据
            for (Activity i : ((AppApplication) getApplication())
                    .getActivities()) {
                com.orhanobut.logger.Logger.d("test" + i.getClass().getName());
                if (i != null) {
                    if (i.getClass() == MainActivity.class) {
                        ((MainActivity) i).xunChaNum
                                .setText(""
                                        + getUnCheckedDevicesCount());
                    }
                    if (i.getClass() == XunChaActivity.class) {

                        ((XunChaActivity) i).times.setText("（第"
                                + prfs.getString(
                                "counts"
                                        + uid,
                                "1") + "次）");
                        ((XunChaActivity) i).getDeviceList(0, 1);
                        ((XunChaActivity) i).childAt.setChecked(true);
                    }

                }
            }
        }


        Intent i = new Intent("com.junova.huizhong");
        i.putExtra("type", 2);
        sendBroadcast(i);
        AppConfig.uploadState = 2;
        WHAT = 0;
    }


}
