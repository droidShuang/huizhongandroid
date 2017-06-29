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
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.junova.huizhong.AppConfig;
import com.junova.huizhong.R;
import com.junova.huizhong.adapter.BanZuAdapter;
import com.junova.huizhong.common.Logger;
import com.junova.huizhong.common.http.AsyncHttpConnection;
import com.junova.huizhong.common.http.HttpMethod;
import com.junova.huizhong.common.http.AsyncHttpConnection.CallbackListener;
import com.junova.huizhong.model.PartParam;

public class GongChangPop extends PopupWindow {
    String id;
    BanZuAdapter workshop;
    BanZuAdapter section;
    BanZuAdapter group;
    List<PartParam> workshopData;
    List<PartParam> sectionData;
    List<PartParam> groupData;
    int station;
    Context context;
    private RelativeLayout gongduan; //工段视图
    private RelativeLayout banzu;
    String name;

    public GongChangPop(Context context) {
        this.context = context;
        View contentView = LayoutInflater.from(context).inflate(
                R.layout.pop_gongchang, null);
        setContentView(contentView);
        ListView cj = (ListView) contentView.findViewById(R.id.chejian_list);

        workshop = new BanZuAdapter(context, new ArrayList<PartParam>(),
                R.layout.chejian_gongduan_item);
        cj.setAdapter(workshop);
        //初始化顏色
        workshop.setcolor(-1);
        id = AppConfig.prefs.getString("partId", "0");
        station = 1;
        getSubLevelList();
        final ListView gd = (ListView) contentView
                .findViewById(R.id.gongduan_list);

        section = new BanZuAdapter(context, new ArrayList<PartParam>(),
                R.layout.gongchang_banzu);
        gd.setAdapter(section);
        final ListView bz = (ListView) contentView
                .findViewById(R.id.banzu_list);

        group = new BanZuAdapter(context, new ArrayList<PartParam>(),
                R.layout.gongchang_banzu);
        bz.setAdapter(group);

        gongduan = (RelativeLayout) contentView
                .findViewById(R.id.gongduan_layout);
        banzu = (RelativeLayout) contentView
                .findViewById(R.id.banzu_layout);
        final TextView tv_gongduan = (TextView) contentView
                .findViewById(R.id.gongduan_text);
        final TextView tv_banzu = (TextView) contentView
                .findViewById(R.id.banzu_text);
        final ImageView img_gongduan = (ImageView) contentView
                .findViewById(R.id.gongduan_img);
        final ImageView img_banzu = (ImageView) contentView
                .findViewById(R.id.banzu_img);
        gongduan.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                openList(gd, img_gongduan);
            }
        });
        banzu.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                openList(bz, img_banzu);
            }
        });
        cj.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                id = workshopData.get(arg2).getId();
                station = workshopData.get(arg2).getStation();
                workshop.setcolor(arg2);
                initView();
                //得到工段張view
                getGongduanView();
                getSubLevelList();
            }
        });
        gd.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                closeList(gd, img_gongduan);
                tv_gongduan.setText(sectionData.get(arg2).getName());
                id = sectionData.get(arg2).getId();
                station = sectionData.get(arg2).getStation();
                getSubLevelList();
                // 得到班主长View
                getBanZuView();

            }
        });
        bz.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                closeList(bz, img_banzu);
                tv_banzu.setText(groupData.get(arg2).getName());
                id = groupData.get(arg2).getId();
                station = groupData.get(arg2).getStation();
                name = groupData.get(arg2).getName();
                dismiss();
            }
        });
        setBackgroundDrawable(new ColorDrawable(Color.WHITE));
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
            case 1:// 获取车间列表
                workshopData = data;
                workshop.updata(workshopData);
                break;
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

    private void openList(View list, View img) {
        list.setVisibility(View.VISIBLE);
        img.setBackgroundResource(R.drawable.xiangxia_2);
    }

    private void closeList(View list, View img) {
        list.setVisibility(View.GONE);
        img.setBackgroundResource(R.drawable.xiangshang_1);
    }

    /**
     * 得到工段長View
     */
    private void getGongduanView() {
        gongduan.setVisibility(View.VISIBLE);
    }

    /**
     * 得到班主长View
     */
    protected void getBanZuView() {
        banzu.setVisibility(View.VISIBLE);
    }

    /**
     * 初始化view
     */
    protected void initView() {
        gongduan.setVisibility(View.GONE);
        banzu.setVisibility(View.GONE);
    }

    public String getName() {
        return name;
    }

    public int getStation() {
        return station;
    }
}
