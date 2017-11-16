package part1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;

public class InvertedIndexCombiner extends MapReduceBase
	implements Reducer<WordFilePair, WordEntry, WordFilePair, WordEntry> {
		
	public void reduce(WordFilePair key, Iterator<WordEntry> values,
		OutputCollector<WordFilePair, WordEntry> output, Reporter reporter) throws IOException {	
		
		WordEntry entry = new WordEntry();		
		while (values.hasNext()) {
			ArrayList<Long> offset = values.next().getOffsets();
			for(int i=0;i<offset.size();i++)
				entry.addOffset(offset.get(i));
		}
		
		output.collect(key, entry);
	}
}
