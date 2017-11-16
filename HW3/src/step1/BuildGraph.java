package step1;

import java.io.IOException;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;

import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;

public class BuildGraph {
	public static void main(String[] args) throws Exception {
		BuildGraph aBuildGraph = new BuildGraph();
		aBuildGraph.filterLinks(args[0], args[1]); 
		aBuildGraph.buildGraph(args[1], args[2]);
	}
	
	public void filterLinks(String inputPath, String outputPath) throws IOException {

		JobConf conf = new JobConf(BuildGraph.class);
		conf.setJobName("remove nonexisting links");

		conf.setMapperClass(FilterLinksMapper.class);
		conf.setReducerClass(FilterLinksReducer.class);

		conf.setMapOutputKeyClass(Text.class);
		conf.setMapOutputValueClass(Links.class);

		conf.setOutputKeyClass(Text.class);
		conf.setOutputValueClass(Text.class);

		conf.setCombinerClass(FilterLinksCombiner.class);
		conf.setPartitionerClass(FilterLinksPartitioner.class);

		FileInputFormat.addInputPath(conf, new Path(inputPath));
		FileOutputFormat.setOutputPath(conf, new Path(outputPath));

		conf.setNumMapTasks(4);
		conf.setNumReduceTasks(3);

		JobClient.runJob(conf);
	}
	
	public void buildGraph(String inputPath, String outputPath) throws IOException {

		JobConf conf = new JobConf(BuildGraph.class);
		conf.setJobName("build graph");

		conf.setMapperClass(BuildGraphMapper.class);
		conf.setReducerClass(BuildGraphReducer.class);

		conf.setMapOutputKeyClass(Text.class);
		conf.setMapOutputValueClass(Links.class);

		conf.setOutputKeyClass(Text.class);
		conf.setOutputValueClass(Links.class);

		conf.setCombinerClass(BuildGraphCombiner.class);
		conf.setPartitionerClass(BuildGraphPartitioner.class);

		FileInputFormat.addInputPath(conf, new Path(inputPath));
		FileOutputFormat.setOutputPath(conf, new Path(outputPath));

		conf.setNumMapTasks(4);
		conf.setNumReduceTasks(3);

		JobClient.runJob(conf);
	}
	
}
