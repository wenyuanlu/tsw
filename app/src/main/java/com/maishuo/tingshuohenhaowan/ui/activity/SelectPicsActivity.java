package com.maishuo.tingshuohenhaowan.ui.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.luck.picture.lib.PictureSelectionModel;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.maishuo.tingshuohenhaowan.R;
import com.maishuo.tingshuohenhaowan.utils.GlideEngine;
import com.maishuo.tingshuohenhaowan.utils.PictureStyleUtil;
import com.qichuang.commonlibs.basic.IBasicActivity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * author ：yh
 * date : 2021/1/21 13:38
 * description :选择图片的页面
 */
public class SelectPicsActivity extends IBasicActivity {

    public static final String SHOW_CAMERA    = "SHOW_CAMERA";//是否有拍照
    public static final String ENABLE_CROP    = "ENABLE_CROP";//是否有裁剪
    public static final String ENABLE_PREVIEW = "ENABLE_PREVIEW";//是否可预览
    public static final String NEED_THUMB     = "NEED_THUMB";//是否需要缩略图
    public static final String SINGLE_BACK    = "SINGLE_BACK";//是否单选直接返回
    public static final String SELECT_COUNT   = "SELECT_COUNT";//可选择的数量

    public static final String CROPWIDTH             = "CROPWIDTH";
    public static final String CROPHEIGHT            = "CROPHEIGHT";
    public static final String COMPRESS_SIZE         = "COMPRESS_SIZE";
    public static final String COMPRESS_PATHS        = "COMPRESS_PATHS";//选择图片返回的数据,多图
    public static final String COMPRESS_SINGLE_PATHS = "COMPRESS_SINGLE_PATHS";//选择图片返回的数据,单图

    private String  mMode        = "image";
    private int     mSelectCount = 1;
    private boolean mShowCamera;
    private boolean mEnableCrop;
    private boolean mEnablePreview;
    private boolean mSingleBack;
    private boolean mNeedThumb;

    private              int     compressCount = 0;
    private              int     compressSize;
    private              int     mCropWidth;
    private              int     mCropHeight;
    private static final Handler mHandler      = new Handler();

