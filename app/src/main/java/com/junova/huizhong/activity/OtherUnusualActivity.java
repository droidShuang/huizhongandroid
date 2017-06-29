/**
 * @Title: OtherUnusualActivity.java
 * @Package com.loongjoy.huizhong.activity
 * @Description: TODO
 * Copyright: Copyright 2015 loongjoy.inc
 * Company:上海龙照电子有限公司
 * 
 * @author deng_long_fei(longfei_deng@loongjoy.com) 
 * @date 2015年11月11日 下午4:28:09
 * @version V1.0
 */

package com.junova.huizhong.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.junova.huizhong.AppConfig;
import com.junova.huizhong.R;
import com.junova.huizhong.adapter.AddYichangGridAdapter;
import com.junova.huizhong.adapter.AddYichangGridAdapter.ClickItemListener;
import com.junova.huizhong.adapter.UnusualAdapter;
import com.junova.huizhong.common.FunctionUtils;
import com.junova.huizhong.common.Logger;
import com.junova.huizhong.model.ExceptionParam;
import com.junova.huizhong.widget.PicGridView;

/**
 * @ClassName: OtherUnusualActivity
 * @Description: TODO
 * @author longfei_deng@loongjoy.com
 * @date 2015年11月11日 下午4:28:09
 *
 */
public class OtherUnusualActivity extends ActiveActivity implements
		OnClickListener {
	private TextView choose;
	public List<String> picHashMap = new ArrayList<String>();
	public String fileName; // 文件名称
	public AddYichangGridAdapter adapter;
	private PicGridView gridView;
	private String fileNsmes;
	private Button back;
	private Button commit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.other_unusual_activity);
		initView();
	}

	/**
	 * 初始化
	 */
	private void initView() {
		back = (Button) findViewById(R.id.back);
		back.setOnClickListener(this);
		commit = (Button) findViewById(R.id.commit);
		commit.setOnClickListener(this);
		choose = (TextView) findViewById(R.id.yc_choose);
		choose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				View view = LayoutInflater.from(getApplicationContext())
						.inflate(R.layout.pop_yichang_type, null);
				final PopupWindow pw = new PopupWindow(view, 320,
						LayoutParams.WRAP_CONTENT);
				// 设置点击外部可以消失
				pw.setBackgroundDrawable(new BitmapDrawable());
				pw.setOutsideTouchable(true);
				pw.setFocusable(true);
				pw.showAsDropDown(choose);

				final List<ExceptionParam> list = new ArrayList<ExceptionParam>();
				for (int i = 0; i < 5; i++) {
					ExceptionParam exceptionParam = new ExceptionParam("1", "你好",
							false, "", "");
					list.add(exceptionParam);
				}

				ListView lV = (ListView) view.findViewById(R.id.type_list);
				final UnusualAdapter adapter = new UnusualAdapter(list,
						getApplicationContext());
				lV.setAdapter(adapter);

				Drawable drawable = getResources().getDrawable(
						R.drawable.xuanze_white_2);
				drawable.setBounds(0, 0, drawable.getMinimumWidth(),
						drawable.getMinimumHeight());
				choose.setCompoundDrawables(null, null, drawable, null);
				lV.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						choose.setText(list.get(position).getName());
						Drawable drawable = getResources().getDrawable(
								R.drawable.xuanze_white_1);
						drawable.setBounds(0, 0, drawable.getMinimumWidth(),
								drawable.getMinimumHeight());
						choose.setCompoundDrawables(null, null, drawable, null);
						pw.dismiss();
					}
				});
			}
		});

		gridView = (PicGridView) findViewById(R.id.gridview);
		adapter = new AddYichangGridAdapter(this, picHashMap, gridView, true);
		gridView.setAdapter(adapter);
		adapter.setItemListener(new ClickItemListener() {

			@Override
			public void OnClickItem(int position) {
				shartCameraAction();
			}
		});

	}

	/**
	 * 删除本地图片
	 */
	protected void deleteLocal() {
		String[] s = fileNsmes.split(",");
		for (String string : s) {
			File file = new File(Environment.getExternalStorageDirectory()
					+ File.separator + AppConfig.PIC_OTHER, string);
			file.delete();
		}

	}

	/**
	 * 拍照
	 */
	public void shartCameraAction() {

		Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		fileName = "other_" + FunctionUtils.getDate() + ".jpg";
		fileNsmes = fileNsmes + fileName + ",";
		// 下面这句指定调用相机拍照后的照片存储的路径
		i.putExtra(MediaStore.EXTRA_OUTPUT,
				Uri.fromFile(new File(getFilePath(), fileName)));
		startActivityForResult(i, 101);
	}

	/**
	 * 路径
	 */
	public String getFilePath() {
		File file = new File(Environment.getExternalStorageDirectory()
				+ File.separator + AppConfig.PIC_OTHER);
		file.mkdir();
		Logger.getInstance().e("path", file.getPath());
		return file.getPath();
	}

	/**
	 * 回掉
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 101:
			File temp = new File(getFilePath(), fileName);
			Logger.getInstance().e("path", temp.getPath());

			if (temp.length() > 0) {
				initGridData(temp.getPath(), adapter);
			}
			break;

		default:
			break;
		}

	}

	/**
	 * 加载图片
	 */
	public void initGridData(String path, AddYichangGridAdapter adapter) {
		if (!path.equals("")) {
			picHashMap.add(path);
		}
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.commit:

			break;

		default:
			break;
		}
	}
}
