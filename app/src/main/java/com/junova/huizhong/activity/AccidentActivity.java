/**
 * @Title: AccidentActivity.java
 * @Package com.loongjoy.huizhong.activity
 * @Description: TODO
 * Copyright: Copyright 2015 loongjoy.inc
 * Company:上海龙照电子有限公司
 * @author hao_mo@loongjoy.com
 * @date 2015-8-31 下午5:31:05
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
import com.junova.huizhong.widget.AccidentPop;
import com.junova.huizhong.widget.DatePop;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.Toast;

/**
 * @author hao_mo@loongjoy.com
 * @ClassName: AccidentActivity
 * @Description: TODO
 * @date 2015-8-31 下午5:31:05
 */

public class AccidentActivity extends Activity implements OnClickListener {
    Button back;
    TextView curDate;
    ListView accidentList;
    AccidentAdapter adapter;
    DatePop pop;
    RelativeLayout dateLayout;
    TextView startDate;
    TextView endDate;
    ImageView dateImg;
    int pageIndex = 1;
    ArrayList<AccidentParam> accidentData;
    private String dateString;
    private String isdate;
    private String start;
    private String end;

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
        setContentView(R.layout.activity_accident);

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // start = end = sdf.format(date); // 当期日期
        String da = sdf.format(date);

        String str[] = da.split("-");
        String startTime = (Integer.parseInt(str[0]) - 1) + "-" + str[1] + "-" + str[2];
        end = da;
        start = startTime;
        initViews();
        getDeviceList(pageIndex);
    }

    private void initViews() {
        back = (Button) findViewById(R.id.back);
        back.setOnClickListener(this);
        curDate = (TextView) findViewById(R.id.cur_date);
        String date = FunctionUtils.getDate();
        curDate.setText(date.substring(0, 4) + "年" + date.substring(4, 6) + "月" + date.substring(6, 8) + "日");
        accidentList = (ListView) findViewById(R.id.list_accident);
        adapter = new AccidentAdapter(getApplicationContext(), new ArrayList<AccidentParam>(), accidentList);
        accidentList.setAdapter(adapter);
        accidentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                AccidentPop pop = new AccidentPop(AccidentActivity.this);
                AccidentParam param = adapter.getItem(position);
                pop.showView(param.getLevel(), param.getDesc(), param.getUrl());
                pop.setWidth(LayoutParams.MATCH_PARENT);
                pop.setHeight(LayoutParams.MATCH_PARENT);
                pop.setFocusable(true);
                pop.showAsDropDown(dateLayout);
            }
        });
        dateLayout = (RelativeLayout) findViewById(R.id.date_layout);
        try {
            getIntent().getStringExtra("date").toString();
            findViewById(R.id.layout).setVisibility(View.GONE);


        } catch (Exception e) {

        }
        dateLayout.setOnClickListener(this);
        // 当前时间
        Date curdate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        dateString = sdf.format(curdate);

        startDate = (TextView) findViewById(R.id.start_date);
        endDate = (TextView) findViewById(R.id.end_date);
        startDate.setText(dateString);
        endDate.setText(dateString);
        dateImg = (ImageView) findViewById(R.id.img_date);
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.back:
                finish();
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
        Log.d("YANGSHUANG", "selectDate: " + pop.isShowing());
    }

    private void getDeviceList(int pageIndex) {
        Map<Object, Object> op = new HashMap<Object, Object>();
        op.put("USERID", AppConfig.prefs.getString("userId", "0"));

        try {
            op.put("PARTID", getIntent().getStringExtra("partId"));
            op.put("STARTDATE", getIntent().getStringExtra("date").toString());
            op.put("ENDDATE", getIntent().getStringExtra("date").toString());
        } catch (Exception e) {
            op.put("PARTID", AppConfig.prefs.getString("partId", "0"));
            op.put("STARTDATE", start);
            op.put("ENDDATE", end);
//            op.put("PARTID", AppConfig.prefs.getString("partId", "0"));
//            op.put("STARTDATE", start);
//            op.put("ENDDATE", end);
        }
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
                                    Toast.makeText(AccidentActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                                    accidentData = new ArrayList<AccidentParam>();
                                    adapter.updata(accidentData);
                                    String msg = obj.getString("message");

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
                try {
                    if (BuildConfig.DEBUG) {
                        url = AppConfig.JUNOVAURL + accident.getString("URL");
                        com.orhanobut.logger.Logger.e(url);
                    } else {
                        url = AppConfig.HUIZHONGURL + accident.getString("URL");
                        com.orhanobut.logger.Logger.e(url);
                    }

                } catch (Exception e) {

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
