package com.meng.eleTool.model;

import com.meng.eleTool.tools.GSON;
import java.io.Serializable;
import java.util.Objects;

public class Model implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1349503427482414965L;
	public int id;//该型号编号
    public String name; //型号
    public String pack;//封装
    public String type;//分类
    public String print;//丝印
    public String from;//来源
    public String note;//注释

    public Model(int id, String name, String pack, String type, String print, String from, String note) {
        this.id = id;
        this.name = name;
        this.pack = pack;
        this.type = type;
        this.print = print;
        this.from = from;
        this.note = note;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Model)) {
            return false;
        }
        Model c = (Model) obj;
        return c.id == id;
    }

    @Override
    public String toString() {
        return GSON.toJson(this);
    }

    public static enum Package {
        _SOP("小外形封装"),
        _SOJ("J型引脚小外形封装"),
        _TSOP("薄型小尺寸封装"),
        _VSOP("甚小外形封装"),
        _SSOP("缩小型SOP"),
        _TSSOP("薄型缩小型SOP"),
        _SOT("小外形晶体管"),
        _SOIC("小外形集成电路"),
        _DIP("双列直插式封装"),
        _PLCC("塑封J引线芯片封装"),
        _TQFP("薄塑封四角扁平封装"),
        _PQFP("塑封四角扁平封装"),
        _BGA("球栅阵列封装"),
        _TBGA("小型球栅阵列封装"),
        _QFP("小型方块平面封装"),
        _QFN("方形扁平无引脚封装"),
        _AXIAL,
        _TO,


        _1206,
        _0603,
        _0402,
        _0201,
        _1210,
        _1812,
        _2510,
        _2512;


        private String note = "";

        Package() {
        }

        Package(String note) {
            this.note = note;
        }

        public String getNote() {
            return note;
        }
    }
}
