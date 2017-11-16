package step5;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;

public class InvertedIndexCombiner extends MapReduceBase
	implements Reducer<WordPagePair, IntWritable, WordPagePair, IntWritable> {
		
	public void reduce(WordPagePair key, Iterator<IntWritable> values,
		OutputCollector<WordPagePair, IntWritable> output, Reporter reporter) throws IOException {	
				
		while (values.hasNext()) {
			values.next();
		}
		
		output.collect(key, new IntWritable(1));
	}
}
