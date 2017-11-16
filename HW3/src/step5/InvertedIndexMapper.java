package step5;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

public class InvertedIndexMapper extends MapReduceBase
	implements Mapper<LongWritable, Text, WordPagePair, IntWritable> {

	public void map(LongWritable key, Text value, OutputCollector<WordPagePair, IntWritable> output, 
		Reporter reporter) throws IOException{
		
			String line = value.toString();
			Pattern pattern1 = Pattern.compile("<page>(.*?)</page>");
			Matcher matcher1 = pattern1.matcher(line);
			while(matcher1.find()){
				String page = matcher1.group(1);
				Pattern pattern2 = Pattern.compile("<title>(.*?)</title>");
				Matcher matcher2 = pattern2.matcher(page);
				String title;
				if(matcher2.find()){
					title = matcher2.group(1);
					Pattern pattern3 = Pattern.compile("<text.*>(.*?)</text>");
					Matcher matcher3 = pattern3.matcher(page);
					if(matcher3.find()){
						String text = matcher3.group(1);
						Pattern pattern4 = Pattern.compile("[a-zA-Z]+");
						Matcher matcher4 = pattern4.matcher(text);
						
						while(matcher4.find()){
							String word = matcher4.group();
							WordPagePair keyPair = new WordPagePair(word, title);
							output.collect(keyPair, new IntWritable(1));
						}
					}
				}
			}
	}
}

