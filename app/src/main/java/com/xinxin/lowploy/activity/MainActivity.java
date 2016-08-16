package com.xinxin.lowploy.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.xinxin.lowploy.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.bt_share)
    Button mBtShare;
    @BindView(R.id.bt_save)
    Button mBtSave;
    @BindView(R.id.seekbar)
    SeekBar mSeekbar;
    @BindView(R.id.seekbar_count)
    TextView mSeekbarCount;
    @BindView(R.id.progressbar)
    LinearLayout mProgressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.bt_share, R.id.bt_save})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_share:
                break;
            case R.id.bt_save:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_choose:
//                path = null;
//                isDone = false;
//                ll_result.setVisibility(View.INVISIBLE);
//                ll_choose.setVisibility(View.VISIBLE);
//                GetPhoto.gallery(MainActivity.this);
                break;
            case R.id.action_done:
//                if (uri != null) {
//                    if (!isDone) {
//                        new StartPolyFun(this, this, iv).start();
//                    } else Snackbar.make(iv, "已经制作完了，请重新选择图片", Snackbar.LENGTH_LONG).show();
//                } else Snackbar.make(iv, "请先选择图片再制作", Snackbar.LENGTH_LONG).show();
//                isDone = true;
                break;
            case R.id.action_about_me:
                //ShareUtils.shareUri(this, "http://www.hugeterry.cn/about");
                break;
            case R.id.action_tst:

                break;
        }
       return super.onOptionsItemSelected(item);
    }
}
