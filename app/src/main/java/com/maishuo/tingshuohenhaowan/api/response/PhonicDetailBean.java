package com.maishuo.tingshuohenhaowan.api.response;

/**
 * Create by yHai on 2021/2/2
 * EXPLAIN:
 */
public class PhonicDetailBean {

    /**
     * id : 11825
     * title : null
     * desc : 舒肤佳，12小时长久呵护
     * image_path : /users/01b063cc545685b18ad6c1af3c14c006/stayvoice/images/2020/65edf5b2af0d5a8a4fc919d8e5631ab1.jpg
     * voice_path : /users/01b063cc545685b18ad6c1af3c14c006/stayvoice/voice/2020/65edf5b2af0d5a8a4fc919d8e5631ab1.mp3
     * voice_time : 15125
     * voice_size : 369501
     * voice_uptype : 1
     * praise_num : 4
     * played_num : 27
     * pv_played_num : 303
     * comment_num : 1
     * share_num : 1
     * title_ai_status : 0
     * desc_ai_status : 1
     * image_ai_status : 1
     * voice_ai_status : 1
     * status : 1
     * is_show : 1
     * is_del : 0
     * user_id : 01b063cc545685b18ad6c1af3c14c006
     * created_at : 2020-11-27 16:33:12
     * updated_at : 2021-02-01 10:32:47
     * show_num : 95
     * is_top : 0
     * tmp_type_ids : null
     * source : 1
     * is_check : 0
     * v_share_num : 0
     * v_praise_num : 0
     * v_comment_num : 0
     * v_played_num : 0
     * is_speech_import : 0
     * is_ad : 1
     * voice_volume : 50
     * bg_music : 1
     * bg_music_volume : 50
     * bg_img : null
     * top_index : 0
     * share_num_str : 1
     * praise_num_str : 4
     * comment_num_str : 1
     * played_num_str : 27
     * uid : 1107
     * uname : 舒肤佳
     * sex : 1
     * avatar : http://test.tingshuowan.com/listen/path?url=/users/01b063cc545685b18ad6c1af3c14c006/images/2020/0161645609c67cb8fc64c0f3df69f71b.jpg
     * type_names : 生活
     * type_ids : 10003
     * is_praise : 0
     * is_attention : 0
     * is_openad : 0
     * share_title : 快来和我一起收听【舒肤佳】的音频吧
     * share_desc : 舒肤佳，12小时长久呵护
     * share_url : http://livetest.tingshuowan.com/h5/stayvoiceshare?stayvoice_id=11825&sign=1234
     * share_thumbimage : /users/01b063cc545685b18ad6c1af3c14c006/stayvoice/images/2020/65edf5b2af0d5a8a4fc919d8e5631ab1.jpg
     * share_wxminprogram_type : 2
     * share_wxminprogram_appid : wx123456789
     * share_wxminprogram_path : /h5/stayvoiceshare?stayvoice_id=11825&sign=1234
     * adinfo : {"id":1,"stayvoice_id":11825,"logo":"http://resource.corpize.com/image/shufujia_202.jpeg","title":"舒肤佳，12小时长久呵护","desc":"肌肤健康不仅保养,还要保护.使用舒肤佳沐浴露持续保护全家肌肤健康.舒肤佳经典净护系列沐浴露含健康配方,温和去除皮肤上的有害物质,有助肌肤保持健康","is_openlayer":0,"android_url":"","ios_url":"","phone_shakeme":"http://api.tingshuowan.com/listen/path?url=/adVoice/phone_shakeme_start.mp3 ","deeplink_shakeme":"http://api.tingshuowan.com/listen/path?url=/adVoice/deeplink_shakeme_start.mp3","download_shakeme":"http://api.tingshuowan.com/listen/path?url=/adVoice/shakeme_start.mp3","openldp":"http://api.tingshuowan.com/listen/path?url=/adVoice/openldp_shakeme_start.mp3","ldp":"https://www.safeguard.com.cn/zh-cn","deeplink":null,"action":4,"wait":5,"interval":5,"volume":30,"created_at":"2020-11-26 17:06:16","updated_at":"2020-11-27 10:06:26","is_shakeme":0,"phone_number":"021-61720502"}
     */

