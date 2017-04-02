package com.ding.chat.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Erick on 2016/11/15.
 */
public class ALLpositionalTitleId4 implements Serializable {

    /**
     * data : [{"name":"按摩技师","id":1},{"name":"初级会计员","id":2},{"name":"初级技工","id":3},{"name":"初级助理会计师","id":4},{"name":"放射技师","id":5},{"name":"副主任护师","id":6},{"name":"副主任技师","id":7},{"name":"副主任药师","id":8},{"name":"副主任医师","id":9},{"name":"高级会计师","id":10},{"name":"高级技工","id":11},{"name":"护理员","id":12},{"name":"护师","id":13},{"name":"护士","id":14},{"name":"会计","id":15},{"name":"会计员","id":16},{"name":"会技师","id":17},{"name":"技师","id":18},{"name":"技士","id":19},{"name":"技术员 ","id":20},{"name":"检验技师","id":21},{"name":"检验技士","id":22},{"name":"检验主管技师","id":23},{"name":"见习护士","id":24},{"name":"见习技师","id":25},{"name":"见习检验技师","id":26},{"name":"见习药师","id":27},{"name":"见习医师","id":28},{"name":"见习助理医师","id":29},{"name":"康复技士","id":30},{"name":"口腔主管技师","id":31},{"name":"统计师","id":32},{"name":"西药技师","id":33},{"name":"药师","id":34},{"name":"医师","id":35},{"name":"医士","id":36},{"name":"中级技工","id":37},{"name":"中药师 ","id":38},{"name":"中药士","id":39},{"name":"中医师","id":40},{"name":"主管护师","id":41},{"name":"主管技师 ","id":42},{"name":"主管药师","id":43},{"name":"主任护师","id":44},{"name":"主任药师","id":45},{"name":"主任医师","id":46},{"name":"主任中药师","id":47},{"name":"主治医师","id":48},{"name":"主治中医师","id":49},{"name":"助理馆员","id":50},{"name":"助理会计师","id":51},{"name":"助理统计师","id":52}]
     * typeName : 职称
     * typeId : positionalTitleId
     */

    private String typeName;
    private String typeId;
    /**
     * name : 按摩技师
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
