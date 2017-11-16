package part1;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;

// pair of filename & word

public class WordFilePair implements WritableComparable{
	private String fileName;
	private String word;

	public WordFilePair(){
		fileName = "";
		word = "";
	}

	public WordFilePair(String fileName, String word) {
		this.fileName = fileName;
		this.word = word;
	}
	
	public String getFileName(){
		return fileName;
	}
	
	public String getWord(){
		return word;
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		fileName = Text.readString(in);
		word = Text.readString(in);		
	}

	@Override
	public void write(DataOutput out) throws IOException {
		Text.writeString(out, fileName);
		Text.writeString(out, word);
	}
	
	@Override
	public String toString(){
		return word + "\t" + fileName;
	}

	@Override
	public int compareTo(Object obj) {
		WordFilePair that = (WordFilePair)obj;
		if(this.getWord().equals(that.getWord()))
			return this.getFileName().compareTo(that.getFileName());
		return this.getWord().compareTo(that.getWord());
	}
}
