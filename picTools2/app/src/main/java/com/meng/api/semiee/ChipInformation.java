package com.meng.api.semiee;

import com.meng.tools.*;

import java.util.*;

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

//    {
//        "code":0,
//        "result":{
//            "agent_contact_way":"",
//            "appNum":7,
//            "brand_is_vip":1,
//            "brand_name":"南芯-SouthChip",
//            "childmodel_num":0,
//            "chipLinks":[
//                        {"fk_t_chip_id":"8c16d867-cf5b-4442-8fc2-773f03fb02e8",
//                         "id":461,
//                         "name":"南芯发布SC8801 SC8802双向升降压充放电控制器",
//                         "order_num":1,
//                         "path":"http://www.chongdiantou.com/wp/archives/2348.html"},
//                        {"fk_t_chip_id":"8c16d867-cf5b-4442-8fc2-773f03fb02e8",
//                         "id":462,
//                         "name":"我们拆解了华为用料最好的快充移动电源！",
//                         "order_num":2,
//                         "path":"http://www.sohu.com/a/158388003_296845"},
//                        {"fk_t_chip_id":"8c16d867-cf5b-4442-8fc2-773f03fb02e8",
//                         "id":463,
//                         "name":"雷军首次布局电源管理芯片：创业公司获千万融资",
//                         "order_num":3,
//                         "path":"http://news.mydrivers.com/1/566/566064.htm"},
//                        {"fk_t_chip_id":"8c16d867-cf5b-4442-8fc2-773f03fb02e8",
//                         "id":464,
//                         "name":"36氪首发 | 给电子产品一颗高性能“心脏”，电源管理方案商南芯半导体完成数千万人民币A轮融资",
//                         "order_num":4,
//                         "path":"https://36kr.com/p/5069322.html"}
//                         ],
//             "choice_similar_num":30,
//             "descr":"SC8802是一个同步4管双向升降压充放电控制器。不管输入电压是低于，高于或者等于电池组电压，它都可以对电池组实现充电管理，包括涓流，恒流，恒压阶段和满充指示。当系统需要从电池组放电时，SC8802能够反向输出所需电压，可输出低于，高于或者等于电池组电压值。SC8802拥有超宽范围输入输出电压。它可支持从2.7V到30V的应用范围，满足客户从1节到6节锂电池的不同需求。SC8802同时采用业界领先的10V驱动器电压，充分利用外置功率管以达到最高的转换效率。SC8802采用电流模式控制升压，降压或者升降压，并可用外部电阻调节开关频率, 电池电压设置值以及输入输出限流值，最大限度地在满足不同应用需求的同时简化设计。SC8802支持双向输出，通过DIR管脚即可轻松控制工作方向。它同时支持包括输入限流，输出限流，动态输入功率调节，内部最高电流限流，输出过压保护，短路保护以及过温保护等一系列保护功能以确保系统能适应各种异常情况。SC8802采用32脚4x4 QFN 封装。SC8802拥有强悍的输出能力，可通过外围电路进行灵活的系统设计，提高系统的热性能。\n\n","devSuiteNum":0,"feature":"• 升降压充电管理控制（涓流，恒流CC，恒压CV和满充指示），可支持1至6节锂电池充电\n\n• 反向升降压放电输出\n\n• 反向放电输出电压可支持PWM信号动态调节\n\n• 支持输入输出端口分别限流（CC模式） ，电阻设置限流值\n\n• 输入/输出限流值可支持PWM信号动态调节\n\n• 超宽输入电压：2.7V ~ 36V （40V耐压）\n\n• 超宽反向输出电压：2.7V ~ 36V\n\n• 外置功率管，集成10V，2A功率管栅极驱动\n\n• 高效率升降压操作\n\n• 开关频率可调：200kHz至600kHz\n\n• 内置电感电流限流\n\n• 可调节输入输出电流限流，双边输出短路保护 ，欠压保护，过压保护，过温保护\n\n• 4x4QFN-32 封装","file_num":1,"fk_t_brand_id":720,"func_file_num":1,"id":"8c16d867-cf5b-4442-8fc2-773f03fb02e8","is_collect":0,"link_num":4,"manufacturer_contact_way":"上海南芯半导体科技有限公司\r\n地址：上海市浦东新区博霞路22号\r\n电话：021-58309616\r\n邮箱：sales@southchip.com",
//             "manufacturer_url":"http://www.southchip.com/",
//             "model":"SC8802",
//             "name":"40V 双向升降压充电器 ",
//             "param_num":10,
//             "reference_price_num":0,
//             "schemaNum":0,
//             "shareUrl":"https://www.semiee.com/bdxx-api/chip/share/8c16d867-cf5b-4442-8fc2-773f03fb02e8",
//             "similar_num":4,
//             "update_time":"2023-02-07 13:40:39",
//             "used":"• 移动电源\n\n• USB Hub\n\n• 智能USB插座\n\n• USB电源传输\n\n• 车充\n\n• 工业应用"
//             }
//       }
}
