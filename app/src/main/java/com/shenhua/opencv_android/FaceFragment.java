package com.shenhua.opencv_android;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.shenhua.opencv_android.views.ObjectDetector;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FaceFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 * @author shenhua
 */
public class FaceFragment extends Fragment implements CameraBridgeViewBase.CvCameraViewListener2 {

    public FaceFragment() {
    }

    public static FaceFragment newInstance() {
        return new FaceFragment();
    }

    private CameraBridgeViewBase cameraView;
    private Mat mRgba;
    private Mat mGray;
    private Mat mByte;
    private ObjectDetector face;
    private ObjectDetector eye;
    private ObjectDetector smile;
    private MatOfRect matOfRect;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext().getApplicationContext(), "Permission Denied.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.content_face, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cameraView = view.findViewById(R.id.cameraView);
        if (!OpenCVLoader.initDebug()) {
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_2_0, getContext(), mLoaderCallback);
        } else {
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
        cameraView.setCvCameraViewListener(this);
        face = new ObjectDetector(getContext().getApplicationContext(), R.raw.lbpcascade_frontalface, 6, 0.2F, 0.2F, new Scalar(255, 0, 0, 255));
        eye = new ObjectDetector(getContext().getApplicationContext(), R.raw.haarcascade_eye, 6, 0.1F, 0.1F, new Scalar(0, 255, 0, 255));
        smile = new ObjectDetector(getContext().getApplicationContext(), R.raw.haarcascade_smile, 10, 0.2F, 0.2F, new Scalar(0, 255, 255, 255));
    }

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(getContext()) {
        @Override
        public void onManagerConnected(int status) {
            super.onManagerConnected(status);
            Snackbar.make(cameraView, status == BaseLoaderCallback.SUCCESS ? "OpenCV Load Success." : "OpenCV Load Failed.", Snackbar.LENGTH_SHORT).show();
            cameraView.enableView();
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        cameraView.disableView();
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        System.out.println("onCameraViewStarted...");
        mRgba = new Mat(height, width, CvType.CV_8UC3);
        mByte = new Mat(height, width, CvType.CV_8UC1);
        matOfRect = new MatOfRect();
    }

    @Override
    public void onCameraViewStopped() {
        System.out.println("onCameraViewStopped...");
        mRgba.release();
        mByte.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        mRgba = inputFrame.rgba();
        // 解决预览视图逆时针90度
        Core.transpose(mRgba, mRgba);
        Core.flip(mRgba, mRgba, 1);

        Rect[] obj = face.detectObject(mRgba, matOfRect);
        for (Rect rect : obj) {
            Imgproc.rectangle(mRgba, rect.tl(), rect.br(), face.getRectColor(), 3);
        }

        return mRgba;
    }
}
