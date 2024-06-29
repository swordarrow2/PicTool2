package com.meng.api.semiee;

import com.meng.tools.*;

public class ChipDescription {

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
        public DsFile dsFile;
        public Object params;
        public Object fnFile;
        public Object smFile;
        public Object chipLinks;
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
        public int choiceSimilarNum;
        public Object sameBrandChoiceSimilarChips;
        public Object internalBrandChoiceSimilarChips;
        public Object abroadBrandChoiceSimilarChips;
        public int devSuiteNum;
        public int appNum;
        public int schemaNum;
        public int childmodel_num;
        public Object childModels;
        public int sample_apply_num;
        public int contactWay_num;

        public class DsFile {

            public int id;
            public String name;
            public String path;
            public String file_type;
            public String file_size;
        } 
    }

    @Override
    public String toString() {
        return GSON.toJson(this);
    }
}
