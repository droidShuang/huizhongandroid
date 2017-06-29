package com.junova.huizhong.widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.junova.huizhong.AppConfig;
import com.junova.huizhong.R;
import com.junova.huizhong.adapter.BanZuAdapter;
import com.junova.huizhong.common.Logger;
import com.junova.huizhong.common.http.AsyncHttpConnection;
import com.junova.huizhong.common.http.AsyncHttpConnection.CallbackListener;
import com.junova.huizhong.common.http.HttpMethod;
import com.junova.huizhong.model.PartParam;

public class CheJianPop extends PopupWindow {
    String id;
    BanZuAdapter section;
    BanZuAdapter group;
    List<PartParam> sectionData;
    List<PartParam> groupData;
    int station;
    Context context;
    protected String name;

    public CheJianPop(Context context) {
        this.context = context;
        id = AppConfig.prefs.getString("partId", "0");
        station = AppConfig.prefs.getInt("station", 0);
        View contentView = LayoutInflater.from(context).inflate(
                R.layout.pop_chejian, null);
        setContentView(contentView);
        ListView gd = (ListView) contentView.findViewById(R.id.gongduan_list);
        ListView bz = (ListView) contentView.findViewById(R.id.banzu_list);
        section = new BanZuAdapter(context, new ArrayList<PartParam>(),
                R.layout.chejian_gongduan_item);
        gd.setAdapter(section);

        group = new BanZuAdapter(context, new ArrayList<PartParam>(),
                R.layout.chejian_banzu_item);
        bz.setAdapter(group);
        //初始化刚开始每个位置都是默认颜色
        section.setcolor(-1);
        gd.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                id = sectionData.get(arg2).getId();
                station = sectionData.get(arg2).getStation();
                section.setcolor(arg2);
                getSubLevelList();
            }
        });
        bz.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                id = groupData.get(arg2).getId();
                station = groupData.get(arg2).getStation();
                name = groupData.get(arg2).getName();
                dismiss();
            }
        });
        getSubLevelList();
    }


    private void getSubLevelList() {
        Map<Object, Object> op = new HashMap<Object, Object>();
        op.put("ID", id);
        op.put("STATION", station);
        new AsyncHttpConnection().post(AppConfig.GET_SUBLEVEL_LIST,
                HttpMethod.getParams(context, op), new CallbackListener() {

                    @Override
                    public void callBack(String result) {
                        Logger.getInstance().e("getSubLevelListResult", result);
                        if ("fail".equals(result)) {
                            getSubLevelList();
                        }
                        if (result != null) {
                            try {
                                JSONObject obj = new JSONObject(result);
                                int status = obj.getInt("status");
                                if (status == 01) {
                                    jsonSubLevelList(obj);
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
        setBackgroundDrawable(new ColorDrawable(Color.WHITE));
    }

    private void jsonSubLevelList(JSONObject obj) {
        List<PartParam> data = new ArrayList<PartParam>();
        try {
            JSONArray array = obj.getJSONArray("data");
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                PartParam pp = new PartParam(object.getString("ID"),
                        object.getInt("STATION"), object.getString("NAME"));
                data.add(pp);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        switch (station) {
            case 2:// 获取工段列表
                sectionData = data;
                section.updata(sectionData);
                break;
            case 3:// 获取班组列表
                groupData = data;
                group.updata(groupData);
                break;
            default:
                break;
        }
    }

    public String getBanZuId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getStation() {
        return station;
    }
}
