package com.tencent.main;

public class SortClass {
	
	private long log_time =0;
	private String trace_id = null;
	private int cate_1_id = 0;
	private int cate_2_id = 0;
	
	public  SortClass(FirstMapValueTuples value) {		

		this.log_time=value.log_time;
		this.trace_id = value.trace_id;
		this.cate_1_id = value.cate_1_id;
		this.cate_2_id = value.cate_2_id;
	}
	
	public long getValue() {
		return this.log_time;
	}
	
	public int getCate1d() {
		return this.cate_1_id;
	}
	
	public String getTrace() {
		return this.trace_id;
	}
	
	public int getCate2d() {
		return this.cate_2_id;
	}
	
	public String getCateList() {
		return this.cate_1_id + ":" + this.cate_2_id;
	}


}
