package com.junova.huizhong.activity;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;

import com.junova.huizhong.AppConfig;
import com.junova.huizhong.R;
import com.junova.huizhong.adapter.ActiveAdapter;
import com.junova.huizhong.common.Logger;
import com.junova.huizhong.common.http.AsyncHttpConnection;
import com.junova.huizhong.common.http.AsyncHttpConnection.CallbackListener;
import com.junova.huizhong.common.http.HttpMethod;
import com.junova.huizhong.model.ActiveParam;
import com.junova.huizhong.widget.ActivityDatePop;
import com.junova.huizhong.widget.CheJianPop;
import com.junova.huizhong.widget.GongChangPop;
import com.junova.huizhong.widget.GongDuanPop;
import com.junova.huizhong.widget.GongSiPop;

public class ActiveActivity extends Activity implements OnClickListener {
    Button back;
    TextView dateTxt;
    TextView bumen;
    ListView activeList;
    List<ActiveParam> activeDate;
    ActiveAdapter adapter;
    LinearLayout leaderLayout;
    TextView chooseBanzu;
    TextView chooseDate;
    PopupWindow chooseBanZu;
    String date;
    String date1;
    String userId;
    String partId;
    int station;
    int pageIndex = 1;
    boolean isFirst = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_list);
        intiViews();

    }

    private void intiViews() {
        userId = AppConfig.prefs.getString("userId", "0");
        partId = AppConfig.prefs.getString("partId", "0");
        station = AppConfig.prefs.getInt("station", 0);
        // 选择日期
        Date curDate = new Date(System.currentTimeMillis());
        date = String.valueOf(curDate.getYear() + 1900) + "-"
                + String.valueOf(curDate.getMonth() + 1) + "-"
                + String.valueOf(curDate.getDate());
        // 今日日期
        Date cDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        date1 = sdf.format(cDate);

        leaderLayout = (LinearLayout) findViewById(R.id.layout_leader);
        chooseBanzu = (TextView) findViewById(R.id.choose_banzu);
        chooseDate = (TextView) findViewById(R.id.choose_date);
        chooseDate.setText(getResources().getString(R.string.chooseTime)
                + date1);
        chooseBanzu.setOnClickListener(this);
        chooseDate.setOnClickListener(this);
        back = (Button) findViewById(R.id.back);
        dateTxt = (TextView) findViewById(R.id.date_today);

		/*
         * String date1 = String.valueOf(curDate.getYear() + 1900) + "年" +
		 * String.valueOf(curDate.getMonth() + 1) + "月" +
		 * String.valueOf(curDate.getDate()) + "日";
		 */
        dateTxt.setText(getResources().getString(R.string.currentDate) + date1);
        bumen = (TextView) findViewById(R.id.bumen);
        bumen.setText(AppConfig.prefs.getString("part", ""));
        activeList = (ListView) findViewById(R.id.list_active);
        back.setOnClickListener(this);
        if (station <= 3) {
            leaderLayout.setVisibility(View.VISIBLE);
            findViewById(R.id.history).setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.choose_banzu:
                selectBanZu(AppConfig.prefs.getInt("station", 0));
                break;
            case R.id.choose_date:
                selectDate();
                break;
            case R.id.history:
                Intent intent = new Intent();
                intent.setClass(ActiveActivity.this, ActiveHistoryActivity.class);
                startActivity(intent);
                break;

            default:
                break;
        }
    }

    private void selectDate() {
        Calendar mCalendar = Calendar.getInstance();
        int year = mCalendar.get(Calendar.YEAR);
        int month = mCalendar.get(Calendar.MONTH);
        int day = mCalendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(ActiveActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String year1 = String.valueOf(year);

                String month = String.valueOf(monthOfYear + 1).length() >= 2 ? String
                        .valueOf(monthOfYear + 1) : "0"
                        + String.valueOf(monthOfYear + 1);
                String day = String.valueOf(dayOfMonth).length() >= 2 ? String
                        .valueOf(dayOfMonth) : "0"
                        + String.valueOf(dayOfMonth);
                if (day.equals("00")) {
                    day = chooseDate.getText().toString().split("-")[2];
                }
                date1 = year + "-" + month + "-" + day;
                chooseDate.setText(getResources()
                        .getString(R.string.chooseTime)
                        + year
                        + "-"
                        + month
                );
                getActivityList(pageIndex);
            }
        }, year, month, day);
        DatePicker datePicker = datePickerDialog.getDatePicker();
        datePicker.setCalendarViewShown(false);
        hideDay(datePicker);
        datePickerDialog.show();

    }

    private void hideDay(DatePicker mDatePicker) {
        Field[] datePickerfFields = mDatePicker.getClass().getDeclaredFields();
        for (Field datePickerField : datePickerfFields) {
            if ("mDaySpinner".equals(datePickerField.getName()) || ("mDayPicker").equals(datePickerField.getName())) {
                datePickerField.setAccessible(true);
                Object dayPicker = new Object();
                try {
                    dayPicker = datePickerField.get(mDatePicker);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
                ((View) dayPicker).setVisibility(View.GONE);
            }
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
                    // gd.setAnimationStyle(R.style.GongDuanPop);
                    gd.showAsDropDown(findViewById(R.id.layout_leader));
                    gd.setOnDismissListener(new OnDismissListener() {

                        @Override
                        public void onDismiss() {
                            if (gd.getName() != null) {
                                partId = gd.getBanZuId();
                                chooseBanzu.setText(gd.getName());
                                getActivityList(pageIndex);
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
                                partId = cj.getBanZuId();
                                chooseBanzu.setText(cj.getName());
                                getActivityList(pageIndex);

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
                            partId = gc.getBanZuId();
                            getActivityList(pageIndex);
                            chooseBanzu.setText(gc.getName());

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
//                            partId = gs.getBanZuId();
//                            getActivityList(pageIndex);
//                            chooseBanzu.setText(gs.getName());
//                        }
//                    });
//                }
//                break;
            default:
                break;
        }
    }

    private void getActivityList(int pageIndex) {
        if (partId.equals("0") && station <= 3) {
            Toast.makeText(this, "请选择班组", Toast.LENGTH_LONG).show();
            return;
        }
        Map<Object, Object> op = new HashMap<Object, Object>();
        op.put("PARTID", partId);
        op.put("DATE", date1);
        // op.put("date", "2015-9-28");
        com.orhanobut.logger.Logger.d("partId"+partId+" date "+date1);
        new AsyncHttpConnection().post(AppConfig.GET_ACTIVITY_LIST,
                HttpMethod.getParams(getApplicationContext(), op),
                new CallbackListener() {

                    @Override
                    public void callBack(String result) {

                        if (result != null) {
                            try {
                                JSONObject obj = new JSONObject(result);
                                int status = obj.getInt("status");
                                if (status == 01) {
                                    jsonActivityList(obj);
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

    private void jsonActivityList(JSONObject obj) {
        try {
            JSONArray array = obj.getJSONArray("data");
            activeDate = new ArrayList<ActiveParam>();
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                String id = object.getString("ID");
                int type = object.getInt("TYPE");
                String title = object.getString("TITLE");
                String summary = object.getString("SUMMARY");
                String day = object.getString("DAY");
                int completeTimes = object.getInt("MONTHTIMES");
                int monthTimes = object.getInt("COMPLETETIMES");
                ActiveParam ap = new ActiveParam(type, title, summary,
                        monthTimes, id, completeTimes, day);
                activeDate.add(ap);
            }
            adapterData(activeDate);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * 适配器初始化和更新数据
     */
    private void adapterData(List<ActiveParam> activeDate2) {
        adapter = new ActiveAdapter(activeDate2, getApplicationContext());
        activeList.setAdapter(adapter);
        activeList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                Intent intent = new Intent(ActiveActivity.this,
                        ActivieDetailActivity.class);
                intent.putExtra("id", activeDate.get(arg2).getId());
                intent.putExtra("title", activeDate.get(arg2).getTitle());
                intent.putExtra("partId", partId);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        //  if (station == 0) {
        int int1 = AppConfig.prefs.getInt("station", -1);
        if (int1 <= 3 & int1 >= 0) {

        } else {
            getActivityList(pageIndex);
        }

        //   }
        super.onResume();
    }
}
