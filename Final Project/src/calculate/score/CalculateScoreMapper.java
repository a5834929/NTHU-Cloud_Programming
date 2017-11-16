package calculate.score;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

public class CalculateScoreMapper extends MapReduceBase
	implements Mapper<LongWritable, Text, Text, FileData> {
	
	private static Configuration conf = null;
	private static HTable table = null;

	static {
		conf = HBaseConfiguration.create();
		try {
			table = new HTable(conf, "WordScore");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String getRecord(String rowKey) throws Exception {
		Get get = new Get(rowKey.getBytes());
        Result rs = table.get(get);
        for (KeyValue kv : rs.raw()) {
			return new String(kv.getValue());
		}
        return "";
	}

	public void map(LongWritable key, Text value, OutputCollector<Text, FileData> output, 
		Reporter reporter) throws IOException{

			String line = value.toString();
			String[] tokens = line.split("\\t");
			String fileName = tokens[0];
			String word = tokens[1];
			
			try {
				String record = getRecord(word.toLowerCase());
				if(!record.equals("")){
					FileData file = new FileData();
					Double score = Double.valueOf(record);
					file.setScore(score);
					if(score.compareTo(8.0)<0)
						file.setEasy(1);
					else if(score.compareTo(10.0)>=0)
						file.setHard(1);
					output.collect(new Text(fileName), file);
				}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
}

