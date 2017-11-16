package step5;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.Partitioner;

public class BuildTablePartitioner implements Partitioner<Text, TableEntry> {
	
	public void configure(JobConf job) {
	}

	public int getPartition(Text key, TableEntry value, int numPartitions) {
		String s = key.toString().substring(0, 1);
		
		if(s.compareToIgnoreCase("g")<=0)
			return 0;
		else
			return 1;
	}
}
