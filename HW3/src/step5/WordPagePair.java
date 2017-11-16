package step5;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;

public class WordPagePair implements WritableComparable{
	private String word;
	private String page;
	
	public WordPagePair(){
		word = "";
		page = "";
	}
	
	public WordPagePair(String word, String page){
		this.word = word;
		this.page = page;
	}
	
	public String getWord(){
		return word;
	}
	
	public String getPage(){
		return page;
	}
	
	@Override
	public void readFields(DataInput in) throws IOException {
		word = Text.readString(in);
		page = Text.readString(in);
	}

	@Override
	public void write(DataOutput out) throws IOException {
		Text.writeString(out, word);
		Text.writeString(out, page);
	}
	
	@Override
	public String toString(){
		return word+"\t"+page;
	}

	@Override
	public int compareTo(Object obj) {
		WordPagePair that = (WordPagePair)obj;
		if(this.getWord().equals(that.getWord()))
			return this.getPage().compareTo(that.getPage());
		return this.getWord().compareTo(that.getWord());
	}
}
