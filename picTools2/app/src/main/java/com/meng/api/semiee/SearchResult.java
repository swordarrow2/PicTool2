package com.meng.api.semiee;

import com.meng.tools.*;
import java.util.*;

public class SearchResult {
        
    public int code;
    public String remark;
    public List<Result> result;
    public int total;
    public int curPage;
    public int pageNum;
    public boolean isLastPage;

    public class Result {
        
        public String id;
        public int fk_t_brand_id;
        public String model;
        public String NAME;
        public String descr;
        public String feature;
        public String used;
        public String terminal_used;
        public String update_time;
        public String remark;
        public int state;
        public String brand_name;
        public int similar_num;
        public int choice_similar_num;
    }

    @Override
    public String toString() {
        return GSON.toJson(this);
    }

    
//     {
//     "code": 0,
//     "remark": null,
//     "result": [
//        {
//         "id": "8c16d867-cf5b-4442-8fc2-773f03fb02e8",
//         "fk_t_brand_id": 720,
//         "model": "SC8802",
//         "NAME": "",
//         "descr": "40V 双向升降压充电器 ",
//         "feature": "",
//         "used": "",
//         "terminal_used": "",
//         "update_time": "2021-01-14 12:55:52",
//         "remark": "",
//         "state": 1,
//         "brand_name": "南芯-SouthChip",
//         "similar_num": 4,
//         "choice_similar_num": 48 },
//       {
//         "id": "abb9cc2c-f444-4798-bcb0-548e2d439b6e",
//         "fk_t_brand_id": 720,
//         "model": "SC8802A",
//         "NAME": "",
//         "descr": "高效率, 同步, 4 管双向升降压充放电控制器",
//         "feature": "",
//         "used": "",
//         "terminal_used": "",
//         "update_time": "2021-05-12 16:51:35",
//         "remark": "ICSPEC",
//         "state": 1,
//         "brand_name": "南芯-SouthChip",
//         "similar_num": 0,
//         "choice_similar_num": 0
//         }],
//     "total": 2,
//     "curPage": 0,
//     "pageNum": 10,
//     "isLastPage": true
//     }
}
