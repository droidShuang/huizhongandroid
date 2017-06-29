/**
 * @Title: LeaderAccidentActivity.java
 * @Package com.loongjoy.huizhong.activity
 * @Description: TODO
 * Copyright: Copyright 2015 loongjoy.inc
 * Company:上海龙照电子有限公司
 * @author hao_mo@loongjoy.com
 * @date 2015-9-1 上午10:35:17
 * @version V1.0
 */

package com.junova.huizhong.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.junova.huizhong.AppConfig;
import com.junova.huizhong.BuildConfig;
import com.junova.huizhong.R;
import com.junova.huizhong.adapter.AccidentAdapter;
import com.junova.huizhong.common.FunctionUtils;
import com.junova.huizhong.common.Logger;
import com.junova.huizhong.common.http.AsyncHttpConnection;
import com.junova.huizhong.common.http.HttpMethod;
import com.junova.huizhong.common.http.AsyncHttpConnection.CallbackListener;
import com.junova.huizhong.model.AccidentParam;
import com.junova.huizhong.widget.CheJianPop;
import com.junova.huizhong.widget.DatePop;
import com.junova.huizhong.widget.GongChangPop;
import com.junova.huizhong.widget.GongDuanPop;
import com.junova.huizhong.widget.GongSiPop;

/**
 * @author hao_mo@loongjoy.com
 * @ClassName: LeaderAccidentActivity
 * @Description: TODO 领导登录事故页面
 * @date 2015-9-1 上午10:35:17
 */

public class LeaderAccidentActivity extends Activity implements OnClickListener {
    Button back;
    TextView date;
    TextView part;
    ListView accidentList;
    AccidentAdapter adapter;
    TextView chooseBanzu;
    TextView startDate;
    TextView endDate;
    ImageView dateImg;
    PopupWindow chooseBanZu;
    DatePop pop;
    RelativeLayout dateLayout;
    String start;
    String end;
    String id = "";
    int station;
    ArrayList<AccidentParam> accidentData;
    int pageIndex = 1;

    /*
     * <p>Title: onCreate</p> <p>Description: </p>
     *
     * @param savedInstanceState
     *
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accident_leader);

        initViews();
        getDeviceList(pageIndex);
    }

    private void initViews() {
        initDate();
        back = (Button) findViewById(R.id.back);
        date = (TextView) findViewById(R.id.date);
        part = (TextView) findViewById(R.id.part);
        String string = FunctionUtils.getDate();
        String dd = getResources().getString(R.string.currentDate) + string.substring(0, 4) + "年"
                + string.substring(4, 6) + "月" + string.substring(6, 8) + "日";
        date.setText(dd);
        part.setText(getResources().getString(R.string.current_department) + AppConfig.prefs.getString("part", ""));
        accidentList = (ListView) findViewById(R.id.list_accident);
        adapter = new AccidentAdapter(getApplicationContext(), new ArrayList<AccidentParam>(), accidentList);
        accidentList.setAdapter(adapter);
        chooseBanzu = (TextView) findViewById(R.id.choose_banzu);
        dateLayout = (RelativeLayout) findViewById(R.id.date_layout);
        back.setOnClickListener(this);
        chooseBanzu.setOnClickListener(this);
        dateLayout.setOnClickListener(this);
        startDate = (TextView) findViewById(R.id.start_date);
        startDate.setText(start);
        endDate = (TextView) findViewById(R.id.end_date);
        endDate.setText(end);
        dateImg = (ImageView) findViewById(R.id.img_date);
    }

    /**
     * 初始化时间
     */
    private void initDate() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // start = end = sdf.format(date); // 当期日期
        String da = sdf.format(date);

