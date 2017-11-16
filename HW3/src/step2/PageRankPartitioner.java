package step2;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.Partitioner;

public class PageRankPartitioner implements Partitioner<Text, Links> {
	
	public void configure(JobConf job) {
	}

	public int getPartition(Text key, Links value, int numPartitions) {
		String s = key.toString().substring(0, 1);
		
		if(s.compareToIgnoreCase("g")<=0)
			return 0;
		else
			return 1;
	}
}
