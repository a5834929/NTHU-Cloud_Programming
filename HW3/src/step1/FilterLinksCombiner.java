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

public class FilterLinksCombiner extends MapReduceBase
	implements Reducer<Text, Links, Text, Links> {
		
	public void reduce(Text key, Iterator<Links> values,
		OutputCollector<Text, Links> output, Reporter reporter) throws IOException {	
		
		ArrayList<String> totalTitles = new ArrayList<String>();
		while(values.hasNext()){
			Links edge = values.next();
			ArrayList<String> titles = edge.getLinks();
			totalTitles.addAll(titles);
		}
		
		Collections.sort(totalTitles);
		Links edge = new Links(1, totalTitles);
		output.collect(key, edge);
	}
}
