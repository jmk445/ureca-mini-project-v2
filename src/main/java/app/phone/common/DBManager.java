package app.phone.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBManager {
	static String url = "jdbc:mysql://localhost:3306/miniproject1";
	static String user = "root";
	static String pwd = "root";
	
	
	public static Connection getConnection() {
		Connection con = null;
		try {
			con = DriverManager.getConnection(url,user,pwd);			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
		return con;
	}
	
	
	public static void releaseConnection(PreparedStatement pstmt, Connection con) {
		try {
			pstmt.close();
			con.close();
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void releaseConnection(ResultSet rs, PreparedStatement pstmt, Connection con) {
		try {
			rs.close();
			pstmt.close();
			con.close();
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
}
