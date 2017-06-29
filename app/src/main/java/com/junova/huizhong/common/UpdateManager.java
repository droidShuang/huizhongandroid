package com.junova.huizhong.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import com.junova.huizhong.R;
import com.junova.huizhong.widget.AppAlertDialog;
import com.junova.huizhong.widget.AppAlertDialog.AppAlertDialogListener;

/**
 * @{#} UpdateManager.java Create on 2014-1-7 上午10:21:12
 * @author xiaohui_tu
 * Effect: update app manager
 */
public class UpdateManager {
	private static final int DOWNLOAD = 1;
	private static final int DOWNLOAD_FINISH = 2;
	private String apkName;
	
	private String downloadUrl;
	private String updateSavePath;
	private int updateProgress;
	private boolean cancelUpdate = false;
	private String updateNote;

	private Context updateContext;
	private ProgressBar updateProgressBar;
	private Dialog updateDownloadDialog;
	
	private AppAlertDialog updateConfirmDialog;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DOWNLOAD:
				updateProgressBar.setProgress(updateProgress);
				break;
			case DOWNLOAD_FINISH:
//				installApk();
				break;
			default:
				break;
			}
		}
	};

	public UpdateManager(Context updateContext, String apkName, String downloadUrl,String uploadNote) {
		this.updateContext = updateContext;
		this.apkName = apkName;
		this.downloadUrl = downloadUrl;
		this.updateNote = uploadNote;
	}

	public void startUpdate() {
		
		updateConfirmDialog = new AppAlertDialog(updateContext,false,
				updateContext.getResources().getString(R.string.check_update), 
				updateNote, 
				updateContext.getResources().getString(R.string.update_immediately), 
				updateContext.getResources().getString(R.string.update_later),
				R.style.alert_dialog, new AppAlertDialogListener() {
					@Override
					public void onClick(View view) {
						if (view.getId() == R.id.positiveButton) {
							showDownloadDialog();
						}
					}
				},true);
		updateConfirmDialog.show();
	}

	/*
	 * show the download dialog, also used the default dialog
	 */
	private void showDownloadDialog() {
		AlertDialog.Builder builder = new Builder(updateContext);
		builder.setTitle(R.string.app_updating);
		final LayoutInflater inflater = LayoutInflater.from(updateContext);
		View v = inflater.inflate(R.layout.common_update_progress, null);
		updateProgressBar = (ProgressBar) v
				.findViewById(R.id.updateProgressBar);
		builder.setView(v);
		builder.setNegativeButton(R.string.cancel_update,
				new OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						cancelUpdate = true;
						delBrokenApk();
					}
				});
		updateDownloadDialog = builder.create();
		updateDownloadDialog.setCanceledOnTouchOutside(false);
		updateDownloadDialog.show();
		downloadApk();
	}

	/*
	 * download thread start
	 */
	private void downloadApk() {
		new downloadApkThread().start();
	}

	/*
	 * download thread
	 */
	private class downloadApkThread extends Thread {
		@Override
		public void run() {
			try {
				//check the sd card
				if (Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					//sd card path
					String sdpath = Environment.getExternalStorageDirectory()
							+ "/";
					updateSavePath = sdpath + "download";
					URL url = new URL(downloadUrl);
					//connect the http
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					conn.connect();
					
					int length = conn.getContentLength();
					InputStream is = conn.getInputStream();

					File file = new File(updateSavePath);
					if (!file.exists()) {
						file.mkdir();
					}
					File apkFile = new File(updateSavePath, apkName);
					FileOutputStream fos = new FileOutputStream(apkFile);
					int count = 0;
					byte buf[] = new byte[1024];
					do {
						int numread = is.read(buf);
						count += numread;
						updateProgress = (int) (((float) count / length) * 100);
						mHandler.sendEmptyMessage(DOWNLOAD);
						if (numread <= 0) {
							mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
							break;
						}
						fos.write(buf, 0, numread);
					} while (!cancelUpdate);
					fos.close();
					is.close();
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			updateDownloadDialog.dismiss();
		}
	}

	/*
	 * delete the not complete download apk
	 */
	private void delBrokenApk(){
		File apkFile = new File(updateSavePath, apkName);
		if(apkFile.exists()){
			apkFile.delete();
		}
	}

	/*
	 * install the apk
	 */
	private void installApk() {
		File apkfile = new File(updateSavePath, apkName);
		if (!apkfile.exists()) {
			return;
		}
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
				"application/vnd.android.package-archive");
		updateContext.startActivity(i);
	}
}
