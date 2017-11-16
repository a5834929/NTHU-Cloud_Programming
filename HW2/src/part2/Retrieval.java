package part2;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class Retrieval {
	private static HashMap<String, ArrayList<WordEntry>> table = new HashMap<String, ArrayList<WordEntry>>();
	private static HashSet<String> fileSet = new HashSet<String>();
	
	public static void main(String[] args) throws Exception {
        try{
        	System.out.println("Building Table......");
            FileSystem fs = FileSystem.get(new Configuration());
            FileStatus[] status = fs.listStatus(new Path(args[0]));
            Pattern pattern = Pattern.compile("part.*");
            for (int i=0;i<status.length;i++){
                Path path = status[i].getPath();
                Matcher matcher = pattern.matcher(path.toString());
                if(matcher.find()){
                    BufferedReader br=new BufferedReader(new InputStreamReader(fs.open(path)));
                    String line = br.readLine();
                	
                	while (line != null){
                		ArrayList<WordEntry> entries = new ArrayList<WordEntry>();
                		String[] fragment = line.split(";");
                		String[] segment = fragment[0].split("\t");
                		String key = segment[0];
                		String fileName;
                		double tf;
                		int first = 2;
                		for(int j=0;j<fragment.length;j++){
                			segment = fragment[j].split("\t");
                			fileName = segment[first];
                			fileSet.add(fileName);
                			ArrayList<Long> offsets = new ArrayList<Long>();
                			Pattern numPat = Pattern.compile("[0-9]+");
                			Matcher numMat = numPat.matcher(segment[first+2]);
                			while(numMat.find())
                				offsets.add(Long.parseLong(numMat.group()));
                			tf = Double.parseDouble(segment[first+1]);
                			
                			entries.add(new WordEntry(fileName, offsets, tf));
                			first = 0;
                		}
                		table.put(key, entries);
                		line = br.readLine();
                	}
            	}	
            }
	    }catch(Exception e){
	    	System.out.println(e);
	    }
        System.out.println("Table Built.");
        System.out.println("Enter query (\"q\" to exit):");
        
        int fileNum = fileSet.size();
        Scanner scanner = new Scanner(System.in);
        while(scanner.hasNext()){
        	String query = scanner.nextLine();
        	if(query.equals("q"))	break;        	
        	String[] tokenOR = query.split(" ");
        	HashMap<String, FileRankEntry> orEntries = new HashMap<String, FileRankEntry>();
        	
        	for(int i=0;i<tokenOR.length;i++){
        		String[] tokenAND = tokenOR[i].split("\\+");
        		HashMap<String, FileRankEntry> andEntries = new HashMap<String, FileRankEntry>();
        		for(int j=0;j<tokenAND.length;j++){
        			if(table.containsKey(tokenAND[j]) || tokenAND[j].charAt(0)=='~'){
        				
        				HashMap<String, FileRankEntry> tmpEntries = new HashMap<String, FileRankEntry>();
        				if(tokenAND[j].charAt(0)!='~'){
        					ArrayList<WordEntry> entries = table.get(tokenAND[j]);
            				for(WordEntry entry:entries){
            					FileRankEntry fr = new FileRankEntry(entry.getFileName());
            					double weight = calWeight(fileNum, entries.size(), entry.getTF());
            					fr.addWeight(weight);
            					fr.addOffsets(tokenAND[j], entry.getOffsets());
            					tmpEntries.put(entry.getFileName(), fr);
            				}
        				}else	tmpEntries = negation(tokenAND[j].substring(1));

        				if(j==0)	andEntries = tmpEntries;
        				else		andEntries = intersection(andEntries, tmpEntries, tokenAND[j]);
        			}else andEntries.clear();
        		}
        		orEntries = union(orEntries, andEntries);
        	}
        	
        	//sort file ranks according to weight and count
        	ArrayList<FileRankEntry> result = new ArrayList<FileRankEntry>();
        	for(Map.Entry<String, FileRankEntry> entry:orEntries.entrySet())
        		result.add(entry.getValue());
        	Collections.sort(result, new Comparator<FileRankEntry>(){
    	        @Override
    	        public int compare(FileRankEntry entry1, FileRankEntry entry2){
    	        	Double w1 = entry1.getWeight();
    	        	Double w2 = entry2.getWeight();
    	            if(w1.equals(w2))	
    	            	return entry1.getFileName().compareTo(entry2.getFileName());
    	            return (w1<w2)?1:-1;
    	        }
    	    });
        	
        	//output the highest 10 files
        	outputResult(FileSystem.get(new Configuration()), result);
        }
	}
	
	public static HashMap<String, FileRankEntry> intersection
		(HashMap<String, FileRankEntry> entries1, HashMap<String, FileRankEntry> entries2, String word){
		
		HashMap<String, FileRankEntry> newEntries = new HashMap<String, FileRankEntry>();
		for(Map.Entry<String, FileRankEntry> e1:entries1.entrySet()){
			for(Map.Entry<String, FileRankEntry> e2:entries2.entrySet()){
				if(e1.getKey().equals(e2.getKey())){
					e1.getValue().addWeight(e2.getValue().getWeight());
					ArrayList<Long> offset = e2.getValue().getOffsets(word);
					e1.getValue().addOffsets(word, offset);
					newEntries.put(e1.getKey(), e1.getValue());
				}
			}
		}
		return newEntries;
	}
	
	public static HashMap<String, FileRankEntry> union
	(HashMap<String, FileRankEntry> entries1, HashMap<String, FileRankEntry> entries2){
		
		HashMap<String, FileRankEntry> newEntries = new HashMap<String, FileRankEntry>();
		for(Map.Entry<String, FileRankEntry> e1:entries1.entrySet()){
			if(entries2.containsKey(e1.getKey())){
				if(e1.getValue().getWeight()>=entries2.get(e1.getKey()).getWeight())
					newEntries.put(e1.getKey(), e1.getValue());
				else
					newEntries.put(e1.getKey(), entries2.get(e1.getKey()));
			}else newEntries.put(e1.getKey(), e1.getValue());
		}
		for(Map.Entry<String, FileRankEntry> e2:entries2.entrySet())
			if(!entries1.containsKey(e2.getKey()))
				newEntries.put(e2.getKey(), entries2.get(e2.getKey()));
		return newEntries;
	}
	
	public static HashMap<String, FileRankEntry> negation(String word){
		
		HashSet<String> set = new HashSet<String>(fileSet);
		HashMap<String, FileRankEntry> map = new HashMap<String, FileRankEntry>();
		if(table.containsKey(word)){
			ArrayList<WordEntry> entries = table.get(word);
			for(WordEntry entry:entries)
				set.remove(entry.getFileName());
		}
		for(String fileName:set){
			FileRankEntry entry = new FileRankEntry(fileName);
			map.put(fileName, entry);
		}
		return map; 
	}
	
	public static double calWeight(int N, int df, double tf){
		return tf*Math.log((double)N/df);
	}
	
	public static void outputResult(FileSystem fs, ArrayList<FileRankEntry> result){
		int rank = 1;
		int len = Math.min(result.size(), 10);
    	System.out.println(result.size()+" files retrieved");
    	System.out.println("****************************************************************");
    	for(int i=1;i<=len;i++){
    		System.out.println("Rank "+rank+"\t"+result.get(i-1).getFileName()+"\tscore = "+result.get(i-1).getWeight());
    		System.out.println("----------------------------------------------------------------");
    		rank++;
    		
    		Path path = new Path("HW2/input/"+result.get(i-1).getFileName());
    		HashMap<String, ArrayList<Long>> offsetMap = result.get(i-1).getMap();
    		for(Map.Entry<String, ArrayList<Long>> entry:offsetMap.entrySet()){
    			if(entry.getKey().charAt(0)!='~'){
	    			System.out.println("\t"+entry.getKey()+" : ");
	    			ArrayList<Long> offsets = entry.getValue();
	    			int flen = Math.min(offsets.size(), 5);
	    			char[] str = new char[25];
	    			for(int j=1;j<=flen;j++){
	    				Long offset = offsets.get(j-1);
	    	    		try{
	    	    			BufferedReader br = new BufferedReader(new InputStreamReader(fs.open(path)));
	    	    			br.skip(Math.max(0, offset-5));
	    	    			br.read(str);
	    	    			System.out.println("\t\tfragment "+j+": "+String.valueOf(str).replace('\n', ' '));
	    	    		}catch(Exception e){
	    	    	    	System.out.println(e);
	    	    		}
	    			}
    			}
    		}
    		System.out.println("****************************************************************");
    	}
        System.out.println("");
	}
	
}
