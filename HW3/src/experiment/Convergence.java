package experiment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class Convergence {
	public static void main(String[] args){
		Convergence aConvergence = new Convergence();
		aConvergence.converge(args[0]); //args[0] = HW3/iteration
	}
	
	public void converge(String inputPath){
		try {
			FileSystem fs;
			fs = FileSystem.get(new Configuration());
			ArrayList<Double> distance = new ArrayList<Double>();
			for(int i=0;i<9;i++){
				System.out.println("calculating "+i+" and "+(i+1));
				FileStatus[] status1 = fs.listStatus(new Path(inputPath+"_"+i));
				FileStatus[] status2 = fs.listStatus(new Path(inputPath+"_"+(i+1)));
				Pattern pattern = Pattern.compile("part.*");
				Double rank = 0.0;
				for(int j=0;j<status1.length;j++){
					Path path1 = status1[j].getPath();
					Path path2 = status2[j].getPath();
					Matcher matcher1 = pattern.matcher(path1.toString());
					Matcher matcher2 = pattern.matcher(path2.toString());
	                if(matcher1.find() && matcher2.find()){
	                	BufferedReader br1 = new BufferedReader(new InputStreamReader(fs.open(path1)));
	                	BufferedReader br2 = new BufferedReader(new InputStreamReader(fs.open(path2)));
	                    String line1 = br1.readLine();
	                    String line2 = br2.readLine();
	                    while(line1!=null && line2!=null){
	                    	Double rank1 = Double.valueOf(line1.split("\\t")[1].split("\\]")[0]);
	                    	Double rank2 = Double.valueOf(line2.split("\\t")[1].split("\\]")[0]);
	                    	rank += Math.abs(rank1-rank2);
	                    	line1 = br1.readLine();
	                    	line2 = br2.readLine();
	                    }
	                }
				}
				distance.add(rank);
			}
			for(Double d:distance)
				System.out.println(d);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	public Double calculateDist(ArrayList<Double>list1, ArrayList<Double>list2){
		Double dist = 0.0;
		for(int i=0;i<list1.size();i++)
			dist += Math.abs(list1.get(i)-list2.get(i));
		return dist;
	}
}
