package com.meng.eleTool.model;

import java.io.Serializable;

public class Record extends BasePojo implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -3821036378993199808L;
	public int _id = -1;
    public int model_id;
    public String io;
    public int nums;
    public String date;

    public Record(int id, int model_id, String io, int nums, String date) {
        _id = id;
        this.model_id = model_id;
        this.io = io;
        this.nums = nums;
        this.date = date;
    }
}
