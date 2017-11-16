package step2;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;

public class PageRankCombiner extends MapReduceBase
	implements Reducer<Text, Links, Text, Links> {
		
	public void reduce(Text key, Iterator<Links> values,
		OutputCollector<Text, Links> output, Reporter reporter) throws IOException {	
		
		double rank = 0.0;
		
		while(values.hasNext()){
			Links tmp = values.next();
			Double tmpRank = tmp.getRank();
			
			if(tmpRank.compareTo(-1.0)==0){
				Links links = new Links(-1, tmp.getLinks());
				output.collect(key, links);
			}
			else
				rank += tmpRank;
		}
		Links outLinks = new Links();
		outLinks.setRank(rank);
		output.collect(key, outLinks);
	}
}