        String str[] = da.split("-");
        String startTime = (Integer.parseInt(str[0]) - 1) + "-" + str[1] + "-" + str[2];
        end = da;
        start = startTime;
//        end = da;
//        start = (Integer.parseInt(da.split("-")[0]) - 1) + "-" + da.split("-")[1] + "-" + da.split("-")[2];
//        if (Integer.parseInt(da.split("-")[1]) == 2 && Integer.parseInt(da.split("-")[2]) > 28) {
//            start = (Integer.parseInt(da.split("-")[0]) - 1) + "-" + da.split("-")[1] + "-" + 28;
//        }
        com.orhanobut.logger.Logger.d("日期：end   " + end + "  start   " + start);
    }

	/*
     * <p>Title: onClick</p> <p>Description: </p>
	 * 
	 * @param arg0
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */

    @Override
    public void onClick(View arg0) {

        switch (arg0.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.choose_banzu:
                selectBanZu(AppConfig.prefs.getInt("station", 0));
                break;
            case R.id.date_layout:
                selectDate();
                break;
            default:
                break;
        }
    }

    private void selectDate() {
        if (pop == null) {
            pop = new DatePop(this);
            pop.setSelectedTwo();
            pop.setFocusable(true);
            pop.setWidth(LayoutParams.MATCH_PARENT);
            pop.setHeight(LayoutParams.WRAP_CONTENT);
            pop.setOnDismissListener(new OnDismissListener() {

                @Override
                public void onDismiss() {
                    if (pop.getStartDate() == "" || pop.getEndDate() == "") {

                    } else {
                        start = pop.getStartDate().substring(0, 4) + "-" + pop.getStartDate().substring(4, 6) + "-"
                                + pop.getStartDate().substring(6, pop.getStartDate().length());
                        end = pop.getEndDate().substring(0, 4) + "-" + pop.getEndDate().substring(4, 6) + "-"
                                + pop.getEndDate().substring(6, pop.getEndDate().length());
                        startDate.setText(start);
                        endDate.setText(end);
                        getDeviceList(pageIndex);

                    }
                }
            });
        }
        if (pop.isShowing()) {
            pop.dismiss();
        } else {

            pop.showAsDropDown(findViewById(R.id.layout));
        }

    }

    private void selectBanZu(int userType) {
        switch (userType) {
            case 3:
                final GongDuanPop gd = new GongDuanPop(getApplicationContext());
                if (gd != null) {
                    gd.setWidth(LayoutParams.MATCH_PARENT);
                    gd.setHeight(LayoutParams.MATCH_PARENT);
                    gd.setFocusable(true);
                    gd.showAsDropDown(findViewById(R.id.layout_leader));
                    gd.setOnDismissListener(new OnDismissListener() {

                        @Override
                        public void onDismiss() {

                            if (gd.getName() != null) {
                                id = gd.getBanZuId();
                                chooseBanzu.setText(gd.getName());
                                if (!FunctionUtils.isBlank(start) && !FunctionUtils.isBlank(end)) {
                                    getDeviceList(pageIndex);
                                }
                            }
                        }
                    });
                }
                break;
            case 2:
                final CheJianPop cj = new CheJianPop(getApplicationContext());
                if (cj != null) {
                    cj.setWidth(LayoutParams.MATCH_PARENT);
                    cj.setHeight(LayoutParams.MATCH_PARENT);
                    cj.setFocusable(true);
                    cj.showAsDropDown(findViewById(R.id.layout_leader));
                    cj.setOnDismissListener(new OnDismissListener() {

                        @Override
                        public void onDismiss() {
                            if (cj.getName() != null) {
                                chooseBanzu.setText(cj.getName());
                                id = cj.getBanZuId();
                                getDeviceList(pageIndex);
                            }
                        }
                    });
                }
                break;
            case 0:
            case 1:
                final GongChangPop gc = new GongChangPop(getApplicationContext());
                if (gc != null) {
                    gc.setWidth(LayoutParams.MATCH_PARENT);
                    gc.setHeight(LayoutParams.MATCH_PARENT);
                    gc.setFocusable(true);
                    gc.showAsDropDown(findViewById(R.id.layout_leader));
                    gc.setOnDismissListener(new OnDismissListener() {

                        @Override
                        public void onDismiss() {

                            if (gc.getName() != null) {
                                chooseBanzu.setText(gc.getName());
                                id = gc.getBanZuId();
                                getDeviceList(pageIndex);
                            }
                        }
                    });
                }
                break;
//            case 0:
//                final GongSiPop gs = new GongSiPop(getApplicationContext());
//                if (gs != null) {
//                    gs.setWidth(LayoutParams.MATCH_PARENT);
//                    gs.setHeight(LayoutParams.MATCH_PARENT);
//                    gs.setFocusable(true);
//                    gs.showAsDropDown(findViewById(R.id.layout_leader));
//                    gs.setOnDismissListener(new OnDismissListener() {
//
//                        @Override
//                        public void onDismiss() {
//                            if (gs.getName() != null) {
//                                chooseBanzu.setText(gs.getName());
//                                id = gs.getBanZuId();
//                                getDeviceList(pageIndex);
//                            }
//                            // id = gs.getBanZuId();
//                            // getDeviceList(pageIndex);
//                        }
//                    });
//                }
//                break;
            default:
                break;
        }
    }

    private void getDeviceList(int pageIndex) {
        Map<Object, Object> op = new HashMap<Object, Object>();
        op.put("PARTID", id);
        op.put("USERID", AppConfig.prefs.getString("userId", "0"));
        op.put("STATION", AppConfig.prefs.getInt("station", 0));
        //	op.put("pageIndex", pageIndex);
        //	op.put("pageSize", 20);
        op.put("STARTDATE", start);
        op.put("ENDDATE", end);
        new AsyncHttpConnection().post(AppConfig.GET_ACCIDENT_LIST, HttpMethod.getParams(getApplicationContext(), op),
                new CallbackListener() {

                    @Override
                    public void callBack(String result) {
                        Logger.getInstance().e("result", result);
                        if (result != null) {
                            try {
                                JSONObject obj = new JSONObject(result);
                                int status = obj.getInt("status");
                                if (status == 01) {
                                    jsonAccidentList(obj);
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

    private void jsonAccidentList(JSONObject obj) {
        try {
            JSONArray accidents = obj.getJSONArray("data");
            accidentData = new ArrayList<AccidentParam>();
            for (int i = 0; i < accidents.length(); i++) {
                JSONObject accident = accidents.getJSONObject(i);
                String id = accident.getString("ID");
                int level = accident.getInt("LEVE");
                String desc = accident.getString("EDESC");
                String time = accident.getString("TIME");
                String url = "";
                if (BuildConfig.DEBUG) {
                    url = AppConfig.JUNOVAURL + accident.getString("URL");
                    com.orhanobut.logger.Logger.e(url);
                } else {
                    url = AppConfig.HUIZHONGURL + accident.getString("URL");
                    com.orhanobut.logger.Logger.e(url);
                }

                accidentData.add(new AccidentParam(desc, level, url, id, time));
            }
            adapter.updata(accidentData);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
