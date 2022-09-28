package com.maishuo.tingshuohenhaowan.api.response;

import android.text.TextUtils;

import com.corpize.sdk.ivoice.admanager.QcAdManager;
import com.maishuo.tingshuohenhaowan.common.Constant;

import java.io.Serializable;
import java.util.List;

/**
 * Create by yHai on 2021/2/2
 * EXPLAIN:
 */
public class PhonicListBean implements Serializable {


    /**
     * list : [{"id":11825,"title":null,"desc":"舒肤佳，12小时长久呵护","image_path":"/users/01b063cc545685b18ad6c1af3c14c006/stayvoice/images/2020/65edf5b2af0d5a8a4fc919d8e5631ab1.jpg","voice_path":"/users/01b063cc545685b18ad6c1af3c14c006/stayvoice/voice/2020/65edf5b2af0d5a8a4fc919d8e5631ab1.mp3","voice_time":15125,"voice_size":369501,"voice_uptype":1,"praise_num":4,"played_num":27,"pv_played_num":303,"comment_num":1,"share_num":1,"title_ai_status":0,"desc_ai_status":1,"image_ai_status":1,"voice_ai_status":1,"status":1,"is_show":1,"is_del":0,"user_id":"01b063cc545685b18ad6c1af3c14c006","created_at":"2020-11-27 16:33:12","updated_at":"2021-02-01 10:32:47","show_num":95,"is_top":0,"tmp_type_ids":null,"source":1,"is_check":0,"v_share_num":0,"v_praise_num":0,"v_comment_num":0,"v_played_num":0,"is_speech_import":0,"is_ad":1,"voice_volume":50,"bg_music":1,"bg_music_volume":50,"bg_img":null,"top_index":0,"share_num_str":"1","praise_num_str":"4","comment_num_str":"1","played_num_str":"27","uid":"1107","uname":"舒肤佳","sex":1,"avatar":"http://test.tingshuowan.com/listen/path?url=/users/01b063cc545685b18ad6c1af3c14c006/images/2020/0161645609c67cb8fc64c0f3df69f71b.jpg","type_names":"生活","type_ids":"10003","is_praise":0,"is_attention":0,"is_openad":0,"share_title":"快来和我一起收听【舒肤佳】的音频吧","share_desc":"舒肤佳，12小时长久呵护","share_url":"http://livetest.tingshuowan.com/h5/stayvoiceshare?stayvoice_id=11825&sign=1234","share_thumbimage":"/users/01b063cc545685b18ad6c1af3c14c006/stayvoice/images/2020/65edf5b2af0d5a8a4fc919d8e5631ab1.jpg","share_wxminprogram_type":2,"share_wxminprogram_appid":"wx123456789","share_wxminprogram_path":"/h5/stayvoiceshare?stayvoice_id=11825&sign=1234","adinfo":{"id":1,"stayvoice_id":11825,"logo":"http://resource.corpize.com/image/shufujia_202.jpeg","title":"舒肤佳，12小时长久呵护","desc":"肌肤健康不仅保养,还要保护.使用舒肤佳沐浴露持续保护全家肌肤健康.舒肤佳经典净护系列沐浴露含健康配方,温和去除皮肤上的有害物质,有助肌肤保持健康","is_openlayer":0,"android_url":"","ios_url":"","phone_shakeme":"http://api.tingshuowan.com/listen/path?url=/adVoice/phone_shakeme_start.mp3 ","deeplink_shakeme":"http://api.tingshuowan.com/listen/path?url=/adVoice/deeplink_shakeme_start.mp3","download_shakeme":"http://api.tingshuowan.com/listen/path?url=/adVoice/shakeme_start.mp3","openldp":"http://api.tingshuowan.com/listen/path?url=/adVoice/openldp_shakeme_start.mp3","ldp":"https://www.safeguard.com.cn/zh-cn","deeplink":null,"action":4,"wait":5,"interval":5,"volume":30,"created_at":"2020-11-26 17:06:16","updated_at":"2020-11-27 10:06:26","is_shakeme":0,"phone_number":"021-61720502"}},{"id":10034,"title":null,"desc":"阿尔法波音乐1","image_path":"/default/stayvoice/initimg/music1.jpg","voice_path":"/default/stayvoice/initmp3/Alphawave1.mp3","voice_time":150047,"voice_size":2401219,"voice_uptype":0,"praise_num":0,"played_num":1,"pv_played_num":1,"comment_num":0,"share_num":100,"title_ai_status":0,"desc_ai_status":0,"image_ai_status":0,"voice_ai_status":0,"status":1,"is_show":1,"is_del":0,"user_id":"f938148126bb7e8d2c670f4bfd3b6d7a","created_at":"2020-10-29 16:44:25","updated_at":"2020-11-27 11:41:45","show_num":99,"is_top":0,"tmp_type_ids":"","source":0,"is_check":0,"v_share_num":0,"v_praise_num":0,"v_comment_num":0,"v_played_num":0,"is_speech_import":0,"is_ad":0,"voice_volume":50,"bg_music":1,"bg_music_volume":50,"bg_img":null,"top_index":0,"share_num_str":"100","praise_num_str":"0","comment_num_str":"0","played_num_str":"1","uid":"1127","uname":"11122","sex":0,"avatar":"http://test.tingshuowan.com/listen/path?url=/users/avatar/f938148126bb7e8d2c670f4bfd3b6d7a.jpeg","type_names":"万象,音乐","type_ids":"10006,10007","is_praise":0,"is_attention":0,"is_openad":0,"share_title":"快来和我一起收听【11122】的音频吧","share_desc":"阿尔法波音乐1","share_url":"http://livetest.tingshuowan.com/h5/stayvoiceshare?stayvoice_id=10034&sign=1234","share_thumbimage":"/default/stayvoice/initimg/music1.jpg","share_wxminprogram_type":2,"share_wxminprogram_appid":"wx123456789","share_wxminprogram_path":"/h5/stayvoiceshare?stayvoice_id=10034&sign=1234","adinfo":[]},{"id":10035,"title":null,"desc":"阿尔法波音乐2","image_path":"/default/stayvoice/initimg/music1.jpg","voice_path":"/default/stayvoice/initmp3/Alphawave2.mp3","voice_time":150047,"voice_size":2401219,"voice_uptype":0,"praise_num":0,"played_num":0,"pv_played_num":0,"comment_num":0,"share_num":100,"title_ai_status":0,"desc_ai_status":0,"image_ai_status":0,"voice_ai_status":0,"status":1,"is_show":1,"is_del":0,"user_id":"f938148126bb7e8d2c670f4bfd3b6d7a","created_at":"2020-10-29 16:47:25","updated_at":null,"show_num":100,"is_top":0,"tmp_type_ids":"","source":0,"is_check":0,"v_share_num":0,"v_praise_num":0,"v_comment_num":0,"v_played_num":0,"is_speech_import":0,"is_ad":0,"voice_volume":50,"bg_music":1,"bg_music_volume":50,"bg_img":null,"top_index":0,"share_num_str":"100","praise_num_str":"0","comment_num_str":"0","played_num_str":"0","uid":"1127","uname":"11122","sex":0,"avatar":"http://test.tingshuowan.com/listen/path?url=/users/avatar/f938148126bb7e8d2c670f4bfd3b6d7a.jpeg","type_names":"万象,音乐","type_ids":"10006,10007","is_praise":0,"is_attention":0,"is_openad":0,"share_title":"快来和我一起收听【11122】的音频吧","share_desc":"阿尔法波音乐2","share_url":"http://livetest.tingshuowan.com/h5/stayvoiceshare?stayvoice_id=10035&sign=1234","share_thumbimage":"/default/stayvoice/initimg/music1.jpg","share_wxminprogram_type":2,"share_wxminprogram_appid":"wx123456789","share_wxminprogram_path":"/h5/stayvoiceshare?stayvoice_id=10035&sign=1234","adinfo":[]},{"id":10036,"title":null,"desc":"阿尔法波音乐3","image_path":"/default/stayvoice/initimg/music1.jpg","voice_path":"/default/stayvoice/initmp3/Alphawave3.mp3","voice_time":151040,"voice_size":2417102,"voice_uptype":0,"praise_num":0,"played_num":0,"pv_played_num":0,"comment_num":0,"share_num":100,"title_ai_status":0,"desc_ai_status":0,"image_ai_status":0,"voice_ai_status":0,"status":1,"is_show":1,"is_del":0,"user_id":"f938148126bb7e8d2c670f4bfd3b6d7a","created_at":"2020-10-29 16:50:43","updated_at":null,"show_num":100,"is_top":0,"tmp_type_ids":"","source":0,"is_check":0,"v_share_num":0,"v_praise_num":0,"v_comment_num":0,"v_played_num":0,"is_speech_import":0,"is_ad":0,"voice_volume":50,"bg_music":1,"bg_music_volume":50,"bg_img":null,"top_index":0,"share_num_str":"100","praise_num_str":"0","comment_num_str":"0","played_num_str":"0","uid":"1127","uname":"11122","sex":0,"avatar":"http://test.tingshuowan.com/listen/path?url=/users/avatar/f938148126bb7e8d2c670f4bfd3b6d7a.jpeg","type_names":"万象,音乐","type_ids":"10006,10007","is_praise":0,"is_attention":0,"is_openad":0,"share_title":"快来和我一起收听【11122】的音频吧","share_desc":"阿尔法波音乐3","share_url":"http://livetest.tingshuowan.com/h5/stayvoiceshare?stayvoice_id=10036&sign=1234","share_thumbimage":"/default/stayvoice/initimg/music1.jpg","share_wxminprogram_type":2,"share_wxminprogram_appid":"wx123456789","share_wxminprogram_path":"/h5/stayvoiceshare?stayvoice_id=10036&sign=1234","adinfo":[]},{"id":10037,"title":null,"desc":"阿尔法波音乐4","image_path":"/default/stayvoice/initimg/music1.jpg","voice_path":"/default/stayvoice/initmp3/Alphawave4.mp3","voice_time":149029,"voice_size":2384919,"voice_uptype":0,"praise_num":0,"played_num":0,"pv_played_num":0,"comment_num":0,"share_num":100,"title_ai_status":0,"desc_ai_status":0,"image_ai_status":0,"voice_ai_status":0,"status":1,"is_show":1,"is_del":0,"user_id":"f938148126bb7e8d2c670f4bfd3b6d7a","created_at":"2020-10-29 16:54:11","updated_at":null,"show_num":100,"is_top":0,"tmp_type_ids":"","source":0,"is_check":0,"v_share_num":0,"v_praise_num":0,"v_comment_num":0,"v_played_num":0,"is_speech_import":0,"is_ad":0,"voice_volume":50,"bg_music":1,"bg_music_volume":50,"bg_img":null,"top_index":0,"share_num_str":"100","praise_num_str":"0","comment_num_str":"0","played_num_str":"0","uid":"1127","uname":"11122","sex":0,"avatar":"http://test.tingshuowan.com/listen/path?url=/users/avatar/f938148126bb7e8d2c670f4bfd3b6d7a.jpeg","type_names":"万象,音乐","type_ids":"10006,10007","is_praise":0,"is_attention":0,"is_openad":0,"share_title":"快来和我一起收听【11122】的音频吧","share_desc":"阿尔法波音乐4","share_url":"http://livetest.tingshuowan.com/h5/stayvoiceshare?stayvoice_id=10037&sign=1234","share_thumbimage":"/default/stayvoice/initimg/music1.jpg","share_wxminprogram_type":2,"share_wxminprogram_appid":"wx123456789","share_wxminprogram_path":"/h5/stayvoiceshare?stayvoice_id=10037&sign=1234","adinfo":[]},{"id":10038,"title":null,"desc":"阿尔法波音乐5","image_path":"/default/stayvoice/initimg/music1.jpg","voice_path":"/default/stayvoice/initmp3/Alphawave5.mp3","voice_time":148036,"voice_size":2369036,"voice_uptype":0,"praise_num":0,"played_num":0,"pv_played_num":0,"comment_num":0,"share_num":100,"title_ai_status":0,"desc_ai_status":0,"image_ai_status":0,"voice_ai_status":0,"status":1,"is_show":1,"is_del":0,"user_id":"f938148126bb7e8d2c670f4bfd3b6d7a","created_at":"2020-10-29 16:55:21","updated_at":null,"show_num":100,"is_top":0,"tmp_type_ids":"","source":0,"is_check":0,"v_share_num":0,"v_praise_num":0,"v_comment_num":0,"v_played_num":0,"is_speech_import":0,"is_ad":0,"voice_volume":50,"bg_music":1,"bg_music_volume":50,"bg_img":null,"top_index":0,"share_num_str":"100","praise_num_str":"0","comment_num_str":"0","played_num_str":"0","uid":"1127","uname":"11122","sex":0,"avatar":"http://test.tingshuowan.com/listen/path?url=/users/avatar/f938148126bb7e8d2c670f4bfd3b6d7a.jpeg","type_names":"万象,音乐","type_ids":"10006,10007","is_praise":0,"is_attention":0,"is_openad":0,"share_title":"快来和我一起收听【11122】的音频吧","share_desc":"阿尔法波音乐5","share_url":"http://livetest.tingshuowan.com/h5/stayvoiceshare?stayvoice_id=10038&sign=1234","share_thumbimage":"/default/stayvoice/initimg/music1.jpg","share_wxminprogram_type":2,"share_wxminprogram_appid":"wx123456789","share_wxminprogram_path":"/h5/stayvoiceshare?stayvoice_id=10038&sign=1234","adinfo":[]},{"id":10039,"title":null,"desc":"阿尔法波音乐6","image_path":"/default/stayvoice/initimg/music1.jpg","voice_path":"/default/stayvoice/initmp3/Alphawave6.mp3","voice_time":152033,"voice_size":2432984,"voice_uptype":0,"praise_num":0,"played_num":0,"pv_played_num":0,"comment_num":0,"share_num":100,"title_ai_status":0,"desc_ai_status":0,"image_ai_status":0,"voice_ai_status":0,"status":1,"is_show":1,"is_del":0,"user_id":"f938148126bb7e8d2c670f4bfd3b6d7a","created_at":"2020-10-29 16:59:42","updated_at":null,"show_num":100,"is_top":0,"tmp_type_ids":"","source":0,"is_check":0,"v_share_num":0,"v_praise_num":0,"v_comment_num":0,"v_played_num":0,"is_speech_import":0,"is_ad":0,"voice_volume":50,"bg_music":1,"bg_music_volume":50,"bg_img":null,"top_index":0,"share_num_str":"100","praise_num_str":"0","comment_num_str":"0","played_num_str":"0","uid":"1127","uname":"11122","sex":0,"avatar":"http://test.tingshuowan.com/listen/path?url=/users/avatar/f938148126bb7e8d2c670f4bfd3b6d7a.jpeg","type_names":"万象,音乐","type_ids":"10006,10007","is_praise":0,"is_attention":0,"is_openad":0,"share_title":"快来和我一起收听【11122】的音频吧","share_desc":"阿尔法波音乐6","share_url":"http://livetest.tingshuowan.com/h5/stayvoiceshare?stayvoice_id=10039&sign=1234","share_thumbimage":"/default/stayvoice/initimg/music1.jpg","share_wxminprogram_type":2,"share_wxminprogram_appid":"wx123456789","share_wxminprogram_path":"/h5/stayvoiceshare?stayvoice_id=10039&sign=1234","adinfo":[]},{"id":10040,"title":null,"desc":"阿尔法波音乐7","image_path":"/default/stayvoice/initimg/music1.jpg","voice_path":"/default/stayvoice/initmp3/Alphawave7.mp3","voice_time":150047,"voice_size":2401219,"voice_uptype":0,"praise_num":0,"played_num":0,"pv_played_num":0,"comment_num":0,"share_num":100,"title_ai_status":0,"desc_ai_status":0,"image_ai_status":0,"voice_ai_status":0,"status":1,"is_show":1,"is_del":0,"user_id":"f938148126bb7e8d2c670f4bfd3b6d7a","created_at":"2020-10-29 17:03:19","updated_at":null,"show_num":100,"is_top":0,"tmp_type_ids":"","source":0,"is_check":0,"v_share_num":0,"v_praise_num":0,"v_comment_num":0,"v_played_num":0,"is_speech_import":0,"is_ad":0,"voice_volume":50,"bg_music":1,"bg_music_volume":50,"bg_img":null,"top_index":0,"share_num_str":"100","praise_num_str":"0","comment_num_str":"0","played_num_str":"0","uid":"1127","uname":"11122","sex":0,"avatar":"http://test.tingshuowan.com/listen/path?url=/users/avatar/f938148126bb7e8d2c670f4bfd3b6d7a.jpeg","type_names":"万象,音乐","type_ids":"10006,10007","is_praise":0,"is_attention":0,"is_openad":0,"share_title":"快来和我一起收听【11122】的音频吧","share_desc":"阿尔法波音乐7","share_url":"http://livetest.tingshuowan.com/h5/stayvoiceshare?stayvoice_id=10040&sign=1234","share_thumbimage":"/default/stayvoice/initimg/music1.jpg","share_wxminprogram_type":2,"share_wxminprogram_appid":"wx123456789","share_wxminprogram_path":"/h5/stayvoiceshare?stayvoice_id=10040&sign=1234","adinfo":[]},{"id":10041,"title":null,"desc":"阿尔法波音乐8","image_path":"/default/stayvoice/initimg/music1.jpg","voice_path":"/default/stayvoice/initmp3/Alphawave8.mp3","voice_time":151040,"voice_size":2417102,"voice_uptype":0,"praise_num":0,"played_num":0,"pv_played_num":0,"comment_num":0,"share_num":100,"title_ai_status":0,"desc_ai_status":0,"image_ai_status":0,"voice_ai_status":0,"status":1,"is_show":1,"is_del":0,"user_id":"f938148126bb7e8d2c670f4bfd3b6d7a","created_at":"2020-10-29 17:07:50","updated_at":null,"show_num":100,"is_top":0,"tmp_type_ids":"","source":0,"is_check":0,"v_share_num":0,"v_praise_num":0,"v_comment_num":0,"v_played_num":0,"is_speech_import":0,"is_ad":0,"voice_volume":50,"bg_music":1,"bg_music_volume":50,"bg_img":null,"top_index":0,"share_num_str":"100","praise_num_str":"0","comment_num_str":"0","played_num_str":"0","uid":"1127","uname":"11122","sex":0,"avatar":"http://test.tingshuowan.com/listen/path?url=/users/avatar/f938148126bb7e8d2c670f4bfd3b6d7a.jpeg","type_names":"万象,音乐","type_ids":"10006,10007","is_praise":0,"is_attention":0,"is_openad":0,"share_title":"快来和我一起收听【11122】的音频吧","share_desc":"阿尔法波音乐8","share_url":"http://livetest.tingshuowan.com/h5/stayvoiceshare?stayvoice_id=10041&sign=1234","share_thumbimage":"/default/stayvoice/initimg/music1.jpg","share_wxminprogram_type":2,"share_wxminprogram_appid":"wx123456789","share_wxminprogram_path":"/h5/stayvoiceshare?stayvoice_id=10041&sign=1234","adinfo":[]},{"id":10042,"title":null,"desc":"阿尔法波音乐9","image_path":"/default/stayvoice/initimg/music1.jpg","voice_path":"/default/stayvoice/initmp3/Alphawave9.mp3","voice_time":182047,"voice_size":2913219,"voice_uptype":0,"praise_num":0,"played_num":1,"pv_played_num":1,"comment_num":0,"share_num":100,"title_ai_status":0,"desc_ai_status":0,"image_ai_status":0,"voice_ai_status":0,"status":1,"is_show":1,"is_del":0,"user_id":"f938148126bb7e8d2c670f4bfd3b6d7a","created_at":"2020-10-29 17:11:50","updated_at":"2020-12-02 14:58:57","show_num":99,"is_top":0,"tmp_type_ids":"","source":0,"is_check":0,"v_share_num":0,"v_praise_num":0,"v_comment_num":0,"v_played_num":0,"is_speech_import":0,"is_ad":0,"voice_volume":50,"bg_music":1,"bg_music_volume":50,"bg_img":null,"top_index":0,"share_num_str":"100","praise_num_str":"0","comment_num_str":"0","played_num_str":"1","uid":"1127","uname":"11122","sex":0,"avatar":"http://test.tingshuowan.com/listen/path?url=/users/avatar/f938148126bb7e8d2c670f4bfd3b6d7a.jpeg","type_names":"万象,音乐","type_ids":"10006,10007","is_praise":0,"is_attention":0,"is_openad":1,"share_title":"快来和我一起收听【11122】的音频吧","share_desc":"阿尔法波音乐9","share_url":"http://livetest.tingshuowan.com/h5/stayvoiceshare?stayvoice_id=10042&sign=1234","share_thumbimage":"/default/stayvoice/initimg/music1.jpg","share_wxminprogram_type":2,"share_wxminprogram_appid":"wx123456789","share_wxminprogram_path":"/h5/stayvoiceshare?stayvoice_id=10042&sign=1234","adinfo":[]}]
     * request : {"prev":{"tab_id":0,"tag_id":0,"userId":"9a8aa29e664bd8673816e8b63b866606","source":"praise","sequence":0},"current":{"tab_id":0,"tag_id":0,"userId":"9a8aa29e664bd8673816e8b63b866606","source":"praise","sequence":"1"},"next":{"tab_id":0,"tag_id":0,"userId":"9a8aa29e664bd8673816e8b63b866606","source":"praise","sequence":0}}
     */

