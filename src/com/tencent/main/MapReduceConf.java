package com.tencent.main;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import com.tencent.main.FirstMapValueTuples;
import com.tencent.main.FirstReduce;

public class MapReduceConf {

	private Configuration conf = new Configuration();
	private ArrayList<String> args = new ArrayList<String>();
	private int NumReduce = 100;

	public MapReduceConf(String[] args, Configuration conf) throws Exception {

		this.conf = conf;
		for (String arg : args) {
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

		Job job1 = new Job(conf, "Job1");
		job1.setJarByClass(SeqSort.class);
		job1.setMapperClass(FirstMap.class);
		job1.setReducerClass(FirstReduce.class);
		job1.setOutputKeyClass(Text.class);
		job1.setOutputValueClass(FirstMapValueTuples.class);
		job1.setNumReduceTasks(NumReduce);
		// the location of data sets
		String[] InputPaths = args.get(0).split(",");
		if (InputPaths.length == 1) {
			String input_path = InputPaths[0];
			String[] path_part = input_path.split("/", -1);
			if (path_part[path_part.length - 1].startsWith("p_"))
				FileInputFormat.addInputPath(job1, new Path(input_path));
			else {
				// Create a configuration object for HADOOP.
				Configuration conf = new Configuration();
				// Get the FileSystem object.
				FileSystem fileSystem = FileSystem.get(new URI(input_path),
						conf);
				FileStatus[] day_dirs = fileSystem.listStatus(new Path(
						input_path));
				for (int i = 0; i < day_dirs.length; i++) {
					String[] path_block = day_dirs[i].toString().split("/", -1);
					if (day_dirs[i].isDir()
							&& path_block[path_block.length - 1]
									.startsWith("p_"))
						FileInputFormat.addInputPath(job1,
								day_dirs[i].getPath());
				}
			}
		} else {
			for (int i = 0; i < InputPaths.length; i++)
				FileInputFormat.addInputPath(job1, new Path(InputPaths[i]));
		}

		FileOutputFormat.setOutputPath(job1, new Path(args.get(1)));
		job1.waitForCompletion(true);
	}

}
