package parse;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;

// offsets of a word

public class Word implements Writable{
	private String word;
	private long offset;

	public Word(){
		word = "";
		offset = 0;
	}
	
	public Word(String word, long offset){
		this.word = word;
		this.offset = offset;
	}
	
	public String getWord(){
		return word;
	}
	
	public long getOffset(){
		return offset;
	}
	
	@Override
	public void readFields(DataInput in) throws IOException {
		word = Text.readString(in);
		offset = in.readLong();
	}

	@Override
	public void write(DataOutput out) throws IOException {
		Text.writeString(out, word);
		out.writeLong(offset);
	}
	
	@Override
	public String toString(){
		StringBuilder builder = new StringBuilder();
		builder.append(word+"\n"+offset);
		return builder.toString();
	}
}