    private RequestBean    request;
    private List<ListBean> list;

    public RequestBean getRequest () {
        return request;
    }

    public void setRequest (RequestBean request) {
        this.request = request;
    }

    public List<ListBean> getList () {
        return list;
    }

    public void setList (List<ListBean> list) {
        this.list = list;
    }

    public static class RequestBean {
        /**
         * prev : {"tab_id":0,"tag_id":0,"userId":"9a8aa29e664bd8673816e8b63b866606","source":"praise","sequence":0}
         * current : {"tab_id":0,"tag_id":0,"userId":"9a8aa29e664bd8673816e8b63b866606","source":"praise","sequence":"1"}
         * next : {"tab_id":0,"tag_id":0,"userId":"9a8aa29e664bd8673816e8b63b866606","source":"praise","sequence":0}
         */

        private PrevBean    prev;
        private CurrentBean current;
        private NextBean    next;

        public PrevBean getPrev () {
            return prev;
        }

        public void setPrev (PrevBean prev) {
            this.prev = prev;
        }

        public CurrentBean getCurrent () {
            return current;
        }

        public void setCurrent (CurrentBean current) {
            this.current = current;
        }

        public NextBean getNext () {
            return next;
        }

        public void setNext (NextBean next) {
            this.next = next;
        }

        public static class PrevBean {
            /**
             * tab_id : 0
             * tag_id : 0
             * userId : 9a8aa29e664bd8673816e8b63b866606
             * source : praise
             * sequence : 0
             */

