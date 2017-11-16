package step1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;

public class BuildGraphReducer extends MapReduceBase
	implements Reducer<Text, Links, Text, Links> {
		
	public void reduce(Text key, Iterator<Links> values,
		OutputCollector<Text, Links> output, Reporter reporter) throws IOException {	
		
		ArrayList<String> totalLinks = new ArrayList<String>();
		while(values.hasNext()){
			Links edge = values.next();
			ArrayList<String> links = edge.getLinks();
			for(String link:links)
				totalLinks.add(link);
		}
		
		Collections.sort(totalLinks);
		Links edge = new Links(1,totalLinks);
		output.collect(key, edge);
	}
}
