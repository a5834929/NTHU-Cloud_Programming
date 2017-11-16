package step5;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;

public class InvertedIndexReducer extends MapReduceBase
	implements Reducer<WordPagePair, IntWritable, Text, Text> {
		
	public void reduce(WordPagePair key, Iterator<IntWritable> values,
		OutputCollector<Text, Text> output, Reporter reporter) throws IOException {	
		
		while (values.hasNext()) {
			values.next();
		}
		
		output.collect(new Text(key.getWord()), new Text(key.getPage()));
	}
}
