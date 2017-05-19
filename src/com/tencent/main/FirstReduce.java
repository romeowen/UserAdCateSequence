package com.tencent.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import com.tencent.main.SortClass;
import com.tencent.main.ValueComparator;

public class FirstReduce extends Reducer<Text, FirstMapValueTuples, Text, Text> {

	public void reduce(Text key, Iterable<FirstMapValueTuples> value,
			Context context) throws IOException, InterruptedException {

		String separator = context.getConfiguration().get("separator");
		String date_time = context.getConfiguration().get("datetime");
		
		ArrayList<SortClass> SortComb = new ArrayList<SortClass>();
		int NumValue = 0;
		for (FirstMapValueTuples val : value) {
			SortClass tmp_val = new SortClass(val);
			SortComb.add(tmp_val);
			NumValue += 1;
		}
		// sorting
		Collections.sort(SortComb, new ValueComparator());
		String sequence = "";
		String TemTrace = "";
		ArrayList<String> TraceList = new ArrayList<String>();
		for (int Numpair = 0; Numpair < NumValue; Numpair++) {		
			TemTrace = SortComb.get(Numpair).getTrace();
			if (!TraceList.contains(TemTrace)) {
				sequence += SortComb.get(Numpair).getCateList() + separator;				
				TraceList.add(SortComb.get(Numpair).getTrace());
			}
		}
		if (sequence.endsWith(separator))
			sequence = sequence.substring(0, sequence.length()-1);
		Text NewKey = new Text(date_time + "\t" + key.toString());
		context.write(NewKey, new Text(sequence));
	}
}