package com.tencent.main;

import java.io.IOException;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import com.tencent.main.FirstMapValueTuples;

public class FirstMap extends Mapper<Object, Text, Text, FirstMapValueTuples> {

	
	private FirstMapValueTuples assemble_value = new FirstMapValueTuples();

	public void map(Object key, Text value, Context context)
			throws IOException, InterruptedException {

		setup(context);
		String[] TempValue = value.toString().split("\001", -1);
		String dimension = context.getConfiguration().get("dimension");

		if (TempValue.length != Integer.parseInt(dimension))
			System.out.println("list length is not sufficient.");
		else {
			//int date_time = Integer.parseInt(TempValue[0]);
			String wuid = TempValue[1].trim();		
			//int label = Integer.parseInt(TempValue[2].trim());
			//long log_time_1 = Long.parseLong(TempValue[3]);
			String trace_id = TempValue[4];
			int cate_1_id = Integer.parseInt(TempValue[5]);
			int cate_2_id= Integer.parseInt(TempValue[6]);

		    long log_time = new BigInteger(TempValue[3]).longValue() * 1000;
		    Date date = new Date(log_time);
		    SimpleDateFormat TimeFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		    log_time = Long.parseLong(TimeFormat.format(date));
			
			assemble_value.trace_id = trace_id;
			assemble_value.log_time = log_time;
			assemble_value.cate_1_id = cate_1_id;
			assemble_value.cate_2_id = cate_2_id;
			Text Newkey = new Text(wuid);			
			context.write(Newkey, assemble_value);
		}

	}

}