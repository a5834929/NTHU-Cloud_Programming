package calculate.score;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;

public class FileWordPair implements WritableComparable{
	private String file;
	private String word;
	
	public FileWordPair(){
		file = "";
		word = "";
	}
	
	public FileWordPair(String file, String word){
		this.file = file;
		this.word = word;
	}
	
	public String getFile(){
		return file;
	}
	
	public String getWord(){
		return word;
	}
	
	@Override
	public void readFields(DataInput in) throws IOException {
		file = Text.readString(in);
		word = Text.readString(in);
	}

	@Override
	public void write(DataOutput out) throws IOException {
		Text.writeString(out, file);
		Text.writeString(out, word);
	}
	
	@Override
	public String toString(){
		return file+"\t"+word;
	}

	@Override
	public int compareTo(Object obj) {
		FileWordPair that = (FileWordPair)obj;
		if(this.getWord().equals(that.getWord()))
			return this.getFile().compareTo(that.getFile());
		return this.getWord().compareTo(that.getWord());
	}
}
