package com.ding.chat.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Erick on 2016/11/15.
 */
public class ALLpartyBranchId3 implements Serializable {

    /**
     * data : [{"name":"妇儿党支部","id":1},{"name":"行管党支部","id":2},{"name":"机关党支部","id":3},{"name":"门诊党支部","id":4},{"name":"内科党支部","id":5},{"name":"内科支部","id":6},{"name":"外五党支部","id":7},{"name":"血液肿瘤党支部","id":8},{"name":"医技党支部","id":9}]
     * typeName : 党支部
     * typeId : partyBranchId
     */

    private String typeName;
    private String typeId;
    /**
     * name : 妇儿党支部
     * id : 1
     */

    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        private String name;
        private int id;

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
    }
}
