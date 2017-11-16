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

public class FilterLinksReducer extends MapReduceBase
	implements Reducer<Text, Links, Text, Text> {
		
	public void reduce(Text key, Iterator<Links> values,
		OutputCollector<Text, Text> output, Reporter reporter) throws IOException {	
		
		int exist = 0;
		ArrayList<String> totalTitles = new ArrayList<String>();
		while(values.hasNext()){
			Links edge = values.next();
			ArrayList<String> titles = edge.getLinks();
			for(String title:titles){
				if(title.equals(" "))
					exist = 1;
				else
					totalTitles.add(title);
			}
		}
		
		if(exist==1){
			Collections.sort(totalTitles);
			output.collect(key, new Text("")); // output: title
			for(String title:totalTitles)
				output.collect(key, new Text(title)); //output: link title
		}
	}
}
