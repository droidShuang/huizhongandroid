/**
 * @Title: ActivityChangePassword.java
 * @Package com.loongjoy.huizhong.activity
 * @Description: TODO
 * Copyright: Copyright 2015 loongjoy.inc
 * Company:上海龙照电子有限公司
 * @author hao_mo@loongjoy.com
 * @date 2015-9-1 下午3:49:01
 * @version V1.0
 */

package com.junova.huizhong.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.junova.huizhong.AppConfig;
import com.junova.huizhong.R;
import com.junova.huizhong.common.FunctionUtils;
import com.junova.huizhong.common.Logger;
import com.junova.huizhong.common.http.AsyncHttpConnection;
import com.junova.huizhong.common.http.AsyncHttpConnection.CallbackListener;
import com.junova.huizhong.common.http.HttpMethod;
import com.junova.huizhong.db.CheckDataBase;
import com.junova.huizhong.db.DeviceDataBase;
import com.junova.huizhong.widget.LoadingDialog;
import com.junova.huizhong.widget.LoginOutDialog;

/**
 * @author hao_mo@loongjoy.com
 * @ClassName: ActivityChangePassword
 * @Description: TODO
 * @date 2015-9-1 下午3:49:01
 */

public class ChangePasswordActivity extends Activity implements OnClickListener {
    Button back;
    Button submit;
    EditText oldPwd;
    EditText newPwd;
    EditText confirm;
    LoadingDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_psd);
        initView();
    }

    private void initView() {
        back = (Button) findViewById(R.id.back);
        submit = (Button) findViewById(R.id.btn_sure);
        oldPwd = (EditText) findViewById(R.id.old_psd);
        newPwd = (EditText) findViewById(R.id.new_psd);
        confirm = (EditText) findViewById(R.id.new_psd_confirm);
        back.setOnClickListener(this);
        submit.setOnClickListener(this);

    }

    private void updataPwd(String oldPwd, String newPwd) {
        dialog = FunctionUtils.showDialog(this);
        Map<Object, Object> op = new HashMap<Object, Object>();
        op.put("USERID", AppConfig.prefs.getString("userId", "0"));
        op.put("OLDPWD", oldPwd);
        op.put("NEWPWD", newPwd);
        op.put("NUMBERCODE", AppConfig.prefs.getString("numberCode", ""));

        new AsyncHttpConnection().post(AppConfig.UP_DATA_USER_PWD,
                HttpMethod.getParams(getApplicationContext(), op),
                new CallbackListener() {
                    @Override
                    public void callBack(String result) {
                        FunctionUtils.closeDialog(dialog);
                        com.orhanobut.logger.Logger.d(result);
                        Logger.getInstance().e("result", result);
                        if (result != null) {
                            try {
                                JSONObject obj = new JSONObject(result);
                                int status = obj.getInt("status");
                                if (status == 01) {
                                    Toast toast = Toast.makeText(getApplicationContext(),
                                            "修改密码成功", Toast.LENGTH_SHORT);
                                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                                    toast.show();
                                    AppConfig.prefs.edit().clear().commit();
                                    AppConfig.uploadState = 0;
                                    CheckDataBase dataBase = new CheckDataBase(getApplicationContext());
                                    dataBase.clear();
                                    DeviceDataBase deviceDataBase = new DeviceDataBase(getApplicationContext());
                                    deviceDataBase.clear();
                                    Intent intent = new Intent(ChangePasswordActivity.this, LoginActivity.class);
                                    ChangePasswordActivity.this.startActivity(intent);
                                    ((Activity) ChangePasswordActivity.this).finish();
                                } else {

                                    Toast toast = Toast.makeText(getApplicationContext(),
                                            obj.getString("message"), Toast.LENGTH_SHORT);
                                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                                    toast.show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.btn_sure:
                String op = oldPwd.getText().toString().trim();
                String np = newPwd.getText().toString().trim();
                String cp = confirm.getText().toString().trim();
                if ("".equals(np) || "".equals(cp)) {

                    Toast toast = Toast.makeText(getApplicationContext(), "密码不能为空",
                            Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    toast.show();
                    break;
                }
                if (np.equals(cp)) {
                    updataPwd(op, np);
                } else {

                    Toast toast = Toast.makeText(getApplicationContext(), "两次输入的密码不一致",
                            Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    toast.show();
                }

                break;
            default:
                break;
        }
    }

}
