package query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Scanner;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;

public class Query {
	private static Configuration conf = null;

	static {
		conf = HBaseConfiguration.create();
	}
	
	public static String getRecord(String tableName, String rowKey) throws Exception {
		HTable table = new HTable(conf, tableName);
		
		Get get = new Get(rowKey.getBytes());
        Result rs = table.get(get);
        for (KeyValue kv : rs.raw()) {
			return new String(kv.getValue());
		}
        return "";
	}
	
	public static void main(String[] args){
		try {
			Query query = new Query();
			query.Query();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void Query(){
		System.out.println("Enter query:");
		Scanner scanner = new Scanner(System.in);
        while(scanner.hasNext()){
			try {
				HashSet<String> pageSet = new HashSet<String>();
				String query = scanner.nextLine();
				String[] tmp = getRecord("100062236_inverted", query).split("##");
	        	for(String s:tmp)
	        		if(!s.equals(""))
	        			pageSet.add(s);
	        	tmp = getRecord("100062236_inverted", query.toLowerCase()).split("##");
	        	for(String s:tmp)
	        		if(!s.equals(""))
	        			pageSet.add(s);
	        	tmp = getRecord("100062236_inverted", query.toUpperCase()).split("##");
	        	for(String s:tmp)
	        		if(!s.equals(""))
	        			pageSet.add(s);	        	
	        	char ch = Character.toUpperCase(query.charAt(0)); 
	        	query = ch+query.substring(1).toLowerCase();
	        	tmp = getRecord("100062236_inverted", query).split("##");
		        for(String s:tmp)
		        	if(!s.equals(""))
		        		pageSet.add(s);
	        	
				if(pageSet.size()!=0){
					ArrayList<Page> pageList = new ArrayList<Page>();
		        	for(String page:pageSet){
			        	String rank = getRecord("100062236_pagerank", page);
			        	if(!rank.equals(""))
			        		pageList.add(new Page(page, Double.valueOf(rank)));
		        	}
		        	Collections.sort(pageList, new Comparator<Page>(){
		    	        @Override 
		    	        public int compare(Page entry1, Page entry2){
		    	        	Double w1 = entry1.getRank();
		    	        	Double w2 = entry2.getRank();
		    	            if(w1.equals(w2))	
		    	            	return entry1.getPageName().compareTo(entry2.getPageName());
		    	            return (w1<w2)?1:-1;
		    	        }
		    	    });
		        	
		        	if(pageList.size()!=0){
		        		int len = Math.min(10, pageList.size());
		        		for(int i=0;i<len;i++)
		        			System.out.println(pageList.get(i).getPageName()+"\t--->\t"+pageList.get(i).getRank());
		        		System.out.println("");
		        	}else
		        		System.out.println("No results found.\n");
		        	pageList.clear();
				}else
					System.out.println("No results found.\n");
				System.out.println("Enter query:");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
        }
	}
}
