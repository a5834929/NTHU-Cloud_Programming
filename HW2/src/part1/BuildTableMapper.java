package part1;

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

public class BuildTableMapper extends MapReduceBase
	implements Mapper<LongWritable, Text, Text, TableEntry> {

	public void map(LongWritable key, Text value, OutputCollector<Text, TableEntry> output, 
		Reporter reporter) throws IOException{
			String line = value.toString();
			String[] tokens = line.split("\t");
			
			String word = tokens[0];
			String fileName = tokens[1];
			double tf = Double.parseDouble(tokens[2]);
			ArrayList<Long> offsets = new ArrayList<Long>();
			
			Pattern pattern = Pattern.compile("[0-9]+");
			Matcher matcher = pattern.matcher(tokens[3]);

			while(matcher.find())
				offsets.add(Long.parseLong(matcher.group()));
			
			WordEntry entry = new WordEntry(fileName, offsets, tf);
			ArrayList<WordEntry> entries = new ArrayList<WordEntry>();
			entries.add(entry);
			output.collect(new Text(word), new TableEntry(entries));
	}
}

