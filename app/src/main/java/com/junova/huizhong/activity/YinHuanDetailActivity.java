package com.junova.huizhong.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.logging.Handler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.junova.huizhong.AppConfig;
import com.junova.huizhong.R;
import com.junova.huizhong.adapter.AddYichangGridAdapter;
import com.junova.huizhong.adapter.UnusualAdapter;
import com.junova.huizhong.common.FileUtil;
import com.junova.huizhong.common.FunctionUtils;
import com.junova.huizhong.common.Logger;
import com.junova.huizhong.common.http.AsyncHttpConnection;
import com.junova.huizhong.common.http.AsyncHttpConnection.CallbackListener;
import com.junova.huizhong.common.http.HttpMethod;
import com.junova.huizhong.db.CheckDataBase;
import com.junova.huizhong.db.DeviceDataBase;
import com.junova.huizhong.model.CheckParam;
import com.junova.huizhong.model.DeviceParam;
import com.junova.huizhong.model.ExceptionParam;
import com.junova.huizhong.model.UpLoadCheckModel;
import com.junova.huizhong.widget.LoadingDialog;
import com.junova.huizhong.widget.PicGridView;

public class YinHuanDetailActivity extends Activity implements OnClickListener {
    Button back;
    Button commit;
    TextView title;
    TextView name;
    TextView id;
    TextView summary;
    TextView status;
    LinearLayout checkLayout;
    String deviceId;
    String check = "";
    String picName = "";//从相册中取出来图片的名称
    private Map<Integer, String> saveNumber;

    int sts;
    private List<CheckParam> checkParamList = new ArrayList<CheckParam>();
    public List<List<String>> picHashMap = new ArrayList<List<String>>();
    public List<AddYichangGridAdapter> adapters = new ArrayList<AddYichangGridAdapter>();
    private boolean isPhoto;


    private List<UpLoadCheckModel> upLoadCheckList;

    int postion = 0;
    int type;// 0 隐患排查，1 a
    String partId;
    boolean leader;
    boolean isClecked = false;

    HashMap<String, Integer> postionOfGrid = new HashMap<String, Integer>();

