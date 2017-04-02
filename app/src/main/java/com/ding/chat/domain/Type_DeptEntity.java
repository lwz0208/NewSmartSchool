package com.ding.chat.domain;

import java.util.List;

/**
 * Created by Erick on 2016/9/4.
 */
public class Type_DeptEntity {
    String typeName;
    String typeId;
    List<Data> data;

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public static class Data {
        List<Children> children;
        int id;
        String name;
        int pId;
        int usersNum;
        List<UserInfo> usersInfo;

        public List<Children> getChildren() {
            return children;
        }

        public void setChildren(List<Children> children) {
            this.children = children;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getpId() {
            return pId;
        }

        public void setpId(int pId) {
            this.pId = pId;
        }

        public int getUsersNum() {
            return usersNum;
        }

        public void setUsersNum(int usersNum) {
            this.usersNum = usersNum;
        }

        public List<UserInfo> getUsersInfo() {
            return usersInfo;
        }

        public void setUsersInfo(List<UserInfo> usersInfo) {
            this.usersInfo = usersInfo;
        }

        public static class Children {
            List<Children> children;

            public List<Children> getChildren() {
                return children;
            }

            public void setChildren(List<Children> children) {
                this.children = children;
            }
        }

        public static class UserInfo {
            String userRealname;
            String personnelId;
            String userId;
            String telephone;

            public String getUserRealname() {
                return userRealname;
            }

            public void setUserRealname(String userRealname) {
                this.userRealname = userRealname;
            }

            public String getPersonnelId() {
                return personnelId;
            }

            public void setPersonnelId(String personnelId) {
                this.personnelId = personnelId;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public String getTelephone() {
                return telephone;
            }

            public void setTelephone(String telephone) {
                this.telephone = telephone;
            }
        }
    }


}