            private int    tab_id;
            private int    tag_id;
            private String userId;
            private String source;
            private int    sequence;

            public int getTab_id () {
                return tab_id;
            }

            public void setTab_id (int tab_id) {
                this.tab_id = tab_id;
            }

            public int getTag_id () {
                return tag_id;
            }

            public void setTag_id (int tag_id) {
                this.tag_id = tag_id;
            }

            public String getUserId () {
                return userId;
            }

            public void setUserId (String userId) {
                this.userId = userId;
            }

            public String getSource () {
                return source;
            }

            public void setSource (String source) {
                this.source = source;
            }

            public int getSequence () {
                return sequence;
            }

            public void setSequence (int sequence) {
                this.sequence = sequence;
            }
        }

        public static class CurrentBean {
            /**
             * tab_id : 0
             * tag_id : 0
             * userId : 9a8aa29e664bd8673816e8b63b866606
             * source : praise
             * sequence : 1
             */

            private int    tab_id;
            private int    tag_id;
            private String userId;
            private String source;
            private String sequence;

            public int getTab_id () {
                return tab_id;
            }

            public void setTab_id (int tab_id) {
                this.tab_id = tab_id;
            }

            public int getTag_id () {
                return tag_id;
            }

            public void setTag_id (int tag_id) {
                this.tag_id = tag_id;
            }

