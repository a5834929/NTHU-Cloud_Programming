package step1;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

public class BuildGraphMapper extends MapReduceBase
	implements Mapper<LongWritable, Text, Text, Links> {

	public void map(LongWritable key, Text value, OutputCollector<Text, Links> output, 
		Reporter reporter) throws IOException{
		
			String line = value.toString();
			String[] tokens = line.split("\\t");
			Links edge = new Links();
			if(tokens.length==2){
				edge.addLink(tokens[0]);
				output.collect(new Text(tokens[1]), edge);
			}else
				output.collect(new Text(tokens[0]), edge);
	}
}

