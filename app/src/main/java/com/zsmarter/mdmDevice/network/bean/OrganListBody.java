package com.zsmarter.mdmDevice.network.bean;

/**
 * Created by hecheng on 2018/6/27
 */
public class OrganListBody {
    private Page page;
    private Params params;

    public OrganListBody(String orgParentID, String orgRange) {
        params = new Params(orgParentID,orgRange);
        page = new Page("100","ASC","orgCode","1");
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public Params getParams() {
        return params;
    }

    public void setParams(Params params) {
        this.params = params;
    }

    public class Page{
        private String pageSize;
        private String orderType;
        private String orderFields;
        private String pageNo;

        public Page(String pageSize, String orderType, String orderFields, String pageNo) {
            this.pageSize = pageSize;
            this.orderType = orderType;
            this.orderFields = orderFields;
            this.pageNo = pageNo;
        }

        public String getPageSize() {
            return pageSize;
        }

        public void setPageSize(String pageSize) {
            this.pageSize = pageSize;
        }

        public String getOrderType() {
            return orderType;
        }

        public void setOrderType(String orderType) {
            this.orderType = orderType;
        }

        public String getOrderFields() {
            return orderFields;
        }

        public void setOrderFields(String orderFields) {
            this.orderFields = orderFields;
        }

        public String getPageNo() {
            return pageNo;
        }

        public void setPageNo(String pageNo) {
            this.pageNo = pageNo;
        }
    }

    public class Params{
        private String orgParentID;
        private String orgRange = "0";

        public Params(String orgRange) {
            this.orgRange = orgRange;
        }

        public Params(String orgParentID, String orgRange) {
            this.orgParentID = orgParentID;
            this.orgRange = orgRange;
        }

        public String getOrgParentID() {
            return orgParentID;
        }

        public void setOrgParentID(String orgParentID) {
            this.orgParentID = orgParentID;
        }

        public String getOrgRange() {
            return orgRange;
        }

        public void setOrgRange(String orgRange) {
            this.orgRange = orgRange;
        }
    }
}
