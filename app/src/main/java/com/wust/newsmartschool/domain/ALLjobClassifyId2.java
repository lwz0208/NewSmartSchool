package com.wust.newsmartschool.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Erick on 2016/11/15.
 */
public class ALLjobClassifyId2 implements Serializable {

    /**
     * data : [{"name":"副科级","id":1},{"name":"科级","id":2},{"name":"负责人","id":3},{"name":"组长","id":15},{"name":"副主席","id":6},{"name":"主席","id":7},{"name":"副院长","id":8},{"name":"院长","id":9},{"name":"副书记","id":10},{"name":"书记","id":11},{"name":"副护士长","id":14},{"name":"护士长","id":13},{"name":"技师长","id":16}]
     * typeName : 职务
     * typeId : jobClassifyId
     */

    private String typeName;
    private String typeId;
    /**
     * name : 副科级
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
