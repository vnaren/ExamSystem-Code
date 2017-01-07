
package com.constants;

import java.sql.*;
import javax.swing.JFrame;

import com.exam.admin.adminManager;
import com.exam.client.clientManager;

public class Authentication {
	
	static boolean Authenticate(String userName,String password){
		boolean status = false;
		try{
			Connection con = GlobalConstants.getConnection();
			Statement stmt = con.createStatement();
			ResultSet rs = null;
			rs = stmt.executeQuery(Queries.getLoginSql(userName, password));
			rs.next();
			if(rs.getString(1).equals("1")){
				GlobalConstants.setRollID( Integer.parseInt(rs.getString(2)) );
				GlobalConstants.setUserID( Integer.parseInt(rs.getString(3)) );
				status = true;
			}
			stmt.close();
			con.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return status;
	}
	
	public static boolean logOut(){
		return false;
	}
	
	static void sendRedirect(JFrame frame){
		switch(GlobalConstants.getRollID()){
			case 1:
				clientManager.checkTest(frame);
				break;
			case 2:
				adminManager.manager(frame);
				//addNewTest.manager(frame);
				break;
		}
	}
}
