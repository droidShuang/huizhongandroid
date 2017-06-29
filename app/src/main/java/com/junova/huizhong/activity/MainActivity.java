package com.junova.huizhong.activity;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.junova.huizhong.AppApplication;
import com.junova.huizhong.AppConfig;
import com.junova.huizhong.R;
import com.junova.huizhong.adapter.CalendarAdapter;
import com.junova.huizhong.adapter.MainNewsAdapter;
import com.junova.huizhong.common.Logger;
import com.junova.huizhong.common.http.AsyncHttpConnection;
import com.junova.huizhong.common.http.AsyncHttpConnection.CallbackListener;
import com.junova.huizhong.common.http.HttpMethod;
import com.junova.huizhong.db.DeviceDataBase;
import com.junova.huizhong.model.Banzhu;
import com.junova.huizhong.model.DeviceParam;
import com.junova.huizhong.model.GreenCross;
import com.junova.huizhong.model.NewsParam;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

public class MainActivity extends Activity implements OnClickListener {
    TextView date;
    TextView temperature;
    TextView weatherText;
    TextView name;
    TextView phone;
    TextView buMen;
    TextView yearAndMonth;
    TextView banzhu_green;
    ViewPager viewpager;
    LinearLayout dot_layout;
    TextView noAccident;
    ImageView moreNews;
    ListView newsList;
    private RelativeLayout userinfo;
    private RelativeLayout xunCha;
    private RelativeLayout btHistory;
    public TextView xunChaNum;
    private RelativeLayout active;
    TextView activeNum;
    private RelativeLayout accident;
    LinearLayout leader;
    MainNewsAdapter listAdapter;
    private CalendarAdapter cAdapter;
    private int year_c = 0;
    private int month_c = 0;
    private int day_c = 0;
    private String currentDate = "";
    private int station; // 职位
    private String safeDay;
    private Timer timer;
    private TimerTask task;
    private ImageView ivLogo;
    long countUncheck;
    Dialog updataDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
//        String ip = AppConfig.prefs.getString("currentIp", "");
//        if (!ip.equals(AppConfig.ip)) {
//            AppConfig.ip = ip;
//            AppConfig.MyHost = "http://" + ip + "/SEWS/api/";//http://10.188.184.188:7001/SEWS/api/ http://10.188.184.191:80/SEWS/api/
//            AppConfig.IMAGEURL = "http://" + ip + "/SEWS/uploadFiles/uploadImgs//";
//            updataCongig(AppConfig.MyHost);
//        }
        Calendar();
        initData();
        //  getIndex();
        ((AppApplication) getApplication()).addActivity(this);
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private void initViews() {
        dot_layout = (LinearLayout) findViewById(R.id.dot_layout);
        viewpager = (ViewPager) findViewById(R.id.view_pager);
        viewpager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                updateIntroAndDot();
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub

            }
        });
        ivLogo = (ImageView) findViewById(R.id.logo);
        //  Glide.with(this).load(R.drawable.logogif).into(ivLogo);
        banzhu_green = (TextView) findViewById(R.id.banzhu_green);
        date = (TextView) findViewById(R.id.text_date);
        temperature = (TextView) findViewById(R.id.text_temp);
        weatherText = (TextView) findViewById(R.id.text_weather);
        name = (TextView) findViewById(R.id.main_name);
        name.setText(getResources().getString(R.string.name)
                + AppConfig.prefs.getString("realname", "") + "(" + AppConfig.prefs.getString("numberCode", "") + ")");
        RelativeLayout userLayout = (RelativeLayout) findViewById(R.id.userinfo);
        userLayout.setOnClickListener(this);
        phone = (TextView) findViewById(R.id.phone);
        phone.setText(getResources().getString(R.string.phone)
                + AppConfig.prefs.getString("phone", ""));
        buMen = (TextView) findViewById(R.id.bumen);
        buMen.setText(getResources().getString(R.string.department)
                + AppConfig.prefs.getString("part", ""));
        buMen.post(new Runnable() {
            @Override
            public void run() {
                if (buMen.getLineCount() > 3) {
                    buMen.setTextSize(15);
                }

            }
        });
        yearAndMonth = (TextView) findViewById(R.id.yearAndMonth);
        noAccident = (TextView) findViewById(R.id.text_wushigu);
        // leader = (LinearLayout) findViewById(R.id.leader);
        moreNews = (ImageView) findViewById(R.id.image_more);
        moreNews.setOnClickListener(this);
        newsList = (ListView) findViewById(R.id.list_wenzhang);
        listAdapter = new MainNewsAdapter(getApplicationContext(),
                new ArrayList<NewsParam>());
        newsList.setAdapter(listAdapter);
        newsList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                Intent intent = new Intent(MainActivity.this,
                        NewsDetailAcyivity.class);
                intent.putExtra("id", newsDate.get(arg2).getId());
                startActivity(intent);
            }

        });
        TextView xunchaText = (TextView) findViewById(R.id.text_xuncha);
        xunCha = (RelativeLayout) findViewById(R.id.layout_xuncha);
        xunCha.setOnClickListener(this);
        xunChaNum = (TextView) findViewById(R.id.text_xuncha_num);
        btHistory = (RelativeLayout) findViewById(R.id.text_history);
        btHistory.setOnClickListener(this);
        active = (RelativeLayout) findViewById(R.id.text_active);
        active.setOnClickListener(this);
        activeNum = (TextView) findViewById(R.id.text_active_num);
        accident = (RelativeLayout) findViewById(R.id.text_accident);
        accident.setOnClickListener(this);
        userinfo = (RelativeLayout) findViewById(R.id.userinfo);
        userinfo.setOnClickListener(this);

        if (AppConfig.prefs.getInt("station", -1) <= 3) {
            xunchaText.setText(getResources().getText(R.string.choucha));
            xunChaNum.setVisibility(View.INVISIBLE);
            activeNum.setVisibility(View.INVISIBLE);
        } else {
            xunchaText.setText(getResources().getText(R.string.xuncha));
        }

        Date da = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");

        String we = getWeekenday(da.getDay());
        we += format.format(da);
        date.setText(we);

    }

    private void downloadApp(String url) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = View.inflate(this, R.layout.dialog_updata, null);
        final ProgressBar pb = (ProgressBar) view.findViewById(R.id.item_pb);
        OkHttpUtils.get().url(AppConfig.ARTICALDOWNLOAD + url).build().execute(new FileCallBack(Environment.getExternalStorageDirectory().getAbsolutePath(), "安全预警" + ".apk") {
            @Override
            public void inProgress(float progress, long total) {
                pb.setProgress((int) (progress * 100));
                com.orhanobut.logger.Logger.d("progress " + progress);
            }

            @Override
            public void onError(Call call, Exception e) {

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

    /**
     * 初始化dot
     */
    private void initDots() {
        int pos;
        if (greens.size() == 0) {
            pos = 1;
        } else {
            if (greens.size() <= 5) {
                pos = greens.size();
            } else {
                pos = 5;
            }
        }
        for (int i = 0; i < pos; i++) {
            View view = new View(this);
            LayoutParams params = new LayoutParams(8, 8);
            if (i != 0) {
                params.leftMargin = 5;
            }
            view.setLayoutParams(params);
            view.setBackgroundResource(R.drawable.selector_dot);
            //    dot_layout.addView(view);
        }
    }

    /**
     * 更新文本
     */
    private void updateIntroAndDot() {
        int currentPage = viewpager.getCurrentItem();
        banzhu_green.setText(greens.get(currentPage).getTeamName());
//        for (int i = 0; i < dot_layout.getChildCount(); i++) {
//            dot_layout.getChildAt(i).setEnabled(i == currentPage);
//        }
    }

    /**
     * getWeekenday
     *
     * @param
     * @return void
     * @author xialong-long_xia@loongjoy.com
     */
    private String getWeekenday(int day) {

        // 0 = Sunday, 1 = Monday, 2 = Tuesday, 3 = Wednesday, 4 = Thursday, 5 =
        // Friday, 6 = Saturday

        String we = null;
        switch (day) {
            case 0:
                we = "周日 ";
                break;
            case 1:
                we = "周一 ";
                break;
            case 2:
                we = "周二 ";
                break;
            case 3:
                we = "周三 ";
                break;
            case 4:
                we = "周四 ";
                break;
            case 5:
                we = "周五 ";
                break;
            case 6:
                we = "周六 ";
                break;
            default:
                break;
        }
        return we;

    }

    public void initData() {
        station = AppConfig.prefs.getInt("station", 0);
        yearAndMonth.setText((year_c + "").substring(2) + "/" + month_c);
    }

    /**
     * 初始化时间
     */
    public void Calendar() {

        Date date = new Date();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
        currentDate = sdf.format(date); // 当期日期
        year_c = Integer.parseInt(currentDate.split("-")[0]);
        month_c = Integer.parseInt(currentDate.split("-")[1]);
        day_c = Integer.parseInt(currentDate.split("-")[2]);


        SharedPreferences prfs = getSharedPreferences(AppConfig.xunchaprfs,
                MODE_MULTI_PROCESS);
        if (AppConfig.prefs.getInt("station", 0) <= 0) {
            String uid = AppConfig.prefs.getString("userId", "0");
            if (!currentDate.equals(prfs.getString("date" + uid, "0"))) {
                prfs.edit().putString("date" + uid, currentDate).commit();
                prfs.edit().putString("counts" + uid, "1").commit();
            }
        }

    }

    //    Handler handler = new Handler() {
//        public void handleMessage(android.os.Message msg) {
//            if (!AppConfig.latitude.equals("")) {
//                getIndex();
//                handler.removeMessages(0);
//            } else {
//                handler.sendEmptyMessageDelayed(0, 1000);
//            }
//        }
//
//        ;
//    };
    private List<NewsParam> newsDate;
    private ArrayList<GreenCross> greens;// 班主十字架集合
    private ArrayList<ArrayList<HashMap<String, String>>> dates;// 十字日期集合
    private GridView shiziGridView;

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.image_more:
                Intent intent = new Intent(this, NewsListActivity.class);
                startActivity(intent);
                break;
            case R.id.layout_xuncha:
                if (AppConfig.prefs.getBoolean("canUp", false)) {


                    Toast toast = Toast.makeText(MainActivity.this, "请等待上传结束之后，再次进行检查", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    toast.show();
                    return;
                }
                int int1 = AppConfig.prefs.getInt("station", -1);

                Intent intent1 = new Intent();

                if (int1 <= 3 & int1 >= 0) {
                    intent1.setClass(this, ChouChaActivity.class);
                } else {
                    intent1.setClass(this, XunChaActivity.class);
                }
                startActivity(intent1);
                break;
            case R.id.text_active:
                Intent intent2 = new Intent(this, ActiveActivity.class);
                startActivity(intent2);
                break;
            case R.id.userinfo:
                Intent userIntent = new Intent(this, UserCenterActivity.class);
                startActivity(userIntent);
                break;
            case R.id.text_accident:
                int uId = AppConfig.prefs.getInt("station", -1);
                Intent intent3 = new Intent(this, AccidentActivity.class);
                if (uId <= 3 && uId >= 0) {
                    intent3 = new Intent(this, LeaderAccidentActivity.class);
                }
                startActivity(intent3);
                break;
            case R.id.text_history:
                Intent historyIntent = new Intent();
                historyIntent.setClass(this, HistoryActivity.class);
                startActivity(historyIntent);
                break;
            default:
                break;
        }
    }

    private void getIndex() {
        Map<Object, Object> op = new HashMap<Object, Object>();
        //d18a9f89e9f64f22bbb5a934799161bd
        op.put("USERID", AppConfig.prefs.getString("userId", "0"));
        // op.put("USERID", AppConfig.prefs.getString("userId", "0"));
        op.put("STATION", AppConfig.prefs.getInt("station", 0));

        new AsyncHttpConnection().post(AppConfig.GET_INDEX,
                HttpMethod.getParams(getApplicationContext(), op),
                new CallbackListener() {

                    @Override
                    public void callBack(String result) {
                        Logger.getInstance().e("result", result);

                        if (result != null) {
                            try {
                                JSONObject obj = new JSONObject(result);
                                int status = obj.getInt("status");
                                com.orhanobut.logger.Logger.json(result);
                                if (status == 01) {

                                    //AppConfig.prefs.edit().putString("getIndex", result).commit();
                                    JsonIndex(obj.getJSONObject("data"));

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

    /**
     * JsonIndex 作用 解析首页数据 TODO(描述)
     *
     * @param @param obj
     * @return void
     * @throws
     * @Title: JsonIndex
     * @Description: TODO
     * @author hao_mo@loongjoy.com
     */
    private void JsonIndex(JSONObject obj) {
        try {
            int day = 0;
            // 解析天气
            JSONObject weathers = obj.getJSONObject("weather");
            //    String week = weathers.getString("week");
            String weatherDate = weathers.getString("date");
            String weatherTemperature = weathers.getString("l_tmp") + "" + weathers.getString("h_tmp");
            String weather = weathers.getString("weather");
            temperature.setText(weatherTemperature);
            weatherText.setText(weather);
            //获取安全天数
            safeDay = obj.getString("safeDays");
            if (Integer.parseInt(safeDay) < 0) {
                safeDay = "0";
            }
            // 解析文章
            JSONArray articles = obj.getJSONArray("Article");
            newsDate = new ArrayList<NewsParam>();
            for (int i = 0; i < articles.length(); i++) {
                JSONObject article = articles.getJSONObject(i);
                String title = article.getString("TITLE");
                String summary = article.getString("SUMMARY");
                String createTime = article.getString("CREATETIME");
                String id = article.getString("ID");
                NewsParam nParam = new NewsParam(title, summary, createTime);
                nParam.setId(id);
                newsDate.add(nParam);
            }
            listAdapter.update(newsDate);

            // 解析绿十字
            JSONArray greenCross = obj.getJSONArray("greenCross");

            GreenCross gs;
            greens = new ArrayList<GreenCross>();

            for (int i = 0; i < greenCross.length(); i++) {
                JSONObject banzObject = greenCross.getJSONObject(i);

                String teamName = banzObject.getString("TEAMNAME");
                String teamId = AppConfig.prefs.getString("partId", "0");
                gs = new GreenCross();
                gs.setTeamName(teamName);
                gs.setTeamId(teamId);
                ArrayList<Banzhu> banzhuList = new ArrayList<Banzhu>();

                JSONArray dataArray = banzObject.getJSONArray("data");
                for (int k = 0; k < dataArray.length(); k++) {
                    JSONObject dataObject = dataArray.getJSONObject(k);
                    String dataString = dataObject.getString("DAT");
                    String levelString = dataObject.getString("LEVE");
                    if (levelString.equals("")) {
                        day++;
                    } else {
                        day = 0;
                    }
                    Banzhu ban = new Banzhu();
                    ban.setDate(dataString);
                    ban.setLevel(levelString);
                    banzhuList.add(ban);
                }
                gs.setBanzList(banzhuList);

                greens.add(gs);

            }
            initDots();
            if (greens.size() != 0) {
                transition();

                updateIntroAndDot();
            }
            viewpager.setAdapter(new MyPagerAdapter());
            DeviceDataBase dbs = new DeviceDataBase(getApplicationContext());
            String userId = AppConfig.prefs.getString("userId", "0");

            long count = dbs.countTotal(0, userId, AppConfig.prefs.getString("partId", "0"));
            xunChaNum.setText(String.valueOf(count));
            int activityNum = obj.getInt("activityNum");
            activeNum.setText(String.valueOf(activityNum));

            if (count == 0) {
                xunChaNum.setText(obj.getString("deviceNum"));
            } else {

                xunChaNum.setText(String.valueOf(count));
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * 转换数据
     */
    public void transition() {
        dates = new ArrayList<ArrayList<HashMap<String, String>>>();
        // ArrayList<HashMap<String, String>> dataArrayList ;
        for (int i = 0; i < greens.size(); i++) {
            GreenCross green = greens.get(i);
            ArrayList<HashMap<String, String>> dataArrayList = new ArrayList<HashMap<String, String>>();
            for (int j = 0; j < green.getBanzList().size(); j++) {
                HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("date", green.getBanzList().get(j).getDate());
                hashMap.put("level", green.getBanzList().get(j).getLevel());
                dataArrayList.add(hashMap);
            }
            dates.add(dataArrayList);
        }
        Log.e("dates", dates.toString());
    }

    /**
     * 重载
     */
    @Override
        protected void onResume() {
            super.onResume();
//        DeviceDataBase dbs = new DeviceDataBase(getApplicationContext());
//        String userId = AppConfig.prefs.getString("userId", "0");
//        long count = dbs.count(0, userId, AppConfig.prefs.getString("partId", "0"));

            //       xunChaNum.setText(String.valueOf(count));
            Map<String, String> params = new HashMap<>();
            com.orhanobut.logger.Logger.e("版本号：" + "1.6");
            params.put("versionCode",AppConfig.version+"");
            OkHttpUtils.post().url(AppConfig.UPDATA).params(params).build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "检查更新失败", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResponse(String response) {
                    if (!TextUtils.isEmpty(response)) {
                        try {
                            JSONObject json = new JSONObject(response);
                            int status = json.getInt("status");
                            if (status == 06) {
                                if (timer == null) {
                                    timer = new Timer();
                                    task = new TimerTask() {
                                        @Override
                                        public void run() {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    getIndex();
                                                    DeviceDataBase dbs = new DeviceDataBase(getApplicationContext());
                                                    String userId = AppConfig.prefs.getString("userId", "0");
                                                    long count = dbs.count(0, userId, AppConfig.prefs.getString("partId", "0"));
                                                    xunChaNum.setText(String.valueOf(count));
                                                }
                                            });

                                        }
                                    };
                                    timer.schedule(task, 1000, 180000);
                                }
                            } else if (status == 07) {
                                downloadApp(json.getString("url"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
                        Toast.makeText(MainActivity.this, "检查更新失败", Toast.LENGTH_SHORT).show();
                    }
                }
            });


            //   getIndex();

        }

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


        class MyPagerAdapter extends PagerAdapter {

            /**
             * 返回多少page
             */
            @Override
            public int getCount() {
                if (greens.size() == 0) {
                    return 1;
                } else {
                    return greens.size();
                }
            }

            /**
             * true: 表示不去创建，使用缓存 false:去重新创建 view： 当前滑动的view
             * object：将要进入的新创建的view，由instantiateItem方法创建
             */
            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            /**
             * 类似于BaseAdapger的getView方法 用了将数据设置给view 由于它最多就3个界面，不需要viewHolder
             */
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View view = View.inflate(MainActivity.this,
                        R.layout.mypageradapter_item, null);
                GridView shiziGridView = (GridView) view
                        .findViewById(R.id.grid_shizi);


                if (day_c == 1) {
                    Date tmpDate = new Date();
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(tmpDate);
                    calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 1);
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-M-d");
                    try {
                        Date newDate = format.parse(format.format(calendar.getTime()));
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
                        String tmpDateString = sdf.format(newDate); // 当期日期
                        year_c = Integer.parseInt(tmpDateString.split("-")[0]);
                        month_c = Integer.parseInt(tmpDateString.split("-")[1]);
                        day_c = Integer.parseInt(tmpDateString.split("-")[2]);

                        yearAndMonth.setText((year_c + "").substring(2) + "/" + month_c);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }
                com.orhanobut.logger.Logger.e("year "+ year_c+" month   "+month_c+" day "+day_c);
                if (greens.size() == 0) {
                    cAdapter = new CalendarAdapter(year_c, month_c, day_c,
                            MainActivity.this);
                } else {
                    cAdapter = new CalendarAdapter(year_c, month_c, day_c,
                            MainActivity.this, greens.get(position).getTeamId(), dates.get(position));
                }
                cAdapter.setCountListener(new CalendarAdapter.CountListener() {
                    @Override
                    public void onCount(int count) {
                        noAccident
                                .setText(getResources().getString(R.string.lianxu)
                                        + safeDay
                                        + getResources().getString(R.string.no_accident));
                    }
                });
                shiziGridView.setAdapter(cAdapter);
                container.addView(view);
                return view;
            }

            /**
             * 销毁page position： 当前需要消耗第几个page object:当前需要消耗的page
             */
            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }

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
}
