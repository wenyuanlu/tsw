package com.maishuo.tingshuohenhaowan.api.response;

/**
 * Create by yun on 2020/12/25
 * EXPLAIN:
 */
public class PhonicBean {


    /**
     * prev : {}
     * current : {"id":10416,"title":null,"desc":"有感情的朗读《三字经》\u2014\u2014《最美的遇见》，第八集#10月9日声优戴超行直播间","image_path":"http://test.tingshuowan.com/listen/path?url=/default/stayvoice/initimg/daichaohang.jpg","voice_path":"http://test.tingshuowan.com/listen/path?url=/default/stayvoice/initmp3/S24.mp3","voice_time":179192,"voice_size":2875694,"voice_uptype":0,"praise_num":0,"played_num":32,"pv_played_num":193,"comment_num":2,"share_num":102,"title_ai_status":0,"desc_ai_status":0,"image_ai_status":0,"voice_ai_status":0,"status":1,"is_show":1,"is_del":0,"user_id":"9b8ee6d48f5aed00d97de0d0016d5078","created_at":"2020-10-29 21:08:57","updated_at":"2020-12-25 10:17:34","show_num":93,"is_top":1,"tmp_type_ids":"","source":0,"is_check":1,"v_share_num":0,"v_praise_num":0,"v_comment_num":0,"v_played_num":0,"is_speech_import":0,"is_ad":0,"_request":{"tab_id":0,"tag_id":0,"userId":"07633c33b55b16059a2565e6f8137ed8","source":"","stayvoice_id":10416,"jump_id":0},"praise_num_str":"0","played_num_str":"32","comment_num_str":"2","share_num_str":"102","uid":"1112","uname":"小眼睛羚羊","sex":2,"avatar":"http://test.tingshuowan.com/listen/path?url=/users/9b8ee6d48f5aed00d97de0d0016d5078/images/2020/0622410f36e32b9b91a31cd8cbc876ba.jpg","adinfo":{},"type_names":"热点,生活","type_ids":"10002,10003","is_praise":0,"is_attention":0,"is_openad":0,"share_title":"快来和我一起收听【小眼睛羚羊】的音频吧","share_desc":"有感情的朗读《三字经》\u2014\u2014《最美的遇见》，第八集#10月9日声优戴超行直播间","share_url":"http://livetest.tingshuowan.com/h5/stayvoiceshare?stayvoice_id=10416&sign=1234","share_thumbimage":"http://test.tingshuowan.com/listen/path?url=/default/stayvoice/initimg/daichaohang.jpg","share_wxminprogram_type":2,"share_wxminprogram_appid":"wx123456789","share_wxminprogram_path":"/h5/stayvoiceshare?stayvoice_id=10416&sign=1234"}
     * next : {"id":10316,"title":null,"desc":"森林鸟18","image_path":"http://test.tingshuowan.com/listen/path?url=/default/stayvoice/initimg/sunshine.jpg","voice_path":"http://test.tingshuowan.com/listen/path?url=/default/stayvoice/initmp3/Forest18.mp3","voice_time":132049,"voice_size":2113245,"voice_uptype":0,"praise_num":0,"played_num":32,"pv_played_num":155,"comment_num":10,"share_num":101,"title_ai_status":0,"desc_ai_status":0,"image_ai_status":0,"voice_ai_status":0,"status":1,"is_show":1,"is_del":0,"user_id":"f20186fc62a24d0e5dc4d88ac8e9c79c","created_at":"2020-10-29 18:40:48","updated_at":"2020-12-25 10:00:05","show_num":95,"is_top":1,"tmp_type_ids":"","source":0,"is_check":1,"v_share_num":0,"v_praise_num":0,"v_comment_num":0,"v_played_num":0,"is_speech_import":0,"is_ad":0,"_request":{"tab_id":0,"tag_id":0,"userId":"07633c33b55b16059a2565e6f8137ed8","source":"","stayvoice_id":10316,"jump_id":0},"praise_num_str":"0","played_num_str":"32","comment_num_str":"10","share_num_str":"101","uid":"1135","uname":"90006","sex":0,"avatar":"http://test.tingshuowan.com/listen/path?url=/users/avatar/f20186fc62a24d0e5dc4d88ac8e9c79c.jpeg","adinfo":{}}
     */

    private PrevBean prev;
    private CurrentBean current;
    private NextBean next;

    public PrevBean getPrev() {
        return prev;
    }

    public void setPrev(PrevBean prev) {
        this.prev = prev;
    }

