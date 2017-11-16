package combine.score;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class ScoreCombiner {
	public static void main(String[] args){
		ScoreCombiner.combine();
	}
	
	public static void combine(){
		try {
			BufferedReader brScore = new BufferedReader(new FileReader("../score.txt"));
			HashMap<String, Double> scoreMap = new HashMap<String, Double>();
			String line = brScore.readLine();
			while(line!=null){
				scoreMap.put(line.split("\\t")[0], Double.valueOf(line.split("\\t")[1]));
				line = brScore.readLine();
			}
			brScore.close();
			BufferedReader brStop = new BufferedReader(new FileReader("../stopword.txt"));
			line = brStop.readLine();
			while(line!=null){
				scoreMap.put(line.split("\\t")[0], 0.0);
				line = brStop.readLine();
			}
			brStop.close();

			PrintWriter writer = new PrintWriter("../finalScore.txt", "UTF-8");
			for(Map.Entry<String, Double> entry:scoreMap.entrySet())
				writer.println(entry.getKey()+"\t"+entry.getValue());
			writer.close();
			System.out.println("done");
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
