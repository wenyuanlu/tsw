package com.maishuo.tingshuohenhaowan.greendaomanager;

import android.database.Cursor;
import android.text.TextUtils;

import com.maishuo.tingshuohenhaowan.greendao.ChatLocalBeanDao;
import com.maishuo.tingshuohenhaowan.greendao.DaoSession;
import com.maishuo.tingshuohenhaowan.api.response.MessageListBean;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * 本地数据保存通用类
 * 查询
 * eq()："equal ('=?')" 等于；
 * notEq() ："not equal ('<>?')" 不等于；
 * like()：" LIKE ?" 值等于；
 * between()：" BETWEEN ? AND ?" 取中间范围；
 * in()：" IN ("  in命令;
 * notIn()：" NOT IN (" not in 命令;
 * gt()：">?"  大于;
 * lt()："< ?"  小于;
 * ge()：">=?"  大于等于;
 * le()："<=? "  小于等于;
 * isNull()：" IS NULL" 为空;
 * isNotNull()：" IS NOT NULL" 不为空;
 * unique() 查询一条
 * list() 返回结果集进内存
 * count() 获取结果数量
 */
public class LocalRepository {

    private static final String TAG                  = "LocalRepository";
    private static final String DISTILLATE_ALL       = "normal";
    private static final String DISTILLATE_BOUTIQUES = "distillate";

    private static volatile LocalRepository sInstance;
    private                 DaoSession      mSession;

    private LocalRepository () {
        mSession = DaoDbLocalHelper.getInstance().getSession();
    }

    public static LocalRepository getInstance () {
        if (sInstance == null) {
            synchronized (LocalRepository.class) {
                if (sInstance == null) {
                    sInstance = new LocalRepository();
                }
            }
        }
        return sInstance;
    }

    /**
     * 重新登录新的数据库
     */
    public void loginNew () {
        mSession = DaoDbLocalHelper.getInstance().loginNew().getSession();
    }

    /***************************************** 增 ************************************************/
    /***************************************** 增 ************************************************/
    /***************************************** 增 ************************************************/

    //添加聊天信息
    public Long insertChat (ChatLocalBean bean) {
        return mSession.getChatLocalBeanDao().insert(bean);
    }

    //添加聊天信息(List集合形式)
    public void insertChatList (List<ChatLocalBean> chatList) {
        if (chatList != null && chatList.size() > 0) {
            for (ChatLocalBean smsBean : chatList) {
                mSession.getChatLocalBeanDao().insert(smsBean);
            }
        }
    }

    /***************************************** 查 ************************************************/
    /***************************************** 查 ************************************************/
    /***************************************** 查 ************************************************/

    /**
     * 获取单个聊天信息
     */
    public ChatLocalBean getChatById (String uid) {
        return mSession.getChatLocalBeanDao().queryBuilder()
                .where(ChatLocalBeanDao.Properties.Uid.in(uid))
                .orderDesc(ChatLocalBeanDao.Properties.Time)//orderDesc降序查找
                .limit(1)
                .unique();//
    }

    /**
     * 获取所有聊天信息
     */
    public List<ChatLocalBean> getChatList (String toUid) {
        QueryBuilder<ChatLocalBean> queryBuilder = mSession.getChatLocalBeanDao()
                .queryBuilder()
                .where(ChatLocalBeanDao.Properties.ToUid.eq(toUid))
                .orderDesc(ChatLocalBeanDao.Properties.Time);//orderDesc降序查找
        List<ChatLocalBean> list = queryBuilder.list();
        return list;
    }

    /**
     * 获取指定id未读消息
     */
    public List<ChatLocalBean> getUnReadListById (int toUid) {
        QueryBuilder<ChatLocalBean> queryBuilder = mSession.getChatLocalBeanDao()
                .queryBuilder()
                .where(ChatLocalBeanDao.Properties.ToUid.eq(toUid), ChatLocalBeanDao.Properties.IsRead.eq("1"));
        List<ChatLocalBean> list = queryBuilder.list();
        return list;
    }

