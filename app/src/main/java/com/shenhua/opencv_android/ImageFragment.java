package com.shenhua.opencv_android;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.shenhua.opencv_android.helper.ProcessHelper;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

/**
 * @author shenhua
 */
public class ImageFragment extends Fragment {

    public ImageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ImageFragment.
     */
    public static ImageFragment newInstance() {
        return new ImageFragment();
    }

    private ImageView ivOrigin;
    private ImageView ivAfter;
    private int currentId = R.id.rbGray;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.content_image, container, false);
    }

    public void p() {
        switch (currentId) {
            case R.id.rbGray:
                processGray();
                break;
            case R.id.rbCanny:
                processCanny();
                break;
            case R.id.rbHarris:
                processHarris();
                break;
            case R.id.rbHough:
                processHough();
                break;
            default:
                break;
        }
    }

    private void processHough() {
        final Bitmap bm = ((BitmapDrawable) ivOrigin.getDrawable()).getBitmap();
        ProcessHelper.get().hough(bm, new ProcessHelper.ProcessCallback() {
            @Override
            public void onSuccess(Bitmap bitmap) {
                ivAfter.setImageBitmap(bitmap);
                bm.recycle();
            }

            @Override
            public void onFailed(String msg) {
                System.out.println(msg);
                Snackbar.make(ivOrigin, msg, Snackbar.LENGTH_SHORT).show();
                bm.recycle();
            }
        });
    }

    private void processHarris() {
        final Bitmap bm = ((BitmapDrawable) ivOrigin.getDrawable()).getBitmap();
        ProcessHelper.get().harris(bm, new ProcessHelper.ProcessCallback() {
            @Override
            public void onSuccess(Bitmap bitmap) {
                ivAfter.setImageBitmap(bitmap);
                bm.recycle();
            }

            @Override
            public void onFailed(String msg) {
                System.out.println(msg);
                Snackbar.make(ivOrigin, msg, Snackbar.LENGTH_SHORT).show();
                bm.recycle();
            }
        });
    }

    private void processCanny() {
        final Bitmap bm = ((BitmapDrawable) ivOrigin.getDrawable()).getBitmap();
        ProcessHelper.get().canny(bm, new ProcessHelper.ProcessCallback() {
            @Override
            public void onSuccess(Bitmap bitmap) {
                ivAfter.setImageBitmap(bitmap);
                bm.recycle();
            }

            @Override
            public void onFailed(String msg) {
                System.out.println(msg);
                Snackbar.make(ivOrigin, msg, Snackbar.LENGTH_SHORT).show();
                bm.recycle();
            }
        });
    }

    private void processGray() {
        final Bitmap bm = ((BitmapDrawable) ivOrigin.getDrawable()).getBitmap();
        ProcessHelper.get().gray(bm, new ProcessHelper.ProcessCallback() {
            @Override
            public void onSuccess(Bitmap bitmap) {
                ivAfter.setImageBitmap(bitmap);
                bm.recycle();
            }

            @Override
            public void onFailed(String msg) {
                System.out.println(msg);
                Snackbar.make(ivOrigin, msg, Snackbar.LENGTH_SHORT).show();
                bm.recycle();
            }
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ivOrigin = view.findViewById(R.id.originIv);
        ivAfter = view.findViewById(R.id.afterIv);
        RadioGroup radioGroup = view.findViewById(R.id.rg);
        if (!OpenCVLoader.initDebug()) {
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_2_0, getContext(), mLoaderCallback);
        } else {
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (currentId == checkedId) {
                    return;
                }
                currentId = checkedId;
            }
        });
    }

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(getContext()) {
        @Override
        public void onManagerConnected(int status) {
            super.onManagerConnected(status);
            Snackbar.make(ivOrigin, status == BaseLoaderCallback.SUCCESS ? "OpenCV Load Success." : "OpenCV Load Failed.", Snackbar.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
