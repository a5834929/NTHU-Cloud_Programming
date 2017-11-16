package parse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;


public class ParseCombiner extends MapReduceBase
	implements Reducer<Text, WordArray, Text, WordArray> {
		
	public void reduce(Text key, Iterator<WordArray> values,
		OutputCollector<Text, WordArray> output, Reporter reporter) throws IOException {	
		
		ArrayList<Word> wArray = new ArrayList<Word>();
		while(values.hasNext()){
			WordArray array = values.next();
			ArrayList<Word> tmp = array.getArticle(); 
			wArray.addAll(tmp);
		}
		Collections.sort(wArray, new Comparator<Word>(){
	        @Override
	        public int compare(Word word1, Word word2){
	        	Long o1 = word1.getOffset();
	        	Long o2 = word2.getOffset();
	            return  o1.compareTo(o2);
	        }
	    });
		
		output.collect(key, new WordArray(wArray));
	}
}
