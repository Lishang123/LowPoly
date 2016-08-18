package com.xinxin.lowploy.core;


public class LowPolyKey {
    public static final int graMax = 30;//边缘检测的阀值控制，值越大，出来的点越少
    public static int pc = 600;//最后绘制的点数量控制
    public static final int initialSize = 4000;//包围三角形的大小
    public static int started = 0;
    public static boolean FIRST = true;
}
