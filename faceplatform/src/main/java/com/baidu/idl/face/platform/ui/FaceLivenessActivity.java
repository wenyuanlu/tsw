/**
 * Copyright (C) 2017 Baidu Inc. All rights reserved.
 */
package com.baidu.idl.face.platform.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.Camera;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.baidu.idl.face.platform.FaceConfig;
import com.baidu.idl.face.platform.FaceSDKManager;
import com.baidu.idl.face.platform.FaceStatusNewEnum;
import com.baidu.idl.face.platform.ILivenessStrategy;
import com.baidu.idl.face.platform.ILivenessStrategyCallback;
import com.baidu.idl.face.platform.ILivenessViewCallback;
import com.baidu.idl.face.platform.LivenessTypeEnum;
import com.baidu.idl.face.platform.manager.TimeManager;
import com.baidu.idl.face.platform.model.FaceExtInfo;
import com.baidu.idl.face.platform.model.ImageInfo;
import com.baidu.idl.face.platform.stat.Ast;
import com.baidu.idl.face.platform.ui.utils.BrightnessUtils;
import com.baidu.idl.face.platform.ui.utils.CameraPreviewUtils;
import com.baidu.idl.face.platform.ui.utils.CameraUtils;
import com.baidu.idl.face.platform.ui.utils.VolumeUtils;
import com.baidu.idl.face.platform.ui.widget.FaceDetectRoundView;
import com.baidu.idl.face.platform.utils.APIUtils;
import com.baidu.idl.face.platform.utils.Base64Utils;
import com.baidu.idl.face.platform.utils.DensityUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ??????????????????
 */
