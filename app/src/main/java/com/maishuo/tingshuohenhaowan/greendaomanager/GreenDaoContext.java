package com.maishuo.tingshuohenhaowan.greendaomanager;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.qichuang.commonlibs.common.PreferencesKey;
import com.qichuang.commonlibs.utils.PreferencesUtils;

import java.io.File;
import java.io.IOException;

/**
 * author ：yh
 * date : 2021/1/19 10:07
 * description : 自定义的数据库地址
 */
public class GreenDaoContext extends ContextWrapper {

    private String  currentUserId = "12345678";//一般用来针对一个用户一个数据库，以免数据混乱问题
    private boolean mIsLogin      = false;//默认不登录
    private Context mContext;

    public GreenDaoContext (Context context, boolean isLogin) {
        super(context);
        this.mIsLogin = isLogin;
        this.mContext = context;
        long uidInt = PreferencesUtils.getLong(PreferencesKey.USER_UID);
        currentUserId = String.valueOf(uidInt);
    }

    /**
     * 获得数据库路径，如果不存在，则创建对象
     *
     * @param dbName
     */
    @Override
    public File getDatabasePath (String dbName) {
        String dbDir;//保存位置随APP可删除
        StringBuffer buffer = new StringBuffer();
        try {
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                    || !Environment.isExternalStorageRemovable()) {
                dbDir = mContext.getExternalFilesDir("db").getPath();
            } else {
                dbDir = mContext.getFilesDir().getPath();
            }

            File baseFile = new File(dbDir);
            // 目录不存在则自动创建目录
            if (!baseFile.exists()) {
                baseFile.mkdirs();
            }

            //创建数据库路径
            buffer.append(baseFile.getPath());
            buffer.append(File.separator);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mIsLogin) {//登录情况才建立文件夹
            buffer.append(currentUserId);
        }
        dbDir = buffer.toString();          // 数据库所在目录

        //创建数据文件名称
        buffer.append(File.separator);
        if (mIsLogin) {//登录情况才和ID联系
            //采用此种方式，将用户id与表名联系到一块命名
            buffer.append(dbName + "_" + currentUserId);
        } else {
            buffer.append(dbName);
        }
        String dbPath = buffer.toString();  // 数据库路径

        // 判断数据库目录是否存在，不存在则创建该目录
        File dirFile = new File(dbDir);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        // 数据库文件是否创建成功
        boolean isFileCreateSuccess = false;
        // 判断数据库文件是否存在，不存在则创建该文件
        File dbFile = new File(dbPath);
        if (!dbFile.exists()) {
            try {
                isFileCreateSuccess = dbFile.createNewFile();// 创建文件
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            isFileCreateSuccess = true;
        }

        // 返回数据库文件对象
        if (isFileCreateSuccess) {
            return dbFile;
        } else {
            return super.getDatabasePath(dbName);
        }

    }

    /**
     * 重载这个方法，是用来打开SD卡上的数据库的，android 2.3及以下会调用这个方法。
     *
     * @param name
     * @param mode
     * @param factory
     */
    @Override
    public SQLiteDatabase openOrCreateDatabase (String name, int mode, SQLiteDatabase.CursorFactory factory) {
        SQLiteDatabase result = SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), factory);
        return result;
    }

    /**
     * Android 4.0会调用此方法获取数据库。
     *
     * @param name
     * @param mode
     * @param factory
     * @param errorHandler
     * @see ContextWrapper#openOrCreateDatabase(String, int,
     * SQLiteDatabase.CursorFactory,
     * DatabaseErrorHandler)
     */
    @Override
    public SQLiteDatabase openOrCreateDatabase (String name, int mode, SQLiteDatabase.CursorFactory factory, DatabaseErrorHandler errorHandler) {
        SQLiteDatabase result = SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), factory);

        return result;
    }

}
