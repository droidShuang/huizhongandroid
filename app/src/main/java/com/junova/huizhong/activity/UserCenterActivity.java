/**
 * @Title: UserCenterActivity.java
 * @Package com.loongjoy.huizhong.activity
 * @Description: TODO
 * Copyright: Copyright 2015 loongjoy.inc
 * Company:上海龙照电子有限公司
 * @author hao_mo@loongjoy.com
 * @date 2015-9-17 上午10:52:53
 * @version V1.0
 */

package com.junova.huizhong.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.junova.huizhong.AppConfig;
import com.junova.huizhong.R;
import com.junova.huizhong.common.FunctionUtils;
import com.junova.huizhong.db.CheckDataBase;
import com.junova.huizhong.db.DeviceDataBase;
import com.junova.huizhong.widget.LoginOutDialog;

/**
 * @author hao_mo@loongjoy.com
 * @ClassName: UserCenterActivity
 * @Description: TODO
 * @date 2015-9-17 上午10:52:53
 */

public class UserCenterActivity extends Activity implements OnClickListener {
    Button back;
    TextView date;
    TextView name;
    RelativeLayout changePwd;
    RelativeLayout loginOut;
    RelativeLayout changeFactory;
    TextView txVersion;

	/*
     * <p>Title: onCreate</p> <p>Description: </p>
	 * 
	 * @param savedInstanceState
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        initView();
    }

    private void initView() {
        back = (Button) findViewById(R.id.back);
        date = (TextView) findViewById(R.id.date);
        txVersion = (TextView) findViewById(R.id.user_tx_version);
        String dateString = FunctionUtils.getDate();
        date.setText(dateString.substring(0, 4) + "-"
                + dateString.substring(4, 6) + "-" + dateString.substring(6, 8));
        name = (TextView) findViewById(R.id.name);
        name.setText(AppConfig.prefs.getString("name", ""));
        changePwd = (RelativeLayout) findViewById(R.id.change_pwd);
        loginOut = (RelativeLayout) findViewById(R.id.login_out);
        changeFactory = (RelativeLayout) findViewById(R.id.login_chanege);
        changePwd.setOnClickListener(this);
        loginOut.setOnClickListener(this);
        back.setOnClickListener(this);
        txVersion.setText(AppConfig.version);
//        if (AppConfig.prefs.getInt("station", -1) == 0) {
//            changeFactory.setVisibility(View.VISIBLE);
//            changeFactory.setOnClickListener(this);
//        }
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.change_pwd:
                Intent intent = new Intent(this, ChangePasswordActivity.class);
                startActivity(intent);
                break;
            case R.id.login_out:
                new LoginOutDialog(UserCenterActivity.this).show();
                break;
            case R.id.login_chanege:
                Intent intent1 = new Intent(this, ChooseFactoryActivity.class);
                startActivity(intent1);
                break;
            default:
                break;
        }
    }
}
