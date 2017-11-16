package step3;

import java.io.IOException;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;

public class SortPageRank {
	public static void main(String[] args) throws Exception {
		SortPageRank aSortPageRank = new SortPageRank();
		aSortPageRank.sortPageRank(args[0], args[1]);
	}
	
	public void sortPageRank(String inputPath, String outputPath) throws IOException {

		JobConf conf = new JobConf(SortPageRank.class);
		conf.setJobName("sort by page rank");

		conf.setMapperClass(SortPageRankMapper.class);
		conf.setReducerClass(SortPageRankReducer.class);

		conf.setMapOutputKeyClass(DoubleWritable.class);
		conf.setMapOutputValueClass(Text.class);

		conf.setOutputKeyClass(Text.class);
		conf.setOutputValueClass(DoubleWritable.class);

		conf.setOutputKeyComparatorClass(SortPageRankKeyComparator.class);
		conf.setCombinerClass(SortPageRankCombiner.class);

		FileInputFormat.addInputPath(conf, new Path(inputPath));
		FileOutputFormat.setOutputPath(conf, new Path(outputPath));

		conf.setNumMapTasks(4);
		conf.setNumReduceTasks(1);

		JobClient.runJob(conf);
	}
}
