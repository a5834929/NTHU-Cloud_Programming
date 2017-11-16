package step5;

import java.io.IOException;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;

public class InvertedIndex {
	public static void main(String[] args) throws Exception {
		InvertedIndex aInvertedIndex = new InvertedIndex();
		aInvertedIndex.phase1(args[0], args[1]);
		aInvertedIndex.phase2(args[1], args[2]);
	}
	
	public void phase1(String inputPath, String outputPath) throws IOException {

		JobConf conf = new JobConf(InvertedIndex.class);
		conf.setJobName("inverted index");

		conf.setMapperClass(InvertedIndexMapper.class);
		conf.setReducerClass(InvertedIndexReducer.class);

		conf.setMapOutputKeyClass(WordPagePair.class);
		conf.setMapOutputValueClass(IntWritable.class);

		conf.setOutputKeyClass(Text.class);
		conf.setOutputValueClass(Text.class);

		conf.setPartitionerClass(InvertedIndexPartitioner.class);
		conf.setCombinerClass(InvertedIndexCombiner.class);

		FileInputFormat.addInputPath(conf, new Path(inputPath));
		FileOutputFormat.setOutputPath(conf, new Path(outputPath));

		conf.setNumMapTasks(3);
		conf.setNumReduceTasks(2);

		JobClient.runJob(conf);
	}
	
	public void phase2(String inputPath, String outputPath) throws IOException {

		JobConf conf = new JobConf(InvertedIndex.class);
		conf.setJobName("build table");

		conf.setMapperClass(BuildTableMapper.class);
		conf.setReducerClass(BuildTableReducer.class);

		conf.setMapOutputKeyClass(Text.class);
		conf.setMapOutputValueClass(TableEntry.class);

		conf.setOutputKeyClass(Text.class);
		conf.setOutputValueClass(TableEntry.class);

		conf.setPartitionerClass(BuildTablePartitioner.class);
		conf.setCombinerClass(BuildTableCombiner.class);

		FileInputFormat.addInputPath(conf, new Path(inputPath));
		FileOutputFormat.setOutputPath(conf, new Path(outputPath));

		conf.setNumMapTasks(3);
		conf.setNumReduceTasks(2);

		JobClient.runJob(conf);
	}

}