    private LoadingDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yinhuan_detail);
        dialog = FunctionUtils.showDialog(this);
        saveNumber = new HashMap<>();
        initViews();

    }

    private void initViews() {
        back = (Button) findViewById(R.id.back);
        commit = (Button) findViewById(R.id.commit);
        back.setOnClickListener(this);
        commit.setOnClickListener(this);
        title = (TextView) findViewById(R.id.title);
        title.setText(getIntent().getStringExtra("title"));
        name = (TextView) findViewById(R.id.name);

        id = (TextView) findViewById(R.id.id);

        summary = (TextView) findViewById(R.id.summary);

        status = (TextView) findViewById(R.id.status);
        checkLayout = (LinearLayout) findViewById(R.id.checkLayout);
        deviceId = getIntent().getStringExtra("id");
        leader = getIntent().getBooleanExtra("leader", false);
        // if (leader) {
        // title.setText(getResources().getString(R.string.yiqi));
        // }
        type = getIntent().getIntExtra("type", 0);
        partId = getIntent().getStringExtra("partId");
        sts = getIntent().getIntExtra("sts", -1);
        if (sts == 0) {
            status.setText("待检查");
        } else if (sts == 1) {
            status.setText("正常");
        } else if (sts == 2) {
            status.setText("异常");
        }

        Logger.getInstance().e("sts", " status: " + sts);

        DeviceDataBase db1 = new DeviceDataBase(getApplicationContext());
        checkParamList = db1.getCheckItems(deviceId, AppConfig.prefs.getString("userId", "0"));
        if (checkParamList != null & checkParamList.size() > 0) {
            CheckDataBase db = new CheckDataBase(this);
            upLoadCheckList = db.getCheckedItem(partId, deviceId);

            for (int i = 0; i < upLoadCheckList.size(); i++) {
                String[] pics = upLoadCheckList.get(i).getPics().split(",");
                List<String> pic = new ArrayList<String>();
                for (String string : pics) {
                    if (!FunctionUtils.isBlank(string)) {
                        Logger.getInstance().e("pics", "pic ------------- " + string);
                        pic.add(getFilePath() + "/" + string);
                    }
                }
                picHashMap.add(pic);


                Logger.getInstance().e("picHashMap", " picHashMap " + picHashMap.size());
            }
            initList();
            FunctionUtils.closeDialog(dialog);
        } else {
            getDeviceDetail();
        }

        // initList();

        if (type == 0) {
            title.setText("仪器检测");
        } else {
            title.setText("隐患排查");
        }
        summary.setText(getIntent().getStringExtra("SUMMARY"));
        id.setText(getIntent().getStringExtra("SPECLD"));
        name.setText(getIntent().getStringExtra("TITLE"));
    }


    private void initList() {
        // getList();
        checkLayout.removeAllViews();
        String erroDesc;
        for (int i = 0; i < checkParamList.size(); i++) {
            final CheckParam cp = checkParamList.get(i);

            if (sts == 0 || sts == 10) {
                picHashMap.add(new ArrayList<String>());
            }
            final int cookiesI = i;

            final View v = LayoutInflater.from(this).inflate(R.layout.check_item, null);
            TextView name = (TextView) v.findViewById(R.id.item_name);

            name.setText(cp.getName());
            final LinearLayout expandLayout = (LinearLayout) v.findViewById(R.id.layout_expand);
            final LinearLayout photoLayout = (LinearLayout) v.findViewById(R.id.photo_layout);
            if (type == 1) {
                photoLayout.setVisibility(View.VISIBLE);
            }
            final EditText etErro = (EditText) v.findViewById(R.id.et_erro);
            try {
                etErro.setText(upLoadCheckList.get(i).getErroDesc());
            } catch (Exception e) {

            }

            RadioGroup rg = (RadioGroup) v.findViewById(R.id.radioGroup);
            Button btSave = (Button) v.findViewById(R.id.bt_save);
            rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(RadioGroup arg0, int arg1) {
                    switch (arg1) {
                        case R.id.rg_normal:
                            if (saveNumber.containsKey(cookiesI)) {
                                saveNumber.remove(cookiesI);
//                                CheckDataBase db = new CheckDataBase(getApplicationContext());

//                                long result = db.updateCheck(deviceId, cp.getId(), "0", cp.getName(),
//                                        cp.getStatus(), AppConfig.UN_SUMBITED, FunctionUtils.getDate(), "", "0", type,
//                                        partId, "");
                            }
                            List<ExceptionParam> exceptionParamList = cp.getExceptions();
                            for (ExceptionParam param :
                                    exceptionParamList) {
                                if (param.isChecked()) {
                                    param.setDesc("");
                                    param.setPhotoes("");
                                    param.setChecked(false);
                                }


                            }
                            etErro.setText("");
                            picHashMap.get(cookiesI).clear();
                            expandLayout.setVisibility(View.GONE);
                            cp.setStatus(leader ? 1 : 1);
                            break;
                        case R.id.rg_abnormal:
                            for (ExceptionParam param :
                                    cp.getExceptions()) {
                                if (param.getId().equals("0")) {
                                    param.setChecked(true);
                                }


                            }
                            expandLayout.setVisibility(View.VISIBLE);
                            cp.setStatus(leader ? 2 : 2);
                            initGridData("", postion, adapters.get(postion));
                            break;
                        default:
                            break;
                    }
                    // CheckDataBase db = new
                    // CheckDataBase(getApplicationContext());
                    // db.deleteRecord(cp.getId(), partId);
                }
            });
            btSave.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    //    CheckDataBase db = new CheckDataBase(getApplicationContext());
                    List<ExceptionParam> exceptionParamList = cp.getExceptions();
                    for (ExceptionParam param :
                            exceptionParamList) {
                        if (param.isChecked()) {
                            param.setDesc(etErro.getText().toString());
                            saveNumber.put(cookiesI, "saveed");
                            Toast toast = Toast.makeText(YinHuanDetailActivity.this, "保存成功", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                            toast.show();
                        }


                    }
                }
            });
            final PicGridView gridView = (PicGridView) v.findViewById(R.id.photo_grid);
            AddYichangGridAdapter adapter;
            try {
                adapter = new AddYichangGridAdapter(this, picHashMap.get(i), gridView);
            } catch (IndexOutOfBoundsException ex) {
                picHashMap.add(new ArrayList<String>());
                adapter = new AddYichangGridAdapter(this, picHashMap.get(i), gridView);
            }

            postionOfGrid.put("exId" + cookiesI, i);
            gridView.setTag(i);
            gridView.setAdapter(adapter);
            adapters.add(adapter);
            gridView.setOnItemClickListener(new itemClickListener(gridView, cookiesI + "", i));
            String error = "0";

            if (sts != 0) {
                if (upLoadCheckList != null) {
                    if (upLoadCheckList.size() > 0 && upLoadCheckList.get(i).getStatus() == 1) {
                        RadioButton childAt = (RadioButton) rg.getChildAt(0);
                        childAt.setChecked(true);
                        rg.check(R.id.rg_normal);
                        error = upLoadCheckList.get(i).getErrorId();
                    } else {
                        RadioButton childAt = (RadioButton) rg.getChildAt(1);
                        childAt.setChecked(true);
                     //   saveNumber.put(cookiesI, "saveed");
                        rg.check(R.id.rg_abnormal);

                    }
                }

            }

            Logger.getInstance().e("error", "error:" + error);
            String errorId = error;
            List<ExceptionParam> exceptions = cp.getExceptions();

            final TextView choose = (TextView) v.findViewById(R.id.yc_choose);
            for (int m = 0; m < exceptions.size(); m++) {
                ExceptionParam params = exceptions.get(m);
                if (params != null) {

                    if (errorId.equals(params.getId())) {
                        Log.d("CHECKERRO", "exception name  " + params.getName());
                        choose.setText(params.getName());
                        //      if (type == 0) {
                        if (choose.getText().toString().trim().equals("其他")) {
                            // postionOfGrid = m;
                            photoLayout.setVisibility(View.VISIBLE);
                        } else {
                            photoLayout.setVisibility(View.INVISIBLE);
                        }
                        //       }
                        params.setChecked(true);
                    }
                }

            }
            choose.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.pop_yichang_type, null);
                    final PopupWindow pw = new PopupWindow(view, 200, LayoutParams.WRAP_CONTENT);
                    // 设置点击外部可以消失
                    pw.setBackgroundDrawable(new BitmapDrawable());
                    pw.setOutsideTouchable(true);
                    pw.setFocusable(true);
                    pw.showAsDropDown(choose);
                    final List<ExceptionParam> exceptions = cp.getExceptions();

                    ListView lV = (ListView) view.findViewById(R.id.type_list);
                    final UnusualAdapter adapter = new UnusualAdapter(exceptions, getApplicationContext());
                    lV.setAdapter(adapter);

                    Drawable drawable = getResources().getDrawable(R.drawable.xuanze_white_2);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    choose.setCompoundDrawables(null, null, drawable, null);
                    lV.setOnItemClickListener(new OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                            for (ExceptionParam exceptionParam : exceptions) {
                                exceptionParam.setChecked(false);
                            }
                            exceptions.get(arg2).setChecked(true);
                            choose.setText(exceptions.get(arg2).getName());

                            Drawable drawable = getResources().getDrawable(R.drawable.xuanze_white_1);
                            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                            choose.setCompoundDrawables(null, null, drawable, null);
                            pw.dismiss();
                            //     if (type == 0) {
                            if (choose.getText().toString().trim().equals("其他") | choose.getText().toString().trim().equals("否决项")) {
                                postionOfGrid.put("exId" + cookiesI, arg2);
                                photoLayout.setVisibility(View.VISIBLE);
                            } else {
                                photoLayout.setVisibility(View.INVISIBLE);
                            }
                            //     }
                        }
                    });

                }
            });

            checkLayout.addView(v);
        }


    }

    /**
     * 拍照
     */
    private void shartCameraAction(final int postionOfGrid, final String id, final String uuid) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = View.inflate(this, R.layout.dialog_take_photo, null);
        Button takePhoto = (Button) dialogView.findViewById(R.id.layout_bt_takephoto);
        Button albumPhoto = (Button) dialogView.findViewById(R.id.layout_bt_album);
        builder.setView(dialogView);

        final Dialog dialog = builder.create();

        dialog.show();
        //拍照
        takePhoto.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // 下面这句指定调用相机拍照后的照片存储的路径
                i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(getFilePath(), getPhotoFileName(postionOfGrid, id, uuid))));
                dialog.cancel();
                startActivityForResult(i, 101);
                isPhoto = true;
            }
        });
        //  从相册获取
        albumPhoto.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");//相片类型
                dialog.cancel();
                picName = getPhotoFileName(postionOfGrid, id, uuid);
                startActivityForResult(intent, 100);

            }
        });


    }

    private String getFilePath() {

        File file = new File(Environment.getExternalStorageDirectory() + File.separator + AppConfig.PIC_FILEDIR);
        file.mkdir();
        Logger.getInstance().e("path", file.getPath());
        return file.getPath();

    }

    private String getAbsolutePath(Uri uri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = YinHuanDetailActivity.this.managedQuery(uri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        if (column_index == -1) {
            return null;
        }
        String path = cursor.getString(column_index);
        return path;
    }

    private String getPhotoFileName(int postionOfGrid, String id, String uuid) {
        Log.e("postion", "postion" + postionOfGrid);

        String name = uuid + "_" + FunctionUtils.getDate() + ".jpg";
        AppConfig.fileName = name;
        return name;
    }

    public void initGridData(String path, int position, AddYichangGridAdapter adapter) {
        if (!path.equals("")) {
            picHashMap.get(position).add(path);
        }
        Logger.getInstance().e("lenth", picHashMap.get(position).size() + "");
        adapter.notifyDataSetChanged();
        Log.d("YANGSHUANG", adapter.getCount() + "count");
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        if (AppConfig.hasphoto) {
            File temp = new File(getFilePath(), AppConfig.fileName);
            List<String> path = picHashMap.get(AppConfig.position);
            path.add(temp.getPath());
            AppConfig.hasphoto = false;
            // adapter.notifyDataSetChanged();
            // adapter.setNotify();
            adapters.get(AppConfig.position).notifyDataSetChanged();
        }

    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent data) {
        // TODO Auto-generated method stub

        switch (arg0) {
            //从相册获取图片
            case 100:

                try {
                    Uri uri = data.getData();
                    String[] proj = {MediaStore.Images.Media.DATA};

                    Cursor actualimagecursor = managedQuery(uri, proj, null, null, null);

                    int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

                    actualimagecursor.moveToFirst();

                    String img_path = actualimagecursor.getString(actual_image_column_index);
                    File file = new File(img_path);

                    String picPath = img_path.substring(0, img_path.lastIndexOf("/"));
                    File newFile = new File(getFilePath() + File.separator + picName);
                    FileUtil.copyFile(file, newFile);
                    initGridData(newFile.getPath(), AppConfig.position, adapters.get(AppConfig.position));

                } catch (Exception e) {
                    // TODO: handle exception
                }

                break;
            //从相机获取图片
            case 101:

                File temp = new File(getFilePath(), AppConfig.fileName);
                Logger.getInstance().e("path", temp.getPath());
                // List<String> pass = picHashMap.get(AppConfig.position);
                // pass.add(temp.getPath());
                if (temp.length() > 0) {

                    initGridData(temp.getPath(), AppConfig.position, adapters.get(AppConfig.position));
                }
                // adapter.notifyDataSetChanged();
                // adapter.setNotify();
                break;

            default:
                break;
        }

        super.onActivityResult(arg0, arg1, data);
    }

    @Override
    public void onBackPressed() {

        if (saveCheck() == 1)
            finish();
    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        switch (arg0.getId()) {
            case R.id.back:

                if (saveCheck() == 1)
                    finish();
                break;
            case R.id.commit:
                if( saveCheck()==1)
                {
                    Set<String> deviceSet = AppConfig.prefs.getStringSet("checkDevice", new HashSet<String>());
                    deviceSet.add(deviceId);
                    AppConfig.prefs.edit().putStringSet("checkDevice", deviceSet).commit();
                    if (getUnCheckedDevicesCount() == 0 & getDeviceCount() == deviceSet.size()) {
                        AppConfig.prefs.edit().putBoolean("canUp", true).commit();
                    }
                    finish();
                }


                break;
            default:
                break;
        }
    }

    private void getDeviceDetail() {


        Map<Object, Object> op = new HashMap<Object, Object>();
        //  op.put("USERID", AppConfig.prefs.getString("userId", "0"));
        op.put("DEVICEID", deviceId);//cf0e4fc6ed764b9c97c682d94a090137
        //     op.put("STATION", AppConfig.prefs.getInt("station", 0));
        new AsyncHttpConnection().post(AppConfig.GET_DEVICE_DETAIL, HttpMethod.getParams(getApplicationContext(), op),
                new CallbackListener() {

                    @Override
                    public void callBack(String result) {
                        Logger.getInstance().e("result", result);
                        FunctionUtils.closeDialog(dialog);
                        Log.d("YANGSHUANG", "callBack: " + result);
                        if (result != null) {
                            try {
                                JSONObject obj = new JSONObject(result);
                                int status = obj.getInt("status");
                                if (status == 01) {
                                    jsonDeciceDetail(obj);
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


    private void jsonDeciceDetail(JSONObject obj) {
        DeviceDataBase db = new DeviceDataBase(getApplicationContext());
        try {
            JSONObject data = obj.getJSONObject("data");
            String title = data.getString("TITLE");
            String specId = data.getString("SPECLD");
            String summary = data.getString("SUMMARY");
            int status = data.getInt("STATUS");
            name.setText(title);
            id.setText(specId);
            this.summary.setText(summary);
            JSONArray checkItems = data.getJSONArray("CHECKITEM");
            //   db.clearError();
            //   db.clearCheckItem(AppConfig.prefs.getString("userId", "0"));

            for (int i = 0; i < checkItems.length(); i++) {
                JSONObject item = checkItems.getJSONObject(i);
                String itemId = item.getString("ID");
                int itemStatus = item.getInt("STATUS");
                String itemName = item.getString("NAME");
                JSONArray exceptions = item.getJSONArray("EXCEPTION");

                List<ExceptionParam> exceptionList = new ArrayList<ExceptionParam>();
                exceptionList.add(new ExceptionParam("0", "其他", false, "", ""));

                db.addError("0", itemId, "其他");

                for (int j = 0; j < exceptions.length(); j++) {
                    JSONObject exception = exceptions.getJSONObject(j);
                    String exceptionId = exception.getString("ID");
                    String exceptionName = exception.getString("NAME");
                    boolean isChecked = false;
                    // if (exception.getInt("ISCHECK") == 1) {
                    //     isChecked = true;
                    //    }
                    //    String photo = exception.getString("PHOTO");
                    String photo = "";
                    //     String desc = exception.getString("DESC");
                    String desc = "";
                    exceptionList.add(new ExceptionParam(exceptionId, exceptionName, isChecked, photo, desc));
                    db.addError(exceptionId, itemId, exceptionName);
                }
                //     exceptionList.add(new ExceptionParam("0", "其他", false, "", ""));

                checkParamList.add(new CheckParam(itemId, itemName, 1, exceptionList, UUID.randomUUID().toString().replace("-", "")));
                db.addCheck(deviceId, itemId, itemName, AppConfig.prefs.getString("userId", "0"));
            }


            switch (sts) {
                case 0:
                    check = getResources().getString(R.string.daijiancha);
                    break;
                case 1:
                    check = getResources().getString(R.string.xuncha_normal);
                    break;
                case 2:
                    check = getResources().getString(R.string.patrol);
                    break;
                case 10:
                    check = getResources().getString(R.string.daichoucha);
                    break;
                case 11:
                    check = getResources().getString(R.string.choucha_normal);
                    break;
                case 12:
                    check = getResources().getString(R.string.spotcheck);
                    break;
                default:
                    break;
            }

            this.status.setText(check);
            if (checkParamList.size() > 0) {
                initList();
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private int saveCheck() {
        if (checkParamList != null && checkParamList.size() > 0) {
            List<Integer> error = new ArrayList<>();
            for (CheckParam param : checkParamList) {

                if (param.getStatus() == 2) {
                    error.add(0);
                }
            }
            if (error.size() != saveNumber.size()) {

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
                return 0;
            }
            int errorCount = 0;
            CheckDataBase db = new CheckDataBase(getApplicationContext());
            for (int i = 0; i < checkParamList.size(); i++) {
                List<String> pathList = picHashMap.get(i);
                String pics = "";
                for (int k = 0; k < pathList.size(); k++) {
                    String[] split = pathList.get(k).split("/");
                    pics = pics + split[split.length - 1];
                    if (k != pathList.size() - 1) {
                        pics = pics + ",";
                    }
                }
                CheckParam checkParam = checkParamList.get(i);
                int status = checkParam.getStatus();
                if (status == 2) {
                    errorCount++;
                } else if (status == 0) {


                    Toast toast = Toast.makeText(getApplicationContext(), "请先检测所有项目", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    toast.show();
                    return 0;
                }

                boolean isAbNormal = false;
                List<ExceptionParam> exceptions = checkParam.getExceptions();
                for (ExceptionParam exceptionParam : exceptions) {
                    if (exceptionParam.isChecked()) {
                        isAbNormal = true;
                        break;
                    }
                }
                //异常
                if (isAbNormal) {
                    if (sts != 0) {
                        for (int j = 0; j < exceptions.size(); j++) {
                            ExceptionParam exceptionParam = exceptions.get(j);
                            if (exceptionParam.isChecked()) {
                                long result = db.updateCheck(deviceId, checkParam.getId(), exceptionParam.getId(),
                                        checkParam.getName(), checkParam.getStatus(), AppConfig.UN_SUMBITED,
                                        FunctionUtils.getDate(), pics, "0", type, partId, exceptionParam.getDesc());
                                Logger.getInstance().e("addChech", "name    " + checkParam.getName() + "    exceptionParam" + exceptionParam.getId() + "    pics    " + pics);

                            }

                        }
                    } else {

                        for (int j = 0; j < exceptions.size(); j++) {

                            ExceptionParam exceptionParam = exceptions.get(j);
                            if (exceptionParam.isChecked()) {
                                long result = db.addCheck(deviceId, checkParam.getId(), exceptionParam.getId(),
                                        checkParam.getName(), checkParam.getStatus(), AppConfig.UN_SUMBITED,
                                        FunctionUtils.getDate(), pics,
                                        "0", type, partId, checkParam.getPARTRECORDID(), exceptionParam.getDesc());
                                Logger.getInstance().e("addChech", "name    " + checkParam.getName() + "    exceptionParam" + exceptionParam.getId() + "    pics    " + pics);

                            }

                        }
                    }
                } else {
                    if (sts != 0) {
                        long result = db.updateCheck(deviceId, checkParam.getId(), "0", checkParam.getName(),
                                checkParam.getStatus(), AppConfig.UN_SUMBITED, FunctionUtils.getDate(), "", "0", type,
                                partId, "");
                        Logger.getInstance().e("addChech", result + "update  Normal ");
                    } else {
                        long result = db.addCheck(deviceId, checkParam.getId(), "0", checkParam.getName(),
                                checkParam.getStatus(), AppConfig.UN_SUMBITED, FunctionUtils.getDate(), "", "0", type,
                                partId, checkParam.getPARTRECORDID(), "");
                        Logger.getInstance().e("addChech", result + "add  Normal ");

                    }
                }


            }
            final Intent intent = new Intent();

            if (errorCount > 0) {
                if (leader) {
                    intent.putExtra("status", 2);

                } else {
                    intent.putExtra("status", 2);
                }

            } else {
                if (leader) {
                    intent.putExtra("status", 1);
                } else {
                    intent.putExtra("status", 1);
                }
            }
            if (getIntent().getIntExtra("position", -1) != -1) {
                intent.putExtra("position", getIntent().getIntExtra("position", -1));
            }
            Intent i = new Intent("com.junova.huizhong");
            i.putExtra("type", 1);
            i.putExtra("deviceId", deviceId);
            i.putExtra("status", intent.getIntExtra("status", 0));
            sendBroadcast(i);
            intent.putExtra("deviceId", deviceId);
            setResult(1001, intent);
            setStatus(deviceId, intent.getIntExtra("status", 0));


        }
        return 1;

    }

    public void setStatus(String deviceId, int status) {
        Logger.getInstance().e("ssssss", deviceId + "    " + status);
        DeviceDataBase db = new DeviceDataBase(getApplicationContext());
        long result = db.updateStatus(deviceId, status, AppConfig.prefs.getString("userId", "0"));
        Logger.getInstance().e("status", result + " set status ");


    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();


    }
    /** */
    /**
     * 文件重命名
     *
     * @param path    文件目录
     * @param oldname 原来的文件名
     * @param newname 新文件名
     */
    public void renameFile(String path, String oldname, String newname) {
        if (!oldname.equals(newname)) {//新的文件名和以前文件名不同时,才有必要进行重命名
            File oldfile = new File(path + "/" + oldname);
            File newfile = new File(path + "/" + newname);
            if (!oldfile.exists()) {
                return;//重命名文件不存在
            }
            if (newfile.exists())//若在该目录下已经有一个文件和新文件名相同，则不允许重命名
                System.out.println(newname + "已经存在！");
            else {
                oldfile.renameTo(newfile);
                Uri localUri = Uri.fromFile(newfile);

                Intent localIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, localUri);

                sendBroadcast(localIntent);
            }
        } else {
            System.out.println("新文件名和旧文件名相同...");
        }
    }

    class itemClickListener implements OnItemClickListener {
        GridView gridView;
        String cookiesI;
        String id;
        int pos;


        public itemClickListener(GridView gridView, String cookiesI, int pos) {
            this.gridView = gridView;
            this.cookiesI = cookiesI;

            this.pos = pos;

        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            postion = (Integer) gridView.getTag();
            AppConfig.position = postion;
            if (position > 0)
                return;
            Log.d("YANGSHUANG", "test		");
            if (position == picHashMap.get(AppConfig.position).size()) {
                Log.d("YANGSHUANG", "test		" + postionOfGrid.get("exId" + cookiesI));

                shartCameraAction(postionOfGrid.get("exId" + cookiesI), checkParamList.get(pos).getId(), checkParamList.get(pos).getPARTRECORDID());

            }
        }
    }

    /**
     * getUnCheckedDevicesCount 作用 获取未检测设备数 TODO(描述)
     *
     * @param @return
     * @return int
     * @throws
     * @Title: getUnCheckedDevicesCount
     * @Description: TODO
     * @author hao_mo@loongjoy.com
     */
    public int getUnCheckedDevicesCount() {
        int count = 0;
        DeviceDataBase db = new DeviceDataBase(getApplicationContext());
        List<DeviceParam> list = db
                .getDevices(AppConfig.prefs.getString("partId", "0"), AppConfig.prefs.getString("userId", "0"));
        for (int i = 0; i < list.size(); i++) {
            DeviceParam param = list.get(i);
            if (param.getStatus() == 0 || param.getStatus() == 10) {
                count++;
            }
        }
        Logger.getInstance().e("count", count);
        return count;
    }

    /**
     * @author rider
     * Created on 2016/8/11 0011 9:41
     * Description :获取检测设备数
     */
    public int getDeviceCount() {
        int count = 0;
        CheckDataBase db = new CheckDataBase(getApplicationContext());
        count = db.getCheckCount();
        return count;
    }


}
