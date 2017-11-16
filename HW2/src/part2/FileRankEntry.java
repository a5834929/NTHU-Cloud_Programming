package part2;

import java.util.ArrayList;
import java.util.HashMap;

public class FileRankEntry{
	private String fileName;
	private double weight;
	private HashMap<String, ArrayList<Long>> map;
	
	public FileRankEntry(String fileName){
		this.fileName = fileName;
		weight = 0.0;
		map = new HashMap<String, ArrayList<Long>>();
	}
	
	public String getFileName(){
		return fileName;
	}
	
	public void addWeight(double weight){
		this.weight += weight;
	}
	
	public void updateWeight(double weight){
		this.weight = Math.max(this.weight, weight);
	}
	
	public double getWeight(){
		return weight;
	}
	
	public void addOffsets(String word, ArrayList<Long> offsets){
		ArrayList<Long> offset = getOffsets(word);
		if(offset==null) offset = new ArrayList<Long>();
		if(offsets!=null) offset.addAll(offsets);
		map.put(word, offset);
	}
	
	public ArrayList<Long> getOffsets(String word){
		return map.get(word);
	}
	
	public HashMap<String, ArrayList<Long>> getMap(){
		return map;
	}
}