    private int        id;
    private Object     title;
    private String     desc;
    private String     image_path;
    private String     voice_path;
    private int        voice_time;
    private int        voice_size;
    private int        voice_uptype;
    private int        praise_num;
    private int        played_num;
    private int        pv_played_num;
    private int        comment_num;
    private int        share_num;
    private int        title_ai_status;
    private int        desc_ai_status;
    private int        image_ai_status;
    private int        voice_ai_status;
    private int        status;
    private int        is_show;
    private int        is_del;
    private String     user_id;
    private String     created_at;
    private String     updated_at;
    private int        show_num;
    private int        is_top;
    private Object     tmp_type_ids;
    private int        source;
    private int        is_check;
    private int        v_share_num;
    private int        v_praise_num;
    private int        v_comment_num;
    private int        v_played_num;
    private int        is_speech_import;
    private int        is_ad;
    private int        voice_volume;
    private int        bg_music;
    private int        bg_music_volume;
    private Object     bg_img;
    private int        top_index;
    private String     share_num_str;
    private String     praise_num_str;
    private String     comment_num_str;
    private String     played_num_str;
    private String     uid;
    private String     uname;
    private int        sex;
    private String     avatar;
    private String     type_names;
    private String     type_ids;
    private int        is_praise;
    private int        is_attention;
    private int        is_openad;
    private String     share_title;
    private String     share_desc;
    private String     share_url;
    private String     share_thumbimage;
    private int        share_wxminprogram_type;
    private String     share_wxminprogram_appid;
    private String     share_wxminprogram_path;
    private AdinfoBean adinfo;

    public int getId () {
        return id;
    }

    public void setId (int id) {
        this.id = id;
    }

    public Object getTitle () {
        return title;
    }

    public void setTitle (Object title) {
        this.title = title;
    }

    public String getDesc () {
        return desc;
    }

    public void setDesc (String desc) {
        this.desc = desc;
    }

    public String getImage_path () {
        return image_path;
    }

    public void setImage_path (String image_path) {
        this.image_path = image_path;
    }

    public String getVoice_path () {
        return voice_path;
    }

    public void setVoice_path (String voice_path) {
        this.voice_path = voice_path;
    }

    public int getVoice_time () {
        return voice_time;
    }

    public void setVoice_time (int voice_time) {
        this.voice_time = voice_time;
    }

    public int getVoice_size () {
        return voice_size;
    }

    public void setVoice_size (int voice_size) {
        this.voice_size = voice_size;
    }

    public int getVoice_uptype () {
        return voice_uptype;
    }

    public void setVoice_uptype (int voice_uptype) {
        this.voice_uptype = voice_uptype;
    }

    public int getPraise_num () {
        return praise_num;
    }

    public void setPraise_num (int praise_num) {
        this.praise_num = praise_num;
    }

    public int getPlayed_num () {
        return played_num;
    }

    public void setPlayed_num (int played_num) {
        this.played_num = played_num;
    }

    public int getPv_played_num () {
        return pv_played_num;
    }

    public void setPv_played_num (int pv_played_num) {
        this.pv_played_num = pv_played_num;
    }

    public int getComment_num () {
        return comment_num;
    }

    public void setComment_num (int comment_num) {
        this.comment_num = comment_num;
    }

    public int getShare_num () {
        return share_num;
    }

    public void setShare_num (int share_num) {
        this.share_num = share_num;
    }

    public int getTitle_ai_status () {
        return title_ai_status;
    }

    public void setTitle_ai_status (int title_ai_status) {
        this.title_ai_status = title_ai_status;
    }

