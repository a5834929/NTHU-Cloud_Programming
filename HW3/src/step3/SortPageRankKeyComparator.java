package step3;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

public class SortPageRankKeyComparator extends WritableComparator {
	
	public SortPageRankKeyComparator() {
		super(DoubleWritable.class, true);
	}

	public int compare(WritableComparable o1, WritableComparable o2) {
		DoubleWritable key1 = (DoubleWritable)o1;
		DoubleWritable key2 = (DoubleWritable)o2;
		
		if(key1.compareTo(key2)<0)		return 1;
		else if(key1.compareTo(key2)>0)	return -1;
		else							return key1.compareTo(key2);
	}
}
