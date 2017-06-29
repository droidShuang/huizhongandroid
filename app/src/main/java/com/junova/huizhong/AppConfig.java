package com.junova.huizhong;

import android.content.Context;
import android.content.SharedPreferences;

import com.junova.huizhong.model.DeviceParam;

import org.apache.http.client.HttpClient;

import java.util.ArrayList;
import java.util.List;

public class AppConfig {
    public static boolean isDebug = true;

    public static Context context;
    public static int pageSize = 20;
    public static int gridPageSize = 15;
    public static int AllPageSize = 50;
    public static int homeGridSize = 9;
    public static int colorId = 0;
    public static String longitude = "";// 经度
    public static String latitude = ""; // 纬度
    public static String appId;
    public static String udId;
    public static String version;
    public static String apiVersion;
    public static int versionCode = 0;
    public static String device;
    public static String os;
    public static int screenWidth;
    public static int screenHeight;
    public static boolean hasNewVersion = false;
    public static String name;
    public static String url;
    public static String updateNote;
    public static String limitNum = "140";
    public static String hearNum = "20";
    public static int checkCount = 1;

    public static SharedPreferences prefs = null;
    public static SharedPreferences appConfig = null;
    public static String configName = "appconfig.xml";

    public static String prfsName = "huizhong.xml";
    public static String xunchaprfs = "xuncha.xml";
    public static HttpClient httpAsyncClient; // httpAsyncClient
    public static long currThreadId; // currThreadId
    public static int unCheckedCount;
    public static int uploadState = 0;//0未上
    // 传，1正在上传，2上传完成
    public final static String PIC_FILEDIR = "huizhongpics";
    public final static String PIC_OTHER = "huizhongother";

    public static String DATABASE = null;
    public final static String DEVICE = "deviceInfo";// 设备基础信息表名
    public final static String CHECK_ITEM = "checkItem";// 检测项表名
    public final static String ERROR_ITEM = "errorItem";// 异常项表名
    public final static String PART_ID = "part_id";// partId表

    public final static String CHECK = "checkRecords";// 检查记录表名
    public static int DATABASE_VERSION = 1;
    public final static int UN_SUMBITED = 0; // 未提交
    public final static int SUMBITED = 1; // 已提交

    public static boolean hasphoto;
    public static int position = 0;
    public static String fileName;
    public static String otherPic;
    public static List<DeviceParam> yinHuanData = new ArrayList<DeviceParam>();
    public static List<DeviceParam> yiQiData = new ArrayList<DeviceParam>();
    public static int upCount = 1;
    public static int partId;                                                   //10.124.87.208 安亭//10.124.87.188金桥//10.188.186.214junova//10.124.90.63
    public static String ip = "10.188.186.221:7001";//10.1.10.75             10.188.186.222             //new 10.124.90.63 安亭  10.124.90.86    金桥
//10.1.10.151

    public static String MyHost = "http://" + ip + "/SEWS/api/";//http://10.188.184.188:7001/SEWS/api/ http://10.188.184.191:80/SEWS/api/
    public static String IMAGEURL = "http://" + ip + "/SEWS/uploadFiles/uploadImgs//";
    public static String JUNOVAURL = "http://" + ip + "/SEWS/uploadFiles/uploadImgs//";//10.188.186.224:7001 junova       10.124.87.212:7001 汇众  10.188.186.221
    public static String HUIZHONGURL = "http://" + ip + "/SEWS/uploadFiles/uploadImgs//";   //10.188.186.224
    public static String ARTICALDOWNLOAD = "http://" + ip + "/SEWS/uploadFiles/file/";
    //http://10.188.184.225:80/SEWS/api/         huizhong       10.124.87.188:7001           172.23.20.209
    // public final static String HOST = "http://huizhong.250.com/api/";
    // public final static String HOST = "http://192.168.1.250/huizhong/api/";
    public static String UPDATA = "http://10.1.10.107:80/SEWS/api/" + "updataApp";
    public static String GET_ACTIVITY_HISTORY_URL = MyHost + "getActifityHistory";
    public static String GET_INDEX = MyHost + "getIndex";
    public static String GET_USER_INFO = MyHost + "getUserInfo";
    public static String GET_ARTICLE_LIST = MyHost + "getTypeArticle";
    public static String GET_ARTICLE_DETAIL = MyHost + "getArticleDetail";
    public static String GET_SUBLEVEL_LIST = MyHost + "getSubLevelList";
    public static String GET_DEVICE_LIST = MyHost + "getDeviceList";
    public static String GET_DEVICE_DETAIL = MyHost + "getDeviceDetail";
    public static String GET_ACTIVITY_LIST = MyHost + "getActivityList";
    public static String GET_ACTIVITY_DETAIL = MyHost + "getActivityDetail";
    public static String GET_ACCIDENT_LIST = MyHost + "getAccidentList";
    public static String UP_DATA_USER_PWD = MyHost + "updateUserPwd";
    public static String SEND_SMS_CODE = MyHost + "sendSmsCode";
    public static String GET_OTHER_DETAIL = MyHost + "getOtherDetail";
    public static String RESET_PWD = MyHost + "resetPwd";
    public static String SUBMIN_ACTIVITY = MyHost + "submitActivity";
    public static String UPLOAD_IMAGES = MyHost + "uploadImaes";
    public static String GET_BATCH_NUMBER = MyHost + "getBatchNumber";
    public static String SUBMIN_DEVICE_RECORD = MyHost + "submitDeviceRecord";
    public static String SUBMIT_OTHEREXCEPTION = MyHost + "submitOtherException";
    public static String SUBMIT_LOG = MyHost + "uploadlogfile";
    public static String ERROR_LOG = MyHost + "errorLog";
}