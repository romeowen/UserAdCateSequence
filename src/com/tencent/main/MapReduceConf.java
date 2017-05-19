package com.tencent.main;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import com.tencent.main.FirstMapValueTuples;
import com.tencent.main.FirstReduce;

public class MapReduceConf {
	
	private Configuration conf =new Configuration();
	private ArrayList<String> args = new ArrayList<String>() ;
	private int NumReduce = 100;
	
	public  MapReduceConf(String [] args, Configuration conf )  throws Exception {
		
		this.conf = conf;	
		for(String arg: args) {
			this.args.add(arg);
		}
		// get the last day
		String date_time = null;
		Date dNow = new Date();
		Date dBefore = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dNow);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		dBefore = calendar.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");  
		date_time = sdf.format(dBefore);
		
		String Separator = this.args.get(2);
		String Dimension = this.args.get(3);
		if (this.args.size() == 5)
			date_time = this.args.get(4);
		else if (this.args.size() == 6) {
			date_time = this.args.get(4);
			NumReduce = Integer.parseInt(this.args.get(5));			
		}
			
		conf.set("separator", Separator);
		conf.set("dimension", Dimension);
		conf.set("datetime", date_time);
	}	

	
	public void FirstMapReduceConf() throws Exception {
		
	     	Job job1 = new Job(conf,"Job1");
			job1.setJarByClass(SeqSort.class);		
			job1.setMapperClass(FirstMap.class);				
			job1.setReducerClass(FirstReduce.class);			
			job1.setOutputKeyClass(Text.class);
			job1.setOutputValueClass(FirstMapValueTuples.class);
			job1.setNumReduceTasks(NumReduce);
			// the location of data sets
			String InputPath [] = args.get(0).split(",");
		    for(int i=0; i< InputPath.length ; i++ ) {	
				FileInputFormat.addInputPath(job1, new Path( InputPath[i]));			
		    }				
			FileOutputFormat.setOutputPath(job1,new Path(args.get(1)));
			job1.waitForCompletion(true);	
	}
	


}

