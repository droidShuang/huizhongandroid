/*
 * function common method 
 */
package com.junova.huizhong.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.http.util.EncodingUtils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;

import com.junova.huizhong.AppApplication;
import com.junova.huizhong.AppConfig;
import com.junova.huizhong.R;
import com.junova.huizhong.widget.LoadingDialog;
import com.junova.huizhong.widget.PullRefreshListView;

public class FunctionUtils {

    private Logger logger = Logger.getInstance();

    /**
     * get screen width
     */
    public static void getScreenSize(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay()
                .getMetrics(dm);
        AppConfig.screenWidth = dm.widthPixels;
        AppConfig.screenHeight = dm.heightPixels;
    }

    /*
     * showDialog 作用
     */
    public static LoadingDialog showDialog(Activity content) {
        try {
            LoadingDialog dialog = new LoadingDialog(content, R.style.alert_dialog);
            dialog.show();
            return dialog;
        } catch (Exception e) {
            return null;
        }


    }

    /*
     * closeDialog 作用
     */
    public static void closeDialog(LoadingDialog dialog) {
        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }

    }

    /*
     * get the file from android assets file
     */
    public static String getFileFromAssets(Context context, String fileName) {
        String result = "";
        try {
            InputStream is = context.getResources().getAssets().open(fileName);
            int lenght = is.available();
            byte[] buffer = new byte[lenght];
            is.read(buffer);
            result = EncodingUtils.getString(buffer, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /*
     * get mobile udid
     */
    public static String getUdId(Context context) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        if (tm == null) {
            return null;
        }
        return tm.getDeviceId();
    }

    /**
     * get app version name and version code
     */
    public static String getAppVersion(Context context) {
        String versionName = "0.0.0";
        int versionCode = 1;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            versionCode = pi.versionCode;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        // AppConfig.versionCode = versionCode;
        return versionName;
    }

    public static String getapiVersion() {
        return "1.1";
    }

    /**
     * get mobile model
     */
    public static String getDevice() {
        return Build.MODEL;
    }

    /**
     * get mobile system version
     */
    public static String getOs() {
        return "android" + Build.VERSION.RELEASE;
    }

    /**
     * check the string is null
     *
     * @param str
     * @return
     */
    public static boolean isBlank(String str) {
        return str == null || "".equals(str);
    }

    /**
     * get mobile phone number and replace china number
     */
    public static String getPhoneNumber(Context context) {
        TelephonyManager phoneMgr = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String tel = phoneMgr.getLine1Number();
        if (tel != null) {
            tel = tel.replace("+86", "").trim();
        }
        return tel;
    }

    /*
     * check mobile network station
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * exit the app and finish all the activity
     *
     * @param context
     */
    public static void exitApp(Context context) {
        AppApplication appApplication = (AppApplication) context
                .getApplicationContext();
        List<Activity> list = appApplication.getActivities();
        for (Activity ac : list) {
            ac.finish();
        }
        list.clear();
        appApplication.onTerminate();
    }

    // /**
    // * share
    // */
    // public static void showShare(Context context) {
    // ShareSDK.initSDK(context);
    // OnekeyShare oks = new OnekeyShare();
    // String downloadUrl = AppConfig.downloadUrl + AppConfig.appId;
    // // 关闭sso授权
    // oks.disableSSOWhenAuthorize();
    // // 分享时Notification的图标和文字
    // oks.setNotification(R.drawable.android_app_icon,
    // context.getString(R.string.app_name));
    // // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
    // oks.setTitle(context.getString(R.string.share));
    // // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
    // oks.setTitleUrl(downloadUrl);
    // // text是分享文本，所有平台都需要这个字段
    // oks.setText("我正在使用"
    // + context.getResources().getString(R.string.app_name)
    // + "，下载地址是：" + downloadUrl);
    // // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
    // //oks.setImagePath("/sdcard/test.jpg");// 确保SDcard下面存在此张图片
    // // url仅在微信（包括好友和朋友圈）中使用
    // oks.setUrl(downloadUrl);
    // // comment是我对这条分享的评论，仅在人人网和QQ空间使用
    // //oks.setComment("我是测试评论文本");
    // // site是分享此内容的网站名称，仅在QQ空间使用
    // oks.setSite(context.getString(R.string.app_name));
    // // siteUrl是分享此内容的网站地址，仅在QQ空间使用
    // oks.setSiteUrl(downloadUrl);
    // // 启动分享GUI
    // oks.show(context);
    // }

    /**
     * @param bitmap     原图
     * @param edgeLength 希望得到的正方形部分的边长
     * @return 缩放截取正中部分后的位图。
     */
    public static Bitmap centerSquareScaleBitmap(Bitmap bitmap, int edgeLength) {
        if (null == bitmap || edgeLength <= 0) {
            return null;
        }

        Bitmap result = bitmap;
        int widthOrg = bitmap.getWidth();
        int heightOrg = bitmap.getHeight();

        if (widthOrg > edgeLength && heightOrg > edgeLength) {
            // 压缩到一个最小长度是edgeLength的bitmap
            int longerEdge = edgeLength * Math.max(widthOrg, heightOrg) / Math
                    .min(widthOrg, heightOrg);
            int scaledWidth = widthOrg > heightOrg ? longerEdge : edgeLength;
            int scaledHeight = widthOrg > heightOrg ? edgeLength : longerEdge;
            Bitmap scaledBitmap;

            try {
                scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth,
                        scaledHeight, true);
            } catch (Exception e) {
                return null;
            }

            // 从图中截取正中间的正方形部分。
            int xTopLeft = (scaledWidth - edgeLength) / 2;
            int yTopLeft = (scaledHeight - edgeLength) / 2;

            try {
                result = Bitmap.createBitmap(scaledBitmap, xTopLeft, yTopLeft,
                        edgeLength, edgeLength);
                scaledBitmap.recycle();
            } catch (Exception e) {
                return null;
            }
        }

        return result;
    }

    /**
     * 转换图片成圆形
     *
     * @param bitmap 传入Bitmap对象
     * @return
     */
    public static Bitmap toRoundBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float roundPx;
        float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
        if (width <= height) {
            roundPx = width / 2;
            top = 0;
            bottom = width;
            left = 0;
            right = width;
            height = width;
            dst_left = 0;
            dst_top = 0;
            dst_right = width;
            dst_bottom = width;
        } else {
            roundPx = height / 2;
            float clip = (width - height) / 2;
            left = clip;
            right = width - clip;
            top = 0;
            bottom = height;
            width = height;
            dst_left = 0;
            dst_top = 0;
            dst_right = height;
            dst_bottom = height;
        }

        Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect src = new Rect((int) left, (int) top, (int) right,
                (int) bottom);
        final Rect dst = new Rect((int) dst_left, (int) dst_top,
                (int) dst_right, (int) dst_bottom);
        final RectF rectF = new RectF(dst);

        paint.setAntiAlias(true);

        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, src, dst, paint);
        return output;
    }

    public static void onLoad(PullRefreshListView listview) {
        long time = System.currentTimeMillis();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date refreshDate = new Date(time);
        String refreshTime = format.format(refreshDate);
        listview.stopRefresh();
        listview.stopLoadMore();
        listview.setRefreshTime(refreshTime);
    }

    /**
     * 图片合成
     *
     * @return
     */
    public static Bitmap createBitmap(Bitmap src, Bitmap watermark) {
        if (src == null) {
            return null;
        }
        int w = src.getWidth();
        int h = src.getHeight();
        int ww = watermark.getWidth();
        int wh = watermark.getHeight();
        // create the new blank bitmap
        Bitmap newb = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图
        Canvas cv = new Canvas(newb);
        // draw src into
        cv.drawBitmap(src, 0, 0, null);// 在 0，0坐标开始画入src
        // draw watermark into
        cv.drawBitmap(watermark, 0, 0, null);// 在src的右下角画入水印
        // save all clip
        cv.save(Canvas.ALL_SAVE_FLAG);// 保存
        // store
        cv.restore();// 存储
        return newb;
    }

    public static String getDate() {
        String time = String.valueOf(System.currentTimeMillis());
        String str = null;
        Date dateTemp = convertStringToDate(time);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddss");
        str = sdf.format(dateTemp);
        return str;
    }

    public static Date convertStringToDate(String date) {
        try {
            return DEFAULT_SDF.parse(date);
        } catch (ParseException e) {
        }
        return new Date();
    }

    public static String SceneList2String(List<String> SceneList)
            throws IOException {
        // 实例化一个ByteArrayOutputStream对象，用来装载压缩后的字节文件。
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        // 然后将得到的字符数据装载到ObjectOutputStream
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                byteArrayOutputStream);
        // writeObject 方法负责写入特定类的对象的状态，以便相应的 readObject 方法可以还原它
        objectOutputStream.writeObject(SceneList);
        // 最后，用Base64.encode将字节文件转换成Base64编码保存在String中
        String SceneListString = new String(Base64.encode(
                byteArrayOutputStream.toByteArray(), Base64.DEFAULT));
        // 关闭objectOutputStream
        objectOutputStream.close();
        return SceneListString;
    }

    @SuppressWarnings("unchecked")
    public static List<String> String2SceneList(String SceneListString)
            throws IOException,
            ClassNotFoundException {
        byte[] mobileBytes = Base64.decode(SceneListString.getBytes(),
                Base64.DEFAULT);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                mobileBytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(
                byteArrayInputStream);
        List<String> SceneList = (List<String>) objectInputStream
                .readObject();
        objectInputStream.close();
        return SceneList;
    }


    public final static String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public final static SimpleDateFormat DEFAULT_SDF = new SimpleDateFormat(
            DEFAULT_PATTERN);

}
