/**
 * @Title: DeviceDataBase.java
 * @Package com.loongjoy.huizhong.db
 * @Description: TODO
 * Copyright: Copyright 2015 loongjoy.inc
 * Company:上海龙照电子有限公司
 * @author hao_mo@loongjoy.com
 * @date 2015-9-21 下午4:07:54
 * @version V1.0
 */

package com.junova.huizhong.db;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.junova.huizhong.AppConfig;
import com.junova.huizhong.model.CheckParam;
import com.junova.huizhong.model.DeviceParam;
import com.junova.huizhong.model.ExceptionParam;

/**
 * @author hao_mo@loongjoy.com
 * @ClassName: DeviceDataBase
 * @Description: TODO
 * @date 2015-9-21 下午4:07:54
 */

public class DeviceDataBase {
    SQLiteDatabase db;
    AppSqliteHelper helper;

    public DeviceDataBase(Context context) {
        helper = new AppSqliteHelper(context);
    }

    public long addDevice(String partId, String name, String specId, String deviceId, int type, String summary, String userId, int status) {

        ContentValues cv = new ContentValues();
        cv.put("summary", summary);
        cv.put("partId", partId);
        cv.put("type", type);
        cv.put("deviceId", deviceId);
        cv.put("specId", specId);
        cv.put("name", name);
        cv.put("status", status);
        cv.put("userId", userId);
        db = helper.getWritableDatabase();
        long result = db.insert(AppConfig.DEVICE, null, cv);
        db.close();
        return result;
    }

