package com.wust.newsmartschool.domain;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Erick on 2016/9/25.
 */
public class GroupMemsEntity implements Serializable {

    /**
     * code : 1
     * data : {"action":"get","uri":"http://a1.easemob.com/516881172/mo/chatgroups/1474448127305","entities":[],"data":{"membersonly":true,"allowinvites":false,"public":false,"name":"李文钊111的讨论组","description":"李文钊111建立的讨论组","affiliations":[{"member":"2871","name":"王嵩333"},{"owner":"2881","name":"李文钊111"}],"id":"1474448127305","maxusers":200,"affiliations_count":2},"timestamp":1474876100593,"duration":6,"count":1}
     * msg : 请求成功
     */

    private int code;
    /**
     * action : get
     * uri : http://a1.easemob.com/516881172/mo/chatgroups/1474448127305
     * entities : []
     * data : {"membersonly":true,"allowinvites":false,"public":false,"name":"李文钊111的讨论组","description":"李文钊111建立的讨论组","affiliations":[{"member":"2871","name":"王嵩333"},{"owner":"2881","name":"李文钊111"}],"id":"1474448127305","maxusers":200,"affiliations_count":2}
     * timestamp : 1474876100593
     * duration : 6
     * count : 1
     */

    private DataBean data;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class DataBean {
        private String action;
        private String uri;
        /**
         * membersonly : true
         * allowinvites : false
         * public : false
         * name : 李文钊111的讨论组
         * description : 李文钊111建立的讨论组
         * affiliations : [{"member":"2871","name":"王嵩333"},{"owner":"2881","name":"李文钊111"}]
         * id : 1474448127305
         * maxusers : 200
         * affiliations_count : 2
         */

        private DataBean1 data;
        private long timestamp;
        private int duration;
        private int count;
        private List<?> entities;

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }

        public String getUri() {
            return uri;
        }

        public void setUri(String uri) {
            this.uri = uri;
        }

        public DataBean1 getData() {
            return data;
        }

        public void setData1(DataBean1 data) {
            this.data = data;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public List<?> getEntities() {
            return entities;
        }

        public void setEntities(List<?> entities) {
            this.entities = entities;
        }

        public static class DataBean1 {
            private boolean membersonly;
            private boolean allowinvites;
            @SerializedName("public")
            private boolean publicX;
            private String name;
            private String description;
            private String id;
            private int maxusers;
            private int affiliations_count;
            /**
             * member : 2871
             * name : 王嵩333
             */

            private List<AffiliationsBean> affiliations;

            public boolean isMembersonly() {
                return membersonly;
            }

            public void setMembersonly(boolean membersonly) {
                this.membersonly = membersonly;
            }

            public boolean isAllowinvites() {
                return allowinvites;
            }

            public void setAllowinvites(boolean allowinvites) {
                this.allowinvites = allowinvites;
            }

            public boolean isPublicX() {
                return publicX;
            }

            public void setPublicX(boolean publicX) {
                this.publicX = publicX;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public int getMaxusers() {
                return maxusers;
            }

            public void setMaxusers(int maxusers) {
                this.maxusers = maxusers;
            }

            public int getAffiliations_count() {
                return affiliations_count;
            }

            public void setAffiliations_count(int affiliations_count) {
                this.affiliations_count = affiliations_count;
            }

            public List<AffiliationsBean> getAffiliations() {
                return affiliations;
            }

            public void setAffiliations(List<AffiliationsBean> affiliations) {
                this.affiliations = affiliations;
            }

            public static class AffiliationsBean {
                private String member = "";
                private String name = "";
                private String owner = "";

                public String getOwner() {
                    return owner;
                }

                public void setOwner(String owner) {
                    this.owner = owner;
                }

                public String getMember() {
                    return member;
                }

                public void setMember(String member) {
                    this.member = member;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }
            }
        }
    }
}
