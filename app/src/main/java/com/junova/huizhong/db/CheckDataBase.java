/**
 * @Title: CheckDb.java
 * @Package com.loongjoy.huizhong.db
 * @Description: TODO
 * Copyright: Copyright 2015 loongjoy.inc
 * Company:上海龙照电子有限公司
 * @author hao_mo@loongjoy.com
 * @date 2015-9-14 下午4:01:55
 * @version V1.0
 */

package com.junova.huizhong.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.junova.huizhong.AppConfig;
import com.junova.huizhong.common.Logger;
import com.junova.huizhong.model.UpLoadCheckModel;

/**
 * @author hao_mo@loongjoy.com
 * @ClassName: CheckDb
 * @Description: TODO
 * @date 2015-9-14 下午4:01:55
 */

public class CheckDataBase {
    SQLiteDatabase db;
    AppSqliteHelper helper;

    public CheckDataBase(Context context) {
        helper = new AppSqliteHelper(context);
    }

    public void closeDb() {
        if (db != null && db.isOpen()) {
            db.close();
        }
    }

    public void clear() {
        Log.d("clearDevice", "clearDevice: checkItem   checkRecords");
        db = helper.getWritableDatabase();
        //	db.execSQL("delete from " + AppConfig.DEVICE + " where userId=" + userId);
        //    db.delete(AppConfig.CHECK_ITEM, "userId=?", new String[]{userId});
        db.execSQL("delete from checkItem");
        db.execSQL("delete from checkRecords");
        db.close();
    }


    public long addCheck(String deviceId, String checkId, String errorId, String name,
                         int status, int submited, String updataTime, String pics,
                         String recordId, int type, String partId, String partRecordId, String errodesc) {
        ContentValues cv = new ContentValues();
        cv.put("deviceId", deviceId);
        cv.put("checkId", checkId);
        cv.put("errorId", errorId);
        cv.put("recordId", recordId);
        cv.put("name", name);
        cv.put("status", status);
        cv.put("submited", submited);
        cv.put("updataTime", updataTime);
        cv.put("pics", pics);
        cv.put("type", type);
        cv.put("partId", partId);
        cv.put("partRecordId", partRecordId);
        cv.put("errodesc", errodesc);
        db = helper.getWritableDatabase();
        long result = db.insert(AppConfig.CHECK, null, cv);
        closeDb();
        return result;
    }

    public long updateCheck(String deviceId, String checkId, String errorId, String name,
                            int status, int submited, String updataTime, String pics,
                            String recordId, int type, String partId, String errodesc) {
        ContentValues cv = new ContentValues();
        cv.put("errorId", errorId);
        cv.put("recordId", recordId);
        cv.put("name", name);
        cv.put("status", status);
        cv.put("submited", submited);
        cv.put("updataTime", updataTime);
        cv.put("pics", pics);
        cv.put("type", type);
        cv.put("partId", partId);
        cv.put("errodesc", errodesc);
        String whereClause = "deviceId=? and CheckId=?";
        String[] whereArgs = new String[]{String.valueOf(deviceId), String.valueOf(checkId)};
        db = helper.getWritableDatabase();
        long result = db.update(AppConfig.CHECK, cv, whereClause, whereArgs);
        closeDb();
        return result;
    }

    public long updataRecordId(String recordId, String partId) {
        ContentValues cv = new ContentValues();
        cv.put("recordId", recordId);
        db = helper.getWritableDatabase();
        int ltresut = db.update(AppConfig.CHECK, cv, "submited=? and partId=?",
                new String[]{String.valueOf(AppConfig.UN_SUMBITED), partId});
        closeDb();
        return ltresut;
    }

    public long deleteRecord(int checkId, int partId) {
        ContentValues cv = new ContentValues();
        cv.put("submited", AppConfig.SUMBITED);
        db = helper.getWritableDatabase();
        int ltresut = db.update(AppConfig.CHECK, cv, "checkId=? and partId=?",
                new String[]{String.valueOf(checkId), String.valueOf(partId)});
        closeDb();
        return ltresut;
    }

    public long updataSubmited(int where, int submited, String recordId) {
        ContentValues cv = new ContentValues();
        cv.put("submited", submited);
        db = helper.getWritableDatabase();
        int ltresut = db.update(AppConfig.CHECK, cv, "submited=? and recordId=?",
                new String[]{String.valueOf(where), recordId});
        closeDb();
        return ltresut;
    }

    public int getCheckCount() {
        db = helper.getReadableDatabase();
        Cursor cursor = db.query(AppConfig.DEVICE, new String[]{"deviceId"}, "", new String[]{}, null, null, null);
        int count = cursor.getCount();
        while (cursor != null && cursor.moveToNext()) {
            String deviceId = cursor.getString(cursor.getColumnIndex("deviceId"));
            com.orhanobut.logger.Logger.d(deviceId);
        }
        cursor.close();
        db.close();
        return count;
    }

