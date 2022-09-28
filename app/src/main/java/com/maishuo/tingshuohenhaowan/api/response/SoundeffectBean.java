package com.maishuo.tingshuohenhaowan.api.response;

import android.os.Parcel;
import android.os.Parcelable;

public class SoundeffectBean implements Parcelable {

    private int id;
    private String name;
    private int type;
    private String img_path;
    private String sound_path;
    private int sort;
    private int version;
    private int status;
    private String created_at;
    private String updated_at;

    public void setId (int id) {
        this.id = id;
    }

    public int getId () {
        return this.id;
    }

    public void setName (String name) {
        this.name = name;
    }

    public String getName () {
        return this.name;
    }

    public void setType (int type) {
        this.type = type;
    }

    public int getType () {
        return this.type;
    }

    public void setImg_path (String img_path) {
        this.img_path = img_path;
    }

    public String getImg_path () {
        return this.img_path;
    }

    public void setSound_path (String sound_path) {
        this.sound_path = sound_path;
    }

    public String getSound_path () {
        return this.sound_path;
    }

    public void setSort (int sort) {
        this.sort = sort;
    }

    public int getSort () {
        return this.sort;
    }

    public void setVersion (int version) {
        this.version = version;
    }

    public int getVersion () {
        return this.version;
    }

    public void setStatus (int status) {
        this.status = status;
    }

    public int getStatus () {
        return this.status;
    }

    public void setCreated_at (String created_at) {
        this.created_at = created_at;
    }

    public String getCreated_at () {
        return this.created_at;
    }

    public void setUpdated_at (String updated_at) {
        this.updated_at = updated_at;
    }

    public String getUpdated_at () {
        return this.updated_at;
    }

    @Override
    public int describeContents () {
        return 0;
    }

    @Override
    public void writeToParcel (Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeInt(this.type);
        dest.writeString(this.img_path);
        dest.writeString(this.sound_path);
        dest.writeInt(this.sort);
        dest.writeInt(this.version);
        dest.writeInt(this.status);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
    }

    public void readFromParcel (Parcel source) {
        this.id = source.readInt();
        this.name = source.readString();
        this.type = source.readInt();
        this.img_path = source.readString();
        this.sound_path = source.readString();
        this.sort = source.readInt();
        this.version = source.readInt();
        this.status = source.readInt();
        this.created_at = source.readString();
        this.updated_at = source.readString();
    }

    public SoundeffectBean () {
    }

    protected SoundeffectBean (Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.type = in.readInt();
        this.img_path = in.readString();
        this.sound_path = in.readString();
        this.sort = in.readInt();
        this.version = in.readInt();
        this.status = in.readInt();
        this.created_at = in.readString();
        this.updated_at = in.readString();
    }

    public static final Parcelable.Creator<SoundeffectBean> CREATOR = new Parcelable.Creator<SoundeffectBean>() {
        @Override
        public SoundeffectBean createFromParcel (Parcel source) {
            return new SoundeffectBean(source);
        }

        @Override
        public SoundeffectBean[] newArray (int size) {
            return new SoundeffectBean[size];
        }
    };
}