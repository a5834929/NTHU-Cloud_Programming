package step4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;

public class LoadToHBase {
	private static Configuration conf = null;

	static {
		conf = HBaseConfiguration.create();
	}

	public static void createTable(String tableName, String[] colFamilies) throws Exception {
		HBaseAdmin admin = new HBaseAdmin(conf);
		if (admin.tableExists(tableName)) {
			System.out.println("Table already exists!");
		} else {
			HTableDescriptor tableDescriptor = new HTableDescriptor(tableName);
			for(String col:colFamilies)
				tableDescriptor.addFamily(new HColumnDescriptor(col));
			
			admin.createTable(tableDescriptor);
			System.out.println("Create table " + tableName);
		}
		admin.close();
	}

	public static void removeTable(String tableName) throws Exception {
		HBaseAdmin admin = new HBaseAdmin(conf);
		admin.disableTable(tableName);
		admin.deleteTable(tableName);
		System.out.println("Remove table " + tableName);
		admin.close();
	}

	public static void addRecord(String tableName, String rowKey, String colFamily,
			String qualifier, String value) throws Exception {
		HTable table = new HTable(conf, tableName);

		Put p = new Put(Bytes.toBytes(rowKey));
		p.add(Bytes.toBytes(colFamily), Bytes.toBytes(qualifier), Bytes.toBytes(value));
		table.put(p);
		System.out.println("Insert record " + rowKey + " to table " + tableName + " ok.");
	}

	public static void deleteRecord(String tableName, String rowKey) throws Exception {
		HTable table = new HTable(conf, tableName);
		Delete d = new Delete();
		table.delete(d);
		System.out.println("Delete record " + rowKey + " from table " + tableName + " ok.");
		table.close();
	}

	public static void getRecord(String tableName, String rowKey) throws Exception {
		HTable table = new HTable(conf, tableName);
		
		Get get = new Get(rowKey.getBytes());
        Result rs = table.get(get);
        for (KeyValue kv : rs.raw()) {
			System.out.print(new String(kv.getRow()) + " ");	
			System.out.print(new String(kv.getFamily()) + ":");
			System.out.print(new String(kv.getQualifier()) + " "); 
			System.out.print(kv.getTimestamp() + " ");
			System.out.println(new String(kv.getValue()));
		}
        table.close();
	}
	
	public static void getAllRecord(String tableName) throws Exception {
		HTable table = new HTable(conf, tableName);
		Scan s = new Scan();
		ResultScanner rs = table.getScanner(s);
		for (Result r: rs) {
			for (KeyValue kv : r.raw()) {
				System.out.print(new String(kv.getRow()) + " ");
				System.out.print(new String(kv.getFamily()) + ":");
				System.out.print(new String(kv.getQualifier()) + " ");
				System.out.print(kv.getTimestamp() + " ");
				System.out.println(new String(kv.getValue()));
			}
		}
		table.close();
	}

	public static void main(String[] args) {
		try {
			if(args[1].equals("pagerank")){
	            removeTable("100062236_pagerank");
	        	createTable("100062236_pagerank", new String[] {"pagerank"});
	            FileSystem fs = FileSystem.get(new Configuration());
	            FileStatus[] status = fs.listStatus(new Path(args[0]));
	            Pattern pattern = Pattern.compile("part.*");
	            for(int i=0;i<status.length;i++){
	            	Path path = status[i].getPath();
	            	Matcher matcher = pattern.matcher(path.toString());
	            	if(matcher.find()){
		            	BufferedReader br=new BufferedReader(new InputStreamReader(fs.open(path)));
		                String line = br.readLine();
		                while(line!=null){
			                String[] tokens = line.split("\\t");
			                addRecord("100062236_pagerank", tokens[0], "pagerank", "rank", tokens[1]);
			                line = br.readLine();
			            }
	            	}
	            }
			}else{
	            removeTable("100062236_inverted");
	        	createTable("100062236_inverted", new String[] {"invertedIndex"});
	            FileSystem fs = FileSystem.get(new Configuration());
	            FileStatus[] status = fs.listStatus(new Path(args[0]));
	            Pattern pattern = Pattern.compile("part.*");
	            for(int i=0;i<status.length;i++){
	            	Path path = status[i].getPath();
	            	Matcher matcher = pattern.matcher(path.toString());
	            	if(matcher.find()){
		            	BufferedReader br=new BufferedReader(new InputStreamReader(fs.open(path)));
		                String line = br.readLine();
		                while(line!=null){
			                String[] tokens = line.split("<>");
			                String word = tokens[0].split("\\t")[0];
			                StringBuilder builder = new StringBuilder();
			                int titleInd = 1;
			                for(int j=0;j<tokens.length;j++){
			                	String title = tokens[j].split("\\t")[titleInd];
			                	builder.append(title+"##");
			                	titleInd = 0;
			                }
			                addRecord("100062236_inverted", word, "invertedIndex", "title", builder.toString());
			                line = br.readLine();
			            }
	            	}
	            }
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