    @Override
    protected void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_pics);
        initView();
    }


    protected void initView () {
        mSelectCount = getIntent().getIntExtra(SELECT_COUNT, 9);//选择的数量
        mShowCamera = getIntent().getBooleanExtra(SHOW_CAMERA, false);//是否有拍照
        mEnableCrop = getIntent().getBooleanExtra(ENABLE_CROP, false);//是否可裁剪
        mEnablePreview = getIntent().getBooleanExtra(ENABLE_PREVIEW, false);//是否预览
        mSingleBack = getIntent().getBooleanExtra(SINGLE_BACK, true);//单选模式下自动返回
        mNeedThumb = getIntent().getBooleanExtra(NEED_THUMB, false);//是否需要缩略图

        mCropWidth = getIntent().getIntExtra(CROPWIDTH, 1);
        mCropHeight = getIntent().getIntExtra(CROPHEIGHT, 1);
        compressSize = getIntent().getIntExtra(COMPRESS_SIZE, 500);

        //选择图片
        selectPicture();
    }

    /**
     * 开启选择图片
     */
    private void selectPicture () {
        PictureStyleUtil pictureStyleUtil = new PictureStyleUtil(this);

        //添加图片
        PictureSelector       pictureSelector       = PictureSelector.create(this);
        PictureSelectionModel pictureSelectionModel = null;
        pictureSelectionModel = pictureSelector.openGallery(PictureMimeType.ofImage());

        pictureSelectionModel
                .loadImageEngine(GlideEngine.createGlideEngine())
                .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                .setPictureStyle(pictureStyleUtil.getStyle())
                .setPictureCropStyle(pictureStyleUtil.getCropStyle())
                .imageFormat(PictureMimeType.JPEG.toLowerCase())// 拍照保存图片格式后缀,默认jpeg
                .isCamera(mShowCamera)
                .isGif(true)
                .maxSelectNum(mSelectCount)
                .imageSpanCount(4)// 每行显示个数 int
                .selectionMode(mSelectCount == 1 ? PictureConfig.SINGLE : PictureConfig.MULTIPLE)// 单选 or 多选
                .isSingleDirectReturn(mSingleBack)// 单选模式下是否直接返回
                .previewImage(mEnablePreview)// 是否可预览图片 true or false
                .enableCrop(mEnableCrop)// 是否裁剪 true or false
                .withAspectRatio(mCropWidth, mCropHeight)//裁剪比例
                .circleDimmedLayer(false)//是否开启圆形裁剪
                .showCropFrame(true)//是否显示裁剪矩形边框 圆形裁剪时建议设为false
                .showCropGrid(true)//是否显示裁剪矩形网格 圆形裁剪时建议设为false
                .isDragFrame(false)//是否可拖动裁剪框(固定)
                .scaleEnabled(true)//裁剪是否可放大缩小图片
                .rotateEnabled(true)//裁剪是否可旋转图片
                .hideBottomControls(true)//隐藏底部uCrop工具栏
                .freeStyleCropEnabled(false)//裁剪框是否可拖拽
                .compress(false)// 是否压缩 true or false
                .minimumCompressSize(Integer.MAX_VALUE)//小于多少kb的图片不压缩
                .compressSavePath(getPath())//自定义压缩图片保存地址，注意Q版本下的适配
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }

    protected void initData () {

    }

    @Override
    protected void onPause () {
        super.onPause();
    }

    @Override
    protected void onDestroy () {
        super.onDestroy();
    }


    @Override
    public void finish () {
        super.finish();
        overridePendingTransition(0, 0);
    }

    /**
     * 保存的图片
     * <p>
     * notQIsShowInGallery : false 非AndroidQ 保存在沙盒中，true : 非AndroidQ 保存在sd卡并且显示在图库中
     *
     * @return sd/->/android/data/packageName/files/Pictures
     */
    public String getAppImgDirPath (boolean notQIsShowInGallery) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            File picDir = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            if (picDir != null) {
                return picDir.getAbsolutePath();
            }
            return "";
        } else {
            if (notQIsShowInGallery) {
                String path = Environment.getExternalStorageDirectory() + "/" + getPackageName() + "/" + Environment.DIRECTORY_PICTURES;
                File   dir  = new File(path);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                return dir.getAbsolutePath();
            } else {
                String path = Environment.getExternalStorageDirectory() + "/" + getPackageName() + "/" + Environment.DIRECTORY_PICTURES + "/cache";
                File   dir  = new File(path);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                createNomedia(path);
                return path;
            }
        }
    }

    private String getPath () {
        String path = getAppImgDirPath(false);
        File   file = new File(path);
        file.mkdirs();
        createNomedia(path);
        return path;
    }

    private void createNomedia (String path) {
        File nomedia = new File(path, ".nomedia");
        if (!nomedia.exists()) {
            try {
                nomedia.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片、视频、音频选择结果回调
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true  注意：音视频除外
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true  注意：音视频除外
                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的

                    List<String> paths = new ArrayList<>();
                    for (int i = 0; i < selectList.size(); i++) {
                        LocalMedia localMedia = selectList.get(i);

                        if (localMedia.isCut()) {
                            // 因为这个lib中 gif裁剪有问题，所以gif裁剪过就不使用裁剪地址，使用原gif地址
                            if (Build.VERSION.SDK_INT >= 29) {
                                if (localMedia.getPath() != null && localMedia.getAndroidQToPath() != null && localMedia.getAndroidQToPath().endsWith(".gif")) {
                                    String path = PictureFileUtils.getPath(getApplicationContext(), Uri.parse(localMedia.getPath()));
                                    paths.add(path);
                                } else {
                                    paths.add(localMedia.getCutPath());
                                }
                            } else {
                                if (localMedia.getPath() != null && localMedia.getPath().endsWith(".gif")) {
                                    paths.add(localMedia.getPath());
                                } else {
                                    paths.add(localMedia.getCutPath());
                                }
                            }
                        } else {
                            if (Build.VERSION.SDK_INT >= 29) {
                                if (!TextUtils.isEmpty(localMedia.getAndroidQToPath())) {
                                    paths.add(localMedia.getAndroidQToPath());
                                } else {
                                    paths.add(localMedia.getRealPath());
                                }
                            } else {
                                paths.add(localMedia.getPath());
                            }
                        }
                    }

                    if ("image".equals(mMode)) {
                        //如果选择的是图片就压缩
                        lubanCompress(paths);
                    }
                    break;
            }
        } else {
            mHandler.postDelayed(() -> {
                Intent intent = new Intent();
                setResult(RESULT_CANCELED, intent);
                finish();
            }, 0);
        }
    }

    /**
     * 图片压缩
     *
     * @param paths
     */
    private void lubanCompress (final List<String> paths) {
        final List<Map<String, String>> lubanCompressPaths = new ArrayList<>();
        Luban.with(this)
                .load(paths)
                .ignoreBy(compressSize)
                .setTargetDir(getPath())
                .filter(path -> !path.endsWith(".gif"))
                .setRenameListener(filePath -> filePath.substring(filePath.lastIndexOf("/")))
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart () {
                    }

                    @Override
                    public void onSuccess (File file) {
                        String thumbPath = "";
                        if (mNeedThumb) {//需要缩略图,走缩略压缩,聊天的时候需要
                            thumbPath = getThumbImage(file.getAbsolutePath());
                        }
                        //先获取图片的宽高
                        BitmapFactory.Options options = new BitmapFactory.Options();//获取Options对象
                        options.inJustDecodeBounds = true;//仅做解码处理，不加载到内存
                        BitmapFactory.decodeFile(file.getAbsolutePath(), options);//解析文件
                        int imgWidth  = options.outWidth;//获取宽高
                        int imgHeight = options.outHeight;//获取宽高
                        // 压缩成功后调用，返回压缩后的图片文件
                        Map<String, String> map = new HashMap<>();
                        map.put("thumbPath", thumbPath);
                        map.put("path", file.getAbsolutePath());
                        map.put("width", imgWidth + "");
                        map.put("height", imgHeight + "");
                        lubanCompressPaths.add(map);
                        compressCount++;
                        compressFinishBack(paths, lubanCompressPaths);
                    }

                    @Override
                    public void onError (Throwable e) {
                        // 当压缩过程出现问题时调用
                        compressCount++;
                        compressFinishBack(paths, lubanCompressPaths);
                    }
                }).launch();
    }

    /**
     * 压缩结束,返回数据
     *
     * @param paths
     * @param compressPaths
     */
    private void compressFinishBack (List<String> paths, List<Map<String, String>> compressPaths) {
        if (compressCount == paths.size()) {
            Intent intent = new Intent();
            if (mSelectCount == 1) {
                Map<String, String> pathMap = compressPaths.get(0);
                intent.putExtra(COMPRESS_SINGLE_PATHS, (Serializable) pathMap);
            } else {
                intent.putExtra(COMPRESS_PATHS, (Serializable) compressPaths);
            }
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    /**
     * 图片按比例大小压缩方法 缩略图
     *
     * @param srcPath （根据路径获取图片并压缩）
     * @return
     */
    public String getThumbImage (String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 现在主流手机比较多是400*240分辨率，所以高和宽我们设置为
        float hh = 300f;// 这里设置高度为400f
        float ww = 180f;// 这里设置宽度为240f
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w >= h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return compressImage(bitmap, 20);// 压缩好比例大小后再进行质量压缩
    }

    /**
     * 质量压缩方法
     *
     * @param image
     * @param kbNumber 压缩到多小以内
     * @return
     */
    public String compressImage (Bitmap image, int kbNumber) {
        if (image == null) {
            return "";
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 80;
        while (baos.toByteArray().length / 1024 > kbNumber) { // 循环判断如果压缩后图片是否大于20kb,大于继续压缩
            baos.reset(); // 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            if (options > 10) {
                options = options - 10;// 每次都减少10
            } else {
                if (options > 5) {
                    options = options - 5;// 每次都减少5
                } else {
                    if (options > 2) {
                        options = options - 2;// 每次都减少2
                    } else {
                        options = 1;
                    }
                }
            }
        }
        ByteArrayInputStream isBm   = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap               bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片

        //创建文件夹
        String filePath;
        String saveFile = getExternalFilesDir("Chat").getAbsolutePath() + "/";
        File   file     = new File(saveFile);
        if (!file.exists()) {
            file.mkdir();
        }
        String name = "IMAGE_CACH_BACK" + new Date().getTime() + ".jpg";
        filePath = saveFile + name;
        File files = new File(filePath);
        try {
            FileOutputStream out = new FileOutputStream(files);
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return files.getAbsolutePath();
    }


}