    /**
     * 获取指定ID的未读数量,最后一条消息
     */
    public List<MessageListBean.FriendBean> getUnReadLastListById (List<MessageListBean.FriendBean> uidList) {
        List<MessageListBean.FriendBean> lastList   = new ArrayList<>();
        List<MessageListBean.FriendBean> unReadlist = new ArrayList<>();
        List<MessageListBean.FriendBean> list       = new ArrayList<>();

        try {
            if (uidList != null && uidList.size() > 0) {
                lastList=getLastListById(uidList);
                unReadlist=getUnReadListById(uidList);

                //组装最后数据
                if (lastList.size() > 0 && unReadlist.size() > 0) {
                    for (MessageListBean.FriendBean lastBean : lastList) {
                        int lastUserId = lastBean.getUserIntId();
                        for (MessageListBean.FriendBean unReadBean : unReadlist) {
                            int unReadUserId = unReadBean.getUserIntId();
                            if (lastUserId == unReadUserId) {
                                lastBean.setUnReadNum(unReadBean.getUnReadNum());
                            }
                        }
                        list.add(lastBean);
                    }
                } else {
                    list.addAll(lastList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * 获取指定id未读消息
     */
    public List<MessageListBean.FriendBean> getUnReadListById (List<MessageListBean.FriendBean> uidList) {
        List<MessageListBean.FriendBean> unReadlist = new ArrayList<>();
        try {
            if (uidList != null && uidList.size() > 0) {
                String dbName = mSession.getChatLocalBeanDao().getTablename();//DaoDbLocalHelper.getInstance().getDbName()
                String uidIn  = "";
                for (MessageListBean.FriendBean bean : uidList) {
                    int userIntId = bean.getUserIntId();
                    if (TextUtils.isEmpty(uidIn)) {
                        uidIn = userIntId + "";
                    } else {
                        uidIn = uidIn + "," + userIntId;
                    }
                }

                //根据uid进行分组并统计条数
                String unreadSql    = "select count(*) as unreadNum,TO_UID from " + dbName + " where TO_UID in(" + uidIn + ") and IS_READ = '1' group by TO_UID";
                Cursor unreadCursor = mSession.getDatabase().rawQuery(unreadSql, null);
                addUnreadBeans(unReadlist, unreadCursor);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //return list;
        return unReadlist;
    }

    /**
     * 获取指定多条UID的最新的一条数据
     */
    public List<MessageListBean.FriendBean> getLastListById (List<MessageListBean.FriendBean> uidList) {
        List<MessageListBean.FriendBean> lastList = new ArrayList<>();
        try {
            if (uidList != null && uidList.size() > 0) {
                String dbName = mSession.getChatLocalBeanDao().getTablename();//DaoDbLocalHelper.getInstance().getDbName()
                String uidIn  = "";
                for (MessageListBean.FriendBean bean : uidList) {
                    int userIntId = bean.getUserIntId();
                    if (TextUtils.isEmpty(uidIn)) {
                        uidIn = userIntId + "";
                    } else {
                        uidIn = uidIn + "," + userIntId;
                    }
                }

                //获取指定UID的最新的一条数据
                String sql     = "select * from (select * from " + dbName + " order by TO_UID, time) b GROUP BY b.TO_UID";//查询所有的uid中time最大的一条
                String sqlmore = "select * from (" + sql + ") a where TO_UID in(" + uidIn + ") GROUP BY a.TO_UID";//查询指定的uid中time最大的一条
                Cursor cursor  = mSession.getDatabase().rawQuery(sqlmore, null);
                addLastBeans(lastList, cursor);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lastList;
    }

    /**
     * 获取所有的未读消息数量
     */
    public int getUnReadCount () {
        QueryBuilder<ChatLocalBean> queryBuilder = mSession.getChatLocalBeanDao()
                .queryBuilder()
                .where(ChatLocalBeanDao.Properties.IsRead.eq("1"));
        long count = queryBuilder.count();
        return (int) count;
    }


    /***************************************** 改 ************************************************/
    /***************************************** 改 ************************************************/
    /***************************************** 改 ************************************************/

    //更新聊天信息
    public void updateChat (ChatLocalBean bean) {
        mSession.getChatLocalBeanDao().update(bean);
    }

    //更新聊天信息,把自己的消息变成撤回
    public void withdrawSelfChat (ChatLocalBean bean) {
        mSession.getChatLocalBeanDao().update(bean);
    }

    //更新聊天信息,把别人的消息变成撤回
    public void withdrawOtherChat (String sendTime, String content) {
        if (!TextUtils.isEmpty(sendTime)) {
            ChatLocalBean selectBean = mSession.getChatLocalBeanDao().queryBuilder()
                    .where(ChatLocalBeanDao.Properties.SendTime.eq(sendTime))
                    .orderDesc(ChatLocalBeanDao.Properties.Time)//orderDesc降序查找
                    .limit(1)
                    .unique();
            if (selectBean != null) {
                selectBean.setText(content);
                selectBean.setType("1");
                selectBean.setSubType("6");
                selectBean.setIsRead("2");
                mSession.getChatLocalBeanDao().update(selectBean);
            }
        }
    }

    /***************************************** 删 ************************************************/
    /***************************************** 删 ************************************************/
    /***************************************** 删 ************************************************/

    //删除聊天(根据id)
    public void deleteChatById (Long id) {
        mSession.getChatLocalBeanDao().deleteByKey(id);
    }

    //删除聊天(根据bean)
    public void deleteChatBean (ChatLocalBean bean) {
        mSession.getChatLocalBeanDao().delete(bean);
    }

    //批量删除聊天
    public void deleteChatList (List<ChatLocalBean> chatBeans) {
        mSession.getChatLocalBeanDao().deleteInTx(chatBeans);
    }

    //删除本地所有聊天
    public void deleteAllBills (String toUid) {
        deleteChatList(getChatList(toUid));
    }

    //删除本地所有数据
    public void deleteAll () {
        mSession.getChatLocalBeanDao().deleteAll();
    }


    //拼接最后一条数据
    private void addLastBeans (List<MessageListBean.FriendBean> list, Cursor cursor) {
        int toUid = cursor.getColumnIndex(ChatLocalBeanDao.Properties.ToUid.columnName);
        int type  = cursor.getColumnIndex(ChatLocalBeanDao.Properties.Type.columnName);
        int time  = cursor.getColumnIndex(ChatLocalBeanDao.Properties.Time.columnName);
        int text  = cursor.getColumnIndex(ChatLocalBeanDao.Properties.Text.columnName);
        //int isRead = cursor.getColumnIndex(ChatLocalBeanDao.Properties.IsRead.columnName);

        while (cursor.moveToNext()) {
            MessageListBean.FriendBean bean = new MessageListBean.FriendBean();
            bean.setUserIntId(Integer.parseInt(cursor.getString(toUid)));
            bean.setTime(cursor.getInt(time) + "");
            bean.setLastReadMessage(cursor.getString(text));
            bean.setType(cursor.getString(type));
            list.add(bean);
        }
        cursor.close();
        cursor=null;
    }

    //拼接未读数据
    private void addUnreadBeans (List<MessageListBean.FriendBean> list, Cursor cursor) {
        int toUid     = cursor.getColumnIndex(ChatLocalBeanDao.Properties.ToUid.columnName);
        int unReadNum = cursor.getColumnIndex("unreadNum");

        while (cursor.moveToNext()) {
            MessageListBean.FriendBean bean = new MessageListBean.FriendBean();
            bean.setUserIntId(Integer.parseInt(cursor.getString(toUid)));
            bean.setUnReadNum(cursor.getInt(unReadNum));
            list.add(bean);
        }
        cursor.close();
        cursor=null;
    }

}
