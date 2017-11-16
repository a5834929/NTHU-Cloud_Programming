package convert.score;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class ScoreConverter {

	public static void main(String[] args) throws Exception {
		ScoreConverter.convert();
	}
	
	public static void convert(){
		try {
			BufferedReader br = new BufferedReader(new FileReader("../frequency.txt"));
			PrintWriter writer = new PrintWriter("../score.txt", "UTF-8");
			String line;
			line = br.readLine();
			while(line!=null){
				String[] tokens = line.split("\\s");
				double score = 0.0;
				if(tokens.length>2)
					score = Math.log(1222421/Double.valueOf(tokens[3]));
				else{
					if(!tokens[1].equals("count"))
						score = Math.log(1222421/Double.valueOf(tokens[1]));
				}
				writer.println(tokens[0]+"\t"+score);
				line = br.readLine();
			}
			br.close();
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
