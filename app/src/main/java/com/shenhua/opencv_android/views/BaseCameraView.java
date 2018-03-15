package com.shenhua.opencv_android.views;

import android.content.Context;
import android.util.AttributeSet;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.InstallCallbackInterface;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;

/**
 * Created by shenhua on 2018-03-14-0014.
 *
 * @author shenhua
 *         Email shenhuanet@126.com
 */
public class BaseCameraView extends JavaCameraView implements LoaderCallbackInterface, CameraBridgeViewBase.CvCameraViewListener2 {

    private int cameraIndex = 0;

    public BaseCameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCvCameraViewListener(this);
    }

    /**
     * 加载OpenCV
     */
    public void loadOpenCV() {
        // 初始化OpenCV
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_2_0, getContext().getApplicationContext(), this);
    }

    @Override
    public void onManagerConnected(int status) {
        switch (status) {
            case LoaderCallbackInterface.SUCCESS:
                System.out.println("onManagerConnected SUCCESS");
                break;
            default:
                System.out.println("onManagerConnected Failed");
                break;
        }
    }

    @Override
    public void onPackageInstall(int operation, InstallCallbackInterface callback) {
        System.out.println("onPackageInstall..");
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        System.out.println("onCameraViewStarted..");
    }

    @Override
    public void onCameraViewStopped() {
        System.out.println("onCameraViewStopped..");
    }

    @Override
    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
        return null;
    }
}
