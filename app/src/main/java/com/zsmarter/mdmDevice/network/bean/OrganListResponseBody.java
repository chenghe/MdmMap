package com.zsmarter.mdmDevice.network.bean;

import java.util.List;

/**
 * Created by hecheng on 2018/6/27
 */
public class OrganListResponseBody extends BaseResponse {

    private Context context;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public class Context {
        private Page page;

        public Page getPage() {
            return page;
        }

        public void setPage(Page page) {
            this.page = page;
        }

        public class Page {
            private List<RowItem> rows;

            public List<RowItem> getRows() {
                return rows;
            }

            public void setRows(List<RowItem> rows) {
                this.rows = rows;
            }

            public class RowItem {
                private String orgName;
                private String orgCode;
                private String oid;
                private String orgParentID;

                public String getOrgName() {
                    return orgName;
                }

                public void setOrgName(String orgName) {
                    this.orgName = orgName;
                }

                public String getOrgCode() {
                    return orgCode;
                }

                public void setOrgCode(String orgCode) {
                    this.orgCode = orgCode;
                }

                public String getOid() {
                    return oid;
                }

                public void setOid(String oid) {
                    this.oid = oid;
                }

                public String getOrgParentID() {
                    return orgParentID;
                }

                public void setOrgParentID(String orgParentID) {
                    this.orgParentID = orgParentID;
                }
            }
        }

    }
}
