package com.wust.newsmartschool.domain;

import java.util.List;

/**
 * Created by Erick on 2017/2/9.
 */
public class MeetingTopics {


    /**
     * code : 1
     * data : [{"item_value_list":[{"item_id":51,"item_value":"hc测试5"},{"item_id":52,"item_value":"555"},{"item_id":53,"item_value":"开发人员测试科室"},{"item_id":54,"item_value":"[\"/upload/files/20170208_120129_5926183201750905.js\"]"}],"id":5}]
     * msg : 请求成功
     */

    private int code;
    private String msg;
    /**
     * item_value_list : [{"item_id":51,"item_value":"hc测试5"},{"item_id":52,"item_value":"555"},{"item_id":53,"item_value":"开发人员测试科室"},{"item_id":54,"item_value":"[\"/upload/files/20170208_120129_5926183201750905.js\"]"}]
     * id : 5
     */

    private List<DataBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private int id;
        /**
         * item_id : 51
         * item_value : hc测试5
         */

        private List<ItemValueListBean> item_value_list;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public List<ItemValueListBean> getItem_value_list() {
            return item_value_list;
        }

        public void setItem_value_list(List<ItemValueListBean> item_value_list) {
            this.item_value_list = item_value_list;
        }

        public static class ItemValueListBean {
            private int item_id;
            private String item_value;

            public int getItem_id() {
                return item_id;
            }

            public void setItem_id(int item_id) {
                this.item_id = item_id;
            }

            public String getItem_value() {
                return item_value;
            }

            public void setItem_value(String item_value) {
                this.item_value = item_value;
            }
        }
    }
}
