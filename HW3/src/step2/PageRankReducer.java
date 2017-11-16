package step2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.JobID;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.mapred.RunningJob;
import org.apache.hadoop.mapred.Counters.Group;

public class PageRankReducer extends MapReduceBase
	implements Reducer<Text, Links, Text, Links> {
	
	private Group counterGroup;

	@Override
	public void configure(JobConf conf) {
	    JobClient client;
		try {
			client = new JobClient(conf);
		    RunningJob parentJob = client.getJob(JobID.forName( conf.get("mapred.job.id") ));
			counterGroup = parentJob.getCounters().getGroup("PageRank");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
		
	public void reduce(Text key, Iterator<Links> values,
		OutputCollector<Text, Links> output, Reporter reporter) throws IOException {	
		
		double alpha = 0.15;
		double rank = 0.0;
		ArrayList<String> links = new ArrayList<String>();
		while(values.hasNext()){
			Links tmp = values.next();
			Double tmpRank = tmp.getRank();
			
			if(tmpRank.compareTo(-1.0)==0)
				links.addAll(tmp.getLinks());
			else
				rank += tmpRank;
		}
		Collections.sort(links);
		long titleCount = counterGroup.getCounter("titleNum");
		double danglingRank = counterGroup.getCounter("danglingRank")/1E10;
		
		rank = alpha + (1-alpha)*(rank + danglingRank/titleCount); 
		Links outLinks = new Links(rank, links);
		output.collect(key, outLinks);
		
	}
}
