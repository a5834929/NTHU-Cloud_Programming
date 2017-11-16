package step2;

import java.io.IOException;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;

import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;

public class PageRank {
	public static void main(String[] args) throws Exception {
		PageRank aPageRank = new PageRank();
		String inputPath = args[0], outputPath;
		for(int i=0;i<2;i++){
			outputPath = args[1]+"_"+i;
			aPageRank.calculatePageRank(inputPath, outputPath);
			inputPath = outputPath;
		}
	}
	
	public void calculatePageRank(String inputPath, String outputPath) throws IOException {

		JobConf conf = new JobConf(PageRank.class);
		conf.setJobName("calculate page rank");

		conf.setMapperClass(PageRankMapper.class);
		conf.setReducerClass(PageRankReducer.class);

		conf.setMapOutputKeyClass(Text.class);
		conf.setMapOutputValueClass(Links.class);

		conf.setOutputKeyClass(Text.class);
		conf.setOutputValueClass(Links.class);

		conf.setCombinerClass(PageRankCombiner.class);
		conf.setPartitionerClass(PageRankPartitioner.class);

		FileInputFormat.addInputPath(conf, new Path(inputPath));
		FileOutputFormat.setOutputPath(conf, new Path(outputPath));

		conf.setNumMapTasks(8);
		conf.setNumReduceTasks(4);

		JobClient.runJob(conf);
	}
}
