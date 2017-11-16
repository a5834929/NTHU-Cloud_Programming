package calculate.score;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;

public class CalculateScoreReducer extends MapReduceBase
	implements Reducer<Text, FileData, Text, FileData> {
	
	public void reduce(Text key, Iterator<FileData> values,
		OutputCollector<Text, FileData> output, Reporter reporter) throws IOException {	

		double score = 0.0;
		int hard = 0, easy = 0;
		while(values.hasNext()){
			FileData file = values.next();
			score += file.getScore();
			hard += file.getHard();
			easy += file.getEasy();
		}
		
		FileData file = new FileData(score, hard, easy);
		output.collect(key, file);
	
	}
}
