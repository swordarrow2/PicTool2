package com.meng.api.semiee;

import com.meng.eleTool.tools.GSON;
import java.util.ArrayList;

public class ChipArticle {

    public int code;
    public Object remark;
    public Result result;

    public class Result {

        public String id;
        public int fkTBrandId;
        public String model;
        public String name;
        public String descr;
        public String feature;
        public String used;
        public String manufacturerContactWay;
        public String agentContactWay;
        public Object semieeContactWay;
        public String manufacturerUrl;
        public Object agentUrl;
        public Object semieeUrl;
        public Object terminalUsed;
        public String updateTime;
        public String brandName;
        public int brandIsVip;
        public int isCollect;
        public Object email;
        public String shareUrl;
        public Object dsFile;
        public Object params;
        public Object fnFile;
        public Object smFile;
        public Object chipLinks;
        public ArrayList<UseFile> useFiles;
        public ArrayList<Object> relatedDataFiles;
        public int paramNum;
        public int fileNum;
        public int linkNum;
        public int funcFileNum;
        public int referencePriceNum;
        public Object items;
        public Object chipReferencePrice;
        public int similarNum;
        public Object sameBrandSimilarChips;
        public Object differenceBrandSimilarChips;
        public int choiceSimilarNum;
        public Object sameBrandChoiceSimilarChips;
        public Object internalBrandChoiceSimilarChips;
        public Object abroadBrandChoiceSimilarChips;
        public int devSuiteNum;
        public int appNum;
        public int schemaNum;
        public int childmodelNum;
        public Object childModels;
        public Object sampleApplyNum;
        public Object contactWayNum;
        
        public class UseFile {
            public int id;
            public String name;
            public String path;
            public String fileType;
            public String fileSize;
        }
    }

    @Override
    public String toString() {
        return GSON.toJson(this);
    }
}
