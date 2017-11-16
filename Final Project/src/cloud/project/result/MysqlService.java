package cloud.project.result;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class MysqlService {
	
	
	public static Connection connect(){
		Connection con=null;
		try {
			Class.forName("com.mysql.jdbc.Driver");			
			con = DriverManager.getConnection("jdbc:mysql://"+Header.DB_DNS+"/"+Header.DB_NAME+"?useUnicode=true&characterEncoding=utf-8",
					Header.DB_ACCOUNT, Header.DB_PSW);			
		} catch (SQLException e) {
			System.out.println("Connect DB Exception :" + e.toString());
		} catch (ClassNotFoundException e){
			e.toString();
		}
		return con;
	}
	
	private static Statement createStatement(Connection con){
		Statement stat=null;
		try {
			stat=con.createStatement();
		} catch (SQLException e) {
			System.out.println("Create stat DB Exception :" + e.toString());
		}
		return stat;
	}
	
	private static void disconnect(Connection con){
		try {
			con.close();
		} catch (SQLException e) {
			System.out.println("Disconnect DB Exception :" + e.toString());
		}
	}
	
	public static int update(String sql){
		int result=-1;
		Connection con=connect();
		Statement stat=MysqlService.createStatement(con);
		try {
			result=stat.executeUpdate(sql);	
		} catch (SQLException e) {
			System.out.println("UpdateDB Exception :" + e.toString());
		} finally{
			disconnect(con);
		}
		return result;
	}

}