            public String getUserId () {
                return userId;
            }

            public void setUserId (String userId) {
                this.userId = userId;
            }

            public String getSource () {
                return source;
            }

            public void setSource (String source) {
                this.source = source;
            }

            public String getSequence () {
                return sequence;
            }

            public void setSequence (String sequence) {
                this.sequence = sequence;
            }
        }

        public static class NextBean {
            /**
             * tab_id : 0
             * tag_id : 0
             * userId : 9a8aa29e664bd8673816e8b63b866606
             * source : praise
             * sequence : 0
             */

            private int    tab_id;
            private int    tag_id;
            private String userId;
            private String source;
            private int    sequence;

            public int getTab_id () {
                return tab_id;
            }

            public void setTab_id (int tab_id) {
                this.tab_id = tab_id;
            }

            public int getTag_id () {
                return tag_id;
            }

            public void setTag_id (int tag_id) {
                this.tag_id = tag_id;
            }

            public String getUserId () {
                return userId;
            }

            public void setUserId (String userId) {
                this.userId = userId;
            }

            public String getSource () {
                return source;
            }

            public void setSource (String source) {
                this.source = source;
            }

            public int getSequence () {
                return sequence;
            }

            public void setSequence (int sequence) {
                this.sequence = sequence;
            }
        }
    }

