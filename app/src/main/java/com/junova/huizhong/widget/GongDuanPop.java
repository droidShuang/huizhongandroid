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
import android.widget.Toast;

import com.junova.huizhong.AppConfig;
import com.junova.huizhong.R;
import com.junova.huizhong.adapter.BanZuAdapter;
import com.junova.huizhong.common.Logger;
import com.junova.huizhong.common.http.AsyncHttpConnection;
import com.junova.huizhong.common.http.HttpMethod;
import com.junova.huizhong.common.http.AsyncHttpConnection.CallbackListener;
import com.junova.huizhong.model.PartParam;

public class GongDuanPop extends PopupWindow {
	String id;
	List<PartParam> groupData;
	BanZuAdapter group;
	int station;
	Context context;
	protected String name;

	public GongDuanPop(final Context context) {
		this.context = context;
		id = AppConfig.prefs.getString("partId", "0");
		station = AppConfig.prefs.getInt("station", 0);
		View contentView = LayoutInflater.from(context).inflate(
				R.layout.pop_gongduan, null);
		setContentView(contentView);
		ListView listView = (ListView) contentView
				.findViewById(R.id.list_banzu);

		group = new BanZuAdapter(context, new ArrayList<PartParam>(),
				R.layout.banzu_item);
		listView.setAdapter(group);
		getSubLevelList();
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {


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
//		op.put("ID", id);
//		op.put("STATION", station);
		op.put("ID", AppConfig.prefs.getString("partId", "0"));
		op.put("STATION", "3");
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
		groupData = new ArrayList<PartParam>();
		try {
			JSONArray array = obj.getJSONArray("data");
			for (int i = 0; i < array.length(); i++) {
				JSONObject object = array.getJSONObject(i);
				PartParam pp = new PartParam(object.getString("ID"),
						object.getInt("STATION"), object.getString("NAME"));
				groupData.add(pp);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		group.updata(groupData);
	}

	public String getBanZuId() {
		return id;
	}

	public int getStation() {
		return station;
	}

	public String getName(){
		return name;
	}
}
