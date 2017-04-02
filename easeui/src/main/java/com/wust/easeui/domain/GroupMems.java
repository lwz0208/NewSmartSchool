package com.wust.easeui.domain;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Erick on 2016/9/9.
 */
public class GroupMems implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * code : 1
     * data : {"action":"get","uri":"http://a1.easemob.com/516881172/mo/chatgroups/239169559020437928","entities":[],"data":[{"membersonly":true,"allowinvites":false,"public":true,"name":"全院","description":"全院群聊","affiliations":[{"owner":"143"},{"member":"155"},{"member":"163"},{"member":"164"},{"member":"162"},{"member":"161"},{"member":"165"},{"member":"166"},{"member":"167"}],"id":"239169559020437928","maxusers":2000,"affiliations_count":9}],"timestamp":1473387653447,"duration":6,"count":1}
     * msg : 请求成功
     */

    private int code;
    /**
     * action : get
     * uri : http://a1.easemob.com/516881172/mo/chatgroups/239169559020437928
     * entities : []
     * data : [{"membersonly":true,"allowinvites":false,"public":true,"name":"全院","description":"全院群聊","affiliations":[{"owner":"143"},{"member":"155"},{"member":"163"},{"member":"164"},{"member":"162"},{"member":"161"},{"member":"165"},{"member":"166"},{"member":"167"}],"id":"239169559020437928","maxusers":2000,"affiliations_count":9}]
     * timestamp : 1473387653447
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
        private long timestamp;
        private int duration;
        private int count;
        private List<?> entities;
        /**
         * membersonly : true
         * allowinvites : false
         * public : true
         * name : 全院
         * description : 全院群聊
         * affiliations : [{"owner":"143"},{"member":"155"},{"member":"163"},{"member":"164"},{"member":"162"},{"member":"161"},{"member":"165"},{"member":"166"},{"member":"167"}]
         * id : 239169559020437928
         * maxusers : 2000
         * affiliations_count : 9
         */

        private List<DataBean1> data;

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

        public List<DataBean1> getData() {
            return data;
        }

        public void setData(List<DataBean1> data) {
            this.data = data;
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
             * owner : 143
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
                private String member;
                private String owner;

                public String getMember() {
                    return member;
                }

                public void setMember(String member) {
                    this.member = member;
                }

                public String getOwner() {
                    return owner;
                }

                public void setOwner(String owner) {
                    this.owner = owner;
                }
            }
        }
    }
}
