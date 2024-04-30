package com.meng.api.semiee;

import com.meng.eleTool.tools.GSON;
import java.util.List;

public class ContactWay {

    public int code;
    public Object remark;
    public Result result;

    public class Result {

        public int brandId;
        public ContactWay_ contactWay;
        public int isViewManufactarText;
        public int agentViewType;
        public List<Object> agentContactWays;

        public class ContactWay_ {

            public int id;
            public String name;
            public Object descri;
            public String address;
            public String telphone;
            public Object phone;
            public Object qq;
            public Object fax;
            public String email;
            public String web_url;
            public int is_same_weixin;
            public Object phone_remark;
            public int is_telphone_view_phone;
            public int is_telphone_view_copy;
            public int is_phone_view_phone;
            public int is_phone_view_copy;
            public int is_son_company;
            public int son_company_num;
            public List<Object> sonContactWays;
        }
    }

    @Override
    public String toString() {
        return GSON.toJson(this);
    }
}