    public CurrentBean getCurrent() {
        return current;
    }

    public void setCurrent(CurrentBean current) {
        this.current = current;
    }

    public NextBean getNext() {
        return next;
    }

    public void setNext(NextBean next) {
        this.next = next;
    }

    public static class PrevBean {
        /**
         * id : 10316
         * title : null
         * desc : 森林鸟18
         * image_path : http://test.tingshuowan.com/listen/path?url=/default/stayvoice/initimg/sunshine.jpg
         * voice_path : http://test.tingshuowan.com/listen/path?url=/default/stayvoice/initmp3/Forest18.mp3
         * voice_time : 132049
         * voice_size : 2113245
         * voice_uptype : 0
         * praise_num : 0
         * played_num : 32
         * pv_played_num : 155
         * comment_num : 10
         * share_num : 101
         * title_ai_status : 0
         * desc_ai_status : 0
         * image_ai_status : 0
         * voice_ai_status : 0
         * status : 1
         * is_show : 1
         * is_del : 0
         * user_id : f20186fc62a24d0e5dc4d88ac8e9c79c
         * created_at : 2020-10-29 18:40:48
         * updated_at : 2020-12-25 10:00:05
         * show_num : 95
         * is_top : 1
         * tmp_type_ids :
         * source : 0
         * is_check : 1
         * v_share_num : 0
         * v_praise_num : 0
         * v_comment_num : 0
         * v_played_num : 0
         * is_speech_import : 0
         * is_ad : 0
         * _request : {"tab_id":0,"tag_id":0,"userId":"07633c33b55b16059a2565e6f8137ed8","source":"","stayvoice_id":10316,"jump_id":0}
         * praise_num_str : 0
         * played_num_str : 32
         * comment_num_str : 10
         * share_num_str : 101
         * uid : 1135
         * uname : 90006
         * sex : 0
         * avatar : http://test.tingshuowan.com/listen/path?url=/users/avatar/f20186fc62a24d0e5dc4d88ac8e9c79c.jpeg
         * adinfo : {}
         */

        private int id;
        private Object title;
        private String desc;
        private String image_path;
        private String voice_path;
        private int voice_time;
        private int voice_size;
        private int voice_uptype;
        private int praise_num;
        private int played_num;
        private int pv_played_num;
        private int comment_num;
        private int share_num;
        private int title_ai_status;
        private int desc_ai_status;
        private int image_ai_status;
        private int voice_ai_status;
        private int status;
        private int is_show;
        private int is_del;
        private String user_id;
        private String created_at;
        private String updated_at;
        private int show_num;
        private int is_top;
        private String tmp_type_ids;
        private int source;
        private int is_check;
        private int v_share_num;
        private int v_praise_num;
        private int v_comment_num;
        private int v_played_num;
        private int is_speech_import;
        private int is_ad;
        private NextBean.RequestBeanX _request;
        private String praise_num_str;
        private String played_num_str;
        private String comment_num_str;
        private String share_num_str;
        private String uid;
        private String uname;
        private int sex;
        private String avatar;
        private NextBean.AdinfoBeanX adinfo;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public Object getTitle() {
            return title;
        }

