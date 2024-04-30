package com.meng.api.semiee;

import com.meng.api.semiee.ChipArticle;
import com.meng.api.semiee.ChipDescription;
import com.meng.api.semiee.ChipInformation;
import com.meng.api.semiee.ChipParameter;
import com.meng.api.semiee.ContactWay;
import com.meng.api.semiee.SearchResult;
import com.meng.eleTool.tools.GSON;
import com.meng.eleTool.tools.Network;

public class SemieeApi {

    public static final String API_SERVER = "https://www.semiee.com/bdxx-api/chip";

    public static SearchResult search(String keyword) {
        return search(keyword, 0, 10);  
    }

    public static SearchResult search(String keyword, int index, int pageSize) {
        return GSON.fromJson(Network.httpGet(API_SERVER + "/search?pageIndex=" + index + "&pageSize=" + pageSize + "&model=" + keyword), SearchResult.class);
    }

    public static ChipArticle getChipArticle(String chipId) {
        return GSON.fromJson(Network.httpGet(API_SERVER + "/" + chipId + "/techniclarticle"), ChipArticle.class);
    }

    public static ChipDescription getChipDescription(String chipId) {
        return GSON.fromJson(Network.httpGet(API_SERVER + "/detail/" + chipId), ChipDescription.class);
    }

    public static ChipInformation getChipInformation(String chipId) {
        return GSON.fromJson(Network.httpGet(API_SERVER + "/" + chipId + "/encyclopedias"), ChipInformation.class);
    }

    public static ChipParameter getChipParameter(String chipId) {
        return GSON.fromJson(Network.httpGet(API_SERVER + "/" + chipId + "/technicalparam"), ChipParameter.class);
    }

    public static ContactWay getContactWay(String chipId) {
        return GSON.fromJson(Network.httpGet(API_SERVER + "/v2/" + chipId + "/contactWay"), ContactWay.class);
    }
}
