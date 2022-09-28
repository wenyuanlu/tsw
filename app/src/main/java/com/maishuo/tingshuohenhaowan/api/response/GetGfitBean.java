package com.maishuo.tingshuohenhaowan.api.response;

import java.util.List;

/**
 * author ：yh
 * date : 2021/1/18 11:32
 * description :
 */
public final class GetGfitBean {


    /**
     * diamodIos : 490
     * diamodAndroid : 20029
     * integral : 9994
     * gifts : [{"id":47,"name":"点赞","type":1,"unit_price":12,"vip_unit_price":12,"effect":2,"gift_version":8,"img":"http://test.tingshuowan.com/listen/path?url=/live/gifts/img_c1c85f5f8d0c3aa43fe106ed12fedf44.png","effect_img":""},{"id":20,"name":"666","type":1,"unit_price":66,"vip_unit_price":66,"effect":1,"gift_version":5,"img":"http://test.tingshuowan.com/listen/path?url=/live/gifts/img_637096cbb05f40e3b8367314140efd6a.png","effect_img":"http://test.tingshuowan.com/listen/path?url=/live/gifts/effectImg_e0b526b4512d1ee114d8c9d6970bb623.svga"},{"id":12,"name":"比心","type":1,"unit_price":1,"vip_unit_price":1,"effect":2,"gift_version":8,"img":"http://test.tingshuowan.com/listen/path?url=/live/gifts/img_d681713bfb3ac63d8563231e64d2977e.png","effect_img":""}]
     * goods : [{"goodsId":1,"goodsName":"帝皇","openMoney":3998,"extendMoney":1998,"goodsVersion":3,"goodsImage":"http://test.tingshuowan.com/listen/path?url=/live/goods/img_e756fe19d74cf82425f4adbe711a1915.png","openDiamond":39980,"extendDiamond":19980,"specialEffectsImage":"http://test.tingshuowan.com/listen/path?url=/live/goods/svga_hd.svga","tags":1,"powers":0,"details":"进场特效：五爪神龙 \\n超值优惠：开通赠送39980玩钻、续费赠送19980玩钻 \\n万众瞩目：发言帝皇标签 \\n贵宾特权：防踢防禁言、专人客服在线  "},{"goodsId":2,"goodsName":"君王","openMoney":1998,"extendMoney":998,"goodsVersion":3,"goodsImage":"http://test.tingshuowan.com/listen/path?url=/live/goods/img_0be441774c62819d1a25da5c0efee759.png","openDiamond":19980,"extendDiamond":998,"specialEffectsImage":"http://test.tingshuowan.com/listen/path?url=/live/goods/svga_jw.svga","tags":2,"powers":0,"details":"进场特效：赤炎朱雀 \\n超值优惠：开通赠送19980玩钻、续费赠送9980玩钻 \\n万众瞩目：发言君王标签 \\n贵宾特权：专人客服在线  "},{"goodsId":3,"goodsName":"诸侯","openMoney":998,"extendMoney":518,"goodsVersion":2,"goodsImage":"http://test.tingshuowan.com/listen/path?url=/live/goods/img_fc6d5a77193a7677c1e6b894453e93cd.png","openDiamond":9980,"extendDiamond":5180,"specialEffectsImage":"http://test.tingshuowan.com/listen/path?url=/live/goods/svga_zh.svga","tags":2,"powers":0,"details":"进场特效：黄金将领 \\n超值优惠：开通赠送9980玩钻、续费赠送5180玩钻 \\n万众瞩目：发言诸侯标签 \\n贵宾特权：客服在线  "},{"goodsId":4,"goodsName":"太守","openMoney":518,"extendMoney":258,"goodsVersion":4,"goodsImage":"http://test.tingshuowan.com/listen/path?url=/live/goods/img_ddf4f042b7d879d5f04bcc1ca654789b.png","openDiamond":5180,"extendDiamond":2580,"specialEffectsImage":"http://test.tingshuowan.com/listen/path?url=/live/goods/svga_ts.svga","tags":2,"powers":0,"details":"进场特效：齐天大圣 \\n超值优惠：开通赠送5180玩钻、续费赠送2580玩钻 \\n万众瞩目：发言太守标签 \\n贵宾特权：客服在线  "},{"goodsId":5,"goodsName":"侠客","openMoney":198,"extendMoney":98,"goodsVersion":2,"goodsImage":"http://test.tingshuowan.com/listen/path?url=/live/goods/img_1624f3096ae469f216335757b22bf0a5.png","openDiamond":1880,"extendDiamond":980,"specialEffectsImage":"http://test.tingshuowan.com/listen/path?url=/live/goods/svga_xk.svga","tags":2,"powers":0,"details":"进场特效：皮卡丘 \\n超值优惠：开通赠送1980玩钻、侠客头像框、续费赠送980玩钻 \\n万众瞩目：发言侠客标签 \\n贵宾特权：客服在线  "}]
     */

    private Long             diamodIos;
    private Long             diamodAndroid;
    private int             integral;
    private List<GiftsBean> gifts;
    private List<GoodsBean> goods;

    public Long getDiamodIos () {
        return diamodIos;
    }

    public void setDiamodIos (Long diamodIos) {
        this.diamodIos = diamodIos;
    }

    public Long getDiamodAndroid () {
        return diamodAndroid;
    }

    public void setDiamodAndroid (Long diamodAndroid) {
        this.diamodAndroid = diamodAndroid;
    }

    public int getIntegral () {
        return integral;
    }

    public void setIntegral (int integral) {
        this.integral = integral;
    }

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

    public static class GiftsBean {
        /**
         * id : 47
         * name : 点赞
         * type : 1
         * unit_price : 12
         * vip_unit_price : 12
         * effect : 2
         * gift_version : 8
         * img : http://test.tingshuowan.com/listen/path?url=/live/gifts/img_c1c85f5f8d0c3aa43fe106ed12fedf44.png
         * effect_img :
         */

        private int     id;
        private String  name;
        private int     type;
        private int     unit_price;
        private int     vip_unit_price;
        private int     effect;
        private int     gift_version;
        private String  img;
        private String  effect_img;
        private boolean isSelect = false;

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

        public boolean isSelect () {
            return isSelect;
        }

        public void setSelect (boolean select) {
            isSelect = select;
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
         * details : 进场特效：五爪神龙 \n超值优惠：开通赠送39980玩钻、续费赠送19980玩钻 \n万众瞩目：发言帝皇标签 \n贵宾特权：防踢防禁言、专人客服在线
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
}