        public void setTitle(Object title) {
            this.title = title;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getImage_path() {
            return image_path;
        }

        public void setImage_path(String image_path) {
            this.image_path = image_path;
        }

        public String getVoice_path() {
            return voice_path;
        }

        public void setVoice_path(String voice_path) {
            this.voice_path = voice_path;
        }

        public int getVoice_time() {
            return voice_time;
        }

        public void setVoice_time(int voice_time) {
            this.voice_time = voice_time;
        }

        public int getVoice_size() {
            return voice_size;
        }

        public void setVoice_size(int voice_size) {
            this.voice_size = voice_size;
        }

        public int getVoice_uptype() {
            return voice_uptype;
        }

        public void setVoice_uptype(int voice_uptype) {
            this.voice_uptype = voice_uptype;
        }

        public int getPraise_num() {
            return praise_num;
        }

        public void setPraise_num(int praise_num) {
            this.praise_num = praise_num;
        }

        public int getPlayed_num() {
            return played_num;
        }

        public void setPlayed_num(int played_num) {
            this.played_num = played_num;
        }

        public int getPv_played_num() {
            return pv_played_num;
        }

        public void setPv_played_num(int pv_played_num) {
            this.pv_played_num = pv_played_num;
        }

        public int getComment_num() {
            return comment_num;
        }

        public void setComment_num(int comment_num) {
            this.comment_num = comment_num;
        }

        public int getShare_num() {
            return share_num;
        }

        public void setShare_num(int share_num) {
            this.share_num = share_num;
        }

        public int getTitle_ai_status() {
            return title_ai_status;
        }

        public void setTitle_ai_status(int title_ai_status) {
            this.title_ai_status = title_ai_status;
        }

        public int getDesc_ai_status() {
            return desc_ai_status;
        }

        public void setDesc_ai_status(int desc_ai_status) {
            this.desc_ai_status = desc_ai_status;
        }

        public int getImage_ai_status() {
            return image_ai_status;
        }

        public void setImage_ai_status(int image_ai_status) {
            this.image_ai_status = image_ai_status;
        }

        public int getVoice_ai_status() {
            return voice_ai_status;
        }

        public void setVoice_ai_status(int voice_ai_status) {
            this.voice_ai_status = voice_ai_status;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getIs_show() {
            return is_show;
        }

        public void setIs_show(int is_show) {
            this.is_show = is_show;
        }

        public int getIs_del() {
            return is_del;
        }

        public void setIs_del(int is_del) {
            this.is_del = is_del;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public int getShow_num() {
            return show_num;
        }

        public void setShow_num(int show_num) {
            this.show_num = show_num;
        }

        public int getIs_top() {
            return is_top;
        }

        public void setIs_top(int is_top) {
            this.is_top = is_top;
        }

        public String getTmp_type_ids() {
            return tmp_type_ids;
        }

        public void setTmp_type_ids(String tmp_type_ids) {
            this.tmp_type_ids = tmp_type_ids;
        }

        public int getSource() {
            return source;
        }

        public void setSource(int source) {
            this.source = source;
        }

        public int getIs_check() {
            return is_check;
        }

        public void setIs_check(int is_check) {
            this.is_check = is_check;
        }

        public int getV_share_num() {
            return v_share_num;
        }

        public void setV_share_num(int v_share_num) {
            this.v_share_num = v_share_num;
        }

        public int getV_praise_num() {
            return v_praise_num;
        }

        public void setV_praise_num(int v_praise_num) {
            this.v_praise_num = v_praise_num;
        }

        public int getV_comment_num() {
            return v_comment_num;
        }

        public void setV_comment_num(int v_comment_num) {
            this.v_comment_num = v_comment_num;
        }

        public int getV_played_num() {
            return v_played_num;
        }

        public void setV_played_num(int v_played_num) {
            this.v_played_num = v_played_num;
        }

        public int getIs_speech_import() {
            return is_speech_import;
        }

        public void setIs_speech_import(int is_speech_import) {
            this.is_speech_import = is_speech_import;
        }

        public int getIs_ad() {
            return is_ad;
        }

        public void setIs_ad(int is_ad) {
            this.is_ad = is_ad;
        }

        public NextBean.RequestBeanX get_request() {
            return _request;
        }

        public void set_request(NextBean.RequestBeanX _request) {
            this._request = _request;
        }

        public String getPraise_num_str() {
            return praise_num_str;
        }

        public void setPraise_num_str(String praise_num_str) {
            this.praise_num_str = praise_num_str;
        }

        public String getPlayed_num_str() {
            return played_num_str;
        }

        public void setPlayed_num_str(String played_num_str) {
            this.played_num_str = played_num_str;
        }

        public String getComment_num_str() {
            return comment_num_str;
        }

        public void setComment_num_str(String comment_num_str) {
            this.comment_num_str = comment_num_str;
        }

        public String getShare_num_str() {
            return share_num_str;
        }

        public void setShare_num_str(String share_num_str) {
            this.share_num_str = share_num_str;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getUname() {
            return uname;
        }

        public void setUname(String uname) {
            this.uname = uname;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public NextBean.AdinfoBeanX getAdinfo() {
            return adinfo;
        }

        public void setAdinfo(NextBean.AdinfoBeanX adinfo) {
            this.adinfo = adinfo;
        }

        public static class RequestBeanX {
            /**
             * tab_id : 0
             * tag_id : 0
             * userId : 07633c33b55b16059a2565e6f8137ed8
             * source :
             * stayvoice_id : 10316
             * jump_id : 0
             */

            private int tab_id;
            private int tag_id;
            private String userId;
            private String source;
            private int stayvoice_id;
            private int jump_id;

            public int getTab_id() {
                return tab_id;
            }

            public void setTab_id(int tab_id) {
                this.tab_id = tab_id;
            }

            public int getTag_id() {
                return tag_id;
            }

            public void setTag_id(int tag_id) {
                this.tag_id = tag_id;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public String getSource() {
                return source;
            }

            public void setSource(String source) {
                this.source = source;
            }

            public int getStayvoice_id() {
                return stayvoice_id;
            }

            public void setStayvoice_id(int stayvoice_id) {
                this.stayvoice_id = stayvoice_id;
            }

            public int getJump_id() {
                return jump_id;
            }

            public void setJump_id(int jump_id) {
                this.jump_id = jump_id;
            }
        }

        public static class AdinfoBeanX {
        }
    }

    public static class CurrentBean {
        /**
         * id : 10416
         * title : null
         * desc : 有感情的朗读《三字经》——《最美的遇见》，第八集#10月9日声优戴超行直播间
         * image_path : http://test.tingshuowan.com/listen/path?url=/default/stayvoice/initimg/daichaohang.jpg
         * voice_path : http://test.tingshuowan.com/listen/path?url=/default/stayvoice/initmp3/S24.mp3
         * voice_time : 179192
         * voice_size : 2875694
         * voice_uptype : 0
         * praise_num : 0
         * played_num : 32
         * pv_played_num : 193
         * comment_num : 2
         * share_num : 102
         * title_ai_status : 0
         * desc_ai_status : 0
         * image_ai_status : 0
         * voice_ai_status : 0
         * status : 1
         * is_show : 1
         * is_del : 0
         * user_id : 9b8ee6d48f5aed00d97de0d0016d5078
         * created_at : 2020-10-29 21:08:57
         * updated_at : 2020-12-25 10:17:34
         * show_num : 93
         * is_top : 1
         * tmp_type_ids :
         * source : 0
         * is_check : 1
         * v_share_num : 0
         * v_praise_num : 0
         * v_comment_num : 0
         * v_played_num : 0
         * is_speech_import : 0
         * is_ad : 0
         * _request : {"tab_id":0,"tag_id":0,"userId":"07633c33b55b16059a2565e6f8137ed8","source":"","stayvoice_id":10416,"jump_id":0}
         * praise_num_str : 0
         * played_num_str : 32
         * comment_num_str : 2
         * share_num_str : 102
         * uid : 1112
         * uname : 小眼睛羚羊
         * sex : 2
         * avatar : http://test.tingshuowan.com/listen/path?url=/users/9b8ee6d48f5aed00d97de0d0016d5078/images/2020/0622410f36e32b9b91a31cd8cbc876ba.jpg
         * adinfo : {}
         * type_names : 热点,生活
         * type_ids : 10002,10003
         * is_praise : 0
         * is_attention : 0
         * is_openad : 0
         * share_title : 快来和我一起收听【小眼睛羚羊】的音频吧
         * share_desc : 有感情的朗读《三字经》——《最美的遇见》，第八集#10月9日声优戴超行直播间
         * share_url : http://livetest.tingshuowan.com/h5/stayvoiceshare?stayvoice_id=10416&sign=1234
         * share_thumbimage : http://test.tingshuowan.com/listen/path?url=/default/stayvoice/initimg/daichaohang.jpg
         * share_wxminprogram_type : 2
         * share_wxminprogram_appid : wx123456789
         * share_wxminprogram_path : /h5/stayvoiceshare?stayvoice_id=10416&sign=1234
         */

        private int id;
        private Object title;
        private String desc;
        private String image_path;
        private String voice_path;
        private int voice_time;
        private int voice_size;
        private int voice_uptype;
        private int praise_num;
        private int played_num;
        private int pv_played_num;
        private int comment_num;
        private int share_num;
        private int title_ai_status;
        private int desc_ai_status;
        private int image_ai_status;
        private int voice_ai_status;
        private int status;
        private int is_show;
        private int is_del;
        private String user_id;
        private String created_at;
        private String updated_at;
        private int show_num;
        private int is_top;
        private String tmp_type_ids;
        private int source;
        private int is_check;
        private int v_share_num;
        private int v_praise_num;
        private int v_comment_num;
        private int v_played_num;
        private int is_speech_import;
        private int is_ad;
        private RequestBean _request;
        private String praise_num_str;
        private String played_num_str;
        private String comment_num_str;
        private String share_num_str;
        private String uid;
        private String uname;
        private int sex;
        private String avatar;
        private AdinfoBean adinfo;
        private String type_names;
        private String type_ids;
        private int is_praise;
        private int is_attention;
        private int is_openad;
        private String share_title;
        private String share_desc;
        private String share_url;
        private String share_thumbimage;
        private int share_wxminprogram_type;
        private String share_wxminprogram_appid;
        private String share_wxminprogram_path;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public Object getTitle() {
            return title;
        }

        public void setTitle(Object title) {
            this.title = title;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getImage_path() {
            return image_path;
        }

        public void setImage_path(String image_path) {
            this.image_path = image_path;
        }

        public String getVoice_path() {
            return voice_path;
        }

        public void setVoice_path(String voice_path) {
            this.voice_path = voice_path;
        }

        public int getVoice_time() {
            return voice_time;
        }

        public void setVoice_time(int voice_time) {
            this.voice_time = voice_time;
        }

        public int getVoice_size() {
            return voice_size;
        }

        public void setVoice_size(int voice_size) {
            this.voice_size = voice_size;
        }

        public int getVoice_uptype() {
            return voice_uptype;
        }

        public void setVoice_uptype(int voice_uptype) {
            this.voice_uptype = voice_uptype;
        }

        public int getPraise_num() {
            return praise_num;
        }

        public void setPraise_num(int praise_num) {
            this.praise_num = praise_num;
        }

        public int getPlayed_num() {
            return played_num;
        }

        public void setPlayed_num(int played_num) {
            this.played_num = played_num;
        }

        public int getPv_played_num() {
            return pv_played_num;
        }

        public void setPv_played_num(int pv_played_num) {
            this.pv_played_num = pv_played_num;
        }

        public int getComment_num() {
            return comment_num;
        }

        public void setComment_num(int comment_num) {
            this.comment_num = comment_num;
        }

        public int getShare_num() {
            return share_num;
        }

        public void setShare_num(int share_num) {
            this.share_num = share_num;
        }

        public int getTitle_ai_status() {
            return title_ai_status;
        }

        public void setTitle_ai_status(int title_ai_status) {
            this.title_ai_status = title_ai_status;
        }

        public int getDesc_ai_status() {
            return desc_ai_status;
        }

        public void setDesc_ai_status(int desc_ai_status) {
            this.desc_ai_status = desc_ai_status;
        }

        public int getImage_ai_status() {
            return image_ai_status;
        }

        public void setImage_ai_status(int image_ai_status) {
            this.image_ai_status = image_ai_status;
        }

        public int getVoice_ai_status() {
            return voice_ai_status;
        }

        public void setVoice_ai_status(int voice_ai_status) {
            this.voice_ai_status = voice_ai_status;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getIs_show() {
            return is_show;
        }

        public void setIs_show(int is_show) {
            this.is_show = is_show;
        }

        public int getIs_del() {
            return is_del;
        }

        public void setIs_del(int is_del) {
            this.is_del = is_del;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public int getShow_num() {
            return show_num;
        }

        public void setShow_num(int show_num) {
            this.show_num = show_num;
        }

        public int getIs_top() {
            return is_top;
        }

        public void setIs_top(int is_top) {
            this.is_top = is_top;
        }

        public String getTmp_type_ids() {
            return tmp_type_ids;
        }

        public void setTmp_type_ids(String tmp_type_ids) {
            this.tmp_type_ids = tmp_type_ids;
        }

        public int getSource() {
            return source;
        }

        public void setSource(int source) {
            this.source = source;
        }

        public int getIs_check() {
            return is_check;
        }

        public void setIs_check(int is_check) {
            this.is_check = is_check;
        }

        public int getV_share_num() {
            return v_share_num;
        }

        public void setV_share_num(int v_share_num) {
            this.v_share_num = v_share_num;
        }

        public int getV_praise_num() {
            return v_praise_num;
        }

        public void setV_praise_num(int v_praise_num) {
            this.v_praise_num = v_praise_num;
        }

        public int getV_comment_num() {
            return v_comment_num;
        }

        public void setV_comment_num(int v_comment_num) {
            this.v_comment_num = v_comment_num;
        }

        public int getV_played_num() {
            return v_played_num;
        }

        public void setV_played_num(int v_played_num) {
            this.v_played_num = v_played_num;
        }

        public int getIs_speech_import() {
            return is_speech_import;
        }

        public void setIs_speech_import(int is_speech_import) {
            this.is_speech_import = is_speech_import;
        }

        public int getIs_ad() {
            return is_ad;
        }

        public void setIs_ad(int is_ad) {
            this.is_ad = is_ad;
        }

        public RequestBean get_request() {
            return _request;
        }

        public void set_request(RequestBean _request) {
            this._request = _request;
        }

        public String getPraise_num_str() {
            return praise_num_str;
        }

        public void setPraise_num_str(String praise_num_str) {
            this.praise_num_str = praise_num_str;
        }

        public String getPlayed_num_str() {
            return played_num_str;
        }

        public void setPlayed_num_str(String played_num_str) {
            this.played_num_str = played_num_str;
        }

        public String getComment_num_str() {
            return comment_num_str;
        }

        public void setComment_num_str(String comment_num_str) {
            this.comment_num_str = comment_num_str;
        }

        public String getShare_num_str() {
            return share_num_str;
        }

        public void setShare_num_str(String share_num_str) {
            this.share_num_str = share_num_str;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getUname() {
            return uname;
        }

        public void setUname(String uname) {
            this.uname = uname;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public AdinfoBean getAdinfo() {
            return adinfo;
        }

        public void setAdinfo(AdinfoBean adinfo) {
            this.adinfo = adinfo;
        }

        public String getType_names() {
            return type_names;
        }

        public void setType_names(String type_names) {
            this.type_names = type_names;
        }

        public String getType_ids() {
            return type_ids;
        }

        public void setType_ids(String type_ids) {
            this.type_ids = type_ids;
        }

        public int getIs_praise() {
            return is_praise;
        }

        public void setIs_praise(int is_praise) {
            this.is_praise = is_praise;
        }

        public int getIs_attention() {
            return is_attention;
        }

        public void setIs_attention(int is_attention) {
            this.is_attention = is_attention;
        }

        public int getIs_openad() {
            return is_openad;
        }

        public void setIs_openad(int is_openad) {
            this.is_openad = is_openad;
        }

        public String getShare_title() {
            return share_title;
        }

        public void setShare_title(String share_title) {
            this.share_title = share_title;
        }

        public String getShare_desc() {
            return share_desc;
        }

        public void setShare_desc(String share_desc) {
            this.share_desc = share_desc;
        }

        public String getShare_url() {
            return share_url;
        }

        public void setShare_url(String share_url) {
            this.share_url = share_url;
        }

        public String getShare_thumbimage() {
            return share_thumbimage;
        }

        public void setShare_thumbimage(String share_thumbimage) {
            this.share_thumbimage = share_thumbimage;
        }

        public int getShare_wxminprogram_type() {
            return share_wxminprogram_type;
        }

        public void setShare_wxminprogram_type(int share_wxminprogram_type) {
            this.share_wxminprogram_type = share_wxminprogram_type;
        }

        public String getShare_wxminprogram_appid() {
            return share_wxminprogram_appid;
        }

        public void setShare_wxminprogram_appid(String share_wxminprogram_appid) {
            this.share_wxminprogram_appid = share_wxminprogram_appid;
        }

        public String getShare_wxminprogram_path() {
            return share_wxminprogram_path;
        }

        public void setShare_wxminprogram_path(String share_wxminprogram_path) {
            this.share_wxminprogram_path = share_wxminprogram_path;
        }

        public static class RequestBean {
            /**
             * tab_id : 0
             * tag_id : 0
             * userId : 07633c33b55b16059a2565e6f8137ed8
             * source :
             * stayvoice_id : 10416
             * jump_id : 0
             */

            private int tab_id;
            private int tag_id;
            private String userId;
            private String source;
            private int stayvoice_id;
            private int jump_id;

            public int getTab_id() {
                return tab_id;
            }

            public void setTab_id(int tab_id) {
                this.tab_id = tab_id;
            }

            public int getTag_id() {
                return tag_id;
            }

            public void setTag_id(int tag_id) {
                this.tag_id = tag_id;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public String getSource() {
                return source;
            }

            public void setSource(String source) {
                this.source = source;
            }

            public int getStayvoice_id() {
                return stayvoice_id;
            }

            public void setStayvoice_id(int stayvoice_id) {
                this.stayvoice_id = stayvoice_id;
            }

            public int getJump_id() {
                return jump_id;
            }

            public void setJump_id(int jump_id) {
                this.jump_id = jump_id;
            }
        }

        public static class AdinfoBean {
        }
    }

    public static class NextBean {
        /**
         * id : 10316
         * title : null
         * desc : 森林鸟18
         * image_path : http://test.tingshuowan.com/listen/path?url=/default/stayvoice/initimg/sunshine.jpg
         * voice_path : http://test.tingshuowan.com/listen/path?url=/default/stayvoice/initmp3/Forest18.mp3
         * voice_time : 132049
         * voice_size : 2113245
         * voice_uptype : 0
         * praise_num : 0
         * played_num : 32
         * pv_played_num : 155
         * comment_num : 10
         * share_num : 101
         * title_ai_status : 0
         * desc_ai_status : 0
         * image_ai_status : 0
         * voice_ai_status : 0
         * status : 1
         * is_show : 1
         * is_del : 0
         * user_id : f20186fc62a24d0e5dc4d88ac8e9c79c
         * created_at : 2020-10-29 18:40:48
         * updated_at : 2020-12-25 10:00:05
         * show_num : 95
         * is_top : 1
         * tmp_type_ids :
         * source : 0
         * is_check : 1
         * v_share_num : 0
         * v_praise_num : 0
         * v_comment_num : 0
         * v_played_num : 0
         * is_speech_import : 0
         * is_ad : 0
         * _request : {"tab_id":0,"tag_id":0,"userId":"07633c33b55b16059a2565e6f8137ed8","source":"","stayvoice_id":10316,"jump_id":0}
         * praise_num_str : 0
         * played_num_str : 32
         * comment_num_str : 10
         * share_num_str : 101
         * uid : 1135
         * uname : 90006
         * sex : 0
         * avatar : http://test.tingshuowan.com/listen/path?url=/users/avatar/f20186fc62a24d0e5dc4d88ac8e9c79c.jpeg
         * adinfo : {}
         */

        private int id;
        private Object title;
        private String desc;
        private String image_path;
        private String voice_path;
        private int voice_time;
        private int voice_size;
        private int voice_uptype;
        private int praise_num;
        private int played_num;
        private int pv_played_num;
        private int comment_num;
        private int share_num;
        private int title_ai_status;
        private int desc_ai_status;
        private int image_ai_status;
        private int voice_ai_status;
        private int status;
        private int is_show;
        private int is_del;
        private String user_id;
        private String created_at;
        private String updated_at;
        private int show_num;
        private int is_top;
        private String tmp_type_ids;
        private int source;
        private int is_check;
        private int v_share_num;
        private int v_praise_num;
        private int v_comment_num;
        private int v_played_num;
        private int is_speech_import;
        private int is_ad;
        private RequestBeanX _request;
        private String praise_num_str;
        private String played_num_str;
        private String comment_num_str;
        private String share_num_str;
        private String uid;
        private String uname;
        private int sex;
        private String avatar;
        private AdinfoBeanX adinfo;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public Object getTitle() {
            return title;
        }

        public void setTitle(Object title) {
            this.title = title;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getImage_path() {
            return image_path;
        }

        public void setImage_path(String image_path) {
            this.image_path = image_path;
        }

        public String getVoice_path() {
            return voice_path;
        }

        public void setVoice_path(String voice_path) {
            this.voice_path = voice_path;
        }

        public int getVoice_time() {
            return voice_time;
        }

        public void setVoice_time(int voice_time) {
            this.voice_time = voice_time;
        }

        public int getVoice_size() {
            return voice_size;
        }

        public void setVoice_size(int voice_size) {
            this.voice_size = voice_size;
        }

        public int getVoice_uptype() {
            return voice_uptype;
        }

        public void setVoice_uptype(int voice_uptype) {
            this.voice_uptype = voice_uptype;
        }

        public int getPraise_num() {
            return praise_num;
        }

        public void setPraise_num(int praise_num) {
            this.praise_num = praise_num;
        }

        public int getPlayed_num() {
            return played_num;
        }

        public void setPlayed_num(int played_num) {
            this.played_num = played_num;
        }

        public int getPv_played_num() {
            return pv_played_num;
        }

        public void setPv_played_num(int pv_played_num) {
            this.pv_played_num = pv_played_num;
        }

        public int getComment_num() {
            return comment_num;
        }

        public void setComment_num(int comment_num) {
            this.comment_num = comment_num;
        }

        public int getShare_num() {
            return share_num;
        }

        public void setShare_num(int share_num) {
            this.share_num = share_num;
        }

        public int getTitle_ai_status() {
            return title_ai_status;
        }

        public void setTitle_ai_status(int title_ai_status) {
            this.title_ai_status = title_ai_status;
        }

        public int getDesc_ai_status() {
            return desc_ai_status;
        }

        public void setDesc_ai_status(int desc_ai_status) {
            this.desc_ai_status = desc_ai_status;
        }

        public int getImage_ai_status() {
            return image_ai_status;
        }

        public void setImage_ai_status(int image_ai_status) {
            this.image_ai_status = image_ai_status;
        }

        public int getVoice_ai_status() {
            return voice_ai_status;
        }

        public void setVoice_ai_status(int voice_ai_status) {
            this.voice_ai_status = voice_ai_status;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getIs_show() {
            return is_show;
        }

        public void setIs_show(int is_show) {
            this.is_show = is_show;
        }

        public int getIs_del() {
            return is_del;
        }

        public void setIs_del(int is_del) {
            this.is_del = is_del;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public int getShow_num() {
            return show_num;
        }

        public void setShow_num(int show_num) {
            this.show_num = show_num;
        }

        public int getIs_top() {
            return is_top;
        }

        public void setIs_top(int is_top) {
            this.is_top = is_top;
        }

        public String getTmp_type_ids() {
            return tmp_type_ids;
        }

        public void setTmp_type_ids(String tmp_type_ids) {
            this.tmp_type_ids = tmp_type_ids;
        }

        public int getSource() {
            return source;
        }

        public void setSource(int source) {
            this.source = source;
        }

        public int getIs_check() {
            return is_check;
        }

        public void setIs_check(int is_check) {
            this.is_check = is_check;
        }

        public int getV_share_num() {
            return v_share_num;
        }

        public void setV_share_num(int v_share_num) {
            this.v_share_num = v_share_num;
        }

        public int getV_praise_num() {
            return v_praise_num;
        }

        public void setV_praise_num(int v_praise_num) {
            this.v_praise_num = v_praise_num;
        }

        public int getV_comment_num() {
            return v_comment_num;
        }

        public void setV_comment_num(int v_comment_num) {
            this.v_comment_num = v_comment_num;
        }

        public int getV_played_num() {
            return v_played_num;
        }

        public void setV_played_num(int v_played_num) {
            this.v_played_num = v_played_num;
        }

        public int getIs_speech_import() {
            return is_speech_import;
        }

        public void setIs_speech_import(int is_speech_import) {
            this.is_speech_import = is_speech_import;
        }

        public int getIs_ad() {
            return is_ad;
        }

        public void setIs_ad(int is_ad) {
            this.is_ad = is_ad;
        }

        public RequestBeanX get_request() {
            return _request;
        }

        public void set_request(RequestBeanX _request) {
            this._request = _request;
        }

        public String getPraise_num_str() {
            return praise_num_str;
        }

        public void setPraise_num_str(String praise_num_str) {
            this.praise_num_str = praise_num_str;
        }

        public String getPlayed_num_str() {
            return played_num_str;
        }

        public void setPlayed_num_str(String played_num_str) {
            this.played_num_str = played_num_str;
        }

        public String getComment_num_str() {
            return comment_num_str;
        }

        public void setComment_num_str(String comment_num_str) {
            this.comment_num_str = comment_num_str;
        }

        public String getShare_num_str() {
            return share_num_str;
        }

        public void setShare_num_str(String share_num_str) {
            this.share_num_str = share_num_str;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getUname() {
            return uname;
        }

        public void setUname(String uname) {
            this.uname = uname;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public AdinfoBeanX getAdinfo() {
            return adinfo;
        }

        public void setAdinfo(AdinfoBeanX adinfo) {
            this.adinfo = adinfo;
        }

        public static class RequestBeanX {
            /**
             * tab_id : 0
             * tag_id : 0
             * userId : 07633c33b55b16059a2565e6f8137ed8
             * source :
             * stayvoice_id : 10316
             * jump_id : 0
             */

            private int tab_id;
            private int tag_id;
            private String userId;
            private String source;
            private int stayvoice_id;
            private int jump_id;

            public int getTab_id() {
                return tab_id;
            }

            public void setTab_id(int tab_id) {
                this.tab_id = tab_id;
            }

            public int getTag_id() {
                return tag_id;
            }

            public void setTag_id(int tag_id) {
                this.tag_id = tag_id;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public String getSource() {
                return source;
            }

            public void setSource(String source) {
                this.source = source;
            }

            public int getStayvoice_id() {
                return stayvoice_id;
            }

            public void setStayvoice_id(int stayvoice_id) {
                this.stayvoice_id = stayvoice_id;
            }

            public int getJump_id() {
                return jump_id;
            }

            public void setJump_id(int jump_id) {
                this.jump_id = jump_id;
            }
        }

        public static class AdinfoBeanX {
        }
    }
}
