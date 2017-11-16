package step5;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.Partitioner;

public class InvertedIndexPartitioner implements Partitioner<WordPagePair, IntWritable> {
	
	public void configure(JobConf job) {
	}

	public int getPartition(WordPagePair key, IntWritable value, int numPartitions) {
		String s = key.getWord().substring(0, 1);
		
		if(s.compareToIgnoreCase("g")<=0)
			return 0;
		else
			return 1;
	}
}
