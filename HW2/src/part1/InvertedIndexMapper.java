package part1;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapred.FileSplit;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

public class InvertedIndexMapper extends MapReduceBase
	implements Mapper<LongWritable, Text, WordFilePair, WordEntry> {

	public void map(LongWritable key, Text value, OutputCollector<WordFilePair, WordEntry> output, 
		Reporter reporter) throws IOException{
			FileSplit fs = (FileSplit)reporter.getInputSplit();
			String fileName = fs.getPath().getName();
			
			String line = value.toString();
			Pattern pattern = Pattern.compile("[a-zA-Z]+");
			Matcher matcher = pattern.matcher(line);
			
			while(matcher.find()){
				long offset = key.get() + (long)matcher.start();
				WordFilePair keyPair = new WordFilePair(fileName, matcher.group());
				WordEntry entry = new WordEntry();
				entry.addOffset(offset);
				
				reporter.incrCounter("fileName", fileName, 1);
				output.collect(keyPair, entry);
			}
	}
}

