package calculate.score;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

public class WordCountMapper extends MapReduceBase
	implements Mapper<LongWritable, Text, FileWordPair, IntWritable> {
	
	private static Configuration conf = null;
	private static HTable table = null;

	static {
		conf = HBaseConfiguration.create();
		try {
			table = new HTable(conf, "WordScore");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String getRecord(String rowKey) throws Exception {
		Get get = new Get(rowKey.getBytes());
        Result rs = table.get(get);
        for (KeyValue kv : rs.raw()) {
			return new String(kv.getValue());
		}
        return "";
	}

	public void map(LongWritable key, Text value, OutputCollector<FileWordPair, IntWritable> output, 
		Reporter reporter) throws IOException{
		
			FileSplit fs = (FileSplit)reporter.getInputSplit();
			String fileName = fs.getPath().getName();
		
			String line = value.toString();
			Pattern pattern = Pattern.compile("[a-zA-Z]+");
			Matcher matcher = pattern.matcher(line);
			
			try {
				while(matcher.find()){
					String word = matcher.group();
					if(Character.isUpperCase(word.charAt(0))){
						String record = getRecord(word.toLowerCase());
						if(!record.equals("")){
							FileWordPair pair = new FileWordPair(fileName, word);
							output.collect(pair, new IntWritable(1));
						}
					}else{
						FileWordPair pair = new FileWordPair(fileName, word);
						output.collect(pair, new IntWritable(1));
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
}

