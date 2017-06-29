package com.junova.huizhong.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.junova.huizhong.AppConfig;
import com.junova.huizhong.R;
import com.junova.huizhong.adapter.ActiveDetailAdapter;
import com.junova.huizhong.common.FunctionUtils;
import com.junova.huizhong.common.Logger;
import com.junova.huizhong.common.http.AsyncHttpConnection;
import com.junova.huizhong.common.http.AsyncHttpConnection.CallbackListener;
import com.junova.huizhong.common.http.HttpMethod;
import com.junova.huizhong.model.ActiveContentParam;
import com.junova.huizhong.model.ActiveItemParam;
import com.junova.huizhong.widget.LoadingDialog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

public class ActivieDetailActivity extends Activity implements OnClickListener {
    Button back;
    Button save;
    TextView activeName;
    TextView activeDate;
    ExpandableListView contentList;
    ActiveDetailAdapter adapter;
    String id;
    String userId;
    String partId;
    int station;
    List<ActiveItemParam> itemList;
    private String title; // 活动标题
    private String date;//当前时间
    private List<Boolean> flagList = new ArrayList<>();
    int groupPosition, childPosition, gridPosition;
    private String imageName = "";
    LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_detail);
        initView();
        initdata();
        getActiviteDetail();
    }

    /**
     * 初始化时间
     */
    private void initdata() {
        // 今日日期
        Date cDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        date = sdf.format(cDate);
        activeName.setText(title);
        activeDate.setText("今日日期：" + date);
    }

    private void initView() {
        id = getIntent().getStringExtra("id");
        title = getIntent().getStringExtra("title");
        userId = AppConfig.prefs.getString("userId", "0");
        station = AppConfig.prefs.getInt("station", 0);
        partId = getIntent().getStringExtra("partId");
        back = (Button) findViewById(R.id.back);
        save = (Button) findViewById(R.id.save);
        if (AppConfig.prefs.getInt("station", 0) < 4) {
            save.setVisibility(View.GONE);
        }
        back.setOnClickListener(this);
        save.setOnClickListener(this);
        activeName = (TextView) findViewById(R.id.active_name);
        activeDate = (TextView) findViewById(R.id.active_date);
        contentList = (ExpandableListView) findViewById(R.id.active_content_list);


    }


    private void getActiviteDetail() {
        Map<Object, Object> op = new HashMap<Object, Object>();
        //    op.put("USERID",userId);
        op.put("ID", id);
        op.put("PARTID", getIntent().getStringExtra("partId"));
        //  op.put("STATION", station);
        new AsyncHttpConnection().post(AppConfig.GET_ACTIVITY_DETAIL,
                HttpMethod.getParams(getApplicationContext(), op),
                new CallbackListener() {

                    @Override
                    public void callBack(String result) {
                        Logger.getInstance().e("result", result);
                        if (result != null) {
                            try {
                                JSONObject obj = new JSONObject(result);
                                int status = obj.getInt("status");
                                if (status == 01) {
                                    jsonActiveDetail(obj);
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

    private void jsonActiveDetail(JSONObject obj) {
        try {
            itemList = new ArrayList<ActiveItemParam>();
            JSONArray items = obj.getJSONArray("data");

            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);
                String id = item.getString("ACTID");
                String title = item.getString("ACTTITLE");
                JSONArray contents = item.getJSONArray("CONTENT");
                List<ActiveContentParam> contentList = new ArrayList<ActiveContentParam>();
                for (int j = 0; j < contents.length(); j++) {
                    JSONObject content = contents.getJSONObject(j);
                    String contentId = content.getString("CONID");
                    String contentTitle = content.getString("CONTITLE");
                    String completed = content.getString("COMPLETED");
                    if (completed.equals("1")) {
                        flagList.add(true);
                    } else {
                        flagList.add(false);
                    }

                    boolean bl = false;
                    if (completed.equals("1")) {
                        bl = true;
                    }

                    contentList.add(new ActiveContentParam(contentId,
                            contentTitle, bl, "", new ArrayList<String>()));
                }
                itemList.add(new ActiveItemParam(id, title, contentList));
            }
            initAdapter();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * 初始化适配器
     */
    private void initAdapter() {
        adapter = new ActiveDetailAdapter(itemList,
                ActivieDetailActivity.this, id);
        adapter.setListener(gridOnItemSelectedListener);
        contentList.setAdapter(adapter);
        contentList.setGroupIndicator(null);
    }

    private void submitActivite() {
        JSONArray jsonArray = getJsonArray();
        if (jsonArray.length() == 0) {
            Toast.makeText(this, "没有添加任何操作，无需提交", Toast.LENGTH_LONG).show();
            return;
        }
        com.orhanobut.logger.Logger.d("array size" + jsonArray.length());
        save.setClickable(false);
        loadingDialog = FunctionUtils.showDialog(this);

//        Map<Object, Object> op = new HashMap<Object, Object>();
//        op.put("USERID", userId);
//        op.put("ACTIVITYID", id);
//        op.put("STATION", station);
//        op.put("PARTID", partId);


        Map<String, String> op = new HashMap<String, String>();
        op.put("USERID", userId);
        op.put("ACTIVITYID", id);
        op.put("STATION", station + "");
        op.put("PARTID", partId);

        op.put("ACTIVITYDETAIL", jsonArray.toString());

        OkHttpUtils.post().url(AppConfig.SUBMIN_ACTIVITY).params(op).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                FunctionUtils.closeDialog(loadingDialog);
                save.setClickable(true);
                Toast.makeText(ActivieDetailActivity.this, "网络异常，请连接网络后重试", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onResponse(String response) {
                if (response != null) {
                    try {
                        com.orhanobut.logger.Logger.json(response);
                        FunctionUtils.closeDialog(loadingDialog);
                        JSONObject obj = new JSONObject(response);
                        int status = obj.getInt("status");

                        if (status == 01) {

                            Logger.getInstance().e("result", response);

                            Toast.makeText(ActivieDetailActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                            ActivieDetailActivity.this.finish();


                        } else {

                            save.setClickable(true);
                            Toast.makeText(ActivieDetailActivity.this, "提交失败，请重试", Toast.LENGTH_SHORT).show();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ActivieDetailActivity.this, "提交失败，请重试" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        FunctionUtils.closeDialog(loadingDialog);
                        save.setClickable(true);
                        com.orhanobut.logger.Logger.e(e.getMessage());
                        //    Toast.makeText(ActivieDetailActivity.this, "提交失败，请重试", Toast.LENGTH_LONG).show();
                    }
                } else {
                    FunctionUtils.closeDialog(loadingDialog);
                    Toast.makeText(ActivieDetailActivity.this, "提交失败，请重试 ", Toast.LENGTH_SHORT).show();
                    com.orhanobut.logger.Logger.e("response 为null");
                    save.setClickable(true);
                    //    Toast.makeText(ActivieDetailActivity.this, "提交失败，请重试", Toast.LENGTH_LONG).show();

                }
            }
        });


    }

    private JSONArray getJsonArray() {
        JSONArray array = new JSONArray();
        int count = 0;
        for (int i = 0; i < itemList.size(); i++) {
            ActiveItemParam itemParam = itemList.get(i);
            List<ActiveContentParam> contents = itemParam.getContents();
            for (int j = 0; j < contents.size(); j++) {

                ActiveContentParam contentParam = contents.get(j);
                if (flagList.get(count) | !contentParam.isCompleted()) {
                    count++;
                    continue;
                }
                JSONObject obj = new JSONObject();
                try {
                    String images = "";
                    String imageTmp = "";
                    for (int k = 0; k < contentParam.getImagePaths().size(); k++) {
                        imageTmp = imageTmp + "AAAAAAAAA" + contentParam.getImagePaths().get(k);
                        images = images + Base64.encodeToString(getSmallBitmap(contentParam.getImagePaths().get(k)), Base64.DEFAULT) + ":";
                    }
                    com.orhanobut.logger.Logger.d("image size " + imageTmp);

                    obj.put("DESCRIPTION", contentParam.getDescribtion());
                    obj.put("IMAGES", images);
                    obj.put("TARID", itemParam.getId());
                    obj.put("DETAILLD", contentParam.getId());
                    obj.put("COMPLETED", contentParam.isCompleted() ? 1 : 0);
                    String cache = AppConfig.prefs.getString(contentParam.getId(), "");
                    if (!cache.equals("")) {
                        AppConfig.prefs.edit().remove(contentParam.getId()).commit();
                    }
                    array.put(obj);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                count++;

            }

        }

        return array;
    }

    private String getFilePath() {

        File file = new File(Environment.getExternalStorageDirectory() + File.separator + "activty");
        file.mkdir();
        Logger.getInstance().e("path", file.getPath());
        return file.getPath();

    }

    private String getPhotoFileName() {


        String name = "activity" + FunctionUtils.getDate() + ".jpg";
        imageName = name;
        return name;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            File temp = new File(getFilePath(), imageName);
            if (temp.length() > 0) {
                HashMap<Integer, Integer> child = new HashMap<>();
                child.put(childPosition, 1);
                adapter.getMap().put(groupPosition, child);
                itemList.get(groupPosition).getContents().get(childPosition).getImagePaths().add(temp.getPath());
                adapter.notifyDataSetChanged();
            }

        }

    }

    /**
     * 拍照
     */
    private void shartCameraAction() {

        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 下面这句指定调用相机拍照后的照片存储的路径
        i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(getFilePath(), getPhotoFileName())));
        startActivityForResult(i, 101);
    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        switch (arg0.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.save:
                if (adapter.getMap().size() == 0) {
                    if (itemList != null && itemList.size() > 0) {
                        submitActivite();
                    }
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("提示");
                    builder.setMessage("请先保存所有项目");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.create().show();
                }

                break;
            default:
                break;
        }
    }

    ActiveDetailAdapter.GridOnItemSelectedListener gridOnItemSelectedListener = new ActiveDetailAdapter.GridOnItemSelectedListener() {
        @Override
        public void onSelectedListener(int groupPosition, int childPosition, int gridPosition) {

            if (itemList.get(groupPosition).getContents().get(childPosition).getImagePaths().size() == gridPosition) {
                ActivieDetailActivity.this.groupPosition = groupPosition;
                ActivieDetailActivity.this.childPosition = childPosition;
                ActivieDetailActivity.this.gridPosition = gridPosition;
                shartCameraAction();
            }
        }
    };

    public byte[] getSmallBitmap(String filePath) {
        Bitmap photo = BitmapFactory.decodeFile(filePath);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] data = stream.toByteArray();
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(data, 0, data.length, options);

        options.inSampleSize = calculateInSampleSize(options, 300, 300);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        Bitmap map = BitmapFactory.decodeByteArray(data, 0, data.length,
                options);
        ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
        map.compress(Bitmap.CompressFormat.JPEG, 100, stream1);

        return stream1.toByteArray();
    }

    // 计算图片的缩放值
    public int calculateInSampleSize(BitmapFactory.Options options,
                                     int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

}
