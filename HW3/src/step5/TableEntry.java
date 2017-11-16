package step5;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;

public class TableEntry implements Writable{
	private ArrayList<String> titles;
	
	public TableEntry(){
		titles = new ArrayList<String>();
	}
	
	public TableEntry(ArrayList<String> titles){
		this.titles = titles;
	}
	
	public ArrayList<String> getTitles(){
		return titles;
	}
	
	public void addTitle(String title){
		titles.add(title);
	}
	
	@Override
	public void readFields(DataInput in) throws IOException {
		titles.clear();
		int size = in.readInt();
		for(int i=0;i<size;i++){
			String title = Text.readString(in);
			titles.add(title);
		}
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeInt(titles.size());
		for(String title:titles)
			Text.writeString(out, title);
	}
	
	@Override
	public String toString(){
		StringBuilder builder = new StringBuilder();
		for(String title:titles){
			builder.append(title.toString());
			builder.append("##");
		}
		return builder.toString();
	}
}
