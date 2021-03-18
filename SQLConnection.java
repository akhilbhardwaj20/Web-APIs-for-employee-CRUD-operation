package com.todo;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

//DB Connection
public class SQLConnection {

	public Connection connect() {
		String url = "jdbc:mysql://localhost:3306/";
		String dbName = "restdb";
		String driver = "com.mysql.jdbc.Driver";
		String userName = "root";
		String password = "";
		Connection conn = null;
		try {
			Class.forName(driver).newInstance();
			conn = DriverManager
					.getConnection(url + dbName, userName, password);
			return conn;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}	
}
