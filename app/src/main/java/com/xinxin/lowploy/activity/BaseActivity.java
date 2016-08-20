package com.xinxin.lowploy.activity;

import android.support.v7.app.AppCompatActivity;

import com.umeng.analytics.MobclickAgent;

/**
 * TODO:
 * Author: Tianxin
 * Date: 2016-08-19
 * Time: 09:58
 */

public class BaseActivity extends AppCompatActivity {
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
