package com.maishuo.tingshuohenhaowan.api.response;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by yHai on 2021/2/3
 * EXPLAIN:
 */
public class PublishTagBean implements Parcelable {


    /**
     * uuid : 43eb0a8baa858c010226ea06acd7be6f
     * type_list : [{"id":10001,"name":"吐槽","type":1,"alert_content":null,"special":null},{"id":10002,"name":"热点","type":1,"alert_content":null,"special":null},{"id":10003,"name":"生活","type":1,"alert_content":null,"special":null},{"id":10004,"name":"搞笑","type":1,"alert_content":null,"special":null},{"id":10005,"name":"自然","type":1,"alert_content":null,"special":null},{"id":10006,"name":"音乐","type":1,"alert_content":"","special":""},{"id":10007,"name":"万象","type":1,"alert_content":"","special":""}]
     */

    private String             uuid;
    private List<TypeListBean>    type_list;
    private List<SoundeffectBean> Soundeffect;

    public void setSoundeffect (List<SoundeffectBean> SoundeffectBean){
        this.Soundeffect = SoundeffectBean;
    }

    public List<SoundeffectBean> getSoundeffect (){
        return this.Soundeffect;
    }

    public String getUuid () {
        return uuid;
    }

    public void setUuid (String uuid) {
        this.uuid = uuid;
    }

    public List<TypeListBean> getType_list () {
        return type_list;
    }

    public void setType_list (List<TypeListBean> type_list) {
        this.type_list = type_list;
    }

    public static class TypeListBean {
        /**
         * id : 10001
         * name : 吐槽
         * type : 1
         * alert_content : null
         * special : null
         */

        private int    id;
        private String name;
        private int    type;
        private Object alert_content;
        private Object special;
        private int showType;
        private String icon_img;

        public String getIcon_img () {
            return icon_img;
        }

        public void setIcon_img (String icon_img) {
            this.icon_img = icon_img;
        }

        public int getShowType () {
            return showType;
        }

        public void setShowType (int showType) {
            this.showType = showType;
        }

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

        public Object getAlert_content () {
            return alert_content;
        }

        public void setAlert_content (Object alert_content) {
            this.alert_content = alert_content;
        }

        public Object getSpecial () {
            return special;
        }

        public void setSpecial (Object special) {
            this.special = special;
        }
    }

    @Override
    public int describeContents () {
        return 0;
    }

    @Override
    public void writeToParcel (Parcel dest, int flags) {
        dest.writeString(this.uuid);
        dest.writeList(this.type_list);
        dest.writeTypedList(this.Soundeffect);
    }

    public void readFromParcel (Parcel source) {
        this.uuid = source.readString();
        this.type_list = new ArrayList<TypeListBean>();
        source.readList(this.type_list, TypeListBean.class.getClassLoader());
        this.Soundeffect = source.createTypedArrayList(SoundeffectBean.CREATOR);
    }

    public PublishTagBean () {
    }

    protected PublishTagBean (Parcel in) {
        this.uuid = in.readString();
        this.type_list = new ArrayList<TypeListBean>();
        in.readList(this.type_list, TypeListBean.class.getClassLoader());
        this.Soundeffect = in.createTypedArrayList(SoundeffectBean.CREATOR);
    }

    public static final Parcelable.Creator<PublishTagBean> CREATOR = new Parcelable.Creator<PublishTagBean>() {
        @Override
        public PublishTagBean createFromParcel (Parcel source) {
            return new PublishTagBean(source);
        }

        @Override
        public PublishTagBean[] newArray (int size) {
            return new PublishTagBean[size];
        }
    };
}
