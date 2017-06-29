package com.junova.huizhong.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.LinearLayout;

import com.junova.huizhong.AppConfig;
import com.junova.huizhong.R;
import com.junova.huizhong.common.FunctionUtils;

public class StartActivity extends Activity {

    private LinearLayout startLayout;
    private AlphaAnimation showMainActivityAnimation;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        activity = this;
        findViewIds();
        goMainActivity();
    }

    private void findViewIds() {
        startLayout = (LinearLayout) this.findViewById(R.id.startLayout);
        FunctionUtils.getScreenSize(this);

        String mobilePhone = FunctionUtils.getPhoneNumber(this);
        if (!FunctionUtils.isBlank(mobilePhone)) {
            SharedPreferences.Editor editor = AppConfig.prefs.edit();
            editor.putString("mobilePhone", mobilePhone);
            editor.commit();
        }
    }

    private void goMainActivity() {
        showMainActivityAnimation = new AlphaAnimation(1.0f, 1.0f);
        showMainActivityAnimation.setDuration(3000);
        startLayout.startAnimation(showMainActivityAnimation);
        showMainActivityAnimation.setAnimationListener(new AnimationListener() {
            public void onAnimationEnd(Animation animation) {
                startMainActivity();
            }

            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    private void startMainActivity() {
        if (FunctionUtils.isNetworkAvailable(this)) {
            if (AppConfig.prefs.getString("userId", "0").equals("0")) {
                overridePendingTransition(R.anim.push_left_in,
                        R.anim.push_left_out);
                startActivity(new Intent(activity, LoginActivity.class));
                finish();

            } else {
                startActivity(new Intent(this, MainActivity.class));
                overridePendingTransition(R.anim.push_left_in,
                        R.anim.push_left_out);
                finish();

            }
        } else {


            if (!AppConfig.prefs.getString("userId", "").equals("")) {
                startActivity(new Intent(this, MainActivity.class));
                overridePendingTransition(R.anim.push_left_in,
                        R.anim.push_left_out);
                finish();
            } else {
                overridePendingTransition(R.anim.push_left_in,
                        R.anim.push_left_out);
                startActivity(new Intent(activity, LoginActivity.class));
                finish();
            }
        }
    }

}
