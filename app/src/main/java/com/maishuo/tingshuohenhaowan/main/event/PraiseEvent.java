package com.maishuo.tingshuohenhaowan.main.event;

import com.maishuo.tingshuohenhaowan.api.response.PhonicListBean;

/**
 * author ：Seven
 * date : 2021/9/7
 * description :点赞逻辑：
 */
public class PraiseEvent {
    /**
     * 0-关注、1-推荐、2-其它留声页
     */
    public int    type;
    /**
     * 用户id
     */
    public String userId;
    /**
     * 点赞作品id
     */
    public int    id;
    /**
     * 点赞状态：0-取消点赞，1-点赞
     */
    public int    praiseStatus;
    /**
     * 点赞数量
     */
    public int    praiseNumber;

    public PraiseEvent (int type, PhonicListBean.ListBean bean) {
        this.type = type;
        this.userId = bean.getUser_id();
        this.id = bean.getId();
        this.praiseStatus = bean.getIs_praise();
        this.praiseNumber = bean.getPraise_num();
    }
}
