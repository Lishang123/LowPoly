package com.xinxin.lowploy.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.xinxin.lowploy.R;
import com.xinxin.lowploy.core.LowPolyKey;
import com.xinxin.lowploy.core.StartLowPoly;
import com.xinxin.lowploy.util.SavePhoto;
import com.xinxin.lowploy.util.ShareUtils;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.xinxin.lowploy.R.id.bt_save;
import static com.xinxin.lowploy.R.id.iv;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.bt_share)
    Button mBtShare;
    @BindView(bt_save)
    Button mBtSave;
    @BindView(R.id.seekbar)
    SeekBar mSeekbar;
    @BindView(R.id.seekbar_count)
    TextView mSeekbarCount;
    @BindView(R.id.progressbar)
    public LinearLayout mProgressbar;
    @BindView(R.id.ll_result)
    public LinearLayout mLlResult;
    @BindView(R.id.ll_choose)
    public LinearLayout mLlChoose;
    @BindView(iv)
    public ImageView mIv;
    private static final int REQUEST_IMAGE_CODE = 12;

    String path;
    boolean isDone;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        setSupportActionBar(mToolbar);
        mSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                mSeekbarCount.setText(progress + 600 + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                LowPolyKey.pc = mSeekbar.getProgress() + 600;
            }
        });
    }

    @OnClick({R.id.bt_share, bt_save})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_share:
                if (path == null || path.length() == 0) {
                    path = SavePhoto.writePhoto(mIv.getDrawingCache());
                    SavePhoto.scanPhotos(path, MainActivity.this);
                    Snackbar.make(mIv, getString(R.string.have_saved_to) + path, Snackbar.LENGTH_LONG).show();
                }
                ShareUtils.shareImage(MainActivity.this, path);
                break;
            case bt_save:
                if (path == null || path.length() == 0) {
                    path = SavePhoto.writePhoto(mIv.getDrawingCache());
                    SavePhoto.scanPhotos(path, MainActivity.this);
                    Snackbar.make(mIv, getString(R.string.have_saved_to) + path, Snackbar.LENGTH_LONG).show();
                } else
                    Snackbar.make(mBtSave, R.string.the_image_saved, Snackbar.LENGTH_SHORT).show();
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_choose:
                path = null;
                isDone = false;
                mLlResult.setVisibility(View.INVISIBLE);
                mLlChoose.setVisibility(View.VISIBLE);
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_IMAGE_CODE);
                break;
            case R.id.action_done:
                if (uri != null) {
                    if (!isDone) {
                        new StartLowPoly(this, this, mIv).start();
                    } else Snackbar.make(mIv, R.string.have_finished, Snackbar.LENGTH_LONG).show();
                } else
                    Snackbar.make(mIv, R.string.choose_image_to_make, Snackbar.LENGTH_LONG).show();
                isDone = true;
                break;
//            case R.id.action_about:
//                //ShareUtils.shareUri(this, "http://www.hugeterry.cn/about");
//                break;
            case R.id.action_alert:
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
                builder.setTitle(getString(R.string.alert)).setMessage(getString(R.string.alert_message)).setPositiveButton(R.string.ok
                        , new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                builder.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CODE) {
            if (resultCode == RESULT_OK) {
                uri = data.getData();
                Bitmap bitmap;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    mIv.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
