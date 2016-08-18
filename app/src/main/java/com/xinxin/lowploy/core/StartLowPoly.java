package com.xinxin.lowploy.core;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.xinxin.lowploy.Bean.MyPoint;
import com.xinxin.lowploy.activity.MainActivity;
import com.xinxin.lowploy.core.DelaunayTriangulation.Pnt;
import com.xinxin.lowploy.core.DelaunayTriangulation.Triangle;
import com.xinxin.lowploy.core.DelaunayTriangulation.Triangulation;
import com.xinxin.lowploy.util.ConvertGreyImg;
import com.xinxin.lowploy.util.DrawTriangle;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class StartLowPoly extends Thread {
    private static final int POLY_START = 1;
    private static final int POLY_STOP = 2;

    public Triangulation dt;
    public List<MyPoint> pnts = new ArrayList<MyPoint>();
    public long time = System.currentTimeMillis();

    private Canvas canvas;
    private Paint p;
    private static Bitmap bmp;

    private int xd, yd;
    private int x, y;
    private int cx, cy, rgb;
    private int in = 0;

    private Context context;
    private ImageView iv;
    private Handler handler;

    public StartLowPoly(Context context, MainActivity activity, ImageView iv) {
        this.context = context;
        this.iv = iv;
        handler = new PolyHandler(activity);
    }

    static class PolyHandler extends Handler {
        WeakReference<MainActivity> mActivityReference;

        PolyHandler(MainActivity activity) {
            mActivityReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            MainActivity activity = mActivityReference.get();
            if (activity != null) {
                switch (msg.what) {
                    case POLY_START:
                        if (activity.mProgressbar.getVisibility() == View.GONE) {
                            activity.mProgressbar.setVisibility(View.VISIBLE);
                            Log.i("PolyFun progress", "start");
                        }
                        break;
                    case POLY_STOP:
                        activity.mIv.setImageBitmap(bmp);
                        if (activity.mProgressbar.getVisibility() == View.VISIBLE) {
                            activity.mProgressbar.setVisibility(View.GONE);
                            Log.i("PolyFun progress", "stop");
                        }
                        activity.mLlChoose.setVisibility(View.INVISIBLE);
                        activity.mLlResult.setVisibility(View.VISIBLE);
                        if (bmp != null) {
                            bmp = null;
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    }

    @Override
    public void run() {
        handler.sendEmptyMessage(POLY_START);
        if (iv.isDrawingCacheEnabled()) {
            iv.destroyDrawingCache();
            Log.i("PolyFun TAG", "destory drawing cache");
        }
        iv.setDrawingCacheEnabled(true);
        //这是包围三角形的诞生地...
        Triangle initialTriangle = new Triangle(
                new Pnt(-LowPolyKey.initialSize, -LowPolyKey.initialSize),
                new Pnt(LowPolyKey.initialSize, -LowPolyKey.initialSize),
                new Pnt(0, LowPolyKey.initialSize));
        dt = new Triangulation(initialTriangle);
        bmp = iv.getDrawingCache();
        int height = bmp.getHeight();
        int width = bmp.getWidth();
        Log.i("PolyFun TAG", "height:" + height + ",width:" + width);
        //加入四个端点
        dt.delaunayPlace(new Pnt(1, 1));
        dt.delaunayPlace(new Pnt(1, height - 1));
        dt.delaunayPlace(new Pnt(width - 1, 1));
        dt.delaunayPlace(new Pnt(width - 1, height - 1));
        //随机加入一些点
        Random random = new Random(System.currentTimeMillis());
        for (int i = 0; i < 50; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            dt.delaunayPlace(new Pnt(x, y));
        }
        //灰度取点
        Bitmap resultBitmap = ConvertGreyImg.convertGreyImg(bmp);
        canvas = new Canvas(bmp);
        for (int y = 1; y < height - 1; ++y) {
            for (int x = 1; x < width - 1; ++x) {
                int rgb = resultBitmap.getPixel(x, y);
                rgb = (rgb & 0xff0000) >> 16;//留灰度
                if (rgb > LowPolyKey.graMax) {
                    pnts.add(new MyPoint(x, y));
                }
            }
        }
        Log.i("PolyFun TAG", "未过滤点集有" + pnts.size() + "个，正在随机排序");
        Collections.shuffle(pnts);
        int count = Math.min(pnts.size(), LowPolyKey.pc);
        Log.i("PolyFun TAG", "正在加入点并剖分三角，请耐心等待");
        for (int i = 0; i < count; i++) {
            MyPoint p = pnts.get(i);
            //  bmp.setPixel(p.x, p.y, 0xffffffff);用来观测加入的三角点
            dt.delaunayPlace(new Pnt(p.x, p.y));
        }

        Log.i("PolyFun TAG", "开始绘制最终结果");
        for (Triangle triangle : dt) {//取出所有三角形
            xd = 0;
            yd = 0;
            Pnt[] vertices = triangle.toArray(new Pnt[0]);//取出三个点

            in = 3;
            for (Pnt pnt : vertices) {//判断三个点都在图片内
                x = (int) pnt.coord(0);
                y = (int) pnt.coord(1);
                xd += x;
                yd += y;
                if (x < 0 || x > width || y < 0 || y > height) {
                    in -= 1;
                }
            }
            if (in == 3) {//三个点都在图内,才画三角形
                //取中点颜色
                cx = xd / 3;
                cy = yd / 3;
                rgb = bmp.getPixel(cx, cy);//三角形填充色
                //绘画图形
                p = DrawTriangle.drawTriangle(vertices, rgb, canvas, context.getResources());
            }

        }

//        canvas.drawBitmap(bmp, width, height, p);

        handler.sendEmptyMessage(POLY_STOP);
        Log.i("PolyFun TAG", "输出图片完成！耗时" + (System.currentTimeMillis() - time) + "ms");
    }

}
