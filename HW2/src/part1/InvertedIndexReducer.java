package part1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.hadoop.mapred.Counters.Group;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.JobID;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.mapred.RunningJob;

public class InvertedIndexReducer extends MapReduceBase
	implements Reducer<WordFilePair, WordEntry, WordFilePair, WordEntry> {
	
	private Group counterGroup;

	@Override
	public void configure(JobConf conf) {
	    JobClient client;
		try {
			client = new JobClient(conf);
		    RunningJob parentJob = client.getJob(JobID.forName( conf.get("mapred.job.id") ));
			counterGroup = parentJob.getCounters().getGroup("fileName");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
		
	public void reduce(WordFilePair key, Iterator<WordEntry> values,
		OutputCollector<WordFilePair, WordEntry> output, Reporter reporter) throws IOException {	
		
		WordEntry entry = new WordEntry();		
		while (values.hasNext()) {
			ArrayList<Long> offset = values.next().getOffsets();
			for(int i=0;i<offset.size();i++)
				entry.addOffset(offset.get(i));
		}
		
		long count = counterGroup.getCounter(key.getFileName());
		entry.calculateTF(count);
		entry.sortOffsets();
		output.collect(key, entry);
	}
}
