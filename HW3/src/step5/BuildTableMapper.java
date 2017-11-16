package step5;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

public class BuildTableMapper extends MapReduceBase
	implements Mapper<LongWritable, Text, Text, TableEntry> {

	public void map(LongWritable key, Text value, OutputCollector<Text, TableEntry> output, 
		Reporter reporter) throws IOException{
			String line = value.toString();
			String[] tokens = line.split("\t");
			
			String word = tokens[0];
			String title = tokens[1];
			TableEntry entry = new TableEntry();
			entry.addTitle(title);
			output.collect(new Text(word), entry);
	}
}