    public List<UpLoadCheckModel> getUnSubmitedCheck(String partId) {
        db = helper.getReadableDatabase();
        String[] columns = new String[]{"deviceId", "checkId", "status",
                "errorId", "pics", "type", "recordId,partRecordId", "errodesc", "name"};
        // String selection = "submited=? and partId=?";
        String selection = "submited=?";
        Log.d("SHANGCHUAN", "handleMessage: partId" + partId);
        String[] selectionArgs = new String[]{String
                .valueOf(AppConfig.UN_SUMBITED)};
        Cursor cursor = db.query(AppConfig.CHECK, columns, selection,
                selectionArgs, null, null, null);
        List<UpLoadCheckModel> result = new ArrayList<UpLoadCheckModel>();

        while (cursor != null && cursor.moveToNext()) {
            String deviceId = cursor.getString(cursor.getColumnIndex("deviceId"));
            String checkId = cursor.getString(cursor.getColumnIndex("checkId"));
            int status = cursor.getInt(cursor.getColumnIndex("status"));
            String errorId = cursor.getString(cursor.getColumnIndex("errorId"));
            String pics = cursor.getString(cursor.getColumnIndex("pics"));
            int type = cursor.getInt(cursor.getColumnIndex("type"));
            String recordsId = cursor.getString(cursor.getColumnIndex("recordId"));
            String partRecordId = cursor.getString(cursor.getColumnIndex("partRecordId"));
            String errodesc = cursor.getString(cursor.getColumnIndex("errodesc"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            result.add(new UpLoadCheckModel(deviceId, checkId, status, errorId,
                    pics, type, recordsId, partRecordId, errodesc, name));
        }

        if (cursor != null) {
            cursor.close();
        }

        closeDb();
        return result;
    }

    public int getCheckItemCount(String partId) {

        return 0;
    }

    public List<UpLoadCheckModel> getCheckedItem(String partId, String deviceId) {
        db = helper.getReadableDatabase();
        String[] columns = new String[]{"checkId", "status",
                "errorId", "pics", "type", "recordId", "partRecordId", "errodesc", "name"};
        String selection = "submited=? and partId=? and deviceId=?";
        String[] selectionArgs = new String[]{String
                .valueOf(AppConfig.UN_SUMBITED), String.valueOf(partId), String.valueOf(deviceId)};
        Cursor cursor = db.query(AppConfig.CHECK, columns, selection,
                selectionArgs, null, null, null);
        List<UpLoadCheckModel> result = new ArrayList<UpLoadCheckModel>();
        while (cursor != null && cursor.moveToNext()) {
            String checkId = cursor.getString(cursor.getColumnIndex("checkId"));
            int status = cursor.getInt(cursor.getColumnIndex("status"));
            String errorId = cursor.getString(cursor.getColumnIndex("errorId"));
            String pics = cursor.getString(cursor.getColumnIndex("pics"));
            String partRecordId = cursor.getString(cursor.getColumnIndex("partRecordId"));
            int type = cursor.getInt(cursor.getColumnIndex("type"));
            String recordsId = cursor.getString(cursor.getColumnIndex("recordId"));
            String errodesc = cursor.getString(cursor.getColumnIndex("errodesc"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            Logger.getInstance().d("cursor", recordsId);
            result.add(new UpLoadCheckModel(deviceId, checkId, status, errorId,
                    pics, type, recordsId, partRecordId, errodesc, name));
        }
        if (cursor != null) {
            cursor.close();
        }
        closeDb();
        return result;
    }

    /**
     * 传入图片的日期 对比巡查记录的日期 如果对应的记录已经提交 则说明这张图待上传 isNeedUpload 作用 TODO(描述)
     *
     * @param @param  date
     * @param @return
     * @return boolean
     * @throws
     * @Title: isNeedUpload
     * @Description: TODO
     * @author hao_mo@loongjoy.com
     */
    public boolean isNeedUpload(String pics) {
        db = helper.getReadableDatabase();
        String[] columns = new String[]{"submited"};
        String selection = "pics like ?";
        String[] selectionArgs = new String[]{pics};

        Cursor cursor = db.query(AppConfig.CHECK, columns, selection,
                selectionArgs, null, null, null);
        while (cursor != null && cursor.moveToNext()) {
            int submited = cursor.getInt(cursor.getColumnIndex("submited"));

            if (submited == AppConfig.UN_SUMBITED) {
                return true;
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return false;
    }

}
