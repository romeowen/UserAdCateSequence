package com.tencent.main;

import java.util.Comparator;


public class ValueComparator implements Comparator<SortClass>{
	
	public int compare (SortClass A, SortClass B) {
		double diff=A.getValue()-B.getValue();
		if (diff>0)
		   return   1;
	    if (diff<0)
		   return  -1;
	    else
	       return   0;
	}
	
	
	

}
