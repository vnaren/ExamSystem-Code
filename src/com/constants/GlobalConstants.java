
package com.constants;

import java.sql.Connection;
import java.sql.DriverManager;

public class GlobalConstants {
	
	private static String driver = "com.mysql.jdbc.Driver";
	private static String dbUrl = "jdbc:mysql://localhost:3306/onlinetest";
	private static String dbUser = "root";
	private static String dbPass = "";
	
	private static Connection con = null;
	
	private static int rollID = 0;
	private static int userID = 0;
	
	public static void clearConstants(){
		driver = null;
		dbUrl = null;
		dbUser = null;
		dbPass = null;
		con = null;
		
		rollID = 0;
		userID = 0;
	}
	
	public static String getDriver(){
		return driver;
	}
	
	public static String getDbUrl(){
		return dbUrl;
	}
	
	public static String getDbUser(){
		return dbUser;
	}
	
	public static String getDbPass(){
		return dbPass;
	}
	
	public static Connection getConnection(){
		try{
			
				Class.forName(driver);
				con = DriverManager.getConnection(dbUrl,dbUser,dbPass);
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return con;
	}
	
	public static void closeConnection(){
		try{
			con.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void setRollID(int ID){
		rollID = ID;
	}
	
	public static int getRollID(){
		return rollID;
	}
	
	public static void setUserID(int ID){
		userID = ID;
	}
	
	public static int getUserID(){
		return userID;
	}
}
