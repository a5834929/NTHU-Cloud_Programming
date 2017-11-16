package parse;

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

public class ParseMapper extends MapReduceBase
	implements Mapper<LongWritable, Text, Text, WordArray> {

	public void map(LongWritable key, Text value, OutputCollector<Text, WordArray> output, 
		Reporter reporter) throws IOException{
			FileSplit fs = (FileSplit)reporter.getInputSplit();
			String fileName = fs.getPath().getName();
			String line = value.toString();
			
			Pattern pattern = Pattern.compile("<p>(.*?)</p>");
			Matcher matcher = pattern.matcher(line);
			
			while(matcher.find()){
				long offset = key.get() + (long)matcher.start();
				String sentence = matcher.group(1);
				String words[] = sentence.split(" ");
				for(int i=0;i<words.length;i++){
					StringBuilder builder = new StringBuilder();
					if(i==0)
						builder.append("<p>\n");
					builder.append("<span>"+words[i]+"</span>");
					if(i==words.length-1)
						builder.append("</p>");
					builder.append("\n");
					Word aWord = new Word(builder.toString(), offset);
					WordArray wArray = new WordArray();
					wArray.addWord(aWord);
					output.collect(new Text(fileName), wArray);
				}
			}
	}
}

