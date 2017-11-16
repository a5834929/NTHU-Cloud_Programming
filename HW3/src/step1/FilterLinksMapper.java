package step1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

public class FilterLinksMapper extends MapReduceBase
	implements Mapper<LongWritable, Text, Text, Links> {

	public void map(LongWritable key, Text value, OutputCollector<Text, Links> output, 
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
					Pattern pattern3 = Pattern.compile("<text(.*?)</text>");
					Matcher matcher3 = pattern3.matcher(page);
					if(matcher3.find()){
						String text = matcher3.group(1);
						String link;
						Pattern pattern4 = Pattern.compile("\\[\\[(.*?)\\]\\]");
						Matcher matcher4 = pattern4.matcher(text);
						while(matcher4.find()){
							link = matcher4.group(1);
							ArrayList<String> titles = new ArrayList<String>();
							titles.add(title);
							output.collect(new Text(link), new Links(0, titles));
						}
						ArrayList<String> links = new ArrayList<String>();
						links.add(" ");
						output.collect(new Text(title), new Links(0, links));	
					}
				}
			}
	}
}

