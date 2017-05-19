package com.tencent.main;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;

 
public class FirstMapValueTuples implements Writable, WritableComparable<FirstMapValueTuples>{
	
    public String trace_id;	
    public long log_time;
    public int cate_1_id;
    public int cate_2_id;
	
	//@Override  copy is not a override method.
	public FirstMapValueTuples copy() {
		
		FirstMapValueTuples tuples = new FirstMapValueTuples();		
		tuples.trace_id = trace_id;
		tuples.log_time = log_time;
		tuples.cate_1_id = cate_1_id;
		tuples.cate_2_id = cate_2_id;

		return tuples;
	}
	
	//  the methods below are all belonged to WritableComparable. among the first two belong to writable,too.
	@Override
	public void write(DataOutput out) throws IOException {		
		out.writeUTF(trace_id);
		out.writeLong(log_time);
		out.writeInt(cate_1_id);
		out.writeInt(cate_2_id);
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		trace_id = in.readUTF();
		log_time = in.readLong();
		cate_1_id = in.readInt();
		cate_2_id = in.readInt();

	}
	
	@Override
	public int compareTo(FirstMapValueTuples arg0) {

		if (this.log_time > arg0.log_time) {
			return 1;
		} else if (this.log_time < arg0.log_time) {
			return -1;
		}		
		return 0;
	}
	
	@Override
	public int hashCode() {

		int result = 17;		
		result = 37 * result + (int) trace_id.hashCode();
		result = 37 * result + (int) log_time;
		long toint = 37 * result + (int) cate_1_id;
		result = 37 * result + (int) (toint ^ (toint >>> 32));
		return result;
	}

	@Override
	public String toString() {
		return trace_id + "," + log_time + "," + cate_1_id + "," + cate_2_id;
	}

	@Override
	public boolean equals(Object o) {

		if (!(o instanceof FirstMapValueTuples))
			return false;

		FirstMapValueTuples tuples = (FirstMapValueTuples) o;

		return 
				tuples.trace_id.equalsIgnoreCase(trace_id)		  
				&& tuples.log_time == log_time
				&& tuples.cate_1_id == cate_1_id
				&& tuples.cate_2_id == cate_2_id;


	}

}
	