    public static class ListBean implements Serializable {
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
        private String     bg_img;
        private int        top_index;
        private String     share_num_str;
        private String     praise_num_str;
        private String     comment_num_str;
        private String     played_num_str;
        private String     uid;
        private String     uname;
        private Object     sex;
        private String     avatar;
        private String     type_names;
        private String     type_ids;
        private int        is_praise;
        private int        is_attention;//0-未关注，1-已关注
        private int        is_openad;
        private String     share_title;
        private String     share_desc;
        private String     share_url;
        private String     share_thumbimage;
        private int        share_wxminprogram_type;
        private String     share_wxminprogram_appid;
        private String     share_wxminprogram_path;
        private AdinfoBean adinfo;
        private int        playType;
        private String     bg_music_path;
        private String     view_time;
        private int        showCustomAd;

        private boolean isPlay;//是否播放 true 播放  false 停止
        private int     progress;//当前进度

        private String      voice_path_cache;
        private String      bg_music_path_cache;
        private int         barrageCount;//请求弹幕次数标记，30秒为一次
        private boolean     isAd;//是否是广告页
        private QcAdManager adManager;//广告管理类
        private String      adId;//用于获取广告，防止以后变动，不返还取本地
        private String      mId;//用于获取广告，防止以后变动，不返还取本地
        private boolean     isLogin;//是否是登录状态