public class FaceLivenessActivity extends AppCompatActivity implements
        SurfaceHolder.Callback,
        Camera.PreviewCallback,
        Camera.ErrorCallback,
        VolumeUtils.VolumeCallback,
        ILivenessStrategyCallback,
        ILivenessViewCallback {

    public static final String TAG = FaceLivenessActivity.class.getSimpleName();

    // View
    protected          View                mRootView;
    protected          FrameLayout         mFrameLayout;
    protected          SurfaceView         mSurfaceView;
    protected          SurfaceHolder       mSurfaceHolder;
    protected          ImageView           mCloseView;
    protected          ImageView           mSoundView;
    protected          ImageView           mSuccessView;
    protected          TextView            mTipsTopView;
    protected          FaceDetectRoundView mFaceDetectRoundView;
    protected          LinearLayout        mImageLayout;
    protected          LinearLayout        mImageLayout2;
    private            RelativeLayout      mRelativeAddImageView;
    private            ImageView           mImageAnim;
    public             View                mViewBg;
    // ????????????
    protected          FaceConfig          mFaceConfig;
    protected          ILivenessStrategy   mILivenessStrategy;
    // ??????Size
    private            Rect                mPreviewRect     = new Rect();
    protected          int                 mDisplayWidth    = 0;
    protected          int                 mDisplayHeight   = 0;
    protected          int                 mSurfaceWidth    = 0;
    protected          int                 mSurfaceHeight   = 0;
    // ????????????
    protected volatile boolean             mIsEnableSound   = true;
    protected          boolean             mIsCreateSurface = false;
    protected          boolean             mIsCompletion    = false;
    // ??????
    protected          Camera              mCamera;
    protected          Camera.Parameters   mCameraParam;
    protected          int                 mCameraId;
    protected          int                 mPreviewWidth;
    protected          int                 mPreviewHight;
    protected          int                 mPreviewDegree;
    // ????????????????????????
    protected          BroadcastReceiver   mVolumeReceiver;

    private Context           mContext;
    private AnimationDrawable mAnimationDrawable;
    private LivenessTypeEnum  mLivenessType = null;

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setScreenBright();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_face_liveness_v3100);
        mContext = FaceLivenessActivity.this;
        DisplayMetrics dm      = new DisplayMetrics();
        Display        display = this.getWindowManager().getDefaultDisplay();
        display.getMetrics(dm);
        mDisplayWidth = dm.widthPixels;
        mDisplayHeight = dm.heightPixels;

        FaceSDKResSettings.initializeResId();
        mFaceConfig = FaceSDKManager.getInstance().getFaceConfig();

        AudioManager am  = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int          vol = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        mIsEnableSound = vol > 0 && mFaceConfig.isSound();

        mRootView = this.findViewById(R.id.liveness_root_layout);
        mFrameLayout = (FrameLayout) mRootView.findViewById(R.id.liveness_surface_layout);

        mSurfaceView = new SurfaceView(this);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.setSizeFromLayout();
        mSurfaceHolder.addCallback(this);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        int w = mDisplayWidth;
        int h = mDisplayHeight;

        FrameLayout.LayoutParams cameraFL = new FrameLayout.LayoutParams(
                (int) (w * FaceDetectRoundView.SURFACE_RATIO),
                (int) (h * FaceDetectRoundView.SURFACE_RATIO),
                Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);

        mSurfaceView.setLayoutParams(cameraFL);
        mFrameLayout.addView(mSurfaceView);

        mRootView.findViewById(R.id.liveness_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                onBackPressed();
            }
        });

        mFaceDetectRoundView = (FaceDetectRoundView) mRootView.findViewById(R.id.liveness_face_round);
        mFaceDetectRoundView.setIsActiveLive(true);
        mCloseView = (ImageView) mRootView.findViewById(R.id.liveness_close);
        mSoundView = (ImageView) mRootView.findViewById(R.id.liveness_sound);
        mSoundView.setImageResource(mIsEnableSound ?
                R.mipmap.icon_titlebar_voice2 : R.drawable.collect_image_voice_selector);
        mSoundView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                mIsEnableSound = !mIsEnableSound;
                mSoundView.setImageResource(mIsEnableSound ?
                        R.mipmap.icon_titlebar_voice2 : R.drawable.collect_image_voice_selector);
                if (mILivenessStrategy != null) {
                    mILivenessStrategy.setLivenessStrategySoundEnable(mIsEnableSound);
                }
            }
        });
        mTipsTopView = (TextView) mRootView.findViewById(R.id.liveness_top_tips);
        mSuccessView = (ImageView) mRootView.findViewById(R.id.liveness_success_image);

        mImageLayout = (LinearLayout) mRootView.findViewById(R.id.liveness_result_image_layout);
        mImageLayout2 = (LinearLayout) mRootView.findViewById(R.id.liveness_result_image_layout2);
        mRelativeAddImageView = (RelativeLayout) mRootView.findViewById(R.id.relative_add_image_view);
        addImageView();
        mViewBg = findViewById(R.id.view_live_bg);
    }

    /**
     * ??????????????????
     */
    private void setScreenBright () {
        int currentBright = BrightnessUtils.getScreenBrightness(this);
        BrightnessUtils.setBrightness(this, currentBright + 100);
    }

    /**
     * ????????????ImageView
     */
    private void addImageView () {
        mFaceDetectRoundView.post(new Runnable() {
            @Override
            public void run () {
                mImageAnim = new ImageView(FaceLivenessActivity.this);
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup
                        .LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.height = DensityUtils.dip2px(mContext, 110); // ?????????????????????
                layoutParams.width = DensityUtils.dip2px(mContext, 87);   // ?????????????????????
                float halfHeight = mFaceDetectRoundView.getHeight() / 2;
                layoutParams.setMargins(0, (int) (halfHeight - (halfHeight * FaceDetectRoundView.HEIGHT_RATIO))
                        - layoutParams.height / 2, 0, 0);
                layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                mImageAnim.setLayoutParams(layoutParams);
                mImageAnim.setScaleType(ImageView.ScaleType.FIT_XY);  // ???????????????????????????
                mRelativeAddImageView.addView(mImageAnim);
            }
        });
    }

    @Override
    public void onResume () {
        super.onResume();
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        mVolumeReceiver = VolumeUtils.registerVolumeReceiver(this, this);
        if (mFaceDetectRoundView != null) {
            mFaceDetectRoundView.setTipTopText("????????????????????????");
        }
        startPreview();
    }

    @Override
    protected void onRestart () {
        super.onRestart();
        Log.e(TAG, "onRestart");
    }

    @Override
    public void onPause () {
        if (mILivenessStrategy != null) {
            mILivenessStrategy.reset();
        }
        VolumeUtils.unRegisterVolumeReceiver(this, mVolumeReceiver);
        mVolumeReceiver = null;
        mFaceDetectRoundView.setProcessCount(0,
                mFaceConfig.getLivenessTypeList().size());
        super.onPause();
        stopPreview();
        mIsCompletion = false;
    }

    @Override
    public void onStop () {
        super.onStop();
    }

    @Override
    public void finish () {
        super.finish();
    }

    @Override
    public void volumeChanged () {
        try {
            AudioManager am = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
            if (am != null) {
                int cv = am.getStreamVolume(AudioManager.STREAM_MUSIC);
                mIsEnableSound = cv > 0;
                mSoundView.setImageResource(mIsEnableSound
                        ? R.mipmap.icon_titlebar_voice2 : R.mipmap.icon_titlebar_voice1);
                if (mILivenessStrategy != null) {
                    mILivenessStrategy.setLivenessStrategySoundEnable(mIsEnableSound);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private Camera open () {
        Camera camera;
        int    numCameras = Camera.getNumberOfCameras();
        if (numCameras == 0) {
            return null;
        }

        int index = 0;
        while (index < numCameras) {
            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
            Camera.getCameraInfo(index, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                break;
            }
            index++;
        }

        if (index < numCameras) {
            camera = Camera.open(index);
            mCameraId = index;
        } else {
            camera = Camera.open(0);
            mCameraId = 0;
        }
        return camera;
    }

    protected void startPreview () {
        if (mSurfaceView != null && mSurfaceView.getHolder() != null) {
            mSurfaceHolder = mSurfaceView.getHolder();
            mSurfaceHolder.addCallback(this);
        }

        if (mCamera == null) {
            try {
                mCamera = open();
            } catch (RuntimeException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (mCamera == null) {
            return;
        }

        if (mCameraParam == null) {
            mCameraParam = mCamera.getParameters();
        }

        mCameraParam.setPictureFormat(PixelFormat.JPEG);
        int degree = displayOrientation(this);
        mCamera.setDisplayOrientation(degree);
        // ??????????????????camera.setDisplayOrientation????????????
        mCameraParam.set("rotation", degree);
        mPreviewDegree = degree;

        Point point = CameraPreviewUtils.getBestPreview(mCameraParam,
                new Point(mDisplayWidth, mDisplayHeight));

        mPreviewWidth = point.x;
        mPreviewHight = point.y;
        // Log.e(TAG, "x = " + mPreviewWidth + " y = " + mPreviewHight);
        // Preview 768,432

        if (mILivenessStrategy != null) {
            mILivenessStrategy.setPreviewDegree(degree);
        }

        mPreviewRect.set(0, 0, mPreviewHight, mPreviewWidth);

        mCameraParam.setPreviewSize(mPreviewWidth, mPreviewHight);
        mCamera.setParameters(mCameraParam);

        try {
            mCamera.setPreviewDisplay(mSurfaceHolder);
            mCamera.stopPreview();
            mCamera.setErrorCallback(this);
            mCamera.setPreviewCallback(this);
            mCamera.startPreview();
        } catch (RuntimeException e) {
            e.printStackTrace();
            CameraUtils.releaseCamera(mCamera);
            mCamera = null;
        } catch (Exception e) {
            e.printStackTrace();
            CameraUtils.releaseCamera(mCamera);
            mCamera = null;
        }
    }

    protected void stopPreview () {
        if (mCamera != null) {
            try {
                mCamera.setErrorCallback(null);
                mCamera.setPreviewCallback(null);
                mCamera.stopPreview();
            } catch (RuntimeException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                CameraUtils.releaseCamera(mCamera);
                mCamera = null;
            }
        }
        if (mSurfaceHolder != null) {
            mSurfaceHolder.removeCallback(this);
        }
        if (mILivenessStrategy != null) {
            mILivenessStrategy = null;
        }
    }

    private int displayOrientation (Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int           rotation      = windowManager.getDefaultDisplay().getRotation();
        int           degrees       = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
            default:
                degrees = 0;
                break;
        }
        int result = (0 - degrees + 360) % 360;
        if (APIUtils.hasGingerbread()) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(mCameraId, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                result = (info.orientation + degrees) % 360;
                result = (360 - result) % 360;
            } else {
                result = (info.orientation - degrees + 360) % 360;
            }
        }
        return result;
    }

    @Override
    public void surfaceCreated (SurfaceHolder holder) {
        mIsCreateSurface = true;
    }

    @Override
    public void surfaceChanged (SurfaceHolder holder,
            int format,
            int width,
            int height) {
        mSurfaceWidth = width;
        mSurfaceHeight = height;
        if (holder.getSurface() == null) {
            return;
        }
        startPreview();
    }

    @Override
    public void surfaceDestroyed (SurfaceHolder holder) {
        mIsCreateSurface = false;
    }

    @Override
    public void onPreviewFrame (byte[] data, Camera camera) {

        if (mIsCompletion) {
            return;
        }

        if (mILivenessStrategy == null) {
            mILivenessStrategy = FaceSDKManager.getInstance().getLivenessStrategyModule(this);
            mILivenessStrategy.setPreviewDegree(mPreviewDegree);
            mILivenessStrategy.setLivenessStrategySoundEnable(mIsEnableSound);

            Rect detectRect = FaceDetectRoundView.getPreviewDetectRect(
                    mDisplayWidth, mPreviewHight, mPreviewWidth);
            mILivenessStrategy.setLivenessStrategyConfig(
                    mFaceConfig.getLivenessTypeList(), mPreviewRect, detectRect, this);
        }
        mILivenessStrategy.livenessStrategy(data);
    }

    @Override
    public void onError (int error, Camera camera) {
    }

    @Override
    public void onLivenessCompletion (FaceStatusNewEnum status, String message,
            HashMap<String, ImageInfo> base64ImageCropMap,
            HashMap<String, ImageInfo> base64ImageSrcMap, int currentLivenessCount) {
        if (mIsCompletion) {
            return;
        }

        onRefreshView(status, message, currentLivenessCount);

        if (status == FaceStatusNewEnum.OK) {
            mIsCompletion = true;
            // saveAllImage(base64ImageCropMap, base64ImageSrcMap);
        }
        // ??????
        Ast.getInstance().faceHit("liveness");
    }

    private void onRefreshView (FaceStatusNewEnum status, String message, int currentLivenessCount) {
        switch (status) {
            case OK:
            case FaceLivenessActionComplete:
            case DetectRemindCodeTooClose:
            case DetectRemindCodeTooFar:
            case DetectRemindCodeBeyondPreviewFrame:
            case DetectRemindCodeNoFaceDetected:
                // onRefreshTipsView(false, message);
                mFaceDetectRoundView.setTipTopText(message);
                mFaceDetectRoundView.setTipSecondText("");
                mFaceDetectRoundView.setProcessCount(currentLivenessCount,
                        mFaceConfig.getLivenessTypeList().size());
                // onRefreshSuccessView(true);
                stopAnim();
                break;
            case FaceLivenessActionTypeLiveEye:
            case FaceLivenessActionTypeLiveMouth:
            case FaceLivenessActionTypeLivePitchUp:
            case FaceLivenessActionTypeLivePitchDown:
            case FaceLivenessActionTypeLiveYawLeft:
            case FaceLivenessActionTypeLiveYawRight:
            case FaceLivenessActionTypeLiveYaw:
                mFaceDetectRoundView.setTipTopText(message);
                mFaceDetectRoundView.setTipSecondText("");
                mFaceDetectRoundView.setProcessCount(currentLivenessCount,
                        mFaceConfig.getLivenessTypeList().size());
                break;
            case FaceLivenessActionCodeTimeout:    // ?????????????????????????????????
                mFaceDetectRoundView.setProcessCount(currentLivenessCount,
                        mFaceConfig.getLivenessTypeList().size());
                // ???????????????
                if (mRelativeAddImageView.getVisibility() == View.INVISIBLE) {
                    mRelativeAddImageView.setVisibility(View.VISIBLE);
                }
                loadAnimSource();
                // ?????????????????????
                int duration = 0;
                for (int i = 0; i < mAnimationDrawable.getNumberOfFrames(); i++) {
                    // ???????????????????????????
                    duration += mAnimationDrawable.getDuration(i);
                }
                TimeManager.getInstance().setActiveAnimTime(duration);
                break;
            default:
                mFaceDetectRoundView.setTipTopText("???????????????");
                mFaceDetectRoundView.setTipSecondText(message);
                mFaceDetectRoundView.setProcessCount(currentLivenessCount,
                        mFaceConfig.getLivenessTypeList().size());
                break;
        }
    }

    // ????????????
    private void loadAnimSource () {
        if (mLivenessType != null) {
            switch (mLivenessType) {
                case Eye:
                    mImageAnim.setBackgroundResource(R.drawable.anim_eye);
                    break;
                case HeadLeft:
                    mImageAnim.setBackgroundResource(R.drawable.anim_left);
                    break;
                case HeadRight:
                    mImageAnim.setBackgroundResource(R.drawable.anim_right);
                    break;
                case HeadDown:
                    mImageAnim.setBackgroundResource(R.drawable.anim_down);
                    break;
                case HeadUp:
                    mImageAnim.setBackgroundResource(R.drawable.anim_up);
                    break;
                case Mouth:
                    mImageAnim.setBackgroundResource(R.drawable.anim_mouth);
                    break;
                default:
                    break;
            }
            mAnimationDrawable = (AnimationDrawable) mImageAnim.getBackground();
            mAnimationDrawable.start();
        }
    }

    private static Bitmap base64ToBitmap (String base64Data) {
        byte[] bytes = Base64Utils.decode(base64Data, Base64Utils.NO_WRAP);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    @Override
    public void setCurrentLiveType (LivenessTypeEnum liveType) {
        mLivenessType = liveType;
    }

    @Override
    public void viewReset () {
        mFaceDetectRoundView.setProcessCount(0, 1);
    }

    @Override
    public void animStop () {
        stopAnim();
    }

    @Override
    public void setFaceInfo (FaceExtInfo faceInfo) {

    }

    private void stopAnim () {
        if (mAnimationDrawable != null) {
            mAnimationDrawable.stop();
            mAnimationDrawable = null;
        }
        if (mRelativeAddImageView.getVisibility() == View.VISIBLE) {
            mRelativeAddImageView.setVisibility(View.INVISIBLE);
        }
    }

    // ----------------------------------------????????????----------------------------------------------
    private void saveAllImage (HashMap<String, ImageInfo> imageCropMap, HashMap<String, ImageInfo> imageSrcMap) {
        if (imageCropMap != null && imageCropMap.size() > 0) {
            List<Map.Entry<String, ImageInfo>> list1 = new ArrayList<>(imageCropMap.entrySet());
            Collections.sort(list1, new Comparator<Map.Entry<String, ImageInfo>>() {

                @Override
                public int compare (Map.Entry<String, ImageInfo> o1,
                        Map.Entry<String, ImageInfo> o2) {
                    String[] key1   = o1.getKey().split("_");
                    String   score1 = key1[2];
                    String[] key2   = o2.getKey().split("_");
                    String   score2 = key2[2];
                    // ????????????
                    return Float.valueOf(score2).compareTo(Float.valueOf(score1));
                }
            });
            setImageView1(list1);
        }

        if (imageSrcMap != null && imageSrcMap.size() > 0) {
            List<Map.Entry<String, ImageInfo>> list2 = new ArrayList<>(imageSrcMap.entrySet());
            Collections.sort(list2, new Comparator<Map.Entry<String, ImageInfo>>() {

                @Override
                public int compare (Map.Entry<String, ImageInfo> o1,
                        Map.Entry<String, ImageInfo> o2) {
                    String[] key1   = o1.getKey().split("_");
                    String   score1 = key1[2];
                    String[] key2   = o2.getKey().split("_");
                    String   score2 = key2[2];
                    // ????????????
                    return Float.valueOf(score2).compareTo(Float.valueOf(score1));
                }
            });
            setImageView2(list2);
        }
    }

    private void setImageView1 (List<Map.Entry<String, ImageInfo>> list) {
        Bitmap bmp = null;
        mImageLayout.removeAllViews();
        for (Map.Entry<String, ImageInfo> entry : list) {
            bmp = base64ToBitmap(entry.getValue().getBase64());
            ImageView iv = new ImageView(this);
            iv.setImageBitmap(bmp);
            mImageLayout.addView(iv, new LinearLayout.LayoutParams(300, 300));
        }
    }

    private void setImageView2 (List<Map.Entry<String, ImageInfo>> list) {
        Bitmap bmp = null;
        mImageLayout2.removeAllViews();
        for (Map.Entry<String, ImageInfo> entry : list) {
            bmp = base64ToBitmap(entry.getValue().getBase64());
            ImageView iv = new ImageView(this);
            iv.setImageBitmap(bmp);
            mImageLayout2.addView(iv, new LinearLayout.LayoutParams(300, 300));
        }
    }
}
