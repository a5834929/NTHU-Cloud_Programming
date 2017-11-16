package part2;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;

// offsets of a word

public class WordEntry implements Writable{
	private String fileName;
	private ArrayList<Long> offsets;
	private double termFrequency;

	public WordEntry(){
		fileName = "";
		offsets = new ArrayList<Long>();
		termFrequency = 0.0;
	}
	
	public WordEntry(String fileName, ArrayList<Long> offsets, double termFrequency){
		this.fileName = fileName;
		this.offsets = offsets;
		this.termFrequency = termFrequency;
	}
	
	public String getFileName(){
		return fileName;
	}
	
	public ArrayList<Long> getOffsets(){
		return offsets;
	}

	public void sortOffsets(){
		Collections.sort(offsets);
	}
	
	public void addOffset(long offset){
		offsets.add(offset);
	}
	
	public void calculateTF(long count){
		termFrequency = offsets.size()/(double)count;
	}
	
	public double getTF(){
		return termFrequency;
	}
	
	@Override
	public void readFields(DataInput in) throws IOException {
		offsets.clear();
		termFrequency = in.readDouble();
		fileName = Text.readString(in);
		int len = in.readInt();
		for(int i=0;i<len;i++)
			offsets.add(in.readLong());
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeDouble(termFrequency);
		Text.writeString(out, fileName);
		out.writeInt(offsets.size());
		for(int i=0;i<offsets.size();i++)
			out.writeLong(offsets.get(i));
	}
	
	@Override
	public String toString(){
		StringBuilder builder = new StringBuilder();
		if(!fileName.isEmpty()) builder.append(fileName+"\t");
		builder.append(termFrequency+"\t[");
		for(int i=0;i<offsets.size();i++){
			builder.append(offsets.get(i).toString());
			if(i<offsets.size()-1) builder.append(",");
			else builder.append("]");
		}
		return builder.toString();
	}
}
