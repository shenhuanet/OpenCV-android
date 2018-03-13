package com.shenhua.opencv_android.helper;

import android.graphics.Bitmap;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.Random;

/**
 * Created by shenhua on 2018-03-12-0012.
 *
 * @author shenhua
 *         Email shenhuanet@126.com
 */
public class ProcessHelper {

    private static ProcessHelper sInstance = null;
    private Mat rgbMat, grayMat;

    public static ProcessHelper get() {
        if (sInstance == null) {
            synchronized (ProcessHelper.class) {
                if (sInstance == null) {
                    sInstance = new ProcessHelper();
                }
            }
        }
        return sInstance;
    }

    private ProcessHelper() {
        rgbMat = new Mat();
        grayMat = new Mat();
    }

    /**
     * 灰度处理
     *
     * @param origin   原始bitmap
     * @param callback 回调
     */
    public void gray(Bitmap origin, ProcessCallback callback) {
        if (origin == null) {
            return;
        }
        try {
            Bitmap bitmap = Bitmap.createBitmap(origin.getWidth(), origin.getHeight(), Bitmap.Config.RGB_565);
            Utils.bitmapToMat(origin, rgbMat);
            Imgproc.cvtColor(rgbMat, grayMat, Imgproc.COLOR_RGB2GRAY);
            Utils.matToBitmap(grayMat, bitmap);
            callback.onSuccess(bitmap);
        } catch (Exception e) {
            callback.onFailed(e.getMessage());
        }
    }

    /**
     * 边缘检测
     *
     * @param origin   原始bitmap
     * @param callback 回调
     */
    public void canny(Bitmap origin, ProcessCallback callback) {
        if (origin == null) {
            return;
        }
        try {
            Bitmap bitmap = Bitmap.createBitmap(origin.getWidth(), origin.getHeight(), Bitmap.Config.RGB_565);
            Utils.bitmapToMat(origin, rgbMat);
            Imgproc.cvtColor(rgbMat, grayMat, Imgproc.COLOR_RGB2GRAY);
            Mat edges = new Mat();
            // 阈值极限
            Imgproc.Canny(grayMat, edges, 50, 300);
            Utils.matToBitmap(edges, bitmap);
            callback.onSuccess(bitmap);
        } catch (Exception e) {
            callback.onFailed(e.getMessage());
        }
    }

    /**
     * 角点检测
     *
     * @param origin   原始bitmap
     * @param callback 回调
     */
    public void harris(Bitmap origin, ProcessCallback callback) {
        if (origin == null) {
            return;
        }
        try {
            Bitmap bitmap = Bitmap.createBitmap(origin.getWidth(), origin.getHeight(), Bitmap.Config.RGB_565);
            Utils.bitmapToMat(origin, rgbMat);
            Imgproc.cvtColor(rgbMat, grayMat, Imgproc.COLOR_RGB2GRAY);
            Mat corners = new Mat();
            Mat tempDst = new Mat();
            Mat tempDstNorm = new Mat();
            // 找出角点
            Imgproc.cornerHarris(grayMat, tempDst, 2, 3, 0.04);
            // 归一化Harris角点的输出
            Core.normalize(tempDst, tempDstNorm, 0, 255, Core.NORM_MINMAX);
            Core.convertScaleAbs(tempDstNorm, corners);
            // 绘制角点
            Random random = new Random();
            for (int i = 0; i < tempDstNorm.cols(); i++) {
                for (int j = 0; j < tempDstNorm.rows(); j++) {
                    double[] value = tempDstNorm.get(j, i);
                    if (value[0] > 250) {
                        // 决定了画出哪些角点，值越大选择画出的点就越少
                        Imgproc.circle(corners, new Point(i, j), 5, new Scalar(random.nextInt(255)), 2);
                    }
                }
            }
            Utils.matToBitmap(corners, bitmap);
            callback.onSuccess(bitmap);
        } catch (Exception e) {
            callback.onFailed(e.getMessage());
        }
    }

    /**
     * 直线检测
     *
     * @param origin   原始bitmap
     * @param callback 回调
     */
    public void hough(Bitmap origin, ProcessCallback callback) {
        if (origin == null) {
            return;
        }
        try {
            Bitmap bitmap = Bitmap.createBitmap(origin.getWidth(), origin.getHeight(), Bitmap.Config.RGB_565);
            Utils.bitmapToMat(origin, rgbMat);
            Imgproc.cvtColor(rgbMat, grayMat, Imgproc.COLOR_RGB2GRAY);
            Mat edges = new Mat();
            Mat src = new Mat(origin.getHeight(), origin.getWidth(), CvType.CV_8UC4);
            Mat lines = new Mat();
            // 拷贝
            Mat origination = new Mat(src.size(), CvType.CV_8UC1);
            src.copyTo(origination);
            // 通过Canny得到边缘图
            Imgproc.cvtColor(origination, grayMat, Imgproc.COLOR_BGR2GRAY);
            Imgproc.Canny(grayMat, edges, 50, 200);
            // 获取直线图
            Imgproc.HoughLinesP(edges, lines, 1, Math.PI / 180, 10, 0, 10);
            Mat houghLines = new Mat();
            houghLines.create(edges.rows(), edges.cols(), CvType.CV_8UC1);
            // 绘制直线
            for (int i = 0; i < lines.rows(); i++) {
                double[] points = lines.get(i, 0);
                if (null != points) {
                    double x1, y1, x2, y2;
                    x1 = points[0];
                    y1 = points[1];
                    x2 = points[2];
                    y2 = points[3];
                    Point pt1 = new Point(x1, y1);
                    Point pt2 = new Point(x2, y2);
                    Imgproc.line(houghLines, pt1, pt2, new Scalar(55, 100, 195), 3);
                }
            }
            Utils.matToBitmap(houghLines, bitmap);
            callback.onSuccess(bitmap);
        } catch (Exception e) {
            callback.onFailed(e.getMessage());
        }
    }

    public interface ProcessCallback {

        /**
         * 处理成功的回调
         *
         * @param bitmap 处理后的bitmap
         */
        void onSuccess(Bitmap bitmap);

        /**
         * 处理失败的回调
         *
         * @param msg 失败信息
         */
        void onFailed(String msg);
    }
}
