package com.meng.api.semiee;

import com.meng.eleTool.tools.GSON;
import java.util.ArrayList;

public class ChipInformation {

    public int code;
    public Object remark;
    public Result result;

    public class Result {

        public String id;
        public int fk_t_brand_id;
        public String model;
        public String name;
        public String descr;
        public String feature;
        public String used;
        public String manufacturer_contact_way;
        public String agent_contact_way;
        public Object semiee_contact_way;
        public String manufacturer_url;
        public Object agent_url;
        public Object semiee_url;
        public Object terminal_used;
        public String update_time;
        public String brand_name;
        public int brand_is_vip;
        public int is_collect;
        public Object email;
        public String shareUrl;
        public Object dsFile;
        public Object params;
        public Object fnFile;
        public Object smFile;
        public ArrayList<ChipLink> chipLinks;
        public Object useFiles;
        public Object relatedDataFiles;
        public int param_num;
        public int file_num;
        public int link_num;
        public int func_file_num;
        public int reference_price_num;
        public Object items;
        public Object chipReferencePrice;
        public int similar_num;
        public Object sameBrandSimilarChips;
        public Object differenceBrandSimilarChips;
        public int choice_similar_num;
        public Object sameBrandChoiceSimilarChips;
        public Object internalBrandChoiceSimilarChips;
        public Object abroadBrandChoiceSimilarChips;
        public int devSuiteNum;
        public int appNum;
        public int schemaNum;
        public int childmodel_num;
        public Object childModels;
        public Object sample_apply_num;
        public Object contactWay_num;

        public class ChipLink {

            public int id;
            public String fk_t_chip_id;
            public String name;
            public String path;
            public int order_num;
        }
    }

    @Override
    public String toString() {
        return GSON.toJson(this);
    }
}
