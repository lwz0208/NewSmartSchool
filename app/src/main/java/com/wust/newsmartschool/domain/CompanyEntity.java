package com.wust.newsmartschool.domain;

import java.io.Serializable;
import java.util.List;

public class CompanyEntity implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    int code;
    int companyId;
    String companyName;
    List<Company_Dep> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public List<Company_Dep> getData() {
        return data;
    }

    public void setData(List<Company_Dep> data) {
        this.data = data;
    }

}
