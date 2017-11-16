package step2;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

public class PageRankMapper extends MapReduceBase
	implements Mapper<LongWritable, Text, Text, Links> {

	public void map(LongWritable key, Text value, OutputCollector<Text, Links> output, 
		Reporter reporter) throws IOException{
		
			String line = value.toString();
			String title = line.split("\\t")[0];
			reporter.incrCounter("PageRank", "titleNum", 1);
			
			String[] elements = line.split("\\t")[1].split("\\]");
			double rank = Double.valueOf(elements[0]);
			
			ArrayList<String> links = new ArrayList<String>();
			for(int i=1;i<elements.length;i++)
				links.add(elements[i]);
			
			Links titleLinks = new Links(-1, links);
			output.collect(new Text(title), titleLinks);
			
			int outDeg = links.size();
			if(outDeg==0)
				reporter.incrCounter("PageRank", "danglingRank", (long)(rank*1E10));
			else{
				for(String link:links){
					Links outLinks = new Links();
					outLinks.setRank(rank/outDeg);
					output.collect(new Text(link), outLinks);
				}
			}
	}
}

