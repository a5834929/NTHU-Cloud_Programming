package calculate.score;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import org.apache.hadoop.io.Writable;

public class FileData implements Writable{
	private double score = 0.0;
	private int hard = 0;
	private int easy = 0;
	
	public FileData(){
		score = 0.0;
		hard = 0;
		easy = 0;
	}
	
	public FileData(double score, int hard, int easy){
		this.score = score;
		this.hard = hard;
		this.easy = easy;
	}
	
	public void setScore(double score){
		this.score = score;
	}
	
	public double getScore(){
		return score;
	}
	
	public void setHard(int hard){
		this.hard = hard;
	}
	
	public int getHard(){
		return hard;
	}
	
	public void setEasy(int easy){
		this.easy = easy;
	}
	
	public int getEasy(){
		return easy;
	}
	
	@Override
	public void readFields(DataInput in) throws IOException {
		score = in.readDouble();
		hard = in.readInt();
		easy = in.readInt();
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeDouble(score);
		out.writeInt(hard);
		out.writeInt(easy);
	}
	
	@Override
	public String toString(){
		return score+"\t"+hard+"\t"+easy;
	}
}
