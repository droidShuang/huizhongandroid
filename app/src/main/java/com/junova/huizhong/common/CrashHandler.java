package com.junova.huizhong.common;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;


import com.junova.huizhong.AppConfig;
import com.junova.huizhong.common.http.AsyncHttpConnection;
import com.junova.huizhong.common.http.HttpMethod;
import com.junova.huizhong.widget.LoadingDialog;
import com.orhanobut.logger.Logger;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Response;


/**
 * Created by junova on 2017-01-03.
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private static final String TAG = "CrashHandler";
    private static final boolean DEBUG = true;

    private static final String PATH = Environment.getExternalStorageDirectory().getPath() + "/ryg_test/log/";
    private static final String FILE_NAME = "crash";

    //log文件的后缀名
    private static final String FILE_NAME_SUFFIX = ".txt";

    private static CrashHandler sInstance = new CrashHandler();

    //系统默认的异常处理（默认情况下，系统会终止当前的异常程序）
    private Thread.UncaughtExceptionHandler mDefaultCrashHandler;

    private Context mContext;

    //构造方法私有，防止外部构造多个实例，即采用单例模式
    private CrashHandler() {
    }

    public static CrashHandler getInstance() {
        return sInstance;
    }

    //这里主要完成初始化工作
    public void init(Context context) {
        //获取系统默认的异常处理器
        mDefaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler();
        //将当前实例设为系统默认的异常处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
        //获取Context，方便内部使用
        mContext = context.getApplicationContext();
    }

    /**
     * 这个是最关键的函数，当程序中有未被捕获的异常，系统将会自动调用#uncaughtException方法
     * thread为出现未捕获异常的线程，ex为未捕获的异常，有了这个ex，我们就可以得到异常信息。
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {

        try {

            //导出异常信息到SD卡中
            String path = dumpExceptionToSDCard(ex);
            //这里可以通过网络上传异常信息到服务器，便于开发人员分析日志从而解决bug
            uploadExceptionToServer(path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //打印出当前调用栈信息
        ex.printStackTrace();

        //     如果系统提供了默认的异常处理器，则交给系统去结束我们的程序，否则就由我们自己结束自己
//        if (mDefaultCrashHandler != null) {
//            mDefaultCrashHandler.uncaughtException(thread, ex);
//        } else {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Looper.prepare();
//
//
//                Looper.loop();
//            }
//        }).start();


        //      }

    }

    private String dumpExceptionToSDCard(Throwable ex) throws IOException {
        //如果SD卡不存在或无法使用，则无法把异常信息写入SD卡
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            if (DEBUG) {
                Log.w(TAG, "sdcard unmounted,skip dump exception");
                return "";
            }
        }

        File dir = new File(PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        long current = System.currentTimeMillis();
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(current));
        //以当前时间创建log文件
        File file = new File(PATH + FILE_NAME + time + FILE_NAME_SUFFIX);

        try {
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            //导出发生异常的时间
            pw.println(time);

            //导出手机信息
            dumpPhoneInfo(pw);

            pw.println();
            //导出异常的调用栈信息
            ex.printStackTrace(pw);
            pw.flush();
            pw.close();
            return file.getPath();
        } catch (Exception e) {
            Logger.e(e.getMessage());
            Log.e(TAG, "dump crash info failed");
        }
        return "";
    }

    private void dumpPhoneInfo(PrintWriter pw) throws PackageManager.NameNotFoundException {
        //应用的版本名称和版本号
        PackageManager pm = mContext.getPackageManager();
        PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(), PackageManager.GET_ACTIVITIES);
        pw.print("App Version: ");
        pw.print(pi.versionName);
        pw.print('_');
        pw.println(pi.versionCode);

        //android版本号
        pw.print("OS Version: ");
        pw.print(Build.VERSION.RELEASE);
        pw.print("_");
        pw.println(Build.VERSION.SDK_INT);

        //手机制造商
        pw.print("Vendor: ");
        pw.println(Build.MANUFACTURER);

        //手机型号
        pw.print("Model: ");
        pw.println(Build.MODEL);

        //cpu架构
        pw.print("CPU ABI: ");
        pw.println(Build.CPU_ABI);
    }

    private void uploadExceptionToServer(String path) throws IOException {
        //TODO Upload Exception Message To Your Web Server
        if (!TextUtils.isEmpty(path)) {
            //  OkHttpUtils.post().addFile("file", "logtext.txt", new File(path)).url(AppConfig.SUBMIT_LOG).build()
            //         OkHttpUtils.postFile().file(new File(path)).url(AppConfig.SUBMIT_LOG).build()
            String encoding = "GBK";
            File file = new File(path);
            String textContext = "";
            String lineTxt = "";
            if (file.isFile() && file.exists()) { //判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file), encoding);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);

                while ((lineTxt = bufferedReader.readLine()) != null) {
                    textContext = textContext + "\n" + lineTxt;
                }

                read.close();
            }
            Logger.d(textContext);
            final HashMap<Object, Object> op = new HashMap<>();
            final HashMap<String, String> params = new HashMap<>();
            params.put("file", textContext);
            op.put("file", textContext);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Looper.prepare();
                    new AsyncHttpConnection().post(AppConfig.SUBMIT_LOG,
                            HttpMethod.getParams(mContext, op), new AsyncHttpConnection.CallbackListener() {
                                @Override
                                public void callBack(String result) {
                                    if (!TextUtils.isEmpty(result)) {
                                        try {
                                            Logger.d(result);
                                            JSONObject jsonObject = new JSONObject(result);
                                            int status = jsonObject.getInt("status");
                                            if (status == 1) {
                                                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                                                builder.setTitle("提示");
                                                builder.setMessage("程序发生异常，点击确认关闭");
                                                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.cancel();
                                                        Process.killProcess(Process.myPid());

                                                    }
                                                });
                                                AlertDialog dialog = builder.create();

                                                dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                                                dialog.show();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            Process.killProcess(Process.myPid());
                                        }
                                    }


                                }
                            });
//                    OkHttpUtils.post().params(params).url(AppConfig.SUBMIT_LOG).build().execute(new StringCallback() {
//                        @Override
//                        public void onError(Call call, Exception e) {
//                            Logger.e(e.getMessage());
//                        }
//
//                        @Override
//                        public void onResponse(String response) {
//                            Toast.makeText(mContext, "successs", Toast.LENGTH_SHORT).show();
//                            Logger.json(response);
//                        }
//                    });
//                    try {
//                        Response response = OkHttpUtils.post().params(params).url(AppConfig.SUBMIT_LOG).build().execute();
//                        String res = response.body().string();
//                        Logger.d(res);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }

                    Looper.loop();
                }
            }).start();
//            new AsyncHttpConnection().post(AppConfig.SUBMIT_LOG,
//                    HttpMethod.getParams(mContext, op), new AsyncHttpConnection.CallbackListener() {
//                        @Override
//                        public void callBack(String result) {
//
//                            Logger.d(result);
//
//                        }
//                    });
//            Response response = OkHttpUtils.post().params(params).url(AppConfig.SUBMIT_LOG).build().execute();
//            String res = response.body().string();
//            Logger.d(res);
//            OkHttpUtils.post().params(params).url(AppConfig.SUBMIT_LOG).build().execute(new StringCallback() {
//                @Override
//                public void onError(Call call, Exception e) {
//                    Logger.e("上传日志失败", e.getMessage());
//                }
//
//                @Override
//                public void onResponse(String response) {
//                    Logger.json(response);
//                    Process.killProcess(Process.myPid());
//                    // 如果系统提供了默认的异常处理器，则交给系统去结束我们的程序，否则就由我们自己结束自己
////        if (mDefaultCrashHandler != null) {
////            mDefaultCrashHandler.uncaughtException(thread, ex);
////        } else {
////
////            Process.killProcess(Process.myPid());
////        }
//                }
//            });
        }else{
            Process.killProcess(Process.myPid());
        }

    }


}
