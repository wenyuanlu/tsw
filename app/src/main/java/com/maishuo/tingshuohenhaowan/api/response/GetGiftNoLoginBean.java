package com.maishuo.tingshuohenhaowan.api.response;

import java.util.List;

/**
 * author: yh
 * date: 2021/2/3 14:50
 * description: 不需要登录的礼物接口返回,用于下载
 */
public class GetGiftNoLoginBean {
    private List<GiftsBean>       gifts;
    private List<GoodsBean>       goods;
    private List<SoundeffectBean> soundeffect;

    public List<GiftsBean> getGifts () {
        return gifts;
    }

    public void setGifts (List<GiftsBean> gifts) {
        this.gifts = gifts;
    }

    public List<GoodsBean> getGoods () {
        return goods;
    }

    public void setGoods (List<GoodsBean> goods) {
        this.goods = goods;
    }

    public List<SoundeffectBean> getSoundeffect () {
        return soundeffect;
    }

    public void setSoundeffect (List<SoundeffectBean> soundeffect) {
        this.soundeffect = soundeffect;
    }

    public static class GiftsBean {
        /**
         * id : 12
         * name : 比心
         * type : 1
         * unit_price : 1
         * vip_unit_price : 1
         * effect : 2
         * gift_version : 8
         * img : http://test.tingshuowan.com/listen/path?url=/live/gifts/img_d681713bfb3ac63d8563231e64d2977e.png
         * effect_img :
         */

        private int    id;
        private String name;
        private int    type;
        private int    unit_price;
        private int    vip_unit_price;
        private int    effect;
        private int    gift_version;
        private String img;
        private String effect_img;

        public int getId () {
            return id;
        }

        public void setId (int id) {
            this.id = id;
        }

        public String getName () {
            return name;
        }

        public void setName (String name) {
            this.name = name;
        }

        public int getType () {
            return type;
        }

        public void setType (int type) {
            this.type = type;
        }

        public int getUnit_price () {
            return unit_price;
        }

        public void setUnit_price (int unit_price) {
            this.unit_price = unit_price;
        }

        public int getVip_unit_price () {
            return vip_unit_price;
        }

        public void setVip_unit_price (int vip_unit_price) {
            this.vip_unit_price = vip_unit_price;
        }

        public int getEffect () {
            return effect;
        }

        public void setEffect (int effect) {
            this.effect = effect;
        }

        public int getGift_version () {
            return gift_version;
        }

        public void setGift_version (int gift_version) {
            this.gift_version = gift_version;
        }

        public String getImg () {
            return img;
        }

        public void setImg (String img) {
            this.img = img;
        }

        public String getEffect_img () {
            return effect_img;
        }

        public void setEffect_img (String effect_img) {
            this.effect_img = effect_img;
        }
    }

    public static class GoodsBean {
        /**
         * goodsId : 1
         * goodsName : 帝皇
         * openMoney : 3998
         * extendMoney : 1998
         * goodsVersion : 3
         * goodsImage : http://test.tingshuowan.com/listen/path?url=/live/goods/img_e756fe19d74cf82425f4adbe711a1915.png
         * openDiamond : 39980
         * extendDiamond : 19980
         * specialEffectsImage : http://test.tingshuowan.com/listen/path?url=/live/goods/svga_hd.svga
         * tags : 1
         * powers : 0
         * details : 进场特效：五爪神龙 超值优惠：开通赠送39980玩钻、续费赠送19980玩钻 \n万众瞩目：发言帝皇标签 \n贵宾特权：防踢防禁言、专人客服在线
         */

        private int    goodsId;
        private String goodsName;
        private int    openMoney;
        private int    extendMoney;
        private int    goodsVersion;
        private String goodsImage;
        private int    openDiamond;
        private int    extendDiamond;
        private String specialEffectsImage;
        private int    tags;
        private int    powers;
        private String details;

        public int getGoodsId () {
            return goodsId;
        }

        public void setGoodsId (int goodsId) {
            this.goodsId = goodsId;
        }

        public String getGoodsName () {
            return goodsName;
        }

        public void setGoodsName (String goodsName) {
            this.goodsName = goodsName;
        }

        public int getOpenMoney () {
            return openMoney;
        }

        public void setOpenMoney (int openMoney) {
            this.openMoney = openMoney;
        }

        public int getExtendMoney () {
            return extendMoney;
        }

        public void setExtendMoney (int extendMoney) {
            this.extendMoney = extendMoney;
        }

        public int getGoodsVersion () {
            return goodsVersion;
        }

        public void setGoodsVersion (int goodsVersion) {
            this.goodsVersion = goodsVersion;
        }

        public String getGoodsImage () {
            return goodsImage;
        }

        public void setGoodsImage (String goodsImage) {
            this.goodsImage = goodsImage;
        }

        public int getOpenDiamond () {
            return openDiamond;
        }

        public void setOpenDiamond (int openDiamond) {
            this.openDiamond = openDiamond;
        }

        public int getExtendDiamond () {
            return extendDiamond;
        }

        public void setExtendDiamond (int extendDiamond) {
            this.extendDiamond = extendDiamond;
        }

        public String getSpecialEffectsImage () {
            return specialEffectsImage;
        }

        public void setSpecialEffectsImage (String specialEffectsImage) {
            this.specialEffectsImage = specialEffectsImage;
        }

        public int getTags () {
            return tags;
        }

        public void setTags (int tags) {
            this.tags = tags;
        }

        public int getPowers () {
            return powers;
        }

        public void setPowers (int powers) {
            this.powers = powers;
        }

        public String getDetails () {
            return details;
        }

        public void setDetails (String details) {
            this.details = details;
        }
    }

    public static class SoundeffectBean {
        /**
         * id : 17
         * name : 2222
         * type : 1
         * img_path : http://test.tingshuowan.com/listen/path?url=/live/SoundEffects/img_5f878072e3d1d6c58a472f717bcf87a2.jpeg
         * sound_path : http://test.tingshuowan.com/listen/path?url=/live/SoundEffects/sound_e92bc0c83ac6510357bb81457927e7a7.mp3
         * sort : 3333
         * version : 7
         * status : 1
         * created_at : 2021-02-24 10:10:41
         * updated_at : 2021-02-24 14:51:23
         */

        private int    id;
        private String name;
        private int    type;
        private String img_path;
        private String sound_path;
        private int    sort;
        private int    version;
        private int    status;
        private String created_at;
        private String updated_at;

        public int getId () {
            return id;
        }

        public void setId (int id) {
            this.id = id;
        }

        public String getName () {
            return name;
        }

        public void setName (String name) {
            this.name = name;
        }

        public int getType () {
            return type;
        }

        public void setType (int type) {
            this.type = type;
        }

        public String getImg_path () {
            return img_path;
        }

        public void setImg_path (String img_path) {
            this.img_path = img_path;
        }

        public String getSound_path () {
            return sound_path;
        }

        public void setSound_path (String sound_path) {
            this.sound_path = sound_path;
        }

        public int getSort () {
            return sort;
        }

        public void setSort (int sort) {
            this.sort = sort;
        }

        public int getVersion () {
            return version;
        }

        public void setVersion (int version) {
            this.version = version;
        }

        public int getStatus () {
            return status;
        }

        public void setStatus (int status) {
            this.status = status;
        }

        public String getCreated_at () {
            return created_at;
        }

        public void setCreated_at (String created_at) {
            this.created_at = created_at;
        }

        public String getUpdated_at () {
            return updated_at;
        }

        public void setUpdated_at (String updated_at) {
            this.updated_at = updated_at;
        }
    }
}
