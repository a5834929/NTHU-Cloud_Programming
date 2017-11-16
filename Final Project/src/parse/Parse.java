package parse;

import java.io.IOException;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;


public class Parse {
	public static void main(String[] args) throws Exception {
		Parse aParse = new Parse();
		aParse.parse(args[0], args[1]); 
	}
	
	public void parse(String inputPath, String outputPath) throws IOException {

		JobConf conf = new JobConf(Parse.class);
		conf.setJobName("parse article");

		conf.setMapperClass(ParseMapper.class);
		conf.setReducerClass(ParseReducer.class);

		conf.setMapOutputKeyClass(Text.class);
		conf.setMapOutputValueClass(WordArray.class);

		conf.setOutputKeyClass(Text.class);
		conf.setOutputValueClass(WordArray.class);

		conf.setCombinerClass(ParseCombiner.class);

		FileInputFormat.addInputPath(conf, new Path(inputPath));
		FileOutputFormat.setOutputPath(conf, new Path(outputPath));

		conf.setNumMapTasks(3);
		conf.setNumReduceTasks(1);

		JobClient.runJob(conf);
	}
	
}
