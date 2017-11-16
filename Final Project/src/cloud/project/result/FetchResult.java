package cloud.project.result;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import cloud.project.result.MysqlService;
import cloud.project.result.AwsManager;
import cloud.project.result.Header;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;

public class FetchResult {
	static double[] interval = {600, 1080, 1464, 1771.2, 2017, 2213.6, 2370.9, 2496.7, 2597.3};
	public static void main(String[] args) throws IOException, SQLException{
		//Connection con = MysqlService.connect();
		//Statement statement = con.createStatement();
		for(int i=0;i<2;i++){
			String s3FileName = "part-0000"+i;
			S3ObjectInputStream in = getS3InputStream(s3FileName);
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String line = reader.readLine();
	        while (line!=null) {            
	            String[] split = line.split("\t");
	            String fileName = split[0];
	            double score = Double.valueOf(split[1]);
	            int hardNum = Integer.valueOf(split[2]);
	            int easyNum = Integer.valueOf(split[3]);
	            int level = getNormalized(score);
	            int id = Integer.valueOf(fileName.substring(0, fileName.length()-4));
	            System.out.println(fileName+"\t"+id+"\t"+score+"\t"+level);
	            String sql = "UPDATE articles SET hardCnt = "+hardNum+", easyCnt="+easyNum+", level = "+level+", status = 1 WHERE id = "+id;
	            System.out.println(sql);
	            //statement.addBatch(sql);
	            //delS3File("data/articles/", fileName);
	            line = reader.readLine();
	        }
	        in.close();
		}
		
		/*statement.executeBatch();
		System.out.println("execute batch");
		delS3Folder("tmp");
		delS3Folder("score");
		statement.close();
		con.close();*/			
	}
	private static void delS3File(String dir, String fileName){
		AmazonS3 s3client = AwsManager.getS3Client();
		s3client.deleteObject(Header.S3_BUCKET_NAME, dir+fileName);
	}
	
	public static void delS3Folder(String folderName) {
		AmazonS3 s3client = AwsManager.getS3Client();
		List<S3ObjectSummary> fileList = s3client.listObjects(Header.S3_BUCKET_NAME, folderName).getObjectSummaries();
		for (S3ObjectSummary file : fileList) {
			s3client.deleteObject(Header.S3_BUCKET_NAME, file.getKey());
		}
		s3client.deleteObject(Header.S3_BUCKET_NAME, folderName);
	}
	
	private static S3ObjectInputStream getS3InputStream(String fileName) throws IOException{
		AmazonS3 s3client = AwsManager.getS3Client();
		S3Object obj = s3client.getObject(Header.S3_BUCKET_NAME,"score/"+fileName);
		S3ObjectInputStream in =  obj.getObjectContent();
		return in;
		        
	}
	
	private static int getNormalized(double score){		
		int level = 10;
		for(int i=0; i<interval.length ;i++){
			if(Double.compare(score, interval[i])<=0){
				level = i+1;
				break;
			}
		}		
		return level;
	}

}