    /**
     * 搜索设备
     */
    public List<DeviceParam> search(String partId, String userId, String keyWord) {
        List<DeviceParam> data = new ArrayList<DeviceParam>();
        db = helper.getWritableDatabase();
        //String sql = "select * from " + AppConfig.DEVICE + " where name like '%" + keyWord + "%' and  partId = " + partId + " and userId=" + userId;
        String sql = "select * from " + AppConfig.DEVICE + " where name like '%" + keyWord + "%' ";
        //    db.execSQL("select * from "+AppConfig.DEVICE +"where name like ? and partId=? and userId=?",new String[]{keyWord,partId,userId});
        Cursor cursor = db.rawQuery(sql, null);


        while (cursor != null && cursor.moveToNext()) {
            String deviceId = cursor.getString(cursor.getColumnIndex("deviceId"));
            int type = cursor.getInt(cursor.getColumnIndex("type"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String summary = cursor.getString(cursor.getColumnIndex("summary"));
            String specId = cursor.getString(cursor.getColumnIndex("specId"));
            int status = cursor.getInt(cursor.getColumnIndex("status"));
            data.add(new DeviceParam(deviceId, type, name, summary, specId, status));
        }
        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return data;

    }

    public void clear() {
        Log.d("clearDevice", "clearDevice: deviceInfo   errorItem       checkItem");
        db = helper.getWritableDatabase();
        //	db.execSQL("delete from " + AppConfig.DEVICE + " where userId=" + userId);
        //    db.delete(AppConfig.CHECK_ITEM, "userId=?", new String[]{userId});

        db.execSQL("delete from deviceInfo");
        db.execSQL("delete from errorItem");
        db.execSQL("delete from checkItem");
        db.close();
    }

    public void clearDevice(String userId) {
        Log.d("clearDevice", "clearDevice: ");
        db = helper.getWritableDatabase();
        //	db.execSQL("delete from " + AppConfig.DEVICE + " where userId=" + userId);
        db.delete(AppConfig.DEVICE, "userId=?", new String[]{userId});
        db.close();
    }

    /**
     * 分辨是隐患还是仪器检测
     * A
     */
    public List<DeviceParam> getDevices(String partId, String userId, int type) {
        List<DeviceParam> data = new ArrayList<DeviceParam>();
        db = helper.getWritableDatabase();
        String[] columns = new String[]{"name", "summary", "specId", "deviceId", "type", "status"};
        //   String selection = "partId=? and userId=? and type=? ";
        //    String[] selectionArgs = new String[]{String.valueOf(partId), String.valueOf(userId), String.valueOf(type)};
        String selection = "partId=? and userId=? ";
        String[] selectionArgs = new String[]{String.valueOf(partId), String.valueOf(userId)};
        Cursor cursor = db.query(AppConfig.DEVICE, columns, selection, selectionArgs, null, null, null);
        while (cursor != null && cursor.moveToNext()) {
            String deviceId = cursor.getString(cursor.getColumnIndex("deviceId"));
            int type1 = cursor.getInt(cursor.getColumnIndex("type"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String summary = cursor.getString(cursor.getColumnIndex("summary"));
            String specId = cursor.getString(cursor.getColumnIndex("specId"));
            int status = cursor.getInt(cursor.getColumnIndex("status"));
            data.add(new DeviceParam(deviceId, type1, name, summary, specId, status));
        }
        //  UUID.randomUUID().toString()
        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return data;
    }

    public List<DeviceParam> getDevices(String partId, String userId) {
        List<DeviceParam> data = new ArrayList<DeviceParam>();
        db = helper.getWritableDatabase();
        String[] columns = new String[]{"name", "summary", "specId", "deviceId", "type", "status"};
//        String selection = "partId=? and userId=?  ";
//       String[] selectionArgs = new String[]{String.valueOf(partId), String.valueOf(userId)};
        String selection = "userId = ?  ";
        String[] selectionArgs = new String[]{String.valueOf(userId)};
        Cursor cursor = db.query(AppConfig.DEVICE, columns, selection, selectionArgs, null, null, null);
        while (cursor != null && cursor.moveToNext()) {
            String deviceId = cursor.getString(cursor.getColumnIndex("deviceId"));
            int type = cursor.getInt(cursor.getColumnIndex("type"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String summary = cursor.getString(cursor.getColumnIndex("summary"));
            String specId = cursor.getString(cursor.getColumnIndex("specId"));
            int status = cursor.getInt(cursor.getColumnIndex("status"));
            data.add(new DeviceParam(deviceId, type, name, summary, specId, status));
        }
        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return data;
    }

    public void addCheck(String deviceId, String checkId, String name, String userId) {
        ContentValues cv = new ContentValues();
        cv.put("checkId", checkId);
        cv.put("name", name);
        cv.put("deviceId", deviceId);
        cv.put("userId", userId);
        db = helper.getWritableDatabase();
        db.insert(AppConfig.CHECK_ITEM, null, cv);
        Log.d("clearDevice", "addCheck: ");
        db.close();
    }

    public void clearCheckItem(String userId) {
        db = helper.getWritableDatabase();
        //	db.execSQL("delete from " + AppConfig.CHECK_ITEM + " where userId=" + userId);
        Log.d("clearDevice", "clearCheckItem: ");
        db.delete(AppConfig.CHECK_ITEM, "userId=?", new String[]{userId});
        db.close();
    }

    public List<CheckParam> getCheckItems(String deviceId, String userId) {
        List<CheckParam> data = new ArrayList<CheckParam>();
        db = helper.getWritableDatabase();
        String[] columns = new String[]{"name", "checkId"};
        String selection = "deviceId=? and userId=?";
        String[] selectionArgs = new String[]{String.valueOf(deviceId), String.valueOf(userId)};
        Cursor cursor = db.query(AppConfig.CHECK_ITEM, columns, selection, selectionArgs, null, null, null);
        while (cursor != null && cursor.moveToNext()) {
            String checkId = cursor.getString(cursor.getColumnIndex("checkId"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            //  int status=cursor.getInt(cursor.getColumnIndex("status"));
            data.add(new CheckParam(checkId, name, 1, getExceptions(checkId), UUID.randomUUID().toString().replace("-", "")));
        }
        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return data;
    }

    public void addError(String errorId, String checkId, String name) {
        ContentValues cv = new ContentValues();
        cv.put("checkId", checkId);
        cv.put("name", name);
        cv.put("errorId", errorId);
        db = helper.getWritableDatabase();
        db.insert(AppConfig.ERROR_ITEM, null, cv);
        db.close();
    }

    public void clearError() {
        db = helper.getWritableDatabase();
        db.execSQL("delete from " + AppConfig.ERROR_ITEM);
        db.close();
    }

    public List<ExceptionParam> getExceptions(String checkId) {
        List<ExceptionParam> data = new ArrayList<ExceptionParam>();
        db = helper.getWritableDatabase();
        String[] columns = new String[]{"name", "errorId",};
        String selection = "checkId=?";
        String[] selectionArgs = new String[]{String.valueOf(checkId)};
        Cursor cursor = db.query(AppConfig.ERROR_ITEM, columns, selection, selectionArgs, null, null, null);
        while (cursor != null && cursor.moveToNext()) {
            String errorId = cursor.getString(cursor.getColumnIndex("errorId"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            data.add(new ExceptionParam(errorId, name, false, "", ""));
        }
        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return data;

    }

    /**
     * 修改设备状态 updateStatus
     *
     * @param @param id 设备id
     * @param @param status 设备状态 (0待巡查，1巡查正常，2巡查异常，10待抽查，11抽查正常，12抽查异常)
     * @return void
     * @author xialong-long_xia@loongjoy.com
     */
    public long updateStatus(String id, int status, String userId) {
        // TODO Auto-generated method stub
        ContentValues cv = new ContentValues();
        cv.put("status", status);
        db = helper.getWritableDatabase();
        long result = db.update(AppConfig.DEVICE, cv, "deviceId=? and userId=?", new String[]{String.valueOf(id),
                userId});
        db.close();
        return result;
    }

    /**
     * 设备状态回滚 updateStatus
     *
     * @param @param id 设备id
     * @param @param status 设备状态 (0待巡查，1巡查正常，2巡查异常，10待抽查，11抽查正常，12抽查异常)
     * @return void
     * @author xialong-long_xia@loongjoy.com
     */
    public long updateStatus(int status, String userId) {
        // TODO Auto-generated method stub
        ContentValues cv = new ContentValues();
        cv.put("status", status);
        db = helper.getWritableDatabase();
        long result = db.update(AppConfig.DEVICE, cv, "userId=?", new String[]{String.valueOf(userId)});
        db.close();
        return result;
    }

    /**
     * 设备是否检查完毕 count
     *
     * @param @param  status
     * @param @param  userId
     * @param @return
     * @return int
     * @author xialong-long_xia@loongjoy.com
     */
    public long count(int status, String userId, String partId) {
        long count = -1;
        db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select count(*) from deviceInfo where status=? and userId=? and partId=?",
                new String[]{String.valueOf(status), userId, partId});
        //游标移到第一条记录准备获取数据
        cursor.moveToFirst();

        // 获取数据中的LONG类型数据
        count = cursor.getLong(0);
        cursor.close();
        db.close();
        return count;
    }


    public long countTotal(int status, String userId, String partId) {
        long count = -1;
        db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select count(*) from deviceInfo where status=? and userId=? and partId=?",
                new String[]{String.valueOf(status), userId, partId});
        //游标移到第一条记录准备获取数据
        cursor.moveToFirst();
        // 获取数据中的LONG类型数据
        count = cursor.getLong(0);
        cursor.close();
        db.close();
        return count;
    }


}
