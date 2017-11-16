package step1;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;

public class Links implements Writable{
	private double rank;
	private ArrayList<String> links;
	
	public Links(){
		rank = 0.0;
		links = new ArrayList<String>();
	}
	
	public Links(double rank, ArrayList<String> links){
		this.rank = rank;
		this.links = links;
	}
	
	public void updateRank(double rank){
		this.rank += rank;
	}
	
	public double getRank(){
		return rank;
	}
	
	public void addLink(String link){
		links.add(link);
	}
	
	public ArrayList<String> getLinks(){
		return links;
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		rank = in.readDouble();
		int size = in.readInt(); 
		links.clear();
		for(int i=0;i<size;i++)
			addLink(Text.readString(in));
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeDouble(rank);
		out.writeInt(links.size());
		for(String link:links)
			Text.writeString(out, link);
	}
	
	@Override
	public String toString(){
		StringBuilder builder = new StringBuilder();
		builder.append(rank+"]");
		for(int i=0;i<links.size();i++)
			builder.append(links.get(i)+"]");
		return builder.toString();
	}
}
