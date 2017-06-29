/**   
* @{#} CacheClearManage.java Create on 2014-5-24 PM 3:44:10   
* @author tuxiaohui
* Copyright (c) 2014 by loongjoy.
* clear the local cache in app
*/
package com.junova.huizhong.common;

import java.io.File;

import android.content.Context;
import android.os.Environment;

public class CacheClearManage {
	//delete the webview cache /data/data/com.loongjoy.appbuilder/cache
	public static void cleanInternalCache(Context context) {
		deleteFilesByDirectory(context.getCacheDir());
	}

	//delete all the local database /data/data/com.loongjoy.appbuilder/databases
	public static void cleanDatabases(Context context) {
		deleteFilesByDirectory(new File("/data/data/"
				+ context.getPackageName() + "/databases"));
	}

	//delete the SharedPreference /data/data/com.loongjoy.appbuilder/shared_prefs
	public static void cleanSharedPreference(Context context) {
		deleteFilesByDirectory(new File("/data/data/"
				+ context.getPackageName() + "/shared_prefs"));
	}

	//delete the database by dbName
	public static void cleanDatabaseByName(Context context, String dbName) {
		context.deleteDatabase(dbName);
	}

	//delete /data/data/com.loongjoy.appbuilder/files
	public static void cleanFiles(Context context) {
		deleteFilesByDirectory(context.getFilesDir());
	}

	//delete the cache in /mnt/sdcard/android/data/com.loongjoy.appbuilder/cache
	public static void cleanExternalCache(Context context) {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			deleteFilesByDirectory(context.getExternalCacheDir());
		}
	}

	//clear the custom cache (.wuye file in sdcard)
	public static void cleanCustomCache(File filePath) {
		deleteFilesByDirectory(filePath);
	}

	//clear all application cache data
	public static void cleanApplicationData(Context context, File filepath) {
		cleanInternalCache(context);
		cleanExternalCache(context);
		//don't delete the shopping cart info
		//cleanDatabases(context);
		cleanSharedPreference(context);
		cleanFiles(context);
		cleanCustomCache(filepath);
		/*
		for (String filePath : filepath) {
			cleanCustomCache(filePath);
		}*/
		
	}

	//delete the the items in the file
	private static void deleteFilesByDirectory(File directory) {
		if (directory != null && directory.exists() && directory.isDirectory()) {
			for (File item : directory.listFiles()) {
				item.delete();
			}
		}
	}
}
