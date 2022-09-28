package com.maishuo.tingshuohenhaowan.bean;

import com.maishuo.tingshuohenhaowan.api.response.PhonicListBean;

import java.util.List;

/**
 * @author yun on 2021/2/5
 * EXPLAIN:
 */
public class PhonicDetailBean {

    /**
     * list : [{"id":11892,"title":null,"desc":"富贵","image_path":"http://test.tingshuowan.com/listen/path?url=/users/4e7594c4984439433bdaaa18c7036e8a/stayvoice/images/2021/5a8467a00c24b5365d11f610c2c8bc5e.jpg","voice_path":"http://test.tingshuowan.com/listen/path?url=/users/4e7594c4984439433bdaaa18c7036e8a/stayvoice/voice/2021/1a702d28fb2de89e376686fc55372a6e.wav","voice_time":18018,"voice_size":1157268,"voice_uptype":0,"praise_num":1,"played_num":11,"pv_played_num":178,"comment_num":0,"share_num":0,"title_ai_status":0,"desc_ai_status":1,"image_ai_status":1,"voice_ai_status":1,"status":1,"is_show":1,"is_del":0,"user_id":"4e7594c4984439433bdaaa18c7036e8a","created_at":"2021-01-27 18:35:39","updated_at":"2021-02-05 15:33:56","show_num":90,"is_top":1,"tmp_type_ids":null,"source":0,"is_check":1,"v_share_num":0,"v_praise_num":0,"v_comment_num":0,"v_played_num":0,"is_speech_import":0,"is_ad":0,"voice_volume":50,"bg_music":null,"bg_music_volume":50,"bg_img":null,"top_index":2,"share_num_str":"0","praise_num_str":"1","comment_num_str":"0","played_num_str":"11","uid":"1279","uname":"好睡的斑马","sex":0,"avatar":"http://test.tingshuowan.com/listen/path?url=/default/me_pic_boy.png","type_names":"吐槽","type_ids":"10001","is_praise":0,"is_attention":1,"is_openad":1,"share_title":"快来和我一起收听【好睡的斑马】的音频吧","share_desc":"富贵","share_url":"http://livetest.tingshuowan.com/h5/stayvoiceshare?stayvoice_id=11892&sign=1234","share_thumbimage":"http://test.tingshuowan.com/listen/path?url=/users/4e7594c4984439433bdaaa18c7036e8a/stayvoice/images/2021/5a8467a00c24b5365d11f610c2c8bc5e.jpg","share_wxminprogram_type":2,"share_wxminprogram_appid":"wx123456789","share_wxminprogram_path":"/h5/stayvoiceshare?stayvoice_id=11892&sign=1234","adinfo":{}}]
     * request : {"prev":[],"current":[],"next":[]}
     */

    private RequestBean                   request;
    private List<PhonicListBean.ListBean> list;

    public RequestBean getRequest () {
        return request;
    }

    public void setRequest (RequestBean request) {
        this.request = request;
    }

    public List<PhonicListBean.ListBean> getList () {
        return list;
    }

    public void setList (List<PhonicListBean.ListBean> list) {
        this.list = list;
    }

    public static class RequestBean {
        private List<?> prev;
        private List<?> current;
        private List<?> next;

        public List<?> getPrev () {
            return prev;
        }

        public void setPrev (List<?> prev) {
            this.prev = prev;
        }

        public List<?> getCurrent () {
            return current;
        }

        public void setCurrent (List<?> current) {
            this.current = current;
        }

        public List<?> getNext () {
            return next;
        }

        public void setNext (List<?> next) {
            this.next = next;
        }
    }


}




