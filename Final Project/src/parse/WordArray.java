package parse;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;

// offsets of a word

public class WordArray implements Writable{
	private ArrayList<Word> article;

	public WordArray(){
		article = new ArrayList<Word>();
	}
	
	public WordArray(ArrayList<Word> article){
		this.article = article;
	}
	
	public ArrayList<Word> getArticle(){
		return article;
	}
	
	public void addWord(Word word){
		article.add(word);
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		article.clear();
		int size = in.readInt();
		for(int i=0;i<size;i++){
			String word = Text.readString(in);
			long offset = in.readLong();
			Word aWord = new Word(word, offset);
			article.add(aWord);
		}
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeInt(article.size());
		for(Word word:article){
			Text.writeString(out, word.getWord());
			out.writeLong(word.getOffset());
		}
	}
	
	@Override
	public String toString(){
		StringBuilder builder = new StringBuilder();
		for(Word word:article)
			builder.append(word.getWord());
		return builder.toString();
	}
}
