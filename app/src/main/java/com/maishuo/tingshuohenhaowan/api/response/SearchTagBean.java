package com.maishuo.tingshuohenhaowan.api.response;

import java.util.List;

/**
 * author ：yh
 * date : 2021/1/18 11:32
 * description :
 */
public final class SearchTagBean {

    private List<RankBean>  rank;
    private List<String>    tags;//搜索历史记录
    private List<TypesBean> types;//搜索的分类标签

    public List<RankBean> getRank () {
        return rank;
    }

    public void setRank (List<RankBean> rank) {
        this.rank = rank;
    }

    public List<TypesBean> getTypes () {
        return types;
    }

    public void setTypes (List<TypesBean> types) {
        this.types = types;
    }

    public List<String> getTags () {
        return tags;
    }

    public void setTags (List<String> tags) {
        this.tags = tags;
    }

    public static class RankBean {
        /**
         * subrankingType : 4
         * subrankingName : 免费榜
         * backImage : http://test.tingshuowan.com/listen/path?url=/default/rankingCoverImage/135.png
         */

        private int    subrankingType;
        private String subrankingName;
        private String backImage;

        public int getSubrankingType () {
            return subrankingType;
        }

        public void setSubrankingType (int subrankingType) {
            this.subrankingType = subrankingType;
        }

        public String getSubrankingName () {
            return subrankingName;
        }

        public void setSubrankingName (String subrankingName) {
            this.subrankingName = subrankingName;
        }

        public String getBackImage () {
            return backImage;
        }

        public void setBackImage (String backImage) {
            this.backImage = backImage;
        }
    }

    public static class TypesBean {
        /**
         * type_id : 10013
         * type_name : w333
         * type : 2
         * alert_content :
         * special :
         */

        private int    type_id;
        private String type_name;
        private int    type;
        private String alert_content;
        private String special;

        public int getType_id () {
            return type_id;
        }

        public void setType_id (int type_id) {
            this.type_id = type_id;
        }

        public String getType_name () {
            return type_name;
        }

        public void setType_name (String type_name) {
            this.type_name = type_name;
        }

        public int getType () {
            return type;
        }

        public void setType (int type) {
            this.type = type;
        }

        public String getAlert_content () {
            return alert_content;
        }

        public void setAlert_content (String alert_content) {
            this.alert_content = alert_content;
        }

        public String getSpecial () {
            return special;
        }

        public void setSpecial (String special) {
            this.special = special;
        }
    }
}