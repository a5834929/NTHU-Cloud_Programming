package step5;

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
		
		ArrayList<String> titles = new ArrayList<String>();
		while (values.hasNext()){
			ArrayList<String> entries = values.next().getTitles();
			titles.addAll(entries);
		}
		
		TableEntry table = new TableEntry(titles);
		output.collect(key, table);
	}
}
