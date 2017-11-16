package calculate.score;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;

public class WordCountReducer extends MapReduceBase
	implements Reducer<FileWordPair, IntWritable, Text, Text> {
	
	public void reduce(FileWordPair key, Iterator<IntWritable> values,
		OutputCollector<Text, Text> output, Reporter reporter) throws IOException {	

		while(values.hasNext()){
			values.next();
		}
		
		output.collect(new Text(key.getFile()), new Text(key.getWord()));
	}
}
