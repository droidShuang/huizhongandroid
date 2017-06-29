package com.junova.huizhong.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.junova.huizhong.AppConfig;

public class AppSqliteHelper extends SQLiteOpenHelper {
    private String CREATE_DEVICE_TABLE = "create table if not exists "
            + AppConfig.DEVICE
            + "(partID text,name text,summary text,specId text,deviceId integer,id integer primary key autoincrement,type integer,status integer,userId text)";
    private String CREATE_CHECK_ITEM_TABLE = "create table if not exists "
            + AppConfig.CHECK_ITEM
            + "(id integer primary key autoincrement,deviceId integer,checkId integer,name text,userId text)";
    private String CREATE_ERROR_ITEM_TABLE = "create table if not exists "
            + AppConfig.ERROR_ITEM
            + "(id integer primary key autoincrement,checkId integer,errorId integer,name text)";
    private String CREATE_PART_ID_TABLE = "create table if not exists "
            + AppConfig.PART_ID
            + "(id integer primary key autoincrement,partId text)";

    /**
     * 创建检查项表单 id 主键，deviceId 设备ID，checkId 检查项ID，errorId 异常id，name 检查项标题， status
     * 异常状态， submited 是否已上传，updataTime 更新时间，pics 图片路径,recordId 记录Id type 类型
     */
    private String CREATE_CHECK_TABLE = "create table if not exists "
            + AppConfig.CHECK
            + "(id integer primary key autoincrement,deviceId integer, checkId integer,errorId integer,name text,status integer,submited integer,updataTime text,pics text,recordId text,type integer,partRecordId text,partId text,errodesc text)";

    public AppSqliteHelper(Context context) {
        super(context, AppConfig.DATABASE, null, AppConfig.DATABASE_VERSION);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase arg0) {
        arg0.execSQL(CREATE_CHECK_TABLE);
        arg0.execSQL(CREATE_DEVICE_TABLE);
        arg0.execSQL(CREATE_CHECK_ITEM_TABLE);
        arg0.execSQL(CREATE_ERROR_ITEM_TABLE);
        arg0.execSQL(CREATE_PART_ID_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        if (arg1 == 1 & arg2 == 2) {

        }
    }

}