    public int getDesc_ai_status () {
        return desc_ai_status;
    }

    public void setDesc_ai_status (int desc_ai_status) {
        this.desc_ai_status = desc_ai_status;
    }

    public int getImage_ai_status () {
        return image_ai_status;
    }

    public void setImage_ai_status (int image_ai_status) {
        this.image_ai_status = image_ai_status;
    }

    public int getVoice_ai_status () {
        return voice_ai_status;
    }

    public void setVoice_ai_status (int voice_ai_status) {
        this.voice_ai_status = voice_ai_status;
    }

    public int getStatus () {
        return status;
    }

    public void setStatus (int status) {
        this.status = status;
    }

    public int getIs_show () {
        return is_show;
    }

    public void setIs_show (int is_show) {
        this.is_show = is_show;
    }

    public int getIs_del () {
        return is_del;
    }

    public void setIs_del (int is_del) {
        this.is_del = is_del;
    }

    public String getUser_id () {
        return user_id;
    }

    public void setUser_id (String user_id) {
        this.user_id = user_id;
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

    public int getShow_num () {
        return show_num;
    }

    public void setShow_num (int show_num) {
        this.show_num = show_num;
    }

    public int getIs_top () {
        return is_top;
    }

    public void setIs_top (int is_top) {
        this.is_top = is_top;
    }

    public Object getTmp_type_ids () {
        return tmp_type_ids;
    }

    public void setTmp_type_ids (Object tmp_type_ids) {
        this.tmp_type_ids = tmp_type_ids;
    }

    public int getSource () {
        return source;
    }

    public void setSource (int source) {
        this.source = source;
    }

    public int getIs_check () {
        return is_check;
    }

    public void setIs_check (int is_check) {
        this.is_check = is_check;
    }

    public int getV_share_num () {
        return v_share_num;
    }

    public void setV_share_num (int v_share_num) {
        this.v_share_num = v_share_num;
    }

    public int getV_praise_num () {
        return v_praise_num;
    }

    public void setV_praise_num (int v_praise_num) {
        this.v_praise_num = v_praise_num;
    }

    public int getV_comment_num () {
        return v_comment_num;
    }

    public void setV_comment_num (int v_comment_num) {
        this.v_comment_num = v_comment_num;
    }

    public int getV_played_num () {
        return v_played_num;
    }

    public void setV_played_num (int v_played_num) {
        this.v_played_num = v_played_num;
    }

    public int getIs_speech_import () {
        return is_speech_import;
    }

    public void setIs_speech_import (int is_speech_import) {
        this.is_speech_import = is_speech_import;
    }

    public int getIs_ad () {
        return is_ad;
    }

    public void setIs_ad (int is_ad) {
        this.is_ad = is_ad;
    }

    public int getVoice_volume () {
        return voice_volume;
    }

    public void setVoice_volume (int voice_volume) {
        this.voice_volume = voice_volume;
    }

    public int getBg_music () {
        return bg_music;
    }

    public void setBg_music (int bg_music) {
        this.bg_music = bg_music;
    }

    public int getBg_music_volume () {
        return bg_music_volume;
    }

    public void setBg_music_volume (int bg_music_volume) {
        this.bg_music_volume = bg_music_volume;
    }

    public Object getBg_img () {
        return bg_img;
    }

    public void setBg_img (Object bg_img) {
        this.bg_img = bg_img;
    }

    public int getTop_index () {
        return top_index;
    }

    public void setTop_index (int top_index) {
        this.top_index = top_index;
    }

    public String getShare_num_str () {
        return share_num_str;
    }

    public void setShare_num_str (String share_num_str) {
        this.share_num_str = share_num_str;
    }

    public String getPraise_num_str () {
        return praise_num_str;
    }

    public void setPraise_num_str (String praise_num_str) {
        this.praise_num_str = praise_num_str;
    }

    public String getComment_num_str () {
        return comment_num_str;
    }

    public void setComment_num_str (String comment_num_str) {
        this.comment_num_str = comment_num_str;
    }

    public String getPlayed_num_str () {
        return played_num_str;
    }

    public void setPlayed_num_str (String played_num_str) {
        this.played_num_str = played_num_str;
    }

    public String getUid () {
        return uid;
    }

    public void setUid (String uid) {
        this.uid = uid;
    }

    public String getUname () {
        return uname;
    }

    public void setUname (String uname) {
        this.uname = uname;
    }

    public int getSex () {
        return sex;
    }

    public void setSex (int sex) {
        this.sex = sex;
    }

    public String getAvatar () {
        return avatar;
    }

    public void setAvatar (String avatar) {
        this.avatar = avatar;
    }

    public String getType_names () {
        return type_names;
    }

    public void setType_names (String type_names) {
        this.type_names = type_names;
    }

    public String getType_ids () {
        return type_ids;
    }

    public void setType_ids (String type_ids) {
        this.type_ids = type_ids;
    }

    public int getIs_praise () {
        return is_praise;
    }

    public void setIs_praise (int is_praise) {
        this.is_praise = is_praise;
    }

    public int getIs_attention () {
        return is_attention;
    }

    public void setIs_attention (int is_attention) {
        this.is_attention = is_attention;
    }

    public int getIs_openad () {
        return is_openad;
    }

    public void setIs_openad (int is_openad) {
        this.is_openad = is_openad;
    }

    public String getShare_title () {
        return share_title;
    }

    public void setShare_title (String share_title) {
        this.share_title = share_title;
    }

    public String getShare_desc () {
        return share_desc;
    }

    public void setShare_desc (String share_desc) {
        this.share_desc = share_desc;
    }

    public String getShare_url () {
        return share_url;
    }

    public void setShare_url (String share_url) {
        this.share_url = share_url;
    }

    public String getShare_thumbimage () {
        return share_thumbimage;
    }

    public void setShare_thumbimage (String share_thumbimage) {
        this.share_thumbimage = share_thumbimage;
    }

    public int getShare_wxminprogram_type () {
        return share_wxminprogram_type;
    }

    public void setShare_wxminprogram_type (int share_wxminprogram_type) {
        this.share_wxminprogram_type = share_wxminprogram_type;
    }

    public String getShare_wxminprogram_appid () {
        return share_wxminprogram_appid;
    }

    public void setShare_wxminprogram_appid (String share_wxminprogram_appid) {
        this.share_wxminprogram_appid = share_wxminprogram_appid;
    }

    public String getShare_wxminprogram_path () {
        return share_wxminprogram_path;
    }

    public void setShare_wxminprogram_path (String share_wxminprogram_path) {
        this.share_wxminprogram_path = share_wxminprogram_path;
    }

    public AdinfoBean getAdinfo () {
        return adinfo;
    }

    public void setAdinfo (AdinfoBean adinfo) {
        this.adinfo = adinfo;
    }

    public static class AdinfoBean {
        /**
         * id : 1
         * stayvoice_id : 11825
         * logo : http://resource.corpize.com/image/shufujia_202.jpeg
         * title : 舒肤佳，12小时长久呵护
         * desc : 肌肤健康不仅保养,还要保护.使用舒肤佳沐浴露持续保护全家肌肤健康.舒肤佳经典净护系列沐浴露含健康配方,温和去除皮肤上的有害物质,有助肌肤保持健康
         * is_openlayer : 0
         * android_url :
         * ios_url :
         * phone_shakeme : http://api.tingshuowan.com/listen/path?url=/adVoice/phone_shakeme_start.mp3
         * deeplink_shakeme : http://api.tingshuowan.com/listen/path?url=/adVoice/deeplink_shakeme_start.mp3
         * download_shakeme : http://api.tingshuowan.com/listen/path?url=/adVoice/shakeme_start.mp3
         * openldp : http://api.tingshuowan.com/listen/path?url=/adVoice/openldp_shakeme_start.mp3
         * ldp : https://www.safeguard.com.cn/zh-cn
         * deeplink : null
         * action : 4
         * wait : 5
         * interval : 5
         * volume : 30
         * created_at : 2020-11-26 17:06:16
         * updated_at : 2020-11-27 10:06:26
         * is_shakeme : 0
         * phone_number : 021-61720502
         */

        private int    id;
        private int    stayvoice_id;
        private String logo;
        private String title;
        private String desc;
        private int    is_openlayer;
        private String android_url;
        private String ios_url;
        private String phone_shakeme;
        private String deeplink_shakeme;
        private String download_shakeme;
        private String openldp;
        private String ldp;
        private Object deeplink;
        private int    action;
        private int    wait;
        private int    interval;
        private int    volume;
        private String created_at;
        private String updated_at;
        private int    is_shakeme;
        private String phone_number;

        public int getId () {
            return id;
        }

        public void setId (int id) {
            this.id = id;
        }

        public int getStayvoice_id () {
            return stayvoice_id;
        }

        public void setStayvoice_id (int stayvoice_id) {
            this.stayvoice_id = stayvoice_id;
        }

        public String getLogo () {
            return logo;
        }

        public void setLogo (String logo) {
            this.logo = logo;
        }

        public String getTitle () {
            return title;
        }

        public void setTitle (String title) {
            this.title = title;
        }

        public String getDesc () {
            return desc;
        }

        public void setDesc (String desc) {
            this.desc = desc;
        }

        public int getIs_openlayer () {
            return is_openlayer;
        }

        public void setIs_openlayer (int is_openlayer) {
            this.is_openlayer = is_openlayer;
        }

        public String getAndroid_url () {
            return android_url;
        }

        public void setAndroid_url (String android_url) {
            this.android_url = android_url;
        }

        public String getIos_url () {
            return ios_url;
        }

        public void setIos_url (String ios_url) {
            this.ios_url = ios_url;
        }

        public String getPhone_shakeme () {
            return phone_shakeme;
        }

        public void setPhone_shakeme (String phone_shakeme) {
            this.phone_shakeme = phone_shakeme;
        }

        public String getDeeplink_shakeme () {
            return deeplink_shakeme;
        }

        public void setDeeplink_shakeme (String deeplink_shakeme) {
            this.deeplink_shakeme = deeplink_shakeme;
        }

        public String getDownload_shakeme () {
            return download_shakeme;
        }

        public void setDownload_shakeme (String download_shakeme) {
            this.download_shakeme = download_shakeme;
        }

        public String getOpenldp () {
            return openldp;
        }

        public void setOpenldp (String openldp) {
            this.openldp = openldp;
        }

        public String getLdp () {
            return ldp;
        }

        public void setLdp (String ldp) {
            this.ldp = ldp;
        }

        public Object getDeeplink () {
            return deeplink;
        }

        public void setDeeplink (Object deeplink) {
            this.deeplink = deeplink;
        }

        public int getAction () {
            return action;
        }

        public void setAction (int action) {
            this.action = action;
        }

        public int getWait () {
            return wait;
        }

        public void setWait (int wait) {
            this.wait = wait;
        }

        public int getInterval () {
            return interval;
        }

        public void setInterval (int interval) {
            this.interval = interval;
        }

        public int getVolume () {
            return volume;
        }

        public void setVolume (int volume) {
            this.volume = volume;
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

        public int getIs_shakeme () {
            return is_shakeme;
        }

        public void setIs_shakeme (int is_shakeme) {
            this.is_shakeme = is_shakeme;
        }

        public String getPhone_number () {
            return phone_number;
        }

        public void setPhone_number (String phone_number) {
            this.phone_number = phone_number;
        }
    }

}
