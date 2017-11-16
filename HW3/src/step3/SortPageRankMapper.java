package step3;

import java.io.IOException;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

public class SortPageRankMapper extends MapReduceBase
	implements Mapper<LongWritable, Text, DoubleWritable, Text> {

	public void map(LongWritable key, Text value, OutputCollector<DoubleWritable, Text> output, 
		Reporter reporter) throws IOException{
		
			String line = value.toString();
			String title = line.split("\\t")[0];
			String rank = line.split("\\t")[1].split("\\]")[0].toString();
			output.collect(new DoubleWritable(Double.valueOf(rank)), new Text(title));
	}
}