        public String getVoice_path_cache () {
            return voice_path_cache;
        }

        public void setVoice_path_cache (String voice_path_cache) {
            this.voice_path_cache = voice_path_cache;
        }

        public String getBg_music_path_cache () {
            return bg_music_path_cache;
        }

        public void setBg_music_path_cache (String bg_music_path_cache) {
            this.bg_music_path_cache = bg_music_path_cache;
        }

        public int getShowCustomAd () {
            return showCustomAd;
        }

        public void setShowCustomAd (int showCustomAd) {
            this.showCustomAd = showCustomAd;
        }

        public int getPlayType () {
            return playType;
        }

        public void setPlayType (int playType) {
            this.playType = playType;
        }

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

        public String getBg_img () {
            return bg_img;
        }

        public void setBg_img (String bg_img) {
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

        public Object getSex () {
            return sex;
        }

        public void setSex (Object sex) {
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

        public int getBarrageCount () {
            barrageCount = getVoice_time() / (30 * 1000);
            if (getVoice_time() % (30 * 1000) != 0) {
                barrageCount++;
            }
            return barrageCount;
        }

        public boolean isAd () {
            return isAd;
        }

        public void setAd (boolean ad) {
            isAd = ad;
        }

        public QcAdManager getAdManager () {
            return adManager;
        }

        public void setAdManager (QcAdManager adManager) {
            this.adManager = adManager;
        }

        public String getAdId () {
            return TextUtils.isEmpty(adId) ? Constant.QC_VOICE_INFO_AD_ADID : adId;
        }

        public void setAdId (String adId) {
            this.adId = adId;
        }

        public String getmId () {
            return TextUtils.isEmpty(mId) ? Constant.COMMON_QC_MID : mId;
        }

        public void setmId (String mId) {
            this.mId = mId;
        }

        public boolean isLogin () {
            return isLogin;
        }

        public void setLogin (boolean login) {
            isLogin = login;
        }

        public AdinfoBean getAdinfo () {
            return adinfo;
        }

        public void setAdinfo (AdinfoBean adinfo) {
            this.adinfo = adinfo;
        }

        public boolean isPlay () {
            return isPlay;
        }

        public void setPlay (boolean play) {
            isPlay = play;
        }

        public int getProgress () {
            return progress;
        }

        public void setProgress (int progress) {
            this.progress = progress;
        }

        public String getBg_music_path () {
            return bg_music_path;
        }

        public void setBg_music_path (String bg_music_path) {
            this.bg_music_path = bg_music_path;
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

        public String getView_time () {
            return view_time;
        }

        public void setView_time (String view_time) {
            this.view_time = view_time;
        }
    }

}
