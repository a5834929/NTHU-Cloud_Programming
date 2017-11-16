package part1;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;

public class TableEntry implements Writable{
	private ArrayList<WordEntry> entries;
	
	public TableEntry(){
		entries = new ArrayList<WordEntry>();
	}
	
	public TableEntry(ArrayList<WordEntry> entries){
		this.entries = entries;
	}
	
	public ArrayList<WordEntry> getWordEntry(){
		return entries;
	}
	
	public void addWordEntry(WordEntry entry){
		entries.add(entry);
	}
	
	@Override
	public void readFields(DataInput in) throws IOException {
		entries.clear();
		int size = in.readInt();
		for(int i=0;i<size;i++){
			ArrayList<Long> offsets = new ArrayList<Long>();
			String fileName = Text.readString(in);
			double tf = in.readDouble();
			int len = in.readInt();
			for(int j=0;j<len;j++)
				offsets.add(in.readLong());
			WordEntry entry = new WordEntry(fileName, offsets, tf);
			entries.add(entry);
		}
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeInt(entries.size());
		for(int i=0;i<entries.size();i++){
			WordEntry entry = entries.get(i);
			ArrayList<Long> offsets = entry.getOffsets();
			Text.writeString(out, entry.getFileName());
			out.writeDouble(entry.getTF());
			out.writeInt(offsets.size());
			for(int j=0;j<offsets.size();j++)
				out.writeLong(offsets.get(j));
		}
	}
	
	@Override
	public String toString(){
		StringBuilder builder = new StringBuilder();
		builder.append(entries.size()+"\t");
		for(int i=0;i<entries.size();i++){
			WordEntry entry = entries.get(i);
			builder.append(entry.toString());
			if(i<entries.size()-1) builder.append(';');
		}
		return builder.toString();
	}
}
