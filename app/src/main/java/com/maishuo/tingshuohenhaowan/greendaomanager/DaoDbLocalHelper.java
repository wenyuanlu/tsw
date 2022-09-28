package com.maishuo.tingshuohenhaowan.greendaomanager;

import android.database.sqlite.SQLiteDatabase;

import com.maishuo.tingshuohenhaowan.common.CustomApplication;
import com.maishuo.tingshuohenhaowan.greendao.DaoMaster;
import com.maishuo.tingshuohenhaowan.greendao.DaoSession;


/**
 * 本地外层数据保存（无登录）
 */
public class DaoDbLocalHelper {
    private static final String DB_NAME = "chat.db";//数据库名称

    private static volatile DaoDbLocalHelper sInstance;
    private                 SQLiteDatabase   mDb;
    private                 DaoMaster        mDaoMaster;
    private                 DaoSession       mSession;

    public static DaoDbLocalHelper getInstance () {
        if (sInstance == null) {
            synchronized (DaoDbLocalHelper.class) {
                if (sInstance == null) {
                    sInstance = new DaoDbLocalHelper();
                }
            }
        }
        return sInstance;
    }

    public String getDbName () {
        return DB_NAME;
    }

    /**
     * 登录---重新获取数据库的操作类
     */
    public DaoDbLocalHelper loginNew () {
        mDb = null;
        mDaoMaster = null;
        mSession = null;
        sInstance = new DaoDbLocalHelper();
        return sInstance;
    }

    private DaoDbLocalHelper () {
        //封装数据库的创建、更新、删除
        DaoMaster.DevOpenHelper openHelper = new DaoMaster.DevOpenHelper(
                new GreenDaoContext(CustomApplication.getApp(), true), DB_NAME, null);
        //获取数据库
        mDb = openHelper.getWritableDatabase();
        //封装数据库中表的创建、更新、删除
        mDaoMaster = new DaoMaster(mDb);  //合起来就是对数据库的操作
        //对表操作的对象。
        mSession = mDaoMaster.newSession(); //可以认为是对数据的操作
    }

    public DaoSession getSession () {
        return mSession;
    }

    public SQLiteDatabase getDatabase () {
        return mDb;
    }

    public DaoSession getNewSession () {
        return mDaoMaster.newSession();
    }
}
