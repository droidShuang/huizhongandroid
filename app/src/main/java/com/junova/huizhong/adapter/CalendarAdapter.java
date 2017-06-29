/**
 * @Title: CalendarAdapter.java
 * @Package com.loongjoy.huizhong.adapter
 * @Description: TODO
 * Copyright: Copyright 2015 loongjoy.inc
 * Company:上海龙照电子有限公司
 * @author deng_long_fei(longfei_deng@loongjoy.com)
 * @date 2015年9月16日 上午10:22:56
 * @version V1.0
 */

package com.junova.huizhong.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.junova.huizhong.R;
import com.junova.huizhong.activity.AccidentActivity;
import com.junova.huizhong.common.SpecialCalendar;
import com.orhanobut.logger.Logger;

/**
 * @author longfei_deng@loongjoy.com
 * @ClassName: CalendarAdapter
 * @Description: 首页日历适配器
 * @date 2015年9月16日 上午10:22:56
 */

public class CalendarAdapter extends BaseAdapter {
    private CountListener countListener;
    private int year_c;
    private int month_c;
    //	private int day_c;
    // private String currentDate = "";
    private Context context;
    private String[] dayNumber = null;
    private SpecialCalendar sc;
    private int[] days;// 存放一个月的事故日期和水平
    private int day_c;
    ArrayList<HashMap<String, String>> listHashMaps;
    private String partId;
    private int safeday;

    public CalendarAdapter(int year_c, int month_c, int day_c, Context context, String partId, ArrayList<HashMap<String, String>> listHashMaps) {
        this.year_c = year_c;
        this.month_c = month_c;
        this.context = context;
        this.day_c = day_c;
        this.listHashMaps = listHashMaps;
        this.partId = partId;
        Data();
        for (int i = 0; i < dayNumber.length; i++) {
            for (int j = 0; j < listHashMaps.size(); j++) {
                String date = listHashMaps.get(j).get("date");


                if (listHashMaps.get(j).get("level").equals("")) {
                    days[i] = 0;

                } else {
                    days[i] = Integer.parseInt(listHashMaps.get(j).get("level"));
                    Log.d("GREENCROSS", "day	" + listHashMaps.get(j).get("level"));
                }

                //		}
            }
        }
    }

    public CalendarAdapter(int year_c, int month_c, int day_c, Context context) {
        this.year_c = year_c;
        this.month_c = month_c;
        this.context = context;
        this.day_c = day_c;
        Data();
    }

    public void setCountListener(CountListener countListener) {
        this.countListener = countListener;
    }

    public void Data() {
        days = new int[47];
        sc = new SpecialCalendar();
        boolean isLeapyear = sc.isLeapYear(year_c); // 是否为闰年
        int daysOfMonth = sc.getDaysOfMonth(isLeapyear, month_c); // 某月的总天数
        dayNumber = new String[47];
        // 把不需要的位置填充""
        for (int i = 0; i < dayNumber.length; i++) {
            if ((0 <= i && i <= 1) || (5 <= i && i <= 8)
                    || (12 <= i && i <= 13) || (35 <= i && i <= 36)
                    || (40 <= i && i <= 44)) {
                dayNumber[i] = "";
            } else {
                dayNumber[i] = i + "";
            }
        }
        // 填充日历中的数据
        int j = 1;
        for (int i = 0; i < dayNumber.length; i++) {
            if (!dayNumber[i].equals("")) {
                if (j <= daysOfMonth) {
                    dayNumber[i] = j++ + "";
                } else {
                    dayNumber[i] = "";
                }
            }
        }

    }

    @Override
    public int getCount() {
        return dayNumber.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        if (null == convertView) {
            // 装载布局文件 app_item.xml
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.calendar_item, null);

            holder = new ViewHolder();

            holder.date_item = (TextView) convertView
                    .findViewById(R.id.date_item);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.date_item.setText(dayNumber[position]);
        holder.date_item.setTextColor(Color.parseColor("#ffffff"));
        final String s = holder.date_item.getText().toString();

        int status = 0;
        if (!holder.date_item.getText().toString().equals("")) {
            if (!listHashMaps.get(Integer.parseInt(holder.date_item.getText().toString()) - 1).get("level").equals(""))
                status = Integer.parseInt(listHashMaps.get(Integer.parseInt(holder.date_item.getText().toString()) - 1).get("level"));
            holder.date_item.setBackgroundColor(Color.parseColor("#68BA87"));
        }

        if (position == 44 || position == 46 || position == 45 || position == 38 || position == 39) {
            if (holder.date_item.getText().toString().equals("")) {
                holder.date_item.setBackgroundColor(Color.parseColor("#bcbcbc"));
            }

        }

        final int finalStatus = status;
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (finalStatus == 0) {
                    Toast.makeText(context, "当日没有安全事故", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!s.equals("")) {
                    Intent intent = new Intent(context, AccidentActivity.class);
                    String year = year_c + "";
                    String month = month_c + "";
                    String day = holder.date_item.getText().toString();
                    if (month_c < 10) {
                        month = "0" + month_c;
                    }
                    if (Integer.parseInt(holder.date_item.getText().toString()) < 10) {
                        day = "0" + Integer.parseInt(holder.date_item.getText().toString());
                    }

                    intent.putExtra("date", year + "-" + month + "-" + day);
                    intent.putExtra("partId", partId);

                    context.startActivity(intent);
                }

            }
        });

        switch (status) {

            case 0:
                // 等级0无事故不做处理

                break;

            case 2:
                // 等级2设置背景为黄色
                holder.date_item.setBackgroundColor(Color.YELLOW);
                // 设置字体为黑色
                holder.date_item.setTextColor(Color.parseColor("#2a2c30"));

                break;
            case 3:
                // 等级3设置背景红色
                holder.date_item.setBackgroundColor(Color.RED);
                // 设置字体黑色
                holder.date_item.setTextColor(Color.parseColor("#2a2c30"));

                break;
            case 1:
                // 等级1设置背景蓝色
                holder.date_item.setBackgroundColor(Color.BLUE);
                // 设置字体黑色
                holder.date_item.setTextColor(Color.parseColor("#2a2c30"));

                break;

            default:

                break;
        }

        //	holder.date_item.setBackgroundColor(Color.parseColor("#05b3ea"));
        if (!holder.date_item.getText().toString().equals("")) {
            if (day_c <= Integer.parseInt(holder.date_item.getText().toString())) {
                Calendar calendar = Calendar.getInstance();
                int month = calendar.get(Calendar.MONTH) + 1;
                Logger.d("month 1 " + month + "month2  " + month_c);
                if (month == month_c) {
                    holder.date_item.setBackgroundColor(Color.parseColor("#bcbcbc"));
                }


            } else {
                if (status == 0) {
                    safeday++;
                } else {
                    safeday = 0;
                }

                countListener.onCount(safeday);
            }
        }
        return convertView;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    private class ViewHolder {
        TextView date_item;
    }

    public int getSafeday() {
        return safeday;
    }

    public interface CountListener {
        void onCount(int count);
    }


}
