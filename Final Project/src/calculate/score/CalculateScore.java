package calculate.score;

import java.io.IOException;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.TextInputFormat;
import org.apache.hadoop.mapred.lib.MultipleInputs;

public class CalculateScore {
	public static void main(String[] args) throws Exception {
		CalculateScore cal = new CalculateScore();
		cal.count(args[2], args[1]);
		cal.calculate(args[1], args[0]);
		/*cal.count(args);
		cal.calculate(args[1], args[0]);*/
	}
	
	public void count(String inputPath, String outputPath) throws IOException {

		JobConf conf = new JobConf(CalculateScore.class);
		conf.setJobName("word count");

		conf.setMapperClass(WordCountMapper.class);
		conf.setReducerClass(WordCountReducer.class);

		conf.setMapOutputKeyClass(FileWordPair.class);
		conf.setMapOutputValueClass(IntWritable.class);

		conf.setOutputKeyClass(Text.class);
		conf.setOutputValueClass(Text.class);

		//for(int i=2;i<paths.length;i++)
			//MultipleInputs.addInputPath(conf, new Path("s3://"+paths[i].substring(35)), TextInputFormat.class, WordCountMapper.class);
		FileInputFormat.addInputPath(conf, new Path(inputPath));
		FileOutputFormat.setOutputPath(conf, new Path(outputPath));
		
		conf.setNumMapTasks(3);
		conf.setNumReduceTasks(2);

		JobClient.runJob(conf);
	}
	
	public void calculate(String inputPath, String outputPath) throws IOException {

		JobConf conf = new JobConf(CalculateScore.class);
		conf.setJobName("calculate score");

		conf.setMapperClass(CalculateScoreMapper.class);
		conf.setReducerClass(CalculateScoreReducer.class);

		conf.setMapOutputKeyClass(Text.class);
		conf.setMapOutputValueClass(FileData.class);

		conf.setOutputKeyClass(Text.class);
		conf.setOutputValueClass(FileData.class);
		
		conf.setCombinerClass(CalculateScoreCombiner.class);

		FileInputFormat.addInputPath(conf, new Path(inputPath));
		FileOutputFormat.setOutputPath(conf, new Path(outputPath));

		conf.setNumMapTasks(3);
		conf.setNumReduceTasks(2);

		JobClient.runJob(conf);
	}
	
}
