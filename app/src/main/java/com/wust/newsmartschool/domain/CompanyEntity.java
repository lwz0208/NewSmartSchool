package com.wust.newsmartschool.domain;

import java.io.Serializable;
import java.util.List;

public class CompanyEntity implements Serializable {


    /**
     * code : 1
     * data : [{"descri":"啊啊啊","subList":[{"descri":"啊啊","subList":[{"descri":"啊啊啊","year":2016,"name":"软件工程","id":1,"type":0}],"name":"软件工程","id":1}],"name":"计算机学院","id":1}]
     * msg : 操作成功
     */

    private int code;
    private String msg;
    /**
     * descri : 啊啊啊
     * subList : [{"descri":"啊啊","subList":[{"descri":"啊啊啊","year":2016,"name":"软件工程","id":1,"type":0}],"name":"软件工程","id":1}]
     * name : 计算机学院
     * id : 1
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

    public static class DataBean implements Serializable {
        private String descri;
        private String name;
        private int id;
        /**
         * descri : 啊啊
         * subList : [{"descri":"啊啊啊","year":2016,"name":"软件工程","id":1,"type":0}]
         * name : 软件工程
         * id : 1
         */

        private List<SubListBean> subList;

        public String getDescri() {
            return descri;
        }

        public void setDescri(String descri) {
            this.descri = descri;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public List<SubListBean> getSubList() {
            return subList;
        }

        public void setSubList(List<SubListBean> subList) {
            this.subList = subList;
        }

        public static class SubListBean implements Serializable {
            private String descri;
            private String name;
            private int id;
            /**
             * descri : 啊啊啊
             * year : 2016
             * name : 软件工程
             * id : 1
             * type : 0
             */

            private List<SubListBean2> subList;

            public String getDescri() {
                return descri;
            }

            public void setDescri(String descri) {
                this.descri = descri;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public List<SubListBean2> getSubList() {
                return subList;
            }

            public void setSubList(List<SubListBean2> subList) {
                this.subList = subList;
            }

            public static class SubListBean2 implements Serializable {
                private String descri;
                private int year;
                private String name;
                private int id;
                private int type;


                public String getDescri() {
                    return descri;
                }

                public void setDescri(String descri) {
                    this.descri = descri;
                }

                public int getYear() {
                    return year;
                }

                public void setYear(int year) {
                    this.year = year;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public int getType() {
                    return type;
                }

                public void setType(int type) {
                    this.type = type;
                }
            }
        }
    }
}
