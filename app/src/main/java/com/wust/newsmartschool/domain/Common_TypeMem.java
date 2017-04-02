package com.wust.newsmartschool.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Erick on 2016/9/5.
 */
public class Common_TypeMem implements Serializable {

    /**
     * code : 1
     * data : [{"partyBranchId":1,"userRealname":"卢海川","personnelId":"","userImgurl":"15671628696.png","userGender":2,"telephone":"15671628696","userId":90},{"partyBranchId":1,"userRealname":"镇方权","personnelId":"","positionalTitleId":1,"userImgurl":"","userGender":2,"telephone":"13163359577","userId":92},{"partyBranchId":1,"workNumber":"","userRealname":"王嵩","personnelId":"","positionalTitleId":4,"userImgurl":"","userGender":2,"telephone":"15107118210","userId":93},{"partyBranchId":1,"workNumber":"","userRealname":"顾进广","personnelId":"","positionalTitleId":4,"userImgurl":"","userGender":2,"telephone":"13307129868","userId":91},{"partyBranchId":1,"workNumber":"1234","userRealname":"测试3","personnelId":"","positionalTitleId":3,"userImgurl":"","userGender":2,"telephone":"1234","userId":119},{"partyBranchId":1,"workNumber":"1234","userRealname":"测试6","personnelId":"543543","positionalTitleId":10,"userImgurl":"","userGender":2,"telephone":"12332","userId":120},{"partyBranchId":1,"workNumber":"12312","userRealname":"测试7","personnelId":"321","positionalTitleId":1,"userImgurl":"","userGender":2,"telephone":"432123","userId":121},{"partyBranchId":1,"workNumber":"123","userRealname":"测试9","personnelId":"13213","positionalTitleId":2,"userImgurl":"","userGender":2,"telephone":"123421","userId":122},{"partyBranchId":1,"workNumber":"123","userRealname":"测试10","personnelId":"4312","positionalTitleId":2,"userImgurl":"","userGender":2,"telephone":"12333","userId":123},{"partyBranchId":1,"workNumber":"123","userRealname":"测试11","personnelId":"321","roleId":0,"positionalTitleId":16,"userImgurl":"","userGender":2,"telephone":"1234231","userId":124},{"partyBranchId":1,"workNumber":"123","userRealname":"测试112","personnelId":"321","roleId":0,"positionalTitleId":16,"userImgurl":"","userGender":2,"telephone":"12342313","userId":125},{"partyBranchId":1,"workNumber":"12312","userRealname":"测试12","personnelId":"12321","roleId":0,"positionalTitleId":1,"userImgurl":"","userGender":2,"telephone":"4213","userId":126},{"partyBranchId":1,"workNumber":"123","userRealname":"测试15","personnelId":"321","roleId":0,"positionalTitleId":1,"userImgurl":"","userGender":2,"telephone":"4322","userId":127},{"partyBranchId":1,"workNumber":"123","userRealname":"测试16","personnelId":"321","roleId":0,"positionalTitleId":1,"userImgurl":"","userGender":2,"telephone":"43221","userId":128},{"partyBranchId":1,"workNumber":"432","userRealname":"测试17","personnelId":"43243124","roleId":2,"positionalTitleId":18,"userImgurl":"","userGender":2,"telephone":"123432","userId":129},{"partyBranchId":1,"workNumber":"3213","userRealname":"测试20","personnelId":"1232132","roleId":1,"positionalTitleId":1,"userImgurl":"","userGender":2,"telephone":"14325","userId":130},{"partyBranchId":1,"workNumber":"12312","userRealname":"测试21","personnelId":"12321","roleId":1,"positionalTitleId":18,"userImgurl":"","userGender":2,"telephone":"3214123","userId":131},{"partyBranchId":1,"workNumber":"123123","userRealname":"3213","personnelId":"12321","roleId":1,"positionalTitleId":20,"userImgurl":"","userGender":2,"telephone":"312312","userId":132},{"partyBranchId":1,"workNumber":"12312","userRealname":"测试25","personnelId":"123","roleId":2,"positionalTitleId":1,"userImgurl":"","userGender":2,"telephone":"4323123","userId":133},{"partyBranchId":1,"workNumber":"12312134324","userRealname":"测试25","personnelId":"12343241324","roleId":2,"positionalTitleId":14,"userImgurl":"","userGender":2,"telephone":"4323123324324","userId":134},{"partyBranchId":1,"workNumber":"412312","userRealname":"测试24","personnelId":"1234123","roleId":1,"positionalTitleId":20,"userImgurl":"","userGender":2,"telephone":"1231231","userId":136},{"partyBranchId":1,"workNumber":"412312","userRealname":"测试26","personnelId":"1234123","roleId":1,"positionalTitleId":20,"userImgurl":"","userGender":2,"telephone":"12312313","userId":142}]
     * msg : 请求成功
     */

    private int code;
    private String msg;
    /**
     * partyBranchId : 1
     * userRealname : 卢海川
     * personnelId :
     * userImgurl : 15671628696.png
     * userGender : 2
     * telephone : 15671628696
     * userId : 90
     */

    private List<Common_TypeMem_Data> data;

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

    public List<Common_TypeMem_Data> getData() {
        return data;
    }

    public void setData(List<Common_TypeMem_Data> data) {
        this.data = data;
    }


}
