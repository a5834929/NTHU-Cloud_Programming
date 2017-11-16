package part1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;

public class BuildTableCombiner extends MapReduceBase
	implements Reducer<Text, TableEntry, Text, TableEntry> {
		
	public void reduce(Text key, Iterator<TableEntry> values,
		OutputCollector<Text, TableEntry> output, Reporter reporter) throws IOException {	
		
		ArrayList<WordEntry> entries = new ArrayList<WordEntry>();
		while (values.hasNext()){
			ArrayList<WordEntry> tmp = values.next().getWordEntry();
			WordEntry entry = 
					new WordEntry(tmp.get(0).getFileName(), tmp.get(0).getOffsets(), tmp.get(0).getTF());
			entries.add(entry);
		}
		
		TableEntry table = new TableEntry(entries);
		output.collect(key, table);
	}
}
